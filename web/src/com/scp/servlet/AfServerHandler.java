package com.scp.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.scp.base.UserSession;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.jqueryui.GridDataModel;

public class AfServerHandler {

	@Resource
	public ServiceContext serviceContext;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public String handle(String action, HttpServletRequest request) {
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils
					.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
					.getBeanFromSpringIoc("daoIbatisTemplate");
			if ("qrySms".equals(action)) {
				//isLogin(request);
				result = this.qrySms(request);
			} 
		
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 查询短信
	 * 
	 * @param request
	 * @return
	 */
	private String qrySms(HttpServletRequest request) {

		UserSession userSession = (UserSession) request.getSession()
				.getAttribute("UserSession");
		String username = userSession.getUsername().toUpperCase();

		String page = request.getParameter("start"); // 取得当前页数,注意这是jqgrid自身的参数
		String rows = request.getParameter("limit"); // 取得每页显示行数，,注意这是jqgrid自身的参数

		int start = Integer.parseInt(StrUtils.isNull(page) ? "0" : page);
		int limit = Integer.parseInt(StrUtils.isNull(rows) ? "20" : rows);
		if (limit == 0) {
			limit = Integer.MAX_VALUE;
		}

		String key= request.getParameter("key");
		try {
			key = URLDecoder.decode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		String qry = "";
		if (!StrUtils.isNull(key)) {
			key = key.replaceAll("'", "''");
			key = key.toUpperCase();
			qry = " AND (UPPER(phone) LIKE '%" + key
					+ "%' OR UPPER(content) LIKE '%" + key + "%')";
		}

		List<Map> listCounts = daoIbatisTemplate
				.queryWithUserDefineSql("SELECT COUNT(*) AS counts FROM sys_sms WHERE isdelete = false  and UPPER(inputer)='"
						+ username + "'" + qry + ";");

		Map map = listCounts.get(0);
		Integer counts = Integer.parseInt(StrUtils.getMapVal(map, "counts"));
		int totalRecord = counts;

		GridDataModel<Map> model = new GridDataModel<Map>();
		model.setTotal(totalRecord);

		List<Map> maps = daoIbatisTemplate
				.queryWithUserDefineSql("SELECT id,phone, content , to_char(senttime, 'YY/MM/DD HH24:MI') as senttime FROM sys_sms WHERE isdelete = FALSE "
						+ " and UPPER(inputer)='"
						+ username
						+ "'"
						+ qry
						+ " ORDER BY senttime desc LIMIT "
						+ rows
						+ " OFFSET "
						+ start);

		model.setRows(maps);

		String ret = JSONObject.fromObject(model).toString();
		return ret;

	}
}
	
	
