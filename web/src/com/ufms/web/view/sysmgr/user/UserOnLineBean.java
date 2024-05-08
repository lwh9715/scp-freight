package com.ufms.web.view.sysmgr.user;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.scp.base.AppSessionLister;
import com.scp.util.AppUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.web.BaseView;
import com.ufms.base.web.GridView;

@WebServlet("/sysmgr/user/useronline")
@ManagedBean(name = "pages.module.data.useronlineBean" ,tableName="")
public class UserOnLineBean extends GridView {
	
	@Override
	public String getGridSql(){
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("\nSELECT ");
		sqlBuilder.append("\n	(SELECT x.abbr FROM sys_corporation x WHERE x.isdelete = FALSE AND x.id = u.corpid) AS corpor");
		sqlBuilder.append("\n	,*");
		sqlBuilder.append("\nFROM sys_useronline o , sys_user u");
		sqlBuilder.append("\nWHERE $qry$");
		sqlBuilder.append("\nAND o.userid = u.id");
		sqlBuilder.append("\nORDER BY logintime DESC");
		sqlBuilder.append("\nLIMIT $limit$ OFFSET $offset$");
		
		return sqlBuilder.toString();
	}
	
	@Override
	public String getGridCountSql(){
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("\nSELECT ");
		sqlBuilder.append("\n	count(*) AS count");
		sqlBuilder.append("\nFROM sys_useronline o , sys_user u");
		sqlBuilder.append("\nWHERE $qry$");
		sqlBuilder.append("\nAND o.userid = u.id");

		return sqlBuilder.toString();
	}
	
	@Action(method="getOnLIneInfo")
	public BaseView getOnLIneInfo(){
		BaseView baseView = new BaseView();
		baseView.setSuccess(true);
		Map onlineSessionMap  = AppSessionLister.onlineSessionMap;
		int count  = onlineSessionMap.size();
		
		baseView.add("onlinenumber",count);
		baseView.add("sysuser",AppUtils.getSysUser());
		return baseView; 
	}
}

