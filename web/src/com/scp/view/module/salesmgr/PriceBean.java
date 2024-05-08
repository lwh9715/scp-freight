package com.scp.view.module.salesmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.salesmgr.priceBean", scope = ManagedBeanScope.REQUEST)
public class PriceBean extends GridView{
	
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}
	
	@Bind
	@SaveState
	private String customer="";
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		
		if(!StrUtils.isNull(customer)){
			qry +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=t.customerid AND sc.abbr ILIKE '%"+customer+"%')";		
		}
		map.put("qry", qry);
		
		//录入人，关联的业务员可以看到
		String sql = "\nAND ( t.salesid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.salesid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.salesid) " //组关联业务员的单，都能看到
				+ ")"
				+ "\n)";
		map.put("filter", sql);
		return map;
	}
	
	@Action
	public void add() {
		String winId = "_edit_price";
		String url = "./priceedit.xhtml";
		AppUtils.openWindow(winId, url);
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_price";
		String url = "./priceedit.xhtml?id=" + this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.busPriceService.removeDate(Long.parseLong(id), AppUtils.getUserSession().getUsercode());
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void emailSend() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_bus_price_notify_email('priceid="+Long.parseLong(id)+";usercode="+AppUtils.getUserSession().getUsercode()+"');");
			}
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Bind
	public UIWindow customerWindow;
	
	@Bind
	@SaveState
	public String includFee;
	
	@Bind
	@SaveState
	public Long customerid;
	
	@Action
	public void addcopy() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		this.customerWindow.show();
	}
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapCusyomer = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid customergrid;
	
	
	@Bind(id = "customergrid", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.customer.customerBean.grid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapCusyomer), start, limit).toArray();

			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.customer.customerBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapCusyomer));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	@Bind
	@SaveState
	public String customerdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(customerdesc) ){
			customerdesc = StrUtils.getSqlFormat(customerdesc);
			qry += "AND (code ILIKE '%"+customerdesc+"%' OR namec ILIKE '%"+customerdesc+"%' OR namee ILIKE '%"+customerdesc+"%' OR abbr ILIKE '%"+customerdesc+"%')";
		}
		
		String filter = AppUtils.custCtrlClauseWhere();
		map.put("filter", filter);
		
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void qrycustomer() {
		this.customergrid.reload();
	}
	
	@Action
	public void confirm(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		String[] customsids = this.customergrid.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选客户信息！");
			return;
		}
		for (String id : customsids) {
			try {
				String nos = serviceContext.busPriceService.addcopyjobs(ids[0],Long.valueOf(id), AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode());
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.qryRefresh();
	}
	
}
