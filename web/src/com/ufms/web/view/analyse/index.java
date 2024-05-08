package com.ufms.web.view.analyse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;

@WebServlet("/analyse_index")
@ManagedBean(name = "pages.module.analyse.index")
public class index extends BaseServlet {

    /**
     * 海上航线分析
     *
     * @param request
     * @return
     */
    @Action(method = "get_index_data")
    public String get_t_volume_data(HttpServletRequest request) {
        String anayear = request.getParameter("anayear");
        String companyId = request.getParameter("companyid");
        if (StrUtils.isNull(companyId)) {
            companyId = "0";
        }
        Calendar now = Calendar.getInstance();
        String compaYear1 = (now.get(Calendar.YEAR) - 1) + "";
        String compaYear2 = now.get(Calendar.YEAR) + "";
        if (!StrUtils.isNull(anayear) && anayear.split(";").length > 1) {
            compaYear1 = anayear.split(";")[0];
            compaYear2 = anayear.split(";")[1];
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT f_analyse_index_data('");
        sql.append("compayear1=" + compaYear1.trim() + ";");
        sql.append("compayear2=" + compaYear2.trim() + ";");
        sql.append("companyid=" + companyId.trim() + ";");
        sql.append("') as json;");

        JSONObject obj = new JSONObject();
        String indexData = "[]";
        try {
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
            indexData = StrUtils.getMapVal(map, "json");
            obj = data_init(obj, indexData, compaYear1, compaYear2, now, "0".equals(companyId) ? true : false);//补充数据
            obj.put("compayear1", compaYear1);
            obj.put("compayear2", compaYear2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 查看系统服务号
     *
     * @param request
     * @return
     */
    @Action(method = "get_sys_config")
    public String get_sys_config(HttpServletRequest request) {
        String sys_config = "";
        String sql = "SELECT f_sys_config('CSNO') AS sys_config";
        try {
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            sys_config = String.valueOf(map.get("sys_config"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sys_config;
    }

    /**
     * 补充全揽视图数据
     *
     * @param obj
     * @param indexData
     * @param compaYear1
     * @param compaYear2
     * @param now
     * @return 视图数据
     * @throws UnsupportedEncodingException
     */
    private JSONObject data_init(JSONObject obj, String indexData,
                                 String compaYear1, String compaYear2, Calendar now, boolean iscomp) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        JSONObject json = JSONObject.fromObject(indexData);

        obj.put("company", null == json.getString("company") ? "" : json.getString("company"));//补充左侧1部分，分公司下拉框数据

        //补充左侧2部分
        JSONArray year = StrUtils.isNull(json.getString("year")) ? new JSONArray() : JSONArray.fromObject(json.getString("year"));
        JSONArray quarter = StrUtils.isNull(json.getString("quarter")) ? new JSONArray() : JSONArray.fromObject(json.getString("quarter"));
        JSONArray month = StrUtils.isNull(json.getString("month")) ? new JSONArray() : JSONArray.fromObject(json.getString("month"));
        JSONArray week = StrUtils.isNull(json.getString("week")) ? new JSONArray() : JSONArray.fromObject(json.getString("week"));
        obj = data_init_left1(obj, compaYear1, compaYear2, now, year, quarter, month, week);

        //补充左侧3部分
        if (StrUtils.isNull(json.getString("bookingToday"))) {
            obj.put("bookingToday", "");
        } else {
            JSONArray bookingToday = StrUtils.isNull(json.getString("bookingToday")) ? new JSONArray() : JSONArray.fromObject(json.getString("bookingToday"));
            JSONArray bookingCompany = StrUtils.isNull(json.getString("bookingCompany")) ? new JSONArray() : JSONArray.fromObject(json.getString("bookingCompany"));
            JSONArray bookingDept = StrUtils.isNull(json.getString("bookingDept")) ? new JSONArray() : JSONArray.fromObject(json.getString("bookingDept"));
            JSONArray bookingLine = StrUtils.isNull(json.getString("bookingLine")) ? new JSONArray() : JSONArray.fromObject(json.getString("bookingLine"));
            obj = data_init_left2(obj, bookingToday, bookingCompany, bookingDept, bookingLine);
        }

        //补充左侧4部分
        if (StrUtils.isNull(json.getString("route"))) {
            obj.put("route", "");
        } else {
            JSONArray route = JSONArray.fromObject(json.getString("route"));
            obj = data_init_left3(obj, route);
        }

        //补充中心1部分
        JSONArray map = StrUtils.isNull(json.getString("map")) ? new JSONArray() : JSONArray.fromObject(json.getString("map"));
        obj = data_init_inner1(obj, compaYear1, compaYear2, now, map);

        //补充中心2部分
        if (StrUtils.isNull(json.getString("shipper"))) {
            obj.put("shipper", "");
        } else {
            JSONArray shipper = JSONArray.fromObject(json.getString("shipper"));
            obj = data_init_inner2(obj, shipper);
        }

        //补充右侧1部分
        obj = data_init_right1(obj, compaYear1, compaYear2, now, year, quarter, month, week);

        //补充右侧2部分
        JSONArray anaquarter = StrUtils.isNull(json.getString("anaquarter")) ? new JSONArray() : JSONArray.fromObject(json.getString("anaquarter"));
        JSONArray anamonth = StrUtils.isNull(json.getString("anamonth")) ? new JSONArray() : JSONArray.fromObject(json.getString("anamonth"));
        JSONArray anaweek = StrUtils.isNull(json.getString("anaweek")) ? new JSONArray() : JSONArray.fromObject(json.getString("anaweek"));
        obj = data_init_right2(obj, compaYear1, compaYear2, now, year, anaquarter, anamonth, anaweek);

        //补充右侧3部分
        if (StrUtils.isNull(json.getString("bycust"))) {
            obj.put("bycust", "");
        } else {
            JSONArray cust = JSONArray.fromObject(json.getString("bycust"));
            obj = data_init_right3(obj, cust);
        }

        //补充右侧4部分
        if (StrUtils.isNull(json.getString("bycust"))) {
            obj.put("compordept", "");
            return obj;
        } else {
            JSONArray cust = new JSONArray();
            if (iscomp) {
                cust = JSONArray.fromObject(json.getString("comp"));
            } else {
                cust = JSONArray.fromObject(json.getString("dept"));
            }
            obj = data_init_right4(obj, iscomp, cust);
        }
        return obj;
    }

    private JSONObject data_init_left1(JSONObject obj,
                                       String compaYear1, String compaYear2, Calendar now, JSONArray year, JSONArray quarter, JSONArray month, JSONArray week) {

        JSONObject newJson = new JSONObject();
        String yearCnts = "0";
        String quarCnts = "0";
        String monthCnts = "0";
        String weekCnts = "0";
        for (int i = 0; i < year.size(); i++) {
            if (null != year.getJSONObject(i) && StrUtils.isNull(year.getJSONObject(i).getString("cnts")) == false) {
                if (year.getJSONObject(i).getString("jobdate").contains(compaYear2) && "null".equals(ObjectUtils.toString(year.getJSONObject(i).getString("cnts"), "")) == false) {
                    yearCnts = year.getJSONObject(i).getString("cnts");
                    break;
                }
            }
        }
        for (int i = 0; i < quarter.size(); i++) {
            if (null != quarter.getJSONObject(i) && StrUtils.isNull(quarter.getJSONObject(i).getString("cnts")) == false) {
                if (quarter.getJSONObject(i).getString("jobdate").contains(compaYear2) && "null".equals(ObjectUtils.toString(quarter.getJSONObject(i).getString("cnts"), "")) == false) {
                    quarCnts = quarter.getJSONObject(i).getString("cnts");
                    break;
                }
            }
        }
        for (int i = 0; i < month.size(); i++) {
            if (null != month.getJSONObject(i) && StrUtils.isNull(month.getJSONObject(i).getString("cnts")) == false) {
                if (month.getJSONObject(i).getString("jobdate").contains(compaYear2) && "null".equals(ObjectUtils.toString(month.getJSONObject(i).getString("cnts"), "")) == false) {
                    monthCnts = month.getJSONObject(i).getString("cnts");
                    break;
                }
            }
        }
        for (int i = 0; i < week.size(); i++) {
            if (null != week.getJSONObject(i) && StrUtils.isNull(week.getJSONObject(i).getString("cnts")) == false) {
                if (week.getJSONObject(i).getString("jobdate").contains(compaYear2) && "null".equals(ObjectUtils.toString(week.getJSONObject(i).getString("cnts"), "")) == false) {
                    weekCnts = week.getJSONObject(i).getString("cnts");
                    break;
                }
            }
        }

        newJson.put("year", yearCnts);
        newJson.put("quarter", quarCnts);
        newJson.put("month", monthCnts);
        newJson.put("week", weekCnts);
        obj.put("leftdata1", newJson);
        return obj;
    }

    private JSONObject data_init_left2(JSONObject obj, JSONArray bookingToday, JSONArray bookingCompany, JSONArray bookingDept, JSONArray bookingLine) {

        JSONObject initArray = new JSONObject();

        for (int i = 0; i < bookingCompany.size(); i++) {
            if ("中集世联达".equals(ObjectUtils.toString(bookingCompany.getJSONObject(i).getString("name"), ""))) {
                bookingCompany.remove(i);
                for (int j = 0; j < bookingDept.size(); j++) {
                    bookingCompany.add(bookingDept.getJSONObject(j));
                }
                break;
            }
        }

        initArray.put("bookingToday", bookingToday);

        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < bookingCompany.size(); i++) {
            jsonValues.add(bookingCompany.getJSONObject(i));
        }

        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                Integer aStr = a.getInt("value");
                Integer bStr = b.getInt("value");
                return -aStr.compareTo(bStr);
            }
        });

        for (int i = 0; i < bookingCompany.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }

        String[] company = new String[sortedJsonArray.size() > 10 ? 11 : sortedJsonArray.size()];
        String[] value = new String[sortedJsonArray.size() > 10 ? 11 : sortedJsonArray.size()];
        int v = 0;
        int j = 0;
        for (int i = 0; i < sortedJsonArray.size(); i++) {
            if ("null".equals(ObjectUtils.toString(sortedJsonArray.getJSONObject(i).getString("name"), "")) || "null".equals(ObjectUtils.toString(sortedJsonArray.getJSONObject(i).getString("value"), ""))) {
                continue;
            }
            if (j < 10) {
                company[j] = String.valueOf(sortedJsonArray.getJSONObject(i).get("name"));
                value[j] = String.valueOf(sortedJsonArray.getJSONObject(i).getString("value"));
                j++;
            } else {
                v += sortedJsonArray.getJSONObject(i).getInt("value");
                j = 11;
            }
        }

        String[] newCompany = new String[j];
        String[] newvalue = new String[j];
        for (int i = 0; i < company.length; i++) {
            if (null != company[i] && "null".equals(company[i]) == false) {
                newCompany[i] = company[i];
                newvalue[i] = value[i];
            }
        }
        if (j > 10) {
            newCompany[j - 1] = "other";
            newvalue[j - 1] = v + "";
        }

        initArray.put("Company", newCompany);
        initArray.put("CompanyValue", newvalue);


        JSONArray array1 = new JSONArray();
        JSONObject other1 = new JSONObject();
        int v1 = 0;
        int j1 = 0;
        for (int i = 0; i < bookingLine.size(); i++) {
            if ("null".equals(ObjectUtils.toString(bookingLine.getJSONObject(i).get("name"), "")) || "null".equals(ObjectUtils.toString(bookingLine.getJSONObject(i).getString("value"), ""))) {
                continue;
            }
            if (j1 < 10) {
                array1.add(bookingLine.getJSONObject(i));
                j1++;
            } else {
                v1 += bookingLine.getJSONObject(i).getInt("value");
                j1 = 11;
            }
        }
        if (v1 > 0) {
            other1.put("name", "其他");
            other1.put("value", v1);
            array1.add(other1);
        }
        initArray.put("bookingLine", array1);

        obj.put("booking", initArray);
        return obj;
    }

    private JSONObject data_init_left3(JSONObject obj, JSONArray route) {
//		JSONObject json = JSONObject.fromObject(indexData);
//		if(StrUtils.isNull(json.getString("route"))){
//			obj.put("route", "");
//			return obj;
//		}
//		JSONArray route = JSONArray.fromObject(json.getString("route"));
        JSONArray array = new JSONArray();
        JSONObject other = new JSONObject();
        int v = 0;
        int j = 0;
        for (int i = 0; i < route.size(); i++) {
            if ("null".equals(ObjectUtils.toString(route.getJSONObject(i).get("name"), "")) || "null".equals(ObjectUtils.toString(route.getJSONObject(i).getString("value"), ""))) {
                continue;
            }
            if (j < 10) {
                array.add(route.getJSONObject(i));
                j++;
            } else {
                v += route.getJSONObject(i).getInt("value");
                j = 11;
            }
        }
        if (v > 0) {
            other.put("name", "其他");
            other.put("value", v);
            array.add(other);
        }
        obj.put("route", array);//补充左侧3部分
        return obj;
    }

    private JSONObject data_init_inner1(JSONObject obj, String compaYear1, String compaYear2, Calendar now, JSONArray map) {
        JSONObject geoCoordMap = new JSONObject();
        JSONObject tvalue = new JSONObject();
        JSONArray arrayData = new JSONArray();
        int j = 0;
        String name = "";
        JSONArray array = new JSONArray();
        JSONArray array1 = new JSONArray();
        for (int i = 0; i < map.size(); i++) {
            if ("null".equals(ObjectUtils.toString(map.get(i), "")) || StrUtils.isNull(map.getString(i))) {
                continue;
            }
            if ("null".equals(ObjectUtils.toString(map.getJSONObject(i).get("namec"), ""))) {
                continue;
            }
            if ("null".equals(ObjectUtils.toString(map.getJSONObject(i).get("value"), "")) || map.getJSONObject(i).getInt("value") <= 0) {
                continue;
            }
            String namec = map.getJSONObject(i).getString("namec");
            int value = "null".equals(ObjectUtils.toString(map.getJSONObject(i).get("value"), "")) ? 0 : map.getJSONObject(i).getInt("value");//判断非空
            String tude = null == map.getJSONObject(i).get("tude") ? "" : map.getJSONObject(i).getString("tude");
            String tude2 = null == map.getJSONObject(i).get("tude2") ? "" : map.getJSONObject(i).getString("tude2");
            String country = map.getJSONObject(i).getString("country");//组装数据必带","不需要判断非空
            //数据拼接，格式：  '美国蒙特里': [-121.874729,36.541478 ]
            geoCoordMap.put(namec, "[" + tude.split(":")[0] + "]");
            geoCoordMap.put(country, "[" + tude2.split(":")[0] + "]");
            JSONObject object = new JSONObject();

            if (tvalue.containsKey(namec)) {//准备航线地图T量数据
                tvalue.put(namec, value + tvalue.getInt(namec));
            } else {
                tvalue.put(namec, value);
            }
            if (tvalue.containsKey(country)) {//准备航线地图T量数据
                tvalue.put(country, value + tvalue.getInt(country));
            } else {
                tvalue.put(country, value);
            }
            if (i == 0) {
                object.put("name", namec);
                array1.add(object);
                object = new JSONObject();
                object.put("name", namec);
                value = (value / 50 + 30) > 120 ? 120 : (value / 50 + 30);//value关系坐标点大小，设置最大为90最小为30
                object.put("value", value);
                array1.add(object);
                object = new JSONObject();
                array.add(array1);
                object = new JSONObject();

                name = namec;
            } else {
                name = map.getJSONObject(i - 1).getString("namec");
            }
            if (name.equals(namec)) {
                array1 = new JSONArray();
                object.put("name", namec);
                array1.add(object);
                object = new JSONObject();
                object.put("name", country);
                object.put("value", 30);
                array1.add(object);
                object = new JSONObject();
                array.add(array1);
                array1 = new JSONArray();
            } else {
                arrayData.add(array);
                array = new JSONArray();
                array1 = new JSONArray();

                object = new JSONObject();
                object.put("name", namec);
                array1.add(object);
                object = new JSONObject();
                object.put("name", namec);
                value = (value / 50 + 30) > 120 ? 120 : (value / 50 + 30);//value关系坐标点大小，设置最大为90最小为30
                object.put("value", value);
                array1.add(object);
                object = new JSONObject();
                array.add(array1);
                object = new JSONObject();
            }

            if (i == map.size() - 1) {
                arrayData.add(array);
            }
        }
        obj.put("geoCoordMap", geoCoordMap);
        obj.put("map", arrayData);
        obj.put("tvalue", tvalue);
        return obj;
    }

    private JSONObject data_init_inner2(JSONObject obj, JSONArray shipper) {
        String[] ships = new String[shipper.size() > 10 ? 11 : shipper.size()];
        String[] value = new String[shipper.size() > 10 ? 11 : shipper.size()];
        int v = 0;
        int j = 0;
        for (int i = 0; i < shipper.size(); i++) {
            if ("null".equals(ObjectUtils.toString(shipper.getJSONObject(i).getString("ship"), "")) || "null".equals(ObjectUtils.toString(shipper.getJSONObject(i).getString("value"), ""))) {
                continue;
            }
            if (j < 10) {
                ships[j] = String.valueOf(shipper.getJSONObject(i).get("ship"));
                value[j] = String.valueOf(shipper.getJSONObject(i).getString("value"));
                j++;
            } else {
                v += shipper.getJSONObject(i).getInt("value");
                j = 11;
            }
        }
        String[] newShip = new String[j];//由于可能存在NULL值，需要过滤掉
        String[] newvalue = new String[j];
        for (int i = 0; i < ships.length; i++) {
            if (null != ships[i] && "null".equals(ships[i]) == false) {
                newShip[i] = ships[i];
                newvalue[i] = value[i];
            }
        }
        if (j > 10) {
            newShip[j - 1] = "other";
            newvalue[j - 1] = v + "";
        }

        obj.put("shipper", newShip);
        obj.put("shipvalue", newvalue);
        return obj;
    }

    private JSONObject data_init_right1(JSONObject obj, String compaYear1, String compaYear2, Calendar now, JSONArray year, JSONArray quarter, JSONArray month, JSONArray week) {
        JSONObject initArray = new JSONObject();
        JSONObject initJson = null;
        int[] cnts1 = {0, 0, 0, 0};
        int[] cnts2 = {0, 0, 0, 0};
        String[] compaYears1 = {compaYear1, compaYear1 + "-" + (int) (Math.ceil((now.get(Calendar.MONTH) + 1) / 3.0)), compaYear1 + "-" + ((now.get(Calendar.MONTH) + 1) > 9 ? (now.get(Calendar.MONTH) + 1) : "0" + (now.get(Calendar.MONTH) + 1)), compaYear1 + "-" + (now.get(Calendar.WEEK_OF_YEAR) - 1)};
        String[] compaYears2 = {compaYear2, compaYear2 + "-" + (int) (Math.ceil((now.get(Calendar.MONTH) + 1) / 3.0)), compaYear2 + "-" + ((now.get(Calendar.MONTH) + 1) > 9 ? (now.get(Calendar.MONTH) + 1) : "0" + (now.get(Calendar.MONTH) + 1)), compaYear2 + "-" + (now.get(Calendar.WEEK_OF_YEAR) - 1)};

        JSONArray array = new JSONArray();
        array.add(year);
        array.add(quarter);
        array.add(month);
        array.add(week);
        for (int i = 0; i < 4; i++) {
            int j = 0;
            //ObjectUtils.toString(array.get(i), "") 特殊情况 null无法判断array.get(i)是否为空，需要先将值转为String再判断
            if (null != array.get(i) && jsonIsNull(array.getJSONArray(i))) {
                if (compaYears1[i].equals(array.getJSONArray(i).getJSONObject(j).getString("jobdate"))) {
                    cnts1[i] = StrUtils.isNull(array.getJSONArray(i).getJSONObject(j).getString("cnts")) ? 0 : Integer.valueOf(array.getJSONArray(i).getJSONObject(j).getString("cnts"));
                    j++;
                    if (array.getJSONArray(i).size() == 1) {
                        continue;
                    }
                }
                if (compaYears2[i].equals(array.getJSONArray(i).getJSONObject(j).getString("jobdate"))) {
                    //判断非空
                    if ("null".equals(ObjectUtils.toString(array.getJSONArray(i).getJSONObject(j).get("cnts"), "")) || StrUtils.isNull(array.getJSONArray(i).getJSONObject(j).getString("cnts"))) {

                        cnts2[i] = 0;
                    } else {
                        cnts2[i] = Integer.valueOf(array.getJSONArray(i).getJSONObject(j).getString("cnts"));

                    }
                }
            }
        }
        //day365部分
        initJson = new JSONObject();
        initJson.put("cnts1", compaYears1[0] + "年: " + cnts1[0]);
        initJson.put("cnts2", compaYears2[0] + "年: " + cnts2[0]);
        initArray.put("day365", initJson);

        //day90部分
        initJson = new JSONObject();
        initJson.put("cnts1", compaYears1[1] + "季: " + cnts1[1]);
        initJson.put("cnts2", compaYears2[1] + "季: " + cnts2[1]);
        initArray.put("day90", initJson);

        //day30部分
        initJson = new JSONObject();
        initJson.put("cnts1", compaYears1[2] + "月: " + cnts1[2]);
        initJson.put("cnts2", compaYears2[2] + "月: " + cnts2[2]);
        initArray.put("day30", initJson);

        //day7部分
        initJson = new JSONObject();
        initJson.put("cnts1", compaYears1[3] + "周: " + cnts1[3]);
        initJson.put("cnts2", compaYears2[3] + "周: " + cnts2[3]);
        initArray.put("day7", initJson);
        obj.put("rightdata1", initArray);

        return obj;
    }

    private JSONObject data_init_right2(JSONObject obj,
                                        String compaYear1, String compaYear2, Calendar now, JSONArray year, JSONArray anaquarter, JSONArray anamonth, JSONArray anaweek) {
        JSONObject initArray = new JSONObject();
        //年对比
        int[] year1 = {0, 0};
        if (year.size() == 2) {
            year1[0] = year.size() == 1 ? 0 : year.getJSONObject(0).getInt("cnts");
            year1[1] = year.size() == 1 ? year.getJSONObject(0).getInt("cnts") : year.getJSONObject(1).getInt("cnts");
        }
        if (year.size() == 1) {
            if (compaYear1.equals(year.getJSONObject(0).getString("jobdate"))) {
                year1[0] = StrUtils.isNull(year.getJSONObject(0).getString("cnts")) ? 0 : Integer.valueOf(year.getJSONObject(0).getString("cnts"));
            } else {
                year1[1] = StrUtils.isNull(year.getJSONObject(0).getString("cnts")) ? 0 : Integer.valueOf(year.getJSONObject(0).getString("cnts"));
            }
        }
        initArray.put("year", year1);
        //季度对比
        int[] quarter1 = {0, 0, 0, 0};
        int[] quarter2 = {0, 0, 0, 0};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < anaquarter.size(); j++) {
                String jobdate = anaquarter.getJSONObject(j).getString("jobdate");
                String cnts = String.valueOf("null".equals(ObjectUtils.toString(anaquarter.getJSONObject(j).get("cnts"), "")) ? 0 : anaquarter.getJSONObject(j).get("cnts"));
                if (null != jobdate && jobdate.equals(compaYear1 + "-" + (i + 1))) {
                    quarter1[i] = null == cnts ? 0 : Integer.valueOf(cnts);
                }
                if (null != jobdate && jobdate.equals(compaYear2 + "-" + (i + 1))) {//
                    quarter2[i] = Integer.valueOf(cnts);
                    break;
                }
            }
        }
        int[][] quarters = {quarter1, quarter2};
        initArray.put("quarter", quarters);
        //月对比
        int[] month1 = new int[12];
        int[] month2 = new int[12];
        String m = "";
        for (int i = 0; i < 12; i++) {
            if (i > 8) {
                m = (i + 1) + "";
            } else {
                m = "0" + (i + 1);
            }
            for (int j = 0; j < anamonth.size(); j++) {
                String jobdate = anamonth.getJSONObject(j).getString("jobdate");
                String cnts = String.valueOf("null".equals(ObjectUtils.toString(anamonth.getJSONObject(j).get("cnts"), "")) ? 0 : anamonth.getJSONObject(j).get("cnts"));
                if (null != jobdate && jobdate.equals(compaYear1 + "-" + m)) {
                    month1[i] = null == cnts ? 0 : Integer.valueOf(cnts);
                }
                if (null != jobdate && jobdate.equals(compaYear2 + "-" + m)) {//
                    month2[i] = null == cnts ? 0 : Integer.valueOf(cnts);
                    break;
                }
            }
        }
        int[][] months = {month1, month2};
        initArray.put("month", months);
        //周对比
        int[] week1 = new int[54];
        int[] week2 = new int[54];
        String w = "";
        for (int i = 0; i < 54; i++) {
            if (i > 8) {
                m = (i + 1) + "";
            } else {
                m = "0" + (i + 1);
            }
            for (int j = 0; j < anaweek.size(); j++) {
                String jobdate = anaweek.getJSONObject(j).getString("jobdate");
                String cnts = String.valueOf("null".equals(ObjectUtils.toString(anaweek.getJSONObject(j).get("cnts"), "")) ? 0 : anaweek.getJSONObject(j).get("cnts"));

                if (null != jobdate && jobdate.equals(compaYear1 + "-" + m)) {
                    week1[i] = null == cnts ? 0 : Integer.valueOf(cnts);
                }
                if (null != jobdate && jobdate.equals(compaYear2 + "-" + m)) {//
                    week2[i] = null == cnts ? 0 : Integer.valueOf(cnts);
                    break;
                }
            }
        }
        int[][] weeks = {week1, week2};
        initArray.put("week", weeks);
        obj.put("rightdata2", initArray);
        return obj;
    }

    private JSONObject data_init_right3(JSONObject obj, JSONArray cust) throws UnsupportedEncodingException {
        JSONArray array = new JSONArray();
        JSONObject newjson = new JSONObject();
        newjson.put("同行", 0);
        newjson.put("直客", 0);
        newjson.put("代理", 0);
        for (int i = 0; i < cust.size(); i++) {
            String type = "代理";
            if ("null".equals(ObjectUtils.toString(cust.get(i), "")) || null == cust.getJSONObject(i) || "null".equals(ObjectUtils.toString(cust.getJSONObject(i).get("name"), ""))) {
                continue;
            }
            if ("AB".equals(cust.getJSONObject(i).get("name"))) {
                String a = "";
            }
            if ("isdirect".equals(cust.getJSONObject(i).get("customertype"))) {
                type = "直客";
            } else if ("istogether".equals(cust.getJSONObject(i).get("customertype"))) {
                type = "同行";
            } else {
                if ("null".equals(ObjectUtils.toString(cust.getJSONObject(i).get("name"), ""))) {
                    continue;
                }
                char[] charArray = String.valueOf(cust.getJSONObject(i).get("name")).toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    if (String.valueOf(charArray[j]).getBytes("UTF-8").length > 1) {
                        type = "直客";
                        break;
                    }
                }
            }
            int value = newjson.getInt(type) + cust.getJSONObject(i).getInt("value");
            newjson.put(type, value);
        }

        JSONObject json2 = new JSONObject();
        json2.put("name", "同行");
        json2.put("value", newjson.getInt("同行"));
        array.add(json2);
        json2 = new JSONObject();
        json2.put("name", "直客");
        json2.put("value", newjson.getInt("直客"));
        array.add(json2);
        json2 = new JSONObject();
        json2.put("name", "代理");
        json2.put("value", newjson.getInt("代理"));
        array.add(json2);
        obj.put("rightdata3", array);
        return obj;
    }

    private JSONObject data_init_right4(JSONObject obj, boolean iscomp, JSONArray cust) {
        JSONArray array = new JSONArray();
        JSONObject other = new JSONObject();

        int v = 0;
        for (int i = 0; i < cust.size(); i++) {
            if (i < 10) {
                array.add(cust.getJSONObject(i));
            } else {
                v += cust.getJSONObject(i).getInt("value");
            }
        }
        if (v > 0) {
            other.put("name", "其他");
            other.put("value", v);
            array.add(other);
        }
        obj.put("rightdata4", array);
        obj.put("iscomp", iscomp);
        return obj;
    }

    public boolean jsonIsNull(JSONArray json) {
        if ("null".equals(ObjectUtils.toString(json, "")) == false && null != json && json.size() > 0) {
            return true;
        }
        return false;
    }

}

