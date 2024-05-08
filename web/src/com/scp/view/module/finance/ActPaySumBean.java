package com.scp.view.module.finance;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.actpaysumBean", scope = ManagedBeanScope.REQUEST)
public class ActPaySumBean extends GridView {
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String pkVal = AppUtils.getReqParam("id");
			qryMap.put("actpayrecid$", pkVal);
		}
	}
	
	@Override
	public void grid_ondblclick() {
	}
}
