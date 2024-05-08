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

@WebServlet("/analyse_t_volume")
@ManagedBean(name = "pages.module.analysetvolume")
public class AnalyseTVolumeBarChart extends BaseServlet {
	
	/**
	 * 海上航线分析
	 * @param request
	 * @return
	 */
	@Action(method="get_t_volume_data")
	public String get_t_volume_data(HttpServletRequest request) {
		String companyId = request.getParameter("key");
		if(StrUtils.isNull(companyId))companyId="100";
		String sql = "select f_analyse_tvolume_barchart("+companyId+") as json;";
		String barChartData = "[]";
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

