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

@WebServlet("/analyse_route")
@ManagedBean(name = "pages.module.analyse_route")
public class AnalyseRouteByPeichart extends BaseServlet {
	/**
	 * 航线分析
	 * @param request
	 * @return 航线分析需要的数据
	 */
	@Action(method="get_analyse_route_data")
	public String getAnalyseRouteData(HttpServletRequest request) {
		
		String sql = "";
		sql = "select f_analyse_route_peichart() as json;";
		String peiChartData = "[]";
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			
			peiChartData = StrUtils.getMapVal(map, "json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peiChartData;
	}
}

