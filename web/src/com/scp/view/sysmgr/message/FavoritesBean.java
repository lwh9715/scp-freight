package com.scp.view.sysmgr.message;

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

import com.scp.model.sys.Favorites;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.sysmgr.msgboard.favoritesBean", scope = ManagedBeanScope.REQUEST)
public class FavoritesBean extends GridFormView {
	
	
	
	@Action
	public void addForm(){
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.favoritesService.favoritesDao
		.findById(this.getGridSelectId());
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		return m;
	}
	
	@SaveState
	@Accessible
	public Favorites selectedRowData = new Favorites(); 
	
	@Override
	protected void doServiceSave() {
		try {
			
			this.serviceContext.favoritesService.saveData(selectedRowData);
			this.refresh();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
		
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
		this.refresh();
	}
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = " f_imp_favorites";
				String args = -100 + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				this.refresh();
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
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
			this.serviceContext.favoritesService.removeDate(ids);
			this.refresh();
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
}
