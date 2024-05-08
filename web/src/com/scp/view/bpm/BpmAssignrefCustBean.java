package com.scp.view.bpm;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "bpm.bpmassignrefcustBean", scope = ManagedBeanScope.REQUEST)
public class BpmAssignrefCustBean extends GridView {
	
	
	@Bind
	@SaveState
	public String libid = "-1";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("libid");
			if(!StrUtils.isNull(id)) {
				libid = id;
			}
		}
	}
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(groupname)){
			qry += "\nAND EXISTS(SELECT 1 FROM sys_role x WHERE x.name LIKE '"
				+groupname+"%' AND EXISTS(SELECT 1 FROM sys_userinrole WHERE userid = t.id AND roleid = x.id))";
			map.put("qry", qry);
		}
		map.put("libid", libid);
		return map;
	}

	@Action
	public void choice() {
		String[] ids = this.grid.getSelectedIds();
		this.serviceContext.bpmAssignRefService.addAssignUser(libid,ids);
		this.alert("OK!");
		this.grid.reload();
		Browser.execClientScript("freashaggin()");
	}
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapFrom = new HashMap<String, Object>();
	
	@Action
	public void clearQryKey(){
		if(qryMapFrom != null){
			qryMapFrom.clear();
			update.markUpdate(true, UpdateLevel.Data, "gridFromPanel");
		}
	}
	
	@Bind
	@SaveState
	public String groupname;
	
}
