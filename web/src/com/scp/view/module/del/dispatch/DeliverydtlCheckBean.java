package com.scp.view.module.del.dispatch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.del.DelDelivery;
import com.scp.model.del.DelDeliverydtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.del.dispatch.deliverydtlcheckBean", scope = ManagedBeanScope.REQUEST)
public class DeliverydtlCheckBean extends MastDtlView {

	@SaveState
	@Accessible
	public DelDelivery selectedRowData = new DelDelivery();

	@SaveState
	@Accessible
	public DelDeliverydtl ddata = new DelDeliverydtl();

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if (selectedRowData.getIsubmit() == null) {

			} else {
				disableAllButton(selectedRowData.getIsubmit());
			}
		}
	}

	@Override
	public void init() {
		selectedRowData = new DelDelivery();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh(); // 从表也刷新

		} else {
			addMaster();
		}
	}

	@Override
	public void refreshForm() {
		super.refreshForm();
	}

	@Override
	public void grid_ondblclick() {

		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		if (editWindow != null)
			editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND truckid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.deliveryMgrService.deliveryDao
				.findById(this.mPkVal);
		getloadnos();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");

	}

	@Override
	protected void doServiceFindData() {
		this.ddata = serviceContext.deliverydtlMgrService.delDeliverydtlDao
				.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		ddata.setItemno(new Short((short) (super.getDataProvider()
				.getTotalCount() + 1)));
		serviceContext.deliverydtlMgrService.saveDtlData(ddata);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		this.alert("OK");
	}

	@Override
	public void doServiceSaveMaster() {
		if (selectedRowData.getCarid() == null
				|| selectedRowData.getSingtime() == null) {
			MessageUtils.alert("车辆,制单日期不能为空");
			return;
		} else {
			// 提交之前必须先要保存,必须子表中也有数据才能提交
			serviceContext.deliverydtlMgrService
					.saveMasterData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			this.refreshMasterForm();
			this.alert("ok");
		}
	}

	private void disableAllButton(Boolean isCheck) {

	}

	/**
	 * 审核通用处理
	 */
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater = AppUtils.getUserSession().getUsername();
		Date date = new Date();
		String sql = "";
		try {
			if (selectedRowData.getIssend())
				throw new CommonRuntimeException(
						"Current is issend,plese unsend first!");
			if (isCheck) {
				sql = "UPDATE del_delivery SET ischeck = TRUE ,checktime = '"
						+ date + "',checkter = '" + updater + "' WHERE id ="
						+ this.mPkVal + ";";
			} else {
				sql = "UPDATE del_delivery SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.deliverydtlMgrService.delDeliverydtlDao
					.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isCheck);
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		}
	}

	/**
	 * 通用处理
	 */
	@Action
	public void issendAjaxSubmit() {

		Boolean isSend = selectedRowData.getIssend();
		String updater = AppUtils.getUserSession().getUsername();
		Date date=new Date();
		String sql = "";
		try {
			if (!selectedRowData.getIscheck())
				throw new CommonRuntimeException(
						"Current is unckeck,plese check first!");
			if (isSend) {
				sql = "UPDATE del_delivery SET issend = TRUE ,sendtime = '"
						+ date +  "' WHERE id ="
						+ this.mPkVal + ";";
			} else {
				sql = "UPDATE del_delivery SET issend = FALSE ,sendtime = NULL WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.deliverydtlMgrService.delDeliverydtlDao
					.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isSend);
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIssend(!isSend);
			
			selectedRowData.setSendtime(isSend ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isSend);
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIssend(!isSend);
			
			selectedRowData.setSendtime(isSend ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isSend);
		}
	}

	@Override
	public void add() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			ddata = new DelDeliverydtl();
			ddata.setTruckid(this.mPkVal);
			super.add();
		}
	}

	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new DelDelivery();
		this.mPkVal = -1l;
		selectedRowData.setSingtime(new Date());
		super.addMaster();
	}

	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.deliverydtlMgrService.delBatch(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}

	@Override
	public void del() {
		try {
			serviceContext.deliverydtlMgrService.removeDate(this.pkVal);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void delMaster() {
		if (selectedRowData.getIsubmit()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.deliveryMgrService.removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
			refresh();
		}
	}

	@Bind(id = "cardesc")
	public List<SelectItem> getCardesc() {
		try {
			return CommonComBoxBean
					.getComboxItems(
							"c.id",
							"c.code||'/'||c.carcolor1||'/'||t.cartype||'/'||COALESCE(d.code,'')||'/'||COALESCE(c.remarks,'')",
							"dat_car c ,dat_cartype t,dat_driver d",
							"WHERE 1=1", "ORDER BY c.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	public UIIFrame attachmentIframe;

	@Action
	public void showAttachmentIframe() {
		try {
			if (this.mPkVal == -1l) {
				return;
			} else {
				attachmentIframe.load(AppUtils.getContextPath()
						+ "/pages/module/common/attachment.faces?linkid="
						+ this.mPkVal);
			}
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
		ddata.setCustomerid((Long) m.get("id"));
		ddata.setCustomerabbr((String) m.get("abbr"));
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
	public UIIFrame arapIframe;

	@Action
	public void showArapEdit() {
		try {
			if (this.mPkVal == -1l) {
				return;
			} else {
				arapIframe.load("/pages/module/finance/arapedit.faces?id="
						+ this.mPkVal + "&jobid=" + findJobId());
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}
	
	@SaveState
	@Accessible
	@Bind
	public String loadnos;
	
	public void getloadnos(){
		if(selectedRowData.getRefid()==null){
			loadnos="";
		}else{
		try {
			loadnos=serviceContext.deliverydtlMgrService.getLoadnos(selectedRowData.getRefid());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		}
	}

}
