package com.scp.view.module.airtransport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.bus.BusAir;
import com.scp.model.finance.FinaJobs;
import com.scp.model.order.BusOrder;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.ship.JobsnosChooseService;
@ManagedBean(name = "pages.module.airtransport.jobseditBean", scope = ManagedBeanScope.REQUEST)
public class JobseditBean extends MastDtlView{
	
	@Autowired
	public ApplicationConf applicationConf;
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	@SaveState
	@Accessible
	public Long shipid;
	
	@Bind
	@SaveState
	public String orderno;
	
	@Bind
	public UIWindow showMessageWindow;
 
	@Bind
	public UIIFrame airIframe;

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
	public UIIFrame invoIframe;

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
	private UIIFrame dtlIFrame;

	@Bind
	public UITabLayout tabLayout;
	
	@Bind
	public UIIFrame agenbillIframe;
	
	@Bind
	public UIIFrame queryIframe;
	
	@Bind
	public UIIFrame formdefineIframe;
	
	
	@Bind
	public UIIFrame foreigntruckIframe;

	@Bind
	public UIIFrame foreigncustomerIframe;
	
	@Bind
	public UIWindow showChildWindow;
	
	@Bind
	@SaveState
	public String deptname;
	
	@Action
	public void showChild() {
		showChildWindow.show();
	}
	
	@Bind
	public UIButton addJobsLeaf;
	
	
	@Bind
	@SaveState
	public Long department;

	@Bind
	@Accessible
	public String fnos;

	@Bind
	public Long sale;

	@SaveState
	public Boolean isPostBack = false;

	public Long userid;
	
	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();

	@SaveState
	@Accessible
	public BusAir shippinginit = new BusAir();

	@SaveState
	@Accessible
	public SysCorpcontacts corpc = new SysCorpcontacts();
	
	@SaveState
	@Accessible
	public String processInstanceId;
	
	@SaveState
	@Accessible
	public String workItemId;
	
	@Bind
	public String customerNamec;
	
	@Bind
	public UIIFrame statusIframe;
	
	@Bind
	public UIButton addMaster;
	
	@SaveState
	public int activeTab = 0;//页面显示页
	
	@Bind
	public String isuserdzz;
	
	@Bind
	public String ismodifynos;
	
	@Bind
	@SaveState
	public String actionJsText;
	
	@Override
	public void refreshForm() {

	}

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.userid = AppUtils.getUserSession().getUserid();
			initialization();
			init();
			//initCtrl();
			this.commonCheck();
			if ((this.getMsgcontent() != null && !"".equals(this
					.getMsgcontent()))) {
				showMessageWindow.show();
				this.getMsgcontent();
				this.update.markUpdate("content");
			}

			String type = AppUtils.getReqParam("type");
			actionJsText = "";
			if("bpm".equals(type)){
				String taskid = AppUtils.getReqParam("taskid");
				if(!StrUtils.isNull(taskid)){
					BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
					List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmTask.getProcessid()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'js'");
					for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
						actionJsText+=bpmWorkItem.getActions();
					}
				}else{
					String processid = AppUtils.getReqParam("processid");
					List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+processid+" AND taskname = 'Start' AND actiontype = 'js'");
					for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
						actionJsText+=bpmWorkItem.getActions();
					}
				}
				if(actionJsText.indexOf("saveMasterJsVar.setDisabled(false)")>=0){
					saveMaster.setDisabled(false);
				}
				if(actionJsText.indexOf("saveMasterJsVar.setDisabled(true)")>=0){
					saveMaster.setDisabled(true);
				}
				
				//System.out.println("actionJsText:"+actionJsText);
				update.markUpdate(true, UpdateLevel.Data, "actionJsText");
			}
			
			List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao.findAllByClauseWhere(" formid = '"+this.getMBeanName()+"' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
			for (Sysformcfg sysformcfg : sysformcfgs) {
				actionJsText+=sysformcfg.getJsaction();
			}
			//System.out.println("actionJsText:"+actionJsText);
			update.markUpdate(true, UpdateLevel.Data, "actionJsText");
		} else {
			this.isPostBack = true;
		}
	}
	@Override
	public void saveMaster() {
		super.saveMaster();
	}
	private void initCtrl() {
		addMaster.setDisabled(true);
		saveMaster.setDisabled(true);
		delMaster.setDisabled(true);
		addJobsLeaf.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(230000100L)) {
			if (s.endsWith("_add")) {
				addMaster.setDisabled(false);
				saveMaster.setDisabled(false);
				addJobsLeaf.setDisabled(false);
			} else if (s.endsWith("_update")) {
				saveMaster.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}else if (s.endsWith("_modify")) {
				ismodifynos = "true";
			}
			
		}
		if(this.selectedRowData != null && selectedRowData.getIslock()){
			saveMaster.setDisabled(true);
			delMaster.setDisabled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Action
	public void clearnos(){
		String sql = "UPDATE fina_jobs set nos = '', updater ='"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE id ="+selectedRowData.getId() + ";";
		try {
			this.serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.refreshMasterForm();
	}

	@Action
	private void changeDept() {
		String sale = AppUtils.getReqParam("sale");
		if (!StrUtils.isNull(sale)) {
			this.sale = Long.parseLong(sale);

			SysDepartment sd = this.serviceContext.userMgrService.sysUserDao
					.findById(this.sale).getSysDepartment();
			if (sd != null) {
				this.department = sd.getId();
			}
			this.update.markUpdate(UpdateLevel.Data, "department");
		}
	}
	
	@Action
	private void createDesktopLink() {
//		if(!applicationConf.getIsUseDzz()){
//			this.alert("DZZ未启用");
//			return;
//		}
		if(this.mPkVal == null || this.mPkVal <= 0){
			this.alert("无效ID");
			return;
		}
		try {
			String httport = AppUtils.getServerHttPort();
			String dzzuid = AppUtils.getUserSession().getDzzuid();
			String sqlQry = "SELECT f_dzz_desktop_add('httport="+httport+";type=Jobs;jobid="+this.mPkVal+";dzzuid="+dzzuid+";') As t;";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			String sql = StrUtils.getMapVal(m, "t");
//			this.serviceContext.dzzService.executeQuery(sql);
			Browser.execClientScript("showmsg()");
			//Browser.execClientScript(script)
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airFinaEnclosure")){
					this.activeTab = i;
				}
			}
			// if(!showAttachmentIframe)
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
			// showAttachmentIframe = true;
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
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airFinaCost")){
					this.activeTab = i;
				}
			}
			
			String url = arapIframe.getSrc();
			if(StrUtils.isNull(url) || url.indexOf("blank.xhtml") > 1){
				arapIframe.setSrc("../finance/arapedit.xhtml?customerid="
						+ this.selectedRowData.getCustomerid() + "&jobid="
						+ this.mPkVal +"&processInstanceId=" + processInstanceId + "&workItemId=" + workItemId);
				this.update.markAttributeUpdate(arapIframe,"src");
			}
			showArapEdit = true;
		}
	}

	@SaveState
	private boolean showReceiptEdit;

	
	/**
	 * CC终审流程
	 */

//	public void affirmLast() {
//		Map<String, Object> m = new HashMap<String, Object>();
//		if (!StrUtils.isNull(processInstanceId) && !StrUtils.isNull(workItemId)) {
//			try {
//				WorkFlowUtil.passProcess(processInstanceId, workItemId, m);
//				MessageUtils.alert("Confirm OK!,流程已通过!");
//			} catch (EngineException e) {
//				e.printStackTrace();
//			} catch (KernelException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			receiptIframe.load(blankUrl);
		} else {
			// if(!showReceiptEdit)
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airFinabill")){
					this.activeTab = i;
				}
			}
			String str = "SELECT id FROM fina_bill WHERE isdelete = FALSE AND jobid = "+this.mPkVal+" ORDER BY CASE WHEN inputtime IS NOT NULL > updatetime  IS NOT NULL THEN inputtime ELSE updatetime END DESC LIMIT 1";
			Map map = null;
			try {
				map = this.serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(str);
			} catch (NoRowException e) {
				//AppUtils.debug("当前工作单无账单,默认进入新增状态!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String url = "";
			if(map!=null){
				url = "../ship/busbill.xhtml?jobid=" + this.mPkVal + "&processInstanceId=" + processInstanceId + "&workItemId=" + workItemId + "&id=" + map.get("id");
			}else{
				url = "../ship/busbill.xhtml?jobid=" + this.mPkVal + "&processInstanceId=" + processInstanceId + "&workItemId=" + workItemId;
			}
			receiptIframe.load(url);
			// showReceiptEdit = true;
		}
	}
	
	
	@Action
	public void showInvoEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			invoIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airFinainvoice")){
					this.activeTab = i;
				}
			}
			// if(!showReceiptEdit)
			String str = "SELECT id FROM fina_invoice WHERE isdelete = FALSE AND jobid = "+this.mPkVal+" ORDER BY CASE WHEN inputtime IS NOT NULL > updatetime  IS NOT NULL THEN inputtime ELSE updatetime END DESC LIMIT 1";
			Map map = null;
			try {
				map = this.serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(str);
			} catch (NoRowException e) {
				//AppUtils.debug("当前工作单无发票,默认进入新增状态!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String url = "";
			if(map!=null){
				url = "../ship/businvoice.xhtml?jobid=" + this.mPkVal + "&id=" + map.get("id");
			}else{
				url = "../ship/businvoice.xhtml?jobid=" + this.mPkVal;
			}
			invoIframe.load(url);
			// showReceiptEdit = true;
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
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airFinaFill")){
					this.activeTab = i;
				}
			}
			// if(!showDocDef)
			docdefIframe.load("../bus/busdocdef.xhtml?linkid=" + this.shipid);
			// showDocDef = true;
		}
	}

	@SaveState
	private boolean showShipping;

	@Action
	public void showShipping() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			airIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(委托)
				if(list.get(i).get("key").equals("airFinaEntrust")){
					this.activeTab = i;
				}
			}
			String url = airIframe.getSrc();
			if(StrUtils.isNull(url) || url.indexOf("blank.xhtml") > 1){
				airIframe.setSrc("../airtransport/air.xhtml?id=" + this.mPkVal);
				this.update.markAttributeUpdate(airIframe,"src");
			}
			
			showShipping = true;
			openIformType = "airIframe";
		}
	}

	@SaveState
	private boolean showTruck;

	@Action
	public void showTruck() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			truckIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(委托)
				if(list.get(i).get("key").equals("airFinaTrack")){
					this.activeTab = i;
				}
			}
			truckIframe.load("../bus/bustruck.xhtml?id=" + this.mPkVal);
			openIformType = "airTrack";
		}
	}
	
	
	
	
	@SaveState
	private boolean showFormDefine;

	@Action
	public void showFormDefine() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			formdefineIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airformdefine")){
					this.activeTab = i;
				}
			}
			formdefineIframe.load("/scp/pages/module/formdefine/dynamicform.xhtml?m="+this.getMBeanName()+"&p="+this.mPkVal);
		}
	}
	

	@SaveState
	private boolean showCustomerIframe;

	@Action
	public void showCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			customerIframe.load(blankUrl);
		} else {
			// if(!showCustomerIframe)
			customerIframe.load("./buscustoms.xhtml?id=" + this.mPkVal);
		}
	}

	@SaveState
	private boolean showBill;

	@Action
	public void showBill() {
//		if (this.mPkVal == -1l) {
//			String blankUrl = AppUtils.getContextPath()
//					+ "/pages/module/common/blank.xhtml";
//			billIframe.load(blankUrl);
//		} else {
//			// if(!showBill)
//			billIframe.load("../ship/busshipbill.xhtml?id=" + this.mPkVal);
//			// showBill = true;
//		}
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
			// if(!showMsg)
			msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
			// showMsg = true;
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
			// if(!showAssign)
			assignIframe.load("../customer/assigneduser.xhtml?id="
					+ this.shipid + "&linktype=J");
			// showAssign = true;
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
			constructIframe.load("../airtransport/jobschild.xhtml?id=" + this.mPkVal);
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
			traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
		}
	}
	


	@Action
	public void showAgenbill() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			agenbillIframe.load(blankUrl);
		} else {
			agenbillIframe.load("../agenbill/agenbill.xhtml?jobid=" + this.mPkVal);
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
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("airFinaTrack")){
					this.activeTab = i;
				}
			}
			String blno=this.selectedRowData.getNos();
			statusIframe.load("../common/goodstrack.xhtml?fkid="+ this.mPkVal+"&blno="+ blno + "&type=S");
		}
	}

	@Override
	public void init() {
		////System.out.println(AppUtils.getUserSession().getUserid());
		addMaster.setDisabled(false);
		selectedRowData = new FinaJobs();
		String id = AppUtils.getReqParam("id");
		 processInstanceId =(String)AppUtils.getReqParam("processInstanceId");
		 workItemId =(String)AppUtils.getReqParam("workItemId");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			this.commonCheck();
		} else {
			addMaster();
		}
		String qryTypeStr = AppUtils.getReqParam("qryType");
		String jobnosStr = AppUtils.getReqParam("jobnos");
		String currentTab = AppUtils.getReqParam("currentTab");
		
		if(!StrUtils.isNull(qryTypeStr)){
			this.qryType = qryTypeStr;
			this.jobnos = jobnosStr;
		}else{
			this.qryType = "nos";
		}
		if(!StrUtils.isNull(currentTab)){
			try {
				int tabIndex = Integer.parseInt(currentTab);
				activeTab = tabIndex;
				this.tabLayout.setActiveTab(tabIndex);
				refreshMasterForm();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	@Action
	public void refreshMasterForm() {

		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.mPkVal);
		if(selectedRowData.getCustomerid() != null && selectedRowData.getCustomerid() > 0){
			SysCorporation sysCorporation  = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			this.customerNamec = StrUtils.isNull(sysCorporation.getNamec())?sysCorporation.getNamee():sysCorporation.getNamec();
		}
		if(selectedRowData.getOrderid() != null){
			try {
				BusOrder bo = this.serviceContext.busOrderMgrService.busOrderDao.findById(selectedRowData.getOrderid());
				this.orderno = bo.getOrderno();
			}catch (NullPointerException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}catch (NoRowException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.commonCheck();
//		if(this.selectedRowData!=null ){
//			this.deptids = getqueryDepartid();
//		}
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		//update.markUpdate(true, UpdateLevel.Data, "deptcomb");
		getshipid(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "shipid");
		for(int i = 0;i<list.size();i++){//遍历找出显示位置
			if(list.get(i).get("key").equals("airFinaEntrust")&&activeTab==i){
				showShipping();
			}
			if(list.get(i).get("key").equals("airFinaCost")&&activeTab==i){
				showArapEdit();
			}
			if(list.get(i).get("key").equals("airFinabill")&&activeTab==i){
				showReceiptEdit();
			}
			if(list.get(i).get("key").equals("airFinainvoice")&&activeTab==i){
				showInvoEdit();
			}
			if(list.get(i).get("key").equals("airFinaEnclosure")&&activeTab==i){
				showAttachmentIframe();
			}
			if(list.get(i).get("key").equals("airFinaFill")&&activeTab==i){
				showDocDef();
			}
			if(list.get(i).get("key").equals("airFinaTrack")&&activeTab==i){
				showStatus();
			}
			if(list.get(i).get("key").equals("airformdefine")&&activeTab==i){
				showFormDefine();
			}
		}
		Browser.execClientScript("changeJobs('"+this.selectedRowData.getCustomerabbr()+"','"+this.selectedRowData.getSales()+"')");
		//this.tabLayout.setActiveTab(activeTab);
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(this.selectedRowData.getSaleid());
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
		//2272 工作单编辑界面，在单号这里切换后，原业务员里面的下拉框数据，还是上一个工作单委托人的，这个会串业务员的，工作单里面刷新后，这个业务员下拉框也要对应刷一下
		Browser.execClientScript("customeridval="+selectedRowData.getCustomerid()+";saleselect();initSalesByCustomer('"+selectedRowData.getSales()+"');");
		showTipsCount();
		showStatus();
		showCustomerIframe();
	}

	@SaveState
	public String openIformType;//记录当前打开的iframe
	
	@Override
	public void doServiceSaveMaster() {
		if(selectedRowData.getSaleid()!=null&&selectedRowData.getSaleid()>0){
			try {
				SysUser sysuer = serviceContext.userMgrService.sysUserDao.findById(selectedRowData.getSaleid());
				selectedRowData.setDeptid(sysuer.getDeptid());//设置部门id为业务员的部门id
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			selectedRowData.setDeptid(null);
		}
		if(openIformType!=null&&openIformType.equals("airIframe")){//当当前打开的iframe是委托时候，调用委托页面里面的保存
			Browser.execClientScript("this.frames['airIframe']",
			"saveMasterJsvar.fireEvent('click');");
		}
		selectedRowData.getCorpid();
		selectedRowData.setIsair(true);
		selectedRowData.setIsshipping(false);
		serviceContext.jobsMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();

		String sql = " isdelete = false AND jobid =" + this.mPkVal;
		try {

			this.shippinginit = serviceContext.busAirMgrService.busAirDao
					.findOneRowByClauseWhere(sql);
			this.refreshMasterForm();
			Browser.execClientScript("showmsg()");

		} catch (NoRowException e) {
			shippinginit = new BusAir();
			setCorpc(this.selectedRowData.getCustomerid());
			shippinginit.setJobid(this.mPkVal);
//			shippinginit.setSignplace(this.companyToSign(this.selectedRowData.getCorpid()));
//			shippinginit.setCustomerid(this.selectedRowData.getCustomerid());
//			shippinginit
//					.setCustomerabbr(this.selectedRowData.getCustomerabbr());
//			shippinginit.setNos(this.selectedRowData.getNos());
			shippinginit.setSingtime(this.selectedRowData.getJobdate());
			shippinginit.setRefno(this.selectedRowData.getRefno());
//			shippinginit.setLdtype(this.selectedRowData.getLdtype());
//			shippinginit.setCustcontact(this.corpc.getName());
//			shippinginit.setCustfax(this.corpc.getFax());
//			shippinginit.setCustmobile(this.corpc.getPhone());
//			shippinginit.setCusttel(this.corpc.getTel());
			serviceContext.busAirMgrService.saveData(shippinginit);
			this.refreshMasterForm();
			Browser.execClientScript("showmsg()");
		} catch (MoreThanOneRowException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			MessageUtils.showException(e);
			refresh();
		}

	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	private void disableAllButton(Boolean flag) {
		saveMaster.setDisabled(flag);
		delMaster.setDisabled(flag);
		addJobsLeaf.setDisabled(flag);
	}

	@Action
	public void addJobsLeaf() {
		try {
			String jobnoleaf = serviceContext.jobsMgrService.addJobsLeafair(
					this.mPkVal, AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!委托单号：[" + jobnoleaf + "]");
			//Browser.execClientScript("tabLayoutJsVar.activeTab=0");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("新增失败！");
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new FinaJobs();

		shippinginit = new BusAir();
		this.mPkVal = -1l;
		selectedRowData.setJobdate(new Date());
		selectedRowData.setJobtype("A");
		selectedRowData.setLdtype("F");
		selectedRowData.setLdtype2("F");
		selectedRowData.setIsair(true);
		selectedRowData.setIsshipping(false);
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCustomerabbr("");
		selectedRowData.setSaleid(null);
		selectedRowData.setDeptid(null);
		selectedRowData.setIslock(false);
		selectedRowData.setSales("");
		this.department = null;
		this.isClose.setDisabled(false);
		this.isCheck.setDisabled(false);
		this.isComplete.setDisabled(false);
		this.repaintCheckbox();
		this.update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");

		this.showArapEdit = false;
		this.showAssign = false;
		this.showAttachmentIframe = false;
		this.showBill = false;
		this.showConstruct = false;
		// this.showCustomerIframe = false;
		this.showDocDef = false;
		this.showMsg = false;
		this.showReceiptEdit = false;
		this.showShipping = false;
		this.showTrace = false;
		this.showTruck = false;
		// 执行客户端脚本
		// Browser.execClientScript("autoShowLink()");

		showArapEdit();
		// showDocDef();
		// showMsg();
		// showTrace();
		// showAttachmentIframe();
		// showAssign();
		showBill();
		// showConstruct();
		// showReceiptEdit();
		// showTruck();
		showShipping();
//		this.deptids = getqueryDepartid();
		this.update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		Browser.execClientScript("deptnameJsVar.setValue();");
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
	public UIWindow showLogsWindow;
	
	@Bind
	public UIWindow showAssignWindow;
	
	@Bind
	public UIWindow showMessageAllWindow;
	
	
	@Action
	public void showLogs() {
		showLogsWindow.show();
	}
	
	@Action
	public void showAssignWin() {
		showAssignWindow.show();
	}
	
	@Action
	public void showMsgWin() {
		showMessageAllWindow.show();
	}
	

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
		if (m.get("salesid") != null) {
			selectedRowData.setSaleid((Long) m.get("salesid"));
			SysUser su = this.serviceContext.userMgrService.sysUserDao
					.findById((Long) m.get("salesid"));
			if (su != null) {
				if (su.getSysDepartment() != null) {
					selectedRowData.setDeptid(su.getSysDepartment().getId());
				} else {
					selectedRowData.setDeptid(null);
				}
			} else {
				selectedRowData.setDeptid(null);
			}
		} else {
			selectedRowData.setSaleid(null);
			selectedRowData.setDeptid(null);
		}
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		this.update.markUpdate(UpdateLevel.Data, "sale");
		this.update.markUpdate(UpdateLevel.Data, "department");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
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
		return this.jobsnosChooseService
				.getJobsnosDataProvider("AND (jobtype = 'A')");
	}

	@Bind
	private String qryJobsnosKey;

	@Bind
	@SaveState
	public String qryType;
	
	public void getshipid(Long id) {
		String sql = "SELECT id FROM bus_air where isdelete =false AND jobid= "
				+ this.mPkVal;
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.shipid = (Long) m.get("id");
		} catch (Exception e) {
			// MsgUtil.showException(e);
			MessageUtils.alert("数据异常,委托单被非法删除,请检查!");
			return;
		}

	}
	
	@Action
	public void jobsnosQry() {
		this.jobsnosChooseService.qryNos(qryJobsnosKey,qryType);
		this.jobnosGrid.reload();
	}

	@Action
	public void showJobnosAction() {
		String jobnos = (String) AppUtils.getReqParam("jobnos");
		qryType = (String) AppUtils.getReqParam("qryType");
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
				List<Map> cs = jobsnosChooseService
						.findJobsnos(qryJobsnosKey,
								"AND (jobtype = 'A')",qryType);
				if (cs.size() == 1) {
					this.jobnos = jobnos;
					this.mPkVal = ((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "jobnos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					
					if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_jobedit_redirect_url"))){
						String redirectUrl = "'./jobsedit.xhtml?id="+this.mPkVal+"&qryType="+qryType+"&jobnos="+jobnos+"&currentTab="+activeTab+"'";
						Browser.execClientScript("window.location.href=" + redirectUrl);
					}else{
						refreshMasterForm();
						showJobsnosWindow.close();
					}
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
		
		this.mPkVal = ((Long) m.get("id"));
		
		if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_jobedit_redirect_url"))){
			String redirectUrl = "'./jobsedit.xhtml?id="+this.mPkVal+"&qryType="+qryType+"&jobnos="+jobnos+"&currentTab="+activeTab+"'";
			Browser.execClientScript("window.location.href=" + redirectUrl);
		}else{
			this.jobnos = ((String) m.get("nos"));
			this.update.markUpdate(UpdateLevel.Data, "jobnos");
			this.update.markUpdate(UpdateLevel.Data, "mPkVal");
			showJobsnosWindow.close();
			this.showArapEdit = false;
			this.showAssign = false;
			this.showAttachmentIframe = false;
			this.showBill = false;
			this.showConstruct = false;
			this.showCustomerIframe = false;
			this.showDocDef = false;
			this.showMsg = false;
			this.showTrace = false;
			this.showReceiptEdit = false;
			this.showShipping = false;
			this.showTruck = false;
			refreshMasterForm();
		}
	}


	public void setCorpc(Long customerid) {
		String sql = "isdelete = FALSE AND isdefault = TRUE AND customerid = "
				+ customerid;
		try {
			this.corpc = serviceContext.customerContactsMgrService.sysCorpcontactsDao
					.findOneRowByClauseWhere(sql);
		} catch (NoRowException e) {
			this.corpc = new SysCorpcontacts();
		} catch (Exception e) {

		}

	}

	@Action
	public void createPod() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("工作单未保存");
			return;
		}
		try {
			serviceContext.jobsMgrService.createPod(this.mPkVal, AppUtils
					.getUserSession().getUserid(), AppUtils.getUserSession()
					.getUsercode());
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Bind
	public String content;

	public String getMsgcontent() {
		String sql = "SELECT f_sys_msgboard_getmsg('linkid=" + this.mPkVal
				+ ";user="+AppUtils.getUserSession().getUsercode()+"')";
		List message = new ArrayList();
		String mes = "";
		message = serviceContext.accountMgrService.datAccountDao
				.executeQuery(sql);
		for (int i = 0; i < message.size(); i++) {
			mes = mes + (String) message.get(i);
		}
		this.content = mes;
		return this.content;
	}

	/**
	 * 关闭,审核,完成判断处理
	 */
	@Action
	public void commonCheck() {
		Boolean isClose = selectedRowData.getIsclose();
		Boolean isCheck = selectedRowData.getIscheck();
		Boolean isComplete = selectedRowData.getIscomplete();
		Boolean isLock = selectedRowData.getIslock();
		
		if(isClose==null)isClose=false;
		if(isCheck==null)isCheck=false;
		if(isComplete==null)isComplete=false;
		if(isLock==null)isLock=false;
		
		if (isLock) {
			this.disableAllButton(true);
			this.isClose.setDisabled(true);
			this.isCheck.setDisabled(true);
			this.isComplete.setDisabled(true);
		} else {
			if (isClose || isCheck || isComplete) {
				this.disableAllButton(true);
			} else {
				this.disableAllButton(false);
				this.isClose.setDisabled(false);
				this.isCheck.setDisabled(false);
				this.isComplete.setDisabled(false);
				//initCtrl();
			}
			if (isCheck || isComplete) {
				this.isClose.setDisabled(true);
			} else {
				if (isClose) {
					this.isClose.setDisabled(true);
					this.isCheck.setDisabled(true);
					this.isComplete.setDisabled(true);
				}
			}
		}
		this.repaintCheckbox();
	}

	@Bind
	public UICheckBox isClose;

	/**
	 * 关闭事件处理
	 */
	@Action
	public void iscloseAjaxSubmit() {
		Boolean isClose = selectedRowData.getIsclose();
		Boolean isCheck = selectedRowData.getIscheck();
		Boolean isComplete = selectedRowData.getIscomplete();

		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (isCheck || isComplete) {
				MessageUtils.alert("暂时无法关闭!");
				return;
			}
			if (!isClose) {
				MessageUtils.alert("工作单已关闭，不可恢复！");
				return;
			}
			if (isClose) {
				sql = "UPDATE fina_jobs SET isclose = TRUE,updater='" + updater
						+ "',updatetime=NOW() WHERE id =" + this.mPkVal + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			
			this.commonCheck();
//			WorkFlowUtil.closejobs(this.mPkVal);
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIsclose(!isClose);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsclose(!isClose);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		}
	}

	@Bind
	public UICheckBox isCheck;

	/**
	 * 审核事件处理
	 */
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (isCheck) {
				sql = "UPDATE fina_jobs SET ischeck = TRUE,checkter = '"
						+ updater + "',checktime = NOW(),updater = '" + updater
						+ "',updatetime=NOW() WHERE id =" + this.mPkVal + ";";
			} else {
				sql = "UPDATE fina_jobs SET ischeck = FALSE,checkter = NULL,checktime = NULL,updater = '"
						+ updater
						+ "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			this.commonCheck();
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
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
			queryIframe.load("/web/blno_query.jsp" );
		}
	}

	@Bind
	public UICheckBox isComplete;

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
//				affirmLast();
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
			selectedRowData.setIscomplete(!isComplete);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		}
	}

	private void repaintCheckbox() {
		this.isClose.repaint();
		this.isCheck.repaint();
		this.isComplete.repaint();
	}

	public String companyToSign(long corpid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("US", "SHENZHEN,CHINA");
		map.put("SZ", "SHENZHEN,CHINA");
		map.put("GZ", "GUANGZHOU,CHINA");
		map.put("NB", "NINGBO,CHINA");
		map.put("SH", "SHANGHAI,CHINA");
		map.put("TJ", "TIANJIN,CHINA");
		map.put("QD", "QINGDAO,CHINA");
		Iterator it = map.keySet().iterator();
		String sql = "SELECT abbcode FROM sys_corporation WHERE id = "
				+ corpid + " ";
		List<Object> list = serviceContext.busShippingMgrService.busShippingDao
				.executeQuery(sql);
		if (list != null && list.size() > 0) {
			String abbr = (String) list.get(0);
			while (it.hasNext()) {
				String key = (String) it.next();
				if (abbr.contains(key)) {
					return map.get(key);
				}
			}
		}
		return null;
	}
	
	/**
	 * 拖车
	 */
	@Action
	public void showForeignTruck() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			foreigntruckIframe.load(blankUrl);
		} else {
			foreigntruckIframe.load("../bus/foreignbustruck.xhtml?id=" + this.mPkVal);
		}
	}

	/**
	 * 清关
	 */
	@Action
	public void showForeignCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			foreigncustomerIframe.load(blankUrl);
		} else {
			foreigncustomerIframe.load("../bus/foreignbuscustoms.xhtml?id=" + this.mPkVal);
		}
	}
	
	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		String corpidv = AppUtils.getReqParam("corpidv");
		Long salesid=null;
		Long deptid;
		String salesname;
		String tradeway;
		StringBuilder querySql = new StringBuilder();
		querySql.append("\n SELECT a.id AS salesid,a.namec AS salesname,deptid,corpid FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE ");
		querySql.append("\n  AND CASE WHEN EXISTS(SELECT a.id AS salesid,a.namec AS salesname FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE AND EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C' ");
		querySql.append("\n  AND EXISTS(SELECT * FROM ");
		querySql.append("\n  (SELECT xx.userid ");
		querySql.append("\n  FROM sys_custlib xx , sys_custlib_user y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND y.userid ="+AppUtils.getUserSession().getUserid()+" AND xx.libtype = 'S' AND xx.userid IS NOT NULL");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z where z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  UNION ");
		querySql.append("\n  SELECT xx.userid");
		querySql.append("\n  FROM sys_custlib xx, sys_custlib_role y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) AND xx.libtype = 'S'");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z WHERE z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  ) AS T");
		querySql.append("\n  WHERE T.userid = x.userid ");
		querySql.append("\n  ))) THEN EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C' ");
		querySql.append("\n  AND EXISTS(");
		querySql.append("\n  SELECT * FROM ");
		querySql.append("\n  (SELECT xx.userid ");
		querySql.append("\n  FROM sys_custlib xx , sys_custlib_user y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND y.userid ="+AppUtils.getUserSession().getUserid()+" AND xx.libtype = 'S' AND xx.userid IS NOT NULL");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z where z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n UNION ");
		querySql.append("\n  SELECT xx.userid");
		querySql.append("\n  FROM sys_custlib xx, sys_custlib_role y ");
		querySql.append("\n  WHERE y.custlibid = xx.id");
		querySql.append("\n  AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) AND xx.libtype = 'S'");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z WHERE z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  ) AS T");
		querySql.append("\n  WHERE T.userid = x.userid ");
		querySql.append("\n  ))");
		querySql.append("\n ELSE (EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C')");
		querySql.append("\n OR EXISTS(SELECT x.salesid FROM sys_corporation x WHERE x.id = "+customerid+" AND x.salesid = a.id))   END");
		querySql.append("\n AND a.corpid = "+corpidv);
		querySql.append("\n LIMIT 1");
		
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
			String salesidStr = StrUtils.getMapVal(m, "salesid");
			salesid = StrUtils.isNull(salesidStr)?0l:Long.valueOf(salesidStr);
			//2025 委托人选择后，提取业务员后，按业务员对应的分公司，修改上面的接单公司
			try {
				if(!StrUtils.isNull(salesidStr)){
					SysUser sysuer = serviceContext.userMgrService.sysUserDao.findById(salesid);
					if(sysuer!=null&&sysuer.getSysCorporation()!=null){
						this.selectedRowData.setCorpid(sysuer.getSysCorporation().getId());
						//this.selectedRowData.setCorpidop(sysuer.getSysCorporation().getId());
						update.markUpdate(true, UpdateLevel.Data, "corpid");
						//update.markUpdate(true, UpdateLevel.Data, "corpidop");
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
			deptid = StrUtils.isNull(StrUtils.getMapVal(m, "deptid"))?0l:Long.valueOf(StrUtils.getMapVal(m, "deptid"));
			salesname = StrUtils.getMapVal(m, "salesname");
			this.selectedRowData.setSaleid(salesid);
			this.selectedRowData.setSales(salesname);
			this.selectedRowData.setDeptid(deptid);
			String corpid = StrUtils.getMapVal(m, "corpid");
			this.salescorpid =  StrUtils.isNull(corpid)?0l:Long.valueOf(corpid);
//			this.deptids = getqueryDepartid();
			deptname = serviceContext.userMgrService.getDepartNameByUserId(salesid);
			update.markUpdate(true, UpdateLevel.Data, "deptname");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("initSalesByCustomer('"+salesname+"');");
			
			SysCorporation sysCorporation  = serviceContext.customerMgrService.sysCorporationDao.findById(Long.valueOf(customerid));
			this.selectedRowData.setContact(sysCorporation.getContact());
			this.selectedRowData.setTel(sysCorporation.getTel1());
			update.markUpdate(true, UpdateLevel.Data, "contact");
			update.markUpdate(true, UpdateLevel.Data, "tel");
			
		} catch (NoRowException e) {
			this.selectedRowData.setSaleid(0l);
			this.selectedRowData.setSales("");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("initSalesByCustomer('');");
		} catch (Exception e) {
			e.printStackTrace();
		}
		update.markUpdate(true, UpdateLevel.Data, "cusales");
		this.selectedRowData.setCustomerid(Long.decode(customerid));
		arrears(customerid);
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(salesid);
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
	}
	/*
	 * 如果该客户欠款在欠账额度之内，不提示，返回空,提示：客户信息(简称，中英文名，欠账额度，当前欠款)
	 * 超过时是否允许接单：不允许：增加提示不允许，并将当前单的界面上id和名称清空，如果允许就不处理只提示
	 * */
	public void arrears(String customerid){
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("customerid",customerid);
		String urlArgs2 = AppUtils.map2Url(argsMap, ";");
		try {

			Long loguserid = AppUtils.getUserSession().getUserid();
			//前海国际事业部
			Map deptmap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select deptid FROM SYS_USER WHERE isdelete = FALSE AND id = "+loguserid);
			if(AppUtils.getUserSession().getCorpidCurrent() != 13632274 && !"1464156732274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565620302274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565643442274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565621682274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565614052274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565616572274".equals(String.valueOf(deptmap.get("deptid")))){
				return;
			}
			String sqlQry = "SELECT * FROM f_sys_corporation_checkamtowe('"+urlArgs2+"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			String str = m.get("f_sys_corporation_checkamtowe").toString();
			if(str.equals("1")){
			}else {
				SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(Long.valueOf(customerid));
				if(sysCorporation.getIsalloworder()){
					MessageUtils.alert(""+str);
				}
				else{
					MessageUtils.alert(str);
					Browser.execClientScript("customeridJsVar.setValue('')");
					Browser.execClientScript("customernameJsVar.setValue('')");
					Browser.execClientScript("$('#customer_input').val('')");
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow setnosWindow;
	
	@Action
	public void setJobNos(){
		//if(this.mPkVal == null || this.mPkVal <= 0){
		//	this.alert("请先保存");
		//	return;
		//}
		this.grid.reload();
		setnosWindow.show();
	}
	
	@Action
	public void getJobNos() {
		if(this.selectedRowData!=null&&this.selectedRowData.getNos()!=null&&!this.selectedRowData.getNos().isEmpty()){
			MessageUtils.alert("工作号已生成!");
			return;
		}
		String querySql ;
		if(this.mPkVal > 0){
			//querySql = "SELECT f_find_jobs_getnos('id="+this.mPkVal+"') AS nos;";
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+"') AS nos;";
		}else{
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+";corpid="+selectedRowData.getCorpid()+";jobdate="+selectedRowData.getJobdate()+"') AS nos;";
		}
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String nos = StrUtils.getMapVal(m, "nos");
			this.selectedRowData.setNos(nos);
			MessageUtils.alert(nos);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SaveState
	public String codes;
	
	@Action
	public void choosenos(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String querySql  = "SELECT code  FROM sys_busnodesc WHERE id = "+ids[0];
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			codes = StrUtils.getMapVal(m, "code");
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		getJobNos();
		setnosWindow.close();
	}
	
	@Override
	public void grid_ondblclick() {
		choosenos();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND r.corpid =" + this.selectedRowData.getCorpid();
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	@SaveState
	@Accessible
	public Long salescorpid = null;
	
	private List<SelectItem> getqueryDepartid(){
		try {
			List<SelectItem> list = null;
			if(salescorpid == null){
				Long id = this.selectedRowData != null && this.selectedRowData.getCorpid() != null ? this.selectedRowData.getCorpid() : -1L;
				 list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid ="+id, "ORDER BY d.name");
			}else{
				 list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid ="+salescorpid, "ORDER BY d.name");
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Action
	public void aorderno(){
		String url = AppUtils.chaosUrlArs("../order/busorderair.xhtml") + "&type=edit&id=" + this.selectedRowData.getOrderid();
		AppUtils.openWindow("_showJobno_" + this.selectedRowData.getOrderid(),url);
	}
	
	@Action
	private void changeCorpid(){
		String corptmp = AppUtils.getReqParam("corpid");
		if(corptmp != null && !corptmp.isEmpty()){
			if(!this.selectedRowData.getCorpid().equals(corptmp)){
				this.selectedRowData.setDeptid(null);
				this.selectedRowData.setCorpid(Long.parseLong(corptmp));
//				this.deptids = getqueryDepartid();
				//update.markUpdate(true,UpdateLevel.Data, "deptcomb");
				//Browser.execClientScript("clearDept();");
				//update.markUpdate(true,UpdateLevel.Data, "masterEditPanel");
			}
		}
	}
	@Action
	public void salesChangeAjaxSubmit() {
		String salesid = AppUtils.getReqParam("salesid");
		String sql  = "SELECT deptid,corpid FROM sys_user WHERE id = " + salesid;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String deptid = StrUtils.getMapVal(m, "deptid");
			String cid = StrUtils.getMapVal(m, "corpid");
			if(!StrUtils.isNull(deptid)){
				this.selectedRowData.setDeptid(StrUtils.isNull(deptid)?0l:Long.valueOf(deptid));
			}
			if(!StrUtils.isNull(cid)){
				this.salescorpid = Long.valueOf(cid);
				this.selectedRowData.setCorpid(StrUtils.isNull(cid)?0l:Long.valueOf(cid));
			}
			update.markUpdate(true, UpdateLevel.Data, "corpid");
			//update.markUpdate(true, UpdateLevel.Data, "deptcomb");
//			this.deptids = getqueryDepartid();
		} catch (NoRowException e) {
			this.selectedRowData.setDeptid(0l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(Long.parseLong(salesid));
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
	}
	
	@Action
	public void pageUp(){
		Map map = getPageNumber(this.mPkVal, 0);
		if(map !=null && map.containsKey("row")){
			int row = Integer.parseInt(map.get("row").toString());
			row-=1;
			if(row>0){
				Map result = getPageNumber(0L, row);
				if(result !=null && result.containsKey("id")){
					Long jobid = Long.parseLong(result.get("id").toString());
					this.jobnos = "";
					this.mPkVal = jobid;
					this.update.markUpdate(UpdateLevel.Data, "jobnos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					refreshMasterForm();
				}
			}else{
				MessageUtils.alert("已经是第一条!");
			}
		}
	}
	
	@Action
	public void pageDown(){
		Map map = getPageNumber(this.mPkVal, 0);
		if(map !=null && map.containsKey("row")){
			int row = Integer.parseInt(map.get("row").toString());
			row+=1;
			Map result = getPageNumber(0L, row);
			if(result !=null && result.containsKey("id")){
				Long jobid = Long.parseLong(result.get("id").toString());
				this.jobnos = "";
				this.mPkVal = jobid;
				this.update.markUpdate(UpdateLevel.Data, "jobnos");
				this.update.markUpdate(UpdateLevel.Data, "mPkVal");
				refreshMasterForm();
			}else{
				MessageUtils.alert("已经是最后一条!");
			}
		}
	}
	
	/**
	 * 获取工作单列表结果集序号(过滤条件同工作单列表)
	 * @param jobid 传入jobid可获取所在结果集序号(不建议同时传入2个变量,为空传入0)
	 * @param pageNumber 传入序号可获取对应jobid
	 * @return 返回null为过界 Map {id,row}
	 */
	public Map getPageNumber(Long jobid,int pageNumber){
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" 
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
									"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpid()+")"+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) "
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("\n SELECT row,id FROM ");
		sbsql.append("\n (SELECT row_number() OVER() AS row,id FROM");
		sbsql.append("\n (SELECT id FROM _fina_jobs_air t");
		sbsql.append("\n WHERE isdelete = FALSE");
		sbsql.append("\n AND jobtype = 'A' ");
		sbsql.append(sql);
		sbsql.append(corpfilter);
		
		String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_air_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			sbsql.append("\n AND t.jobdate >= NOW()::DATE-60");
		}else{
			sbsql.append("\n AND t.jobdate >= NOW()::DATE-"+Integer.parseInt(findUserCfgVal));
		}
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		String seach = ConfigUtils.findUserSysCfgVal("pages.module.airtransport.jobsBean.search", AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(seach)){
			sbsql.append(seach);
		}
		
		String ordersql = AppUtils.getUserColorder("pages.module.airtransport.jobsBean.grid");
		if(!StrUtils.isNull(ordersql)){
			ordersql = " ORDER BY " + ordersql;
			sbsql.append(ordersql);
		}
		sbsql.append("\n ) AS T ) AS T2");
		sbsql.append("\n WHERE ");
		if(jobid > 0){
			sbsql.append("\n T2.id ="+jobid);//查jobid对应的序列号
		}
		if(pageNumber > 0){
			sbsql.append("\n T2.row ="+pageNumber);//查序列号对应的jobid
		}
		Map map = null;
		try {
			map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
		} catch (NoRowException e) {
			//返回null为过界
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		return map;
	}
	
	
	@SaveState
	LinkedList<Map> list = new LinkedList<Map>();
	
	//初始化模块
	public void initialization(){
		list.clear();
		Map<String,String> m = new HashMap<String, String>();
		m.put("key", "airFinaEntrust");
		list.add(m);
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("key", "airFinaCost");
		list.add(m1);
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("key", "airFinabill");
		list.add(m2);
		Map<String,String> m3 = new HashMap<String, String>();
		m3.put("key", "airFinainvoice");
		list.add(m3);
		Map<String,String> m4 = new HashMap<String, String>();
		m4.put("key", "airFinaEnclosure");
		list.add(m4);
		Map<String,String> m5 = new HashMap<String, String>();
		m5.put("key", "airFinaFill");
		list.add(m5);
		Map<String,String> m6 = new HashMap<String, String>();
		m6.put("key", "airFinaTrack");
		Map<String,String> m7 = new HashMap<String, String>();
		m7.put("key", "airformdefine");
		list.add(m7);
	}	
	
	/*
	 * 控制模块权限
	 * */
	@Action
	public void airSubmodule(){
		List<Map> li = serviceContext.jobsMgrService.findAirModinroleMy();
		for(int i = 0;i<list.size();i++){//遍历找出角标位置
			boolean isremove = true;
			String value = list.get(i).get("key").toString();
			for(int j = 0;j<li.size();j++){
				if(value.equals(li.get(j).get("code"))){
					isremove = false;
				}
			}
			if(isremove){
				this.tabLayout.removeTab(value);
			}
		}
		for(int i = 0;i<list.size();i++){//遍历找出角标位置
			boolean isremove = true;
			String value = list.get(i).get("key").toString();
			for(int j = 0;j<li.size();j++){
				if(value.equals(li.get(j).get("code"))){
					isremove = false;
				}
			}
			if(isremove){
				list.remove(i);
			}
		}
	}
	
	@Action
	public void saveRemark(){
		saveMaster();
		Browser.execClientScript("remarkWindow.hide();");
	}
	
	
	@Action
	public void synchroChild(){
		try{
			String checksql = "SELECT f_bus_air_sync_child('jobid="+this.mPkVal+"')";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(checksql);
			Browser.execClientScript("showmsg()");
		}catch(Exception e){
			MessageUtils.showException(e);
			e.printStackTrace();
		}
		
		
	}
	
	@Action
	public void refreshAjaxSubmit(){
		this.refresh();
		refreshMasterForm();
	}
	
	
	
	@Bind
	private UIWindow showCopyDataWindow;
	
	@Bind
	@SaveState
	public boolean isentrust = false;
	
	@Bind
	@SaveState
	public boolean isbookings = false;
	
	@Bind
	@SaveState
	public boolean ismbls = false;
	
	@Bind
	@SaveState
	public boolean transceiver = false;
	
	@Bind
	@SaveState
	public boolean iscabinet = false;
	
	@Bind
	@SaveState
	public String copyNo;
	
	@Bind
	@SaveState
	public String selectType = "J";
	
	@Action
	public void copyData() {
		if(!isentrust&&!iscabinet&&!isbookings&&!ismbls&&!transceiver){
			MessageUtils.alert("请至少选择一项复制内容");
			return;
		}
		String isentrusts = "";
		String iscabinets = "";
		String isbooking = "";
		String ismbl = "";
		String transceivers = "";
		if(isentrust){
			isentrusts = "t";
		}
		if(iscabinet){
			iscabinets = "t";
		}
		if(isbookings){
			isbooking = "t";
		}
		if(ismbls){
			ismbl = "t";
		}
		if(transceiver){
			transceivers = "t";
		}
		String querySql  = "SELECT f_fina_jobs_copy2thisair('nos="+copyNo+";user="+AppUtils.getUserSession().getUsername()+";jobid="+this.mPkVal.toString()+";isentrusts="+isentrusts+";iscabinets="+iscabinets+";isbooking="+isbooking+";selectType="+selectType+";ismbl="+ismbl+";transceiver="+transceivers+"') AS nos;";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			Browser.execClientScript("showmsg()");
			showCopyDataWindow.close();
			this.refreshMasterForm();
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	@Bind
	public UIIFrame knowledgeBaseIframe;
	
	@Action
	public void showKnowledgeBase() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			knowledgeBaseIframe.load(blankUrl);
		} else {
			knowledgeBaseIframe.load("../../sysmgr/knowledge/knowledgeBase.jsp?id="
					+ this.mPkVal+"&language="+AppUtils.getUserSession().getMlType()+"&src=A");
		}
	}
	
	
	/**
	 * 20180810 动态显示按钮上面数据个数，比如留言数量，知识库数量
	 * Neo 
	 */
	@Action
	public void showTipsCount(){
		if(this.mPkVal > 0){
			String messageTips = this.serviceContext.jobsMgrService.findTipsCount(this.mPkVal , "sys_msgboard");
			if(!StrUtils.isNull(messageTips)){
				Browser.execClientScript("showButtonTips('showMsgWin',"+messageTips+")");
			}
			messageTips = this.serviceContext.jobsMgrService.findTipsCount(this.mPkVal , "sys_knowledgelib");
			if(!StrUtils.isNull(messageTips)){
				Browser.execClientScript("showButtonTips('showKnowledgeBase',"+messageTips+")");
			}
		}
	}
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Action
	public void applyBPMform() {
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-6F5F0C51";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+this.selectedRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str =  s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
				}
				Browser.execClientScript("bpmWindowJsVar.show();");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	@SaveState
	public String bpmremark = "";
	
	@Bind
	@SaveState
	public String taskname;
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-6F5F0C51";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
				lists.add(selectItem);
			}
		}catch(NoRowException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
	@Action
	public void applyBPM() {
		if(this.mPkVal==null||!(mPkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-6F5F0C51";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+bpmremark+";taskname="+taskname+";refno="+this.selectedRowData.getNos()+";refid="+this.selectedRowData.getId()+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
				String sub =  sm.get("rets").toString();
				MessageUtils.alert("OK");
				Browser.execClientScript("bpmWindowJsVar.hide();");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser.contains(id)){
				this.nextAssignUser = nextAssignUser + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	@Bind
	public UIIFrame traceIframe2;
	
	@Action
	public void showBpm(){
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+this.mPkVal+"' LIMIT 1";
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
		}
		taskCommentsIframe.load("../../../bpm/bpmshowcomments.jsp?processinstanceid="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		traceIframe2.load("../../../bpm/model/trace.html?language="+AppUtils.getUserSession().getMlType()
				+"&id="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show();traceWindow.show()");
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
}
