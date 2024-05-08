package com.scp.view.sysmgr.mail;

import java.io.File;
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

@ManagedBean(name = "pages.sysmgr.mail.emailsentBean", scope = ManagedBeanScope.REQUEST)
public class EmailSentBean extends GridFormView {
	
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
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysEmailService.sysEmailDao.findById(this.pkVal);
		//AppUtils.debug(selectedRowData.getContent());
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.sysEmailService.saveData(selectedRowData);
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		editIframe.load("./emailedit.xhtml?type=T&id=" + this.pkVal);
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
	
	@Action
	public void delemailbefor(){
		try {
			serviceContext.sysEmailService.deleteEmail(10);
			this.grid.reload();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
