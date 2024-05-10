package com.scp.view.module.data;

import com.scp.model.data.Datdoor2doorday;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

@ManagedBean(name = "pages.module.data.datdoor2doordayBean", scope = ManagedBeanScope.REQUEST)
public class Datdoor2doordayBean extends GridFormView {

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}

	@SaveState
	@Accessible
	public Datdoor2doorday selectedRowData = new Datdoor2doorday();

	@Override
	public void add() {
		selectedRowData = new Datdoor2doorday();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.door2doordayMgrService.datdoor2doordayDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.door2doordayMgrService.saveData(selectedRowData);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			this.serviceContext.door2doordayMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			alert("请选择一行记录!");
		} else {
			try {
				this.serviceContext.door2doordayMgrService.removeDate(Long.valueOf(ids[0]));
				this.add();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Bind
	public UIWindow importDataWindow;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
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
				String callFunction = "f_imp_door2doorday";
				String args = this.pkVal + ",'" + AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
}
