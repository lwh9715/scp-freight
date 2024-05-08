package com.scp.view.module.bus;

import java.io.IOException;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.bus.BusShipSchedule;
import com.scp.service.bus.ShipScheduleService;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.bus.shipschedulemgrBean", scope = ManagedBeanScope.REQUEST)
public class ShipScheduleMgrBean extends GridFormView {
	
	@ManagedProperty("#{shipScheduleService}")
	public ShipScheduleService shipScheduleService;
	
	@SaveState
	@Accessible
	public BusShipSchedule selectedRowData = new BusShipSchedule();
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}

	

	@Override
	protected void doServiceFindData() {
		selectedRowData = shipScheduleService.busShipScheduleDao.findById(this.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		shipScheduleService.saveData(selectedRowData);
	}
	
	@Action
	public void importBatch() {
		year = "2013";
		month = "12";
		importWindow.show();
	}
	
	@Action
	public void saveBatch() {
//		ApplicationUtils.debug(year);
//		ApplicationUtils.debug(month);
//		ApplicationUtils.debug(detail);
//		ApplicationUtils.debug(shipline);
//		
		if(StrUtils.isNull(year) || StrUtils.isNull(month) || StrUtils.isNull(detail) || StrUtils.isNull(shipline)) {
			this.alert("year|month|detail|shipline 不能为空！");
			return;
		}
		try {
			shipScheduleService.saveDataBatch(shipline,year,month,detail);
			this.alert("OK!");
			detail = "";
			importWindow.close();
		} catch (IOException e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Bind
	public UIWindow importWindow;
	
	@Bind
	public String year;
	
	@Bind
	public String month;
	
	@Bind
	public String shipline;
	
	
	@Bind
	public String detail;
	
}
