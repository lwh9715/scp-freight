/*$Id: UserLoginFilter.java,v 1.1 2008/07/15 05:13:54 liqi Exp $
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * author   date   comment
 * chenhongxin  2008-4-14  Created
*/
package com.scp.login;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;

/**
 * 用户登陆检测过滤器，拦截/module/*的访问路径，
 * 检查访问用户是否登陆，未登陆则跳转到登陆页面。
 * @author chenhongxin
 */
public class UserLoginFilter implements Filter {

    public void destroy() {
        
    }

    public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)request).getSession();
        String contextRoot = ((HttpServletRequest)request).getContextPath();
        
        String servletPath=((HttpServletRequest)request).getServletPath();
        //System.out.println("~~~~~~~~~~~~servletPath:"+servletPath);
        
        if(servletPath.contains("/crm/")){
        	chain.doFilter(request, response);
        	return ;
        }
        
        if(session == null || session.getAttribute("UserSession") == null) {
        	String toUrl = "";
        	ApplicationConf applicationConf = (ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf");
//			boolean isUseDzz = applicationConf.getIsUseDzz();
//			if(!isUseDzz){
//				toUrl = "/login_new/relogin2.html";
//			}else{
				toUrl = "/login/relogin.html";
//			}
            ((HttpServletResponse)response).sendRedirect(contextRoot + toUrl);
            return ;
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

}
