package com.scp.view.bpm;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "bpm.bpmlogsBean", scope = ManagedBeanScope.REQUEST)
public class BpmLogsBean extends GridView{
	
	
	@Bind
	@SaveState
	public String taskid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.gridLazyLoad = false;
			taskid = AppUtils.getReqParam("taskid");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter  = "\nAND processinstance_id = ANY(SELECT x.processinstanceid FROM bpm_task x WHERE x.id = "+taskid+")";
		map.put("filter", filter);
		return map;
	}
	
	
	@Override
	public void grid_ondblclick(){
		
	}
	
}
