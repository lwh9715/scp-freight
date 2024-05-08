package com.scp.view.base;

import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.developer.util.FacesUtils;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.util.Flash;
@ManagedBean(name = "messageTipBean", scope = ManagedBeanScope.REQUEST)
public class MessageTipBean{
	
	@Inject
    private PartialUpdateManager update;
	
	@Bind
	public String titleMsg;

	@Bind
	public String message;
	
	@Bind
	public String messageDetail;

	@BeforeRender 
	public void beforeRender(boolean isPostback){
		if(!isPostback){
			Flash flash = FacesUtils.getFlash();
			
			titleMsg = String.valueOf(flash.get("TitleMsg"));
			message = String.valueOf(flash.get("Message"));
			messageDetail  = String.valueOf(flash.get("MessageDetail"));
			
			flash.remove("TitleMsg");
			flash.remove("Message");
			flash.remove("MessageDetail");
			
			update.markUpdate(UpdateLevel.Data, "message");
			update.markUpdate(UpdateLevel.Data, "messageDetail");
		}
	}
	
}
