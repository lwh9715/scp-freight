package com.scp.view.module.train;

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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusTrain;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;
@ManagedBean(name = "pages.module.train.bookingSpaceBean", scope = ManagedBeanScope.REQUEST)
public class BookingSpaceBean extends MastDtlView{
	
	@SaveState
	@Accessible
	public BusTrain selectedRowData = new BusTrain();
	
	@SaveState
	@Accessible
	public String porttype;
	
	@SaveState
	@Accessible
	public String portsql = "AND 1=1";
	
	@Bind
	public UIWindow showPortWindow;
	
	@Bind
	public UIIFrame busFreightchargeIFrame;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			refreshMasterForm();
			super.applyGridUserDef();
		}
	}
	
	@Action
	public void showPortAction() {
		String portcode = (String) AppUtils.getReqParam("portcode");
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
		}
		if ("0".equals(porttype)) {
			Browser.execClientScript("polButton.focus");
		} else if ("1".equals(porttype)) {
			Browser.execClientScript("podButton.focus");
		} else if ("2".equals(porttype)) {
			Browser.execClientScript("pddButton.focus");
		}
	}
	
	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;
	
	@Bind
	private String qryPortKey;
	
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
	public void portGrid_ondblclick() {
		Object[] objs = portGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if ("0".equals(porttype)) {
			this.selectedRowData.setMblpol((String) m.get("namee"));
			this.selectedRowData.setMblpolcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "pol");
			this.update.markUpdate(UpdateLevel.Data, "polcode");
		} else if ("1".equals(porttype)) {
			this.selectedRowData.setMblpod((String) m.get("namee"));
			this.selectedRowData.setMblpodcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "pod");
			this.update.markUpdate(UpdateLevel.Data, "podcode");
		} else if ("2".equals(porttype)) {
			this.selectedRowData.setMblpdd((String) m.get("namee"));
			this.selectedRowData.setMblpddcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "pdd");
			this.update.markUpdate(UpdateLevel.Data, "pddcode");
		}
		showPortWindow.close();
	}
	
	@Override
	public void doServiceSaveMaster() {
		try {
			this.pkVal = selectedRowData.getId();
			selectedRowData.setCnorname(AppUtils.replaceStringByRegEx(selectedRowData.getCnorname()));
			selectedRowData.setCnortitle(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitle()));
			selectedRowData.setCneename(AppUtils.replaceStringByRegEx(selectedRowData.getCneename()));
			selectedRowData.setCneetitle(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitle()));
			selectedRowData.setNotifyname(AppUtils.replaceStringByRegEx(selectedRowData.getNotifyname()));
			selectedRowData.setNotifytitle(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitle()));
			selectedRowData.setAgenname(AppUtils.replaceStringByRegEx(selectedRowData.getAgenname()));
			selectedRowData.setAgentitle(AppUtils.replaceStringByRegEx(selectedRowData.getAgentitle()));
			selectedRowData.setClaimPre(AppUtils.replaceStringByRegEx(selectedRowData.getClaimPre()));
			selectedRowData.setClaimTruck(AppUtils.replaceStringByRegEx(selectedRowData.getClaimTruck()));
			selectedRowData.setClaimBill(AppUtils.replaceStringByRegEx(selectedRowData.getClaimBill()));
			selectedRowData.setClaimClear(AppUtils.replaceStringByRegEx(selectedRowData.getClaimClear()));
			selectedRowData.setFactoryinfo(AppUtils.replaceStringByRegEx(selectedRowData.getFactoryinfo()));
			selectedRowData.setRemark1(AppUtils.replaceStringByRegEx(selectedRowData.getRemark1()));
			selectedRowData.setRemark2(AppUtils.replaceStringByRegEx(selectedRowData.getRemark2()));
			selectedRowData.setRemark3(AppUtils.replaceStringByRegEx(selectedRowData.getRemark3()));
			selectedRowData.setRemark4(AppUtils.replaceStringByRegEx(selectedRowData.getRemark4()));
			selectedRowData.setRemark5(AppUtils.replaceStringByRegEx(selectedRowData.getRemark5()));
			selectedRowData.setCnortitlemblname(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitlemblname()));
			selectedRowData.setCnortitlembl(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitlembl()));
			selectedRowData.setCneetitlemblname(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitlemblname()));
			selectedRowData.setCneetitlembl(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitlembl()));
			selectedRowData.setNotifytitlemblname(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitlemblname()));
			selectedRowData.setNotifytitlembl(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitlembl()));
			selectedRowData.setMarksnombl(AppUtils.replaceStringByRegEx(selectedRowData.getMarksnombl()));
			selectedRowData.setGoodsdescmbl(AppUtils.replaceStringByRegEx(selectedRowData.getGoodsdescmbl()));
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}

	@SaveState
	@Accessible
	@Bind
	public Long customerid;
	
	@Bind
	@SaveState
	public String busfreightcharge;

	@Override
	public void init() {
		String id = AppUtils.getReqParam("id").trim();
		if (!StrUtils.isNull(id)){
			jobid = Long.parseLong(id);
		}else{
			jobid = -1L;
		}
		this.job = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
		this.customerid = job.getCustomerid();
		busFreightchargeIFrame.setSrc("/scp/pages/module/ship/busfreightcharge.xhtml?jobid=" + this.jobid);
		update.markAttributeUpdate(busFreightchargeIFrame, "src");
		try {
			String sql = "SELECT string_agg(b.freightcharge||' - '||b.collect,f_newline()) AS freightcharge FROM bus_freightcharge b WHERE b.jobid = "+this.jobid+"";
			Map m = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String freightcharge = StrUtils.getMapVal(m, "freightcharge");
			busfreightcharge = freightcharge;
			this.update.markUpdate(UpdateLevel.Data, "busfreightcharge");
		}  catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	@SaveState
	@Accessible
	@Bind
	public Long jobid;

	@Override
	public void refreshMasterForm() {
		try {
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao
					.findOneRowByClauseWhere(sql);
		} catch (MoreThanOneRowException e) {
			selectedRowData = new BusTrain();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			this.mPkVal = this.selectedRowData.getId();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		}
		
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
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
	
	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider(sql);
	}
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@Bind
	private String qryCustomerKey;
	
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}
	
	@Action
	public void showCustomerMBL(){
		custypeMBL = (String) AppUtils.getReqParam("custypeMBL");
		//MBL发货人
		if ("1".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'O' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))";
			Browser.execClientScript("cnortitlemblname.focus");
		//MBL收货人
		}else if("2".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'P' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))";
			Browser.execClientScript("cneetitlemblname.focus");
		}//MBL通知人
		else if ("3".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))";
			Browser.execClientScript("notifytitlemblname.focus");
		} //MBL代理
		else if ("4".equals(custypeMBL)) {
			sql = " AND contactype2 = 'A'";
			Browser.execClientScript("agennamembl.focus");
		} 
		showCustomerWindow.show();
		this.customerGrid.reload();
	}
	
	@SaveState
	@Accessible
	public String custype;
	
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
			
			this.selectedRowData.setCneetitlembl((String) m.get("contactxt"));
			
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
			this.update.markUpdate(UpdateLevel.Data, "agenid");
			this.update.markUpdate(UpdateLevel.Data, "agenname");
			this.update.markUpdate(UpdateLevel.Data, "agentitle");

		}
		if("1".equals(custypeMBL)){
			this.selectedRowData.setCnortitlemblid((Long) m.get("id"));
			this.selectedRowData.setCnortitlemblname((String) m.get("name"));
			this.selectedRowData.setCnortitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cnortitlemblid");
			this.update.markUpdate(UpdateLevel.Data, "cnortitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "cnortitlembl");
		}
		else if("2".equals(custypeMBL)){
			this.selectedRowData.setCneetitlembid((Long) m.get("id"));
			this.selectedRowData.setCneetitlemblname((String) m.get("name"));
			this.selectedRowData.setCneetitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembid");
			this.update.markUpdate(UpdateLevel.Data, "cneetitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
		}
		else if("3".equals(custypeMBL)){
			this.selectedRowData.setNotifytitlemblid((Long) m.get("id"));
			this.selectedRowData.setNotifytitlemblname((String) m.get("name"));
			this.selectedRowData.setNotifytitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifytitlemblid");
			this.update.markUpdate(UpdateLevel.Data, "notifytitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "notifytitlembl");
		}
		else if("4".equals(custypeMBL)){
			this.selectedRowData.setAgenidmbl((Long) m.get("id"));
			this.selectedRowData.setAgennamembl((String) m.get("name"));
			this.selectedRowData.setAgentitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "agenidmbl");
			this.update.markUpdate(UpdateLevel.Data, "agennamembl");
			this.update.markUpdate(UpdateLevel.Data, "agentitlembl");
		}
		showCustomerWindow.close();
	}
	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	@SaveState
	@Accessible
	public FinaJobs job = new FinaJobs();

	
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
				if(type.equals("4")){
					try{
						String sql = "  contactype2 = 'O' AND isdelete = FALSE AND  name = '" + cnename+"'";
						//System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}catch (Exception e){
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("O");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-O-" + getCusCode("4")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCnortitlemblid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCnortitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");
					}
				}
				else if (type.equals("5")) {
					// MBL收货人
					try{
						String sql = "  contactype2 = 'P' AND isdelete = FALSE AND  name = '" + cnename+"'";
						//System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}catch (Exception e){
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("P");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-P-" + getCusCode("5")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCneetitlembid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCneetitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");
					}
			}else if (type.equals("6")) {
				try{
					String sql = "  contactype2 = 'Q' AND isdelete = FALSE AND  name = '" + cnename+"'";
					//System.out.println(sql);
					List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
					sysCorp = SysCorpcontactss.get(0);
					sysCorp.setName(cnename);
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerabbr(this.job.getCustomerabbr());
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");

				}catch (Exception e){
					sysCorp = new SysCorpcontacts();
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerid(this.customerid);
					sysCorp.setContactype("B");
					sysCorp.setContactype2("Q");
					String code = (StrUtils.isNull(cnename) ? (this.job
							.getCustomerabbr()
							+ "-Q-" + getCusCode("6")) : cnename);
					sysCorp.setName(StrUtils.isNull(cnename) ? code
							: cnename);
					sysCorp.setCustomerabbr(this.job.getCustomerabbr());
					sysCorp.setId(0);
					sysCorp.setSex("M");
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");
					selectedRowData.setNotifytitlemblid(Long
							.valueOf(getCusdesc(code)[0]));
					selectedRowData.setNotifytitlemblname(code);
					update.markUpdate(true, UpdateLevel.Data,
							"masterEditPanel");
				}

			}
			else if (type.equals("7")) {
				try{
					String sql = "  contactype2 = 'A' AND isdelete = FALSE AND  name = '" + cnename+"'";
					List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
					sysCorp = SysCorpcontactss.get(0);
					sysCorp.setName(cnename);
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerabbr(this.job.getCustomerabbr());
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");

				}catch (Exception e){
					sysCorp = new SysCorpcontacts();
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerid(this.customerid);
					sysCorp.setContactype("B");
					sysCorp.setContactype2("A");
					String code = (StrUtils.isNull(cnename) ? (this.job
							.getCustomerabbr()
							+ "-Q-" + getCusCode("2")) : cnename);
					sysCorp.setName(StrUtils.isNull(cnename) ? code
							: cnename);
					sysCorp.setCustomerabbr(this.job.getCustomerabbr());
					sysCorp.setId(0);
					sysCorp.setSex("M");
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");
					selectedRowData.setAgenidmbl(Long
							.valueOf(getCusdesc(code)[0]));
					selectedRowData.setAgennamembl(code);
					update.markUpdate(true, UpdateLevel.Data,
							"masterEditPanel");
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
		}else if (type.equals("4")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'O' AND customerid = "
				+ this.customerid;
		}else if (type.equals("5")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'P' AND customerid = "
				+ this.customerid;
		}else if (type.equals("6")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'Q' AND customerid = "
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
	public void addhblagent1(){
		this.selectedRowData.setCneetitlembl(this.selectedRowData.getAgentitle());
		update.markUpdate(true, UpdateLevel.Data, "cneetitlembl");
	}
	@Action
	public void addhblagent(){
		this.selectedRowData.setNotifytitlembl(this.selectedRowData.getAgentitle());
		update.markUpdate(true, UpdateLevel.Data, "notifytitlembl");
	}
	
	@Action
	public void addhblagent2(){
		this.selectedRowData.setAgentitlembl(this.selectedRowData.getAgentitle());
		update.markUpdate(true, UpdateLevel.Data, "agentitlembl");
	}

	@Action
	public void refreshs() {
		this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao.findById(mPkVal);
		this.update.markUpdate(true,UpdateLevel.Data,"masterEditPanel");
		this.alert("OK");
	}
	
	@SaveState
	public String type;
	
	@Bind
	@SaveState
	public String detailsContent;
	
	@Bind
	public UIWindow detailsWindow;
	
	@Bind
	@SaveState
	public String detailsTitle;
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");
		if("11".equals(type)){
			this.detailsContent = content;
			this.detailsTitle = "发货人";
		} else if("12".equals(type)){
			this.detailsContent = content;
			this.detailsTitle = "收货人";
		} else if("13".equals(type)){
			this.detailsContent = content;
			this.detailsTitle = "通知人";
		} else if("9".equals(type)){
			this.detailsContent = content;
			this.detailsTitle = "标记与号码";
		} else if("10".equals(type)){
			this.detailsContent = content;
			this.detailsTitle = "件数包装品名";
		} else if("14".equals(type)){
			this.detailsContent = content;
			this.detailsTitle = "MBL代理";
		}
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
		this.detailsWindow.show();
		Browser.execClientScript("coutRowLength();");
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
	
	public void setDetails(String type) {
		if("11".equals(type)){
			this.selectedRowData.setCnortitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cnortitlembl");
		} else if("12".equals(type)){
			this.selectedRowData.setCneetitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
		} else if("13".equals(type)){
			this.selectedRowData.setNotifytitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifytitlembl");
		}else if("9".equals(type)){
			this.selectedRowData.setMarksnombl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "marksnombl");
		} else if("10".equals(type)){
			this.selectedRowData.setGoodsdescmbl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "goodsdescmbl");
		}
	}
	
	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.type);
		this.detailsWindow.close();
	}
	
	@Action
	public void pickup() {
		BusTrain bs = serviceContext.busTrainMgrService.findByjobId(this.jobid);
		
		this.selectedRowData.setMarksnombl(bs.getMarksno());
		this.selectedRowData.setGoodsdescmbl(bs.getGoodsdesc());
		
		
		this.selectedRowData.setMblpolcode(bs.getPolcode());
		Browser.execClientScript("$('#pol_input').val('"+bs.getPolcode()+"');;");
		this.selectedRowData.setMblpol(bs.getPol());
		
		this.selectedRowData.setMblpodcode(bs.getPodcode());
		Browser.execClientScript("$('#pod_input').val('"+bs.getPodcode()+"');;");
		this.selectedRowData.setMblpod(bs.getPod());
		
		this.selectedRowData.setMblpddcode(bs.getPddcode());
		Browser.execClientScript("$('#pdd_input').val('"+bs.getPddcode()+"');;");
		this.selectedRowData.setMblpdd(bs.getPdd());
		
		this.selectedRowData.setMbldestinationcode(bs.getDestinationcode());
		Browser.execClientScript("$('#destination_input').val('"+bs.getDestinationcode()+"');;");
		this.selectedRowData.setMbldestination(bs.getDestination());
		
		this.selectedRowData.setVesselmbl(bs.getVessel());
		this.selectedRowData.setVoyagembl(bs.getVoyage());
		this.selectedRowData.setSignplacembl(bs.getSignplace());
		this.selectedRowData.setFreightitemmbl(bs.getFreightitem());
		this.selectedRowData.setCarrieridmbl(bs.getCarrierid());
		this.selectedRowData.setCarryitemmbl(bs.getCarryitem());
		this.selectedRowData.setEtdmbl(bs.getEtd());
		this.selectedRowData.setEtambl(bs.getEta());
		
		this.selectedRowData.setMblnombl(bs.getMblno());
		this.selectedRowData.setVescodembl(bs.getVescode());
		
		
		
		this.update.markUpdate(UpdateLevel.Data, "goodsdescmbl");
		this.update.markUpdate(UpdateLevel.Data, "marksnombl");
	}
	
}
