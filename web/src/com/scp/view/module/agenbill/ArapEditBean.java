package com.scp.view.module.agenbill;

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
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UINumberField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.agenbill.arapeditBean", scope = ManagedBeanScope.REQUEST)
public class ArapEditBean extends GridFormView {

	@SaveState
	@Accessible
	public FinaArap selectedRowData = new FinaArap();

	@SaveState
	@Accessible
	public FinaJobs jobs = new FinaJobs();

	@Bind
	@SaveState
	private Long arapfkid;

	@Bind
	@SaveState
	private Long jobid;

	@Bind
	@SaveState
	private Long customerid = -100l;

	@SaveState
	@Bind
	private String iswarehouse = "N";

	@SaveState
	@Accessible
	public Long agenbillid;
	
	@Bind
	@SaveState
	public String arTip;

	@Bind
	@SaveState
	public String apTip;

	@Bind
	@SaveState
	public String profitTip;

	@Bind
	@SaveState
	public String arTipNoTax;

	@Bind
	@SaveState
	public String apTipNoTax;

	@Bind
	@SaveState
	public String profitTipNoTax;

	@Bind
	@SaveState
	public String processInstanceId;

	@Bind
	@SaveState
	public String workItemId;

	@Bind
	@SaveState
	public String exchangeTip;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("agenbillid").trim();
			String flag =AppUtils.getReqParam("isCheck").trim();
			if(!StrUtils.isNull(id)) {
				agenbillid=Long.valueOf(id);
				Boolean isCheck=Boolean.parseBoolean(flag);
				disableAllButton(isCheck);
			}
		}
	}

	@SaveState
	private boolean isShowJoin = true;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry +=  "\nAND agenbillid = " + this.agenbillid;
		m.put("qry", qry);
		return m;
	}

	public String getCustomercode() {
		String sql = "SELECT code ,abbr FROM sys_corporation  WHERE id ="
				+ customerid;
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
		return (String) list.get(0).get("code") + "/"
				+ (String) list.get(0).get("abbr");
	}

	


	
	protected void initAdd() {
		selectedRowData = new FinaArap();
		if (-100l == this.customerid) {
		} else {
			this.selectedRowData.setCustomercode(this.getCustomercode());
		}
		this.selectedRowData.setSharetype("N");
		this.selectedRowData.setFkid(arapfkid);
		this.selectedRowData.setCustomerid(customerid);
		this.selectedRowData.setPpcc("PP");
		this.selectedRowData.setJobid(jobid);
		this.selectedRowData.setCurrency(AppUtils.getUserSession()
				.getBaseCurrency());
		this.selectedRowData.setAraptype("AR");
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));
		this.selectedRowData.setPiece(new BigDecimal(1));
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Override
	public void add() {

		super.add();
		initAdd();
	}

	@Override
	public void qryAdd() {
		super.qryAdd();
		initAdd();
	}

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}

		try {
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			this.alert("OK");
			this.add();
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
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

	@Action
	public void copyaddAdd() {
		initAdd();
	}

	@Action
	public void share() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			this.alert("Please one row!");
			return;
		}
		try {
			String ret = this.serviceContext.arapMgrService.shareFee(ids,
					AppUtils.getUserSession().getUsercode());
			// this.alert("OK!\n"+ret);
			MessageUtils.showMsg("OK!\n" + ret);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Bind
	private String qryCustomerKey;

	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getAllCustomerDataProvider("");
	}

	@Action
	public void showCustomer() {
		String customercode = (String) AppUtils.getReqParam("customercode");
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			customerQry();
			Browser.execClientScript("sc.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
				this.selectedRowData.setCustomerid((Long) cs.get(0).get("id"));
				this.selectedRowData.setCustomercode(cs.get(0).get("code")
						+ "/" + cs.get(0).get("abbr"));
				this.update.markUpdate(UpdateLevel.Data, "customerid");
				this.update.markUpdate(UpdateLevel.Data, "customercode");

				showCustomerWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
				showCustomerWindow.show();
				customerQry();
				Browser.execClientScript("sc.focus");
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
		this.selectedRowData.setCustomerid((Long) m.get("id"));
		String customercode = (String) m.get("code") + "/"
				+ (String) m.get("abbr");
		this.selectedRowData.setCustomercode(customercode);
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customercode");
		showCustomerWindow.close();
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
		this.serviceContext.arapMgrService.saveData(selectedRowData);
		// initArapInfo();
	}

	/**
	 * 费用名称
	 * 
	 * @return
	 */
	@Bind(id = "feeitemtype")
	public List<SelectItem> getFeeitemtype() {
		try {
			if (null == iswarehouse) {
				iswarehouse = "N";
			}

			return CommonComBoxBean
					.getComboxItems(
							"d.id",
							"COALESCE(d.code,'')||'/'||COALESCE(d.name,'')",
							"dat_feeitem AS d",
							"WHERE ( iswarehouse = '"
									+ iswarehouse
									+ "' OR ispublic = TRUE ) AND NOT EXISTS(SELECT 1 FROM dat_feeitem_join x WHERE x.idfm = d.id AND x.jointype = 'J')",
							"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	public void ppAjaxSubmit() {
		Boolean isSubmit = jobs.getIsconfirm_pp();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm_pp = TRUE ,confirm_pp_time = NOW(),confirm_pp_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm_pp = FALSE ,confirm_pp_time = NOW(),confirm_pp_user = '"
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

	@Action
	public void ccAjaxSubmit() {
		Boolean isSubmit = jobs.getIsconfirm_cc();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm_cc = TRUE ,confirm_cc_time = NOW(),confirm_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm_cc = FALSE ,confirm_cc_time = NOW(),confirm_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			if (!StrUtils.isNull(processInstanceId)
					&& !StrUtils.isNull(workItemId)) {
//				this.affirmNoCC();
			} else {
				MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
			}
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

	@Action
	public void arAjaxSubmit() {
		Boolean isSubmit = jobs.getIscomplete_ar();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET iscomplete_ar = TRUE ,complete_ar_time = NOW(),complete_ar_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET iscomplete_ar = FALSE ,complete_ar_time = NOW(),complete_ar_user = '"
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

	@Action
	public void apAjaxSubmit() {
		Boolean isSubmit = jobs.getIscomplete_ap();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET iscomplete_ap = TRUE ,complete_ap_time = NOW(),complete_ap_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET iscomplete_ap = FALSE ,complete_ap_time = NOW(),complete_ap_user = '"
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

	@Action
	public void pp2AjaxSubmit() {
		Boolean isSubmit = jobs.getIsconfirm2_pp();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm2_pp = TRUE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm2_pp = FALSE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
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

	@Action
	public void cc2AjaxSubmit() {
//		if (!StrUtils.isNull(processInstanceId) && !StrUtils.isNull(workItemId)
//				&& this.jobs.getIsconfirm_cc() == false) {
//			MessageUtils.alert("客服CC尚未确认，不能完成此操作!");
//			return;
//		}
		Boolean isSubmit = jobs.getIsconfirm2_cc();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm2_cc = TRUE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm2_cc = FALSE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			if (!StrUtils.isNull(processInstanceId)
					&& !StrUtils.isNull(workItemId)) {
//				this.affirmNoCC2(this.jobid,WorkFlowEnumerateShip.CC_QUERY);
			} else {
				MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
			}
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

	@Bind
	@SaveState
	private String qryFeeitemKey;

	@Action
	public void feeitemQry() {
		this.customerService.qryFeeitem(qryFeeitemKey);
		this.feeitemGrid.reload();
	}

	@Bind
	public UIWindow showFeeitemWindow;

	@Bind
	public UIDataGrid feeitemGrid;

	@Bind(id = "feeitemGrid", attribute = "dataProvider")
	public GridDataProvider getFeeitemGridDataProvider() {
		return this.customerService
				.getFeeitemDataProvider("\nAND iswarehouse='" + iswarehouse
						+ "'");
	}

	@Action
	public void showFeeitem() {
		String feeitemcode = (String) AppUtils.getReqParam("feeitemcode");
		qryFeeitemKey = feeitemcode;
		int index = qryFeeitemKey.indexOf("/");
		if (index > 1)
			qryFeeitemKey = qryFeeitemKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryFeeitemKey");
			showFeeitemWindow.show();
			feeitemQry();
			Browser.execClientScript("sf.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findFeeitem(qryFeeitemKey,
					iswarehouse);
			if (cs.size() == 1) {
				Map map = cs.get(0);
				this.selectedRowData.setFeeitemid((Long) map.get("id"));
				this.update.markUpdate(UpdateLevel.Data, "feeitemid");
				showFeeitemWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryFeeitemKey");
				showFeeitemWindow.show();
				feeitemQry();
				Browser.execClientScript("sf.focus");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void feeitemGrid_ondblclick() {
		Object[] objs = feeitemGrid.getSelectedValues();
		Map map = (Map) objs[0];
		this.selectedRowData.setFeeitemid(((Long) map.get("id")));
		this.update.markUpdate(UpdateLevel.ValueOnly, "feeitemid");
		showFeeitemWindow.close();
	}

	// @Bind
	// public UIWindow showTempletWindow;
	//	
	// @Bind
	// public UIIFrame templetFrame;

	@Action
	public void impTemplet() {
		// templetFrame.load("../other/feetemplatechooser.xhtml?jobid="+this.jobid);
		// showTempletWindow.show();
		String winId = "_arapTemplet";
		String url = "../other/feetemplatechooser.xhtml?jobid=" + this.jobid;
		// int width = 980;
		// int height = 600;
		AppUtils.openWindow(winId, url);
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
	public void refresh() {
		super.refresh();
		// initArapInfo();
	}

	@Bind
	public UIWindow showbatchupdateWindow;

	@Bind
	public UIIFrame batchupdateFrame;

	@Action
	public void batchedit() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择一条记录");
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < ids.length; i++) {
				if (i == (ids.length - 1)) {
					sb.append(ids[i]);
				} else {
					sb.append(ids[i] + ",");
				}

			}
			showbatchupdateWindow.show();
			batchupdateFrame.load("./batchupdate.xhtml?iswarehouse="
					+ this.iswarehouse + "&ids=" + sb.toString());
		}
	}

	public void closeShowbatchupdateWindow() {
		showbatchupdateWindow.close();
	}

	@Action(id = "showbatchupdateWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}

	@Action
	public void hideShare() {
		this.qryMap.put("rptype", "G");
		this.grid.reload();
	}

	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "FY_JS.raq";

	@Action
	public void scanReport() {
		if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
			MessageUtils.alert("请选择收据格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		if (showwmsinfilename.equals("FY_JS_ZD.raq")) {
			String openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/static/arap/FY_JS_ZD.raq";
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs2());
		} else {
			if (showwmsinfilename.equals("FY_JS_TC.raq")) {
				String openUrl = rpturl
						+ "/reportJsp/showReport.jsp?raq=/static/arap/FY_JS_TC.raq";
				AppUtils.openWindow("_shipbillReport", openUrl + getArgs2());
			} else {
				if (showwmsinfilename.equals("FY_JS_SJ.raq")) {
					String openUrl = rpturl
							+ "/reportJsp/showReport.jsp?raq=/static/arap/FY_JS_SJ.raq";
					AppUtils.openWindow("_shipbillReport", openUrl + getArgs2());
				} else {
					String openUrl = rpturl
							+ "/reportJsp/showReport.jsp?raq=/static/arap/"
							+ showwmsinfilename;
					AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
				}
			}
		}
	}

	private String getArgs() {
		String args = "";
		args += "&id=" + this.jobid;
		return args;
	}

	private String getArgs2() {
		String args = "";
		args += "&id=" + this.jobid + "&parentid=" + this.jobid + "&jobid="
				+ this.jobid + "&jobidfm=" + this.jobid;
		return args;
	}

	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'allarap' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Bind
	@SaveState
	public UINumberField w_price;

	@Bind
	@SaveState
	public UINumberField w_pricenotax;

	@Bind
	@SaveState
	@Accessible
	public String priceFlag = "B-A";

	/**
	 * 税前单价/税后单价输入转换
	 */
	@Action
	public void switchPrice() {
		if (StrUtils.isNull(w_pricenotax.getCls())) {
			this.priceFlag = "A-B";
		} else {
			this.priceFlag = "B-A";
		}

		w_pricenotax
				.setCls(StrUtils.isNull(w_pricenotax.getCls()) ? "under_line"
						: "");
		w_price.setCls(StrUtils.isNull(w_price.getCls()) ? "under_line" : "");

		this.w_pricenotax.repaint();
		this.w_price.repaint();
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
			sql += "\nUPDATE fina_arap SET isconfirm = TRUE,updater = '"
					+ usercode
					+ "',updatetime = NOW() WHERE isdelete = FALSE AND id = "
					+ id + ";";
		}
		if (!StrUtils.isNull(sql)) {
			try {
				this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);
				MessageUtils.alert("OK");
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
			sql += "\nUPDATE fina_arap SET isconfirm = FALSE,updater = '"
					+ usercode
					+ "',updatetime = NOW() WHERE isdelete = FALSE AND id = "
					+ id + ";";
		}
		if (!StrUtils.isNull(sql)) {
			try {
				this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);
				MessageUtils.alert("OK");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/*
	 * 确认/取消业务报告
	 */
	@Action
	public void rptAjaxSubmit() {
		Boolean isconfirmrpt = jobs.getIsconfirmrpt();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isconfirmrpt) {
				sql = "UPDATE fina_jobs SET isconfirmrpt = TRUE ,isconfirmrpt_time = NOW(),isconfirmrpt_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirmrpt = FALSE ,isconfirmrpt_time = NOW(),isconfirmrpt_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isconfirmrpt ? "Confirm OK!" : " Cancel OK!");
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

	@Action
	public void createreqbill() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		try {
			Long reqid = serviceContext.jobsMgrService.creareReqBill(ids,
					usercode);
			AppUtils.openWindow("_newpayapplyedit",
					"./payapplyedit.xhtml?type=edit&id=" + reqid
							+ "&customerid=-1");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void chooseBusUnit() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		AppUtils.openWindow("_newChooseBusUnit", AppUtils.getContextPath()
				+ "/pages/sysmgr/unit/busunitchooser.xhtml?id="
				+ StrUtils.array2List(ids) + "&customerid=-1", 500, 380);
	}

	@Action
	public void showDynamic() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/feeinfo.raq";
		AppUtils.openWindow("_showDynamic", openUrl + "&jobid=" + this.jobid);
	}

	@Action
	public void showProfit() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/feeprofit.raq";
		AppUtils.openWindow("_showProfit", openUrl + "&id=" + this.jobid);
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
		if (this.jobid == -1l) {
			MessageUtils.alert("请先保存主表");
			return;
		}
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_imp_arap";

				// String args = this.jobid + "";
				String args = this.jobid + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Action
	public void showJoin() {
		isShowJoin = !isShowJoin;
		this.grid.reload();
	}

	@Action
	public void showJoinRpt() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/feejoin.raq";
		AppUtils.openWindow("_showJoin", openUrl + "&id=" + this.jobid);
	}

	@Action
	public void joinConfirm() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("至少选择两个!");
			return;
		}
		String idss = StrUtils.array2List(ids);
		String sql = "\nSELECT f_fina_fee_join('feeid=" + idss + ";feeitemid="
				+ join2Feeitemid + ";arap=" + join2ARAP + ";ppcc=" + join2PPCC
				+ ";currency=" + join2Currency + ";userid="
				+ AppUtils.getUserSession().getUserid() + "') AS join";

		try {
			//AppUtils.debug(sql);
			this.serviceContext.arapMgrService.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			MessageUtils.alert("OK!");
			this.join2Window.close();
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void joinClear() {
		String sql = "SELECT f_fina_fee_join_clear('jobid=" + this.jobid
				+ "');";
		try {
			//AppUtils.debug(sql);
			this.serviceContext.arapMgrService.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			MessageUtils.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIWindow join2Window;

	@Bind
	@SaveState
	private Long join2Feeitemid;

	@Bind
	@SaveState
	public String join2PPCC = "PP";

	@Bind
	@SaveState
	public String join2Currency = "CNY";

	@Bind
	@SaveState
	public String join2ARAP = "AR";

	/**
	 * 合并费用项目
	 */
	@Action
	public void join() {
		String ids[] = this.grid.getSelectedIds();

		if (ids == null || ids.length < 1) {
			this.alert("请至少一行将被合并的费用!");
			return;
		}
		join2Window.show();
		this.update.markUpdate(UpdateLevel.Data, "join2Feeitemid");
		// ishow = false;
	}

	@Bind(id = "join2Feeitem")
	public List<SelectItem> getJoin2Feeitem() {
		String ids[] = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			return null;
		}
		String id = StrUtils.array2List(ids);
		try {
			return CommonComBoxBean.getComboxItems("d.id",
					"d.code||'/'||COALESCE(name,'')", "dat_feeitem AS d",
					"WHERE d.code = 'ALLINCH' OR d.code = 'INLAND' OR d.code = 'YUNFEI' OR d.code = 'DISB' OR d.code = 'CLEARANCE CHARGE' ",
					"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

//	/**
//	 * 开始CC费用确认流程
//	 */
//	@Action
//	public void startCC() {
//		
//		if (this.jobs.getNos().contains("DJCD")) {
//			if(this.queryPricer()==false){
//				MessageUtils.alert("此单为单独清关,如果要启动CC费用确认流程,请指派报价客服!");
//				return;
//			}
//			
//		}
//		
//		if(queryStartCC()==true){
//			MessageUtils.alert("此单不为海运整柜单子,无法启动该流程!");
//			return;
//		}
//		
//		if(queryCCDivider() == false && queryPricer()== false){
//			MessageUtils.alert("没有寻找到该单指派流程的客服(报价客服或国内客服),请指派客服,再启动!");
//			return;
//		}
//		if(this.queryStart()==true){
//			pass("CCFeeConfirmProcess");
//			MessageUtils.alert("CC费用确认流程已启动!");
//		}else{
//			MessageUtils.alert("已启动该流程,不能重复启动!");
//		}
//	}
//	public void pass(String processid) {
//
//		Map<String, Object> m = new HashMap<String, Object>();
//		m.put("id", jobid);
//		m.put("sn", jobs.getNos());
//		try {
//			// WorkFlowUtil.startFF("ShipFCLProcess0712", m, AppUtil
//			// .getUserSession().getUsercode());
//			WorkFlowUtil.startFF(processid, m, AppUtils.getUserSession()
//					.getUsercode(),jobid);
//
//		} catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
//	}

//	/**
//	 * CC客服确认
//	 */
//
//	public void affirmNoCC() {
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

	/**
	 * CC财务确认
	 */
//	public void affirmNoCC2(Long id,String Actitivy) {
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(id,"CCFeeConfirmProcess",Actitivy,
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
//
//	}
	
//	/**
//	 * 确认判断是否重复启动流程
//	 */
//	public boolean queryStart(){
//		String sql = "SELECT v.value FROM t_ff_rt_taskinstance t,t_ff_rt_procinst_var v WHERE v.processinstance_id = t.processinstance_id AND t.process_id = 'CCFeeConfirmProcess' AND v.name = 'id' AND v.value LIKE 'java.lang.Long#%' AND  CAST(replace(v.value,'java.lang.Long#','') AS BIGINT) = "+jobid+";";
//		List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
//		if(list.size() <= 0 || "".equals(list)){
//			return true;
//		}else{return false;}
//	}
	
//	/**
//	 * 判断当前单号是否有客服分配
//	 */
//	public boolean queryCCDivider(){
//		List<Map> usersForRole = WorkFlowUtil.findAllUsersForRoleByJobsAssign("C",jobid);
//		if (usersForRole.size()==0){
//			return false;
//		}else{
//			return true;
//		}
//	}
	
//	/**
//	 *判断为正规海运才可启动该流程
//	 */
//	public boolean queryStartCC(){
//		String sql = "SELECT id FROM fina_jobs t WHERE t.ldtype = 'F' AND t.ldtype2 = 'F' AND t.isdelete = FALSE AND t.id = " + this.jobid;
//		List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
//		if(list.size() <= 0 || "".equals(list)){
//			return true;
//		}else{return false;}
//	}
	
//	/**
//	 * 判断是否有报价客服
//	 */
//	public boolean queryPricer(){
//		List<Map> usersForRole = WorkFlowUtil.findPricer("C",jobid);
//		if (usersForRole.size()==0){
//			return false;
//		}else{
//			return true;
//		}
//	}
	
	
	@Bind
	public UIButton add;
	@Bind
	public UIButton del;
	
    public void disableAllButton(Boolean ischeck) {
		add.setDisabled(ischeck);
		del.setDisabled(ischeck);
	}
}
