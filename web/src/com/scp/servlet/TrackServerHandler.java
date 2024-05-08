package com.scp.servlet;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.scp.util.AppUtils;
import com.scp.view.comp.jqueryui.GridDataModel;

public class TrackServerHandler {


	public String handle(String action, HttpServletRequest request) {
		String result = "";
		String blno = request.getParameter("blno");
		String sql = 
				"\nSELECT " +
					"\nt.dealdata" +
					"\n,t.locationc" +
					"\n,t.locatione" +
					"\n,t.statusc " +
					"\n,t.statuse " +
				"\nFROM bus_goodstrack t " +
				"\nWHERE (EXISTS(SELECT 1 FROM fina_jobs x WHERE x.nos = '"+blno+"'AND x.isdelete = false and x.id = t.fkid)) AND dealdata<=now()"+				
				"\nUNION ALL"+
				"\nSELECT " +
					"\nt.dealdata" +
					"\n,t.locationc" +
					"\n,t.locatione" +
					"\n,t.statusc " +
					"\n,t.statuse " +
				"\nFROM bus_goodstrack t " +
				"\nWHERE (EXISTS(SELECT 1 FROM fina_jobs x WHERE x.nos = '"+blno+"'AND x.isdelete = false and x.parentid = t.fkid)) AND dealdata<=now()"+
				"\nUNION ALL"+
				"\nSELECT " +
					"\nt.dealdata" +
					"\n,t.locationc" +
					"\n,t.locatione" +
					"\n,t.statusc " +
					"\n,t.statuse " +
				"\nFROM bus_goodstrack t " +			
				"WHERE(EXISTS(SELECT 1 FROM bus_ship_container x WHERE x.sono = '"+blno+"'AND x.isdelete = false and x.jobid = t.fkid)) AND dealdata<=now()" +
				"\nORDER BY dealdata DESC;";
		List<Map> maps =  AppUtils.getServiceContext().daoIbatisTemplate
		.queryWithUserDefineSql(sql);
		GridDataModel<Map> model = new GridDataModel<Map>();
		model.setRows(maps);

		result = JSONObject.fromObject(model).get("rows").toString();
		
		return result;
	}
}
