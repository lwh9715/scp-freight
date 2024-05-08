package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusShipjoingoods;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.busshipjoingoodsBean", scope = ManagedBeanScope.REQUEST)
public class BusShipJoingoodsBean extends GridFormView {

	@SaveState
	@Accessible
	public BusShipjoingoods selectedRowData = new BusShipjoingoods();
	
	
	@SaveState
	@Accessible
	public Long shipjoin;
	
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			shipjoin=Long.valueOf(id);
			qryMap.put("shipjoin$", this.shipjoin);
		}
	}

	
	@Override
	public void add() {
		selectedRowData = new BusShipjoingoods();
		selectedRowData.setShipjoin(this.shipjoin);
		super.add();
	}

	

	

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选要删除的行！");
			return;
		}
		for(String id : ids) {
			try {
				serviceContext.busShipjoingoodsMgrService.removeDate(Long.valueOf(id));
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.alert("OK");
		this.grid.reload();
	}

	

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.busShipjoingoodsMgrService.busShipjoingoodsDao.findById(this
				.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busShipjoingoodsMgrService.saveData(selectedRowData);
		alert("OK");
		refresh();
	}

	
}
