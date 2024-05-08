package com.scp.view.module.air;

import java.util.Date;
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

import com.scp.exception.CommonRuntimeException;
import com.scp.exception.MoreThanOneRowException;
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
import com.scp.view.module.ship.JobsnosChooseService;

@ManagedBean(name = "pages.module.air.jobseditlclBean", scope = ManagedBeanScope.REQUEST)
public class JobsEditlclBean extends MastDtlView {

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
	public UIIFrame statusIframe;
	
	@Bind
	public UIIFrame queryIframe;
	
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
//			disableAllButton(selectedRowData.getIscheck());
			
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

	@Action
	public void showAttachmentIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			attachmentIframe.load(blankUrl);
		} else {
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
		}
	}

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
		}
	}
	
	/**
	 * 完成事件处理
	 */
	@Action
	public void iscompleteAjaxSubmit() {
		Boolean isComplete = selectedRowData.getIscomplete();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (isComplete) {
				sql = "UPDATE fina_jobs SET iscomplete = TRUE,updater='"
						+ updater + "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			} else {
				sql = "UPDATE fina_jobs SET iscomplete = FALSE,updater='"
						+ updater + "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			this.commonCheck();
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIscomplete(!isComplete);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		}
		
	} 
	
	/**
	 * 关闭,审核,完成判断处理
	 */
	@Action
	public void commonCheck() {
		Boolean isComplete = selectedRowData.getIscomplete();
		if (isComplete) {
			this.disableAllButton(true);
			} else {
			this.disableAllButton(false);
			}
	}

	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			receiptIframe.load(blankUrl);
		} else {
			receiptIframe.load("../ship/busbill.xhtml?jobid=" + this.mPkVal);
		}
	}

	@Action
	public void showDocDef() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			docdefIframe.load(blankUrl);
		} else {
			docdefIframe.load("../bus/busdocdef.xhtml?linkid=" + this.shipid);
		}
	}

	@Action
	public void showShipping() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			shippingIframe.load(blankUrl);
		} else {
			shippingIframe.load("../air/busshippinglcl.xhtml?id=" + this.mPkVal);
		}
	}

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
			 truckIframe.load("../bus/bustruck.xhtml?id=" + this.mPkVal);
		}
	}

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
			 customerIframe.load("../bus/buscustoms.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showBill() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			billIframe.load(blankUrl);
		} else {
			billIframe.load("../ship/busshipbill.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showMsg() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			msgIframe.load(blankUrl);
		} else {
			msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
		}
	}
	
	/**
	 * 状态
	 */
	@Action
	public void showStatus() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			statusIframe.load(blankUrl);
		} else {
			String blno=this.selectedRowData.getNos();
			statusIframe.load("../air/status.xhtml?fkid="+ this.mPkVal+"&blno="+ blno);
		}
	}
	
	/**
	 * 跟踪
	 */
	@Action
	public void showQuery() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			queryIframe.load(blankUrl);
		} else {
			String blno=this.selectedRowData.getNos();
			queryIframe.load("../air/blno_query.jsp?blno="+ blno);
		}
	}

	@Action
	public void showConstruct() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			constructIframe.load(blankUrl);
		} else {
			constructIframe.load("../ship/jobschild.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		} else {
			traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
		}
	}
	
	@Action
	public void showAssign() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			assignIframe.load(blankUrl);
		} else {
			assignIframe.load("../customer/assigneduser.xhtml?id="
					+ this.shipid + "&linktype=J");
		}
	}

	@Override
	public void init() {
		selectedRowData = new FinaJobs();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			disableAllButton(selectedRowData.getIsubmit());
		}else{
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {

		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.mPkVal);
		if(this.selectedRowData != null) {
			if(!this.selectedRowData.getIstruck()) {
				this.tabLayout.removeTab("truckPanel");
			} else {
				if(this.isPostBack) {
					this.tabLayout.addTab("拖车", "../bus/bustruck.xhtml?id=" + this.mPkVal,"commontabico_tpcls");
				}
			}
			
			if(!this.selectedRowData.getIscus()) {
				this.tabLayout.removeTab("customsPanel");
			} else {
				if(this.isPostBack) {
					this.tabLayout.addTab("报关", "../bus/buscustoms.xhtml?id=" + this.mPkVal,"commontabico_tpcls");
				}
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		//setFnos();
		getshipid(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "shipid");
		showArapEdit();
		// showDocDef();
		// showMsg();
		// showTrace();
		// showAttachmentIframe();
		// showAssign();
		//showBill();
		// showConstruct();
		// showReceiptEdit();
		// showTruck();
		showShipping();

	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.jobsMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();

		String sql = " isdelete = false AND jobid =" + this.mPkVal;
		try {

			this.shippinginit = serviceContext.busShippingMgrService.busShippingDao
					.findOneRowByClauseWhere(sql);
			this.refreshMasterForm();
			Browser.execClientScript("showmsg()");

		} catch (NoRowException e) {
			shippinginit = new BusShipping();
			setCorpc(this.selectedRowData.getCustomerid());
			shippinginit.setJobid(this.mPkVal);
			shippinginit.setCustomerid(this.selectedRowData.getCustomerid());
			shippinginit
					.setCustomerabbr(this.selectedRowData.getCustomerabbr());
			shippinginit.setNos(this.selectedRowData.getNos());
			shippinginit.setSingtime(this.selectedRowData.getJobdate());
			shippinginit.setRefno(this.selectedRowData.getRefno());
			shippinginit.setLdtype(this.selectedRowData.getLdtype());
			shippinginit.setCustcontact(this.corpc.getName());
			shippinginit.setCustfax(this.corpc.getFax());
			shippinginit.setCustmobile(this.corpc.getPhone());
			shippinginit.setCusttel(this.corpc.getTel());
			//shippinginit.setWarehouseid(setWarehouseid());
			serviceContext.busShippingMgrService.saveData(shippinginit);
			this.refreshMasterForm();
			Browser.execClientScript("showmsg()");
		}catch(MoreThanOneRowException e){
			MessageUtils.showException(e);
		}
		catch (Exception e) {
			MessageUtils.showException(e);
		} 
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
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new FinaJobs();
		
		shippinginit = new BusShipping();
		this.mPkVal = -1l;
		selectedRowData.setJobdate(new Date());
		selectedRowData.setJobtype("D");//
		selectedRowData.setLdtype("L");
		selectedRowData.setLdtype2("B");
		selectedRowData.setIsshipping(true);
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		//执行客户端脚本
		//Browser.execClientScript("autoShowLink()");
		
		showArapEdit();
		// showDocDef();
		// showMsg();
		// showTrace();
		// showAttachmentIframe();
		// showAssign();
		//showBill();
		// showConstruct();
		// showReceiptEdit();
		// showTruck();
		showShipping();

	}

	@Override
	public void delMaster() {
		try {
			serviceContext.jobsMgrService.removeDate(this.mPkVal, AppUtils
					.getUserSession().getUsercode());
			this.addMaster();
			Browser.execClientScript("showmsg()");
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
		return this.jobsnosChooseService.getJobsnosDataProvider("AND jobtype = 'D'");
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
						"AND jobtype = 'D'");
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

	
	@Action
	public void createPod(){
		if(this.mPkVal ==-1l){
			MessageUtils.alert("工作单未保存");
			return;
		}
		try {
			serviceContext.jobsMgrService.createPod(this.mPkVal,AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			//setFnos();
			
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 加载海外单号
	 */
	public void setFnos(){
		String sql = "SELECT f_lists((SELECT nos FROM fina_jobs WHERE id = k.jobidto AND isdelete = FALSE)) AS fnos FROM fina_jobs_link k  WHERE k.jobidfm = "+this.mPkVal;
		
		try {
			Map m = serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.fnos = (String) m.get("fnos");
			} catch (NoRowException e) {
			this.fnos ="";
		}
			
			this.update.markUpdate(UpdateLevel.Data, "fnos");
	}

	
	
	@Action
	public void addJobsLeaf() {
		try {
			String jobnoleaf = serviceContext.jobsMgrService.addJobsLeaf(this.mPkVal , AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!委托单号："+jobnoleaf);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void isubmitAjaxSubmit() {
		Boolean isSubmit = selectedRowData.getIsubmit();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		try {
			if (this.mPkVal == -1l)throw new CommonRuntimeException("Plese save first!");
			if (selectedRowData.getIscheck())throw new CommonRuntimeException("Current is check,plese uncheck first!");
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isubmit = TRUE ,submitime = NOW(),submiter = '"+updater+"' WHERE id ="+this.mPkVal+";";
			}else {
				sql = "UPDATE fina_jobs SET isubmit = FALSE ,submitime = NULL,submiter = NULL WHERE id ="+this.mPkVal+";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isSubmit);
		}catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			refreshMasterForm();
			this.disableAllButton(!isSubmit);
		}catch (Exception e) {
			MessageUtils.showException(e);
			refreshMasterForm();
			this.disableAllButton(!isSubmit);
		}
	}
	
//	/**
//	 * 设置仓库为默认的外运仓
//	 */
//	public Long setWarehouseid(){
//		String sql = "SELECT id FROM dat_warehouse WHERE code = 'wyc' AND isdelete = FALSE;";
//		try {
//			Map m = serviceContext.daoIbatisTemplate
//					.queryWithUserDefineSql4OnwRow(sql);
//			Long warehouseid = (Long) m.get("id");
//			return warehouseid;
//		} catch (Exception e) {
//			return null;
//		}
//	
//		
//	}
	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "airentrust.raq";
	/**
	 * 预览报表 --
	 */
	@Action
	public void preview() {
		
		String rpturl = AppUtils.getRptUrl();
		try{
			String filename = showwmsinfilename;
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/air/"+filename+"&id="+this.mPkVal;
			AppUtils.openWindow("_print", openUrl);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	

}
