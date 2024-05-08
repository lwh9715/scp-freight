package com.scp.servlet;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplateMySql;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;

public class DzzServerHandler {

	@Resource
	public ServiceContext serviceContext;

	@Resource
	public DaoIbatisTemplateMySql daoIbatisTemplateMySql;

	public String handle(String action, HttpServletRequest request) {
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils
					.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplateMySql = (DaoIbatisTemplateMySql) AppUtils
					.getBeanFromSpringIoc("daoIbatisTemplateMySql");
			if("test".equals(action)) {
				result = this.queryTest(request);
			}
			
			if("addico".equals(action)) {
				result = this.addDesktopIco(request);
			}
			
			if("removeico".equals(action)) {
				result = this.removeDesktopIco(request);
			}

		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}

	private String removeDesktopIco(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private String addDesktopIco(HttpServletRequest request) {
		return null;
	}

	private String queryTest(HttpServletRequest request) {
		String sql = "SELECT * FROM yos_icos limit 1";
		Map m = daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(sql);
		//AppUtils.debug(m);
		return m.toString();
	}
}