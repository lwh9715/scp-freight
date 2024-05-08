package com.ufms.web.view.sysmgr;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysLogDao;
import com.scp.model.sys.SysLog;
import com.scp.util.AppUtils;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.GridFormView;

@WebServlet("/sysmgr/log")
@ManagedBean(name = "pages.module.data.logBean", tableName = "sys_log", orderby = "logtime DESC")
public class LogBean extends GridFormView {


    public static void insertLog(StringBuffer logdesc) {
        try {
            SysLog sysLog = new SysLog();
            sysLog.setInputer("insertLog");
            sysLog.setLogtime(new Date());
            sysLog.setLogdesc(logdesc.toString());
            sysLog.setLogtype("DEBUG");
            SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
            sysLogDao.create(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertLog异常,e_" + e.getMessage());
        }
    }


    public static void insertLastingLog(StringBuffer logdesc) {
        try {
            String logdescstr = logdesc.toString().replaceAll("'", "''");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "INSERT INTO sys_lastinglog (id,inputer,logtime,logdesc,isdelete,logtype)"
                    + "\nVALUES (getid_tmp(),'insertLastingLog',now(),'" + logdescstr + "','f','lasting');";
            daoIbatisTemplate.updateWithUserDefineSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertLastingLog异常,e_" + e.getMessage());
        }
    }

    public static void insertLastingLog2(StringBuffer logdesc, String inputer) {
        try {
            String logdescstr = logdesc.toString().replaceAll("'", "''");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "INSERT INTO sys_lastinglog (id,inputer,logtime,logdesc,isdelete,logtype)"
                    + "\nVALUES (getid_tmp(),'" + inputer + "',now(),'" + logdescstr + "','f','lasting');";
            daoIbatisTemplate.updateWithUserDefineSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertLastingLog2异常,e_" + e.getMessage());
        }
    }

    public static void insertLastingLog3(StringBuffer logdesc, String inputer, String logtype) {
        try {
            String logdescstr = logdesc.toString().replaceAll("'", "''");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "INSERT INTO sys_lastinglog (id,inputer,logtime,logdesc,isdelete,logtype)"
                    + "\nVALUES (getid_tmp(),'" + inputer + "',now(),'" + logdescstr + "','f','" + logtype + "');";
            daoIbatisTemplate.updateWithUserDefineSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertLastingLog2异常,e_" + e.getMessage());
        }
    }

}

