package com.scp.view.module.stock;

import java.math.BigDecimal;
import java.util.Calendar;
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

@ManagedBean(name = "pages.module.stock.wmsoutcheckeditBean", scope = ManagedBeanScope.REQUEST)
public class WmsOutCheckEditBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	
	@ManagedProperty("#{warehouselocalMgrService}")
	private WarehouselocalMgrService warehouselocalMgrService;
	
	@ManagedProperty("#{warenosoutchooseserviceBean}")
	private WarenosOutChooseService warenosoutchooseserviceBean;
	
	

	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;
	
	@ManagedProperty("#{wmsOutMgrService}")
	private WmsOutMgrService wmsOutMgrService;
	
	@ManagedProperty("#{wmsOutdtlMgrService}")
	private WmsOutdtlMgrService wmsOutdtlMgrService;
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@SaveState
	@Accessible
	public WmsOut selectedRowData= new WmsOut();
	
	@SaveState
	@Accessible
	public WmsOutdtl ddata =new WmsOutdtl();
	
	@SaveState
	@Accessible
	public String processInstanceId;
	
	@SaveState
	@Accessible
	public String workItemId;
	
	@Bind
	@SaveState
	public UIButton doSave;
	
	@Override
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(selectedRowData.getIscheck()==null){
				
			}else{
				disableAllButton(selectedRowData.getIscheck());
			}
		}
	}
	
	@Action
	public void showAttachmentIframe(){
		try {
			attachmentIframe.load(AppUtils.getContextPath()+"/pages/module/common/attachment.xhtml?linkid="+this.mPkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
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
		return this.customerService.getCustomerDataProvider();
	}
	
	@Action
	public void showCustomer(){
		this.popQryKey = AppUtils.getReqParam("customercode");
		this.customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}
	
	@Action
	public void customerGrid_ondblclick(){
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map)objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomerabbr((String) m.get("abbr"));
		this.update.markUpdate(UpdateLevel.Data,"customerid");
		this.update.markUpdate(UpdateLevel.Data,"customerabbr");
		showCustomerWindow.close();
	}
	

	
	@Bind
	public String popQryKey;
	
	@Action
	public void popQry(){
		this.customerService.qry(popQryKey);
		this.customerGrid.reload();
	}
	
	
	@Bind
	@SaveState
	public Integer stocknum;

	public void getStock() {
		Long customerid = this.selectedRowData.getCustomerid();
		String goodscode = this.ddata.getGoodscode();
		String sql = "" + "\nSELECT " + "\nsum(piece) AS piece "
				+ "\nFROM _wms_stock " + "\nWHERE  customerid  = " + customerid
				+ " AND goodscode = '" + goodscode + "'"
				+ "\nGROUP BY  customerid ,goodscode " + ";";
		//System.out.println("-------" + sql);
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			stocknum = ((BigDecimal) m.get("piece")).intValue(); //

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	@Bind
	public UIWindow showGoodsWindow;
	
	@Bind
	public UIDataGrid inventoryGrid;

	@Bind
	public String goodscodeQryKey;
	
	@Action
	public void goodscodeQry(){
		this.goodsService.qry(goodscodeQryKey);
		this.inventoryGrid.reload();
	}

	@Action
	public void showGoods(){
		/*this.selectedRowData.getCustomerid();
		//System.out.println(this.selectedRowData.getCustomerid());
		this.goodsinventoryService.qry(this.selectedRowData.getCustomerid());*/
		showGoodsWindow.show();
		inventoryGrid.reload();
	}
	
	@Action
	public void goodsGrid_ondblclick(){
		Object[] objs = inventoryGrid.getSelectedValues();
		Map m = (Map)objs[0];
		
		showGoodsWindow.close();
	
	}
	
	@Bind
	public String localset; //必须放在form里边 可以做一个易隐藏域好传值
	
	
	@Action
	public void setLocal(){
	String localInfo=AppUtils.getReqParam("localInfo");	//从action中的id中获取参数绑定(js中绑定的)
		
	Long s=	this.warehouselocalMgrService.findlocalid(localInfo);//获取locid
	
	//ddata.setLocid(s);
	
	this.update.markUpdate(UpdateLevel.Data,"locid");
	
		
	}
	
	
	
	private void disableAllButton(Boolean isCheck) {
		doSave.setDisabled(isCheck);
	}
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			affirmLast();
			sql = "UPDATE wms_out SET ischeck = TRUE ,checktime = "+(selectedRowData.getChecktime()!=null?"'"+selectedRowData.getChecktime()+"'":"NOW()")+" ,checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE wms_out SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id ="+this.mPkVal+";";
		}
		try {
			wmsOutMgrService.wmsOutDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isCheck);
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
			
		}
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
	
	@Bind
	public UIIFrame arapIframe;
	
	
	@Action
	public void showArapEdit() {
		try {
			if(this.mPkVal==-1l){
				return;
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
		processInstanceId =AppUtils.getReqParam("processInstanceId");
		 workItemId =AppUtils.getReqParam("workItemId");
		if(!StrUtils.isNull(id)){
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
			
		}
	}



	@Override
	public void refreshMasterForm() {
		this.selectedRowData = wmsOutdtlMgrService.wmsOutDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(UpdateLevel.Data, "mPkVal");
		showArapEdit();
		
		
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = wmsOutdtlMgrService.wmsOutdtlDao.findById(this.pkVal);
		
	}
	
	@Action
	@Override
	protected void doServiceSave() {
		if(this.mPkVal == -1l)return;
    	wmsOutdtlMgrService.saveDtlData(ddata);
		this.add();
		this.alert("OK");
	}

	@Override
	public void doServiceSaveMaster() {
		wmsOutdtlMgrService.saveMasterData(selectedRowData);
		
		this.mPkVal = selectedRowData.getId();
		this.alert("ok");
		
	}
	
	
	@Bind
	public UIIFrame stockChooserIframe;
	
	@Override
	public void add() {
		if(this.mPkVal == -1l){
			MessageUtils.alert("主表无数据,请先保存主表");
			return;
		}else{
			ddata = new WmsOutdtl();
			
			ddata.setOutid(this.mPkVal);
			
			super.add();
			
			stockChooserIframe.load("./wmsstockchooser.xhtml?customerid="+this.selectedRowData.getCustomerid()+"&outid="+this.mPkVal);
			//System.out.println(this.pkVal);
		}
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
//		this.data = getViewControl().findById(this.pkVal);
		doServiceFindData();
		this.refreshForm();
		if(editdtlWindow != null)editdtlWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editdtlPanel");
	}
	
	
	 @Action
	 public void showStock(){
		 add();
	 }
	
	
	
	@Override
	public void addMaster() {
		this.selectedRowData = new WmsOut();
		
		this.mPkVal = -1l;
		
  
		super.addMaster();
	}
	@Override
	public void del() {
		wmsOutdtlMgrService.removeDate(ddata.getId());
		this.add();
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
	
	
	@Bind(id="Localdtl")
    public List<SelectItem> getLocaldtl() {
		try {
			
			return CommonComBoxBean.getComboxItems("d.id","COALESCE(code,'')||'/'||COALESCE(namec,'')","dat_country AS d","WHERE 1=1","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/*@Action
	public void showReport() {
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/stock/wms-out.raq";
		ApplicationUtilBase.openWindow("_wmsinReport", openUrl + getArgs());
	}*/

	

	private String getArgs() {
		String args="";
		args+="&outid="+this.mPkVal;
		return args;
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
	
	@Action
	public void doSave(){
		try {
			wmsOutdtlMgrService.saveMasterData(selectedRowData);
			alert("ok");
		} catch (Exception e) {
			e.printStackTrace();
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
	public void shoWmsincheckAction(){
		String warenos = AppUtils.getReqParam("warenos");
		qryWarenosKey = warenos;
		int index = qryWarenosKey.indexOf("/");
		if (index > 1)
			qryWarenosKey = qryWarenosKey.substring(0, index);

		String type = AppUtils.getReqParam("type");
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
								" AND (wmstype = 'O' OR wmstype = 'L' OR  wmstype = 'P')");
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
		this.mPkVal = (Long.valueOf(ids[0]));
		this.update.markUpdate(UpdateLevel.Data, "mPkVal");
		shoWarenosWindow.close();
		refreshMasterForm();
		this.refresh();
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
