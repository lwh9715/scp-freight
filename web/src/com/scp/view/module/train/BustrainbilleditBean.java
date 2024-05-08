package com.scp.view.module.train;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.bus.BusTrain;
import com.scp.model.bus.BusTrainBill;
import com.scp.model.data.DatPackage;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;
@ManagedBean(name = "pages.module.train.bustrainbilleditBean", scope = ManagedBeanScope.REQUEST)
public class BustrainbilleditBean extends MastDtlView{
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;

	@SaveState
	@Accessible
	public BusTrainBill selectedRowData = new BusTrainBill();

	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();

	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();

	@SaveState
	@Accessible
	public BusTrain bustrain = new BusTrain();
	
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
	
	
	
	@Override
	public void refresh() {
		this.grid.reload();
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			workItemId = (String) AppUtils.getReqParam("workItemId");
			jobid = Long.parseLong(AppUtils.getReqParam("jobid"));
			List<BusTrain> bus = this.serviceContext.busTrainMgrService.busTrainDao.findAllByClauseWhere("jobid = "+jobid);
			bustrain = bus.get(0);
			billfilename = bustrain.getHblrpt();
			qryMap.put("jobid$", this.jobid);
			this.userid = AppUtils.getUserSession().getUserid().toString();
		}
	}
	
	@Override
	public void init() {
		selectedRowData = new BusTrainBill();
		String id = AppUtils.getReqParam("id");
		String jobid = AppUtils.getReqParam("jobid").trim();
		String bltype = AppUtils.getReqParam("bltype").trim();
		this.bltype = bltype;
		this.jobid = Long.valueOf(jobid);
		this.job = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
		this.customerid = job.getCustomerid();
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			this.bltype = this.selectedRowData.getBltype();
		} else {
			addMaster();
		}
		try {
			baseurl = ConfigUtils.findSysCfgVal(ConfigUtils.SysCfgKey.rpt_srv_url.name()); 
		} catch (Exception e) {
		}
		
	}
	
	@Override
	public void addMaster() {
		this.selectedRowData = new BusTrainBill();
		this.bustrain = serviceContext.busTrainMgrService.busTrainDao.findById(gettrainid(this.jobid));
		selectedRowData.setJobid(this.jobid);
		selectedRowData.setBltype(this.bltype);
		selectedRowData.setTrainid(gettrainid(this.jobid));
		selectedRowData.setBillcount("THREE(3)");
		selectedRowData.setPodid(this.bustrain.getPolid());
		selectedRowData.setPod(this.bustrain.getPod());
		selectedRowData.setPol(this.bustrain.getPol());
		selectedRowData.setPolid(this.bustrain.getPolid());
		selectedRowData.setPdd(this.bustrain.getPdd());
		selectedRowData.setPddid(this.bustrain.getPddid());
		selectedRowData.setCarryitem("CY-CY");
		selectedRowData.setVessel(this.bustrain.getVessel());
		selectedRowData.setVoyage(this.bustrain.getVoyage());
		selectedRowData.setCarrierid(this.bustrain.getCarrierid());
		selectedRowData.setFreightitem(this.bustrain.getFreightitem());
		selectedRowData.setPoa(this.bustrain.getPoa());
		selectedRowData.setDestination(this.bustrain.getDestination());
		selectedRowData.setPretrans(this.bustrain.getPretrans());
		selectedRowData.setPayplace(this.bustrain.getPayplace());
		selectedRowData.setHblno(this.bustrain.getHblno());
		selectedRowData.setPolcode(this.bustrain.getPolcode());
		selectedRowData.setPddcode(this.bustrain.getPddcode());
		selectedRowData.setPodcode(this.bustrain.getPodcode());
		selectedRowData.setDestinationcode(this.bustrain.getDestinationcode());
		if(this.bustrain.getPacker()!=null&&!this.bustrain.getPacker().equals("")){
			List<DatPackage> datPackage = serviceContext.packageMgrService.datPackageDao.findAllByClauseWhere("namee = '"+this.bustrain.getPacker()+"' AND isdelete = FALSE");
			if (datPackage!=null && datPackage.size() > 0){
				selectedRowData.setPackid(datPackage.get(0).getId());
			}
		}
		selectedRowData.setCneeid(this.bustrain.getCneeid());
		selectedRowData.setCneename(this.bustrain.getCneename());
		selectedRowData.setCneetitle(this.bustrain.getCneetitle());
		selectedRowData.setCnorid(this.bustrain.getCnorid());
		selectedRowData.setCnorname(this.bustrain.getCnorname());
		selectedRowData.setCnortitle(this.bustrain.getCnortitle());
		selectedRowData.setNotifyid(this.bustrain.getNotifyid());
		selectedRowData.setNotifyname(this.bustrain.getNotifyname());
		selectedRowData.setNotifytitle(this.bustrain.getNotifytitle());
		selectedRowData.setAgenid(this.bustrain.getAgenid());
		selectedRowData.setAgenname(this.bustrain.getAgenname());
		selectedRowData.setAgentitle(this.bustrain.getAgentitle());
		selectedRowData.setHbltype(this.bustrain.getHbltype());
		selectedRowData.setMbltype(this.bustrain.getMbltype());
		selectedRowData.setAtd(this.bustrain.getAtd());
		selectedRowData.setCarryitem(this.bustrain.getCarryitem());
		selectedRowData.setFreightitem(this.bustrain.getFreightitem());
		selectedRowData.setPaymentitem(this.bustrain.getPaymentitem());
		selectedRowData.setPiece(this.bustrain.getPiece());
		selectedRowData.setSignplace(this.bustrain.getSignplace());
		
		selectedRowData.setIsshowship(false);
		if ("H".equals(this.bltype)) {
			String sql = "SELECT chr(ascii(SUBSTRING(reverse(hblno) FROM 0 FOR 2))+1) FROM bus_train_bill WHERE jobid = "+this.jobid+" AND bltype = 'H' AND isdelete = FALSE ORDER BY reverse(hblno) DESC LIMIT 1";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map!=null&& map.containsKey("chr")&&map.get("chr")!=null){
					String chr = map.get("chr").toString();
					selectedRowData.setHblno(this.bustrain.getHblno()+chr);
				}else{
					selectedRowData.setHblno(this.bustrain.getHblno()+"A");
				}
			} catch (Exception e) {
				selectedRowData.setHblno(this.bustrain.getHblno()+"A");
			}
			selectedRowData.setMblno(this.bustrain.getMblno());
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
			selectedRowData.setHblno(this.bustrain.getHblno());
		}

		this.mPkVal = -1l;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
		

	}
	
	public Long gettrainid(Long id) {
		String sql = "SELECT id FROM bus_train where isdelete =false AND jobid= "
				+ id;
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("id");
	}
	
	/**
	 * 根据委托单中的数量确定提单号码
	 * 
	 * @return
	 */
	private String getHblno() {
		String sql = "SELECT COUNT(*)AS count FROM bus_train_bill WHERE isdelete = FALSE AND trainid= "
				+ gettrainid(this.jobid);
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
				+ gettrainid(this.jobid);
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		String sono = String.valueOf(m.get("sono"));
		if ("null".equals(sono) || "".equals(sono)) {
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
				serviceContext.busTrainBillMgrService.removeDate(selectedRowData.getId());
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
		if (StrUtils.isNull(selectedRowData.getFreightitem())) {
			MessageUtils.alert("请选择运费条款");
			return;
		}
		try {
			selectedRowData.setCnorname(AppUtils.replaceStringByRegEx(selectedRowData.getCnorname()));
			selectedRowData.setCnortitle(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitle()));
			selectedRowData.setCneename(AppUtils.replaceStringByRegEx(selectedRowData.getCneename()));
			selectedRowData.setCneetitle(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitle()));
			selectedRowData.setNotifyname(AppUtils.replaceStringByRegEx(selectedRowData.getNotifyname()));
			selectedRowData.setNotifytitle(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitle()));
			selectedRowData.setAgenname(AppUtils.replaceStringByRegEx(selectedRowData.getAgenname()));
			selectedRowData.setAgentitle(AppUtils.replaceStringByRegEx(selectedRowData.getAgentitle()));
			serviceContext.busTrainBillMgrService.saveData(selectedRowData);
			this.mPkVal = this.selectedRowData.getId();
			refreshMasterForm();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.busTrainBillMgrService.busTrainBillDao.findById(this.mPkVal);
		if(this.selectedRowData == null)return;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
	}
	
	
	public void refreshMasterForm2() {
		this.selectedRowData = serviceContext.busTrainBillMgrService.busTrainBillDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.grid.reload();
		this.grid.setSelections(getGridSelIds());
		
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
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao.findById(this.pkVal);
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

	@Bind
	@SaveState
	public Boolean isOneLine = false;
	
	@Action
	public void chooseship() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("请先保存提单");
			return;
		}
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null)
			ids = new String[] {};
		try {
			this.serviceContext.busShipContainerMgrService.updateContainerSelects(this.grid.getSelectedIds(), this.mPkVal);
			serviceContext.busTrainBillMgrService.choosetrain(ids, this.mPkVal,this.selectedRowData.getBltype(),this.jobid,this.selectedRowData.getIsdetail());
			
			this.serviceContext.userMgrService.sysCorporationDao
			.executeSQL("UPDATE bus_train_bill set marksno = f_bus_trainbill_cntinfo('billid="
					+ this.mPkVal
					+ ";type="
					+ (isOneLine ? "1" : "2")
					+ "') WHERE id = "
					+ this.selectedRowData.getId()
					+ ";");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refreshMasterForm2();
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if ("H".equals(this.bltype)) {
			qry += "\nAND (billid IS NULL OR billid =" + this.mPkVal + ")";
			m.put("order", "billid");
		} else if ("M".equals(this.bltype)) {
			qry += "\nAND (billmblid IS NULL OR billmblid = " + this.mPkVal
					+ ")";
			m.put("order", "billmblid");
		}
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
		String upg = "UPDATE bus_train set hblrpt = '" + rpturl + "' where isdelete = FALSE AND jobid = " + this.jobid + ";";
		this.serviceContext.busTrainMgrService.busTrainDao.executeSQL(upg);
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
					"sys_report AS d", "WHERE modcode='bustrainsingle' AND isdelete = FALSE",
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
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ orderRpt;
		String url = openUrl + getArgsusr();
		AppUtils.openWindow("_shippingOrderReport",url);
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
			serviceContext.busTrainBillMgrService.busTrainBillDao.executeQuery("SELECT f_bus_trainbill_container_createbillinfo('billid=" + this.mPkVal + ";bltype=" + this.selectedRowData.getBltype() + ";isdetail="+this.selectedRowData.getIsdetail()+"');");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refreshMasterForm2();
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
	
	@Bind(id = "billformat")
	public List<SelectItem> getBillformat() {
		try {
			billtype = "trainhbl";
			if(this.selectedRowData!=null && "M".equals(this.selectedRowData.getBltype())){
				billtype = "trainmbl";
			}
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='"+billtype+"' AND (userid IS NULL OR ispublic OR userid = "+AppUtils.getUserSession().getUserid()+")",
					"AND isdelete = false order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
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

		}
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
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

}
