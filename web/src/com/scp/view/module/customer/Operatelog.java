package com.scp.view.module.customer;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.customer.operatelogBean", scope = ManagedBeanScope.REQUEST)
public class Operatelog extends GridView {


	@SaveState
	@Accessible
	public String id;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			 id = AppUtils.getReqParam("id");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND ((linkid =ANY  (SELECT " + id +" UNION ALL select id FROM sys_user_assign t WHERE linkid = "+id+" AND linktype ILIKE'%' || TRIM ( 'C' ) || '%'  AND linktype = 'C')) )";
		m.put("qry", qry);
		return m;
	}
	
	
	
}
