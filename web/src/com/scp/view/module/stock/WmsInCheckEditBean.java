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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.model.wms.WmsIn;
import com.scp.model.wms.WmsIndtl;
import com.scp.service.data.WarehouselocalMgrService;
import com.scp.service.wms.WmsInMgrService;
import com.scp.service.wms.WmsIndtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.GoodsStockBean;

@ManagedBean(name = "pages.module.stock.wmsincheckeditBean", scope = ManagedBeanScope.REQUEST)
public class WmsInCheckEditBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	
	@ManagedProperty("#{warehouselocalMgrService}")
	private WarehouselocalMgrService warehouselocalMgrService;
	
	

	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;
	
	@ManagedProperty("#{wmsInMgrService}")
	private WmsInMgrService wmsInMgrService;
	
	@ManagedProperty("#{wmsIndtlMgrService}")
	private WmsIndtlMgrService wmsIndtlMgrService;
	
	@ManagedProperty("#{warenoschooseserviceBean}")
	private WarenosChooseService warenosChooseService;
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@SaveState
	@Accessible
	public WmsIn selectedRowData= new WmsIn();
	
	@SaveState
	@Accessible
	public WmsIndtl ddata =new WmsIndtl();
	
	@SaveState
	@Accessible
	public String processInstanceId;
	
	@SaveState
	@Accessible
	public String workItemId;
	
	
	@SaveState
	public String whcustomername="";
	
	@SaveState
	public String salenamec="";
	
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
		this.localId = this.ddata.getLocid();
		getInitLoccalDesc();
		super.refreshForm();
		update.markUpdate(true, UpdateLevel.Data, "locdesc");
		
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
	public UIWindow showGoodsWindow;
	
	@Bind
	public UIDataGrid goodsGrid;

	@Bind
	public String goodscodeQryKey;
	
	@Action
	public void goodscodeQry(){
		this.goodsService.qry(goodscodeQryKey);
		this.goodsGrid.reload();
	}
	
	@Bind(id = "goodsGrid", attribute = "dataProvider")
	public GridDataProvider getGoodsGridDataProvider() {
		return this.goodsService.getgoodsDataProvider();
	}
	
	@Action
	public void showGoods(){
		//this.goodscodeQryKey = "";
		//this.goodsService.qry(null);
		showGoodsWindow.show();
		goodsGrid.reload();
	}
	
	@Action
	public void goodsGrid_ondblclick(){
		Object[] objs = goodsGrid.getSelectedValues();
		Map m = (Map)objs[0];
		ddata.setGoodsid((Long) m.get("id"));
		ddata.setGoodscode((String) m.get("code"));
		ddata.setGoodsnamec((String) m.get("namec"));
		ddata.setGoodsnamee((String) m.get("namee") );
		ddata.setGoodscolor((String) m.get("color"));
		ddata.setGoodsl((BigDecimal) m.get("l"));
		ddata.setGoodsw((BigDecimal) m.get("w"));
		ddata.setGoodsh((BigDecimal) m.get("h"));
		ddata.setGoodsv((BigDecimal) m.get("v"));
		
		 
		
		
		this.update.markUpdate(UpdateLevel.Data,"goodsid");
		this.update.markUpdate(UpdateLevel.Data,"goodscode");
		this.update.markUpdate(UpdateLevel.Data,"goodsnamec");
		this.update.markUpdate(UpdateLevel.Data,"goodsnamee");
		this.update.markUpdate(UpdateLevel.Data,"goodscolor");
		this.update.markUpdate(UpdateLevel.Data,"goodsl");

		this.update.markUpdate(UpdateLevel.Data,"goodsw");
		this.update.markUpdate(UpdateLevel.Data,"goodsh");
		this.update.markUpdate(UpdateLevel.Data,"goodsv");
		showGoodsWindow.close();
	
	}
	
	@SaveState
	@Accessible
	public Long  localId ;
	
	
	@Action
	public void setLocal(){
	String localInfo=AppUtils.getReqParam("localInfo");	//从action中的id中获取参数绑定(js中绑定的)
		
	Long s=	this.warehouselocalMgrService.findlocalid(localInfo);//获取locid
	
	ddata.setLocid(s);
	
	this.update.markUpdate(UpdateLevel.Data,"locid");
	this.getInitLoccalDesc();
	this.update.markUpdate(UpdateLevel.Data, "locdesc");
		
	}
	
	@Bind
	@SaveState
	public String locdesc = "";
	
	// 加载储位
	public void getInitLoccalDesc() {
		String sql = "\nSELECT  f_findwmslocaldesc(" + this.pkVal
				+ ",NULL,'I') AS localdesc" + ";";
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			locdesc = (String) m.get("localdesc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	


	
	@Bind
	public UIIFrame arapIframe;
	
	
	
	@Action
	public void showArapEdit() {
		try {
			
			arapIframe.load("../finance/arapedit.xhtml?id=" + this.mPkVal
					+ "&jobid=" + findJobId()+"&customerid="+this.selectedRowData.getCustomerid()+ "&iswarehouse= Y ");
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}

	@Bind
	public UIButton updateLocal;
	
	
	private void disableAllButton(Boolean isCheck) {
		
		updateLocal.setDisabled(isCheck);
		
	}
	
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			sql = "UPDATE wms_in SET ischeck = TRUE ,checktime = "+(selectedRowData.getChecktime()!=null?"'"+selectedRowData.getChecktime()+"'":"NOW()")+" ,checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
			affirmLast();
		}else {
			sql = "UPDATE wms_in SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id ="+this.mPkVal+";";
		}
		try {
			wmsInMgrService.wmsInDao.executeSQL(sql);
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
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND inid = " + mPkVal;
        m.put("qry", qry);
		return m;
	}



	@Override
	public void init() {
		selectedRowData = new WmsIn();
		String id = AppUtils.getReqParam("id");
		processInstanceId =(String)AppUtils.getReqParam("processInstanceId");
		 workItemId =(String)AppUtils.getReqParam("workItemId");
		if(!StrUtils.isNull(id)){
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
			
		}
	}



	@Override
	public void refreshMasterForm() {
		this.selectedRowData = wmsIndtlMgrService.wmsInDao.findById(this.mPkVal);
		
		
		if(this.selectedRowData.getCustomerid() != null){
			SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			if(sysCorporation!=null){
				this.whcustomername = sysCorporation.getAbbr();
			}
		}
		
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
		showAttachmentIframe();
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = wmsIndtlMgrService.wmsIndtlDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
    if(this.mPkVal == -1l)return;
		
    wmsIndtlMgrService.saveDtlData(ddata);
		this.add();
		this.alert("OK");
		
	}

	@Override
	public void doServiceSaveMaster() {
		
		try {
			wmsIndtlMgrService.saveMasterData(selectedRowData);
			
			this.mPkVal = selectedRowData.getId();
			this.alert("ok");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	@Override
	public void add() {
		ddata = new WmsIndtl();
		ddata.setCurrency(AppUtils.getUserSession().getBaseCurrency());
		ddata.setInid(this.mPkVal);
		
		
		super.add();
	}
	

	@Override
	public void addMaster() {
		this.selectedRowData = new WmsIn();
		
		this.mPkVal = -1l;
		
  
		super.addMaster();
	}
	@Override
	public void del() {
		wmsIndtlMgrService.wmsIndtlDao.remove(ddata);
		this.add();
		this.alert("OK");
		
		
		refresh();
	}
	
	
	@Override
	public void delMaster() {
		wmsIndtlMgrService.removeDate(this.mPkVal);
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
	
	@Bind
	@SaveState
	@Accessible
	public String splitLocalText;

	@Bind
	public UIWindow splitLocalWindow;

	@Action
	public void splitLocal() {
		splitLocalText = "";
		splitLocalWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "splitLocalText");
	}

	@Action
	public void splitLocalBatch() {
		if (StrUtils.isNull(splitLocalText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String data = StrUtils.toDBC(splitLocalText).replaceAll(" ", "")
					.replaceAll("\\n", "");
			String[] format = splitLocalText.split(",");
			String rex1 = "^[A-Za-z0-9]+:[A-Z]{1,2}(([0-9][1-9])|([1-9]0)):[1-9]\\d*$";
			for (int i = 0; i < format.length; i++) {
				if (format[i].matches(rex1) == false) {
					MessageUtils.alert("格式错误,请检查");
					return;
				}
			}
			Long id = this.pkVal;
			try {
				serviceContext.wmsIndtlMgrService().splitLocal(id, data);
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
	public String updateLocalText;

	@Bind
	public UIWindow updateLocalWindow;

	@Action
	public void updateLocal() {
		updateLocalText = "";
		updateLocalWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "updateLocalText");
	}

	@Action
	public void updateLocalBatch() {
		if (StrUtils.isNull(updateLocalText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String data = StrUtils.toDBC(updateLocalText).replaceAll(" ", "");
			String[] format = updateLocalText.split("\\n");
			String rex1 = "^[A-Za-z0-9]+:[A-Z]{1,2}(([0-9][1-9])|([1-9]0)):[1-9]\\d*$";
			for (int i = 0; i < format.length; i++) {
				String[] splitDate=format[i].split(",");
				for (int j = 0; j < splitDate.length; j++) {
					if (splitDate[j].matches(rex1) == false) {
						MessageUtils.alert("格式错误,请检查");
						return;
					}
				}
			}
			Long id = this.mPkVal;
			try {
				serviceContext.wmsIndtlMgrService().updateLocal(id, data);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	/*@Action
	public void showReport() {
		
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/stock/wms-in.raq";
		AppUtils.openWindow("_wmsinReport", openUrl + getArgs());
	}
	
	@Action
	public void showReportincheck() {
		
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/stock/wms_incheck.raq";
		AppUtils.openWindow("_wmsinReport", openUrl + getArgs());
	}*/
	

	private String getArgs() {
		String args="";
		args+="&inid="+this.mPkVal;
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
		this.warenosChooseService.qryNos(qryWarenosKey);
		this.warenosGrid.reload();
	}
	
	@Action 
	public void shoWmsincheckAction(){
		String warenos = (String) AppUtils.getReqParam("wmsnos");
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
				List<Map> cs = warenosChooseService
						.findJobsnos(qryWarenosKey,
								"AND ( wmstype = 'I' OR  wmstype = 'G' OR  wmstype = 'C' OR wmstype = 'P')");
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
		return this.warenosChooseService
				.getJobsnosDataProvider(" AND ( wmstype = 'I' OR  wmstype = 'G' OR  wmstype = 'C' OR wmstype = 'P') ");
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
	
	@Inject(value = "l")
	private MultiLanguageBean l;
	
	@Bind(id = "warehouseinformat")
	public List<SelectItem> getWarehouseinformat() {
		try {
			if(l.m.getMlType()==MLType.en){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namee",
						"sys_report AS d", " WHERE modcode = 'warehousein' AND isdelete = FALSE",
						"order by filename");
			}else if(l.m.getMlType()==MLType.ch){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'warehousein' AND isdelete = FALSE",
						"order by filename");
			}else{
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'warehousein' AND isdelete = FALSE",
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
	public String showwarehouseinformat;
	
	@Action
	public void preview() {
		if (showwarehouseinformat == null || "".equals(showwarehouseinformat)) {
			MessageUtils.alert("请选择格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		for (String s : AppUtils.getUserRoleModuleCtrl(900000L)){
			if (s.endsWith("eportdesign")) {
				String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?design=true&raq=/stock/"
				+ showwarehouseinformat;
				AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
						+ "&userid="
						+ AppUtils.getUserSession().getUserid()
						+"&language="+AppUtils.getUserSession().getMlType().toString());
				return;
			} 
		}
		
		String openUrl = rpturl
		+ "/reportJsp/showReport.jsp?design=false&raq=/stock/"
		+ showwarehouseinformat;
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

	
	@Bind
	public UIIFrame packListIframe;

	@Bind
	public UIWindow showPackListWindow;
	/**
	 * 装箱单明细
	 * */
	@Action
	public void packList() {
		
		String[] ids = grid.getSelectedIds();

		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		String id = ids[0];
		Long cid = Long.valueOf(id);
		packListIframe.load("../stock/containerdetail.xhtml?cid=" + cid);
		showPackListWindow.show();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		try{
			if(this.selectedRowData.getSaleid() != null){
				SysUser user = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getSaleid());
				if(user!=null){
					this.salenamec = user.getNamec();
				}
			}
			if(this.selectedRowData.getCustomerid() != null){
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
				if(sysCorporation!=null){
					this.whcustomername = sysCorporation.getAbbr();
				}
			}
		}catch(NoRowException e){
			
		}
	}
	
}
