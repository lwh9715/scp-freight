package com.scp.view.module.api.freightower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

import net.sf.json.JSONObject;

import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.base.db.DaoUtil;
import com.ufms.base.utils.HttpUtil;
//import com.ufms.web.view.edi.plat.ApiPlatUtil;

public class ApiFreighTowerTools {

	private static String TOKEN;

	private static Calendar TOKEN_GET_TIME;

	private static boolean isTokenInvalid(){
		if(TOKEN_GET_TIME == null){
			TOKEN_GET_TIME=Calendar.getInstance();
			return false;
		}
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, -24);
		if(now.compareTo(TOKEN_GET_TIME)>0){
			return true;
		}else{
			return false;
		}
	}

	public static String getToken(){

		if(isTokenInvalid()){
			return TOKEN;
		}

		String token = "";

		String username = "zjsc";
		String password = "123456";

//    	String username = ApiPlatUtil.findConfigVal("FTWR_username");
//    	String password = ApiPlatUtil.findConfigVal("FTWR_password");



		String para = "username="+username+"&password="+password;

		String url = "http://api.freightower.com/auth/token";

		String resp = HttpUtil.sendGet(url, para);
		//System.out.println("resp:"+resp);

		JSONObject jsonObject = JSONObject.fromObject(resp);
		int statusCode = jsonObject.getInt("statusCode");
		String content = jsonObject.getString("content");
		if(statusCode == 20000){
			TOKEN_GET_TIME = Calendar.getInstance();
			token = content;
			TOKEN = token;
		}
		//System.out.println("token:"+token);


		return token;
	}

	public static String getToken2(){
		if(isTokenInvalid()){
			return TOKEN;
		}

		String token = "";

		String username = "zjsc";
		String password = "123456";

//    	String username = ApiPlatUtil.findConfigVal("FTWR_username");
//    	String password = ApiPlatUtil.findConfigVal("FTWR_password");

		String url = "http://openapi.freightower.com/auth/api/token";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("password", password);
		String para = jsonObject.toString();
		String resp = HttpUtil.sendPost(url, para);

		JSONObject jsonObject2 = JSONObject.fromObject(resp);
		int statusCode = jsonObject2.getInt("statusCode");
		JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
		String access_token = jsonObject3.getString("access_token");
		if(statusCode == 20000){
			TOKEN_GET_TIME = Calendar.getInstance();
			token = access_token;
			TOKEN = token;
		}
		return token;
	}

	public static String getAuthorization(){
		String authorization = "";
		String token = getToken();

		authorization = "Bearer" + " " + token;
		//System.out.println("authorization:"+authorization);
		return authorization;
	}

	public static String postOceanTrack(String billNo , String carrierCode){
		String resp = "";

		String url = "http://api.freightower.com/trace/ocean";

		String param = "";

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("billNo", billNo);
		jsonObject.put("carrierCode",carrierCode);

		jsonObject.put("containerNo", "");
		jsonObject.put("portCode", "");
		jsonObject.put("isExport", "E");

		param = jsonObject.toString();
		param = "["+param+"]";

		//System.out.println("param:"+param);
		resp = sendPost(url , param);

		//System.out.println("resp:"+resp);
		return resp;
	}


	public static void postAndSave(String sono , String carrierCode){
		String resp = "";
		try {
//			resp = ApiPlatUtil.postPlatFTWRTrack(sono, carrierCode);
			//System.out.println("1.postPlatFTWRTrack 提交到平台:......"+resp);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		resp = ApiFreighTowerTools.postOceanTrack(sono, carrierCode);

		ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");

		String ediId = "";
		try {
			String querySql = "SELECT id FROM edi_inttra_report b WHERE sono ='"+sono+"' AND editype = 'FTWR' LIMIT 1;";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			ediId = StrUtils.getMapVal(map, "id");
		} catch (NoRowException e) {
			Long id = DaoUtil.getPkIdTmp();
			ediId = id.toString();
		}
		if(StrUtils.isNull(ediId)){
			return;
		}

		String insUpgSql =
				"\nINSERT INTO edi_inttra_report(id) SELECT "+ediId+" FROM _virtual WHERE NOT EXISTS (SELECT 1 FROM edi_inttra_report WHERE id = "+ediId+");"+
						"\nUPDATE edi_inttra_report SET editype='FTWR',sono='"+StrUtils.getSqlFormat(sono)+"',filename=NULL,editext='"+StrUtils.getSqlFormat(resp)+"',inputtime=NOW() WHERE id = "+ediId+";";

		insUpgSql += "\nSELECT f_edi_inttra_phrase_process('" + ediId + "');";
		serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(insUpgSql);


		// OkktUtils.doSometing(serviceContext,sono,carrierCode,resp);
	}


	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

			conn.setRequestMethod("POST");

			// 设置通用的请求属性
			conn.setRequestProperty("Accept", "application/json");

			conn.setRequestProperty("Authorization", getAuthorization());
			conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");

			//设置超时
			conn.setConnectTimeout(500000);
			conn.setReadTimeout(500000);

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(param.toString());
			wr.flush();


			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public static void main(String[] args){
		postOceanTrack("2682699242","OOCL");
	}
}
