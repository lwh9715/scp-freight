package com.scp.view.module.bus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;

@ManagedBean(name = "pages.module.bus.bustruckBean", scope = ManagedBeanScope.REQUEST)
public class BusTruckBean extends MastDtlView {

	@SaveState
	@Accessible
	public BusTruck selectedRowData = new BusTruck();

	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();

	/**
	 * 司机下拉
	 */
	@Bind
	@SelectItems
	@SaveState
	private List<SelectItem> drivers;

	/**
	 * 拖车单号下拉
	 */
	@Bind
	@SelectItems
	@SaveState
	private List<SelectItem> trucknoses;

	/**
	 * 工作单id
	 */
	@SaveState
	@Accessible
	public Long jobid;

	/**
	 * 拖车关联的所有货柜id 拼接：xxx,xxx,xxx
	 */
	@Bind
	@SaveState
	public String containerids;

	/**
	 * 状态描述（I：初始；F：完成）
	 */
	@Bind
	@SaveState
	public String truckstatedesc;

	@Bind
	private UIButton saveMaster;

	@Bind
	private UIButton delMaster;

	@Bind
	private UIButton chooseship;

	@Bind
	@SaveState
	@Accessible
	public UIWindow showDtlWindow;

	@Bind
	@SaveState
	@Accessible
	public String dtlContent;
	
	@Bind
	@SaveState
	public String jobnos;
	
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	public String src;
	
	@SaveState
	@Accessible
	public FinaJobs finaJobs;


	@Override
	public void beforeRender(boolean isPostBack) {
		// TODO Auto-generated method stub
		if (!isPostBack) {
			init();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			this.userid = AppUtils.getUserSession().getUserid();
		}
		super.applyGridUserDef();
	}

	@Override
	public void init() {
		selectedRowData = new BusTruck();
		selectedRowData.setCurrencyap("CNY");
		selectedRowData.setCurrencyar("CNY");
		String jobid = AppUtils.getReqParam("id");
		jobid = jobid.replaceAll("#", "");
		src = AppUtils.getReqParam("id");
		this.jobid = Long.valueOf(jobid);
		this.initCombox();
		String message = this.ShowMessage();
		if (!StrUtils.isNull(message)) {
			showDtlWindow.show();
			this.dtlContent = message;
			this.update.markUpdate(UpdateLevel.Data, "dtlContent");
		}
		if (this.jobid != null) {
			finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			this.selectedRowData.setJobid(this.jobid);
			this.containerids = this.serviceContext.busTruckMgrService
					.getLinkContainersid(this.mPkVal);
			update.markUpdate(true, UpdateLevel.Data, "containerids");
		}
	}

	// 设置拖车号下拉列表值
	public void initCombox() {
		if (this.jobid != null) {
			List<BusTruck> bustruckList = this.serviceContext.busTruckMgrService
					.getBusTruckListByJobid(this.jobid);
			if (bustruckList != null && bustruckList.size() > 0) {
				List<SelectItem> items = new ArrayList<SelectItem>();
				for (BusTruck bt : bustruckList) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String data = sdf.format(bt.getInputtime()==null?Calendar.getInstance().getTime():bt.getInputtime());
					items.add(new SelectItem(bt.getNos(), bt.getNos()));
				}
				items.add(new SelectItem(null, ""));
				this.trucknoses = items;
				if (this.mPkVal == -1L) {
					this.mPkVal = bustruckList.get(0).getId();
				}
				this.refreshMasterForm();
			} else {
				this.addMaster();
			}
		}
	}
	
	@Inject(value = "l")
	protected MultiLanguageBean l;

	@Override
	public void addMaster() {
		this.selectedRowData = new BusTruck();
		this.selectedRowData.setId(0L);
		this.selectedRowData.setJobid(this.jobid);
		this.selectedRowData.setTruckstate("I");
		this.truckstatedesc = (String) l.m.get("初始");
		this.selectedRowData.setReportname("");
		this.selectedRowData.setSingtime(new Date());
		this.mPkVal = -1l;
		this.changeDriver();
		Browser.execClientScript("clearFactory()");
		this.refreshMasterForm();
		refresh();

	}

	@Override
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			try {
				serviceContext.busTruckMgrService.removeDate(selectedRowData
						.getId());
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			this.addMaster();
			this.initCombox();
			this.containerids = this.serviceContext.busTruckMgrService
					.getLinkContainersid(this.mPkVal);
			update.markUpdate(true, UpdateLevel.Data, "containerids");
			Browser.execClientScript("showmsg()");
		}
	}

	/**
	 * 保存
	 */
	@Override
	public void doServiceSaveMaster() {
		this.saveation();
		Browser.execClientScript("showmsg()");
	}
	
	public void saveation(){
		// 地域标记,默认设为C,中国
		this.selectedRowData.setAreatype("C");
		// 拿到页面上的报关公司id
		Long truckid = this.selectedRowData.getTruckid();
		// 根据id拿到公司的namec赋值到customabbr
		if (truckid != null) {
			SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao
					.findById(truckid);
			this.selectedRowData.setTruckabbr(sc.getNamec());
		}
		// 保存
		this.serviceContext.busTruckMgrService.saveData(this.selectedRowData);
		this.mPkVal = this.selectedRowData.getId();
		this.initCombox();
	}
	
	/**
	 * 保存并新增
	 */
	@Action
	public void saveMaster2(){
		this.saveation();
		this.addMaster();
	}

	@Action
	@Override
	public void refreshMasterForm() {
		if (this.mPkVal != -1L) {
			this.selectedRowData = this.serviceContext.busTruckMgrService.busTruckDao
					.findById(this.mPkVal);
			if (this.selectedRowData != null) {
				if ("I".equals(this.selectedRowData.getTruckstate())) {
					this.truckstatedesc = (String) l.m.get("初始");
					this.disableAllButton(false);
				} else {
					this.truckstatedesc = (String) l.m.get("完成");
					this.disableAllButton(true);
				}

				this.changeDriver();
				if(this.selectedRowData.getTruckid()!=null&&this.selectedRowData.getTruckid()>0){
					SysCorporation syscorp = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getTruckid());
					if(syscorp!=null){
						Browser.execClientScript("$('#customer_input').val('"+syscorp.getNamec()+"');");
					}
				}
			}
		} else {
			this.disableAllButton(false);
		}
		findlinkjos();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.refresh();
		this.grid.setSelections(getGridSelIds());
		try {
			//2108  处理拖车公司行是自己分公司问题 ：如果报关公司是自己，就显示通知按钮
			SysCorporation corpration = serviceContext.sysCorporationService.sysCorporationDao
						.findOneRowByClauseWhere("  iscustomer = false AND isdelete = FALSE AND id = "+ selectedRowData.getTruckid());
			if (corpration != null) {
				Browser.execClientScript("noticeJsvar.show()");
			} else {
				Browser.execClientScript("noticeJsvar.hide()");
			}
		} catch (NoRowException e) {
			Browser.execClientScript("noticeJsvar.hide()");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	public void findlinkjos(){
		try {
			//查找对应生成的报关工作单
			BusTruck busTruck = serviceContext.busTruckMgrService.busTruckDao
					.findOneRowByClauseWhere("linktbl = 'bus_truck' AND linkid = "+ selectedRowData.getId());
			FinaJobs finajob = serviceContext.jobsMgrService.finaJobsDao.findById(busTruck.getJobid());
			jobnos = finajob.getNos();
		} catch (NoRowException e) {	
			jobnos = "";
		} catch (Exception e) {
			jobnos = "";
			e.printStackTrace();
		}
	}
	
	@Override
	public void add() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		super.add();
	}

	@Override
	public void del() {
		serviceContext.busShipContainerMgrService.removeDate(this
				.getGridSelectId());
		Browser.execClientScript("showmsg()");
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busShipContainerMgrService.saveData(dtlData);
		Browser.execClientScript("showmsg()");
	}

	/**
	 * 关联
	 */
	@Action
	public void chooseship() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("请先保存拖车单");
			return;

		}
		// 获取选中的箱/柜id号
		String[] ids = this.grid.getSelectedIds();
		// 关联报关和箱/柜
		this.serviceContext.busTruckMgrService.busTruckLink(this.mPkVal, ids);
		// 根据拖车id获得所有箱柜id
		String cotainerids = this.serviceContext.busTruckMgrService
				.getLinkContainersid(this.mPkVal);
		this.containerids = cotainerids;
		update.markUpdate(true, UpdateLevel.Data, "containerids");
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.grid.reload();
		this.grid.setSelections(getGridSelIds());

	}

	@Accessible
	public int[] getGridSelIds() {

		String sql = 
				  "\nSELECT "
				+ "\n(CASE WHEN EXISTS(SELECT 1 FROM bus_truck_link x WHERE truckid = " + this.mPkVal + " AND x.containerid = a.id) THEN TRUE ELSE FALSE END) AS flag"
				+ "\nFROM _bus_ship_container a "
				+ "\nWHERE a.isdelete = FALSE AND parentid IS NULL"
				+ "\nAND a.jobid = "+ this.jobid 
				+ "\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x WHERE x.containerid = a.id)"
				+ "\n		OR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid =" + this.mPkVal + " AND k.containerid = a.id)"
				+ "\n	 )"
				+ "\nORDER BY id";
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if ((Boolean) map.get("flag"))
					rowList.add(rownum);
				rownum++;
			}
			int row[] = new int[rowList.size()];
			for (int i = 0; i < rowList.size(); i++) {
				row[i] = rowList.get(i);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 司机框下拉选择时,更新司机对应的信息
	 */
	@Action
	private void changeDriverInfo() {
		String driver = AppUtils.getReqParam("driver");
		if (driver != null) {
			String[] driverInfo;
			try {
				driverInfo = this.serviceContext.busTruckMgrService
						.queryDriverInfo(driver);
				if (driverInfo != null) {
					this.selectedRowData.setDriverno(driverInfo[0]);
					this.selectedRowData.setDrivertel(driverInfo[1]);
					this.selectedRowData.setDrivermobile(driverInfo[2]);
					update
							.markUpdate(true, UpdateLevel.Data,
									"masterEditPanel");
				}
			} catch (Exception e) {
			}

		}
	}

	/**
	 * 拖车公司下拉选择时,更新司机信息
	 */
	@Action
	private void changeDriver() {
		Long truckid = this.selectedRowData.getTruckid();
		if (truckid != null) {
			String user = AppUtils.getUserSession().getUsercode();
			this.drivers = this.serviceContext.busTruckMgrService
					.queryDriversByInputer(user, truckid);
		} else {
			this.drivers = null;
		}
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	}

	/**
	 * 装货联系人下拉选择时,更新司机对应的信息
	 */
	@Action
	private void changeLoadContactInfo() {
		String loadcontact = AppUtils.getReqParam("loadcontact");
		if (loadcontact != null) {
			String[] loadContactInfo;
			try {
				loadContactInfo = this.serviceContext.busTruckMgrService
						.queryLoadContactInfo(loadcontact);
				if (loadContactInfo != null) {
					this.selectedRowData.setContacttel(loadContactInfo[0]);
					this.selectedRowData.setContactmobile(loadContactInfo[1]);
					this.selectedRowData.setLoadaddress(loadContactInfo[2]);
					this.selectedRowData.setLoadremarks(loadContactInfo[3]);
					update
							.markUpdate(true, UpdateLevel.Data,
									"masterEditPanel");
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 拖车单号下拉变化时，更新拖车信息
	 */
	@Action
	private void changeTruckInfo() {
		String trucknos = this.selectedRowData.getNos();
		if (!StrUtils.isNull(trucknos)) {
			try {
				this.selectedRowData = this.serviceContext.busTruckMgrService.busTruckDao.findOneRowByClauseWhere("nos = '"+trucknos+"' AND jobid = " + this.jobid);
				if (this.selectedRowData != null) {
					this.mPkVal = this.selectedRowData.getId();
					this.containerids = this.serviceContext.busTruckMgrService
							.getLinkContainersid(this.mPkVal);
					update.markUpdate(true, UpdateLevel.Data, "containerids");
					Browser.execClientScript("updateFactory();");
					this.refreshMasterForm();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 装货人下拉
	 */
	@Bind(id = "loadcontacts")
	public List<SelectItem> getLoadcontacts() {
		String user = AppUtils.getUserSession().getUsercode();
		String customerAbbr = null;
		if (this.selectedRowData != null) {
			Long jobid = this.selectedRowData.getJobid();
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao
					.findById(jobid);
			if (finaJobs != null) {
				customerAbbr = finaJobs.getCustomerabbr();
			}
		}
		return this.serviceContext.busTruckMgrService
				.queryLoadContactsByInputer(user, customerAbbr);
	}

	@Action
	public void finish() {
		String truckState = this.selectedRowData.getTruckstate();
		String updater = AppUtils.getUserSession().getUsername();
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存！");
			return;
		}
		if ("F".equals(truckState)) {
			MessageUtils.alert("已完成,请勿重复点击!");
			return;
		} else {
			try {
				String sql = "UPDATE bus_truck SET truckstate = 'F',updater='"
						+ updater + "',updatetime=NOW() WHERE id ="
						+ this.mPkVal;
				this.serviceContext.busTruckMgrService.busTruckDao
						.executeSQL(sql);
//				passTruct();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}

		this.refreshMasterForm();
	}

	@Action
	public void cancel() {
		String truckState = this.selectedRowData.getTruckstate();
		String updater = AppUtils.getUserSession().getUsername();
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存！");
			return;
		}
		if ("I".equals(truckState)) {
			MessageUtils.alert("尚未完成，无需取消！!");
			return;
		} else {
			try {
				String sql = "UPDATE bus_truck SET truckstate = 'I',updater='"
						+ updater + "',updatetime=NOW() WHERE id ="
						+ this.mPkVal;
				this.serviceContext.busTruckMgrService.busTruckDao
						.executeSQL(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.refreshMasterForm();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "";
		filter += 
				  "\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x WHERE x.containerid = t.id)"
				+ "\n		OR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid =" + this.mPkVal + " AND k.containerid = t.id)"
				+ "\n	 )"
				+ "\nAND t.jobid = " + this.jobid;
		m.put("filter", filter);
		m.put("truckid", this.mPkVal);
		return m;
	}

	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			MLType mlType = AppUtils.getUserSession().getMlType();
			return CommonComBoxBean.getComboxItems("d.filename", mlType.equals(LMapBase.MLType.en)?"COALESCE(d.namee,d.namec)":"d.namec",
					"sys_report AS d", " WHERE modcode = 'TruckRpt' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}


	@Action
	public void scanReport() {
		if (this.selectedRowData.getReportname() == null || "".equals(this.selectedRowData.getReportname())) {
			MessageUtils.alert("请选择格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/ship/"+this.selectedRowData.getReportname();
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}

	private String getArgs() {
		Long userid = AppUtils.getUserSession().getUserid();
		String args = "";
		args += "&id=" + this.mPkVal + "&userid=" + userid;
		return args;
	}

	private void disableAllButton(Boolean flag) {
		saveMaster.setDisabled(flag);
		delMaster.setDisabled(flag);
		chooseship.setDisabled(flag);
	}

	/**
	 * 保存关闭
	 */
	@Action
	protected void save2() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			this.editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

//	// 点击完成按钮,自动完成改任务
//	public void passTruct() {
//		try {
//			String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid,
//					WorkFlowEnumerateShip.TRUCT_END, "id");
//			WorkFlowUtil.passProcessBatch(workids);
//			MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//			// 正常走流程
//		} catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (CommonRuntimeException e) {
//			MessageUtils.alert(e.getMessage());
//		}
//	}

	@Action
	public void showdtlaction() {
		String isShow = AppUtils.getReqParam("isShow");
		if ("1".equals(isShow)) {
			String message = this.ShowMessage();
			if (!StrUtils.isNull(message)) {
				showDtlWindow.show();
				this.dtlContent = message;
				this.update.markUpdate(UpdateLevel.Data, "dtlContent");
			} else {
				//MessageUtils.alert("无拖车要求");
			}
		} else {
			showDtlWindow.close();
		}
	}

	public String ShowMessage() {
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid,
//				WorkFlowEnumerateShip.TRUCT_END, "id");
		String re = "";
		String ins = "";
//		if (workids == null) {
//			ins = "";
//		} else {
//			IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//			IWorkItem workItem = workflowSession.findWorkItemById(workids[0]);
//			String processinstranseid = workItem.getTaskInstance()
//					.getProcessInstanceId();
//			ins = WorkFlowUtil.getActivityConsByWorkitem(processinstranseid,
//					workids[0]);
//		}

		if (this.jobid != null) {
			try{
				BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao
						.findOneRowByClauseWhere("jobid = " + this.jobid + "");
				String ClaimClear = busShipping.getClaimTruck();
				if (!StrUtils.isNull(ClaimClear) && !StrUtils.isNull(ins)) {
					re = ClaimClear + "\n" + ins;
				} else if (!StrUtils.isNull(ClaimClear) && StrUtils.isNull(ins)) {
					re = ClaimClear;
				} else if (StrUtils.isNull(ClaimClear) && !StrUtils.isNull(ins)) {
					re = ins;
				} else {
					re = null;
				}
			}catch(NoRowException e){
				return re;
			}
		}
		return re;
	}
	
	
	
	@Bind
	@SaveState
	public String detailsContent;

	@Bind
	@SaveState
	public String detailsTitle;

	@SaveState
	public String type;

	@Bind
	public UIWindow detailsWindow;
	
	@Bind
	public UIWindow showDefineWindow;
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");

		if ("1".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "装货备注";
		} else if ("2".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "装货地址";
		} else if ("3".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "报关资料送至";
		} 
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
		
		this.detailsWindow.show();
	}
	
	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.type);
		this.detailsWindow.close();
	}
	
	public void setDetails(String type) {
		if ("1".equals(type)) {
			this.selectedRowData.setLoadremarks(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "loadremarks");
		}else if ("2".equals(type)) {
			this.selectedRowData.setLoadaddress(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "loadaddress");
		} else if ("3".equals(type)) {
			this.selectedRowData.setCusdoc_toaddr(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cusdoc_toaddr");
		} 
	}
	
	/**
	 * 输入框(大框)保存
	 */
	@Action
	public void saveDetails() {
		setDetails(this.type);
		this.saveMaster();
		this.detailsWindow.close();
	}
	
	@Action
	public void notice(){
		String sql = "SELECT f_fina_jobs2truck('customid="+selectedRowData.getId()+":userid="+AppUtils.getUserSession().getUserid()+"')";
		try {
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			refreshMasterForm();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void gojobnosaction(){
		try {
			//查找对应生成的报关工作单
			BusTruck buscustomer = serviceContext.busTruckMgrService.busTruckDao
					.findOneRowByClauseWhere("linktbl = 'bus_truck' AND linkid = "+ selectedRowData.getId());
			FinaJobs finajob = serviceContext.jobsMgrService.finaJobsDao
					.findById(buscustomer.getJobid());
			String url = AppUtils.chaosUrlArs("/scp/pages/module/land/jobsedit.xhtml")+ "&id=" + finajob.getId();
			AppUtils.openWindow("_showcusJobno_" + finajob.getId(), url);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void updateContactInfoAction(){
		String factory = AppUtils.getReqParam("factory").toString();
		if(!StrUtils.isNull(factory)){
			try{
				String sql = "SELECT contact,tel1,addressc FROM sys_corporation WHERE abbr = '"+factory+"' UNION SELECT name AS contact,tel AS tel1, address AS addressc FROM sys_corpcontacts WHERE name ='"+factory+"' LIMIT 1";
				Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				selectedRowData.setLoadcontact(StrUtils.getMapVal(map, "contact"));
				selectedRowData.setLoadaddress(StrUtils.getMapVal(map, "addressc"));
				selectedRowData.setContacttel(StrUtils.getMapVal(map, "tel1"));
				update.markUpdate(true,UpdateLevel.Data,"loadcontact");
				update.markUpdate(true,UpdateLevel.Data,"contacttel");
				update.markUpdate(true,UpdateLevel.Data,"loadaddress");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	@SaveState
	@Accessible
	public String sql = "AND 1=1";

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind
	private String qryCustomerKey;
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		String sql2 = sql;
		return this.customerService.getCustomerDataProvider(sql2);
	}
	
	
	@Action
	public void showLoadaddress() {
		showCustomerWindow.show();
		sql = "AND contactype = 'T' AND customerid = "+finaJobs.getCustomerid()+"";
		customerQry();
	}
	
	@Action
	public void saveLoadaddress() {
		String address = AppUtils.getReqParam("address").trim();
		SysCorpcontacts sysCorp = new SysCorpcontacts();
		sysCorp.setCustomerid(finaJobs.getCustomerid());
		sysCorp.setContactype("T");
		sysCorp.setAddress(address);
		sysCorp.setCustomerabbr(finaJobs.getCustomerabbr());
		sysCorp.setId(0);
		sysCorp.setSex("M");
		serviceContext.customerContactsMgrService.saveData(sysCorp);
	}
	
	@Action
	public void customerGrid_ondblclick() {
		String[] ids = this.customerGrid.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		SysCorpcontacts cor = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findById(Long.parseLong(ids[0].toString()));
		this.selectedRowData.setLoadaddress(cor.getAddress());
		this.update.markUpdate(UpdateLevel.Data, "loadaddress");
	}
	
	@Action
	public void deleteCustomer() {
		String[] ids = this.customerGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		for (String id : ids) {
			SysCorpcontacts cor = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findById(Long.parseLong(id));
			cor.setIsdelete(true);
			this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.modify(cor);
		}
		this.customerGrid.reload();
	}
	
}
