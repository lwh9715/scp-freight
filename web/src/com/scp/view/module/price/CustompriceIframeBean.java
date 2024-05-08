package com.scp.view.module.price;


import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;

import com.scp.service.price.PricefclpubruleMgrService;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.custompriceIframeBean", scope = ManagedBeanScope.REQUEST)
public class CustompriceIframeBean extends EditGridFormView{
	
	@ManagedProperty("#{pricefclpubruleMgrService}")
	public PricefclpubruleMgrService pricefclpubruleMgrService;
	 
	@Override
	protected void update(Object obj) {
		pricefclpubruleMgrService.updateBatchEditGrid(obj);
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
    
}
