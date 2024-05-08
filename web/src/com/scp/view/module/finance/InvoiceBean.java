package com.scp.view.module.finance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.operamasks.faces.component.form.impl.UIDateField;
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.developer.util.FacesUtils;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.model.data.DatAccount;
import com.scp.model.finance.FinaInvoice;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.invoiceBean", scope = ManagedBeanScope.REQUEST)
public class InvoiceBean extends GridFormView {
	
	@SaveState
	@Accessible
	public FinaInvoice selectedRowData = new FinaInvoice();
	
	@Bind
	public UIWindow showInvoicesWindow;
	
	@Bind
	@SaveState
	public String customernamec;
	
	@Bind
	@SaveState
	public String customerid;
	
	@Bind
	@SaveState
	public Date jobdatestart;
	
	@Bind
	@SaveState
	public Date jobdateend;
	
	@Bind
	@SaveState
	public Date etddatestart;
	
	@Bind
	@SaveState
	public Date etddateend;
	
	@Bind
	@SaveState
	public Date arapstart;
	
	@Bind
	@SaveState
	public Date arapend;
	
	@Bind
	@SaveState
	public String currency;
	
	@Bind
	@SaveState
	public String sono;
	
	@Bind
	@SaveState
	public String jobno;
	
	@Bind
	@SaveState
	public String remark;
	
	@Bind
	@SaveState
	public String araptype;
	
	@Bind
	@SaveState
	public String corpid;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryInvoices = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid clientGrid;
	
	@Bind
	private String qryCustomerKey;

	@Bind
	public UIWindow invoicesCountWindow;
	
	@Bind
	@SaveState
	public String invoiceClassify = "ym";
	
	@Bind
	@SaveState
	public Date ymDateFm = new Date();
	
	@Bind
	@SaveState
	public Date ymdDateFm = new Date();
	
	@Bind
	@SaveState
	public Date ymdDateTo = new Date();
	
	@Bind
	@SaveState
	public boolean usingNo;
	
	@Bind
	@SaveState
	public String noFm;
	
	@Bind
	@SaveState
	public String noTo;
	
	@Bind
	@SaveState
	public String invoicesCurrency;
	
	@Bind
	@SaveState
	public boolean includeDelete;
	
	@Bind
	@SaveState
	public String invoicesReport;
	
	@SaveState
	@Bind(id="ymDateFm")
	public UIDateField ymDateFm_ui;
	
	@SaveState
	@Bind(id="ymdDateFm")
	public UIDateField ymdDateFm_ui;
	
	@SaveState
	@Bind(id="ymdDateTo")
	public UIDateField ymdDateTo_ui;
	
	@SaveState
	@Bind(id="noFm")
	public UITextField noFm_ui;
	
	@SaveState
	@Bind(id="noTo")
	public UITextField noTo_ui;
	
	@Bind
	public UIButton invoiceSetup;
	
	@Bind
	public UIButton invoicePrint;
	
	@Bind
	public UIButton invoiceInvalid;
	
	
	@Bind
	@SaveState
	public String isOpenInvHT_E; //系统设置是否开通航天电子发票接口
	
	@Bind
	@SaveState
	public String isOpenInvHT_Page; //系统设置是否开通航天纸质发票接口
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
//			ymdDateFm_ui.setDisabled(true);
//			ymdDateTo_ui.setDisabled(true);
//			noFm_ui.setDisabled(true);
//			noTo_ui.setDisabled(true);
			this.customerChooseBean.setQryforNull();
			this.customerChooseBean.setQrysqlforNull();
			initCtrl();
			identity = ConfigUtils.findUserCfgVal("invoice_identity", AppUtils.getUserSession().getUserid());
			kptype = ConfigUtils.findUserCfgVal("invoice_kptype", AppUtils.getUserSession().getUserid());
			fphxz = ConfigUtils.findUserCfgVal("invoice_fphxz", AppUtils.getUserSession().getUserid());
			String pageSize = ConfigUtils.findUserCfgVal("pages.module.finance.invoiceBean.invoicesGrid", AppUtils.getUserSession().getUserid());
			if (!StrUtils.isNull(pageSize) && StrUtils.isNumber(pageSize)) {
				invoicesGridPageSize = Integer.parseInt(pageSize);
			}
			
			isOpenInvHT_E = ConfigUtils.findSysCfgVal("inv_ht_e");
			isOpenInvHT_Page = ConfigUtils.findSysCfgVal("inv_ht_page");
		}
	}

	@Action
	public void preview() {
//		String rpturl = AppUtil.getContextPath();
//		try{
//			String filename = AppUtil.findReportFile();
//			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/"+filename+"&pkid="+this.pkVal;
//			AppUtil.openWindow("_print", openUrl);
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//		}
	}
	
	@Action(id="grid",event="ondblclick")
	public void grid_ondblclicks() {
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			return;
		}
		//String sqls = "SELECT f_refresh_invoice_rate('customerid=-1;currencyto=;invoiceid="+ids[0]+";');";
		//this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqls);
		AppUtils.openWindow("发票编辑", "./invoicedtl.aspx?&id="+ids[0]);
	}
	
	@Action
	public void changeAccountAction(){
		String accountid = AppUtils.getReqParam("accountid");
		if(StrUtils.isNull(accountid)){
			
		}else{
			DatAccount datAccount = this.serviceContext.accountMgrService.datAccountDao.findById(Long.parseLong(accountid));
			this.selectedRowData.setAccountcont(datAccount.getAccountcont());
			update.markUpdate(true, UpdateLevel.Data, "accountcont");
		}
	}
	
	@Action
	public void clientGrid_ondblclick() {//左边客户双击
		String id = clientGrid.getSelectedIds()[0];
		try {
			for (String s : AppUtils.getUserRoleModuleCtrl(512000L)) {
				if (s.endsWith("_add")) {
					AppUtils.openWindow("发票编辑", "./invoicedtl.aspx?&customerid="+id);
					return;
				}else{
					this.alert("无新增发票权限!");
				}
			}
			//String sqls = "SELECT f_refresh_invoice_rate('customerid="+id+";currencyto=CNY;invoiceid=-1;');";
			//this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqls);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public String qrytyoe;
	@Bind
	public String qryfindv;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.setQryforNull();
		this.customerChooseBean.setQrysqlforNull();
		this.customerChooseBean.qryInvoice(qryCustomerKey,true,qrytyoe,qryfindv);
		this.clientGrid.reload();
	}
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return this.customerChooseBean.getInvoiceClientDataProvider();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.invoiceMgrService.finaInvoiceDao.findById(pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.invoiceMgrService.saveData(selectedRowData);
		this.pkVal = selectedRowData.getId();
		this.alert("OK");
		doServiceFindData();
	}
	
	@Bind(id="customers")
    public List<SelectItem> getCustomers() {
    	try {
    		//String whereSql = "WHERE corpid=" + corpid + " AND isinvalid = TRUE AND iscsuser = false AND isys = TRUE and isdelete = false";
			//return getComboxItems("d.code","d.code","sys_user AS d",whereSql,"ORDER BY d.code");
    		return null;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Action(id = "clientGrid", event = "onrowselect")
    public void departmentGrid_onrowselect() {
		String[] ids = this.clientGrid.getSelectedIds();
        if (ids != null) {
        	this.customernamec = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.parseLong(ids[0])).getNamec();
    		this.customerid = ids[0];
    		update.markUpdate(UpdateLevel.Data,"customernamec");
    		update.markUpdate(UpdateLevel.Data,"customerid");
    		qryInvoices.remove("qry");
    		qryInvoices.put("qry", " OR customerid = "+customerid);
    		this.invoicesGrid.reload();
    		//刷新右边发票窗口
    		this.qryMap.put("clientid$", customerid);
    		this.grid.reload();
        }
    }
	
	@Action
	public void showInvoices(){
		String[] ids = this.clientGrid.getSelectedIds();
		if(ids==null||ids.length < 1){
			MessageUtils.alert("请先在左边客户面板点选待搜索的客户!");
			return;
		}
		this.customernamec = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.parseLong(ids[0])).getNamec();
		this.customerid = ids[0];
		//String sqls = "SELECT f_refresh_invoice_rate('customerid="+customerid+";currencyto=CNY;invoiceid=-1;');";
		//this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqls);
		update.markUpdate(UpdateLevel.Data,"customernamec");
		update.markUpdate(UpdateLevel.Data,"customerid");
		qryInvoices.remove("qry");
		qryInvoices.put("qry", " OR customerid = "+customerid);
		if(qryInvoices.containsKey("filter")){
			qryInvoices.remove("filter");
		}
		qryInvoices.put("filter", "AND a.araptype = 'AR'");
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			qryInvoices.put("corpfilter", " \n AND a.corpid="+AppUtils.getUserSession().getCorpidCurrent());
//		}
		String corpfilter = "\n AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE  x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		qryInvoices.put("corpfilter", corpfilter);
		this.invoicesGrid.reload();
		this.showInvoicesWindow.show();
	}
	
	@Action
	public void searchArap(){
		StringBuffer clauseWhere = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(qryInvoices.containsKey("filter")){
			qryInvoices.remove("filter");
		}
		if(this.jobdatestart != null){
			clauseWhere.append("\n AND EXISTS (SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = a.jobid AND x.jobdate >= '"+dateFormat.format(this.jobdatestart)+"')");
		}
		if(this.jobdateend != null){
			clauseWhere.append("\n AND EXISTS (SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = a.jobid AND x.jobdate <= '"+dateFormat.format(this.jobdateend)+"')");
		}
		
		if(this.etddatestart != null){
			clauseWhere.append("\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND etd >= '"+dateFormat.format(this.etddatestart)+"')");
		}
		if(this.etddateend != null){
			clauseWhere.append("\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND etd <= '"+dateFormat.format(this.etddateend)+"')");
		}
		
		if(this.arapstart != null){
			clauseWhere.append("\n AND a.arapdate >='"+dateFormat.format(this.arapstart)+"'");
		}
		if(this.arapend != null){
			clauseWhere.append("\n AND a.arapdate <='"+dateFormat.format(this.arapend)+"'");
		}
		if(this.currency != null && !this.currency.isEmpty()){
			clauseWhere.append("\n AND a.currency = '"+this.currency+"'");
		}
		if(this.sono != null && !this.sono.isEmpty()){
			clauseWhere.append("\n AND (EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND x.sono = '"+this.sono+"') OR EXISTS (SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND x.sono = '"+this.sono+"') )");
		}
		if(this.jobno != null && !this.jobno.isEmpty()){
			clauseWhere.append("\n AND EXISTS (SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = a.jobid AND x.nos ILIKE '%"+jobno+"%')");
		}
		if(this.remark != null && !this.remark.isEmpty()){
			clauseWhere.append("\n AND remarks ILIKE '%"+remark+"%' ");
		}
		if(this.araptype != null && "ar".equals(this.araptype)){
			clauseWhere.append("\n AND a.araptype = 'AR'");
		}else if(this.araptype != null && "ap".equals(this.araptype)){
			clauseWhere.append("\n AND a.araptype = 'AP'");
		}
		if(!StrUtils.isNull(corpid)){
			clauseWhere.append("\n AND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=a.corpid AND sc.id = '"+corpid+"')");	
		}
		qryInvoices.put("filter", clauseWhere.toString());
		this.invoicesGrid.reload();
	}
	
	@Action
	public void enterArap(){
		String[] ids = this.invoicesGrid.getSelectedIds();
		if(ids!=null && ids.length > 0){
			selectedRowData = new FinaInvoice();
			selectedRowData.setInvoicedate(new Date());
			selectedRowData.setCurrency(AppUtils.getUserSession().getBaseCurrency());
			this.pkVal = -1L;
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			update.markUpdate(true, UpdateLevel.Data, "pkVal");
			this.selectedRowData.setClientid(Long.parseLong(this.customerid));
			this.selectedRowData.setClientname(this.customernamec);
			int max = ids.length;
			StringBuffer sb = new StringBuffer();
			for (int i=0;i<ids.length;i++) {
				sb.append(ids[i]);
				if(max!=i){
					sb.append(",");
				}
			}
			FacesUtils.getFlash().put("invoiceids", sb.toString());
			AppUtils.openWindow("发票编辑", "./invoicedtl.aspx?customerid="+this.customerid);
		}else{
			MessageUtils.alert("开综合发票请至少勾选1条费用!");
		}
	}
	
	@Action
	public void closeArapWindow(){
		this.showInvoicesWindow.close();
	}
	
	/**
	 * 综合发票窗口
	 */
	@Bind
	public UIDataGrid invoicesGrid;

	@Bind(id = "invoicesGrid", attribute = "dataProvider")
	protected GridDataProvider getInvoiceDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.invoiceBean.invoicesGrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, qryInvoices, start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.invoiceBean.invoicesGrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, qryInvoices);
				if (list == null || list.size() < 1){
					return 0;
				}
				String countStr = list.get(0).get("counts").toString();
				Long count = Long.parseLong(StrUtils.isNull(countStr)?"0":countStr);
				return count.intValue();
			}
		};
	}
	
	
	public Integer invoicesGridPageSize = 100;
	
	@Action
	public void doChangeInvoicesGridPage(){//每页条数设定
		String page = AppUtils.getReqParam("page");
		this.invoicesGrid.setRows(Integer.parseInt(page));
		this.invoicesGrid.rebind();
		try {
			ConfigUtils.refreshUserCfg("pages.module.finance.invoiceBean.invoicesGrid", page, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void doChangeClientGridPage(){//每页条数设定
		String page = AppUtils.getReqParam("page");
		this.clientGrid.setRows(Integer.parseInt(page));
		this.clientGrid.rebind();
	}
	
	@Bind
	@SaveState
	public String qrynos;

	@Bind
	@SaveState
	private String invoicestr;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		if(map.containsKey("qry")){
			String qry = map.get("qry").toString();
			String qry3 = "";
			qry = qry.replace("AND UPPER(nos) LIKE UPPER(", "AND EXISTS (SELECT 1 FROM fina_jobs x,fina_arap y WHERE x.id = y.jobid AND t.id = y.invoiceid AND x.isdelete = FALSE AND y.isdelete = FALSE AND x.nos ILIKE ");
			map.remove("eqy");
			String qry1 = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x,sys_corporation y WHERE x.corpid = y.corpid AND y.id = t.clientid AND y.isdelete = false AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
			if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())qry += qry1;//非saas模式不控制
			if(!StrUtils.isNull(qrynos)){
				qry += "\n AND (EXISTS(SELECT 1 FROM fina_jobs a,fina_arap b WHERE a.id = b.jobid AND t.id = b.invoiceid AND a.isdelete = FALSE AND b.isdelete = FALSE AND a.nos LIKE '%"+qrynos+"%')"
					+  "\n OR EXISTS(SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.id = t.jobid AND x.nos LIKE '%"+qrynos+"%'))";
			}
			if (!StrUtils.isNull(invoicestr)) {
				map.put("qry3"," AND editype = '"+invoicestr+"'");
			}
			map.put("qry", qry);
		}
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			String corpfilter = "AND t.corpid="+AppUtils.getUserSession().getCorpidCurrent();
//			map.put("corpfilter", corpfilter);
//		}
		String corpfilter = "\n AND t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
			corpfilter = "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = "+AppUtils.getUserSession().getCorpid()+") " +
					"			THEN " +
					"				t.corpid = ANY(SELECT id FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())" +
						"								UNION ALL" +
						"								SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + 
						"								)" +
					"			ELSE " +
					"				t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")" +
					"			END)";
		}else{
			corpfilter = "\n AND t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")";
		}
		if (!StrUtils.isNull(bankCode)) {
			corpfilter += "\n AND t.accountid = ANY(SELECT DISTINCT d.id FROM dat_account d WHERE d.isdelete = false AND d.code ILIKE '%" + bankCode + "%')";
		}
		if (!StrUtils.isNull(bankName)) {
			corpfilter += "\n AND t.accountid = ANY(SELECT DISTINCT d.id FROM dat_account d ,dat_bank da WHERE d.bankid = da.id AND d.isdelete = false AND da.abbr ILIKE '%" + bankName + "%')";
		}
		map.put("corpfilter", corpfilter);
		return map;
	}
	
	@Action
	public void invoiceCountPanel(){
		if(invoicesCountWindow!=null){
			invoicesCountWindow.show();
		}
	}
	
	@Action(id="invoiceClassify",event="onchange")
	public void invoiceClassifyChange(){
		if("ym".equals(invoiceClassify)){
			ymDateFm_ui.setDisabled(true);
			ymdDateFm_ui.setDisabled(false);
			ymdDateTo_ui.setDisabled(false);
		}else{
			ymDateFm_ui.setDisabled(false);
			ymdDateFm_ui.setDisabled(true);
			ymdDateTo_ui.setDisabled(true);
		}
		update.markUpdate(true,UpdateLevel.Data, ymDateFm_ui);
		update.markUpdate(true,UpdateLevel.Data, ymdDateFm_ui);
		update.markUpdate(true,UpdateLevel.Data, ymdDateTo_ui);
	}
	
	@Action
	public void showCount(){
		if(invoicesReport == null || invoicesReport.isEmpty()){
			MessageUtils.alert("请先选择需要查询的统计表!");
			return;
		}
		
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("invoiceClassify",invoiceClassify);
		argsMap.put("ymDateFm",new SimpleDateFormat("yyyy-MM").format(ymDateFm==null ? new Date() : ymDateFm));
		argsMap.put("ymdDateFm",new SimpleDateFormat("yyyy-MM-dd").format(ymdDateFm==null ? new Date() : ymdDateFm));
		argsMap.put("ymdDateTo",new SimpleDateFormat("yyyy-MM-dd").format(ymdDateTo==null ? new Date() : ymdDateTo));
		
		argsMap.put("usingNo",String.valueOf(usingNo));
		argsMap.put("noFm",noFm);
		argsMap.put("noTo",noTo);
		argsMap.put("invoicesCurrency",invoicesCurrency);
		argsMap.put("includeDelete",String.valueOf(includeDelete));
		argsMap.put("userid",AppUtils.getUserSession().getUserid().toString());
		String urlArgs = AppUtils.map2Url(argsMap,":");
		
		String arg = "&currency="+invoicesCurrency+"&para=" + urlArgs+"&userid="+AppUtils.getUserSession().getUserid();
		String openUrl = AppUtils.getRptUrl() + "/reportJsp/showReportReadOnly.jsp?raq=/static/inv/" + invoicesReport;
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
	}
	@Action
	public void cancelCount(){
		if(invoicesCountWindow!=null){
			invoicesCountWindow.close();
		}
	}
	
	
	@Bind
	@SaveState
	private String invSvr;
	
	@Bind
	@SaveState
	private String invoiceTypev;
	
	@Bind
	@SaveState
	private String listed;
	
	@Bind
	@SaveState
	private String CheckEWM;
	
	@Bind
	@SaveState
	private String ncpFlag;
	
	@Bind
	@SaveState
	private String identity;
	
	@Bind
	@SaveState
	private String kptype;
	
	@Bind
	@SaveState
	private String fphxz;
	
	@Bind
	@SaveState
	private String invSvrElectron;
	
	@Action
	public void openinginvoice(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			alert("请选择一行！");
			return;
		}
		try{
			invSvr = serviceContext.invoiceMgrService.openinginvoice(ids, invoiceTypev, listed, CheckEWM, ncpFlag);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		update.markUpdate(true, UpdateLevel.Data, "setInvoicepennel");
		this.grid.reload();
	}
	
	@Action
	public void openinginvoiceElectron(){
		try{
			ConfigUtils.refreshUserCfg("invoice_identity",identity, AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("invoice_kptype",kptype, AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("invoice_fphxz",fphxz, AppUtils.getUserSession().getUserid());
		}catch(Exception e){
			e.printStackTrace();
		}
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			alert("请选择一行！");
			return;
		}
		try{
			invSvrElectron = serviceContext.invoiceMgrService.openingElectronInvoice(ids, identity, kptype, fphxz);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		update.markUpdate(true, UpdateLevel.Data, "setInvoicepennelElectron");
		this.grid.reload();
	}
	
	@Bind
	@SaveState
	private String getInvoiceElectron;
	
	@Action
	public void getInvoice(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			alert("请选择一行！");
			return;
		}
		try{
			getInvoiceElectron = serviceContext.invoiceMgrService.getInvoiceByOrderno(ids,identity);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		update.markUpdate(true, UpdateLevel.Data, "getInvoicepennelElectron");
		this.grid.reload();
	}
	
	@Bind
	@SaveState
	private String infoKindTypev;
	
	@Bind
	@SaveState
	private String printKind;
	
	@Bind
	@SaveState
	private String printInvSvr;
	
	@Action
	public void printinvoice(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			alert("请选择一行！");
			return;
		}
		try{
			printInvSvr = serviceContext.invoiceMgrService.printinvoice(ids, infoKindTypev, printKind);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		update.markUpdate(true, UpdateLevel.Data, "setPrintInvoicepennel");
		this.grid.reload();
	}
	
	@Bind
	public String printCancelInvoice;
	
	@Bind
	@SaveState
	private String cancelInvoiceTypev;
	
	@Action
	public void cancelInvoice(){
		printCancelInvoice = "";
		update.markUpdate(true, UpdateLevel.Data, "cancelInvoicepennel");
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			alert("请选择一行！");
			return;
		}
		try{
			printCancelInvoice = serviceContext.invoiceMgrService.cancelInvoice(ids, cancelInvoiceTypev);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		update.markUpdate(true, UpdateLevel.Data, "cancelInvoicepennel");
		this.grid.reload();
	}

	@Override
	public void clearQryKey() {
		qrynos = "";
		Browser.execClientScript("qrynosJsvar.setValue('')");
		super.clearQryKey();
	}
	
	
	//权限控制
	private void initCtrl() {
		invoiceSetup.setDisabled(true);
		invoicePrint.setDisabled(true);
		invoiceInvalid.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(512000L)) {
			if (s.endsWith("_setup")) {
				invoiceSetup.setDisabled(false);
			} else if (s.endsWith("_print")) {
				invoicePrint.setDisabled(false);
			} else if (s.endsWith("_invalid")) {
				invoiceInvalid.setDisabled(false);
			}
		}
	}
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_sys_corpinv";
				String args = "-1,'"+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Bind
	@SaveState
	private String bankCode;

	@Bind
	@SaveState
	private String bankName;

	@Action
	public void searchfee() {
		this.qryRefresh();
	}

}
