package com.scp.view.module.cs;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.cs.csbookingfeeBean", scope = ManagedBeanScope.REQUEST)
public class CsBookingFeeBean extends GridFormView {
	
	@SaveState
	public String bookingid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		bookingid = AppUtils.getReqParam("bookingid");
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		m.put("qry", "isdelete = FALSE AND bookingid = " + bookingid);
		return m;
	}
	
}
