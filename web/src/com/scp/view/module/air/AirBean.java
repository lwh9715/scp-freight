package com.scp.view.module.air;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import com.scp.base.ConstantBean.Module;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusAir;
import com.scp.model.bus.BusGoods;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.AgentChooseService;
import com.scp.view.module.data.PortChooseService;
import com.ufms.web.view.sysmgr.LogBean;

@ManagedBean(name = "pages.module.air.airBean", scope = ManagedBeanScope.REQUEST)
public class AirBean extends EditGridFormView {
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@SaveState
	@Accessible
	public BusAir selectedRowData = new BusAir();
	
	@SaveState
	@Accessible
	public BusGoods dtlData = new BusGoods();

	@SaveState
	@Accessible
	public FinaJobs finajobs = new FinaJobs();
	
	@Bind
	public Boolean ishold = false;
	
	@Bind
	public Boolean isput = false;
	
	@SaveState
	public String customcompany="";
	
	@Bind
	@SaveState
	public String isSonjob = "0";
	
	@SaveState
	public String agentcompay="";
	
	@Bind
	@SaveState
	public String automatic;
	
	@Bind
	@SaveState
	public BigDecimal automaticval;
	
	@SaveState
	@Accessible
	public Long jobid;
	
	@Action
	public void isput_oncheck(){
		boolean put = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_air.getValue())) {
			if (s.endsWith("_isput")) {
				put = true;
			} 
		}
		if(put){
			selectedRowData.setIsput(isput);
			selectedRowData.setPuter(isput?AppUtils.getUserSession().getUsercode():null);
			selectedRowData.setPuttime(isput?Calendar.getInstance().getTime():null);
			update.markUpdate(true, UpdateLevel.Data, "isput");
			update.markUpdate(true, UpdateLevel.Data, "puter");
		}else{
			MessageUtils.alert("无放货权限!");
			isput = !isput;
			selectedRowData.setIsput(isput);
			update.markUpdate(true, UpdateLevel.Data, "isput");
			return;
		}
		try {
			serviceContext.busAirMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isput = isput ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "isput");
		}
	}
	
	@Action
	public void ishold_oncheck(){
		boolean hold = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_air.getValue())) {
			if (s.endsWith("_ishold")) {
				hold = true;
			} 
		}
		if(hold){
			selectedRowData.setIshold(ishold);
			selectedRowData.setHolder(ishold?AppUtils.getUserSession().getUsercode():null);
			selectedRowData.setHoldtime(ishold?Calendar.getInstance().getTime():null);
			update.markUpdate(true, UpdateLevel.Data, "ishode");
			update.markUpdate(true, UpdateLevel.Data, "holder");
		}else{
			MessageUtils.alert("无扣货权限!");
			ishold = !ishold;
			selectedRowData.setIshold(ishold);
			update.markUpdate(true, UpdateLevel.Data, "ishode");
			return;
		}
		try {
			serviceContext.busAirMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			ishold = ishold ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "ishold");
		}
	}
	
	@Bind
	public UIWindow showSFTWindow;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			super.applyGridUserDef();
		}
	}
	
	/**
	 * 关闭,审核,完成判断处理
	 */
	@Action
	public void commonCheck() {
//		Boolean isComplete = selectedRowData.getIscomplete();
//		if (isComplete) {
//			this.disableAllButton(true);
//		} else {
//			this.disableAllButton(false);
//			}
	}
	
	/**
	 * CC终审流程
	 */

	public void affirmLast() {
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
	}

	public void init() {
		String jobid = AppUtils.getReqParam("id");
		this.jobid = Long.valueOf(jobid);
		if(jobid==null || jobid.isEmpty()){
			this.pkVal = -1L;
		}else{
			try {
				this.finajobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid));
				this.selectedRowData = this.serviceContext.busAirMgrService.findByJobId(Long.valueOf(jobid));
				this.pkVal = selectedRowData.getId();
				//查询此单有没有子单
				String sql = "SELECT EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = "+selectedRowData.getJobid()+") AS isSonjob";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String s = m.get("issonjob").toString();
				if(s.equals("true")){
					isSonjob = "1";
				}else{
					isSonjob = "0";
				}

				if (this.finajobs != null && this.finajobs.getIscheck()) {
					Browser.execClientScript("ischeckedited();");
				}
			} catch (NumberFormatException e) {
				this.pkVal = -1L;
			}
		}
		this.refreshMasterForm();
		//processInstanceId =(String)AppUtils.getReqParam("processInstanceId");
		// workItemId =(String)AppUtils.getReqParam("workItemId");
		try{
			automatic = ConfigUtils.findSysCfgVal("bus_air_automatic_calculation");
			automaticval = new BigDecimal(ConfigUtils.findSysCfgVal("bus_air_automatic_calculation_val"));
		}catch(Exception e){
			automatic = "Y";
			automaticval = new BigDecimal(167);
			e.printStackTrace();
		}
	}

	@Action
	public void refreshMasterForm() {
		try {
			
			this.selectedRowData = this.serviceContext.busAirMgrService.busAirDao.findById(this.pkVal);
			this.customerid = finajobs.getCustomerid();
			this.pkVal = this.selectedRowData.getId();
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			if(selectedRowData.getCustomid()!=null&&this.selectedRowData.getCustomid()>0){
				SysCorporation sysCorporation = 
					serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getCustomid());
				String namee = sysCorporation.getNamee();
				this.customcompany=(namee!=null&&!namee.equals("")?namee:sysCorporation.getCode());
			}
			if(StrUtils.isNull(this.selectedRowData.getTransamt()))this.selectedRowData.setTransamt("N.V.D");
			if(StrUtils.isNull(this.selectedRowData.getCustomeamt()))this.selectedRowData.setCustomeamt("N.V.C");
			if(StrUtils.isNull(this.selectedRowData.getInsuranceamt()))this.selectedRowData.setInsuranceamt("NIL");
		} catch (Exception e) {
			e.printStackTrace();
			this.addMaster();
		} finally {
			this.editGrid.reload();
		}
		update.markUpdate(UpdateLevel.Data, "centerEditPanel");
		update.markUpdate(UpdateLevel.Data, "pkVal");
		update.markUpdate(UpdateLevel.Data, "pkVal");
		
		this.refresh();
		this.editGrid.setSelections(getGridSelIds());
		if(selectedRowData.getAgentid()!=null&&this.selectedRowData.getAgentid()>0){
			SysCorporation sysCorporation = 
				serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getAgentid());
			String abbr = sysCorporation.getAbbr();
			this.agentcompay=(abbr!=null&&!abbr.equals("")?abbr:sysCorporation.getNamec());
		}
	}

	public void doServiceSaveMaster() {
		try {
			this.refreshMasterForm();
			this.alert("ok");
		} catch (NoRowException e) {
			this.refreshMasterForm();
			this.alert("ok");
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

	public void addMaster() {
		this.selectedRowData = new BusAir();
		this.pkVal = -1L;
		this.selectedRowData.setJobid(this.pkVal);
		update.markUpdate(UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(UpdateLevel.Data, "centerEditPanel");
	}

	@Action
	public void saveMaster() {
		try {
			if (this.pkVal > 0) {
				this.serviceContext.busAirMgrService.busAirDao.modify(this.selectedRowData);
			} else {
				this.serviceContext.busAirMgrService.busAirDao.create(this.selectedRowData);
			}
			saveContainer();
			Browser.execClientScript("showmsg()");
			Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
			this.refreshMasterForm();
		} catch (Exception e) {
			String exceptionmessage = e.getMessage();
			LogBean.insertLog(new StringBuffer().append("空运委托保存失败,saveMaster失败原因为").append(e.getMessage()));
			if (exceptionmessage.contains("java.lang.RuntimeException")) {
				exceptionmessage = exceptionmessage.replaceAll("java.lang.RuntimeException:", "");
				Browser.execClientScript("layer.msg('" + exceptionmessage + "',{offset:['20%','40%'],time:5000,type: 1});");
			} else {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void saveContainer() {
		this.editGrid.commit();
		if (modifiedData != null) {
			if (finajobs.getIscheck()) {
				alert("审核后，柜子不允许修改!");
				return;
//				throw new RuntimeException("审核后，柜子不允许修改!");
			}

		    update(modifiedData);
		}
		if (addedData != null) {
		    add(addedData);
		}
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busGoodsMgrService.updateBatchEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busGoodsMgrService.addBatchEditGrid(addedData,this.pkVal);
	}
	
	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busGoodsMgrService.busGoodsDao
		.findById(this.pkValCnt);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busGoodsMgrService.saveData(dtlData);
		alert("OK");
	}

	@Action
	public void showSFT() {
		showSFTWindow.show();
	}
	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	@SaveState
	@Accessible
	public SysCorporation syspor = new SysCorporation();
	
	@SaveState
	@Accessible
	@Bind
	public Long customerid;
	
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
	
	@Action
	public void showCustomerMBL(){
		custypeMBL = (String) AppUtils.getReqParam("custypeMBL");
		String type = (String) AppUtils.getReqParam("type");
		custype = (String) AppUtils.getReqParam("custype");
		//MBL发货人
		if ("1".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'O' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))"+
			//1947 系统设置增加，收发通不按委托人提取
			"\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = " + this.customerid+" OR (ishipper = TRUE AND a.salesid IS NULL)) END)"
			+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=S;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";
			//Browser.execClientScript("cnortitlemblname.focus");
		//MBL收货人
		}else if("2".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))" +
					" OR (contactype2 = 'C' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isconsignee = true AND x.id = customerid AND x.isdelete = false)))"+
					//1947 系统设置增加，收发通不按委托人提取
					"\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = " + this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL) OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))) END)"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=C;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";
			//Browser.execClientScript("cneetitlemblname.focus");
		}//MBL通知人
		else if ("3".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))" +
					" OR (contactype2 = 'C' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isconsignee = true AND x.id = customerid AND x.isdelete = false)))"+
					//1947 系统设置增加，收发通不按委托人提取
					"\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = " + this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL) OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))) END)"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=N;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";;
			//Browser.execClientScript("notifytitlemblname.focus");
		} 
		showCustomerWindow.show();
		this.customerGrid.reload();
	}
	
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
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=C;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";;
				Browser.execClientScript("cneename.focus");

				// 发货人
			} else if ("1".equals(custype)) {
				sql = "\nAND contactype = 'B' AND (contactype2 = 'S' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (ishipper = TRUE AND a.salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=S;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";;
				Browser.execClientScript("cnorname.focus");
				// 通知人
			} else if ("2".equals(custype)) {
				sql = "\nAND contactype = 'B' AND (contactype2 = 'N' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=N;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";;
				Browser.execClientScript("notifyname.focus");
				// 第二通知人
			} else if ("3".equals(custype)) {
				sql = "\nAND contactype = 'B' AND (contactype2 = 'N' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))" +
					//1947 系统设置增加，收发通不按委托人提取
					  "\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid+" OR (isconsignee = TRUE AND a.salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=N;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";;
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
	
	@Bind
	private String qryPortKey;
	
	@SaveState
	@Accessible
	public String porttype;
	
	@SaveState
	@Accessible
	public String portsql = "AND 1=1";

	@Bind
	public UIWindow showPortWindow;
	
	@Bind
	public UIDataGrid portGrid;
	
	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;
	
	@Action
	public void portQry() {
		this.portchooseserviceBean.qryPort(qryPortKey);		
		this.portGrid.reload();
	}
	
	@Bind(id = "portGrid", attribute = "dataProvider")
	public GridDataProvider getportGridDataProvider() {
		portsql += "\nAND isvalid = TRUE ";
		return this.portchooseserviceBean.getPortDataProvider(portsql);
	}
	
	@Action
	public void showPortAction() {
		String portcode = (String) AppUtils.getReqParam("portcode");

		qryPortKey = portcode;
		int index = qryPortKey.indexOf("/");
		if (index > 1){
			qryPortKey = qryPortKey.substring(0, index);
		}
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
					this.selectedRowData.setPol((String) cs.get(0).get("namee"));
					this.selectedRowData.setPolcode(StrUtils.getMapVal(cs.get(0), "code"));
					this.update.markUpdate(UpdateLevel.Data, "polid");
					this.update.markUpdate(UpdateLevel.Data, "pol");
					this.update.markUpdate(UpdateLevel.Data, "polcode");
				} else if ("1".equals(porttype)) {
					this.selectedRowData.setPodid((Long) cs.get(0).get("id"));
					this.selectedRowData.setPod((String) cs.get(0).get("namee"));
					this.selectedRowData.setPodcode(StrUtils.getMapVal(cs.get(0), "code"));
					this.update.markUpdate(UpdateLevel.Data, "podid");
					this.update.markUpdate(UpdateLevel.Data, "pod");
					this.update.markUpdate(UpdateLevel.Data, "podcode");
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

	@Action
	public void portGrid_ondblclick() {
		Object[] objs = portGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if ("0".equals(porttype)) {
			this.selectedRowData.setPolid((Long) m.get("id"));
			this.selectedRowData.setPol((String) m.get("namee"));
			this.selectedRowData.setPolcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "polid");
			this.update.markUpdate(UpdateLevel.Data, "pol");
			this.update.markUpdate(UpdateLevel.Data, "polcode");
		} else if ("1".equals(porttype)) {
			this.selectedRowData.setPodid((Long) m.get("id"));
			this.selectedRowData.setPod((String) m.get("namee"));
			this.selectedRowData.setPodcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "podid");
			this.update.markUpdate(UpdateLevel.Data, "pod");
			this.update.markUpdate(UpdateLevel.Data, "podcode");
		} else if ("2".equals(porttype)) {
			//this.selectedRowData.setPddid((Long) m.get("id"));
			//this.selectedRowData.setPdd((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "pddid");
			this.update.markUpdate(UpdateLevel.Data, "pdd");
		}
		showPortWindow.close();
	}
	
	@Bind
	public UIIFrame defineIframe;
	
	@Bind
	public UIWindow showDefineWindow;
	/**
	 * 显示定制页面
	 */
	@Action
	public void showDefine() {
		defineIframe.load("./busairdefine.xhtml?id=" + this.pkVal);
		showDefineWindow.show();
	}
	
	@Action
	public void addhblagent(){
		this.selectedRowData.setNotifytitle(this.selectedRowData.getNotifytitle());
		update.markUpdate(true, UpdateLevel.Data, "notifytitle");
	}
	@Action
	public void addhblagent1(){
		this.selectedRowData.setCneetitle(this.selectedRowData.getCneetitle());
		update.markUpdate(true, UpdateLevel.Data, "cneetitle");
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
				portQry();
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
	
	
	
	@Action
	public void refreshDtl() {
		this.editGrid.reload();
	}
	
	@Action
	public void addGoods() {
		dtlData = new BusGoods();
		dtlData.setLinkid(this.pkVal);
		dtlData.setLinktbl("bus_air");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void delGoods() {
		String[] ids = this.editGrid.getSelectedIds();
		if(ids == null || ids.length ==0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for(String id : ids) {
			if(p.matcher(id).matches()){
				try{
					serviceContext.busGoodsMgrService.removeDate(Long.valueOf(id), AppUtils.getUserSession().getUsercode());
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		this.alert("OK");
		this.editGrid.reload();
	}
	
	@Bind
	@SaveState
	public Long pkValCnt;
	
	@Action
	public void editGoods(){
		try {
			String[] ids = this.editGrid.getSelectedIds();
			if (ids == null || ids.length != 1) {
				MessageUtils.alert("请选中一行!");
				return;
			}
			this.pkValCnt = getGridSelectId();
			doServiceFindData();
			this.refreshForm();
			if(editWindow != null)editWindow.show();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			update.markUpdate(true, UpdateLevel.Data, "pkValCnt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 保存关闭
	 */
	@Action
	protected void saveGoods() {
		try {
			serviceContext.busGoodsMgrService.saveData(dtlData);
			refreshDtl();
			//this.editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Accessible
	public int[] getGridSelIds() {
		String sql = 
				"\nSELECT "+
				"\n(CASE WHEN EXISTS(SELECT 1 FROM bus_goods WHERE linkid=" + this.pkValCnt + ") THEN TRUE ELSE FALSE END) AS flag"+
				"\nFROM bus_goods a "+
				"\nWHERE a.isdelete = FALSE " +
				"\nAND a.linkid = " + this.pkVal;
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
	
	
	@Action
	public void chooseship(){
		try {
			this.saveContainer();
			Long id = -1L;
			if(this.pkVal!=null && this.pkVal>0){
				id = this.pkVal;
			}else if(this.selectedRowData.getId()>0){
				id = this.selectedRowData.getId();
			}

			this.serviceContext.busAirMgrService.saveData(selectedRowData);
			this.serviceContext.busAirMgrService.updateGoodsSelects(this.editGrid.getSelectedIds(),id);
			this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_bus_goods_createbillinfo('shipid="+this.selectedRowData.getId()+";');");
			
			String sql = " isdelete = false AND id =" + this.selectedRowData.getId();
			this.selectedRowData = serviceContext.busAirMgrService.busAirDao
					.findOneRowByClauseWhere(sql);
			
			update.markUpdate(true, UpdateLevel.Data, "centerEditPanel");
			this.editGrid.reload();
		} catch (InvalidDataAccessResourceUsageException e) {
			e.printStackTrace();
			MessageUtils.alert("请先保存!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
	
	
	@Bind
	public UIIFrame containerIframe;
	
	@Bind
	public UIWindow showContainerWindow;
	/**
	 * 显示货品明细
	 * */
	@Action
	public void qryContainer() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		String id = ids[0];
		Long cid = Long.valueOf(id);
		containerIframe.load("/scp/pages/module/ship/containerdetail.xhtml?cid="+cid);
		showContainerWindow.show();
	}
	
	@Action
	public void showHblSplit(){
		String url = "../air/busairbill.xhtml?id=" + selectedRowData.getJobid();
		AppUtils.openWindow("____", url);
		
	}
	
	@Action
    public void importhbl(){
		if(this.finajobs == null || this.finajobs.getNos().isEmpty()){
    		MessageUtils.alert("请先生成工作单号!");
    	}else{
    		this.selectedRowData.setHawbno(this.finajobs.getNos().replace("-", "").replace("/", "").replace("\\", ""));
    	}
    }
	
	@Action
	public void addGood(){
		dtlData = new BusGoods();
		dtlData.setLinkid(pkVal);
		editGrid.appendRow(dtlData);
	}	
	
	@Action
	public void exportContactNotice() {
		String arg = "&para=jobid=" + this.jobid + ":userid="
				+ AppUtils.getUserSession().getUserid() + ":corpidcurrent="
				+ AppUtils.getUserSession().getCorpid() + ":";
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/define/contact_air.raq";
		String newRaq = ConfigUtils
				.findSysCfgVal("bus_air_contact_notice");
		String newOpenUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/define/" + newRaq;
		if (newRaq != null && newRaq != "") {
			AppUtils.openWindow("_apAllCustomReport", newOpenUrl + arg);
		} else {
			AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		}
	}
	
	@Action
	public void provingairajax(){
		String coltype = AppUtils.getReqParam("coltype");
		String colval = AppUtils.getReqParam("colval").trim();
		if(StrUtils.isNull(colval)){
			return;
		}
		//2761 空运界面增加检查提醒 
		//一个提单号只能在一个主单内使用（主单内的子单不受限制），如果其他工作单号录入相同提单号，系统弹出一个对话框做提醒
		String sql = "SELECT string_agg(COALESCE(y.nos,x.sono),',') AS nos FROM bus_air x,fina_jobs y " 
				   +"\n WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.jobid = y.id AND y.id <> "+jobid
				   +"\n AND x."+coltype+"='"+colval+"'"
				   +"\n	AND COALESCE(y.parentid,0) <> "+jobid
				   +"\n	AND NOT EXISTS(SELECT 1 FROM fina_jobs WHERE isdelete = FALSE AND jobtype = 'A' AND id = "+jobid+" AND COALESCE(parentid,0) = y.id)";
		try{
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.get("nos")!=null){
				alert(("sono".equals(coltype)?"订舱号：":"MAWB NO.")+"重复，已存在的单号为："+m.get("nos").toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	// 导入
	@SaveState
	public String temFileUrl;

	@Bind
	public UIWindow showexportWindow;

	@Action
	public void exportshipping1() {
		showexportWindow.show();
	}

	@Bind
	@SaveState
	private String types;

	@Action
	public void showExport() {
		if ("1".equals(types)) {
			Browser.execClientScript("exc1()");
		} else {
			MessageUtils.alert("请先选择格式!");
		}
		showexportWindow.close();
	}

	@Action
	public void downloadTemplete() {
		// 导出托书模版
		String exportFileName = "importTempleteForJobs_Air.xlsx";
		String cfgFile = ConfigUtils
				.findSysCfgVal("bus_air_importTempleteForJobs_Air");
		if (!StrUtils.isNull(cfgFile)) {
			exportFileName = cfgFile;
		}
		this.temFileUrl = AppUtils.getHttpServletRequest().getSession()
				.getServletContext().getRealPath("")
				+ File.separator
				+ "upload"
				+ File.separator
				+ "air"
				+ File.separator + exportFileName;
	}

	@Bind(id = "downloadShipTemplete", attribute = "src")
	private File getDownload7() {
		return new File(temFileUrl);
	}

	// 导出托书
	@Action
	public void exportshipping() {
		if (this.selectedRowData != null && this.selectedRowData.getId() > 0) {
			// 生成下载临时文件路径
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));
			fileUrl.append("importJobsTemplete");
			fileUrl.append(new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date()));
			fileUrl.append("_");
			fileUrl.append(AppUtils.getUserSession().getUsercode());
			fileUrl.append(".xlsx");
			this.temFileUrl = fileUrl.toString();
			File file = new File(fileUrl.toString());
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("\n SELECT *");
				sbsql.append("\n FROM bus_air");
				sbsql.append("\n WHERE");
				sbsql.append("\n isdelete = FALSE");
				sbsql.append("\n AND id = " + this.selectedRowData.getId());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sbsql.toString());

				// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
				String exportFileName = "importTempleteForJobs_Air.xlsx";
				String cfgFile = ConfigUtils.findSysCfgVal("bus_shipping_importTempleteForJobs_Air");
				if (!StrUtils.isNull(cfgFile)) {
					exportFileName = cfgFile;
					map.put("corporation", "");
				}

				// 模版所在路径
				String fromFileUrl = AppUtils.getHttpServletRequest()
						.getSession().getServletContext().getRealPath("")
						+ File.separator
						+ "upload"
						+ File.separator
						+ "air"
						+ File.separator + exportFileName;
				if (!ReadExcel.exportJobsForExcelAir(new File(fromFileUrl), file, map)) {
					return;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			MessageUtils.alert("请先保存工作单!");
			return;
		}
	}

	@Bind(id = "downloadexportshipping", attribute = "src")
	private File getDownload6() {
		return new File(temFileUrl);
	}




	//导入
	@Bind
	public UIIFrame uploadiframe;

	@Action
	public void openuploadwindow() {
		uploadiframe.load("./uploadtemplete.aspx?method=importair&jobid="+ this.jobid);
		importFileWindow.show();
	}

	@Action
	public void closeFileWindowAction() {
		this.refresh();
	}



}
