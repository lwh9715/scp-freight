package com.scp.schedule;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

/**
 * 
 * 自动gps硬件信息获取
 * 思迈尔提供的供应商
 * 
 * 
 */
public class AuotoGetGpsTrack {

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
		try {
			DaoIbatisTemplate daoIbatisTemplate;
			try {
				daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			} catch (Exception e1) {
				e1.printStackTrace();
				//System.out.println(e1.getLocalizedMessage());
				isRun = false;
				return;
			}
			
			String querySql = "SELECT DISTINCT gpsidno FROM bus_gps_ref x WHERE status = 'R' ORDER BY gpsidno;";
			List<Map> lists = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			for (Map map : lists) {
				String gpsno = StrUtils.getMapVal(map, "gpsidno");
				//System.out.println("gpsno:"+gpsno);
				String getGpsreturn = getGpsInfo(gpsno);
				JSONObject getGpsreturnjson = JSONObject.fromObject(getGpsreturn);
				JSONObject json2 = JSONObject.fromObject(getGpsreturnjson.get("result"));
				
				if(getGpsreturnjson!=null && "1".equals(json2.get("code").toString())){//请求成功
					JSONArray jsonArray = (JSONArray) json2.get("data");
					for (int i = 0; i < jsonArray.size(); i++) {
						String data = jsonArray.get(i).toString();
						data = StrUtils.getSqlFormat(data);
						String sql = "\nSELECT f_bus_gps_creatbusgps_zhb('"+gpsno+"','["+data+"]');";
						//System.out.println(sql);
						daoIbatisTemplate.queryWithUserDefineSql(sql);
					}
				}else{
					System.out.println("发送post获取追货宝轨迹信息失败:-->"+getGpsreturn);
				}
				Thread.sleep(400l);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			isRun = false;
			return;
		}
	}
	
	
	
	public String getGpsInfo(String equipno) throws Exception {
		String appkey = "a0e0082b-91f7-4aa0-8a23-7279da410661";
		String appsecret = "89a6d322-9bf8-468b-845f-b967ade0e580";
		Date date = new Date();
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -30);
        Date date1 = now.getTime();//?起始日期
		
		//Date date2 = sdf.parse("2020-06-28 19:20:00"); 
		Date date2 = Calendar.getInstance().getTime(); 
		
		String str = appsecret+"appkey"+appkey+"btime"+dateFormat.format(date1).toString()+"equipno"+equipno+"etime"+dateFormat.format(date2).toString()+"formatjson"+"isoffsetlonlat0isplacename0methodGetTrackDetail_ZHB"+"timestamp"+dateFormat.format(date).toString()+appsecret;
		String sign = encodeByMD5(str);

		String getGpsreturn = AppUtils.sendPost("http://api.e6yun.com/public/v3/Other/Call"
				, "method=GetTrackDetail_ZHB&appkey="+appkey+"&timestamp="+dateFormat.format(date)+"&format=json&equipno="+equipno+"&btime="+dateFormat.format(date1)+"&etime="+dateFormat.format(date2)+"&isoffsetlonlat=0&isplacename=0&sign="+sign.toUpperCase()+"");
		String URL = "http://api.e6yun.com/public/v3/Other/Call?method=GetTrackDetail_ZHB&appkey="+appkey+"&timestamp="+dateFormat.format(date)+"&format=json&equipno=-1&btime="+dateFormat.format(date1)+"&etime="+dateFormat.format(date2)+"&isoffsetlonlat=0&isplacename=0&sign="+sign.toUpperCase()+"";
		
		return getGpsreturn;
	}

	public static void main(String[] args) {
		AuotoGetGpsTrack auotoGetGps = new AuotoGetGpsTrack();
		try {
			String gpsno = "863014531878636";
			String getGpsreturn = auotoGetGps.getGpsInfo(gpsno);
			System.out.println(getGpsreturn);
			auotoGetGps.running();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}