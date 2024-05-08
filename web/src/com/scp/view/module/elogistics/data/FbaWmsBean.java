package com.scp.view.module.elogistics.data;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.data.DatWarehouse;
import com.scp.service.data.WarehouseMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.elogistics.data.fbawmsBean", scope = ManagedBeanScope.REQUEST)
public class FbaWmsBean extends GridFormView{
	@ManagedProperty("#{warehouseMgrService}")
	public WarehouseMgrService warehouseMgrService;
	
	@SaveState
	@Accessible
	public DatWarehouse selectedRowData = new DatWarehouse();
	
	

	@Override
	protected void doServiceFindData() {
		selectedRowData = warehouseMgrService.datWarehouseDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		warehouseMgrService.saveData(selectedRowData);
		
		this.alert("OK");
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}
	
	
	@Action
	public void add() {
		super.grid_ondblclick();
		String winId = "_warehousearea";
		String url = "./warehousearea.xhtml";
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_warehouse_fba";
				String args = "'"+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args , false);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
}
