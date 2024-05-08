package com.scp.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.ufms.base.wx.WeiXinUtil;

public class WeiXinServerHandler {
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public JdbcTemplate jdbcTemplate;
	
	// 获取日志信息
	public String handle(String action, HttpServletRequest request, HttpServletResponse resp) {
		String result = "";
		String userid = request.getParameter("userid");
		if("bind".equals(action)){
			
			String code = request.getParameter("code");
			String type = request.getParameter("type");
			
			String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");
			String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");
			
			String cs_url_base = ConfigUtils.findSysCfgVal("cs_url_base");
			
			try {
//				String openid = weiXinService.getOpenid( code ,appID , appsecret);
				String openid = WeiXinUtil.getOpenid(code, appID, appsecret);
				if(!StrUtils.isNull(openid) && openid.indexOf("ERROR")>0){
					System.out.println("**************************ERROR:"+openid);
					openid = "";
				}
				if(!StrUtils.isNull(openid)){
					serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
					String dmlSql = "UPDATE sys_user SET opneid = '"+openid+"' WHERE id = "+userid+" AND COALESCE(opneid , '') = '';";
					if("so_user_cs".equals(type)){
						dmlSql = "UPDATE cs_user SET openid = '"+openid+"' WHERE id = "+userid+" AND COALESCE(openid , '') = '';";
					}else if("scan_login".endsWith(type)){
						dmlSql = "INSERT INTO sys_useronline(id,logintime,loginuuid,openid)"
							   + "\nVALUES(getid(),now(),'"+userid+"','"+openid+"')";
					}
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
					result = "OK";
					
					if("so_user_cs".equals(type)){
						resp.sendRedirect(cs_url_base + "/weixin.html");
					}else if("scan_login".endsWith(type)){
						resp.sendRedirect(cs_url_base + "/weixinscanlogin.html?loginuuid="+userid);
					}
				}
			} catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
				try {
					resp.sendRedirect(cs_url_base + "/weixin.html?success=false&message=error");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return result;
	}
	
}