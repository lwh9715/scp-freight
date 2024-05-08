package com.scp.servlet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;

public class UpdateUserServerHandler {
	
	
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request,HttpServletResponse response){
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			if("pwd".equals(action)) {
				result = this.updatepassword(request,response);
			}
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	
	private String updatepassword(HttpServletRequest request,HttpServletResponse response) {
		try {
			String uid = request.getParameter("uid");
			if(uid != null && uid.length() > 0){
//				String dzz_user_pwd = "SELECT password,salt FROM yos_user WHERE uid = "+uid.replaceAll("'", "''");
//				Map m3 = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(dzz_user_pwd);
//				if(m3!=null && m3.containsKey("password") && m3.containsKey("salt")){
//					//System.out.println(uid);
//					serviceContext.userMgrService.sysUserDao.executeSQL("UPDATE sys_user SET ciphertext='"+String.valueOf(m3.get("password"))+"',secretkey='"+String.valueOf(m3.get("salt"))+"' WHERE isdelete = false AND fmsid ="+uid.replaceAll("'", "''"));
//				}
			}
		} catch (NoRowException e) {
			
		} catch (NullPointerException e) {
			
		} catch (Exception e) {
			
		}
		return "success";
	}
}
