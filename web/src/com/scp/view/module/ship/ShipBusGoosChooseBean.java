package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipbusgoodschooseBean", scope = ManagedBeanScope.REQUEST)
public class ShipBusGoosChooseBean extends GridView {
	
	
	
	
	
	@SaveState
	@Accessible
	public Long linkid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			linkid=Long.valueOf(AppUtils.getReqParam("linkid"));
		}
	}

	
	@Action
	public void choosejobs(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0||ids.length >1) {
			MessageUtils.alert("请勾选一条记录");
			return;
		}
		 try {
			 String sql = "SELECT f_busgoods_addcopy("+ids[0]+","+this.linkid+",'"+AppUtils.getUserSession().getUsercode()+"');";
			//ApplicationUtils.debug(sql);
			 this.serviceContext.busGoodsMgrService.busGoodsDao
				.executeQuery(sql);
			 MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	 
	
	//
	@Override
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		String sql = "select j.customerid FROM bus_ship_container c,fina_jobs j WHERE c.id = "+this.linkid +" AND c.isdelete = FALSE AND c.jobid = j.id AND j.isdelete = FALSE";
		Map m1  = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		Long customerid = (Long) m1.get("customerid");
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//单独的子节点,不为空,不为自己,
		qry += "\nAND t.customerid = "+customerid;
		//没有子节点;
		m.put("qry", qry);
		return m;
	}
	
	
}
