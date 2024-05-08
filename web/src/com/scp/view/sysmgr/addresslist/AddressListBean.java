package com.scp.view.sysmgr.addresslist;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.addresslist.addresslistBean", scope = ManagedBeanScope.REQUEST)
public class AddressListBean extends GridView {

	@Action
	public void perAddMgr() {
		String winId = "_UsrAddressMgr";
		String url = "./usraddress.xhtml";
		int width = 1024;
		int height = 740;
		AppUtils.openWindow(winId, url);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter ="\nAND(EXISTS"
		+"\n	(SELECT" 
		+"\n		1 "
		+"\n	FROM sys_custlib x , sys_custlib_user y  "
		+"\n	WHERE y.custlibid = x.id  "
		+"\n		AND y.userid = "+AppUtils.getUserSession().getUserid()
		+"\n		AND x.libtype = 'S'  "
		//+"\n		AND (x.userid = g.salesid  OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linktype = 'C' AND linkid = g.id AND userid = x.userid AND roletype = 'S')))"
		+"\n		AND x.userid = ANY(SELECT g.salesid UNION(SELECT zzz.userid FROM sys_user_assign zzz WHERE zzz.linktype = 'C' AND zzz.linkid = g.id AND roletype = 'S')))"
		+"\nOR EXISTS"
		+"\n	(SELECT" 
		+"\n		1 "
		+"\n	FROM sys_custlib x , sys_custlib_role y " 
		+"\n	WHERE y.custlibid = x.id  "
		+"\n		AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
		+"\n		AND x.libtype = 'S'  "
		//+"\n		AND (x.userid = g.salesid  OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linktype = 'C' AND linkid = g.id AND userid = x.userid AND roletype = 'S'))))";
		+"\n		AND x.userid = ANY(SELECT g.salesid UNION(SELECT zzz.userid FROM sys_user_assign zzz WHERE zzz.linktype = 'C' AND zzz.linkid = g.id AND roletype = 'S'))))";
		m.put("filter", filter);
		return m;
	}
	
	
}
