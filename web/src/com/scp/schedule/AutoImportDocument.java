package com.scp.schedule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scp.util.*;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.scp.model.bus.BusAir;
import com.scp.model.bus.BusTrain;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusCustoms;
import com.scp.model.ship.BusShipping;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysCorporation;
import com.scp.service.ServiceContext;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * 自动importdocument
 */
public class AutoImportDocument {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        System.out.println("Autoimportdocument Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ Autoimportdocument wraning:another process is running!reTryCount:" + reTryCount);
            reTryCount++;
            if (reTryCount < 5) { //按2分钟一次，5次10分钟如果之前的还没响应，重新启动执行
                return;
            } else {
                reTryCount = 0;
            }
        }
        isRun = true;
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            importdocument(serviceContext);
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }

    public static void main(String[] args) {
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIocJunit("serviceContext");
            importdocument(serviceContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void importdocument(ServiceContext serviceContext) {
        StringBuffer logsb = new StringBuffer();
        try {
            logsb.append("AutoImportDocument开始");
            String url0 = "http://apistage.huilianyi.com/gateway/oauth/token?grant_type=client_credentials&scope=write";
            String url1 = "http://apistage.huilianyi.com/gateway/e-archives/api/open/v1/attachment/upload";
            String url2 = "http://apistage.huilianyi.com/gateway/e-archives/api/open/v1/import/document";


            //获取token
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("cache-control", "no-cache");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
            headers.put("client_id", "823700-80179");
            headers.put("client_secret", "973e867994dea4c03c212f11ebe7193d5e88c2eb");
            headers.put("grant_type", "client_credentials");
            headers.put("scope", "write");
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            String Authorization = "Basic " + Base64.encode("b0b9c092-dedb-4c8e-93bd-be910d746cc3:ZjE3NTYxZmYtMzM5YS00YjM4LTg5NTEtMWRlYzA1NGE5NDA1").replace("\r\n", "");
            headers.put("Authorization", Authorization);
            String result0 = "";
            String access_token = "";
            try {
                result0 = HttpClientUtil.requestOCRForHttp(url0, new HashMap<String, String>(), headers);
                access_token = JSONObject.fromObject(result0).getString("access_token");
            } catch (Exception e) {
                result0 = OkHttpUtil.OkHttppost(url0, new HashMap<String, String>(), headers);
                access_token = JSONObject.fromObject(result0).getString("access_token");
            }
            logsb.append(",result0").append(result0);

            String querySql = "select *\n" +
                    "from\n" +
                    "(\n" +
                    "\tselect fj.id as jobid, fj.jobdate ,sa.*\n" +
                    "\tfrom fina_jobs fj \n" +
                    "\tinner join sys_attachment sa on fj.id = sa.linkid\n" +
                    "\twhere fj.isdelete = false and sa.isdelete = FALSE \n" +
                    "\n" +
                    "\tunion\n" +
                    "\tselect fj.id as jobid, fj.jobdate ,sa.*\n" +
                    "\tfrom fina_jobs fj \n" +
                    "\tinner join bpm_processinstance bp on fj.id ::text = bp.refid\n" +
                    "\tinner join sys_attachment sa on bp.id = sa.linkid \n" +
                    "\twhere fj.isdelete = false and sa.isdelete = FALSE \n" +
                    "\n" +
                    "\tunion\n" +
                    "\tselect fj.id as jobid, fj.jobdate ,sa.*\n" +
                    "\tfrom fina_jobs fj \n" +
                    "\tinner join bus_ship_book_cnt bsbc on fj.id = bsbc.jobid\n" +
                    "\tinner join bus_ship_booking bsb on bsbc.linkid = bsb.id \n" +
                    "\tinner join sys_attachment sa on bsb.id = sa.linkid \n" +
                    "\twhere fj.isdelete = false and sa.isdelete = FALSE and bsb.isdelete = FALSE \n" +
                    "\n" +
                    "\tunion\n" +
                    "\tselect fj.id as jobid, fj.jobdate ,sa.*\n" +
                    "\tfrom fina_jobs fj \n" +
                    "\tinner join bus_order bo on fj.id = bo.jobid\n" +
                    "\tinner join sys_attachment sa on bo.id = sa.linkid \n" +
                    "\twhere fj.isdelete = false and sa.isdelete = FALSE and bo.isdelete = FALSE \n" +
                    "\n" +
                    "\tunion\n" +
                    "\tselect fj.id as jobid, fj.jobdate ,sa.*\n" +
                    "\tfrom fina_jobs fj \n" +
                    "\tinner join sys_attachment sa on (concat(fj.id,100)::BIGINT) = sa.linkid\n" +
                    "\twhere fj.isdelete = false and sa.isdelete = FALSE \n" +
                    ") aaa\n" +
                    "where jobdate between '2022-11-01' and '2022-12-31' and isimportdocument = false";
            List<Map> sysAttachmentMapList = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql);
            if (!sysAttachmentMapList.isEmpty()) {
                logsb.append(",sysAttachmentMapList长度为").append(sysAttachmentMapList.size());
                for (int i = 0; i < sysAttachmentMapList.size(); i++) { // 循环发送需要自动发送的邮件
                    Map sysAttachmentMap = sysAttachmentMapList.get(i);
                    //上传文件
                    String sysAttachmenturl = AppUtils.getAttachFilePath() + sysAttachmentMap.get("id") + sysAttachmentMap.get("filename");
                    Map<String, File> files = new HashMap<String, File>();
                    File file = new File(sysAttachmenturl);
                    files.put("x." + file.getName(), file);
                    String result1 = upLoadFilePost(url1, files, access_token);
                    logsb.append("\r\n\r\n\r\n\r\n,当前为").append(i).append(",sysAttachment").append(JSON.toJSONString(sysAttachmentMap)).append(",result1").append(result1);
                    if (result1 == null) {
                        logsb.append(",文件为空");
                        continue;
                    }
                    String attachmentOID = JSONObject.fromObject(result1).getJSONObject("result").getString("attachmentOID");


                    SysCorporation sysCorporation = null;
                    String businesstype = "";
                    BusShipping busShipping = null;
                    BusAir busAir = null;
                    BusTruck busTruck = null;
                    BusCustoms busCustoms = null;
                    BusTrain busTrain = null;
                    FinaJobs finaJobs = serviceContext.jobsMgrService.finaJobsDao.findById((Long) sysAttachmentMap.get("linkid"));
                    if (finaJobs != null) {
                        sysCorporation = serviceContext.userMgrService.sysCorporationDao.findById(finaJobs.getCustomerid());
                        if ("S".equals(finaJobs.getJobtype())) {
                            businesstype = "海运";
                            List<BusShipping> busShippingList = serviceContext.busShippingMgrService.busShippingDao.findAllByClauseWhere("jobid=" + finaJobs.getId());
                            busShipping = busShippingList.get(0);
                        }
                        if ("A".equals(finaJobs.getJobtype())) {
                            businesstype = "空运";
                            List<BusAir> busAirList = serviceContext.busAirMgrService.busAirDao.findAllByClauseWhere("jobid=" + finaJobs.getId());
                            busAir = busAirList.get(0);
                        }
                        if ("L".equals(finaJobs.getJobtype())) {
                            businesstype = "陆运";
                            List<BusTruck> busTruckList = serviceContext.busTruckMgrService.busTruckDao.findAllByClauseWhere("jobid=" + finaJobs.getId());
                            busTruck = busTruckList.get(0);
                        }
                        if ("C".equals(finaJobs.getJobtype())) {
                            businesstype = "报关";
                            List<BusCustoms> busCustomsList = serviceContext.busCustomsMgrService.busCustomsDao.findAllByClauseWhere("jobid=" + finaJobs.getId());
                            busCustoms = busCustomsList.get(0);
                        }
                        if ("H".equals(finaJobs.getJobtype())) {
                            businesstype = "铁运";
                            List<BusTrain> busTrainList = serviceContext.busTrainMgrService.busTrainDao.findAllByClauseWhere("jobid=" + finaJobs.getId());
                            busTrain = busTrainList.get(0);
                        }
                    }

                    if (!StrUtils.isNull(businesstype)) {
                        String datatypename = "";
                        String profiletypesgroupQuerySql = "SELECT * from data_profiletypesgroup;";
                        List<Map> profiletypesgroupMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(profiletypesgroupQuerySql);
                        String attachmenttypesql = "SELECT * FROM sys_role where id = " + sysAttachmentMap.get("roleid");
                        Map attachmenttypeMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachmenttypesql);
                        for (Map map0 : profiletypesgroupMap) {
                            if (map0.get("businesstype").toString().contains(businesstype) && attachmenttypeMap.get("name").equals(map0.get("attachmenttype"))) {
                                datatypename = String.valueOf(map0.get("datatypename"));
                                break;
                            }
                        }

                        if (!StrUtils.isNull(datatypename)) {
                            String profiletypeconfigurationQuerySql = "SELECT * from data_profiletypeconfiguration where datatypename = '" + datatypename + "';";
                            List<Map> profiletypeconfigurationMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(profiletypeconfigurationQuerySql);
                            if (profiletypeconfigurationMap != null && profiletypeconfigurationMap.size() > 0) {
                                StringBuffer thisjsonsb = new StringBuffer();
                                thisjsonsb.append("[\n");
                                thisjsonsb.append(" {\n");
                                thisjsonsb.append("    \"particularYear\":\"" + sysAttachmentMap.get("jobdate").toString().split("-")[0] + "\",\n");
                                thisjsonsb.append("    \"originalNumber\":\"" + sysAttachmentMap.get("id") + "\",\n");
                                thisjsonsb.append("    \"primaryField\":\"" + "UFMS_" + sysAttachmentMap.get("id") + "\",\n");
                                thisjsonsb.append("    \"isPaper\":\"false\",\n");
                                thisjsonsb.append("    \"documentSource\":\"UFMS\",\n");
                                thisjsonsb.append("    \"attachmentList\":[\n");
                                thisjsonsb.append("        {\n");
                                thisjsonsb.append("            \"attachmentOID\":\"" + attachmentOID + "\"\n");
                                thisjsonsb.append("        }\n");
                                thisjsonsb.append("    ],\n");

                                thisjsonsb.append("    \"companyCode\":\"1931\",\n");     //公司代码
                                thisjsonsb.append("    \"documentTypeCode\":\"" + profiletypeconfigurationMap.get(0).get("filetypecode") + "\",\n");  //资料类型编码
                                thisjsonsb.append("    \"fieldValueList\": [\n");
                                for (Map thiscustomfieldcodeMap : profiletypeconfigurationMap) {
                                    thisjsonsb.append("        {\n");
                                    thisjsonsb.append("            \"fieldCode\": \"" + thiscustomfieldcodeMap.get("customfieldcode") + "\",\n");
                                    if ("BASE_IS_PAPER".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        thisjsonsb.append("            \"value\":\"false\"\n");
                                    } else if ("GZBH".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        thisjsonsb.append("            \"value\":\"" + finaJobs.getNos() + "\"\n");   //工作编号
                                    } else if ("YWLX".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        thisjsonsb.append("            \"value\":\"" + businesstype + "\"\n");    //业务类型
                                    } else if ("TDH".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        if ("S".equals(finaJobs.getJobtype())) {
                                            thisjsonsb.append("            \"value\":\"" + busShipping.getMblno() + "\"\n");  //mbl编号
                                        } else if ("H".equals(finaJobs.getJobtype())) {
                                            thisjsonsb.append("            \"value\":\"" + busTrain.getMblno() + "\"\n");  //mbl编号
                                        } else {
                                            thisjsonsb.append("            \"value\":\"" + "" + "\"\n");  //mbl编号
                                        }
                                    } else if ("WTDWMC".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        thisjsonsb.append("            \"value\":\"" + sysCorporation.getNamec() + "\"\n");   //委托人
                                    } else if ("WTRQ".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        thisjsonsb.append("            \"value\":\"" + new SimpleDateFormat("yyyy-MM-dd").format(finaJobs.getJobdate()) + "\"\n");
                                    } else if ("WTRQ".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        thisjsonsb.append("            \"value\":\"" + finaJobs.getSales() + "\"\n");
                                    } else if ("SBQJ".equals(thiscustomfieldcodeMap.get("customfieldcode"))) {
                                        Calendar calendar = Calendar.getInstance(); //上报月份
                                        int SBQJ = calendar.get(Calendar.MONTH) + 1;
                                        thisjsonsb.append("            \"value\":\"" + SBQJ + "\"\n");
                                    } else {
                                        thisjsonsb.append("            \"value\":\"" + "" + "\"\n");
                                    }
                                    thisjsonsb.append("        },\n");
                                }
                                thisjsonsb = new StringBuffer(thisjsonsb.substring(0, thisjsonsb.length() - 2));
                                thisjsonsb.append("    ]\n");
                                thisjsonsb.append("}\n");
                                thisjsonsb.append("]\n");
                                logsb.append(",thisjson为").append(thisjsonsb.toString());


                                headers.put("Content-Type", "application/json;charset=utf-8");
                                headers.put("Authorization", "Bearer " + access_token);
                                String result2 = httpsRequest(headers, url2, "POST", thisjsonsb.toString());
                                logsb.append(",result2为").append(result2);
                                String statusCode = JSONObject.fromObject(result2).getString("statusCode");
                                if ("0000".equals(statusCode)) {
                                    String sql2 = "UPDATE sys_attachment SET isimportdocument = TRUE WHERE id=" + sysAttachmentMap.get("id") + ";";
                                    serviceContext.sysAttachmentService.sysAttachmentDao.executeSQL(sql2);
                                }
                            }
                        }
                    }
                }
            }
            logsb.append("\r\n,同步附件数据结束。");
            System.out.println("同步附件数据结束syso");
        } catch (Exception e) {
            logsb.append(",AutoImportDocument importdocument报错,e为").append(e.getMessage());
            e.printStackTrace();
        }
        LogBean.insertLog(logsb);
    }


    public static String upLoadFilePost(String actionUrl, Map<String, File> files, String access_token) {
        try {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String PREFIX = "--", LINEND = "\r\n";
            String MULTIPART_FROM_DATA = "multipart/form-data";
            String CHARSET = "UTF-8";
            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);
            conn.setRequestMethod("POST"); // Post方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
            conn.setRequestProperty("Authorization", "Bearer " + access_token);


            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            // 发送文件数据
            if (files != null)
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());
                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();

            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                InputStream in = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(in);
                BufferedReader bufReader = new BufferedReader(isReader);
                String line = "";
                String data = "";
                while ((line = bufReader.readLine()) != null) {
                    data += line;
                }
                outStream.close();
                conn.disconnect();
                return data;
            }
            outStream.close();
            conn.disconnect();
        } catch (Exception e) {

        }
        return null;
    }

    public static String httpsRequest(Map<String, String> headers, String urlNameString, String requestMethod, String outputStr) throws IOException {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(requestMethod);

            connection.setConnectTimeout(500000);
            connection.setReadTimeout(500000);
            // 建立实际的连接
            connection.connect();

            if (!StrUtils.isNull(outputStr)) {
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                out.append(outputStr);
                out.flush();
                out.close();
            }

            int code = connection.getResponseCode();
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
        } catch (Exception e) {
            throw new RuntimeException(new StringBuffer().append("httpsRequest失败,失败原因为").append(e.getMessage()).toString());
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return result;
    }

    public static String httpsRequest2(Map<String, String> headers, String urlNameString, String requestMethod, String outputStr) throws IOException {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(urlNameString);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法


            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }

            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(outputStr);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}