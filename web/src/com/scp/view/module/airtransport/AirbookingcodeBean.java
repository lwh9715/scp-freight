package com.scp.view.module.airtransport;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.bus.AirBookcode;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.airtransport.airbookingcodeBean", scope = ManagedBeanScope.REQUEST)
public class AirbookingcodeBean extends GridFormView{

	@SaveState
	@Accessible
	public AirBookcode selectedRowData = new AirBookcode();
	
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		this.userid = AppUtils.getUserSession().getUserid();
	}
	
	
	@Action
	public void qryAdd(){
		selectedRowData = new AirBookcode();
		selectedRowData.setId(0L);
		this.add();
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		serviceContext.airBookcodeMgrService.removeDate(ids, AppUtils.getUserSession().getUsercode());
		this.alert("OK!");
		this.refresh();
	}
	
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.airBookcodeMgrService.airBookcodeDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		serviceContext.airBookcodeMgrService.saveData(selectedRowData);
		
	}
	
}
