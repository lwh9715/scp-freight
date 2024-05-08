package com.scp.view.module.ship;

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
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import com.scp.util.*;
import org.apache.commons.lang.StringUtils;
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
import com.scp.model.data.DatGoodstype;
import com.scp.model.data.DatLine;
import com.scp.model.data.DatPort;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipBooking;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysEmail;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.schedule.AutoImportDocument;
import com.scp.service.data.PortyMgrService;
import com.scp.view.comp.EditGridFormView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;
import com.scp.view.module.somgr.SonoBean;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * @author Administrator
 *
 */
@ManagedBean(name = "pages.module.ship.busshippingBean", scope = ManagedBeanScope.REQUEST)
public class BusShippingBean extends EditGridFormView {

	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@SaveState
	@Accessible
	public BusShipping selectedRowData = new BusShipping();

	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();

	@Autowired
	public ApplicationConf applicationConf;

	@SaveState
	@Bind
	protected String CSNO = "";

	@SaveState
	@Accessible
	@Bind
	public String bookingid;

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
	public UIDataGrid gridShipschedule;

	@Bind
	public UIButton saveMasterRemarks;

	@Bind
	public UIButton saveMasterMbl;

	@Bind
	public UIButton saveMaster;

	@Bind
	public UIButton chooseShip;

	@Bind
	public UIButton save;

	@Bind
	public UIButton save1;

	@Bind
	public UIButton saveDetails;

	@Bind
	public UIButton save2;

	@Bind
	public UIButton saveForm;

	@Bind
	public UIButton openuploadwindow;

	@Bind
	public UIButton vgmCreat;

	@SaveState
	private BusShipSchedule shipschedule;

	@Bind
	@SaveState
	@Accessible
	public Long linkid;

	public Long userid;

	@Accessible
	public SysEmail sysEmail = new SysEmail();

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
	public String website;// 船公司网站地址

	@Bind
	public String issaas;

	@Bind
	public char ishuazhan = '0';

	@Bind
	@SaveState
	public String existap = "Y";

	@Bind
	@SaveState
	public String actionJsText;// 按不同公司自定义js从 sys_formcfg 获取

	@SaveState
	public String agentcompay = "";

	@SaveState
	public String priceuser = "";

	@Bind
	@SaveState
	public String cntdesc = "";
	
	@Bind
	@SaveState
	public String checkcntno; //检查箱号

	@Bind
	@SaveState
	public String hblagentmodify; //HBL代理修改

	@Bind
	@SaveState
	public String pdd_pod; //卸港同步目的港、目的地

	@Bind
	@SaveState
	public String uplatUrl;

	@Bind
	@SaveState
	public String syscfg_etd_atd;

	public String confirmsononumber = "0";
	
	
	@Bind
	@SaveState
	public String isexternalmatching = "";

	@Bind
	@SaveState
	public String sonopiliang;

	@Bind
	@SaveState
	public String printingcode = "";

	@Override
	public void beforeRender(boolean isPostBack) {
		//SynUfmsAndBms syn = new SynUfmsAndBms();
		//syn.synStart();
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
				if (chooseShip != null)
					chooseShip.setDisabled(true);
				saveMasterMbl.setDisabled(true);
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
				//System.out.println(js);
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

			if (selectedRowData.getPriceuserid() != null && this.selectedRowData.getPriceuserid() > 0) {
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
				String username = us.getNamec();
				this.priceuser = (username != null && !username.equals("") ? username:us.getCode());
			}

			checkcntno = ConfigUtils.findSysCfgVal("bus_ship_container_checkcntno");
			pdd_pod = ConfigUtils.findSysCfgVal("pdd_pod_destination");
			uplatUrl = ConfigUtils.findSysCfgVal("uplat_url");
			hblagentmodify = ConfigUtils.findSysCfgVal("hbl_agent_modify");
			syscfg_etd_atd = ConfigUtils.findSysCfgVal("sys_cfg_etd_update_atd");

			update.markUpdate(true, UpdateLevel.Data, "checkcntno");
			update.markUpdate(true, UpdateLevel.Data, "hblagentmodify");
			update.markUpdate(true, UpdateLevel.Data, "pdd_pod");
			update.markUpdate(true, UpdateLevel.Data, "uplatUrl");
			CSNO = ConfigUtils.findSysCfgVal("CSNO");

			//提单类型锁定+箱量箱型可读
			String querySql = "select COUNT(1) as c,COALESCE((SELECT f_fina_jobs_cntdesc('jobid="+this.jobid+ "')),'') AS cntdesc from fina_arap a where a.jobid = "+this.jobid+ " AND a.isdelete = false AND a.vchdtlid > 0";
			
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				if(Integer.valueOf(String.valueOf(m.get("c")))>0){
					Browser.execClientScript("bltypeJs.setReadOnly(true);");
					Browser.execClientScript("parent.bltypeJsisshowJsVar.setValue('hidden');");
				}
				cntdesc = String.valueOf(m.get("cntdesc"));
				Browser.execClientScript("cntdescJsVar.setValue('"+cntdesc+"');");
				Browser.execClientScript("cntdescJsVar.setStyle('background:#d0e5ea;border: none;');");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//提单类型
//			String querySql = "pages.module.finance.arapeditBean.grid.page";
//			Map args = getQryClauseWherearap(new HashMap());
//			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
//			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
//			for (Map map : list) {
//				String yearperiod = String.valueOf(map.get("yearperiod")).replace("null", "");
//				// yearperiod = "2020-10";
//				if (!StrUtils.isNull(yearperiod)) {
//					Browser.execClientScript("bltypeJs.setReadOnly(true);");
//					Browser.execClientScript("parent.bltypeJsisshowJsVar.setValue('hidden');");
//				}
//			}
		}
	}

	public Map getQryClauseWherearap(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);

		// neo 控制费用显示
		String filterJobs = "(EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"') x WHERE x.id = t.id)" +
				" OR (t.corpid = 157970752274 AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = " + this.jobid + " AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + AppUtils.getUserSession().getUserid() + " and ischoose = true))))";
		//filterJobs += "\nAND (t.jobid = " + this.jobid + " OR EXISTS(SELECT 1 FROM fina_jobs x where x.parentid = " + this.jobid + " AND x.id = t.jobid AND x.isdelete = false))";
		// neo 优化写法 20200331
		filterJobs += "\nAND t.jobid = ANY(SELECT " + this.jobid + " UNION (SELECT x.id FROM fina_jobs x where x.parentid = " + this.jobid + " AND x.isdelete = false))";
		map.put("jobs", filterJobs);

		if (true) {// 显示合计，不显示明细
			map.put("isShowJoin", "(t.parentid IS NULL)");
		} else {// 显示明细，不显示合计
			map.put("isShowJoin", "(rptype <> 'O')");
		}
		String orderby = ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime");
		if(StrUtils.isNull(orderby) || "N".equals(orderby)){
			map.put("orderSql", "");
		}else{
			map.put("orderSql", ",t.inputtime");
		}

		return map;
	}

	public void showHMnumber() {
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow("SELECT count(*) FROM bus_ship_bill WHERE isdelete = FALSE AND bltype = 'H' AND jobid = "
						+ this.jobid.toString());
		hblNumber = m.get("count").toString();
		Map mn = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow("SELECT count(*) FROM bus_ship_bill WHERE isdelete = FALSE AND bltype = 'M' AND jobid = "
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


		String sql = "select f_checkright('vgmshow',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as vgmshow";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("vgmshow") != null&& m.get("vgmshow").toString().equals("true")) {
				vgmCreat.setDisabled(true);
			}
		} catch (Exception e) {
		}
	}

	public void init() {
		String id = AppUtils.getReqParam("id").trim();
		if(!StrUtils.isNull(id)){
			jobid = Long.valueOf(id);
		}
		this.job = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.jobid);
		if (this.job != null && !this.job.getIslock()) {
			openuploadwindow.setDisabled(false);
		} else {
			openuploadwindow.setDisabled(true);
		}
		if (this.job != null && this.job.getIscheck()) {
			Browser.execClientScript("ischeckedited();");
		}

		this.customerid = job.getCustomerid();
		// 查找该用户是不是华展宁波分公司的
		String sql = "SELECT f_sys_getcsno() = '2199' AND EXISTS(SELECT 1 FROM sys_user x WHERE id = "
				+ AppUtils.getUserSession().getUserid()
				+ "AND EXISTS(SELECT 1 FROM sys_corporation WHERE code = 'NBFGS' AND isdelete = FALSE AND id = x.corpid)) AS isnb";
		try {
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("isnb") != null
					&& m.get("isnb").toString().equals("true")) {
				ishuazhan = '1';
			}
		} catch (Exception e) {
			ishuazhan = '0';
		}

		// 是否存在导入的运费
		String sqlfk = "SELECT f_sys_getcsno() = '2199' AND EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = false AND araptype ='AP' AND fktbl is not null AND fktbl <> '' AND jobid = "
				+ this.jobid + ") AS fktblarap";
		try {
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sqlfk);
			if (m != null && m.get("fktblarap") != null
					&& m.get("fktblarap").toString().equals("true")) {
				existap = "Y";
			} else {
				existap = "N";
			}
		} catch (Exception e) {
			existap = "N";
		}

		List<BusShipBooking> busShipBookingList = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("jobid = '" + jobid + "'  AND isdelete = false");
		if (!busShipBookingList.isEmpty() && busShipBookingList.size() == 1) {
			bookingid = String.valueOf(busShipBookingList.get(0).getId());
		} else {
			bookingid = "0";
		}

		getGridVesVoyData();
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
		this.selectedRowData = new BusShipping();
		// this.mPkVal = -1l;
		this.pkVal = -1l;
		// super.addMaster();
	}

	@Action
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			serviceContext.busShippingMgrService.removeDate(selectedRowData
					.getId());

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
					BusShipContainer busShipContainer = this.serviceContext.busShipContainerMgrService.busShipContainerDao.findById(Long.valueOf(s));
					String sql = "UPDATE bus_ship_book_cnt SET jobid = null WHERE id in " +
							"(select bsbc.id from bus_ship_booking bsb left join bus_ship_book_cnt bsbc on bsb.id=bsbc.linkid where bsb.isdelete=false and " +
							" bsb.sono='" + busShipContainer.getSono() + "' and bsbc.cntno = '" + busShipContainer.getCntno() + "' and bsbc.jobid= " + busShipContainer.getJobid() + " ); ";
					this.serviceContext.busShipContainerMgrService.busShipContainerDao.executeSQL(sql);

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
			if (!ishaveSono0()) {
				return;
			}

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
			if (!ishaveSono0()) {
				return;
			}

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
			if (!ishaveSono0()) {
				return;
			}

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

	/**
	 * 保存VGM信息
	 */
	@Action
	protected void saveVgm() {
		saveTemperature();
	}


	/**
	 * Neo 2014/5/7 延船
	 */
	/*
	 * @Action public void delayShip() { String winId = "_ship_delay"; String
	 * url = "./busshipdelay.xhtml?id=" + this.mPkVal; AppUtil.openWindow(winId,
	 * url); }
	 *//**
	 * Neo 2014/5/8 甩柜
	 */
	/*
	 * @Action public void dropCnt() { String winId = "_drop_cnt"; String url =
	 * "./busshipdropcnt.xhtml?id=" + this.mPkVal; AppUtil.openWindow(winId,
	 * url); }
	 */

	@Override
	public void refresh() {
		super.refresh();
		refreshMasterForm();
	}

	@Action
	public void refreshMaster() {
		refresh();
	}



	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "\nAND jobid = " + jobid;
		m.put("filter",filter);
		return m;
	}

	@SaveState
	public String contractidnamec = "";

	@Action
	public void refreshMasterForm() {

		try {
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao
					.findOneRowByClauseWhere(sql);
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			isunder = selectedRowData.isIsunder();
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
			selectedRowData = new BusShipping();
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
			Browser.execClientScript("$('#priceuser_input').val('" + sysUser.getNamec() + "')");
		} else {
			Browser.execClientScript("$('#priceuser_input').val('')");
		}
		getwebsite();
		showTipsCount();

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

	@Action
	public void showTipsCount(){
		if(this.pkVal > 0){
			String messageTips = serviceContext.busShippingMgrService.findTipsCount(jobid);
			if(!StrUtils.isNull(messageTips)){
				Browser.execClientScript("showButtonTips('showRemarks',"+messageTips+")");
			}
		}
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
		} else if ("4".equals(porttype)) {
			portsql = "AND ispod = TRUE";
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
		}/*else if ("4".equals(porttype)) {
			this.selectedRowData.setPddid((Long) m.get("id"));
			this.selectedRowData.setPdd((String) m.get("namee"));
			this.selectedRowData.setPddcode((String) m.get("code"));
			this.update.markUpdate(UpdateLevel.Data, "potid");
			this.update.markUpdate(UpdateLevel.Data, "pot");
			this.update.markUpdate(UpdateLevel.Data, "potcode");
		}*/
		showPortWindow.close();
	}

	// @Action
	// public void chooseBook() {
	//
	// String[] ids = this.editGrid.getSelectedIds();
	// if(ids==null||ids.length==0||ids.length>1){
	// MsgUtil.alert("请选择单行记录");
	// return;
	// }
	// String url = AppUtil.getContextPath() +
	// "/pages/module/ship/shipbookingchoose.xhtml?containerid="+ids[0]+"&shipid="+this.mPkVal;
	// dtlIFrame.setSrc(url);
	// update.markAttributeUpdate(dtlIFrame, "src");
	// update.markUpdate(true, UpdateLevel.Data, dtlDialog);
	// dtlDialog.show();
	// }

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
//		wfImporPackingList(this.selectedRowData.getJobid(),
//				WorkFlowEnumerateShip.SHIPPINGMAIL);
		String url = AppUtils.getContextPath()
				+ "/pages/sysmgr/mail/emailsendedit.xhtml?type=shipping&id="
				+ this.pkVal;
		AppUtils.openWindow("_sendMail_shipping", url);
	}

	@Bind(id = "gridShipschedule", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@SuppressWarnings("deprecation")
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.shipcosteditBean.gridShipschedule.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();

			}

			@SuppressWarnings("deprecation")
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.shipcosteditBean.gridShipschedule.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		return super.getQryClauseWhere(queryMap);
	}

	@Action
	public void clearQryKeysc() {
		if (qryMapShip != null) {
			qryMapShip.clear();
			update.markUpdate(true, UpdateLevel.Data, "shipschedulePanel");
			this.gridShipschedule.reload();
		}
	}

	@Action
	public void qryRefreshShip() {
		this.gridShipschedule.reload();
	}

	@Action
	public void qryRefreshShip2() {
		this.gridShipschedule.reload();
	}

	@Action
	public void chooseShip() {
		shipscheduleWindow.show();
		this.gridShipschedule.reload();
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

	/**
	 *
	 */
	@Action
	public void gridShipschedule_ondblclick() {
		try {
			this.selectedRowData.setScheduleid(Long.valueOf(this.gridShipschedule.getSelectedIds()[0]));
			setShipschedule();
			this.selectedRowData.setScheduleYear(this.shipschedule.getYearno());
			this.selectedRowData.setScheduleMonth(this.shipschedule.getMonthno());
			this.selectedRowData.setScheduleWeek(this.shipschedule.getWeekno());
			this.selectedRowData.setCarrierid(getCarrierid(this.shipschedule.getCarrier()));
			this.selectedRowData.setVessel(this.shipschedule.getVes());
			this.selectedRowData.setVoyage(this.shipschedule.getVoy());
			this.selectedRowData.setCls(this.shipschedule.getCls());
			this.selectedRowData.setEtd(this.shipschedule.getEtd());
			this.selectedRowData.setEta(this.shipschedule.getEta());
			this.selectedRowData.setVescode(this.shipschedule.getVescode());
			this.selectedRowData.setClstime(this.shipschedule.getCy());
			this.selectedRowData.setVgmdate(this.shipschedule.getVgm());


			//用船期的航线查出对应的航线代码赋值给委托中的航线
			if(!StrUtils.isNull(this.shipschedule.getShipline())){
				String sql2 = "namec = '" + this.shipschedule.getShipline() + "'";
				List<DatLine> datLine = serviceContext.lineMgrService.datLineDao.findAllByClauseWhere(sql2);
				if(datLine != null && datLine.size() > 0){
					this.selectedRowData.setRoutecode(datLine.get(0).getCode());
				}
			}

			// 如果 id找不到 ,那么代码都不设置 在报价审核生成工作单的触发器 也有相应处理
			if (getPolorPodId(this.shipschedule.getPol()) == null) {

			} else {
				String sql = "namee = '" + this.shipschedule.getPol() + "'";
				List<DatPort> datPort = portyMgrService.datPortDao
						.findAllByClauseWhere(sql);
				this.selectedRowData.setPolcode(datPort.get(0).getCode());
				this.selectedRowData.setPol(this.shipschedule.getPol());
				this.selectedRowData.setPolid(getPolorPodId(this.shipschedule
						.getPol()));
				// Browser.execClientScript("refreshRouteInfo('"+datPort.get(0).getLine()+"')");
			}
			if (getPolorPodId(this.shipschedule.getPod()) == null) {

			} else {
				String sql = "namee = '" + this.shipschedule.getPod() + "'";
				List<DatPort> datPort = portyMgrService.datPortDao
						.findAllByClauseWhere(sql);
				this.selectedRowData.setPodcode(datPort.get(0).getCode());
				this.selectedRowData.setPod(this.shipschedule.getPod());
				this.selectedRowData.setPodid(getPolorPodId(this.shipschedule
						.getPod()));

				if (this.selectedRowData.getPddid() == null) {
					this.selectedRowData.setPddcode(datPort.get(0).getCode());
					this.selectedRowData.setPdd(this.selectedRowData.getPod());
					this.selectedRowData.setPddid(this.selectedRowData.getPodid());
				} else {

				}
			}

			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			Browser.execClientScript("refreshScheduleChoose()");
			shipscheduleWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

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

//		wfImporPackingList(this.selectedRowData.getJobid(),
//				WorkFlowEnumerateShip.BUSREPORT);

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
						+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=C;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
						;
				Browser.execClientScript("cneename.focus");

				// 发货人
			} else if ("1".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'S' "
						+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						// 或者对应客户勾选了发货人
						+ this.customerid + " OR (ishipper = TRUE AND salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=S;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
						;
				Browser.execClientScript("cnorname.focus");
				// 通知人
			} else if ("2".equals(custype)) {
				sql = " AND contactype = 'B' AND (contactype2 = 'N' OR contactype2 = 'C') "
						+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
						+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)"
						+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=N;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
						;
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
					+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=H;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
					;
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
			sql = " AND contactype = 'B' AND (contactype2 = 'O' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))"
				+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=S;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";
			Browser.execClientScript("cnortitlemblname.focus");
			// MBL收货人
		} else if ("2".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'P' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))"
					+ " OR (contactype2 = 'C' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isconsignee = true AND x.id = customerid AND x.isdelete = false)))"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=C;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";
			Browser.execClientScript("cneetitlemblname.focus");
		}// MBL通知人
		else if ("3".equals(custypeMBL)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false))"
					+ " OR (contactype2 = 'C' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isconsignee = true AND x.id = customerid AND x.isdelete = false)))"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=N;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))";
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
		String wheresql = "WHERE customerid IN(SELECT customerid FROM bus_shipping WHERE id ="
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
		defineIframe.load("./busshippingdefine.xhtml?id=" + this.pkVal);
		showDefineWindow.show();
	}

	/**
	 * 显示邮件页面
	 */
	@Action
	public void showEmail() {
		/*emailIframe.load("/scp/pages/sysmgr/mail/emailedit.aspx?type=D&id="+ "-1" + "&src=jobship&jobid="+ this.selectedRowData.getJobid());
		showEmailWindow.show();*/

		String url = AppUtils.getContextPath();
		String openurl = url + "/pages/sysmgr/mail/emailedit.aspx";
		AppUtils.openWindow("_showEmail", openurl + "?type=D&id="+ "-1" + "&src=jobship&jobid="+ this.selectedRowData.getJobid());

	}

	/**
	 * 显示订舱页面
	 * */
	@Action
	public void showbookingspace() {
		bookingIframe.load("../ship/bookingSpace.xhtml?id="
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
		}else if ("28".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("detailsWindow.setTitle('SO');");
			Browser.execClientScript("backJs.hide();");
			Browser.execClientScript("saveDetailsJs.hide();");
			Browser.execClientScript("toUpJs.hide();");
		} else if ("29".equals(type)) {
			this.detailsContent = content;
			Browser.execClientScript("type29()");
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
		}else if ("28".equals(type)) {
			this.selectedRowData.setSono(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "sono");
		}else if ("29".equals(type)) {
			this.selectedRowData.setHscode(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "hscode");
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
//			wfImporPackingList(this.selectedRowData.getJobid(),
//					WorkFlowEnumerateShip.PACKING_BILL);
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"
					+ packagelistbillFile;
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
		}
	}

//	/**
//	 * 确认导出装箱单 或者发票 委托邮件 业务报告
//	 */
//	public void wfImporPackingList(Long id, String Actitivy) {
//
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(id, Actitivy,
//				"id");
//		try {
//			WorkFlowUtil.passProcessBatch(workids);
//			MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//		} catch (CommonRuntimeException e) {
//			// MsgUtil.alert(e.getLocalizedMessage());
//		} catch (EngineException e) {
//			// MsgUtil.alert(e.getErrorMsg());
//			// e.printStackTrace();
//		} catch (KernelException e) {
//			// MsgUtil.alert(e.getErrorMsg());
//			// e.printStackTrace();
//		}
//	}

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

	@Bind
	public UIWindow showSFTWindow;

	@Action
	public void showRemarks() {
		showRemarksWindow.show();
	}

	@Action
	public void showSFT() {
		showSFTWindow.show();
	}

	@Bind
	public Boolean isput = false;

	@Bind
	public Boolean isunder = false;

	@Bind
	public Boolean ishold = false;

	@Bind
	public Boolean istelrel = false;

	@Bind
	public Boolean istelrelback = false;

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
			serviceContext.busShippingMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isput = isput ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "isput");
		}
	}

	@Action
	public void isunder_oncheck() {
		boolean under = false;
		boolean cancelunder = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
			if (s.endsWith("_isunder")) {
				under = true;
			}
			if (s.endsWith("_cancelunder")) {
				cancelunder = true;
			}
		}
		isunder = selectedRowData.isIsunder();
		if(isunder){
			if (cancelunder) {
				isunder = false;
				selectedRowData.setIsunder(isunder);
				selectedRowData.setUnder(isunder ? AppUtils.getUserSession().getUsercode() : null);
				selectedRowData.setUndertime(isunder ? Calendar.getInstance().getTime(): null);
				update.markUpdate(true, UpdateLevel.Data, "isunder");
				update.markUpdate(true, UpdateLevel.Data, "under");
			} else {
				MessageUtils.alert("无取消压单权限!");
				isunder = true;
				//isunder = !isunder;
				selectedRowData.setIsunder(isunder);
				update.markUpdate(true, UpdateLevel.Data, "isunder");
				return;
			}
		}else{
			if (under) {
				isunder = true;
				selectedRowData.setIsunder(isunder);
				selectedRowData.setUnder(isunder ? AppUtils.getUserSession().getUsercode() : null);
				selectedRowData.setUndertime(isunder ? Calendar.getInstance().getTime(): null);
				update.markUpdate(true, UpdateLevel.Data, "isunder");
				update.markUpdate(true, UpdateLevel.Data, "under");
			} else {
				MessageUtils.alert("无压单权限!");
				isunder = false;
				selectedRowData.setIsunder(isunder);
				update.markUpdate(true, UpdateLevel.Data, "isunder");
				return;
			}
		}

		/*if (under) {
			selectedRowData.setIsunder(isunder);
			selectedRowData.setUnder(isunder ? AppUtils.getUserSession().getUsercode() : null);
			selectedRowData.setUndertime(isunder ? Calendar.getInstance().getTime(): null);
			update.markUpdate(true, UpdateLevel.Data, "isunder");
			update.markUpdate(true, UpdateLevel.Data, "under");
		} else {
			MessageUtils.alert("无压单权限!");
			isunder = !isunder;
			selectedRowData.setIsunder(isunder);
			update.markUpdate(true, UpdateLevel.Data, "isunder");
			return;
		}

		if (cancelunder) {
			selectedRowData.setIsunder(isunder);
			selectedRowData.setUnder(isunder ? AppUtils.getUserSession().getUsercode() : null);
			selectedRowData.setUndertime(isunder ? Calendar.getInstance().getTime(): null);
			update.markUpdate(true, UpdateLevel.Data, "isunder");
			update.markUpdate(true, UpdateLevel.Data, "under");
		} else {
			MessageUtils.alert("无压单权限!");
			isunder = !isunder;
			selectedRowData.setIsunder(isunder);
			update.markUpdate(true, UpdateLevel.Data, "isunder");
			return;
		}*/

		try {
			serviceContext.busShippingMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			isunder = isunder ? false : true;
			update.markUpdate(true, UpdateLevel.Data, "isunder");
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
			serviceContext.busShippingMgrService.saveData(selectedRowData);
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
			serviceContext.busShippingMgrService.saveData(selectedRowData);
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
			serviceContext.busShippingMgrService.saveData(selectedRowData);
			if(istelrel){
				String sql = "SELECT f_sys_mail_generate('type=istelrel_bus;id="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS info";
				Map<String,String> map =serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String txt = String.valueOf(map.get("info"));
				String[] mailInfos = txt.split("-.-.-");
				String sysEmailid = mailInfos[5];
//
				Map<String,String> attachments = new HashMap<String, String>();
				for (int i = 0; i < sysEmailid.split(",").length; i++) {
					if(null == sysEmailid.split(",")[i]){
						continue;
					}
					this.serviceContext.sysEmailService.sendEmailHtml(Long.valueOf(sysEmailid.split(",")[i]), attachments);
				}
			}
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
			if (!ishaveSono()) {
				return;
			}

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


			Browser.execClientScript("autoChooseAndSelect()");
			Browser.execClientScript("ftSubscribeJsVar.fireEvent('click');");
			getGridVesVoyData();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	public boolean ishaveSono() {
		//请在每个有箱号的箱子行，填写对应的订舱号/SO号，再保存。
		boolean ishaveSono = true;
		net.sf.json.JSONArray json0 = net.sf.json.JSONArray.fromObject(modifiedData);
		for (int i = 0; i < json0.size(); i++) {
			String cntno = json0.getJSONObject(i).getString("cntno");
			String sono = json0.getJSONObject(i).getString("sono");
			if (!StrUtils.isNull(cntno) && !"null".equals(cntno)) {
				if ("null".equals(sono) || StrUtils.isNull(sono)) {
					ishaveSono = false;
				}
			}
		}
		net.sf.json.JSONArray json1 = net.sf.json.JSONArray.fromObject(addedData);
		for (int i = 0; i < json1.size(); i++) {
			String cntno = json1.getJSONObject(i).getString("cntno");
			String sono = json1.getJSONObject(i).getString("sono");
			if (!StrUtils.isNull(cntno) && !"null".equals(cntno)) {
				if ("null".equals(sono) || StrUtils.isNull(sono)) {
					ishaveSono = false;
				}
			}
		}
		if (!ishaveSono) {
			MessageUtils.alert("请在每个有箱号箱子行的[订舱号/SO]栏位，填写对应的订舱号/SO号，再保存。");
		}
		return ishaveSono;
	}


	public boolean ishaveSono0() {
		//请在每个有箱号的箱子行，填写对应的订舱号/SO号，再保存。
		boolean ishaveSono = true;
		if (!StrUtils.isNull(dtlData.getCntno()) && StrUtils.isNull(dtlData.getSono()) ) {
			ishaveSono = false;
		}
		if (!ishaveSono) {
			MessageUtils.alert("请在每个有箱号箱子行的[订舱号/SO]栏位，填写对应的订舱号/SO号，再保存。");
		}
		return ishaveSono;
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
			//this.serviceContext.busShippingMgrService.saveData(this.selectedRowData);
			this.serviceContext.busShipContainerMgrService.updateContainerSelects(this.editGrid.getSelectedIds(), id);
			this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_bus_shipping_container_createbillinfo('jobid=" + this.selectedRowData.getJobid() + ";');");
			this.serviceContext.userMgrService.sysCorporationDao.executeSQL("UPDATE bus_shipping set marksno = f_bus_shipping_cntinfo('billid="
							+ this.selectedRowData.getJobid()
							+ ";type="
							+ (isOneLine ? "1" : "2")
							+ "') WHERE id = "
							+ id
							+ ";");
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere(sql);
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			isunder = selectedRowData.isIsunder();

			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.editGrid.reload();
			this.qryRefresh();
			Browser.execClientScript("layer.msg('集装箱OK',{offset:['60%','40%'],time:1000});");
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
				.executeSQL("UPDATE bus_shipping set marksno = f_bus_shipping_cntinfo('billid="
						+ id
						+ ";type="
						+ (isOneLine ? "1" : "2")
						+ "') WHERE id = " + id + ";");
		String sql = " isdelete = false AND jobid =" + this.jobid;
		this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao
				.findOneRowByClauseWhere(sql);
		ishold = selectedRowData.isIshold();
		isput = selectedRowData.isIsput();
		isunder = selectedRowData.isIsunder();

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
				.executeSQL("UPDATE bus_shipping set goodsdesc = f_bus_shipping_goodsinfo('billid="
						+ id + "') WHERE id = " + id + ";");
		String sql = " isdelete = false AND jobid =" + this.jobid;
		this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao
				.findOneRowByClauseWhere(sql);
		ishold = selectedRowData.isIshold();
		isput = selectedRowData.isIsput();
		isunder = selectedRowData.isIsunder();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		// this.qryRefresh();
		// alert("OK");
	}

	@Action
	public void showHblSplit() {
		String url = "../ship/busshipbill.xhtml?id=" + this.jobid;
		//AppUtils.openWindow("____", url);
		AppUtils.openNewPage(url);
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

	// @Override
	// public void processUpload(FileUploadItem fileUploadItem)
	// throws IOException {
	// if (fileUploadItem.getFieldName().equals("fileUpload1"))
	// deleteOldFiles();
	// InputStream input = null;
	// FileOutputStream output = null;
	// try{
	//
	// input = fileUploadItem.openStream();
	// File file = new File(getSaveToPath(fileUploadItem));
	// output = new FileOutputStream(file);
	// byte[] buf = new byte[4096];
	// // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
	// int length = UIFileUpload.END_UPLOADING;
	// while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
	// output.write(buf, 0, length);
	// }
	// try{
	// Map<String, String> map = ReadExcel.importJobsForExcel(file);
	// if(map != null && map.size() > 0){
	// StringBuffer sbsql = new StringBuffer();
	// sbsql.append("SELECT f_rpt_import_shipping('cnortitle="+map.get("cnortitle")+";");
	// sbsql.append("cneetitle="+map.get("cneetitle")+";");
	// sbsql.append("notifytitle="+map.get("notifytitle")+";");
	// sbsql.append("pretrans="+map.get("pretrans")+";");
	// sbsql.append("poa="+map.get("poa")+";");
	// sbsql.append("vessel="+map.get("vessel")+";");
	// sbsql.append("voyage="+map.get("voyage")+";");
	// sbsql.append("pol="+map.get("pol")+";");
	// sbsql.append("pdd="+map.get("pdd")+";");
	// sbsql.append("pod="+map.get("pod")+";");
	// sbsql.append("destination="+map.get("destination")+";");
	// sbsql.append("cntinfos="+map.get("cntinfos").replaceAll("'", "''")+";");
	// sbsql.append("goodsinfo="+map.get("goodsinfo").replaceAll("'",
	// "''")+";");
	// sbsql.append("grswgt="+map.get("grswgt")+";");
	// sbsql.append("cbm="+map.get("cbm")+";");
	// sbsql.append("agentitle="+map.get("agentitle")+";");
	// sbsql.append("jobid="+this.jobid+";corpid="+AppUtils.getUserSession().getCorpidCurrent()+"') AS jobid");
	// Map result =
	// this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
	// Long jobid = Long.parseLong(result.get("jobid").toString());
	// //System.out.println(jobid);
	// MessageUtils.alert("OK!");
	// this.importFileWindow.close();
	// this.mPkVal = jobid;
	// this.refresh();
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }catch(IOException e){
	// e.printStackTrace();
	// }finally{
	// if (output != null)
	// try {
	// output.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	public String getSavePath() {
		return System.getProperty("java.io.tmpdir");
	}

	// @SwfUploadListener
	// public void upload1(HttpServletRequest request, HttpServletResponse
	// response) {
	// String savePath = System.getProperty("java.io.tmpdir") + "/";
	// ServletFileUpload upload = new ServletFileUpload();
	// InputStream stream = null;
	// BufferedInputStream bis = null;
	// BufferedOutputStream bos = null;
	// try {
	// if (ServletFileUpload.isMultipartContent(request)) {
	// FileItemIterator iter = upload.getItemIterator(request);
	// while (iter.hasNext()) {
	// FileItemStream item = iter.next();
	// stream = item.openStream();
	// if (!item.isFormField()) {
	// String filename = new File(item.getName()).getName();
	// bis = new BufferedInputStream(stream);
	// bos = new BufferedOutputStream(new FileOutputStream(new File(savePath +
	// filename)));
	// Streams.copy(bis, bos, true);
	//
	// Map<String, String> map = ReadExcel.importJobsForExcel(new File(savePath
	// + filename));
	// if(map != null && map.size() > 0){
	// StringBuffer sbsql = new StringBuffer();
	// sbsql.append("SELECT f_rpt_import_shipping('cnortitle="+map.get("cnortitle").replaceAll("'",
	// "''")+";");
	// sbsql.append("cneetitle="+map.get("cneetitle").replaceAll("'",
	// "''")+";");
	// sbsql.append("notifytitle="+map.get("notifytitle").replaceAll("'",
	// "''")+";");
	// sbsql.append("pretrans="+map.get("pretrans").replaceAll("'", "''")+";");
	// sbsql.append("poa="+map.get("poa").replaceAll("'", "''")+";");
	// sbsql.append("vessel="+map.get("vessel").replaceAll("'", "''")+";");
	// sbsql.append("voyage="+map.get("voyage").replaceAll("'", "''")+";");
	// sbsql.append("pol="+map.get("pol").replaceAll("'", "''")+";");
	// sbsql.append("pdd="+map.get("pdd").replaceAll("'", "''")+";");
	// sbsql.append("pod="+map.get("pod").replaceAll("'", "''")+";");
	// sbsql.append("destination="+map.get("destination").replaceAll("'",
	// "''")+";");
	// sbsql.append("cntinfos="+map.get("cntinfos").replaceAll("'", "''")+";");
	// sbsql.append("goodsinfo="+map.get("goodsinfo").replaceAll("'",
	// "''")+";");
	// sbsql.append("grswgt="+map.get("grswgt").replaceAll("'", "''")+";");
	// sbsql.append("cbm="+map.get("cbm").replaceAll("'", "''")+";");
	// sbsql.append("agentitle="+map.get("agentitle").replaceAll("'",
	// "''")+";");
	// sbsql.append("jobid="+this.jobid+";corpid="+AppUtils.getUserSession().getCorpidCurrent()+"') AS jobid");
	// //Map result =
	// this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
	// String sq = sbsql.toString();
	// sq = sq.replaceAll("/20''RF//", "20RF");
	// sq = sq.replaceAll("/40''FR//", "40FR");
	// sq = sq.replaceAll("/20''HD//", "20HD");
	// sq = sq.replaceAll("/20''GP//", "20GP");
	// sq = sq.replaceAll("/45''HQ//", "45HQ");
	// sq = sq.replaceAll("/40''GP//", "40GP");
	// sq = sq.replaceAll("/40''HQ//", "40HQ");
	// sq = sq.replaceAll("/", "|");
	// //System.out.println(sq);
	// Vector<String> sqlBatch = new Vector<String>();
	//
	// sqlBatch.add(sq);
	// try {
	// this.serviceContext.daoIbatisTemplate.executeQueryBatchByJdbc(sqlBatch);
	// MessageUtils.alert("OK!");
	// this.refresh();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// break;
	//
	//
	// }
	// }
	// }
	// }
	// } catch (FileUploadException e) {
	// } catch (IOException e1) {
	// } finally{
	// if(stream != null){
	// try{
	// stream.close();
	// }catch(Exception e){}
	// }
	// if(bis != null){
	// try{
	// bis.close();
	// }catch(Exception e){}
	// }
	// if(bos != null){
	// try{
	// bos.close();
	// }catch(Exception e){}
	// }
	// }
	// }

	// 临时保存系统生成可供下载文件路径
	@SaveState
	public String temFileUrl;

	@SaveState
	public String temFileUrl2;

	@SaveState
	public String temFileUrl3;

	@SaveState
	public String temFileUrl4;

	@SaveState
	public String temFileUrl5;

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
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 30 and d.code = carryitem) AS carryitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = freightitem) AS freightitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = paymentitem) AS paymentitem");

				sbsql.append("\n 	,(SELECT COALESCE(a.namee,'')||f_newline()||COALESCE(a.namec) FROM sys_corporation a WHERE isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE  id ="
								+ AppUtils.getUserSession().getUserid()
								+ " AND isdelete = FALSE  AND x.corpid = a.id)) AS corporation");
				sbsql.append("\n FROM bus_shipping");
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
				sbsql.append("\n FROM bus_shipping b");
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

	// 导出泽世托书
	@Action
	public void exportshipping2() {
		if (this.selectedRowData != null && this.selectedRowData.getId() > 0) {
			// 生成下载临时文件路径
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));

			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("\n SELECT cnortitle");
				sbsql.append("\n 	,(SELECT nos FROM fina_jobs WHERE id = bus_shipping.jobid AND isdelete = FALSE limit 1) AS nos");
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
				sbsql.append("\n 	,etd");
				sbsql.append("\n 	,agentitle");
				sbsql.append("\n 	,remark_booking");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 30 and d.code = carryitem) AS carryitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = freightitem) AS freightitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = paymentitem) AS paymentitem");

				sbsql.append("\n 	,(SELECT COALESCE(a.namee,'')||f_newline()||COALESCE(a.namec) FROM sys_corporation a WHERE isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE  id ="
								+ AppUtils.getUserSession().getUserid()
								+ " AND isdelete = FALSE  AND x.corpid = a.id)) AS corporation");
				sbsql.append("\n FROM bus_shipping");
				sbsql.append("\n WHERE");
				sbsql.append("\n isdelete = FALSE");
				sbsql.append("\n AND id = " + this.selectedRowData.getId());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sbsql.toString());

				if(map==null || map.size() == 0){
					return;
				}
				if(map.containsKey("nos") && map.get("nos") != null){
					fileUrl.append(map.get("nos"));
				}else{
					fileUrl.append("1");
				}
				fileUrl.append(".xlsx");
				this.temFileUrl3 = fileUrl.toString();
				File file = new File(fileUrl.toString());
				// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
				String exportFileName = "shippinglist.xlsx";
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
				if (!ReadExcel.exportJobsForExcel2(new File(fromFileUrl), file,
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

	// 导出泽世托书
	@Action
	public void exportshipping4() {
		if (this.selectedRowData != null && this.selectedRowData.getId() > 0) {
			// 生成下载临时文件路径
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));

			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("\n SELECT cnortitlembl as cnortitle");
				sbsql.append("\n 	,(SELECT nos FROM fina_jobs WHERE id = bus_shipping.jobid AND isdelete = FALSE limit 1) AS nos");
				sbsql.append("\n 	,cneetitlembl as cneetitle");
				sbsql.append("\n 	,notifytitlembl as notifytitle");
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
				sbsql.append("\n 	,etd");
				sbsql.append("\n 	,agentitle");
				sbsql.append("\n 	,remark_booking");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 30 and d.code = carryitem) AS carryitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = freightitem) AS freightitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = paymentitem) AS paymentitem");

				sbsql.append("\n 	,(SELECT COALESCE(a.namee,'')||f_newline()||COALESCE(a.namec) FROM sys_corporation a WHERE isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE  id ="
								+ AppUtils.getUserSession().getUserid()
								+ " AND isdelete = FALSE  AND x.corpid = a.id)) AS corporation");
				sbsql.append("\n FROM bus_shipping");
				sbsql.append("\n WHERE");
				sbsql.append("\n isdelete = FALSE");
				sbsql.append("\n AND id = " + this.selectedRowData.getId());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sbsql.toString());

				if(map==null || map.size() == 0){
					return;
				}
				if(map.containsKey("nos") && map.get("nos") != null){
					fileUrl.append(map.get("nos"));
				}else{
					fileUrl.append("1");
				}
				fileUrl.append(".xlsx");
				this.temFileUrl4 = fileUrl.toString();
				File file = new File(fileUrl.toString());
				// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
				String exportFileName = "shippinglist_mbl.xlsx";
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
				if (!ReadExcel.exportJobsForExcel3(new File(fromFileUrl), file,
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

	// 导出泽世托书
	@Action
	public void exportshipping5() {
		if (this.selectedRowData != null && this.selectedRowData.getId() > 0) {
			// 生成下载临时文件路径
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));

			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("\n SELECT cnortitlembl as cnortitle");
				sbsql.append("\n 	,(SELECT nos FROM fina_jobs WHERE id = bus_shipping.jobid AND isdelete = FALSE limit 1) AS nos");
				sbsql.append("\n 	,cneetitlembl as cneetitle");
				sbsql.append("\n 	,notifytitlembl as notifytitle");
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
				sbsql.append("\n 	,etd");
				sbsql.append("\n 	,agentitle");
				sbsql.append("\n 	,remark_booking");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 30 and d.code = carryitem) AS carryitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = freightitem) AS freightitem");
				sbsql.append("\n 	,(SELECT d.namee from dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20 and d.code = paymentitem) AS paymentitem");

				sbsql.append("\n 	,(SELECT COALESCE(a.namee,'')||f_newline()||COALESCE(a.namec) FROM sys_corporation a WHERE isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE  id ="
								+ AppUtils.getUserSession().getUserid()
								+ " AND isdelete = FALSE  AND x.corpid = a.id)) AS corporation");
				sbsql.append("\n FROM bus_shipping");
				sbsql.append("\n WHERE");
				sbsql.append("\n isdelete = FALSE");
				sbsql.append("\n AND id = " + this.selectedRowData.getId());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sbsql.toString());

				if(map==null || map.size() == 0){
					return;
				}
				if(map.containsKey("nos") && map.get("nos") != null){
					fileUrl.append(map.get("nos"));
				}else{
					fileUrl.append("1");
				}
				fileUrl.append(".xlsx");
				this.temFileUrl5 = fileUrl.toString();
				File file = new File(fileUrl.toString());
				// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
				String exportFileName = "shippinglist_mbl2.xlsx";
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
				if (!ReadExcel.exportJobsForExcel3(new File(fromFileUrl), file,
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
		String exportFileName = "importTempleteForNingbo.xls";
		this.temFileUrl2 = AppUtils.getHttpServletRequest().getSession()
				.getServletContext().getRealPath("")
				+ File.separator
				+ "upload"
				+ File.separator
				+ "ship"
				+ File.separator + exportFileName;
	}

	@Action
	public void exportshippingNingbo() {
		// 导出泽世托书模版
		String exportFileName = "shippinglist.xlsx";
		this.temFileUrl3 = AppUtils.getHttpServletRequest().getSession()
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

	@Bind(id = "downloadexportNingbo", attribute = "src")
	private File getDownload11() {
		return new File(temFileUrl3);
	}

	@Bind(id = "downloadexportNingbo4", attribute = "src")
	private File getDownload14() {
		return new File(temFileUrl4);
	}


	@Bind(id = "downloadexportNingbo5", attribute = "src")
	private File getDownload15() {
		return new File(temFileUrl5);
	}

	@Bind(id = "downloadexportshippinglist", attribute = "src")
	private File getDownload8() {
		return new File(temFileUrl2);
	}

	@Bind(id = "downloadShipTempletelist", attribute = "src")
	private File getDownload9() {
		return new File(temFileUrl2);
	}

	@Bind(id = "downloadshippingNingbo", attribute = "src")
	private File getDownload10() {
		return new File(temFileUrl3);
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
			serviceContext.busShippingMgrService.saveData(selectedRowData);
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
			serviceContext.busShippingMgrService.saveData(selectedRowData);
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
			Browser.execClientScript("parent.refreshAjaxPanel.submit();");
			refresh();
			update.markUpdate(true, UpdateLevel.Data, "pkVal");

			//2632 查日志表记录这个提示信息(日志录入时间，间隔为10秒之内的一条，避免有多条的情况显示到历史的记录)，用alert方式显示出来。
			String sql = "SELECT remarks FROM bus_optrack WHERE jobid = " + jobid + " AND opusr = '" + AppUtils.getUserSession().getUsercode() + "' " +
					"AND optime > now() - interval '10 second' AND opdesc LIKE '%栏目值[修改委托资料自动同步运价异常]%' LIMIT 1;";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if (m != null && m.size() > 0) {
					MessageUtils.alert(m.get("remarks").toString());
				}
			} catch (NoRowException e) {
			}

			Browser.execClientScript("chooseContainerJsVar.fireEvent('click');");
			getGridVesVoyData();
			Browser.execClientScript("layer.msg('委托保存成功',{offset:['20%','40%'],time:1000,type: 1});");
		} catch (Exception e) {
			String exceptionmessage = e.getMessage();
			LogBean.insertLog(new StringBuffer().append("海运委托保存失败,saveMaster失败原因为").append(e.getMessage()));
			if (exceptionmessage.contains("java.lang.RuntimeException")) {
				exceptionmessage = exceptionmessage.replaceAll("java.lang.RuntimeException:", "");
				Browser.execClientScript("layer.msg('" + exceptionmessage + "',{offset:['20%','40%'],time:5000,type: 1});");
			} else {
				MessageUtils.showException(e);
			}
		}
	}

	public void doServiceSaveMaster() {
		try {
			if (selectedRowData.getEtd() != null
					&& selectedRowData.getEta() != null
					&& selectedRowData.getEta()
					.before(selectedRowData.getEtd())) {
				throw new RuntimeException("ETA不能小于ETD");
			}
			if (selectedRowData.getAta() != null
					&& selectedRowData.getAtd() != null
					&& selectedRowData.getAta()
					.before(selectedRowData.getAtd())) {
				throw new RuntimeException("ATA不能小于ATD");
			}

//			String sql222 = " isdelete = false AND jobid =" + this.jobid;
//			BusShipping oldBusShipping = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere(sql222);
//			if (oldBusShipping.getIsconfirm() || oldBusShipping.getIsprint() || oldBusShipping.getIssign() || oldBusShipping.getIscomplete()) {
//				if (oldBusShipping.getAtd() != null && oldBusShipping.getAtd().getTime()!= selectedRowData.getAtd().getTime()) {
//					throw new RuntimeException("hbl提单已确认，无法修改ATD。");
//				}
//			}

			//			IF new.isconfirm OR new.isprint OR new.issign OR new.iscomplete THEN  //HBL确认判断
			//			IF new.isconfirmmbl OR new.isprintmbl OR new.isgetmbl OR new.isreleasembl THEN //MBL确认判断
			String sql = "SELECT (CASE WHEN isconfirm OR isprint OR issign OR iscomplete THEN 1 " +
					"WHEN isconfirmmbl OR isprintmbl OR isgetmbl OR isreleasembl THEN 2 ELSE 0 END) AS flag" +
					" FROM bus_shipping WHERE isdelete = FALSE AND id =" + selectedRowData.getId();
			Map m = new HashMap();
			try {
				m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(null != m && m.containsKey("flag") && "1".equals(String.valueOf(m.get("flag")))){//HBL确认
				selectedRowData.setCnortitlemblname(AppUtils
						.replaceStringByRegEx(selectedRowData.getCnortitlemblname()));
				selectedRowData.setCnortitlembl(AppUtils
						.replaceStringByRegEx(selectedRowData.getCnortitlembl()));
				selectedRowData.setCneetitlemblname(AppUtils
						.replaceStringByRegEx(selectedRowData.getCneetitlemblname()));
				selectedRowData.setCneetitlembl(AppUtils
						.replaceStringByRegEx(selectedRowData.getCneetitlembl()));
				selectedRowData.setNotifytitlemblname(AppUtils
						.replaceStringByRegEx(selectedRowData.getNotifytitlemblname()));
				selectedRowData.setNotifytitlembl(AppUtils
						.replaceStringByRegEx(selectedRowData.getNotifytitlembl()));
			}else if("2".equals(String.valueOf(m.get("flag")))){								//MBL确认
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
			}else{
				selectedRowData.setCnortitlemblname(AppUtils
						.replaceStringByRegEx(selectedRowData.getCnortitlemblname()));
				selectedRowData.setCnortitlembl(AppUtils
						.replaceStringByRegEx(selectedRowData.getCnortitlembl()));
				selectedRowData.setCneetitlemblname(AppUtils
						.replaceStringByRegEx(selectedRowData.getCneetitlemblname()));
				selectedRowData.setCneetitlembl(AppUtils
						.replaceStringByRegEx(selectedRowData.getCneetitlembl()));
				selectedRowData.setNotifytitlemblname(AppUtils
						.replaceStringByRegEx(selectedRowData.getNotifytitlemblname()));
				selectedRowData.setNotifytitlembl(AppUtils
						.replaceStringByRegEx(selectedRowData.getNotifytitlembl()));

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
			}
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
			selectedRowData.setMarksno(AppUtils
					.replaceStringByRegEx(selectedRowData.getMarksno()));
			selectedRowData.setGoodsdesc(AppUtils
					.replaceStringByRegEx(selectedRowData.getGoodsdesc()));
			selectedRowData.setTotledesc(AppUtils
					.replaceStringByRegEx(selectedRowData.getTotledesc()));
			serviceContext.busShippingMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			Browser.execClientScript("showmsg()");

			if (ishaveSono()) {
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
			}


			//记录印刷编号
			if (!StrUtils.isNull(printingcode)) {
				String printcodesql = "select * from bus_ship_bill_reg where iscancel=false and printingcode ='" + printingcode + "' order by id desc limit 1 ";
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

			refresh();
		} catch (Exception e) {
			throw new RuntimeException(e);
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

	// @Action(id="editGrid",event="ondblclick")
	// public void grid_ondblclick(){
	// this.pkVal = getGridSelectId();
	// doServiceFindData();
	// this.refreshForm();
	// if(editWindow != null)editWindow.show();
	// update.markUpdate(true, UpdateLevel.Data, "editPanel");
	// }

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

	@Bind
	@SaveState
	public String cntypeBatch;

	@Action
	public void addContainer() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setShipid(this.selectedRowData.getId());

		//System.out.println("cntypeBatch:"+cntypeBatch);

		try {
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

					if(!StrUtils.isNull(cntypeBatch) && StrUtils.isNumber(cntypeBatch)){
						dtlData.setCntypeid(Long.valueOf(cntypeBatch));
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
//					if (temps == null || temps.size() <= 0) {
//						dtlData.setOrderno(jsonArray.size() + 1);
//					} else {
//						dtlData.setOrderno(temps.size() + jsonArray.size() + 1);
//					}
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
							List<BusShipContainer> temps = serviceContext.busShipContainerMgrService.busShipContainerDao.findAllByClauseWhere("jobid = " + this.jobid + " AND isdelete = false AND parentid IS NULL ORDER BY orderno");
							if (temps != null && temps.size() > 0) {
								if(!StrUtils.isNull(cntypeBatch) && StrUtils.isNumber(cntypeBatch)){
									dtlData.setCntypeid(Long.valueOf(cntypeBatch));
								}else{
									dtlData.setCntypeid(temps.get(temps.size() - 1).getCntypeid());
								}

								dtlData.setLdtype(temps.get(temps.size() - 1).getLdtype());
								dtlData.setPackagee(temps.get(temps.size() - 1).getPackagee());
								dtlData.setGrswgt(temps.get(temps.size() - 1).getGrswgt());
								dtlData.setPiece(temps.get(temps.size() - 1).getPiece());
								dtlData.setCbm(temps.get(temps.size() - 1).getCbm());
								dtlData.setVgm(temps.get(temps.size() - 1).getVgm());
							} else {
								if(!StrUtils.isNull(cntypeBatch) && StrUtils.isNumber(cntypeBatch)){
									dtlData.setCntypeid(Long.valueOf(cntypeBatch));
								}
								dtlData.setLdtype(selectedRowData.getLdtype());
								dtlData.setPackagee(selectedRowData.getPacker());
								dtlData.setGrswgt(new BigDecimal(0));
								dtlData.setPiece(0);
								dtlData.setCbm(new BigDecimal(0));
								dtlData.setVgm(new BigDecimal(0));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//dtlData.setOrderno(temps.size() + 1);
					dtlData.setSono(sonopiliang);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		editGrid.appendRow(dtlData);
		Browser.execClientScript("setTimeout(fixContainOrderNo(),2000);");
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
			String sql1 = "UPDATE bus_shipping set marksno = f_bus_shipping_cntinfo('billid="
					+ this.selectedRowData.getJobid()
					+ ";type="
					+ (isOneLine ? "1" : "2") + "') WHERE id = " + id + ";";
			this.serviceContext.userMgrService.sysCorporationDao
					.executeSQL(sql1);
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao
					.findOneRowByClauseWhere(sql);
			ishold = selectedRowData.isIshold();
			isput = selectedRowData.isIsput();
			isunder = selectedRowData.isIsunder();
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
		containerIframe.load("../ship/containerdetail.xhtml?cid=" + cid);
		showContainerWindow.show();
	}
	
	
	@Action
	public void autoSynBySo() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		try {
			String sql = "UPDATE bus_ship_container SET isautosynbyso = (CASE WHEN COALESCE(isautosynbyso,true) THEN FALSE ELSE TRUE END) WHERE id IN("+StrUtils.array2List(ids)+")";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			
			for (String id : ids) {
				sql = "SELECT f_bus_ship_book_cnt_sync_jobs('src=jobsfrombookcnt;cntid="+id+";userid="+AppUtils.getUserSession().getUserid()+"');";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			}
			this.editGrid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
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
			String sql = "SELECT code FROM dat_line x WHERE x.isdelete = false AND EXISTS(SELECT 1 FROM dat_port "
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

	/*@Action
	public void potajaxSubmit() {
		String potid = AppUtils.getReqParam("potid").toString();
		// 2201 提单港口包含国家 =Y时
		// ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils
				.findSysCfgVal("bill_port_connect_country");
		if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
					+ "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
					+ potid + ") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser
							.execClientScript("potJsvar.setValue(potJsvar.getValue()+','+'"
									+ m.get("namee").toString() + "')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

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
		if (!StrUtils.isNull(pddid)) {
			// 2221 目的港带航线地方修改 如果港口里面有设置二级航线，这里显示二级航线，没有二级航线则显示港口的航线
			String sql = "SELECT code FROM dat_line x WHERE EXISTS(SELECT 1 FROM dat_port "
					+ "WHERE (CASE WHEN line2<>'' AND line2 IS NOT NULL THEN line2 = x.namec ELSE line = x.namec END) AND id ="
					+ pddid + ")";
			try {
				Map m = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				if (m != null && m.get("code") != null) {
					if("Y".equals(pdd_pod)){
						this.selectedRowData.setRoutecode(m.get("code").toString());
						update.markUpdate(UpdateLevel.Data, "routecode");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 2201 提单港口包含国家 =Y时
			// ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
			String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
			if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
				String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
						+ "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
						+ pddid + ") LIMIT 1";
				try {
					Map m = serviceContext.daoIbatisTemplate
							.queryWithUserDefineSql4OnwRow(sql1);
					if (m != null && m.get("namee") != null) {
						Browser.execClientScript("pddJsvar.setValue(pddJsvar.getValue()+','+'"
										+ m.get("namee").toString() + "')");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		String sql2 = "isdelete = false and namec = '" + line + "'";
		List<DatLine> datLine = serviceContext.lineMgrService.datLineDao.findAllByClauseWhere(sql2);
		if(datLine != null && datLine.size()>0){
			this.selectedRowData.setRoutecode(datLine.get(0).getCode());
		}
		update.markUpdate(UpdateLevel.Data, "routecode");
	}


	/**
	 *一键投保将工作单信息NSERT到保险表dat_insurance
	 *
	 * @return
	 */
	@Action
	private void insurancetSubmit() {
		String sql = "SELECT f_insurance_getjobs('jobid="+this.jobid+";usercode="+AppUtils.getUserSession().getUsercode()+"')";
		try {
			this.serviceContext.busShippingMgrService.busShippingDao.executeQuery(sql);
			Browser.execClientScript("setTimeout(showInsurancet(), 1000);");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	//选船期
	@Bind
	public UIWindow shipqueryWindow;

	@Bind
	public UIIFrame shipqueryIFrame;

	@Action
	public void chooseShipByShipquery() {
		shipqueryWindow.show();

		shipqueryIFrame.load(AppUtils.getContextPath()+ "/pages/module/price/shipquery.xhtml?menuflag=xuanchuanqi&polcode=" + selectedRowData.getPolcode()+"&podcode="+ selectedRowData.getPodcode()
				+"&carrierid="+ selectedRowData.getCarrierid()+"&jobid="+ selectedRowData.getJobid());
	}


	@Action
	public void setSomethingValueSubmit() {
		try {
			String xxx = AppUtils.getReqParam("xxx").toString();
			String[] aaa = xxx.split("_");

			this.selectedRowData.setVessel(aaa[0]);
			this.selectedRowData.setVoyage(aaa[1]);
			if (!StrUtils.isNull(aaa[2])) {
				this.selectedRowData.setCarrierid(Long.valueOf(aaa[2]));
			}
			this.selectedRowData.setEtd(new SimpleDateFormat("yyyy-MM-dd").parse(aaa[3]));
			this.selectedRowData.setEta(new SimpleDateFormat("yyyy-MM-dd").parse(aaa[4]));
			this.selectedRowData.setLinecode(aaa[5]);
			update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");

			shipqueryWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@SuppressWarnings("deprecation")
	public void confirmsono(){
		try {
			String sono = selectedRowData.getSono();
			FinaJobs finaJobs = serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid));
			BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid = " + this.jobid + "");
			if (!StrUtils.isNull(sono) && "0".equals(confirmsononumber) && !sono.equals(busShipping.getSono())) {	 //so不相同才验证,提示修改.相同则继续保存
				sono = sono.split(",")[0].trim();
				List<BusShipBooking> list = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("sono = '" + sono + "' AND isdelete = false");

				if (list.isEmpty()) {    //如果SO号，并不存在于【SO管理内】，则可以只是保留录入的SO号的文本信息。

				} else if (list != null && list.size() == 1) {
					BusShipBooking bsb = list.get(0);

					//可分配状态
					Map<String, String> m0 = new HashMap<String, String>();
					m0.put("filterStatus", "1=1 and sono = '" + sono + "'" + SonoBean.getZfilter());
					List<Map> Qlist0 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.somgr.sonoBean.grid.count", m0);
					String listcount0 = Qlist0.isEmpty() ? "0" : (Qlist0.get(0).get("counts").toString());

					//无效状态
					Map<String, String> m1 = new HashMap<String, String>();
					String filter1 = "1=1 and sono = '" + sono + "'";
					filter1 += SonoBean.getSfilter();
					m1.put("filterStatus", filter1);
					List<Map> Qlist1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.somgr.sonoBean.grid.count", m1);
					String listcount1 = Qlist1.isEmpty() ? "0" : (Qlist1.get(0).get("counts").toString());

					if ((bsb.getSalesid() != null && finaJobs.getSaleid() != null && bsb.getSalesid().longValue() != finaJobs.getSaleid().longValue())
							|| (bsb.getJobid() != null && bsb.getJobid() != finaJobs.getId())) { //如果这个SO号已经分派或绑定，操作员硬录入，就需要弹窗提醒，这个SO已经绑定和分派了，不可用。
						MessageUtils.showMsg("此so号已分派其他业务员或工作单,不可使用");
						confirmsononumber = "2";
					} else if (("1".equals(listcount0))
							|| (bsb.getSalesid() != null && finaJobs.getSaleid() != null && bsb.getSalesid().longValue() == finaJobs.getSaleid().longValue())
							|| ("1".equals(listcount1) && !"1".equals(bsb.getBookstate()))) {
						//SO是可分配状态的，则需要提醒SO会带资料过来，请操作员再次认真确定，是否绑定这个SO号，可以挑选 确定 或 取消。一旦确定, SO的资料就会带过来，覆盖原来的资料。
						//被分配此业务员的so,可重新绑定
						//无效状态,非Cancelled/Declined状态,可重新绑定

						String tipstr = getCarrierdesc(bsb.getCarrierid()) + "_" + bsb.getPol() + "_" + bsb.getPod();
						Browser.execClientScript("ifimportbusbooking('" + tipstr + "');");
						confirmsononumber = "2";
					}
				} else if (list != null && list.size() > 1) {
					MessageUtils.showMsg("so数据中有多条此so号");
					confirmsononumber = "2";
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void ifimportbusbookingSubmit() {
		try {
			confirmsononumber = "1";
			saveMaster();
			confirmsononumber = "0";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	public String getCarrierdesc(Long id) {
		String sql = "SELECT c.code AS carrierdesc FROM sys_corporation c  WHERE  c.id=" + id;
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			return ((String) m.get("carrierdesc")).trim();
		} catch (NoRowException e) {
			return "";
		} catch (MoreThanOneRowException e) {
			return "";
		}
	}


	@Bind
	public UIWindow showSosWindow2;
	@Bind
	public UIIFrame soQry2Iframe;
	@Action
	public void showBookingSubmit() {
		String sono = AppUtils.getReqParam("sono");
		String bookingid = "";
		String sql = "SELECT id as bookingid FROM _bus_ship_booking_cimc T WHERE sono = '" + sono.trim() + "' order by inputtime desc";
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if (list != null && list.size() > 0) {
				bookingid = (String.valueOf(list.get(0).get("bookingid")) ).trim();
			}
		} catch (Exception e) {
		}

		if (!StrUtils.isNull(bookingid)) {
			showSosWindow2.show();
			soQry2Iframe.load(AppUtils.getContextPath() + "/pages/module/somgr/booking.xhtml?id=" + Long.parseLong(bookingid) + "&r=edit");
		} else {
			MessageUtils.showMsg("该订舱号无对应so数据");
		}
	}
	
	@Action
	public void vgmCreat() {
		try {
			String sql="select f_api_vgm('jobid="+this.jobid+";"+"userid="+AppUtils.getUserSession().getUserid()+";type=create');";
			daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			MessageUtils.alert("OK！");
		}catch (NoRowException e) {
		}catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			return;
		}
		
		String url = "/scp/pages/module/api/vgm/vgmedit.aspx?id=0&jobid="+selectedRowData.getJobid();
		showSosWindow2.show();
		soQry2Iframe.load(url);
	}
	
	@Action
	public void esiCreat() {
		try {
			String[] ids = editGrid.getSelectedIds();
			if (ids == null || ids.length < 1) {
				MessageUtils.alert("请勾选柜子!Please choose cnts");
				return;
			}
			String sql = "SELECT f_edi_esi_choose('type=JOBS2ESI;cntids="+StrUtils.array2List(ids)+";jobid="+this.jobid+";"+"userid="+AppUtils.getUserSession().getUserid()+";') AS esiid";
        	Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String url = "/scp/pages/module/edi/ediesiedit.xhtml?id="+StrUtils.getMapVal(map, "esiid");
			AppUtils.openNewPage(url);
		}catch (NoRowException e) {
		}catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			return;
		}
	}
	

	@SaveState
	@Accessible
	@Bind
	public String bookingid2 = "";

	public void getGridVesVoyData() {
		try {
			Map m = null;
			try {
				String sql = "\t\t\tSELECT \n" +
						"\t\t\t\t'SELECT f_vesvoy_busshipping_sync_jobs(''type=70;busshipbookid='||y.id||';jobid='||x.jobid||''');' AS dml\n" +
						"\t\t\t\t,y.id AS bookingid2\n" +
						"\t\t\t\t,x.jobid AS firstjobid\n" +
						"\t\t\tFROM bus_ship_book_cnt x , bus_ship_booking y WHERE x.linkid = y.id AND y.isdelete = FALSE\n" +
						"\t\t\tAND EXISTS(\n" +
						"\t\t\t\tselect 1 \n" +
						"\t\t\t\tfrom (\n" +
						"\t\t\t\t\tSELECT \n" +
						"\t\t\t\t\t\trow_number() over (partition by zz.jobid order by zz.id ) as cntindex\n" +
						"\t\t\t\t\t\t,* \n" +
						"\t\t\t\t\tFROM _bus_ship_container zz , fina_jobs yy \n" +
						"\t\t\t\t\twhere zz.parentid IS NULL \n" +
						"\t\t\t\t\t\tAND zz.isdelete = FALSE  \n" +
						"\t\t\t\t\t\tAND yy.isdelete = FALSE  \n" +
						"\t\t\t\t\t\tAND zz.jobid = yy.id\n" +
						"\t\t\t\t\t\tAND zz.cntno IS NOT NULL \n" +
						"\t\t\t\t\t\tAND zz.cntno <> '' \n" +
						"\t\t\t\t\t\tAND zz.sono IS NOT NULL \n" +
						"\t\t\t\t\t\tAND zz.sono <> '' \n" +
						"\t\t\t\t\t\tAND NOT EXISTS (SELECT 1 FROM bus_shipping WHERE jobid = yy.id AND isdelete = FALSE AND busstatus = 'R')\n" +
						"\t\t\t\t\t\tAND yy.id = " + jobid + "\n" +
						"\t\t\t\t) aa\n" +
						"\t\t\t\twhere aa.cntindex = 1 AND aa.cntno = x.cntno AND aa.sono = y.sono \n" +
						"\t\t\t)\t" +
						" order by y.inputtime desc" +
						" limit 1";
				m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			} catch (Exception e) {
			}


			if (m == null || m.isEmpty()) {
				bookingid2 = "";
			} else {
				bookingid2 = (String.valueOf(m.get("bookingid2"))).trim();
				String dml = (String.valueOf(m.get("dml"))).trim();
				if (!StrUtils.isNull(dml) && !"null".equals(dml)) {
					Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(dml);
					if (!map.isEmpty() && "船名航次不符，请根据下方船名航次修改。".equals(map.get("f_vesvoy_busshipping_sync_jobs"))) {
						Browser.execClientScript("layer.msg('船名航次不符，请根据下方船名航次修改。',{offset:['90%','40%'],time:1000,type: 1});");
					}
				}
				
				//触发修改后必须更正业务日期
				// String jobid = (String.valueOf(m.get("x.firstjobid"))).trim();
//				String qrysql = "SELECT COALESCE(f_fina_jobs_date('jobid="+jobid+"'),'') as submitime";
//				m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(qrysql);
//				String submitime  = String.valueOf(m.get("submitime"));
				String updatesql = "UPDATE fina_jobs SET submitime = COALESCE(to_date(f_fina_jobs_date('jobid='||"+jobid+"||''), 'YYYY-MM-DD') ,null) WHERE isdelete = FALSE AND id="+jobid+";";
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(updatesql);
			}
			gridVesVoy.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIDataGrid gridVesVoy;

	@Bind(id = "gridVesVoy", attribute = "dataProvider")
	protected GridDataProvider getGridVesVoyDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.somgr.bookingBean.gridVesVoy.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereGridVesVoy(qryMapShip), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				return 1000;
			}
		};
	}

    private Object getQryClauseWhereGridVesVoy(Map<String, Object> queryMap) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("qry", "\n1=1");
        map.put("start", "0");
        map.put("limit", "1000");
        String bookingid2Str = StrUtils.isNull(this.bookingid2) ? "-111" : this.bookingid2;
        map.put("filter", "\nAND linkid = " + bookingid2Str);
        return map;
    }

	@Action
	public void clearUnseeChar() {
		try {
			if(detailsContent != null){
				//				System.out.println(detailsContent);
				int lenBefore = detailsContent.length();
				//detailsContent = AppUtils.replaceStringByRegEx(detailsContent);

				StringBuffer stringBuffer = new StringBuffer();
				String[] strArrs = detailsContent.split("\n");
				int index = 1;
				for (String str : strArrs) {
					System.out.println("before:"+str.length());
					str = str.replaceAll("[^\\w\\u4E00-\\u9FA5\\s\\.,:\\-\\+\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\=\\[\\]\\{\\}\\;\\'\\\\\\?\\|\\/\\<\\>\\\"\\`\\·\\！\\￥\\（\\）\\【\\】\\《\\》]*", "");
					System.out.println("after:"+str.length());
					str = str.trim();
					stringBuffer.append(str);
					if(index<strArrs.length)stringBuffer.append("\n");
					index++;
				}
				detailsContent = stringBuffer.toString();
				System.out.println(detailsContent);

				int lenAfter = detailsContent.length();
				//				System.out.println(detailsContent);
				Browser.execClientScript("coutRowLength()");
				if(lenBefore != lenAfter){
					this.alert("成功替换隐藏字符:" + (lenBefore - lenAfter));
				}else{
					this.alert("未找到隐藏字符");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Action
	public void exporttDataView() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选中一行!");
			return;
		}

		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/analyze/containers_list.raq";

		String args = "";
		args += "&userid=" + AppUtils.getUserSession().getUserid();
		args += "&idsarraystr=" + StringUtils.join(ids, ",");

		AppUtils.openWindow("_exporttDataView", openUrl + args);
	}

	@Bind
	@SaveState
	private Boolean priceuserconfirm;

	/**
	 * 询价员确认
	 */
	@Action
	public void confirmSubmit(){
		priceuserconfirm = null == selectedRowData.getPriceuserconfirm() ? false : selectedRowData.getPriceuserconfirm();
		if(null == selectedRowData || selectedRowData.getId() < 1){
			MessageUtils.alert("请先保存");
			this.refresh();
			return;
		}
		long userid = null == selectedRowData.getPriceuserid() ? 0L : selectedRowData.getPriceuserid();
		try {
			if (userid>0L || priceuserconfirm == true){

				//系统管理员权限
				boolean isadmin = getCheckright("AdminAuth", AppUtils.getUserSession().getUserid());

				if (!AppUtils.getUserSession().getUserid().equals(selectedRowData.getPriceuserid()) && userid>0L && !isadmin) {
					MessageUtils.alert("请联系确认人解锁 或 管理员进行解锁");
					return;
				}

				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("Update bus_shipping SET priceuserconfirm = "+!priceuserconfirm+",priceuserid = null,updater = '"+AppUtils.getUserSession().getUsercode()+"' WHERE isdelete = false and id = "+selectedRowData.getId());
			}else{
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("Update bus_shipping SET priceuserconfirm = "+!priceuserconfirm+",priceuserid = "+AppUtils.getUserSession().getUserid()+",updater = '"+AppUtils.getUserSession().getUsercode()+"' WHERE isdelete = false and id = "+selectedRowData.getId());
			}
			this.refresh();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.alert("尚未有询价员确认或取消权限!");
			e.printStackTrace();
		}
	}

	@Action
	public void vesvoyquery() {
		String url = "/scp/pages/module/ship/voyage.aspx?id=0&jobid="+selectedRowData.getJobid()+"&bookingid2="+bookingid2;
		showSosWindow2.show();
		soQry2Iframe.load(url);
	}


	@Action
	public void saveShoufatong() {
		try {
			selectedRowData.setCnortitlemblname(selectedRowData.getCnorname());
			selectedRowData.setCnortitlembl(selectedRowData.getCnortitle());
			selectedRowData.setCnortitlemblid(selectedRowData.getCnorid());
			selectedRowData.setCneetitlemblname(selectedRowData.getCneename());
			selectedRowData.setCneetitlembl(selectedRowData.getCneetitle());
			selectedRowData.setCneetitlembid(selectedRowData.getCneeid());
			selectedRowData.setNotifytitlemblname(selectedRowData.getNotifyname());
			selectedRowData.setNotifytitlembl(selectedRowData.getNotifytitle());
			selectedRowData.setNotifytitlemblid(selectedRowData.getNotifyid());
			serviceContext.busShippingMgrService.saveData(selectedRowData);
			refresh();
		} catch (Exception e) {
			MessageUtils.alert(e.getMessage());
		}
	}

	@Action
	public void importzhongjiorderdata() {
		StringBuffer logsb = new StringBuffer();
		boolean issuccess = false;
		try {
			if (StrUtils.isNull(selectedRowData.getMblno())) {
				this.alert("请填写mbl提单号");
				return;
			}

			String sql = "select sc.namec,substring(scp.merchantcode,7,4) as merchantcode from sys_corporation sc left join sys_corpext scp on sc.id=scp.customerid " +
					"where sc.isdelete =false and sc.iscustomer = false and scp.merchantcode is not null and sc.namec not in ('世倡嘉美国际物流（上海）有限公司','CIMC G S LOGISTICS LLC')";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for (Map map : maps) {
				String merchantcode = String.valueOf(map.get("merchantcode"));

				String url0 = "http://120.76.119.53:9031/ufms/queryOrder?orderNo=" + selectedRowData.getMblno() + "&customerCode=" + merchantcode;
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("cache-control", "no-cache");
				headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
				headers.put("client_id", "823700-80179");
				headers.put("client_secret", "973e867994dea4c03c212f11ebe7193d5e88c2eb");
				headers.put("grant_type", "client_credentials");
				headers.put("Content-Type", "application/json;charset=utf-8");
				String ss = "{\n" +
						"\t\"orderNo\": \"" + selectedRowData.getMblno() + "\",\n" +
						"\t\"customerCode\": \"" + merchantcode + "\"\n" +
						"}";
				String result0 = AutoImportDocument.httpsRequest(headers, url0, "POST", ss);
				logsb.append("importzhongjiorderdata开始，result0为").append(result0);
				net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(result0);
				if (jsonobject.containsKey("data") && jsonobject.getJSONObject("data") != null) {
					result0 = result0.replaceAll(";", "");
					String sql1 = "SELECT f_zhongji_ufms_sync_jobs2('jobid=" + this.jobid + ";business=" + result0 + ";userid=" + AppUtils.getUserSession().getUserid() + "');";
					daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
					issuccess = true;
				}
			}

			if (issuccess) {
				this.alert("OK");
				Browser.execClientScript("parent.refreshMasterForm.fireEvent('click');");
			} else {
				this.alert("未获取到有效信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		LogBean.insertLog(logsb);
	}


	@Action
	public void ftSubscribe() {
		StringBuffer stringBuffer = new StringBuffer();
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			// MessageUtils.alert("请勾选记录！");
			return;
		}
		try {
			for (String id : ids) {
				String querySql = "select\n" +
						"(SELECT COALESCE(UPPER(x.code),'') FROM sys_corporation x WHERE id = bs.carrierid) AS carrier\n" +
						", bsc.sono\n" +
						"from bus_shipping bs\n" +
						"inner join bus_ship_container bsc on bsc.isdelete=false and bsc.parentid is null and bsc.jobid=bs.jobid\n" +
						"where bs.isdelete=false and bsc.id=" + id + " ;";
				Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				String sono = StrUtils.getMapVal(map, "sono");
				String carrierCode = StrUtils.getMapVal(map, "carrier");

				FtUtils.handleFt(stringBuffer, sono, "", carrierCode);
			}
			MessageUtils.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		LogBean.insertLastingLog2(stringBuffer, "ftSubscribe_busshippingBean");
	}



}