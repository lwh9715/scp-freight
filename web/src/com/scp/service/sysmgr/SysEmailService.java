package com.scp.service.sysmgr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import org.apache.commons.lang.StringUtils;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysAttachmentDao;
import com.scp.dao.sys.SysEmailDao;
import com.scp.dao.sys.SysEmailFastextDao;
import com.scp.dao.sys.SysLogDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.model.ship.BusShipBooking;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysEmail;
import com.scp.model.sys.SysEmailFastext;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysUser;
import com.scp.schedule.EMailReciveUtil;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.PDFUtil;
import com.scp.util.StrUtils;
import com.scp.util.ConfigUtils.UsrCfgKey;

@Component
public class SysEmailService{

	@Resource
	public SysEmailDao sysEmailDao;

	@Resource
	public SysUserDao sysUserDao;

	@Resource
	public SysAttachmentDao sysAttachmentDao;

	@Resource
	public SysEmailFastextDao sysEmailFastextDao;

	@Resource
	public SysLogDao sysLogDao;

	public void saveData(SysEmail data) {
		if(0 == data.getId()){
			sysEmailDao.create(data);
		}else{
			SysEmail bean = sysEmailDao.findById(data.getId());
			if(bean == null){
				sysEmailDao.create(data);
			}else{
				sysEmailDao.modify(data);
			}
		}
	}

	public void removeDate(Long id) {
		SysEmail data = sysEmailDao.findById(id);
		sysEmailDao.remove(data);
	}

//	public List<SysEmail> queryUnSendEmail(String ids[]) {
//		String id = StrTools.array2List(ids);
//		return sysEmailDao.findAllByClauseWhere("emailtype = 'S' AND issent = FALSE AND isdelete = FALSE AND id IN("+id+");");
//	}

	public List<SysEmail> queryUnSendEmail(Long emailid) {
		return sysEmailDao.findAllByClauseWhere("emailtype = 'S' AND issent = FALSE AND isdelete = FALSE AND id = " + emailid);
	}


	/**
	 * 发送普通邮件
	 * @param userid
	 * @param emailid
	 * @throws Exception
	 */
	public void sendEmail(Long emailid) throws Exception {

		List<SysEmail> sysEmails = queryUnSendEmail(emailid);
		for (SysEmail sysEmail : sysEmails) {
			String id = String.valueOf(sysEmail.getId());
			SysUser sysUser = sysUserDao.findOneRowByClauseWhere(" code = '" + sysEmail.getInputer() + "'");
			Map<String,String> m = ConfigUtils.findUserCfgVals(
					new String[]{
							UsrCfgKey.email_srv_smtp.name(),
							UsrCfgKey.email_srv_port.name(),
							UsrCfgKey.email_srv_pop3.name(),
							UsrCfgKey.email_pop3_account.name(),
							UsrCfgKey.email_pop3_pwd.name(),
							},sysUser.getId() );

			EMailSendUtil email = new EMailSendUtil(
					m.get(UsrCfgKey.email_srv_smtp.name()),
					Integer.parseInt(m.get(UsrCfgKey.email_srv_port.name())),
					m.get(UsrCfgKey.email_pop3_account.name()),
					m.get(UsrCfgKey.email_pop3_account.name()),
					m.get(UsrCfgKey.email_pop3_pwd.name())
					);
			String content = sysEmail.getContent();//内容
			String subject = sysEmail.getSubject();//主题
			String acceptAddress = sysEmail.getAddressee();//收件人地址
			String ccAddress = sysEmail.getCopys();//收件人地址
			email.sendEmail(content,subject, acceptAddress,ccAddress);
			sysEmail.setIssent(true);
			sysEmail.setSenttime(Calendar.getInstance().getTime());
			sysEmailDao.modify(sysEmail);
		}
	}

	/**
	 * 发送HTML邮件
	 * @param userid
	 * @param emailid
	 * @throws Exception
	 */
	public void sendEmailHtml(Long emailid, Map<String, String> attachments) throws Exception {
		List<SysEmail> sysEmails = queryUnSendEmail(emailid);
		for (SysEmail sysEmail : sysEmails) {
			String id = String.valueOf(sysEmail.getId());
			EMailSendUtil email;
			String sysemailsender = ConfigUtils.findSysCfgVal("email_pop3_account");//如果发件人是系统设置中的发件人，则表示这个邮件是系统自动生成的，按系统设置的发件人提取
			if(sysEmail.getSender().equals(sysemailsender)){
				email = new EMailSendUtil(
						ConfigUtils.findSysCfgVal("email_srv_smtp"),
						Integer.parseInt(ConfigUtils.findSysCfgVal("email_srv_port")),
						ConfigUtils.findSysCfgVal("email_pop3_account"),
						ConfigUtils.findSysCfgVal("email_pop3_account"),
						ConfigUtils.findSysCfgVal("email_pop3_pwd")
						);
			}else{
				SysUser sysUser = sysUserDao.findOneRowByClauseWhere(" code = '" + sysEmail.getInputer() + "'");
				Map<String,String> m = ConfigUtils.findUserCfgVals(
						new String[]{
								UsrCfgKey.email_srv_smtp.name(),
								UsrCfgKey.email_srv_port.name(),
								UsrCfgKey.email_srv_pop3.name(),
								UsrCfgKey.email_pop3_account.name(),
								UsrCfgKey.email_pop3_pwd.name(),
								},sysUser.getId());

				email = new EMailSendUtil(
						m.get(UsrCfgKey.email_srv_smtp.name()),
						Integer.parseInt(m.get(UsrCfgKey.email_srv_port.name())),
						m.get(UsrCfgKey.email_pop3_account.name()),
						m.get(UsrCfgKey.email_pop3_account.name()),
						m.get(UsrCfgKey.email_pop3_pwd.name())
						);
			}




			String content = sysEmail.getContent();//内容
			String subject = sysEmail.getSubject();//主题
			String acceptAddress = sysEmail.getAddressee();//收件人地址
			String ccAddress = sysEmail.getCopys();//Copys地址
			try {
				email.sendEmailHtml(content,subject, acceptAddress ,ccAddress, attachments);
				sysEmail.setIssent(true);
				sysEmail.setSenttime(Calendar.getInstance().getTime());
			} catch (Exception e) {//发送错误，标记尝试次数++，提取部分，过滤超过3次失败的邮件
				sysEmail.setTrycount(sysEmail.getTrycount()+1);
				e.printStackTrace();
				throw e;
			}
			sysEmailDao.modify(sysEmail);
		}
	}

	/**
	 * 发送普通邮件
	 * @param userid
	 * @throws Exception
	 */
//	public void sendEmail(Long userid , Long emailid) throws Exception {
//		Map<String,String> m = CfgUtil.findUserCfgVals(
//				new String[]{
//						UsrCfgKey.email_srv_smtp.name(),
//						UsrCfgKey.email_srv_port.name(),
//						UsrCfgKey.email_srv_pop3.name(),
//						UsrCfgKey.email_pop3_account.name(),
//						UsrCfgKey.email_pop3_pwd.name(),
//						},userid );
//
//		EMailSendUtil email = new EMailSendUtil(
//				m.get(UsrCfgKey.email_srv_smtp.name()),
//				Integer.parseInt(m.get(UsrCfgKey.email_srv_port.name())),
//				m.get(UsrCfgKey.email_pop3_account.name()),
//				m.get(UsrCfgKey.email_pop3_account.name()),
//				m.get(UsrCfgKey.email_pop3_pwd.name())
//				);
//		List<SysEmail> sysEmails = queryUnSendEmail(emailid);
//		for (SysEmail sysEmail : sysEmails) {
//			String id = String.valueOf(sysEmail.getId());
//			String content = sysEmail.getContent();//内容
//			String subject = sysEmail.getSubject();//主题
//			String acceptAddress = sysEmail.getAddressee();//收件人地址
//			String ccAddress = sysEmail.getCopys();//Copys地址
//
//			email.sendEmail(content,subject, acceptAddress , ccAddress);
//			sysEmail.setIssent(true);
//			sysEmail.setSenttime(Calendar.getInstance().getTime());
//			sysEmailDao.modify(sysEmail);
//		}
//	}
//

	/**
	 * 发送HTML邮件
	 * @param userid
	 * @throws Exception
	 */
//	public void sendEmailHtml(Long userid, Long emailid, Map<String, String> attachments) throws Exception {
//		Map<String,String> m = CfgUtil.findUserCfgVals(
//				new String[]{
//						UsrCfgKey.email_srv_smtp.name(),
//						UsrCfgKey.email_srv_port.name(),
//						UsrCfgKey.email_srv_pop3.name(),
//						UsrCfgKey.email_pop3_account.name(),
//						UsrCfgKey.email_pop3_pwd.name(),
//						},userid );
//
//		EMailSendUtil email = new EMailSendUtil(
//				m.get(UsrCfgKey.email_srv_smtp.name()),
//				Integer.parseInt(m.get(UsrCfgKey.email_srv_port.name())),
//				m.get(UsrCfgKey.email_pop3_account.name()),
//				m.get(UsrCfgKey.email_pop3_account.name()),
//				m.get(UsrCfgKey.email_pop3_pwd.name())
//				);
//		List<SysEmail> sysEmails = queryUnSendEmail(emailid);
//		for (SysEmail sysEmail : sysEmails) {
//			String id = String.valueOf(sysEmail.getId());
//			String content = sysEmail.getContent();//内容
//			String subject = sysEmail.getSubject();//主题
//			String acceptAddress = sysEmail.getAddressee();//收件人地址
//			String ccAddress = sysEmail.getCopys();//Copys地址
//
//			email.sendEmailHtml(content,subject, acceptAddress,ccAddress,attachments);
//			sysEmail.setIssent(true);
//			sysEmail.setSenttime(Calendar.getInstance().getTime());
//			sysEmailDao.modify(sysEmail);
//		}
//	}

	public void receiveEmail() throws Exception {
		SysLog sysLog = new SysLog();
		
		Session session = null;
		Store store = null;
		Folder folder = null;
		
		try {
			String myEmailhost = ConfigUtils.findSysCfgVal("email_srv_pop3");    //
			String myEmailpost = ConfigUtils.findSysCfgVal("email_receive_port");    //
			String myEmailAccount = ConfigUtils.findSysCfgVal("email_pop3_account");   //
			String myEmailPassword = ConfigUtils.findSysCfgVal("email_pop3_pwd");    //
			String email_type = ConfigUtils.findSysCfgVal("email_type");    //
			if (StrUtils.isNull(email_type)) {
				email_type = "POP3";
			}
			email_type = email_type.toLowerCase();

			try {
				myEmailPassword = new EMailSendUtil().decrypt(myEmailPassword);
			} catch (Exception e) {
			}

			// 1. 创建参数配置, 用于连接邮件服务器的参数配置
			Properties props = new Properties();                    // 参数配置
			props.setProperty("mail.store.protocol", email_type);    // 使用imap协议
			props.setProperty("mail." + email_type + ".host", myEmailhost);   // 发件人的邮箱的 SMTP 服务器地址
			props.setProperty("mail." + email_type + ".port", myEmailpost);

			// 2. 根据配置创建会话对象, 用于和邮件服务器交互
			session = Session.getInstance(props);

			store = session.getStore(email_type);
			store.connect(myEmailAccount, myEmailPassword);

			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);  //

			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			Message msgs[] = folder.search(ft);
			sysLog.setLogdesc("邮箱获取邮件成功,邮件长度为" + msgs.length);
			for (int i = 0; i < msgs.length; i++) {
				com.scp.schedule.EMailReciveUtil rm = new com.scp.schedule.EMailReciveUtil((MimeMessage) msgs[i]);
				recive(rm, msgs[i], i, myEmailAccount, sysLog);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			sysLog.setLogdesc(sysLog.getLogdesc() + ",receiveEmail()失败,失败原因为" + e.getMessage());
		} finally {
			try {
				if(folder != null){
					folder.close(true);
				}
			} catch (Exception e) {
			}
			try {
				if(store != null){
					store.close();
				}
			} catch (Exception e) {
			}
			folder = null;
			store = null;
			session = null;
			sysLog.setLogdesc(sysLog.getLogdesc() + " ****finally 释放资源****");
		}

		sysLog.setInputer("email");
		sysLog.setLogtime(new Date());
		sysLog.setLogdesc(sysLog.getLogdesc().replaceAll("'", "\""));
		sysLog.setLogtype("DEBUG");
		sysLogDao.create(sysLog);
	}

	private void recive(EMailReciveUtil rm, Part part, int i, String myEmailAccount, SysLog sysLog) throws Exception {
		try {
			String subject = rm.getSubject();
			String from = rm.getFrom();

			rm.getMailContent(part);
			String content = rm.getBodyText();
			String sendDate = rm.getSendDate();
			String msgId = rm.getMessageId();
			String sql = "INSERT INTO sys_email(id, msgid , subject, content, sender,addressee, emailtype, inputtime) " +
					"   SELECT getid(),'%s','%s','%s','%s','%s','R',NOW() FROM _virtual WHERE NOT EXISTS (SELECT 1 FROM sys_email WHERE msgid = '%s');";
			sql = String.format(sql, StrUtils.getSqlFormat(msgId), StrUtils.getSqlFormat(subject), StrUtils.getSqlFormat(content)
					, StrUtils.getSqlFormat(from), StrUtils.getSqlFormat(myEmailAccount), StrUtils.getSqlFormat(sendDate), StrUtils.getSqlFormat(msgId));
			SysEmailDao sysEmailDao = (SysEmailDao) AppUtils.getBeanFromSpringIoc("sysEmailDao");
			sysEmailDao.executeSQL(sql);
			sysLog.setLogdesc(sysLog.getLogdesc() + ",\r\n \n\n \n\n \n\n 当前邮件获取成功,已保存sys_email,开始解析附件,邮件主题为" + subject);

			//根据邮件处理相应动作，邮件主题必须按此处格式标准
			//允许固定格式在主题任意位置,但必须有此固定格式
			String precessSubject = subject;
			precessSubject = precessSubject.trim();
			precessSubject = precessSubject.replaceAll("转发: ", "");
			precessSubject = precessSubject.replaceAll("Fw:", "");
			precessSubject = precessSubject.replaceAll(" ", "");
			precessSubject = precessSubject.replaceAll("：", ":");
			precessSubject = precessSubject.replaceAll("（", "(");
			precessSubject = precessSubject.replaceAll("）", ")");
			precessSubject = precessSubject.replaceAll("，", ",");

			String precessContent = content;
			precessContent = precessContent.trim();
			precessContent = precessContent.replaceAll("转发: ", "");
			precessContent = precessContent.replaceAll("Fw:", "");
			precessContent = precessContent.replaceAll(" ", "");
			precessContent = precessContent.replaceAll("：", ":");
			precessContent = precessContent.replaceAll("（", "(");
			precessContent = precessContent.replaceAll("）", ")");
			precessContent = precessContent.replaceAll("，", ",");


			//获取邮件,根据附件修改工作单数据
			//上传附件:任意附件类型:工作单号()
			if (!StrUtils.isNull(precessSubject) && precessSubject.contains("上传附件:") && precessSubject.contains(":工作单号(")) {
				//主题格式：
				//		上传附件:SO:工作单号()
				//		上传附件:委托书:工作单号()
				//    	上传附件:船公司账单:工作单号()
				//      上传附件:水单:工作单号()
				//拆分单号出来，按工作单号，查到jobid，附件这里对应插一条记录
				prcessFileA0(rm, part, subject, sysLog);
				return;
			}

			//获取邮件,根据附件新增修改so数据
			if (isPrcessFileA1(precessSubject, subject, sysLog)) {
				prcessFileA1(rm, part, subject, sysLog);
				return;
			}

			//获取邮件,将附件新增到对应so数据上
			List<String> keyWordStrList = new ArrayList<String>();
			if (isPrcessFileA2(precessSubject, precessContent, subject, keyWordStrList, sysLog)) {
				//上传附件:SO:SO(SZPC21030002D)
				//FSDF FSDF  Booking Confirmation : SHZ4206713 KJKJKL LKFJDS
				//Booking Confirmation  Note - MSC FLAVIA/UK139A/CHINOOK Bkg# 181AY21G0309241D1;. - CIMC GLOBE SUCCESS LOGISTICS CO., LTD
				prcessFileA2(rm, part, keyWordStrList, sysLog);
				return;
			}

			//获取邮件,根据正文修改so数据
			if (isPrcessFileA3(precessSubject, subject, sysLog)) {
				prcessFileA3(rm, part, subject, sysLog);
				return;
			}

			sysLog.setLogdesc(sysLog.getLogdesc() + ",recive()失败,失败原因为邮件名称未通过验证,邮件名称为" + subject);
		} catch (Exception e) {
			sysLog.setLogdesc(sysLog.getLogdesc() + ",recive()失败,失败原因为" + e.getMessage());
		}
	}

	private void prcessFileA0(EMailReciveUtil rm, Part part, String subject, SysLog sysLog) throws Exception {
		String from = rm.getFrom();
		if (from.contains("<") && from.contains(">")) {
			from = from.substring(from.indexOf("<") + 1, from.lastIndexOf(">"));
		}


		//解析主题subject
		//上传附件:SO:工作单号(GSZS0008857)
		subject = subject.trim();
		subject = subject.replaceAll("转发: ", "");
		subject = subject.replaceAll("Fw:", "");
		subject = subject.replaceAll(" ", "");
		subject = subject.replaceAll("：", ":");
		subject = subject.replaceAll("（", "(");
		subject = subject.replaceAll("）", ")");
		subject = subject.replaceAll("，", ",");
		subject = subject.substring(subject.indexOf("上传附件"));
		subject = subject.substring(0, subject.indexOf(")") + 1);
		System.out.println(subject);
		sysLog.setLogdesc(sysLog.getLogdesc() + ",prcessFileA0解析主题成功,解析结果subject为" + subject);


		String thisRolename = subject.substring(subject.indexOf("上传附件:") + 5, subject.indexOf(":工作单号"));
		String numberStr = subject.substring(subject.indexOf("工作单号(") + 5, subject.indexOf(")"));
		ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");

		numberStr += ",";
		String[] numberStrSplits = numberStr.split(",");
		sysLog.setLogdesc(sysLog.getLogdesc() + ",解析subject固定格式内容成功,解析结果thisRolename为" + thisRolename + ",numberStr为" + numberStr);
		for (String thisNumber : numberStrSplits) {
			if (StrUtils.isNull(thisNumber)) {
				continue;
			}

			//thisNumber可能为nos或sono
			String sql0 = "select fj.ID AS jobid from fina_jobs fj left join bus_shipping bs on fj.id=bs.jobid where fj.isdelete =false and bs.isdelete=false and (fj.nos='" + thisNumber + "' or bs.sono='" + thisNumber + "')";
			Map map0 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql0);
			String jobid = StrUtils.getMapVal(map0, "jobid");

			String sql = "SELECT id FROM sys_role where name='" + thisRolename.trim() + "' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);


			//保存附件
			String linkid = jobid;
			String roleid = StrUtils.getMapVal(map, "id");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			String filename = "";
			try {
				sysLog.setLogdesc(sysLog.getLogdesc() + ",开始保存附件,linkid为" + linkid + ",roleid为" + roleid);
				if (!StrUtils.isNull(linkid) && !map.isEmpty() && !StrUtils.isNull(roleid) && part.isMimeType("multipart/*")) {
					Multipart mp = (Multipart) part.getContent();
					for (int y = 0; y < mp.getCount(); y++) {
						BodyPart mpart = mp.getBodyPart(y);
						String dispostion = mpart.getDisposition();
						if ((dispostion != null) && (dispostion.equals(Part.ATTACHMENT) || dispostion.equals(Part.INLINE))) {
							filename = mpart.getFileName();
							if (filename.toLowerCase().indexOf("gb18030") != -1 || filename.toLowerCase().contains("utf-8") || filename.toLowerCase().contains("gbk")) {
								filename = MimeUtility.decodeText(filename);
							}
							bis = new BufferedInputStream(mpart.getInputStream());

							sysLog.setLogdesc(sysLog.getLogdesc() + ",获取附件名称成功,filename为" + filename);
							if (!StrUtils.isNull(filename)) {
								String getidSql = "SELECT getid() AS newPkId";
								Map mapPkId = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(getidSql);
								String newPkId = StrUtils.getMapVal(mapPkId, "newpkid");
								String newFileName = newPkId + filename;
								System.out.println("prcessUploadAttachmentFileAction.newFileName:" + newFileName);
								//String path = System.getProperty("java.io.tmpdir") + "emailtemp" + File.separator + System.currentTimeMillis() + "_" + filename;
								String path = AppUtils.getAttachFilePath();
								File storefile = new File(path + File.separator + newFileName);
								sysLog.setLogdesc(sysLog.getLogdesc() + ",正在保存附件,storefile为" + storefile.getAbsolutePath());
								bos = new BufferedOutputStream(new FileOutputStream(storefile));
								int c;
								while ((c = bis.read()) != -1) {
									bos.write(c);
									bos.flush();
								}

								sysLog.setLogdesc(sysLog.getLogdesc() + ",保存附件成功,storefile为" + storefile.getAbsolutePath());
								String fileType = getFileTypelinux(storefile.getAbsolutePath());
								String sqlAttachment = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer,inputtime, roleid,remarks)" +
										"   VALUES (" + newPkId + ", " + linkid + ", '" + filename + "', '" + fileType + "' , " + (new BigDecimal(storefile.length())) + ", COALESCE((SELECT code " +
										"FROM sys_user x where x.email1 = '" + from + "' AND isdelete = false limit 1),'email'), NOW(), " + roleid + ", 'email_prcessFileA0');";
								sysEmailDao.executeSQL(sqlAttachment);

								sysLog.setLogdesc(sysLog.getLogdesc() + ",保存sqlAttachment成功,sqlAttachment为" + sqlAttachment);
								//系统设置中开启后，so上传按不同船公司格式提取数据更新到海运委托表中，主要是船名航次，so，etd cls等信息
								if ("Y".equals(ConfigUtils.findSysCfgVal("sys_attachment_so_autogetdata"))) {
									try {
										SysAttachment sysAttachment = new SysAttachment();
										PDFUtil.parsePdf(storefile, linkid, roleid, sysAttachment, sysLog);

										String updateAttachmentsql = "UPDATE sys_attachment set remarks ='email_" + sysAttachment.getRemarks() + "' WHERE id = " + newPkId;
										sysEmailDao.executeSQL(updateAttachmentsql);
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									throw new RuntimeException("pdf附件解析失败,sys_attachment_so_autogetdata:" + ConfigUtils.findSysCfgVal("sys_attachment_so_autogetdata"));
								}
							} else {
								throw new RuntimeException("filename为空");
							}
						}
					}
				} else {
					throw new RuntimeException("解析附件主题失败,附件主题subject_" + subject);
				}
			} catch (Exception e) {
				e.printStackTrace();
				sysLog.setLogdesc(sysLog.getLogdesc() + ",保存附件失败,失败原因为" + e.getMessage());
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (Exception e) {
					}
				}
				if (bos != null) {
					try {
						bos.close();
					} catch (Exception e) {
					}
				}
			}
		}
	}


	private boolean isPrcessFileA1(String precessSubject, String subject, SysLog sysLog) {
		//PIL
		//Booking Receipt Notice (BKG No : SZCG10356000)【Suspected phishing email, please pay attention to password security】
		if (!StrUtils.isNull(subject) && subject.contains("(BKG No : ")) {
			return true;
		}

		//EMC的订舱确认: EVER GLORY 1167-012WE 149114419611 Booking Confirmation<<SBBBSKL9>>
		if (subject.contains("Booking Confirmation") && subject.contains("<<") && subject.contains(">>")) {
			return true;
		}

		//GSL
		//MSC
		//Fw: GOSUGZH0189361 BO7/9/S - Booking Confirmation
		if (subject.contains("Booking Confirmation") && subject.contains(" - ")) {
			return true;
		}

		//[HMM Mailing : Booking Confirmation] Booking : SZPO30307900
		if (subject.contains("Booking Confirmation") && subject.contains("HMM Mailing : ")) {
			return true;
		}

		//MAERSK订舱确认
		//Booking Confirmation : 214286662
		if (subject.contains("Booking Confirmation : ")) {
			return true;
		}

		//HPL
		if (subject.contains("HL-")) {
			return true;
		}

		//CNC
		if (subject.contains("CNC - 订舱确认可供使用")) {
			return true;
		}

		//YML
		if (subject.contains("阳明海运订舱确认")) {
			return true;
		}

		sysLog.setLogdesc(sysLog.getLogdesc() + ",isPrcessFileA1不通过");
		return false;
	}

	private void prcessFileA1(EMailReciveUtil rm, Part part, String subject, SysLog sysLog) throws Exception {
		String from = rm.getFrom();
		if (from.contains("<") && from.contains(">")) {
			from = from.substring(from.indexOf("<") + 1, from.lastIndexOf(">"));
		}


		ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		String thisRolename = "SO";
		sysLog.setLogdesc(sysLog.getLogdesc() + ",prcessFileAOnParsePdf解析keyword成功,解析结果thisRolename为" + thisRolename);


		//保存附件
		String sql = "SELECT id FROM sys_role where name='" + thisRolename.trim() + "' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
		Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String roleid = StrUtils.getMapVal(map, "id");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String filename = "";
		try {
			sysLog.setLogdesc(sysLog.getLogdesc() + ",开始保存附件" + ",roleid为" + roleid);
			if (!StrUtils.isNull(roleid) && part.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) part.getContent();
				for (int y = 0; y < mp.getCount(); y++) {
					BodyPart mpart = mp.getBodyPart(y);
					String dispostion = mpart.getDisposition();
					if ((dispostion != null) && (dispostion.equals(Part.ATTACHMENT) || dispostion.equals(Part.INLINE))) {
						filename = mpart.getFileName();
						if (filename.toLowerCase().indexOf("gb18030") != -1 || filename.toLowerCase().contains("utf-8") || filename.toLowerCase().contains("gbk")) {
							filename = MimeUtility.decodeText(filename);
						}
						bis = new BufferedInputStream(mpart.getInputStream());

						sysLog.setLogdesc(sysLog.getLogdesc() + ",获取附件名称成功,filename为" + filename);
						if (!StrUtils.isNull(filename)) {
							String getidSql = "SELECT getid() AS newPkId";
							Map mapPkId = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(getidSql);
							String newPkId = StrUtils.getMapVal(mapPkId, "newpkid");
							String newFileName = newPkId + filename;
							String path = AppUtils.getAttachFilePath();
							File storefile = new File(path + File.separator + newFileName);
							sysLog.setLogdesc(sysLog.getLogdesc() + ",正在保存附件,storefile为" + storefile.getAbsolutePath());
							bos = new BufferedOutputStream(new FileOutputStream(storefile));
							int c;
							while ((c = bis.read()) != -1) {
								bos.write(c);
								bos.flush();
							}
							sysLog.setLogdesc(sysLog.getLogdesc() + ",保存附件成功,storefile为" + storefile.getAbsolutePath());

							BusShipBooking busShipBooking = new BusShipBooking();
							PDFUtil.parsePdfToSo(storefile, busShipBooking, roleid, sysLog);

							String fileType = getFileTypelinux(storefile.getAbsolutePath());
							String sqlAttachment = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer,inputtime, roleid,remarks)" +
									"   VALUES (" + newPkId + ", " + busShipBooking.getId() + ", '" + filename + "', '" + fileType + "' , " + (new BigDecimal(storefile.length()))
									+ ", COALESCE((SELECT code FROM sys_user x where x.email1 = '" + from + "' AND isdelete = false limit 1),'email'), NOW(), " + roleid + ", 'email_prcessFileA1');";
							sysEmailDao.executeSQL(sqlAttachment);
							sysLog.setLogdesc(sysLog.getLogdesc() + ",保存sqlAttachment成功,sqlAttachment为" + sqlAttachment);
						} else {
							throw new RuntimeException("filename为空");
						}
					}
				}
			} else {
				throw new RuntimeException("保存附件失败,验证不通过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sysLog.setLogdesc(sysLog.getLogdesc() + ",保存附件失败,失败原因为" + e.getMessage());
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean isPrcessFileA2(String precessSubject, String precessContent, String subject, List<String> keyWordStrList, SysLog sysLog) {
		/**
		 * 上传附件:SO:SO(SZPC21030002D)
		 *
		 * Booking Confirmation : SHZ4206713
		 *
		 * Booking Confirmation Note - MSC FLAVIA/UK139A/CHINOOK Bkg# 181AY21G0309241D1;. - CIMC GLOBE SUCCESS LOGISTICS CO., LTD
		 *
		 * GOSUSHH30694389 AR6/4/S - Booking Confirmation
		 *
		 * HL-76422553 aubne
		 */



		//上传附件:SO:SO(SZPC21030002D)
		String thisMark = "上传附件:SO:SO";
		if ((!StrUtils.isNull(precessSubject) && precessSubject.contains(thisMark)) || (!StrUtils.isNull(precessContent) && precessContent.contains(thisMark))) {
			if (!StrUtils.isNull(precessSubject) && (precessSubject.contains(thisMark))) {
				keyWordStrList.add(precessSubject);
			} else if (!StrUtils.isNull(precessContent) && (precessContent.contains(thisMark))) {
				keyWordStrList.add(precessContent);
			}

			String keyword = keyWordStrList.get(0);
			keyword = keyword.substring(keyword.indexOf(thisMark) + thisMark.length() + 1);
			keyword = keyword.substring(0, keyword.indexOf(")"));
			keyWordStrList.clear();
			keyWordStrList.add(keyword);
			return true;
		}

		String keyword = subject;
		if (keyword.contains("Booking Confirmation")) {
			if (keyword.contains(" : ")) {   										//Booking Confirmation : SHZ4206713
				String numberStr = keyword.split(" : ")[1];
				keyWordStrList.add(numberStr.trim());
				return true;
			} else if (keyword.contains("# ")) {      								// Booking Confirmation Note - MSC FLAVIA/UK139A/CHINOOK Bkg# 181AY21G0309241D1;. - CIMC GLOBE SUCCESS LOGISTICS CO., LTD
				String numberStr = keyword.substring(keyword.indexOf("# ") + 2);
				numberStr = numberStr.substring(0, numberStr.indexOf(";"));
				keyWordStrList.add(numberStr.trim());
				return true;
			} else if (keyword.contains(" - ")) {      //GOLD STAR , zim			//GOSUSHH30694389 AR6/4/S - Booking Confirmation
				String numberStr = keyword.substring(0, keyword.indexOf(" "));
				keyWordStrList.add(numberStr.trim());
				return true;
			} else {
				return false;
			}
		}

		//HPL	//HL-76422553 aubne
		String keywordHPL = subject;
		String thisMarkHPL = "HL-";
		if (keywordHPL.contains(thisMarkHPL)) {
			String numberStr = keywordHPL.substring(keywordHPL.indexOf(thisMarkHPL) + thisMarkHPL.length());
			numberStr = numberStr.substring(0, numberStr.indexOf(" "));
			keyWordStrList.add(numberStr.trim());
			return true;
		}

		sysLog.setLogdesc(sysLog.getLogdesc() + ",isPrcessFileA2不通过");
		return false;
	}

	private void prcessFileA2(EMailReciveUtil rm, Part part, List<String> keywordList, SysLog sysLog) throws MessagingException {
		String from = rm.getFrom();
		if (from.contains("<") && from.contains(">")) {
			from = from.substring(from.indexOf("<") + 1, from.lastIndexOf(">"));
		}


		ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		String thisRolename = "SO";
		String numberStr = keywordList.get(0);
		sysLog.setLogdesc(sysLog.getLogdesc() + ",prcessFileA1解析keyword成功,解析结果thisRolename为" + thisRolename + ",numberStr为" + numberStr);

		String[] numberStrSplits = numberStr.split(",");
		for (String thisNumber : numberStrSplits) {
			if (StrUtils.isNull(thisNumber)) {
				continue;
			}

			List<BusShipBooking> list = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("sono = '" + thisNumber + "' AND isdelete = false");
			String sql = "SELECT id FROM sys_role where name='" + thisRolename.trim() + "' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String roleid = StrUtils.getMapVal(map, "id");

			if (list != null && list.size() > 0) {
				//保存附件
				for (BusShipBooking bsb : list) {
					String linkid = String.valueOf(bsb.getId());
					saveFile(linkid, roleid, part, from, sysLog, serviceContext);
				}
			} else {
				//保存附件
				String currentTimestr = String.valueOf(System.currentTimeMillis());
				String linkid = currentTimestr;
				sysLog.setLogdesc(sysLog.getLogdesc() + ",未找到对应so数据");
				saveFile(linkid, roleid, part, from, sysLog, serviceContext);

				//保存未匹配数据
				String dmlSql = "INSERT INTO bus_ship_booking_prcessfile(id,sono,currenttimestr,inputer,inputtime)"
						+ "\nVALUES(getid(),'" + thisNumber + "','" + currentTimestr + "','prcessFileA1',now())";
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
			}
		}
	}

	private boolean isPrcessFileA3(String precessSubject, String subject, SysLog sysLog) {
		//HMM VGM Receipt Confirmation of Input
		if (subject.contains("HMM VGM Receipt Confirmation of Input")) {
			return true;
		}

		//转发: COSCO SHIPPING Lines Booking Confirmation. BKG#: COSU6322791740 CC6068644507
		if (subject.contains("COSCO SHIPPING Lines Booking Confirmation")) {
			return true;
		}

		//NEW GOLDEN SEA SHIPPING Booking Confirmation. BKG#: COAU7236000851 CC4716569970
		if (subject.contains("SEA SHIPPING Booking Confirmation")) {
			return true;
		}

		//2687068141 - OOCL Booking Acknowledgement (SHZ)
		if (subject.contains("OOCL Booking Acknowledgement")) {
			return true;
		}

		//OOCL Acknowledgment of VGM Submission
		if (subject.contains("OOCL Acknowledgment of VGM Submission")) {
			return true;
		}


		sysLog.setLogdesc(sysLog.getLogdesc() + ",isPrcessFileA3不通过");
		return false;
	}

	private void prcessFileA3(EMailReciveUtil rm, Part part, String subject, SysLog sysLog) throws MessagingException {
		String from = rm.getFrom();
		if (from.contains("<") && from.contains(">")) {
			from = from.substring(from.indexOf("<") + 1, from.lastIndexOf(">"));
		}
		String content = rm.getBodyText();

		if (subject.contains("HMM VGM Receipt Confirmation of Input")) {
			String flag = "<span style=\"font-family:Arial,Helvetica,sans-serif;\">";

			String sonoStr = content.substring(StringUtils.ordinalIndexOf(content, flag, 3) + flag.length() + 35);
			sonoStr = sonoStr.substring(0, sonoStr.indexOf("</strong>"));

			String cntnos1Str = content.substring(StringUtils.ordinalIndexOf(content, flag, 22) + flag.length());
			cntnos1Str = cntnos1Str.substring(0, cntnos1Str.indexOf("</span>"));

			String vgmsubtimeStr = content.substring(StringUtils.ordinalIndexOf(content, "Sent:</strong>", 1) + "Sent:</strong>".length());
			vgmsubtimeStr = vgmsubtimeStr.substring(0, vgmsubtimeStr.indexOf("</p>"));

			sysLog.setLogdesc(sysLog.getLogdesc() + ",prcessFileA3解析keyword成功,sonoStr为" + sonoStr + ",cntnos1Str为" + cntnos1Str + ",vgmStr为" + vgmsubtimeStr);

			ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			List<BusShipBooking> busShipBookingList = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("sono = '" + sonoStr + "'  AND isdelete = false");
			if (busShipBookingList != null && busShipBookingList.size() == 1) {
				BusShipBooking oldbsb = busShipBookingList.get(0);
				oldbsb.setCntnos1(cntnos1Str);
				oldbsb.setVgmsubtime(PDFUtil.stringToDate(vgmsubtimeStr));
				serviceContext.busBookingMgrService.saveData(oldbsb);
			} else {
				sysLog.setLogdesc(sysLog.getLogdesc() + ",busShipBookingList长度不为1");
			}
		} else if (subject.contains("COSCO SHIPPING Lines Booking Confirmation") || subject.contains("SEA SHIPPING Booking Confirmation") || subject.contains("OOCL Booking Acknowledgement")) {
			if (from.contains("PLEASE-No-Repl-IRIS-4@OOCL.COM") || from.contains("PROIRIS@COSCON.COM")) {
				String sonoStr = "";
				if (subject.contains("COSCO SHIPPING Lines Booking Confirmation") || subject.contains("SEA SHIPPING Booking Confirmation")) {
					sonoStr = subject.substring(subject.indexOf("BKG#: ") + "BKG#: ".length() + 4).split(" ")[0];
				} else {
					sonoStr = subject.split(" - ")[0];
				}
				sysLog.setLogdesc(sysLog.getLogdesc() + ",prcessFileA3解析keyword成功,sonoStr为" + sonoStr);

				ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
				List<BusShipBooking> busShipBookingList = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("sono = '" + sonoStr + "'  AND isdelete = false");
				if (busShipBookingList != null && busShipBookingList.size() == 1) {
					BusShipBooking oldbsb = busShipBookingList.get(0);
					StringBuffer toStr = rm.getTo();
					oldbsb.setEmailtostr(toStr.toString());
					serviceContext.busBookingMgrService.saveData(oldbsb);

					String sql = "SELECT id FROM sys_role where name='SO' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
					Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					String roleid = StrUtils.getMapVal(map, "id");
					String linkid = String.valueOf(oldbsb.getId());
					saveFile(linkid, roleid, part, from, sysLog, serviceContext);
				} else {
					sysLog.setLogdesc(sysLog.getLogdesc() + ",busShipBookingList长度不为1");
				}
			}
		} else if (subject.contains("OOCL Acknowledgment of VGM Submission")) {
			String sonoStr = subject.substring(subject.indexOf("(Booking No.") + "(Booking No.".length(), subject.indexOf(")"));
			sonoStr = sonoStr.trim();

			String vgmsubtimeStr = content.substring(content.indexOf("Submission Time:") + "Submission Time:".length());
			vgmsubtimeStr = vgmsubtimeStr.substring(0, vgmsubtimeStr.indexOf("CCT"));
			vgmsubtimeStr = vgmsubtimeStr.trim().replace(", ", " ");

			sysLog.setLogdesc(sysLog.getLogdesc() + ",prcessFileA3_OOCL Acknowledgment of VGM Submission解析keyword成功,sonoStr为" + sonoStr + ",vgmsubtimeStr为" + vgmsubtimeStr);

			ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			List<BusShipBooking> busShipBookingList = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("sono = '" + sonoStr + "'  AND isdelete = false");
			if (busShipBookingList != null && busShipBookingList.size() == 1) {
				BusShipBooking oldbsb = busShipBookingList.get(0);
				oldbsb.setVgmsubtime(PDFUtil.stringToDate(vgmsubtimeStr));
				serviceContext.busBookingMgrService.saveData(oldbsb);
				sysLog.setLogdesc(sysLog.getLogdesc() + ",修改oldbsb,vgmsubtime为" + oldbsb.getVgmsubtime());
			} else {
				sysLog.setLogdesc(sysLog.getLogdesc() + ",busShipBookingList长度不为1");
			}
		}
	}



	private void saveFile(String linkid, String roleid, Part part, String from, SysLog sysLog, ServiceContext serviceContext) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String filename = "";
		try {
			sysLog.setLogdesc(sysLog.getLogdesc() + ",开始保存附件,linkid为" + linkid + ",roleid为" + roleid);
			if (!StrUtils.isNull(linkid) && !"null".equals(linkid) && !StrUtils.isNull(roleid) && part.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) part.getContent();
				for (int y = 0; y < mp.getCount(); y++) {
					BodyPart mpart = mp.getBodyPart(y);
					String dispostion = mpart.getDisposition();
					if ((dispostion != null) && (dispostion.equals(Part.ATTACHMENT) || dispostion.equals(Part.INLINE))) {
						filename = mpart.getFileName();
						if (filename.toLowerCase().indexOf("gb18030") != -1 || filename.toLowerCase().contains("utf-8") || filename.toLowerCase().contains("gbk")) {
							filename = MimeUtility.decodeText(filename);
						}
						bis = new BufferedInputStream(mpart.getInputStream());

						sysLog.setLogdesc(sysLog.getLogdesc() + ",获取附件名称成功,filename为" + filename);
						if (!StrUtils.isNull(filename)) {
							String getidSql = "SELECT getid() AS newPkId";
							Map mapPkId = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(getidSql);
							String newPkId = StrUtils.getMapVal(mapPkId, "newpkid");
							String newFileName = newPkId + filename;
							System.out.println("prcessUploadAttachmentFileAction.newFileName:" + newFileName);
							String path = AppUtils.getAttachFilePath();
							File storefile = new File(path + File.separator + newFileName);
							sysLog.setLogdesc(sysLog.getLogdesc() + ",正在保存附件,storefile为" + storefile.getAbsolutePath());
							bos = new BufferedOutputStream(new FileOutputStream(storefile));
							int c;
							while ((c = bis.read()) != -1) {
								bos.write(c);
								bos.flush();
							}

							sysLog.setLogdesc(sysLog.getLogdesc() + ",保存附件成功,storefile为" + storefile.getAbsolutePath());
							String fileType = getFileTypelinux(storefile.getAbsolutePath());
							String sqlAttachment = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer,inputtime, roleid,remarks)" +
									"   VALUES (" + newPkId + ", " + linkid + ", '" + filename + "', '" + fileType + "' , " + (new BigDecimal(storefile.length()))
									+ ", COALESCE((SELECT code FROM sys_user x where x.email1 = '" + from + "' AND isdelete = false limit 1),'email'), NOW(), " + roleid + ", 'email');";
							sysEmailDao.executeSQL(sqlAttachment);
							sysLog.setLogdesc(sysLog.getLogdesc() + ",保存sqlAttachment成功,sqlAttachment为" + sqlAttachment);
						} else {
							throw new RuntimeException("filename为空");
						}
					}
				}
			} else {
				throw new RuntimeException("保存附件失败,验证不通过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sysLog.setLogdesc(sysLog.getLogdesc() + ",保存附件失败,失败原因为" + e.getMessage());
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
		}
	}




	public static String getFileTypelinux(String fileUrl) {
		String fileType = "";
		fileType = getContentTypeByLocal(fileUrl);
		if (StrUtils.isNull(fileType) || "null".equals(fileType)) {
			fileType = getContentTypeByType(fileUrl);
		}
		if (StrUtils.isNull(fileType) || "null".equals(fileType)) {
			fileType = getContentType(fileUrl);
		}
		return fileType;
	}

	public static String getContentTypeByLocal(String fileUrl) {
		String contentType = null;
		Path path = Paths.get(fileUrl);
		try {
			contentType = Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("getContentTypeByLocal, File ContentType is : " + contentType);
		return contentType;
	}

	public static String getContentTypeByType(String fileUrl) {
		String contentType = null;
		try {
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			contentType = fileNameMap.getContentTypeFor(fileUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("getContentTypeByType, File ContentType is : " + contentType);
		return contentType;
	}

	public static String getContentType(String fileUrl) {
		String contentType = null;
		try {
			contentType = new MimetypesFileTypeMap().getContentType(new File(fileUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("getContentType, File ContentType is : " + contentType);
		return contentType;
	}






	public void intoJobsEmail(String[] ids,Long linkid) throws Exception{
		if(ids!=null&&ids.length>0){
			for(int i=0;i<ids.length;i++){
				SysAttachment jobsysAttachment = sysAttachmentDao.findById(Long.parseLong(ids[i]));
				SysAttachment sysAttachment = new SysAttachment();
				sysAttachment.setLinkid(linkid);
				sysAttachment.setFilename(jobsysAttachment.getFilename());


				String filePath = AppUtils.getAttachFilePath() + jobsysAttachment.getId() + jobsysAttachment.getFilename();
				sysAttachment.setFilepath(filePath);//特殊处理，附件文件已经存在，这个标记绝对路径保存，发送邮件的时候直接用这个路径带文件名发送文件
				String url = "/scp/attachment/" + jobsysAttachment.getId() + jobsysAttachment.getFilename();
				sysAttachment.setUrl(url);

				sysAttachment.setContenttype(jobsysAttachment.getContenttype());
				sysAttachment.setFilesize(jobsysAttachment.getFilesize());
				System.out.println(filePath);
				String sql = "SELECT 1 from sys_attachment WHERE linkid = " + sysAttachment.getLinkid() + " and filepath = '"+filePath+"';";
				int count = sysAttachmentDao.executeQuery(sql).size();
				System.out.println(count);
				if(count<1){
					sysAttachmentDao.create(sysAttachment);
				}
			}
		}
	}

	public void delJobsEmail(String[] ids,Long linkid){
		if(ids!=null&&ids.length>0){
			for(int i=0;i<ids.length;i++){
				SysAttachment jobsysAttachment = sysAttachmentDao.findById(Long.parseLong(ids[i]));
				String sql = "DELETE FROM sys_attachment WHERE linkid ="+linkid
				+" AND filename ='"+jobsysAttachment.getFilename()
				+"' AND contenttype='"+jobsysAttachment.getContenttype()+"' AND filesize ="+jobsysAttachment.getFilesize() ;
				sysAttachmentDao.executeSQL(sql);
			}
		}
	}

	/**
	 * 2033 发件箱增加批量追加内容功能
	 */
	public void addStringon(String[] ids,String insertStringv){
		for(String id:ids){
			try{
				SysEmail sysEmail = sysEmailDao.findById(Long.parseLong(id));
				if(sysEmail!=null&&sysEmail.getContent().indexOf("_________________________________________________________")>0){
					StringBuffer sb = new StringBuffer(sysEmail.getContent());
					sysEmail.setContent(sb.insert(sb.indexOf("_________________________________________________________"), insertStringv+"<br/>").toString());
				}else{
					sysEmail.setContent(sysEmail.getContent()+"<br/>"+insertStringv);
				}
				sysEmailDao.createOrModify(sysEmail);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置为自动发送
	 * @param ids
	 */
	public void setIsautosend(String[] ids){
		if(ids!=null&&ids.length>0){
			for(int i=0;i<ids.length;i++){
				SysEmail sysemail = sysEmailDao.findById(Long.parseLong(ids[i]));
				sysemail.setIsautosend(true);
				sysEmailDao.modify(sysemail);
			}
		}
	}

	/**
	 *  删除daynum天以前的邮件
	 * @param daynum 删除多少天以前的
	 */
	public void deleteEmail(int daynum){
		String sql = "DELETE FROM sys_email WHERE inputtime < now() - interval '"+daynum+" days'";
		sysEmailDao.executeSQL(sql);
	}

	public void updateSysEmailFastextEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		SysEmailFastext ddata = new SysEmailFastext();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String subject=String.valueOf(jsonObject.get("subject"));
			String content=String.valueOf(jsonObject.get("content"));

			ddata = sysEmailFastextDao.findById(Long.valueOf(id));
			ddata.setSubject(subject);
			ddata.setContent(content);
			sysEmailFastextDao.modify(ddata);
		}
	}

	public void addSysEmailFastextEditGrid(Object addData) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			SysEmailFastext data = new SysEmailFastext();
			if(jsonObject.get("subject")!=null){
				data.setSubject(String.valueOf(jsonObject.get("subject")));
			}else{
				data.setSubject(null);
			}
			if(jsonObject.get("content")!=null){
				data.setContent(String.valueOf(jsonObject.get("content")));
			}else{
				data.setContent(null);
			}
			sysEmailFastextDao.create(data);
		}
	}

	public void removedSysEmailFastextEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		SysEmailFastext data = new SysEmailFastext();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "DELETE FROM sys_email_fastext WHERE id = ANY(ARRAY["+ids+"])";
			sysEmailFastextDao.executeSQL(sql);
		}
	}

	public void removeSysEmailFastextByid(Long id) {
		String sql = "DELETE FROM sys_email_fastext WHERE id ="+id+";";
		sysEmailFastextDao.executeSQL(sql);
	}
}
