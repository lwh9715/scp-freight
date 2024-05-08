package com.scp.view.module.price;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.pricehisBean", scope = ManagedBeanScope.REQUEST)
public class PricehisBean extends GridView{
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
	}
}