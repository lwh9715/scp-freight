package com.scp.view.module.report.qry;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.report.qry.dtlIFrameGoodsBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameGoodsBean extends GridView{
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","arggoodsidJsVar.setValue('');");
		Browser.execClientScript("parent.window","setGoodsIds('');");
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
}
