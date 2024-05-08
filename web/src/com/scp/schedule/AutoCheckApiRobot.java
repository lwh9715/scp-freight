package com.scp.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.scp.dao.api.ApiRobotBookDao;
import com.scp.dao.api.ApiRobotEsiDao;
import com.scp.model.api.ApiRobotBook;
import com.scp.model.api.ApiRobotEsi;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.ufms.base.utils.HttpUtil;

public class AutoCheckApiRobot {
	private static boolean isRun = false;

	public void execute() throws Exception {
		// if(AppUtils.isDebug)return;
		// AppUtils.debug("AutoFixBugJob Start:" + new Date());
		if (isRun) {
			System.out.print("AutoCheckApiRobot wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			checkBook();
			checkEsi();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}
	

	private static void checkBook() throws Exception {
		ApiRobotBookDao apiRobotBookDao = (ApiRobotBookDao)ApplicationUtilBase.getBeanFromSpringIoc("apiRobotBookDao");
		try {
			List<ApiRobotBook> list = apiRobotBookDao.findAllByClauseWhere("uuid is not null AND isdelete = false");
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					long id = list.get(i).getId();
					String  usercode = list.get(i).getInputer();
					SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
                    int time=list.get(i).getStartime().compareTo(date);
                    if( time<=0||list.get(i).getStartime()==null){
					    String json="{\"jobID\": "+list.get(i).getUuid()+"}";
					    String url = ConfigUtils.findRobotCfgVal("server_url")+"/cargoService/respBooking";
					    String res = HttpUtil.post(url, json);
						System.out.println(res);
						Gson gson = new Gson();
						Map<String, Object> ret = new HashMap<String, Object>();
						ret= gson.fromJson(res, ret.getClass());
						String result=ret.get("result").toString();
						String status=ret.get("msg").toString();
						if(!result.isEmpty()){
							String insertSql = "update api_robot_book set response='" + ret
							+ "',status='" + status + "' where id='" + id + "'";
							apiRobotBookDao.executeSQL(insertSql);	
						}else{
							continue;
						}	
                    }
				}
			}
		} catch (Exception e1) {
			isRun = false;
			return;
		}
	}
		
	private static void checkEsi() throws Exception {
		ApiRobotEsiDao apiRobotEsiDao = (ApiRobotEsiDao)ApplicationUtilBase.getBeanFromSpringIoc("apiRobotEsiDao");
		try {
			List<ApiRobotEsi> list = apiRobotEsiDao.findAllByClauseWhere("uuid is not null AND isdelete = false AND COALESCE(response,'') NOT LIKE '%Draft has been successfully saved.%' AND COALESCE(response,'') NOT LIKE '%SI was saved.%' ");
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					long id = list.get(i).getId();
					String jobId = list.get(i).getUuid();
					String status=null;
					String result=null;
					if(jobId==null){
						continue;
					}
					String json="{\"jobID\":\""+jobId+"\"}";
				    String url = ConfigUtils.findRobotCfgVal("server_url")+"/cargoService/respSI";
				    String res = HttpUtil.post(url, json);
					System.out.println(res);
					Gson gson = new Gson();
					Map<String, Object> ret = new HashMap<String, Object>();
					ret= gson.fromJson(res, ret.getClass());
					try{
						result=ret.get("result").toString();
						status=ret.get("msg").toString();
					}catch(Exception e){	
						return;
					}
					if(!result.isEmpty()){
						String insertSql = "update api_robot_esi set response='" + StrUtils.getSqlFormat(ret.toString())
						+ "',status='" + StrUtils.getSqlFormat(status) + "' where id='" + id + "'";
						apiRobotEsiDao.executeSQL(insertSql);	
					}else{
						continue;
					}
				}
				
				}
		}catch (Exception e1) {
			isRun = false;
			return;
		}
	}

}
