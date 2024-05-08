package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.stock.wmstransBean", scope = ManagedBeanScope.REQUEST)
public class WmstransBean  extends GridView{
	
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	@Action
	public void add() {
		String winId = "_edit_wmstrans";
		String url = "./wmstransedit.xhtml";
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_wmstrans";
		String url = "./wmstransedit.xhtml?id="+this.getGridSelectId();
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		return m;
	}
}
