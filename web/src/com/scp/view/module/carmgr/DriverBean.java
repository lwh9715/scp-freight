package com.scp.view.module.carmgr;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.data.DatDriver;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.carmgr.driverBean", scope = ManagedBeanScope.REQUEST)
public class DriverBean extends GridFormView {

	
	@SaveState
	@Accessible
	public DatDriver selectedRowData = new DatDriver();
	
	@Override
	public void add() {
		selectedRowData = new DatDriver();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.driverMgrService.datDriverDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.driverMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void del() {
		serviceContext.driverMgrService.datDriverDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		refresh();
	}
	
}