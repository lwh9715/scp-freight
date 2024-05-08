package com.scp.view.module.del.dispatch;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.del.dispatch.loadingcheckBean", scope = ManagedBeanScope.REQUEST)
public class LoadingCheckBean extends GridView {
	
	@Action
	public void add() {
		String winId = "_Loadingdtl";
		String url = "./loadingdtlcheck.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_Loadingdtl";
		String url = "./loadingdtlcheck.faces?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
}
