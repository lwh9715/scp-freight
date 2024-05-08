package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusShipBooking;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.bookingBean", scope = ManagedBeanScope.REQUEST)
public class BusBooking extends GridView {
	
	@SaveState
	@Accessible
	public BusShipBooking selectedRowData = new BusShipBooking();
	
	

	@Override
	public void beforeRender(boolean isPostBack) {
		// TODO Auto-generated method stub
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}


	@Action
	public void add() {
		String winId = "_edit_booking";
		String url = "./bookingedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		this.selectedRowData=serviceContext.busBookingMgrService.busShipBookingDao.findById(this.getGridSelectId());
		super.grid_ondblclick();
		String winId = "_edit_booking";
		String url = "../salesmgr/booking.xhtml?id="+this.selectedRowData.getId()+"&r="+AppUtils.base64Encoder("edit");
		AppUtils.openWindow(winId, url);
	}
	
	public Long getBookingid(Long id){
		
		return id;
		
	}
	
//	@Action
//	public void load() {
//
//		
//		String url = AppUtil.getContextPath()
//				+ "/pages/module/ship/delaydropcont.xhtml";
//		dtlIFrame.setSrc(url);
//		update.markAttributeUpdate(dtlIFrame, "src");
//		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
//		dtlDialog.show();
//	}
//	
//	@Bind
//	private UIWindow dtlDialog;
//	@Bind
//	private UIIFrame dtlIFrame;
//
//	@Action(id = "dtlDialog", event = "onclose")
//	private void dtlEditDialogClose() {
//		refresh();
//	}
	
	
	@Action
	public void load() {
		String winId = "_edit_delaydropcont";
		String url = AppUtils.getContextPath()
		+ "/pages/module/ship/delaydropcont.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
}
