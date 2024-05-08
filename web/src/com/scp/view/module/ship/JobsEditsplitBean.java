package com.scp.view.module.ship;

import java.util.List;
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
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysDepartment;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.ship.jobseditsplitBean", scope = ManagedBeanScope.REQUEST)
public class JobsEditsplitBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	
	

	@Bind
	@SaveState
	@Accessible
	public Long shipid;

	@Bind
	public UIIFrame shippingIframe;

	@Bind
	public UIIFrame truckIframe;

	@Bind
	public UIIFrame customerIframe;

	@Bind
	public UIIFrame billIframe;

	@Bind
	public UIIFrame arapIframe;

	@Bind
	public UIIFrame receiptIframe;

	@Bind
	public UIIFrame attachmentIframe;

	@Bind
	public UIIFrame docdefIframe;

	@Bind
	public UIIFrame assignIframe;

	@Bind
	public UIIFrame constructIframe;

	@Bind
	public UIIFrame msgIframe;

	@Bind
	public UIIFrame traceIframe;
	
	@Bind
	public UITabLayout tabLayout;
	
	@Bind
	public Long department;
	
	@Bind
	@Accessible
	public String fnos;
	
	@Bind
	public Long sale;
	
	@SaveState
	public Boolean isPostBack = false;

	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();

	@SaveState
	@Accessible
	public BusShipping shippinginit = new BusShipping();
	
	@SaveState
	@Accessible
	public SysCorpcontacts corpc = new SysCorpcontacts();

	@Override
	public void refreshForm() {

	}

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			// disableAllButton(selectedRowData.getIscheck());
		} else {
			this.isPostBack = true;
		}
	}
	
	@Action
    private void changeDept() {
		String sale = AppUtils.getReqParam("sale");
        if(!StrUtils.isNull(sale)) {
        	this.sale = Long.parseLong(sale);
        	
        	SysDepartment sd = this.serviceContext.userMgrService.sysUserDao.findById(this.sale).getSysDepartment();
        	if(sd != null) {
        		this.department = sd.getId();
        	}
          this.update.markUpdate(UpdateLevel.Data, "department");
        }
    }

	@SaveState
	private boolean showAttachmentIframe;
	@Action
	public void showAttachmentIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			attachmentIframe.load(blankUrl);
		} else {
			if(!showAttachmentIframe)attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
			showAttachmentIframe = true;
		}
	}

	@SaveState
	private boolean showArapEdit;
	@Action
	public void showArapEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			arapIframe.load(blankUrl);
		} else {
			arapIframe.load("../finance/arapedit.xhtml?customerid="
					+ this.selectedRowData.getCustomerid() + "&jobid="
					+ this.mPkVal);
			//showArapEdit = true;
		}
	}

	@SaveState
	private boolean showReceiptEdit;
	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			receiptIframe.load(blankUrl);
		} else {
			if(!showReceiptEdit)receiptIframe.load("../ship/busbill.xhtml?jobid=" + this.mPkVal);
			showReceiptEdit = true;
		}
	}

	@SaveState
	private boolean showDocDef;
	@Action
	public void showDocDef() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			docdefIframe.load(blankUrl);
		} else {
			if(!showDocDef)docdefIframe.load("../bus/busdocdef.xhtml?linkid=" + this.shipid);
			showDocDef = true;
		}
	}

	@SaveState
	private boolean showShipping;
	@Action
	public void showShipping() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			shippingIframe.load(blankUrl);
		} else {
			if(!showShipping)shippingIframe.load("../ship/busshipping.xhtml?id=" + this.mPkVal);
			showShipping = true;
		}
	}

	@SaveState
	private boolean showTruck;
	/**
	 * 拖车
	 * Neo 2014 4/30
	 */
	@Action
	public void showTruck() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			truckIframe.load(blankUrl);
		} else {
			if(!showTruck)truckIframe.load("../bus/bustruck.xhtml?id=" + this.mPkVal);
			showTruck = true;
		}
	}

	@SaveState
	private boolean showCustomerIframe;
	/**
	 * 报关
	 * Neo 2014 4/29
	 */
	@Action
	public void showCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			customerIframe.load(blankUrl);
		} else {
			if(!showCustomerIframe)customerIframe.load("../bus/buscustoms.xhtml?id=" + this.mPkVal);
			showCustomerIframe = true;
		}
	}

	@SaveState
	private boolean showBill;
	@Action
	public void showBill() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			billIframe.load(blankUrl);
		} else {
			if(!showBill)billIframe.load("../ship/busshipbill.xhtml?id=" + this.mPkVal);
			showBill = true;
		}
	}

	@SaveState
	private boolean showMsg;
	@Action
	public void showMsg() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			msgIframe.load(blankUrl);
		} else {
			if(!showMsg)msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
			showMsg = true;
		}
	}

	@SaveState
	private boolean showAssign;
	@Action
	public void showAssign() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			assignIframe.load(blankUrl);
		} else {
			if(!showAssign)assignIframe.load("../customer/assigneduser.xhtml?id="
					+ this.shipid + "&linktype=J");
			showAssign = true;
		}
	}

	@SaveState
	private boolean showConstruct;
	
	
	@Action
	public void showConstruct() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			constructIframe.load(blankUrl);
		} else {
			constructIframe.load("../ship/jobschildsplit.xhtml?id=" + this.mPkVal);
			
		}
	}

	@SaveState
	private boolean showTrace;
	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		} else {
			if(!showTrace)traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
			showTrace = true;
		}
	}

	@Override
	public void init() {
		selectedRowData = new FinaJobs();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		}else{
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {

		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.mPkVal);
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		//setFnos();
		getshipid(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "shipid");
		
		 showConstruct();
		

	}

	

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
	}

	

	@Override
	public void delMaster() {
		try {
			serviceContext.jobsMgrService.removeDate(this.mPkVal, AppUtils
					.getUserSession().getUsercode());
			this.addMaster();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {

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
		selectedRowData.setSaleid((Long)m.get("salesid"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		this.update.markUpdate(UpdateLevel.Data, "sale");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	public void getshipid(Long id) {
		String sql = "SELECT id FROM bus_shipping where isdelete =false AND jobid= "
				+ this.mPkVal;
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.shipid = (Long) m.get("id");
		} catch (Exception e) {
			//MsgUtil.showException(e);
			MessageUtils.alert("数据异常,委托单被非法删除,请检查!");
			return;
		}
		
	}

	// 弹窗选择工作单
	@Bind
	@SaveState
	@Accessible
	public String jobnos;

	@ManagedProperty("#{jobsnoschooseserviceBean}")
	private JobsnosChooseService jobsnosChooseService;

	@Bind
	public UIWindow showJobsnosWindow;

	@Bind
	public UIDataGrid jobnosGrid;

	@Bind(id = "jobnosGrid", attribute = "dataProvider")
	public GridDataProvider getJobnosGridDataProvider() {
		return this.jobsnosChooseService.getJobsnosDataProvider("AND (jobtype = 'F' OR jobtype = 'I') ");
	}

	@Bind
	private String qryJobsnosKey;

	@Action
	public void jobsnosQry() {
		this.jobsnosChooseService.qryNos(qryJobsnosKey);
		this.jobnosGrid.reload();
	}

	@Action
	public void showJobnosAction() {
		String jobnos = (String) AppUtils.getReqParam("jobnos");
		qryJobsnosKey = jobnos;
		int index = qryJobsnosKey.indexOf("/");
		if (index > 1)
			qryJobsnosKey = qryJobsnosKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryJobsnosKey");
			showJobsnosWindow.show();
			jobsnosQry();
			Browser.execClientScript("qryButton.focus");
			return;
			// type = '0',只返回一个直接回填,多个显示弹窗
		} else {
			try {
				List<Map> cs = jobsnosChooseService.findJobsnos(qryJobsnosKey,
						"AND jobtype = 'F'");
				if (cs.size() == 1) {
					this.jobnos = ((String) cs.get(0).get("nos"));
					this.mPkVal = ((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "jobnos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					refreshMasterForm();
					showJobsnosWindow.close();
				} else {
					this.update.markUpdate(UpdateLevel.Data, "qryJobsnosKey");
					showJobsnosWindow.show();
					jobsnosQry();
					Browser.execClientScript("qryButton.focus");
				}

			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	}

	@Action
	public void jobnosGrid_ondblclick() {
		Object[] objs = jobnosGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.jobnos = ((String) m.get("nos"));
		this.mPkVal = ((Long) m.get("id"));
		this.update.markUpdate(UpdateLevel.Data, "jobnos");
		this.update.markUpdate(UpdateLevel.Data, "mPkVal");
		showJobsnosWindow.close();
		refreshMasterForm();
	}
//	/**
//	 * NEO
//	 * 2014/4/14 11:48
//	 * 搜索工作单完成的时候 下方页面跳转到委托单
//	 */
//	public void refreshMasterForm2() {
//
//		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao
//				.findById(this.mPkVal);
//		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
//		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
//		getshipid(this.mPkVal);
//		update.markUpdate(true, UpdateLevel.Data, "shipid");
//		showShipping();
//	}
	
	public void setCorpc(Long customerid){
		String sql = "isdelete = FALSE AND isdefault = TRUE AND customerid = "+customerid;
		
		try {
			this.corpc = serviceContext.customerContactsMgrService.sysCorpcontactsDao
					.findOneRowByClauseWhere(sql);
		} catch (NoRowException e) {
			this.corpc =  new SysCorpcontacts();
		}catch(Exception e){
			
		}
		
	}

	
	
	

	

	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}

}
