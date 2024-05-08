package com.scp.view.module.insurance;

import java.util.Map;

import net.sf.json.JSONObject;

import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.ufms.base.utils.HttpUtil;

/**
 *
 */
public class InsuranceUtils {
	
	private static String url;
	private static String client_id;
	private static String client_secret;
	
	public static void main(String[] args) {
//		String token = getToken();
//		System.out.println(token);
		
//		String userId = getUserId("12345678","深圳航迅科技","neo@ufms.cn","18818526948");
//		System.out.println(userId);
		
		JSONObject jsonObject = getOrderInfo("20201106484997563691");
		System.out.println(jsonObject);
	}
	
	public static void init(){
    	try {
			if(StrUtils.isNull(url))url = ConfigUtils.findSysCfgVal("sys_insurance_apiurl");
			if(StrUtils.isNull(client_id))client_id = ConfigUtils.findSysCfgVal("sys_insurance_client_id");
			if(StrUtils.isNull(client_secret))client_secret = ConfigUtils.findSysCfgVal("sys_insurance_client_secret");
		} catch (Exception e) {
	    	url = "https://test.linghangbao.com";
			client_id = "test";
	    	client_secret = "9f38562a37d1cd0457daf057deaaec33";
		}
	}

	public static String getToken(){
		init();
    	String method = "/Api/Thirdpart/token";
    	String token = "";
		try {
			JSONObject json = new JSONObject();
			json.put("client_id", client_id);
			json.put("client_secret", client_secret);
			String buf = String.valueOf(json);
			
			String response = HttpUtil.httpsRequest(url + method, "GET",buf);
			System.out.println(response);
			
			JSONObject jsonRespnse = JSONObject.fromObject(response);
			token = jsonRespnse.getJSONObject("data").getString("token");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token;
	}
	
	public static String getUserId(String creditNo , String company , String email , String mobile){
		init();
    	String method = "/Api/Thirdpart/grant";
    	String userId = "";
		try {
			JSONObject json = new JSONObject();
			json.put("client_id", client_id);
			
			String token = getToken();
			System.out.println(token);
			
			json.put("token", token);
			
			JSONObject contract = new JSONObject();
			
			contract.put("creditNo", creditNo);
			contract.put("company", company);
			contract.put("email", email);
			contract.put("mobile", mobile);
			
			json.put("contract", contract);
			String buf = String.valueOf(json);
			
			System.out.println(buf);
			
			String response = HttpUtil.httpsRequest(url + method, "GET",buf);
			System.out.println(response);
			
			JSONObject jsonRespnse = JSONObject.fromObject(response);
			userId = jsonRespnse.getJSONObject("data").getString("userId");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userId;
	}
	
	public static void sendInsurance(Long id){
		init();
    	String method = "/Api/Thirdpart/toubaoInfo";
		try {
			String sql = "SELECT * FROM f_edi_insurance('jobid="+ id + ";token="+getToken()+"') AS insurancetion";
			Map m =AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String insurancetion = m.get("insurancetion").toString();
			//System.out.println("insurancetion--->" +insurancetion);
			String response = HttpUtil.httpsRequest(url + method, "POST",insurancetion);
			//System.out.println(response);
			JSONObject jsonRespnse = JSONObject.fromObject(response);
			String url = jsonRespnse.getJSONObject("data").getString("url");
			//System.out.println("url-->"+url);
			Browser.execClientScript("getinsuranceurl('"+url+"');");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 返回json 值
	 *  status(data内)	保单状态
		policy_no	保单号
		baofei	原币保费
		baofei_currency	保费币种
		baofei_rmb	人民币保费
		suretime	承保时间
		companyName	保险公司简称
		url	电子保单下载链接
	 */
	public static JSONObject getOrderInfo(String keywords){
		init();
		JSONObject jsonRespnse = null;
    	String method = "/Api/Thirdpart/orderInfo";
    	try {
			JSONObject json = new JSONObject();
			json.put("client_id", client_id);
			
			String token = getToken();
			json.put("token", token);
			
			JSONObject contract = new JSONObject();
			contract.put("searchType", 2);
			contract.put("keywords", keywords);//第三方订单号
			
			json.put("contract", contract);
			String buf = String.valueOf(json);
			
			System.out.println(buf);
			
			String response = HttpUtil.httpsRequest(url + method, "GET",buf);
			System.out.println(response);
			
			jsonRespnse = JSONObject.fromObject(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonRespnse;
	}
	
	
}
