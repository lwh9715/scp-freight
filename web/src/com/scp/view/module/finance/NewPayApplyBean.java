package com.scp.view.module.finance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
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

@WebServlet("/payapply")
@ManagedBean(name = "pages.module.finance.newpayapplyBean")
public class NewPayApplyBean extends BaseServlet {

    /**
     * 获取付款列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "list")
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
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            //默认显示1个月内的数据--打开高级查找后查恢复所有数据
            if (object.containsKey("islimit") && object.get("islimit") != null && !StrUtils.isNull(object.get("islimit").toString())) {
                if (object.get("islimit").toString() == "true") {
                    sql += " AND t.singtime > CURRENT_DATE - INTERVAL '1 months'";
                }
            }
            //供应商id
            if (object.containsKey("customerid") && object.get("customerid") != null && !StrUtils.isNull(object.get("customerid").toString())) {
                sql += " AND t.customerid = " + object.get("customerid").toString();
            }
            //付款单号
            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                String replaceAll = object.get("nos").toString().replaceAll(" ", "','");
                sql += " AND (nos ilike '%" + object.get("nos").toString() + "%' OR nos in ('" + replaceAll + "'))";
            }
            //收付款单号
            if (object.containsKey("rpnos") && object.get("rpnos") != null && !StrUtils.isNull(object.get("rpnos").toString())) {
                String replaceAll = object.get("rpnos").toString().replaceAll(" ", "','");
                sql += " AND (rpnos ilike '%" + object.get("rpnos").toString() + "%' OR rpnos in ('" + replaceAll + "'))";
            }
            //日期
            if (object.containsKey("singtime") && object.get("singtime") != null && !StrUtils.isNull(object.get("singtime").toString())) {
                JSONArray array = JSONArray.parseArray(object.get("singtime").toString());
                sql += " AND t.singtime between '" + array.get(0) + "' and '" + array.get(1) + "'";
            }

            String corpFilter = "\n  AND EXISTS (SELECT 1 FROM sys_user_corplink x WHERE x.corpid = t.corpid AND x.ischoose = TRUE AND x.userid = " + session.get("userid").toString() + ") " +
                    "\nAND ( EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = t.customerid AND xx.userid = "
                    + session.get("userid").toString()
                    + ") OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + ")" +
                    "OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_air y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + ")" +
                    "OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_train y WHERE x.linkid = y.id AND y.jobid = d.jobid AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + ")" +
                    "OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = d.jobid  AND x.linktype = 'J' AND x.userid =" + session.get("userid").toString() + ")" +
                    "OR EXISTS (SELECT 1 FROM sys_userinrole x,sys_user u WHERE x.roleid = 3006994888 AND x.userid = " + session.get("userid").toString()
                    + " AND x.userid = u.id AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = d.jobid AND " +
                    "u.corpid = ANY(SELECT job.corpid UNION SELECT job.corpidop UNION SELECT job.corpidop2  " +
                    "UNION (SELECT corpid from fina_corp WHERE isdelete = FALSE AND jobid = job.id)) AND job.isdelete = FALSE)) " +
                    "OR EXISTS"
                    + "\n(SELECT 1 FROM sys_custlib x , sys_custlib_user y  WHERE y.custlibid = x.id AND y.userid = " + session.get("userid").toString()
                    + "\n AND x.libtype = 'S' AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND " +
                    "EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = d.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //关联的业务员的单，都能看到
                    + ") OR EXISTS (SELECT 1 FROM sys_custlib x , sys_custlib_role y WHERE y.custlibid = x.id "
                    + "\n AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n AND x.libtype = 'S'  "
                    + "\n AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job " +
                    "WHERE job.id = d.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //组关联业务员的单，都能看到
                    + "))";

            String querySql = "pages.module.finance.newpayapply";
            Map args = new HashMap();
            args.put("qry", sql);
            args.put("filter", corpFilter);

            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("list", StrUtils.getMapVal(list.get(0), "list"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Action(method = "delPayApply")
    public BaseView del(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            String usercode = session.get("usercode").toString();
            String sql = "UPDATE fina_rpreq SET isdelete = TRUE,updater = '" + usercode + "',updatetime = NOW() WHERE id = " + object.get("id").toString();
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            daoIbatisTemplate.updateWithUserDefineSql(sql);
            view.setSuccess(true);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setSuccess(false);
            view.add("tip", tip);
        }
        return view;
    }
}