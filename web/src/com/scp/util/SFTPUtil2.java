package com.scp.util;


import com.jcraft.jsch.*;
import org.apache.pdfbox.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * @version 1.0.0
 * @ClassName: SFTPUtil2
 * @Description: sftp连接工具类
 */
public class SFTPUtil2 {

    private static ChannelSftp sftp;

    private Session session;

    // FTP 登录用户名
    private String userName;
    // FTP 登录密码
    private String password;
    // FTP 服务器地址IP地址
    private String host;
    // FTP 端口
    private int port;

    /**
     * 构造基于密码认证的sftp对象
     *
     * @param userName
     * @param password
     * @param host
     * @param port
     */
    public SFTPUtil2(String userName, String password, String host, int port) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public SFTPUtil2() {
    }

    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void login() {
        try {
            JSch jsch = new JSch();

            session = jsch.getSession(userName, host, port);
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    // /**
    //  * 递归根据路径创建文件夹
    //  *
    //  * @param dirs     根据 / 分隔后的数组文件夹名称
    //  * @param tempPath 拼接路径
    //  * @param length   文件夹的格式
    //  * @param index    数组下标
    //  * @return
    //  */
    // public void mkdirDir(String[] dirs, String tempPath, int length, int index) {
    //     // 以"/a/b/c/d"为例按"/"分隔后,第0位是"";顾下标从1开始
    //     index++;
    //     if (index < length) {
    //         // 目录不存在，则创建文件夹
    //         tempPath += "/" + dirs[index];
    //     }
    //     try {
    //         sftp.cd(tempPath);
    //         if (index < length) {
    //             mkdirDir(dirs, tempPath, length, index);
    //         }
    //     } catch (SftpException ex) {
    //         try {
    //             sftp.mkdir(tempPath);
    //             sftp.cd(tempPath);
    //         } catch (SftpException e) {
    //             e.printStackTrace();
    //
    //         }
    //         mkdirDir(dirs, tempPath, length, index);
    //     }
    // }

    // /**
    //  * 将输入流的数据上传到sftp作为文件（多层目录）
    //  *
    //  * @param directory    上传到该目录(多层目录)
    //  * @param sftpFileName sftp端文件名
    //  * @param input        输入流
    //  * @throws SftpException
    //  * @throws Exception
    //  */
    // public void uploadMore(String directory, String sftpFileName, InputStream input) throws SftpException {
    //     try {
    //         sftp.cd(directory);
    //     } catch (SftpException e) {
    //         // 目录不存在，则创建文件夹
    //         String[] dirs = directory.split("/");
    //         String tempPath = "";
    //         int index = 0;
    //         mkdirDir(dirs, tempPath, dirs.length, index);
    //     }
    //     sftp.put(input, sftpFileName);// 上传文件
    // }

    public void uploadMore2(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);// 上传文件
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录(单层目录)
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param byteArr      要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);
        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    public static void main(String[] args) throws Exception {
        SFTPUtil2 SFTPUtil2 = new SFTPUtil2("cimc", "nh4vzUFMVGyr3cGdMFBp", "35.195.8.227", 2222);
        SFTPUtil2.login();
        File file = new File("D:\\\\upload\\scp\\attachfile\\202109_84353782274_IFTSTA_1321752852_1017522424.txt");
        InputStream is = new FileInputStream(file);
        // 多级目录创建并上传
        // SFTPUtil2.uploadMore("/a/b/c/d", "test_2020062511111.dat", is);
        SFTPUtil2.uploadMore2("/project44cimc", "test_2020062511111.dat", is);

        Vector<ChannelSftp.LsEntry> files = sftp.ls("/project44cimc");
        SFTPUtil2.logout();
    }
}