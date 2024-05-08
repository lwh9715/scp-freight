package com.scp.view.bpm;

import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.bpm.BpmWorkItemDao;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "bpm.bpmworkitemBean", scope = ManagedBeanScope.REQUEST)
public class BpmworkitemBean extends GridFormView{

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.selectedRowData = serviceContext.bpmWorkItemService.bpmWorkItemDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		serviceContext.bpmWorkItemService.saveData(selectedRowData);
		
	}

	@SaveState
	public BpmWorkItem selectedRowData = new BpmWorkItem();
	
	@Resource
	public BpmWorkItemDao bpmWorkItemDao;
	
	
	
    @SaveState
	public String processId;
    
    @SaveState
	public String taskname;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			processId = AppUtils.getReqParam("processid");
			taskname = AppUtils.getReqParam("taskname");
//			try {
//				taskname=new String(taskname.getBytes("ISO-8859-1"),"UTF-8");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}  
		}
	}
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "";
		filter += "\nAND process_id = "+Long.parseLong(processId)+"";
		String filter2 = "";
		filter2 += "\nAND taskname = '"+taskname+"'";
		m.put("filter", filter);
		m.put("filter2", filter2);
		return m;
	}
	
	@Override
	public void add() {
		super.add();
		selectedRowData = new BpmWorkItem();
		selectedRowData.setProcess_id(Long.parseLong(processId));
		selectedRowData.setTaskname(taskname);
	}
	
	@Action
	public void edit(){
		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Override
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.bpmWorkItemService.removeDate(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
}
