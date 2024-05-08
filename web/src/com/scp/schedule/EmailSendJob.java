package com.scp.schedule;

import java.io.File;
import java.security.Security;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysEmail;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.utils.AppUtil;

/**
 * 自动发送邮件
 *
 * @author Neo
 */
public class EmailSendJob {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        //AppUtils.debug("EmailSendJob Start:" + new Date());
        System.out.println("EmailSendJob Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ EmailSendJob wraning:another process is running!reTryCount:" + reTryCount);
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
            send(serviceContext);
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }


    public static void main(String[] args) throws Exception {
        ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIocJunit("serviceContext");
        EmailSendJob emailSendJob = new EmailSendJob();
        emailSendJob.send(serviceContext);
    }

    private void send(ServiceContext serviceContext) throws Exception {
        //区分工作报告类型，在另外一个任务里面，不然会被这个任务自动发送掉
        String querySql =
                "\nSELECT * " +
                        "\nFROM sys_email e" +
                        "\nWHERE emailtype = 'S' " +
                        "\nAND issent = FALSE " +
                        "\nAND isautosend = TRUE " +
                        "\nAND COALESCE(linktbl,'') NOT LIKE '%工作报告%' " +
                        "\nAND trycount <= 3 " +
                        "\nAND addressee IS NOT NULL  " +
                        "\nAND TRIM(addressee) <> '' " +
                        "\nAND isdelete = FALSE" +
                        "\nAND COALESCE(e.status,'') NOT LIKE '%user not exist%' " +
                        "\nAND COALESCE(e.status,'') NOT LIKE '%Invalid Addresses%'" +
                        "\nAND COALESCE(e.status,'') NOT LIKE '%Missing domain%'" + 
                        "\nAND COALESCE(e.status,'') NOT LIKE '%501 Bad address syntax%'" +
                        "\nORDER BY trycount,id" +
                        "\nLIMIT 50";

        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");


        List<Map> lists = daoIbatisTemplate.queryWithUserDefineSql(querySql);
        List<SysEmail> emails = new ArrayList<SysEmail>();
        for (Map map : lists) {
            SysEmail sysEmail = new SysEmail();
            sysEmail.setId(Long.valueOf(StrUtils.getMapVal(map, "id")));
            sysEmail.setContent(StrUtils.getMapVal(map, "content"));
            sysEmail.setSubject(StrUtils.getMapVal(map, "subject"));
            sysEmail.setAddressee(StrUtils.getMapVal(map, "addressee"));
            sysEmail.setCopys(StrUtils.getMapVal(map, "copys"));
            sysEmail.setIssent(true);
            sysEmail.setSenttime(Calendar.getInstance().getTime());
            emails.add(sysEmail);
        }

        EMailSendUtil eMailSendUtil = new EMailSendUtil(
                ConfigUtils.findSysCfgVal("email_srv_smtp"),
                Integer.parseInt(ConfigUtils.findSysCfgVal("email_srv_port")),
                ConfigUtils.findSysCfgVal("email_pop3_account"),
                ConfigUtils.findSysCfgVal("email_pop3_account"),
                ConfigUtils.findSysCfgVal("email_pop3_pwd")
        );
        Session session = getSession();
        Transport transport = session.getTransport();
        transport.connect(eMailSendUtil.getEmailServer(), eMailSendUtil.getPort(), eMailSendUtil.getUsername(), eMailSendUtil.getPassword());//用户名和密码
        try {
            int count = 0;
            for (SysEmail sysEmail : emails) {
                try {
                    if (!transport.isConnected()) {
                        transport.connect(eMailSendUtil.getEmailServer(), eMailSendUtil.getPort(), eMailSendUtil.getUsername(), eMailSendUtil.getPassword());//用户名和密码
                    }


                    MimeMessage message = getMimeMessage(serviceContext, eMailSendUtil, sysEmail,session);


                    transport.sendMessage(message, message.getAllRecipients());//所有地址
                    String sql = "UPDATE sys_email SET issent = true ,senttime = NOW() WHERE id = " + sysEmail.getId() + ";";
                    daoIbatisTemplate.updateWithUserDefineSql(sql);
                } catch (Exception e) {

                    String msg = e.getLocalizedMessage();
                    msg = StrUtils.getSqlFormat(msg);
                    String sql = "UPDATE sys_email SET trycount = COALESCE(trycount,0) + 1 ,status = '" + msg + "' WHERE id = " + sysEmail.getId() + ";";
                    daoIbatisTemplate.updateWithUserDefineSql(sql);
                    MessageUtils.reportException("email send error,please check", e);
                    e.printStackTrace();
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    public static Session getSession() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.localhost", "localhost");
        props.setProperty("mail.transport.protocol", "smtp");

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);
        session.setDebug(false);

        return session;
    }

    public static MimeMessage getMimeMessage(ServiceContext serviceContext, EMailSendUtil eMailSendUtil, SysEmail sysEmail, Session session) throws Exception {
        //设置端口信息
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(eMailSendUtil.getDispatchAddress()));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipients(Message.RecipientType.TO, eMailSendUtil.buildInternetAddress(sysEmail.getAddressee())); // 收件人

        // 4,抄送人
        if (!StrUtils.isNull(sysEmail.getCopys())) {
            message.setRecipients(Message.RecipientType.CC, eMailSendUtil.buildInternetAddress(sysEmail.getCopys())); // 抄送人
        }

        // 5. Subject: 邮件主题
        message.setSubject(MimeUtility.encodeText(sysEmail.getSubject(), "utf-8", "B"));

        // 6，设置邮件内容，混合模式
        MimeMultipart msgMultipart = new MimeMultipart("mixed");
        message.setContent(msgMultipart);

        // 7，设置消息正文
        MimeBodyPart content = new MimeBodyPart();
        msgMultipart.addBodyPart(content);

        // 8，设置正文格式
        MimeMultipart bodyMultipart = new MimeMultipart("related");
        content.setContent(bodyMultipart);

        // 9，设置正文内容
        MimeBodyPart htmlPart = new MimeBodyPart();
        bodyMultipart.addBodyPart(htmlPart);
        htmlPart.setContent(sysEmail.getContent(), "text/html;charset=UTF-8");

        // 10，设置附件
        List<SysAttachment> sa = serviceContext.sysAttachmentService.sysAttachmentDao.findAllByClauseWhere("linkid = " + sysEmail.getId());
        if (sa != null && sa.size() > 0) {
            for (SysAttachment att : sa) {
                // 拼成完整路径(包含文件名)
                String fileurl = "";
                String fileurl0 = AppUtils.getAttachFilePath() + att.getId() + att.getFilename();
                String fileurl1 = AppUtils.getAttachFilePath() + att.getFilename();
                if (new File(fileurl0).exists()) {
                    fileurl = fileurl0;
                }
                if (new File(fileurl1).exists()) {
                    fileurl = fileurl1;
                }

                File file = new File(fileurl);
                if (file.exists()) {
                    //设置相关文件
                    MimeBodyPart filePart = new MimeBodyPart();
                    FileDataSource dataSource = new FileDataSource(file);
                    DataHandler dataHandler = new DataHandler(dataSource);
                    // 文件处理
                    filePart.setDataHandler(dataHandler);
                    // 附件名称
                    filePart.setFileName(file.getName());
                    // 放入正文（有先后顺序，所以在正文后面）
                    msgMultipart.addBodyPart(filePart);
                }
            }
        }

        // 11. 设置发件时间
        message.setSentDate(new Date());

        // 12. 保存文件准备发送
        message.saveChanges();

        return message;
    }
}