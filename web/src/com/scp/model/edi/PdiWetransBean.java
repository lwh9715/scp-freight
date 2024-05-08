package com.scp.model.edi;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;
import com.ufms.web.view.sysmgr.LogBean;

@ManagedBean(name = "pages.module.edi.pdiwetransBean", scope = ManagedBeanScope.REQUEST)
public class PdiWetransBean extends EdiGridView {
    @Bind
    @SaveState
    private String startDate;

    @Bind
    @SaveState
    private String endDate;
    @Bind
    @SaveState
    private String vessel = "";
    @Bind
    @SaveState
    private String voyage = "";
    @Bind
    @SaveState
    private String filetype = "1";
    @Bind
    @SaveState
    private String sendercode = "CNBKGPIL";
    @Bind
    @SaveState
    private String acceptcode = "CNBKGPIL";

    @Bind
    @SaveState
    private String bookingCode;
    @Bind
    @SaveState
    private Date senddate = new Date();
    @Bind
    @SaveState
    private String specification;
    @Bind
    @SaveState
    public String hblnosubs = "";

    @Bind
    @SaveState
    String dynamicClauseWhere = "";

    @Bind
    public UIWindow showsetFIPWindow;

    @SaveState
    private Map<String, String> cfgDataMap;

    @Bind
    @SaveState
    public boolean isFTP;

    @SaveState
    public String path;

    @SaveState
    public String randomString;

    @Bind
    @SaveState
    private String salescode;

    @Bind
    @SaveState
    private String autoSplit = "N";

    @Bind
    @SaveState
    public String bookingcorpid;

    @SaveState
    public String jobid;

    @SaveState
    public String billid;

    @SaveState
    public String amsSend;

    @SaveState
    public String blMethod="ORI";

    @SaveState
    public String corporationid;

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            try {
                initData();
                super.applyGridUserDef();
                jobid = AppUtils.getReqParam("jobid");
                billid = AppUtils.getReqParam("billid");
                corporationid = String.valueOf(AppUtils.getUserSession().getCorpidCurrent());
                if (!StrUtils.isNull(jobid) && StrUtils.isNumber(jobid)) {
                    gridLazyLoad = false;
                } else {
                    gridLazyLoad = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void initData() throws Exception {
        String cfgDataMap[] = {"edi_pil_ftp_host", "edi_pil_ftp_port", "edi_pil_ftp_sername", "edi_pil_ftp_possword"
                , "edi_pil_ftp_uploadpath", "edi_pil_ftp_downloadpath", "edi_pil_ftp_localpath", "edi_pil_ftp_sendcode"
                , "edi_pil_ftp_recivecode", "edi_pil_ftp_companyid", "edi_pil_ftp_corporatename", "edi_pil_bookingCode", "edi_pil_autoSplit"};
        this.cfgDataMap = ConfigUtils.findUserCfgVals(cfgDataMap, AppUtils.getUserSession().getUserid());
        if (!StrUtils.isNull(this.cfgDataMap.get("edi_pil_ftp_sendcode")) && this.cfgDataMap.get("edi_pil_ftp_sendcode") != null) {
            sendercode = this.cfgDataMap.get("edi_pil_ftp_sendcode");
        }
        if (!StrUtils.isNull(this.cfgDataMap.get("edi_pil_ftp_recivecode")) && this.cfgDataMap.get("edi_pil_ftp_recivecode") != null) {
            acceptcode = this.cfgDataMap.get("edi_pil_ftp_recivecode");
        }
        if (!StrUtils.isNull(this.cfgDataMap.get("edi_pil_bookingCode")) && this.cfgDataMap.get("edi_pil_bookingCode") != null) {
            bookingCode = this.cfgDataMap.get("edi_pil_bookingCode");
        }
        if (!StrUtils.isNull(this.cfgDataMap.get("edi_pil_autoSplit")) && this.cfgDataMap.get("edi_pil_autoSplit") != null) {
            autoSplit = this.cfgDataMap.get("edi_pil_autoSplit");
        }
    }

    @SaveState
    public String jobidfirst = "";//记录首次勾选id

    @SaveState
    public String idss = "";

    @Action
    public void grid_onselectionchange() {//单击勾选事件
        String[] ids = this.grid.getSelectedIds();
        if (ids.length > 0
                && ids != null) {
            idss = idss + ids[0] + ",";
            jobidfirst = idss.substring(0, idss.indexOf(","));
            //			//System.out.println(idss+"///"+jobidfirst);
        }
        if (ids == null || ids.length == 0) {
            idss = "";
        }
    }

    /**
     * TODO	:	发送API待完善
     */
    @Action
    public void updown() {
        try {
            String[] ids = this.grid.getSelectedIds();
            if (ids == null || ids.length == 0) {
                MessageUtils.alert("请选择至少一条记录");
                return;
            }

            for (String jobid : ids) {
                Long userid = AppUtils.getUserSession().getUserid();
                Map<String, String> argsMap = new HashMap<String, String>();
                argsMap.put("jobids", jobid);
                argsMap.put("sendercode", sendercode);
                argsMap.put("senddate", new SimpleDateFormat("yyyyMMddHHmm").format(senddate));
                argsMap.put("filetype", filetype);
                argsMap.put("userid", userid.toString());
                argsMap.put("salescode", salescode);
                argsMap.put("bookingCode", bookingCode);
                argsMap.put("autoSplit", autoSplit);
                argsMap.put("bookingcorpid", bookingcorpid);
                argsMap.put("amsSend", amsSend);
                argsMap.put("blMethod", blMethod);
                argsMap.put("billid", StrUtils.isNull(billid) ? "0" : billid);
                argsMap.put("userid", String.valueOf(AppUtils.getUserSession().getUserid()));
                argsMap.put("corporationid", corporationid);

                String urlArgs2 = AppUtils.map2Url(argsMap, ";");
                String sqlQry = "SELECT f_edi_wetrans('" + urlArgs2 + "') AS tx_returntext";
                Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
                String returnStr = String.valueOf(m.get("tx_returntext"));

                String url = "https://xdwlapi.cimclogistics.com:14447/env-109/por-51/customer/standardAPI/DKH";
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("cache-control", "no-cache");
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
                headers.put("client_id", "823700-80179");
                headers.put("client_secret", "973e867994dea4c03c212f11ebe7193d5e88c2eb");
                headers.put("grant_type", "client_credentials");
                headers.put("Content-Type", "application/json;charset=utf-8");
                headers.put("apikey", "MQKRIWK8LR0bdqmiWlqZnu1LGkI33sXo");

                String result = httpsRequest(headers, url, "POST", returnStr);
                LogBean.insertLastingLog(new StringBuffer().append("PdiWetransBean WETRANS结束,sqlQry为").append(sqlQry).append(",returnStr为").append(returnStr).append(",result为").append(result));
                MessageUtils.alert(result);
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

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
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            connection.setSSLSocketFactory(ssf);
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(500000);
            // 建立实际的连接
            connection.connect();

            if(!StrUtils.isNull(outputStr)){
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

            result = result.replaceAll("Ü", "U");
        } catch (Exception e) {
            throw new RuntimeException(new StringBuffer().append("PdiWetransBean httpsRequest失败,失败原因为").append(e.getMessage()).toString());
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return result;
    }





    @Action
    public void exporting() {
        String[] ids = this.grid.getSelectedIds();
        boolean running = false;
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请选择至少一条记录");
            path = "";
            return;
        }
        String jobids = "";
        for (int i = 0; i < ids.length; i++) {
            jobids = jobids + ids[i] + ",";
        }
        jobids = jobidfirst + "," + jobids.replace(jobidfirst, "");
        jobids = jobids.replaceAll("^,*|,*$", "");//正则去掉两边的逗号
        //System.out.println(jobids);
        running = running(jobids);
        if (running) {
            if (isFTP) {
                MessageUtils.alert("txt文件生成成功");
            } else {
                MessageUtils.alert("txt文件生成成功，已经保存到桌面edi文件夹中");
            }
        } else {
            //			MessageUtils.alert("条款不符合规范！");
            if (isFTP) {

            } else {
                //				MessageUtils.alert("导出失败,请看结果信息");
                path = "";
            }
        }
        this.grid.reload();
    }

    @Bind
    @SaveState
    private String arrStr;

    public boolean running(String jobids) {
        Long userid = AppUtils.getUserSession().getUserid();
        Map<String, String> argsMap = new HashMap<String, String>();
        String urlArgs2 = "";
        argsMap.put("jobids", jobids);
        argsMap.put("sendercode", sendercode);
        argsMap.put("senddate", new SimpleDateFormat("yyyyMMddHHmm").format(senddate));
        argsMap.put("filetype", filetype);
        argsMap.put("userid", userid.toString());
        argsMap.put("salescode", salescode);
        argsMap.put("bookingCode", bookingCode);
        argsMap.put("autoSplit", autoSplit);
        argsMap.put("bookingcorpid", bookingcorpid);
        argsMap.put("amsSend", amsSend);
        argsMap.put("blMethod", blMethod);
        argsMap.put("billid", StrUtils.isNull(billid) ? "0" : billid);
        urlArgs2 = AppUtils.map2Url(argsMap, ";");
        try {
            String sqlQry = "SELECT f_edi_wetrans('" + urlArgs2 + "') AS tx_returntext";
            Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
            String returnStr = String.valueOf(m.get("tx_returntext"));

            this.grid.reload();
            return writer(returnStr.toString());
        } catch (Exception e) {
            MessageUtils.showException(e);
            return false;
        }
    }


    @Bind(id = "fileDownLoad", attribute = "src")
    private InputStream getDownload5() {
        try {
            InputStream input = new FileInputStream(path);
            return input;
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        } finally {
            //获得当前系统桌面路径
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            String absolutePath = desktopDir.getAbsolutePath();
            //删除该文件夹下所有文件
            delAllFile(absolutePath + "/edi/pil");
        }
    }

    @Bind
    @SaveState
    public String fileName;
    @Bind
    @SaveState
    public String contentType;

    /*
     * 写入文件
     * */
    public boolean writer(String str) {
        Random ra = new Random();
        //获得当前系统桌面路径
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        String absolutePath = desktopDir.getAbsolutePath();
        Format format = new SimpleDateFormat("yyyyMMdd");
        randomString = format.format(new Date()) + getcounts();
        contentType = "text/plain";
        File file = new File(absolutePath + "/edi/pil");
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        fileName = randomString + ".PMS";
        path = absolutePath + "/edi/pil/" + fileName;
        File newFile = newFile(path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
            bw.write(str);
            bw.newLine();
            //关闭资源
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isFTP) {
            return testUpload();
        } else {
            return true;
        }
    }

    @Action
    public void savesetFIP() {
        try {
            ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
            MessageUtils.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        this.sendercode = this.getCfgDataMap().get("edi_pil_ftp_sendcode");
        this.acceptcode = this.getCfgDataMap().get("edi_pil_ftp_recivecode");
        this.bookingCode = this.getCfgDataMap().get("edi_pil_bookingCode");
        this.autoSplit = this.getCfgDataMap().get("edi_pil_autoSplit");
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
    }

    public Map<String, String> getCfgDataMap() {
        return this.cfgDataMap;
    }

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        if (!StrUtils.isNull(jobid) && StrUtils.isNumber(jobid)) {
            qry += "\nAND t.id=" + jobid;
        }
        m.put("qry", qry);
        //初始化
        dynamicClauseWhere = " 1=1 ";
        dynamicClauseWhere += "\nAND jobdate BETWEEN '"
                + (StrUtils.isNull(startDate) ? "0001-01-01" : startDate)
                + "' AND '"
                + (StrUtils.isNull(endDate) ? "9999-12-31" : endDate)
                + "'";
        //船名航次拼接
        if (vessel != "") {
            dynamicClauseWhere += "\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs.vessel LIKE '%" + vessel + "%')";
        }
        if (voyage != "") {
            dynamicClauseWhere += "\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs.voyage LIKE '%" + voyage + "%')";
        }
        m.put("dynamicClauseWhere", dynamicClauseWhere);

        //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
        String sql = "\nAND ( t.saleid = " + AppUtils.getUserSession().getUserid()
                + "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
                + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + AppUtils.getUserSession().getUserid() + ")) " + //能看所有外办订到本公司的单权限的人能看到对应单
                "AND t.corpid <> t.corpidop AND t.corpidop = " + AppUtils.getUserSession().getCorpidCurrent() + ")"
                + "\n	OR EXISTS"
                + "\n				(SELECT "
                + "\n					1 "
                + "\n				FROM sys_custlib x , sys_custlib_user y  "
                + "\n				WHERE y.custlibid = x.id  "
                + "\n					AND y.userid = " + AppUtils.getUserSession().getUserid()
                + "\n					AND x.libtype = 'S'  "
                + "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
                + ")"
                + "\n	OR EXISTS"
                + "\n				(SELECT "
                + "\n					1 "
                + "\n				FROM sys_custlib x , sys_custlib_role y  "
                + "\n				WHERE y.custlibid = x.id  "
                + "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = " + AppUtils.getUserSession().getUserid() + " AND z.roleid = y.roleid)"
                + "\n					AND x.libtype = 'S'  "
                + "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
                + ")"
                //过滤工作单指派
                + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
                + AppUtils.getUserSession().getUserid() + ")"
                + "\n)";

        // 权限控制 neo 2014-05-30
        m.put("filter", sql);
        String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid =" + AppUtils.getUserSession().getUserid() + ") " +
                "\n OR t.saleid =" + AppUtils.getUserSession().getUserid() + ") ";
        m.put("corpfilter", corpfilter);
        return m;
    }

    /**
     * FTP不放文件测试
     */
    @Action
    public void testUploads2() {
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        try {
            ftpClient.connect(this.cfgDataMap.get("edi_pil_ftp_host"));
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_pil_ftp_sername"), this.cfgDataMap.get("edi_pil_ftp_possword"));
            if (login) {
                this.alert("FTP配置配置正确！");
                return;
            } else {
                this.alert("账号或密码错误1！");
            }
        } catch (SocketException e) {
            this.alert("FTP配置配置错误2！");
            e.printStackTrace();
        } catch (IOException e) {
            this.alert("FTP配置配置错误3！");
            e.printStackTrace();
        }

    }

    /**
     * FTP上传单个文件测试
     */
    @Action
    public void testUploads() {
        //获得当前系统桌面路径
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        String absolutePath = desktopDir.getAbsolutePath();

        File file = new File(absolutePath + "/text.txt");
        try {
            file.createNewFile();

            path = absolutePath + "/text.txt";
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            FileWriter fw = new FileWriter(path);
            fw.write("该文件为测试文件，请删除！");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (testUpload()) {
            this.alert("FTP配置配置正确！");
        } else {
            //			this.alert("FTP配置配置有误！");
        }
        file.delete();
    }

    @Action
    public boolean testUpload() {
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;

        try {
            ftpClient.connect(this.cfgDataMap.get("edi_inttra_ftp_host"));
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_inttra_ftp_sername"), this.cfgDataMap.get("edi_inttra_ftp_possword"));
            if (login) {
                File srcFile = new File(path);
                fis = new FileInputStream(srcFile);
                //设置上传目录
                ftpClient.changeWorkingDirectory("/" + this.cfgDataMap.get("edi_inttra_ftp_uploadpath"));
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("GBK");
                //设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.storeFile("intiarsi" + randomString + ".txt", fis);
                return true;
            } else {
                //            	this.alert("FTP配置填写错误！");
                return false;
            }
        } catch (SocketException e) {
            this.alert("FTP配置配置错误2！");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                this.alert("关闭FTP连接发生异常！");
                return false;
                //throw new RuntimeException("关闭FTP连接发生异常！", e); 
            }
        }
    }

    /**
     * FTP下载单个文件测试
     */
    @Action
    public void testDownload() {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;
        try {
            ftpClient.connect("192.168.0.101");
            ftpClient.login("admin", "123");
            String remoteFileName = "/admin/pic/3.gif";
            fos = new FileOutputStream("c:/down.gif");
            ftpClient.setBufferSize(1024);
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(remoteFileName, fos);
        } catch (IOException e) {
            e.printStackTrace();
            this.alert("FTP客户端出错！");
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fos);
            //System.out.println("55555555555555");
            try {
                ftpClient.disconnect();
                //System.out.println("66666666666666");
            } catch (IOException e) {
                e.printStackTrace();
                this.alert("关闭FTP连接发生异常！");
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
    }

    @Override
    public void grid_ondblclick() {
        super.grid_ondblclick();
        String winId = "_edit_jobs";
        String url = "../ship/jobsedit.xhtml?id=" + this.getGridSelectId();
        AppUtils.openWindow(winId, url);
    }

}
