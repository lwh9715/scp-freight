package com.scp.view.module.kahang;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.Sysformcfg;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
@ManagedBean(name = "pages.module.kahang.buslandBean", scope = ManagedBeanScope.REQUEST)
public class BusLandBean extends EditGridFormView {

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
	private UIButton addContainer;
	
	@Bind
	private UIButton addMaster;
	
	@Bind
	private UIButton delMaster;
	
	@Bind
	private UIButton containeradd;
	
	@Bind
	private UIButton save1;
	
	@Bind
	private UIButton saveForms;
	
	@Bind
	private UIButton save2;
	

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
	private String impexp;
	
	@SaveState
	public String truckcompany="";
	
	/**
	 * 司机2下拉
	 */
	@Bind
	@SelectItems
	@SaveState
	private List<SelectItem> drivers2;
	
	@Bind
	@SaveState
	public String actionJsText;// 按不同公司自定义js从 sys_formcfg 获取

	@Override
	public void beforeRender(boolean isPostBack) {
		// TODO Auto-generated method stub
		if (!isPostBack) {
			init();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			if(!StrUtils.isNull(AppUtils.getReqParam("impexp"))){
				impexp = AppUtils.getReqParam("impexp");
			}
		
			if(selectedRowData.getTruckid()!=null&&this.selectedRowData.getTruckid()>0){
				SysCorporation sysCorporation = 
					serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getTruckid());
				String abbr = sysCorporation.getAbbr();
				this.truckcompany=(abbr!=null&&!abbr.equals("")?abbr:sysCorporation.getNamec());
			}
			
			actionJsText = "";
			List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao
					.findAllByClauseWhere(" formid = '"+ this.getMBeanName()+ "' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
			for (Sysformcfg sysformcfg : sysformcfgs) {
				actionJsText += sysformcfg.getJsaction();
			}
			update.markUpdate(true, UpdateLevel.Data, "actionJsText");
		}
		super.applyGridUserDef();
	}

	public void init() {
		selectedRowData = new BusTruck();
		this.pkVal = -1L;
		String jobid = AppUtils.getReqParam("id");
		jobid = jobid.replaceAll("#", "");
		this.jobid = Long.valueOf(jobid);
		this.initCombox();
		this.refreshMasterForm();
		if (this.jobid != null) {
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			if(finaJobs != null && (finaJobs.getIscheck()||finaJobs.getIscomplete())){
				saveMaster.setDisabled(true);

				addContainer.setDisabled(true);
				addMaster.setDisabled(true);
				
				delMaster.setDisabled(true);
				containeradd.setDisabled(true);
				save1.setDisabled(true);
				saveForms.setDisabled(true);
				save2.setDisabled(true);
			}
			this.selectedRowData.setJobid(this.jobid);
			this.containerids = this.serviceContext.busTruckMgrService
					.getLinkContainersid(this.pkVal);
			update.markUpdate(true, UpdateLevel.Data, "containerids");
		}
	}

	// 设置拖车号下拉列表值
	public void initCombox() {
		if (this.jobid != null) {
			try {
				BusTruck bustruck = this.serviceContext.busTruckMgrService.busTruckDao.findOneRowByClauseWhere("jobid = "+this.jobid+" AND isdelete = false");
				if (this.pkVal == -1L) {
					this.pkVal = bustruck.getId();
				}
				this.refreshMasterForm();
			} catch (NoRowException e){
				this.pkVal = -1L;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Inject(value = "l")
	protected MultiLanguageBean l;

	@Action
	public void addMaster() {
		dtlData = new BusShipContainer();
		this.pkValCnt = -1L;
		dtlData.setJobid(this.jobid);
		editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkValCnt");
	}

	@Action
	public void delMaster() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if(p.matcher(s).matches()){
				try {
					serviceContext.busShipContainerMgrService.removeDate(Long.parseLong(s));
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		editGrid.remove();
	}

	public void saveation(){
		// 地域标记,默认设为C,中国
		this.selectedRowData.setAreatype("C");
		// 拿到页面上的报关公司id
		Long truckid = this.selectedRowData.getTruckid();
		// 根据id拿到公司的namec赋值到customabbr
		if (truckid != null) {
			SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(truckid);
			if(sc != null){
				this.selectedRowData.setTruckabbr(sc.getNamec());
			}
		}
		// 保存
		this.serviceContext.busTruckMgrService.saveData(this.selectedRowData);
		this.pkVal = this.selectedRowData.getId();
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
	public void refreshMasterForm() {
		if (this.pkVal != -1L) {
			this.selectedRowData = this.serviceContext.busTruckMgrService.busTruckDao
					.findById(this.pkVal);
			if (this.selectedRowData != null) {
				//if ("I".equals(this.selectedRowData.getTruckstate())) {
					//this.truckstatedesc = (String) l.m.get("初始");
					//this.disableAllButton(false);
				//} else {
					//this.truckstatedesc = (String) l.m.get("完成");
					//this.disableAllButton(true);
				//}
				this.changeDriver();
			}
		} else {
			this.selectedRowData.setJobid(this.jobid);
			this.selectedRowData.setNos(this.selectedRowData.getNos());
			this.selectedRowData.setAreatype("");
			this.selectedRowData.setTruckstate("I");
			this.selectedRowData.setSingtime(Calendar.getInstance().getTime());
			this.disableAllButton(false);
		}

		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.refresh();
		this.editGrid.setSelections(getGridSelIds());
	}

	@Action
	public void add() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
	}

	@Override
	public void del() {
		serviceContext.busShipContainerMgrService.removeDate(this
				.getGridSelectId());
		Browser.execClientScript("showmsg()");
		this.editGrid.reload();
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
				.findById(this.pkValCnt);
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
		if (this.pkVal == -1l) {
			MessageUtils.alert("请先保存拖车单");
			return;

		}
		// 获取选中的箱/柜id号
		String[] ids = this.editGrid.getSelectedIds();
		// 关联报关和箱/柜
		this.serviceContext.busTruckMgrService.busTruckLink(this.pkVal, ids);
		// 根据拖车id获得所有箱柜id
		String cotainerids = this.serviceContext.busTruckMgrService
				.getLinkContainersid(this.pkVal);
		this.containerids = cotainerids;
		update.markUpdate(true, UpdateLevel.Data, "containerids");
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.editGrid.reload();
		this.editGrid.setSelections(getGridSelIds());

	}

	@Accessible
	public int[] getGridSelIds() {

		String sql = "\nSELECT "
				+ "\n(CASE WHEN EXISTS(SELECT 1 FROM bus_truck_link x WHERE truckid = "
				+ this.pkVal
				+ " AND x.containerid = a.id) THEN TRUE ELSE FALSE END) AS flag"
				+ "\nFROM _bus_ship_container a "
				+ "\nWHERE a.isdelete = FALSE " + "\nAND a.jobid = "
				+ this.jobid + "\nORDER BY id";
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
				driverInfo = this.serviceContext.busTruckMgrService.queryDriverInfo(driver);
				if (driverInfo != null) {
					this.selectedRowData.setDriverno(driverInfo[0]);
					this.selectedRowData.setDrivertel(driverInfo[1]);
					this.selectedRowData.setDrivermobile(driverInfo[2]);
					update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
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
			this.drivers = this.serviceContext.busTruckMgrService.queryDriversByInputer(user, truckid);
			this.drivers2 = this.serviceContext.busTruckMgrService.queryDriversByInputer(user, truckid);
		} else {
			this.drivers = null;
			this.drivers2 = null;
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
					update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
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
					this.pkVal = this.selectedRowData.getId();
					this.containerids = this.serviceContext.busTruckMgrService
							.getLinkContainersid(this.pkVal);
					update.markUpdate(true, UpdateLevel.Data, "containerids");
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
		if (this.pkVal == -1L) {
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
						+ this.pkVal;
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
		if (this.pkVal == -1L) {
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
						+ this.pkVal;
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
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x WHERE x.containerid = t.id)"
				+ "\nOR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid ="
				+ this.pkVal
				+ " AND k.containerid = t.id))"
				+ "\nAND t.jobid = " + this.jobid;
		m.put("qry", qry);
		return m;
	}

	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			MLType mlType = AppUtils.getUserSession().getMlType();
			return CommonComBoxBean.getComboxItems("d.filename", mlType.equals(LMapBase.MLType.en)?"COALESCE(d.namee,d.namec)":"d.namec",
					"sys_report AS d", " WHERE modcode = 'land' AND isdelete = FALSE",
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
		String arg = "&para=jobid="+this.jobid+":userid="+AppUtils.getUserSession().getUserid()+":corpidcurrent="+AppUtils.getUserSession().getCorpid()+":";
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/land/"+this.selectedRowData.getReportname();
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs() +arg);
	}

	private String getArgs() {
		Long userid = AppUtils.getUserSession().getUserid();
		String args = "";
		args += "&id=" + this.pkVal + "&userid=" + userid;
		return args;
	}

	private void disableAllButton(Boolean flag) {
		saveMaster.setDisabled(flag);
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

	// 点击完成按钮,自动完成改任务
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
		} else {
			showDtlWindow.close();
		}
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
		} else if ("4".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "卸货地址";
		} else if ("5".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "卸货备注";
		} else if ("6".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "运费说明";
		} else if ("7".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "运费备注";
		}  else if ("8".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "备注";
		} else if ("9".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "特别事项";
		} else if ("10".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "装货信息";
		} else if ("11".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "香港报关信息";
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
		if ("2".equals(type)) {
			this.selectedRowData.setLoadremarks(this.detailsContent);
		}else if ("1".equals(type)) {
			this.selectedRowData.setLoadaddress(this.detailsContent);
		} else if ("3".equals(type)) {
			this.selectedRowData.setCusdoc_toaddr(this.detailsContent);
		} else if ("4".equals(type)) {
			this.selectedRowData.setUnloadaddress(this.detailsContent);
		} else if ("5".equals(type)) {
			this.selectedRowData.setUnloadremarks(this.detailsContent);
		} else if ("6".equals(type)) {
			this.selectedRowData.setFeedesc(this.detailsContent);
		} else if ("7".equals(type)) {
			this.selectedRowData.setFeeremarks(this.detailsContent);
		} else if ("8".equals(type)) {
			this.selectedRowData.setRemarks(this.detailsContent);
		}else if ("9".equals(type)) {
			this.selectedRowData.setSpecial(this.detailsContent);
		}else if ("10".equals(type)) {
			this.selectedRowData.setLoadinfo(this.detailsContent);
		}else if ("11".equals(type)) {
			this.selectedRowData.setCustomhkinfo(this.detailsContent);
		}
		this.update.markUpdate(true,UpdateLevel.Data, "masterEditPanel");
	}
	
	/**
	 * 输入框(大框)保存
	 */
	@Action
	public void saveDetails() {
		setDetails(this.type);
		this.save1();
		this.detailsWindow.close();
	}
	
	/**
	 * @return 1979 陆运工作单修改 提取这个委托人下其他拖车单下面DISTINCT装货公司
	 */
	@Bind(id="selectfactory")
	@SelectItems
	private List<SelectItem> getSelectfactory(){
		try {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT factory FROM bus_truck a "+
						 "\n	WHERE a.isdelete = FALSE AND a.factory <> '' AND a.factory IS NOT NULL "+
						 "\n	AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = a.jobid "+
						 "\n	AND EXISTS(SELECT 1 FROM fina_jobs q,bus_truck w "+
						 "\n							WHERE q.id = w.jobid AND q.id = "+this.selectedRowData.getJobid()+" AND q.customerid = x.customerid));";
	    	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
	    	if(list!=null){
	    		Object value = null;
	    		Object lable = null;
	    		for (Map dept : list) {
	    			lable = dept.get("factory");
	    			value = dept.get("factory");
					items.add(new SelectItem(String.valueOf(value),
							String.valueOf(lable)));
	    		}	
	    	}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 1979 陆运工作单修改 选择胡子那个货公司后，将上一单里面后面对应的信息带过来
	 */
	@Action
	public void changeDactory(){
		String factory = AppUtils.getReqParam("factory");
		String sql = "isdelete = FALSE AND factory = '"+factory+"' ORDER BY loadtime DESC LIMIT 1";
		try {
			List<BusTruck> busTruck = serviceContext.busTruckMgrService.busTruckDao
					.findAllByClauseWhere(sql);
			if (busTruck != null && busTruck.size() > 0) {
				this.selectedRowData.setLoadtime(busTruck.get(0).getLoadtime());
				this.selectedRowData.setLoadcontact(busTruck.get(0)
						.getLoadcontact());
				this.selectedRowData.setContacttel(busTruck.get(0)
						.getContacttel());
				this.selectedRowData.setLoadaddress(busTruck.get(0)
						.getLoadaddress());
				this.selectedRowData.setLoadremarks(busTruck.get(0)
						.getLoadremarks());
				update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * @return 1979 陆运工作单修改
	 */
	@Bind(id="selectunloadfactory")
	@SelectItems
	private List<SelectItem> getSelectunloadfactory(){
		try {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT unloadfactory FROM bus_truck a "+
						 "\n	WHERE a.isdelete = FALSE AND a.unloadfactory <> '' AND a.unloadfactory IS NOT NULL "+
						 "\n	AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = a.jobid "+
						 "\n	AND EXISTS(SELECT 1 FROM fina_jobs q,bus_truck w "+
						 "\n							WHERE q.id = w.jobid AND q.id = "+this.selectedRowData.getJobid()+" AND q.customerid = x.customerid));";
	    	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
	    	if(list!=null){
	    		Object value = null;
	    		Object lable = null;
	    		for (Map dept : list) {
	    			lable = dept.get("unloadfactory");
	    			value = dept.get("unloadfactory");
					items.add(new SelectItem(String.valueOf(value),
							String.valueOf(lable)));
	    		}	
	    	}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 1979 陆运工作单修改
	 */
	@Action
	public void changefactory(){
		String unloadfactory = AppUtils.getReqParam("unloadfactory");
		String sql = "isdelete = FALSE AND unloadfactory = '"+unloadfactory+"' ORDER BY unloadtime DESC LIMIT 1";
		List<BusTruck> busTruck = serviceContext.busTruckMgrService.busTruckDao.findAllByClauseWhere(sql);
		try{
			if(busTruck!=null&&busTruck.size()>0){
				this.selectedRowData.setUnloadtime(busTruck.get(0).getUnloadtime());
				this.selectedRowData.setUnloadcontact(busTruck.get(0).getUnloadcontact());
				this.selectedRowData.setUnloadconttel(busTruck.get(0).getUnloadconttel());
				this.selectedRowData.setUnloadaddress(busTruck.get(0).getUnloadaddress());
				this.selectedRowData.setUnloadremarks(busTruck.get(0).getUnloadremarks());
				update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			}
		} catch (Exception e) {
		}
	}
	
	@Action
	public void setCustoms(){
		String customsid = AppUtils.getReqParam("customsid");
		String sql = "isdelete = FALSE AND customerid = "+customsid+" ";
		try{
			List<SysCorpcontacts> sysCorpcontacts = serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
			if(sysCorpcontacts!=null&&sysCorpcontacts.size()>0){
				this.selectedRowData.setCustomscontact(sysCorpcontacts.get(0).getName());
				this.selectedRowData.setCustomsconttel(sysCorpcontacts.get(0).getTel());
				this.selectedRowData.setCustomsaddress(sysCorpcontacts.get(0).getAddress());
				update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			}
		}catch(Exception e){
		}
	}
	
	//新增
	@Action
	protected void containeradd(){
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
	}
	
	//保存
	@Action
	protected void save1() {
		try {
			dtlData.setIspace(false);
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	//保存并新增
	@Action
	protected void saveForms() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			dtlData = new BusShipContainer();
			dtlData.setJobid(this.jobid);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 1979 陆运工作单修改香港报关公司
	 */
	@Action
	public void changecustomsidhk(){
		String customsidhk = AppUtils.getReqParam("customsidhk");
		String sql = "isdelete = FALSE AND customsidhk = '"+customsidhk+"' ORDER BY unloadtime DESC LIMIT 1";
		List<BusTruck> busTruck = serviceContext.busTruckMgrService.busTruckDao.findAllByClauseWhere(sql);
		try{
			if(busTruck!=null&&busTruck.size()>0){
				this.selectedRowData.setCustomscontacthk(busTruck.get(0).getCustomscontacthk());
				this.selectedRowData.setCustomsconttelhk(busTruck.get(0).getCustomsconttelhk());
				update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			}
		} catch (Exception e) {
		}
	}
	
	
	/**
	 * 香港报关公司下拉选择框
	 */
	@Bind(id="seleccustomsidhk")
	@SelectItems
	private List<SelectItem> getSeleccustomsidhk(){
		try {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT customsidhk FROM bus_truck a "+
						 "\n	WHERE a.isdelete = FALSE AND a.customsidhk <> '' AND a.customsidhk IS NOT NULL "+
						 "\n	AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = a.jobid "+
						 "\n	AND EXISTS(SELECT 1 FROM fina_jobs q,bus_truck w "+
						 "\n							WHERE q.id = w.jobid AND q.id = "+this.selectedRowData.getJobid()+" AND q.customerid = x.customerid));";
	    	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
	    	if(list!=null){
	    		Object value = null;
	    		Object lable = null;
	    		for (Map dept : list) {
	    			lable = dept.get("customsidhk");
	    			value = dept.get("customsidhk");
					items.add(new SelectItem(String.valueOf(value),
							String.valueOf(lable)));
	    		}	
	    	}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Bind
	@SaveState
	public Long pkValCnt;
	
	@Action
	public void edit(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		try{
			this.pkValCnt = getGridSelectId();
			doServiceFindData();
			this.refreshForm();
			if(editWindow != null)editWindow.show();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}catch (NumberFormatException e) {
			MessageUtils.alert("新增条目请先保存!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void saveMaster(){
		try {
			doServiceSaveMaster();
			Browser.execClientScript("parent.refreshAjaxPanel.submit();");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		refresh();
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	public void doServiceSaveMaster() {
		try {
			serviceContext.busTruckMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			this.editGrid.commit();
			if (modifiedData != null) {
			    update(modifiedData);
			}
			if (addedData != null) {
			    add(addedData);
			}
			if(removedData != null){
				remove(removedData);
			}
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busShipContainerMgrService.updateBatchEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busShipContainerMgrService.addBatchEditGrid(addedData,jobid,selectedRowData.getId());
	}
	
	@Override
	protected void remove(Object removedData) {
		serviceContext.busShipContainerMgrService.removedBatchEditGrid(removedData);
	}
	@Action
	public void addContainer(){
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setShipid(this.selectedRowData.getId());
		
		try {
			List<BusShipContainer> temps = serviceContext.busShipContainerMgrService.busShipContainerDao.findAllByClauseWhere("jobid = "+this.jobid+" AND isdelete = false AND parentid IS NULL ORDER BY orderno");
			
			if(addedData != null){
				JSONArray jsonArray = (JSONArray) addedData;
				if(jsonArray != null && jsonArray.size() > 0){
					JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.size()-1);
					if(jsonObject.get("cntypeid")!=null && !String.valueOf(jsonObject.get("cntypeid")).isEmpty()){
						dtlData.setCntypeid(Long.parseLong(String.valueOf(jsonObject.get("cntypeid"))));
					}else{
						dtlData.setCntypeid(null);
					}
					if(jsonObject.get("ldtype")!=null && !String.valueOf(jsonObject.get("ldtype")).isEmpty()){
						dtlData.setLdtype(String.valueOf(jsonObject.get("ldtype")));
					}else{
						dtlData.setLdtype(null);
					}
					if(jsonObject.get("packagee")!=null && !String.valueOf(jsonObject.get("packagee")).isEmpty()){
						dtlData.setPackagee(String.valueOf(jsonObject.get("packagee")));
					}else{
						dtlData.setPackagee(null);
					}
					if(temps == null || temps.size()<=0){
						dtlData.setOrderno(jsonArray.size()+1);
					}else{
						dtlData.setOrderno(temps.size()+jsonArray.size()+1);
					}
					if(jsonObject.get("grswgt2")!=null && !String.valueOf(jsonObject.get("grswgt2")).isEmpty()){
						dtlData.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt2"))));
					}else{
						dtlData.setGrswgt(new BigDecimal(0));
					}
					if(jsonObject.get("cbm2")!=null && !String.valueOf(jsonObject.get("cbm2")).isEmpty()){
						dtlData.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
					}else{
						dtlData.setCbm(new BigDecimal(0));
					}
					if(jsonObject.get("vgm")!=null && !String.valueOf(jsonObject.get("vgm")).isEmpty()){
						dtlData.setVgm(new BigDecimal(String.valueOf(jsonObject.get("vgm"))));
					}else{
						dtlData.setVgm(new BigDecimal(0));
					}
					if(jsonObject.get("piece2")!=null && !String.valueOf(jsonObject.get("piece2")).isEmpty()){
						dtlData.setPiece(new Integer(String.valueOf(jsonObject.get("piece2"))));
					}else{
						dtlData.setPiece(new Integer(0));
					}
				}else{
					String[] ids = editGrid.getSelectedIds();
					if(ids != null && ids.length > 0){//找到选择的最后一行
						String id = ids[ids.length-1];
						if(StrUtils.isNumber(id)){
							BusShipContainer temp = serviceContext.busShipContainerMgrService.busShipContainerDao.findById(Long.valueOf(id));
							dtlData.setCntypeid(temp.getCntypeid());
							dtlData.setLdtype(temp.getLdtype());
							dtlData.setPackagee(temp.getPackagee());
							dtlData.setGrswgt(temp.getGrswgt());
							dtlData.setPiece(temp.getPiece());
							dtlData.setCbm(temp.getCbm());
							dtlData.setVgm(temp.getVgm());
						}
					}else{
						try {//没有勾选的情况下，找到最后一行
							if(temps != null && temps.size()>0){
								dtlData.setCntypeid(temps.get(temps.size()-1).getCntypeid());
								dtlData.setLdtype(temps.get(temps.size()-1).getLdtype());
								dtlData.setPackagee(temps.get(temps.size()-1).getPackagee());
								dtlData.setGrswgt(temps.get(temps.size()-1).getGrswgt());
								dtlData.setPiece(temps.get(temps.size()-1).getPiece());
								dtlData.setCbm(temps.get(temps.size()-1).getCbm());
								dtlData.setVgm(temps.get(temps.size()-1).getVgm());
							}else{
								dtlData.setGrswgt(new BigDecimal(0));
								dtlData.setPiece(0);
								dtlData.setCbm(new BigDecimal(0));
								dtlData.setVgm(new BigDecimal(0));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					dtlData.setOrderno(temps.size()+1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		editGrid.appendRow(dtlData);
	}	
	
	
	@Action
	public void saveContainer(){
		try {
			editGrid.commit();
			boolean isChangeFlag = false;
			//如果列表里面修改的柜子，数据没有保存，先保存再处理后面数据  20190507 
			if (modifiedData != null) {
			    update(modifiedData);
			    JSONArray jsonArray = (JSONArray) modifiedData;
			    if(jsonArray.size()>0)isChangeFlag=true;
			}
			if (addedData != null) {
			    add(addedData);
			    JSONArray jsonArray = (JSONArray) addedData;
			    if(jsonArray.size()>0)isChangeFlag=true;
			}
			if(removedData != null){
				remove(removedData);
				JSONArray jsonArray = (JSONArray) removedData;
			    if(jsonArray.size()>0)isChangeFlag=true;				
			}
			if(isChangeFlag){
				editGrid.reload();
			}
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
}
