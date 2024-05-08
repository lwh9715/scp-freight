package com.scp.aop;

import org.aspectj.lang.JoinPoint;

import com.scp.exception.NoSessionException;
import com.scp.util.AppUtils;

public class DaoUpdateInterceptor {

	public Object doBasicProfiling(JoinPoint pjp) throws Throwable {
		
		String user;
		try {
			user = AppUtils.getUserSession().getUsercode();
		} catch (NoSessionException e) {
			user = "";
		}
		
		pjp = AppUtils.aopProcessUpdate(pjp , user);
		
		return pjp;
	}
}
