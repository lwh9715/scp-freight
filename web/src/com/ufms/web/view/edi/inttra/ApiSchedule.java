package com.ufms.web.view.edi.inttra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.web.BaseServlet;
import com.ufms.web.view.sysmgr.LogBean;


@WebServlet("/edi/inttra/api")
public class ApiSchedule extends BaseServlet {

    @Action(method = "getSchedule")
    public String getSchedule(HttpServletRequest request) {
        String returnText = "";
        try {
            String accessToken = getAuth();
            Map<String, String> headers = initHeadMap();
            headers.put("Authorization", accessToken);

            String originPort = request.getParameter("polcode");//
            String destinationPort = request.getParameter("podcode"); //
            String searchDate = request.getParameter("etd");//
            String searchDateType = request.getParameter("searchDateType");//
            String weeksOut = request.getParameter("weeksOut");//
            String scacs = request.getParameter("shipcarrier");//
            String directOnly = request.getParameter("directOnly");//
            String includeNearbyOriginPorts = request.getParameter("includeNearbyOriginPorts");//
            String includeNearbyDestinationPorts = request.getParameter("includeNearbyDestinationPorts");//

            Map<String, String> args = new LinkedHashMap<String, String>();
            args.put("originPort", originPort);
            args.put("destinationPort", destinationPort);
            args.put("searchDate", searchDate);
            args.put("searchDateType", searchDateType);
            args.put("weeksOut", weeksOut);
            if (!StrUtils.isNull(scacs) && !"null".equals(scacs)) {
                args.put("scacs", scacs);
            }
            args.put("directOnly", directOnly);
            args.put("includeNearbyOriginPorts", includeNearbyOriginPorts);
            args.put("includeNearbyDestinationPorts", includeNearbyDestinationPorts);

            String url = "https://api.inttra.com/oceanschedules/schedule";
            String urlArgs = map2UrlArgs(args);
            returnText = httpsRequest(headers, url + urlArgs, "GET", null);
        } catch (Exception e) {
            returnText = "船期查询getSchedule失败,失败原因为" + e.getMessage();
            LogBean.insertLog(new StringBuffer().append("船期查询getSchedule失败,失败原因为").append(e.getMessage()));
        }
        return returnText;
    }

    @Action(method = "getAuth")
    public String getAuth() {
        String accessToken = null;
        try {
            Map<String, String> headers = initHeadMap();
            String url = "https://api.inttra.com/auth";
            String res = httpsRequest(headers, url, "GET", null);
            Gson gson = new Gson();
            Map<String, Object> ret = new HashMap<String, Object>();
            ret = gson.fromJson(res, ret.getClass());
            accessToken = ret.get("access_token").toString();
        } catch (Exception e) {
            throw new RuntimeException(new StringBuffer().append("船期查询getAuth失败,失败原因为").append(e.getMessage()).toString());
        }
        return accessToken;
    }


    private Map initHeadMap() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "no-cache");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
        headers.put("client_id", "823700-80179");
        headers.put("client_secret", "973e867994dea4c03c212f11ebe7193d5e88c2eb");
        headers.put("grant_type", "client_credentials");
        headers.put("Content-Type", "application/json;charset=utf-8");
        return headers;
    }

    private String map2UrlArgs(Map<String, String> args) {
        String urlArgs = "?";
        for (String key : args.keySet()) {
            urlArgs += key + "=" + args.get(key) + "&";
        }
        urlArgs = urlArgs.substring(0, urlArgs.length() - 1);
        return urlArgs;
    }

    /**
     * 发送https请求
     *
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(Map<String, String> headers, String urlNameString, String requestMethod, String outputStr) throws IOException {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(requestMethod);

            //创建SSL对象
            TrustManager[] tm = {new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

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
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(500000);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            //for (String key : map.keySet()) {
            //}

            int code = connection.getResponseCode();
            // 定义 BufferedReader输入流来读取URL的响应
            if (200 == code) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } else {
                InputStream errorStream = connection.getErrorStream();
                in = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"));
            }

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            result = result.replaceAll("Ü", "U");
        } catch (Exception e) {
            throw new RuntimeException(new StringBuffer().append("船期查询httpsRequest失败,失败原因为").append(e.getMessage()).toString());
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return result;
    }

}
