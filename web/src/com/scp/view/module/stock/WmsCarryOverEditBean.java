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
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.wms.WmsOut;
import com.scp.model.wms.WmsOutdtl;
import com.scp.service.data.WarehouselocalMgrService;
import com.scp.service.wms.WmsOutMgrService;
import com.scp.service.wms.WmsOutdtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.GoodsStockBean;

@ManagedBean(name = "pages.module.stock.wmscarryovereditBean", scope = ManagedBeanScope.REQUEST)
public class WmsCarryOverEditBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@ManagedProperty("#{warehouselocalMgrService}")
	private WarehouselocalMgrService warehouselocalMgrService;

	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;


	@ManagedProperty("#{wmsOutMgrService}")
	private WmsOutMgrService wmsOutMgrService;

	@ManagedProperty("#{wmsOutdtlMgrService}")
	private WmsOutdtlMgrService wmsOutdtlMgrService;

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Accessible
	public WmsOut selectedRowData = new WmsOut();

	@SaveState
	@Accessible
	public WmsOutdtl ddata = new WmsOutdtl();

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
	@SaveState
	public Integer stocknum;

	public void getStock() {
		Long customerid = this.selectedRowData.getCustomerid();
		String goodscode = this.ddata.getGoodscode();
		String sql = "" + "\nSELECT " + "\nsum(piece) AS piece "
				+ "\nFROM _wms_stock " + "\nWHERE  customerid  = " + customerid
				+ " AND goodscode = '" + goodscode + "'"
				+ "\nGROUP BY  customerid ,goodscode " + ";";
		//System.out.println("-------" + sql);
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			stocknum = ((BigDecimal) m.get("piece")).intValue(); //

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void refreshForm() {

		getStock();
		super.refreshForm();
		this.update.markUpdate(true, UpdateLevel.Data, "stocknum");

	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		this.customerService.qry(null);
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
		this.customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Bind
	public UIWindow showGoodsWindow;

	@Bind
	public UIDataGrid inventoryGrid;

	@Bind
	public String goodscodeQryKey;

	@Action
	public void goodscodeQry() {
		this.goodsService.qry(goodscodeQryKey);
		this.inventoryGrid.reload();
	}


	@Action
	public void showGoods() {
		/*
		 * this.selectedRowData.getCustomerid();
		 * //System.out.println(this.selectedRowData.getCustomerid());
		 * this.goodsinventoryService.qry(this.selectedRowData.getCustomerid());
		 */
		showGoodsWindow.show();
		inventoryGrid.reload();
	}

	@Action
	public void goodsGrid_ondblclick() {
		Object[] objs = inventoryGrid.getSelectedValues();
		Map m = (Map) objs[0];

		showGoodsWindow.close();

	}

	@Bind
	public String localset; // 必须放在form里边 可以做一个易隐藏域好传值

	@Action
	public void setLocal() {
		String localInfo = AppUtils.getReqParam("localInfo"); // 从action中的id中获取参数绑定(js中绑定的)

		Long s = this.warehouselocalMgrService.findlocalid(localInfo);// 获取locid

		// ddata.setLocid(s);

		this.update.markUpdate(UpdateLevel.Data, "locid");

	}

	@Action
	public void isubmitAjaxSubmit() {
		Boolean isCheck = this.selectedRowData.getIscheck();
		String issubValue = AppUtils.getReqParam("issub").toUpperCase();
		Boolean isSubvalue = false; // 注意返回来的是Boolean对象;
		if (issubValue.endsWith("TRUE")) {
			isSubvalue = true;
		} else {
			isSubvalue = false;
		}
		// 提交之前必须先要保存,必须子表中也有数据才能提交
		if (this.mPkVal == -1l && isSubvalue == true) {
			MessageUtils.alert("操作非法:请先保存主表数据,录入具体入库商品信息后才能提交");
			String script = "issubmit.setValue(false);";
			Browser.execClientScript(script);
			return;
		} else if (this.mPkVal != -1l && isSubvalue == false && isCheck == true) {
			MessageUtils.alert("操作非法:请先取消审核以后才可以取消提交");
			String script = "issubmit.setValue(true);";
			Browser.execClientScript(script);
			return;
		} else if (this.mPkVal == -1l && isSubvalue == false) {
			// 对应的情况是:不能提交以后,用户自己取消提交
		} else if (this.mPkVal != -1l && isSubvalue == false
				&& isCheck == false) {
			// 取消提交,将对应的提交信息清空
			selectedRowData.setIsubmit(isSubvalue);
			selectedRowData.setSubmiter(null);
			selectedRowData.setSubmitime(null);
			wmsOutdtlMgrService.saveMasterData(selectedRowData);
			this.refreshMasterForm();

		} else if (this.mPkVal != -1l && isSubvalue == true && isCheck == false) {
			selectedRowData.setIsubmit(isSubvalue);
			selectedRowData.setSubmiter(AppUtils.getUserSession().getUsername());
			selectedRowData.setSubmitime(new Date());
			wmsOutdtlMgrService.saveMasterData(selectedRowData);
			this.refreshMasterForm();

		}
	}

	@Action
	public void showStock() {
		add();
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
		// this.update.markUpdate(UpdateLevel.Data , arapIframe);
	}

	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}

	public void disableAllButton(boolean ischeck) {
		if (ischeck) {
			String script = "dtlGet.setDisabled(true);dtlSave.setDisabled(true);dtlDel.setDisabled(true);del.setDisabled(true);save.setDisabled(true);";
			Browser.execClientScript(script);
		} else {
			String script = "dtlGet.setDisabled(false);dtlSave.setDisabled(false);dtlDel.setDisabled(false);del.setDisabled(false);save.setDisabled(false);";
			Browser.execClientScript(script);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND outid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void init() {
		selectedRowData = new WmsOut();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();

		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = wmsOutdtlMgrService.wmsOutDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = wmsOutdtlMgrService.wmsOutdtlDao.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		if (this.mPkVal == -1l)
			return;

		wmsOutdtlMgrService.saveDtlData(ddata);

		this.alert("OK");
	}

	@Override
	public void doServiceSaveMaster() {
		wmsOutdtlMgrService.saveMasterData(selectedRowData);

		this.mPkVal = selectedRowData.getId();
		this.alert("ok");

	}

	@Bind
	public UIIFrame stockChooserIframe;

	@Override
	public void add() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("主表无数据,请先保存主表");
			return;
		} else {
			ddata = new WmsOutdtl();

			ddata.setOutid(this.mPkVal);

			super.add();

			stockChooserIframe.load("./wmsstockchooser.xhtml?customerid="
					+ this.selectedRowData.getCustomerid() + "&outid="
					+ this.mPkVal);
			//System.out.println(this.pkVal);
		}
	}

	@Override
	public void addMaster() {
		this.selectedRowData = new WmsOut();
		super.addMaster();
	}

	@Bind
	public UIWindow editdtlWindow;

	@Bind
	public UIPanel editdtlPanel;

	@Bind
	@SaveState
	public Long pkVal;

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		if (editdtlWindow != null)
			editdtlWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editdtlPanel");

	}

	@Override
	public void saveMaster() {

		super.saveMaster();
		this.refreshMasterForm();
	}

	@Override
	public void del() {
		wmsOutdtlMgrService.removeDate(ddata.getId());
		this.alert("OK");

		refresh();
	}

	@Override
	public void delMaster() {
		wmsOutMgrService.removeDate(this.mPkVal);
		this.addMaster();
		this.alert("OK");

		refresh();
	}

}
