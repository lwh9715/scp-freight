package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.senddocBean", scope = ManagedBeanScope.REQUEST)
public class SendDocBean extends GridView {
	
	
	

	@Action
	public void add() {
		String winId = "_senddoc";
		String url = "./senddocedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_senddoc";
		String url = "./senddocedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void docDefine() {
		String url = "./senddocdefine.xhtml";
		AppUtils.openWindow("_docDefine", url );
	}
	
}
