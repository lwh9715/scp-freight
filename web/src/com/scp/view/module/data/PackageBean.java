package com.scp.view.module.data;

import com.scp.model.data.DatPackage;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.html.impl.UIIFrame;

@ManagedBean(name = "pages.module.data.packageBean", scope = ManagedBeanScope.REQUEST)
public class PackageBean extends GridFormView {

	
	@SaveState
	@Accessible
	public DatPackage selectedRowData = new DatPackage();
	
	@Bind
	public UIIFrame edi_mapping;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String ediUrl = "../edi/edi_map.aspx?edittype=package";
			this.edi_mapping.load(ediUrl);
		}
	}
	
	@Override
	public void add() {
		selectedRowData = new DatPackage();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.packageMgrService.datPackageDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.packageMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			serviceContext.packageMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Override
	public void del() {
		if(selectedRowData.getId()==0){
			this.add();	
		}else{
		serviceContext.packageMgrService.removeDate(selectedRowData.getId());
		refresh();
		this.add();
		this.alert("OK");
		}
	}
	
}
