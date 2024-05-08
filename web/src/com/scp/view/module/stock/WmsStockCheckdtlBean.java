package com.scp.view.module.stock;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsDispatch;
import com.scp.model.wms.WmsDispatchdtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.GoodsStockBean;

@ManagedBean(name = "pages.module.stock.wmsstockcheckdtlBean", scope = ManagedBeanScope.REQUEST)
public class WmsStockCheckdtlBean extends EditGridView {
	
	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	public UIDataGrid grid;

	@Bind
	@SaveState
	public Long pkVal;
	@Bind
	@SaveState
	public Long mPkVal = -1L;

	@SaveState
	@Accessible
	public WmsDispatch selectedRowData = new WmsDispatch();

	@SaveState
	@Accessible
	public WmsDispatchdtl ddata = new WmsDispatchdtl();

	@SaveState
	@Accessible
	public int showAll = 0;

	@Override
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(selectedRowData.getIscheck()==null){
				
			}else{
				disableAllButton(selectedRowData.getIscheck());
			}
		}
	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomerabbr((String) m.get("abbr"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Override
	public void clearQryKey() {
		// TODO Auto-generated method stub
		super.clearQryKey();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND dispatchid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	public void init() {
		selectedRowData = new WmsDispatch();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh(); // 从表也刷新

		} else {
			addMaster();
		}
	}

	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.wmsDispatchService().wmsDispatchDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	protected void doServiceFindData() {
		this.ddata = serviceContext.wmsDispatchdtlService().wmsDispatchdtlDao
				.findById(this.pkVal);

	}

	protected void doServiceSave() {
		if (this.mPkVal == -1l)
			return;
		serviceContext.wmsDispatchdtlService().saveDtlData(ddata);
		this.alert("OK");

	}

	public void doServiceSaveMaster() throws Exception {
		try {
			if (selectedRowData.getCustomerid() == null
					|| selectedRowData.getWarehouseid() == null
					|| selectedRowData.getSingtime() == null) {
				MessageUtils.alert("仓库,客户,制单日期不能为空");
				return;
			} else {
				// 提交之前必须先要保存,必须子表中也有数据才能提交
				selectedRowData.setDispatchtype("P");
				serviceContext.wmsDispatchdtlService()
						.saveMasterData(selectedRowData);

				this.mPkVal = selectedRowData.getId();
				this.refreshMasterForm();
				refresh();
			}
		} catch (Exception e) {
			throw new Exception();
		}
	}
	
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			sql = "UPDATE wms_dispatch SET ischeck = TRUE ,checktime = NOW(),checkter = '"+updater+"' WHERE id = "+this.mPkVal+";";
		}else {
			sql = "UPDATE wms_dispatch SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id = "+this.mPkVal+";";
		}
		try {
			serviceContext.wmsDispatchdtlService().wmsDispatchDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isCheck);
		}catch (Exception e) {
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck?null:Calendar.getInstance().getTime());
			refreshMasterForm();
			this.disableAllButton(!isCheck);
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	@Bind
	public UIButton save;
	
	@Bind
	public UIButton delBatch;
	@Bind
	public UIButton showStock;
	
	/**
	 * 控制按钮是否可用
	 * @param isCheck
	 */
	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
		save.setDisabled(isCheck);
		showStock.setDisabled(isCheck);
		delBatch.setDisabled(isCheck);
	}

	@Action
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new WmsDispatch();
		this.mPkVal = -1l;
		Long warehouseid = this.serviceContext.userMgrService.findWareHaousId(AppUtils.getUserSession().getUserid());
		selectedRowData.setWarehouseid(warehouseid);
		selectedRowData.setSingtime(new Date());
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		// this.grid.reload();
	}

	@Action
	public void del() {
		if (selectedRowData.getIscheck()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.wmsDispatchdtlService().removeDate(ddata.getId());
			this.alert("OK");
			refresh();
		}
	}

	@Action
	public void delMaster() {
		if (selectedRowData.getIscheck()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.wmsDispatchService().removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
			refresh();
		}
	}

	public void add() {

	}

	@Action
	public void saveMaster() {
		try {
			doServiceSaveMaster(); // Master
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void update(Object companys) {

	}

	
	
	@Override
	public void save() {
		try {
			serviceContext.wmsDispatchdtlService().saveCheckDtlDatas(
					modifiedData);
			MessageUtils.alert("OK");
			refresh();
			
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			return;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	

	@Override
	public void editGrid_ondblclick() {

	}

	@Bind
	public UIIFrame stockChooserIframe;

	@Action
	public void showStock() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("主表无数据,请先保存主表");
			return;
		} else {
			editWindow.show();
			stockChooserIframe.load("./wmsstockcheckchooser.xhtml?customerid="
					+ this.selectedRowData.getCustomerid() + "&warehouseid="
					+ this.selectedRowData.getWarehouseid() + "&outid="
					+ this.mPkVal);
		}
	}

	@Bind
	public UIWindow editWindow;

	public void closeEidtWindow() {
		editWindow.close();
	}

	@Action(id = "editWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}
	
	@Action
	public void delBatch() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.wmsDispatchdtlService().delBatch(ids);
			MessageUtils.alert("删除成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}
}
