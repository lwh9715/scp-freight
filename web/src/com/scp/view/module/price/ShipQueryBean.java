package com.scp.view.module.price;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.comp.action.ActionGridExport;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.utils.HttpUtil;
import com.ufms.web.view.sysmgr.LogBean;

@ManagedBean(name = "pages.module.price.shipqueryBean", scope = ManagedBeanScope.REQUEST)
public class ShipQueryBean extends GridView {

    @Bind
    @SaveState
    public String polcode;

    @Bind
    @SaveState
    public String podcode;

    @Bind
    @SaveState
    public String etd;

    @Bind
    @SaveState
    public String shipcarrier;

    @Bind
    @SaveState
    public String searchDateType;

    @Bind
    @SaveState
    public String weeksOut;

    @Bind
    @SaveState
    public String directOnly;

    @Bind
    @SaveState
    public String includeNearbyOriginPorts;

    @Bind
    @SaveState
    public String includeNearbyDestinationPorts;

    @Bind
    @SaveState
    public String menuflag;

    @Bind
    @SaveState
    public String jobid;

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            etd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            searchDateType = "ByDepartureDate"; // ByDepartureDate	/	ByArrivalDate
            weeksOut = "4";


            //选船期页面
            String thismenuflag = AppUtils.getReqParam("menuflag");
            if ("xuanchuanqi".equals(thismenuflag)) {
                String thispolcode = AppUtils.getReqParam("polcode");
                String thispodcode = AppUtils.getReqParam("podcode");
                String thiscarrierid = AppUtils.getReqParam("carrierid");
                jobid = AppUtils.getReqParam("jobid");

                menuflag = thismenuflag;
                if (!isNull(thispolcode)) {
                    polcode = thispolcode;
                }
                if (!isNull(thispodcode)) {
                    podcode = thispodcode;
                }

                String sql = "select * FROM sys_corporation t WHERE 1=1 AND iscarrier = TRUE AND isdelete = false and remarks is not null and remarks !='' and id=" + thiscarrierid;
                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(sql);
                if (mapList != null && mapList.size() == 1) {
                    String remarks = String.valueOf(mapList.get(0).get("remarks"));
                    if (!isNull(remarks)) {
                        shipcarrier = remarks;
                    }
                }

                update.markUpdate(true, UpdateLevel.Data, "shipquerycfg");
            }
        }
    }


    /**
     * 数据显示构件，GRID
     */
    @Bind
    public UIDataGrid gridSchedule;

    @SaveState
    public int size = 100;

    @SaveState
    public Object[] elements = null;

    @Bind(id = "gridSchedule", attribute = "dataProvider")
    protected GridDataProvider getScheduleDataProvider() {
        return new GridDataProvider() {
            public Object[] getElements() {
                return elements;
            }

            public int getTotalCount() {
                return size;
            }
        };
    }


    @SaveState
    @Accessible
    public Map<String, Object> qryMapShipcarrier = new HashMap<String, Object>();

    @Bind
    public UIDataGrid gridShipcarrier;

    @Bind(id = "gridShipcarrier", attribute = "dataProvider")
    protected GridDataProvider getGridScheduleDataProvider() {
        return new GridDataProvider() {
            public Object[] getElements() {
                String sqlId = "pages.module.price.shipqueryBean.gridShipcarrier.page";
                return serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMapShipcarrier), start, limit).toArray();
            }

            public int getTotalCount() {
                String sqlId = "pages.module.price.shipqueryBean.gridShipcarrier.count";
                List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMapShipcarrier));
                Long count = (Long) list.get(0).get("counts");
                return count.intValue();
            }
        };
    }

    @Bind
    @SaveState
    public String qryshipcarrierdesc = "";

    public Map getQryClauseWhere2(Map<String, Object> queryMap) {
        Map map = getQryClauseWhere(queryMap);
        String qry = map.get("qry").toString();
        if (!isNull(qryshipcarrierdesc)) {
            qryshipcarrierdesc = StrUtils.getSqlFormat(qryshipcarrierdesc);
            qry += "AND (namec ILIKE '%" + qryshipcarrierdesc + "%' OR helpcode ILIKE '%" + qryshipcarrierdesc + "%')";
        }
        map.put("qry", qry);
        return map;
    }

    @SaveState
    public int starts = 0;

    @SaveState
    public int limits = 100;

    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        StringBuilder buffer = new StringBuilder();
        Map<String, String> map = new HashMap<String, String>();
        buffer.append("\n1=1 ");
        if (queryMap != null && queryMap.size() >= 1) {
            Set<String> set = queryMap.keySet();
            for (String key : set) {
                Object val = queryMap.get(key);
                String qryVal = "";

                if (val != null && !isNull(val.toString())) {
                    qryVal = val.toString();
                    if (val instanceof Date) {
                        Date dateVal = (Date) val;
                        long dateValLong = dateVal.getTime();
                        Date d = new Date(dateValLong);
                        Format format = new
                                SimpleDateFormat("yyyy-MM-dd");
                        String dVar = format.format(dateVal);
                        buffer.append("\nAND CAST(" + key + " AS DATE) ='"
                                + dVar + "'");
                    } else {
                        int index = key.indexOf("$");
                        if (index > 0) {
                            buffer.append("\nAND " + key.substring(0, index)
                                    + "=" + val);
                        } else {
                            val = val.toString().replaceAll("'", "''");
                            buffer.append("\nAND UPPER(" + key
                                    + ") LIKE UPPER('%'||" + "TRIM('" + val + "')" + "||'%')");
                        }
                    }
                }
            }
        }
        String qry = isNull(buffer.toString()) ? "" : buffer.toString();
        map.put("limit", limits + "");
        map.put("start", starts + "");
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
        for (String id : ids) {
            SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
            String helpcode = sysCorporation.getRemarks();
            if (isNull(this.shipcarrier)) {
                this.shipcarrier = helpcode;
            } else {
                if (!this.shipcarrier.contains(helpcode)) {
                    this.shipcarrier = this.shipcarrier + "," + helpcode;
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


    @Action
    public void qrySchudule() {
        try {
            if (isNull(polcode)) {  // CNSHK
                Browser.execClientScript("layer.msg('起运港不能为空',{time:10000});");
                return;
            }
            if (isNull(podcode)) { // USLAX
                Browser.execClientScript("layer.msg('目的港不能为空',{time:10000});");
                return;
            }
            if (isNull(etd)) {
                Browser.execClientScript("layer.msg('etd不能为空',{time:10000});");
                return;
            }
            if (isNull(weeksOut)) {
                Browser.execClientScript("layer.msg('周不能为空',{time:10000});");
                return;
            }

            String sql = "SELECT * FROM sys_config where isdelete='false' and key='sys_api_shipquery_url'  ";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            String sys_api_shipquery_url = String.valueOf(map.get("val"));
            String httpUrl0 = sys_api_shipquery_url + "/edi/inttra/api";
            String httpUrl1 = "method=getSchedule&polcode=" + polcode + "&podcode=" + podcode + "&etd=" + etd
                    + "&shipcarrier=" + shipcarrier + "&searchDateType=" + searchDateType + "&weeksOut=" + weeksOut
                    + "&directOnly=" + directOnly + "&includeNearbyOriginPorts=" + includeNearbyOriginPorts + "&includeNearbyDestinationPorts=" + includeNearbyDestinationPorts;

            LogBean.insertLog(new StringBuffer().append("船期查询开始,当前时间为" + new Date() + ",url为").append(httpUrl0).append("?").append(httpUrl1));
            String response = HttpUtil.sendGet(httpUrl0, httpUrl1);
            LogBean.insertLog(new StringBuffer().append("船期查询已获取数据,当前时间为" + new Date() + ",response为").append(response));

            String code = "";
            String message = "";
            if ("[]".equals(response)) {
                message = "inttra未查到有效船期数据!";
            } else if (response.contains("errorMessages")) {
                message = response.substring(response.indexOf("errorMessages\":[\"") + "errorMessages\":[\"".length(), response.lastIndexOf("\"]}"));
            } else if (response.contains("失败")) {
                message = response;
            } else {
                code = "10000";
                message = "查询成功!";
            }

            if ("10000".equals(code)) {
                Browser.execClientScript("layer.msg('" + message + "',{time:2000});");
                JSONArray jsonRespnses = JSONArray.fromObject(response);
                size = jsonRespnses.size();
                elements = jsonRespnses.toArray();

                //记录查询日志
                String userid = String.valueOf(AppUtils.getUserSession().getUserid());
                String polcodesql = "insert into sys_operationlog(id,userid,menuname,elementname,usevalue) values (getid()," + userid + ",'船期查询','polcode','" + polcode + "')";
                String podcodesql = "insert into sys_operationlog(id,userid,menuname,elementname,usevalue) values (getid()," + userid + ",'船期查询','podcode','" + podcode + "')";
                daoIbatisTemplate.updateWithUserDefineSql(polcodesql);
                daoIbatisTemplate.updateWithUserDefineSql(podcodesql);
            } else {
                Browser.execClientScript("layer.msg('" + message + "',{time:10000});");
                size = 100;
                elements = null;
            }
            this.gridSchedule.reload();
        } catch (Exception e) {
            Browser.execClientScript("layer.msg('查询qrySchudule出错',{time:10000});");
            e.printStackTrace();
            LogBean.insertLog(new StringBuffer().append("船期查询失败,失败原因为").append(e.getMessage()));
        }
    }

    @Action
    public void clearQryKey() {
        Browser.execClientScript("$('#polDiv_input').val('');$('#podDiv_input').val('');");
        polcode = "";
        podcode = "";
        etd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        shipcarrier = "";
        weeksOut = "4";
        directOnly = "false";
        includeNearbyOriginPorts = "false";
        includeNearbyDestinationPorts = "false";
        update.markUpdate(true, UpdateLevel.Data, "shipquerycfg");
    }

    @Action
    public void exportThistable() {
        try {
            if (elements == null || elements.length == 0) {
                Browser.execClientScript("layer.msg('请先查询有效数据',{time:10000});");
                return;
            }

            ActionGridExport actionGridExport = new ActionGridExport();
            actionGridExport.setKeys((String) AppUtils.getReqParam("key"));
            actionGridExport.setVals((String) AppUtils.getReqParam("value"));
            List<Map> list = new ArrayList<Map>();
            for (Object object : elements) {
                JSONObject jsonObject = (JSONObject) object;
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                //循环转换
                Iterator it = jsonObject.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                    map.put(entry.getKey(), entry.getValue());
                }
                list.add(map);
            }

            actionGridExport.execute(list);
            Browser.execClientScript("simulateExport.fireEvent('click');");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void gridSchedule_ondblclick() {
        try {
            if ("xuanchuanqi".equals(menuflag)) {
                int thisnumber = this.gridSchedule.getSelections()[0];

                for (int i = 0; i < elements.length; i++) {
                    if (i == thisnumber) {
                        JSONObject thisjson = (JSONObject) elements[i];
                        String pol = thisjson.getString("originUnloc");
                        String pod = thisjson.getString("destinationUnloc");
                        String vessel = thisjson.getString("vesselName");
                        String voyage = thisjson.getString("voyageNumber");
                        String carrierName = thisjson.getString("carrierName");
                        String serviceName = thisjson.getString("serviceName");
                        String carrierid = "";
                        String etd = thisjson.getString("originDepartureDate");
                        String eta = thisjson.getString("destinationArrivalDate");
                        String vgmCutoff = thisjson.getString("vgmCutoff");

                        String sql = "select * FROM sys_corporation t WHERE 1=1 AND iscarrier = TRUE AND isdelete = false  and namec='" + carrierName + "'";
                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                        List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(sql);
                        if (mapList != null && mapList.size() == 1) {
                            String thiscarrierid = String.valueOf(mapList.get(0).get("id"));
                            if (!isNull(thiscarrierid)) {
                                carrierid = thiscarrierid;
                            }
                        }

                        //记录船期
                        String isHavaSaveSql = "select count(1) thissum from bus_shipschedule where 1=1 and isdelete = false ";
                        isHavaSaveSql += "and carrier = '" + carrierName + "'";
                        isHavaSaveSql += "and pol = '" + pol + "'";
                        isHavaSaveSql += "and pod = '" + pod + "'";
                        isHavaSaveSql += "and pod = '" + pod + "'";
                        isHavaSaveSql += "and ves = '" + vessel + "'";
                        isHavaSaveSql += "and voy = '" + voyage + "'";
                        isHavaSaveSql += "and vgm " + (isNull(vgmCutoff) ? "is null " : " = to_date('" + vgmCutoff + "','yyyy-MM-dd hh24:mi:ss')");
                        isHavaSaveSql += "and etd = " + "to_date('" + etd + "','yyyy-MM-dd hh24:mi:ss')";
                        isHavaSaveSql += "and eta = " + "to_date('" + eta + "','yyyy-MM-dd hh24:mi:ss')";
                        isHavaSaveSql += "and jobid = '" + jobid + "'";
                        List<Map> isHavaSaveList = daoIbatisTemplate.queryWithUserDefineSql(isHavaSaveSql);
                        if (isHavaSaveList != null && isHavaSaveList.size() == 1) {
                            String thissum = String.valueOf(isHavaSaveList.get(0).get("thissum"));
                            if ("0".equals(thissum)) {

                                String savesql = "INSERT INTO bus_shipschedule(id,carrier,pol,pod,ves,voy";
                                if (!isNull(vgmCutoff)) {
                                    savesql += ",vgm";
                                }
                                savesql += ",etd,eta,schtype,jobid,inputer,inputtime)";
                                savesql += "VALUES(getid(),'" + carrierName;
                                savesql += "','" + pol + "','" + pod + "','";
                                savesql += vessel + "','" + voyage + "'";
                                if (!isNull(vgmCutoff)) {
                                    savesql += ",to_date('" + vgmCutoff + "','yyyy-MM-dd hh24:mi:ss')";
                                }
                                savesql += ",to_date('" + etd + "','yyyy-MM-dd hh24:mi:ss')";
                                savesql += ",to_date('" + eta + "','yyyy-MM-dd hh24:mi:ss')";
                                savesql += ",'S'";
                                savesql += "," + jobid;
                                savesql += ",'xuanchuanqi'";
                                savesql += ",to_date('" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + "','yyyy-MM-dd hh24:mi:ss')";
                                savesql += ")";
                                daoIbatisTemplate.updateWithUserDefineSql(savesql);
                            }
                        }

                        //页面赋值
                        String txt = vessel + "_" + voyage + "_" + carrierid + "_" + etd + "_" + eta + "_" + serviceName;
                        Browser.execClientScript("setParentValue('" + txt + "')");

                        Browser.execClientScript("layer.msg('获取船期成功',{time:2000});");
                    }
                }
            }
        } catch (Exception e) {
            Browser.execClientScript("layer.msg('获取船期出错',{time:2000});");
            LogBean.insertLog(new StringBuffer().append("gridSchedule_ondblclick失败,失败原因为").append(e.getMessage()));
        }
    }

    public static boolean isNull(String var0) {
        return null == var0 || "".equals(var0.trim()) || "null".equals(var0.trim());
    }
}
