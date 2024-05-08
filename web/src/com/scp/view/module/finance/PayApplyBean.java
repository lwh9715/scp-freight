package com.scp.view.module.finance;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.finance.FinaStatement;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

@ManagedBean(name = "pages.module.finance.payapplyBean", scope = ManagedBeanScope.REQUEST)
public class PayApplyBean extends GridView {
	
	@SaveState
	@Accessible
	public FinaStatement selectedRowData = new FinaStatement();
	
	@Bind
	@SaveState
	public String searchAr = "ap";

	@Bind
	@SaveState
	public boolean lazyfilter = false;

	@Action(id="searchAr",event="onchange")
	public void filterIframeClientele(){
		if (lazyfilter) {
			this.customerQry();
		}
	}


	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			initData();
			super.applyGridUserDef();
			this.qryMap.put("t.reqtype", "Q");
			this.customerChooseBean.setQryforNull();
			this.customerChooseBean.setQrysqlforNull();
		}
		
	}
	
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String id = grid.getSelectedIds()[0];
		AppUtils.openNewPage("./payapplyedit.xhtml?type=edit&id="+id+"&customerid=-1&araptype=AP");
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
		AppUtils.openNewPage("./payapplyedit.xhtml?type=new&id=-1&customerid="+id+"&araptype=AP");
	}
	
	@Bind
	private String qryCustomerKey;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.setQryforNull();
		this.customerChooseBean.setQrysqlforNull();
		this.customerChooseBean.qry(qryCustomerKey , true);
		this.clientGrid.reload();
	}
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {

		//默认应付
		boolean isRet = true;
		if (this.searchAr.equals("ar")) {
			isRet = false;
		}
		lazyfilter = true;

		return this.customerChooseBean.getRpreqClientDataProvider(isRet);
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
		String corpFilter = "\n  AND EXISTS (SELECT 1 FROM sys_user_corplink x WHERE x.corpid = t.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+") " +
				"\nAND ( EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = t.customerid AND xx.userid = "
				+ AppUtils.getUserSession().getUserid()
				+ ") OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")" +
						"OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_air y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")" +
						"OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_train y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")" +
						"OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = d.jobid  AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")" +
						"OR EXISTS (SELECT 1 FROM sys_userinrole x,sys_user u WHERE x.roleid = 3006994888 AND x.userid = "+ AppUtils.getUserSession().getUserid() + " AND x.userid = u.id AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = d.jobid AND u.corpid = ANY(SELECT job.corpid UNION SELECT job.corpidop UNION SELECT job.corpidop2  UNION (SELECT corpid from fina_corp WHERE isdelete = FALSE AND jobid = job.id)) AND job.isdelete = FALSE)) " +
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
		
		String setDays = "\n AND (t.singtime >= NOW()::DATE-"+this.setDays+" OR t.singtime IS NULL) ";
		m.put("setDays", setDays);
		return m;
	}
	
	@Bind
	public UIWindow unloaDaysWindows;
	
	@Bind
	@SaveState
	public String setDays = "60";
	
	@Action
	public void cancel(){
		unloaDaysWindows.close();
	}
	
	protected void initData(){
		String findUserCfgVal = ConfigUtils.findUserCfgVal("rpreq_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="60";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("rpreq_date",this.setDays, AppUtils.getUserSession().getUserid());
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
