package com.scp.view.bpm;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmAssign;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "bpm.bpmtaskassignBean", scope = ManagedBeanScope.REQUEST)
public class BpmAssignBean extends GridFormView {
	
	@Bind
    @SaveState
    @Accessible
	public String processInstanceId;

	@Bind
    @SaveState
    @Accessible
	public String processId;
	
	@Bind
    @SaveState
    @Accessible
	public String workItemId;
	
	@Bind
    @SaveState
    @Accessible
	public String taskId;
	
	@Bind
    @SaveState
	public String taskName;
	
	@Bind
	@SaveState
	@Accessible
	public String assignid;
	

	@SaveState
	@Accessible
	public BpmAssign selectedRowData = new BpmAssign();
	
	
	@Bind
	@SaveState
	public String actionJsText;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			processId = (String)AppUtils.getReqParam("processId");
			processInstanceId = (String)AppUtils.getReqParam("processInstanceId");
			workItemId = (String)AppUtils.getReqParam("workItemId");
			taskId = (String)AppUtils.getReqParam("taskId");
			taskName = (String)AppUtils.getReqParamDealChinese("taskName");
			assignid = (String)AppUtils.getReqParam("Assignid");
			
			//qryMap.put("taskid", taskId);
			//qryMap.put("userid$", AppUtils.getUserSession().getUserid());
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			
			/*if(!AppUtils.isDebug){
				actionJsText = "eventJsVar.hide();";
				this.update.markUpdate(UpdateLevel.Data, "actionJsText");
			}*/
		}
	}
	
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String sql = 
					"\nSELECT * " +
					"\nFROM jsonb_populate_recordset(null::bpm_model_json_type,(SELECT gojson->>'nodeDataArray' FROM bpm_process WHERE id = "+processId+")::jsonb)" +
					"\nWHERE text NOT LIKE 'main%'";
			List<Map> m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map i:m){
				SelectItem selectItem = new SelectItem(i.get("text"), i.get("text").toString());
				lists.add(selectItem);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		String filter = "";
		filter += "\nAND process_id = '" + processId + "'";
		Map m = super.getQryClauseWhere(queryMap);
		m.put("filter", filter);
		return m;
	}

	@Override
	public void add() {
		selectedRowData = new BpmAssign();
		selectedRowData.setUserid(AppUtils.getUserSession().getUserid());
		selectedRowData.setTousercode("");
		selectedRowData.setTaskId(taskId);
		selectedRowData.setTaskname(taskName);
		selectedRowData.setProcess_id(Long.valueOf(processId));
		Browser.execClientScript("$('#incarico_input').val('')");
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.bpmAssignService.bpmAssignDao.findById(this.pkVal);
		String usercode = selectedRowData.getTousercode();
		Browser.execClientScript("$('#incarico_input').val('"+usercode+"')");
	}


	@Override
	protected void doServiceSave() {
//		if(selectedRowData.getUsercode() != null && selectedRowData.getUsercode() != ""){
			serviceContext.bpmAssignService.saveData(selectedRowData);
			this.grid.reload();
			//this.alert("OK");
//		}else{
//			this.alert("指派人必填！");
//		}
		
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0){
			alert("请选择!");
			return;
		}
		try{
			serviceContext.bpmAssignService.removeDate(ids);
		}catch (Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		refresh();
	}
	
	@Bind
	public UIWindow showEventWindow;
	
	@Bind
	public UIIFrame eventIframe;

	@Bind
	public UIWindow assignrefWindow;
	
	@Bind
	public UIIFrame assignrefIframe;
	
	@Action
	public void assignref(){
		this.pkVal = getGridSelectId();
		if(!(pkVal > -1)){
			alert("请选择一行");
			return;
		}
		selectedRowData = serviceContext.bpmAssignService.bpmAssignDao.findById(this.pkVal);
		String taskname = selectedRowData.getTaskname();
		Long processid = selectedRowData.getProcess_id();
		String args;
		try {
			args = java.net.URLEncoder.encode(taskname,"utf-8");
			assignrefIframe.load("./bpmassignrefedit.xhtml?assignid="+selectedRowData.getId());
			assignrefWindow.show();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void event(){
		this.pkVal = getGridSelectId();
		if(!(pkVal > -1)){
			alert("请选择一行");
			return;
		}
		selectedRowData = serviceContext.bpmAssignService.bpmAssignDao.findById(this.pkVal);
		String taskname = selectedRowData.getTaskname();
		Long processid = selectedRowData.getProcess_id();
		String args;
		try {
			args = java.net.URLEncoder.encode(taskname,"utf-8");
			eventIframe.load("./bpmworkitem.xhtml?processid="+processid+"&taskname="+args);
			showEventWindow.show();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void copyadd() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0){
			alert("请选择!");
			return;
		}
		try{
			serviceContext.bpmAssignService.addcopy(ids);
		}catch (Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		refresh();
	}

	@Action
	public void automaticadd(){
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String sql = "SELECT f_bpm_assign_autofill('processid="+processId+"')";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			refresh();
		}catch(NoRowException e){
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIWindow editBatchWindo;
	
	@Action
	public void batchUpdate() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0){
			alert("请选择!");
			return;
		}
		batchisalert = "";
		batchalertafter = "";
		batchaftertype = "";
		batchurl = "";
		batchformview = "";
		update.markUpdate(true, UpdateLevel.Data, "eb");
		editBatchWindo.show();
	}
	
	@Bind
	public String batchisalert;
	
	@Bind
	public String batchalertafter;
	
	@Bind
	public String batchaftertype;
	
	@Bind
	public String batchurl;
	
	@Bind
	public String batchformview;
	
	
	@Action
    private void saveBatch() {
		String[] ids = this.grid.getSelectedIds();
		try {
			if(batchisalert != ""){
				serviceContext.bpmAssignService.updateBatch(ids , "isalert" ,batchisalert , AppUtils.getUserSession().getUsercode());
			}
			if(batchalertafter != ""){
				serviceContext.bpmAssignService.updateBatch(ids , "alertafter" ,batchalertafter , AppUtils.getUserSession().getUsercode());
			}
			if(batchaftertype != ""){
				serviceContext.bpmAssignService.updateBatch(ids , "aftertype" ,batchaftertype , AppUtils.getUserSession().getUsercode());
			}
			if(batchurl != ""){
				serviceContext.bpmAssignService.updateBatch(ids , "url" ,batchurl , AppUtils.getUserSession().getUsercode());
			}
			if(batchformview != ""){
				serviceContext.bpmAssignService.updateBatch(ids , "formview" ,batchformview , AppUtils.getUserSession().getUsercode());
			}
			
			MessageUtils.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
