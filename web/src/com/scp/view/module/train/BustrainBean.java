package com.scp.view.module.train;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.dao.sys.SysCorpcontactsDao;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusShipSchedule;
import com.scp.model.bus.BusTrain;
import com.scp.model.data.DatGoodstype;
import com.scp.model.data.DatLine;
import com.scp.model.data.DatPort;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.service.data.PortyMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;
@ManagedBean(name = "pages.module.train.bustrainBean", scope = ManagedBeanScope.REQUEST)
public class BustrainBean extends EditGridFormView {
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@SaveState
	@Accessible
	public BusTrain selectedRowData = new BusTrain();

	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();

	@Autowired
	public ApplicationConf applicationConf;

	@SaveState
	@Accessible
	@Bind
	public Long jobid;

	@Bind(id = "istelrel")
	public UICheckBox istelrel_box;

	@Bind(id = "istelrelback")
	public UICheckBox istelrelback_box;

	@Bind(id = "telrelnos")
	public UITextField telrelnos_text;

	@Bind(id = "telreler")
	public UITextField telreler_text;

	@SaveState
	@Accessible
	@Bind
	public Long customerid;

	@SaveState
	@Accessible
	public String custype;

	@SaveState
	@Accessible
	public String custypeMBL;

	@SaveState
	@Accessible
	public String sql = "AND 1=1";

	@SaveState
	@Accessible
	public String sqlMy = "";

	@SaveState
	@Accessible
	public FinaJobs job = new FinaJobs();

	public SysCorpcontacts sysCorp;

	@Bind
	public UIWindow shipscheduleWindow;

	@Bind
	public UIButton saveMasterRemarks;

//	@Bind
//	public UIButton saveMasterMbl;

	@Bind
	public UIButton saveMaster;

	@Bind
	public UIButton save;

	@Bind
	public UIButton save1;

	@Bind
	public UIButton save2;

	@Bind
	public UIButton saveForm;

	@Bind
	public UIButton openuploadwindow;

	@SaveState
	private BusShipSchedule shipschedule;

	@Bind
	@SaveState
	@Accessible
	public Long linkid;

	public Long userid;

	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();

	@Bind
	@SaveState
	public String hblNumber;

	@Bind
	@SaveState
	public String mblNumber;

	@SaveState
	public String goodstype = "";

	@Bind
	@SaveState
	public String website;

	@Bind
	public String issaas;

	@Bind
	@SaveState
	public String actionJsText;// 按不同公司自定义js从 sys_formcfg 获取

	@SaveState
	public String agentcompay = "";
   
	
	@Bind
	@SaveState
	public String customerno;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			initCtrl();
			init();
			super.applyGridUserDef();
			refresh();
			showHMnumber();
			if (this.job != null
					&& (job.getIslock() || job.getIscomplete() || job
							.getIsclose())) {
				saveMaster.setDisabled(true);
//				saveMasterMbl.setDisabled(true);
				saveMasterRemarks.setDisabled(true);
				// save.setDisabled(true);
				save1.setDisabled(true);
				save2.setDisabled(true);
				saveForm.setDisabled(true);
			}
			issaas = String.valueOf(applicationConf.isSaas());
			this.update.markUpdate("issaas");
			if (!getSysformcfg().equals("")) {
				String js = "setSysformcfg('"
						+ getSysformcfg().replaceAll("\"", "\\\\\"") + "')";
				Browser.execClientScript(js);
			}

			actionJsText = "";
			List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao
					.findAllByClauseWhere(" formid = '"
							+ this.getMBeanName()
							+ "' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
			for (Sysformcfg sysformcfg : sysformcfgs) {
				actionJsText += sysformcfg.getJsaction();
			}
			// System.out.println("actionJsText:"+actionJsText);
			update.markUpdate(true, UpdateLevel.Data, "actionJsText");

			if (selectedRowData.getAgentid() != null
					&& this.selectedRowData.getAgentid() > 0) {
				SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao
						.findById(this.selectedRowData.getAgentid());
				String abbr = sysCorporation.getAbbr();
				this.agentcompay = (abbr != null && !abbr.equals("") ? abbr
						: sysCorporation.getNamec());
			}
			getcustomerno();
			
		}
	}

	public void showHMnumber() {
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow("SELECT count(*) FROM bus_train_bill WHERE isdelete = FALSE AND bltype = 'H' AND jobid = "
						+ this.jobid.toString());
		hblNumber = m.get("count").toString();
		Map mn = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow("SELECT count(*) FROM bus_train_bill WHERE isdelete = FALSE AND bltype = 'M' AND jobid = "
						+ this.jobid.toString());
		mblNumber = mn.get("count").toString();
	}

	private void initCtrl() {
		istelrel_box.setDisabled(true);
		istelrelback_box.setDisabled(true);
		telrelnos_text.setDisabled(true);
		telreler_text.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs
				.getValue())) {
			if (s.endsWith("_telrel")) {
				istelrel_box.setDisabled(false);
				telrelnos_text.setDisabled(false);
				telreler_text.setDisabled(false);
			} else if (s.endsWith("_telrelback")) {
				istelrelback_box.setDisabled(false);
			}
		}
	}

	public void init() {
		String id = AppUtils.getReqParam("id").trim();
		jobid = Long.valueOf(id);
		this.job = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.jobid);
		if (this.job != null && !this.job.getIslock()) {
			//openuploadwindow.setDisabled(false);
		} else {
			//openuploadwindow.setDisabled(true);
		}
		this.customerid = job.getCustomerid();

		if (job.getIscheck()) {
			Browser.execClientScript("ischeckedited();");
		}
	}
	
	@Bind
	public UIIFrame portIFrame;

	@Action
	public void showportAction() {
		String type = (String) AppUtils.getReqParam("type");
		portIFrame.setSrc("../stock/popqrygrid.xhtml?type="+type.toString());
		update.markAttributeUpdate(portIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, portIFrame);
		
		showPortWindows.show();
	}
	
	@Action
	public void setportAction() {
		String type = (String) AppUtils.getReqParam("type");
		String id = (String) AppUtils.getReqParam("id");
		
		try {
			DatPort datPort = serviceContext.portyMgrService.datPortDao.findById(Long.valueOf(id));
			if("pol".equals(type)){
				//Browser.execClientScript("polidJsvar.setValue('"+datPort.getId()+"')");
				selectedRowData.setPolid(datPort.getId());
				selectedRowData.setPolcode(datPort.getCode());
				Browser.execClientScript("polCodeJsvar.setValue('"+datPort.getCode()+"')");
				Browser.execClientScript("polJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#pol_input').val('"+datPort.getCode()+"')");
			}
			if("pod".equals(type)){
				selectedRowData.setPodid(datPort.getId());
				selectedRowData.setPodcode(datPort.getCode());
				Browser.execClientScript("podCodeJsvar.setValue('"+datPort.getCode()+"')");
				Browser.execClientScript("podJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#pod_input').val('"+datPort.getCode()+"')");
			}
			if("pdd".equals(type)){
				selectedRowData.setPddid(datPort.getId());
				selectedRowData.setPddcode(datPort.getCode());
				Browser.execClientScript("pddCodeJsvar.setValue('"+datPort.getCode()+"')");
				Browser.execClientScript("pddJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#pdd_input').val('"+datPort.getCode()+"')");
			}
			/*if("poa".equals(type)){
				selectedRowData.setPoa(datPort.getCode());
				Browser.execClientScript("poanameJsVar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#poa_input').val('"+datPort.getNamee()+"')");
			}*/
			if("destination".equals(type)){
				selectedRowData.setDestinationcode(datPort.getCode());
				selectedRowData.setDestination(datPort.getNamee());
				Browser.execClientScript("destinationcodeJs.setValue('"+datPort.getCode()+"')");
				Browser.execClientScript("destinationJs.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#destination_input').val('"+datPort.getCode()+"')");
			}
			if("agentname".equals(type)){
				SysCorporation data = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
				Browser.execClientScript("loaditemJs.setValue('"+data.getNamec()+"')");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		showPortWindow.close();
		
	}
	
	@Override
	public void insert() {
		// dtlData = new BusShipContainer();
		// dtlData.setJobid(this.jobid);
		// dtlData.setLdtype(selectedRowData.getLdtype());
		// dtlData.setPackagee(selectedRowData.getPacker());
		// dtlData.setShipid(this.selectedRowData.getId());
		// dtlData.setOrderno(5);
		//		
		// editGrid.appendRow(dtlData);
		// super.add();
	}

	@Action
	public void addMaster() {
		this.selectedRowData = new BusTrain();
		// this.mPkVal = -1l;
		this.pkVal = -1l;
		// super.addMaster();
	}

	@Action
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			serviceContext.busShippingMgrService.removeDate(selectedRowData.getId());
			this.addMaster();
			refreshMasterForm();
			this.alert("OK");
		}
	}

	@Override
	public void del() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if (p.matcher(s).matches()) {
				try {
					this.serviceContext.busShipContainerMgrService
							.removeDate(Long.parseLong(s));
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		editGrid.remove();

		// doServiceSaveMaster();
		// try {
		// serviceContext.busShipContainerMgrService.removeDate(ids);
		// this.alert("OK");
		// this.editGrid.reload();
		// } catch (Exception e) {
		// MessageUtils.showException(e);
		// }
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
				.findById(this.pkValCnt);
	}

	/*
	 * 保存新增
	 */
	@Override
	protected void doServiceSave() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			dtlData = new BusShipContainer();
			dtlData.setJobid(this.jobid);
			dtlData.setLdtype(selectedRowData.getLdtype());
			dtlData.setPackagee(selectedRowData.getPacker());
			this.dtlData.setShipid(this.selectedRowData.getId());
			if (dtlData != null && dtlData.getIsselect() != null
					&& dtlData.getIsselect()) {
				updateChooseship();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void addhblagent() {
		this.selectedRowData.setNotifytitlembl(this.selectedRowData
				.getAgentitle());
		update.markUpdate(true, UpdateLevel.Data, "notifytitlembl");
	}

	@Action
	public void addhblagent1() {
		this.selectedRowData.setCneetitlembl(this.selectedRowData
				.getAgentitle());
		update.markUpdate(true, UpdateLevel.Data, "cneetitlembl");
	}

	/**
	 * 保存关闭
	 */
	@Action
	protected void save2() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			this.editWindow.close();
			if (dtlData != null && dtlData.getIsselect() != null
					&& dtlData.getIsselect()) {
				updateChooseship();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 保存关闭
	 */
	@Action
	protected void save1() {
		try {
			dtlData.setMarkno(AppUtils
					.replaceStringByRegEx(dtlData.getMarkno()));
			dtlData.setGoodsnamee(AppUtils.replaceStringByRegEx(dtlData
					.getGoodsnamee()));
			dtlData.setMaterial(AppUtils.replaceStringByRegEx(dtlData
					.getMaterial()));
			dtlData.setGoodsname(AppUtils.replaceStringByRegEx(dtlData
					.getGoodsname()));
			dtlData.setRemarks(AppUtils.replaceStringByRegEx(dtlData
					.getRemarks()));
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			if (dtlData != null && dtlData.getIsselect() != null
					&& dtlData.getIsselect()) {
				updateChooseship();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 保存冷冻柜
	 */
	@Action
	protected void saveTemperature() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			alert("OK");
			if (dtlData != null && dtlData.getIsselect() != null
					&& dtlData.getIsselect()) {
				updateChooseship();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 保存危险柜
	 */
	@Action
	protected void savedangerous() {
		saveTemperature();
	}

	@Override
	public void refresh() {
		super.refresh();
		refreshMasterForm();
	}

	@Action
	public void refreshMaster() {
		refresh();
	}

	public void doServiceSaveMaster() {
		try {
			if (selectedRowData.getEtd() != null
					&& selectedRowData.getEta() != null
					&& selectedRowData.getEta()
							.before(selectedRowData.getEtd())) {
				MessageUtils.alert("ETA不能小于ETD");
				return;
			}
			if (selectedRowData.getAta() != null
					&& selectedRowData.getAtd() != null
					&& selectedRowData.getAta()
							.before(selectedRowData.getAtd())) {
				MessageUtils.alert("ATA不能小于ATD");
				return;
			}
			selectedRowData.setCnorname(AppUtils
					.replaceStringByRegEx(selectedRowData.getCnorname()));
			selectedRowData.setCnortitle(AppUtils
					.replaceStringByRegEx(selectedRowData.getCnortitle()));
			selectedRowData.setCneename(AppUtils
					.replaceStringByRegEx(selectedRowData.getCneename()));
			selectedRowData.setCneetitle(AppUtils
					.replaceStringByRegEx(selectedRowData.getCneetitle()));
			selectedRowData.setNotifyname(AppUtils
					.replaceStringByRegEx(selectedRowData.getNotifyname()));
			selectedRowData.setNotifytitle(AppUtils
					.replaceStringByRegEx(selectedRowData.getNotifytitle()));
			selectedRowData.setAgenname(AppUtils
					.replaceStringByRegEx(selectedRowData.getAgenname()));
			selectedRowData.setAgentitle(AppUtils
					.replaceStringByRegEx(selectedRowData.getAgentitle()));
			selectedRowData.setClaimPre(AppUtils
					.replaceStringByRegEx(selectedRowData.getClaimPre()));
			selectedRowData.setClaimTruck(AppUtils
					.replaceStringByRegEx(selectedRowData.getClaimTruck()));
			selectedRowData.setClaimBill(AppUtils
					.replaceStringByRegEx(selectedRowData.getClaimBill()));
			selectedRowData.setClaimClear(AppUtils
					.replaceStringByRegEx(selectedRowData.getClaimClear()));
			selectedRowData.setFactoryinfo(AppUtils
					.replaceStringByRegEx(selectedRowData.getFactoryinfo()));
			selectedRowData.setRemark1(AppUtils
					.replaceStringByRegEx(selectedRowData.getRemark1()));
			selectedRowData.setRemark2(AppUtils
					.replaceStringByRegEx(selectedRowData.getRemark2()));
			selectedRowData.setRemark3(AppUtils
					.replaceStringByRegEx(selectedRowData.getRemark3()));
			selectedRowData.setRemark4(AppUtils
					.replaceStringByRegEx(selectedRowData.getRemark4()));
			selectedRowData.setRemark5(AppUtils
					.replaceStringByRegEx(selectedRowData.getRemark5()));
			//selectedRowData.setCnortitlemblname(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitlemblname()));
			//selectedRowData.setCnortitlembl(AppUtils.replaceStringByRegEx(selectedRowData.getCnortitlembl()));
			//selectedRowData.setCneetitlemblname(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitlemblname()));
			//selectedRowData.setCneetitlembl(AppUtils.replaceStringByRegEx(selectedRowData.getCneetitlembl()));
			//selectedRowData.setNotifytitlemblname(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitlemblname()));
			//selectedRowData.setNotifytitlembl(AppUtils.replaceStringByRegEx(selectedRowData.getNotifytitlembl()));
			selectedRowData.setMarksno(AppUtils.replaceStringByRegEx(selectedRowData.getMarksno()));
			selectedRowData.setGoodsdesc(AppUtils.replaceStringByRegEx(selectedRowData.getGoodsdesc()));
			selectedRowData.setTotledesc(AppUtils.replaceStringByRegEx(selectedRowData.getTotledesc()));
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			Browser.execClientScript("showmsg()");
			this.editGrid.commit();
			if (modifiedData != null) {
				update(modifiedData);
			}
			if (addedData != null) {
				add(addedData);
			}
			if (removedData != null) {
				remove(removedData);
			}
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND jobid = " + jobid;
		m.put("qry", qry);
		return m;
	}

	@SaveState
	public String contractidnamec = "";

	@Action
	public void refreshMasterForm() {

		try {
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busTrainMgrService.findByjobId(this.jobid);
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			isputmbl = selectedRowData.isIsputmbl();
			istelrel = selectedRowData.getIstelrel();
			istelrelback = selectedRowData.getIstelrelback();

			isprintmbl = selectedRowData.getIsprintmbl();
			isgetmbl = selectedRowData.getIsgetmbl();
			isreleasembl = selectedRowData.getIsreleasembl();
			if (selectedRowData.getContractid() != null) {
				SysCorporation sysCorporation = serviceContext.userMgrService.sysCorporationDao
						.findById(selectedRowData.getContractid());
				// Browser.execClientScript("$('#customer_input').val('"+sysCorporation.getNamec()+"')");
				contractidnamec = sysCorporation.getNamec();
			}
			// if(selectedRowData.getRoutecode()!=null){
			// Browser.execClientScript("$('#route_input').val('"+selectedRowData.getRoutecode()+"')");
			// }
			if (selectedRowData.getGoodstypeid() != null) {
				DatGoodstype datGoodstype = serviceContext.goodstypeMgrService.datGoodstypeDao
						.findById(selectedRowData.getGoodstypeid());
				Browser.execClientScript("$('#goodstype_input').val('"
						+ datGoodstype.getNamec() + "')");
			}
			if (selectedRowData.getPolcode() == null) {
				selectedRowData.setPolcode("");
			}
			if (selectedRowData.getPodcode() == null) {
				selectedRowData.setPodcode("");
			}
			if (selectedRowData.getPddcode() == null) {
				selectedRowData.setPddcode("");
			}
			if (selectedRowData.getDestinationcode() == null) {
				selectedRowData.setDestinationcode("");
			}
		} catch (MoreThanOneRowException e) {
			selectedRowData = new BusTrain();
			selectedRowData.setJobid(this.jobid);
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			this.pkVal = this.selectedRowData.getId();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "SFTMBLPanel");
			update.markUpdate(true, UpdateLevel.Data, "pkVal");
			this.editGrid.setSelections(getGridSelIds());
			this.editGrid.reload();
		}
		if (this.selectedRowData != null
				&& this.selectedRowData.getPriceuserid() != null) {
			SysUser sysUser = serviceContext.userMgrService.sysUserDao
					.findById(this.selectedRowData.getPriceuserid());
			Browser.execClientScript("$('#priceuser_input').val('"
					+ sysUser.getNamec() + "')");
		} else {
			Browser.execClientScript("$('#priceuser_input').val('')");
		}
		getwebsite();
	}

	@SaveState
	@Accessible
	public String portsql = "AND 1=1";

	@SaveState
	@Accessible
	public String porttype;

	@Bind
	public UIWindow showPortWindow;
	
	@Bind
	public UIWindow showPortWindows;

	@Bind
	public UIDataGrid portGrid;

	@Bind
	private String qryPortKey;

	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;

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
		String type = (String) AppUtils.getReqParam("type");
		porttype = (String) AppUtils.getReqParam("porttype");

		if ("0".equals(porttype)) {
			portsql = "AND ispol = TRUE";
		} else if ("1".equals(porttype)) {
			portsql = "AND ispod = TRUE";
		} else if ("2".equals(porttype)) {
			portsql = "AND ispdd = TRUE";
		} else if ("3".equals(porttype)) {
			portsql = "AND isdestination = TRUE";
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

			Browser.execClientScript("refreshRouteInfo('"
					+ (String) m.get("line") + "')");

			this.update.markUpdate(UpdateLevel.Data, "routecode");
			this.update.markUpdate(UpdateLevel.Data, "podid");
			this.update.markUpdate(UpdateLevel.Data, "pod");
			this.update.markUpdate(UpdateLevel.Data, "podcode");
		} else if ("2".equals(porttype)) {
			this.selectedRowData.setPddid((Long) m.get("id"));
			this.selectedRowData.setPdd((String) m.get("namee"));
			this.selectedRowData.setPddcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "pddid");
			this.update.markUpdate(UpdateLevel.Data, "pdd");
			this.update.markUpdate(UpdateLevel.Data, "pddcode");
		} else if ("3".equals(porttype)) {
			this.selectedRowData.setDestination((String) m.get("namee"));
			this.selectedRowData.setDestinationcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "destinationcode");
			this.update.markUpdate(UpdateLevel.Data, "destination");
		}
		showPortWindow.close();
	}

	@Action
	public void chooseBook() {

		Long containerid = 0L;
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			containerid = 0L;

		} else if (ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		} else {

			containerid = Long.valueOf(ids[0]);
		}

		String url = AppUtils.getContextPath()
				+ "/pages/module/ship/shipbookingchoose.xhtml?shipid="
				+ this.pkVal + "&containerid=" + containerid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	@Action
	public void cancelBook() {

		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选行");
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

	@Action
	public void returnCus() {

		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选行");
			return;
		}
		try {
			serviceContext.busBookingMgrService.returnCus(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 发送邮件 neo 2014-04-11
	 */
	@Action
	public void sendMail() {
		String url = AppUtils.getContextPath()
				+ "/pages/sysmgr/mail/emailsendedit.xhtml?type=shipping&id="
				+ this.pkVal;
		AppUtils.openWindow("_sendMail_shipping", url);
	}


	// 日期格式转换
	public static String getYYYYMMDD(Date date) {
		DateFormat yyyymmddFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			return yyyymmddFormat.format(date);
		} else {
			return null;
		}
	}

	// 字符串转日期
	public static Date getDateType(String str) {
		if (str == null) {
			return null;
		}
		Date date = null;
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = new Date();
			date = dateFormate.parse(str);
		} catch (Exception ex) {
		}
		return date;
	}

	@ManagedProperty("#{portyMgrService}")
	public PortyMgrService portyMgrService;

	

	private Long getCarrierid(String carriercode) {

		try {
			String sql = "SELECT id FROM sys_corporation WHERE abbr = '"
					+ carriercode
					+ "' AND iscarrier = true AND isdelete = FALSE";
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			Long carrierid = (Long) m.get("id");

			return carrierid;

		} catch (Exception e) {
			MessageUtils.alert("没找到对应的船公司id,请手动下拉");
			return null;
		}

	}

	public void setShipschedule() {
		shipschedule = serviceContext.shipScheduleService.busShipScheduleDao
				.findById(selectedRowData.getScheduleid());
	}

	/**
	 * @param data
	 * @return
	 */
	public Long getPolorPodId(String data) {
		String qry = "";
		if (StrUtils.isNull(data)) {
			return null;
		} else {
			qry = "SELECT id FROM dat_port WHERE (namee = '" + data
					+ "' OR  code = '" + data
					+ "' ) AND isdelete = FALSE limit 1";
		}

		try {
			Map map = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(qry);
			Long id = (Long) map.get("id");
			// ApplicationUtils.debug("id="+id);
			return id;

		} catch (Exception e) {
			return null;
		}

	}

	@Action
	public void showReport() {
		// 暂时注释 2014-08-22 neo
		if (!this.job.getIsconfirmrpt()) {
			MessageUtils.alert("费用中未确认业务报告！");
			return;
		}
		// importBusReport(this.selectedRowData.getJobid());
		String rpturl = AppUtils.getRptUrl();
		String openUrl = "";

		String sql = "SELECT COUNT(*) AS count FROM fina_jobs WHERE isdelete = FALSE AND parentid = "
				+ this.job.getId();
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		Long i = (Long) m.get("count");
		// 没有子单
		if (i == 0) {
			openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/ship/booking.raq";
		} else {
			openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/ship/booking2.raq";
		}
		AppUtils.openWindow("_ship_bookink", openUrl + getArgs());
	}

	private String getArgs() {
		String args = "";
		args += "&id=" + this.selectedRowData.getId();
		return args;
	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind
	private String qryCustomerKey;

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
		String sql2 = sql + sqlMy;
		return this.customerService.getCustomerDataProvider(sql2);
	}

	@Action
	public void showCustomer() {
		custypeMBL = "";
		String customercode = (String) AppUtils.getReqParam("customercode");
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);
		// 1947 另外这里弹窗点开的时候，之前是把小框里面的内容自动带到查询输入框的，这个拿掉，不然还要手动清掉内容再查询
		qryCustomerKey = "";
		String type = (String) AppUtils.getReqParam("type");

		custype = (String) AppUtils.getReqParam("custype");

		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			customerQry();
			// 收货人
			if ("0".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'C' " +
				// 1947 系统设置增加，收发通不按委托人提取
						" AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						// 或者对应客户勾选了收货人
						+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)";
				Browser.execClientScript("cneename.focus");

				// 发货人
			} else if ("1".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'S' "
						+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						// 或者对应客户勾选了发货人
						+ this.customerid + " OR (ishipper = TRUE AND salesid IS NULL)) END)";
				Browser.execClientScript("cnorname.focus");
				// 通知人
			} else if ("2".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'N' OR contactype2 = 'C') "
						+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)";
				Browser.execClientScript("notifyname.focus");
				// hbl代理
			} else if ("3".equals(custype)) {
				// sql = " AND isagentdes = true AND contactype2 = 'A'";
				// 不清楚为什么要加isagentdes = true，暂时去掉
				sql = "AND contactype2 = 'A'";
				Browser.execClientScript("agenname.focus");
			} else if ("4".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'N' OR contactype2 = 'C') "
					+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
					+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)";
			Browser.execClientScript("notifyname2.focus");
			// hbl代理
		}
			return;
		}

		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
				if ("0".equals(custype)) {
					this.selectedRowData.setCneeid((Long) cs.get(0).get("id"));
					this.selectedRowData.setCneename((String) cs.get(0).get("name"));
					this.selectedRowData.setCneetitle((String) cs.get(0).get("contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "cneeid");
					this.update.markUpdate(UpdateLevel.Data, "cneename");
					this.update.markUpdate(UpdateLevel.Data, "cneetitle");
				} else if ("1".equals(custype)) {
					this.selectedRowData.setCnorid((Long) cs.get(0).get("id"));
					this.selectedRowData.setCnorname((String) cs.get(0).get("name"));
					this.selectedRowData.setCnortitle((String) cs.get(0).get("contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "cnorid");
					this.update.markUpdate(UpdateLevel.Data, "cnorname");
					this.update.markUpdate(UpdateLevel.Data, "cnortitle");
				} else if ("2".equals(custype)) {
					this.selectedRowData.setNotifyid((Long) cs.get(0).get("id"));
					this.selectedRowData.setNotifyname((String) cs.get(0).get("name"));
					this.selectedRowData.setNotifytitle((String) cs.get(0).get("contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "notifyid");
					this.update.markUpdate(UpdateLevel.Data, "notifyname");
					this.update.markUpdate(UpdateLevel.Data, "notifytitle");
				} else if ("3".equals(custype)) {
					this.selectedRowData.setAgenid((Long) cs.get(0).get("id"));
					this.selectedRowData.setAgenname((String) cs.get(0).get("customerabbr"));
					this.selectedRowData.setAgentitle((String) cs.get(0).get("contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "agenid");
					this.update.markUpdate(UpdateLevel.Data, "agenname");
					this.update.markUpdate(UpdateLevel.Data, "agentitle");
				} else if ("4".equals(custype)) {
					this.selectedRowData.setNotifyid2((Long) cs.get(0).get("id"));
					this.selectedRowData.setNotifyname2((String) cs.get(0).get("name"));
					this.selectedRowData.setNotifytitle2((String) cs.get(0).get("contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "notifyid2");
					this.update.markUpdate(UpdateLevel.Data, "notifyname2");
					this.update.markUpdate(UpdateLevel.Data, "notifytitle2");
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
				} else if ("4".equals(custype)) {
					Browser.execClientScript("notifyname2.focus");
				}

			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void showCustomerMBL() {
		custype = "";
		custypeMBL = (String) AppUtils.getReqParam("custypeMBL");
		// MBL发货人
		if ("1".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'O' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))";
			Browser.execClientScript("cnortitlemblname.focus");
			// MBL收货人
		} else if ("2".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'P' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))"
					+ " OR (contactype2 = 'C' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isconsignee = true AND x.id = customerid AND x.isdelete = false)))";
			Browser.execClientScript("cneetitlemblname.focus");
		}// MBL通知人
		else if ("3".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))"
					+ " OR (contactype2 = 'C' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isconsignee = true AND x.id = customerid AND x.isdelete = false)))";
			Browser.execClientScript("notifytitlemblname.focus");
		} else if ("4".equals(custypeMBL)) {
			sql = " AND contactype2 = 'A'";
			Browser.execClientScript("agennamembl.focus");
		}
		showCustomerWindow.show();
		this.customerGrid.reload();
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

			this.selectedRowData.setCneetitlembl((String) m.get("contactxt"));
			if (m.get("customerid") != null) {
				try {
					// 2271 海运工作单委托中，HBL代理选择的时候，按这个代理对应的客户，赋值给目的港代理下拉框中值
					long customid = Long.parseLong(m.get("customerid").toString());
					SysCorporation cus = serviceContext.customerMgrService.sysCorporationDao.findById(customid);
					if (cus != null && cus.getIsagentdes() == true) {// 是目的港代理才赋值
						this.selectedRowData.setAgentdesid(customid);
						this.update.markUpdate(UpdateLevel.Data, "agentdesid");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
			this.update.markUpdate(UpdateLevel.Data, "agenid");
			this.update.markUpdate(UpdateLevel.Data, "agenname");
			this.update.markUpdate(UpdateLevel.Data, "agentitle");

		}
		if ("1".equals(custypeMBL)) {
			this.selectedRowData.setCnortitlemblid((Long) m.get("id"));
			this.selectedRowData.setCnortitlemblname((String) m.get("name"));
			this.selectedRowData.setCnortitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cnortitlemblid");
			this.update.markUpdate(UpdateLevel.Data, "cnortitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "cnortitlembl");
		} else if ("2".equals(custypeMBL)) {
			this.selectedRowData.setCneetitlembid((Long) m.get("id"));
			this.selectedRowData.setCneetitlemblname((String) m.get("name"));
			this.selectedRowData.setCneetitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembid");
			this.update.markUpdate(UpdateLevel.Data, "cneetitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
		} else if ("3".equals(custypeMBL)) {
			this.selectedRowData.setNotifytitlemblid((Long) m.get("id"));
			this.selectedRowData.setNotifytitlemblname((String) m.get("name"));
			this.selectedRowData.setNotifytitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifytitlemblid");
			this.update.markUpdate(UpdateLevel.Data, "notifytitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "notifytitlembl");
		} else if ("4".equals(custypeMBL)) {
			this.selectedRowData.setAgenidmbl((Long) m.get("id"));
			this.selectedRowData.setAgennamembl((String) m.get("name"));
			this.selectedRowData.setAgentitlembl((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "agenidmbl");
			this.update.markUpdate(UpdateLevel.Data, "agennamembl");
			this.update.markUpdate(UpdateLevel.Data, "agentitlembl");
		}
		showCustomerWindow.close();
	}

	@Resource
	public SysCorpcontactsDao sysCorpcontactsDao;

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
					try {
						String sql = "  contactype2 = 'C' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao
								.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("C");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-C-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCneeid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCneename(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}

					// 发货人
				} else if (type.equals("1")) {
					try {
						String sql = "  contactype2 = 'S' AND isdelete = FALSE AND  name = '"+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("S");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-S-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCnorid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCnorname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}
					// hbl代理
				} else if (type.equals("2")) {
					try {
						String sql = "  contactype2 = 'A' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao
								.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("A");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-A-" + getCusCode("2")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setAgenid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setAgenname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}
					// 通知人
				} else if (type.equals("3")) {
					try {
						String sql = "  contactype2 = 'N' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("N");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-N-" + getCusCode("3")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotifyid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotifyname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}
					// 发货人MBL
				} else if (type.equals("4")) {
					try {
						String sql = "  contactype2 = 'O' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("O");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-O-" + getCusCode("4")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCnortitlemblid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCnortitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}
				} else if (type.equals("5")) {
					// MBL收货人
					try {
						String sql = "  contactype2 = 'P' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("P");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-P-" + getCusCode("5")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCneetitlembid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCneetitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}
				} else if (type.equals("6")) {
					try {
						String sql = "  contactype2 = 'Q' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao
								.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("Q");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-Q-" + getCusCode("6")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotifytitlemblid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotifytitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}

				} else if (type.equals("7")) {
					try {
						String sql = "  contactype2 = 'A' AND isdelete = FALSE AND  name = '"
								+ cnename + "'";
						// System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");

					} catch (Exception e) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("A");
						String code = (StrUtils.isNull(cnename) ? (this.job.getCustomerabbr()
								+ "-A-" + getCusCode("2")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotifytitlemblid(Long.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotifytitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
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
		} else if (type.equals("4")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'O' AND customerid = "
					+ this.customerid;
		} else if (type.equals("5")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'P' AND customerid = "
					+ this.customerid;
		} else if (type.equals("6")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'Q' AND customerid = "
					+ this.customerid;
		}

		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("count");
	}

	public String[] getCusdesc(String code) {
		String[] re = new String[2];
		String sql = "SELECT name,id FROM sys_corpcontacts WHERE  name ='"
				+ code + "' AND customerid = " + this.customerid + " LIMIT 1;";
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

	@Bind(id = "sonodesc")
	public List<SelectItem> getSonodesc() {
		try {
			// ApplicationUtils.debug(this.dtlData.getBookdtlid());
			return CommonComBoxBean.getComboxItems("d.dtlid", "d.bookno",
					"_bus_booking_choose AS d", "WHERE ( d.dtlid = "
							+ this.dtlData.getBookdtlid()
							+ " OR (d.bookstate= 'A' OR d.bookstate= 'I'))",
					"ORDER BY d.bookno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Bind(id = "custcontacts")
	public List<SelectItem> getCustcontacts() {
		String wheresql = "WHERE customerid IN(SELECT customerid FROM bus_train WHERE id ="
				+ this.pkVal + " AND isdelete = FALSE) AND isdelete = FALSE";
		try {
			return CommonComBoxBean.getComboxItems("d.name", "d.name",
					"sys_corpcontacts AS d", wheresql, "");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	private void changecustcontac() {
		String custcontact = AppUtils.getReqParam("custcontact");
		if (custcontact != null) {
			List<Object[]> objs = this.serviceContext.busShippingMgrService.queryCustcontactInfo(custcontact);
			if (objs != null) {
				for (Object[] obj : objs) {
					this.selectedRowData.setCustmobile(this.chang(obj[0]));
					this.selectedRowData.setCusttel(this.chang(obj[1]));
					this.selectedRowData.setCustfax(this.chang(obj[2]));
					update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
				}
			}
		}
	}

	public String chang(Object custcontact) {
		if ("".equals(custcontact) || custcontact == null) {
			return "";
		}
		return custcontact.toString();
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

	@Bind
	public UIWindow showDefineWindow;

	@Bind
	public UIWindow showEmailWindow;

	@Bind
	public UIWindow showbookingspaceWindow;

	@Bind
	public UIIFrame bookingIframe;

	/**
	 * 显示定制页面
	 */
	@Action
	public void showDefine() {
		defineIframe.load("../train/bustraindefine.xhtml?id=" + this.pkVal);
		showDefineWindow.show();
	}

	/**
	 * 显示邮件页面
	 */
	@Action
	public void showEmail() {
		emailIframe.load("/scp/pages/sysmgr/mail/emailedit.aspx?type=D&id="
				+ "-1" + "&src=jobship&jobid="
				+ this.selectedRowData.getJobid());
		showEmailWindow.show();
	}

	/**
	 * 显示订舱页面
	 * */
	@Action
	public void showbookingspace() {
		bookingIframe.load("../train/bookingSpace.xhtml?id="
				+ this.selectedRowData.getJobid());
		showbookingspaceWindow.show();
	}

	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");

		if ("1".equals(type)) { // 委托要求大框
			this.detailsContent = content;
			Browser.execClientScript("type1()");

		} else if ("2".equals(type)) { // 拖车要求大框
			this.detailsContent = content;
			Browser.execClientScript("type2()");

		} else if ("3".equals(type)) { // 提单要求大框
			this.detailsContent = content;
			Browser.execClientScript("type3()");

		} else if ("4".equals(type)) { // 清关要求大框
			this.detailsContent = content;
			Browser.execClientScript("type4()");

		} else if ("5".equals(type)) { // 报关要求大框
			this.detailsContent = content;
			Browser.execClientScript("type5()");

		} else if ("6".equals(type)) { // 工厂信息
			this.detailsContent = content;
			Browser.execClientScript("type6()");

		} else if ("7".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type7()");
		} else if ("9".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type9()");
		} else if ("10".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type10()");
		} else if ("11".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type11()");
		} else if ("12".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type12()");
		} else if ("13".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type13()");
		} else if ("14".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type14()");
		} else if ("15".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type15()");
		} else if ("16".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type16()");
		} else if ("17".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type17()");
		} else if ("18".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type18()");
		} else if ("19".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type19()");
		} else if ("20".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type20()");
		} else if ("21".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type21()");
		} else if ("22".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type22()");
		}else if ("23".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("detailsWindow.setTitle('MBL发货人');");
		}else if ("24".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("detailsWindow.setTitle('MBL收货人');");
		}else if ("25".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("detailsWindow.setTitle('MBL通知人');");
		}else if ("26".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("detailsWindow.setTitle('MBL代理');");
		}else if ("27".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("detailsWindow.setTitle('第二通知人');");
		}
		
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		/* this.update.markUpdate(UpdateLevel.Data, "detailsTitle"); */

		this.detailsWindow.show();
		Browser.execClientScript("coutRowLength();");
	}

	/**
	 * neo 自动计算行字符长度，edi中要求每行35字符
	 * 
	 * @param event
	 */
	@Action
	public void detailsContent_onchange() {
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

	@Action
	public void setDetail() {
		chooseContainer();
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
		if ("1".equals(type)) { // 委托要求大框
			this.selectedRowData.setClaimPre(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "claimPre");

		} else if ("2".equals(type)) { // 拖车要求大框
			this.selectedRowData.setClaimTruck(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "claimTruck");

		} else if ("3".equals(type)) { // 提单要求大框
			this.selectedRowData.setClaimBill(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "claimBill");

		} else if ("4".equals(type)) { // 清关要求大框
			this.selectedRowData.setClaim_doc(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "claim_doc");

		} else if ("5".equals(type)) { // 报关要求大框
			this.selectedRowData.setClaimClear(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "claimClear");
		} else if ("6".equals(type)) { // 工厂信息大框
			this.selectedRowData.setFactoryinfo(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "factoryinfo");
		} else if ("7".equals(type)) {
			this.selectedRowData.setDelnote(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "delnoteClear");
		} else if ("9".equals(type)) {
			this.selectedRowData.setMarksno(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "marksno");
		} else if ("10".equals(type)) {
			this.selectedRowData.setGoodsdesc(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "goodsdesc");
		} else if ("11".equals(type)) {
			this.selectedRowData.setCnortitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");
		} else if ("12".equals(type)) {
			this.selectedRowData.setCneetitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cneetitle");
		} else if ("13".equals(type)) {
			this.selectedRowData.setNotifytitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");
		} else if ("14".equals(type)) {
			this.selectedRowData.setAgentitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "agentitle");
		} else if ("15".equals(type)) {
			this.dtlData.setMarkno(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "marknos");
		} else if ("16".equals(type)) {
			this.dtlData.setGoodsnamee(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "goodsnamee");
		} else if ("17".equals(type)) {
			this.selectedRowData.setHolddesc(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "holddesc");
		} else if ("18".equals(type)) {
			this.selectedRowData.setPutdesc(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "putdesc");
		} else if ("19".equals(type)) {
			this.selectedRowData.setContractno(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "contractno");
		} else if ("20".equals(type)) {
			this.selectedRowData.setRemark_booking(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "remark_booking");
		} else if ("21".equals(type)) {
			this.selectedRowData.setTotledesc(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "totledesc");
		} else if ("22".equals(type)) {
			this.selectedRowData.setRemark1(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "totledesc");
		}else if ("23".equals(type)) {
			this.selectedRowData.setCnortitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cnortitlembl");
		}else if ("24".equals(type)) {
			this.selectedRowData.setCneetitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
		}else if ("25".equals(type)) {
			this.selectedRowData.setNotifytitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifytitlembl");
		}else if ("26".equals(type)) {
			this.selectedRowData.setAgentitlembl(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "agentitlembl");
		}else if ("27".equals(type)) {
			this.selectedRowData.setNotifytitle2(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "agentitlembl");
		}

	}

	@Bind
	@SaveState
	public String packagelistFile;

	@Bind
	@SaveState
	public String packagelistbillFile;

	@Bind(id = "packagelistbill")
	public List<SelectItem> getPackagelistbill() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec ",
					"sys_report AS d",
					"WHERE modcode='shippackagebill' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Bind(id = "packagelist")
	public List<SelectItem> getPackagelist() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec ",
					"sys_report AS d",
					"WHERE modcode='shippackage' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	/**
	 * 预览装箱单报表 --没有控制
	 */
	@Action
	public void scanpackreport() {
		if (StrUtils.isNull(packagelistFile)) {
			MessageUtils.alert("请选择装箱单格式");
		} else {
			// 暂时拿掉
			// wfImporPackingList(this.selectedRowData.getJobid(),WorkFlowEnumerateShip.PACKING_LIST);
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"
					+ packagelistFile;
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
		}
	}

	/**
	 * 预览装箱单发票报表 --没有控制
	 */
	@Action
	public void scanPackBillReport() {
		if (StrUtils.isNull(packagelistbillFile)) {
			MessageUtils.alert("请选择装箱单发票格式");
		} else {
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"
					+ packagelistbillFile;
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
		}
	}


	@Bind
	private UIWindow dtlDialoggoods;
	@Bind
	private UIIFrame dtlIFramegoods;

	@Action(id = "dtlDialoggoods", event = "onclose")
	private void dtlDialoggoodsClose() {
		// 检查明细货物
		String sql = "SELECT f_busgoods_check(" + this.linkid + ")";
		try {
			this.serviceContext.busShippingMgrService.busShippingDao.executeQuery(sql);
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showgoodsdtl() {
		this.linkid = Long.valueOf(AppUtils.getReqParam("linkid"));
		this.update.markUpdate(UpdateLevel.Data, "linkid");
		String url = AppUtils.getContextPath()
				+ "/pages/module/bus/busgoods.xhtml?linkid=" + linkid;
		dtlIFramegoods.setSrc(url);
		update.markAttributeUpdate(dtlIFramegoods, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialoggoods);
		dtlDialoggoods.show();
	}

	@Bind
	public UIWindow showRemarksWindow;

	@Action
	public void showRemarks() {
		showRemarksWindow.show();
	}

	@Bind
	public Boolean isput = false;

	@Bind
	public Boolean ishold = false;

	@Bind
	public Boolean istelrel = false;

	@Bind
	public Boolean istelrelback = false;
	
	@Bind
	public Boolean isputmbl = false;

	@Action
	public void isput_oncheck() {
		boolean put = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
			if (s.endsWith("_isput")) {
				put = true;
			}
		}
		if (put) {
			selectedRowData.setIsput(isput);
			selectedRowData.setPuter(isput ? AppUtils.getUserSession().getUsercode() : null);
			selectedRowData.setPuttime(isput ? Calendar.getInstance().getTime(): null);
			update.markUpdate(true, UpdateLevel.Data, "isput");
			update.markUpdate(true, UpdateLevel.Data, "puter");
		} else {
			MessageUtils.alert("无放货权限!");
			isput = !isput;
			selectedRowData.setIsput(isput);
			update.markUpdate(true, UpdateLevel.Data, "isput");
			return;
		}
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isput = isput ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "isput");
		}
	}
	
	
	
	@Action
	public void isputmbl_oncheck() {
		boolean put = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
			//if (s.endsWith("_isputmbl")) {
				put = true;
			//}
		}
		if (put) {
			selectedRowData.setIsputmbl(isputmbl);
			selectedRowData.setPutermbl(isputmbl ? AppUtils.getUserSession().getUsercode() : null);
			selectedRowData.setPuttimembl(isputmbl ? Calendar.getInstance().getTime(): null);
			update.markUpdate(true, UpdateLevel.Data, "isputmbl");
			update.markUpdate(true, UpdateLevel.Data, "putermbl");
		} else {
			MessageUtils.alert("无MBL放货权限!");
			isputmbl = !isputmbl;
			selectedRowData.setIsputmbl(isputmbl);
			update.markUpdate(true, UpdateLevel.Data, "isputmbl");
			return;
		}
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isputmbl = isputmbl ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "isputmbl");
		}
	}
	

	@Action
	public void ishold_oncheck() {
		boolean hold = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
			if (s.endsWith("_ishold")) {
				hold = true;
			}
		}
		if (hold) {
			selectedRowData.setIshold(ishold);
			selectedRowData.setHolder(ishold ? AppUtils.getUserSession().getUsercode() : null);
			selectedRowData.setHoldtime(ishold ? Calendar.getInstance().getTime() : null);
			update.markUpdate(true, UpdateLevel.Data, "ishode");
			update.markUpdate(true, UpdateLevel.Data, "holder");
		} else {
			MessageUtils.alert("无扣货权限!");
			ishold = !ishold;
			selectedRowData.setIshold(ishold);
			update.markUpdate(true, UpdateLevel.Data, "ishode");
			return;
		}
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			ishold = ishold ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "ishold");
		}
	}

	@Action
	public void istelrelback_oncheck() {
		// boolean put = false;
		// for (String s :
		// AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
		// if (s.endsWith("_isput")) {
		// put = true;
		// }
		// }
		// if(put){
		selectedRowData.setIstelrelback(istelrelback);
		selectedRowData.setTelrelbacktime(istelrelback ? Calendar.getInstance().getTime() : null);
		update.markUpdate(true, UpdateLevel.Data, "istelrelback");
		// }else{
		// MessageUtils.alert("抱歉,您没有放货权限!请确认您的权限,如有疑问请联系管理员!");
		// isput = !isput;
		// selectedRowData.setIsput(isput);
		// update.markUpdate(true, UpdateLevel.Data, "isput");
		// return;
		// }
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			istelrelback = istelrelback ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "istelrelback");
		}
	}

	@Action
	public void istelrel_oncheck() {
		// boolean put = false;
		// for (String s :
		// AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
		// if (s.endsWith("_isput")) {
		// put = true;
		// }
		// }
		// if(put){
		selectedRowData.setIstelrel(istelrel);
		selectedRowData.setTelreltime(istelrel ? Calendar.getInstance().getTime() : null);
		selectedRowData.setTelreler(istelrel ? AppUtils.getUserSession().getUsername() : null);
		update.markUpdate(true, UpdateLevel.Data, "istelrel");
		// }else{
		// MessageUtils.alert("抱歉,您没有放货权限!请确认您的权限,如有疑问请联系管理员!");
		// isput = !isput;
		// selectedRowData.setIsput(isput);
		// update.markUpdate(true, UpdateLevel.Data, "isput");
		// return;
		// }
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			istelrel = istelrel ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "istelrel");
		}
	}

	@Action
	public void saveContainer() {
		try {
			editGrid.commit();
			boolean isChangeFlag = false;
			// 如果列表里面修改的柜子，数据没有保存，先保存再处理后面数据 20190507
			if (modifiedData != null) {
				update(modifiedData);
				JSONArray jsonArray = (JSONArray) modifiedData;
				if (jsonArray.size() > 0)
					isChangeFlag = true;
			}
			if (addedData != null) {
				add(addedData);
				JSONArray jsonArray = (JSONArray) addedData;
				if (jsonArray.size() > 0)
					isChangeFlag = true;
			}
			if (removedData != null) {
				remove(removedData);
				JSONArray jsonArray = (JSONArray) removedData;
				if (jsonArray.size() > 0)
					isChangeFlag = true;
			}
			if (isChangeFlag) {
				editGrid.reload();
			}
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public String idsnum="";//记录勾选顺序
	
	@Bind
	@SaveState
	public boolean isAutoaort = false;

	@Action
	public void chooseContainer() {
		
		try{
			if(isAutoaort){
				String idsnums = idsnum.substring(0,idsnum.length()-1);
				//2429 柜子序号，点选择的时候，按勾选传入的顺序重新按1234复制
				serviceContext.busShipContainerMgrService.setOrdernoOrder(idsnums, selectedRowData.getJobid());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Long id = -1L;
		if (this.pkVal != null && this.pkVal > 0) {
			id = this.pkVal;
		} else if (this.selectedRowData.getId() > 0) {
			id = this.selectedRowData.getId();
		}
		try {
			this.serviceContext.busTrainMgrService.saveData(this.selectedRowData);
			this.serviceContext.busShipContainerMgrService.updateContainerSelects(this.editGrid.getSelectedIds(), id);
			this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_bus_train_container_createbillinfo('jobid="
							+ this.selectedRowData.getJobid() + ";');");
			this.serviceContext.userMgrService.sysCorporationDao
					.executeSQL("UPDATE bus_train set marksno = f_bus_train_cntinfo('billid="
							+ this.selectedRowData.getJobid()
							+ ";type="
							+ (isOneLine ? "1" : "2")
							+ "') WHERE id = "
							+ id
							+ ";");
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao.findOneRowByClauseWhere(sql);
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			isputmbl = selectedRowData.isIsputmbl();

			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.editGrid.reload();
			this.qryRefresh();
			alert("OK");
		} catch (InvalidDataAccessResourceUsageException e) {
			e.printStackTrace();
			MessageUtils.alert("请先保存!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	@SaveState
	public Boolean isOneLine = false;

	@Action
	public void reCreateCntInfo() {
		Long id = -1L;
		if (this.pkVal != null && this.pkVal > 0) {
			id = this.pkVal;
		} else if (this.selectedRowData.getId() > 0) {
			id = this.selectedRowData.getId();
		}
		this.serviceContext.userMgrService.sysCorporationDao
				.executeSQL("UPDATE bus_train set marksno = f_bus_shipping_cntinfo('billid="
						+ id
						+ ";type="
						+ (isOneLine ? "1" : "2")
						+ "') WHERE id = " + id + ";");
		String sql = " isdelete = false AND jobid =" + this.jobid;
		this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao.findOneRowByClauseWhere(sql);
		ishold = selectedRowData.isIshold();
		isput = selectedRowData.isIsput();
		isputmbl = selectedRowData.isIsputmbl();

		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		// this.qryRefresh();
		// alert("OK");
	}

	@Action
	public void reCreatePkgInfo() {
		Long id = -1L;
		if (this.pkVal != null && this.pkVal > 0) {
			id = this.pkVal;
		} else if (this.selectedRowData.getId() > 0) {
			id = this.selectedRowData.getId();
		}
		this.serviceContext.userMgrService.sysCorporationDao
				.executeSQL("UPDATE bus_train set goodsdesc = f_bus_shipping_goodsinfo('billid="
						+ id + "') WHERE id = " + id + ";");
		String sql = " isdelete = false AND jobid =" + this.jobid;
		this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao.findOneRowByClauseWhere(sql);
		ishold = selectedRowData.isIshold();
		isput = selectedRowData.isIsput();
		isputmbl = selectedRowData.isIsputmbl();

		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		// this.qryRefresh();
		// alert("OK");
	}

	@Action
	public void showHblSplit() {
		String url = "../train/bustrainbill.xhtml?id=" + this.jobid;
		AppUtils.openWindow("____", url);

	}

	@Accessible
	public int[] getGridSelIds() {
		Long id = -1L;
		if (jobid > 0) {
			id = jobid;
		} else if (this.selectedRowData.getJobid() != null
				&& this.selectedRowData.getJobid() > 0) {
			id = this.selectedRowData.getJobid();
		}
		String sql = "";
		if ("-1".equals(jobid)) {
			return null;
		} else {
			sql = "SELECT isselect FROM bus_ship_container WHERE isdelete = FALSE AND parentid IS NULL AND jobid = "
					+ id + "ORDER BY orderno";
		}
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if (map.get("isselect") != null
						&& (Boolean) map.get("isselect"))
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

	@Override
	public void qryRefresh() {
		// this.editGrid.repaint();
		super.qryRefresh();
	}

	@Bind
	public UIIFrame defineIframe;

	@Bind
	public UIIFrame emailIframe;

	

	public String getSavePath() {
		return System.getProperty("java.io.tmpdir");
	}

	// 临时保存系统生成可供下载文件路径
	@SaveState
	public String temFileUrl;

	@SaveState
	public String temFileUrl2;

	@Bind
	public UIButton exportshipping1;

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
		} else if ("2".equals(types)) {
			Browser.execClientScript("exc2()");
		} else {
			MessageUtils.alert("请先选择格式!");
		}
		showexportWindow.close();
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
				sbsql.append("\n SELECT cnortitle");
				sbsql.append("\n 	,cneetitle");
				sbsql.append("\n 	,notifytitle");
				sbsql.append("\n 	,pretrans");
				sbsql.append("\n 	,poa");
				sbsql.append("\n	,vessel");
				sbsql.append("\n 	,voyage");
				sbsql.append("\n	,pol");
				sbsql.append("\n 	,pdd");
				sbsql.append("\n 	,pod");
				sbsql.append("\n 	,destination");
				sbsql.append("\n 	,marksno");
				sbsql.append("\n 	,goodsdesc");
				sbsql.append("\n 	,grswgt");
				sbsql.append("\n	,cbm");
				sbsql.append("\n 	,agentitle");
				sbsql.append("\n 	,remark_booking");
				sbsql.append("\n 	,(SELECT COALESCE(a.namee,'')||f_newline()||COALESCE(a.namec) FROM sys_corporation a WHERE isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE  id ="
								+ AppUtils.getUserSession().getUserid()
								+ " AND isdelete = FALSE  AND x.corpid = a.id)) AS corporation");
				sbsql.append("\n FROM bus_train");
				sbsql.append("\n WHERE");
				sbsql.append("\n isdelete = FALSE");
				sbsql.append("\n AND id = " + this.selectedRowData.getId());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sbsql.toString());

				// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
				String exportFileName = "importTempleteForJobs.xlsx";
				String cfgFile = ConfigUtils
						.findSysCfgVal("bus_shipping_importTempleteForJobs");
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
						+ "ship"
						+ File.separator + exportFileName;
				// //System.out.println(fileUrl);
				// //System.out.println(fromFileUrl);
				if (!ReadExcel.exportJobsForExcel(new File(fromFileUrl), file,
						map)) {
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

	// 导出样单
	@Action
	public void exportshippinglist() {
		if (this.selectedRowData != null && this.selectedRowData.getId() > 0) {
			// 生成下载临时文件路径
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));
			fileUrl.append("JobsList");
			fileUrl.append(new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date()));
			fileUrl.append("_");
			fileUrl.append(AppUtils.getUserSession().getUsercode());
			fileUrl.append(".xls");
			this.temFileUrl2 = fileUrl.toString();
			File file = new File(fileUrl.toString());
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("\n SELECT b.cnortitle");
				sbsql.append("\n 	,b.cneetitle");
				sbsql.append("\n 	,b.notifytitle");
				sbsql.append("\n 	,b.claim_bill");
				sbsql.append("\n	,b.vessel");
				sbsql.append("\n 	,b.voyage");
				sbsql.append("\n	,b.pol");
				sbsql.append("\n 	,b.pdd");
				sbsql.append("\n 	,b.destination");
				sbsql.append("\n 	,b.marksno");
				sbsql.append("\n 	,b.piece");
				sbsql.append("\n 	,b.goodsdesc");
				sbsql.append("\n 	,b.grswgt");
				sbsql.append("\n	,b.cbm");
				sbsql
						.append("\n	,(select string_agg(COALESCE(cntno,''),',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS cntno ");
				sbsql
						.append("\n	,(select string_agg(COALESCE(sealno,''),',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS sealno ");
				sbsql
						.append("\n	,(select string_agg(COALESCE(piece,0)::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS piecee ");
				sbsql
						.append("\n	,(select string_agg(COALESCE(grswgt,0)::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS grswgtc ");
				sbsql
						.append("\n	,(select string_agg(COALESCE(cbm,0)::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS cbmc ");
				sbsql
						.append("\n	,(select string_agg(COALESCE(vgm,0)::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS vgm ");
				sbsql.append("\n FROM bus_train b");
				sbsql.append("\n WHERE");
				sbsql.append("\n b.isdelete = FALSE");
				sbsql.append("\n AND b.id = " + this.selectedRowData.getId());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sbsql.toString());
				// 导出样单,指定的模板，
				String exportFileName = "Jobslist.xls";

				// 模版所在路径
				String fromFileUrl = AppUtils.getHttpServletRequest()
						.getSession().getServletContext().getRealPath("")
						+ File.separator
						+ "upload"
						+ File.separator
						+ "ship"
						+ File.separator + exportFileName;
				if (!ReadExcel.exportJobsListForExcel(new File(fromFileUrl),
						file, map)) {
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

	@Action
	public void downloadTemplete() {
		// 导出托书模版
		String exportFileName = "importTempleteForJobs.xlsx";
		String cfgFile = ConfigUtils
				.findSysCfgVal("bus_shipping_importTempleteForJobs");
		if (!StrUtils.isNull(cfgFile)) {
			exportFileName = cfgFile;
		}
		this.temFileUrl = AppUtils.getHttpServletRequest().getSession()
				.getServletContext().getRealPath("")
				+ File.separator
				+ "upload"
				+ File.separator
				+ "ship"
				+ File.separator + exportFileName;
	}

	@Action
	public void downloadTempletelist() {
		// 导出样单模版
		String exportFileName = "Jobslist.xls";
		this.temFileUrl2 = AppUtils.getHttpServletRequest().getSession()
				.getServletContext().getRealPath("")
				+ File.separator
				+ "upload"
				+ File.separator
				+ "ship"
				+ File.separator + exportFileName;
	}

	@Bind(id = "downloadShipTemplete", attribute = "src")
	private File getDownload7() {
		return new File(temFileUrl);
	}

	@Bind(id = "downloadexportshipping", attribute = "src")
	private File getDownload6() {
		return new File(temFileUrl);
	}

	@Bind(id = "downloadexportshippinglist", attribute = "src")
	private File getDownload8() {
		return new File(temFileUrl2);
	}

	@Bind(id = "downloadShipTempletelist", attribute = "src")
	private File getDownload9() {
		return new File(temFileUrl2);
	}

	@Bind
	public UIIFrame uploadiframe;

	@Action
	public void openuploadwindow() {
		// System.out.println(AppUtils.getHttpServletRequest().getServletPath());

		uploadiframe.load("./uploadtemplete.aspx?method=importshipping&jobid="
				+ this.jobid);
		importFileWindow.show();
	}

	@Action
	public void importhbl() {
		if (this.job == null || this.job.getNos().isEmpty()) {
			MessageUtils.alert("请先生成工作单号!");
		} else {
			this.selectedRowData.setHblno(this.job.getNos().replace("-", "")
					.replace("/", "").replace("\\", ""));
		}
	}

	@Action
	public void closeFileWindowAction() {
		this.refresh();
	}

	@Action
	public void refreshCustomer() {
		sqlMy = "";
		qryCustomerKey = null;
		customerQry();
	}

	@Action
	public void refreshCustomerMy() {
		sqlMy = "AND (inputer ='" + AppUtils.getUserSession().getUsercode()
				+ "'OR updater ='" + AppUtils.getUserSession().getUsercode()
				+ "')";
		this.customerGrid.reload();
	}

	@Action
	public void deleteCustomer() {
		String[] ids = this.customerGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		for (String id : ids) {
			SysCorpcontacts cor = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao
					.findById(Long.parseLong(id));
			cor.setIsdelete(true);
			this.serviceContext.customerContactsMgrService.sysCorpcontactsDao
					.modify(cor);
		}
		this.customerGrid.reload();
	}

	@Bind
	public Boolean isprintmbl = false;

	@Bind
	public Boolean isreleasembl = false;

	@Bind
	public Boolean isgetmbl = false;

	@Action
	public void isprintmbl_oncheck() {
		selectedRowData.setIsprintmbl(isprintmbl);
		selectedRowData.setDateprintmbl(isprintmbl ? Calendar.getInstance()
				.getTime() : null);
		selectedRowData.setUserprintmbl(isprintmbl ? AppUtils.getUserSession()
				.getUsercode() : null);
		this.saveMaster();
		refresh();
	}

	/**
	 * 取单
	 */
	@Action
	public void isgetmbl_oncheck() {
		boolean getmbl = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs
				.getValue())) {
			if (s.endsWith("_mblget")) {
				getmbl = true;
			}
		}
		if (getmbl) {
			selectedRowData.setIsgetmbl(isgetmbl);
			selectedRowData.setDategetmbl(isgetmbl ? Calendar.getInstance()
					.getTime() : null);
			selectedRowData.setUsergetmbl(isgetmbl ? AppUtils.getUserSession()
					.getUsercode() : null);
			update.markUpdate(true, UpdateLevel.Data, "isgetmbl");
			update.markUpdate(true, UpdateLevel.Data, "getmbl");
		} else {
			MessageUtils.alert("无取单权限!");
			isgetmbl = !isgetmbl;
			selectedRowData.setIsgetmbl(isgetmbl);
			update.markUpdate(true, UpdateLevel.Data, "isgetmbl");
			return;
		}
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isgetmbl = isgetmbl ? false : true;
			selectedRowData.setDategetmbl(null);
			selectedRowData.setUsergetmbl(null);
			update.markUpdate(true, UpdateLevel.Data, "isgetmbl");
		}
	}

	@Action
	public void isreleasembl_oncheck() {
		boolean releasembl = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs
				.getValue())) {
			if (s.endsWith("_mblput")) {
				releasembl = true;
			}
		}
		if (releasembl) {
			selectedRowData.setIsreleasembl(isreleasembl);
			selectedRowData.setDatereleasembl(isreleasembl ? Calendar
					.getInstance().getTime() : null);
			selectedRowData.setUserreleasembl(isreleasembl ? AppUtils
					.getUserSession().getUsercode() : null);
			update.markUpdate(true, UpdateLevel.Data, "isreleasembl");
			update.markUpdate(true, UpdateLevel.Data, "releasembl");
		} else {
			MessageUtils.alert("无放单权限!");
			isreleasembl = !isreleasembl;
			selectedRowData.setIsreleasembl(isreleasembl);
			update.markUpdate(true, UpdateLevel.Data, "isreleasembl");
			return;
		}
		try {
			serviceContext.busTrainMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isreleasembl = isreleasembl ? false : true;
			selectedRowData.setDatereleasembl(null);
			selectedRowData.setUserreleasembl(null);
			update.markUpdate(true, UpdateLevel.Data, "isreleasembl");
		}
	}

	@Action
	public void refreshAjaxSubmit() {
		this.refresh();
	}

	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_cnts";
				String args = this.jobid + "," + this.pkVal + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.editGrid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Action
	public void exportContactNotice() {
		String arg = "&para=jobid=" + this.jobid + ":userid="
				+ AppUtils.getUserSession().getUserid() + ":corpidcurrent="
				+ AppUtils.getUserSession().getCorpid() + ":";
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/bus/contact_notice.raq";
		String newRaq = ConfigUtils
				.findSysCfgVal("bus_shipping_contact_notice");
		String newOpenUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/bus/" + newRaq;
		if (newRaq != null && newRaq != "") {
			AppUtils.openWindow("_apAllCustomReport", newOpenUrl + arg);
		} else {
			AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		}
	}

	@Action
	public void saveMaster() {
		try {
			doServiceSaveMaster(); // Master
//			Browser.execClientScript("parent.refreshAjaxPanel.submit();");
//			String querySql="update wms_in set customerno="+customerno+" where sono='"+selectedRowData.getSono()+"'";
//			this.serviceContext.busBillnoMgrService.busBillnoMgrDao.executeSQL(querySql);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		refresh();
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		//2632 查日志表记录这个提示信息(日志录入时间，间隔为10秒之内的一条，避免有多条的情况显示到历史的记录)，用alert方式显示出来。
		String sql = "SELECT remarks FROM bus_optrack WHERE jobid = "+jobid+" AND opusr = '"+AppUtils.getUserSession().getUsercode()+"' " +
					"AND optime > now() - interval '10 second' AND opdesc LIKE '生成栏目值[修改委托资料自动同步运价异常]%' LIMIT 1;";
		try{
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.size()>0){
				MessageUtils.alert(m.get("remarks").toString());
			}
		}catch(NoRowException e){
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Action
	public void saveMasterMbl() {
		saveMaster();
	}

	@Action
	public void saveMasterRemarks() {
		saveMaster();
	}

	@Bind
	@SaveState
	public Long pkValCnt;

	@Action
	public void edit() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		try {
			this.pkValCnt = getGridSelectId();
			doServiceFindData();
			this.refreshForm();
			if (editWindow != null)
				editWindow.show();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		} catch (NumberFormatException e) {
			MessageUtils.alert("新增条目请先保存!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void update(Object modifiedData) {
		serviceContext.busShipContainerMgrService
				.updateBatchEditGrid(modifiedData);
	}

	@Override
	protected void add(Object addedData) {
		serviceContext.busShipContainerMgrService.addBatchEditGrid(addedData,
				jobid, selectedRowData.getId());
	}

	@Override
	protected void remove(Object removedData) {
		serviceContext.busShipContainerMgrService
				.removedBatchEditGrid(removedData);
	}

	@Action
	public void addContainer() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setShipid(this.selectedRowData.getId());

		try {
			List<BusShipContainer> temps = serviceContext.busShipContainerMgrService.busShipContainerDao
					.findAllByClauseWhere("jobid = "
							+ this.jobid
							+ " AND isdelete = false AND parentid IS NULL ORDER BY orderno");

			if (addedData != null) {
				JSONArray jsonArray = (JSONArray) addedData;
				if (jsonArray != null && jsonArray.size() > 0) {
					JSONObject jsonObject = (JSONObject) jsonArray
							.get(jsonArray.size() - 1);
					// System.out.println("jsonObject:"+jsonObject);
					if (jsonObject.get("cntypeid") != null
							&& !String.valueOf(jsonObject.get("cntypeid"))
									.isEmpty()) {
						dtlData.setCntypeid(Long.parseLong(String
								.valueOf(jsonObject.get("cntypeid"))));
					} else {
						dtlData.setCntypeid(null);
					}
					if (jsonObject.get("ldtype") != null
							&& !String.valueOf(jsonObject.get("ldtype"))
									.isEmpty()) {
						dtlData.setLdtype(String.valueOf(jsonObject
								.get("ldtype")));
					} else {
						dtlData.setLdtype(null);
					}
					if (jsonObject.get("packagee") != null
							&& !String.valueOf(jsonObject.get("packagee"))
									.isEmpty()) {
						dtlData.setPackagee(String.valueOf(jsonObject
								.get("packagee")));
					} else {
						dtlData.setPackagee(null);
					}
					if (temps == null || temps.size() <= 0) {
						dtlData.setOrderno(jsonArray.size() + 1);
					} else {
						dtlData.setOrderno(temps.size() + jsonArray.size() + 1);
					}
					if (jsonObject.get("grswgt2") != null
							&& !String.valueOf(jsonObject.get("grswgt2"))
									.isEmpty()) {
						dtlData.setGrswgt(new BigDecimal(String
								.valueOf(jsonObject.get("grswgt2"))));
					} else {
						dtlData.setGrswgt(new BigDecimal(0));
					}
					if (jsonObject.get("cbm2") != null
							&& !String.valueOf(jsonObject.get("cbm2"))
									.isEmpty()) {
						dtlData.setCbm(new BigDecimal(String.valueOf(jsonObject
								.get("cbm2"))));
					} else {
						dtlData.setCbm(new BigDecimal(0));
					}
					if (jsonObject.get("vgm") != null
							&& !String.valueOf(jsonObject.get("vgm")).isEmpty()) {
						dtlData.setVgm(new BigDecimal(String.valueOf(jsonObject
								.get("vgm"))));
					} else {
						dtlData.setVgm(new BigDecimal(0));
					}
					if (jsonObject.get("piece2") != null
							&& !String.valueOf(jsonObject.get("piece2"))
									.isEmpty()) {
						dtlData.setPiece(new Integer(String.valueOf(jsonObject
								.get("piece2"))));
					} else {
						dtlData.setPiece(new Integer(0));
					}
				} else {
					String[] ids = editGrid.getSelectedIds();
					if (ids != null && ids.length > 0) {// 找到选择的最后一行
						String id = ids[ids.length - 1];
						if (StrUtils.isNumber(id)) {
							BusShipContainer temp = serviceContext.busShipContainerMgrService.busShipContainerDao
									.findById(Long.valueOf(id));
							dtlData.setCntypeid(temp.getCntypeid());
							dtlData.setLdtype(temp.getLdtype());
							dtlData.setPackagee(temp.getPackagee());
							dtlData.setGrswgt(temp.getGrswgt());
							dtlData.setPiece(temp.getPiece());
							dtlData.setCbm(temp.getCbm());
							dtlData.setVgm(temp.getVgm());
						}
					} else {
						try {// 没有勾选的情况下，找到最后一行
							if (temps != null && temps.size() > 0) {
								dtlData.setCntypeid(temps.get(temps.size() - 1)
										.getCntypeid());
								dtlData.setLdtype(temps.get(temps.size() - 1)
										.getLdtype());
								dtlData.setPackagee(temps.get(temps.size() - 1)
										.getPackagee());
								dtlData.setGrswgt(temps.get(temps.size() - 1)
										.getGrswgt());
								dtlData.setPiece(temps.get(temps.size() - 1)
										.getPiece());
								dtlData.setCbm(temps.get(temps.size() - 1)
										.getCbm());
								dtlData.setVgm(temps.get(temps.size() - 1)
										.getVgm());
							} else {
								dtlData.setLdtype(selectedRowData.getLdtype());
								dtlData
										.setPackagee(selectedRowData
												.getPacker());
								dtlData.setGrswgt(new BigDecimal(0));
								dtlData.setPiece(0);
								dtlData.setCbm(new BigDecimal(0));
								dtlData.setVgm(new BigDecimal(0));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					dtlData.setOrderno(temps.size() + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		editGrid.appendRow(dtlData);
	}

	@Action
	public void containeradd() {
		BusShipContainer old = dtlData;
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setLdtype(old.getLdtype());
		dtlData.setCntypeid(old.getCntypeid());
		dtlData.setPackagee(old.getPackagee());
		this.dtlData.setShipid(this.selectedRowData.getId());
	}

	@Action
	public void addContainerwindow() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setLdtype(selectedRowData.getLdtype());
		dtlData.setPackagee(selectedRowData.getPacker());
		this.dtlData.setShipid(this.selectedRowData.getId());
		this.pkValCnt = -1L;
		if (editWindow != null)
			editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkValCnt");
	}

	@Action
	public void saveAndAdd() {
		save1();
		containeradd();
	}

	private void updateChooseship() {
		Long id = -1L;
		if (this.pkVal != null && this.pkVal > 0) {
			id = this.pkVal;
		} else if (this.selectedRowData.getId() > 0) {
			id = this.selectedRowData.getId();
		}
		try {
			this.serviceContext.userMgrService.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow("SELECT f_bus_shipping_container_createbillinfo('shipid="
							+ this.selectedRowData.getId() + ";');");
			String sql1 = "UPDATE bus_train set marksno = f_bus_shipping_cntinfo('billid="
					+ this.selectedRowData.getJobid()
					+ ";type="
					+ (isOneLine ? "1" : "2") + "') WHERE id = " + id + ";";
			this.serviceContext.userMgrService.sysCorporationDao
					.executeSQL(sql1);
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busTrainMgrService.busTrainDao.findOneRowByClauseWhere(sql);
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			isputmbl = selectedRowData.isIsputmbl();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIIFrame containerIframe;

	@Bind
	public UIWindow showContainerWindow;

	@Bind
	public UIIFrame dangersIframe;

	@Bind
	public UIWindow showDangersWindow;

	/**
	 * 危险柜明细
	 * */
	@Action
	public void qryDangers() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		String id = ids[0];
		Long cid = Long.valueOf(id);
		dangersIframe.load("../ship/dangersdetail.xhtml?cid=" + cid);
		showDangersWindow.show();
	}

	/**
	 * 装箱单明细
	 * */
	@Action
	public void qryContainer() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		String id = ids[0];
		Long cid = Long.valueOf(id);
		containerIframe.load("../stock/containerdetail.xhtml?cid=" + cid);
		showContainerWindow.show();
	}

	public void getwebsite() {
		if (this.selectedRowData != null
				&& this.selectedRowData.getCarrierid() != null
				&& this.selectedRowData.getCarrierid() > 0) {
			SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao
					.findById(this.selectedRowData.getCarrierid());
			this.website = sysCorporation.getHomepage();
			update.markUpdate(true, UpdateLevel.Data, "website");
		}
	}

	@Bind
	public String informationText;

	@Action
	public void showBasicinformation() {
		try {
			String sql = "SELECT * FROM f_fina_jobs_baseinfo('jobid="
					+ selectedRowData.getJobid() + "') AS information";
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			informationText = m.get("information").toString();
		} catch (Exception e) {
			informationText = "";
		}
		Browser.execClientScript("informationWindow.show();");
		update.markUpdate(true, UpdateLevel.Data, "informationText");
	}

	@Action
	public void podajaxSubmit() {
		String podid = AppUtils.getReqParam("podid").toString();
		if (!StrUtils.isNull(podid)) {
			// 2221 目的港带航线地方修改 如果港口里面有设置二级航线，这里显示二级航线，没有二级航线则显示港口的航线
			String sql = "SELECT code FROM dat_line x WHERE EXISTS(SELECT 1 FROM dat_port "
					+ "WHERE (CASE WHEN line2<>'' AND line2 IS NOT NULL THEN line2 = x.namec ELSE line = x.namec END) AND id ="
					+ podid + ")";
			try {
				Map m = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				if (m != null && m.get("code") != null) {
					this.selectedRowData.setRoutecode(m.get("code").toString());
					update.markUpdate(UpdateLevel.Data, "routecode");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 2201 提单港口包含国家 =Y时
			// ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
			String findSysCfgVal = ConfigUtils
					.findSysCfgVal("bill_port_connect_country");
			if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
				String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
						+ "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
						+ podid + ") LIMIT 1";
				try {
					Map m = serviceContext.daoIbatisTemplate
							.queryWithUserDefineSql4OnwRow(sql1);
					if (m != null && m.get("namee") != null) {
						Browser
								.execClientScript("podJsvar.setValue(podJsvar.getValue()+','+'"
										+ m.get("namee").toString() + "')");
					}
				} catch (NoRowException e) {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Action
	public void polajaxSubmit() {
		String polid = AppUtils.getReqParam("polid").toString();
		// 2201 提单港口包含国家 =Y时
		// ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils
				.findSysCfgVal("bill_port_connect_country");
		if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
					+ "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
					+ polid + ") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser
							.execClientScript("polJsvar.setValue(polJsvar.getValue()+','+'"
									+ m.get("namee").toString() + "')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Action
	public void destinationajaxSubmit() {
		String destinationid = AppUtils.getReqParam("destinationid").toString();
		// 2201 提单港口包含国家 =Y时
		// ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils
				.findSysCfgVal("bill_port_connect_country");
		if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
					+ "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
					+ destinationid + ") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser
							.execClientScript("destinationJs.setValue(destinationJs.getValue()+','+'"
									+ m.get("namee").toString() + "')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Action
	public void pddajaxSubmit() {
		String pddid = AppUtils.getReqParam("pddid").toString();
		// 2201 提单港口包含国家 =Y时
		// ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils
				.findSysCfgVal("bill_port_connect_country");
		if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
					+ "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
					+ pddid + ") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser
							.execClientScript("pddJsvar.setValue(pddJsvar.getValue()+','+'"
									+ m.get("namee").toString() + "')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *主柜信息
	 * 
	 * @return
	 */
	@Bind(id = "parentCnt")
	public List<SelectItem> getParentCnt() {
		try {
			// ApplicationUtils.debug(this.dtlData.getBookdtlid());
			return CommonComBoxBean
					.getComboxItems(
							"x.id",
							"COALESCE(x.cntypedesc,'') ||':'|| COALESCE(x.cntno,'')",
							"_bus_ship_container AS x",
							"WHERE x.isdelete = false and x.jobid = ANY(SELECT j.parentid FROM fina_jobs j WHERE j.id = "
									+ this.jobid + ")", "ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	/**
	 *手动输入的目的港查出对应的航线代码
	 * 
	 * @return
	 */
	@Action
	public void linejaxSubmit() {
		String line = AppUtils.getReqParam("line").toString();
		String sql2 = "namec = '" + line + "'";
		List<DatLine> datLine = serviceContext.lineMgrService.datLineDao.findAllByClauseWhere(sql2);
		if(datLine != null && datLine.size()>0){
			this.selectedRowData.setRoutecode(datLine.get(0).getCode());
		}
		update.markUpdate(UpdateLevel.Data, "routecode");
	}
	
	@Bind
	public UIDataGrid putgrid;
	
	@Bind
	public UIWindow setputnosWindow;
	
	
	
	
	@Bind(id = "putgrid", attribute = "dataProvider")
	protected GridDataProvider getBookDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".putgrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
				.queryForList(sqlId, getQryClauseWhereBook(putqryMap)).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".putgrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhereBook(putqryMap));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	
	@SaveState
	@Accessible
	public Map<String, Object> putqryMap = new HashMap<String, Object>();
	
	public Map getQryClauseWhereBook(Map<String, Object> queryMap) {
		Map map = new HashMap();
		String qry = "\n 1=1";
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void settelrelnos() {
		//this.putgrid.reload();
		//setputnosWindow.show();
		UUID id=UUID.randomUUID();
		long userids = AppUtils.getUserSession().getUserid();
		SysUser us = serviceContext.userMgrService.sysUserDao.findById(userids);
		String namee = us.getNamee();
		String putno = (id.toString().replace("-", "")).substring(0, 14).toUpperCase();
		Browser.execClientScript("telrelnosJs.setValue('"+putno+"')");
		Browser.execClientScript("telrelerJs.setValue('"+namee+"')");
	
	}
	
	@Action
	public void putgrid_ondblclick() {
		choosePutnos();
	}
	
	@SaveState
	public String codes = "jobs_train_putno";
	
	@Action
	public void choosePutnos(){
		String[] ids = this.putgrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String querySql  = "SELECT code FROM sys_busnodesc WHERE id = "+ids[0];
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			codes = StrUtils.getMapVal(m, "code");
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		if(!StrUtils.isNull(selectedRowData.getTelrelnos())){
			alert("电放号已生成");
			setputnosWindow.close();
			return;
		}
		try {
			
			String querySqlw = "SELECT f_auto_busno('code="+codes+";date="+this.job.getJobdate()+";corpid="+selectedRowData.getCorpid()+";') AS putno;";
			//System.out.println("querySqlw:"+querySqlw);
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySqlw);
			String putno = StrUtils.getMapVal(map, "putno");
			Browser.execClientScript("telrelnosJs.setValue('"+putno+"')");
			setputnosWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void getcustomerno() {
		if(!StrUtils.isNull(selectedRowData.getSono())){
			try {
//				String querySql="select customerno from wms_in  where isdelete = false AND sono='"+selectedRowData.getSono()+"' LIMIT 1";
//				Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
//				customerno = (String) m.get("customerno");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
