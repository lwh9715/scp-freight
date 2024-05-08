package com.scp.aop;

import org.aspectj.lang.JoinPoint;

import com.scp.exception.NoSessionException;
import com.scp.util.AppUtils;

public class DaoCreateOrUpdateInterceptor {

	public Object doBasicProfiling(JoinPoint pjp) throws Throwable {
		
		String user;
		Long corpid;
		try {
			user = AppUtils.getUserSession().getUsercode();
			corpid = AppUtils.getUserSession().getCorpid();
		} catch (NoSessionException e) {
			user = "";
			corpid = 100l;
		}
		
		pjp = AppUtils.aopProcessCreateOrUpdate(pjp , user , corpid);
		
		return pjp;
	}
}
