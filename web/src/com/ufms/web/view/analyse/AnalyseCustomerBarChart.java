package com.ufms.web.view.analyse;

import java.util.Date;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;

@WebServlet("/analyse_customer")
@ManagedBean(name = "pages.module.get_customer_data")
public class AnalyseCustomerBarChart extends BaseServlet {
	/**
	 * 海上航线分析 客户类型T量对比
	 * @param request
	 * @return
	 */
	@Action(method="get_customer_data")
	public String get_customer_data(HttpServletRequest request) {
		String key = request.getParameter("key");
		if(StrUtils.isNull(key))key=(new Date()).getYear()+";100";
		
		String year = key.split(";")[0];
		String companyId = key.split(";")[1];
		
		String param = "";
		if("".equals(year) || "0".equals(year)) {
			year = (new Date()).getYear()+"";
		}
		if("".equals(companyId) || "0".equals(companyId)) {
			param = "'"+year+"%',100";
		}else {
			param = "'"+year+"%',"+companyId;
		}
		
		String sql = "select f_analyse_customer_barchart("+param+") as json;";
		String barChartData = "[]";//返回数据
		
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			
			barChartData =StrUtils.getMapVal(map, "json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return barChartData;
	}
}