package com.scp.model.edi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;



/**
 * 
 */

/**   
 *    
 * 项目名称：p4fjy   
 * 类名称：Test   
 * 类描述：   
 * 创建人：lyc   
 * 创建时间：2020-10-23 上午11:18:17   
 * @version    
 *    
 */
public class PostUtil {
	public static void main(String[] args) throws Exception {
		String url = "http://api.nbeport.com/router/rest";

		String user_id = "SZZS1608";
		String api_scrt = "d05993f3-e352-47db-961e-9c096cc8a99b";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = sdf.format(new Date());
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("user_id", user_id);
		map.put("timestamp", timestamp);
		map.put("method", "eportyun.packlist.upload.clp");
		map.put("format", "json");
		map.put("version", "2.0");
		map.put("data", "[{\"billNo\":\"IAlIN30256\",\"vesselName\":\"VIRGO\",\"voyageNo\":\"V.005W\",\"lineCode\":\"\",\"loadPort\":\"NINGBO\",\"loadPortCode\":\"CNNGB\",\"deliveryPlace\":\"MOMBASA,KENYA\",\"deliveryPlaceCode\":\"KEMBA\",\"destinationPort\":\"MOMBASA,KENYA\",\"destinationPortCode\":\"KEMBA\",\"receiptPlace\":\"\",\"transitPort\":\"KEMWA\",\"clearingCode\":\"SYWL\",\"carrierCode\":\"HPL赫伯罗特\",\"carrier\":\"HPL赫伯罗特\",\"ctnOperatorCode\":\"\",\"forwarderContacts\":\"MYT\",\"forwarderTel\":\"0574-89089857\",\"forwarderName\":\"SYWL\",\"email\":\"myt@sinaocean.com\",\"consignmentNo\":\"SYWL2056185\",\"ctnType\":\"40HC\",\"costcoNo\":\"99LUCPXHHYB_SYWL20561851\",\"firstForwarderName\":\"SYWL\",\"contactEmail\":\"\",\"dataSource\":\"4\",\"bookingOrgName\":\"宁波申洋物流有限公司\",\"clientCode\":\"\",\"carrierType\":\"\",\"cargoType\":\"\",\"place\":\"\",\"ctnLoadRequire\":\"\",\"ctnLoadTime\":null,\"ctnLoadContact\":\"\",\"ctnLoadTel\":\"\",\"ctnRemark\":\"\",\"customerName\":\"ZY\",\"customerTel\":\"0574-87750896\",\"customerEmail\":\"zhuyan@sinaocean.com\",\"documentName\":\"\",\"documentTel\":\"\",\"documentEmail\":\"\",\"closingDate\":\"2020-10-29 20:00:00\",\"cutOffTime\":null,\"isShutOut\":\"N\",\"pickYardName\":\"\",\"dockName\":\"BLCT3\",\"unCode\":\"\",\"tradeFlag\":\"W\",\"fleetName\":\"\",\"contractNo\":\"\",\"contractCustomer\":\"\",\"etd\":\"2020/10/31 00:00:00\",\"bookingOrgCode\":\"HPL赫伯罗特\",\"busClientCode\":\"\",\"yardClosingTime\":null,\"amsClosingTime\":null,\"jobNo\":\"SYWL2056185\",\"sendType\":\"00\",\"vesselNameBig\":\"\",\"voyageNoBig\":\"\",\"billNoBig\":\"HLCUNG12010CKWQ8\",\"cargoList\":[{\"otherBillNo\":\"HLCSIA30256\",\"packageType\":\"CTNS\",\"packageTypeCode\":\"CT\",\"qty\":\"1160\",\"grossWeight\":\"8800\",\"volume\":\"68\",\"cargoName\":\"CHILD SHOES\\r\\nLADIES SHOES\\r\\nBAG\\r\\nHS:850164\",\"remark\":\"N/M\"}]}]");
		StringBuffer sb = new StringBuffer();
		for (String key:map.keySet()) {
			sb.append(key+"="+map.get(key)+"&");
		}
		sb.append("key="+api_scrt);
		String sign = encodeMD5(sb.toString());
		map.put("sign", sign);
		System.out.println(sb.toString());
		System.out.println(sign);
		try {
			String result = doPost(url, map);
			System.out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String doPost(String url, Map<String, String> params)
			throws IOException {
		return doPost(url, params, null, "utf-8", 60000,
				60000);
	}
	public static String doPost(String url, Map<String, String> params,
			Map<String, String> cookies, String charset, int connectTimeout,
			int readTimeout) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		String query = buildQuery(params, charset);
		byte[] content = {};
		if (query != null) {
			content = query.getBytes(charset);
		}
		return doPost(url, ctype, charset, content, cookies, connectTimeout,
				readTimeout);
	}
	public final static String encodeMD5(String s) {
	      try {
	          byte[] btInput = s.getBytes("utf-8");
	          MessageDigest mdInst = MessageDigest.getInstance("MD5");
	          mdInst.update(btInput);
	          byte[] md = mdInst.digest();
	          StringBuffer sb = new StringBuffer();
	          for (int i = 0; i < md.length; i++) {
	              int val = ((int) md[i]) & 0xff;
	              if (val < 16){
	                  sb.append("0");
	              }
	              sb.append(Integer.toHexString(val));
	          }
	          return sb.toString();
	      } catch (Exception e) {
	          return null;
	      }
	  }
	public static String buildQuery(Map<String, String> params, String charset)
			throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuffer query = new StringBuffer();
		// Set entries = params.entrySet();
		boolean hasParam = false;

		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (StringUtils.isNotEmpty(name)) {
				if (value == null) {
					value = "";
				}
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=").append(
						URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}
	
	public static String doPost(String url, String ctype, String charset,
			byte[] content, Map<String, String> cookies, int connectTimeout,
			int readTimeout) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				conn = getConnection(new URL(url), cookies, "POST", ctype);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				throw e;
			}
			try {

				out = conn.getOutputStream();
				out.write(content);
				rsp = getResponseAsString(conn, charset);
			} catch (IOException e) {
				throw e;
			}

		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}
	
	public static HttpURLConnection getConnection(URL url,
			Map<String, String> cookies, String method, String ctype)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (cookies != null) {
			for (String key : cookies.keySet()) {
				String value = cookies.get(key);

				conn.addRequestProperty(key, value);
			}
			String redirect = cookies.get("URL_REDIRECT");
			if ("0".equals(redirect)) {
				conn.setInstanceFollowRedirects(false);
			}
		}
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);

		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		if (ctype != null) {
			conn.setRequestProperty("Content-Type", ctype);
		}
		return conn;
	}
	
	public static String getResponseAsString(HttpURLConnection conn,
			String charset) throws IOException {
		charset = getResponseCharset(conn.getContentType(), charset);
		InputStream es = conn.getErrorStream();

		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (StringUtils.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":"
						+ conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}
	
	private static String getResponseCharset(String ctype, String charset) {
		if (charset == null || charset.equals("")) {
			charset = "utf-8";
		}

		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}
	
	private static String getStreamAsString(InputStream stream, String charset)
			throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
}
