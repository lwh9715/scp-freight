package com.scp.util;

import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.ufms.base.db.DaoUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtUtils {

    public static void main(String[] args) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIocJunit("serviceContext");
            // getLenovoTraceData(new StringBuffer(), serviceContext, "GOSUGZH0176701", resp);
            // updateReturncnttimeData("GOSUGZH0176701", serviceContext);
            // doSometing(serviceContext, "GOSUGZH0176701", "MSC", "");
            // doSometing(serviceContext, "GOSUGZH0176701","", resp);
            // doSometing(serviceContext, "44445555", "", resp);
            // doSometing(StringBuffer,serviceContext, "GOSUGZH0176701", "", resp);
        } catch (Exception e) {
            System.out.println(1);
        }
    }


    public static void handleFt(StringBuffer stringBuffer, String billNo, String containerNo, String carrierCode) {
        try {
            stringBuffer.append("nbspnbspnbsp,handleFt开始");
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            carrierCode = conversioncarrierCode(stringBuffer, serviceContext, billNo, containerNo, carrierCode);
            if ((!StrUtils.isNull(billNo) || !StrUtils.isNull(containerNo)) && !StrUtils.isNull(carrierCode)) {
                String resp = FtUtils.postOceanTrack(stringBuffer, billNo, containerNo, carrierCode);
                resp = FtUtils.updateresp(stringBuffer, serviceContext, billNo, carrierCode, resp);
                boolean isrepetition = FtUtils.edi_inttra(stringBuffer, serviceContext, billNo, containerNo, carrierCode, resp);

                String statusCode = JSONObject.fromObject(resp).getString("statusCode");
                if ("20000".equals(statusCode) || "40000".equals(statusCode)) {
                    stringBuffer.append("nbsp,doSometing开始");
                    if (!isrepetition) {
                        FtUtils.processVesvoySync(stringBuffer, serviceContext, billNo, resp);
                        FtUtils.updateReturncnttimeData(stringBuffer, serviceContext, billNo);
                        FtUtils.project44(stringBuffer, serviceContext, billNo, resp);
                    }
                    FtUtils.getLenovoTraceData(stringBuffer, serviceContext, billNo, resp);
                }
            } else {
                stringBuffer.append("nbspnbspnbsp,参数缺失");
                stringBuffer.append("，billNo为").append(billNo);
                stringBuffer.append("，containerNo为").append(containerNo);
                stringBuffer.append("，carriercode为").append(carrierCode);
            }
        } catch (Exception e) {
            stringBuffer.append(",handleFt异常，e为").append(e.getMessage());
        }
    }

    private static String conversioncarrierCode(StringBuffer stringBuffer, ServiceContext serviceContext, String billNo, String containerNo, String carrierCode) {
        stringBuffer.append("，carriercode0为").append(carrierCode);
        try {
            String querySql = "SELECT namee  FROM api_data x WHERE isdelete =false and x.srctype = 'FTWR' AND maptype = 'CARRIER' and (code='" + carrierCode + "' or namee='" + carrierCode + "')";
            Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            carrierCode = StrUtils.getMapVal(map, "namee");
        } catch (Exception e) {
            carrierCode = "";
        }
        stringBuffer.append("，carriercode1为").append(carrierCode);
        return carrierCode;
    }

    public static String postOceanTrack(StringBuffer stringBuffer, String billNo, String containerNo, String carrierCode) {
        String resp = "";
        try {
            stringBuffer.append("，billNo为").append(billNo);
            stringBuffer.append("，containerNo为").append(containerNo);
            stringBuffer.append("，carriercode为").append(carrierCode);

            String url = "http://openapi.freightower.com/application/v1/query";

            JSONObject jsonObject = new JSONObject();
            if (!StrUtils.isNull(billNo) && !"".equals(billNo)) {
                jsonObject.put("billNo", billNo);
            }
            if (!StrUtils.isNull(containerNo) && !"".equals(containerNo)) {
                jsonObject.put("containerNo", containerNo);
            }
            if (!StrUtils.isNull(carrierCode) && !"".equals(carrierCode)) {
                jsonObject.put("carrierCode", carrierCode);
            }
            jsonObject.put("portCode", "");
            jsonObject.put("isExport", "");

            String param = jsonObject.toString();
            String token = FtUtils.getAuthorization();
            resp = FtUtils.sendPost(url, param, token);
            resp = StrUtils.getSqlFormat(resp);
            stringBuffer.append("，token为").append(token);
            stringBuffer.append("，resp为").append(resp);
        } catch (Exception e) {
            stringBuffer.append("，postOceanTrack，e为").append(e.getMessage());
        }
        return resp;
    }

    //处理单号格式有误的问题
    private static String updateresp(StringBuffer stringBuffer, ServiceContext serviceContext, String oldbillNo, String carriercode, String resp) {
        try {
            stringBuffer.append("nbsp,updateresp开始");
            if ("YML".equals(carriercode) && resp.contains("单号格式有误")) {
                stringBuffer.append("，重新查询YML");
                String realbillNo = oldbillNo.substring(0, 10);
                stringBuffer.append("，realbillNo为").append(realbillNo);
                stringBuffer.append("，carriercode为").append(carriercode);
                resp = FtUtils.postOceanTrack(stringBuffer, realbillNo, "", carriercode);
            }
        } catch (Exception e) {
            stringBuffer.append("，updateresp，e为").append(e.getMessage());
        }
        stringBuffer.append(",updateresp结束");
        return resp;
    }

    public static boolean edi_inttra(StringBuffer stringBuffer, ServiceContext serviceContext, String billNo, String containerNo, String carrierCode, String resp) {
        boolean isrepetition = false;
        String ediId = "";
        try {
            String querySql = "SELECT id FROM edi_inttra_report b WHERE sono ='" + billNo + "' AND editype = 'FTWR' LIMIT 1;";
            Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            ediId = StrUtils.getMapVal(map, "id");

            String insUpgSql0 = "select * from edi_inttra_report WHERE id = " + ediId + " LIMIT 1;";
            Map map2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(insUpgSql0);
            String editext = String.valueOf(map2.get("editext"));
            if (StrUtils.getSqlFormat(editext).equals(resp)) {
                stringBuffer.append("nbsp,当前resp重复");
                isrepetition = true;
            } else {
                stringBuffer.append("nbsp,当前resp不重复");
            }
        } catch (NoRowException e) {
            Long id = DaoUtil.getPkIdTmp();
            ediId = id.toString();
        }


        String insUpgSql1 = "INSERT INTO edi_inttra_report(id) SELECT " + ediId + " FROM _virtual WHERE NOT EXISTS (SELECT 1 FROM edi_inttra_report WHERE id = " + ediId + ");";
        String insUpgSql2 = "UPDATE edi_inttra_report SET editype='FTWR',sono='" + StrUtils.getSqlFormat(billNo) + "',filename=NULL,editext='" + resp + "',inputtime=NOW() WHERE id = " + ediId + ";";
        String insUpgSql3 = "SELECT f_edi_inttra_phrase_process('" + ediId + "');";

        stringBuffer.append("nbsp,insUpgSql1为").append(insUpgSql1);
        // stringBuffer.append("nbsp,insUpgSql2为").append(insUpgSql2);
        stringBuffer.append("nbsp,insUpgSq3为").append(insUpgSql3);

        serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insUpgSql1);
        serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insUpgSql2);
        serviceContext.daoIbatisTemplate.updateWithUserDefineSql(insUpgSql3);
        return isrepetition;
    }

    private static void processVesvoySync(StringBuffer stringBuffer, ServiceContext serviceContext, String billNo, String resp) {
        try {
            stringBuffer.append("nbsp，processVesvoySync开始");

            String sqlQry = "SELECT id,jobid  FROM bus_ship_booking WHERE isdelete=false and  sono = '" + billNo + "' limit 1;";
            Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
            String booking = StrUtils.getMapVal(m, "id");
            String jobid = StrUtils.getMapVal(m, "jobid");

            String sqlQry1 = "SELECT *  FROM f_vesvoy_busshipping_sync_jobs('type=0;busshipbookid=" + booking + ";jobid=0') AS sqlqry1";
            stringBuffer.append("，sqlQry1为" + sqlQry1);
            Map m1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry1);
            String returntext = StrUtils.getMapVal(m1, "sqlqry1");
            if (StrUtils.isNull(returntext)) {
                stringBuffer.append("，returntext为空");
            } else {
                stringBuffer.append("，returntext不为空,returntext为" + returntext);
            }
            stringBuffer.append("，processVesvoySync结束");

            //触发修改后必须更正业务日期
            //			String qrysql = "SELECT COALESCE(f_fina_jobs_date('jobid="+jobid+"'),'') as submitime";
            //			Map m2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(qrysql);
            //			String submitime  = String.valueOf(m2.get("submitime"));
            stringBuffer.append("，更正业务日期开始");
            String updatesql = "UPDATE fina_jobs SET submitime = COALESCE(to_date(f_fina_jobs_date('jobid='||id||''), 'YYYY-MM-DD') ,null) WHERE isdelete = FALSE AND id=" + jobid + ";";
            serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(updatesql);
        } catch (Exception e) {
            stringBuffer.append("，processVesvoySync,e为").append(e.getMessage());
        }
    }

    //还箱时间
    public static void updateReturncnttimeData(StringBuffer stringBuffer, ServiceContext serviceContext, String billNo) {
        stringBuffer.append("nbsp,updateReturncnttimeData开始,sono为").append(billNo);
        try {
            String sql0 = "select fj.id as jobid,* from fina_jobs fj where fj.isdelete=false " +
                    "\tAND EXISTS (select 1 from bus_ship_container bsc where fj.id=bsc.jobid and bsc.parentid is null and bsc.isdelete =false and bsc.sono = '" + billNo + "'" +
                    "and EXISTS (SELECT 1 FROM bus_ship_book_cnt x , bus_ship_booking y WHERE x.linkid = y.id AND " +
                    "y.isdelete = FALSE  AND bsc.cntno = x.cntno AND bsc.sono = y.sono ))";
            List<Map> list0 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
            if (list0 != null && list0.size() > 0) {
                stringBuffer.append("，updateReturncnttimeData已经获取到工作单数据，list0长度为").append(list0.size());
                for (int i = 0; i < list0.size(); i++) {
                    Map<String, String> thismap = list0.get(i);

                    Map<String, String> argsMap = new HashMap<String, String>();
                    argsMap.put("jobid", String.valueOf(thismap.get("jobid")));
                    argsMap.put("nos", String.valueOf(thismap.get("nos")));
                    String urlArgs2 = AppUtils.map2Url(argsMap, ";");
                    String sqlQry = "SELECT f_returncnttime_temp1('" + urlArgs2 + "') AS tx_returntext";
                    stringBuffer.append(",sqlQry为").append(sqlQry);
                    Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
                    String returnStr = String.valueOf(m.get("tx_returntext"));
                    stringBuffer.append(",returnStr为").append(returnStr);
                }
            }
        } catch (Exception e) {
            stringBuffer.append(",异常为").append(e.getMessage());
        }
    }

    //发送联想接口
    public static void getLenovoTraceData(StringBuffer stringBuffer, ServiceContext serviceContext, String billNo, String resp) {
        stringBuffer.append("nbsp,getLenovoTraceData开始,sono为").append(billNo);
        try {
            Map<String, String> returnStrMap = new HashMap<String, String>();

            String sql0 = "select fj.id  as jobid,\n" +
                    "       bsc.id as containerid,\n" +
                    "       *\n" +
                    "from fina_jobs fj\n" +
                    "         inner join bus_ship_container bsc\n" +
                    "                    on bsc.isdelete = false and bsc.parentid is null and fj.id = bsc.jobid and bsc.sono = trim('" + billNo + "') AND EXISTS(SELECT 1\n" +
                    "                                                                                                                                      FROM bus_ship_book_cnt x,\n" +
                    "                                                                                                                                           bus_ship_booking y\n" +
                    "                                                                                                                                      WHERE x.linkid = y.id\n" +
                    "                                                                                                                                        AND y.isdelete = FALSE\n" +
                    "                                                                                                                                        AND bsc.cntno = x.cntno\n" +
                    "                                                                                                                                        AND bsc.sono = y.sono)\n" +
                    "where fj.isdelete = false\n" +
                    "  and fj.customerid in (select id\n" +
                    "                        from sys_corporation\n" +
                    "                        where namec ilike '%联想%'\n" +
                    "                           or namee ilike '%lenove%'\n" +
                    "                           or namee ilike '%LENOVO%')\n" +
                    " AND fj.jobdate > (NOW() + '-3month')";
            List<Map> list0 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
            if (list0 != null && list0.size() > 0) {
                stringBuffer.append("，getLenovoTraceData已经获取到联想箱子数据，list0长度为").append(list0.size());
                //当条sono订舱号下的所属柜子数据发送
                for (int i = 0; i < list0.size(); i++) {
                    String jobid = StrUtils.getMapVal(list0.get(i), "jobid");

                    JSONObject jsonObject_resp = JSONObject.fromObject(resp);
                    JSONObject jsonObject_result = jsonObject_resp.getJSONObject("data").getJSONObject("result");
                    JSONArray JSONArray_containers = JSONArray.fromObject(jsonObject_result.get("containers"));
                    for (int j = 0; j < JSONArray_containers.size(); j++) {
                        JSONObject JSONObject_thisone = JSONObject.fromObject(JSONArray_containers.get(j));
                        String containerNo = String.valueOf(JSONObject_thisone.get("containerNo"));

                        String sql1 = "select fj.id  as jobid,\n" +
                                "       bsc.id as containerid,\n" +
                                "       *\n" +
                                "from fina_jobs fj\n" +
                                "         inner join bus_ship_container bsc\n" +
                                "                    on bsc.isdelete = false and bsc.parentid is null and fj.id = bsc.jobid and bsc.cntno = trim('" + containerNo + "') \n" +
                                "where fj.isdelete = false\n" +
                                "  and fj.id=" + jobid;
                        List<Map> list1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql1);
                        if (list1 != null && list1.size() > 0) {
                            for (int k = 0; k < list1.size(); k++) {
                                String containerid = StrUtils.getMapVal(list1.get(k), "containerid");
                                String declarenoStr = StrUtils.getMapVal(list1.get(k), "declareno");
                                FtUtils.postLenovoTraceData(stringBuffer, serviceContext, jobid, containerid, declarenoStr, resp, returnStrMap);
                            }
                        }
                    }

                    //发送邮件给操作
                    if (returnStrMap != null && returnStrMap.size() > 0) {
                        String emailsql = "SELECT f_sys_mail_generate('type=postlenovotracedata;lenovotracedata=" + returnStrMap.get("returnStr") + ";jobid=" + jobid + ";userid=" + "-111" + ";id=" + "-111" +
                                "') AS" +
                                " mail";
                        serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(emailsql);
                    }
                }
            } else {
                stringBuffer.append(",未获取到有效箱子数据");
            }
        } catch (Exception e) {
            stringBuffer.append(",异常为").append(e.getMessage());
        }
    }

    public static void postLenovoTraceData(StringBuffer stringBuffer, ServiceContext serviceContext, String jobid, String containerid, String declarenoStr, String resp, Map returnStrMap) {
        try {
            if (!StrUtils.isNull(declarenoStr)) {
                stringBuffer.append("nbsp,declarenoStr有数据");
                String[] declarenoArray = declarenoStr.trim().split("/");
                for (int i = 0; i < declarenoArray.length; i++) {
                    String thisdeclareno = declarenoArray[i];
                    stringBuffer.append("nbsp,当前declareno为").append(thisdeclareno);

                    Map<String, String> argsMap = new HashMap<String, String>();
                    argsMap.put("jobid", jobid);
                    argsMap.put("containerid", containerid);
                    argsMap.put("declareno", thisdeclareno);
                    argsMap.put("resp", resp);

                    OkHttpUtil.postlenovo(stringBuffer, serviceContext, argsMap, returnStrMap);
                }
            } else {
                stringBuffer.append("nbsp,declarenoStr为空");
            }
        } catch (Exception e) {
            stringBuffer.append("nbsp,postLenovoTraceData异常为").append(e.getMessage());
        }
    }


    //发送project44文件
    public static void project44(StringBuffer stringBuffer, ServiceContext serviceContext, String billNo, String resp) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            stringBuffer.append("nbsp,OkktUtils，running开始");

            String sql0 = "select distinct bs.jobid\n" +
                    "from bus_shipping bs\n" +
                    "         left join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and bs.jobid = bsc.jobid\n" +
                    "where bs.isdelete = false\n" +
                    "  and (bsc.sono = '" + billNo + "')\n" +
                    "  AND EXISTS(SELECT 1 FROM fina_jobs fj where fj.isdelete = false and fj.id = bs.jobid and fj.jobdate > (NOW() + '-3month'))\n" +
                    "  limit 10\n";
            List<Map> list0 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
            if (list0 != null && list0.size() > 0) {
                stringBuffer.append("，project44已经获取到工作单数据，list0长度为").append(list0.size());
                for (int i = 0; i < list0.size(); i++) {
                    Map<String, String> thismap = list0.get(i);

                    String jobids = StrUtils.getMapVal(thismap, "jobid");
                    Map<String, String> argsMap = new HashMap<String, String>();
                    argsMap.put("jobids", jobids);
                    //resp是否需要重新查询
                    argsMap.put("resp", resp);
                    String sqlQry = "SELECT * FROM f_edi_project44('" + jobids + "','" + resp + "')";
                    stringBuffer.append("，sqlQry为").append(sqlQry);
                    Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
                    Object object = m.get("f_edi_project44");
                    stringBuffer.append("，object为").append("nbsp").append(object);
                    String str = "";
                    if (object == null || object == "") {
                        stringBuffer.append("，当前object为空");
                    } else {
                        //发送f_edi_project44数据到接口
                        str = object.toString();

                        String filename = System.currentTimeMillis() + "_" + jobids + ".txt";
                        String filepath = System.getProperty("java.io.tmpdir") + File.separator + "upload_prod_temp";
                        File filepathFile = new File(filepath);
                        if (!filepathFile.exists()) { //如果不存在
                            filepathFile.mkdirs(); //创建目录
                        }
                        String fileurl = filepath + File.separator + filename;
                        stringBuffer.append("，fileurl为").append(fileurl);
                        fos = new FileOutputStream(fileurl, true);
                        fos.write(str.getBytes());
                        fos.close();
                        stringBuffer.append("，filename为").append(filename);
                        stringBuffer.append("，fileurl为").append(fileurl);
                        stringBuffer.append("，生成文件完成");
                    }
                }
            }
        } catch (Exception e) {
            stringBuffer.append("，project44,e为").append(e.getMessage());
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAuthorization() {
        String url = "http://openapi.freightower.com/auth/api/token";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientId", "s973bdb79bcba5cc7");
        jsonObject.put("secret", "123456");
        String resp = FtUtils.sendPost(url, jsonObject.toString(), "");

        JSONObject jsonObject2 = JSONObject.fromObject(resp);
        int statusCode = jsonObject2.getInt("statusCode");
        JSONObject dataJSONObject = jsonObject2.getJSONObject("data");
        String access_token = dataJSONObject.getString("access_token");
        String token = "";
        if (statusCode == 20000) {
            token = access_token;
        }

        String authorization = "Bearer" + " " + token;
        return authorization;
    }

    public static String sendPost(String url, String param, String token) {
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

            if (!StrUtils.isNull(token)) {
                conn.setRequestProperty("Authorization", token);
            }
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
        } finally {// 使用finally块来关闭输出流、输入流
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

}

