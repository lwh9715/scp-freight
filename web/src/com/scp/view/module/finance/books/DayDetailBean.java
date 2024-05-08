package com.scp.view.module.finance.books;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.daydetailBean", scope = ManagedBeanScope.REQUEST)
public class DayDetailBean extends GridView {
	
	@Bind
	@SaveState
	public String args;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String year = AppUtils.getReqParam("year");
			String datefm = AppUtils.getReqParam("datefm");
			String dateto = AppUtils.getReqParam("dateto");
			String actcode = AppUtils.getReqParam("actcode");
			String currency = AppUtils.getReqParam("currency");
			String comid = AppUtils.getReqParam("comid");

			args = "'actsetid=" + AppUtils.getUserSession().getActsetid()
					+ ";year=" + year + ";datefm=" + datefm + ";dateto=" + dateto
					+ ";currency=" + currency + ";actcode=" + actcode + "'";
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("filter", args);
		return map;
	}

}
