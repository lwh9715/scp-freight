package com.scp.view.module.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
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

@WebServlet("/receivablespayablesnew")

@ManagedBean(name = "pages.module.report.receivablespayablesnewBean")
public class ReceivablespayablesnewBean extends BaseServlet {


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


            //初始化
            String dynamicClauseAllWhere = "";
            if ("jobdate".equals(dates) || "inputtime".equals(dates) || "updatetime".equals(dates)) {
                dynamicClauseAllWhere = "  AND " + dates + "::DATE BETWEEN '" + (StrUtils.isNull(startdate) ? "0001-01-01" : startdate) + "' AND '" + (StrUtils.isNull(enddate) ? "9999-12-31" : enddate) + "'";
            }


            boolean isshow = false;
            try {
                String isshowpublicsql = "SELECT EXISTS(SELECT 1 FROM sys_role sr , sys_modinrole am WHERE sr.code = '" + sysUser.getCode() + "' AND roletype = 'C' AND am.roleid = sr.id AND am.moduleid = 299220)"
                        + "\nOR EXISTS(SELECT 1 FROM sys_role sr , sys_modinrole am , sys_userinrole ur WHERE roletype = 'M' AND am.roleid = sr.id AND am.moduleid = 299220 AND ur.roleid = sr.id AND ur.userid = "
                        + sysUser.getId() + ") AS flag";
                Map ispublic = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isshowpublicsql);
                if ("true".equals(StrUtils.getMapVal(ispublic, "flag"))) {
                    isshow = true;
                }
            } catch (Exception e) {
                isshow = false;
            }
            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String permissionsWhere = "AND ( fj.saleid = " + sysUser.getId()
                    + "\n	OR (fj.inputer ='" + sysUser.getCode() + "')" //录入人有权限
                    + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + sysUser.getId() + ")) " + //能看所有外办订到本公司的单权限的人能看到对应单
                    "AND fj.corpid <> " + sysUser.getSysCorporation().getId() + " AND " + sysUser.getSysCorporation().getId() + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM " +
                    "fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib x , sys_custlib_user y  "
                    + "\n				WHERE y.custlibid = x.id  "
                    + "\n					AND y.userid = " + sysUser.getId()
                    + "\n					AND x.libtype = 'S'  "
                    //+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = fj.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
                    + "\n					AND x.userid = fj.saleid " //关联的业务员的单，都能看到
                    + ")"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib x , sys_custlib_role y  "
                    + "\n				WHERE y.custlibid = x.id  "
                    + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + sysUser.getId() + " AND z.roleid = y.roleid)"
                    + "\n					AND x.libtype = 'S'  "
                    //+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = fj.saleid) " //组关联业务员的单，都能看到
                    + "\n					AND x.userid = fj.saleid " //组关联业务员的单，都能看到
                    + ")"

                    //过滤工作单指派
                    + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = fj.id AND x.linktype = 'J' AND x.userid ="
                    + sysUser.getId() + ")"
                    + "\n)"
                    //2749 税局查账系统分离方案
                    + (isshow == true ? "\nAND EXISTS(SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.jobid = fj.id"
                    + " AND EXISTS(SELECT 1 FROM sys_corporation y WHERE y.isdelete = FALSE AND y.iscustomer = TRUE "
                    + "AND y.id = x.customerid))" : "");

            String corpfilterWhere = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT fj.corpid UNION SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION SELECT corpid FROM fina_corp c" +
                    " WHERE c.jobid = fj.id AND c.isdelete = FALSE) AND x.ischoose = TRUE AND userid =" + sysUser.getId() + ") OR COALESCE(fj.saleid,0) <= 0)";


            // neo 控制费用显示 当前用户对应结算地费用
            String filterJobs = "and fa.corpid = ANY (SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.userid = " + sysUser.getId() + " and x.ischoose = true)";


            String sqlId = "pages.module.report.receivablespayablesnewBean.grid.page";
            Map parameter = new HashMap();
            parameter.put("dynamicClauseAllWhere", dynamicClauseAllWhere);
            parameter.put("permissionsWhere", permissionsWhere);
            parameter.put("corpfilterWhere", corpfilterWhere);
            parameter.put("filterJobs", filterJobs);
            List<Map> dataList = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, parameter);

            String reRet = JSON.toJSONString(dataList);
            view.setSuccess(true);
            view.setData(reRet);
            LogBean.insertLog(new StringBuffer().append("profitnew获取条件,thissql0为").append(thissql0).append(",thissql1为").append(thissql1));
        } catch (Exception e) {
            e.printStackTrace();
            LogBean.insertLog(new StringBuffer().append("profitnew报错,e为").append(e.getMessage()).append(",thissql0为").append(thissql0).append(",thissql1为").append(thissql1));
        }
        return view;
    }
}
