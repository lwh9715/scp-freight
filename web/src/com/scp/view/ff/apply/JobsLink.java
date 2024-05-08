package com.scp.view.ff.apply;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.ff.apply.jobslinkBean", scope = ManagedBeanScope.REQUEST)
public class JobsLink extends GridView {
	
	@Bind
	@SaveState
	public String jobids = "-1";
	
	
	@Bind
	@SaveState
	public String type = "jobdate";
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			String id = AppUtils.getReqParam("id");
			String type = AppUtils.getReqParam("type");
			jobids = id;
			update.markUpdate(true, UpdateLevel.Data, "jobids");
		}
	}
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		String jobid = jobids.split(",")[0];
		FinaJobs job = serviceContext.jobsMgrService.finaJobsDao.findByjobId(Long.valueOf(jobid));
		String usercode =  AppUtils.getUserSession().getUsercode();
		Long customerid = job.getCustomerid();
		Map map = super.getQryClauseWhere(queryMap);
		map.put("customerid", customerid);
		map.put("usercode", usercode);
		map.put("saleid", "ANY(SELECT DISTINCT saleid FROM fina_jobs WHERE id IN ("+jobids+"))");
		
		if("jobdate".equals(type)){
			map.put("date_ym", ",to_char(jobdate,'yyyy-MM') AS date_ym");
		}else if("eta".equals(type)){
			map.put("date_ym", ",(SELECT to_char(y.eta,'yyyy-MM') FROM bus_shipping as y WHERE y.isdelete = FALSE AND y.jobid = a.id) AS date_ym");
		}else{
			map.put("date_ym", ",(SELECT to_char(y.etd,'yyyy-MM') FROM bus_shipping as y WHERE y.isdelete = FALSE AND y.jobid = a.id) AS date_ym");
		}
		
		return map;
	}
	
	@Action
	public void refreshjobdate(){
		type = "jobdate";
		this.refresh();
	}
	
	@Action
	public void refreshetd(){
		type = "etd";
		this.refresh();
	}
	
	@Action
	public void refresheta(){
		type = "eta";
		this.refresh();
	}

}
