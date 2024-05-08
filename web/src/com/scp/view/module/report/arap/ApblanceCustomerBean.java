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

@ManagedBean(name = "pages.module.report.arap.apblancecustomerBean", scope = ManagedBeanScope.REQUEST)
public class ApblanceCustomerBean extends GridView {

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;
	
	@Bind
	public String  currency = "1";

	@Bind
	@SaveState
	public String customerabbr;
	
	@Bind
	@SaveState
	public String reporttype = "YE_EG.raq";

	@Bind
	@SaveState
	public String popQryKey;

	@SaveState
	public String customerid;
	
	@Bind
	public String ppccType = "1";

	@Bind
	@SaveState
	public UIDataGrid customerGrid;

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			dateFrom = new Date();
			dateTo = new Date();
		}
	}

	@Bind
	@SaveState
	public UIWindow showCustomerWindow;

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
	public void report() {
		if (dateFrom == null || dateTo == null || customerabbr == null) {
			MessageUtils.alert("检索条件不能为空!请检查");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/arap/"+reporttype;
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(dateFrom);
		String datTo = f1.format(dateTo);
		String arg;
		if(this.reporttype.contains("arap_join")){
			 arg = "&datefm=" + datfrom + "&dateto=" + datTo + "&customerid="
			+ customerid +"&ppcctype=" + ppccType;
		}else{
			arg = "&datefm=" + datfrom + "&dateto=" + datTo + "&customerid="
				+ customerid+ "&id=" +AppUtils.getUserSession().getCorpid()+"&ppcctype=" + ppccType +"&currency="+ currency;
		}
		return arg;
	}
	
	@Action
	public void savemsg(){
		String [] ids = customerGrid.getSelectedIds();
		if(ids.length < 0 || ids !=null){
			customerid = "("+StrUtils.array2List(ids)+")";
		}
	}

}
