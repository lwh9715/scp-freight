package com.scp.view.module.stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporation;
import com.scp.model.wms.WmsOut;
import com.scp.model.wms.WmsOutdtl;
import com.scp.service.data.WarehouselocalMgrService;
import com.scp.service.wms.WmsOutMgrService;
import com.scp.service.wms.WmsOutdtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.GoodsStockBean;

@ManagedBean(name = "pages.module.stock.wmsouteditBean", scope = ManagedBeanScope.REQUEST)
public class WmsOutEditBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@ManagedProperty("#{warehouselocalMgrService}")
	private WarehouselocalMgrService warehouselocalMgrService;

	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;

	@ManagedProperty("#{wmsOutMgrService}")
	private WmsOutMgrService wmsOutMgrService;

	@ManagedProperty("#{wmsOutdtlMgrService}")
	private WmsOutdtlMgrService wmsOutdtlMgrService;
	
	@ManagedProperty("#{warenosoutchooseserviceBean}")
	private WarenosOutChooseService warenosoutchooseserviceBean;
	

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Accessible
	public WmsOut selectedRowData = new WmsOut();
	
	@SaveState
	@Accessible
	public String sql="";
	
	@Bind
	@SaveState
	public String soptext;
	
	@SaveState
	public String whcustomername="";

	@SaveState
	@Accessible
	public WmsOutdtl ddata = new WmsOutdtl();
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(selectedRowData.getIsubmit()==null){
				
			}else{
				disableAllButton(selectedRowData.getIsubmit());
			}
		}
	}


	@Action
	public void showAttachmentIframe() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				attachmentIframe.load(blankUrl);
			} else {
				attachmentIframe.load(AppUtils.getContextPath()
						+ "/pages/module/common/attachment.xhtml?linkid="
						+ this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	@SaveState
	public Integer stocknum;

	public void getStock() {
		Long customerid = this.selectedRowData.getCustomerid();
		String goodscode = this.ddata.getGoodscode();
		String goodsnamec = this.ddata.getGoodsnamec();
		String goodssize = this.ddata.getGoodssize();
		String markno = this.ddata.getMarkno();
		String goodscolor = this.ddata.getGoodscolor();
		String sql = ""
				+ "\nSELECT "
				+ "\nsum(piece) AS piece "
				+ "\nFROM _wms_stock "
				+ "\nWHERE  customerid  = "
				+ customerid
				+ " AND goodscode = '"
				+ goodscode
				+ "'"
				+ " AND goodsnamec = '"
				+ goodsnamec
				+ "'"
				+ " AND goodssize = '"
				+ goodssize
				+ "'"
				+ " AND markno = '"
				+ markno
				+ "'"
				+ " AND goodscolor = '"
				+ goodscolor
				+ "'"
				+ "\nGROUP BY  goodscode , customerid , goodsnamec,markno,goodssize,goodscolor"
				+ ";";
		//System.out.println("-------" + sql);
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			stocknum = ((BigDecimal) m.get("piece")).intValue(); //

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void refreshForm() {
		getStock();
		super.refreshForm();
		this.update.markUpdate(true, UpdateLevel.Data, "stocknum");

	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		sql="AND iswarehouse = true";
		return customerService.getCustomerDataProvider(sql);
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		this.customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomerabbr((String) m.get("abbr"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		this.customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Bind
	public UIWindow showGoodsWindow;

	@Bind
	public UIDataGrid inventoryGrid;

	@Bind
	public String goodscodeQryKey;

	@Action
	public void goodscodeQry() {
		this.goodsService.qry(goodscodeQryKey);
		this.inventoryGrid.reload();
	}

	@Action
	public void showGoods() {
		/*
		 * this.selectedRowData.getCustomerid();
		 * //System.out.println(this.selectedRowData.getCustomerid());
		 * this.goodsinventoryService.qry(this.selectedRowData.getCustomerid());
		 */
		showGoodsWindow.show();
		inventoryGrid.reload();
	}

	@Action
	public void goodsGrid_ondblclick() {
		Object[] objs = inventoryGrid.getSelectedValues();
		Map m = (Map) objs[0];

		showGoodsWindow.close();

	}

	@Bind
	public String localset; // 必须放在form里边 可以做一个易隐藏域好传值

	@Action
	public void setLocal() {
		String localInfo = AppUtils.getReqParam("localInfo"); // 从action中的id中获取参数绑定(js中绑定的)

		Long s = this.warehouselocalMgrService.findlocalid(localInfo);// 获取locid

		// ddata.setLocid(s);

		this.update.markUpdate(UpdateLevel.Data, "locid");

	}
	
	
	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	@Bind
	public UIButton save;
	
	@Bind
	public UIButton add;
	
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton delBatch;
	
	@Bind
	public UIButton impout;
	
	@Bind
	public UIButton updateLocal;
	
	
	
	@Bind
	public UIButton getGoods;
	
	
	@Bind
	public UIButton showStocks;
	
	
	
	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
		save.setDisabled(isCheck);
		add.setDisabled(isCheck);
		del.setDisabled(isCheck);
		delBatch.setDisabled(isCheck);
		impout.setDisabled(isCheck);
		updateLocal.setDisabled(isCheck);
		getGoods.setDisabled(isCheck);
		showStocks.setDisabled(isCheck);
		
	}

	@Action
	public void isubmitAjaxSubmit() {
		Boolean isSubmit = selectedRowData.getIsubmit();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		try {
			if (this.mPkVal == -1l)throw new CommonRuntimeException("Plese save first!");
			if (selectedRowData.getIscheck())throw new CommonRuntimeException("Current is check,plese uncheck first!");
			if (isSubmit) {
				sql = "UPDATE wms_out SET isubmit = TRUE ,submitime = NOW(),submiter = '"+updater+"' WHERE id ="+this.mPkVal+";";
			}else {
				sql = "UPDATE wms_out SET isubmit = FALSE ,submitime = NULL,submiter = NULL WHERE id ="+this.mPkVal+";";
			}
			serviceContext.wmsOutdtlMgrService().wmsOutDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isSubmit);
		}catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			refreshMasterForm();
			this.disableAllButton(!isSubmit);
		}catch (Exception e) {
			MessageUtils.showException(e);
			refreshMasterForm();
			this.disableAllButton(!isSubmit);
		}
	}
	@Action
	public void showStock() {
		add();
	}

	@Bind
	public UIIFrame arapIframe;

	@Action
	public void showArapEdit() {
		try {
			if(this.mPkVal==-1l){
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				arapIframe.load(blankUrl);
			}else{
			arapIframe.load("../finance/arapedit.xhtml?id=" + this.mPkVal
					+ "&jobid=" + findJobId()+"&customerid="+this.selectedRowData.getCustomerid()+ "&iswarehouse= Y ");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND outid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void init() {
		selectedRowData = new WmsOut();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();

		} else {
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = wmsOutdtlMgrService.wmsOutDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
		try {
			String sql = "SELECT sop FROM sys_corporation WHERE id = "+this.selectedRowData.getCustomerid();
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.size()>0&&m.get("sop")!=null){
				soptext = m.get("sop").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = wmsOutdtlMgrService.wmsOutdtlDao.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		if (this.mPkVal == -1l)
			return;

		wmsOutdtlMgrService.saveDtlData(ddata);

		this.alert("OK");
	}

	@Override
	public void doServiceSaveMaster() {
		if(this.selectedRowData.getCustomerid() != null){
			SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			this.selectedRowData.setCustomerabbr(sysCorporation.getAbbr());
		}
		wmsOutdtlMgrService.saveMasterData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");
		
	}

	@Bind
	public UIIFrame stockChooserIframe;

	@Override
	public void add() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("主表无数据,请先保存主表");
			return;
		} else {
			ddata = new WmsOutdtl();

			ddata.setOutid(this.mPkVal);

			super.add();

			stockChooserIframe.load("./wmsstockchooser.xhtml?customerid="
					+ this.selectedRowData.getCustomerid() + "&outid="
					+ this.mPkVal);
		}
	}

	@Bind
	public UIWindow editWindow;

	public void closeEidtWindow() {
		editWindow.close();
	}

	@Action(id = "editWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}

	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new WmsOut();
		Long warehouseid = this.serviceContext.userMgrService.findWareHaousId(AppUtils.getUserSession().getUserid());
		selectedRowData.setWarehouseid(warehouseid);
		selectedRowData.setSingtime(new Date());
		selectedRowData.setIslocal(true);
		super.addMaster();
	}

	@Bind
	public UIWindow editdtlWindow;

	@Bind
	public UIPanel editdtlPanel;

	@Bind
	@SaveState
	public Long pkVal;

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		if (editdtlWindow != null)
			editdtlWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editdtlPanel");

	}

	@Override
	public void saveMaster() {
		if (selectedRowData.getCustomerid() == null
				|| selectedRowData.getWarehouseid() == null
				|| selectedRowData.getSingtime() == null) {
			MessageUtils.alert("仓库,客户,制单日期不能为空");
			return;
		} else {
		this.mPkVal = selectedRowData.getId();
		super.saveMaster();
		}
	}

	@Override
	public void del() {
		wmsOutdtlMgrService.removeDate(ddata.getId());
		this.alert("OK");

		refresh();
	}

	@Override
	public void delMaster() {
		wmsOutMgrService.removeDate(this.mPkVal);
		this.addMaster();
		this.alert("OK");

		refresh();
	}

	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			wmsOutdtlMgrService.delBatch(ids);
			MessageUtils.alert("删除成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}

	/**
	 * 拣货按钮触发
	 */
	@Action
	public void getGoods() {
		String sql = "select f_wms_choose_stock(" + this.mPkVal + ");";
		try {
			wmsOutdtlMgrService.wmsOutdtlDao.executeQuery(sql);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 拣货报表
	 */
	/*
	@Action
	public void showGetGoodsReport() {
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/stock/wms-out-getgoods.raq";
		AppUtils.openWindow("_wmsinReport", openUrl + getArgs());
	}*/

	private String getArgs() {
		String args = "";
		args += "&outid=" + this.mPkVal;
		return args;
	}

	@Bind
	@SaveState
	@Accessible
	public String updateLocalText;

	@Bind
	public UIWindow updateLocalWindow;

	@Action
	public void updateLocal() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			impoutText = "";
			updateLocalWindow.show();
			this.update.markUpdate(UpdateLevel.Data, "impoutText");
		}
	}

	/**
	 * 直接写的,不是excel
	 */
	@Action
	public void updateLocalBatch() {
		if (StrUtils.isNull(updateLocalText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String data = StrUtils.toDBC(updateLocalText).replaceAll(" ", "");

			Long id = this.mPkVal;
			String user = AppUtils.getUserSession().getUsercode();
			try {
				String sql = "select f_wms_choose_stock_manual('" + data + "',"
						+ id + ",'" + user + "');";
				wmsOutdtlMgrService.wmsOutdtlDao.executeQuery(sql);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Bind
	@SaveState
	@Accessible
	public String impoutText;

	@Bind
	public UIWindow impoutWindow;

	@Action
	public void impout() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			impoutText = "";
			impoutWindow.show();
			this.update.markUpdate(UpdateLevel.Data, "impoutText");
		}
	}
	/**
	 * 是导入带表头的excel
	 */
	@Action
	public void impoutBatch() {

		if (StrUtils.isNull(impoutText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_wmsoutdetail";
				String args = this.mPkVal + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						impoutText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Bind
	public UIIFrame receiptIframe;
	
	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			receiptIframe.load(blankUrl);
		} else {
			// if(!showReceiptEdit)
			receiptIframe.load("../ship/busbill.xhtml?jobid=" + findJobId());
			// showReceiptEdit = true;
		}
	}
	
	@Bind
	public UIIFrame traceIframe;

	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		} else {
			// if(!showTrace)
			traceIframe.load("../bus/optrace.xhtml?jobid=" + findJobId());
			// showTrace = true;
		}
	}
	
	@Bind
	private String qryWarenosKey;
	
	@Bind
	@SaveState
	private UIWindow shoWarenosWindow;
	
	@Bind
	public UIDataGrid warenosGrid;
	
	@Bind
	@SaveState
	public String warenos;

	@Action
	public void warenosQry() {
		this.warenosoutchooseserviceBean.qryNos(qryWarenosKey);
		this.warenosGrid.reload();
	}
	
	@Action 
	public void showWarenosAction(){
		String warenos = (String) AppUtils.getReqParam("warenos");
		qryWarenosKey = warenos;
		int index = qryWarenosKey.indexOf("/");
		if (index > 1)
			qryWarenosKey = qryWarenosKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryWarenosKey");
			shoWarenosWindow.show();
			warenosQry();
			Browser.execClientScript("qryButton.focus");
			return;
			// type = '0',只返回一个直接回填,多个显示弹窗
		} else {
			try {
				List<Map> cs = warenosoutchooseserviceBean
						.findJobsnos(qryWarenosKey,
								"AND (wmstype = 'O' OR wmstype = 'L' OR  wmstype = 'P')");
				if (cs.size() == 1) {
					this.warenos = ((String) cs.get(0).get("nos"));
					this.mPkVal = ((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "warenos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					refreshMasterForm();
					this.refresh();
					shoWarenosWindow.close();
				} else {
					this.update.markUpdate(UpdateLevel.Data, "qryWarenosKey");
					shoWarenosWindow.show();
					warenosQry();
					Browser.execClientScript("qryButton.focus");
				}

			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	}
	
	@Bind(id = "warenosGrid", attribute = "dataProvider")
	public GridDataProvider getJobnosGridDataProvider() {
		return this.warenosoutchooseserviceBean
				.getJobsnosDataProvider(" AND (wmstype = 'O' OR wmstype = 'L' OR  wmstype = 'P')");
	}
	
	@Action
	public void warenosGrid_ondblclick() {
//		Object[] objs = warenosGrid.getSelectedValues();
		 String[] ids = warenosGrid.getSelectedIds();
		 if(ids == null && ids.length < 0){
			 return;
		 }
//		Map m = (Map)objs[0];
//		String wmsnos = ((String) m.get("nos"));
		this.mPkVal = (Long.valueOf(ids[0]));
		this.update.markUpdate(UpdateLevel.Data, "mPkVal");
		shoWarenosWindow.close();
		refreshMasterForm();
		this.refresh();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		try{
			if(this.selectedRowData.getCustomerid() != null){
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
				if(sysCorporation!=null){
					this.whcustomername = sysCorporation.getAbbr();
				}
			}
		}catch(NoRowException e){
			
		}
	}
	
	@Action
	public void saveMastersop(){
		if(selectedRowData!=null&&selectedRowData.getCustomerid()>0){
			SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			sysCorporation.setSop(soptext);
			serviceContext.sysCorporationService.saveData(sysCorporation);
		}
		this.saveMaster();
		Browser.execClientScript("tipsWindow.hide();");
	}
	
	
	@Inject(value = "l")
	private MultiLanguageBean l;
	
	@Bind(id = "warehouseoutformat")
	public List<SelectItem> getWarehouseoutformat() {
		try {
			if(l.m.getMlType()==MLType.en){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namee",
						"sys_report AS d", " WHERE modcode = 'warehouseout' AND isdelete = FALSE",
						"order by filename");
			}else if(l.m.getMlType()==MLType.ch){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'warehouseout' AND isdelete = FALSE",
						"order by filename");
			}else{
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'warehouseout' AND isdelete = FALSE",
						"order by filename");
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	@SaveState
	@Accessible
	public String showwarehouseoutformat;
	
	@Action
	public void preview() {
		if (showwarehouseoutformat == null || "".equals(showwarehouseoutformat)) {
			MessageUtils.alert("请选择格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		for (String s : AppUtils.getUserRoleModuleCtrl(900000L)){
			if (s.endsWith("eportdesign")) {
				String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?design=true&raq=/stock/"
				+ showwarehouseoutformat;
				AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
						+ "&userid="
						+ AppUtils.getUserSession().getUserid()
						+"&language="+AppUtils.getUserSession().getMlType().toString());
				return;
			} 
		}
		
		String openUrl = rpturl
		+ "/reportJsp/showReport.jsp?design=false&raq=/stock/"
		+ showwarehouseoutformat;
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
				+ "&userid="
				+ AppUtils.getUserSession().getUserid()
				+"&language="+AppUtils.getUserSession().getMlType().toString());
	}
	
	@Bind
	public UIWindow showLogsWindow;
	
	@Action
	public void showLogs() {
		showLogsWindow.show();
	}
	
}