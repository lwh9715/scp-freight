package com.scp.view.module.ship;


import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.bus.BusShipSchedule;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.ship.busshipdelayBean", scope = ManagedBeanScope.REQUEST)
public class BusShipDelayBean extends GridFormView {
	
	@SaveState
	@Accessible
	public BusShipping selectedRowData = new BusShipping();

	
	@SaveState
	@Accessible
	public BusShipSchedule shipschedule = new BusShipSchedule();
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)) {
				this.pkVal = Long.parseLong(id);
				this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao.findById(this.pkVal);
				showTrace();
			}
		}
	}

	/**
	 * 选船期
	 */
	@Action
	public void chooseShip() {
		
		if(this.selectedRowData.getCarrierid() != null) {
			SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getCarrierid());
			this.qryMap.put("carrier", sc.getCode());
		}
		this.qryMap.put("pol", this.selectedRowData.getPol());
		this.qryMap.put("pod", this.selectedRowData.getPod());
		this.qryMap.put("ves", this.selectedRowData.getVessel());
		this.editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "shipschedulePanel");
		this.grid.reload();
	}
	
	@Override
	public void grid_ondblclick() {
		this.selectedRowData.setScheduleid(this.getGridSelectId());
		setShipschedule();
		this.selectedRowData.setScheduleYear(this.shipschedule.getYearno());
		this.selectedRowData.setScheduleMonth(this.shipschedule.getMonthno());
		this.selectedRowData.setScheduleWeek(this.shipschedule.getWeekno());
		this.selectedRowData.setVessel(this.shipschedule.getVes());
		this.selectedRowData.setVoyage(this.shipschedule.getVoy());
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.close();
	}
	
	public void setShipschedule(){
		if(this.selectedRowData.getScheduleid()==null){
			shipschedule = new BusShipSchedule();
		}else{
			shipschedule = serviceContext.shipScheduleService.busShipScheduleDao.findById(selectedRowData.getScheduleid());
		}
	}


	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
//		Integer nextshipcount = this.selectedRowData.getNextshipcount();
//		if(nextshipcount == null) {
//			this.selectedRowData.setNextshipcount(1);
//		} else {
//			this.selectedRowData.setNextshipcount(nextshipcount + 1);
//		}
//		try {
			this.serviceContext.busShippingMgrService.saveData(this.selectedRowData);
			showTrace();
//		} catch(Exception e) {
//			this.selectedRowData.setNextshipcount(nextshipcount);
//		}
		this.pkVal = selectedRowData.getId();
		this.refresh();
		this.alert("ok");
	}

	@Bind
	public UIIFrame traceIframe;
	
	@Action
	public void showTrace() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		} else {
			traceIframe.load("../bus/optrace.xhtml?linkid=" + this.pkVal);
		}
	}
	
}
