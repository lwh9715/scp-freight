package com.scp.view.sysmgr.msgboard;

import java.util.Calendar;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UISimpleHtmlEditor;

import com.scp.model.sys.SysMsgboard;
import com.scp.service.sysmgr.SysMsgBoardService;
import com.scp.util.AppUtils;
import com.scp.view.comp.FormView;
@ManagedBean(name = "pages.sysmgr.msgboard.msgboardresponseBean", scope = ManagedBeanScope.REQUEST)
public class MsgBoardResponseBean extends FormView{

	@Bind
	public UISimpleHtmlEditor  msgContent;
	
	@ManagedProperty("#{sysMsgBoardService}")
	public SysMsgBoardService sysMsgBoardService;
	
	@SaveState
	@Accessible
	public SysMsgboard selectedRowData = new SysMsgboard(); 
	
	@Bind
	@SaveState
	public String parentid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			parentid = AppUtils.getReqParam("id");
			this.add();
		}
	}

//	@Override
//	public void afterShow() {
//		super.afterShow();
////		this.msgContent = (String)this.data.get("content");
//		String content = (String)this.data.get("msgcontent");
//		msgContent.setValue(content);
//		msgContent.repaint();
//		
//		this.update.markUpdate(UpdateLevel.Data, "msgContent");
//		Browser.execClientScript("msgContentJsVar.setValue('"+content+"')");
//	}

	@Override
	public void add() {
		selectedRowData = new SysMsgboard();
		super.add();
		this.refresh();
		selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
		selectedRowData.setInputtime(Calendar.getInstance().getTime());
		selectedRowData.setParentid(Long.parseLong(parentid));
		msgContent.setValue((String)this.selectedRowData.getMsgcontent());
		msgContent.repaint();
	}
	

	@Override
	public void save() {
		selectedRowData.setMsgcontent(this.msgContent.getValue().toString());
		sysMsgBoardService.saveData(selectedRowData);
		super.save();
	}
	
	
}
