package com.scp.view.module.finance;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.model.finance.FinaStatement;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.payapplyarBean", scope = ManagedBeanScope.REQUEST)
public class PayapplyarBean extends GridView {
	
	@SaveState
	@Accessible
	public FinaStatement selectedRowData = new FinaStatement();
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			this.qryMap.put("t.reqtype", "Q");
		}
		
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String id = grid.getSelectedIds()[0];
		AppUtils.openWindow("_newpayapplyedit", "./payapplyedit.xhtml?type=edit&id="+id+"&customerid=-1&araptype=AR");
	}
	
	@Action
	public void clientGrid_onrowselect() {
		String id = clientGrid.getSelectedIds()[0];
		this.qryMap.put("customerid$", id);
		this.grid.reload();
	}
	
	
	@Override
	public void qryRefresh() {
		this.qryMap.remove("customerid$");
		super.qryRefresh();
		this.grid.reload();
		this.clientGrid.setSelectedRow(-1);
		this.clientGrid.reload();
	}

	
	@Bind
	public UIDataGrid clientGrid;

	
	@Action
	public void clientGrid_ondblclick() {
		String id = clientGrid.getSelectedIds()[0];
		AppUtils.openWindow("_newRP", "./payapplyedit.xhtml?type=new&id=-1&customerid="+id+"&araptype=AR");
	}
	
	@Bind
	private String qryCustomerKey;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.qry(qryCustomerKey , true);
		this.clientGrid.reload();
	}
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return this.customerChooseBean.getRpreqArClientDataProvider();
	}
	
	@Action
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0||ids.length>1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		
		try {
			String idalies = ids[0];
			String id = idalies.split("\\.")[0];
			this.serviceContext.reqMgrService.removeDate(Long.valueOf(id));
			this.alert("OK!");
			this.refresh();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m =  super.getQryClauseWhere(queryMap);
		String corpFilter = "  AND EXISTS (SELECT 1 FROM sys_user_corplink x WHERE x.corpid = t.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+") " +
				"AND ( EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = t.customerid AND xx.userid = "
				+ AppUtils.getUserSession().getUserid()
				+ ") OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")" +
						"OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = d.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = d.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //组关联业务员的单，都能看到
				+ "))";
		
		m.put("filter", corpFilter);
		return m;
	}
}
