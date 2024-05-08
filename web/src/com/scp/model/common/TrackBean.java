package com.scp.model.common;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusGoodstrack;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.common.trackBean", scope = ManagedBeanScope.REQUEST)
public class TrackBean extends GridFormView {
	
	
	@SaveState
	@Accessible
	public BusGoodstrack selectedRowData = new BusGoodstrack();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			this.add();
		}
	}

	@Override
	public void add() {
		selectedRowData = new BusGoodstrack();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.busGoodstrackMgrService.busGoodstrackDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		this.serviceContext.busGoodstrackMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	
	@Override
	public void del() {
		super.del();
	}

}
