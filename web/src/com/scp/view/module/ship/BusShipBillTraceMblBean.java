package com.scp.view.module.ship;

import java.util.Calendar;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusShipBill;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.ship.busshipbilltracemblBean", scope = ManagedBeanScope.REQUEST)
public class BusShipBillTraceMblBean extends FormView {

	@SaveState
	@Accessible
	public BusShipBill selectedRowData = new BusShipBill();

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String billid = AppUtils.getReqParam("billid");
		if(!StrUtils.isNull(billid)){
			selectedRowData = this.serviceContext.busShipBillMgrService.busShipBillDao.findById(Long.parseLong(billid));
			
			
			ismake = selectedRowData.getIsmake();
			isadjust = selectedRowData.getIsadjust();
			isconfirm = selectedRowData.getIsconfirm();
			isprint = selectedRowData.getIsprint();
			issign = selectedRowData.getIssign();
			iscomplete = selectedRowData.getIscomplete();
			
			this.refresh();
		}
	}
	
	@Bind
	private Boolean ismake = false;
	
	@Bind
	public Boolean isadjust = false;
	
	@Bind
	public Boolean isconfirm = false;
	
	@Bind
	public Boolean isprint = false;
	
	@Bind
	public Boolean issign = false;
	
	@Bind
	public Boolean iscomplete = false;
	

	@Override
	public void refresh() {
		super.refresh();
	}

	@Override
	public void save() {
		super.save();
		this.serviceContext.busShipBillMgrService.busShipBillDao.modify(selectedRowData);
	}

	
	@Action
	public void ismake_oncheck(){
		selectedRowData.setIsmake(ismake);
		selectedRowData.setDatemake(ismake?Calendar.getInstance().getTime():null);
		selectedRowData.setUsermake(ismake?AppUtils.getUserSession().getUsercode():null);
		refresh();
		this.save();
	}
	
	@Action
	public void isadjust_oncheck(){
		selectedRowData.setIsadjust(isadjust);
		selectedRowData.setDateadjust(isadjust?Calendar.getInstance().getTime():null);
		selectedRowData.setUseradjust(isadjust?AppUtils.getUserSession().getUsercode():null);
		refresh();
		this.save();
	}
	
	@Action
	public void isconfirm_oncheck(){
		selectedRowData.setIsconfirm(isconfirm);
		selectedRowData.setDateconfirm(isconfirm?Calendar.getInstance().getTime():null);
		selectedRowData.setUserconfirm(isconfirm?AppUtils.getUserSession().getUsercode():null);
		refresh();
		this.save();
	}
	
	@Action
	public void isprint_oncheck(){
		selectedRowData.setIsprint(isprint);
		selectedRowData.setDateprint(isprint?Calendar.getInstance().getTime():null);
		selectedRowData.setUserprint(isprint?AppUtils.getUserSession().getUsercode():null);
		refresh();
		this.save();
	}
	
	@Action
	public void issign_oncheck(){
		selectedRowData.setIssign(issign);
		selectedRowData.setDatesign(issign?Calendar.getInstance().getTime():null);
		selectedRowData.setUsersign(issign?AppUtils.getUserSession().getUsercode():null);
		refresh();
		this.save();
	}
	
	@Action
	public void iscomplete_oncheck(){
		selectedRowData.setIscomplete(iscomplete);
		selectedRowData.setDatecomplete(iscomplete?Calendar.getInstance().getTime():null);
		selectedRowData.setUsercomplete(iscomplete?AppUtils.getUserSession().getUsercode():null);
		refresh();
		this.save();
	}
	
}
