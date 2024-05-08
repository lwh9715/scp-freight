package com.scp.view.module.airtransport;

import java.util.Calendar;
import java.util.Map;

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
import org.operamasks.faces.user.util.Browser;

import com.scp.base.LMap;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.model.finance.FinaJobs;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
@ManagedBean(name = "pages.module.airtransport.bpmBean", scope = ManagedBeanScope.REQUEST)
public class BpmBean{
	
	@Inject
	protected PartialUpdateManager update;
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;
	
	
	@Bind
	@SaveState
	public String taskRemarks;
	
	@Bind
	@SaveState
	public String type;
	
	@Bind
	@SaveState
	public String taskId;
	
	@Bind
	@SaveState
	public String jobid;
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				jobid = AppUtils.getReqParam("jobid");
				type = AppUtils.getReqParam("type");
				if("submit".equals(type)){
					
				}else{
					String querySql = "select id from _bpm_task where refid = '"+jobid+"' and state <> '3' and state <> '9' ORDER BY taskcreatedtime DESC limit 1";
					Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
					this.taskId = StrUtils.getMapVal(map, "id");
				}
				update.markUpdate(true, UpdateLevel.Data, "type");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Action
	public void submit(){
		try {
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="81433600";
			String processCode = "BPM-6F5F0C51";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid));
			String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+StrUtils.getSqlFormat(taskRemarks)+";taskname=;refno="+finaJobs.getNos()+";refid="+this.jobid+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
			String sub =  sm.get("rets").toString();
			MessageUtils.alert("OK");
			Browser.execClientScript("parent","closeTaskWindows()");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	
	/*
	 * 下一步
	 */
	@Action
	public void next(){
		//alert(nextAssignUser);
		//this.pkVal = getGridSelectId();
		try{
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="81433600";
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
//			if(bpmTask.getState()!=2){//2165 处理两个人同时指派一个流程的情况
//				MessageUtils.alert("该流程已经被其他人指派，请刷新待办列表重新进入指派页面");
//				return;
//			}
			bpmTask.setComments(taskRemarks);
			bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
			bpmTask.setCommentime(Calendar.getInstance().getTime());
			serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
			
			
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			String sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Next;assignuserid="+nextAssignUser+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			//this.editWindow.close();
			Browser.execClientScript("parent","closeTaskWindows()");
			
			Browser.execClientScript("alert('OK')");
			
			//this.refresh();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	/*
	 * 退回上一步
	 */
	@Action
	public void last(){
		try{
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="81433600";
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
//			if(bpmTask.getState()!=2){//2165 处理两个人同时指派一个流程的情况
//				MessageUtils.alert("该流程已经被其他人指派，请刷新待办列表重新进入指派页面");
//				return;
//			}
			LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
			
			bpmTask.setComments(l.get("打回上一步") + ":" + taskRemarks);
			bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
			bpmTask.setCommentime(Calendar.getInstance().getTime());
			
			serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			String sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Last;assignuserid="+nextAssignUser+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			//this.editWindow.close();
			Browser.execClientScript("parent","closeTaskWindows()");
			Browser.execClientScript("alert('OK')");
			//this.refresh();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
}
