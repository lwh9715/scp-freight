package com.scp.view.module.report.porfit;

import java.math.BigDecimal;
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

@ManagedBean(name = "pages.module.report.profit.receivablerealBean", scope = ManagedBeanScope.REQUEST)
public class ReceivableRealBean extends GridView{

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;

	@Bind
	public String hkdto = "*";

	@Bind
	public BigDecimal hkdrat;

	@Bind
	public String cnyto = "*";

	@Bind
	public BigDecimal cnyrat;

	@Bind
	public String dhsto = "*";

	@Bind
	public BigDecimal dhsrat;

	@Bind
	public String sarto = "*";

	@Bind
	public BigDecimal sarrat;
	
	@Bind
	public String salesid = "";
	
	@SaveState
	public String customerid = "1";

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			dateFrom = new Date();
			dateTo = new Date();
		}
	}
	
	
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
	
	@Bind
	@SaveState
	public String customerabbr;

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
	
	@Action
	public void savemsg(){
		String [] ids = customerGrid.getSelectedIds();
		if(ids.length < 0 || ids !=null){
			customerid = "("+StrUtils.array2List(ids)+")";
		}
	}


	@Action
	public void report() {
		if (!check())
			return;
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/profit/profit.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
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
		if (hkdrat == null || cnyrat == null || dhsrat == null
				|| sarrat == null) {
			MessageUtils.alert("兑换率不能为空!");
			return false;
		}
		return true;
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(dateFrom);
		String datTo = f1.format(dateTo);
		String arg = "&dateFrom=" + datfrom
				+ "&dateTo=" + datTo + "&hkd2usd=" + hkdrat + "&rmb2usd="
				+ cnyrat + "&dhs2usd=" + dhsrat + "&sr2usd=" + sarrat
				+ "&hkd2usd_xratetype=" + hkdto + "&rmb2usd_xratetype=" + cnyto
				+ "&dhs2usd_xratetype=" + dhsto + "&sr2usd_xratetype=" + sarto + "&salesid=" + (StrUtils.isNull(salesid)?"1":salesid) + "&customerid="
				+ customerid;
		return arg;
	}
}
