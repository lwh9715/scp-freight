package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.finance.JobsMgrService;
import com.scp.util.AppUtils;
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

@WebServlet("/merge")
@ManagedBean(name = "pages.module.commerce.busMergeBean")
public class BusMergeBean extends BaseServlet {

    public JobsMgrService jobsMgrService() {
        return (JobsMgrService) AppUtils.getBeanFromSpringIoc("jobsMgrService");
    }

    public static DaoIbatisTemplate daoIbatisTemplatesta = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

    /**
     * 查詢合并数据MBL-HBL-未配载
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "list")
    public BaseView queryMergeData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            JSONObject object = getJsonObject(request);
            String keyword = "";
            String orderby = "ORDER BY fj.ldtype2 ASC, fj.inputtime DESC";
            String querySql = "";

            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n	OR (fj.inputer ='" + session.get("usercode").toString() + "')"
                    + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
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

            if (object.get("methodFlag").toString().equals("mbl")) {

                querySql = "base.commerce.marge_mbl_ship";

                if (object.containsKey("query") && !object.get("query").toString().equals("")) {
                    JSONObject qry = JSONObject.parseObject(object.get("query").toString());

                    if (qry.containsKey("jobtype")) {
                        if (!qry.get("nos").toString().isEmpty()) {
                            keyword += "AND fj.nos ILIKE" + "'%" + qry.get("nos").toString() + "%'";
                        }
                        if (qry.get("jobtype").toString().equals("A")) {
                            querySql = "base.commerce.marge_mbl_air";
                        }
                    }
                }
                Map args = new HashMap(10);
                args.put("qry", keyword);
                args.put("filter", filter);
                args.put("userid", session.get("userid").toString());
                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
                view.add("mbl", StrUtils.getMapVal(list.get(0), "json"));

            } else if (object.get("methodFlag").toString().equals("hbl")) {

                if (object.containsKey("id") && !object.get("id").toString().equals("")) {
                    keyword += "AND fj.parentid = " + object.get("id").toString();
                }

                querySql = "base.commerce.marge_hbl";
                Map args = new HashMap(10);
                args.put("qry", keyword);
                args.put("userid", session.get("userid").toString());
                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
                view.add("hbl", StrUtils.getMapVal(list.get(0), "json"));

            } else if (object.get("methodFlag").toString().equals("single")) {

                if (object.containsKey("ldtype2") && !object.get("ldtype2").toString().equals("")) {
                    keyword += " AND fj.ldtype2 " + this.getLdType(object.get("ldtype2").toString());
                }
                if (object.containsKey("nosstype") && !object.get("nosstype").toString().equals("")) {
                    if (object.get("nosstype").toString().equals("TSZ")) {
                        keyword += " AND fj.nos not ilike 'TXM%' AND fj.nos not ilike 'TAE%'";
                    } else if (object.get("nosstype").toString().equals("TXM")) {
                        keyword += " AND fj.nos ilike 'TXM%'";
                    } else if (object.get("nosstype").toString().equals("TAE")) {
                        keyword += " AND fj.nos ilike 'TAE%'";
                    }
                }
                if (object.containsKey("qry") && !object.get("qry").toString().equals("")) {
                    JSONObject qry = JSONObject.parseObject(object.get("qry").toString());
                    if (!qry.get("hblno").toString().equals("")) {
                        keyword += " AND (fj.nos ilike '%" + qry.get("hblno").toString() + "%'";
                        String replaceAll = qry.get("hblno").toString().replaceAll(" ", "','");
                        keyword += " OR fj.nos in ('" + replaceAll + "'))";
                        orderby = "ORDER BY position(',' || fj.nos ::text || ',' in ',' || array_to_string(Array ['" + replaceAll + "'], ',') || ',')";
                    }
                }
                querySql = "base.commerce.marge_single";
                Map args = new HashMap(10);
                args.put("qry", keyword);
                args.put("filter", filter);
                args.put("orderby", orderby);
                args.put("userid", session.get("userid").toString());
                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
                view.add("single", StrUtils.getMapVal(list.get(0), "json"));
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        return view;
    }

    /**
     * 添加配载数据到M单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "submit")
    public BaseView submitMergeData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            object.put("corpid", session.get("corpid").toString());
            object.put("userid", session.get("userid").toString());
            object.put("username", session.get("username").toString());

            String querySql = "";
            querySql = "SELECT f_commerce_merge_submit('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");

            view.setData(reRet);
            view.setSuccess(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 从M单去除配载数据
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "mergeNewJob")
    public BaseView mergeNewJob(HttpServletRequest request) {
        BaseView view = new BaseView();

        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            //检查是否有新增子单权限
            String viewSql = "SELECT (f_checkright('mergeadd', id) <> 0) as ismergeadd FROM sys_user WHERE id = " + session.get("userid").toString() + "";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(viewSql);
            if (StrUtils.getMapVal(m, "ismergeadd").equals("true")) {
                String querySql = "";
                querySql = "SELECT f_commerce_merge_nos('" + JSONObject.toJSON(json) + "') AS json;";
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                String reRet = StrUtils.getMapVal(map, "json");
                view.setData(reRet);
                view.setSuccess(true);
            } else {
                view.setData("无添加子单权限");
                view.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 同步M单ETD和ETA到H单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "syncEtdDate")
    public BaseView syncEtdDate(HttpServletRequest request) {
        BaseView view = new BaseView();

        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_sync_date('" + JSONObject.toJSON(json) + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = StrUtils.getMapVal(map, "json");
            view.setData(reRet);
            view.setSuccess(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 添加配载数据到M单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "save")
    public BaseView saveMergeData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_merge_save('" + JSONObject.toJSON(json) + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");

            view.setData(reRet);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 添加配载数据到M单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "savelist")
    public BaseView saveMergeList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "";
            querySql = "SELECT f_commerce_merge_save_list('" + JSONObject.toJSON(json) + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");

            view.setData(reRet);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 从M单去除配载数据
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "remove")
    public BaseView removeMergeData(HttpServletRequest request) {
        BaseView view = new BaseView();

        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            //检查是否有新增子单权限
            String viewSql = "SELECT (f_checkright('mergedel', id) <> 0) as ismergedel FROM sys_user WHERE id = " + session.get("userid").toString() + "";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(viewSql);
            if (StrUtils.getMapVal(m, "ismergedel").equals("true")) {
                String querySql = "";
                querySql = "SELECT f_commerce_merge_remove('" + JSONObject.toJSON(json) + "') AS json;";
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                String reRet = "";
                reRet = StrUtils.getMapVal(map, "json");
                view.setData(reRet);
                view.setSuccess(true);
            } else {
                view.setData("无移除子单权限");
                view.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 从M单去除配载数据
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "removelist")
    public BaseView removeMergeList(HttpServletRequest request) {
        BaseView view = new BaseView();

        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            //检查是否有新增子单权限
            String viewSql = "SELECT (f_checkright('mergedel', id) <> 0) as ismergedel FROM sys_user WHERE id = " + session.get("userid").toString() + "";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(viewSql);
            if (StrUtils.getMapVal(m, "ismergedel").equals("true")) {
                String querySql = "";
                querySql = "SELECT f_commerce_merge_remove_list('" + JSONObject.toJSON(json) + "') AS json;";
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                String reRet = "";
                reRet = StrUtils.getMapVal(map, "json");
                view.setData(reRet);
                view.setSuccess(true);
            } else {
                view.setData("无移除子单权限");
                view.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 解析前端传获取值
     *
     * @param request
     * @return
     * @throws IOException
     */
    private JSONObject getJsonObject(HttpServletRequest request) throws IOException {
        String json;
        InputStream is = request.getInputStream();
        json = IOUtils.toString(is, "UTF-8");
        json = StrUtils.getSqlFormat(json);
        return JSONObject.parseObject(json);
    }

    /**
     * 电商类型转换
     *
     * @return
     * @throws IOException
     */
    private String getLdType(String key) {
        String result = "";
        if (key.equals("S")) {
            result = "IN ('H','X','Z')";
        } else if (key.equals("A")) {
            result = "IN ('K','X')";
        } else if (key.equals("L")) {
            result = "IN ('T','X')";
        }
        return result;
    }
}
