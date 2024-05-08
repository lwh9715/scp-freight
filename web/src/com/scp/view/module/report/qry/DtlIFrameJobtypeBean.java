package com.scp.view.module.report.qry;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.report.qry.dtlIFrameJobtypeBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameJobtypeBean extends GridView{
	
	@Action
	public void grid_onselectionchange(){
		refershs();
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	 
	private void refershs() {
		String args = "";
		if (this.grid.getSelectedIds().length > 0
				&& this.grid.getSelectedIds() != null) {
			args = StrUtils.array2List(this.grid.getSelectedIds());
		}
		Browser.execClientScript("parent.window","setJobtypeIds('"+args+"');");
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argjobtypeJsVar.setValue('');");
		Browser.execClientScript("parent.window","setJobtypeIds('');");
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
}
