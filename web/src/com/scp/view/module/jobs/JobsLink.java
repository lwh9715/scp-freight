package com.scp.view.module.jobs;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.jobs.jobslinkBean", scope = ManagedBeanScope.REQUEST)
public class JobsLink extends GridView {
	
	@Bind
	@SaveState
	public String jobid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)) {
				jobid = id;
			}
		}
	}
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("jobid", jobid);
		return map;
	}

	

	
}
