package com.scp.view.module.customer;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.custctrlBean", scope = ManagedBeanScope.REQUEST)
public class CustCtrlBean extends GridFormView {

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}

//	@Override
//	protected String findQryCountSql() {
//		String queryCountSql = "SELECT COUNT(*) FROM sys_user u WHERE "+qrySql+" AND isadmin = 'N' AND valid = 'Y';";
//		return queryCountSql;
//	}
//
//
//	@Override
//	protected String findQrySql(int start, int limit) {
//		String querySql = 
//			"\nSELECT " +
//			"\n(CASE WHEN EXISTS (SELECT 1 FROM sys_corpctrl x WHERE x.userid = u.id AND x.customerid = "+customerid+") THEN TRUE ELSE FALSE END) AS chooseflag" +
//			"\n,* " +
//			"\nFROM sys_user u " +
//			"\nWHERE isadmin = 'N' " +
//			"\nAND valid = 'Y' " +
//			"\nORDER BY code" +
//			"\nlimit "+limit+" offset "+start+"";
//		return querySql;
//	}
	
	
//	@Override
//	protected void doSave() {
//		String dmlSql = 
//			"DELETE FROM sys_corpctrl WHERE customerid = " + customerid +";";
//		String[] ids = this.grid.getSelectedIds();
//		for (String userid : ids) {
//			dmlSql += "\nINSERT INTO sys_corpctrl(id,customerid,userid) VALUES(getid(),"+customerid+","+userid+");";
//		}
//		try {
//			AppDaoUtil.execute(dmlSql);
//			MsgUtil.alert("OK!");
//			this.grid.reload();
//			this.clientGrid.reload();
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//		}
//	}
}
