package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shippingholdBean", scope = ManagedBeanScope.REQUEST)
public class ShippingHoldBean extends GridView {

	@Bind
	@SaveState
	public Boolean iscare = false;

	@SaveState
	private Long jobid;
	
	@Bind
	@SaveState
	private String startDateJ;
	
	@Bind
	@SaveState
	private String endDateJ;
	
	@Bind
	@SaveState
	private String startDateA;
	
	@Bind
	@SaveState
	private String endDateA;
	
	@Bind
	@SaveState
	private String carryitem;
	
	@Bind
	@SaveState
	public UIWindow searchWindow;
	
	@Bind
	@SaveState
	public String scanReport;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.qryMap.put("shipholdstate","扣");
		}
	}
	@Action(id = "grid", event = "onrowselect")
	public void grid_onrowselect() {
		this.jobid = Long.parseLong(this.grid.getSelectedIds()[0]);
		this.showArapEdit();
	}
	
	@Action
	public void grid_ondblclick(){
		String url = AppUtils.getContextPath()
		+ "/pages/module/ship/busshiphold.xhtml?linkid="
		+ this.jobid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	@Bind
	public UIIFrame arapIframe;

	@SaveState
	private boolean showArapEdit;

	@Action
	public void showArapEdit() {
		if (this.jobid == null) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			arapIframe.load(blankUrl);
		} else {
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			arapIframe.load("../ship/shippingholduse.xhtml?customerid="
					+ finaJobs.getCustomerid() + "&jobid=" + this.jobid);
			// showArapEdit = true;
			
			
		
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		// ATD日期和工作单日期区间查询
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(startDateJ) || !StrUtils.isNull(endDateJ)) {
			qry += "\nAND t.jobdate BETWEEN '" + (StrUtils.isNull(startDateJ) ? "0001-01-01" : startDateJ)
				+ "' AND '" + (StrUtils.isNull(endDateJ) ? "9999-12-31" : endDateJ) + "'";
		}
		if(!StrUtils.isNull(startDateA) || !StrUtils.isNull(endDateA)) {
			qry += "\nAND ((t.atd BETWEEN '" + (StrUtils.isNull(startDateA) ? "0001-01-01" : startDateA)
				+ "' AND '" + (StrUtils.isNull(endDateA) ? "9999-12-31" : endDateA) + "')"
				+ "\n OR t.atd IS NULL)";
		}
		m.put("qry", qry);
		
		if(StrUtils.isNull(carryitem)) { //运费条款的查询（连接bus_shipping）
			m.put("carryitem", "1=1");
		} else {
			String sql = "EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND carryitem = '"+carryitem+"')";
			m.put("carryitem", sql);
		}
		
		if (!iscare) {
			m.put("icare", "1=1");
		} else {
			String sql="(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="+AppUtils.getUserSession().getUserid()+")";
			sql+="OR t.inputer ='"+AppUtils.getUserSession().getUsercode()+"' OR t.updater = '"+AppUtils.getUserSession().getUsercode()+"')";
			m.put("icare",sql);
			
		}
		
		String sql = "\nAND (EXISTS" +
		 "\n				(SELECT " +
		 "\n					1 " +
		 "\n				FROM sys_custlib_cust y , sys_custlib_user z " +
		 "\n				WHERE y.custlibid = z.custlibid " +
		 "\n				AND y.corpid = customerid" + 
		 "\n				AND z.userid = " + AppUtils.getUserSession().getUserid()+ ")" +
		 //xx.linktype = 'C' AND 
		 "\n	OR EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = customerid AND xx.userid = " + AppUtils.getUserSession().getUserid()+ ")"+
		 "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="+AppUtils.getUserSession().getUserid()+")"+
		 "\n	OR (t.inputer ='"+AppUtils.getUserSession().getUsercode()+"' OR t.updater = '"+AppUtils.getUserSession().getUsercode()+"')"+
		 "\n)";
		
		//权限控制 neo 2014-05-30
		m.put("filter", sql);
		
		return m;
	}
	
	@Override
	public void clearQryKey() {
		this.startDateJ = "";
		this.endDateJ = "";
		this.startDateA = "";
		this.endDateA = "";
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		super.clearQryKey();
	}
	
	
	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
	
	@Action
	public void searchfee(){
		this.qryRefresh();
	}
	
	@Action
	public void clear(){
		this.clearQryKey();
	}
	
	@Action
	public void search(){
		this.searchWindow.show();
	}
	
	@Action
	public void scanReport(){
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
		+ "/reportJsp/showReport.jsp?raq=/static/KH_MX.raq";
			AppUtils.openWindow("_shipbillReport", openUrl);
	}
}
