package com.ufms.web.view.analyse;

import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;

@WebServlet("/analyse_company_route")
@ManagedBean(name = "pages.module.analyse_company_route")
public class AnalyseCompanyRoute extends BaseServlet {
	/**
	 * 海上航线分析 根据船公司航线分组
	 * @param request
	 * @return
	 */
	@Action(method="get_company_route_data")
	public String logout(HttpServletRequest request) {
		String companyId = request.getParameter("key");
		if(StrUtils.isNull(companyId))companyId="100";
		String sql = "select f_analyse_company_route_barchart() as json;";
		String peiChartData = "[]";
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			
			peiChartData =StrUtils.getMapVal(map, "json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peiChartData;
	}
}

