package com.scp.view.module.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.scp.view.module.api.robot.ApiEncryptUtil;
import com.scp.view.module.api.robot.ApiEncryptUtil.EncryptionException;

public class ApiTools{
	
	
	
	public static String getApiUrl(){
		return ConfigUtils.findSysCfgVal("sys_cosco_server_url");
	}

	public static String getApiKey(String carrier , Long userid){
		String apiKey = ConfigUtils.findUserSysCfgVal(carrier+"_apikey",userid);
		if(!StrUtils.isNull(apiKey)){
			try {
				ApiEncryptUtil apiEncryptUtil = new ApiEncryptUtil();
				apiKey = apiEncryptUtil.encrypt(apiKey);
				apiKey = URLEncoder.encode(apiKey,"utf-8");
			} catch (EncryptionException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return apiKey;
	}
	
	public static void main(String[] args) throws EncryptionException, UnsupportedEncodingException {
		String key = "f6952850-f5b8-11eb-a6b3-910cc425467a";
		ApiEncryptUtil apiEncryptUtil = new ApiEncryptUtil();
		String apiKey = apiEncryptUtil.encrypt(key);
		System.out.println(apiKey);
		System.out.println(URLEncoder.encode(apiKey,"utf-8"));
		System.out.println(apiEncryptUtil.decrypt(URLDecoder.decode(URLEncoder.encode(apiKey,"utf-8"),"utf-8")));
	}
}
