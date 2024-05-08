package com.scp.view.module.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.sys.SysUser;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * @author CIMC
 */

@WebServlet("/boxquantitynew2")

@ManagedBean(name = "pages.module.report.boxQuantityNew2Bean")
public class BoxQuantityNew2Bean extends BaseServlet {


    /**
     * 获取单条订单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "query")
    public BaseView BusCommerceJobId(HttpServletRequest request) {
        BaseView view = new BaseView();
        String thissql0 = "";
        String thissql1 = "";
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);


            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String userid = jsonObject.get("userid").toString();
            String dates = jsonObject.getJSONObject("query").get("dates").toString();
            String startdate = jsonObject.getJSONObject("query").get("startdateStr").toString();
            String enddate = jsonObject.getJSONObject("query").get("enddateStr").toString();
            SysUser sysUser = ((ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext")).sysUserAssignMgrService.sysUserDao.findById(Long.valueOf(userid));
            JSONArray columndefsJSONArray = jsonObject.getJSONArray("columndefs");


            String Afield = "";
            String Sfield = "";
            String Ofield = "";
            for (int i = 0; i < columndefsJSONArray.size(); i++) {
                JSONObject thisJSONObject = columndefsJSONArray.getJSONObject(i);
                if (thisJSONObject.containsValue("nos") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.nos\n";
                }
                if (thisJSONObject.containsValue("remarks") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.remarks\n";
                }
                if (thisJSONObject.containsValue("customerabbr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.customerabbr\n";
                }
                if (thisJSONObject.containsValue("refno") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.refno\n";
                }
                if (thisJSONObject.containsValue("isconfirm_pp") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isconfirm_pp\n";
                }
                if (thisJSONObject.containsValue("isconfirm_cc") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isconfirm_cc\n";
                }
                if (thisJSONObject.containsValue("iscomplete_ar") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.iscomplete_ar\n";
                }
                if (thisJSONObject.containsValue("iscomplete_ap") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.iscomplete_ap\n";
                }
                if (thisJSONObject.containsValue("isconfirm2_pp") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isconfirm2_pp\n";
                }
                if (thisJSONObject.containsValue("isconfirm2_cc") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isconfirm2_cc\n";
                }
                if (thisJSONObject.containsValue("isclc") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isclc\n";
                }
                if (thisJSONObject.containsValue("arstatus") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.arstatus\n";
                }
                if (thisJSONObject.containsValue("apstatus") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.apstatus\n";
                }
                if (thisJSONObject.containsValue("invstatus") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.invstatus\n";
                }
                if (thisJSONObject.containsValue("isinvoice") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isinvoice\n";
                }
                if (thisJSONObject.containsValue("isconfirm_bus") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,t.isconfirm_bus\n";
                }
                if (thisJSONObject.containsValue("releasetime") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,to_char( (SELECT releasetime FROM bus_customs WHERE jobid = t.id AND isdelete = FALSE order by nos LIMIT 1) ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS releasetime\n";
                }
                if (thisJSONObject.containsValue("singtime") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,to_char( (SELECT singtime  FROM bus_customs WHERE jobid = t.id AND isdelete = FALSE order by nos LIMIT 1) ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS singtime\n";
                }
                if (thisJSONObject.containsValue("custype") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT namec from dat_filedata WHERE isleaf = TRUE AND isdelete = false AND fkcode = 40 AND code =(SELECT custype  FROM bus_customs WHERE jobid = t.id AND clstype='O' AND isdelete = FALSE order by nos LIMIT 1)) AS custype\n";
                }
                if (thisJSONObject.containsValue("status") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT namec from dat_filedata WHERE isleaf = TRUE AND isdelete = false AND fkcode = 100 AND code =(SELECT status  FROM bus_customs WHERE jobid = t.id AND clstype='O' AND isdelete = FALSE order by nos LIMIT 1)) AS status\n";
                }
                if (thisJSONObject.containsValue("cntchecktime") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,to_char( (SELECT cntcheck_time  FROM bus_customs WHERE jobid = t.id AND isdelete = FALSE order by nos LIMIT 1) ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS cntchecktime\n";
                }
                if (thisJSONObject.containsValue("cntreleasetime") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,to_char( (SELECT cntrelease_time  FROM bus_customs WHERE jobid = t.id AND isdelete = FALSE order by nos LIMIT 1) ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS cntreleasetime\n";
                }
                if (thisJSONObject.containsValue("hblnumber") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT count(1) FROM bus_ship_bill WHERE isdelete = FALSE AND bltype = 'H' AND jobid = t.id) AS hblnumber\n";
                }
                if (thisJSONObject.containsValue("mblnumber") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT count(1) FROM bus_ship_bill WHERE isdelete = FALSE AND bltype = 'M' AND jobid = t.id) AS mblnumber\n";
                }
                if (thisJSONObject.containsValue("piece") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT SUM(piece) FROM bus_ship_container WHERE isdelete = FALSE AND jobid = t.id AND parentid is null) AS piece\n";
                }
                if (thisJSONObject.containsValue("grswgt") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT SUM(grswgt) FROM bus_ship_container WHERE isdelete = FALSE AND jobid = t.id AND parentid is null) AS grswgt\n";
                }
                if (thisJSONObject.containsValue("cbm") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT SUM(cbm) FROM bus_ship_container WHERE isdelete = FALSE AND jobid = t.id AND parentid is null) AS cbm\n";
                }
                if (thisJSONObject.containsValue("truckstate") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT (CASE WHEN x.truckstate = 'I' THEN '初始' ELSE '完成' END) FROM bus_truck x,dat_filedata y WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.truckstate = y.code LIMIT 1) AS truckstate\n";
                }
                if (thisJSONObject.containsValue("isinsurance") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT isinsurance FROM bus_truck WHERE isdelete = FALSE AND jobid = t.id LIMIT 1) AS isinsurance\n";
                }
                if (thisJSONObject.containsValue("truckabbr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT y.abbr FROM bus_truck x,sys_corporation y WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.truckid = y.id LIMIT 1) AS truckabbr\n";
                }
                if (thisJSONObject.containsValue("customsabbr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT y.abbr FROM bus_customs x,sys_corporation y WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.customid = y.id LIMIT 1) AS customsabbr\n";
                }
                if (thisJSONObject.containsValue("trucktype") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT y.namec FROM bus_truck x,dat_filedata y WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.trucktype = y.code AND x.trucktype IS NOT NULL AND x.trucktype <> '' AND y.isleaf = TRUE AND y.isdelete = false AND y.fkcode = 110 LIMIT 1) AS trucktype\n";
                }
                if (thisJSONObject.containsValue("iscls") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT iscls FROM bus_truck WHERE isdelete = FALSE AND jobid = t.id LIMIT 1) AS iscls\n";
                }
                if (thisJSONObject.containsValue("finance") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg(x.namec,',') FROM sys_user x , sys_user_assign y WHERE x.id =y.userid AND y.roletype = 'F' AND y.linkid = (select id from bus_shipping bs where bs.isdelete = false and bs.jobid=t.id limit 1) AND  COALESCE(y.isdelete,false) = false)  AS finance\n";
                }
                if (thisJSONObject.containsValue("mbltypedesc") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT namec FROM dat_filedata WHERE isdelete = false AND fkcode = 150 AND isleaf = TRUE AND code = (select mbltype from bus_shipping bs where bs.isdelete = false and bs.jobid=t.id limit 1) LIMIT 1) AS mbltypedesc\n";
                }
                if (thisJSONObject.containsValue("cncount") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,CASE WHEN t.ldtype = 'F' THEN (SELECT count(*) FROM bus_ship_container WHERE isdelete = false and parentid is null AND jobid = t.id) ELSE 0 END AS cncount\n";
                }
                if (thisJSONObject.containsValue("clearstate") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT namec from dat_filedata WHERE isleaf = TRUE AND isdelete = false AND fkcode = 120 AND code =(SELECT clearingstate  FROM bus_customs WHERE jobid = t.id AND clstype='I' AND isdelete = FALSE order by nos LIMIT 1)) AS clearstate\n";
                }
                if (thisJSONObject.containsValue("now_eta") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(now()::DATE - (select eta from bus_shipping bs where bs.isdelete = false and bs.jobid=t.id limit 1)) AS now_eta\n";
                }
                if (thisJSONObject.containsValue("saleuser") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT namec||'/'||namee||'/'||COALESCE((SELECT name FROM sys_department WHERE id = x.deptid),'') FROM sys_user x WHERE id = t.saleid) saleuser\n";
                }
                if (thisJSONObject.containsValue("opcompany") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT abbr FROM sys_corporation WHERE id = t.corpidop AND isdelete = FALSE) AS opcompany\n";
                }
                if (thisJSONObject.containsValue("company") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT abbr FROM sys_corporation WHERE id = t.corpid AND isdelete = FALSE) AS company\n";
                }
                if (thisJSONObject.containsValue("customernamec") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT namec FROM sys_corporation WHERE id = t.customerid AND isdelete = FALSE) AS customernamec\n";
                }
                if (thisJSONObject.containsValue("cnts") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT f_fina_jobs_cnts('jobid='::text||t.id)) AS cnts\n";
                }
                if (thisJSONObject.containsValue("cntdescnew") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT f_fina_jobs_cntdesc('jobid='::text||t.id)) AS cntdescnew\n";
                }
                if (thisJSONObject.containsValue("bookinger") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg(COALESCE(namec,'')||'/'||COALESCE(namee,''),',') FROM sys_user x , sys_user_assign y WHERE x.id =y.userid AND y.roletype = 'T' AND y.linkid = (select id from bus_shipping bs where bs.isdelete = false and bs.jobid=t.id limit 1)  AND  COALESCE(y.isdelete,false) = false)  AS bookinger\n";
                }
                if (thisJSONObject.containsValue("docnametime") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT y.inputtime FROM sys_user_assign y WHERE y.linktype = 'J' AND y.roletype = 'D' AND y.linkid = (select id from bus_shipping bs where bs.isdelete = false and bs.jobid=t.id limit 1)  AND COALESCE(y.isdelete,false) = false limit 1)  AS docnametime\n";
                }
                if (thisJSONObject.containsValue("sonoss") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg((c.sono)::text, ','::text) AS string_agg FROM bus_ship_container c WHERE (((((c.jobid = t.id) AND (c.isdelete = false)) AND (c.cntno IS NOT NULL)) AND ((c.cntno)::text <> ''::text)) AND (c.parentid IS NULL))) AS sonoss\n";
                }
                if (thisJSONObject.containsValue("cargotypename") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(CASE WHEN t.cargotype = 'A' THEN '自揽货' WHEN t.cargotype = 'B' THEN '自揽同行' WHEN t.cargotype = 'C' THEN '指定货' " +
                            "WHEN t.cargotype = 'D' THEN '代订舱' ELSE '' END) AS cargotypename\n";
                }
                if (thisJSONObject.containsValue("depter2") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT x.name FROM sys_department x  WHERE ((x.isdelete = false) AND (x.id = (select deptid from sys_user where id =t.saleid)))) AS depter2\n";
                }
                if (thisJSONObject.containsValue("customertypenamec") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT d.namec FROM dat_filedata AS d   WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 95 AND d.code = (select customertype from sys_corporation where id =t.customerid limit 1)) AS customertypenamec\n";
                }
                if (thisJSONObject.containsValue("ldtypestr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(CASE WHEN t.ldtype = 'F' THEN 'FCL' WHEN t.ldtype = 'L' THEN 'FCL' ELSE '' END) AS ldtypestr\n";
                }
                if (thisJSONObject.containsValue("tradewaystr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(CASE WHEN t.ldtype = 'F' THEN 'FOB' WHEN t.ldtype = 'C' THEN 'CIF' WHEN t.ldtype = 'D' THEN 'DDP' WHEN t.ldtype = 'E' THEN 'EXW' WHEN t.ldtype = 'A' THEN 'FCA' WHEN t.ldtype = 'G' THEN 'DDU' WHEN t.ldtype = 'H' THEN 'DAP' ELSE '' END) AS tradewaystr\n";
                }
                if (thisJSONObject.containsValue("jobdatestr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,to_char( (jobdate) ::timestamp, 'yyyy-mm-dd') AS jobdatestr\n";
                }
                if (thisJSONObject.containsValue("cntnos") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg(cntno,',') FROM bus_ship_container WHERE isdelete = FALSE AND parentid IS NULL AND jobid = t.id) AS cntnos\n";
                }
                if (thisJSONObject.containsValue("sealnostr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg(sealno,',') FROM bus_ship_container WHERE isdelete = FALSE AND parentid IS NULL AND jobid = t.id) AS sealnostr\n";
                }
                if (thisJSONObject.containsValue("impexpv") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT COALESCE(d.namee,d.namec) FROM dat_filedata AS d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 170 AND d.code = t.impexp LIMIT 1) AS impexpv\n";
                }
                if (thisJSONObject.containsValue("sonostr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg(sono,',') FROM bus_ship_container WHERE isdelete = FALSE AND parentid IS NULL AND jobid = t.id) AS sonostr\n";
                }
                if (thisJSONObject.containsValue("hblnostr") && !thisJSONObject.containsKey("hide")) {
                    Afield += "\t\t\t\t\t,(SELECT string_agg(b.hblno,',') FROM bus_ship_bill b WHERE b.jobid = t.id AND b.isdelete = FALSE )AS hblnostr\n";
                }


                if (thisJSONObject.containsValue("orderno") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(SELECT x.orderno FROM bus_order x WHERE x.isdelete = false AND x.jobid = t.id LIMIT 1) AS orderno\n";
                    Ofield += ",NULL AS orderno";
                }
                if (thisJSONObject.containsValue("jobstatedesc") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t\t,CASE t.jobstate\n" +
                            "            WHEN 'I'::text THEN '未安排'::text\n" +
                            "            WHEN 'S'::text THEN '已选舱'::text\n" +
                            "            WHEN 'TI'::text THEN '安排拖车'::text\n" +
                            "            WHEN 'TF'::text THEN '拖车完成'::text\n" +
                            "            WHEN 'CI'::text THEN '安排报关'::text\n" +
                            "            WHEN 'CF'::text THEN '报关完成'::text\n" +
                            "            WHEN 'BI'::text THEN '做提单'::text\n" +
                            "            WHEN 'BF'::text THEN '提单已打印'::text\n" +
                            "            WHEN 'DI'::text THEN '出账单'::text\n" +
                            "            WHEN 'DF'::text THEN '账单已打印'::text\n" +
                            "            WHEN 'J'::text THEN '已并单'::text\n" +
                            "            WHEN 'E'::text THEN '已寄单'::text\n" +
                            "            WHEN 'QG'::text THEN '已清关'::text\n" +
                            "            WHEN 'TG'::text THEN '已拖柜'::text\n" +
                            "            WHEN 'F'::text THEN '完成'::text\n" +
                            "            ELSE NULL::text\n" +
                            "        END AS jobstatedesc";
                    Ofield += ",NULL AS jobstatedesc";
                }
                if (thisJSONObject.containsValue("bltype") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.bltype) AS bltype";
                    Ofield += ",NULL AS bltype";
                }
                if (thisJSONObject.containsValue("cnortitle") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(substring(b.cnortitle FROM 1 FOR (CASE WHEN position(CHR(10) in b.cnortitle)< 1  THEN length(b.cnortitle) ELSE (position(CHR(10) in b.cnortitle)-1) END))) AS cnortitle";
                    Ofield += ",NULL AS cnortitle";
                }
                if (thisJSONObject.containsValue("cneetitle") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(substring(b.cneetitle FROM 1 FOR (CASE WHEN position(CHR(10) in b.cneetitle)< 1  THEN length(b.cneetitle) ELSE (position(CHR(10) in b.cneetitle)-1) END))) AS cneetitle\n";
                    Ofield += ",NULL AS cneetitle";
                }
                if (thisJSONObject.containsValue("notifytitle") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT (regexp_split_to_array(notifytitle,chr(10)))[1] FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id) AS notifytitle\n";
                    Ofield += ",NULL AS notifytitle";
                }
                if (thisJSONObject.containsValue("isput") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.isput) AS isput";
                    Ofield += ",NULL AS isput";
                }
                if (thisJSONObject.containsValue("puter") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.puter) AS puter";
                    Ofield += ",NULL AS puter";
                }
                if (thisJSONObject.containsValue("putdesc") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.putdesc) AS putdesc";
                    Ofield += ",NULL AS putdesc";
                }
                if (thisJSONObject.containsValue("putstatus") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.putstatus) AS putstatus";
                    Ofield += ",NULL AS putstatus";
                }
                if (thisJSONObject.containsValue("ishold") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.ishold) AS ishold";
                    Ofield += ",NULL AS ishold";
                }
                if (thisJSONObject.containsValue("holder") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.holder) AS holder";
                    Ofield += ",NULL AS holder";
                }
                if (thisJSONObject.containsValue("holddesc") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.holddesc) AS holddesc";
                    Ofield += ",NULL AS holddesc";
                }
                if (thisJSONObject.containsValue("holdtime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",to_char( b.holdtime\t::timestamp, 'yyyy-mm-dd') AS holdtime";
                    Ofield += ",NULL AS holdtime";
                }
                if (thisJSONObject.containsValue("isconfirmmbl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.isconfirmmbl) AS isconfirmmbl";
                    Ofield += ",NULL AS isconfirmmbl";
                }
                if (thisJSONObject.containsValue("dateconfirmmbl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",to_char( b.dateconfirmmbl\t::timestamp, 'yyyy-mm-dd') AS dateconfirmmbl";
                    Ofield += ",NULL AS dateconfirmmbl";
                }
                if (thisJSONObject.containsValue("isreleasembl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.isreleasembl) AS isreleasembl";
                    Ofield += ",NULL AS isreleasembl";
                }
                if (thisJSONObject.containsValue("datereleasembl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",to_char( b.datereleasembl\t::timestamp, 'yyyy-mm-dd') AS datereleasembl";
                    Ofield += ",NULL AS datereleasembl";
                }
                if (thisJSONObject.containsValue("istelrel") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.istelrel) AS istelrel";
                    Ofield += ",NULL AS istelrel";
                }
                if (thisJSONObject.containsValue("telreltime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",to_char( b.telreltime\t::timestamp, 'yyyy-mm-dd') AS telreltime";
                    Ofield += ",NULL AS telreltime";
                }
                if (thisJSONObject.containsValue("telrelnos") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.telrelnos) AS telrelnos";
                    Ofield += ",NULL AS telrelnos";
                }
                if (thisJSONObject.containsValue("telreler") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.telreler) AS telreler";
                    Ofield += ",NULL AS telreler";
                }
                if (thisJSONObject.containsValue("istelrelback") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(b.istelrelback) AS istelrelback";
                    Ofield += ",NULL AS istelrelback";
                }
                if (thisJSONObject.containsValue("clearancecusname") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(SELECT string_agg((a.abbr)::text, ','::text) AS string_agg \n" +
                            "FROM sys_corporation a WHERE ((a.isdelete = false) \n" +
                            "\tAND (EXISTS ( SELECT 1 FROM bus_customs WHERE ((((bus_customs.clearancecusid = a.id) AND ((bus_customs.clstype)::text = 'I'::text)) AND (bus_customs.jobid = t.id)) AND (bus_customs.isdelete = false)))))\n" +
                            ") AS clearancecusname";
                    Ofield += ",NULL AS clearancecusname";
                }
                if (thisJSONObject.containsValue("usergetmbl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.usergetmbl) AS usergetmbl\n";
                    Ofield += ",NULL AS usergetmbl";
                }
                if (thisJSONObject.containsValue("packer") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.packer) AS packer\n";
                    Ofield += ",NULL AS packer";
                }
                if (thisJSONObject.containsValue("ata") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,to_char( b.ata\t::timestamp, 'yyyy-mm-dd') AS ata\n";
                    Ofield += ",NULL AS ata";
                }
                if (thisJSONObject.containsValue("destination") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.destination) AS destination\n";
                    Ofield += ",NULL AS destination";
                }
                if (thisJSONObject.containsValue("goodstypenamec") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT y.namec FROM bus_shipping x,dat_goodstype y WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.goodstypeid = y.id) AS goodstypenamec\n";
                    Ofield += ",NULL AS goodstypenamec";
                }
                if (thisJSONObject.containsValue("agentdesidabbr") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,COALESCE((SELECT y.abbr FROM bus_shipping x,sys_corporation y WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.agentdesid = y.id),'') AS agentdesidabbr\n";
                    Ofield += ",NULL AS agentdesidabbr";
                }
                if (thisJSONObject.containsValue("clstime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,to_char(  b.clstime ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS clstime\n";
                    Ofield += ",NULL AS clstime";
                }
                if (thisJSONObject.containsValue("agenter") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(select c.abbr from sys_corporation c, bus_shipping b where c.id=b.agentid AND b.jobid = t.id AND c.isagent  = TRUE AND b.isdelete = FALSE) AS agenter\n";
                    Ofield += ",NULL AS agenter";
                }
                if (thisJSONObject.containsValue("priceusernamec") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT namec FROM sys_user x,bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.id = y.priceuserid AND y.jobid = t.id) as priceusernamec\n";
                    Ofield += ",NULL AS priceusernamec";
                }
                if (thisJSONObject.containsValue("contractno") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.contractno) AS contractno\n";
                    Ofield += ",NULL AS contractno";
                }
                if (thisJSONObject.containsValue("routecode") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(select c.namec from dat_line c, bus_shipping b where (c.code = b.routecode OR c.namec = b.routecode) AND b.jobid = t.id AND c.isdelete = false LIMIT 1) AS routecode\n";
                    Ofield += ",NULL AS routecode";
                }
                if (thisJSONObject.containsValue("poa") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.poa) AS poa\n";
                    Ofield += ",NULL AS poa";
                }
                if (thisJSONObject.containsValue("hbltypedesc") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT namec from dat_filedata WHERE isleaf = TRUE AND isdelete = false AND fkcode = 140 AND code = b.hbltype) AS hbltypedesc\n";
                    Ofield += ",NULL AS hbltypedesc";
                }
                if (thisJSONObject.containsValue("agentitle") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT (regexp_split_to_array(agentitle,chr(10)))[1] FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id) AS agentitle\n";
                    Ofield += ",NULL AS agentitle";
                }
                if (thisJSONObject.containsValue("busstatusdesc") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT namec FROM dat_filedata x , bus_shipping y WHERE x.isdelete = false AND x.fkcode = 320 AND x.isleaf = TRUE AND x.code = y.busstatus AND y.jobid = t.id LIMIT 1) AS busstatusdesc\n";
                    Ofield += ",NULL AS busstatusdesc";
                }
                if (thisJSONObject.containsValue("isreleasecnt") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.isreleasecnt) AS isreleasecnt\n";
                    Ofield += ",NULL AS isreleasecnt";
                }
                if (thisJSONObject.containsValue("releasecnttime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(to_char( b.releasecnttime ::timestamp, 'yyyy-mm-dd hh24:mi:ss')) AS releasecnttime\n";
                    Ofield += ",NULL AS releasecnttime";
                }
                if (thisJSONObject.containsValue("ispickcnt") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.ispickcnt) AS ispickcnt\n";
                    Ofield += ",NULL AS ispickcnt";
                }
                if (thisJSONObject.containsValue("pickcnttime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(to_char( b.pickcnttime ::timestamp, 'yyyy-mm-dd hh24:mi:ss')) AS pickcnttime\n";
                    Ofield += ",NULL AS pickcnttime";
                }
                if (thisJSONObject.containsValue("shipagent") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.shipagent) AS shipagent\n";
                    Ofield += ",NULL AS shipagent";
                }
                if (thisJSONObject.containsValue("potcode") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.potcode) AS potcode\n";
                    Ofield += ",NULL AS potcode";
                }
                if (thisJSONObject.containsValue("sodate") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,to_char( b.sodate ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS sodate\n";
                    Ofield += ",NULL AS sodate";
                }
                if (thisJSONObject.containsValue("sono") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.sono) AS sono\n";
                    Ofield += ",NULL AS sono";
                }
                if (thisJSONObject.containsValue("pono") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.pono) AS pono\n";
                    Ofield += ",NULL AS pono";
                }
                if (thisJSONObject.containsValue("dategatein") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(to_char( b.dategatein ::timestamp, 'yyyy-mm-dd')) AS dategatein\n";
                    Ofield += ",NULL AS dategatein";
                }
                if (thisJSONObject.containsValue("priceuser") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT namec||'/'||namee||'/'||COALESCE((SELECT name FROM sys_department WHERE id = x.deptid),'') FROM sys_user x WHERE id = b.priceuserid) as priceuser\n";
                    Ofield += ",NULL AS priceuser";
                }
                if (thisJSONObject.containsValue("mblnos") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(((COALESCE(b.mblno, ''::character varying))::text || ' '::text) || \n" +
                            "(SELECT COALESCE(string_agg((y.mblno)::text, ','::text), ''::text) AS coalesce FROM bus_ship_bill y WHERE y.isdelete = false AND y.jobid = t.id)) AS mblnos\n";
                    Ofield += ",NULL AS mblnos";
                }
                if (thisJSONObject.containsValue("freightitemnamec") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT namee FROM dat_filedata d WHERE  d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and code = b.freightitem limit 1) as freightitemnamec\n";
                    Ofield += ",NULL AS freightitemnamec";
                }
                if (thisJSONObject.containsValue("paymentitemnamec") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT namee FROM dat_filedata d WHERE  d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and code = b.paymentitem limit 1) as paymentitemnamec\n";
                    Ofield += ",NULL AS paymentitemnamec";
                }
                if (thisJSONObject.containsValue("holdtime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.holdtime\n";
                    Ofield += ",NULL AS holdtime";
                }
                if (thisJSONObject.containsValue("puttime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.puttime\n";
                    Ofield += ",NULL AS puttime";
                }
                if (thisJSONObject.containsValue("dateconfirmmbl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.dateconfirmmbl\n";
                    Ofield += ",NULL AS dateconfirmmbl";
                }
                if (thisJSONObject.containsValue("datereleasembl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.datereleasembl\n";
                    Ofield += ",NULL AS datereleasembl";
                }
                if (thisJSONObject.containsValue("telreltime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.telreltime\n";
                    Ofield += ",NULL AS telreltime";
                }
                if (thisJSONObject.containsValue("telrelbacktime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.telrelbacktime\n";
                    Ofield += ",NULL AS telrelbacktime";
                }
                if (thisJSONObject.containsValue("dategetmbl") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(to_char( b.dategetmbl ::timestamp, 'yyyy-mm-dd hh24:mi:ss')) as dategetmbl\n";
                    Ofield += ",NULL AS dategetmbl";
                }
                if (thisJSONObject.containsValue("loadtime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT to_char( max(bt.loadtime) ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS max FROM bus_truck bt WHERE bt.isdelete = false AND bt.jobid = t.id) AS loadtime\n";
                    Ofield += ",NULL AS loadtime";
                }
                if (thisJSONObject.containsValue("storehousedate") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT max(bc.storehousedate) AS max FROM bus_customs bc WHERE bc.jobid = t.id AND bc.isdelete = false) AS storehousedate\n";
                    Ofield += ",NULL AS storehousedate";
                }
                if (thisJSONObject.containsValue("sidate") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,to_char( b.sidate ::timestamp, 'yyyy-mm-dd hh24:mi:ss') as sidate\n";
                    Ofield += ",NULL AS sidate";
                }
                if (thisJSONObject.containsValue("polcode") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.polcode) AS polcode\n";
                    Ofield += ",NULL AS polcode";
                }
                if (thisJSONObject.containsValue("pol") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.pol) AS pol\n";
                    Ofield += ",NULL AS pol";
                }
                if (thisJSONObject.containsValue("pod") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.pod) AS pod\n";
                    Ofield += ",NULL AS pod";
                }
                if (thisJSONObject.containsValue("pddcode") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.pddcode) AS pddcode\n";
                    Ofield += ",NULL AS pddcode";
                }
                if (thisJSONObject.containsValue("pdd") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(b.pdd) AS pdd\n";
                    Ofield += ",NULL AS pdd";
                }
                if (thisJSONObject.containsValue("carrier") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT sys_corporation.code FROM sys_corporation WHERE ((sys_corporation.id = b.carrierid) AND (sys_corporation.isdelete = false))) AS carrier\n";
                    Ofield += ",NULL AS carrier";
                }
                if (thisJSONObject.containsValue("vessel") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.vessel\n";
                    Ofield += ",NULL AS vessel";
                }
                if (thisJSONObject.containsValue("voyage") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,b.voyage\n";
                    Ofield += ",NULL AS voyage";
                }
                if (thisJSONObject.containsValue("csname") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT string_agg((x.namec)::text, ','::text) AS string_agg FROM sys_user x, sys_user_assign y\n" +
                            "WHERE ((((x.id = y.userid) AND ((y.roletype)::text = 'C'::text)) AND (y.linkid = b.id)) AND (COALESCE(y.isdelete, false) = false))) AS csname\n";
                    Ofield += ",NULL AS csname";
                }
                if (thisJSONObject.containsValue("opname") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT string_agg((x.namec)::text, ','::text) AS string_agg FROM sys_user x,sys_user_assign y\n" +
                            "WHERE ((((x.id = y.userid) AND ((y.roletype)::text = 'O'::text)) AND (y.linkid = b.id)) AND (COALESCE(y.isdelete, false) = false))) AS opname\n";
                    Ofield += ",NULL AS opname";
                }
                if (thisJSONObject.containsValue("docname") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT string_agg((SELECT (((COALESCE(y.namec, ''::character varying))::text || '/'::text) || (COALESCE(y.namee, ''::character varying))::text) FROM sys_user y\n" +
                            "\t\tWHERE ((y.isdelete = false) AND (y.id = x.userid))), ','::text) AS string_agg \n" +
                            "FROM sys_user_assign x\n" +
                            "WHERE ((((x.linkid = b.id) AND ((x.linktype)::text = 'J'::text)) AND ((x.roletype)::text = 'D'::text)) AND (COALESCE(x.isdelete, false) = false))) AS docname\n";
                    Ofield += ",NULL AS docname";
                }
                if (thisJSONObject.containsValue("shipjoinos") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT string_agg((j.nos)::text, ''::text) AS string_agg \n" +
                            "FROM bus_shipjoin j WHERE ((j.id = ANY (ARRAY( SELECT l.shipjoin FROM bus_shipjoinlink l, bus_shipping s_1 WHERE ((l.shipid = s_1.id) AND (s_1.jobid = t.id))))) AND (j.isdelete = false))\n" +
                            ") AS shipjoinos\n";
                    Ofield += ",NULL AS shipjoinos";
                }
                if (thisJSONObject.containsValue("invoceno") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t\t\t\t,(SELECT string_agg(DISTINCT (x.invoiceno)::text, ','::text) AS string_agg \n" +
                            "FROM fina_invoice x, fina_arap y\n" +
                            "WHERE ((((((y.jobid = t.id) AND (x.isdelete = false)) AND (y.isdelete = false)) AND (y.invoiceid = x.id)) AND (x.invoiceno IS NOT NULL)) AND ((x.invoiceno)::text <> ''::text))) AS invoceno\n";
                    Ofield += ",NULL AS invoceno";
                }
                if (thisJSONObject.containsValue("emptycasetime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += "\t,(\n" +
                            "\t\t\t\t\tSELECT  to_char( dealdate ::timestamp, 'yyyy-mm-dd hh24:mi:ss')\n" +
                            "\t\t\t\t\tFROM bus_ship_container bsc\n" +
                            "\t\t\t\t\tinner join bus_ship_book_cnt bsbc on bsc.jobid = bsbc.jobid and bsc.cntno = bsbc.cntno\n" +
                            "\t\t\t\t\tinner join bus_ship_container_track bsct on bsbc.id = bsct.linkid and (sts1code ='EE' or sts1code ='STSP')\n" +
                            "\t\t\t\t\tWHERE bsc.isdelete = false and bsc.jobid = t.id  limit 1\n" +
                            "\t\t\t\t) AS emptycasetime";
                    Ofield += ",NULL AS emptycasetime";
                }
                if (thisJSONObject.containsValue("emptycaseplace") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(\n" +
                            "\t\t\t\t\tSELECT locatione\n" +
                            "\t\t\t\t\tFROM bus_ship_container bsc\n" +
                            "\t\t\t\t\tinner join bus_ship_book_cnt bsbc on bsc.jobid = bsbc.jobid and bsc.cntno = bsbc.cntno\n" +
                            "\t\t\t\t\tinner join bus_ship_container_track bsct on bsbc.id = bsct.linkid and (sts1code ='EE' or sts1code ='STSP')\n" +
                            "\t\t\t\t\tWHERE bsc.isdelete = false and bsc.jobid = t.id  limit 1\n" +
                            "\t\t\t\t) AS emptycaseplace";
                    Ofield += ",NULL AS emptycaseplace";
                }
                if (thisJSONObject.containsValue("heavyboxtime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(\n" +
                            "\t\t\t\t\tSELECT  to_char( dealdate ::timestamp, 'yyyy-mm-dd hh24:mi:ss')\n" +
                            "\t\t\t\t\tFROM bus_ship_container bsc\n" +
                            "\t\t\t\t\tinner join bus_ship_book_cnt bsbc on bsc.jobid = bsbc.jobid and bsc.cntno = bsbc.cntno\n" +
                            "\t\t\t\t\tinner join bus_ship_container_track bsct on bsbc.id = bsct.linkid and (sts1code ='OA' or sts1code ='GITM')\n" +
                            "\t\t\t\t\tWHERE bsc.isdelete = false and bsc.jobid = t.id  limit 1\n" +
                            "\t\t\t\t) AS heavyboxtime";
                    Ofield += ",NULL AS heavyboxtime";
                }
                if (thisJSONObject.containsValue("heavyboxplace") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(\n" +
                            "\t\t\t\t\tSELECT locatione\n" +
                            "\t\t\t\t\tFROM bus_ship_container bsc\n" +
                            "\t\t\t\t\tinner join bus_ship_book_cnt bsbc on bsc.jobid = bsbc.jobid and bsc.cntno = bsbc.cntno\n" +
                            "\t\t\t\t\tinner join bus_ship_container_track bsct on bsbc.id = bsct.linkid and (sts1code ='OA' or sts1code ='GITM')\n" +
                            "\t\t\t\t\tWHERE bsc.isdelete = false and bsc.jobid = t.id  limit 1\n" +
                            "\t\t\t\t) AS heavyboxplace";
                    Ofield += ",NULL AS heavyboxplace";
                }
                if (thisJSONObject.containsValue("atd") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",to_char( b.atd ::timestamp, 'yyyy-mm-dd') AS atd";
                    Ofield += ",NULL AS atd";
                }
                if (thisJSONObject.containsValue("deliverysuitcasetime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(\n" +
                            "\t\t\t\t\tSELECT to_char( dealdate ::timestamp, 'yyyy-mm-dd hh24:mi:ss')\n" +
                            "\t\t\t\t\tFROM bus_ship_container bsc\n" +
                            "\t\t\t\t\tinner join bus_ship_book_cnt bsbc on bsc.jobid = bsbc.jobid and bsc.cntno = bsbc.cntno\n" +
                            "\t\t\t\t\tinner join bus_ship_container_track bsct on bsbc.id = bsct.linkid and (sts1code ='Z8' or sts1code ='STCS')\n" +
                            "\t\t\t\t\tWHERE bsc.isdelete = false and bsc.jobid = t.id  limit 1\n" +
                            "\t\t\t\t) AS deliverysuitcasetime";
                    Ofield += ",NULL AS deliverysuitcasetime";
                }
                if (thisJSONObject.containsValue("emptyboxtime") && !thisJSONObject.containsKey("hide")) {
                    Sfield += ",(\n" +
                            "\t\t\t\t\tSELECT to_char( dealdate ::timestamp, 'yyyy-mm-dd hh24:mi:ss')\n" +
                            "\t\t\t\t\tFROM bus_ship_container bsc\n" +
                            "\t\t\t\t\tinner join bus_ship_book_cnt bsbc on bsc.jobid = bsbc.jobid and bsc.cntno = bsbc.cntno\n" +
                            "\t\t\t\t\tinner join bus_ship_container_track bsct on bsbc.id = bsct.linkid and (sts1code ='RD' or sts1code ='RCVE')\n" +
                            "\t\t\t\t\tWHERE bsc.isdelete = false and bsc.jobid = t.id  limit 1\n" +
                            "\t\t\t\t) AS emptyboxtime";
                    Ofield += ",NULL AS emptyboxtime";
                }
            }

            //初始化
            String dynamicClauseAllWhere = "";
            String dynamicClauseOutsideWhere = "";
            if ("jobdate".equals(dates) || "inputtime".equals(dates) || "updatetime".equals(dates)) {
                dynamicClauseAllWhere = "  AND " + dates + "::DATE BETWEEN '" + (StrUtils.isNull(startdate) ? "0001-01-01" : startdate) + "' AND '" + (StrUtils.isNull(enddate) ? "9999-12-31" : enddate) + "'";
            } else {
                dynamicClauseOutsideWhere = "  AND " + dates + "::DATE BETWEEN '" + (StrUtils.isNull(startdate) ? "0001-01-01" : startdate) + "' AND '" + (StrUtils.isNull(enddate) ? "9999-12-31" : enddate) + "'";
            }

            //世倡和中集 ：退载默认不显示
            String unloadedWhere = " AND busstatus <> 'R'";

            boolean isshow = false;
            try {
                String isshowpublicsql = "SELECT EXISTS(SELECT 1 FROM sys_role sr , sys_modinrole am WHERE sr.code = '" + sysUser.getCode() + "' AND roletype = 'C' AND am.roleid = sr.id AND am.moduleid = 299220)"
                        + "\nOR EXISTS(SELECT 1 FROM sys_role sr , sys_modinrole am , sys_userinrole ur WHERE roletype = 'M' AND am.roleid = sr.id AND am.moduleid = 299220 AND ur.roleid = sr.id AND ur.userid = " + sysUser.getId() + ") AS flag";
                Map ispublic = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isshowpublicsql);
                if ("true".equals(StrUtils.getMapVal(ispublic, "flag"))) {
                    isshow = true;
                }
            } catch (Exception e) {
                isshow = false;
            }
            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String permissionsWhere = "AND ( t.saleid = " + sysUser.getId()
                    + "\n	OR (t.inputer ='" + sysUser.getCode() + "')" //录入人有权限
                    + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + sysUser.getId() + ")) " + //能看所有外办订到本公司的单权限的人能看到对应单
                    "AND t.corpid <> " + sysUser.getSysCorporation().getId() + " AND " + sysUser.getSysCorporation().getId() + " = ANY(SELECT t.corpidop UNION SELECT t.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = t.id AND c.isdelete =FALSE)))"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib x , sys_custlib_user y  "
                    + "\n				WHERE y.custlibid = x.id  "
                    + "\n					AND y.userid = " + sysUser.getId()
                    + "\n					AND x.libtype = 'S'  "
                    //+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
                    + "\n					AND x.userid = t.saleid " //关联的业务员的单，都能看到
                    + ")"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib x , sys_custlib_role y  "
                    + "\n				WHERE y.custlibid = x.id  "
                    + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + sysUser.getId() + " AND z.roleid = y.roleid)"
                    + "\n					AND x.libtype = 'S'  "
                    //+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
                    + "\n					AND x.userid = t.saleid " //组关联业务员的单，都能看到
                    + ")"

                    //过滤工作单指派
                    + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="
                    + sysUser.getId() + ")"
                    + "\n)"
                    //2749 税局查账系统分离方案
                    + (isshow == true ? "\nAND EXISTS(SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.jobid = t.id"
                    + " AND EXISTS(SELECT 1 FROM sys_corporation y WHERE y.isdelete = FALSE AND y.iscustomer = TRUE "
                    + "AND y.id = x.customerid))" : "");

            String corpfilterWhere = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT t.corpid UNION SELECT t.corpidop UNION SELECT t.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = t.id AND c.isdelete = FALSE) AND x.ischoose = TRUE AND userid =" + sysUser.getId() + ") OR COALESCE(t.saleid,0) <= 0)";


            thissql0 = "select\n" +
                    "\t\t\t\tsum(joblength) as joblength\n" +
                    "\t\t\tfrom(\n" +
                    "\t\t\t\tSELECT\n" +
                    "\t\t\t\t\tcount(1) as joblength\n" +
                    "\t\t\t\tFROM fina_jobs t\n" +
                    "\t\t\t\tINNER JOIN bus_shipping b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\t\tWHERE t.isdelete = false AND t.jobtype = 'S'\n" +
                    "\t\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t\t" + unloadedWhere + "\n" +
                    "\t\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t\t" + corpfilterWhere + "\n" +
                    "\n" +
                    "\t\t\t\tUNION ALL\n" +
                    "\t\t\t\tSELECT\n" +
                    "\t\t\t\t\tcount(1) as joblength\n" +
                    "\t\t\t\tFROM fina_jobs t\n" +
                    "\t\t\t\tINNER JOIN bus_air b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\t\tWHERE t.isdelete = false AND t.jobtype = 'A'\n" +
                    "\t\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t\t" + unloadedWhere + "\n" +
                    "\t\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t\t" + corpfilterWhere + "\n" +
                    "\n" +
                    "\t\t\t\tUNION ALL\n" +
                    "\t\t\t\tSELECT\n" +
                    "\t\t\t\t\tcount(1) as joblength\n" +
                    "\t\t\t\tFROM fina_jobs t\n" +
                    "\t\t\t\tINNER JOIN bus_truck b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\t\tWHERE t.isdelete = false AND t.jobtype = 'L'\n" +
                    "\t\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t\t" + corpfilterWhere + "\n" +
                    "\n" +
                    "\t\t\t\tUNION ALL\n" +
                    "\t\t\t\tSELECT\n" +
                    "\t\t\t\t\tcount(1) as joblength\n" +
                    "\t\t\t\tFROM fina_jobs t\n" +
                    "\t\t\t\tINNER JOIN bus_customs b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\t\tWHERE t.isdelete = false AND t.jobtype = 'C'\n" +
                    "\t\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t\t" + corpfilterWhere + "\n" +
                    "\t\t\t) ss";

            thissql1 = "SELECT\n" +
                    "\t\t\t\tt.id\n" +
                    "\t\t\t\t,t.jobdate\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t,t.saleid\n" +
                    "\t\t\t\t,t.inputer\n" +
                    "\t\t\t\t,t.corpid\n" +
                    "\t\t\t\t,t.corpidop\n" +
                    "\t\t\t\t,t.corpidop2\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t" + Afield + "\n" +
                    "\t\t\t\t,b.mblno\n" +
                    "\t\t\t\t,b.hblno\n" +
                    "\t\t\t\t,b.sono\n" +
                    "\t\t\t\t,b.cnortitle\n" +
                    "\t\t\t\t,b.cneetitle\n" +
                    "\t\t\t\t,b.notifytitle\n" +
                    "\t\t\t\t,(SELECT abbr FROM sys_corporation WHERE id = b.agenid) AS agenabbr\n" +
                    "\t\t\t\t,to_char( b.etd ::timestamp, 'yyyy-mm-dd') AS etd\n" +
                    "\t\t\t\t,to_char( b.eta ::timestamp, 'yyyy-mm-dd') AS eta\n" +
                    "\t\t\t\t,to_char( b.cls ::timestamp, 'yyyy-mm-dd') AS cls\n" +
                    "\t\t\t\t" + Sfield + "\n" +
                    "\t\t\tFROM fina_jobs t\n" +
                    "\t\t\tINNER JOIN bus_shipping b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\tWHERE t.isdelete = false AND t.jobtype = 'S'\n" +
                    "\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t" + unloadedWhere + "\n" +
                    "\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t" + corpfilterWhere + "\n" +
                    "\n" +
                    "\t\t\tUNION ALL\n" +
                    "\t\t\tSELECT\n" +
                    "\t\t\t\tt.id\n" +
                    "\t\t\t\t,t.jobdate\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t,t.saleid\n" +
                    "\t\t\t\t,t.inputer\n" +
                    "\t\t\t\t,t.corpid\n" +
                    "\t\t\t\t,t.corpidop\n" +
                    "\t\t\t\t,t.corpidop2\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t" + Afield + "\n" +
                    "\t\t\t\t,b.mawbno AS mblno\n" +
                    "\t\t\t\t,b.hawbno AS hblno\n" +
                    "\t\t\t\t,b.sono\n" +
                    "\t\t\t\t,b.cnortitle\n" +
                    "\t\t\t\t,b.cneetitle\n" +
                    "\t\t\t\t,b.notifytitle\n" +
                    "\t\t\t\t,(SELECT abbr FROM sys_corporation WHERE id = b.agentdesid) AS agenabbr\n" +
                    "\t\t\t\t,to_char( b.flightdate1 ::timestamp, 'yyyy-mm-dd hh24:mi:ss') AS etd\n" +
                    "\t\t\t\t,to_char( b.eta ::timestamp, 'yyyy-mm-dd') AS eta\n" +
                    "\t\t\t\t,NULL AS cls\n" +
                    "\t\t\t\t" + Ofield + "\n" +
                    "\t\t\tFROM fina_jobs t\n" +
                    "\t\t\tINNER JOIN bus_air b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\tWHERE t.isdelete = false AND t.jobtype = 'A'\n" +
                    "\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t" + unloadedWhere + "\n" +
                    "\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t" + corpfilterWhere + "\n" +
                    "\n" +
                    "\t\t\tUNION ALL\n" +
                    "\t\t\tSELECT\n" +
                    "\t\t\t\tt.id\n" +
                    "\t\t\t\t,t.jobdate\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t,t.saleid\n" +
                    "\t\t\t\t,t.inputer\n" +
                    "\t\t\t\t,t.corpid\n" +
                    "\t\t\t\t,t.corpidop\n" +
                    "\t\t\t\t,t.corpidop2\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t" + Afield + "\n" +
                    "\t\t\t\t,'' AS mblno\n" +
                    "\t\t\t\t,'' AS hblno\n" +
                    "\t\t\t\t,b.sono\n" +
                    "\t\t\t\t,'' AS cnortitle\n" +
                    "\t\t\t\t,'' AS cneetitle\n" +
                    "\t\t\t\t,'' AS notifytitle\n" +
                    "\t\t\t\t,'' AS agenabbr\n" +
                    "\t\t\t\t,to_char( b.etd ::timestamp, 'yyyy-mm-dd') AS etd\n" +
                    "\t\t\t\t,NULL AS eta\n" +
                    "\t\t\t\t,to_char( b.cls ::timestamp, 'yyyy-mm-dd') AS cls\n" +
                    "\t\t\t\t" + Ofield + "\n" +
                    "\t\t\tFROM fina_jobs t\n" +
                    "\t\t\tINNER JOIN bus_truck b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\tWHERE t.isdelete = false AND t.jobtype = 'L'\n" +
                    "\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t" + corpfilterWhere + "\n" +
                    "\n" +
                    "\t\t\tUNION ALL\n" +
                    "\t\t\tSELECT\n" +
                    "\t\t\t\tt.id\n" +
                    "\t\t\t\t,t.jobdate\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t,t.saleid\n" +
                    "\t\t\t\t,t.inputer\n" +
                    "\t\t\t\t,t.corpid\n" +
                    "\t\t\t\t,t.corpidop\n" +
                    "\t\t\t\t,t.corpidop2\n" +
                    "\t\t\t\t,t.jobtype\n" +
                    "\t\t\t\t" + Afield + "\n" +
                    "\t\t\t\t,'' AS mblno\n" +
                    "\t\t\t\t,'' AS hblno\n" +
                    "\t\t\t\t,b.sono\n" +
                    "\t\t\t\t,'' AS cnortitle\n" +
                    "\t\t\t\t,'' AS cneetitle\n" +
                    "\t\t\t\t,'' AS notifytitle\n" +
                    "\t\t\t\t,'' AS agenabbr\n" +
                    "\t\t\t\t,NULL AS etd\n" +
                    "\t\t\t\t,NULL AS eta\n" +
                    "\t\t\t\t,NULL AS cls\n" +
                    "\t\t\t\t" + Ofield + "\n" +
                    "\t\t\tFROM fina_jobs t\n" +
                    "\t\t\tINNER JOIN bus_customs b on b.isdelete = FALSE AND t.id = b.jobid\n" +
                    "\t\t\tWHERE t.isdelete = false AND t.jobtype = 'C'\n" +
                    "\t\t\t\t" + dynamicClauseAllWhere + "\n" +
                    "\t\t\t\t" + permissionsWhere + "\n" +
                    "\t\t\t\t" + corpfilterWhere + "";


            LogBean.insertLog(new StringBuffer().append("boxquantitynew获取条件,thissql0为").append(thissql0).append(",thissql1为").append(thissql1));
            Map<String, String> totalMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(thissql0.toString());
            int joblength = Integer.parseInt(String.valueOf(totalMap.get("joblength")));
            if (joblength > 40000) {
                view.setSuccess(false);
                view.setMessage("查询数据超过40000条");
                return view;
            } else {
                List<Map> dataList = daoIbatisTemplate.queryWithUserDefineSql(thissql1.toString());
                String reRet = JSON.toJSONString(dataList);
                view.setSuccess(true);
                view.setData(reRet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogBean.insertLog(new StringBuffer().append("boxquantitynew报错,e为").append(e.getMessage()).append(",thissql0为").append(thissql0).append(",thissql1为").append(thissql1));
        }
        return view;
    }
}
