package com.scp.view.sysmgr.timetask;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysTimeTaskLog;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.timetask.systimetasklogBean", scope = ManagedBeanScope.REQUEST)
public class SysTimeTaskLogBean extends GridFormView {
	
	@SaveState
	@Bind
	public String timetaskid = "-1";
	
	@SaveState
	public SysTimeTaskLog data;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)){
				this.timetaskid = id;
			}
			update.markUpdate(true,UpdateLevel.Data,"timetaskid");
		}
	}
	
	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysTimeTaskService.sysTimeTaskLogDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		//serviceContext.sysTimeTaskService.saveDataTask(data);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if(StrUtils.isNull(timetaskid))timetaskid = "-1";
		m.put("filter", "\nAND (timetaskid = " + timetaskid + "  OR -1 = "+timetaskid+")");
		return m;
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
				serviceContext.sysTimeTaskService.removeDateTaskLog(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	
	@Action
	public void delAll(){
		try {
			serviceContext.sysTimeTaskService.delAllLogs(timetaskid);
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
}
