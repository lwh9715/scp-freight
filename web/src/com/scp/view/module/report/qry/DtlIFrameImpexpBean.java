package com.scp.view.module.report.qry;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameImpexpBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameImpexpBean extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	 
	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argimpexpJsVar.setValue('');");
		Browser.execClientScript("parent.window","setImpexpIds('');");
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
}
