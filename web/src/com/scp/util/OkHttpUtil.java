package com.scp.util;

import com.scp.service.ServiceContext;
import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class OkHttpUtil {

    private static final OkHttpClient httpClient = new OkHttpClient();
    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    //测试环境
    // private static final String KEY = "qXpmgvQb6G2ZE2eNxKcfwWUYgfIa";
    // private static final String Secret = "KWiwMclTaJcCtK9UEDnzcaQ1TNsa";
    // private static final String TOKEN_URL = "https://api-cn-t.lenovo.com/uat/token";
    // private static final String SVN_URL = "https://api-cn-t.lenovo.com/uat/v1.0/supplychain/loc_shipment/receive/iftsta";

    //正式环境
    private static final String KEY = "POZ17VoZttnmA1mGYkvrTBSOoC8a";
    private static final String Secret = "icxi_Dg7FFoz3wpzqRTg0HryG34a";
    private static final String TOKEN_URL = "https://api-cn.lenovo.com/token";
    private static final String SVN_URL = "https://api-cn.lenovo.com/v1.0/supplychain/loc_shipment/receive/iftsta";

    public static void postlenovo(StringBuffer stringBuffer, ServiceContext serviceContext, Map<String, String> argsMap, Map<String, String> returnStrMap) throws IOException {
        String urlArgs2 = AppUtils.map2Url(argsMap, ";");
        String sqlQry = "SELECT f_zhongji_Lenovo_sync_jobs_trace('" + urlArgs2 + "') AS tx_returntext";
        stringBuffer.append(",sqlQry为").append("nbsp").append(sqlQry);
        Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
        String returnStr = String.valueOf(m.get("tx_returntext"));
        stringBuffer.append("nbsp,returnStr为").append("nbsp").append(returnStr.replaceAll("\\r","").replaceAll("\\n","").replaceAll(" ",""));

        if (StrUtils.isNull(returnStr)) {
            stringBuffer.append(",returnStr为空");
        } else {
            String[] returnStrArray = returnStr.split("nbsp");
            for (int i = 0; i < returnStrArray.length; i++) {
                String thisreturnStr = returnStrArray[i];

                stringBuffer.append("nbsp,returnStr有值");
                String Authorization = "Bearer " + getToken();
                stringBuffer.append(",Authorization为").append(Authorization);
                RequestBody body = RequestBody.create(mediaType, thisreturnStr);
                Request request = new Request.Builder()
                        .url(SVN_URL)
                        .addHeader("Authorization", Authorization)
                        .addHeader("content-type", "application/json")
                        .addHeader("Accept", "application/json")
                        .post(body)
                        .build();
                Response response = httpClient.newCall(request).execute();
                if (response.code() == 401) System.out.println("Token expired:401 ");
                else if (response.code() >= 400 && response.code() < 500) System.out.println("Request Exception: " + response.code());
                else if (response.code() >= 500) System.out.println("Internal Exception: " + response.code());
                else if (!response.isSuccessful()) throw new IOException("Something bad happens...");
                String res = response.body().string();
                stringBuffer.append(",res为").append(res);

                returnStrMap.put("returnStr", thisreturnStr);
            }
        }
    }

    private static String getToken() throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();
        String Authorization = "Basic " + getBasicAuth();
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", Authorization.replaceAll("\r|\n", ""))
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        String res = response.body().string();
        String token = JSONObject.fromObject(res).get("access_token").toString();
        return token;
    }


    public static String getBasicAuth() throws UnsupportedEncodingException {
        final String text = KEY + ":" + Secret;
        String encodedText = Base64.encode(text);
        encodedText = encodedText.replaceAll("nbsp", "");
        return encodedText;
    }


    public static String OkHttppost(String url, Map<String, String> requestParams, Map<String, String> headers) throws Exception {
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cache-control", headers.get("cache-control"))
                .addHeader("User-Agent", headers.get("User-Agent"))
                .addHeader("client_id", headers.get("client_id"))
                .addHeader("client_secret", headers.get("client_secret"))
                .addHeader("grant_type", headers.get("grant_type"))
                .addHeader("scope", headers.get("scope"))
                .addHeader("Content-Type", headers.get("Content-Type"))
                .addHeader("Authorization", headers.get("Authorization"))
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        String res = response.body().string();
        return res;
    }
}
