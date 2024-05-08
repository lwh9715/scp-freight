package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameAgentBean", scope = ManagedBeanScope.REQUEST)
public class IFrameAgent extends GridView {
	
	@SaveState
	public boolean isOperations = false;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String isOperations = AppUtils.getReqParam("isOperation");
 			if(isOperations.equals("true")){
 				this.isOperations = true;
			}
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argsClienteleJsVar.setValue('');");
		Browser.execClientScript("parent.window","setClienteleiIds('');");
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
