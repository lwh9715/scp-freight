package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSONArray;
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
import org.springframework.util.StringUtils;

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

@WebServlet("/goodstrack")
@ManagedBean(name = "pages.module.commerce.busGoodsTrackBean")
public class BusGoodsTrackBean extends BaseServlet {

    public JobsMgrService jobsMgrService() {
        return (JobsMgrService) AppUtils.getBeanFromSpringIoc("jobsMgrService");
    }

    /**
     * 查詢轨迹列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "list")
    public BaseView BusGoodsTrackList(HttpServletRequest request) {

        BaseView view = new BaseView();
        try {
            String json = "";
            String keyword = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            if (!StringUtils.isEmpty(json)) {
                keyword += "AND (bc.nos ILIKE" + "'%" + json + "%' OR bc.mblnum ILIKE" + "'%" + json + "%'" +
                        "OR bc.sonum ILIKE" + "'%" + json + "%'OR bc.ponum ILIKE" + "'%" + json + "%' OR bc.sayctns ILIKE" + "'%" + json + "%')";
            }
            String querySql = "base.commerce.goodstracklist";
            Map args = new HashMap();
            args.put("qry", keyword);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("goodstracklist", StrUtils.getMapVal(list.get(0), "json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 查询工作单轨迹
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "queryGoodsTrack")
    public BaseView queryGoodsTrack(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            String querySql = "";
            if (jsonObject.get("jobtype").equals("S")) {
                querySql = "base.commerce.goodstrack_ship";
            } else {
                querySql = "base.commerce.goodstrack";
            }
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            if (!list.isEmpty()) {
                view.add("goodstrack", StrUtils.getMapVal(list.get(0), "goodstrack"));
            }
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 更新工作单轨迹
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "updateGoodsTrack")
    public BaseView updateGoodsTrack(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_goodstrack_update('" + json + "') AS json;";
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
     * 批量删除工作单轨迹
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "delGoodsTracks")
    public BaseView delGoodsTracks(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_goodstrack_dels('" + json + "') AS json;";
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
     * 删除工作单轨迹
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "delGoodsTrack")
    public BaseView delGoodsTrack(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_goodstrack_del('" + json + "') AS json;";
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
     * 获取轨迹列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "locuslist")
    public BaseView busCommerceLocusList(HttpServletRequest request) {
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
                    sql += " AND (fj.nos not ilike 'TAE%' AND fj.nos not ilike 'TDA%')";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    sql += " AND (fj.nos not ilike 'TSZ%' AND fj.nos not ilike 'TDA%')";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    sql += " AND fj.nos ilike 'TDA%'";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            sql += " AND fj.nos ilike 'TSZ%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            sql += " AND fj.nos ilike 'TAE%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            sql += " AND fj.nos ilike 'TDA%'";
                        }
                    }
                }
            }

            //默认显示3个月内的数据--打开高级查找后查恢复所有数据
            if (object.containsKey("islimit") && object.get("islimit") != null && !StrUtils.isNull(object.get("islimit").toString())) {
                if (object.get("islimit").toString() == "true") {
                    sql += " AND fj.jobdate > CURRENT_DATE - INTERVAL '3 months'";
                }
            }

            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                sql += " AND (fj.nos ilike '%" + object.get("nos").toString() + "%'";
                String replaceAll = object.get("nos").toString().replaceAll(" ", "','");
                sql += " OR fj.nos in ('" + replaceAll + "') )";
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

            //费用统计只显示青岛和途曦的工作单
            sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                    "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274)))";

            String querySql = "base.commerce.locuslist";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("filter", filter);

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("locuslist", StrUtils.getMapVal(list.get(0), "locuslist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
