package com.scp.view.bpm;

import java.util.List;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.LMap;
import com.scp.base.LMapBase.MLType;
import com.scp.model.bpm.BpmAssign;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmProcessinstance;
import com.scp.model.bpm.BpmTask;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

/**
 * 
 * 待办工单处理页面(编辑界面) 
 * @author neo
 */
@ManagedBean(name = "bpm.bpmtaskdoneshowBean", scope = ManagedBeanScope.REQUEST)
public class BpmTaskDoneShowBean extends FormView{
	
	@Bind
	@SaveState
	public String tips;
	
	@Bind
	@SaveState
	public String taskRemarks;
	
	
	@Bind
	@SaveState
	public String taskId;
	
	
	@Bind
	@SaveState
	public String src;
	
	@SaveState
	public boolean formatLinkBPMNo = false;//1832 待办打开时，区分是链接打开还是双击打开
	
	
	
	@Bind
	public UIIFrame todoIframe;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			taskId = AppUtils.getReqParam("taskId");
			src = AppUtils.getReqParam("src");
			try {
				formatLinkBPMNo = Boolean.parseBoolean(AppUtils.getReqParam("formatLink"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String url = "";
			
			String araptype = AppUtils.getReqParam("araptype");
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			try {
				formatLinkBPMNo = Boolean.parseBoolean(AppUtils.getReqParam("formatLink"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String formview = "";
			List<BpmAssign> lists = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere(" isdelete = FALSE AND process_id = '"+bpmTask.getProcessid()+"' AND taskname = '"+bpmTask.getTaskname()+"'");
			if(lists != null && lists.size() > 0){
				formview = lists.get(0).getFormview();
			}
			
			if(!StrUtils.isNull(formview)){
				if(formview.indexOf("jobid")>0){
					formview = formview+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit&";
				}else if(formview.indexOf("?")>0){
					formview = formview+"&id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit";
				}else{
					formview = formview+"?id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit";
				}
				url = formview;
			}else{
				//url = "bpmpayapplydtlcheck.xhtml"+"?araptype="+araptype+"&id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId();
			}
		
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			tips = bpmProcess.getNamec() + "-->" + bpmTask.getTaskname() + "-->" + bpmProcessinstance.getNos();
			try {
				if(AppUtils.getUserSession().getMlType().equals(MLType.en)){
					LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
					tips = bpmProcess.getNamee() + "-->" + l.get(bpmTask.getTaskname()) + "-->" + bpmProcessinstance.getNos();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StrUtils.isNull(url))url="/scp/common/blank.html";
			
			//System.out.println("url:"+url);
			todoIframe.load(url);
			
			this.update.markUpdate(UpdateLevel.Data, "taskId");
		}
	}
}
