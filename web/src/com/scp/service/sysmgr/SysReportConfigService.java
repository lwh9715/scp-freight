package com.scp.service.sysmgr;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.operamasks.org.json.simple.JSONArray;
import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysReportConfigDao;
import com.scp.util.AppUtils;

@Component
public class SysReportConfigService{
	@Resource
	public SysReportConfigDao sysReportConfigDao;
	
	/**
	 * 保存锁选择的常用报表
	 * */
	public void saveSetUsercinfig(String rptype,String reportid,boolean isuse){
		String sql = 
			"\n INSERT INTO sys_report_config ("+
            "\n id, rptype, reportid, userid, isuse)"+
            "\n SELECT getid(), '" + rptype + "', "+Long.valueOf(reportid)+","+AppUtils.getUserSession().getUserid()+", "+isuse+" FROM _virtual where NOT EXISTS(SELECT 1 FROM sys_report_config x WHERE x.rptype = '" + rptype + "' AND reportid ="+Long.valueOf(reportid)+" and userid ="+AppUtils.getUserSession().getUserid()+")";
		
		String sql2 = 
			"\n UPDATE sys_report_config set isuse = "+isuse+" WHERE userid = "+AppUtils.getUserSession().getUserid()+
			"\n AND rptype = '"+rptype+"' AND reportid = " + Long.valueOf(reportid);
		sysReportConfigDao.executeSQL(sql);
		sysReportConfigDao.executeSQL(sql2);
	}
	
	/**
	 * 初始化常用报表
	 * */
	public void initialize(String rptype,boolean isuse){
		String sql = 
			"\n DELETE FROM sys_report_config WHERE userid = "+AppUtils.getUserSession().getUserid()+
			"\n AND rptype = '"+rptype+"' AND isuse = " + isuse;
		sysReportConfigDao.executeSQL(sql);
	}
	
	public String getDateTojson(JSONArray modified,ArrayList<Object> values){
		net.sf.json.JSONArray datev = new net.sf.json.JSONArray();
		for (Object v : values) {
			HashMap<String, Object> map = (HashMap<String, Object>)v;
			String id = map.get("id").toString();
			String currencyfm = map.get("currencyfm").toString();
			String rate = map.get("rate").toString();
			String xtype = map.get("xtype").toString();
			String currencyto = map.get("currencyto").toString();
			JSONObject ob = new JSONObject();
			ob.put("id", id);
			ob.put("currencyfm", currencyfm);
			ob.put("rate", rate);
			ob.put("xtype", xtype);
			ob.put("currencyto", currencyto);
			datev.add(ob);
		}
		for (Object v : modified) {
			datev.add(v);
		}
		return datev.toString();
	}
	
}
