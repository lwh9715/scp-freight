
package com.scp.base;

import com.scp.util.AppUtilBase;
import com.scp.util.AppUtils;
import com.ufms.base.utils.AppUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.Serializable;
import java.util.TimeZone;

public class WebInitListener implements ServletContextListener, Serializable {

    public void contextDestroyed(ServletContextEvent arg0) {

    }

    public void contextInitialized(ServletContextEvent arg0) {
        AppUtils.applicationContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
        AppUtilBase.applicationContext = AppUtils.applicationContext;
        AppUtil.applicationContext = AppUtils.applicationContext;
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(zone);

    }
}
