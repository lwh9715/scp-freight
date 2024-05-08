package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UISimpleHtmlEditor;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysMsgboard;
import com.scp.service.sysmgr.SysMsgBoardService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.ship.shipmsgboardBean", scope = ManagedBeanScope.REQUEST)
public class ShipMsgBoardBean extends GridFormView{

	@Bind
	public UISimpleHtmlEditor  msgContent;
	
	@ManagedProperty("#{sysMsgBoardService}")
	public SysMsgBoardService sysMsgBoardService;
	
	@SaveState
	@Accessible
	public SysMsgboard selectedRowData = new SysMsgboard(); 
	
	@SaveState
	@Accessible
	public Long jobid;
	
	@Bind
	@SaveState
	@Accessible
	public String msgcontent1;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("jobid").trim();
			jobid=Long.valueOf(id);
			qryMap.put("linkid$", this.jobid);
			
		}
	}
	


	@Override
	public void add() {
		selectedRowData =  new SysMsgboard();
		selectedRowData.setLinkid(this.jobid);
		this.pkVal = -1L;
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel2");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	
	
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = sysMsgBoardService.sysMsgboardDao.findById(this
				.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		sysMsgBoardService.saveData(selectedRowData);
		this.editWindow.close();
		refresh();
		
	}
	
	
	
	@Override
	public void del() {
		
		try {
			sysMsgBoardService.removeDate(this.getGridSelectId() , AppUtils.getUserSession().getUsercode());
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Override
	public void grid_ondblclick(){
//		this.pkVal = getGridSelectId();
//		doServiceFindData();
//		update.markUpdate(true, UpdateLevel.Data, "editPanel");
//		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Action
	public void saveContent(){
		String content=AppUtils.getReqParam("content").trim();
		if(StrUtils.isNull(content)){
			MessageUtils.alert("please input something");
			return;
		}else{
			this.selectedRowData=new SysMsgboard();
			selectedRowData.setLinkid(this.jobid);
			selectedRowData.setMsgcontent(content);
			sysMsgBoardService.saveData(selectedRowData);
			this.msgcontent1="";
			this.grid.reload();
			this.update.markUpdate(UpdateLevel.Data, "editPanel");
			update.markUpdate(true, UpdateLevel.Data, "msgcontent1");
			
		}
		
	}
	

}
