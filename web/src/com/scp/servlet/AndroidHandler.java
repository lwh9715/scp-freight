package com.scp.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.scp.util.JSONReader;
import com.scp.util.StrUtils;

public class AndroidHandler {
	
	//获取日志信息
	public String handle(String action,HttpServletRequest request){
		String result = "Unuse";
		if("gps".equals(action)){
			String data = request.getParameter("data");
			StringBuffer exeSql = new StringBuffer(); 
			if(!StrUtils.isNull(data)) {
				JSONReader jr = new JSONReader();
				Object jsonObject = jr.read(data);
				if(jsonObject instanceof Map) {
					Map m = (Map)jsonObject;
					getInsSql(exeSql, m);
				} else if (jsonObject instanceof List<?>) {
					List<Map> list = (List<Map>)jsonObject;
					for(Map m : list) {
						getInsSql(exeSql, m);
					}
				} else {
					return result;
				}
			}
			try {
				if(!StrUtils.isNull(exeSql.toString())) {
					result = "OK";
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = "Execute Sql Error";
			}
		}
		return result;
	}

	private void getInsSql(StringBuffer exeSql,
			Map m) {
		String latitude = StrUtils.getMapVal(m, "x");//维度
		String longitude = StrUtils.getMapVal(m, "y");//经度
		String imei = StrUtils.getMapVal(m, "imei");;
		String result = "SRV@GPS->>>>>>>> imei:" + imei + "维度:" + latitude + "经度:" + longitude;
		//AppUtils.debug(result);
		
		String sql = "\n INSERT INTO _gps(gpsid,imei,latitude,longitude) VALUES (getpkid(1),'%s','%s','%s');";
		sql = String.format(sql, imei , latitude , longitude);
		exeSql.append(sql);
	}
}