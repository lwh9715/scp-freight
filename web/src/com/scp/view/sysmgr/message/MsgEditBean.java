package com.scp.view.sysmgr.message;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysMessage;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

/**
 * 公告
 */
@ManagedBean(name = "pages.sysmgr.message.msgeditBean", scope = ManagedBeanScope.REQUEST)
public class MsgEditBean extends FormView{
	
	@SaveState
	@Accessible
	public SysMessage selectedRowData = new SysMessage();

	@Bind(id = "msgsubj")
	@SaveState
	public String msgsubj;

	@Bind(id = "ckeditor")
	@SaveState
	public String ckeditor;
	
	@Bind(id = "msgtype")
	@SaveState
	public String msgtype;
	
	@Bind(id = "messageid")
	@SaveState
	private Long messageid;
	
	@SaveState
	private String modulecode;
	

	// 页面加载时执行方法
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(!StrUtils.isNull(AppUtils.getReqParam("modulecode"))){
				modulecode = AppUtils.getReqParam("modulecode");
			}
			String id = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)){
				this.selectedRowData = this.serviceContext.sysMessageService.sysMessageDao.findById(Long.valueOf(id));
				this.pkVal = selectedRowData.getId();
				msgsubj = selectedRowData.getTitle();
				ckeditor = selectedRowData.getContent();
				msgtype = selectedRowData.getMsgtype();
				messageid = this.pkVal;
				
				update.markUpdate(UpdateLevel.Data, "messageid");
				update.markUpdate(UpdateLevel.Data, "msgtype");
				update.markUpdate(UpdateLevel.Data, "msgsubj");
				update.markUpdate(UpdateLevel.Data, "ckeditor");
				//Browser.execClientScript("setValue('"+ckeditor+"');");
				
			}else {
				add();
			}
		}
	}
	@Action
	public void add() {
		this.pkVal = -1l;
		selectedRowData = new SysMessage();
		selectedRowData.setModulecode(modulecode);
		msgsubj = "";
		ckeditor = "";
		msgtype = "";
		update.markUpdate(UpdateLevel.Data, "msgsubj");
		update.markUpdate(UpdateLevel.Data, "ckeditor");
		update.markUpdate(UpdateLevel.Data, "msgtype");
	}
	
	
	@Action
	public void saveAction() {
		this.selectedRowData.setTitle(AppUtils.getReqParam("msgsubj"));
		this.selectedRowData.setIssystem(false);
		this.selectedRowData.setMsgtype(AppUtils.getReqParam("msgtype"));
		this.selectedRowData.setContent(AppUtils.getReqParam("editor1"));
		this.serviceContext.sysMessageService.saveData(selectedRowData);
		this.pkVal = selectedRowData.getId();
		super.save();
		MessageUtils.alert("OK!");
	}
	@Override
	public void refresh() {
		selectedRowData = this.serviceContext.sysMessageService.sysMessageDao.findById(this.pkVal);
		
		msgsubj = selectedRowData.getTitle();
		ckeditor = selectedRowData.getContent();
		msgtype = selectedRowData.getMsgtype();
		
		update.markUpdate(UpdateLevel.Data, "msgsubj");
		update.markUpdate(UpdateLevel.Data, "ckeditor");
		update.markUpdate(UpdateLevel.Data, "msgtype");
		super.refresh();
	}
	
	
	@Bind
	public UIIFrame attachmentIframe;
	
	
	@Action
	public void showAttachmentIframe() {
		try {
			if(this.pkVal==-1l){
				MessageUtils.alert("请先保存消息!");
				return;
			}else{
				attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.pkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow getmsgtypeWindow;
	
	@Bind
	private UIIFrame getmsgtypeIFrame;
	
	
	@Action
	public void getmsgtype() {
		if(this.pkVal==-1l){
			MessageUtils.alert("请先保存消息!");
			return;
		}
		String url = "./getmsguser.xhtml?messageid="+this.pkVal;
		getmsgtypeIFrame.setSrc(url);
		update.markAttributeUpdate(getmsgtypeIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, getmsgtypeWindow);
		getmsgtypeWindow.show();
	}
	
	@Action
	public void deleteuser() {
		this.pkVal = -1l;
		String sql = "DELETE FROM sys_message_ref WHERE messageid = "+messageid+"";
		this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		//MessageUtils.alert("OK");
		Browser.execClientScript("delectOK()");
	}
	
	
}
