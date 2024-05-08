package com.scp.view.module.ship;

import java.util.Calendar;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusCustomsTaxret;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.buscustomstaxretBean", scope = ManagedBeanScope.REQUEST)
public class BusCustomsTaxretBean extends GridFormView {

	@SaveState
	@Accessible
	public BusCustomsTaxret selectedRowData = new BusCustomsTaxret();

	@Action
	public void clear() {
		this.clearQryKey();
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = this.serviceContext.busCustomsTaxretMgrService.busCustomsTaxretDao
		.findById(this.getGridSelectId());
		
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.busCustomsTaxretMgrService
		.saveData(selectedRowData);
	}
	
	@Action
	public void isgetbillAction() {
		if(selectedRowData.getIsgetbill()) {
			selectedRowData.setGetbilldate(Calendar.getInstance().getTime());
		} else {
			selectedRowData.setGetbilldate(null);
		}
	}
	
	@Action
	public void istocusAction() {
		if(selectedRowData.getIstocus()) {
			selectedRowData.setTocusdate(Calendar.getInstance().getTime());
		} else {
			selectedRowData.setTocusdate(null);
		}
	}
	
	@Action
	public void isretcusAction() {
		if(selectedRowData.getIsretcus()) {
			selectedRowData.setRetcusdate(Calendar.getInstance().getTime());
		} else {
			selectedRowData.setRetcusdate(null);
		}
	}
	
	@Action
	public void isfinasignAction() {
		if(selectedRowData.getIsfinasign()) {
			selectedRowData.setFinasigndate(Calendar.getInstance().getTime());
		} else {
			selectedRowData.setFinasigndate(null);
		}
	}
	
	@Action
	public void isrescheckAction() {
		if(selectedRowData.getIsrescheck()) {
			selectedRowData.setRescheckdate(Calendar.getInstance().getTime());
			selectedRowData.setReschecker(AppUtils.getUserSession().getUsercode());
		} else {
			selectedRowData.setRescheckdate(null);
			selectedRowData.setReschecker("");
		}
	}
	
	@Action
	public void isresAction() {
		if(selectedRowData.getIsres()) {
			selectedRowData.setResdate(Calendar.getInstance().getTime());
			selectedRowData.setReser(AppUtils.getUserSession().getUsercode());
		} else {
			selectedRowData.setResdate(null);
			selectedRowData.setReser("");
		}
	}
	
	@Action
	public void iscssignAction() {
		if(selectedRowData.getIscssign()) {
			selectedRowData.setCssigndate(Calendar.getInstance().getTime());
			selectedRowData.setCssigner(AppUtils.getUserSession().getUsercode());
		} else {
			selectedRowData.setCssigndate(null);
			selectedRowData.setCssigner("");
		}
	}
	
}
