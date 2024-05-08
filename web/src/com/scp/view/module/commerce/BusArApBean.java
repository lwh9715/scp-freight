package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import org.apache.commons.io.IOUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CIMC
 */

@WebServlet("/arap")
@ManagedBean(name = "pages.module.commerce.BusArApBean")
public class BusArApBean extends BaseServlet {

    /**
     * 获取费用列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "araplist")
    public BaseView busCommerceArApList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            String sql = "";
            String sql2 = "";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    sql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND ((fj.nos ilike 'TSZ%' AND fj.deptid <> 1565607982274) OR (fj.deptid = 516230252274 AND fj.deptop <> 1565607982274) OR (fj.deptop = 516230252274 AND fj.deptid <> 1565607982274) OR (fj.nos ilike 'TSZ%' AND fj.deptid = 1565607982274 AND fj.submitime >= '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    sql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%' AND fj.nos not ilike 'TBZ%' AND (fj.nos ilike 'TAE%' OR fj.deptop = 516228802274))";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    sql += " AND (fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%' AND ((fj.nos ilike 'TXM%' AND fj.deptid <> 516230252274) OR (fj.nos ilike 'TBZ%' AND fj.deptid <> 516230252274) OR ((fj.nos ilike 'TSZ%' OR fj.nos not ilike 'TSZ%') AND fj.deptid = 1565607982274 AND fj.deptop <> 516230252274 AND fj.submitime < '2024-02-26') OR ((fj.nos ilike 'TSZ%' OR fj.nos not ilike 'TSZ%') AND fj.deptop = 1565607982274 AND fj.deptid <> 516230252274 AND fj.submitime < '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    sql += " AND fj.nos ilike 'TDA%'";
                } else if (object.get("nostype").toString().equals("TOC")) {
                    sql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND (fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%' OR fj.deptid = 1565607982274 OR fj.deptop = 1565607982274 OR fj.nos ilike 'TSZ%' OR fj.deptid = 516230252274 OR fj.deptop = 516230252274))";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " limit 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            sql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TXM%')";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            sql += " AND fj.nos ilike 'TAE%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            sql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%')";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            sql += " AND fj.nos ilike 'TDA%'";
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
            //高级查找
            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                String replaceAll = object.get("nos").toString().replaceAll(" ", "','");
                sql2 += " AND (nos ilike '%" + object.get("nos").toString() + "%' OR nos in ('" + replaceAll + "') " +
                        "OR ponum ilike '%" + object.get("nos").toString() + "%' OR ponum in ('" + replaceAll + "'))";
            }
            if (object.containsKey("inputtime") && object.get("inputtime") != null && !StrUtils.isNull(object.get("inputtime").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("inputtime").toString());
                sql += " AND z.inputtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            if (object.containsKey("arapdate") && object.get("arapdate") != null && !StrUtils.isNull(object.get("arapdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("arapdate").toString());
                sql += " AND z.arapdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //业务日期
            if (object.containsKey("submitime") && object.get("submitime") != null && !StrUtils.isNull(object.get("submitime").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("submitime").toString());
                sql += " AND fj.submitime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                sql += " AND fj.jobdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //公司
            if (object.containsKey("customerabbr") && object.get("customerabbr") != null && !StrUtils.isNull(object.get("customerabbr").toString())) {
                sql += " AND fj.customerabbr ilike '%" + object.get("customerabbr").toString() + "%'";
            }
            //结算公司
            if (object.containsKey("settlementabbr") && object.get("settlementabbr") != null && !StrUtils.isNull(object.get("settlementabbr").toString())) {
                sql += " AND z.customerid IN (SELECT id FROM sys_corporation WHERE (code ILIKE '%" + object.get("settlementabbr").toString() + "%' " +
                        "OR namec ILIKE '%" + object.get("settlementabbr").toString() + "%'))";
            }
            if (object.containsKey("sales") && object.get("sales") != null && !StrUtils.isNull(object.get("sales").toString())) {
                sql += " AND fj.sales ilike '%" + object.get("sales").toString() + "%'";
            }
            //录入人
            if (object.containsKey("inputer") && object.get("inputer") != null && !StrUtils.isNull(object.get("inputer").toString())) {
                sql += " AND z.inputer ilike '%" + object.get("inputer").toString() + "%'";
            }
            //更新人
            if (object.containsKey("updater") && object.get("updater") != null && !StrUtils.isNull(object.get("updater").toString())) {
                sql += " AND z.updater ilike '%" + object.get("updater").toString() + "%'";
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
            if (object.containsKey("etd") && object.get("etd") != null && !StrUtils.isNull(object.get("etd").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("etd").toString());
                sql2 += " AND etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
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
                    sql += " AND fj.jobtype <> 'D' AND NOT EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = fj.id AND isdelete = FALSE)";
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
            //费用确认状态
            if (object.containsKey("iscomplete_check") && object.get("iscomplete_check") != null && !StrUtils.isNull(object.get("iscomplete_check").toString())) {
                if (object.get("iscomplete_check").toString() == "true") {
                    sql += " AND z.isconfirm = true";
                } else {
                    sql += " AND z.isconfirm = false";
                }
            }
            //核销状态
            if (object.containsKey("iswriteoff") && object.get("iswriteoff") != null && !StrUtils.isNull(object.get("iswriteoff").toString())) {
                if (object.get("iswriteoff").toString() == "true") {
                    sql += " AND z.amtstl2 = z.amount";
                } else {
                    sql += " AND z.amtstl2 <> z.amount";
                }
            }
            //H单虚拟成本
            if (object.containsKey("isvirtualcost") && object.get("isvirtualcost") != null && !StrUtils.isNull(object.get("isvirtualcost").toString())) {
                if (object.get("isvirtualcost").toString() == "true") {
                    sql += " AND z.customerid = 11540072274 AND EXISTS(SELECT 1 FROM fina_jobs x1 WHERE id = fj.id AND EXISTS(SELECT 1 FROM fina_jobs xxx WHERE xxx.id = x1.parentid AND xxx.jobtype <> 'D' AND xxx.isdelete = FALSE AND (xxx.deptid = 516230252274 OR xxx.deptop = 516230252274)))";
                }
            }
            //是否含期间
            if (object.containsKey("isfinancial") && object.get("isfinancial") != null && !StrUtils.isNull(object.get("isfinancial").toString())) {
                if (object.get("isfinancial").toString() == "true") {
                    sql += " AND z.vchdtlid is not null";
                } else {
                    sql += " AND z.vchdtlid is null";
                }
            }
            //费用相关sql
            if (object.containsKey("financialdate") && object.get("financialdate") != null && !StrUtils.isNull(object.get("financialdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("financialdate").toString());
                sql2 += " AND (SELECT x.year || '-' || x.period FROM fs_vch x WHERE x.isdelete = FALSE\n" +
                        " AND x.id = ANY (SELECT y.vchid FROM fs_vchdtl y WHERE y.isdelete = FALSE AND y.id = vchdtlid LIMIT 1)\n" +
                        " LIMIT 1) between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //增减费用
            if (object.containsKey("isamend") && object.get("isamend") != null && !StrUtils.isNull(object.get("isamend").toString())) {
                sql += " AND z.isamend = " + object.get("isamend").toString() + "";
            }
            //虚拟费用
            if (object.containsKey("isvirtual") && object.get("isvirtual") != null && !StrUtils.isNull(object.get("isvirtual").toString())) {
                if (object.get("isvirtual").toString().equals("true")) {
                    sql += " AND z.rptype = 'O'";
                } else {
                    sql += " AND z.rptype <> 'O'";
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
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "')" //录入人有权限
                    + "\n OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND fj.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n OR EXISTS (SELECT 1 FROM sys_custlib sc , sys_custlib_user y WHERE y.custlibid = sc.id  AND y.userid = " + session.get("userid").toString()
                    + "\n AND sc.libtype = 'S' AND sc.userid = fj.saleid )" //关联的业务员的单，都能看到
                    + "\n OR EXISTS (SELECT 1  FROM sys_custlib sc , sys_custlib_role y WHERE y.custlibid = sc.id  "
                    + "\n AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n AND sc.libtype = 'S' AND sc.userid = fj.saleid )" //组关联业务员的单，都能看到
                    //过滤工作单指派
                    + "\n OR EXISTS(SELECT 1 FROM sys_user_assign sua, bus_commerce y WHERE sua.linkid = y.id AND sua.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND sua.linktype = 'J' AND sua.userid =" + session.get("userid").toString() + "))";

            //途曦只能查看途曦的费用
            sql += " AND z.corpid = 11540072274";

            //费用统计只显示青岛和途曦的工作单
            sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274)))";

            String querySql = "base.commerce.araplist";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("filter", filter);
            args.put("qry2", sql2);
            args.put("corpid", session.get("corpid").toString());

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("araplist", StrUtils.getMapVal(list.get(0), "araplist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取账单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "billlist")
    public BaseView busCommerceBillList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            String sql = "";
            String sql2 = "";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            //途曦按照部分区分工作单
            if (session.get("corpid").toString().equals("11540072274")) {
                String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                    sql += " AND (x.nos ilike 'TSZ%' OR x.deptop = 516230252274 OR x.nos ilike 'TBZ%' OR x.deptop = 1565607982274)";
                } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                    sql += " AND x.nos ilike 'TAE%'";
                } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                    sql += " AND (x.nos ilike 'TSZ%' OR x.deptop = 516230252274 OR x.nos ilike 'TBZ%' OR x.deptop = 1565607982274)";
                } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                    sql += " AND x.nos ilike 'TDA%'";
                }
            }

            //高级查找
            if (object.containsKey("customerid") && object.get("customerid") != null && !StrUtils.isNull(object.get("customerid").toString())) {
                sql += " AND z.customerid = " + object.get("customerid").toString() + "";
            }
            //查看应收或应付
            if (object.containsKey("isap") && object.get("isap") != null && !StrUtils.isNull(object.get("isap").toString())) {
                if (object.get("isap").toString() == "true") {
                    sql += " AND z.araptype = 'AP'";
                } else {
                    sql += " AND z.araptype = 'AR'";
                }
            }
            //入仓时间/etd/订单日期/创建日期
            if (object.containsKey("datetype") && object.get("datetype") != null && !StrUtils.isNull(object.get("datetype").toString())) {
                if (object.get("datetype").toString().equals("wmsindate")) {
                    if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                        sql2 += " AND y.wmsindate between '" + array.get(0) + "' and '" + array.get(1) + "'";
                    }
                } else if (object.get("datetype").toString().equals("etd")) {
                    if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                        sql += " AND y.etd between '" + array.get(0) + "' and '" + array.get(1) + "'";
                    }
                } else if (object.get("datetype").toString().equals("jobdate")) {
                    if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                        sql += " AND x.jobdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
                    }
                } else if (object.get("datetype").toString().equals("arapdate")) {
                    if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                        sql += " AND z.arapdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
                    }
                } else if (object.get("datetype").toString().equals("confirmtime")) {
                    if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                        sql += " AND z.confirmtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                    }
                } else {
                    if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                        JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                        sql += " AND x.inputtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
                    }
                }
            }

            //结算地
            if (object.containsKey("corpStr") && object.get("corpStr") != null && !StrUtils.isNull(object.get("corpStr").toString())) {
                if (object.get("corpStr").toString().equals("TOC")) {
                    sql += " AND z.corpid = 11540072274";
                } else if (object.get("corpStr").toString().equals("GHK")) {
                    sql += " AND z.corpid = 157970752274";
                } else if (object.get("corpStr").toString().equals("GDB")) {
                    sql += " AND z.corpid = 399480672274";
                }
            }

            String filter = "\nAND ( x.saleid = " + session.get("userid").toString()
                    + "\n	OR (x.inputer ='" + session.get("usercode").toString() + "')"
                    + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND x.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT x.corpidop UNION SELECT x.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = x.id AND c.isdelete =FALSE)))"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib sc , sys_custlib_user y  "
                    + "\n				WHERE y.custlibid = sc.id  "
                    + "\n					AND y.userid = " + session.get("userid").toString()
                    + "\n					AND sc.libtype = 'S'  "
                    + "\n					AND sc.userid = x.saleid " //关联的业务员的单，都能看到
                    + ")"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib sc , sys_custlib_role y  "
                    + "\n				WHERE y.custlibid = sc.id  "
                    + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n					AND sc.libtype = 'S'  "
                    + "\n					AND sc.userid = x.saleid " //组关联业务员的单，都能看到
                    + ")"
                    //过滤工作单指派
                    + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign sua, bus_commerce y WHERE sua.linkid = y.id AND sua.isdelete = FALSE " +
                    "AND y.jobid = x.id AND sua.linktype = 'J' AND sua.userid =" + session.get("userid").toString() + ")"
                    + "\n)";

            //费用统计只显示青岛和途曦的工作单
            sql += " AND (x.corpid = 11540072274 OR x.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = x.id AND corpid = 11540072274)))";

            String querySql = "base.commerce.billlist";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("filter", filter);
            args.put("commerceqry", sql2);
            args.put("corpid", session.get("corpid").toString());

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("billlist", StrUtils.getMapVal(list.get(0), "billlist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 设置为虚拟费用
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "changeVirtual")
    public BaseView changeVirtual(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            //检查是否是虚拟费用
            String viewSql = "SELECT EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND rptype <> 'O' AND customerid = corpid AND id = " + json + ") AS isvirtual";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(viewSql);

            if (StrUtils.getMapVal(map, "isvirtual").equals("true")) {
                String querySql = "UPDATE fina_arap SET rptype = 'O' WHERE isdelete = FALSE AND customerid = corpid AND id = " + json + ";";
                daoIbatisTemplate.updateWithUserDefineSql(querySql);
                view.setSuccess(true);
            } else {
                view.setData("不符合费用纠正，请重新勾选");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取业绩统计
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "saleProList")
    public BaseView BusCommerceSaleProList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String sql = "";
            String singsql = "";
            String etdsql = "";
            String hsql = "";
            String msql = "";
            String nossql = "";
            String corpsql = "";

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
            //业务员
            if (object.containsKey("saleid") && object.get("saleid") != null && !StrUtils.isNull(object.get("saleid").toString())) {
                msql += " AND fj.saleid = " + object.get("saleid").toString() + "";
                hsql += " AND (fj.saleid = " + object.get("saleid").toString() + " OR bc.saleidop = " + object.get("saleid").toString() + ")";
            }
            //日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                nossql += " AND fj.submitime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "')" //录入人有权限
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

            //费用统计只显示途曦的工作单
            sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274 AND isdelete = FALSE)))";

            corpsql += " AND a.corpid = " + session.get("corpid").toString();

            String querySql = "base.commerce.saleprolist";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("hqry", hsql);
            args.put("mqry", msql);
            args.put("etdsql", etdsql);
            args.put("singsql", singsql);
            args.put("filter", filter);
            args.put("nostype", nossql);
            args.put("corpsql", corpsql);
            args.put("corpid", session.get("corpid").toString());

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("saleprolist", StrUtils.getMapVal(list.get(0), "saleprolist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 业务明细统计
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "salechild")
    public BaseView BusCommerceSaleChild(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "";
            String nossql = "";
            String singsql = "";
            String etdsql = "";
            String corpsql = " AND a.corpid = " + session.get("corpid").toString();

            //途曦按照部分区分工作单
            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    nossql += " AND (x.nos not ilike 'TAE%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TXM%' AND ((x.nos ilike 'TSZ%' AND x.deptid <> 1565607982274) OR (x.deptid = 516230252274 AND x.deptop <> 1565607982274) OR (x.deptop = 516230252274 AND x.deptid <> 1565607982274) OR (x.nos ilike 'TSZ%' AND x.deptid = 1565607982274 AND x.submitime >= '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    nossql += " AND (x.nos not ilike 'TSZ%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TXM%' AND x.nos not ilike 'TBZ%' AND (x.nos ilike 'TAE%' OR x.deptop = 516228802274))";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    nossql += " AND (x.nos not ilike 'TDA%' AND x.nos not ilike 'TAE%' AND ((x.nos ilike 'TXM%' AND x.deptid <> 516230252274) OR (x.nos ilike 'TBZ%' AND x.deptid <> 516230252274) OR ((x.nos ilike 'TSZ%' OR x.nos not ilike 'TSZ%') AND x.deptid = 1565607982274 AND x.deptop <> 516230252274 AND x.submitime < '2024-02-26') OR ((x.nos ilike 'TSZ%' OR x.nos not ilike 'TSZ%') AND x.deptop = 1565607982274 AND x.deptid <> 516230252274 AND x.submitime < '2024-02-26')))";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    nossql += " AND x.nos ilike 'TDA%'";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            nossql += " AND (x.nos not ilike 'TAE%' AND x.nos not ilike 'TDA%' AND (x.nos ilike 'TXM%' OR x.nos ilike 'TBZ%' OR x.deptop = 1565607982274 OR x.nos ilike 'TSZ%' OR x.deptop = 516230252274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            nossql += " AND (x.nos not ilike 'TSZ%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TXM%' AND x.nos not ilike 'TBZ%' AND (x.nos ilike 'TAE%' OR x.deptop = 516228802274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            nossql += " AND (x.nos not ilike 'TSZ%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TAE%' AND (x.nos ilike 'TXM%' OR x.nos ilike 'TBZ%' OR x.deptop = 1565607982274))";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            nossql += " AND x.nos ilike 'TDA%'";
                        }
                    }
                }
            }
            //工作单日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                String[] jobdates = object.get("jobdate").toString().split(",");
                sql += " AND x.submitime between '" + jobdates[0] + "' and '" + jobdates[1] + "'";
            }
            //费用统计只显示途曦的工作单
            sql += " AND (x.corpid = 11540072274 OR x.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = x.id AND corpid = 11540072274 AND isdelete = FALSE)))";

            String querySql = "base.commerce.salechild";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("saleid", object.get("saleid").toString());
            args.put("nossql", nossql);
            args.put("corpsql", corpsql);
            args.put("etdsql", etdsql);
            args.put("singsql", singsql);
            args.put("corpid", session.get("corpid").toString());
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("salechild", StrUtils.getMapVal(list.get(0), "salechild"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取业务量统计
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "businessvol")
    public BaseView BusCommerceBusinessVol(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String sql = "";
            String hsql = "";
            String msql = "";
            String desc = " ORDER BY nostotal DESC nulls last";
            String nossql = "";
            String corpsql = "";

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    nossql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TBZ%')";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TBZ%')";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    nossql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%' AND fj.nos not ilike 'TAE%')";
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
                            nossql += " AND fj.nos ilike 'TBZ%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            nossql += " AND fj.nos ilike 'TDA%'";
                        }
                    }
                }
            }

            if (object.containsKey("model") || object.get("model") != null) {
                if (object.get("model").toString().equals("P")) {
                    desc = " ORDER BY nostotal DESC nulls last";
                } else if (object.get("model").toString().equals("G")) {
                    desc = " ORDER BY chargeweight DESC nulls last";
                } else if (object.get("model").toString().equals("C")) {
                    desc = " ORDER BY cbmtotal DESC nulls last";
                } else if (object.get("model").toString().equals("T")) {
                    desc = " ORDER BY cnttotal DESC nulls last";
                }
            }

            //工作单日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("jobdate").toString());
                sql += " AND fj.jobdate between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }
            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n OR (fj.inputer ='" + session.get("usercode").toString() + "')" //录入人有权限
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

            //费用统计只显示途曦的工作单
            sql += " AND fj.corpid = 11540072274";

            corpsql += " AND a.corpid = " + session.get("corpid").toString();

            String querySql = "base.commerce.businessvol";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("hqry", hsql);
            args.put("mqry", msql);
            args.put("filter", filter);
            args.put("desc", desc);
            args.put("nostype", nossql);
            args.put("corpsql", corpsql);
            args.put("corpid", session.get("corpid").toString());

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("businessvol", StrUtils.getMapVal(list.get(0), "businessvol"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 业务量明细统计
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "customerchild")
    public BaseView BusCommercecustomerChild(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "";
            String nossql = "";
            String corpsql = " AND a.corpid = " + session.get("corpid").toString();

            //途曦按照部分区分工作单
            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    nossql += " AND (x.nos not ilike 'TAE%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TBZ%')";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    nossql += " AND (x.nos not ilike 'TSZ%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TBZ%')";
                } else if (object.get("nostype").toString().equals("TBZ")) {
                    nossql += " AND (x.nos not ilike 'TSZ%' AND x.nos not ilike 'TDA%' AND x.nos not ilike 'TAE%')";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    nossql += " AND x.nos ilike 'TDA%'";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            nossql += " AND (x.nos not ilike 'TAE%' AND x.nos not ilike 'TDA%')";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            nossql += " AND x.nos ilike 'TAE%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            nossql += " AND x.nos ilike 'TBZ%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            nossql += " AND x.nos ilike 'TDA%'";
                        }
                    }
                }
            }
            //工作单日期
            if (object.containsKey("jobdate") && object.get("jobdate") != null && !StrUtils.isNull(object.get("jobdate").toString())) {
                String[] jobdates = object.get("jobdate").toString().split(",");
                sql += " AND x.jobdate between '" + jobdates[0] + "' and '" + jobdates[1] + "'";
            }
            //费用统计只显示途曦的工作单
            sql += " AND x.corpid = 11540072274";

            String querySql = "base.commerce.customerchild";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("customerid", object.get("customerid").toString());
            args.put("nossql", nossql);
            args.put("corpsql", corpsql);
            args.put("corpid", session.get("corpid").toString());
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("customerchild", StrUtils.getMapVal(list.get(0), "customerchild"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 批量费用确认
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "ArApConfirm")
    public BaseView ArApConfirm(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String usercode = session.get("usercode").toString();

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_arap_confirm('" + json + "','" + usercode + "','confirm') AS json;";
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
     * 批量费用取消
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "ArApCancel")
    public BaseView ArApCancel(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String usercode = session.get("usercode").toString();

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_arap_confirm('" + json + "','" + usercode + "','cancel') AS json;";
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

}