package com.scp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * @wsh 实现连接FTP服务器，实现文件的上传和下载
 */
public class FtpUtils {

    public static FTPClient ftpClient = null;
    //ftp服务器IP
    public static String hostname = "";
    //ftp登录账号
    public static String username = "";
    //ftp登录密码
    public static String password = "";
    //ftp文件地址
    public static String ftpDownPath = "/outbound";


    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIocJunit("serviceContext");
            DaoIbatisTemplate daoIbatisTemplate = serviceContext.daoIbatisTemplate;
            File storefile = new File("d:\\CIMCGLOBESUCCESS_IFTMBC_EDI2021120210321117-40.edi");
            // parseFileCargosmart(storefile, sb, daoIbatisTemplate);
            FtpUtils.parseSoEdiCargosmart();
        } catch (Exception e) {
            e.printStackTrace();
            //LogBean.insertLog(sb.append(",FtpUtil parseSoEdi失败,失败原因为").append(e.getMessage()));
        }
    }

    public static void parseSoEdi() {
        StringBuffer sb = new StringBuffer();
        try {
            FTPdownDir(sb);
            LogBean.insertLog(sb.append(",FtpUtil parseSoEdi成功"));
        } catch (Exception e) {
            e.printStackTrace();
            LogBean.insertLog(sb.append(",FtpUtil parseSoEdi失败,失败原因为").append(e.getMessage()));
        }
    }

    /**
     * 获取FTPClient对象
     *
     * @param hostname FTP主机服务器
     * @param password FTP 登录密码
     * @param username FTP登录用户名
     * @return
     */
    public static FTPClient getFTPClient(String hostname, String username, String password) {
        FTPClient ftpClient = null;
        try {
            //创建一个ftp客户端
            ftpClient = new FTPClient();
            // 连接FTP服务器
            ftpClient.connect(hostname);
            // 登陆FTP服务器
            ftpClient.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ftpClient;
    }


    /**
     * FTP 批量文件下载
     *
     * @param sb
     */
    public static String FTPdownDir(StringBuffer sb) throws IOException {
        try {
            sb.append("开始连接ftp服务器");
            hostname = ConfigUtils.findSysCfgVal("sys_ftp_soedi_hostname");
            username = ConfigUtils.findSysCfgVal("sys_ftp_soedi_username");
            password = ConfigUtils.findSysCfgVal("sys_ftp_soedi_password");

            ftpClient = getFTPClient(hostname, username, password);
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            boolean b = ftpClient.changeWorkingDirectory(ftpDownPath);
            if (!b) {
                throw new RuntimeException("ftp服务器的下载文件夹未找到" + ftpDownPath);
            }

            sb.append(",连接获取远程文件夹成功");
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            DaoIbatisTemplate daoIbatisTemplate = serviceContext.daoIbatisTemplate;

            //保存附件
            sb.append(",准备下载文件");
            FTPFile[] remoteFiles = ftpClient.listFiles();
            if (remoteFiles != null) {
                sb.append(",开始下载文件,文件长度为" + remoteFiles.length);
                for (FTPFile remoteFile : remoteFiles) {
                    String filename = remoteFile.getName();
                    String currentmoncth = new SimpleDateFormat("yyyyMM").format(new Date());
                    File storefile = new File(AppUtils.getAttachFilePath() + File.separator + "inttra" + File.separator + filename); //存附件文件夹 intta目录下面
                    OutputStream os = new FileOutputStream(storefile);
                    try {
                        b = ftpClient.retrieveFile(filename, os);
                        sb.append("\r\n" + filename + "下载成功");
                        //解析edi
                        sb.append(",删除文件结束,开始解析edi");
                        parseFile(storefile, sb, daoIbatisTemplate);

                        //删除文件
                        if (b) {
                            ftpClient.dele(remoteFile.getName());
                        }
                    } catch (Exception e) {
                        sb.append("\r\n" + filename + "下载失败");
                    } finally {
                        os.close();
                    }
                }
            } else {
                sb.append(",已连接到ftp服务器,结束下载文件,文件长度为0");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }

        sb.append("\r\nFTPdownDir结束.");
        return "00000";
    }


    private static void parseFile(File file, StringBuffer sb, DaoIbatisTemplate daoIbatisTemplate) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(file));
        try {
            StringBuffer sqlBuffer = new StringBuffer();
            String fileContent = "";
            String str = null;
            while ((str = r.readLine()) != null) {
                fileContent += str;
            }
            String filename = file.getName();
            String editext = fileContent;
            editext = editext.replaceAll("'", "''");
            Map mapPkId = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT getid_tmp() AS newid");
            String reportid = StrUtils.getMapVal(mapPkId, "newid");
            sqlBuffer.append("\ninsert into edi_inttra_report(id,filename,editext,inputtime) " +
                    "values(" + reportid + ",'" + filename + "','" + editext + "',to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "','yyyy-MM-dd hh24:mi:ss'));");

            fileContent = fileContent.replaceAll("[?]'", "&&&&&");
            String[] hangContentArray = fileContent.split("'");
            for (String hangConten : hangContentArray) {
                hangConten = hangConten.replaceAll("&&&&&", "?'");
                hangConten = hangConten.replaceAll("'", "''");
                String sql = "\nSELECT f_edi_inttra_phrase('" + hangConten + "'," + reportid + ");";
                sqlBuffer.append(sql);
            }
            daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlBuffer.toString());

            //处理附件数据
            ediInttraProcess(sb, reportid);
        } catch (Exception e) {
            throw new RuntimeException(",parseFile失败," + e.getMessage());
        } finally {
            r.close();
        }
        sb.append(",parseFile成功");
    }


    public static void ediInttraProcess(StringBuffer sb, String reportid) {
        String sql = "SELECT f_edi_inttra_phrase_process('" + reportid + "');";
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            DaoIbatisTemplate daoIbatisTemplate = serviceContext.daoIbatisTemplate;
            daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            sb.append(",ediInttraProcess成功");
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(",ediInttraProcess失败").append(",sql为").append(sql).append(",失败原因为").append(e.getMessage());
        }
    }


    //Cargosmart下载解析
    public static void parseSoEdiCargosmart() {
        StringBuffer sb = new StringBuffer();
        try {
            FTPdownDirCargosmart(sb);
            LogBean.insertLog(sb.append(",FtpUtil parseSoEdiCargosmart成功"));
        } catch (Exception e) {
            e.printStackTrace();
            LogBean.insertLog(sb.append(",FtpUtil parseSoEdiCargosmart失败,失败原因为").append(e.getMessage()));
        }
    }

    public static String FTPdownDirCargosmart(StringBuffer sb) throws IOException {
        try {
            sb.append("开始连接ftp服务器");
            hostname = "transfer.cargosmart.com";
            username = "cimcglobesuccessftp";
            password = "82hX1d=OiUiZiBbn";

            ftpClient = getFTPClient(hostname, username, password);
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            String[] fileDirectoryArray = new String[]{"/out/bc", "/out/bl", "/out/ct","/out/ack"};
            for (String fileDirectory : fileDirectoryArray) {
                boolean b = ftpClient.changeWorkingDirectory(fileDirectory);
                if (!b) {
                    throw new RuntimeException("ftp服务器的下载文件夹未找到," + fileDirectory);
                }

                sb.append(",连接获取远程文件夹成功");
                ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
                DaoIbatisTemplate daoIbatisTemplate = serviceContext.daoIbatisTemplate;

                //保存附件
                sb.append(",准备下载文件");
                FTPFile[] remoteFiles = ftpClient.listFiles();
                if (remoteFiles != null) {
                    sb.append(",开始下载文件,文件长度为" + remoteFiles.length);
                    for (FTPFile remoteFile : remoteFiles) {
                        String filename = remoteFile.getName();
                        File storefile = new File(AppUtils.getAttachFilePath() + File.separator + "inttra" + File.separator + filename); //存附件文件夹 intta目录下面
                        OutputStream os = new FileOutputStream(storefile);
                        try {
                            b = ftpClient.retrieveFile(filename, os);
                            sb.append("\r\n" + filename + "下载成功");
                            //解析edi
                            sb.append(",删除文件结束,开始解析edi");
                            parseFileCargosmart(storefile, sb, daoIbatisTemplate);

                            //删除文件
                            if (b) {
                                ftpClient.dele(remoteFile.getName());
                            }
                        } catch (Exception e) {
                            sb.append("\r\n" + filename + "下载失败");
                        } finally {
                            os.close();
                        }
                    }
                } else {
                    sb.append(",已连接到ftp服务器,结束下载文件,文件长度为0");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }

        sb.append("\r\nFTPdownDirCargosmart结束.");
        return "00000";
    }

    private static void parseFileCargosmart(File file, StringBuffer sb, DaoIbatisTemplate daoIbatisTemplate) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(file));
        try {
            StringBuffer sqlBuffer = new StringBuffer();
            String fileContent = "";
            String str = null;
            while ((str = r.readLine()) != null) {
                fileContent += str;
            }
            String filename = file.getName();
            String editext = fileContent;
            editext = editext.replaceAll("'", "''");
            Map mapPkId = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT getid_tmp() AS newid");
            String reportid = StrUtils.getMapVal(mapPkId, "newid");
            sqlBuffer.append("\ninsert into edi_inttra_report(id,filename,editext,inputtime) " +
                    "values(" + reportid + ",'" + filename + "','" + editext + "',to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "','yyyy-MM-dd hh24:mi:ss'));");

            fileContent = fileContent.replaceAll("[?]'", "&&&&&");
            String[] hangContentArray = fileContent.split("'");
            for (String hangConten : hangContentArray) {
                hangConten = hangConten.replaceAll("&&&&&", "?'");
                hangConten = hangConten.replaceAll("'", "''");
                String sql = "\nSELECT f_edi_inttra_phrase('" + hangConten + "'," + reportid + ");";
                sqlBuffer.append(sql);
            }
            daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlBuffer.toString());

            //处理附件数据
            ediInttraProcessCargosmart(sb, reportid);
        } catch (Exception e) {
            throw new RuntimeException(",parseFile失败," + e.getMessage());
        } finally {
            r.close();
        }
        sb.append(",parseFile成功");
    }


    public static void ediInttraProcessCargosmart(StringBuffer sb, String reportid) {
        String sql = "SELECT f_edi_inttra_phrase_process('" + reportid + "');";
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            DaoIbatisTemplate daoIbatisTemplate = serviceContext.daoIbatisTemplate;
            daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            sb.append(",ediInttraProcessCargosmart成功");
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(",ediInttraProcessCargosmart失败").append(",sql为").append(sql).append(",失败原因为").append(e.getMessage());
        }
    }
}