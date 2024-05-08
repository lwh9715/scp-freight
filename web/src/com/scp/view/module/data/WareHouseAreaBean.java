package com.scp.view.module.data;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.data.DatWarehouse;
import com.scp.model.data.DatWarehouseArea;
import com.scp.service.data.WarehouseareaMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.data.warehouseareaBean", scope = ManagedBeanScope.REQUEST)
public class WareHouseAreaBean extends MastDtlView {

	@ManagedProperty("#{warehouseareaMgrService}")
	public WarehouseareaMgrService warehouseareaMgrService;
	
	@SaveState
	@Accessible
	public DatWarehouseArea selectedRowData = new  DatWarehouseArea();
	
	
	
	
	@Override
	public void init() {
		mData = new DatWarehouse();
		String id = AppUtils.getReqParam("id");
		if(!StrUtils.isNull(id)){
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
		}
	}

	
	@Override
	public void addMaster() {
		this.mData = new DatWarehouse();
		
		this.mPkVal = -1l;
		
  
		super.addMaster();
	}
        
 
	@Override
	public void add() {
		selectedRowData = new DatWarehouseArea();
		selectedRowData.setWarehouseid(this.mPkVal);
		
		
		super.add();
	}
	



	@Override
	public void refreshMasterForm() {
		this.mData = warehouseareaMgrService.warehouseDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}


	@Override
	protected void doServiceFindData() {
		this.selectedRowData = warehouseareaMgrService.warehouseAreaDao.findById(this.pkVal);
	}

	@Override
	public void doServiceSaveMaster() {
		warehouseareaMgrService.saveMasterData((DatWarehouse)mData);
		
		this.mPkVal = ((DatWarehouse)mData).getId();
		this.alert("ok");
	}
	

	@Override
	protected void doServiceSave() {
		if(this.mPkVal == -1l)return;
		
		warehouseareaMgrService.saveDtlData(selectedRowData);
		this.add();
		this.alert("OK");
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND warehouseid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void del() {
		try {
			warehouseareaMgrService.warehouseAreaDao.remove(selectedRowData);
			this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void delMaster() {
		try {
			warehouseareaMgrService.removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
}
