package com.scp.view.module.oa.ff;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.OaLeaveapplication;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@ManagedBean(name = "pages.module.oa.ff.vacateflowBean", scope = ManagedBeanScope.REQUEST)
public class VacateFlowBean {
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;

	@Inject
	protected PartialUpdateManager update;

	@SaveState
	public String processInstanceId;

	@SaveState
	public String workItemId;
	
	@Bind
	@SaveState
	public String jobnos;
	
	@Bind
	@SaveState
	public String leavelappid;
	
	@Bind
	@SaveState
	public String day;

	@Bind
	public Long assign;
	
	@Bind
	@SaveState
	@Accessible
	public OaLeaveapplication selectedRowData = new OaLeaveapplication();

	@Bind
	@SaveState
	@Accessible
	public String currwork;

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();

		}
	}

	@Action
	public void query() {

	}

	public void init() {
		processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
		workItemId = (String) AppUtils.getReqParam("workItemId");
		jobnos = (String) AppUtils.getReqParam("sn");
		if (!StrUtils.isNull(workItemId)) {
//			IWorkItem workItem = AppUtils.getWorkflowSession().findWorkItemById(
//					workItemId);
			leavelappid = AppUtils.getReqParam("id");
			update.markUpdate(true, UpdateLevel.Data, "leavelappid");
			getReason(leavelappid);
		}

		

	}
	
	public void getReason(String id){
//		
//		String sql = "SELECT reason FROM oa_leaveapplication WHERE isdelete = FALSE AND id = "+id;
//		 Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		   currwork =(String)map.get("reason");
//		  update.markUpdate(true, UpdateLevel.Data, "currwork");
		selectedRowData = this.serviceContext.oaLeaveapplicationService().oaLeaveapplicationDao.findById(Long.valueOf(id));
	}

	@Action
	public void pass() {
		List actorIds = new ArrayList();
		if (this.assign == null) {

		} else {
			actorIds.add(AppUtils.getUserCode(this.assign));
		}
		if (!StrUtils.isNull(processInstanceId)) {
//			Map<String, Object> m = new HashMap<String, Object>();
//			String type = queryAppproMgr();
//			if(!StrUtils.isNull(type)){
//				m.put("datechange",type);
//			}
//			try {
//				WorkFlowUtil.passProcess(processInstanceId, workItemId, m,
//						actorIds);
//				MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
		}
	}

	@Action
	public void notpass() {
		if (!StrUtils.isNull(processInstanceId)) {
//			Map<String, Object> m = new HashMap<String, Object>();
//			// 只要单号不变不需要 进行update
//			// m.put("id",this.selectedRowData.getId());// 设置id
//			// m.put("sn",this.selectedRowData.getNos());// 设置流水号
//			try {
//				WorkFlowUtil.notPassProcess(processInstanceId, workItemId, m);
//				MessageUtils.alert("NotPass OK");
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
		}
	}
	
	/**
	 * 每次判断是否经理审批节点
	 */
	public String queryAppproMgr(){
		String sql = "SELECT ( CASE WHEN EXISTS(SELECT 1 FROM _wf_jobs t WHERE t.processcreatorid = '"+AppUtils.getUserSession().getUsercode()+"' AND t.refid = '"+leavelappid+"'  AND t.processid like 'LeavelApp%' AND (t.workitemstate = 0 OR t.workitemstate = 1) AND" + 
					 "\n activityid = 'LeavelApp.Activity71') THEN '0' ELSE '1' END)AS type";
		 Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		 String type = (String)map.get("type");
		 if(type.equals("0")){
			 return getType();
		 }else{
			 return "";
		 }
	}
	
	public String getType(){
		String sql = "SELECT days FROM oa_leaveapplication WHERE isdelete = FALSE AND id = "+this.leavelappid;
		Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		BigDecimal days = (BigDecimal)map.get("days");
		BigDecimal bd = new BigDecimal("3");
		if(days.compareTo(bd)==-1){
			return "0";
		}else{
			return "1";
		}
	}
}
