package com.scp.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;

public class ReportServerHandler{
	
	
	
	
	
	public String handle(String action, HttpServletRequest request,HttpServletResponse response){
		String customsid = request.getParameter("customsid");//jobid
		String result = "";
		try {
			String sql = "\nSELECT "+
			"\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+ 
			"\n FROM (" +
			"\n SELECT " +
			"\n		 a.*" +
			"\n		,b.itno,b.itdate,b.amsno,b.cargolocation,b.address,b.itno,b.itdate,b.itlocation,b.remarks4,b.isfno" +
			"\n 	,b.remarks3,a.onboarddate,to_char(b.date_lfd,'yyyy-MM-dd HH:MM:SS') AS date_lfd" +
			"\n		,(SELECT abbr FROM sys_corporation WHERE a.carrierid = id) AS carrierabbr"+
			"\n 	,(SELECT COALESCE(namee,code) FROM sys_corporation WHERE b.customid = id) AS companynamee"+
			"\n 	,(to_char(now(),'dd-MM-yyyy MM:dd')) AS nowDate" +
			"\n 	,(SELECT string_agg(x.cntno || '/' || x.cntypedesc || '/' || x.sealno,f_newline()) FROM _bus_ship_container x where x.jobid = a.jobid and x.isdelete = false) AS marking"+
			"\n 	,a.piece || ' ' || a.packer ||' '||(SELECT string_agg(x.goodsnamee,f_newline()) FROM bus_ship_container x where x.jobid = a.jobid and x.isdelete = false) AS ppkggdsname"+
			"\n FROM bus_shipping a,bus_customs b " +
			"\n WHERE b.id = "+(customsid.equals("")?"-1":customsid)+" " +
			"\n AND b.jobid=a.jobid) AS T" ;
			String ret = "";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
			Map<String, Object> map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{'label': ''}";
			}
			return ret;
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	public String booking(String action, HttpServletRequest request,HttpServletResponse response){
		String shipid = request.getParameter("shipid");//jobid
		String result = "";
		try {
			String sql = "\nSELECT "+
			"\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+ 
			"\n FROM (SELECT b.cnorname ,b.contractno,b.cneetitle,b.notifytitle,b.sono,b.grswgt,(b.vessel||'/'||b.voyage) AS vsvo,b.cls,b.cls,b.pol,b.pdd,b.poa" +
			",'TOTAL:'||(SELECT f_fina_jobs_cntdesc('jobid='::text||b.jobid)) AS cntdesc"+
			"\n FROM bus_shipping b WHERE b.id = "+(shipid.equals("")?"-1":shipid)+") AS T" ;
			String ret = "";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
			Map<String, Object> map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{'label': ''}";
			}
			return ret;
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	
	
}
