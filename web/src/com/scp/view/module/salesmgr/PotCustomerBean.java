package com.scp.view.module.salesmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.salesmgr.potcustomerBean", scope = ManagedBeanScope.REQUEST)
public class PotCustomerBean extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		// TODO Auto-generated method stub
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}

	@Action
	public void add() {
		String winId = "_edit_potcustomer";
		String url = "./potcustomeredit.xhtml";
		AppUtils.openWindow(winId, url);
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_potcustomer";
		String url = "./potcustomeredit.xhtml?id=" + this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
//		this.qryMap.put("iscarrier$", "false");
//		this.qryMap.put("isagent$", "false");
//		this.qryMap.put("istruck$", "false");
//		this.qryMap.put("iscustom$", "false");
//		this.qryMap.put("issupplier$", "false");
//		this.qryMap.put("iscooperative$", "false");
		
		Map map = super.getQryClauseWhere(queryMap);
		
		String linkSalesSql = 
			  "\n	OR EXISTS"
			+ "\n				(SELECT "
			+ "\n					1 "
			+ "\n				FROM sys_custlib x , sys_custlib_user y  "
			+ "\n				WHERE y.custlibid = x.id  "
			+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
			+ "\n					AND x.libtype = 'S'  "
			+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = a.salesid AND z.isdelete = false) " //关联的业务员的单，都能看到
			+ ")"
			+ "\n	OR EXISTS"
			+ "\n				(SELECT "
			+ "\n					1 "
			+ "\n				FROM sys_custlib x , sys_custlib_role y  "
			+ "\n				WHERE y.custlibid = x.id  "
			+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
			+ "\n					AND x.libtype = 'S'  "
			+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = a.salesid) " //组关联业务员的单，都能看到
			+ ")"
			;
		
		String filterByInputerOrUpdaterConfig = ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2link");
		String filterByInputOrUpdate = "\nOR inputer = '"+AppUtils.getUserSession().getUsercode()+"'" +"\nOR updater = '"+AppUtils.getUserSession().getUsercode()+"'";
		
		Long userid = AppUtils.getUserSession().getUserid();
		map.put("filter", " AND (salesid = "+userid
				+ ("Y".equals(filterByInputerOrUpdaterConfig)?filterByInputOrUpdate:"")
				+"\n" + ("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2assign"))?"OR EXISTS(SELECT 1 FROM _sys_user_assign y WHERE y.linkid=a.id AND linktype='C' AND y.userid="+userid+")":"")
				//+"\n" + ("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2saleslink"))?linkSalesSql:"")
				+"\n" + linkSalesSql
				+"\nOR EXISTS(SELECT 1 FROM _sys_user_assign y WHERE y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid="+userid+"))");
		return map;
	}
}
