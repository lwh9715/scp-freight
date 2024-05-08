package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipcostBean", scope = ManagedBeanScope.REQUEST)
public class ShipCostBean extends GridView {
	
	
	

	@Action
	public void add() {
		String winId = "_edit_ship_cost";
		String url = "./shipcostedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_ship_cost";
		String url = "./shipcostedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
}
