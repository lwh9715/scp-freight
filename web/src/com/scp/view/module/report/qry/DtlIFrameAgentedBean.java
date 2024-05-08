package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameAgentedBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameAgentedBean extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	 

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argAgentdedidJsVar.setValue('');");
		Browser.execClientScript("parent.window","setAgented('');");
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		return m;
	}
	
	
	
}
