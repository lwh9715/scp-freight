package com.scp.view.module.salesmgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.salesmgr.analyzelineBean", scope = ManagedBeanScope.REQUEST)
public class AnalyzeLineBean extends GridView {

	@Bind
	@SaveState
	public Date jobdatefm;

	@Bind
	@SaveState
	public Date jobdateto;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			getWeekDate();
		}
	}

	@Bind
	public UIIFrame showReportIframe;

	@Override
	public void grid_onrowselect() {
		if (jobdatefm == null || jobdateto == null) {
			MessageUtils.alert("检索条件不能为空!请检查");
			return;
		}
		String url = "";
		url = AppUtils.getContextPath()
				+ "/reportJsp/showReport.jsp?raq=/static/analyze/customer_zx.raq"
				+ getArgs();
		showReportIframe.load(url);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map
				.put(
						"filter",
						" AND EXISTS (SELECT 1 FROM fina_jobs x WHERE a.id = x.customerid AND x.saleid = "
								+ AppUtils.getUserSession().getUserid() + ")");
		return map;
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(jobdatefm);
		String datTo = f1.format(jobdateto);
		String args = "&customerid=" + Long.toString(this.getGridSelectId())
				+ "&jobdatefm=" + datfrom + "&jobdateto=" + datTo;
		return args;
	}

	/**
	 * 获取本周日期范围
	 */
	public void getWeekDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 获得本周周一
		jobdatefm = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		jobdateto = cal.getTime();// 获得本周周末日期
	}

	@Bind
	@SaveState
	public String popQryKey;

	@SaveState
	public Long customerid;

	@Bind
	@SaveState
	public String customerabbr;

	@Bind
	@SaveState
	public UIWindow showCustomerWindow;

	@Bind
	@SaveState
	public UIDataGrid customerGrid;

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.customerid = (Long) m.get("id");
		this.customerabbr = (String) m.get("abbr");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}

}
