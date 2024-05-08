package com.scp.view.module.finance;

import java.util.HashMap;
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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.prepaidBean", scope = ManagedBeanScope.REQUEST)
public class PrepaidBean extends GridView {
	
	@Autowired
	public ApplicationConf applicationConf;
	
	
	@SaveState
	private Long clientid = -1l;
	
	
	@Bind
	public String customergridPanel;
	
	@Bind
	public String customerids;

	
	
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid customergrid;

	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			initData();
			super.applyGridUserDef();
			this.customerChooseBean.setQryforNull();
			this.customerChooseBean.setQrysqlforNull();
		}
	}
	@Override
	public void grid_ondblclick(){
		super.grid_ondblclick();
		String gridid = grid.getSelectedIds()[0];
		String id = gridid.split(",")[0];
		AppUtils.openWindow(false , "_newPrepaid", "./prepaidedit.xhtml?type=edit&id="+id+"&customerid=-1");
	}
	
	@Override
	public void qryRefresh() {
		clientid = -1l;
		this.qryMap.remove("clientid$");
		super.qryRefresh();
		this.grid.reload();
		this.clientGrid.setSelectedRow(-1);
		this.clientGrid.reload();
	}
	
	@Action
	public void qryRefresh2() {
		
		
		this.grid.reload();
	}
	
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
//		String sql = "\n(EXISTS" + "\n (SELECT 1 "
//			+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
//			+ "\n				WHERE y.custlibid = z.custlibid "
//			+ "\n				AND y.corpid = clientid" + "\n				AND z.userid = "
//			+ AppUtils.getUserSession().getUserid() + ")" + ")";
//		//权限控制 neo 2016-07-24
		m.put("filter", " 1=1");
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);		
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer corpfilter = new StringBuffer();;
//			corpfilter.append("\n AND (a.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" OR a.agentcorpid = "+AppUtils.getUserSession().getCorpidCurrent()+")");
//			m.put("corpfilter", corpfilter.toString());
//		}
		
		StringBuffer corpfilter = new StringBuffer();
		corpfilter.append("\n AND EXISTS (SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = a.corpid) AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")");
		m.put("corpfilter", corpfilter.toString());
		String setDays = "\n AND a.singtime >= NOW()::DATE-"+this.setDays;
		m.put("setDays", setDays);
		return m;
	}
	@Bind
	private String qryCustomerKey;
	
	@Bind
	public String search;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.setQryforNull();
		this.customerChooseBean.setQrysqlforNull();
		this.customerChooseBean.qry(qryCustomerKey , true);
		this.clientGrid.reload();
	}

	@Bind
	public UIDataGrid clientGrid;
	
	@ManagedProperty("#{customerchooseBean}")
	public CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
//		this.setQryNull();
		return this.customerChooseBean.getActPayRecClientDataProvider();
	}
	
	@Action
	public void clientGrid_ondblclick() {
		String id = clientGrid.getSelectedIds()[0];
		AppUtils.openWindow(false , "_newPrepaid", "./prepaidedit.xhtml?type=new&id=-1&customerid="+id+"");
	}
	
	@Action
	public void clientGrid_onrowselect() {
		String[] ids = clientGrid.getSelectedIds();
		if(ids == null || ids.length !=1)return;
		String id = clientGrid.getSelectedIds()[0];
		clientid = Long.parseLong(id);
		
		this.qryMap.put("clientid$", clientid);
		this.grid.reload();
	}
	
	@Action
	public void del(){
		String[] ids  = grid.getSelectedIds();
		if(ids.length < 1){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		String id = ids[0];
		try {
			this.serviceContext.finaPrepaidMgrService.removeDate(id,AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
		String findUserCfgVal = ConfigUtils.findUserCfgVal("prepaid_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="60";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("prepaid_date",this.setDays, AppUtils.getUserSession().getUserid());
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void clearQryKey() {
		super.clearQryKey();
	}
	
	
	@Action
	public void generateRP() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行!");
			return;
		}
		try {
			String actpayrecid = serviceContext.finaPrepaidMgrService.createRP(ids[0] , AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("生成成功");
			String url = AppUtils.chaosUrlArs("../finance/actpayreceditnew.xhtml") + "&type=edit&id=" + actpayrecid;
			AppUtils.openWindow("_showPrepaiActpayrec_" + actpayrecid,url);
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
}
