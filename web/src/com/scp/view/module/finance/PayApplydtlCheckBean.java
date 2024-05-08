package com.scp.view.module.finance;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaActpayrec;
import com.scp.model.finance.FinaRpreq;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.finance.payapplydtlcheckBean", scope = ManagedBeanScope.REQUEST)
public class PayApplydtlCheckBean extends MastDtlView {

	

	@SaveState
	@Accessible
	public FinaRpreq selectedRowData = new FinaRpreq();
	
	@Bind
	@SaveState
	public String actpayrecno;
	
	@Bind
	@SaveState
	public Long actpayrecid;
	
	@SaveState
	public Long corpidcurrent;
	
	@Bind
	@SaveState
	public boolean ispublic = false;
	
	@Bind
	@SaveState
	public String step = "";
	
	@Bind
	@SaveState
	public String araptype;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			init();
			corpidcurrent = selectedRowData.getCustomerid();
			if(selectedRowData.getAccountid()!=null){
				String sql = "SELECT bankname FROM sys_corpaccount WHERE id= "+selectedRowData.getAccountid();
				Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(m.size()>0){
					Browser.execClientScript("$('#account_input').val('"+ m.get("bankname").toString()+"')");
				}
			}
		}
	}
	

	@Override
	public void init() {
		selectedRowData = new FinaRpreq();
		String id = AppUtils.getReqParam("id");
		araptype = AppUtils.getReqParam("araptype");
		if(!StrUtils.isNull(id)){
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
			
		}
	}

	



	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}


	@Bind
    @SaveState
    @Accessible
	public String workItemId;

	@Bind
	public String dtlContent;
	
	@Bind
	@SaveState
	private String processId = "";
	
	@Bind
    @SaveState
    @Accessible
	public String taskName;
	
	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.reqMgrService.finaRpreqDao.findById(this.mPkVal);
		disableAllButton(this.selectedRowData.getIscheck());
		try {
			FinaActpayrec finaActpayrec = serviceContext.actPayRecService.findActPayByActpayrecid(selectedRowData.getActpayrecid());
			if(finaActpayrec != null){
				actpayrecno = finaActpayrec.getActpayrecno();
				actpayrecid = finaActpayrec.getId();
				update.markUpdate(true, UpdateLevel.Data, "editPanel");
			}
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(UpdateLevel.Data, "mPkVal");
		showSumDtl();
	}



	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void doServiceSave() {
		this.serviceContext.reqMgrService.saveData(selectedRowData);
	}

	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "PA_AP.raq";

	@Action
	public void preview() {
		String rpturl = AppUtils.getRptUrl();
		try{
			String filename = showwmsinfilename;
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/static/payapply/"+filename+"&id="+this.mPkVal;
			AppUtils.openWindow("_print", openUrl);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'PayApply' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		qryMap.put("customerid$", selectedRowData.getCustomerid());
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "clause");
		qry += " rpreqid = " + mPkVal;
		m.put("clause", qry);
		m.put("corpidcurrent", " AND EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid='||a.jobid||';userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent=" + AppUtils.getUserSession().getCorpidCurrent() + "') x WHERE x.id = fa.id)");
		//查看毛利率
		m.put("userid", AppUtils.getUserSession().getUserid());

//		if(!StrUtils.isNull(araptype)&&araptype.equals("AR")){
//			m.put("araptype", " AND a.araptype = 'AR'");
//		}else if(!StrUtils.isNull(araptype)&&araptype.equals("AP")){
//			m.put("araptype", " AND a.araptype = 'AP'");
//		}

		if(!StrUtils.isNull(araptype)&&araptype.equals("AR")){
			m.put("araptype", "\n AND a.araptype = 'AR'");
			m.put("rptype", "\n AND r.rptype = 'R'");
		}else if(!StrUtils.isNull(araptype)&&araptype.equals("AP")){
			String fina_payapply_showar = ConfigUtils.findSysCfgVal("fina_payapply_showar");
			if("Y".equals(fina_payapply_showar)){//付款申请包含应收
			}else{
				m.put("araptype", "\n AND a.araptype = 'AP'");
			}
			m.put("rptype", "\n AND r.rptype = 'P'");
		}

		return m;
	}
	
	@Resource(name="transactionTemplate")
	private TransactionTemplate transactionTemplate;
	
	@Bind
    @SaveState
    @Accessible
	public String processInstanceId;
	
	@Bind
	@SaveState
	@Accessible
	public String currwork;
	
	@Bind
    @SaveState
    @Accessible
    public String workstate;
	
	@Bind
    @SaveState
    @Accessible
	public String taskId;
	
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			sql = "UPDATE fina_rpreq SET ischeck = TRUE ,checktime = NOW(),checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE fina_rpreq SET ischeck = FALSE ,checktime = NULL,checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}
		try {
			serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck?null:selectedRowData.getCheckter());
			selectedRowData.setChecktime(isCheck?null:selectedRowData.getChecktime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		}
	}
	
	@Action
	public void isapproveAjaxSubmit() {
		Boolean isapprove = selectedRowData.getIsapprove();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isapprove) {
			sql = "UPDATE fina_rpreq SET isapprove = TRUE ,approvetime = NOW(),approver = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE fina_rpreq SET isapprove = FALSE ,approvetime = NULL,approver = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}
		try {
			serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsapprove(!isapprove);
			selectedRowData.setApprover(isapprove?null:selectedRowData.getApprover());
			selectedRowData.setApprovetime(isapprove?null:selectedRowData.getApprovetime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isapprove);
		}
	}
	
	
	
	
	@Action
	public void generateRP() {
		try {
			String[] ids = new String[1];
			ids[0] = this.mPkVal.toString();
			serviceContext.reqMgrService.createRPmany(ids , AppUtils.getUserSession().getUsercode());
			if(selectedRowData.getIscheck()&&!StrUtils.isNull(processInstanceId)){//Ischeck = true并且processInstanceId不为空才走流程
//				transactionTemplate.execute(new TransactionCallback() {
//					@Override
//					public Object doInTransaction(TransactionStatus arg0) {
//						if (!StrUtils.isNull(processInstanceId)) {
//							Map<String,Object> m =new HashMap<String,Object>();
//							List<String> actorIds = WorkFlowUtil.findCurrentTaskAssign(processInstanceId , taskId);
//							String remarks = "";
//				 			try {
//				 				//String[] ids = new String[1];
//				 				//ids[0] = mPkVal.toString();
//				 				//long a = serviceContext.reqMgrService.createRPmany(ids);//点审核同时生成收付款
//				 				//if(a>0){//生成收付款成功才走到下一步流程，不然双击行会报错
//				 					m.put("step", "3");
//				 					m.put("return_back", "0");
//									WorkFlowUtil.passProcess(processInstanceId, workItemId, m , actorIds , remarks , "");
//									workstate="已确认";
//					                update.markUpdate(true, UpdateLevel.Data, "workstate");
//									MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//				 				//}
//							} catch (EngineException e) {
//								MessageUtils.alert(e.getErrorMsg());
//								e.printStackTrace();
//							} catch (KernelException e) {
//								MessageUtils.alert(e.getErrorMsg());
//								e.printStackTrace();
//							} catch (Exception e) {
//								MessageUtils.showException(e);
//							}
//						}
//						sendweixin();
//						return null;
//					}
//					
//				});
			}
			refreshMasterForm();
			showActpayrec();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
//	@Action
//	public void notgenerateRP() {
//		try {
//			if(selectedRowData.getIscheck()&&!StrUtils.isNull(processInstanceId)){//Ischeck = true并且processInstanceId不为空才走流程
//				transactionTemplate.execute(new TransactionCallback() {
//					@Override
//					public Object doInTransaction(TransactionStatus arg0) {
//						if (!StrUtils.isNull(processInstanceId)) {
//							Map<String,Object> m =new HashMap<String,Object>();
//							List<String> actorIds = WorkFlowUtil.findCurrentTaskAssign(processInstanceId , taskId);
//							String remarks = "";
//				 			try {
//			 					m.put("return_back", "1");
//			 					m.put("step", "1");
//								WorkFlowUtil.passProcess(processInstanceId, workItemId, m , actorIds , remarks , "");
//								workstate="已确认";
//				                update.markUpdate(true, UpdateLevel.Data, "workstate");
//								MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//							} catch (EngineException e) {
//								MessageUtils.alert(e.getErrorMsg());
//								e.printStackTrace();
//							} catch (KernelException e) {
//								MessageUtils.alert(e.getErrorMsg());
//								e.printStackTrace();
//							} catch (Exception e) {
//								MessageUtils.showException(e);
//							}
//						}
//						return null;
//					}
//					
//				});
//			}
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//			e.printStackTrace();
//		}
//	}
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Action
	public void showSumDtl() {
		String url = "/scp/pages/module/finance/rpreqsum.xhtml?id="+this.mPkVal;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
	}
	
	
	@Bind
	public UIButton save;
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton generateRP;
	
//	@Bind
//	public UIButton notischeck;
	
//	@Bind
//	public UIButton review;
	
//	@Bind
//	public UIButton notgenerateRP;
	
	private void disableAllButton(Boolean ischeck) {
		if(save != null)save.setDisabled(ischeck);
		if(del != null)del.setDisabled(ischeck);
		if(generateRP != null)generateRP.setDisabled(!ischeck);
	}

	@Action
	public void showActpayrec(){
		String url = AppUtils.chaosUrlArs("/scp/pages/module/finance/actpayrecedit.aspx") + "&type=edit&id=" + actpayrecid;
		AppUtils.openWindow("_showActpayrec_" + actpayrecid,url);
	}


	@Override
	public void refresh() {
		super.refresh();
		processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
		String workItemIdStr = (String) AppUtils.getReqParam("workItemId");
		if(!StrUtils.isNull(workItemIdStr)){
//			this.workItemId = workItemIdStr;
//			ProcessInstance processInstance = (ProcessInstance)AppUtils.getWorkflowSession().findProcessInstanceById(processInstanceId);
//			processId  = processInstance.getProcessId();
//			Map<String, Object> m = processInstance.getProcessInstanceVariables();
//			ispublic = (m.get("ispublic")!=null&&((String) m.get("ispublic")).equals("0")?false:true);
//			step =m.get("ispublic")!=null?(String) m.get("step"):"";
//			String setuo = m.get("return_back")!=null?m.get("return_back").toString():"";
//			if(step.equals("1")&&!setuo.equals("1")){
//				generateRP.setDisabled(true);
////				notgenerateRP.setDisabled(true);
////				review.setDisabled(true);
//			}else if(step.equals("2")&&!setuo.equals("1")){
////				notischeck.setDisabled(true);
////				review.setDisabled(true);
//			}
//			if(setuo.equals("1")){
//				generateRP.setDisabled(true);
////				notgenerateRP.setDisabled(true);
////				notischeck.setDisabled(true);
//			}
//			IWorkItem workItem = AppUtils.getWorkflowSession().findWorkItemById(workItemId);
//			currwork =workItem.getTaskInstance().getDisplayName();
//			taskId = workItem.getTaskInstance().getId();
//			taskName = workItem.getTaskInstance().getDisplayName();
//			workstate="等待确认";
//			dtlContent = WorkFlowUtil.getActivityDesc(workItem.getTaskInstance().getActivityId());
//			serviceContext.tFfAssignService().copyAssignFromTemp(processId , processInstanceId , taskName);
		}
	}
	
	@Bind
	public UIDataGrid grid;
	
//	@Action
//	public void confirmFee(){
//		String[] ids = this.grid.getSelectedIds();
//		//String[] ids = null;
//		if (ids == null || ids.length == 0) {
//			MessageUtils.alert("请先勾选要确认的行");
//			return;
//		}
//		try {
//			this.serviceContext.reqMgrService.finishDate(ids , AppUtils.getUserSession().getUsercode() ,true);
//			qryRefresh();
//		} catch (Exception e) {
//			//MsgUtil.showException(e);
//		}
//		
//	}
//	
//	@Action
//	public void unConfirmFee(){
//		String[] ids = this.grid.getSelectedIds();
//		//String[] ids = null;
//		if (ids == null || ids.length == 0) {
//			MessageUtils.alert("请先勾选要确认的行");
//			return;
//		}
//		try {
//			this.serviceContext.reqMgrService.finishDate(ids , AppUtils.getUserSession().getUsercode() ,false);
//			qryRefresh();
//		} catch (Exception e) {
//			//MsgUtil.showException(e);
//		}
//		
//	}
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Bind
	public UIWindow attachmentWindows;
	
	@Action
	public void showAttachment(){
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid="+ this.mPkVal+"&code=payapply");
		attachmentWindows.show();
	}
	
	@Action
	public void isapproverAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIsapprove();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			sql = "UPDATE fina_rpreq SET isapprove = TRUE ,approvetime = NOW(),approver = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE fina_rpreq SET isapprove = FALSE ,approvetime = NULL,approver = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}
		try {
			serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsapprove(!isCheck);
			selectedRowData.setApprover(isCheck?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setApprovetime(isCheck?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		}
	}
	
	@Action
	public void isprintAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIsprint();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			sql = "UPDATE fina_rpreq SET isprint = TRUE ,printtime = NOW(),printer = '"+updater+"', printcount = COALESCE(printcount,0) + 1 WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE fina_rpreq SET isprint = FALSE ,printtime = NULL,printer = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}
		try {
			serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsprint(!isCheck);
			selectedRowData.setPrinter(isCheck?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setPrinttime(isCheck?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		}
	}
}
