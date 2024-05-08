package com.scp.view.module.ship;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.model.data.DatPackage;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipBill;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.EncryptUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.api.robot.ApiEncryptUtil;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;

@ManagedBean(name = "pages.module.ship.busshipbilleditBean", scope = ManagedBeanScope.REQUEST)
public class BusShipBilleditBean extends MastDtlView {

	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;

	@SaveState
	@Accessible
	public BusShipBill selectedRowData = new BusShipBill();

	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();

	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();

	@SaveState
	@Accessible
	public BusShipping shipping = new BusShipping();
	
	@SaveState
	@Accessible
	public Boolean query = true;

	@Bind
	@SaveState
	@Accessible
	public String billfilename = "HBL(GSIT).raq";
	
	@Bind
	@SaveState
	@Accessible
	public Long jobid;

	@SaveState
	@Accessible
	public Long customerid;

	@SaveState
	@Accessible
	public String sql = "AND 1=1";

	@SaveState
	@Accessible
	public String portsql = "AND 1=1";

	@SaveState
	@Accessible
	public String custype;

	@SaveState
	@Accessible
	public String bltype;

	@SaveState
	@Accessible
	public String porttype;
	
	@Bind
	@SaveState
	@Accessible
	public UIWindow showDtlWindow;
	
	@Bind
	@SaveState
	@Accessible
	public String requesturl;
	
	@Bind
	@SaveState
	@Accessible
	public String dtlContent;
	
	@Bind
	@SaveState
	@Accessible
	public UICombo cotrcarryitem;

	@SaveState
	@Accessible
	public FinaJobs job = new FinaJobs();

	@SaveState
	public String workItemId;

	@Bind
	@SaveState
	public String billtype;
	
	@Bind
	@SaveState
	public String baseurl;
	
	@Bind
	@SaveState
	public String userid;
	
	@Bind
	public char ishuazhan = '0';
	
	@Bind
	@SaveState
	public String hblattrows;
	
	@Bind
	public UIWindow showRemarksWindow;
	
	@Bind
	public String putstatus;

	@Bind
	@SaveState
	public String printingcode = "";
	
	@Override
	public void refreshForm() {
		if(this.selectedRowData == null)return;
		if (this.selectedRowData.getIsprintlock()) {
			disableAllButton(true);
		}else{
			disableAllButton(false);
		}
	}

	@Override
	public void refresh() {
		this.grid.reload();
		refreshForm();
	}

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			initCtrl();
			workItemId = (String) AppUtils.getReqParam("workItemId");
			List<BusShipping> bus = null;
			jobid = StrUtils.isNull(AppUtils.getReqParam("jobid"))?getJobidByid():Long.valueOf(AppUtils.getReqParam("jobid"));
			try {
				bus = this.serviceContext.busShippingMgrService.busShippingDao.findAllByClauseWhere("jobid = "+jobid);
				shipping = bus.get(0);
				putstatus = shipping.getPutstatus();
			} catch (Exception e) {
				// TODO: handle exception
			}
			billfilename = shipping.getHblrpt();
			hblattrows = ConfigUtils.findSysCfgVal("hbl_att_rows");
			qryMap.put("jobid$", this.jobid);
			//this.userid = AppUtils.base64Encoder(AppUtils.getUserSession().getUserid().toString());
			this.userid = AppUtils.getUserSession().getUserid().toString();


		}
	}
	
	/**
	 *	根据id查询jobid
	 * @return
	 */
	private Long getJobidByid() {
		BusShipBill busshipbill = null;
		Long id = Long.parseLong(AppUtils.getReqParam("id"));
		if(null == id){
			return 0L;
		}
		try {
			busshipbill = serviceContext.busShipBillMgrService.busShipBillDao.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null == busshipbill.getJobid() ? 0L : busshipbill.getJobid();
	}

	private void initCtrl() {
		addMaster.setDisabled(true);
		saveMaster.setDisabled(true);
		delMaster.setDisabled(true);
//		scanReport.setDisabled(true);
//		isprintlock.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_ship_bill.getValue())) {
			if (s.endsWith("_add")) {
				addMaster.setDisabled(false);
				saveMaster.setDisabled(false);
			} else if (s.endsWith("_update")) {
				saveMaster.setDisabled(false);
			}else if (s.endsWith("_report")) {
//				scanReport.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}else if (s.endsWith("_unlock")) {
//				isprintlock.setDisabled(false);
			}
		}
		if (this.selectedRowData.getIsprintlock()) {
			disableAllButton(true);
		}
	}

	@Override
	public void init() {
		selectedRowData = new BusShipBill();
		String id = AppUtils.getReqParam("id");
		String bltype = AppUtils.getReqParam("bltype").trim();
		this.bltype = bltype;
		this.jobid = StrUtils.isNull(AppUtils.getReqParam("jobid"))?getJobidByid():Long.valueOf(AppUtils.getReqParam("jobid"));
		this.job = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.jobid);
		this.customerid = job.getCustomerid();
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			this.bltype = this.selectedRowData.getBltype();
			if (this.selectedRowData.getIsprintlock()) {
				disableAllButton(true);
			}else{
				disableAllButton(false);
			}

		} else {
			addMaster();
			
		}
//		if(this.bltype !=null && !"".equals(this.bltype)){
//			this.setDisable();
//		}
		try {
			baseurl = ConfigUtils.findSysCfgVal(ConfigUtils.SysCfgKey.rpt_srv_url.name()); 
		} catch (Exception e) {
		}
		String message = this.ShowMessage();
		if (message != null) {
			showDtlWindow.show();
			this.dtlContent = message;
			this.update.markUpdate(UpdateLevel.Data, "dtlContent");
		}
		//查找该用户是不是华展宁波分公司的
		String sql = "SELECT f_sys_getcsno() = '2199' AND EXISTS(SELECT 1 FROM sys_user x WHERE id = "+AppUtils.getUserSession().getUserid() +
				"AND EXISTS(SELECT 1 FROM sys_corporation WHERE code = 'NBFGS' AND isdelete = FALSE AND id = x.corpid)) AS isnb";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("isnb") != null&& m.get("isnb").toString().equals("true")) {
				ishuazhan = '1';
			}
		} catch (Exception e) {
			ishuazhan = '0';
		}
	}

	@Override
	public void addMaster() {
		this.selectedRowData = new BusShipBill();
		
		this.shipping = serviceContext.busShippingMgrService.busShippingDao
				.findById(getshipid(this.jobid));
		
		selectedRowData.setJobid(this.jobid);
		selectedRowData.setBltype(this.bltype);
		selectedRowData.setShipid(getshipid(this.jobid));
		selectedRowData.setBillcount("THREE(3)");
		selectedRowData.setPodid(this.shipping.getPolid());
		selectedRowData.setPod(this.shipping.getPod());
		selectedRowData.setPol(this.shipping.getPol());
		selectedRowData.setPolid(this.shipping.getPolid());
		selectedRowData.setPdd(this.shipping.getPdd());
		selectedRowData.setPddid(this.shipping.getPddid());
		selectedRowData.setCarryitem("CY-CY");
		selectedRowData.setVessel(this.shipping.getVessel());
		selectedRowData.setVoyage(this.shipping.getVoyage());
		selectedRowData.setCarrierid(this.shipping.getCarrierid());
		selectedRowData.setFreightitem(this.shipping.getFreightitem());
		selectedRowData.setPoa(this.shipping.getPoa());
		selectedRowData.setDestination(this.shipping.getDestination());
		selectedRowData.setPretrans(this.shipping.getPretrans());
		selectedRowData.setPayplace(this.shipping.getPayplace());
		selectedRowData.setHblno(this.shipping.getHblno());
		selectedRowData.setPolcode(this.shipping.getPolcode());
		selectedRowData.setPddcode(this.shipping.getPddcode());
		selectedRowData.setPodcode(this.shipping.getPodcode());
		selectedRowData.setDestinationcode(this.shipping.getDestinationcode());
		if(this.shipping.getPacker()!=null&&!this.shipping.getPacker().equals("")){
			List<DatPackage> datPackage = serviceContext.packageMgrService.datPackageDao.findAllByClauseWhere("namee = '"+this.shipping.getPacker()+"' AND isdelete = FALSE");
			if (datPackage!=null && datPackage.size() > 0){
				selectedRowData.setPackid(datPackage.get(0).getId());
			}
		}
			// if(StrTools.isNull(this.shipping.getCarryitem())) {
		// String sql =
		// "SELECT EXISTS(SELECT 1 FROM bus_ship_container WHERE shipid = " +
		// this.shipping.getId() +
		// " AND ldtype = 'L' AND isdelete = FALSE) AS islcl;";
		// Map m =
		// this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		// Boolean islcl = Boolean.valueOf(m.get("islcl").toString());
		// if(islcl) {
		// selectedRowData.setCarryitem("CFS-CFS");
		// }
		// } else {
		// selectedRowData.setCarryitem(this.shipping.getCarryitem());
		// }



		if ("H".equals(bltype)) {
			selectedRowData.setCnorid(this.shipping.getCnorid());
			selectedRowData.setCnorname(this.shipping.getCnorname());
			selectedRowData.setCnortitle(this.shipping.getCnortitle());

			selectedRowData.setCneeid(this.shipping.getCneeid());
			selectedRowData.setCneename(this.shipping.getCneename());
			selectedRowData.setCneetitle(this.shipping.getCneetitle());

			selectedRowData.setNotifyid(this.shipping.getNotifyid());
			selectedRowData.setNotifyname(this.shipping.getNotifyname());
			selectedRowData.setNotifytitle(this.shipping.getNotifytitle());
		} else if ("M".equals(bltype)) {
			selectedRowData.setCnorid(this.shipping.getCnortitlemblid());
			selectedRowData.setCnorname(this.shipping.getCnortitlemblname());
			selectedRowData.setCnortitle(this.shipping.getCnortitlembl());

			selectedRowData.setCneeid(this.shipping.getCneetitlembid());
			selectedRowData.setCneename(this.shipping.getCneetitlemblname());
			selectedRowData.setCneetitle(this.shipping.getCneetitlembl());

			selectedRowData.setNotifyid(this.shipping.getNotifytitlemblid());
			selectedRowData.setNotifyname(this.shipping.getNotifytitlemblname());
			selectedRowData.setNotifytitle(this.shipping.getNotifytitlembl());
		}



		selectedRowData.setAgenid(this.shipping.getAgenid());
		selectedRowData.setAgenname(this.shipping.getAgenname());
		selectedRowData.setAgentitle(this.shipping.getAgentitle());
		selectedRowData.setHbltype(this.shipping.getHbltype());
		selectedRowData.setMbltype(this.shipping.getMbltype());
		selectedRowData.setAtd(this.shipping.getAtd());
		selectedRowData.setCarryitem(this.shipping.getCarryitem());
		selectedRowData.setFreightitem(this.shipping.getFreightitem());
		selectedRowData.setPaymentitem(this.shipping.getPaymentitem());
		selectedRowData.setPiece(this.shipping.getPiece());
		selectedRowData.setSignplace(this.shipping.getSignplace());
		
//		selectedRowData.setNotifytitle("SAME AS CONSIGNEE");
		selectedRowData.setIsshowship(false);
		if ("H".equals(this.bltype)) {
			// this.selectedRowData.setFreightitem("PP");
			//selectedRowData.setHblno(this.shipping.getNos() + getHblno());
			//selectedRowData .setAgentitle("");
			String sql = "SELECT chr(ascii(SUBSTRING(reverse(hblno) FROM 0 FOR 2))+1) FROM bus_ship_bill WHERE jobid = "+this.jobid+" AND bltype = 'H' AND isdelete = FALSE ORDER BY reverse(hblno) DESC LIMIT 1";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map!=null&& map.containsKey("chr")&&map.get("chr")!=null){
					String chr = map.get("chr").toString();
					selectedRowData.setHblno(this.shipping.getHblno()+chr);
				}else{
					selectedRowData.setHblno(this.shipping.getHblno()+"A");
				}
			} catch (Exception e) {
				selectedRowData.setHblno(this.shipping.getHblno()+"A");
			}
			selectedRowData.setMblno(this.shipping.getMblno());
		} else if ("M".equals(this.bltype)) {
			String sql = "SELECT chr(ascii(SUBSTRING(reverse(mblno) FROM 0 FOR 2))+1) FROM bus_ship_bill WHERE jobid = "+this.jobid+" AND bltype = 'M' AND isdelete = FALSE ORDER BY reverse(mblno) DESC LIMIT 1";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map!=null&& map.containsKey("chr")&&map.get("chr")!=null){
					String chr = map.get("chr").toString();
					selectedRowData.setMblno(getMblno()+chr);
				}else{
					selectedRowData.setMblno(getMblno()+"A");
				}
			} catch (Exception e) {
				selectedRowData.setMblno(getMblno()+"A");
			}
			selectedRowData.setHblno(this.shipping.getHblno());
			//selectedRowData.setAgentitle("");
		}

		// selectedRowData.setCarryitem(this.shipping.getCarryitem());

//		selectedRowData.setCneetitle("TO ORDER");

		this.mPkVal = -1l;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		disableAllButton(false);
		refresh();
		
		Browser.execClientScript("changeHblType()");
	}

	/**
	 * 根据委托单中的数量确定提单号码
	 * 
	 * @return
	 */
	private String getHblno() {
		String sql = "SELECT COUNT(*)AS count FROM bus_ship_bill WHERE isdelete = FALSE AND shipid= "
				+ getshipid(this.jobid);
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		Long count = ((Long) m.get("count"));

		if (count == 0) {
			return "";
		} else {
			return String.valueOf((char) (64 + count));
		}

	}

	/**
	 * 取柜子中订舱号最小的作为MBLNO
	 * 
	 * @return
	 */
	private String getMblno() {
		String sql = "SELECT MIN(sono) AS sono FROM bus_ship_container WHERE isdelete = FALSE AND shipid= "
				+ getshipid(this.jobid);
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		String sono = String.valueOf(m.get("sono"));
		if ("null".equals(sono) || "".equals(sono)) {
			//MessageUtils.alert("尚未订舱!");
			return "";
		}
		return sono;
	}

	@Override
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			try {
				serviceContext.busShipBillMgrService.removeDate(selectedRowData
						.getId());
				this.addMaster();
				refreshMasterForm();
				this.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Override
	public void doServiceSaveMaster() {
//		if (selectedRowData.getPackid() == null) {
//			MessageUtils.alert("请选择包装");
//			return;
//		}
		if (StrUtils.isNull(selectedRowData.getFreightitem())) {
			MessageUtils.alert("请选择运费条款");
			return;
		}

		// //存在流程,同时该提单类型是HBL ,同时该流程正好是做提单这一环节,那么 运费默认是cc ,如果不是cc,那么需要申请
		// if(!StrTools.isNull(this.workItemId)&&this.selectedRowData.getBltype().equals("H")&&!this.selectedRowData.getFreightitem().equals("CC")){
		// IWorkflowSession workflowSession2 = AppUtil.getWorkflowSession();
		// IWorkItem workItem2 = workflowSession2.findWorkItemById(workItemId);
		// String activityid2 =workItem2.getTaskInstance().getActivityId();
		// if((workItem2.getTaskInstance().getProcessId()+"."+
		// WorkFlowEnumerateShip.SHIPBILL).equals(activityid2)){
		// MsgUtil.alert("HBL默认为CC,如需修改成PP,请点击按钮[申请PP],等待上级审批");
		// return ;
		// }
		//			
		// }
		try {
			selectedRowData.setCnorname(AppUtils.replaceStringByRegEx(selectedRowData.getCnorname()));
			selectedRowData.setCnortitle(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitle()));
			selectedRowData.setCneename(AppUtils.replaceStringByRegEx(selectedRowData.getCneename()));
			selectedRowData.setCneetitle(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitle()));
			selectedRowData.setNotifyname(AppUtils.replaceStringByRegEx(selectedRowData.getNotifyname()));
			selectedRowData.setNotifytitle(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitle()));
			selectedRowData.setAgenname(AppUtils.replaceStringByRegEx(selectedRowData.getAgenname()));
			selectedRowData.setAgentitle(AppUtils.replaceStringByRegEx(selectedRowData.getAgentitle()));
			serviceContext.busShipBillMgrService.saveData(selectedRowData);
			this.mPkVal = this.selectedRowData.getId();



			//记录印刷编号
			if (!StrUtils.isNull(printingcode)) {
				String printcodesql = "select * from bus_ship_bill_reg where iscancel=false  and printingcode ='" + printingcode + "' order by id desc limit 1 ";
				List printcodesqlList = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(printcodesql);
				if (printcodesqlList == null || printcodesqlList.size() == 0) {
					String printcodeinsertsql = "insert into bus_ship_bill_reg (id, billno, billlading, printingcode, agentname,inputer,inputtime, iscancel)\n" +
							"values (getid()" +
							",'" + selectedRowData.getHblno() + "'" +
							",'" + selectedRowData.getHbltype() + "'" +
							",'" + printingcode + "'" +
							",'" + (!StrUtils.isNull(selectedRowData.getAgentitle()) ? selectedRowData.getAgentitle().split("\n")[0] : "") + "'" +
							",'" + AppUtils.getUserSession().getUsercode() + "'" +
							",now()" +
							",false" +
							");";
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql(printcodeinsertsql);
				}
			}
			refreshMasterForm();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

	}

	@Override
	public void refreshMasterForm() {

		this.selectedRowData = serviceContext.busShipBillMgrService.busShipBillDao
				.findById(this.mPkVal);
		
		if(this.selectedRowData == null)return;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
		if (this.selectedRowData.getIsprintlock()) {
			disableAllButton(true);
		} else {
			disableAllButton(false);
		}
		initCtrl();


		//印刷编号
		if (!StrUtils.isNull(selectedRowData.getHblno())) {
			String printcodesql = "select * from bus_ship_bill_reg where iscancel=false and billno = '"+selectedRowData.getHblno()+ "' order by id desc limit 1 ";
			List<Map> printcodesqlList = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(printcodesql);
			if (printcodesqlList != null && printcodesqlList.size() == 1) {
				printingcode = String.valueOf(printcodesqlList.get(0).get("printingcode")).replace("null", "");
			} else if (printcodesqlList == null || printcodesqlList.size() == 0) {
				printingcode = "";
			}
		}
	}
	public void refreshMasterForm2() {
		this.selectedRowData = serviceContext.busShipBillMgrService.busShipBillDao
				.findById(this.mPkVal);

		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		// this.grid.repaint();
		// this.grid.reload();
		// this.grid.rebind();
		// this.grid.reload();
		// this.grid.rebind();
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		// this.grid.rebind();
		this.grid.reload();
		this.grid.setSelections(getGridSelIds());
		if (this.selectedRowData.getIsprintlock()) {
			disableAllButton(true);
		}else{
			disableAllButton(false);
		}
		// Browser.execClientScript("window.location.reload();");
	}

	@Override
	public void add() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		super.add();
	}

	@Override
	public void del() {
		try {
			serviceContext.busShipContainerMgrService.removeDate(this.grid.getSelectedIds());
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busShipContainerMgrService.saveData(dtlData);
		alert("OK");
	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind
	private String qryCustomerKey;
	
	
	@Action
	public void setDetail(){
		chooseship();
	}
	/**
	 * 
	 */
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider(sql);
	}

	@Action
	public void showCustomer() {
		String customercode = (String) AppUtils.getReqParam("customercode");
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);
		//1947 另外这里弹窗点开的时候，之前是把小框里面的内容自动带到查询输入框的，这个拿掉，不然还要手动清掉内容再查询
		qryCustomerKey = "";
		String type = (String) AppUtils.getReqParam("type");

		custype = (String) AppUtils.getReqParam("custype");

		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			customerQry();
			// 收货人
			if ("0".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'C' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("cneename.focus");

				// 发货人
			} else if ("1".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'S' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (ishipper = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("cnorname.focus");
				// 通知人
			} else if ("2".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'N' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("notifyname.focus");
				// hbl代理
			} else if ("3".equals(custype)) {
				sql = " AND isagentdes = true";
				Browser.execClientScript("agenname.focus");

			}

			return;
		}

		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
				if ("0".equals(custype)) {
					this.selectedRowData.setCneeid((Long) cs.get(0).get("id"));
					this.selectedRowData.setCneename((String) cs.get(0).get(
							"name"));
					this.selectedRowData.setCneetitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "cneeid");
					this.update.markUpdate(UpdateLevel.Data, "cneename");
					this.update.markUpdate(UpdateLevel.Data, "cneetitle");
				} else if ("1".equals(custype)) {

					this.selectedRowData.setCnorid((Long) cs.get(0).get("id"));
					this.selectedRowData.setCnorname((String) cs.get(0).get(
							"name"));
					this.selectedRowData.setCnortitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "cnorid");
					this.update.markUpdate(UpdateLevel.Data, "cnorname");
					this.update.markUpdate(UpdateLevel.Data, "cnortitle");

				} else if ("2".equals(custype)) {

					this.selectedRowData
							.setNotifyid((Long) cs.get(0).get("id"));
					this.selectedRowData.setNotifyname((String) cs.get(0).get(
							"name"));
					this.selectedRowData.setNotifytitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "notifyid");
					this.update.markUpdate(UpdateLevel.Data, "notifyname");
					this.update.markUpdate(UpdateLevel.Data, "notifytitle");

				} else if ("3".equals(custype)) {

					this.selectedRowData.setAgenid((Long) cs.get(0).get("id"));
					this.selectedRowData.setAgenname((String) cs.get(0).get(
							"customerabbr"));
					;
					this.selectedRowData.setAgentitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "agenid");
					this.update.markUpdate(UpdateLevel.Data, "agenname");
					this.update.markUpdate(UpdateLevel.Data, "agentitle");
				}
				showCustomerWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
				showCustomerWindow.show();
				customerQry();

				if ("0".equals(custype)) {
					Browser.execClientScript("cneename.focus");

				} else if ("1".equals(custype)) {
					Browser.execClientScript("cnorname.focus");
				} else if ("2".equals(custype)) {
					Browser.execClientScript("notifyname.focus");
				} else if ("3".equals(custype)) {
					Browser.execClientScript("agenname.focus");
				}

			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if ("0".equals(custype)) {
			this.selectedRowData.setCneeid((Long) m.get("id"));
			this.selectedRowData.setCneename((String) m.get("name"));
			this.selectedRowData.setCneetitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cneeid");
			this.update.markUpdate(UpdateLevel.Data, "cneename");
			this.update.markUpdate(UpdateLevel.Data, "cneetitle");
		} else if ("1".equals(custype)) {

			this.selectedRowData.setCnorid((Long) m.get("id"));
			this.selectedRowData.setCnorname((String) m.get("name"));
			this.selectedRowData.setCnortitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cnorid");
			this.update.markUpdate(UpdateLevel.Data, "cnorname");
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");

		} else if ("2".equals(custype)) {
			this.selectedRowData.setNotifyid((Long) m.get("id"));
			this.selectedRowData.setNotifyname((String) m.get("name"));
			this.selectedRowData.setNotifytitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifyid");
			this.update.markUpdate(UpdateLevel.Data, "notifyid");
			this.update.markUpdate(UpdateLevel.Data, "notifyname");
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");
		} else if ("3".equals(custype)) {
			this.selectedRowData.setAgenid((Long) m.get("id"));
			this.selectedRowData.setAgenname((String) m.get("customerabbr"));
			this.selectedRowData.setAgentitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "agenid");
			this.update.markUpdate(UpdateLevel.Data, "agenname");
			this.update.markUpdate(UpdateLevel.Data, "agentitle");

		}
		showCustomerWindow.close();
	}

	/**
	 * 预览报表 --没有控制
	 */
	@Action
	public void scanReport() {
		//AppUtils.debug(Application.class.getResource("/").toString());
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showPreview.jsp?raq=/define/"
				+ billfilename;
		
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
		
	}

//	/**
//	 * 打印报表 --控制
//	 */
//	@Action
//	public void printReport() {
//
//		if (selectedRowData.getIsprintlock()) {
//			MessageUtils.alert("您已打印过提单,请先解锁");
//			return;
//		}
//		
//		String rpturl = AppUtils.getRptUrl();
//		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
//				+ billfilename;
//		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());

//		if (this.getReportCode(billfilename).contains("ORG")) { // 如果打正本，勾选提单锁定，其他类型不操作
//			String sql = "UPDATE bus_ship_bill  SET isprintlock  = TRUE , reportid = "
//					+ getReportid(billfilename)
//					+ " ,updater = '"
//					+ AppUtils.getUserSession().getUsercode()
//					+ "' , updatetime = NOW() WHERE id = " + this.mPkVal + ";";
//			// ApplicationUtils.debug(sql);
//			//打印正本，向bus_ship_bill_reg插入数据，提单登记显示一条记录
//			BusShipBillreg busshipbillreg = new BusShipBillreg();
//			busshipbillreg.setBillid(this.mPkVal);
////			String sql2 = "INSERT INTO bus_ship_bill_reg(id,code,billid,iscancel)VALUES(getid(),'',"+this.mPkVal+",'FALSE');";
//			try {
//				serviceContext.busShipBillregMgrService.BusShipBillregDao.create(busshipbillreg);
//				serviceContext.busShipBillMgrService.busShipBillDao
//						.executeSQL(sql);
////				serviceContext.busShipBillMgrService.busShipBillDao
////				.executeSQL(sql2);
//				refreshMasterForm();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
//		}
	
//		/**
//		 * HBL提单类型启动流程
//		 */
//		if (!"".equals(this.bltype)&& this.bltype != null &&"H".equals(this.bltype)) {
//		/**
//		 * 启动流程
//		 */
//			//this.start();
//		}
//
//	}

	private String getArgs() {
		String args = "";
		args += "&id=" + this.mPkVal;
		return args;
	}

	@Bind
	public UIWindow showPortWindow;

	@Bind
	public UIDataGrid portGrid;

	@Bind
	private String qryPortKey;

	/**
	 * 
	 */
	@Action
	public void portQry() {
		this.portchooseserviceBean.qryPort(qryPortKey);
		this.portGrid.reload();
	}

	@Bind(id = "portGrid", attribute = "dataProvider")
	public GridDataProvider getportGridDataProvider() {
		return this.portchooseserviceBean.getPortDataProvider(portsql);
	}

	@Action
	public void showPortAction() {
		String portcode = (String) AppUtils.getReqParam("portcode");

		qryPortKey = portcode;
		int index = qryPortKey.indexOf("/");
		if (index > 1)
			qryPortKey = qryPortKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");

		porttype = (String) AppUtils.getReqParam("porttype");

		if ("0".equals(porttype)) {
			portsql = "AND ispol = TRUE";
		} else if ("1".equals(porttype)) {
			portsql = "AND ispod = TRUE";
		} else if ("2".equals(porttype)) {
			portsql = "AND ispdd = TRUE";
		}

		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryPortKey");
			showPortWindow.show();
			portQry();
			if ("0".equals(porttype)) {

				Browser.execClientScript("polButton.focus");

			} else if ("1".equals(porttype)) {

				Browser.execClientScript("podButton.focus");
			} else if ("2".equals(porttype)) {

				Browser.execClientScript("pddButton.focus");
			}
			return;
		}

		try {
			List<Map> cs = portchooseserviceBean.findPort(qryPortKey, portsql);
			if (cs.size() == 1) {
				if ("0".equals(porttype)) {
					this.selectedRowData.setPolid((Long) cs.get(0).get("id"));

					this.selectedRowData
							.setPol((String) cs.get(0).get("namee"));
					this.update.markUpdate(UpdateLevel.Data, "polid");

					this.update.markUpdate(UpdateLevel.Data, "pol");
				} else if ("1".equals(porttype)) {

					this.selectedRowData.setPodid((Long) cs.get(0).get("id"));

					this.selectedRowData
							.setPod((String) cs.get(0).get("namee"));
					this.update.markUpdate(UpdateLevel.Data, "podid");

					this.update.markUpdate(UpdateLevel.Data, "pod");

				} else if ("2".equals(porttype)) {

					this.selectedRowData.setPddid((Long) cs.get(0).get("id"));

					this.selectedRowData
							.setPdd((String) cs.get(0).get("namee"));
					this.update.markUpdate(UpdateLevel.Data, "pddid");

					this.update.markUpdate(UpdateLevel.Data, "pdd");

				}
				showPortWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryPortKey");
				showPortWindow.show();
				portQry();

				if ("0".equals(porttype)) {
					Browser.execClientScript("polButton.focus");

				} else if ("1".equals(porttype)) {
					Browser.execClientScript("podButton.focus");
				} else if ("2".equals(porttype)) {
					Browser.execClientScript("pddButton.focus");
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void portGrid_ondblclick() {
		Object[] objs = portGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if ("0".equals(porttype)) {
			this.selectedRowData.setPolid((Long) m.get("id"));
			this.selectedRowData.setPolcode((String) m.get("code"));
			this.selectedRowData.setPol((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "polid");
			this.update.markUpdate(UpdateLevel.Data, "polcode");
			this.update.markUpdate(UpdateLevel.Data, "pol");
		} else if ("1".equals(porttype)) {

			this.selectedRowData.setPodid((Long) m.get("id"));
			this.selectedRowData.setPodcode((String) m.get("code"));	
			this.selectedRowData.setPod((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "podid");
			this.update.markUpdate(UpdateLevel.Data, "podcode");
			this.update.markUpdate(UpdateLevel.Data, "pod");

		} else if ("2".equals(porttype)) {
			this.selectedRowData.setPddid((Long) m.get("id"));
			this.selectedRowData.setPddcode((String) m.get("code"));
			this.selectedRowData.setPdd((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "pddid");
			this.update.markUpdate(UpdateLevel.Data, "pddcode");
			this.update.markUpdate(UpdateLevel.Data, "pdd");
		}
		showPortWindow.close();
	}

	@Accessible
	public int[] getGridSelIds() {

		String sql = "";
		if ("-1".equals(jobid)) {
			return null;
		} else {
			if ("H".equals(this.bltype)) {
				sql = "\nSELECT "
						+ "\n(CASE WHEN a.billid = "
						+ this.mPkVal
						+ " THEN TRUE ELSE FALSE END) AS flag"
						+ "\nFROM _bus_ship_container a "
						+ "\nWHERE a.isdelete =false AND  (billid IS NULL OR billid ="
						+ this.mPkVal + ") AND a.jobid ="+ jobid + "\nORDER BY a.billid";
			} else if ("M".equals(this.bltype)) {
				sql = "\nSELECT "
						+ "\n(CASE WHEN a.billmblid = "
						+ this.mPkVal
						+ " THEN TRUE ELSE FALSE END) AS flag"
						+ "\nFROM _bus_ship_container a "
						+ "\nWHERE a.isdelete =false AND  (a.billmblid IS NULL OR a.billmblid ="
						+ this.mPkVal + ")AND a.jobid ="+ jobid + "\nORDER BY a.billmblid";
			}

		}
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if ((Boolean) map.get("flag"))
					rowList.add(rownum);
				rownum++;
			}
			int row[] = new int[rowList.size()];
			for (int i = 0; i < rowList.size(); i++) {
				row[i] = rowList.get(i);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 一个都没有勾选,全部设置为false
	 */
	@Action
	public void chooseship() {
		//AppUtils.debug(this.selectedRowData.getIsdetail());
		if (this.mPkVal == -1l) {
			MessageUtils.alert("请先保存提单");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null)
			ids = new String[] {};
		try {
			serviceContext.busShipBillMgrService.chooseShip(ids, this.mPkVal,
					this.selectedRowData.getBltype(),this.jobid,this.selectedRowData.getIsdetail());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refreshMasterForm2();
	}

	public Long getshipid(Long id) {
		String sql = "SELECT id FROM bus_shipping where isdelete =false AND jobid= "
				+ id;
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("id");

	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		
		String filter = "\nAND jobid = " + this.jobid;
		
		if ("H".equals(this.bltype)) {
			filter += "\nAND (billid IS NULL OR billid =" + this.mPkVal + ")";
			m.put("order", "billid");
		} else if ("M".equals(this.bltype)) {
			filter += "\nAND (billmblid IS NULL OR billmblid = " + this.mPkVal
					+ ")";
			m.put("order", "billmblid");
		}else{
			m.put("order", "billid,billmblid");
		}
		
		m.put("filter",filter);
		m.put("qry", qry);
		return m;
	}

	@Action
	public void savecne() {
		String cneid = AppUtils.getReqParam("cneid").trim();
		String cnename = AppUtils.getReqParam("cnename").trim();
		String cnetitle = AppUtils.getReqParam("cnetitle").trim();
		String type = AppUtils.getReqParam("type").trim();

		try {
			// cneid 为空,说明是新增的用户 ,不为空 update
			if (StrUtils.isNull(cneid) && StrUtils.isNull(cnetitle)) {
				MessageUtils.alert("请输入正确信息");

			} else {
				if (type.equals("0")) {

					// 保存收货人
					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("C");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-C-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCneeid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCneename(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}
					// 发货人
				} else if (type.equals("1")) {

					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("S");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-S-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCnorid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCnorname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}
					// hbl代理
				} else if (type.equals("2")) {

					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("A");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-A-" + getCusCode("2")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setAgenid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setAgenname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}
					// 通知人
				} else if (type.equals("3")) {

					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("N");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-N-" + getCusCode("3")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotifyid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotifyname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}
				}

			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	public Long getCusCode(String type) {
		String sql = "";
		if (type.equals("0")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'C' AND customerid = "
					+ this.customerid;
		} else if (type.equals("1")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'S' AND customerid = "
					+ this.customerid;
		} else if (type.equals("2")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'A' AND customerid = "
					+ this.customerid;
		} else if (type.equals("3")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'N' AND customerid = "
					+ this.customerid;
		}

		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("count");
	}

	public String[] getCusdesc(String code) {
		String[] re = new String[2];
		try {
			String sql = "SELECT name,id FROM sys_corpcontacts WHERE  name ='"+code+"' AND customerid = "+this.customerid+" LIMIT 1;";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			re[0] = ((Long) m.get("id")).toString();
			re[1] = (String) m.get("name");
		} catch (Exception e) {
			re[0] = "0";
			re[1] = "";
		}
		return re;

	}

	@Action
	public void chooseBook() {

		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		String url = AppUtils.getContextPath()
				+ "/pages/module/ship/shipbookingchoose.xhtml?containerid="
				+ ids[0] + "&shipid=" + getshipid(this.jobid);
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Action
	public void cancelBook() {

		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			serviceContext.busBookingMgrService.cancelBook(ids, AppUtils
					.getUserSession().getUsercode());
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	@Bind(id = "billformat")
	public List<SelectItem> getBillformat() {
		try {
			billtype = "shippinghbl";
			if(this.selectedRowData!=null && "M".equals(this.selectedRowData.getBltype())){
				billtype = "shippingmbl";
			}
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='"+billtype+"' AND (userid IS NULL OR ispublic OR userid = "+AppUtils.getUserSession().getUserid()+")",
					"AND isdelete = false order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	/*
	 * 提单解锁
	 */
	@Action
	public void isprintlockAjaxSubmit() {
		if(this.mPkVal < 0){
			MessageUtils.alert("请先保存!");
			selectedRowData.setIsprintlock(false);
			update.markUpdate(true, UpdateLevel.Data, "isprintlock");
			return;
		}
		Boolean isprintlock = selectedRowData.getIsprintlock();
		if (isprintlock == true) {
			//MessageUtils.alert("不能手动锁定");
			//selectedRowData.setIsprintlock(false);
			//refreshMasterForm();
			//return;
		}
		String sql = "UPDATE bus_ship_bill  SET isprintlock = " + isprintlock
				+ " ,updater = '" + AppUtils.getUserSession().getUsercode()
				+ "' , updatetime = NOW() WHERE id = " + this.mPkVal + ";";
		try {
			serviceContext.busShipBillMgrService.busShipBillDao.executeSQL(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refreshMasterForm();
	}
	
	public Long getReportid(String filename) {
		String sql = " filename = '" + this.billfilename + "'";
		Long id;
		try {
			id = serviceContext.sysReportMgrService.sysReportDao
					.findOneRowByClauseWhere(sql).getId();
		} catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
		return id;
	}

	public String getReportCode(String filename) {
		String sql = " filename = '" + this.billfilename + "'";
		String code;
		try {
			code = serviceContext.sysReportMgrService.sysReportDao
					.findOneRowByClauseWhere(sql).getCode();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return code;
	}

	@Bind
	@SaveState
	public String detailsContent;

	@Bind
	@SaveState
	public String detailsTitle;

	@SaveState
	public String type;

	@Bind
	public UIWindow detailsWindow;

	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");
		this.detailsWindow.show();

		if ("1".equals(type)) { // 发货人大框
			this.detailsContent = content;
			this.detailsTitle = "发货人";

		} else if ("2".equals(type)) { // 收货人大框
			this.detailsContent = content;
			this.detailsTitle = "收货人";

		} else if ("3".equals(type)) { // 通知人大框
			this.detailsContent = content;
			this.detailsTitle = "通知人";

		} else if ("4".equals(type)) { // HBL代理大框
			this.detailsContent = content;
			this.detailsTitle = "HBL代理";

		} else if ("5".equals(type)) { // 唛头大框
			this.detailsContent = content;
			this.detailsTitle = "唛头";

		} else if ("6".equals(type)) { // 品名大框
			this.detailsContent = content;
			this.detailsTitle = "品名";

		}else if ("7".equals(type)) { // 柜子编辑中的品名（英文）
			this.detailsContent = content;
			this.detailsTitle = "品名(英文)";

		}else if ("8".equals(type)) { // 唛头（小框）
			this.detailsContent = content;
			this.detailsTitle = "唛头";
		}

		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
	}

	/**
	 * 
	 */
	@Action
	public void saveDetails() {
		if(this.selectedRowData.getIsprintlock()) {
			MessageUtils.alert("提单已锁定，不能修改！");
			return;
		}

		setDetails(this.type);
		this.saveMaster();


		if(0 != dtlData.getId()){
			serviceContext.busShipContainerMgrService.saveData(dtlData);
		}
		this.detailsWindow.close();
	}

	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.type);
		this.detailsWindow.close();
	}

	public void setDetails(String type) {
		if ("1".equals(type)) { // 发货人大框
			this.selectedRowData.setCnortitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");

		} else if ("2".equals(type)) { // 收货人大框
			this.selectedRowData.setCneetitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cneetitle");

		} else if ("3".equals(type)) { // 通知人大框
			this.selectedRowData.setNotifytitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");

		} else if ("4".equals(type)) { // HBL代理大框
			this.selectedRowData.setAgentitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "agentitle");

		} else if ("5".equals(type)) { // 唛头大框
			this.selectedRowData.setMarksno(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "marksno");

		} else if ("6".equals(type)) { // 品名大框
			this.selectedRowData.setGoodsdesc(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "goodsdesc");

		}else if ("7".equals(type)) { // 柜子编辑中的品名（英文）
			this.dtlData.setGoodsnamee(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "goodsnamee");

		}else if ("8".equals(type)) {  // 唛头（小框）
			this.dtlData.setMarkno(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "markno");
		}
	}

	/**
	 * 保存关闭
	 */
	@Action
	protected void saveCnt() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			this.editWindow.close();
			if(dtlData.getIsselect()){
				updateChooseship();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	private UIButton saveMaster;
	
	@Bind
	private UIButton addMaster;
	
	@Bind
	private UIButton scanReport;
	
	@Bind
	private UIButton delMaster;
	
//	@Bind
//	private UICheckBox isprintlock;
	
	@Bind
	private UIButton saveDetails;
	
	@Bind
	private UIButton chooseship;
	
	@Bind
	private UIButton butcnorname;   
	
	@Bind
	private UIButton bucneename;
	
	@Bind
	private UIButton bunotifyname;
	
	@Bind
	private UIButton buagenname;
	
	@Bind
	private UIButton saveCnt;
	

	private void disableAllButton(Boolean flag) {
		saveMaster.setDisabled(flag);
		delMaster.setDisabled(flag);
		saveDetails.setDisabled(flag);
		chooseship.setDisabled(flag);
		butcnorname.setDisabled(flag);
		bucneename.setDisabled(flag);
		bunotifyname.setDisabled(flag);
		buagenname.setDisabled(flag);
		saveCnt.setDisabled(flag);
	}
	
	@Action
	public void showdtlaction() {
		String isShow = AppUtils.getReqParam("isShow");
		if ("1".equals(isShow)) {
			String message = this.ShowMessage();
			if (message != null) {
				showDtlWindow.show();
				this.dtlContent = message;
				this.update.markUpdate(UpdateLevel.Data, "dtlContent");
			} else {
				MessageUtils.alert("无提单要求");
			}
		}else{
			showDtlWindow.close();
		}
	}
	 
//		public String ShowMessage() {
//			if (this.jobid != null) {
//				BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao
//						.findOneRowByClauseWhere("jobid = " + this.jobid + "");
//				String ClaimClear = busShipping.getClaimBill();
//				if (ClaimClear == null ||"".equals(ClaimClear) ) {
//					return null;
//				}
//				return "\n提单要求\n" + ClaimClear + "";
//			}
//			return null;
//		}
		
		public String ShowMessage() {
//			String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid,
//					WorkFlowEnumerateShip.SHIPBILL, "id");
			String re = "";
			String ins = "";
//			if (workids == null) {
//				ins = "";
//			} else {
//				IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//				IWorkItem workItem = workflowSession.findWorkItemById(workids[0]);
//				String processinstranseid = workItem.getTaskInstance()
//						.getProcessInstanceId();
//				ins = WorkFlowUtil.getActivityConsByWorkitem(processinstranseid,
//						workids[0]);
//			}

			if (this.jobid != null) {
				BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao
						.findOneRowByClauseWhere("jobid = " + this.jobid + "");
				String ClaimClear = busShipping.getClaimBill();
				if (!StrUtils.isNull(ClaimClear) && !StrUtils.isNull(ins)) {
					re = ClaimClear + "\n" + ins;
				} else if (!StrUtils.isNull(ClaimClear) && StrUtils.isNull(ins)) {
					re = ClaimClear;
				} else if (StrUtils.isNull(ClaimClear) && !StrUtils.isNull(ins)) {
					re = ins;
				} else {
					re = null;
				}

			}
			return re;
		}

		@Bind
		private UIWindow dtlDialog2;
		@Bind
		private UIIFrame dtlIFrame2;

		
		@Action
		public void regbill() {
			if (this.mPkVal == -1l) {
				MessageUtils.alert("请先保存主表");
				return;
			}
			if(!this.selectedRowData.getIsprintlock()){
				MessageUtils.alert("提单还未打印,或者多次打印,请确认!");
				
			}
			String url = AppUtils.getContextPath()
					+ "/pages/module/ship/billregister.xhtml?billnos="
					+ this.selectedRowData.getHblno();
			dtlIFrame2.setSrc(url);
			update.markAttributeUpdate(dtlIFrame2, "src");
			update.markUpdate(true, UpdateLevel.Data, dtlDialog2);
			dtlDialog2.show();
		}
		
		public void setDisable(){
			if (this.bltype.equals("M")) {
				cotrcarryitem.disable();
			}
		}
		/**
		 * 区分启动流程类型
		 */
		public String queryFCLStart(){
			String querySql = "SELECT s.ishbl,s.ismbl,s.mblcorpid FROM bus_shipping s, fina_jobs t WHERE t.id = s.jobid AND  t.isdelete = FALSE AND s.isdelete = FALSE AND t.ldtype = 'F' AND t.ldtype2 = 'F' AND t.id = " +this.jobid;
			 Map map;
			try {
				map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				 Boolean ishbl =(Boolean)map.get("ishbl");
				 Boolean ismbl =(Boolean)map.get("ismbl");
				  if(ishbl == true&&ismbl==true){
					  String sql = "SELECT code FROM sys_corporation WHERE isdelete = FALSE AND id = " + (Long)map.get("mblcorpid");
					  Map map2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					  String corp = (String)map2.get("code");
					  if(corp !=null && corp.equals("GZ")){//MBL公司为广州
						  return "GZ";
					  }else if(corp !=null && corp.equals("DB")){//MBL公司为迪拜
						  return "DB";
					  }
				  }else if(ishbl == true&&ismbl==false){//启动HBL核销
					  return "CH";
				  }
				  return "0";
			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
		}
		
		/**
		 * 开始流程
		 */
//		@Action
//		public void start() {
//			String type = this.queryFCLStart();
//			if(type.equals("0")){
//				return;
//			}else if(type.equals("GZ")||type.equals("DB")){
//				if(this.queryDoc()==false){
//					MessageUtils.alert("此单为整柜,没有指派国内文件,无法启动HBL换MBL流程!");
//					return;
//				}
//				
//				if(this.queryDService()==false){
//					MessageUtils.alert("此单为整柜,没有指派国外客服,无法启动HBL换MBL流程!");
//					return;
//				}
//				
//				if(this.queryCService()==false){
//					MessageUtils.alert("此单为整柜,没有指派国内客服,无法启动HBL换MBL流程!");
//					return;
//				}
//				
//			}else if(type.equals("CH")){
//				if(this.queryDoc()==false){
//					MessageUtils.alert("此单为整柜,没有指派国内文件,无法启动HBL核销流程!");
//					return;
//				}
//				
//				if(this.queryCService()==false){
//					MessageUtils.alert("此单为整柜,没有指派国内客服,无法启动HBL核销流程!");
//					return;
//				}
//				
//				if(this.queryDService()==false){
//					MessageUtils.alert("此单为整柜,没有指派国外客服,无法启动HBL核销流程!!");
//					return;
//				}
//				
//			}
//			
//			
////			if(queryCCDivider() == false && queryPricer()== false){
////				MsgUtil.alert("没有寻找到该单指派流程的客服(报价客服或国内客服),请指派客服,再启动!");
////				return;
////			}
//			 Boolean type3 = this.queryStart("HBLExchangeMBLProcessPort");
//			if(type3==true && type.equals("DB")){
//				pass("HBLExchangeMBLProcessPort");
//				MessageUtils.alert("HBL换MBL(口岸)流程启动成功!");
//			}else if(type3==false){
//				MessageUtils.alert("此单已开启过HBL换MBL(口岸)流程!");
//			}else{
//				 Boolean type2 = this.queryStart("HBLExchangeMBLProcess");
//			if(type2==true && type.equals("GZ")){
//				pass("HBLExchangeMBLProcess");
//				MessageUtils.alert("HBL换MBL(目的港)流程启动成功!");
//			  	}else if(type2==false){
//			  	MessageUtils.alert("此单已开启过HBL换MBL(目的港)流程!");	
//			  	}else{
//			  		 Boolean type4 = this.queryStart("HBLVerification");
//			  		if(type4 == true && type.equals("CH")){//为HBL核销
//			  			pass("HBLVerification");
//			  			MessageUtils.alert("HBL核销流程启动成功!");
//			  		}else if(type4 == false){
//			  			MessageUtils.alert("此单已开启过HBL核销流程!");	
//			  		}
//			  	}
//			}
//		}
		
//		public void pass(String processid) {
//			Map<String, Object> m = new HashMap<String, Object>();
//			m.put("id", jobid);
//			m.put("sn", job.getNos());
//			try {
//				// WorkFlowUtil.startFF("ShipFCLProcess0712", m, AppUtil
//				// .getUserSession().getUsercode());
//				WorkFlowUtil.startFF(processid, m, AppUtils.getUserSession()
//						.getUsercode(),jobid);
//
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
//		}
		
//		/**
//		 * 确认判断是否重复启动流程
//		 */
//		public boolean queryStart(String flowName){
//			String sql = "SELECT v.value FROM t_ff_rt_taskinstance t,t_ff_rt_procinst_var v WHERE v.processinstance_id = t.processinstance_id AND t.process_id = '"+flowName+"' AND v.name = 'id' AND v.value LIKE 'java.lang.Long#%' AND  CAST(replace(v.value,'java.lang.Long#','') AS BIGINT) = "+jobid+";";
//			List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
//			if(list.size() <= 0 || "".equals(list)){
//				return true;
//			}else{return false;}
//		}
//		/**
//		 * 确认是否有国内文件分配
//		 */
//		public boolean queryDoc(){
//			List<Map> usersForRole = WorkFlowUtil.findAllUsersForRoleByJobsAssign("D",jobid);
//			if (usersForRole.size()==0){
//				return false;
//			}else{
//				return true;
//			}
//		}
//		/**
//		 * 确认是否有国外客服分配
//		 */
//		public boolean queryDService(){
//			List<Map> usersForRole = WorkFlowUtil.findAllForeignUsersForRoleByJobsAssign("C",jobid);
//			if (usersForRole.size()==0){
//				return false;
//			}else{
//				return true;
//		}
//		}
//		/**
//		 * 确认是否有国内客服分配	
//		 */
//			public boolean queryCService(){
//				List<Map> usersForRole = WorkFlowUtil.findAllUsersForRoleByJobsAssign("C",jobid);
//				if (usersForRole.size()==0){
//					return false;
//				}else{
//					return true;
//			}
//		}
			
		@Action
		public void printattach(){
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
					+ "HBL(GSIT_ORG)-ATTACHMENT.raq";
			AppUtils.openWindow("_shippingHblReport", openUrl + getArgs());
		}
		
		@Action
		public void savelastbill(){
			String rpturl = AppUtils.getReqParam("hblrptjs");
			String upg = "UPDATE bus_shipping set hblrpt = '" + rpturl + "' where isdelete = FALSE AND jobid = " + this.jobid + ";";
			this.serviceContext.busShippingMgrService.busShippingDao.executeSQL(upg);
			////System.out.println(this.jobid+billfilename);
		}
		
		
		@Bind
		@SaveState
		public String splitCount;
		
		
		/*
		 * 拆柜
		 */
		@Action
		public void splitCnt() {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("选择一个柜子拆分！");
				return;
			}
			if(StrUtils.isNull(splitCount) || "0".equals(splitCount)) {
				MessageUtils.alert("请选择拆分份数(不能为0)！");
				return;
				
			}
			String usercode = AppUtils.getUserSession().getUsercode();
			String sql = "SELECT f_bus_ship_container_split('cntid="+ids[0]+";splitcount="+splitCount+";usercode="+usercode+"');";
			try {
				this.serviceContext.busCustomsMgrService.busCustomsDao.executeQuery(sql);
				MessageUtils.alert("OK");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		
		@Bind(id = "orderrpt")
		public List<SelectItem> getOrderrpt() {
			try {
				return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
						"sys_report AS d", "WHERE modcode='shippingsingle' AND isdelete = FALSE",
						"order by code");
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
		}
		
		@SaveState
		@Bind
		public String orderRpt;
		
		@Action
		public void printOrder() {
			String rpturl = AppUtils.getRptUrl();
			String bill_single_new = ConfigUtils.findSysCfgVal("bill_single_new");
			Map map = null ;
			try {
				String querySql = "SELECT 1 FROM sys_report where modcode = 'shippingsingle' AND templete IS NOT NULL AND filename = '"+orderRpt+"';";
				map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			} catch (Exception e) {
			}
			if("Y".equals(bill_single_new) && (map != null && map.size() == 1)){
				String url = rpturl + "/reportEdit/file/print.jsp?rp="+orderRpt+"&b="+this.mPkVal+"&u="+AppUtils.getUserSession().getUserid()+"&reporttype=shippingsingle";
				AppUtils.openWindow("_shippingOrderReport",url);
			}else{
				String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/" + orderRpt;
				String url = openUrl + getArgsusr();
		//		String upg = "UPDATE bus_shipping set orderrpt = '" + orderRpt + "' where id = " + billid + ";";
		//		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
				AppUtils.openWindow("_shippingOrderReport",url);
			}
			
		}
		
		private String getArgsusr() {
			String args = "";
			Long userid = AppUtils.getUserSession().getUserid();
			args = args+"&id=" + this.selectedRowData.getId()+"&userid="+userid;
			return args;
		}
		
		
		private void updateChooseship() {
			if (this.mPkVal == -1l) {
				MessageUtils.alert("请先保存提单");
				return;
			}
			try {
				serviceContext.busShipBillMgrService.busShipBillDao.executeQuery("SELECT f_bus_ship_container_createbillinfo('billid=" + this.mPkVal + ";bltype=" + this.selectedRowData.getBltype() + ";isdetail="+this.selectedRowData.getIsdetail()+"');");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
			this.refreshMasterForm2();
		}
		
		public String apiKeyGenerate(String ApiSecret,String ApiSalt) throws UnsupportedEncodingException{
			return EncryptUtil.md5Encode(ApiSecret+ApiSalt,"UTF-8");
		}
		
		@Bind
		public UIWindow hxWindow;
		
		@Bind
		@SaveState
		private String hxurl;
		
		@Action
		public void onlineFeedLink(){
			StringBuilder sb = new StringBuilder();
			long currentTimeMillis = System.currentTimeMillis();//时间戳
			String ApiKey ="";
			try {
				ApiKey = apiKeyGenerate(currentTimeMillis+"",(("H".equals(this.bltype))?this.selectedRowData.getHblno():this.selectedRowData.getMblno()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(this.selectedRowData.getHblno()==null||this.selectedRowData.getHblno().equals("")){
				MessageUtils.alert("HBL No can not be null!");
				return;
			}
			String h = ("H".equals(this.bltype))?"HBL":"MBL";
			String csUrlbase = ConfigUtils.findSysCfgVal("cs_url_base");
			//String csUrlbase = "http://127.0.0.1:8020/hx";
			String csUrlbasetime = ConfigUtils.findSysCfgVal("cs_url_base_time");//系统中URL有效时间
			int parseInt = csUrlbasetime==null||csUrlbasetime.equals("")?3:Integer.parseInt(csUrlbasetime);
			FinaJobs job = serviceContext.jobsMgrService.finaJobsDao.findById(this.selectedRowData.getJobid());
			if(job.getNos().equals("")){
				MessageUtils.alert("Job No. can not be null!");
				return;
			}
			String url = csUrlbase+"/si/billesi/"+(("H".equals(this.bltype))?selectedRowData.getHblno():selectedRowData.getMblno())
					+"?tp="+currentTimeMillis+"&key="+ApiKey+"&billtype="+h;
			
			//neo 20191216 新版so 系统地址
			if(csUrlbase.indexOf("so")>1){
				String jobid = String.valueOf(this.selectedRowData.getJobid());
				//jobid = jobid.substring(0, jobid.length()-4);
				url = csUrlbase+"/esibill?method=esiInfo&nos="+(("H".equals(this.bltype))?selectedRowData.getHblno():selectedRowData.getMblno())+"&tp="+currentTimeMillis+"&key="+ApiKey+"&key2="+selectedRowData.getId()+"&billtype="+h;
			}
			
			sb.append(url);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = dateformat.format(currentTimeMillis+parseInt * 60 * 60 * 1000).toString();
			sb.append("\n\n链接有效时间至："+time) ;
			String hblno = (selectedRowData.getHblno()==null?"":selectedRowData.getHblno());
			String sono = (shipping.getSono()==null?"":shipping.getSono());
			sb.append("\n\nSO:"+sono);
			sb.append("\n\nHBL:"+hblno);
			this.hxurl = sb.toString();
			update.markUpdate(true,UpdateLevel.Data,"hxurl");
			hxWindow.show();
			Browser.execClientScript("sono.setValue('"+sono+"');hblno.setValue('"+hblno+"');testUrl.setValue('"+url+"');time.setValue('"+time+"');refreshQRCode();");
		}
		
		@Bind
		public UIIFrame containerIframe;
		
		@Bind
		public UIWindow showContainerWindow;
		
		/**
		 * 装箱单明细
		 * */
		@Action
		public void qryContainer() {
			String[] ids = grid.getSelectedIds();
			if (ids == null || ids.length <= 0 || ids.length > 1) {
				MessageUtils.alert("请选中一行!");
				return;
			}
			String id = ids[0];
			Long cid = Long.valueOf(id);
			containerIframe.load("../ship/containerdetail.xhtml?cid="+cid);
			showContainerWindow.show();
		}
		
		@Action
		public void showRemarks() {
			showRemarksWindow.show();
		}
		
		@Action
		public void saveMasterRemarks() {
			saveMaster();
		}

		@Action
		public void esiRebot(){
			try{
				String sql = "SELECT (SELECT regexp_split_to_table(UPPER(y.code) , '\\(') LIMIT 1) AS carriercode  FROM bus_shipping x , sys_corporation y WHERE x.carrierid = y.id AND x.jobid = "+jobid+"";
				Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String carriercode = StrUtils.getMapVal(map, "carriercode");
				if(StrUtils.isNull(carriercode)){
					MessageUtils.alert("船公司不能为空!");
					return;
				}
				ApiEncryptUtil apiEncryptUtil = new ApiEncryptUtil();
				String userCode = AppUtils.getUserSession().getUsercode();
				
				String logID = "";
				String logPin = "";
				
				Vector<String> inttraV = new Vector<String>();
				
				inttraV.add("ANL");
				inttraV.add("CMA");
				inttraV.add("CNC");
				inttraV.add("COSCO");
				inttraV.add("DAL");
				inttraV.add("MSK");
				inttraV.add("MSC");
				inttraV.add("GOLD STAR");
				inttraV.add("HPL");
				inttraV.add("SEALAND");
				inttraV.add("ZIM");
				
				String carrierPrefix = "";
				
				for (String key : inttraV) {
					if(carriercode.equalsIgnoreCase(key)){
						carrierPrefix = "inttra";
					}
				}
				if("YML".equalsIgnoreCase(carriercode)){
					carrierPrefix = "yml";
				}
				
				
				
				if(!StrUtils.isNull(carrierPrefix)){
					String user = ConfigUtils.findRobotCfgVal(carrierPrefix + "_esi_user",userCode);
					String pwd = ConfigUtils.findRobotCfgVal(carrierPrefix + "_esi_pwd",userCode);
					
					if(StrUtils.isNull(user)){
						MessageUtils.alert("ESI账号未配置!");
						return;
					}
					
					if(StrUtils.isNull(pwd)){
						MessageUtils.alert("ESI密码未配置!");
						return;
					}
					
					logID = apiEncryptUtil.encrypt(user);
					logPin = new EMailSendUtil().decrypt(pwd);
					if(!StrUtils.isNull(logPin)){
						logPin = apiEncryptUtil.encrypt(logPin);
					}
				}
				String querySql = "select f_api_ufms_esirobot_bill('billid="+selectedRowData.getId()+";userid="+AppUtils.getUserSession().getUserid()+";logID="+logID+";logPin="+logPin+";') AS json";
				
		        map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		        if (map != null && map.containsKey("json") && map.get("json") != null) {
		        	String json = StrUtils.getMapVal(map, "json"); 
		        	json = StrUtils.getSqlFormat(json);
		        	String jobno =null;
		        	if(selectedRowData.getBltype()=="H"){
		        		if(selectedRowData.getHblno().isEmpty()){
		        			jobno = StrUtils.getSqlFormat(selectedRowData.getMblno());
		        		}else{
		        			jobno = StrUtils.getSqlFormat(selectedRowData.getHblno());
		        		}
		        		 
		        	}else{
		        		if(selectedRowData.getMblno().isEmpty()){
		        			jobno = StrUtils.getSqlFormat(selectedRowData.getHblno());
		        		}else{
		        			jobno = StrUtils.getSqlFormat(selectedRowData.getMblno());
		        		}
		        		 
		        	}
		        	
		        	
		        	//System.out.println(json);
		        	sql="insert into api_robot_esi(id,nos,jsonpost,inputer,inputtime) values(getid(),'"+jobno+"','"+json+"','"+AppUtils.getUserSession().getUsercode()+"' , NOW())";
		        	this.serviceContext.apiRobotEsiService.apiRobotEsiDao.executeSQL(sql);
		        	this.alert("OK");
		        }
			} catch (Exception e) {
	            MessageUtils.showException(e);
	        }
		}
}
