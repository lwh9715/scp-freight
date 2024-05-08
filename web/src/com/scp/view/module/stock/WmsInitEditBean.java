package com.scp.view.module.stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.wms.WmsIn;
import com.scp.model.wms.WmsIndtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.GoodsStockBean;

@ManagedBean(name = "pages.module.stock.wmsiniteditBean", scope = ManagedBeanScope.REQUEST)
public class WmsInitEditBean extends MastDtlView {
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Accessible
	public WmsIn selectedRowData = new WmsIn();

	@SaveState
	@Accessible
	public WmsIndtl ddata = new WmsIndtl();

	@Override
	public void refreshForm() {
		this.localId = this.ddata.getLocid();
		getInitLoccalDesc();
		super.refreshForm();
		update.markUpdate(true, UpdateLevel.Data, "locdesc");

	}

	@Bind
	@SaveState
	public String locdesc = "";

	// 编辑未拆分储位
	public void getEditLocalDesc() {
		String sql = 
				"\nSELECT " +
				"\ny.code || '->' || z.code AS localdesc "+
				"\nFROM dat_warehouse_area y,dat_warehouse_loc z "+
				"\nWHERE z.id  = " + localId + 
				"\n\tAND y.id = z.areaid" + ";";
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			locdesc = (String) m.get("localdesc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 加载储位
	public void getInitLoccalDesc() {
//		String sql = 
//			"\nSELECT  f_findwmslocaldesc(" + this.pkVal+ ") AS localdesc" + ";";
//		try {
//			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//			locdesc = (String) m.get("localdesc");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	

	@Override
	public void grid_ondblclick() {

		this.pkVal = getGridSelectId();
		// this.data = getViewControl().findById(this.pkVal);
		doServiceFindData();
		this.refreshForm();
		if (editWindow != null)
			editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Action
	public void showAttachmentIframe() {
		try {
			attachmentIframe.load(AppUtils.getContextPath()+"/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
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

	@Bind
	public UIWindow showGoodsWindow;

	@Bind
	public UIDataGrid goodsGrid;

	@Bind
	public String goodscodeQryKey;

	@Action
	public void goodscodeQry() {
		goodsService.qry(goodscodeQryKey);
		this.goodsGrid.reload();
	}

	@Bind(id = "goodsGrid", attribute = "dataProvider")
	public GridDataProvider getGoodsGridDataProvider() {
		return goodsService.getgoodsDataProvider();
	}

	@Action
	public void showGoods() {
		// this.goodscodeQryKey = "";
		// this.goodsService.qry(null);
		showGoodsWindow.show();
		goodsGrid.reload();
	}

	@Action
	public void goodsGrid_ondblclick() {
		Object[] objs = goodsGrid.getSelectedValues();
		Map m = (Map) objs[0];
		ddata.setGoodsid((Long) m.get("id"));
		ddata.setGoodscode((String) m.get("code"));
		ddata.setGoodsnamec((String) m.get("namec"));
		ddata.setGoodsnamee((String) m.get("namee"));
		ddata.setGoodscolor((String) m.get("color"));
		ddata.setGoodsl((BigDecimal) m.get("l"));
		ddata.setGoodsw((BigDecimal) m.get("w"));
		ddata.setGoodsh((BigDecimal) m.get("h"));
		ddata.setGoodsv((BigDecimal) m.get("v"));

		this.update.markUpdate(UpdateLevel.Data, "goodsid");
		this.update.markUpdate(UpdateLevel.Data, "goodscode");
		this.update.markUpdate(UpdateLevel.Data, "goodsnamec");
		this.update.markUpdate(UpdateLevel.Data, "goodsnamee");
		this.update.markUpdate(UpdateLevel.Data, "goodscolor");
		this.update.markUpdate(UpdateLevel.Data, "goodsl");

		this.update.markUpdate(UpdateLevel.Data, "goodsw");
		this.update.markUpdate(UpdateLevel.Data, "goodsh");
		this.update.markUpdate(UpdateLevel.Data, "goodsv");
		showGoodsWindow.close();

	}

	@SaveState
	@Accessible
	public Long localId;

	@Action
	public void setLocal() {
		String localInfo = AppUtils.getReqParam("localInfo"); // 从action中的id中获取参数绑定(js中绑定的)
		localId = this.serviceContext.warehouselocalMgrService.findlocalid(localInfo);// 获取locid
		ddata.setLocid(localId);
		this.update.markUpdate(UpdateLevel.Data, "locid");
		this.getEditLocalDesc();
		this.update.markUpdate(UpdateLevel.Data, "locdesc");

	}

	@Bind
	public UIIFrame arapIframe;

	@Action
	public void showArapEdit() {
		try {
			arapIframe.load("../finance/arapedit.xhtml?id=" + this.mPkVal
					+ "&jobid=" + findJobId());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND inid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void init() {
		selectedRowData = new WmsIn();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh(); // 从表也刷新

		} else{
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.wmsIndtlMgrService().wmsInDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = serviceContext.wmsIndtlMgrService().wmsIndtlDao.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		if (this.mPkVal == -1l)
			return;
		serviceContext.wmsIndtlMgrService().saveDtlData(ddata);
		this.alert("OK");

	}

	@Override
	public void doServiceSaveMaster() {
		// 提交之前必须先要保存,必须子表中也有数据才能提交
		selectedRowData.setWmstype("Z");
		selectedRowData.setSubmitime(new Date());
		selectedRowData.setSubmiter(AppUtils.getUserSession().getUsername());
		selectedRowData.setIsubmit(true);
		serviceContext.wmsIndtlMgrService().saveMasterData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");

	}



	@Override
	public void add() {
		ddata = new WmsIndtl();
		this.locdesc = null;
		ddata.setCurrency(AppUtils.getUserSession().getBaseCurrency());
		ddata.setInid(this.mPkVal);
		super.add();
		update.markUpdate(true, UpdateLevel.Data, "locdesc");
	}

	@Override
	public void addMaster() {
		this.selectedRowData = new WmsIn();
		Long warehouseid = this.serviceContext.userMgrService.findWareHaousId(AppUtils.getUserSession().getUserid());
		selectedRowData.setWarehouseid(warehouseid);
		this.mPkVal = -1l;
		selectedRowData.setSingtime(new Date());

		super.addMaster();
	}

	@Override
	public void del() {
		if (selectedRowData.getIsubmit()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.wmsIndtlMgrService().removeDate(ddata.getId());
			this.add();
			this.alert("OK");
			refresh();
		}
	}

	@Override
	public void delMaster() {
		if (selectedRowData.getIsubmit()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.wmsInMgrService().removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
			refresh();
		}
	}

	@Bind
	@SaveState
	@Accessible
	public String splitLocalText;

	@Bind
	public UIWindow splitLocalWindow;

	@Action
	public void splitLocal() {
		splitLocalText = "";
		splitLocalWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "splitLocalText");
	}

	@Action
	public void splitLocalBatch() {
		if (StrUtils.isNull(splitLocalText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String data = StrUtils.toDBC(splitLocalText).replaceAll(" ", "")
					.replaceAll("\\n", "");
			String[] format = splitLocalText.split(",");
			String rex1 = "^[A-Za-z0-9]+:[A-Z]{1,2}(([0-9][1-9])|([1-9]0)):[1-9]\\d*$";
			for (int i = 0; i < format.length; i++) {
				if (format[i].matches(rex1) == false) {
					MessageUtils.alert("格式错误,请检查");
					return;
				}
			}
			Long id = this.pkVal;
			try {
				serviceContext.wmsIndtlMgrService().splitLocal(id, data);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}


	@Bind
	@SaveState
	@Accessible
	public String updateLocalText;

	@Bind
	public UIWindow updateLocalWindow;

	@Action
	public void updateLocal() {
		updateLocalText = "";
		updateLocalWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "updateLocalText");
	}

	@Action
	public void updateLocalBatch() {
		if (StrUtils.isNull(updateLocalText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String data = StrUtils.toDBC(updateLocalText).replaceAll(" ", "");
			String[] format = updateLocalText.split("\\n");
			String rex1 = "^[A-Za-z0-9]+:[A-Z]{1,2}(([0-9][1-9])|([1-9]0)):[1-9]\\d*$";
			for (int i = 0; i < format.length; i++) {
				String[] splitDate=format[i].split(",");
				for (int j = 0; j < splitDate.length; j++) {
					if (splitDate[j].matches(rex1) == false) {
						MessageUtils.alert("格式错误,请检查");
						return;
					}
				}
			}
			Long id = this.mPkVal;
			try {
				serviceContext.wmsIndtlMgrService().updateLocal(id, data);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
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
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_wmsindetail";
				String args = this.mPkVal + ",'" + AppUtils.getUserSession().getUsercode()+"'";
				this.serviceContext.commonDBService.addBatchFromExcelText(importDataText , callFunction , args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	

	
}
