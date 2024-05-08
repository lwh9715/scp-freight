package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.unionapBean", scope = ManagedBeanScope.REQUEST)
public class UnionapBean extends GridFormView {

	
	
	@SaveState
	@Accessible
	public Long shipjoin;
	
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			shipjoin=Long.valueOf(id);
		}
	}





	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String sql=" l.shipjoin = "+this.shipjoin;
		m.put("qry2", sql);
		return m;
	}
	
	
	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}





	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}

	@Action
	public void scanReport(){
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/static/arap/CB_DZ.raq";
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}
	private String getArgs() {
		String args="";
		args+="&id="+this.shipjoin;
		return args;
	}
	
	

	

	
}
