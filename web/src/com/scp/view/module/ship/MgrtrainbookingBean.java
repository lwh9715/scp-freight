package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.data.DatPort;
import com.scp.model.ship.MgrTrainBooking;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.ship.mgrtrainbookingBean", scope = ManagedBeanScope.REQUEST)
public class MgrtrainbookingBean extends GridFormView{
	
	
	@SaveState
	@Accessible
	public MgrTrainBooking selectedRowData = new MgrTrainBooking();
	
	@Bind
	@SaveState
	public String trainno;
	
	@Action
	public void addForm(){
		this.pkVal = -1L;
		this.selectedRowData = new MgrTrainBooking();
		selectedRowData.setBktype("LCL");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
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
				//this.serviceContext.priceTrainService.removePriceByPriceName(pricename);
				String callFunction = "f_imp_bus_booking_train";
				String args ="'"+AppUtils.getUserSession().getUsercode()+"'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Override
	public void grid_ondblclick(){
		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		Browser.execClientScript("hiddenHQ()");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Override
	public void refreshForm() {
		if(selectedRowData.getPolid()!=null &&selectedRowData.getPodid()!=null){
			DatPort portPol = serviceContext.portyMgrService.datPortDao.findById(this.selectedRowData.getPolid());
			DatPort portPod = serviceContext.portyMgrService.datPortDao.findById(this.selectedRowData.getPodid());
			Browser.execClientScript("$('#pol1_input').val('"+portPol.getNamee()+"')");
			Browser.execClientScript("$('#pod1_input').val('"+portPod.getNamee()+"')");
			Browser.execClientScript("bktype.setValue('"+selectedRowData.getBktype()+"')");
		}else{
			Browser.execClientScript("$('#pol1_input').val('')");
			Browser.execClientScript("$('#pod1_input').val('')");
			//Browser.execClientScript("$('#bktype').val('')");
		}	
	}
	
	@Action
	public void saveForm(){
		try {
			doServiceSave();
			if(editWindow != null)editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
	}
	
	@Action
	public void close(){
		try {
			if(editWindow != null)editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
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
			this.serviceContext.mgrTrainBookingService.removeDate(ids,
					AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			
		}
	}


	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.mgrTrainBookingService.mgrTrainBookingDao.findById(pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.mgrTrainBookingService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			this.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public String polid;
	
	@Bind
	@SaveState
	public String podid;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(trainno)){
			qry += "\nAND t.trainno LIKE '%"+trainno+"%'";
		}
		
		if(!StrUtils.isNull(polid)){
			qry += "\nAND t.polid = "+polid+"";
		}
		if(!StrUtils.isNull(podid)){
			qry += "\nAND t.podid = "+podid+"";
		}
		m.put("qry", qry);
		return m;
	}
}
