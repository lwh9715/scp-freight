package com.scp.servlet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.scp.util.AppUtils;
public class MessageServerHandle {
	public String handle(String billnos,HttpServletRequest request) {
		String result = "";
		String sql ="SELECT  " + 
			"\nb.id" +
			",\nb.hblno" +
			",\n(SELECT string_agg(cntno,',') FROM bus_ship_container WHERE isdelete = FALSE AND billid = b.id) AS cntno" +
			",\n(SELECT to_char(x.eta,'YYYY年MM月DD日') FROM  bus_shipping x WHERE x.jobid = b.jobid) AS eta " +
			",\n(SELECT to_char(x.eta,'MM/DD/YY') FROM  bus_shipping x WHERE x.jobid = b.jobid) AS eta_e " +
			",\n(SELECT string_agg((CASE  WHEN  t.loadtime IS NULL THEN '' ELSE to_char(t.loadtime,'YYYY年MM月DD日')END),',') FROM bus_truck t  WHERE   t.jobid = b.jobid AND t.isdelete = FALSE)AS tctime" +   
			",\n(SELECT f_lists((CASE WHEN tel1 IS NULL OR tel1 = '' THEN tel2 ELSE tel1 END)) FROM sys_user s,sys_user_assign t ,bus_shipping x WHERE t.rolearea = 'D' AND t.roletype = 'C' AND s.isdelete = FALSE AND x.isdelete = FALSE AND s.id = t.userid AND t.linkid = x.id AND x.jobid = b.jobid) AS tel" +  
			"\nFROM bus_ship_bill b" + 
			"\nWHERE b.isdelete = FALSE"+
			"\nAND b.hblno = '"+billnos+"'"; 
		try {
			Map map =  AppUtils.getServiceContext().daoIbatisTemplate
			.queryWithUserDefineSql4OnwRow(sql);
			createLog((String)map.get("hblno"),(Long)map.get("id"),request);
			JSONObject jsonObjectrRow = new JSONObject();
			jsonObjectrRow.put("hblno", (String)map.get("hblno"));
			jsonObjectrRow.put("cntno", (String)map.get("cntno"));
			jsonObjectrRow.put("eta", (String)map.get("eta"));
			jsonObjectrRow.put("tctime", (String)map.get("tctime"));
			jsonObjectrRow.put("tel", (String)map.get("tel"));
			jsonObjectrRow.put("eta_e", (String)map.get("eta_e"));
			return jsonObjectrRow.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 写入日志
	 */
	public void createLog(String nos,long id,HttpServletRequest request){
		String name =request.getRemoteHost();
		String os = request.getHeader("user-agent");
		String type ="";
		if(os.contains("Android")){
			type = "Android";
		}else if(os.contains("iPhone")){
			type = "iPhone";
		} 
		String ip = request.getRemoteAddr();
		String logdesc = "提单二维码扫描记录：提单号:"+nos+";IP地址:"+name+";操作系统:"+type+";操作系统及浏览器与浏览器版本信息:"+os+"";
		//记录日志
		String sql = "SELECT f_log('','"+logdesc+"',"+id+")";
		AppUtils.getServiceContext().daoIbatisTemplate
		.queryWithUserDefineSql4OnwRow(sql);
		//AppUtils.debug(logdesc);
		//AppUtils.debug("记录成功!");
	}
}
