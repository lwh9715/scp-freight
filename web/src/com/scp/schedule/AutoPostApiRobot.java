package com.scp.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.scp.dao.api.ApiRobotBookDao;
import com.scp.dao.api.ApiRobotEsiDao;
import com.scp.model.api.ApiRobotBook;
import com.scp.model.api.ApiRobotEsi;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.StrUtils;
import com.scp.view.module.api.robot.ApiEncryptUtil;
import com.ufms.base.utils.HttpUtil;

public class AutoPostApiRobot {
	private static boolean isRun = false;

	public void execute() throws Exception {
		// if(AppUtils.isDebug)return;
		// AppUtils.debug("AutoFixBugJob Start:" + new Date());
		if (isRun) {
			System.out.print("AutoPostApiRobot wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			submitBook();
			submitEsi();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}

	private void submitBook() throws Exception {
		String sql = "";
		String stauts = "";
		ApiRobotBookDao apiRobotBookDao = (ApiRobotBookDao)ApplicationUtilBase.getBeanFromSpringIoc("apiRobotBookDao");
		try {
			List<ApiRobotBook> list = apiRobotBookDao.findAllByClauseWhere("uuid is null AND isdelete = false");
			
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
                    int time=list.get(i).getStartime().compareTo(date);
					if( time<=0||list.get(i).getStartime()==null){
						long id = list.get(i).getId();
						String json = list.get(i).getJsonpost();
						JSONObject jsonObject = JSONObject.fromObject(json);
						ApiEncryptUtil apiEncryptUtil = new ApiEncryptUtil();
						String carriercode =list.get(i).getCarriercode();
						String usercode=list.get(i).getInputer();
						if(StrUtils.isNull(carriercode)){
							continue;
						}else{
							
							if("COSCO".equalsIgnoreCase(carriercode)){
								jsonObject.put("authName", apiEncryptUtil.encrypt(ConfigUtils.findRobotCfgVal("cosoc_bk_user",usercode)));
								String pwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("cosoc_bk_pwd",usercode));
								jsonObject.put("loginPin", apiEncryptUtil.encrypt(pwd));
								jsonObject.put("notifyMail", ConfigUtils.findRobotCfgVal("cosoc_bk_email",usercode));
							}else if("HPL".equalsIgnoreCase(carriercode)){
								jsonObject.put("authName", apiEncryptUtil.encrypt(ConfigUtils.findRobotCfgVal("hpl_bk_user",usercode)));
								String pwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("hpl_bk_pwd",usercode));
								jsonObject.put("loginPin", apiEncryptUtil.encrypt(pwd));
								jsonObject.put("notifyMail", ConfigUtils.findRobotCfgVal("hpl_bk_email",usercode));
							}else if("COSCO-E".equalsIgnoreCase(carriercode)){
								String pwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("cosoc_ele_pwd",usercode));
								jsonObject.put("authName", apiEncryptUtil.encrypt(ConfigUtils.findRobotCfgVal("cosoc_ele_user",usercode)));
								jsonObject.put("loginPin", apiEncryptUtil.encrypt(pwd));
								jsonObject.put("notifyMail", ConfigUtils.findRobotCfgVal("cosoc_ele_email",usercode));
								String paypwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("cosoc_ele_paypwd",usercode));
								jsonObject.put("payPin", apiEncryptUtil.encrypt(paypwd));
							}
							jsonObject.put("carrierCode", carriercode);
							String url = ConfigUtils.findRobotCfgVal("server_url",usercode);
							String  jsondate=null;
							Gson gson = new Gson();
							Map<String, Object> jsonAll = new HashMap<String, Object>();
							jsonAll= gson.fromJson(json, jsonAll.getClass());
							if(jsonAll.get("nMax").toString().equals("1")){
							    url=url+"/cargoService/repBooking";
								jsondate="["+jsonObject.toString()+"]";
							}else{
								url=url+"/cargoService/reqOneBooking";
								jsondate=jsonObject.toString();
							}
							String res = HttpUtil.post(url, jsondate);
							System.out.println("AutoPostApiRobot submitBook res:"+res);
							Map<String, Object> ret = new HashMap<String, Object>();
							ret= gson.fromJson(res, ret.getClass());
							String result=ret.get("result").toString();
							String status=ret.get("msg").toString();
							if(result.isEmpty()){
								continue;
							}else{
								String insertSql = "update api_robot_book set uuid = '" + result +"' where id=" + id + ";";
								apiRobotBookDao.executeSQL(insertSql);
							}
					}
					
						
					}
				}
			}
		} catch (Exception e1) {
			isRun = false;
			return;
		}
		// sql = "SELECT f_auto_fix('type=0');";
		// sysUserDao.executeQuery(sql);
	}
	
	
	private void submitEsi() throws Exception {
		String sql = "";
		String stauts = "";
		ApiRobotEsiDao apiRobotEsiDao = (ApiRobotEsiDao)ApplicationUtilBase.getBeanFromSpringIoc("apiRobotEsiDao");
		try {
			List<ApiRobotEsi> list = apiRobotEsiDao.findAllByClauseWhere("uuid is null AND isdelete = false");
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ApiRobotEsi apiRobotEsi = list.get(i);
					String usercode=apiRobotEsi.getInputer();
					String json = apiRobotEsi.getJsonpost();
					JSONObject jsonObject = JSONObject.fromObject(json);
					String url = ConfigUtils.findRobotCfgVal("server_url");
					url=url+"/cargoService/reqSI";
					String result=null;
					String jsondate=null;
					
					jsondate = jsonObject.toString();
					Gson gson = new Gson();
					
					String res = HttpUtil.post(url,jsondate);
					System.out.println("AutoPostApiRobot submitEsi res:"+res);
					Map<String, Object> ret = new HashMap<String, Object>();
					ret= gson.fromJson(res, ret.getClass());
					try{
						result=ret.get("result").toString();
						if(!result.isEmpty()){
							String insertSql = "update api_robot_esi set uuid = '" + result +"' where id=" + apiRobotEsi.getId() + ";";
							apiRobotEsiDao.executeSQL(insertSql);	
						}
					}catch(Exception e){
						isRun = false;
						return;
					}
				}
			}
		}catch (Exception e1) {
			isRun = false;
			return;
		}
	}
}
