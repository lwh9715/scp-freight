package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.jobsplitBean", scope = ManagedBeanScope.REQUEST)
public class Jobsplit extends GridView {
	
	
	
	@Bind
	@SaveState
	public Boolean iscare = false;
	

	@Action
	public void add() {
		String winId = "_editsplit_jobs";
		String url = "./jobseditsplit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_editsplit_jobs";
		String url = "./jobseditsplit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if (!iscare) {
			m.put("icare", "1=1");
		} else {
			String sql="(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="+AppUtils.getUserSession().getUserid()+")";
			sql+="OR t.inputer ='"+AppUtils.getUserSession().getUsercode()+"' OR t.updater = '"+AppUtils.getUserSession().getUsercode()+"')";
			m.put("icare",sql);
			
		}
		return m;
	}
	
	
	
}
