package com.scp.view.module.carmgr;

import java.util.List;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.base.CommonComBoxBean;
import com.scp.model.car.CarRepaireRecord;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.carmgr.maintainBean", scope = ManagedBeanScope.REQUEST)
public class CarRepaireRecordBean extends GridFormView {

	@SaveState
	@Accessible
	public CarRepaireRecord selectedRowData = new CarRepaireRecord();



	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.carRepaireRecordMgrService.carRepaireRecordDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.carRepaireRecordMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}

	

	@Bind(id = "cardesc")
	public List<SelectItem> getCardesc() {
		try {
			return CommonComBoxBean.getComboxItems("d.id", "code",
					"dat_car AS d", "WHERE 1=1", "ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	public void checkBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.carRepaireRecordMgrService.updateCheck(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}

	@Action
	public void cancelCheckBatch() {

		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}
			serviceContext.carRepaireRecordMgrService.updateCancelCheck(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "carrepairedtl";
		String url = "./carrepairedtl.xhtml?id="+this.getGridSelectId();
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
	
	
	@Action
	public void add() {
		
		super.grid_ondblclick();
		String winId = "carrepairedtl";
		String url = "./carrepairedtl.xhtml";
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}

}