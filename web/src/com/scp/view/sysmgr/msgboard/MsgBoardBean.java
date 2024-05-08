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
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.sysmgr.msgboard.msgboardBean", scope = ManagedBeanScope.REQUEST)
public class MsgBoardBean extends GridFormView{

	@Bind
	public UISimpleHtmlEditor  msgContent;
	
	@ManagedProperty("#{sysMsgBoardService}")
	public SysMsgBoardService sysMsgBoardService;
	
	@SaveState
	@Accessible
	public SysMsgboard selectedRowData = new SysMsgboard(); 
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		this.pkVal = getGridSelectId();
		this.selectedRowData = sysMsgBoardService.sysMsgboardDao.findById(this.pkVal);
		msgContent.setValue((String)this.selectedRowData.getMsgcontent());
		msgContent.repaint();
	}

	@Override
	public void add() {
		selectedRowData = new SysMsgboard();
		super.add();
		this.refresh();
		selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
		selectedRowData.setInputtime(Calendar.getInstance().getTime());
		msgContent.setValue((String)this.selectedRowData.getMsgcontent());
		msgContent.repaint();
	}

	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
		selectedRowData.setMsgcontent(this.msgContent.getValue().toString());
		sysMsgBoardService.saveData(selectedRowData);
	}
}
