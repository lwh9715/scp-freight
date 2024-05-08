package com.scp.view.module.airtransport;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bus.BusAirFlight;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.airtransport.airflightBean", scope = ManagedBeanScope.REQUEST)
public class AirflightBean  extends GridFormView{
	
	@SaveState
	@Accessible
	public BusAirFlight selectedRowData = new BusAirFlight();
	
	
	

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}

	@Override
	public void del() {
		super.del();
		String[] ids = this.grid.getSelectedIds();

		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.busAirFlightMgrService.removeDate(ids,
					AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.busAirFlightMgrService.busAirFlightDao
				.findById(this.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.busAirFlightMgrService.saveData(selectedRowData);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

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
				String callFunction = "f_imp_airflight";
				String args = -100 + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Action
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}

	@Override
	public void add() {
		super.add();
		selectedRowData = new BusAirFlight();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
}
