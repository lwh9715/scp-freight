package com.scp.util;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SFTPUtils {

    private static String host;//服务器连接ip
    private static String username;//用户名
    private static String password;//密码
    private static int port;//端口号
    private static ChannelSftp sftp = null;
    private static Session sshSession = null;

    public SFTPUtils() {
    }

    public SFTPUtils(String host, int port, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public SFTPUtils(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    /**
     * 通过SFTP连接服务器
     */
    public static void connect() {
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public static void disconnect() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (sshSession != null) {
            if (sshSession.isConnected()) {
                sshSession.disconnect();
            }
        }
    }


    /**
     * 上传单个文件
     *
     * @param remotePath     ：远程保存目录
     * @param remoteFileName ：保存文件名
     * @param localPath      ：本地上传目录(以路径符号结束)
     * @param localFileName  ：上传的文件名
     * @param stringBuffer
     * @return
     */
    public static boolean uploadFile(String remotePath, String remoteFileName, String fileurl, StringBuffer stringBuffer) throws SftpException, FileNotFoundException {
        FileInputStream in = null;
        try {
            cdDir(remotePath);
            File file = new File(fileurl);
            stringBuffer.append("\r\n,当前fileurl为").append(fileurl);
            in = new FileInputStream(file);
            sftp.put(in, remoteFileName);
            stringBuffer.append(",已上传文件名为").append(fileurl);
            return true;
        } catch (Exception e) {
            stringBuffer.append(",已上传文件失败为").append(fileurl);
            stringBuffer.append(e.getMessage());
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    stringBuffer.append(",关闭ioc异常");
                }
            }
        }
    }

    /**
     * 批量上传文件
     *
     * @param remotePath   ：远程保存目录
     * @param localPath    ：本地上传目录(以路径符号结束)
     * @param del          ：上传后是否删除本地文件
     * @param stringBuffer
     * @return
     */
    public static void bacthUploadFile(String remotePath, String localPath, boolean del, StringBuffer stringBuffer) {
        try {
            File file = new File(localPath);
            stringBuffer.append("，bacthUploadFile开始，localPath=" + localPath);
            File[] files = file.listFiles();
            stringBuffer.append("，upload file start:remotePath=" + remotePath + "and localPath=" + localPath + ",file size is " + files.length);

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && files[i].getName().indexOf("bak") == -1) {
                    String thisfileurl = localPath + File.separator + files[i].getName();
                    try {
                        if (uploadFile(remotePath, files[i].getName(), thisfileurl, stringBuffer) && del) {

                        }
                        deleteFile(thisfileurl, stringBuffer);
                    } catch (Exception e) {
                        stringBuffer.append("，上传删除文件异常").append(e.getMessage()).append(",thisfileurl为").append(thisfileurl);
                    }
                }
            }

            stringBuffer.append("，upload file is success:remotePath=" + remotePath + "and localPath=" + localPath + ",file size is " + files.length);
        } catch (Exception e) {
            stringBuffer.append("，bacthUploadFile异常").append(e.getMessage());
        }
    }

    /**
     * 删除本地文件
     *
     * @param filePath
     * @param stringBuffer
     * @return
     */
    public static boolean deleteFile(String fileurl, StringBuffer stringBuffer) {
        stringBuffer.append(",准备删除文件");
        boolean rs = false;
        try {
            File file = new File(fileurl);
            if (!file.exists()) {
                return false;
            }

            if (!file.isFile()) {
                return false;
            }
            rs = file.delete();
            stringBuffer.append(",已删除文件目录为").append(fileurl);
        } catch (Exception e) {
            stringBuffer.append(",删除文件失败").append(fileurl);
        }
        return rs;
    }


    /**
     * 创建目录
     *
     * @param createpath
     * @return
     */
    public static boolean cdDir(String createpath) throws SftpException {
        try {
            if (isDirExist(createpath)) {
                sftp.cd(createpath);
                return true;
            }
        } catch (SftpException e) {

        }
        return false;
    }

    /**
     * 判断目录是否存在
     *
     * @param directory
     * @return
     */
    public static boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        SFTPUtils sftp = null;
        try {
            sftp = new SFTPUtils("35.195.8.227", 2222, "cimc", "nh4vzUFMVGyr3cGdMFBp");
            sftp.connect();

            StringBuffer stringBuffer = new StringBuffer();
            // uploadFile("project44cimc1", "test_2020062511111.dat", "D:\\\\upload\\scp\\attachfile\\", "202109_84353782274_IFTSTA_1321752852_1017522424.txt");
            bacthUploadFile("project44cimc", "D:\\\\upload\\scp\\attachfile\\testfile\\", true, stringBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftp.disconnect();
        }
    }
}

