package com.scp.view.module.finance;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.rpreqsumBean", scope = ManagedBeanScope.REQUEST)
public class RpreqSumBean extends GridView {
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String pkVal = AppUtils.getReqParam("id");
			qryMap.put("rpreqid$", pkVal);
		}
	}
	
	@Override
	public void grid_ondblclick() {
	}
}
