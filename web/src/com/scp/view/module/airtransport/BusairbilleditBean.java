package com.scp.view.module.airtransport;

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
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.bus.BusAir;
import com.scp.model.bus.BusAirBill;
import com.scp.model.data.DatPackage;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;
@ManagedBean(name = "pages.module.airtransport.busairbilleditBean", scope = ManagedBeanScope.REQUEST)
public class BusairbilleditBean extends MastDtlView{
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;

	@SaveState
	@Accessible
	public BusAirBill selectedRowData = new BusAirBill();

	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	@SaveState
	@Accessible
	public BusAir busair = new BusAir();
	
	@SaveState
	@Accessible
	public Long jobid;

	@SaveState
	@Accessible
	public Long customerid;
	
	@SaveState
	@Accessible
	public String bltype;

	@SaveState
	@Accessible
	public String sql = "AND 1=1";

	@SaveState
	@Accessible
	public String portsql = "AND 1=1";
	
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
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			workItemId = (String) AppUtils.getReqParam("workItemId");
			jobid = Long.parseLong(AppUtils.getReqParam("jobid"));
			List<BusAir> bus = this.serviceContext.busAirMgrService.busAirDao.findAllByClauseWhere("jobid = "+jobid);
			busair = bus.get(0);
			this.userid = AppUtils.getUserSession().getUserid().toString();
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry = "\n t.linkid = " + getairid(this.jobid);
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void refresh() {
		//this.grid.reload();
	}
	
	public Long getairid(Long id) {
		String sql = "SELECT id FROM bus_air where isdelete =false AND jobid= "
				+ id;
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("id");
	}
	
	
	@Override
	public void addMaster(){
		this.selectedRowData = new BusAirBill();
		this.busair = serviceContext.busAirMgrService.busAirDao.findById(getairid(this.jobid));
		
		selectedRowData.setJobid(this.jobid);
		selectedRowData.setBltype(this.bltype);
		selectedRowData.setBusairid(getairid(this.jobid));
		selectedRowData.setBillcount("THREE(3)");
		selectedRowData.setPodid(this.busair.getPolid());
		selectedRowData.setPod(this.busair.getPod());
		selectedRowData.setPol(this.busair.getPol());
		selectedRowData.setPolid(this.busair.getPolid());
		selectedRowData.setCarrierid(this.busair.getCarrierid());
		if(this.busair.getUnit()!=null&&!this.busair.getUnit().equals("")){
			List<DatPackage> datPackage = serviceContext.packageMgrService.datPackageDao.findAllByClauseWhere("namee = '"+this.busair.getUnit()+"' AND isdelete = FALSE");
			if (datPackage!=null && datPackage.size() > 0){
				selectedRowData.setPackid(datPackage.get(0).getId());
			}
		}
		selectedRowData.setCneeid(this.busair.getCneeid());
		selectedRowData.setCneename(this.busair.getCneename());
		selectedRowData.setCneetitle(this.busair.getCneetitle());
		selectedRowData.setCnorid(this.busair.getCnorid());
		selectedRowData.setCnorname(this.busair.getCnorname());
		selectedRowData.setCnortitle(this.busair.getCnortitle());
		selectedRowData.setNotifyid(this.busair.getNotifyid());
		selectedRowData.setNotifyname(this.busair.getNotifyname());
		selectedRowData.setNotifytitle(this.busair.getNotifytitle());
		selectedRowData.setAgentdesid(this.busair.getAgentdesid());
		selectedRowData.setAgentdescode(this.busair.getAgentdescode());
		selectedRowData.setAgentdesabbr(this.busair.getAgentdesabbr());
		selectedRowData.setAgentid(this.busair.getAgentid());
		selectedRowData.setDestination(this.busair.getDestination());
		selectedRowData.setFlightdate1(this.busair.getFlightdate1());
		selectedRowData.setFlightno1(this.busair.getFlightno1());
		
		if ("H".equals(this.bltype)) {
			String sql = "SELECT chr(ascii(SUBSTRING(reverse(hawbno) FROM 0 FOR 2))+1) FROM bus_air_bill WHERE jobid = "+this.jobid+" AND bltype = 'H' AND isdelete = FALSE ORDER BY reverse(hawbno) DESC LIMIT 1";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map!=null&& map.containsKey("chr")&&map.get("chr")!=null){
					String chr = map.get("chr").toString();
					selectedRowData.setHawbno(this.busair.getHawbno()+chr);
				}else{
					selectedRowData.setHawbno(this.busair.getHawbno()+"A");
				}
			} catch (Exception e) {
				selectedRowData.setHawbno(this.busair.getHawbno()+"A");
			}
			selectedRowData.setMawbno(this.busair.getMawbno());
		} else if ("M".equals(this.bltype)) {
			String sql = "SELECT chr(ascii(SUBSTRING(reverse(mawbno) FROM 0 FOR 2))+1) FROM bus_air_bill WHERE jobid = "+this.jobid+" AND bltype = 'M' AND isdelete = FALSE ORDER BY reverse(mawbno) DESC LIMIT 1";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map!=null&& map.containsKey("chr")&&map.get("chr")!=null){
					String chr = map.get("chr").toString();
					selectedRowData.setMawbno(this.busair.getMawbno()+chr);
				}else{
					selectedRowData.setMawbno(this.busair.getMawbno()+"A");
				}
			} catch (Exception e) {
				selectedRowData.setMawbno(this.busair.getMawbno()+"A");
			}
			selectedRowData.setHawbno(this.busair.getHawbno());
		}
		
		this.mPkVal = -1l;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
		
	}
	
	

	@Override
	public void doServiceSaveMaster() {
		try {
			selectedRowData.setCnorname(AppUtils.replaceStringByRegEx(selectedRowData.getCnorname()));
			selectedRowData.setCnortitle(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitle()));
			selectedRowData.setCneename(AppUtils.replaceStringByRegEx(selectedRowData.getCneename()));
			selectedRowData.setCneetitle(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitle()));
			selectedRowData.setNotifyname(AppUtils.replaceStringByRegEx(selectedRowData.getNotifyname()));
			selectedRowData.setNotifytitle(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitle()));
			serviceContext.busAirBillMgrService.saveData(selectedRowData);
			this.mPkVal = this.selectedRowData.getId();
			refreshMasterForm();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}

	@Override
	public void init() {
		selectedRowData = new BusAirBill();
		String id = AppUtils.getReqParam("id");
		String jobid = AppUtils.getReqParam("jobid").trim();
		String bltype = AppUtils.getReqParam("bltype").trim();
		this.bltype = bltype;
		this.jobid = Long.valueOf(jobid);
		//this.job = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
		//this.customerid = job.getCustomerid();
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
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.busAirBillMgrService.busAirBillDao.findById(this.mPkVal);
		if(this.selectedRowData == null)return;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			try {
				serviceContext.busAirBillMgrService.removeDate(selectedRowData.getId());
				this.addMaster();
				refreshMasterForm();
				this.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Bind
	private String qryPortKey;
	
	@SaveState
	@Accessible
	public String porttype;
	
	@Bind
	public UIWindow showPortWindow;
	
	@Bind
	public UIDataGrid portGrid;
	
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
			portsql = "AND ispol = TRUE AND isship = FALSE";
		} else if ("1".equals(porttype)) {
			portsql = "AND ispod = TRUE AND isship = FALSE";
		} else if ("2".equals(porttype)) {
			portsql = "AND ispdd = TRUE AND isship = FALSE";
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

					//this.selectedRowData.setPddid((Long) cs.get(0).get("id"));

					//this.selectedRowData.setPdd((String) cs.get(0).get("namee"));
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
			this.selectedRowData.setPol((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "polid");
			this.update.markUpdate(UpdateLevel.Data, "pol");
		} else if ("1".equals(porttype)) {
			this.selectedRowData.setPodid((Long) m.get("id"));
			this.selectedRowData.setPod((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "podid");
			this.update.markUpdate(UpdateLevel.Data, "pod");
		} else if ("2".equals(porttype)) {
			this.update.markUpdate(UpdateLevel.Data, "pddid");
			this.update.markUpdate(UpdateLevel.Data, "pdd");
		}
		showPortWindow.close();
	}
	
	
	@Bind
	private String qryCustomerKey;
	
	@SaveState
	@Accessible
	public String custype;
	
	@Bind
	public UIWindow showCustomerWindow;
	
	@Bind
	public UIDataGrid customerGrid;
	
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}
	
	@SaveState
	@Accessible
	public String sqlMy = "";
	
	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		String sql2 = sql + sqlMy;
		return this.customerService.getCustomerDataProvider(sql2);
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
				// 第二通知人
			} else if ("3".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'N' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("notify2name.focus");
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
					Browser.execClientScript("notify2name.focus");
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
			this.update.markUpdate(UpdateLevel.Data, "notifyname");
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");
		} 
		showCustomerWindow.close();
	}
	
	
	@Bind(id = "billformat")
	public List<SelectItem> getBillformat() {
		try {
			billtype = "air";
			if(this.selectedRowData!=null && "M".equals(this.selectedRowData.getBltype())){
				billtype = "airmbl";
			}
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='"+billtype+"' AND (userid IS NULL OR ispublic OR userid = "+AppUtils.getUserSession().getUserid()+")",
					"AND isdelete = false order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
}
