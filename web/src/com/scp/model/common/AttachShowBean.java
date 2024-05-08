package com.scp.model.common;

import java.io.InputStream;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysAttachment;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.common.attachshowBean", scope = ManagedBeanScope.REQUEST)
public class AttachShowBean extends GridFormView {

	@Bind
	@SaveState
	private Long dPkVal;
	
	@Bind
	@SaveState
	private String linkid;
	
    
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.linkid = AppUtils.getReqParam("linkid");
			this.qryMap.put("linkid$", linkid);
			this.update.markUpdate(UpdateLevel.Data,"linkid");
			this.grid.reload();
		}
	}
	

	@Override
	public void grid_ondblclick(){
		this.dPkVal = this.getGridSelectId();
		this.update.markUpdate(UpdateLevel.Data,"dPkVal");
    	Browser.execClientScript("downloadBtn.fireEvent('submit');");
	}
	
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String contentType;
    
    @Action
	public void download(){
    	this.dPkVal = this.getGridSelectId();
    	if(this.dPkVal == -1) {
    		MessageUtils.alert("please choose one!");
    		return;
    	}
    	SysAttachment sysAttachment = this.serviceContext.sysAttachmentService.sysAttachmentDao.findById(dPkVal);
		this.fileName = sysAttachment.getFilename();
		this.contentType = sysAttachment.getContenttype();
    	this.update.markUpdate(UpdateLevel.Data,"dPkVal");
	}
	

    
    @Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
    	try {
			return this.serviceContext.attachmentService.readFile(dPkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }



	@Override
	protected void doServiceFindData() {
		
	}


	@Override
	protected void doServiceSave() {
		
	}
}