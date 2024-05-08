package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipHold;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.busshipholdBean", scope = ManagedBeanScope.REQUEST)
public class BusShipHoldBean extends GridFormView {

	@SaveState
	@Accessible
	public BusShipHold selectedRowData = new BusShipHold();
	
	@SaveState
	@Accessible
	public Long linkid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("linkid").trim();
			linkid=Long.valueOf(id);
			qryMap.put("linkid$", this.linkid);
			add();
		}
	}

	@Override
	public void grid_ondblclick(){
		
	}
	
	@Override
	public void add(){
		this.pkVal = -1L;
		selectedRowData = new BusShipHold();
		selectedRowData.setLinkid(this.linkid);
		selectedRowData.setLinktbl("fina_jobs");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.busShipHoldMgrService.busShipHoldDao.findById(this
				.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		try {
			if(selectedRowData.getId() !=0){
				MessageUtils.alert("不能修改以往状态");
			}else{
				serviceContext.busShipHoldMgrService.saveData(selectedRowData);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}



	@Override
	public void refresh() {
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void holdbill(){
		selectedRowData.setHoldstate("H");
		selectedRowData.setHoldtype("B");
		serviceContext.busShipHoldMgrService.saveData(selectedRowData);
		this.refresh();
		this.add();
	}
	
	@Action
	public void passbill(){
		selectedRowData.setHoldstate("R");
		selectedRowData.setHoldtype("B");
		serviceContext.busShipHoldMgrService.saveData(selectedRowData);
		this.refresh();
		this.add();
		
	}
	
	@Action
	public void holdgoods(){
		selectedRowData.setHoldstate("H");
		selectedRowData.setHoldtype("G");
		serviceContext.busShipHoldMgrService.saveData(selectedRowData);
		this.refresh();
		this.add();
	}
	
	@Action
	public void passgoods(){
		selectedRowData.setHoldstate("R");
		selectedRowData.setHoldtype("G");
		serviceContext.busShipHoldMgrService.saveData(selectedRowData);
		this.refresh();
		this.add();
	}
	
	
}
