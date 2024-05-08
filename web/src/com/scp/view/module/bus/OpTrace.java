package com.scp.view.module.bus;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.bus.optraceBean", scope = ManagedBeanScope.REQUEST)
public class OpTrace extends GridView {
	
	@SaveState
	public String jobid;
	
	@SaveState
	public String linkid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			jobid = AppUtils.getReqParam("jobid");
			linkid = AppUtils.getReqParam("linkid");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map map = super.getQryClauseWhere(queryMap);
		if(StrUtils.isNull(jobid)) {
			map.put("filter", "\nAND linkid = " + linkid);
		}else{
			map.put("filter", "\nAND jobid = " + jobid);
		}
		return map;
	}
	
	
	
}
