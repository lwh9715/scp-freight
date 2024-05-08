package com.scp.view.module.cs;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.cs.csbookingdtlBean", scope = ManagedBeanScope.REQUEST)
public class CsBookingDtlBean extends GridFormView {
	
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
		String qry = (String)m.get("qry");
		qry += "AND bookingid = " + bookingid;
		m.put("qry", qry);
		return m;
	}
	
}
