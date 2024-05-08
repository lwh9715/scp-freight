package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.commerce.BusCommerceResponse;
import com.scp.model.commerce.BusPackListResponse;
import com.scp.service.commerce.BusCommerceService;
import com.scp.service.finance.JobsMgrService;
import com.scp.util.*;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author CIMC
 */

@WebServlet("/commerce")
@ManagedBean(name = "pages.module.commerce.busCommerceBean")
public class BusCommerceBean extends BaseServlet {

    public BusCommerceService busCommerceService() {
        return (BusCommerceService) AppUtils.getBeanFromSpringIoc("busCommerceService");
    }

    public JobsMgrService jobsMgrService() {
        return (JobsMgrService) AppUtils.getBeanFromSpringIoc("jobsMgrService");
    }

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
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "' OR fj.inputer ='" + session.get("username").toString() + "')" //录入人有权限
                    + "\n OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND fj.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_user y WHERE y.custlibid = x.id AND y.userid = " + session.get("userid").toString()
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //关联的业务员的单，都能看到
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_role y WHERE y.custlibid = x.id "
                    + "\n AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //组关联业务员的单，都能看到
                    //过滤工作单指派
                    + "\n OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_commerce y WHERE x.linkid = y.id AND x.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + "))";

            filter += " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT fj.corpid UNION SELECT fj.corpidop " +
                    "UNION SELECT fj.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete = FALSE) " +
                    "AND x.ischoose = TRUE AND userid =" + session.get("userid").toString() + ") OR COALESCE(fj.saleid,0) <= 0)";


            String querySql = "base.commerce.fina_jobs";
            String querySql1 = "base.commerce.bus_commerce";
            String querySql2 = "base.commerce.packlist";
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            args.put("userid", session.get("userid").toString());
            args.put("filter", filter);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> list1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql1, args);
            List list2 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql2, args);
            view.add("jobs", StrUtils.getMapVal(list.get(0), "jobs"));
            view.add("commerce", StrUtils.getMapVal(list1.get(0), "commerce"));
            view.add("buspacklist", list2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 轨迹数据
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "queryJob")
    public BaseView BusCommerceQueryJob(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String sql = "";
            String qryno = "";
            String tempStr = object.get("selectNum").toString().replaceAll("\n", "','");

            if (object.containsKey("selectModel") && object.get("selectModel").toString().equals("nos")) {
                sql += " AND fj.nos in ('" + tempStr + "')";
            } else if (object.containsKey("selectModel") && object.get("selectModel").toString().equals("refno")) {
                sql += " AND fj.refno in ('" + tempStr + "')";
            } else if (object.containsKey("selectModel") && object.get("selectModel").toString().equals("mbl")) {
                sql += " AND (fj.nos in ('" + tempStr + "') OR fj.parentid IN (SELECT id FROM fina_jobs WHERE isdelete = false AND nos IN ('" + tempStr + "')))";
            } else if (object.containsKey("selectModel") && object.get("selectModel").toString().equals("sonum")) {
                qryno += " AND sonum in ('" + tempStr + "')";
            } else if (object.containsKey("selectModel") && object.get("selectModel").toString().equals("ponum")) {
                qryno += " AND ponum in ('" + tempStr + "')";
            }

            //统计报表只显示途曦的工作单
            sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274 AND isdelete = FALSE)))";

            String querySql = "base.commerce.queryJob";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("qryno", qryno);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("query", StrUtils.getMapVal(list.get(0), "query"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取订单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "list")
    public BaseView BusCommerceList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String sql = "";
            String nossql = "";
            String orderby = "ORDER BY fj.inputtime DESC";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND ((fj.nos ilike 'TSZ%' AND fj.deptid <> 1565607982274) OR (fj.deptid = 516230252274 AND fj.deptop <> 1565607982274) OR (fj.deptop = 516230252274 AND fj.deptid <> 1565607982274) OR (fj.nos ilike 'TSZ%' AND fj.deptid = 1565607982274 AND fj.submitime >= '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND fj.nos not ilike 'TBZ%' AND (fj.nos ilike 'TAE%' OR fj.deptop = 516228802274))";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    nossql += " AND (fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%' AND ((fj.nos ilike 'TXM%' AND fj.deptid <> 516230252274) OR (fj.nos ilike 'TBZ%' AND fj.deptid <> 516230252274) OR ((fj.nos ilike 'TSZ%' OR fj.nos not ilike 'TSZ%') AND fj.deptid = 1565607982274 AND fj.deptop <> 516230252274 AND fj.submitime < '2024-02-26') OR ((fj.nos ilike 'TSZ%' OR fj.nos not ilike 'TSZ%') AND fj.deptop = 1565607982274 AND fj.deptid <> 516230252274 AND fj.submitime < '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    nossql += " AND fj.nos ilike 'TDA%'";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%')";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            nossql += " AND fj.nos ilike 'TAE%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            nossql += " AND (fj.nos ilike 'TBZ%' OR fj.nos not ilike 'TXM%')";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            nossql += " AND fj.nos ilike 'TDA%'";
                        }
                    }
                }
            }

            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                sql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR bc.ponum ilike '%" + object.get("nos").toString() + "%'";
                String replaceAll = object.get("nos").toString().replaceAll(" ", "','");
                sql += " OR fj.nos in ('" + replaceAll + "') OR bc.ponum in ('" + replaceAll + "'))";
                orderby = "ORDER BY position(',' || fj.nos ::text || ',' in ',' || array_to_string(Array ['" + replaceAll + "'], ',') || ',')";
            }
            //高级查找etd
            if (object.containsKey("etd") && object.get("etd") != null && !StrUtils.isNull(object.get("etd").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("etd").toString());
                sql += " AND bc.etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //是否ETD为空
            if (object.containsKey("isetd") && object.get("isetd") != null && !StrUtils.isNull(object.get("isetd").toString())) {
                if (object.get("isetd").toString() == "true") {
                    sql += " AND bc.etd is null";
                }
            }
            //ETA
            if (object.containsKey("eta") && object.get("eta") != null && !StrUtils.isNull(object.get("eta").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("eta").toString());
                sql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //是否ETA为空
            if (object.containsKey("iseta") && object.get("iseta") != null && !StrUtils.isNull(object.get("iseta").toString())) {
                if (object.get("iseta").toString() == "true") {
                    sql += " AND bc.eta is null";
                }
            }
            //工作单日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                sql += " AND fj.jobdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //业务日期
            if (object.containsKey("submitime") && object.get("submitime") != null && !StrUtils.isNull(object.get("submitime").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("submitime").toString());
                sql += " AND fj.submitime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //费用日期
            if (object.containsKey("arapdate") && object.get("arapdate") != null && !StrUtils.isNull(object.get("arapdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("arapdate").toString());
                sql += " AND EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = false AND jobid = fj.id AND arapdate BETWEEN '" + array.get(0) + "' AND '" + array.get(1) + "')";
            }
            //PO num
            if (object.containsKey("ponum") && object.get("ponum") != null && !StrUtils.isNull(object.get("ponum").toString())) {
                sql += " AND bc.ponum ilike '%" + object.get("ponum").toString() + "%'";
            }
            if (object.containsKey("mblnum") && object.get("mblnum") != null && !StrUtils.isNull(object.get("mblnum").toString())) {
                sql += " AND bc.mblnum ilike '%" + object.get("mblnum").toString() + "%'";
            }
            //参考号
            if (object.containsKey("refno") && object.get("refno") != null && !StrUtils.isNull(object.get("refno").toString())) {
                sql += " AND (fj.refno2 ilike '%" + object.get("refno").toString() + "%' OR fj.refno ilike '%" + object.get("refno").toString() + "%')";
            }
            if (object.containsKey("sonum") && object.get("sonum") != null && !StrUtils.isNull(object.get("sonum").toString())) {
                sql += " AND bc.sonum ilike '%" + object.get("sonum").toString() + "%'";
            }
            if (object.containsKey("customerabbr") && object.get("customerabbr") != null && !StrUtils.isNull(object.get("customerabbr").toString())) {
                sql += " AND fj.customerabbr ilike '%" + object.get("customerabbr").toString() + "%'";
            }
            //接单公司
            if (object.containsKey("corpabbr") && object.get("corpabbr") != null && !StrUtils.isNull(object.get("corpabbr").toString())) {
                sql += " AND fj.corpid = (SELECT id FROM sys_corporation WHERE code ILIKE '%" + object.get("corpabbr").toString() + "%' " +
                        "OR namec ILIKE '%" + object.get("corpabbr").toString() + "%' AND iscustomer = false LIMIT 1)";
            }
            //操作公司
            if (object.containsKey("corpidopabbr") && object.get("corpidopabbr") != null && !StrUtils.isNull(object.get("corpidopabbr").toString())) {
                sql += " AND fj.corpidop = (SELECT id FROM sys_corporation WHERE code ILIKE '%" + object.get("corpidopabbr").toString() + "%' " +
                        "OR namec ILIKE '%" + object.get("corpidopabbr").toString() + "%' AND iscustomer = false LIMIT 1)";
            }
            //pol
            if (object.containsKey("polnamec") && object.get("polnamec") != null && object.get("polnamec") != null && !StrUtils.isNull(object.get("polnamec").toString())) {
                sql += " AND bc.polnamec ilike '%" + object.get("polnamec").toString() + "%'";
            }
            //pod
            if (object.containsKey("podnamec") && object.get("podnamec") != null && !StrUtils.isNull(object.get("podnamec").toString())) {
                sql += " AND bc.podnamec ilike '%" + object.get("podnamec").toString() + "%'";
            }
            //业务员
            if (object.containsKey("sales") && object.get("sales") != null && !StrUtils.isNull(object.get("sales").toString())) {
                sql += " AND fj.saleid = (SELECT id FROM sys_user WHERE namec = '" + object.get("sales").toString() + "' LIMIT 1)";
            }
            //分公司业务员
            if (object.containsKey("opsales") && object.get("opsales") != null && !StrUtils.isNull(object.get("opsales").toString())) {
                sql += " AND bc.saleidop = (SELECT id FROM sys_user WHERE namec = '" + object.get("opsales").toString() + "' LIMIT 1)";
            }
            //操作
            if (object.containsKey("opname") && object.get("opname") != null && !StrUtils.isNull(object.get("opname").toString())) {
                sql += "AND EXISTS (SELECT 1 FROM sys_user_assign x , bus_commerce y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))";
            }
            //客服
            if (object.containsKey("csname") && object.get("csname") != null && !StrUtils.isNull(object.get("csname").toString())) {
                sql += "AND EXISTS (SELECT 1 FROM sys_user_assign x , bus_commerce y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))";
            }
            //是否已入仓
            if (object.containsKey("isinwmsin") && object.get("isinwmsin") != null && !StrUtils.isNull(object.get("isinwmsin").toString())) {
                if (object.get("isinwmsin").toString() == "true") {
                    sql += " AND bc.wmsindate is not null";
                } else {
                    sql += " AND bc.wmsindate is null";
                }
            }
            //是否香港代收付
            if (object.containsKey("isghk") && object.get("isghk") != null && !StrUtils.isNull(object.get("isghk").toString())) {
                if (object.get("isghk").toString() == "true") {
                    sql += " AND EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 157970752274 AND isdelete = FALSE)";
                }
            }
            //集拼
            if (object.containsKey("ismbl") && object.get("ismbl") != null && !StrUtils.isNull(object.get("ismbl").toString())) {
                if (object.get("ismbl").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)";
                } else {
                    sql += " AND fj.jobtype = 'D' AND fj.parentid > 0";
                }
            }
            //非集拼
            if (object.containsKey("isorder") && object.get("isorder") != null && !StrUtils.isNull(object.get("isorder").toString())) {
                if (object.get("isorder").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND EXISTS(SELECT 1 FROM fina_jobs WHERE parentid <> fj.id AND isdelete = FALSE)";
                } else {
                    sql += " AND fj.jobtype = 'D' AND fj.parentid = 0";
                }
            }
            //完成状态
            if (object.containsKey("iscomplete") && object.get("iscomplete") != null && !StrUtils.isNull(object.get("iscomplete").toString())) {
                if (object.get("iscomplete").toString() == "true") {
                    sql += " AND fj.iscomplete = true";
                } else {
                    sql += " AND fj.iscomplete = false";
                }
            }

            //往来不一致
            if (object.containsKey("isconsistent") && object.get("isconsistent") != null && !StrUtils.isNull(object.get("isconsistent").toString())) {
                if (object.get("isconsistent").toString().equals("true")) {
                    sql += " AND (SELECT (SELECT COUNT(1) AS c\n" +
                            "        FROM (SELECT t.corpid,\n" +
                            "                     t.customerid,\n" +
                            "                     t.currency,\n" +
                            "                     COALESCE(t.amount, 0) + COALESCE(t2.amount, 0) AS amount\n" +
                            "              FROM (SELECT a.corpid,\n" +
                            "                           customerid,\n" +
                            "                           currency,\n" +
                            "                           sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
                            "                    FROM fina_arap a\n" +
                            "                    WHERE a.jobid = fj.id\n" +
                            "                      AND a.isdelete = FALSE\n" +
                            "                      AND a.rptype != 'O'\n" +
                            "                      AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
                            "                    GROUP BY a.corpid, customerid, currency) t\n" +
                            "                       LEFT JOIN (SELECT a.corpid,\n" +
                            "                                         customerid,\n" +
                            "                                         currency,\n" +
                            "                                         sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
                            "                                  FROM fina_arap a\n" +
                            "                                  WHERE a.jobid = fj.id\n" +
                            "                                    AND a.isdelete = FALSE\n" +
                            "                                    AND a.rptype != 'O'\n" +
                            "                                    AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
                            "                                  GROUP BY a.corpid, customerid, currency) t2 ON (t2.customerid = t.corpid AND t2.corpid = t.customerid AND t2.currency = t.currency)\n" +
                            "             ) TT\n" +
                            "        WHERE TT.amount != 0) > 0)";
                }
            }

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "' OR fj.inputer ='" + session.get("username").toString() + "')" //录入人有权限
                    + "\n OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND fj.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_user y WHERE y.custlibid = x.id AND y.userid = " + session.get("userid").toString()
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //关联的业务员的单，都能看到
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_role y WHERE y.custlibid = x.id "
                    + "\n AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //组关联业务员的单，都能看到
                    //过滤工作单指派
                    + "\n OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_commerce y WHERE x.linkid = y.id AND x.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + "))";

            filter += " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT fj.corpid UNION SELECT fj.corpidop " +
                    "UNION SELECT fj.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete = FALSE) " +
                    "AND x.ischoose = TRUE AND userid =" + session.get("userid").toString() + ") OR COALESCE(fj.saleid,0) <= 0)";

            String querySql = "base.commerce.list";
            String authSql = "base.commerce.auth";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("nostype", nossql);
            args.put("filter", filter);
            args.put("orderby", orderby);
            args.put("userid", session.get("userid").toString());
            args.put("corpid", session.get("corpid").toString());
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> authList = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(authSql, args);
            view.add("commercelist", StrUtils.getMapVal(list.get(0), "commercelist"));
            view.add("auth", StrUtils.getMapVal(authList.get(0), "auth"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取统计报表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "business")
    public BaseView BusCommerceBusinessList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            String sql = "";
            String arapsql = "";
            String arbillsql = "";
            String isvirarapsql = "";
            String greater = "";
            String wmsin = "";
            String nossql = "";
            String corpsql = "";
            String financial = "";
            String airsql = "";
            String shipsql = "";
            String landsql = "";
            String custsql = "";
            String commercesql = "";
            String newfield = " '' AS newfield";


            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            //全国权限 可查看所有利润
            String roleSql = "SELECT EXISTS(SELECT 1 FROM sys_user x,sys_userinrole y WHERE x.id = " + session.get("userid").toString() + " AND x.id = y.userid AND y.roleid = 4003066888) AS isexists;";
            Map roleMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(roleSql);
            if (StrUtils.getMapVal(roleMap, "isexists").equals("false")) {
                corpsql += " AND a.corpid = " + session.get("corpid").toString();
            }

            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND ((fj.nos ilike 'TSZ%' AND fj.deptid <> 1565607982274) OR (fj.deptid = 516230252274 AND fj.deptop <> 1565607982274) OR (fj.deptop = 516230252274 AND fj.deptid <> 1565607982274) OR (fj.nos ilike 'TSZ%' AND fj.deptid = 1565607982274 AND fj.submitime >= '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND fj.nos not ilike 'TBZ%' AND (fj.nos ilike 'TAE%' OR fj.deptop = 516228802274))";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    nossql += " AND (fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%' AND ((fj.nos ilike 'TXM%' AND fj.deptid <> 516230252274) OR (fj.nos ilike 'TBZ%' AND fj.deptid <> 516230252274) OR ((fj.nos ilike 'TSZ%' OR fj.nos not ilike 'TSZ%') AND fj.deptid = 1565607982274 AND fj.deptop <> 516230252274 AND fj.submitime < '2024-02-26') OR ((fj.nos ilike 'TSZ%' OR fj.nos not ilike 'TSZ%') AND fj.deptop = 1565607982274 AND fj.deptid <> 516230252274 AND fj.submitime < '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    nossql += " AND fj.nos ilike 'TDA%'";
                } else if (object.get("nostype").toString().equals("TOC")) {
                    nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND (fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%' OR fj.deptid = 1565607982274 OR fj.deptop = 1565607982274 OR fj.nos ilike 'TSZ%' OR fj.deptid = 516230252274 OR fj.deptop = 516230252274))";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND (fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%' OR fj.deptop = 1565607982274 OR fj.nos ilike 'TSZ%' OR fj.deptop = 516230252274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND fj.nos not ilike 'TBZ%' AND (fj.nos ilike 'TAE%' OR fj.deptop = 516228802274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%' AND (fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%' OR fj.deptop = 1565607982274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            nossql += " AND (fj.nos ilike 'TDA%')";
                        }
                    }
                }
            }

            //默认显示1个月内的数据--打开高级查找后查恢复所有数据
            if (object.containsKey("islimit") && object.get("islimit") != null && !StrUtils.isNull(object.get("islimit").toString())) {
                if (object.get("islimit").toString() == "true") {
                    sql += " AND fj.jobdate > CURRENT_DATE - INTERVAL '1 months'";
                }
            }

            //高级查找ETD
            if (object.containsKey("etd") && object.get("etd") != null && !StrUtils.isNull(object.get("etd").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("etd").toString());
                airsql += " AND bc.singtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                custsql += " AND bc.singtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                shipsql += " AND bc.etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
                landsql += " AND bc.loadtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                commercesql += " AND bc.etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //是否ETD为空
            if (object.containsKey("isetd") && object.get("isetd") != null && !StrUtils.isNull(object.get("isetd").toString())) {
                if (object.get("isetd").toString() == "true") {
                    airsql += " AND bc.singtime is null";
                    custsql += " AND bc.singtime is null";
                    shipsql += " AND bc.etd is null";
                    landsql += " AND bc.loadtime is null";
                    commercesql += " AND bc.etd is null";
                }
            }
            //是否ETA为空
            if (object.containsKey("iseta") && object.get("iseta") != null && !StrUtils.isNull(object.get("iseta").toString())) {
                if (object.get("iseta").toString() == "true") {
                    airsql += " AND bc.eta is null";
                    custsql += " AND bc.eta is null";
                    shipsql += " AND bc.eta is null";
                    landsql += " AND false";
                    commercesql += " AND bc.eta is null";
                }
            }
            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                //批量查询支持空格 和 逗号
                String replaceAll = object.get("nos").toString().replaceAll(",", "','").replaceAll(" ", "','");
                airsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                shipsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                landsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                custsql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') OR bc.sono in ('" + replaceAll + "'))";
                commercesql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%' OR fj.nos in ('" + replaceAll + "') " +
                        "OR bc.ponum ilike '%" + object.get("nos").toString() + "%' OR bc.ponum in ('" + replaceAll + "') OR bc.sonum in ('" + replaceAll + "'))";
            }
            //ETA
            if (object.containsKey("eta") && object.get("eta") != null && !StrUtils.isNull(object.get("eta").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("eta").toString());
                airsql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                custsql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                shipsql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                commercesql += " AND bc.eta between '" + array.get(0) + "' and '" + array.get(1) + "'";
                landsql += " AND false";
            }
            //工作单日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                sql += " AND fj.jobdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //业务日期
            if (object.containsKey("submitime") && object.get("submitime") != null && !StrUtils.isNull(object.get("submitime").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("submitime").toString());
                sql += " AND fj.submitime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //费用日期
            if (object.containsKey("arapdate") && object.get("arapdate") != null && !StrUtils.isNull(object.get("arapdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("arapdate").toString());
                sql += " AND EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = false AND jobid = fj.id AND arapdate BETWEEN '" + array.get(0) + "' AND '" + array.get(1) + "')";
            }
            //期间sql
            if (object.containsKey("financialdate") && object.get("financialdate") != null && !StrUtils.isNull(object.get("financialdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("financialdate").toString());
                //费用过滤
                isvirarapsql += " AND (SELECT x.year || '-' || x.period FROM fs_vch x WHERE x.isdelete = FALSE\n" +
                        " AND x.id = ANY (SELECT y.vchid FROM fs_vchdtl y WHERE y.isdelete = FALSE AND y.id = a.vchdtlid LIMIT 1)\n" +
                        " LIMIT 1) BETWEEN '" + array.get(0) + "' and '" + array.get(1) + "'";
                //工作单过滤
                sql += " AND EXISTS(SELECT 1 FROM fs_vch x WHERE x.isdelete = FALSE\n" +
                        " AND (x.year || '-' || x.period) BETWEEN '" + array.get(0) + "' and '" + array.get(1) + "'\n" +
                        " AND x.id in (SELECT y.vchid FROM fs_vchdtl y WHERE y.isdelete = FALSE \n" +
                        " AND y.id in (SELECT xx.vchdtlid FROM fina_arap xx WHERE xx.jobid = fj.id)) LIMIT 1)";
            }
            //M单查h单
            if (object.containsKey("mbl") && object.get("mbl") != null && !StrUtils.isNull(object.get("mbl").toString())) {
                sql += " AND parentid = (SELECT id FROM fina_jobs WHERE nos = '" + object.get("mbl").toString() + "' AND isdelete = FALSE LIMIT 1)";
            }
            //po Num
            if (object.containsKey("ponum") && object.get("ponum") != null && !StrUtils.isNull(object.get("ponum").toString())) {
                airsql += " AND false";
                custsql += " AND false";
                shipsql += " AND false";
                landsql += " AND false";
                commercesql += " AND bc.ponum ilike '%" + object.get("ponum").toString() + "%'";
            }
            //mbl Num
            if (object.containsKey("mblnum") && object.get("mblnum") != null && !StrUtils.isNull(object.get("mblnum").toString())) {
                airsql += " AND bc.mawbno ilike '%" + object.get("mblnum").toString() + "%'";
                shipsql += " AND bc.mblno ilike '%" + object.get("mblnum").toString() + "%'";
                landsql += " AND false";
                custsql += " AND false";
                commercesql += " AND bc.mblnum ilike '%" + object.get("mblnum").toString() + "%'";
            }
            //so
            if (object.containsKey("sonum") && object.get("sonum") != null && !StrUtils.isNull(object.get("sonum").toString())) {
                airsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                custsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                shipsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                landsql += " AND bc.sono ilike '%" + object.get("sonum").toString() + "%'";
                commercesql += " AND bc.sonum ilike '%" + object.get("sonum").toString() + "%'";
            }
            //参考号
            if (object.containsKey("refno") && object.get("refno") != null && !StrUtils.isNull(object.get("refno").toString())) {
                sql += " AND (fj.refno2 ilike '%" + object.get("refno").toString() + "%' OR fj.refno ilike '%" + object.get("refno").toString() + "%')";
            }
            //客商名称
            if (object.containsKey("customerabbr") && object.get("customerabbr") != null && !StrUtils.isNull(object.get("customerabbr").toString())) {
                sql += " AND fj.customerabbr ilike '%" + object.get("customerabbr").toString() + "%'";
            }
            //接单公司
            if (object.containsKey("corpabbr") && object.get("corpabbr") != null && !StrUtils.isNull(object.get("corpabbr").toString())) {
                sql += " AND fj.corpid = (SELECT id FROM sys_corporation WHERE code ILIKE '%" + object.get("corpabbr").toString() + "%' " +
                        "OR namec ILIKE '%" + object.get("corpabbr").toString() + "%' AND iscustomer = false LIMIT 1)";
            }
            //操作公司
            if (object.containsKey("corpidopabbr") && object.get("corpidopabbr") != null && !StrUtils.isNull(object.get("corpidopabbr").toString())) {
                sql += " AND fj.corpidop = (SELECT id FROM sys_corporation WHERE code ILIKE '%" + object.get("corpidopabbr").toString() + "%' " +
                        "OR namec ILIKE '%" + object.get("corpidopabbr").toString() + "%' AND iscustomer = false LIMIT 1)";
            }
            if (object.containsKey("polnamec") && object.get("polnamec") != null && object.get("polnamec") != null && !StrUtils.isNull(object.get("polnamec").toString())) {
                airsql += " AND bc.polcode ilike '%" + object.get("polnamec").toString() + "%'";
                shipsql += " AND bc.polcode ilike '%" + object.get("polnamec").toString() + "%'";
                landsql += " AND FALSE";
                custsql += " AND FALSE";
                commercesql += " AND bc.polcode ilike '%" + object.get("polnamec").toString() + "%'";
            }
            if (object.containsKey("podnamec") && object.get("podnamec") != null && !StrUtils.isNull(object.get("podnamec").toString())) {
                airsql += " AND bc.podcode ilike '%" + object.get("polnamec").toString() + "%'";
                shipsql += " AND bc.podcode ilike '%" + object.get("polnamec").toString() + "%'";
                landsql += " AND FALSE";
                custsql += " AND FALSE";
                commercesql += " AND bc.podcode ilike '%" + object.get("polnamec").toString() + "%'";
            }
            //业务员
            if (object.containsKey("sales") && object.get("sales") != null && !StrUtils.isNull(object.get("sales").toString())) {
                sql += " AND fj.saleid = (SELECT id FROM sys_user WHERE namec = '" + object.get("sales").toString() + "' LIMIT 1)";
            }
            //分公司业务员
            if (object.containsKey("opsales") && object.get("opsales") != null && !StrUtils.isNull(object.get("opsales").toString())) {
                commercesql += " AND bc.saleidop = (SELECT id FROM sys_user WHERE namec = '" + object.get("opsales").toString() + "' LIMIT 1)";
                airsql += " AND FALSE";
                shipsql += " AND FALSE";
                landsql += " AND FALSE";
                custsql += " AND FALSE";
            }
            //录入人
            if (object.containsKey("inputer") && object.get("inputer") != null && !StrUtils.isNull(object.get("inputer").toString())) {
                sql += " AND fj.inputer ilike '%" + object.get("inputer").toString() + "%'";
            }
            //更新人
            if (object.containsKey("updater") && object.get("updater") != null && !StrUtils.isNull(object.get("updater").toString())) {
                sql += " AND fj.updater ilike '%" + object.get("updater").toString() + "%'";
            }
            //操作
            if (object.containsKey("opname") && object.get("opname") != null && !StrUtils.isNull(object.get("opname").toString())) {
                sql += " AND (EXISTS (SELECT x.userid FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_air y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_truck y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_commerce y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'O' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("opname").toString() + "%' LIMIT 1)))";
            }
            //客服
            if (object.containsKey("csname") && object.get("csname") != null && !StrUtils.isNull(object.get("csname").toString())) {
                sql += " AND (EXISTS (SELECT x.userid FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_air y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_truck y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1))\n" +
                        "OR EXISTS (SELECT 1 FROM sys_user_assign x , bus_commerce y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id " +
                        "AND y.jobid = fj.id AND x.linktype = 'J' AND x.roletype = 'C' " +
                        "AND userid = (SELECT id FROM sys_user WHERE namec ilike '" + object.get("csname").toString() + "%' LIMIT 1)))";
            }
            //是否入仓
            if (object.containsKey("isinwmsin") && object.get("isinwmsin") != null && !StrUtils.isNull(object.get("isinwmsin").toString())) {
                if (object.get("isinwmsin").toString() == "true") {
                    wmsin += " AND bc.wmsindate is not null";
                } else {
                    wmsin += " AND bc.wmsindate is null";
                }
            }
            //是否主单
            if (object.containsKey("ismbl") && object.get("ismbl") != null && !StrUtils.isNull(object.get("ismbl").toString())) {
                if (object.get("ismbl").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)";
                } else {
                    sql += " AND fj.jobtype = 'D' AND fj.parentid > 0";
                }
            }
            //是否香港代收付
            if (object.containsKey("isghk") && object.get("isghk") != null && !StrUtils.isNull(object.get("isghk").toString())) {
                if (object.get("isghk").toString() == "true") {
                    sql += " AND EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 157970752274 AND isdelete = FALSE)";
                }
            }
            //非集拼
            if (object.containsKey("isorder") && object.get("isorder") != null && !StrUtils.isNull(object.get("isorder").toString())) {
                if (object.get("isorder").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND NOT EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)";
                } else {
                    sql += " AND fj.jobtype = 'D' AND fj.parentid = 0";
                }
            }
            //未收款账单（周账单）
            if (object.containsKey("isunpaidpay") && object.get("isunpaidpay") != null && !StrUtils.isNull(object.get("isunpaidpay").toString())) {
                if (object.get("isunpaidpay").toString().equals("true")) {
                    sql += " AND NOT EXISTS(SELECT 1 FROM fina_arap WHERE jobid = fj.id AND isdelete = FALSE" +
                            " AND (billid is NOT null or blno is NOT null) AND araptype = 'AR' AND corpid = 11540072274 AND rptype != 'O')" +
                            " AND ((SELECT sum(COALESCE(amtstl2, 0)) = 0 FROM fina_arap WHERE isdelete = false AND araptype = 'AR' AND jobid = fj.id)\n" +
                            "    OR NOT EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = false AND araptype = 'AR' AND jobid = fj.id))";

                    arbillsql = " AND a.amtstl2 = 0 ";
                }
            }
            //是否外配
            if (object.containsKey("isexternal") && object.get("isexternal") != null && !StrUtils.isNull(object.get("isexternal").toString())) {
                shipsql += " AND bc.isexternalmatching = 'T'";
                commercesql += " AND FALSE";
                landsql += " AND FALSE";
                custsql += " AND FALSE";
                airsql += " AND FALSE";
            }
            //完成状态
            if (object.containsKey("iscomplete") && object.get("iscomplete") != null && !StrUtils.isNull(object.get("iscomplete").toString())) {
                if (object.get("iscomplete").toString() == "true") {
                    sql += " AND fj.iscomplete = true";
                } else {
                    sql += " AND fj.iscomplete = false";
                }
            }
            //审核状态
            if (object.containsKey("ischeck") && object.get("ischeck") != null && !StrUtils.isNull(object.get("ischeck").toString())) {
                if (object.get("ischeck").toString() == "true") {
                    sql += " AND fj.ischeck = true";
                } else {
                    sql += " AND fj.ischeck = false";
                }
            }
            //贸易方式
            if (object.containsKey("istrade") && object.get("istrade") != null && !StrUtils.isNull(object.get("istrade").toString())) {
                if (object.get("istrade").toString() == "true") {
                    sql += " AND fj.tradeway <> ''";
                } else {
                    sql += " AND fj.tradeway = ''";
                }
            }
            //费用完成状态--应收
            if (object.containsKey("isar") && object.get("isar") != null && !StrUtils.isNull(object.get("isar").toString())) {
                if (object.get("isar").toString() == "true") {
                    sql += " AND fj.iscomplete_ar = true";
                } else {
                    sql += " AND fj.iscomplete_ar = false";
                }
            }
            //费用完成状态--应付
            if (object.containsKey("isap") && object.get("isap") != null && !StrUtils.isNull(object.get("isap").toString())) {
                if (object.get("isap").toString() == "true") {
                    sql += " AND fj.iscomplete_ap = true";
                } else {
                    sql += " AND fj.iscomplete_ap = false";
                }
            }
            //费用完成状态--财务
            if (object.containsKey("isfinancial") && object.get("isfinancial") != null && !StrUtils.isNull(object.get("isfinancial").toString())) {
                if (object.get("isfinancial").toString() == "true") {
                    sql += " AND fj.isconfirm2_pp = true";
                } else {
                    sql += " AND fj.isconfirm2_pp = false";
                }
            }
            //亏损状态
            if (object.containsKey("islose") && object.get("islose") != null && !StrUtils.isNull(object.get("islose").toString())) {
                if (object.get("islose").toString() == "true") {
                    greater += " AND profitusd < 0";
                } else {
                    greater += " AND profitrmb < 0";
                }
            }
            //增减费用
            if (object.containsKey("isamend") && object.get("isamend") != null && !StrUtils.isNull(object.get("isamend").toString())) {

                if (object.get("isamend").toString().equals("true")) {

                    //费用日期
                    if (object.containsKey("arapdate") && object.get("arapdate") != null && !StrUtils.isNull(object.get("arapdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("arapdate").toString());
                        isvirarapsql += " AND a.isamend = TRUE AND a.arapdate BETWEEN '" + array.get(0) + "' AND '" + array.get(1) + "'";
                    }

                    sql += " AND (SELECT EXISTS(SELECT 1 FROM fina_arap WHERE jobid = fj.id AND isdelete = FALSE AND isamend = TRUE))";
                }
            }
            //虚拟费用
            if (object.containsKey("isvirtual") && object.get("isvirtual") != null && !StrUtils.isNull(object.get("isvirtual").toString())) {
                if (object.get("isvirtual").toString().equals("true")) {
                    isvirarapsql += " AND a.rptype = 'O'";
                } else {
                    isvirarapsql += " AND a.rptype <> 'O'";
                }
            }

            //合并费用不一致
            if (object.containsKey("ismergercost") && object.get("ismergercost") != null && !StrUtils.isNull(object.get("ismergercost").toString())) {
                if (object.get("ismergercost").toString().equals("true")) {
                    sql += " AND fj.jobtype <> 'D' AND EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)" +
                            " AND ((SELECT COALESCE(sum(amount), 0) FROM fina_arap " +
                            " WHERE jobid = fj.id and rptype = 'O' and araptype = 'AR' AND isdelete = FALSE)" +
                            " <> (SELECT COALESCE(sum(amount), 0) FROM fina_arap " +
                            " WHERE jobid IN (SELECT id FROM fina_jobs WHERE isdelete = FALSE AND parentid = fj.id) " +
                            " AND rptype = 'O' AND araptype = 'AP' AND isdelete = FALSE))";
                }
            }

            //业务日期不一致
            if (object.containsKey("issubmitime") && object.get("issubmitime") != null && !StrUtils.isNull(object.get("issubmitime").toString())) {
                if (object.get("issubmitime").toString() == "true") {
                    sql += " AND (EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.parentid = fj.id AND xx.isdelete = FALSE AND xx.jobtype = 'D' AND xx.submitime <> fj.submitime) " +
                            "OR fj.submitime IS NULL) AND fj.jobdate > '2024-01-01'";
                }
            }

            //承担负利润
            if (object.containsKey("undertake") && object.get("undertake") != null && !StrUtils.isNull(object.get("undertake").toString())) {
                if (object.get("undertake").toString() == "true") {
                    sql += " AND fj.jobtype <> 'D' AND (fj.deptid = 516230252274 OR fj.deptop = 516230252274) " +
                            "AND EXISTS(SELECT 1 FROM fina_jobs xx WHERE (xx.deptid = 1565607982274 OR xx.deptop = 1565607982274) AND parentid = fj.id AND xx.isdelete = FALSE)";

                    greater += " AND profitrmb < 0";

                    newfield = " NULLIF((select SUM(y.vol) from fina_jobs x, bus_commerce y where x.id = y.jobid and x.isdelete = false " +
                            "and x.parentid = result.id AND (x.deptid = 1565607982274 OR x.deptop = 1565607982274)) / " +
                            "(select SUM(y.vol) from fina_jobs x, bus_commerce y where x.id = y.jobid and x.isdelete = false" +
                            " and x.parentid = result.id) * result.profitrmb, 0)::NUMERIC(18, 2) AS newfield";

                }
            }

            //欠款状态
            if (object.containsKey("isowe") && object.get("isowe") != null && !StrUtils.isNull(object.get("isowe").toString())) {
                if (object.get("isowe").toString() == "true") {
                    sql += " AND ((SELECT COALESCE (SUM (f_amtto((CASE WHEN isamend = TRUE " +
                            "AND (CASE WHEN COALESCE(f_sys_config('fina_arap_porfit_isamend_jobdate'),'N') = 'Y' " +
                            "THEN TRUE ELSE FALSE END) = FALSE THEN a.arapdate ELSE fj.jobdate END), a.currency, 'CNY', a.amount - a.amtstl2)), 0) " +
                            "FROM fina_arap a WHERE a.isdelete = FALSE AND a.araptype = 'AR' AND a.rptype <> 'O' " +
                            "AND a.jobid = fj.id AND a.corpid =" + session.get("corpid").toString() + ") > 0)";
                } else {
                    sql += " AND ((SELECT COALESCE (SUM (f_amtto((CASE WHEN isamend = TRUE " +
                            "AND (CASE WHEN COALESCE(f_sys_config('fina_arap_porfit_isamend_jobdate'),'N') = 'Y' " +
                            "THEN TRUE ELSE FALSE END) = FALSE THEN a.arapdate ELSE fj.jobdate END), a.currency, 'CNY', a.amount - a.amtstl2)), 0) " +
                            "FROM fina_arap a WHERE a.isdelete = FALSE AND a.araptype = 'AP' AND a.rptype <> 'O' " +
                            "AND a.jobid = fj.id AND a.corpid =" + session.get("corpid").toString() + ") > 0)";
                }
            }

            //往来不一致
            if (object.containsKey("isconsistent") && object.get("isconsistent") != null && !StrUtils.isNull(object.get("isconsistent").toString())) {
                if (object.get("isconsistent").toString().equals("true")) {
                    sql += " AND (SELECT (SELECT COUNT(1) AS c\n" +
                            "        FROM (SELECT t.corpid,\n" +
                            "                     t.customerid,\n" +
                            "                     t.currency,\n" +
                            "                     COALESCE(t.amount, 0) + COALESCE(t2.amount, 0) AS amount\n" +
                            "              FROM (SELECT a.corpid,\n" +
                            "                           customerid,\n" +
                            "                           currency,\n" +
                            "                           sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
                            "                    FROM fina_arap a\n" +
                            "                    WHERE a.jobid = fj.id\n" +
                            "                      AND a.isdelete = FALSE\n" +
                            "                      AND a.rptype != 'O'\n" +
                            "                      AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
                            "                    GROUP BY a.corpid, customerid, currency) t\n" +
                            "                       LEFT JOIN (SELECT a.corpid,\n" +
                            "                                         customerid,\n" +
                            "                                         currency,\n" +
                            "                                         sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
                            "                                  FROM fina_arap a\n" +
                            "                                  WHERE a.jobid = fj.id\n" +
                            "                                    AND a.isdelete = FALSE\n" +
                            "                                    AND a.rptype != 'O'\n" +
                            "                                    AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
                            "                                  GROUP BY a.corpid, customerid, currency) t2 ON (t2.customerid = t.corpid AND t2.corpid = t.customerid AND t2.currency = t.currency)\n" +
                            "             ) TT\n" +
                            "        WHERE TT.amount != 0) > 0)";
                }
            }

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "' OR fj.inputer ='" + session.get("username").toString() + "')" //录入人有权限
                    + "\n OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND fj.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_user y WHERE y.custlibid = x.id AND y.userid = " + session.get("userid").toString()
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //关联的业务员的单，都能看到
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_role y WHERE y.custlibid = x.id "
                    + "\n AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n AND x.libtype = 'S' AND x.userid = fj.saleid )" //组关联业务员的单，都能看到
                    //过滤工作单指派
                    + "\n OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_commerce y WHERE x.linkid = y.id AND x.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + "))";

            filter += " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT fj.corpid UNION SELECT fj.corpidop " +
                    "UNION SELECT fj.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete = FALSE) " +
                    "AND x.ischoose = TRUE AND userid =" + session.get("userid").toString() + ") OR COALESCE(fj.saleid,0) <= 0)";

            //统计报表只显示途曦的工作单
            sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274 AND isdelete = FALSE)))";

            String querySql = "base.commerce.business";
            String authSql = "base.commerce.auth";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("airsql", airsql);
            args.put("shipsql", shipsql);
            args.put("landsql", landsql);
            args.put("custsql", custsql);
            args.put("commercesql", commercesql);
            args.put("filter", filter);
            args.put("nostype", nossql);
            args.put("arapsql", arapsql);
            args.put("arbillsql", arbillsql);
            args.put("isvirarapsql", isvirarapsql);
            args.put("greater", greater);
            args.put("newfield", newfield);
            args.put("isinwmsin", wmsin);
            args.put("corpsql", corpsql);
            args.put("financial", financial);
            args.put("userid", session.get("userid").toString());
            args.put("corpid", session.get("corpid").toString());

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> authList = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(authSql, args);
            view.add("businesslist", StrUtils.getMapVal(list.get(0), "businesslist"));
            view.add("auth", StrUtils.getMapVal(authList.get(0), "auth"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取预报列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "shipments")
    public BaseView BusCommerceShipments(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String sql = "";

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "' OR fj.inputer ='" + session.get("username").toString() + "')" //录入人有权限
                    + "\n OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND fj.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib x , sys_custlib_user y  "
                    + "\n				WHERE y.custlibid = x.id  "
                    + "\n					AND y.userid = " + session.get("userid").toString()
                    + "\n					AND x.libtype = 'S'  "
                    + "\n					AND x.userid = fj.saleid " //关联的业务员的单，都能看到
                    + ")"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib x , sys_custlib_role y  "
                    + "\n				WHERE y.custlibid = x.id  "
                    + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n					AND x.libtype = 'S'  "
                    + "\n					AND x.userid = fj.saleid " //组关联业务员的单，都能看到
                    + ")"
                    //过滤工作单指派
                    + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_commerce y WHERE x.linkid = y.id AND x.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + ")"
                    + "\n)";

            filter += " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT fj.corpid UNION SELECT fj.corpidop " +
                    "UNION SELECT fj.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete = FALSE) " +
                    "AND x.ischoose = TRUE AND userid =" + session.get("userid").toString() + ") OR COALESCE(fj.saleid,0) <= 0)";

            String querySql = "base.commerce.shipments";
            Map args = new HashMap();
            args.put("filter", filter);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("shipments", StrUtils.getMapVal(list.get(0), "shipments"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 并单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "joblist")
    public BaseView BusCommerceJobList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String corpsql = "";
            corpsql += " AND a.corpid = " + session.get("corpid").toString();

            Map args = new HashMap();
            args.put("id", json);
            args.put("corpsql", corpsql);
            String querySql = "base.commerce.joblist";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("joblist", StrUtils.getMapVal(list.get(0), "joblist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 分单利润
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "profitchild")
    public BaseView BusCommerceProfitChild(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "base.commerce.profitchild";
            String corpsql = "";
            corpsql += " AND a.corpid = " + session.get("corpid").toString();
            Map args = new HashMap();
            args.put("id", json);
            args.put("corpsql", corpsql);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("profitchild", StrUtils.getMapVal(list.get(0), "profitchild"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 统计报表-分单利润
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "checkprofit")
    public BaseView BusCommerceCheckProfit(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "base.commerce.payprofit";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            String ids = "";
            String[] idList = json.split(",");
            for (int i = 0; i < idList.length; i++) {
                String idsSql = "SELECT string_agg(id::varchar,',') as ids\n" +
                        "FROM (\n" +
                        "         SELECT x1.id\n" +
                        "         FROM fina_jobs x1\n" +
                        "         WHERE x1.isdelete = FALSE\n" +
                        "           AND (x1.id = (SELECT x2.parentid FROM fina_jobs x2 WHERE x2.isdelete = FALSE AND x2.id = " + idList[i] + ")\n" +
                        "             OR x1.parentid in (SELECT x3.parentid FROM fina_jobs x3 WHERE x3.isdelete = FALSE AND x3.parentid <> 0 AND x3.id = " + idList[i] + ")\n" +
                        "             OR x1.id = (SELECT x4.id FROM fina_jobs x4 WHERE x4.isdelete = FALSE AND x4.id = " + idList[i] + ")\n" +
                        "             OR x1.parentid in (SELECT x5.id FROM fina_jobs x5 WHERE x5.isdelete = FALSE AND x5.id = " + idList[i] + "))\n" +
                        "         ORDER BY (CASE x1.jobtype WHEN 'D' THEN 0 ELSE 1 END) DESC\n" +
                        "     ) AS json;";

                Map idsMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(idsSql);
                ids += StrUtils.getMapVal(idsMap, "ids") + ",";
            }
            //正则表达式 移除字符串最后出现的逗号
            String oid = ids.replaceAll(",\\s*$", "");

            Map args = new HashMap();
            args.put("id", oid);
            args.put("userid", session.get("userid").toString());
            args.put("corpsql", " AND a.corpid = " + session.get("corpid").toString());
            args.put("orderby", "ORDER BY position(',' || x.id::text || ',' in ',' || array_to_string(Array ['" + oid + "'], ',') || ',')");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("payprofit", StrUtils.getMapVal(list.get(0), "payprofit"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 付款申请-分单利润
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "payprofit")
    public BaseView BusCommercePayProfit(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "base.commerce.payprofit";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            //获取工作单ID
            String idSql = "SELECT (string_agg(DISTINCT jobid::varchar,',')) as ids FROM fina_arap where id in (SELECT arapid FROM fina_rpreqdtl where isdelete = FALSE AND amtreq <> 0 AND rpreqid = " + json + ");";
            Map idMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(idSql);

            String ids = "";
            String[] idList = StrUtils.getMapVal(idMap, "ids").split(",");
            for (int i = 0; i < idList.length; i++) {
                String idsSql = "SELECT string_agg(id::varchar,',') as ids\n" +
                        "FROM (\n" +
                        "         SELECT x1.id\n" +
                        "         FROM fina_jobs x1\n" +
                        "         WHERE x1.isdelete = FALSE\n" +
                        "           AND (x1.id = (SELECT x2.parentid FROM fina_jobs x2 WHERE x2.isdelete = FALSE AND x2.id = " + idList[i] + ")\n" +
                        "             OR x1.parentid in (SELECT x3.parentid FROM fina_jobs x3 WHERE x3.isdelete = FALSE AND x3.parentid <> 0 AND x3.id = " + idList[i] + ")\n" +
                        "             OR x1.id = (SELECT x4.id FROM fina_jobs x4 WHERE x4.isdelete = FALSE AND x4.id = " + idList[i] + ")\n" +
                        "             OR x1.parentid in (SELECT x5.id FROM fina_jobs x5 WHERE x5.isdelete = FALSE AND x5.id = " + idList[i] + "))\n" +
                        "         ORDER BY (CASE x1.jobtype WHEN 'D' THEN 0 ELSE 1 END) DESC\n" +
                        "     ) AS json;";

                Map idsMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(idsSql);
                ids += StrUtils.getMapVal(idsMap, "ids") + ",";
            }
            //正则表达式 移除字符串最后出现的逗号
            String oid = ids.replaceAll(",\\s*$", "");

            Map args = new HashMap();
            args.put("id", oid);
            args.put("userid", session.get("userid").toString());
            args.put("corpsql", " AND a.corpid = " + session.get("corpid").toString());
            args.put("orderby", "ORDER BY position(',' || x.id::text || ',' in ',' || array_to_string(Array ['" + oid + "'], ',') || ',')");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("payprofit", StrUtils.getMapVal(list.get(0), "payprofit"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 已作废列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "jobsdellist")
    public BaseView BusCommerceJobDelList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "base.commerce.jobsdellist";
            Map args = new HashMap();
            args.put("qry", json);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("jobsdellist", StrUtils.getMapVal(list.get(0), "jobsdellist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 工作单完成确认/取消
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "iscomplete")
    public BaseView BusCommerceJobIsComplete(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            String comVal = "JobsCommerceComplete";
            if (object.get("iscomplete").toString().equals("false")) {
                comVal = "JobsCommerceCancelComplete";
            }
            //检查是否有完成权限
            String viewSql = "SELECT (f_checkright('" + comVal + "', id) <> 0) as iscomplete FROM sys_user WHERE id = " + session.get("userid").toString() + "";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(viewSql);

            if (StrUtils.getMapVal(map, "iscomplete").equals("true")) {
                String querySql = "UPDATE fina_jobs SET iscomplete = " + object.get("iscomplete").toString() + "," +
                        " complete_user = '" + session.get("usercode").toString() + "'," +
                        " complete_time= NOW()," +
                        " updater = '" + session.get("usercode").toString() + "'," +
                        " updatetime = NOW() " +
                        " WHERE id = " + object.get("id").toString() + " AND isdelete = FALSE;";
                daoIbatisTemplate.updateWithUserDefineSql(querySql);
                view.setSuccess(true);
            } else {
                if (object.get("iscomplete").toString().equals("true")) {
                    view.setData("无确认工作单完成权限");
                } else {
                    view.setData("无解锁工作单完成权限");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 工作单审核确认/取消
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "ischeck")
    public BaseView BusCommerceJobIsCheck(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            String checkVal = "JobsCommerceCheck";
            if (object.get("ischeck").toString().equals("false")) {
                checkVal = "JobsCommerceCancelCheck";
            }

            //检查是否有完成权限
            String viewSql = "SELECT (f_checkright('" + checkVal + "', id) <> 0) as ischeck FROM sys_user WHERE id = " + session.get("userid").toString() + "";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(viewSql);
            if (StrUtils.getMapVal(map, "ischeck").equals("true")) {
                String querySql = "UPDATE fina_jobs SET ischeck = " + object.get("ischeck").toString() + "," +
                        " checkter = '" + session.get("usercode").toString() + "'," +
                        " checktime = NOW()," +
                        " updater = '" + session.get("usercode").toString() + "'," +
                        " updatetime = NOW() " +
                        "WHERE id = " + object.get("id").toString() + " AND isdelete = FALSE;";
                daoIbatisTemplate.updateWithUserDefineSql(querySql);
                view.setSuccess(true);
            } else {
                if (object.get("ischeck").toString().equals("true")) {
                    view.setData("无确认工作单审核权限");
                } else {
                    view.setData("无解锁工作单审核权限");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 工作单是否放货
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "isrelease")
    public BaseView BusCommerceJobIsRelease(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String querySql = "UPDATE fina_jobs SET isrelease = " + object.get("isrelease").toString()
                    + " WHERE id = " + object.get("id").toString() + " AND isdelete = FALSE;";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            daoIbatisTemplate.updateWithUserDefineSql(querySql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 更新订单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "update")
    public BaseView BusCommerceUpdate(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            JSONObject finajobs = JSONObject.parseObject(object.get("finajobs").toString());
            finajobs.put("updater", session.get("usercode").toString());
            object.put("finajobs", finajobs);

            String querySql = "";
            querySql = "SELECT f_commerce_update('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            if (!StrUtils.getMapVal(map, "json").isEmpty()) {
                reRet = StrUtils.getMapVal(map, "json");
                String querySql1 = "SELECT f_commerce_buspacklist_join('" + object.toString() + "') AS json;";
                daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);
            }
            view.setData(reRet);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 创建订单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "save")
    public BaseView BusCommerceSave(HttpServletRequest request) {
        BaseView view = new BaseView();

        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            JSONObject finajobs = JSONObject.parseObject(object.get("finajobs").toString());
            finajobs.put("inputer", session.get("username").toString());
            object.put("finajobs", finajobs);

            String querySql = "";
            querySql = "SELECT f_commerce_join('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            if (!StrUtils.getMapVal(map, "json").isEmpty()) {
                reRet = StrUtils.getMapVal(map, "json");
                finajobs.put("id", reRet);
                object.put("finajobs", finajobs);

                String querySql1 = "SELECT f_commerce_buspacklist_join('" + object.toString() + "') AS json;";
                daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);
            }
            view.setData(reRet);
            view.setSuccess(true);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 集拼主单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "setMaster")
    public BaseView BusCommerceSetMaster(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_master('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 复制订单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "copy")
    public BaseView BusCommerceCopy(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_copy('" + json + "','" + session.get("username").toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 恢复工作单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "recover")
    public BaseView BusCommerceRecover(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_recover('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 删除订单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "del")
    public BaseView BusCommerceDel(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_del('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 彻底删除
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "delCompletely")
    public BaseView BusCommerceDelCompletely(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_del_completely('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 批量刷新M单 -- 应收费用记录
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "refreshMblOrders")
    public BaseView BusCommerceRefreshMblOrders(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_refreshmblorders('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 批量同步M单H单etd-eta日期
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "refreshJobInfo")
    public BaseView BusCommerceRrefreshJobInfo(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_refreshjobinfo('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 批量确认 -- 工作单完成
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "completeHblOrders")
    public BaseView BusCommerceCompleteHblOrders(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String usercode = session.get("usercode").toString();

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_completehblorders('" + json + "','" + usercode + "','order') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 批量完成应收
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "completeHblAr")
    public BaseView BusCommerceCompleteHblAr(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String usercode = session.get("usercode").toString();

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_completehblorders('" + json + "','" + usercode + "','ar') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 批量完成应付
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "completeHblAp")
    public BaseView BusCommerceCompleteHblAp(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String usercode = session.get("usercode").toString();

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_completehblorders('" + json + "','" + usercode + "','ap') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");
            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }


    /**
     * 查询工作单节点 -- 去掉改功能 20221228
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "querynode")
    public BaseView queryBusCommerceNode(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            String querySql = "base.commerce.querynode";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            if (!list.isEmpty()) {
                view.add("commerceNode", StrUtils.getMapVal(list.get(0), "node"));
            }
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 更新工作单节点
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "updatenode")
    public BaseView updateBusCommerceNode(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_updatenode('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.setData(StrUtils.getMapVal(map, "json"));
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 清除工作单节点
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "clearnode")
    public BaseView clearBusCommerceNode(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_clearnode('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.setData(StrUtils.getMapVal(map, "json"));
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }


    /**
     * 刷新M单到单个H单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "refreshHbl")
    public BaseView refreshHbl(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_refresh_hbl('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.setData(StrUtils.getMapVal(map, "json"));
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 刷新M单到H单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "refreshMbl")
    public BaseView refreshMbl(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_refresh_mbl('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.setData(StrUtils.getMapVal(map, "json"));
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }


    /**
     * 獲取操作日志
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "querylog")
    public BaseView queryLog(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            String querySql = "base.commerce.querylog";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.setData(StrUtils.getMapVal(list.get(0), "log"));

        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }


    @Action(method = "querysysgriddeflist")
    public BaseView querysysgriddeflist(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql1 = "base.commerce.bus_commerce.sysgriddeflist";
            Map args = new HashMap();
            args.put("userid", session.get("userid").toString());
            args.put("gridid", "'" + jsonObject.get("gridid") + "'");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List list1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql1, args);
            view.add("sysgriddeflist", list1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Action(method = "savesysgriddef")
    public BaseView savesysgriddef(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            object.put("userid", session.get("userid").toString());
            String querySql = "";
            querySql = "SELECT f_commerce_savesysgriddef('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.setData(StrUtils.getMapVal(map, "json"));
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;

    }

    @Action(method = "savecolumndefs")
    public BaseView savecolumndefs(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            String thissysgriddefid = jsonObject.get("thissysgriddefid").toString();
            String thissysgriddefcolkey = jsonObject.get("thissysgriddefcolkey").toString();

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String updateSql = "update sys_griddef set colkey ='" + thissysgriddefcolkey + "' where id = " + thissysgriddefid;
            daoIbatisTemplate.updateWithUserDefineSql(updateSql);
            view.setSuccess(true);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;

    }

    @Action(method = "togglecolumndefs")
    public BaseView togglecolumndefs(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            String userid = session.get("userid").toString();
            String gridid = jsonObject.get("gridid").toString();
            String thissysgriddefid = jsonObject.get("thissysgriddefid").toString();

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String updateSql0 = "update sys_griddef set isselect ='否' where userid = " + userid + " AND gridid = '" + gridid + "'";
            daoIbatisTemplate.updateWithUserDefineSql(updateSql0);

            String updateSql = "update sys_griddef set isselect = '是' where id = " + thissysgriddefid;
            daoIbatisTemplate.updateWithUserDefineSql(updateSql);
            view.setSuccess(true);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 还原定制
     *
     * @param request
     * @return
     */
    @Action(method = "refreshColumnDef")
    public BaseView refreshColumnDef(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            String userid = session.get("userid").toString();
            String id = jsonObject.get("id").toString();
            String gridid = jsonObject.get("gridid").toString();

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String updateSql0 = "update sys_griddef set isselect ='否' where userid = " + userid + " AND gridid = '" + gridid + "' AND id = '" + id + "'";
            daoIbatisTemplate.updateWithUserDefineSql(updateSql0);
            view.setSuccess(true);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 装箱单导入excel
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "importtemplate")
    public BaseView ImportTemplate(HttpServletRequest request) throws IOException, FileUploadException {

        BaseView view = new BaseView();

        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        request.setCharacterEncoding("utf-8");
        InputStream stream = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = upload.parseRequest(request);
        List<FileItem> needFileItem = new LinkedList<FileItem>();
        String jobtype = "";
        String customerid = "";

        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                needFileItem.add(fileItem);
            } else {
                if ("jobtype".equals(fileItem.getFieldName())) {
                    jobtype = fileItem.getString("utf-8");
                }
                if ("customerid".equals(fileItem.getFieldName())) {
                    customerid = fileItem.getString("utf-8");
                }
            }
        }

        try {
            String querySql = "";
            stream = needFileItem.get(0).getInputStream();
            Workbook workbook = new XSSFWorkbook(stream);
            if (workbook != null) {
                int sheetCount = workbook.getNumberOfSheets();
                if (sheetCount > 0) {
                    List<BusCommerceResponse> list = new ArrayList();
                    List<BusPackListResponse> packListResponseList = new ArrayList();
                    BusCommerceResponse commerceResponse = new BusCommerceResponse();
                    commerceResponse.setCustomerid(customerid);

                    if (session.get("corpid").toString().equals("1122274")) {

                        // 青岛 - 文本内容
                        for (int i = 0; i < sheetCount; i++) {
                            if (i == 1) {
                                break;
                            }
                            Sheet sheet = workbook.getSheetAt(i);
                            //工作单信息
                            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                if (rowNum == 14) {
                                    break;
                                }
                                Row row = sheet.getRow(rowNum);
                                if (row == null || row.getFirstCellNum() < 0 || StringUtils.isBlank(row.getCell(0).toString())) {
                                    break;
                                }
                                //客户订单号
                                commerceResponse.setPo(getCellValue(row.getCell(0)).equals("客户订单号") ? getCellValue(row.getCell(1)) : commerceResponse.getPo());
                                //产品渠道
                                commerceResponse.setProductName(getCellValue(row.getCell(0)).equals("产品渠道") ? getCellValue(row.getCell(1)) : commerceResponse.getProductName());
                                //交税方式
                                commerceResponse.setPaytaxestype(getCellValue(row.getCell(0)).equals("交税方式") ? getCellValue(row.getCell(1)) : commerceResponse.getPaytaxestype());
                                //报关方式
                                commerceResponse.setDeclaretype(getCellValue(row.getCell(5)).equals("报关方式") ? getCellValue(row.getCell(7)) : commerceResponse.getDeclaretype());
                                //购买保险
                                commerceResponse.setFumigationrequired(getCellValue(row.getCell(5)).equals("购买保险") ? getCellValue(row.getCell(7)).equals("是") ? true : false : commerceResponse.isFumigationrequired());
                                //保险货值及币种
                                commerceResponse.setBeneficiary(getCellValue(row.getCell(5)).equals("保险货值及币种") ? getCellValue(row.getCell(7)) : commerceResponse.getBeneficiary());
                                //录入人
                                commerceResponse.setInputer(session.get("username").toString());
                                //工作单类型
                                commerceResponse.setJobtype(jobtype);
                                //商业/私人地址
                                commerceResponse.setShiptopostcode(getCellValue(row.getCell(0)).equals("收件人姓名") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getShiptopostcode());
                                commerceResponse.setCompanyname(getCellValue(row.getCell(0)).equals("收件人公司") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getCompanyname());
                                commerceResponse.setBookingtoaddress(getCellValue(row.getCell(0)).equals("收件人地址") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getBookingtoaddress());
                                commerceResponse.setBookingtocity(getCellValue(row.getCell(0)).equals("收件人城市") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getBookingtocity());
                                commerceResponse.setBookingtofax(getCellValue(row.getCell(0)).equals("收件人省份/州") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getBookingtofax());
                                commerceResponse.setPostcode(getCellValue(row.getCell(0)).equals("收件人邮编") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getPostcode());
                                commerceResponse.setDestcountrycode(getCellValue(row.getCell(0)).equals("收件人国家二字代码") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getDestcountrycode());
                                commerceResponse.setTel(getCellValue(row.getCell(0)).equals("收件人电话") ?
                                        getCellValue(row.getCell(1)).equals("") ? getCellValue(row.getCell(7)) : getCellValue(row.getCell(1)) : commerceResponse.getTel());
                            }
                            //装箱单内容
                            for (int rowNum = 15; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                Row row = sheet.getRow(rowNum);
                                if (row == null || row.getFirstCellNum() < 0 || row.getLastCellNum() < 10) {
                                    break;
                                }
                                packListResponseList.add(new BusPackListResponse(row, session.get("username").toString()));
                            }
                        }

                        querySql = "SELECT f_commerce_import_join_qd('" + JSON.toJSONString(commerceResponse) + "','" + JSONObject.toJSON(packListResponseList).toString() + "') AS json;";

                    } else {
                        // 途曦 - 文本内容
                        for (int i = 0; i < sheetCount; i++) {
                            if (i == 1) {
                                break;
                            }
                            Sheet sheet = workbook.getSheetAt(i);
                            for (int rowNum = 10; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                Row row = sheet.getRow(rowNum);
                                if (row == null || row.getFirstCellNum() < 0 || StringUtils.isBlank(row.getCell(0).toString())) {
                                    break;
                                }
                                list.add(new BusCommerceResponse(row, session.get("usercode").toString(), jobtype));
                            }
                        }

                        // 图片内容
//                    List<?> pictures = workbook.getAllPictures();
//                    if (pictures != null && !pictures.isEmpty()) {
//                        for (int i = 0; i < pictures.size(); i++) {
//                            PictureData picture = (PictureData) pictures.get(i);
//                            byte[] data = picture.getData();
//                            FileOutputStream out = new FileOutputStream("C:\\Users\\CIMC\\Downloads\\" + i + ".jpg");
//                            out.write(data);
//                            out.close();
//                        }
//                    }
                        querySql = "SELECT f_commerce_import_join('" + JSONObject.toJSON(list).toString() + "') AS json;";
                    }
                    DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                    Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                    String jobid = StrUtils.getMapVal(map, "json");

                    //解析完excel 并上传附件
                    String serverName = request.getContextPath().replace("/", "");
                    String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                    FileOperationUtil.newFolder(path);

                    path += File.separator + serverName;
                    FileOperationUtil.newFolder(path);

                    path += File.separator + "attachfile" + File.separator;
                    FileOperationUtil.newFolder(path);

                    //获取唯一ID
                    String attachidsql = "select getid() as attachid";
                    Map attachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachidsql);
                    String attachid = StrUtils.getMapVal(attachidmap, "attachid");

                    File shpFile = new File(path + File.separator + attachid + needFileItem.get(0).getName());
                    shpFile.createNewFile();

                    copyFile(needFileItem.get(0).getInputStream(), new FileOutputStream(shpFile));

                    //上传到箱单发票附件
                    String querySql2 = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer, inputtime, roleid)\n" +
                            "VALUES (" + attachid + ", " + jobid + ",'" + needFileItem.get(0).getName() + "','" +
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" + "', '" + needFileItem.get(0).getSize() +
                            "','" + session.get("username").toString() + "', NOW(), 6594792199);";
                    daoIbatisTemplate.updateWithUserDefineSql(querySql2);


                    view.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            stream.close();
        }

        return view;
    }

    /**
     * 导出模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @Action(method = "exporttemplate")
    public void ExportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String name = request.getParameter("name");
        File file = new File(AppUtils.getImportTemplateFilePath());
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getAbsolutePath().endsWith("commerce")) {
                if (listFiles[i].isDirectory()) {
                    File[] files = listFiles[i].listFiles();
                    for (int j = 0; j < files.length; j++) {
                        if (files[j].getAbsolutePath().endsWith(name)) {

                            try {
                                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(files[j]));
                                byte[] buffer = new byte[bis.available()];
                                bis.read(buffer);
                                bis.close();
                                //输出流导出文件
                                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                                //设置响应头
                                response.setCharacterEncoding("utf-8");
                                //设置导出类型
                                response.setContentType("application/vnd.ms-excel");
                                response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(files[j].getName(), "utf8"));
                                bos.write(buffer);
                                bos.flush();
                                bos.close();
                                break;
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建订单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "saveRemark")
    public BaseView BusCommerceSaveRemark(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            String querySql = "";

            if (object.get("type").toString().equals("D")) {
                querySql = "UPDATE bus_commerce SET remark = '" + object.get("remark").toString() + "' WHERE " +
                        "jobid = " + object.get("id").toString() + " AND isdelete = FALSE;";
            } else if (object.get("type").toString().equals("S")) {
                querySql = "UPDATE bus_shipping SET remark1 = '" + object.get("remark").toString() + "' WHERE " +
                        "jobid = " + object.get("id").toString() + " AND isdelete = FALSE;";
            } else if (object.get("type").toString().equals("A")) {
                querySql = "UPDATE bus_air SET remarks = '" + object.get("remark").toString() + "' WHERE " +
                        "jobid = " + object.get("id").toString() + " AND isdelete = FALSE;";
            }


            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
        } catch (Exception e) {
            view.setData(null);
        }
        return view;
    }

    public String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }
        String valueStr = "";
        switch (cell.getCellType()) {
            // 读取到的是公式
            case Cell.CELL_TYPE_FORMULA:
                try {
                    String tempStr1 = String.valueOf(cell.getNumericCellValue());
                    if (tempStr1.indexOf(".") > 0) {
                        valueStr = tempStr1.replace(".0", "");
                    } else {
                        valueStr = tempStr1;
                    }
                } catch (IllegalStateException e) {
                    String tempStr2 = String.valueOf(cell.getRichStringCellValue());
                    if (tempStr2.indexOf(".") > 0) {
                        valueStr = tempStr2.replace(".0", "");
                    } else {
                        valueStr = tempStr2;
                    }
                }
                break;
            case Cell.CELL_TYPE_NUMERIC:
                String tempStr3 = cell.toString();
                if (tempStr3.indexOf(".") > 0) {
                    valueStr = tempStr3.replace(".0", "");
                } else {
                    valueStr = tempStr3;
                }
                break;
            case Cell.CELL_TYPE_STRING:
                valueStr = cell.toString();
                break;
        }
        return valueStr;
    }

    /**
     * 复制文件
     *
     * @param in  原文件流输入流
     * @param out 目标文件输出流
     */
    public static void copyFile(InputStream in, OutputStream out) {
        int len;
        byte[] b = new byte[1024];
        try {
            while ((len = in.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
                out.flush();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查勾选多个工作单的客户是否一致
     *
     * @param ids
     * @return
     */
    public boolean custemerEqually(String[] ids) {
        String sql = "SELECT DISTINCT customerid FROM fina_jobs WHERE FALSE ";
        for (String id : ids) {
            sql += "\n OR id = " + id;
        }
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        List<Map> m = daoIbatisTemplate.queryWithUserDefineSql(sql);
        if (m.size() > 1) {
            return true;
        }
        return false;
    }

    /**
     * 放货申请
     */
    @Action(method = "releaseApplication")
    public BaseView releaseApplication(HttpServletRequest request) {

        BaseView view = new BaseView();

        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String[] ids = json.split(",");
            String querySql = "";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map m1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT count(1) AS c from (SELECT DISTINCT saleid FROM fina_jobs" +
                    " WHERE isdelete = FALSE AND id IN(" + StrUtils.array2List(ids) + ") ) t");

            boolean custemerEqually = this.custemerEqually(ids);
            if (custemerEqually) {
                view.setData("选择多单情况下必须是同一客户");
                return view;
            }
            if (!"1".equals(String.valueOf(m1.get("c")))) {
                view.setData("选择多单情况下必须是同一业务");
                return view;
            }

            Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COUNT(1) FROM fina_jobs" +
                    " WHERE isdelete = FALSE AND iscomplete_ar = TRUE AND iscomplete_ap= TRUE AND id in (" + StrUtils.array2List(ids) + ");");
            if (Integer.valueOf(String.valueOf(m.get("count"))) == 0) {
                view.setData("费用应收应付未完成");
                return view;
            }

            String url = "";
            String winId = "_edit_releasebillapply";
            if ("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))) {
                url = "/scp/bpm/apply/bpmnewreleasebillapply.xhtml?jobid=" + StrUtils.array2List(ids);
            } else {
                url = "../../../pages/ff/apply/bpmnewreleasebillapply.xhtml?jobid=" + StrUtils.array2List(ids);
            }
            view.setData(url);
            view.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
