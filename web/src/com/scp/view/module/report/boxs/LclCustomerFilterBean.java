package com.scp.view.module.report.boxs;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;
@ManagedBean(name = "pages.module.report.boxs.lclcustomerfilterBean", scope = ManagedBeanScope.REQUEST)
public class LclCustomerFilterBean extends GridView {

	@Bind
	public Date dateFrom;

	@Bind
	@SaveState
	public Date dateTo;

	@SaveState
	public String customerids;

	@Bind
	@SaveState
	public String condition;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			customerService.setQrysqlforNull();
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
	public GridDataProvider getCustomersGridDataProvider() {
		return customerService.getCustomersDataProvider();
	}

	@Action
	public void refresh2() {
		this.customerService.qry(condition, true);
		this.customerGrid.reload();
	}

	@Action
	public void report() {
		String[] ids = this.customerGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请至少选择一条数据!");
			return;
		}
		this.customerids = StrUtils.array2List(ids);
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/boxs/LCL_CUSTOMER.raq";
		AppUtils.openWindow("_apFilterCustomReport", openUrl + getArgs());
	}

	private String getArgs() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String arg = "&dateFrom=" + df.format(dateFrom) + "&dateTo="
				+ df.format(dateTo) + "&customerid=" + customerids;
		return arg;
	}
}
