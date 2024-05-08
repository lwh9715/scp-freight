package com.scp.view.module.somgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.scp.util.FtUtils;
import com.ufms.web.view.sysmgr.LogBean;
import org.apache.commons.lang.StringUtils;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.ship.BusShipBooking;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.price.ShipQueryBean;


@ManagedBean(name = "pages.module.somgr.sonoBean", scope = ManagedBeanScope.REQUEST)
public class SonoBean extends GridView {
	@SaveState
	@Accessible
	public BusShipBooking selectedRowData = new BusShipBooking();

	@SaveState
	@Accessible
	@Bind
	public Long currentUserid;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			filterStatus = "X";
			init();
			currentUserid = AppUtils.getUserSession().getUserid();
		}
		// TODO Auto-generated method stub
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}


	@Action
	public void add() {
		String winId = "_edit_booking";
		String url = "./booking.xhtml";
		bookingIframe.load(url);
		showbookingWindow.show();
	}

    @Action
	public void delMaster(){
    	String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please chose one row first!");
		}else{
			try {
				for (String id : ids){
					serviceContext.busBookingMgrService.removeDate(Long.valueOf(id).longValue(), AppUtils.getUserSession().getUserid().toString());
				}
				MessageUtils.alert("OK");
			} catch (Exception e) {

			}
			this.grid.reload();
		}

    }
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

	public Long getBookingid(Long id){

		return id;

	}
	@Bind
	private String countX = "0";

	@Bind
	private String countS = "0";

	@Bind
	private String countZ = "0";

	@Bind
	private String countQ = "0";

	@Bind
	private String countT = "0";

	@Bind
	private String countC = "0";

	@Bind
	private String countA = "0";

	@Bind
	private String countG = "0";

	private void init() {
		try {
			String[] aa = new String[]{"X", "S", "Z", "Q", "T", "C", "A", "G"};
			String nowfilterStatus = filterStatus;
			for (String s : aa) {
				filterStatus = s;
				String sqlId = getMBeanName() + ".grid.count";
				Map map = getQryClauseWhere(qryMap);
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, map);
				String listcount = list.isEmpty() ? "0" : (list.get(0).get("counts").toString());
				if ("X".equalsIgnoreCase(filterStatus)) {
					this.countX = listcount;
				} else if ("S".equalsIgnoreCase(filterStatus)) {
					this.countS = listcount;
				} else if ("Z".equalsIgnoreCase(filterStatus)) {
					this.countZ = listcount;
				} else if ("Q".equalsIgnoreCase(filterStatus)) {
					this.countQ = listcount;
				} else if ("T".equalsIgnoreCase(filterStatus)) {
					this.countT = listcount;
				} else if ("C".equalsIgnoreCase(filterStatus)) {
					this.countC = listcount;
				} else if ("A".equalsIgnoreCase(filterStatus)) {
					this.countA = listcount;
				} else if ("G".equalsIgnoreCase(filterStatus)) {
					this.countG = listcount;
				}
			}
			filterStatus = nowfilterStatus;
		} catch (Exception e) {
		}
	}
	@SaveState
	public String filterStatus;

	@Action
	public void filterByStatus() {
		this.filterStatus = AppUtils.getReqParam("type");
		this.refresh();
		init();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if (!StrUtils.isNull(filterStatus)) {
			String filter = "";
			if ("X".equalsIgnoreCase(filterStatus)) {  //有效
				filter += "\n AND (CASE WHEN srctype = 'inttra' THEN bookstate = '6' WHEN srctype = 'cargosmart' THEN bookstate in ('9','5') END) ";
				filter += "\n AND '" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' <= clsbig::DATE ";
			} else if ("S".equalsIgnoreCase(filterStatus)) {	//无效
				filter = getSfilter();
			} else if ("Z".equalsIgnoreCase(filterStatus)) {	//可分配 (有效且未分配)
				filter = getZfilter();
			} else if ("Q".equalsIgnoreCase(filterStatus)) {	//已分配
				filter += "\n AND salesid is not NULL";
			} else if ("T".equalsIgnoreCase(filterStatus)) {	//未建工作单 (有效且已分配且未建工作单)
				filter += "\n AND (CASE WHEN srctype = 'inttra' THEN bookstate = '6' WHEN srctype = 'cargosmart' THEN bookstate in ('9','5') END)  ";
				filter += "\n AND '" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' <= clsbig::DATE ";

				filter += "\n AND salesid is not NULL " +
						"AND jobno IS NULL";
			} else if ("C".equalsIgnoreCase(filterStatus)) {	//已建工作单
				filter += "\n AND jobno IS NOT NULL";
			} else if ("A".equalsIgnoreCase(filterStatus)) {	//全部
				filter = "";
			} else if ("G".equalsIgnoreCase(filterStatus)) {	//有柜号
				filter += "\n AND EXISTS(SELECT 1 FROM bus_ship_book_cnt bsbc where b.id = bsbc.linkid AND cntno IS NOT NULL and cntno !='' )";
			}
			m.put("filterStatus", filter);
		}

		String filter1 = "";//"\n AND inputer = 'inttra'";
		if (!StrUtils.isNull(shipcarrier)) {
			filter1 += "\n AND cast(carrierid as text) IN (select regexp_split_to_table('" + shipcarrierids + "', ',') )";
		}
		m.put("filter", filter1);
		return m;
	}

	public static String getSfilter() {
		String filter = "";
		filter += "\n AND (( (CASE WHEN srctype = 'inttra' THEN bookstate = '6' WHEN srctype = 'cargosmart' THEN bookstate in ('9','5') END)  ";
		filter += "\n AND ('" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' > clsbig::DATE ";
		filter += "\n or clsbig is null)) " +
				"or ( bookstate is null or ((CASE WHEN srctype = 'inttra' THEN bookstate != '6' WHEN srctype = 'cargosmart' THEN bookstate not in ('9','5') END)))) ";
		return filter;
	}

	public static String getZfilter() {
		String filter = "";
		filter += "\n AND (CASE WHEN srctype = 'inttra' THEN bookstate = '6' WHEN srctype = 'cargosmart' THEN bookstate in ('9','5') END)  ";
		filter += "\n AND '" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' <= clsbig::DATE ";

		filter += "\n AND salesid is NULL  ";
		return filter;
	}

	@SaveState
	@Bind
	public String saleid;
	@SaveState
	@Bind
	public String nos;
	@Bind
	public UIWindow editWindow;

	@Action
	public void changeStatus() {
		try {
			String status = AppUtils.getReqParam("status");
			if (StrUtils.isNull(status)) {
				return;
			}
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("Please select row");
				return;
			}
			if (status.equals("S")) {
				String dmlSql = "UPDATE bus_ship_booking SET bookstate = '6' WHERE id IN(" + StrUtils.array2List(ids) + ");";
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
				this.refresh();
			} else if (status.equals("Z")) {
				String dmlSql = "UPDATE bus_ship_booking SET bookstate = '0' WHERE id IN(" + StrUtils.array2List(ids) + ");";
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
				this.refresh();
			} else if (status.equals("Q")) {
				ids1 = ids;
				try {
					String sql = "select salesid from _bus_ship_booking where id=" + ids[0];
					Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					saleid = StrUtils.getMapVal(map, "salesid");
				} catch (Exception e) {

				}

				if (ids == null || ids.length != 1) {
					MessageUtils.alert("请勾选一条记录！");
					return;
				}

				if (!"Z".equalsIgnoreCase(filterStatus)) {
					MessageUtils.alert("可分配下才允许进行分派！");
					return;
				}

				editWindow.show();
			} else if (status.equals("T")) {	//收回
				withdrawSO(ids,serviceContext);
				untieSO(ids,serviceContext);
				this.refresh();
			} else if (status.equals("F")) {	//解绑
				withdrawSO(ids, serviceContext);
				this.refresh();
			}

			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//解绑
	public static void withdrawSO(String[] ids, ServiceContext serviceContext) {
		//bus_ship_booking bsb left join fina_jobs fj on bsb.jobid=fj.id left join bus_shipping bs on bsb.jobid=bs.jobid
		String dmlSql = "UPDATE bus_shipping SET sono = NULL WHERE jobid IN " +
				"(select jobid from bus_ship_booking where cast(id as text)  IN (select regexp_split_to_table('" + StrUtils.array2List(ids) + "', ',') ));";
		dmlSql += "\n UPDATE bus_ship_booking SET jobid=null WHERE id IN(" + StrUtils.array2List(ids) + ");";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
	}

	//收回
	public static void untieSO(String[] ids, ServiceContext serviceContext) {
		String dmlSql = "UPDATE bus_ship_booking SET salesid=null , assignuserid = NULL , assigntime = NULL WHERE id IN(" + StrUtils.array2List(ids) + ");";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
	}


	@SaveState
	@Bind
	public String[] ids1;

	@Action
	public void savesaleid() {
		if (StrUtils.isNull(saleid)) {
			MessageUtils.alert("需要选择分派人！");
			return;
		}
		String sql0 = "UPDATE bus_ship_booking SET salesid=" + saleid + ", assignuserid = " + AppUtils.getUserSession().getUserid() + " ,assigntime = NOW() WHERE id IN(" + StrUtils.array2List(ids1) + ");";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql0);

		if (!StrUtils.isNull(this.nos)) {
			List<BusShipping> busShippingList = serviceContext.busShippingMgrService.busShippingDao.findAllByClauseWhere(" isdelete = false AND nos ='" + this.nos + "'");
			if (busShippingList.isEmpty()) {
				MessageUtils.alert("请输入有效海运单工作号！");
				return;
			}

			BusShipping busShipping = busShippingList.get(0);
			String sql1 = "UPDATE bus_ship_book_cnt SET jobid=" + busShipping.getJobid() + " WHERE linkid IN(" + StrUtils.array2List(ids1) + ");";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql1);

			String sql2 = "SELECT f_bus_booking_sync_jobs('bookingid=" + ids1[0] + ";jobid=" + busShipping.getJobid() + ";userid=" + AppUtils.getUserSession().getUserid() + "')";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql2);
		}

		editWindow.close();
		saleid = "";
		Browser.execClientScript("$('#sales_input').val('')");
		nos = "";
		MessageUtils.alert("OK");
		this.refresh();
	}

	@Override
	public void refresh() {
		init();
		super.refresh();
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
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
		update.markUpdate(true, UpdateLevel.Data, "shipquerycfg");
	}

	@Action
	public void empty() {
		this.shipcarrier = "";
		update.markUpdate(true, UpdateLevel.Data, "shipquerycfg");
	}

	public void clearQryKey() {
		super.clearQryKey();
		empty();
	}

	@Action
	public void ftSubscribe() {
		StringBuffer stringBuffer = new StringBuffer();
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选记录！");
			return;
		}
		try {
			for (String id : ids) {
				String querySql = "SELECT (SELECT COALESCE(UPPER(x.code),'') FROM sys_corporation x WHERE id = b.carrierid) AS carrier , sono FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.carrierid IS " +
						"NOT NULL AND b.id =" + id + " ;";
				Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				String sono = StrUtils.getMapVal(map, "sono");
				String carrierCode = StrUtils.getMapVal(map, "carrier");

				FtUtils.handleFt(stringBuffer, sono, "", carrierCode);
			}
			MessageUtils.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		// LogBean.insertLastingLog2(stringBuffer, "ftSubscribe_sonoBean");
	}
}
