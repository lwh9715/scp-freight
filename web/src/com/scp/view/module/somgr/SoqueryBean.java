package com.scp.view.module.somgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.ship.BusShipBooking;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;


@ManagedBean(name = "pages.module.somgr.soqueryBean", scope = ManagedBeanScope.REQUEST)
public class SoqueryBean extends GridView {

	@SaveState
	@Accessible
	@Bind
	public Long currentUserid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			currentUserid = AppUtils.getUserSession().getUserid();
		}
	}
	
	
	@Bind
	@SaveState
	public String customerid;
	
	
	@Bind
	@SaveState
	public String corpidop;

	@Bind
	@SaveState
	public String salesid;

	@Bind
	@SaveState
	public String paymentitem;


	@Action
	public void createJobs() {
		try {
			String[] ids = this.grid.getSelectedIds();
			String sql = "SELECT f_bus_booking2job('bookingid=" + ids[0] + ";userid=" + AppUtils.getUserSession().getUserid()
					+ ";customerid=" + customerid + ";corpidop=" + corpidop + ";salesid=" + salesid + ";paymentitem=" + paymentitem + "') AS jobid";
			List<Map> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			String jobid = StrUtils.getMapVal(map.get(0), "jobid");
			Browser.execClientScript("window.open('/scp/pages/module/ship/jobsedit.xhtml?id="+jobid+"');");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCreateJobs() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选择一条记录!");
			return;
		}
		Browser.execClientScript("createJobsWindowJsVar.show();");
	}
	
	
	@Action
	public void changeStatus() {
		try {
			String status = AppUtils.getReqParam("status");
			if(StrUtils.isNull(status)){
				return;
			}
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("Please select row");
				return;
			}
			if(status.equals("T")){	//退回
				SonoBean.withdrawSO(ids,serviceContext);
				SonoBean.untieSO(ids,serviceContext);
				this.refresh();
			}else if(status.equals("F")){	//解绑
				SonoBean.withdrawSO(ids, serviceContext);
				this.refresh();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Bind
	@SaveState
	private boolean isdate = false;
	@Bind
	@SaveState
	private String dates;
	@Bind
	@SaveState
	private String timeperiod;
	@Bind
	@SaveState
	private String dateastart;
	@Bind
	@SaveState
	private String dateend;
	@SaveState
	@Bind
	private String carrieridv;
	@SaveState
	@Bind
	public String bookname;
	@Bind
	@SaveState
	public String polid;
	@Bind
	@SaveState
	public String podid;
	@Bind
	@SaveState
	String dynamicClauseWhere = "";


	@Bind
	public UIWindow searchWindow;

	@Action
	public void searchSenior() {
		this.searchWindow.show();
	}

	@Action
	public void searchfee() {
		this.qryRefresh();
	}

	@Action
	public void clearfee() {
		this.clearQryKey();
	}

	@Action
	public void clearAndClosefee() {
		this.clearQryKey();
		this.searchWindow.close();
	}

	@Override
	public void clearQryKey() {
		isdate = false;
		dates = "";
		timeperiod = "";
		dateastart="";
		dateend="";
		carrieridv = "";
		Browser.execClientScript("$('#book_input').val('');");
		bookname = "";
		Browser.execClientScript("$('#polDiv_input').val('');");
		polid = "";
		Browser.execClientScript("$('#podDiv_input').val('');");
		podid = "";
		update.markUpdate(true, UpdateLevel.Data, "shipquerycfg");
	}

	@Action
	public void settimeperiod() {
		if ("当日".equals(timeperiod)) {
			dateastart = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			dateend = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		} else if ("当周".equals(timeperiod)) {
			dateastart = getMondayOfThisWeek();
			dateend = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		} else if ("当月".equals(timeperiod)) {
			dateastart = getFirstOfThisMonth();
			dateend = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		}
	}

	public static String getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		String thisWeekMonday = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return thisWeekMonday;
	}

	public static String getFirstOfThisMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDayForMonth = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

		return firstDayForMonth;
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);

		String filter = "";
		if (!"demo".equals(AppUtils.getUserSession().getUsercode())) {
			filter = "\n AND salesid = " + AppUtils.getUserSession().getUserid();
		}
		m.put("filter", filter);


		//初始化
		dynamicClauseWhere = " 1=1 ";
		//高级检索中日期区间查询拼接语句
		if (isdate) {
			if(!StrUtils.isNull(dates)){
				dynamicClauseWhere += "\nAND " + dates + "::DATE BETWEEN '"
						+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
						+ "' AND '"
						+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
						+ "'";
			}
		}

		if(!StrUtils.isNull(carrieridv)){
			dynamicClauseWhere += "\n AND carrierid =" + carrieridv + "";
		}
		if(!StrUtils.isNull(bookname)){
			dynamicClauseWhere += "\n AND userbook ='" + bookname + "'";
		}
		if(!StrUtils.isNull(polid)){
			dynamicClauseWhere += "\n AND polid =" + polid + "";
		}
		if(!StrUtils.isNull(podid)){
			dynamicClauseWhere += "\n AND podid =" + podid + "";
		}
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		return m;
	}

	@SaveState
	@Accessible
	public BusShipBooking selectedRowData = new BusShipBooking();

	@Bind
	public UIWindow showbookingWindow;

	@Bind
	public UIIFrame bookingIframe;

	@Override
	public void grid_ondblclick() {
		this.selectedRowData=serviceContext.busBookingMgrService.busShipBookingDao.findById(this.getGridSelectId());
		super.grid_ondblclick();
		String winId = "_edit_booking";
		String url = "./booking.xhtml?id="+this.selectedRowData.getId()+"&r="+ AppUtils.base64Encoder("edit");
		bookingIframe.load(url);
		showbookingWindow.show();
	}
}
