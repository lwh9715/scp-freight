package com.scp.model.edi;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.EdiMap;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.edi.edi_mapBean", scope = ManagedBeanScope.REQUEST)
public class Edi_MapBean extends GridFormView {
	
	@SaveState
	@Accessible
	public EdiMap selectedRowData = new EdiMap();
	
	@Bind
	@SaveState
	private String edittype;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			edittype = AppUtils.getReqParam("edittype");
			if(!StrUtils.isNull(edittype)){
				this.qryMap.put("maptype", edittype);
			}
		}
	}
	
	@Override
	public void add() {
		this.selectedRowData = new EdiMap();
		if("port".equals(edittype)){
			this.selectedRowData.setMaptype("PORT");
		}else if("package".equals(edittype)){
			this.selectedRowData.setMaptype("PACKAGE");
		}else if("cnttype".equals(edittype)){
			this.selectedRowData.setMaptype("CNTTYPE");
		}
		super.add();
	}
	
	
	
	@Action
	public void addCopy(){
		EdiMap newBean = new EdiMap();
		newBean.setEditype(this.selectedRowData.getEditype());
		newBean.setMaptype(this.selectedRowData.getMaptype());
		this.selectedRowData = newBean;
		super.add();
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.ediMapMgrService.ediMapDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.ediMapMgrService.saveData(selectedRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.pkVal = selectedRowData.getId();
		this.refresh();
		MessageUtils.alert("OK");
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				Long idL = Long.parseLong(id);
				this.serviceContext.ediMapMgrService.removeData(idL);
				this.refresh();
				MessageUtils.alert("OK!");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void refreshForm() {
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
}