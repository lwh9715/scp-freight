package com.scp.view.module.api.vgm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

public class ApiVgmTools {
	
	
	public static String postVgm(String vgmid , String userid){
		String resp = "";
		

		//正式地址
		String secret = "K8UJRTIKCM14VNKS2SZ2BNJ1Q9K89I5H";
		String keyId = "cb3d818a50044f26bd24de3057182e06";
		String url = "https://open.eline56.com/services/openapi/vgm/send?keyId="+keyId+"&secret="+secret;
		
		String param = "";
		
		ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
		
		
		String sql = "SELECT x.* FROM api_vgmdtl x WHERE x.vgmid = " + vgmid + ";";
		List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		for (Map map : lists) {
			String dtlid = StrUtils.getMapVal(map, "id");
			
			String querySql = "SELECT f_api_vgm_getinfo('vgmdtlid="+dtlid+";userid="+userid+"') AS vgminfo;";
			map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			
			String argsJson = StrUtils.getMapVal(map, "vgminfo");
			
			System.out.println("url:"+url);
			
			argsJson = "[" + argsJson + "]";
			
			System.out.println("param:"+argsJson);
			
			Map<String, String> headers = new HashMap<String, String>();
			
			resp = httpsRequest(headers , url, "POST" , argsJson);
			System.out.println("resp:"+resp);
			
			
			try {
				querySql = "SELECT f_api_vgm('vgmdtlid="+dtlid+";userid="+userid+";type=postresp;resp="+resp+"');";
				System.out.println("resp SQL:"+querySql);
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		return resp;
	}
	
	
	public static String postVgmTest(String vgmid , String userid){
		String resp = "";
		
		//测试地址
		String secret = "43BKJ83VEJAC7ZWCHP7CPLTELOMP9SO3";
		String keyId = "83244cf07a30458f84d97111e5e1e061";
		String url = "http://open.test.eline56.com:8868/services/openapi/vgm/send?keyId="+keyId+"&secret="+secret;
		
		
		String param = "";
		
		ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
		
		
		String sql = "SELECT x.* FROM api_vgmdtl x WHERE x.vgmid = " + vgmid + ";";
		List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		for (Map map : lists) {
			String dtlid = StrUtils.getMapVal(map, "id");
			
			String querySql = "SELECT f_api_vgm_getinfo('vgmdtlid="+dtlid+";userid="+userid+"') AS vgminfo;";
			Map mapInfo = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			
			String argsJson = StrUtils.getMapVal(mapInfo, "vgminfo");
			
			argsJson = "[" + argsJson + "]";
			
			System.out.println("param:"+argsJson);
			resp = sendPost(url , "POST" ,argsJson);
			
			System.out.println("resp:"+resp);
			
			try {
				querySql = "SELECT f_api_vgm('vgmdtlid="+dtlid+";userid="+userid+";type=postresp;resp="+resp+"');";
				System.out.println("resp SQL:"+querySql);
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resp;
	}
	
	
	public static String getVgmStatus(String vgmid , String userid){
		String resp = "";
		

		//正式地址
		String secret = "K8UJRTIKCM14VNKS2SZ2BNJ1Q9K89I5H";
		String keyId = "cb3d818a50044f26bd24de3057182e06";
		String url = "https://open.eline56.com/services/openapi/vgm/send?keyId="+keyId+"&secret="+secret;
		
		String param = "";
		
		ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
		
		String querySql = "SELECT d.sono AS masterbillno,d.containerno,d.id AS vgmdtlid FROM api_vgm v , api_vgmdtl d WHERE v.id = d.vgmid AND v.id = "+vgmid +";";
		List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql);
		
		for (Map map : lists) {
			String cntno = StrUtils.getMapVal(map, "containerno");
			String masterbillno = StrUtils.getMapVal(map, "masterbillno");
			url = "https://open.eline56.com/services/openapi/query?keyId="+keyId+"&secret="+secret+"&masterBillNo="+masterbillno+"&containerNo="+cntno+"&businessCode=10086";
			
			Map<String, String> headers = new HashMap<String, String>();
			
			resp = httpsRequest(headers , url, "GET" , null);
			System.out.println("resp:"+resp);
			
			String vgmdtlid = StrUtils.getMapVal(map, "vgmdtlid");
			
			try {
				querySql = "SELECT f_api_vgm('vgmdtlid="+vgmdtlid+";userid="+userid+";type=getresp;resp="+resp+"');";
				System.out.println("resp SQL:"+querySql);
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//System.out.println("param:"+argsJson);
		
		
		//System.out.println("resp:"+resp);
		return resp;
	}
	
	public static String getVgmStatusTest(String vgmid , String userid){
		String resp = "";
		
		//测试地址
		String secret = "43BKJ83VEJAC7ZWCHP7CPLTELOMP9SO3";
		String keyId = "83244cf07a30458f84d97111e5e1e061";
		String url = "http://open.test.eline56.com:8868/services/openapi/query?keyId="+keyId+"&secret="+secret+"&masterBillNo=xxxx&containerNo=xxxx&businessCode=10086";
		
		
		String param = "";
		
		ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
		
		String querySql = "SELECT d.sono AS masterbillno,d.containerno,d.id AS vgmdtlid FROM api_vgm v , api_vgmdtl d WHERE v.id = d.vgmid AND v.id = "+vgmid +";";
		List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql);
		
		for (Map map : lists) {
			String cntno = StrUtils.getMapVal(map, "containerno");
			String masterbillno = StrUtils.getMapVal(map, "masterbillno");
			url = "http://open.test.eline56.com:8868/services/openapi/query?keyId="+keyId+"&secret="+secret+"&masterBillNo="+masterbillno+"&containerNo="+cntno+"&businessCode=10086";
			resp = sendPost(url ,"GET", null);
			System.out.println("resp:"+resp);
			String vgmdtlid = StrUtils.getMapVal(map, "vgmdtlid");
			try {
				querySql = "SELECT f_api_vgm('vgmdtlid="+vgmdtlid+";userid="+userid+";type=getresp;resp="+resp+"');";
				System.out.println("resp SQL:"+querySql);
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//System.out.println("param:"+argsJson);
		
		
		//System.out.println("resp:"+resp);
		return resp;
	}
	
	
	
	/**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url,String type, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            
            conn.setRequestMethod(type);
            
            // 设置通用的请求属性
            conn.setRequestProperty("Accept", "application/json");
            
            //conn.setRequestProperty("Authorization", getAuthorization());
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            
            //设置超时
            conn.setConnectTimeout(500000);
            conn.setReadTimeout(500000);
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            if(!StrUtils.isNull(param)){
            	 // 获取URLConnection对象对应的输出流
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(param.toString());
                wr.flush();
            }
            
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
    
    
    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(Map<String, String> headers , String requestUrl, String requestMethod,String param) {
    	String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = requestUrl ;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpsURLConnection connection = (HttpsURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			
			connection.setDoOutput(true);
			connection.setDoInput(true);    
			connection.setUseCaches(false);  
			connection.setRequestMethod(requestMethod);
			
			// 设置通用的请求属性
			connection.setRequestProperty("Accept", "application/json");
            
            //conn.setRequestProperty("Authorization", getAuthorization());
			connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
			
			 //创建SSL对象
            TrustManager[] tm = {new X509TrustManager(){
				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
            }};
            //SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            connection.setSSLSocketFactory(ssf);
            
			
			//设置超时
			connection.setConnectTimeout(5000000);
			connection.setReadTimeout(5000000);
			
			if(!StrUtils.isNull(param)){
				// 获取URLConnection对象对应的输出流
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
	            if (param != null) {
	            	out.write(param.getBytes("UTF-8")); 
	            }
	            out.flush(); 
	        	out.close(); 
			}
            
			// 获取所有响应头字段
			//Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			//for (String key : map.keySet()) {
				//System.out.println(key + "--->" + map.get(key));
			//}
			int code = connection.getResponseCode();
			//System.out.println("connection.getResponseCode:"+code); 
			//System.out.println("connection.getResponseMessage:"+connection.getResponseMessage()); 
			// 定义 BufferedReader输入流来读取URL的响应
			if(200==code){
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}else{
				InputStream errorStream = connection.getErrorStream();
				in = new BufferedReader(new InputStreamReader(errorStream));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				System.out.println("错误返回码"+code);
				System.out.println(result);
			}
			//
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
    }
	
	
	public static void main(String[] args){
		//postVgmTest("48922080000","100");
		
		//getVgmStatusTest("48922080000","100");
		getVgmStatus("48922080000","100");
	}
}
