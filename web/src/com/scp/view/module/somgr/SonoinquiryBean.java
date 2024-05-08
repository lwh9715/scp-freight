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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipBooking;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.price.ShipQueryBean;


@ManagedBean(name = "pages.module.somgr.sonoinquiryBean", scope = ManagedBeanScope.REQUEST)
public class SonoinquiryBean extends GridView {
    @SaveState
    @Accessible
    public BusShipBooking selectedRowData = new BusShipBooking();

    @SaveState
    @Accessible
    @Bind
    public Long currentUserid;

    @Bind
    @SaveState
    public String ves;

    @Bind
    @SaveState
    public String voy;

    @Bind
    @SaveState
    private boolean isdate = false;
    @Bind
    @SaveState
    private String dates;
    @Bind
    @SaveState
    private String dateastart;
    @Bind
    @SaveState
    private String dateend;

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            currentUserid = AppUtils.getUserSession().getUserid();

            Calendar calendar = Calendar.getInstance();
            Date date = new Date();//取时间
            calendar.setTime(date); //需要将date数据转移到Calender对象中操作
            calendar.add(calendar.DATE, -10);//把日期往后增加n天.正数往后推,负数往前移动
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            dateastart = new SimpleDateFormat("yyyy-MM-dd").format(date) + " 00:00";
            dateend = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59";
        }
        super.beforeRender(isPostBack);
        super.applyGridUserDef();
    }

    @Override
    public void refresh() {
        super.refresh();
        update.markUpdate(true, UpdateLevel.Data, "gridPanel");
    }


    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);

        String filter1 = "";
        //高级检索中日期区间查询拼接语句
        if (isdate) {
            if(!StrUtils.isNull(dates)){
                filter1 += "\nAND " + dates + "::timestamptz BETWEEN '"
                        + (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
                        + "' AND '"
                        + (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
                        + "'";
            }
        }
        if (!StrUtils.isNull(shipcarrier)) {
            filter1 += "\n AND cast(carrierid as text) IN (select regexp_split_to_table('" + shipcarrierids + "', ',') )";
        }
        if (!StrUtils.isNull(ves)) {
            filter1 += "\nAND (ves0 ILIKE '%'|| '" + ves + "' ||'%' or ves1 ILIKE '%'|| '" + ves + "' ||'%')";
        }
        if (!StrUtils.isNull(voy)) {
            filter1 += "\nAND (voy0 ILIKE '%'|| '" + voy + "' ||'%' or voy1 ILIKE '%'|| '" + voy + "' ||'%')";
        }

        m.put("filter", filter1);
        return m;
    }






    @Bind
    public UIWindow showbookingWindow;

    @Bind
    public UIIFrame bookingIframe;

    @Override
    public void grid_ondblclick() {
        String[] ids = this.grid.getSelectedIds();
        String thisid = ids[0].split("_")[0];
        this.selectedRowData=serviceContext.busBookingMgrService.busShipBookingDao.findById(Long.valueOf(thisid));
        super.grid_ondblclick();
        String winId = "_edit_booking";
        String url = "./booking.xhtml?id="+this.selectedRowData.getId()+"&r="+ AppUtils.base64Encoder("edit");
        bookingIframe.load(url);
        showbookingWindow.show();
    }

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
        dateastart="";
        dateend="";
        ves="";
        voy="";
        qryMap.clear();

        update.markUpdate(true, UpdateLevel.Data, "shipquerycfg1");
    }

    @Action
    public void empty() {
        this.shipcarrier = "";
        shipcarrierids = "";
        update.markUpdate(true, UpdateLevel.Data, "shipquerycfg1");
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

}
