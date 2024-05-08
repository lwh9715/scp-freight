package com.scp.view.sysmgr.mail;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.LMapBase.MLType;
import com.scp.model.sys.SysEmailTemplate;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.sysmgr.mail.emailtemplateeditBean", scope = ManagedBeanScope.REQUEST)
public class EmailtemplateeditBean extends GridView{
	
	@SaveState
	public Long id;
	
	@SaveState
	@Accessible
	public SysEmailTemplate selectedRowData = new SysEmailTemplate();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			super.applyGridUserDef();
			String id = AppUtils.getReqParam("id");
			String subject = AppUtils.getReqParam("subject");
			MLType mlType = AppUtils.getUserSession().getMlType();
			if(!StrUtils.isNull(id)) {
				this.id = Long.parseLong(id);
				if("-1".equals(id)){
					selectedRowData.setSubject(subject);
				}else{
					selectedRowData = this.serviceContext.sysEmailTemplateService.sysEmailTemplateDao.findById(this.id);
				}
			}
			this.update.markUpdate(UpdateLevel.Data, "editPanel");
			if("en".equals(mlType.toString())){
				Browser.execClientScript("changelang();");
			}
		}
	}
	
	@Action
	protected void saveAction() {
		String content = AppUtils.getReqParam("editor1");
		String namec = AppUtils.getReqParam("namec");
		String namee = AppUtils.getReqParam("namee");
		String subject = AppUtils.getReqParam("subject");
		this.selectedRowData.setContent(content);
		this.selectedRowData.setNamec(namec);
		this.selectedRowData.setNamee(namee);
		this.selectedRowData.setSubject(subject);
		try {
			this.serviceContext.sysEmailTemplateService.saveData(selectedRowData);
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Bind
	@SaveState
	public String seColums;
	
	@Action
	public void seColums_onselect(){
		String js = "editor1.setValue(editor1.getValue()+'"+seColums+"');";//追加到邮件内容里面
		Browser.execClientScript(js);
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
}
