package com.scp.schedule;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.scp.dao.sys.SysUserDao;
import com.scp.util.AppUtils;

/**
 * 
 * 自动gps硬件信息获取
 * 思迈尔提供的供应商
 * 
 * 
 */
public class AuotoGetGps {

	private static boolean isRun = false;

	public void execute() throws Exception {
		if (isRun) {
			System.out.print("AuotoGetGps wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			running();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static String encodeByMD5(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	private void running() throws Exception {
		SysUserDao sysUserDao;
		try {
				String appkey = "a0e0082b-91f7-4aa0-8a23-7279da410661";
				String appsecret = "89a6d322-9bf8-468b-845f-b967ade0e580";
				Date date = new Date();
				SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String str = appsecret+"appkey"+appkey+"equipno-1"+"formatjson"+"methodGetVehcileInfo_ZHB"+"timestamp"+dateFormat.format(date).toString()+appsecret;
				String sign = encodeByMD5(str);

				String getGpsreturn = AppUtils.sendPost("http://api.e6yun.com/public/v3/Other/Call"
						, "method=GetVehcileInfo_ZHB&appkey="+appkey+"&timestamp="+dateFormat.format(date)+"&format=json&equipno=-1&sign="+sign.toUpperCase()+"");
				JSONObject getGpsreturnjson = JSONObject.fromObject(getGpsreturn);
				JSONObject json2 = JSONObject.fromObject(getGpsreturnjson.get("result"));
				
				if(getGpsreturnjson!=null && "1".equals(json2.get("code").toString())){//请求成功
					JSONArray jsonArray = (JSONArray) json2.get("data");
					for (int i = 0; i < jsonArray.size(); i++) {
						String sql = "SELECT f_bus_gps_creatbusgps_zhb('["+jsonArray.get(i)+"]');";
						AppUtils.getServiceContext().sysUserAssignMgrService.sysUserDao.executeQuery(sql);
					}
				}else{
				}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
	
	
	public static void main(String[] args) {
		AuotoGetGps auotoGetGps = new AuotoGetGps();
		try {
			auotoGetGps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}