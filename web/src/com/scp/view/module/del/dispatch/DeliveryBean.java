package com.scp.view.module.del.dispatch;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.del.dispatch.deliveryBean", scope = ManagedBeanScope.REQUEST)
public class DeliveryBean extends GridView {
	
	@Action
	public void add() {
		String winId = "deliverydtl";
		String url = "./deliverydtl.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "deliverydtl";
		String url = "./deliverydtl.faces?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
}
