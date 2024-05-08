package com.scp.view.module.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.data.DatFeeitem;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.apcomparebillBean", scope = ManagedBeanScope.REQUEST)
public class ApCompareBillBean extends GridFormView {
	
	@Bind
	@SaveState
	private Long jobid;
	
	@SaveState
	@Accessible
	public FinaJobs jobs = new FinaJobs();
	
	@SaveState
	@Accessible
	public FinaArap selectedRowData = new FinaArap();

	@Bind
	public String isadditionalcost;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String jobid = AppUtils.getReqParam("id");
			usrCfg();
			if (!StrUtils.isNull(jobid)) {
				this.jobid = Long.parseLong(jobid);
				this.update.markUpdate(UpdateLevel.Data, "jobid");
				jobs = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
				update.markUpdate(true, UpdateLevel.Data, "isconfirm_bus");
			}
			showprofit();
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);

		// neo 控制费用显示
		String filterJobs = "EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"') x WHERE x.id = t.id)";
		//filterJobs += "\nAND (t.jobid = " + this.jobid + " OR EXISTS(SELECT 1 FROM fina_jobs x where x.parentid = " + this.jobid + " AND x.id = t.jobid AND x.isdelete = false))";
		// neo 优化写法 20200331
		filterJobs += "\nAND t.jobid = ANY(SELECT " + this.jobid + " UNION (SELECT x.id FROM fina_jobs x where x.parentid = " + this.jobid + " AND x.isdelete = false))";
		
		//filterJobs += "\n AND araptype = 'AP'";
		//只显示结算公司是船公司和订舱代理的
		filterJobs += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.jobid AND (t.customerid = carrierid OR t.customerid = agentid))";
		map.put("jobs", filterJobs);

		map.put("isShowJoin", "(rptype <> 'O')");
		
		return map;

	}
	
	@Bind(id="billFile")
    public List<SelectItem> getBillFile() {
    	try {
			return CommonComBoxBean.getComboxItems("d.id","COALESCE((SELECT x.name FROM sys_role x WHERE x.id = d.roleid LIMIT 1),'')||d.filename","COALESCE((SELECT x.name FROM sys_role x WHERE x.id = d.roleid LIMIT 1),'')||d.filename","sys_attachment d","WHERE d.isdelete = false AND d.linkid = " + this.jobid + " and (contenttype ILIKE '%pdf%' OR filename ILIKE '%.pdf')","ORDER BY d.id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		SysCorporation custom = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		if(custom!=null&&custom.getCode()!=null){
			Browser.execClientScript("$('#customer_input').val('"+custom.getCode()+"');");
		}
		DatFeeitem feeit = serviceContext.feeItemMgrService.datFeeitemDao.findById(selectedRowData.getFeeitemid());
		if(feeit!=null){
			Browser.execClientScript("$('#feei_input').val('"+feeit.getCode()+"/"+feeit.getName()+"');");
		}
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = this.serviceContext.arapMgrService.finaArapDao
		.findById(this.getGridSelectId());
		
	}

	@Override
	protected void doServiceSave() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		SysCorporation custmer = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		selectedRowData.setCustomercode(StrUtils.isNull(custmer.getAbbr())?custmer.getCode():custmer.getAbbr());
		//系统设置中结算方式必填
		String sql = "SELECT EXISTS(SELECT * FROM sys_config WHERE key = 'sys_settlement_method' AND val = 'Y') AS issettlement;";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		boolean issettlement = Boolean.parseBoolean(m.get("issettlement").toString());
		if(issettlement&&StrUtils.isNull(selectedRowData.getPayplace())){
			alert("结算方式必填");
		}
		this.serviceContext.arapMgrService.saveData(selectedRowData);
	}
	
	
	@SaveState
	public String currencycyadd = "CNY";
	
	@Bind
	@SaveState
	public String currency="USD";
	
	/*
	 * 查个人设置
	 * */
	public void usrCfg(){
		Map map = ConfigUtils.findUserCfgVals(new String[]{"fee_profits_cy2","fee_add_cyid"}, AppUtils.getUserSession().getUserid());
		if(map != null && map.size() > 0){
			if(map.get("fee_profits_cy2")!=null&&!map.get("fee_profits_cy2").equals("")){
				this.currency = map.get("fee_profits_cy2").toString();
			}
			if(map.get("fee_add_cyid")!=null){
				this.currencycyadd = map.get("fee_add_cyid").toString();
			}
		}
	}
	
	
	protected void initAdd() {
		selectedRowData = new FinaArap();
		this.selectedRowData.setSharetype("N");
		this.selectedRowData.setPpcc("PP");
		this.selectedRowData.setJobid(jobid);
		if(currencycyadd == null || currencycyadd.equals("")){
			this.selectedRowData.setCurrency(AppUtils.getUserSession()
					.getBaseCurrency());
		}else{
			this.selectedRowData.setCurrency(currencycyadd);
		}
		this.selectedRowData.setAraptype("AR");
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));
		this.selectedRowData.setPiece(new BigDecimal(1));
		this.selectedRowData.setSharetype("N");
		
		String payplace = ConfigUtils.findUserCfgVal("fee_add_payplace", AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(payplace)){
			this.selectedRowData.setPayplace(payplace);
		}
//		this.selectedRowData.setPayplace("CN");
		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpidCurrent() < 0 ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent());
		Browser.execClientScript("$('#feei_input').val('')");
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void delBat() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}

		try {
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			this.grid.rebind();
		}
	}
	
	/**
	 * 子单单号
	 * 
	 * @return
	 */
	@Bind(id = "jobsChildNo")
	public List<SelectItem> getJobsChildNo() {
		try {
			return CommonComBoxBean.getComboxItems("d.id",
					"COALESCE(d.nos,'')", "fina_jobs AS d", "WHERE id ="
							+ this.jobid + " OR parentid=" + this.jobid
							+ " ANd isdelete = false", "ORDER BY nos");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	/**
	 * 提单分单号码
	 * 
	 * @return
	 */
	@Bind(id = "busshipbillno")
	public List<SelectItem> getBusshipbillno() {
		try {
			List<SelectItem> lista = CommonComBoxBean.getComboxItems("DISTINCT d.mblno ", "d.mblno", "bus_ship_bill AS d", "WHERE jobid ="
							+ this.jobid + " AND isdelete = false AND COALESCE(d.mblno,'') <> ''", "");
			List<SelectItem> listb = CommonComBoxBean.getComboxItems("DISTINCT d.hblno ", "d.hblno", "bus_ship_bill AS d", "WHERE jobid ="
					+ this.jobid + " AND isdelete = false AND COALESCE(d.hblno,'') <> ''", "");
			lista.addAll(listb);
			return lista;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Action
	public void addwin() {
		add();
	}
	
	@Action
	public void copyadd() {
		this.pkVal = -100L;

		FinaArap temp = selectedRowData;

		FinaArap newObj = new FinaArap();
		newObj.setJobid(temp.getJobid());
		newObj.setAraptype(temp.getAraptype());
		newObj.setCustomerid(temp.getCustomerid());
		newObj.setCustomercode(temp.getCustomercode());
		newObj.setFkid(temp.getFkid());
		newObj.setFeeitemid(temp.getFeeitemid());
		newObj.setCurrency(temp.getCurrency());
		newObj.setPiece(temp.getPiece());
		newObj.setPpcc(temp.getPpcc());
		newObj.setUnit(temp.getUnit());
		newObj.setCorpid(temp.getCorpid());

		this.selectedRowData = newObj;
		this.update.markUpdate(true, UpdateLevel.Data, "pkId");
		// super.initAdd();
		this.selectedRowData.setAmount(new BigDecimal(0));
		this.selectedRowData.setBillid(null);
		this.selectedRowData.setBillxrate(new BigDecimal(0));
		this.selectedRowData.setBillamount(new BigDecimal(0));

		this.selectedRowData.setPrice(new BigDecimal(0));
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));

		this.update.markUpdate(UpdateLevel.Data, "amount");
		this.update.markUpdate(UpdateLevel.Data, "piece");
		this.update.markUpdate(UpdateLevel.Data, "price");
	}
	
	/*
	 * 保存新增
	 */
	@Action
	protected void saveadd() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		try {
			this.serviceContext.arapMgrService.saveData(selectedRowData);
			refresh();
			add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/*
	 * 保存并关闭
	 */
	@Action
	protected void saveclose() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		try {
			this.serviceContext.arapMgrService.saveData(selectedRowData);
			refresh();
			this.editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void del() {
		try {
			String[] ids = new String[] { String.valueOf(this.pkVal) };
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			this.add();
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIButton add;

	@Bind
	public UIButton delBat;

	@Bind
	public UIButton showDynamic;

	@Bind
	public UIButton batchedit;
	
	@Bind
	public UIButton batchmodify;

	@Bind
	public UIButton createrCurrent;

	@Bind
	public UIButton scanReport;

	@Bind
	public UIButton impTemplet;

	@Bind
	public UIButton impOtherJobs;

	@Bind
	public UIButton addwin;
	
	@Bind
	public UIButton addBatch;
	
	@Bind
	public UIButton copyadd;
	
	@Bind
	public UIButton save;
	
	@Bind
	public UIButton saveadd;
	
	@Bind
	public UIButton saveclose;
	
	@Bind
	public UIButton del;
	
	private void initCtrl() {
		add.setDisabled(true);
		delBat.setDisabled(true);
		showDynamic.setDisabled(true);
		batchedit.setDisabled(true);
		batchmodify.setDisabled(true);
		createrCurrent.setDisabled(true);
		impTemplet.setDisabled(true);
		impOtherJobs.setDisabled(true);
		addwin.setDisabled(true);
		addBatch.setDisabled(true);
		copyadd.setDisabled(true);
		save.setDisabled(true);
		saveadd.setDisabled(true);
		saveclose.setDisabled(true);
		del.setDisabled(true);

		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_arap.getValue())) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
				impTemplet.setDisabled(false);
				impOtherJobs.setDisabled(false);
				addwin.setDisabled(false);
				addBatch.setDisabled(false);
				copyadd.setDisabled(false);
				createrCurrent.setDisabled(false);
				batchedit.setDisabled(false);
				batchmodify.setDisabled(false);
				save.setDisabled(false);
				saveclose.setDisabled(false);
				saveadd.setDisabled(false);
			}else if (s.endsWith("_update")) {
				save.setDisabled(false);
				saveclose.setDisabled(false);
				batchedit.setDisabled(false);
				batchmodify.setDisabled(false);
			} else if (s.endsWith("_report")) {
				showDynamic.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delBat.setDisabled(false);
				del.setDisabled(false);
			} else if (s.equals("HideAPAmtstlShow")) {
				String isHideAPAmtstlShowJsVar = "Y";
				if(StrUtils.isNull(isHideAPAmtstlShowJsVar))isHideAPAmtstlShowJsVar="N";
				Browser.execClientScript("isHideAPAmtstlShowJsVar.setValue('"+isHideAPAmtstlShowJsVar+"')");
			}
		}
		//2151  此列只有财务和商务才可以看到这个金额，其他人是不授权就不能看到的
		Map m = null;
		String sql = "SELECT EXISTS (SELECT * FROM sys_user u WHERE u.id = "+AppUtils.getUserSession().getUserid()
				   + "\nAND EXISTS(SELECT * FROM sys_userinrole x WHERE x.isdelete = FALSE AND x.userid = u.id "
				 + "\nAND EXISTS(SELECT * FROM sys_role y WHERE y.id = x.roleid AND (y.code ILIKE 'financial%' OR y.code ILIKE 'Business')))) AS Additionalcost";
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		Boolean isAdditionalcost = m!=null&&m.get("additionalcost")!=null&&m.get("additionalcost").toString().equals("true");
		if (!getCheckright("Additionalcost",AppUtils.getUserSession().getUserid())&&!isAdditionalcost){
			isadditionalcost = "0";
		}
	}
	
	@Action
	public void isconAjax() {
		Boolean isSubmit = jobs.getIsconfirm_bus();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm_bus = TRUE ,confirm_bus_time = NOW(),confirm_bus_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm_bus = FALSE ,confirm_bus_time = NOW(),confirm_bus_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}
	
	/**
	 * 确认费用
	 */
	@Action
	public void confirmArAp() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "";
		for (String id : ids) {
			sql += "\nUPDATE fina_arap SET isconfirm = TRUE,confirmer = '"
					+ usercode
					+ "',confirmtime = NOW() " 
					+",updater='"+usercode+"',updatetime = NOW()"
					+"WHERE isdelete = FALSE AND COALESCE(isconfirm,FALSE) = FALSE AND id = "
					+ id + ";";
		}
		if (!StrUtils.isNull(sql)) {
			try {
				this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);
				Browser.execClientScript("showmsg()");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/**
	 * 取消确认
	 */
	@Action
	public void cancelArAp() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "";
		for (String id : ids) {
			sql += "\nUPDATE fina_arap SET isconfirm = FALSE,confirmer = '"
					+ usercode
					+ "',confirmtime = NOW()" 
					+ ",updater='"+usercode+"',updatetime = NOW()"
					+ " WHERE isdelete = FALSE AND id = "
					+ id + ";";
		}
		if (!StrUtils.isNull(sql)) {
			try {
				this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);
				Browser.execClientScript("showmsg()");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Action
	public void refresh() {
		super.refresh();
		Browser.execClientScript("sumamount();");
	}
	
	
	@Action
	public void iscompleteAjaxSubmit() {
		Boolean isComplete = jobs.getIscomplete();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.jobid != null) {
				sql = "UPDATE fina_jobs SET iscomplete = "+(isComplete?"TRUE":"FALSE")+",updater='"
					+ updater + "',updatetime=NOW() WHERE id ="
					+ this.jobid + ";";
				serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			}
			alert("OK");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			jobs.setIscomplete(!isComplete);
		} catch (Exception e) {
			MessageUtils.showException(e);
			jobs.setIscomplete(!isComplete);
		}
		update.markUpdate(true, UpdateLevel.Data, "iscomplete");
	}
	
	
	
	@Bind
	@SaveState
	public String profit;
	
	@Action
	public void showprofit() {
		try {
			String sql = "SELECT * FROM f_findarapinfo_profit_only('jobid="+this.jobid+";tax=Y;userid="+AppUtils.getUserSession().getUserid()+";currency=USD') AS information";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			profit = m.get("information").toString();
		} catch (Exception e) {
			profit = "";
		}
		update.markUpdate(true, UpdateLevel.Data, "profit");
	}
	
	
}
