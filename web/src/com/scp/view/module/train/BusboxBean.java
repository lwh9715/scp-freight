package com.scp.view.module.train;

import java.util.ArrayList;
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
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusTrain;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.train.busboxBean", scope = ManagedBeanScope.REQUEST)
public class BusboxBean extends MastDtlView {

	@SaveState
	@Accessible
	public BusTrain selectedRowData = new BusTrain();

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
		selectedRowData = new BusTrain();
		
		String jobid = AppUtils.getReqParam("jobid");
		jobid = jobid.replaceAll("#", "");
		this.jobid = Long.valueOf(jobid);
		String message = this.ShowMessage();
		if (!StrUtils.isNull(message)) {
			showDtlWindow.show();
			this.dtlContent = message;
			this.update.markUpdate(UpdateLevel.Data, "dtlContent");
		}
		if (this.jobid != null) {
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			this.selectedRowData = this.serviceContext.busTrainMgrService.findByjobId(this.jobid);
			this.containerids = this.serviceContext.busTruckMgrService
					.getLinkContainersid(this.mPkVal);
			update.markUpdate(true, UpdateLevel.Data, "containerids");
		}
	}

	
	
	@Inject(value = "l")
	protected MultiLanguageBean l;

/*	@Override
	public void addMaster() {
		this.selectedRowData = new BusTrain();
		this.selectedRowData.setId(0L);
		this.selectedRowData.setJobid(this.jobid);
		this.selectedRowData.setTruckstate("I");
		this.truckstatedesc = (String) l.m.get("初始");
		this.selectedRowData.setReportname("");
		this.selectedRowData.setSingtime(new Date());
		this.mPkVal = -1l;
		this.changeDriver();
		Browser.execClientScript("clearFactory()");
		//this.refreshMasterForm();
		refresh();

	}*/

	/*@Override
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
*/
	/**
	 * 保存
	 */
	@Override
	public void doServiceSaveMaster() {
		
		try{
			
			if(dtlData != null){
				serviceContext.busShipContainerMgrService.saveData(dtlData);
			}
			MessageUtils.showMsg("OK");
			this.grid.reload();
			//this.mPkVal = selectedRowData.getId();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * 保存并新增
	 */
	@Action
	public void saveMaster2(){
		this.addMaster();
	}

	/*@Action
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
	}*/

	
	
	@Override
	public void add() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setLdtype(selectedRowData.getLdtype());
		dtlData.setShipid(this.selectedRowData.getId());
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
		this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao.findById(this.pkVal);
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




	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "";
		filter += 
//				  "\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x WHERE x.containerid = t.id)"
//				+ "\n		OR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid =" + this.mPkVal + " AND k.containerid = t.id)"
//				+ "\n	 )"
				"\nAND t.jobid = " + this.jobid;
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
	
	/**
	 *主柜信息
	 * 
	 * @return
	 */
	@Bind(id = "parentCnt")
	public List<SelectItem> getParentCnt() {
		try {
			// ApplicationUtils.debug(this.dtlData.getBookdtlid());
			return CommonComBoxBean
					.getComboxItems(
							"x.id",
							"COALESCE(x.cntypedesc,'') ||':'|| COALESCE(x.cntno,'')",
							"_bus_ship_container AS x",
							"WHERE x.isdelete = false and x.jobid = ANY(SELECT j.parentid FROM fina_jobs j WHERE j.id = "
									+ this.jobid + ")", "ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Action
	public void containeradd() {
		BusShipContainer old = dtlData;
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setLdtype(old.getLdtype());
		dtlData.setCntypeid(old.getCntypeid());
		dtlData.setPackagee(old.getPackagee());
		
		this.dtlData.setShipid(this.selectedRowData.getId());
	}
	
	
	/**
	 * 保存关闭
	 *//*
	@Action
	protected void save1() {
		try {
			dtlData.setMarkno(AppUtils
					.replaceStringByRegEx(dtlData.getMarkno()));
			dtlData.setGoodsnamee(AppUtils.replaceStringByRegEx(dtlData
					.getGoodsnamee()));
			dtlData.setMaterial(AppUtils.replaceStringByRegEx(dtlData
					.getMaterial()));
			dtlData.setGoodsname(AppUtils.replaceStringByRegEx(dtlData
					.getGoodsname()));
			dtlData.setRemarks(AppUtils.replaceStringByRegEx(dtlData
					.getRemarks()));
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			if (dtlData != null && dtlData.getIsselect() != null
					&& dtlData.getIsselect()) {
				//updateChooseship();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}*/
	
	@Action
	public void save1() {
		try {
			doServiceSaveMaster(); //Master
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		//update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}
	
	
	

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub
		
	}
}
