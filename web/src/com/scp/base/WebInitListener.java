
package com.scp.base;

import java.io.Serializable;
import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.scp.schedule.ext.ScheduleMgr;
import com.scp.util.AppUtilBase;
import com.scp.util.AppUtils;
import com.ufms.base.utils.AppUtil;

public class WebInitListener implements ServletContextListener ,Serializable {
	
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		AppUtils.applicationContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()); 
		AppUtilBase.applicationContext = AppUtils.applicationContext; 
		AppUtil.applicationContext = AppUtils.applicationContext;
//		//标记是否是调试模式
//		AppUtils.isDebug = true;
//		AppUtils.getRptUrl() = AppUtils.isDebug?"http://192.168.0.188:8888/scp":"http://120.25.241.190:8888/scp";
//		//标记是否是只读数据库
//		AppUtils.isReadOnlyDB = false;
//		//标记是否自动填充多语言
//		AppUtils.isAutoFillLs = false;
		TimeZone zone = TimeZone.getTimeZone("GMT+8");  
	    TimeZone.setDefault(zone);  
	    
//	    FacesContext context = FacesContext.getCurrentInstance();
//		Locale locale = Locale.SIMPLIFIED_CHINESE;
//		//Locale locale = Locale.ENGLISH;
//		Locale.setDefault(locale);
//		context.getViewRoot().setLocale(locale);
		
//		ExternalContext externalContext = context.getExternalContext();
//		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(AppUtils.getHttpSession().getServletContext()); 
////		if(applicationContext == null) {
//			applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//			//System.out.println("ALERT! ApplicationContext load double form ClassPath");
//			//System.out.println("ALERT! ApplicationContext load double form ClassPath");
//			//System.out.println("ALERT! ApplicationContext load double form ClassPath");
////		}
//	    try {
//			SystemInfo systemInfo = SystemMoniter.checkSystemInfoBase();
//			systemInfo.licenseType = "product";
//			//systemInfo.remark2 = "HangXunKeJi";
//			//AppUtils.applicationContext = applicationContext;
//			//ApplicationUtilBase.applicationContext = applicationContext;
//		} catch (BusinessException e) {
//			//e.printStackTrace();
//			//System.out.println(e.getLocalizedMessage());
//		}
		
		if(!AppUtils.isDebug){
			try {
				ScheduleMgr scheduleMgr = (ScheduleMgr)AppUtils.getBeanFromSpringIoc("scheduleMgr");
				scheduleMgr.addJob();
				System.out.println("ScheduleMgr start.............................");
			} catch (Exception e) {
				System.out.println("ScheduleMgr start error ............................." + e.getLocalizedMessage());
				//e.printStackTrace();
			}
		}
		/*//根据系统设置开启或关闭ehcache缓存
		try {
			String isStartEcCache = ConfigUtils.findSysCfgVal("sys_cache_state");
			if("Y".equalsIgnoreCase(isStartEcCache)){
				EhCacheUtil.switchState(true);
			}else{
				EhCacheUtil.switchState(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
