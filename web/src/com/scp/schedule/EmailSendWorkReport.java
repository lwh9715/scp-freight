package com.scp.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysEmail;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

/**
 * 
 * 自动发送邮件
 * 
 * @author Neo
 * 
 */
public class EmailSendWorkReport {

	private static boolean isRun = false;

	public void execute() throws Exception {
		//AppUtils.debug("EmailSendWorkReport Start:" + new Date());
		if (isRun) {
			System.out
					.println("@@@ EmailSendWorkReport wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			send();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}

	private void send() throws Exception {
		String whereSql = "emailtype = 'S' AND issent = FALSE AND isdelete = FALSE AND COALESCE(linktbl,'') LIKE '%工作报告%' AND linkid IS NOT NULL";
		List<SysEmail> emails = AppUtils.getServiceContext().sysEmailService.sysEmailDao
				.findAllByClauseWhere(whereSql);

		for (SysEmail se : emails) { // 循环发送需要自动发送的邮件
			// 附件数组
			Map<String, String> attachments = null;
			// 发送前查看是否有附件
			List<SysAttachment> sa = AppUtils.getServiceContext().sysAttachmentService.sysAttachmentDao
					.findAllByClauseWhere("linkid = " + se.getId());
			if (sa != null && sa.size() > 0) {
				attachments = new HashMap<String, String>();
				for (SysAttachment att : sa) {
					// 拼成完整路径(包含文件名)
					attachments.put(att.getFilename(), AppUtils
							.getAttachFilePath()
							+ att.getId() + att.getFilename());
				}
			}
			try { // 出现异常的邮件，标记isautosend为false，下次不再发送，纪录日志
				String sql = "SELECT timeend FROM oa_workreport WHERE isdelete = FALSE AND id = "
						+ se.getLinkid();
				Date now  = new Date();
				Map map = AppUtils.getServiceContext().daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date endtime = dateFormat.parse(map.get("timeend").toString());// 根据截止时间来发送邮件
				String time = dateFormat.format(endtime);
				
				String currentTime = dateFormat.format(now);
				if(time.equals(currentTime)){//此处判断时间是否相等
					AppUtils.getServiceContext().sysEmailService.sendEmailHtml(se
							.getId(), attachments);
					String sql2 = "UPDATE oa_workreport SET isemailsent = TRUE WHERE " + se.getLinkid();
					AppUtils.getServiceContext().sysAttachmentService.sysAttachmentDao
					.executeSQL(sql2);
				}
			} catch (Exception e) {
				MessageUtils.reportException("email send error,please check", e);
			}
		}

	}
}
