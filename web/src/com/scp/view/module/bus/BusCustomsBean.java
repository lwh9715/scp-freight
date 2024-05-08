package com.scp.view.module.bus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.ship.BusCustomsDao;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusCustoms;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.bus.buscustomsBean", scope = ManagedBeanScope.REQUEST)
public class BusCustomsBean extends MastDtlView {

	@Resource
	private BusCustomsDao busCustomsDao;

	@SaveState
	@Accessible
	public BusCustoms selectedRowData = new BusCustoms();


	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();
	
	/**
	 * 报关单号下拉
	 */
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> customsnoses;

	/**
	 * 工作id
	 */
	@SaveState
	@Accessible
	public Long jobid;
	
	/**
	 * 报关关联的所有货柜id
	 * 拼接：xxx,xxx,xxx
	 */
	@Bind
	@SaveState
	public String containerids;
	
	/**
	 * 状态描述（I：初始；F：完成）
	 */
	@Bind
	@SaveState
	public String customstatedesc;
	
	@Bind
	private UIButton saveMaster;
	
	@Bind
	private UIButton delMaster;
	
	@Bind
	private UIButton chooseship;
	
	@Bind
	@Accessible
	public UIWindow showDtlWindow;
	
	@Bind
	@Accessible
	public String dtlContent;
	
	@Bind
	@SaveState
	public String jobnos;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		// TODO Auto-generated method stub
		if (!isPostBack) {
			init();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
		}
		super.applyGridUserDef();
	}

	@Override
	public void init() {
		selectedRowData = new BusCustoms();
		selectedRowData.setCurrencyap("CNY");
		selectedRowData.setCurrencyar("CNY");
		String jobid = AppUtils.getReqParam("id");
		this.jobid = Long.valueOf(jobid);
		this.initCombox();
		String message = this.ShowMessage();
		if(!StrUtils.isNull(message)){
    		showDtlWindow.show();
    		this.dtlContent = message;
    		this.update.markUpdate(UpdateLevel.Data, "dtlContent");	
		}
		if(this.jobid != null) {
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			this.selectedRowData.setJobid(this.jobid);
			this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
			update.markUpdate(true, UpdateLevel.Data, "containerids");
		}
		try {
			//1988 处理报关行是自己分公司问题 ：如果报关公司是自己，就显示通知按钮
			SysCorporation corpration = serviceContext.sysCorporationService.sysCorporationDao
					.findOneRowByClauseWhere(" iscustomer = false AND isdelete = FALSE AND id = " + selectedRowData.getCustomid());
			if (corpration == null) {
				Browser.execClientScript("noticeJsvar.hide()");
			}
		} catch (NoRowException e) {
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		findlinkjos();
	}
	
	public void findlinkjos(){
		try {
			//查找对应生成的报关工作单
			BusCustoms buscustomer = serviceContext.busCustomsMgrService.busCustomsDao
					.findOneRowByClauseWhere("linktbl = 'bus_customs' AND linkid = "+ selectedRowData.getId());
			FinaJobs finajob = serviceContext.jobsMgrService.finaJobsDao.findById(buscustomer.getJobid());
			jobnos = finajob.getNos();
		} catch (Exception e) {
			jobnos = "";
		}
	}
	
	public void initCombox() {
		if (this.jobid != null) {
			List<BusCustoms> buscustomsList = this.serviceContext.busCustomsMgrService.getBusCustomsListByJobid(this.jobid);
			if(buscustomsList != null && buscustomsList.size() > 0) {
				List<SelectItem> items = new ArrayList<SelectItem>();
				for(BusCustoms bc : buscustomsList) {
					items.add(new SelectItem(bc.getNos(), bc.getNos()));
				}
				items.add(new SelectItem(null, ""));
				this.customsnoses = items;
				if(this.mPkVal == -1L) {
					this.mPkVal = buscustomsList.get(0).getId();
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
		this.selectedRowData = new BusCustoms();
		this.selectedRowData.setJobid(this.jobid);
		// 设置默认签单时间为当天
		this.selectedRowData.setSingtime(new Date());
		// 设置默认状态为“初始”
		this.selectedRowData.setCustomstate("I");
		this.customstatedesc = (String)l.m.get("初始");
		this.mPkVal = -1l;
		try{
			BusShipping ship = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid="+jobid);
			if(ship!=null){
				this.selectedRowData.setVessel(ship.getVessel());
				this.selectedRowData.setVoyage(ship.getVoyage());
				this.selectedRowData.setPoa(ship.getPoa());
				this.selectedRowData.setPol(ship.getPol());
				this.selectedRowData.setCls(ship.getClstime());
			}
		}catch(NoRowException e){
		}
		Browser.execClientScript("noticeJsvar.hide()");
		this.refreshMasterForm();
		refresh();

	}

	@Override
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			try {
				serviceContext.busCustomsMgrService.removeDate(selectedRowData
						.getId());
				this.addMaster();
				this.initCombox();
				this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
				Browser.execClientScript("showmsg()");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/*
	 * 保存
	 */
	@Override
	public void doServiceSaveMaster() {
		//地域标记,默认设为C,中国
		this.selectedRowData.setAreatype("C");
		//拿到页面上的报关公司id
		Long customid = this.selectedRowData.getCustomid();
		//根据id拿到公司的namec赋值到customabbr
		if(customid != null) {
			SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(customid);
			this.selectedRowData.setCustomabbr(sc.getNamec());
		}
		try {
			//保存
			this.serviceContext.busCustomsMgrService.saveData(this.selectedRowData);
			this.mPkVal = this.selectedRowData.getId();
		} catch (Exception e) {
			this.mPkVal = 0L;
			MessageUtils.showException(e);
		}
		this.initCombox();
	}

	@Action
	@Override
	public void refreshMasterForm() {
		if(this.mPkVal != -1L) {
			this.selectedRowData = this.busCustomsDao.findById(this.mPkVal);
			if(this.selectedRowData != null) {
				if("I".equals( this.selectedRowData.getCustomstate())) {
					this.customstatedesc = (String)l.m.get("初始");
					this.disableAllButton(false);
				} else {
					this.customstatedesc = (String)l.m.get("完成");
					this.disableAllButton(true);
				}
				try {
					SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getOperationid());
					String js = "$('#operationcustomer_input').val('"+ (sysCorporation != null ? sysCorporation.getNamec() : "" )+ "');";
					Browser.execClientScript(js);
					
				} catch (Exception e) {
					Browser.execClientScript("$('#customer_input').val('');");
				}
				if(this.selectedRowData.getCustomid()!=null&&this.selectedRowData.getCustomid()>0){
					SysCorporation syscorp = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomid());
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
			//1988 处理报关行是自己分公司问题 ：如果报关公司是自己，就显示通知按钮
			SysCorporation corpration = serviceContext.sysCorporationService.sysCorporationDao
						.findOneRowByClauseWhere("  iscustomer = false AND isdelete = FALSE AND id = "+ selectedRowData.getCustomid());
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
//		Browser.execClientScript("showmsg()");
	}

	@Override
	public void add() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		super.add();
	}

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length ==0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		for(String id : ids) {
			serviceContext.busShipContainerMgrService.removeDate(Long.valueOf(id));
		}
		
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

	/*
	 * 关联
	 */
	@Action
	public void chooseship() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert((String)l.m.get("请先保存报关单"));
			return;
		}
		// 获取选中的柜子id
		String[] ids = this.grid.getSelectedIds();
		// 关联报关和柜子
		try {
			// 主柜和拆分柜不能同时选择
			if(ids != null && ids.length > 0) {
				for(String id : ids) {
					String sql = "SELECT COALESCE((SELECT parentid FROM bus_ship_container WHERE id = "+id+" AND isdelete = FALSE),0) IN ("+StrUtils.array2List(ids)+") AS flag";
					Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					Boolean flag = Boolean.valueOf(m.get("flag").toString());
					if(flag) {
						MessageUtils.alert((String)l.m.get("主柜和拆分柜不能同时选择")+"!");
						return;
					}
				}
			}
			
			this.serviceContext.busCustomsMgrService.busCustomsLink(this.mPkVal, ids);
			// 根据报关id获得所有柜子id
			String cotainerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
			this.containerids = cotainerids;
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			update.markUpdate(UpdateLevel.Data, "containerids");
			this.refresh();
			this.grid.setSelections(getGridSelIds());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/*
	 * Neo 2014/5/6
	 * 完成，改变状态
	 */
	@Action
	public void finish() {
		String customsState = this.selectedRowData.getCustomstate();
		String updater=AppUtils.getUserSession().getUsername();
		if(this.mPkVal == -1L) {
			MessageUtils.alert((String)l.m.get("请先保存")+"!");
			return;
		}
		if("F".equals(customsState)) {
			MessageUtils.alert((String)l.m.get("已完成,请勿重复点击")+"!");
			return;
		} else {
			try {
				String sql = "UPDATE bus_customs SET customstate = 'F',updater='" + updater+ "',updatetime=NOW() WHERE id = "+this.mPkVal+";";
//				if(!StrTools.isNull(selectedRowData.getTaxretno())) { //如果核销单号不为空，则插入一条退税核销纪录
//					sql += "\nINSERT INTO bus_customs_taxret (id,customsid) VALUES(getid(),"+this.mPkVal+");";
//				}
				this.serviceContext.busCustomsMgrService.busCustomsDao.executeSQL(sql);
//				passCus();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		
		this.refreshMasterForm();
	}
	
	@Action
	public void cancel() {
		String customState = this.selectedRowData.getCustomstate();
		String updater=AppUtils.getUserSession().getUsername();
		if(this.mPkVal == -1L) {
			MessageUtils.alert((String)l.m.get("请先保存")+"！");
			return;
		}
		if("I".equals(customState)) {
			MessageUtils.alert((String)l.m.get("尚未完成，无需取消")+"!");
			return;
		} else {
			try {
				String sql = "UPDATE bus_customs SET customstate = 'I',updater='" + updater+ "',updatetime=NOW() WHERE id = "+this.mPkVal+";";
				sql += "\nDELETE FROM bus_customs_taxret WHERE customsid = "+this.mPkVal+";";
				this.serviceContext.busCustomsMgrService.busCustomsDao.executeSQL(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.refreshMasterForm();
	}
	
	/*
	 * 报关单号下拉变化时，更新报关信息
	 */
	@Action
    private void changeCustomsInfo() {
		String customsnos = this.selectedRowData.getNos();
        if(!StrUtils.isNull(customsnos)) {
        	try {
				this.selectedRowData = this.serviceContext.busCustomsMgrService.busCustomsDao.findByNo(customsnos);
				if(this.selectedRowData != null) {
					this.mPkVal = this.selectedRowData.getId();
					this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
					update.markUpdate(true, UpdateLevel.Data, "containerids");
					this.refreshMasterForm();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
	
	@Accessible
	public int[] getGridSelIds() {

		String sql = 
				"\nSELECT "+
				"\n(CASE WHEN EXISTS(SELECT 1 FROM bus_customs_link WHERE customsid=" + this.mPkVal + " AND containerid = a.id) THEN TRUE ELSE FALSE END) AS flag"+
				"\nFROM _bus_ship_container a "+
				"\nWHERE a.isdelete = FALSE " +
				"\nAND a.jobid = " + this.jobid +
				"\nORDER BY cntno,sealno,ldtype";
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
		String qry = StrUtils.getMapVal(m, "qry");
		qry += 
			//"\nAND (NOT EXISTS (SELECT 1 FROM bus_customs_link x WHERE x.containerid = t.id)" +
			//"\nOR EXISTS(SELECT 1 FROM bus_customs_link c WHERE c.customsid =" + this.mPkVal + " AND c.containerid = t.id))"+
			"\nAND t.jobid = " + this.jobid;
		m.put("qry", qry);
		return m;
	}
	
	@Bind(id="reportbuscustomsmat")
	public List<SelectItem> getReportbuscustomsmat(){
		try{
			MLType mlType = AppUtils.getUserSession().getMlType();
			return CommonComBoxBean.getComboxItems("d.filename",mlType.equals(LMapBase.MLType.en)?"COALESCE(d.namee,d.namec)":"d.namec",
					"sys_report AS d"," WHERE modcode = 'CustomRpt' AND isdelete = FALSE",
					"order by filename");
		}catch(Exception e){
			MessageUtils.showException(e);
			return null;
		}
	}
	@Bind
	@SaveState
	@Accessible
	public String showbuscustomsname="";
	
	@Action
	public void scanReport() {
		if (showbuscustomsname == null || "".equals(showbuscustomsname)) {
			MessageUtils.alert((String)l.m.get("请选择格式！")+"!");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"+showbuscustomsname;
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
	
	@Bind
	@SaveState
	public String splitCount;
	
	/*
	 * 拆柜
	 */
	@Action
	public void splitCnt() {
		String[] ids = this.grid.getSelectedIds();
		if(ids.length == 0 || ids == null) {
			MessageUtils.alert((String)l.m.get("选择一个柜子拆分")+"!");
			return;
		}
		if(StrUtils.isNull(splitCount) || "0".equals(splitCount)) {
			MessageUtils.alert((String)l.m.get("请选择拆分份数(不能为0)")+"!");
			return;
			
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "SELECT f_bus_ship_container_split('cntid="+ids[0]+";splitcount="+splitCount+";usercode="+usercode+"');";
		try {
			this.serviceContext.busCustomsMgrService.busCustomsDao.executeQuery(sql);
			Browser.execClientScript("showmsg()");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/*
	 * 选舱
	 */
	@Action
	public void chooseBook() {
		
		Long containerid = 0L;
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			containerid = 0L;
			
		}else if(ids.length>1){
			MessageUtils.alert((String)l.m.get("请选择单行记录")+"!");
			return;
		}else{
			
			containerid=Long.valueOf(ids[0]);
		}
		
		String sql = "SELECT id FROM bus_shipping WHERE isdelete = FALSE AND jobid =" + this.jobid + ";";
		String shipid = "";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			shipid = m.get("id").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipbookingchoose.xhtml?shipid="+shipid+"&containerid="+containerid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	/*
	 * 取消选舱
	 */
	@Action
	public void cancelBook() {
		
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0||ids.length<1){
			MessageUtils.alert((String)l.m.get("请勾选行")+"!");
			return;
		}
		try {
			serviceContext.busBookingMgrService.cancelBook(ids, AppUtils.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/*
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
	
	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
	
//	//点击完成按钮,自动完成改任务
//	public void passCus(){
//		try{
//				String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid, WorkFlowEnumerateShip.CUS_END, "id");
//				WorkFlowUtil.passProcessBatch(workids);
//				MessageUtils.alert((String)l.m.get("已完成，自动流转到下一个环节"));
//				//正常走流程
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
				//MessageUtils.alert((String)l.m.get("无报关要求"));
			}
		}else{
			showDtlWindow.close();
		}
	}

//	public String ShowMessage() {
//		if (this.jobid != null) {
//			BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao
//					.findOneRowByClauseWhere("jobid = " + this.jobid + "");
//			String ClaimClear = busShipping.getClaimClear();
//			if (ClaimClear == null ||"".equals(ClaimClear)) {
//				return null;
//			}else{
//				return "\n报关要求\n" + ClaimClear + "";
//			}
//		}
//		return null;
//	}
	
	
	public String ShowMessage() {
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid, WorkFlowEnumerateShip.CUS_END, "id");
		String re = "";
		String ins = "";
//		if(workids == null){
//			ins = "";
//		}else{
//			IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//			IWorkItem workItem = workflowSession
//					.findWorkItemById(workids[0]);
//			String processinstranseid = workItem.getTaskInstance().getProcessInstanceId();
//			ins=WorkFlowUtil.getActivityConsByWorkitem(processinstranseid, workids[0]);
//		}
		if (this.jobid != null) {
			try{
				BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao
						.findOneRowByClauseWhere("jobid = " + this.jobid + "");
				String ClaimClear = busShipping.getClaimClear();
				if(!StrUtils.isNull(ClaimClear)&&!StrUtils.isNull(ins)){
					re = ClaimClear+"\n"+ins;
				}else if(!StrUtils.isNull(ClaimClear)&&StrUtils.isNull(ins)){
					re = ClaimClear;
				}else if(StrUtils.isNull(ClaimClear)&&!StrUtils.isNull(ins)){
					re = ins;
				}else{
					re = null;
				}
			}catch(NoRowException e){
				return re;
			}
		}
		return re;
	}
	
	@Action
	public void notice(){
		String sql = "SELECT f_fina_jobs2customer('customid="+selectedRowData.getId()+":userid="+AppUtils.getUserSession().getUserid()+"')";
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
			BusCustoms buscustomer = serviceContext.busCustomsMgrService.busCustomsDao
					.findOneRowByClauseWhere("linktbl = 'bus_customs' AND linkid = "+ selectedRowData.getId());
			FinaJobs finajob = serviceContext.jobsMgrService.finaJobsDao
					.findById(buscustomer.getJobid());
			String url = AppUtils.chaosUrlArs("/scp/pages/module/customs/jobsedit.xhtml")+ "&id=" + finajob.getId();
			AppUtils.openWindow("_showcusJobno_" + finajob.getId(), url);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
