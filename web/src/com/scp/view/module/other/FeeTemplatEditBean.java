package com.scp.view.module.other;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.FinaArapTempdtl;
import com.scp.model.finance.FinaArapTemplet;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.other.feetemplateditBean", scope = ManagedBeanScope.REQUEST)
public class FeeTemplatEditBean extends MastDtlView {
	
	@SaveState
	@Accessible
	public FinaArapTemplet selectedRowData = new FinaArapTemplet();
	
	@SaveState
	@Accessible
	public FinaArapTempdtl ddata = new FinaArapTempdtl();
	

	@Bind
	public UIWindow batchEditWindow;
	
	@Override
	public void refreshForm() {
		
	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			//	disableAllButton(selectedRowData.getIscheck());
		}
	}
	

	@Override
	public void add() {
		super.add();
		this.ddata = new FinaArapTempdtl();
		this.ddata.setTempletid(this.mPkVal);
//		this.ddata.setCustomerid(customerid);
		this.ddata.setPpcc("PP");
		this.ddata.setTaxrate(new BigDecimal(1));
		this.ddata.setCurrency(AppUtils.getUserSession().getBaseCurrency());
		this.ddata.setAraptype("AR");
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void copyadd() {
		this.pkVal = -100L;

		FinaArapTempdtl temp = ddata;

		FinaArapTempdtl newObj = new FinaArapTempdtl();
		newObj.setAraptype(temp.getAraptype());
		newObj.setCustomerid(temp.getCustomerid());
		newObj.setCustomercode(temp.getCustomercode());
		newObj.setFeeitemid(temp.getFeeitemid());
		newObj.setPpcc(temp.getPpcc());
		newObj.setCustype(temp.getCustype());
		newObj.setCntypeid(temp.getCntypeid());
		newObj.setCurrency(temp.getCurrency());
		newObj.setTempletid(this.mPkVal);
		
		this.ddata = newObj;
		this.update.markUpdate(true, UpdateLevel.Data, "pkId");
		// super.initAdd();
		this.ddata.setAmount(new BigDecimal(0));

		this.update.markUpdate(UpdateLevel.Data, "amount");
		this.update.markUpdate(UpdateLevel.Data, "piece");
		this.update.markUpdate(UpdateLevel.Data, "price");
	}

	@Override
	public void init() {
		selectedRowData = new FinaArapTemplet();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else{
			addMaster();
		}
		//this.qryMap.put("templetid$", this.mPkVal);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND templetid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}


	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.feeTemplateMgrService.finaArapTempletDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.feeTemplateMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		//this.alert("ok");
	}
	
	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	
	//
//	private void disableAllButton(Boolean isCheck) {
//		saveMaster.setDisabled(isCheck);
//		delMaster.setDisabled(isCheck);
//	}
	
	
	public void addMaster() {
		//disableAllButton(false);
		this.selectedRowData = new FinaArapTemplet();
		//selectedRowData.setSingtime(new Date());
		this.selectedRowData.setIsprivate(true);
		this.mPkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	public void delMaster() {
		this.mPkVal = this.selectedRowData.getId();
		serviceContext.feeTemplateMgrService.removeDate(this.mPkVal,AppUtils.getUserSession().getUsercode());
		this.addMaster();
		this.alert("OK");
	}
	
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.feeTemplateMgrService.removeDtlDates(ids, AppUtils.getUserSession().getUsercode());
		qryRefresh();
	}
	
	//列表批量删除
	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.feeTemplateMgrService.removeDtlDates(ids, AppUtils.getUserSession().getUsercode());
		qryRefresh();
	}
	

	@Override
	protected void doServiceFindData() {
		ddata = serviceContext.feeTemplateMgrService.finaArapTempdtlDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.feeTemplateMgrService.saveDataDtl(ddata);
		//this.alert("OK");
	}
	
	
	/**
	 * 费用名称
	 * 
	 * @return
	 */
	@Bind(id = "feeitemtype")
	public List<SelectItem> getFeeitemtype() {
		try {
			return CommonComBoxBean.getComboxItems("d.id", "COALESCE(d.code,'')||'/'||COALESCE(d.name,'')",
					"dat_feeitem AS d", "WHERE  1=1 AND isdelete = false" , "ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
//	@Bind
//	public UIButton importData;
//	
//	@Bind
//	@SaveState
//	@Accessible
//	public String importDataText;
//
//	@Bind
//	public UIWindow importDataWindow;
//
//	@Action
//	public void importData() {
//		if (this.mPkVal == -1L) {
//			MessageUtils.alert("请先保存主表数据");
//			return;
//		} else {
//			importDataText = "";
//			importDataWindow.show();
//			this.update.markUpdate(UpdateLevel.Data, "importDataText");
//		}
//	}
//
//	@Action
//	public void importDataBatch() {
//		if (StrUtils.isNull(importDataText)) {
//			Browser.execClientScript("window.alert('" + "Data is null" + "');");
//			return;
//		} else {
//			try {
//				String callFunction = "f_imp_ship_booking";
//				String args = this.mPkVal + ",'"
//						+ AppUtils.getUserSession().getUsercode() + "'";
//				this.serviceContext.commonDBService.addBatchFromExcelText(
//						importDataText, callFunction, args);
//				MessageUtils.alert("OK!");
//				this.grid.reload();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
//		}
//	}
	
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
		return this.customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		String customercode = (String) AppUtils.getReqParam("customercode");
		String editType = (String) AppUtils.getReqParam("editType");
		this.editType = Boolean.parseBoolean(editType);
		update.markUpdate(UpdateLevel.Data, "editType");
		
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
				this.ddata.setCustomerid((Long) cs.get(0).get("id"));
				this.ddata.setCustomercode(cs.get(0).get("code")
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
		//System.out.println("editType"+editType);
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		String customercode=(String) m.get("code")+"/"+(String) m.get("abbr");
		Long customerid = (Long) m.get("id");
		if(editType){
			this.batch_customerid   = customerid;
			this.batch_customercode   = customercode;
			this.update.markUpdate(UpdateLevel.Data, "batch_customerid");
			this.update.markUpdate(UpdateLevel.Data, "batch_customercode");
		}else{
			this.ddata.setCustomerid(customerid);
			this.ddata.setCustomercode(customercode);
			this.update.markUpdate(UpdateLevel.Data, "customerid");
			this.update.markUpdate(UpdateLevel.Data, "customercode");
		}
		showCustomerWindow.close();
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
	public GridDataProvider getFeeitemGridDataProvider(){
		return this.customerService.getFeeitemDataProvider("");
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

			List<Map> cs = customerService.findFeeitem(qryFeeitemKey,"");

			if (cs.size() == 1) {
				Map map = cs.get(0);
				this.ddata.setFeeitemid((Long) map.get("id"));
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
		this.ddata.setFeeitemid(((Long) map.get("id")));
		this.update.markUpdate(UpdateLevel.ValueOnly, "feeitemid");
		showFeeitemWindow.close();
	}

	@Override
	public void save() {
		super.save();
		qryRefresh();
	}

	@Override
	public void qryRefresh() {
		super.qryRefresh();
		this.grid.reload();
	}
	
	@Action
	public void clearCustomercode(){
		this.ddata.setCustomercode(null);
		this.ddata.setCustomerid(0L);
	}
	
	@Action
	public void addBatch(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0){
			MessageUtils.alert("请至少勾选一项!");
			return;
		}
		batchQryRefresh();
		batchEditWindow.show();
	}
	@Action
	public void batchQryRefresh(){
		batch_customerid = -100L;
		batch_customercode = null;
		batch_arap = null;
		batch_ppcc = null;
		batch_custype = null;
		batch_currency = null;
		batch_amount = null;
		batch_cntypeid = null;
		update.markUpdate(true,UpdateLevel.Data, "batch_panel");
	}
	@Action
	public void batchSave(){
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("UPDATE fina_arap_tempdtl SET");
		if(!StrUtils.isNull(batch_arap)){
			sbsql.append(" araptype='");
			sbsql.append(batch_arap);
			sbsql.append("',");
		}
		if(!StrUtils.isNull(batch_ppcc)){
			sbsql.append(" ppcc='");
			sbsql.append(batch_ppcc);
			sbsql.append("',");
		}
		if(!StrUtils.isNull(batch_customercode) && batch_customerid > 0){
			sbsql.append(" customercode='");
			sbsql.append(batch_customercode);
			sbsql.append("',customerid=");
			sbsql.append(batch_customerid);
			sbsql.append(",");
		}
		if(!StrUtils.isNull(batch_custype)){
			sbsql.append(" custype='");
			sbsql.append(batch_custype);
			sbsql.append("',");
		}
		if(!StrUtils.isNull(batch_currency)){
			sbsql.append(" currency='");
			sbsql.append(batch_currency);
			sbsql.append("',");
		}
		if(!StrUtils.isNull(batch_amount)){
			sbsql.append(" amount=");
			sbsql.append(batch_amount);
			sbsql.append(",");
		}
		if(!StrUtils.isNull(batch_cntypeid)){
			sbsql.append(" cntypeid=");
			sbsql.append(batch_cntypeid);
			sbsql.append(",");
		}
		if(!StrUtils.isNull(batch_unit)){
			sbsql.append(" unit='");
			sbsql.append(batch_unit.replaceAll("'", "''"));
			sbsql.append("',");
		}
		if(sbsql.toString().endsWith(",")){
			StringBuilder sbsql2  = new StringBuilder(sbsql.substring(0, sbsql.length()-1));
			sbsql2.append(",updater='"+AppUtils.getUserSession().getUsercode()+"',updatetime='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"'");
			sbsql2.append(" WHERE isdelete = FALSE AND (id=-100 ");
			String[] ids = this.grid.getSelectedIds();
			for (String id : ids) {
				sbsql2.append("OR id=");
				sbsql2.append(id);
			}
			sbsql2.append(")");
			try {
				this.serviceContext.feeItemMgrService.datFeeitemDao.executeSQL(sbsql2.toString());
				MessageUtils.alert("OK!");
				batchEditWindow.close();
				this.qryRefresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
			
		}else{
			MessageUtils.alert("OK!");
		}
	}
	@Action
	public void batchDel(){
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("UPDATE fina_arap_tempdtl SET isdelete = TRUE");
		sbsql.append(",updater='"+AppUtils.getUserSession().getUsercode()+"',updatetime='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"'");
		sbsql.append(" WHERE isdelete = FALSE AND (id=-100 ");
		String[] ids = this.grid.getSelectedIds();
		for (String id : ids) {
			sbsql.append("OR id=");
			sbsql.append(id);
		}
		sbsql.append(")");
		try {
			this.serviceContext.feeItemMgrService.datFeeitemDao.executeSQL(sbsql.toString());
			MessageUtils.alert("OK!");
			batchEditWindow.close();
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public long batch_customerid;
	
	@Bind
	@SaveState
	public String batch_customercode;
	
	@Bind
	@SaveState
	public boolean editType;
	
	@Bind
	@SaveState
	public String batch_arap;
	
	@Bind
	@SaveState
	public String batch_ppcc;
	
	@Bind
	@SaveState
	public String batch_custype;
	
	@Bind
	@SaveState
	public String batch_currency;
	
	@Bind
	@SaveState
	public String batch_amount;
	
	@Bind
	@SaveState
	public String batch_unit;
	
	@Bind
	@SaveState
	public String batch_cntypeid;
	
	//列表批量新增
	@Action
	public void adds() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.feeTemplateMgrService.addsDates(ids, AppUtils.getUserSession().getUsercode());
		qryRefresh();
	}
}
