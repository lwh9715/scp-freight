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

@WebServlet("/analyse_port_map")
@ManagedBean(name = "pages.module.analyse_port_map")
public class AnalysePortMap extends BaseServlet {
	/**
	 * 世界港口地图
	 * @param request
	 * @return
	 */
	@Action(method="get_port_map_data")
	public String getPortMapData(HttpServletRequest request) {
		String sql = "select f_analyse_port_map() as json;";
		String routeData = "[]";
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			
			routeData = StrUtils.getMapVal(map, "json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routeData;
	}
}

