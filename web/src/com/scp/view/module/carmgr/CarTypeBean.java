package com.scp.view.module.carmgr;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.data.DatCartype;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.carmgr.cartypeBean", scope = ManagedBeanScope.REQUEST)
public class CarTypeBean extends GridFormView {

	
	@SaveState
	@Accessible
	public DatCartype selectedRowData = new DatCartype();
	
	@Override
	public void add() {
		selectedRowData = new DatCartype();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.carTypeMgrService.datCartypeDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.carTypeMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void del() {
		serviceContext.carTypeMgrService.datCartypeDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		refresh();
	}
	
}