package com.scp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ListIterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 快递100查询接口
 * https://www.kuaidi100.com/openapi/api_post.shtml
 */
public class KuaiDi100QueryAPI {
	//官方示例URL说明:
	//id:商户申请的key
	//com:快递公司代码
	//nu:快递单号
	//valicode:废弃
	//show:返回格式0.json 1.xml 2.html 3.text
	//muti:行数0 1行 1 多行  默认多行
	//order: 按照时间排序
	//http://api.kuaidi100.com/api?id=[]&com=[]&nu=[]&valicode=[]&show=[0|1|2|3]&muti=[0|1]&order=[desc|asc]
	
	private final String key = "210f8cd3ddfbdfdf";
	
	
	public static void main(String[] args) {
		
		KuaiDi100QueryAPI kuaidi = new KuaiDi100QueryAPI();
		try {
			//System.out.println(kuaidi.queryByJson("yuantong", "885228812036969877"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String queryByJson(String code,String number) throws Exception{
		
		StringBuilder url = new StringBuilder();
		//官网找的
		url.append("http://www.kuaidi100.com/query?type=");
		url.append(code);
		url.append("&postid=");
		url.append(number);
		url.append("&id=1&valicode=&temp=");
		url.append(Math.random());
		
		//正式API
//		url.append("http://api.kuaidi100.com/api?id=");
//		url.append(key);
//		url.append("&com=");
//		url.append(code);
//		url.append("&nu=");
//		url.append(number);
//		url.append("&valicode=&show=0&muti=1&order=desc");
		
		StringBuilder sbmsg = new StringBuilder();
		JSONObject jobj = JSONObject.fromObject(sendPost(url.toString()));
		if("200".equals(jobj.getString("status"))){
			if(jobj.containsKey("Reason")){
				sbmsg.append(jobj.getString("Reason"));
			}else{
				JSONArray array = jobj.getJSONArray("data");
				ListIterator it = array.listIterator();
				while(it.hasNext()){
					JSONObject j = JSONObject.fromObject(it.next());
					sbmsg.append(j.getString("time"));
					sbmsg.append("\n");
					sbmsg.append("\t");
					sbmsg.append(j.getString("context"));
					sbmsg.append("\n");
				}
			}
		}else{
			sbmsg.append(jobj.getString("message"));
		}
		return sbmsg.toString();
	}
	
	public String sendPost(String url){
		 OutputStreamWriter out = null;
	        BufferedReader in = null;        
	        StringBuilder result = new StringBuilder(); 
	        try {
	            URL realUrl = new URL(url);
	            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            conn.connect();
	            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	            out.flush();
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result.append(line);
	            }
	        } catch (Exception e) {            
	            e.printStackTrace();
	        }
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result.toString();
	}
}

