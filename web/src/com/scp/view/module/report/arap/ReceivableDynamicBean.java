package com.scp.view.module.report.arap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.report.arap.receivabledynamicBean", scope = ManagedBeanScope.REQUEST)
public class ReceivableDynamicBean extends GridView{

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;

	@Bind
	public String saleid;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			dateFrom = new Date();
			dateTo = new Date();
		}
	}
	
	@SaveState
	public String customerid;
	
	@Bind
	@SaveState
	public UIDataGrid customerGrid;

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}
	
	
	@Bind
	@SaveState
	public UIWindow showCustomerWindow;
	
	
	@Bind
	@SaveState
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.customerid = m.get("id").toString();
		this.customerabbr = (String) m.get("abbr");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}
	
	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}
	
	@Bind
	@SaveState
	public String customerabbr;



	@Action
	public void report() {
		if (!check())
			return;
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport_master.jsp?raq=/static/arap/RE_DY.raq";
		AppUtils.openWindow("RE_DY", openUrl + getArgs());
	}

	private boolean check() {
		if (dateFrom == null) {
			MessageUtils.alert("日期不能为空!");
			return false;
		}
		if (dateTo == null) {
			MessageUtils.alert("日期不能为空!");
			return false;
		}
		if (StrUtils.isNull(customerid)) {
			MessageUtils.alert("客户不能为空!");
			return false;
		}
		return true;
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(dateFrom);
		String datTo = f1.format(dateTo);
		String arg = "&saleid=" + saleid + "&customerid="
		+ customerid + "&dateFrom=" + datfrom
				+ "&dateTo=" + datTo;
		
		return arg;
	}
}
