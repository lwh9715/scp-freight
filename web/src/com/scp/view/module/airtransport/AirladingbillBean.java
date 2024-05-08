package com.scp.view.module.airtransport;

import java.math.BigDecimal;
import java.util.List;
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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.NoRowException;
import com.scp.model.bus.BusAir;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.AgentChooseService;
@ManagedBean(name = "pages.module.airtransport.airladingbillBean", scope = ManagedBeanScope.REQUEST)
public class AirladingbillBean extends MastDtlView {
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@SaveState
	@Accessible
	public BusAir selectedRowData = new BusAir();
	
	@Bind
	@SaveState
	public String automatic;
	
	@Bind
	@SaveState
	public BigDecimal automaticval;
	
	@SaveState
	@Accessible
	public FinaJobs finajobs = new FinaJobs();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			refreshMasterForm();
			super.applyGridUserDef();
		}
	}
	
	
	@Override
	public void init(){
		String id = AppUtils.getReqParam("id").trim();
		if (!StrUtils.isNull(id) && StrUtils.isNumber(id)){
			this.pkVal = Long.parseLong(id);
			this.selectedRowData = this.serviceContext.busAirMgrService.busAirDao.findById(Long.parseLong(id));
			this.finajobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.parseLong(id));
		}
		
		try{
			automatic = ConfigUtils.findSysCfgVal("bus_air_automatic_calculation");
			automaticval = new BigDecimal(ConfigUtils.findSysCfgVal("bus_air_automatic_calculation_val"));
		}catch(Exception e){
			automatic = "Y";
			automaticval = new BigDecimal(166.7);
			e.printStackTrace();
		}
	}


	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Action
	public void saveMaster() {
		try {
			if (this.pkVal > 0) {
				this.serviceContext.busAirMgrService.busAirDao.modify(this.selectedRowData);
			} else {
				this.serviceContext.busAirMgrService.busAirDao.create(this.selectedRowData);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		
		this.refreshMasterForm();
	}
	
	
	
	@SaveState
	public String type;
	
	@Bind
	@SaveState
	public String detailsContent;
	
	@Bind
	@SaveState
	public String detailsTitle;
	
	@Bind
	public UIWindow detailsWindow;
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");
		

		if ("1".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "发货人";

		} else if ("2".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "收货人";

		} else if ("3".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "通知人";

		} else if ("4".equals(type)) { 
			this.detailsContent = content;
			this.detailsTitle = "第二通知人";

		} else if ("5".equals(type)) {
			this.detailsContent = content;
			this.detailsTitle = "目的港代理";
		}
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
		
		this.detailsWindow.show();
		Browser.execClientScript("coutRowLength();");
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
		if ("1".equals(type)) { //发货人
			this.selectedRowData.setCnortitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");

		} else if ("2".equals(type)) { //收货人
			this.selectedRowData.setCneetitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cneetitle");

		} else if ("3".equals(type)) { //通知人
			this.selectedRowData.setNotifytitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");

		} else if ("4".equals(type)) { //第二通知人
			this.selectedRowData.setNotify2title(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notify2title");

		} else if ("5".equals(type)) { //目的港代理
			this.selectedRowData.setAgentdesabbr(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "agentdesabbr");
		}
	}
	
	/**
	 * 输入框(大框)保存
	 */
	@Action
	public void saveDetails() {
		setDetails(this.type);
		this.saveMaster();
		this.detailsWindow.close();
	}
	
	
	@SaveState
	@Accessible
	@Bind
	public Long customerid;
	
	
	@SaveState
	@Accessible
	public String custypeMBL;
	
	@SaveState
	@Accessible
	public String sql = "AND 1=1";
	
	@Bind
	public UIWindow showCustomerWindow;
	
	@Bind
	public UIDataGrid customerGrid;
	
	
	@Bind
	private String qryCustomerKey;
	
	@SaveState
	@Accessible
	public String custype;
	
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
				sql = "\nAND contactype = 'B' AND (contactype2 = 'C' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("cneename.focus");

				// 发货人
			} else if ("1".equals(custype)) {
				sql = "\nAND contactype = 'B' AND (contactype2 = 'S' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (ishipper = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("cnorname.focus");
				// 通知人
			} else if ("2".equals(custype)) {
				sql = "\nAND contactype = 'B' AND (contactype2 = 'N' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)";
				Browser.execClientScript("notifyname.focus");
				// 第二通知人
			} else if ("3".equals(custype)) {
				sql = "\nAND contactype = 'B' AND (contactype2 = 'N' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
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
	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	@SaveState
	@Accessible
	public SysCorporation syspor = new SysCorporation();
	
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
						String code = (StrUtils.isNull(cnename) ? (this.finajobs
								.getCustomerabbr()
								+ "-C-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCneeid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCneename(code);
						update.markUpdate(true, UpdateLevel.Data,
								"centerEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
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
						String code = (StrUtils.isNull(cnename) ? (this.finajobs
								.getCustomerabbr()
								+ "-S-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCnorid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCnorname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"centerEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}
				} 

					// 通知人
				else if (type.equals("3")) {

					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("N");
						String code = (StrUtils.isNull(cnename) ? (this.finajobs
								.getCustomerabbr()
								+ "-N-" + getCusCode("3")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotifyid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotifyname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"centerEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}
				//第二通知人
				}else if (type.equals("4")) {

					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("N");
						String code = (StrUtils.isNull(cnename) ? (this.finajobs
								.getCustomerabbr()
								+ "-N-" + getCusCode("3")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotify2id(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotify2name(code);
						update.markUpdate(true, UpdateLevel.Data,
								"centerEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}
				}else if (type.equals("5")) {
					// MBL收货人
					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("P");
						String code = (StrUtils.isNull(cnename) ? (this.finajobs
								.getCustomerabbr()
								+ "-P-" + getCusCode("5")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCneeid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCneename(code);
						update.markUpdate(true, UpdateLevel.Data,
								"centerEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}
			}else if (type.equals("6")) {

				if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
					sysCorp = new SysCorpcontacts();
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerid(this.customerid);
					sysCorp.setContactype("B");
					sysCorp.setContactype2("Q");
					String code = (StrUtils.isNull(cnename) ? (this.finajobs
							.getCustomerabbr()
							+ "-Q-" + getCusCode("6")) : cnename);
					sysCorp.setName(StrUtils.isNull(cnename) ? code
							: cnename);
					sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
					sysCorp.setId(0);
					sysCorp.setSex("M");
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");
					selectedRowData.setNotifyid(Long
							.valueOf(getCusdesc(code)[0]));
					selectedRowData.setNotifyname(code);
					update.markUpdate(true, UpdateLevel.Data,
							"centerEditPanel");

				} else if (!StrUtils.isNull(cneid)
						&& !StrUtils.isNull(cnetitle)) {
					sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
							.findById(Long.valueOf(cneid));
					sysCorp.setName(cnename);
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerabbr(this.finajobs.getCustomerabbr());
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
		if(this.pkVal >0){
			this.serviceContext.busAirMgrService.busAirDao.modify(this.selectedRowData);
		}else{
			this.serviceContext.busAirMgrService.busAirDao.create(this.selectedRowData);
			
		}
		this.refreshMasterForm();
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
		String sql = "SELECT name,id FROM sys_corpcontacts WHERE  name ='"+code+"' AND customerid = "+this.customerid+" LIMIT 1;";
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			re[0] = ((Long) m.get("id")).toString();
			re[1] = (String) m.get("name");
		} catch (NoRowException e) {
			re[0] = "0";
			re[1] = "";
		}
		return re;
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
			this.selectedRowData.setNotify2id((Long) m.get("id"));
			this.selectedRowData.setNotify2name((String) m.get("name"));
			this.selectedRowData.setNotify2title((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notify2name");
			this.update.markUpdate(UpdateLevel.Data, "notify2title");
			this.update.markUpdate(UpdateLevel.Data, "notify2id");

		}else if ("5".equals(custype)) {
			this.selectedRowData.setNotify2id((Long) m.get("id"));
			this.selectedRowData.setMblcnorname((String) m.get("name"));
			this.selectedRowData.setMblcnortitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "mblcnorname");
			this.update.markUpdate(UpdateLevel.Data, "mblcnortitle");
			this.update.markUpdate(UpdateLevel.Data, "notify2id");

		}else if ("6".equals(custype)) {
			this.selectedRowData.setNotify2id((Long) m.get("id"));
			this.selectedRowData.setMblcneename((String) m.get("name"));
			this.selectedRowData.setMblcneetitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "mblcneename");
			this.update.markUpdate(UpdateLevel.Data, "mblcneetitle");
			this.update.markUpdate(UpdateLevel.Data, "notify2id");

		}else if ("7".equals(custype)) {
			this.selectedRowData.setNotify2id((Long) m.get("id"));
			this.selectedRowData.setMblnotifyname((String) m.get("name"));
			this.selectedRowData.setMblnotifytitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "mblnotifyname");
			this.update.markUpdate(UpdateLevel.Data, "mblnotifytitle");
			this.update.markUpdate(UpdateLevel.Data, "notify2id");

		}
		showCustomerWindow.close();
	}
	
	
	
	
	@Bind
	private String qryAgentKey;
	
	@Bind
	public UIWindow showAgentWindow;
	
	@Bind
	public UIDataGrid agentGrid;
	
	@ManagedProperty("#{agentchooseserviceBean}")
	private AgentChooseService agentchooseserviceBean;
	
	@Action
	public void agentQry() {
		this.agentchooseserviceBean.qryPort(qryAgentKey);		
		this.agentGrid.reload();
	}
	
	@Bind(id = "agentGrid", attribute = "dataProvider")
	public GridDataProvider getagentGridDataProvider() {
		return this.agentchooseserviceBean.getPortDataProvider();
	}
	
	@Action
	public void showAgentAction(){
		String code = (String) AppUtils.getReqParam("customercode");
		qryAgentKey = code;
		this.update.markUpdate(UpdateLevel.Data, "qryAgentKey");
		showAgentWindow.show();
		agentQry();
		Browser.execClientScript("agentdescode.focus");
		
		try {
			List<Map> cs = agentchooseserviceBean.findPort(qryAgentKey);
			if (cs.size() == 1) {
					this.selectedRowData.setAgentdesid((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "agentdesid");
					this.update.markUpdate(UpdateLevel.Data, "agentdescode");
					this.update.markUpdate(UpdateLevel.Data, "agentdesabbr");
					//showAgentWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryAgentKey");
				showAgentWindow.show();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
	@Action
	public void agentGrid_ondblclick() {
		Object[] objs = agentGrid.getSelectedValues();
		Map m = (Map) objs[0];
			this.selectedRowData.setAgentdesid((Long) m.get("id"));
			this.selectedRowData.setAgentdescode((String) m.get("customercode"));
			this.selectedRowData.setAgentdesabbr((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "agentdesid");
			this.update.markUpdate(UpdateLevel.Data, "agentdescode");
			this.update.markUpdate(UpdateLevel.Data, "agentdesabbr");
			showAgentWindow.close();
	}
	
	
	
	@Action
	public void saveagent() {
		String agentid = AppUtils.getReqParam("agentid").trim();
		String agentcode = AppUtils.getReqParam("agentcode").trim();
		String agentabbr = AppUtils.getReqParam("agentabbr").trim();
		try {
			if (StrUtils.isNull(agentid) && StrUtils.isNull(agentabbr)) {
				MessageUtils.alert("请输入正确信息");
			}else{
				syspor = serviceContext.customerMgrService.sysCorporationDao
				.findById(Long.valueOf(agentid));
				syspor.setCode(agentcode);
				syspor.setAddressc(agentabbr);
				serviceContext.customerMgrService
				.saveData(syspor);
				MessageUtils.alert("OK");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		if(this.pkVal >0){
			this.serviceContext.busAirMgrService.busAirDao.modify(this.selectedRowData);
		}else{
			this.serviceContext.busAirMgrService.busAirDao.create(this.selectedRowData);
		}
		this.refreshMasterForm();
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry = "\n t.linkid = " + this.pkVal;
		m.put("qry", qry);
		return m;
	}
	
}
