package com.scp.view.module.finance.books;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.detailgridBean", scope = ManagedBeanScope.REQUEST)
public class DetailGridBean extends GridView {
	
	
	@Bind
	@SaveState
	public String args;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String year = AppUtils.getReqParam("year");
			String periodfm = AppUtils.getReqParam("periodfm");
			String periodto = AppUtils.getReqParam("periodto");
			String actcode = AppUtils.getReqParam("actcode");
			String currency = AppUtils.getReqParam("currency");
			String astid = AppUtils.getReqParam("astid");

			args = "'actsetid=" + AppUtils.getUserSession().getActsetid()
					+ ";year=" + year + ";periodfm=" + periodfm + ";periodto=" + periodto
					+ ";currency=" + currency + ";actcode=" + actcode + ";astid=" + astid + "'";
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("filter", args);
		return map;
	}

}
