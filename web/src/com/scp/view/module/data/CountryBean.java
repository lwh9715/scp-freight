package com.scp.view.module.data;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.data.DatCountry;
import com.scp.service.data.CountryMgrService;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.countryBean", scope = ManagedBeanScope.REQUEST)
public class CountryBean extends GridFormView {

	@ManagedProperty("#{countryMgrService}")
	public CountryMgrService countryMgrService;
	
	@SaveState
	@Accessible
	public DatCountry selectedRowData = new DatCountry();
	
	@Override
	public void add() {
		selectedRowData = new DatCountry();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = countryMgrService.datCountryDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		countryMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			countryMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Override
	public void del() {
		if(selectedRowData.getId()==0){
			this.add();	
		}else{
			countryMgrService.removeDate(selectedRowData.getId());
		refresh();
		this.add();
		this.alert("OK");
		}
	}
}
