package com.scp.view.module.ship;

import java.util.ArrayList;
import java.util.List;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipBill;
import com.scp.model.ship.BusShipBillreg;
import com.scp.model.ship.BusShipping;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.billregisterBean", scope = ManagedBeanScope.REQUEST)
public class BillRegisterBean extends GridView {

	
	@SaveState
	@Accessible
	public BusShipBillreg selectedRowData;

	@SaveState
	@Accessible
	public BusShipBill busshipbill = new BusShipBill();

	@Bind
	public UIWindow showbillregisterWindow;

	@Bind
	@SaveState
	public UIButton save;

	@Bind
	@SaveState
	public long billregid;

	@SaveState
	public String processInstanceId;

	@SaveState
	public String workItemId;

	@SaveState
	public String billnos;
	
	@SaveState
	@Bind
	public String billid="";

	@SaveState
	public String sn;
	
	public static final String usercode = AppUtils.getUserSession().getUsercode();

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			init();
		}
	}

	public void init() {
		String sn = AppUtils.getReqParam("sn");
		
		processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
		workItemId = (String) AppUtils.getReqParam("workItemId");
		billnos = (String) AppUtils.getReqParam("billnos");
		billid = (String) AppUtils.getReqParam("billid");
		if (!StrUtils.isNull(workItemId)) {
//			IWorkItem workItem = AppUtils.getWorkflowSession().findWorkItemById(
//					workItemId);
			this.qryMap.put("billnos", sn);
		}
		if (!StrUtils.isNull(billnos)) {
			this.qryMap.put("billno", billnos);
		}
		
	}
	
	@Action
	public void exportBtn() {
		//MessageUtils.alert("QQQQQQQQQQ");
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/ship/billregister.raq";
		AppUtils.openWindow("",openUrl);
	}

	@Action
	public void pass() {
		if (StrUtils.isNull(workItemId)) {
			MessageUtils.alert("当前没有活动的节点");
			return;
		}
		List actorIds = new ArrayList();

		if (!StrUtils.isNull(processInstanceId)) {
//			Map<String, Object> m = new HashMap<String, Object>();
//			try {
//				WorkFlowUtil.passProcess(processInstanceId, workItemId, m,
//						actorIds);
//				MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
		}
	}

	 @Action
	 public void add() {
		 this.selectedRowData = new BusShipBillreg();
		 if(billid!=""){
			 this.selectedRowData.setBillid(Long.parseLong(billid));
			 BusShipping busShipping = this.serviceContext.busShippingMgrService.busShippingDao.findById(Long.parseLong(billid));
			 FinaJobs finaJob = this.serviceContext.jobsMgrService.finaJobsDao.findById(busShipping.getJobid());
			 this.selectedRowData.setSales(finaJob.getSales());
			 Browser.execClientScript("$('#sales_input').val('"+finaJob.getSales()+"')");
		 }
		 this.selectedRowData.setBillno(billnos);
		 update.markUpdate(true, UpdateLevel.Data, "editPanel");
		 this.update.markUpdate(UpdateLevel.Data, "busbillid");
		 this.update.markUpdate(UpdateLevel.Data, "register");
		 this.update.markUpdate(UpdateLevel.Data, "canel");
		 showbillregisterWindow.show();
	 }

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		billregid = this.getGridSelectId();
		selectedRowData = new BusShipBillreg();
		selectedRowData = serviceContext.busShipBillregMgrService.BusShipBillregDao
				.findById(billregid);
		
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		this.update.markUpdate(UpdateLevel.Data, "busbillid");
		this.update.markUpdate(UpdateLevel.Data, "register");
		this.update.markUpdate(UpdateLevel.Data, "canel");
		showbillregisterWindow.show();
	}

	// public String getBillnos(){
	// long billid = selectedRowData.getBillid();
	// busshipbill =
	// serviceContext.busShipBillMgrService.busShipBillDao.findOneRowByClauseWhere("id = '"+billid+"'");
	// String mblno = busshipbill.getMblno();
	// String hblno = busshipbill.getHblno();
	// if(mblno == null ||"".equals(mblno)){
	// return hblno;
	// }
	// return mblno;
	// }

	@Action
	public void save() {
		try {
			serviceContext.busShipBillregMgrService.saveData(selectedRowData);
			Browser.execClientScript("showmsg()");
			showbillregisterWindow.close();
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			refresh();
		}
	}

	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		for (int i = 0; i < ids.length; i++) {
			long regid = Long.parseLong(ids[i]);
			String sql = "\ndelete from bus_ship_bill_reg where id = " + regid
					+ "";
			serviceContext.busShipBillregMgrService.BusShipBillregDao
					.executeSQL(sql);
			//this.Deletegoodstrack(regid);
			Browser.execClientScript("showmsg()");
			this.refresh();
		}
	}

	@Action
	public void recallinvsbmit() {
		Object isrecall = AppUtils.getReqParam("isrecall");
		boolean isaff = Boolean.valueOf(String.valueOf(isrecall));
		
		String sql = "";
		if(isaff){
			sql = "UPDATE bus_ship_bill_reg SET isrecall = "+isaff+" ,recalldate = NOW(),recalluser = '"
			+ AppUtils.getUserSession().getUsercode() + "' WHERE id =" + selectedRowData.getId() + ";";
		}else{
			sql = "UPDATE bus_ship_bill_reg SET isrecall = "+isaff+" ,recalldate = null,recalluser = null WHERE id =" + selectedRowData.getId() + ";";
		}
		try {
			serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(sql);
			MessageUtils.alert(isaff ? "Confirm OK!" : " Cancel OK!");
			refresh();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}