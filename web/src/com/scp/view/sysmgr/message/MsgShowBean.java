package com.scp.view.sysmgr.message;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysMessage;
import com.scp.util.AppUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.sysmgr.message.msgshowBean", scope = ManagedBeanScope.REQUEST)
public class MsgShowBean extends FormView {
	
	
	
	@SaveState
	@Accessible
	public SysMessage selectedRowData = new SysMessage();
	
	@Bind
	@SaveState
	private String title;
	
	@Bind
	@SaveState
	private String inputer;
	
	@Bind
	@SaveState
	private Long linkid;
	
	@Bind
	@SaveState
	private String content;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String id = (String) AppUtils.getReqParam("id");
			this.selectedRowData = this.serviceContext.sysMessageService.sysMessageDao.findById(Long.valueOf(id));
			
			content = (String)this.selectedRowData.getContent();
			inputer = (String)this.selectedRowData.getInputer();
			title = (String)this.selectedRowData.getTitle();
			linkid = this.selectedRowData.getId();
			
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			
			attachmentIframe.load(AppUtils.getContextPath()+ "/pages/module/common/attachshow.xhtml?linkid="+ id);
		}
	}
	
	
	@Bind
	public UIIFrame attachmentIframe;

}
