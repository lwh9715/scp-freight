package com.scp.schedule;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.CfgUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;


public class AutoPostUplat {
	private static boolean isRun = false;

	public void execute() throws Exception {
		// if(AppUtils.isDebug)return;
		// AppUtils.debug("AutoFixBugJob Start:" + new Date());
		if (isRun) {
			System.out.print("AutoPostUplat wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			//submitQccQuery();
			submitEPortQuery();
			submitYanTianQuery();
			submitEPortSoQuery("");
			submitNanShaSoQuery("");
			
			
			downEPortSoData("");
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}
	
	
	public static String getPlatUrl(){
		String uplatUrl = ConfigUtils.findSysCfgVal("uplat_url");
		
		if(uplatUrl == null || uplatUrl == ""){
			uplatUrl = "http://hangxun.vicp.io:18888/uplat";
		}
		return uplatUrl;
	}

	

	public void submitQccQuery() throws Exception {
		try {
			
			
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String sql = 
				"\nSELECT "+
				"\n	x.namec , f_sys_config('CSNO') AS csno  "+
				"\nFROM sys_corporation x "+ 
				"\nWHERE x.isdelete = FALSE  "+
				//"\n	AND inputtime < (NOW() + '-30day') "+ 
				"\n	AND x.namec is not null  "+
				"\n	AND x.namec <> ''  "+
				"\n	AND iscustomer = TRUE "+ 
				"\n	AND iscarrier = FALSE "+
				"\n	AND isairline = FALSE "+
				"\n	AND namec ~ '[\u2e80-\ua4cf]|[\uf900-\ufaff]|[\ufe30-\ufe4f]' "+
				"\n	AND length(namec) > 4 "+
				//"\n AND isofficial = TRUE "+
				"\n AND NOT EXISTS(SELECT 1 FROM plat_subscribe zz WHERE zz.nos = x.namec AND zz.substate = 'Upload' AND subtype = 'CUS_QCC')"+
				"\n AND (isshipping OR isair) "+
				"\n	ORDER BY inputtime DESC LIMIT 10"+
				"\n";
			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(lists != null && lists.size()>0){
				for (Map map : lists) {
					String customernamec = StrUtils.getMapVal(map, "namec");
					
					String insertSql = "INSERT INTO plat_subscribe(id,nos,subtype,substate) VALUES(getid_tmp(),'"+StrUtils.getSqlFormat(customernamec)+"' , 'CUS_QCC' , 'Upload');";
					
					customernamec = URLEncoder.encode((URLEncoder.encode(customernamec, "utf-8")), "utf-8");
					
					String csno = StrUtils.getMapVal(map, "csno");
					
					String url = getPlatUrl() + "/subscribe?method=refresh&cntno="+customernamec+"&subtype=CUS_QCC"+ "&csno=" + csno;
					//System.out.println("submitQccQuery:"+url);
					String response = AppUtils.sendPost(url, "");
					if(!StrUtils.isNull(response)){
						if(response.indexOf("\"status\": 200") > 0 || response.indexOf("\"status\": 404") > 0){
							serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insertSql);
						}
					}
				}	
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
	
	
	private void submitEPortQuery() throws Exception {
		try {
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String sql = 
				"\nSELECT "+
				"\n	z.cntno , " +
				"\n (cls::TIMESTAMP + '-7DAY')::DATE AS starttime ," +
				"\n cls AS endtime , " +
				"\n x.jobid , " +
				"\n f_sys_config('CSNO') AS csno  "+
				"\n FROM bus_shipping x , bus_ship_container z  "+
				"\n where cls >= (NOW() + '-2Day' )::DATE AND cls <= ((NOW() + '6Day' )::DATE)  "+
				"\n AND x.isdelete = FALSE  "+
				"\n AND etd IS NOT NULL  "+
				"\n AND x.polid = ANY(SELECT x.id FROM dat_port x where x.namee IN ('SHEKOU','CHIWAN','MAWAN','DACHANWAN'))  "+
				"\n AND z.jobid = x.jobid and z.isdelete = false and z.cntno is not null  "+
				"\n and TRIM(z.cntno) <> '' AND z.cntno is not null"+
				"\n AND EXISTS(SELECT 1 FROM fina_jobs z where z.id = x.jobid and z.isdelete = false and z.parentid IS NULL)   "+
				
				"\n AND NOT EXISTS(SELECT 1 FROM plat_subscribe zz WHERE zz.nos = z.cntno AND zz.substate = 'Upload' AND subtype = 'CNT_Track')"+
				
				"\n	ORDER BY z.inputtime LIMIT 200"+
				"\n";
			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(lists != null && lists.size()>0){
				for (Map map : lists) {
					String cntno = StrUtils.getMapVal(map, "cntno");
					
					String insertSql = "INSERT INTO plat_subscribe(id,nos,subtype,substate) VALUES(getid_tmp(),'"+StrUtils.getSqlFormat(cntno)+"' , 'CNT_Track' , 'Upload');";
					
					String csno = StrUtils.getMapVal(map, "csno");
					String starttime = StrUtils.getMapVal(map, "starttime");
					String endtime = StrUtils.getMapVal(map, "endtime");
					
					String jobid = StrUtils.getMapVal(map, "jobid");
					
					String url = getPlatUrl() + "/subscribe?method=refresh&cntno="+cntno+"&subtype=CNT_Track"+ "&csno=" + csno+ "&starttime=" + starttime + "&endtime=" + endtime;
					//System.out.println("submitEPortQuery:"+url);
					String response = AppUtils.sendPost(url, "");
					if(!StrUtils.isNull(response)){
						if(response.indexOf("\"status\": 200") > 0 || response.indexOf("\"status\": 404") > 0){
							serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insertSql);
						}
					}
				}	
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
	
	private void submitYanTianQuery() throws Exception {
		try {
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String sql = 
				"\nSELECT "+
				"\n	z.cntno , " +
				"\n (cls::TIMESTAMP + '-7DAY')::DATE AS starttime ," +
				"\n cls AS endtime , " +
				"\n x.jobid , " +
				"\n f_sys_config('CSNO') AS csno  "+
				"\n FROM bus_shipping x , bus_ship_container z  "+
				"\n where cls >= (NOW() + '-2Day' )::DATE AND cls <= ((NOW() + '6Day' )::DATE)  "+
				"\n AND x.isdelete = FALSE  "+
				"\n AND etd IS NOT NULL  "+
				"\n AND x.polid = ANY(SELECT x.id FROM dat_port x where x.namee IN ('YANTIAN'))  "+
				"\n AND z.jobid = x.jobid and z.isdelete = false and z.cntno is not null  "+
				"\n and TRIM(z.cntno) <> '' AND z.cntno is not null"+
				"\n AND EXISTS(SELECT 1 FROM fina_jobs z where z.id = x.jobid and z.isdelete = false and z.parentid IS NULL)   "+
				
				"\n AND NOT EXISTS(SELECT 1 FROM plat_subscribe zz WHERE zz.nos = z.cntno AND zz.substate = 'Upload' AND subtype = 'CNT_Track')"+
				
				"\n	ORDER BY z.inputtime LIMIT 200"+
				"\n";
			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(lists != null && lists.size()>0){
				for (Map map : lists) {
					String cntno = StrUtils.getMapVal(map, "cntno");
					
					String insertSql = "INSERT INTO plat_subscribe(id,nos,subtype,substate) VALUES(getid_tmp(),'"+StrUtils.getSqlFormat(cntno)+"' , 'CNT_Track' , 'Upload');";
					
					String csno = StrUtils.getMapVal(map, "csno");
					String starttime = StrUtils.getMapVal(map, "starttime");
					String endtime = StrUtils.getMapVal(map, "endtime");
					
					String jobid = StrUtils.getMapVal(map, "jobid");
					
					String url = getPlatUrl() + "/subscribe?method=refresh&cntno="+cntno+"&subtype=CNT_Track"+ "&csno=" + csno+ "&starttime=" + starttime + "&endtime=" + endtime + "&srcsite=156yt";
					//System.out.println("submitYanTianQuery:"+url);
					String response = AppUtils.sendPost(url, "");
					if(!StrUtils.isNull(response)){
						if(response.indexOf("\"status\": 200") > 0 || response.indexOf("\"status\": 404") > 0){
							serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insertSql);
						}
					}
				}	
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
	
	public static void submitEPortSoQuery(String sono) {
		try {
			String qryFilter = "";
			String level = "0";
			if(!StrUtils.isNull(sono)){//按SO手工抓取
				qryFilter = "\nAND z.sono='" + StrUtils.getSqlFormat(sono) + "'";
				level = "20";
			}else{//定时任务按条件过滤需要查的数据
				qryFilter = "\n AND NOT EXISTS(SELECT 1 FROM plat_subscribe zz WHERE zz.nos = z.sono AND zz.substate = 'Upload' AND subtype = 'CNT_SO_GET' AND args = 'jobid=||x.jobid||')";
				qryFilter += "\n AND cls <= ((NOW() + '5DAY' )::DATE) AND cls >= ((NOW() + '-5DAY' )::DATE) ";
			}
			
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String sql = 
				"\nSELECT DISTINCT"+
				"\n cls,"+
				"\n(SELECT zz.nos FROM fina_jobs zz where zz.id = x.jobid and zz.isdelete = false and zz.parentid IS NULL) AS jobno,"+
				"\n	z.sono AS cntno , f_sys_config('CSNO') AS csno ," +
				"\n (cls::TIMESTAMP + '-5DAY')::DATE AS starttime ," +
				"\n (cls::TIMESTAMP + '+5DAY')::DATE AS endtime , " +
				"\n x.jobid , " +
				"\n(CASE WHEN EXISTS(SELECT 1 FROM dat_port xx where xx.id = x.polid AND xx.namee IN ('CHIWAN','MAWAN')) THEN 'CCT' ELSE 'SCT' END) AS args "+
				"\n FROM bus_shipping x , bus_ship_container z  "+
				"\n where x.isdelete = FALSE and z.parentid IS NULL"+
				"\n AND etd IS NOT NULL " +
				"\n AND x.polid = ANY(SELECT xz.id FROM dat_port xz where xz.namee IN ('SHEKOU','CHIWAN','MAWAN','DACHANWAN'))  "+
				"\n AND z.jobid = x.jobid and z.isdelete = false"+
				"\n and TRIM(z.sono) <> ''  AND z.sono is not null AND z.sono NOT LIKE '%;%' AND z.sono NOT LIKE '%,%' AND z.sono NOT LIKE '%/%' AND z.sono NOT LIKE '%、%'"+
				//"\n and COALESCE(z.cntno,'') = '' "+
				"\n AND EXISTS(SELECT 1 FROM fina_jobs zz where zz.id = x.jobid and zz.isdelete = false and zz.parentid IS NULL AND zz.impexp = 'E')   "+
				qryFilter +
				"\n	ORDER BY cls LIMIT 200"+
				"\n";
			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(lists != null && lists.size()>0){
				for (Map map : lists) {
					String cntno = StrUtils.getMapVal(map, "cntno");
					String args = StrUtils.getMapVal(map, "args");
					String jobid = StrUtils.getMapVal(map, "jobid");
					String insertSql = "INSERT INTO plat_subscribe(id,nos,subtype,substate,args) VALUES(getid_tmp(),'"+StrUtils.getSqlFormat(cntno)+"' , 'CNT_SO_GET' , 'Upload' , 'jobid="+jobid+"');";
					
					String csno = StrUtils.getMapVal(map, "csno");
					
					String starttime = StrUtils.getMapVal(map, "starttime");
					String endtime = StrUtils.getMapVal(map, "endtime");
					
					
					
					String url = getPlatUrl() + "/subscribe?method=refresh&cntno="+cntno+"&subtype=CNT_SO_GET"+ "&csno=" + csno + "&starttime=" + starttime + "&endtime=" + endtime + "&args=" + "jobid="+jobid + "&level="+level;
					//System.out.println("submitEPortSoQuery:"+url);
					String response = AppUtils.sendPost(url, "");
					if(!StrUtils.isNull(response)){
						if(response.indexOf("\"status\": 200") > 0 || response.indexOf("\"status\": 404") > 0){
							serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insertSql);
						}
					}
				}	
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
	
	/**
	 * 提交南沙SO抓取数据
	 * @param sono
	 */
	public static void submitNanShaSoQuery(String sono) {
		try {
			String qryFilter = "";
			String level = "0";
			if(!StrUtils.isNull(sono)){//按SO手工抓取
				qryFilter = "\nAND z.sono='" + StrUtils.getSqlFormat(sono) + "'";
				level = "20";
			}else{//定时任务按条件过滤需要查的数据
				qryFilter = "\n AND NOT EXISTS(SELECT 1 FROM plat_subscribe zz WHERE zz.nos = z.sono AND zz.substate = 'Upload' AND subtype = 'CNT_SO_GET' AND args = 'jobid=||x.jobid||')";
				qryFilter += "\n AND cls <= ((NOW() + '5DAY' )::DATE) AND cls >= ((NOW() + '-5DAY' )::DATE) ";
			}
			
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String sql = 
				"\nSELECT DISTINCT"+
				"\n cls,"+
				"\n(SELECT zz.nos FROM fina_jobs zz where zz.id = x.jobid and zz.isdelete = false and zz.parentid IS NULL) AS jobno,"+
				"\n	z.sono AS cntno , f_sys_config('CSNO') AS csno ," +
				"\n (cls::TIMESTAMP + '-5DAY')::DATE AS starttime ," +
				"\n (cls::TIMESTAMP + '+5DAY')::DATE AS endtime , " +
				"\n x.jobid , " +
				"\n '' AS args "+
				"\n FROM bus_shipping x , bus_ship_container z  "+
				"\n where x.isdelete = FALSE and z.parentid IS NULL"+
				"\n AND etd IS NOT NULL " +
				"\n AND x.polid = ANY(SELECT xz.id FROM dat_port xz where xz.namee IN ('NANSHA'))  "+
				"\n AND z.jobid = x.jobid and z.isdelete = false"+
				"\n and TRIM(z.sono) <> ''  AND z.sono is not null AND z.sono NOT LIKE '%;%' AND z.sono NOT LIKE '%,%' AND z.sono NOT LIKE '%/%' AND z.sono NOT LIKE '%、%'"+
				//"\n and COALESCE(z.cntno,'') = '' "+
				"\n AND EXISTS(SELECT 1 FROM fina_jobs zz where zz.id = x.jobid and zz.isdelete = false and zz.parentid IS NULL AND zz.impexp = 'E')   "+
				qryFilter +
				"\n	ORDER BY cls LIMIT 200"+
				"\n";
			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(lists != null && lists.size()>0){
				for (Map map : lists) {
					String cntno = StrUtils.getMapVal(map, "cntno");
					String args = StrUtils.getMapVal(map, "args");
					String jobid = StrUtils.getMapVal(map, "jobid");
					String insertSql = "INSERT INTO plat_subscribe(id,nos,subtype,substate,args) VALUES(getid_tmp(),'"+StrUtils.getSqlFormat(cntno)+"' , 'CNT_SO_GET' , 'Upload' , 'jobid="+jobid+"');";
					
					String csno = StrUtils.getMapVal(map, "csno");
					
					String starttime = StrUtils.getMapVal(map, "starttime");
					String endtime = StrUtils.getMapVal(map, "endtime");
					
					
					
					String url = getPlatUrl() + "/subscribe?method=refresh&cntno="+cntno+"&subtype=CNT_SO_GET"+ "&csno=" + csno + "&starttime=" + starttime + "&endtime=" + endtime + "&args=" + "jobid="+jobid + "&level="+level + "&srcsite=NanSha";
					//System.out.println("submitEPortSoQuery:"+url);
					String response = AppUtils.sendPost(url, "");
					if(!StrUtils.isNull(response)){
						if(response.indexOf("\"status\": 200") > 0 || response.indexOf("\"status\": 404") > 0){
							serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insertSql);
						}
					}
				}	
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
	
	
	public static void downEPortSoData(String sono) {
		try {
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String csno = CfgUtil.findSysCfgVal("CSNO");
			
			String url = getPlatUrl() + "/subscribe";
			String args = "method=getData&subtype=CNT_SO_GET&csno=" + csno;
			if(!StrUtils.isNull(sono)){
				args += "&sono=" + sono;
			}
			//System.out.println("downEPortSoData:"+url + "?" + args);
			String response = AppUtils.sendGet(url, args);
			if(!StrUtils.isNull(response)){
				if(response.startsWith("[")){
					JSONArray jsonArray = JSONArray.fromObject(response);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						String sql = jsonObject.getString("feedback");
						String id = jsonObject.getString("id");
						//System.out.println("downEPortSoData sql:" + sql);
						
						serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
						
						if(!StrUtils.isNull(sono)){
							
						}else{
							args = "method=respStatus&id=" + id;
							AppUtils.sendGet(url, args);//通知标记已执行...
//							String sqlInsert = 
//								"\nINSERT INTO sys_dml_pool( "+
//								"\nid, linkid, linktbl, srctype, dmlsql,inputtime)"+
//								"\nVALUES (getid_tmp(), 100, 'Robot', 'Uplat Robot', '"+StrUtils.getSqlFormat(sql)+"', NOW());"+
//								"";
//							serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sqlInsert);
						}
					}
					
				}
			}
			
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
}
