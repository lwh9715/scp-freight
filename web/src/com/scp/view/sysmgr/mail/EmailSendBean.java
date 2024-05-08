package com.scp.view.sysmgr.mail;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysEmail;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.web.view.sysmgr.LogBean;

@ManagedBean(name = "pages.sysmgr.mail.emailsendBean", scope = ManagedBeanScope.REQUEST)
public class EmailSendBean extends GridFormView {
	
	@SaveState
	@Accessible
	public SysEmail selectedRowData = new SysEmail();
	
	@Bind
	public UIIFrame editIframe;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
		}
	}
	
	@Action
	public void send(){
		StringBuffer sb = new StringBuffer("emailsendBean_send开始");
		try {
			String ids[] = this.grid.getSelectedIds();
			if(ids == null || ids.length < 0) {
				MessageUtils.alert("Please choose one!");
				return;
			}
			for (String id : ids) {
				sb.append("，当前id为").append(id);
				// 附件数组
				Map<String,String> attachments = null;
				// 发送前查看是否有附件
				List<SysAttachment> sa = serviceContext.sysAttachmentService.sysAttachmentDao.findAllByClauseWhere("linkid = " + id);
				if(sa != null && sa.size() > 0) {
					attachments = new HashMap<String, String>();
					for(SysAttachment att : sa) {
						// 拼成完整路径(包含文件名)
						String fileurl = "";
						String fileurl0 = AppUtils.getAttachFilePath() + att.getId() + att.getFilename();
						String fileurl1 =  AppUtils.getAttachFilePath() + att.getFilename();
						if (new File(fileurl0).exists()) {
							fileurl = fileurl0;
							sb.append(",fileurl0存在");
						}
						if (new File(fileurl1).exists()) {
							fileurl = fileurl1;
							sb.append(",fileurl1存在");
						}

						sb.append(",fileurl为").append(fileurl);
						attachments.put(att.getFilename(), fileurl);
					}
				}
				this.serviceContext.sysEmailService.sendEmailHtml(Long.valueOf(id),attachments);
			}
			this.refresh();
			MessageUtils.alert("OK!");
		} catch (java.lang.NullPointerException e){
			MessageUtils.alert("Error,please check! 错误,请检查!");
		} catch (javax.mail.SendFailedException e){
			MessageUtils.alert("501 Bad address syntax,please check! 收件人地址错误,请检查!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		LogBean.insertLog(sb);
	}
	
	@Override
	public void add() {
		selectedRowData = new SysEmail();
//		String email = AppUtils.getUserSession().getConfigVal("email1");
		super.add();
		editIframe.load("./emailedit.xhtml?id=" + this.pkVal);
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		editIframe.load("./emailedit.xhtml?id=" + this.pkVal);
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysEmailService.sysEmailDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.sysEmailService.saveData(selectedRowData);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap){
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		String usercode =  AppUtils.getUserSession().getUsercode().toUpperCase();
		if("DEV".equals(usercode) || "DEMO".equals(usercode)) {
		}else{
			this.qryMap.put("inputer",usercode);
		}
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String deleteSql = "";
		
		try {
			for(String id : ids) {
				// 获取邮件中的附件
				List<SysAttachment> sa = serviceContext.sysAttachmentService.sysAttachmentDao
					.findAllByClauseWhere("linkid = " + this.pkVal);
				// 1.如果有附件,先删除本地附件文件
				if(sa != null) {
					for(SysAttachment att : sa) {
						File f = new File(AppUtils.getAttachFilePath() + att.getId() + att.getFilename());
						if(f.isFile() && f.exists()) {
							f.delete();
						}
					}
				}
				
				// 拼接删除语句
				deleteSql += "\nUPDATE sys_attachment SET isdelete = TRUE WHERE linkid = " + Long.parseLong(id) + ";";
				// 删除邮件记录
				this.serviceContext.sysEmailService.removeDate(Long.parseLong(id));
			}
			if(!StrUtils.isNull(deleteSql)) {
				// 2.删除数据库表中附件纪录
				serviceContext.sysAttachmentService.sysAttachmentDao.executeSQL(deleteSql);
			}
			
			MessageUtils.alert("OK");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public String insertStringv;
	
	/**
	 * 2033 发件箱增加批量追加内容功能 有_________________________________________________________就加在上面，没有就加上最下面
	 */
	@Action
	public void addStringon(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		serviceContext.sysEmailService.addStringon(ids,insertStringv);
		alert("OK");
	}
	
	@Action
	public void setIsautosend(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			serviceContext.sysEmailService.setIsautosend(ids);
			this.grid.reload();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
