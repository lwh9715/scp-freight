package com.scp.util;

import javax.servlet.http.HttpServletRequest;

import com.scp.base.ApplicationConf;

public class SaasUtil {

	public static String filterByCorpid(String tabelAlis){
		String qry = "";
		try {
			qry = filterByCorpid(tabelAlis,AppUtils.getUserSession().getUserid().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qry;
	}

	public static String filterByCorpid(String tabelAlis, String userid) {
		String qry = "";
		ApplicationConf applicationConf = (ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf");
		if(!applicationConf.isSaas()){
			qry = "";//非saas模式不控制
		}else{
			qry = "\nAND "+tabelAlis+".corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+userid+")";
		}
		return qry;
	}
	
	
	public static String filterByCorpid(String tabelAlis, HttpServletRequest request) {
		String qry = "";
		try {
			qry = filterByCorpid(tabelAlis,request.getSession().getAttribute("userid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qry;
	}
	
	public static String filterById(String tabelAlis){
		String qry = "";
		try {
			qry = filterById(tabelAlis,AppUtils.getUserSession().getUserid().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qry;
	}
	
	public static String filterById(String tabelAlis, String userid) {
		String qry = "";
		ApplicationConf applicationConf = (ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf");
		if(!applicationConf.isSaas()){
			qry = "";//非saas模式不控制
		}else{
			qry = "\nAND "+tabelAlis+".id = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+userid+")";
		}
		return qry;
	}
}
