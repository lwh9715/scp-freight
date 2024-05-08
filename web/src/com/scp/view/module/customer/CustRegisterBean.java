package com.scp.view.module.customer;


import java.util.Date;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.sys.Register;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.customer.custregisterBean", scope = ManagedBeanScope.REQUEST)
public class CustRegisterBean extends MastDtlView {
	
	@Bind
	@SaveState
	@Accessible
	public Register selectedRowData = new Register();
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton save;
	
	@Bind
	public UIWindow showWindow;
	
	@Override
	public void grid_ondblclick(){
		super.grid_ondblclick();
		showWindow.show();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.registerService.registerDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		selectedRowData.setCheckter(AppUtils.getUserSession().getUsername());
		selectedRowData.setChecktime(new Date());
		serviceContext.registerService.saveData(selectedRowData);
		MessageUtils.alert("ok");
		
	}
	
	@Action
	public void delMaster(){
		String[] ids = this.grid.getSelectedIds();
		serviceContext.registerService.removeDates(ids);
		this.refresh();
	}
	
	@Action
	public void del(){
		serviceContext.registerService.removeDate(this.pkVal);
		this.refresh();
		MessageUtils.alert("ok");
		showWindow.close();
	}
	

	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub
		
	}
}
