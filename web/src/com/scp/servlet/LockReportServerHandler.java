package com.scp.servlet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

public class LockReportServerHandler {
	
	
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request,HttpServletResponse response){
		String userid = request.getParameter("userid");//用户id
		String reportid = request.getParameter("reportid");//报表id
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			if("tolockbill".equals(action)) {
				result = this.lockbill(request,response,userid,reportid);
			}
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 读取个人设置判断是否锁账单
	 * @param request
	 * @param response
	 * @param userid
	 * @param reportid
	 * @return
	 */
	private String lockbill(HttpServletRequest request,HttpServletResponse response, String userid, String reportid) {
		if(!Boolean.parseBoolean(ConfigUtils.findUserCfgVal("bill_export_lock", Long.parseLong(userid)))){
			return "''";
		}
		try {
			String username = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid)).getNamec();
			String sql = "UPDATE fina_bill SET isconfirm = TRUE,confirmer='"+username+"',confirmdate='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())+"' WHERE isdelete = FALSE AND id = "+StrUtils.getSqlFormat(reportid);
			serviceContext.billMgrService.finaBillDao.executeSQL(sql);
		} catch (NoRowException e) {
			
		} catch (NullPointerException e) {
			
		} catch (Exception e) {
			
		}
		return "''";
	}
	
	
}
