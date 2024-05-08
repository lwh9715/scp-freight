package com.scp.view.module.somgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.price.ShipQueryBean;

@ManagedBean(name = "pages.module.somgr.sostatisticsBean", scope = ManagedBeanScope.REQUEST)
public class SostatisticsBean extends GridView {

	@SaveState
	@Accessible
	@Bind
	public Long currentUserid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			currentUserid = AppUtils.getUserSession().getUserid();
			super.applyGridUserDef();

			// state0 = "6";
			// state1 = "已分派";
			// state2 = "已建工作单";
			// state3 = "没有过截关期";
		}
	}
	
	@Action
	public void createJobs() {
		try {
			String[] ids = this.grid.getSelectedIds();
			Long userid = AppUtils.getUserSession().getUserid();
			String uuid = ids[0];
			String priceid = uuid.split("-")[0];
			String bargeid = uuid.split("-")[1];
			String sql = "";
			//System.out.println(sql);
			List<Map> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			String jobid = StrUtils.getMapVal(map.get(0), "jobid");
			String alter45hq = StrUtils.getMapVal(map.get(1), "jobid");
			if(!StrUtils.isNull(alter45hq)){
				Browser.execClientScript("layer.alert('"+alter45hq+"', {icon: 7},function(){window.open('/scp/pages/module/ship/jobsedit.xhtml?id="
						+jobid+"');layer.close(layer.index);});");
			}else{
				Browser.execClientScript("window.open('/scp/pages/module/ship/jobsedit.xhtml?id="+jobid+"');");
			}
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
	public void refreshpage() {
		this.clearQryKey();
		empty();
		super.refresh();
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
	public String bookname;
	@Bind
	@SaveState
	public String vessel;
	@Bind
	@SaveState
	public String voyage;
	@Bind
	@SaveState
	public String polcode;
	@Bind
	@SaveState
	public String state0;
	@Bind
	@SaveState
	public String state1;
	@Bind
	@SaveState
	public String state2;
	@Bind
	@SaveState
	public String state3;
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
		empty();
	}

	@Action
	public void clearAndClosefee() {
		this.clearfee();
		this.searchWindow.close();
	}

	@Override
	public void clearQryKey() {
		isdate = false;
		dates = "";
		timeperiod = "";
		dateastart="";
		dateend="";
		bookname = "";
		vessel = "";
		voyage = "";
		polcode = "";

		state0 = "";
		state1 = "";
		state2 = "";
		state3 = "";
		update.markUpdate(true, UpdateLevel.Data, "shipquerycfg1");
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


		//初始化
		dynamicClauseWhere = "";
		//高级检索中日期区间查询拼接语句
		if (isdate) {
			if(!StrUtils.isNull(dates)){
				dynamicClauseWhere += "\nAND " + dates + "::timestamptz BETWEEN '"
						+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
						+ "' AND '"
						+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
						+ "'";
			}
		}
		if (!StrUtils.isNull(shipcarrier)) {
			dynamicClauseWhere += "\n AND cast(carrierid as text) IN (select regexp_split_to_table('" + shipcarrierids + "', ',') )";
		}
		if(!StrUtils.isNull(bookname)){
			dynamicClauseWhere += "\n AND userbook ilike '%" + bookname + "%'";
		}
		if(!StrUtils.isNull(vessel)){
			dynamicClauseWhere += "\n AND vessel ilike '%" + vessel + "%'";
		}
		if(!StrUtils.isNull(voyage)){
			dynamicClauseWhere += "\n AND voyage ilike '%" + voyage + "%'";
		}
		if(!StrUtils.isNull(polcode)){
			dynamicClauseWhere += "\n AND pol ='" + polcode + "'";
		}

		if(!StrUtils.isNull(state0)){
			dynamicClauseWhere += "\n AND bookstate ='" + state0 + "'";
		}
		if(!StrUtils.isNull(state1)){
			if ("已分派".equals(state1)) {
				dynamicClauseWhere += "\n AND salesid is not NULL";
			} else {
				dynamicClauseWhere += "\n AND salesid is NULL  ";
			}
		}
		if(!StrUtils.isNull(state2)){
			if ("已建工作单".equals(state2)) {
				dynamicClauseWhere += "\n AND jobid IS NOT NULL";
			} else {
				dynamicClauseWhere += "\n AND jobid IS NULL";
			}
		}
		if(!StrUtils.isNull(state3)){
			if ("没有过截关期".equals(state3)) {
				dynamicClauseWhere += "\n AND '" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' <= clsbig::DATE ";
			} else {
				dynamicClauseWhere += "\n AND '" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' > clsbig::DATE ";
			}
		}

		m.put("dynamicClauseWhere", dynamicClauseWhere);
		return m;
	}


	@Bind
	@SaveState
	public String shipcarrier;

	@Bind
	@SaveState
	public String shipcarrierids;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapShipcarrier = new HashMap<String, Object>();

	@Bind
	public UIDataGrid gridShipcarrier;

	@Bind(id = "gridShipcarrier", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {
			public Object[] getElements() {
				String sqlId = "pages.module.somgr.sonoBean.gridShipcarrier.page";
				return serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere3(qryMapShipcarrier), start, limit).toArray();
			}

			public int getTotalCount() {
				String sqlId = "pages.module.somgr.sonoBean.gridShipcarrier.count";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere3(qryMapShipcarrier));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	@Bind
	@SaveState
	public String qryshipcarrierdesc = "";

	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		Map map = new LinkedHashMap();
		map.put("qry", "1=1");
		map.put("limit", "10000");
		map.put("start", "0");
		String qry = map.get("qry").toString();
		if (!ShipQueryBean.isNull(qryshipcarrierdesc)) {
			qryshipcarrierdesc = StrUtils.getSqlFormat(qryshipcarrierdesc);
			qry += "AND (namec ILIKE '%" + qryshipcarrierdesc + "%' OR code ILIKE '%" + qryshipcarrierdesc + "%')";
		}
		map.put("qry", qry);
		return map;
	}


	@Action
	public void qryshipcarrier() {
		this.gridShipcarrier.reload();
	}


	@Action
	public void confirm() {
		shipcarrier = "";
		String[] ids = this.gridShipcarrier.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		shipcarrierids = StringUtils.join(ids, ",");
		for (String id : ids) {
			SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
			String code = sysCorporation.getCode();
			if (ShipQueryBean.isNull(this.shipcarrier)) {
				this.shipcarrier = code;
			} else {
				if (!this.shipcarrier.contains(code)) {
					this.shipcarrier = this.shipcarrier + "," + code;
				}
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "shipquerycfg1");
	}

	@Action
	public void empty() {
		this.shipcarrier = "";
		shipcarrierids = "";
		update.markUpdate(true, UpdateLevel.Data, "shipquerycfg1");
	}
}
