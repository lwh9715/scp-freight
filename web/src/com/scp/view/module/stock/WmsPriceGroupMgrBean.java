package com.scp.view.module.stock;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.wms.WmsPrice;
import com.scp.model.wms.WmsPriceGroup;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.stock.wmspricegroupmgrBean", scope = ManagedBeanScope.REQUEST)
public class WmsPriceGroupMgrBean extends MastDtlView {

	
	@SaveState
	@Accessible
	public WmsPriceGroup selectedRowData = new WmsPriceGroup();
	
	@Override
	public void init() {
		mData = new WmsPrice();
		String id = AppUtils.getReqParam("id");
		if(!StrUtils.isNull(id)){
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
		} else {
			addMaster();
		}
	}

	
	@Override
	public void addMaster() {
		
		this.mData = new WmsPrice();
		this.mPkVal = -1l;
		((WmsPrice) mData).setPricetype("S");
		((WmsPrice) mData).setTimestart(new Date());
		((WmsPrice) mData).setTimeend(new Date(199,12,30));
		Long warehouseid = this.serviceContext.userMgrService.findWareHaousId(AppUtils.getUserSession().getUserid());
		((WmsPrice) mData).setWarehouseid(warehouseid);
		super.addMaster();
	}
        
 
	@Override
	public void add() {
		selectedRowData = new WmsPriceGroup();
		super.add();
	}
	



	@Override
	public void refreshMasterForm() {
		this.mData = serviceContext.wmsPriceGroupMgrService().wmsPriceDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}


	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.wmsPriceGroupMgrService().wmsPriceGroupDao.findById(this.pkVal);
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.wmsPriceGroupMgrService().saveMasterData((WmsPrice)mData);
		
		this.mPkVal = ((WmsPrice)mData).getId();
		this.alert("ok");
	}
	

	@Override
	protected void doServiceSave() {
		if(this.mPkVal == -1l)return;
		
		serviceContext.wmsPriceGroupMgrService().saveDtlData(selectedRowData);
		this.add();
		this.alert("OK");
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND priceid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void del() {
		serviceContext.wmsPriceGroupMgrService().wmsPriceGroupDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		
		
		refresh();
	}
	
	@Override
	public void delMaster() {
		serviceContext.wmsPriceGroupMgrService().removeDate(this.mPkVal);
		this.addMaster();
		this.alert("OK");
		
		refresh();
	}
	
	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
		serviceContext.wmsPriceGroupMgrService().delBatch(ids);
		MessageUtils.alert("ok!");
		refresh();
	}
	
	
	@Action
	public void customChoose() {
		String id = AppUtils.getReqParam("priceid");
		if (StrUtils.isNull(id)) {
			return;
		}
		Long priceid=new Long(id);
		String url = AppUtils.getContextPath() + "/pages/module/stock/customerchoose.xhtml?priceid="+priceid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
	
	
}
