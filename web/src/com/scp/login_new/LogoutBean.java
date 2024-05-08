package com.scp.login_new;

import org.operamasks.faces.annotation.AfterRender;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;

import com.scp.service.LoginService;

@ManagedBean(name="login_new.logoutBean", scope=ManagedBeanScope.REQUEST)
public class LogoutBean {
	
	@ManagedProperty("#{loginService}")
	public LoginService loginService;
	
	@AfterRender
	public void afterRender() {
		loginService.logout();
	}

}

