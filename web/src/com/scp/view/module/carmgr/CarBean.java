package com.scp.view.module.carmgr;

import java.util.List;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.base.CommonComBoxBean;
import com.scp.model.data.DatCar;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.carmgr.carBean", scope = ManagedBeanScope.REQUEST)
public class CarBean extends GridFormView {

	
	@SaveState
	@Accessible
	public DatCar selectedRowData = new DatCar();
	
	@Override
	public void add() {
		selectedRowData = new DatCar();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.carMgrService.datCarDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.carMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void del() {
		serviceContext.carMgrService.datCarDao.remove(selectedRowData);
		this.alert("OK");
		refresh();
	}
	

	@Bind(id="cartypedesc")
    public List<SelectItem> getCartypedesc() {
		try {
			return CommonComBoxBean.getComboxItems("d.id","cartype","dat_cartype AS d","WHERE 1=1","ORDER BY d.cartype");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="driverdesc")
    public List<SelectItem> getDriverdesc() {
		try {
			return CommonComBoxBean.getComboxItems("d.id","code||'/'||COALESCE(namec,'') || '/' ||COALESCE(namee,'')","dat_driver AS d","WHERE 1=1","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
}