package com.scp.view.module.stock;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;

import com.scp.base.CommonComBoxBean;
import com.scp.base.MultiLanguageBean;
import com.scp.base.ConstantBean.Module;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoRowException;
import com.scp.model.data.DatLine;
import com.scp.model.data.DatPort;
import com.scp.model.data.DatWarehouse;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.model.wms.WmsIn;
import com.scp.model.wms.WmsIndtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.GoodsStockBean;

@ManagedBean(name = "pages.module.stock.wmsineditBean", scope = ManagedBeanScope.REQUEST)
public class WmsInEditBean extends EditGridFormView {
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@ManagedProperty("#{goodsstockBean}")
	private GoodsStockBean goodsService;
	
	@ManagedProperty("#{warenoschooseserviceBean}")
	private WarenosChooseService warenosChooseService;

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Accessible
	public WmsIn selectedRowData = new WmsIn();

	@SaveState
	@Accessible
	public WmsIndtl ddata = new WmsIndtl();
	
	@SaveState
	@Accessible
	public String sql="";
	
	@SaveState
	public String salenamec="";
	
	@SaveState
	public String polwarehouse="";
	
	@SaveState
	public String warehouse="";
	
	@SaveState
	public String whcustomername="";
	
	@Bind
	@SaveState
	public String soptext;
	
	@Bind
	@SaveState
	public UITextField sono;
	
	@Bind
	@SaveState
	public String initLines;
	
	@Bind
	@SaveState
	public Long mPkVal = -1L;
	
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			init();
			initCtrl();
			if(selectedRowData.getIsubmit()==null){
				
			}else{
				disableAllButton(selectedRowData.getIsubmit());
			}
		}
	}
	
	private void initCtrl() {
		sono.setReadOnly(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.wms_in.getValue())) {
			if (s.endsWith("_sono_edit")) {
				sono.setReadOnly(false);
			} 
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
	@SaveState
	public String locdesc = "";
	
	
	

	// 编辑未拆分储位
	public void getEditLocalDesc() {
		String sql = "\nSELECT " + "\ny.code || '->' || z.code AS localdesc "
				+ "\nFROM dat_warehouse_area y,dat_warehouse_loc z "
				+ "\nWHERE z.id  = " + localId + "\n\tAND y.id = z.areaid"
				+ ";";
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			locdesc = (String) m.get("localdesc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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


	@Override
	public void grid_ondblclick() {

		this.pkVal = getGridSelectId();
		// this.data = getViewControl().findById(this.pkVal);
		doServiceFindData();
		this.refreshForm();
		if (editWindow != null)
			editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Action
	public void showAttachmentIframe() {
		try {
			if(this.mPkVal==-1l){
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				attachmentIframe.load(blankUrl);
			}else{
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomerabbr((String) m.get("abbr"));
		selectedRowData.setSaleid((Long)m.get("salesid"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		this.update.markUpdate(UpdateLevel.Data, "sale");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Bind
	public UIWindow showGoodsWindow;

	@Bind
	public UIDataGrid goodsGrid;

	@Bind
	public String goodscodeQryKey;

	@Action
	public void goodscodeQry() {
		goodsService.qry(goodscodeQryKey);
		this.goodsGrid.reload();
	}

	@Bind(id = "goodsGrid", attribute = "dataProvider")
	public GridDataProvider getGoodsGridDataProvider() {
		return goodsService.getgoodsDataProvider();
	}

	@Action
	public void showGoods() {
		// this.goodscodeQryKey = "";
		// this.goodsService.qry(null);
		showGoodsWindow.show();
		goodsGrid.reload();
	}

	@Action
	public void goodsGrid_ondblclick() {
		Object[] objs = goodsGrid.getSelectedValues();
		Map m = (Map) objs[0];
		ddata.setGoodsid((Long) m.get("id"));
		ddata.setGoodscode((String) m.get("code"));
		ddata.setGoodsnamec((String) m.get("namec"));
		ddata.setGoodsnamee((String) m.get("namee"));
		ddata.setGoodscolor((String) m.get("color"));
		ddata.setGoodsl((BigDecimal) m.get("l"));
		ddata.setGoodsw((BigDecimal) m.get("w"));
		ddata.setGoodsh((BigDecimal) m.get("h"));
		ddata.setGoodsv((BigDecimal) m.get("v"));

		this.update.markUpdate(UpdateLevel.Data, "goodsid");
		this.update.markUpdate(UpdateLevel.Data, "goodscode");
		this.update.markUpdate(UpdateLevel.Data, "goodsnamec");
		this.update.markUpdate(UpdateLevel.Data, "goodsnamee");
		this.update.markUpdate(UpdateLevel.Data, "goodscolor");
		this.update.markUpdate(UpdateLevel.Data, "goodsl");

		this.update.markUpdate(UpdateLevel.Data, "goodsw");
		this.update.markUpdate(UpdateLevel.Data, "goodsh");
		this.update.markUpdate(UpdateLevel.Data, "goodsv");
		showGoodsWindow.close();

	}

	@SaveState
	@Accessible
	public Long localId;

	@Action
	public void setLocal() {
		String localInfo = AppUtils.getReqParam("localInfo"); // 从action中的id中获取参数绑定(js中绑定的)
		localId = this.serviceContext.warehouselocalMgrService
				.findlocalid(localInfo);// 获取locid
		ddata.setLocid(localId);
		this.update.markUpdate(UpdateLevel.Data, "locid");
		this.getEditLocalDesc();
		this.update.markUpdate(UpdateLevel.Data, "locdesc");

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

	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}



	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND inid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	
	public void init() {
		selectedRowData = new WmsIn();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh(); // 从表也刷新

		} else {
			addMaster();
		}
	}

	
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.wmsIndtlMgrService().wmsInDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
		showAttachmentIframe();
		try {
			String sql = "SELECT sop FROM sys_corporation WHERE id = "+this.selectedRowData.getCustomerid();
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.size()>0&&m.get("sop")!=null){
				soptext = m.get("sop").toString();
			}
			
			String lineSql ="SELECT namec AS lines FROM dat_line WHERE code = '"+selectedRowData.getLines()+"'";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(lineSql);
			if(map!=null&&map.size()>0&&map.get("lines")!=null){
				initLines = map.get("lines").toString();
			}
			update.markUpdate(true, UpdateLevel.Data, initLines);
			 Calendar calendar=Calendar.getInstance();
		     calendar.set(Calendar.HOUR_OF_DAY, 12); 
		     calendar.set(Calendar.MINUTE, 0);
		     calendar.set(Calendar.SECOND, 0);
		     Date date=calendar.getTime();
			selectedRowData.setNationwidetime(date);
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = serviceContext.wmsIndtlMgrService().wmsIndtlDao
				.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {

//		if (this.ddata.getBox() == null || this.ddata.getPiece() == null
//				|| this.ddata.getPieceinbox() == null) {
//			MessageUtils.alert("数量,箱数和每箱数量不能为空");
//			return;
//		} else if ((this.ddata.getBox().intValue())
//				* this.ddata.getPieceinbox().longValue() != this.ddata
//				.getPiece()) {
//			MessageUtils.alert("fail:数量=箱数*每箱数量");
//			return;
//		} else {
			serviceContext.wmsIndtlMgrService().saveDtlData(ddata);
			update.markUpdate(true, UpdateLevel.Data, "pkVal");
			this.alert("OK");
//		}
	}

	
	public void doServiceSaveMaster() {
			// 提交之前必须先要保存,必须子表中也有数据才能提交
			selectedRowData.setWmstype("I");
			//保存客户id的时候同时保存客户简称
			if(this.selectedRowData.getCustomerid() != null){
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
				this.selectedRowData.setCustomerabbr(sysCorporation.getAbbr());
			}
			serviceContext.wmsIndtlMgrService().saveMasterData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			
			selectedRowData = serviceContext.wmsIndtlMgrService().wmsInDao.findById(this.mPkVal);
			
			this.refreshMasterForm();
			this.alert("OK");
//		}
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
	public UIButton adddtl;
	
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton delBatch;
	
	@Bind
	public UIButton importData;
	
	@Bind
	public UIButton updateLocal;
	
	@Bind
	public UIButton splitLocal;
	
	@Bind
	public UIButton savedtl;
	
	@Bind
	public UIButton edit;
	
	@Action 
	public void adddtl(){
		this.add();
	}
	
	private void disableAllButton(Boolean isubmit) {
		saveMaster.setDisabled(isubmit);
		delMaster.setDisabled(isubmit);
		save.setDisabled(isubmit);
		add.setDisabled(isubmit);
		adddtl.setDisabled(isubmit);
		del.setDisabled(isubmit);
		delBatch.setDisabled(isubmit);
		importData.setDisabled(isubmit);
		updateLocal.setDisabled(isubmit);
		splitLocal.setDisabled(isubmit);
		savedtl.setDisabled(isubmit);
		edit.setDisabled(isubmit);
	}

	/**
	 * 提交通用处理
	 */
	@Action
	public void isubmitAjaxSubmit() {
		Boolean isSubmit = selectedRowData.getIsubmit();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		try {
			if (this.mPkVal == -1l)throw new CommonRuntimeException("Plese save first!");
			if (selectedRowData.getIscheck())throw new CommonRuntimeException("Current is check,plese uncheck first!");
			if (isSubmit) {
				sql = "UPDATE wms_in SET isubmit = TRUE ,submitime = NOW(),submiter = '"+updater+"' WHERE id ="+this.mPkVal+";";
			}else {
				sql = "UPDATE wms_in SET isubmit = FALSE ,submitime = NULL,submiter = NULL WHERE id ="+this.mPkVal+";";
			}
			serviceContext.wmsIndtlMgrService().wmsIndtlDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isSubmit);
		}catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIsubmit(!isSubmit);
			selectedRowData.setSubmiter(isSubmit?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setSubmitime(isSubmit?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isSubmit);
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsubmit(!isSubmit);
			selectedRowData.setSubmiter(isSubmit?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setSubmitime(isSubmit?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isSubmit);
		}
	}
	
	@Action
	public void isReturnAjaxSubmit(){
		Boolean isreturn = selectedRowData.getIsreturn();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		try{
			if (this.mPkVal == -1l)throw new CommonRuntimeException("Plese save first!");
			//sql = "UPDATE wms_in SET isreturn = TRUE ,updatetime = NOW(),updater = '"+updater+"' WHERE id ="+this.mPkVal+";";
			/*if (selectedRowData.getIscheck())throw new CommonRuntimeException("Current is check,plese uncheck first!");
			if (selectedRowData.getIsubmit())throw new CommonRuntimeException("Current is sumbit,plese uncheck first!");*/
			
			sql = "UPDATE wms_in SET isreturn = "+isreturn+" ,updatetime = NOW(),updater = '"+updater+"' WHERE id ="+this.mPkVal+";";
			
			serviceContext.wmsIndtlMgrService().wmsIndtlDao.executeSQL(sql);
			refreshMasterForm();
			MessageUtils.showMsg("OK!");
		}catch(Exception e){
			//MessageUtils.alert(e.getLocalizedMessage());
			MessageUtils.showException(e);
			selectedRowData.setIsreturn(!isreturn);
			e.printStackTrace();
		}
	}
	
	
	@Action
	public void addGrid() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			editGrid.appendRow();
		}
		
	}

	@Action
	public void add() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			ddata = new WmsIndtl();
			this.locdesc = null;
			ddata.setCurrency(AppUtils.getUserSession().getBaseCurrency());
			ddata.setInid(this.mPkVal);
			ddata.setIscompleted(false);
			super.qryAdd();
			update.markUpdate(true, UpdateLevel.Data, "locdesc");
		}
		
		this.ddata.setBox(1);
		this.ddata.setPiece(1);
		this.ddata.setPieceinbox(new BigDecimal(1));
		this.ddata.setPackagee("PACKAGES");
	}
	
	@Action
	public void saveMaster() {
		try {
			doServiceSaveMaster(); //Master
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	
	
	
	@Action
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new WmsIn();
		this.mPkVal = -1l;
		selectedRowData.setSingtime(new Date());
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
		String dateNationtime = formatter.format(new Date());
		String dateClosingtime = formatter.format(new Date());
		dateNationtime+=" 16:00";
		dateClosingtime+=" 12:00";
		try {
			SimpleDateFormat formatter1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm");
			Date date1 = formatter1.parse(dateNationtime);
			Date date2 = formatter1.parse(dateClosingtime);
			selectedRowData.setNationwidetime(date1);
			selectedRowData.setClosingtime(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long warehouseid = this.serviceContext.userMgrService.findWareHaousId(AppUtils.getUserSession().getUserid());
		selectedRowData.setWarehouseid(warehouseid);
		selectedRowData.setLines("");
		selectedRowData.setGdsintype("");
		selectedRowData.setPoa("");
		selectedRowData.setPolwarehouseid(warehouseid);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		//this.grid.reload();
		showArapEdit();
		
	}

	@Override
	public void del() {
		if (selectedRowData.getIsubmit()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.wmsIndtlMgrService().removeDate(ddata.getId());
			this.add();
			this.alert("OK");
			refresh();
		}
	}
	
	
	@Action
	public void delBatch() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.wmsIndtlMgrService().delBatch(ids);
			MessageUtils.alert("删除成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}

	@Action
	public void delMaster() {
		if (selectedRowData.getIsubmit()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			try {
				serviceContext.wmsInMgrService().removeDate(this.mPkVal);
				this.addMaster();
				this.alert("OK");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
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
		if (this.pkVal == -1L) {
			MessageUtils.alert("请先保存数据后再拆分");
			return;
		} else {
			splitLocalText = "";
			splitLocalWindow.show();
			this.update.markUpdate(UpdateLevel.Data, "splitLocalText");
		}
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
			String rex1 = "^[A-Za-z0-9]+:(([0-9][0-9][1-9])|([0-9][1-9]0)):(([0-9][0-9][1-9])|([0-9][1-9]0)):[1-9]\\d*$";
			for (int i = 0; i < format.length; i++) {
				String[] pieceinbox = format[i].split(":");
				int num = Integer.parseInt(pieceinbox[3])
						% this.ddata.getPieceinbox().intValue();
				if (format[i].matches(rex1) == false) {
					MessageUtils.alert("fail:格式错误,请检查");
					return;
				}
				// else if(num!=0){
				// MessageUtils.alert("fail:拆分数量%每箱件数=0");
				// return;
				// }
				else {
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
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			updateLocalText = "";
			updateLocalWindow.show();
			this.update.markUpdate(UpdateLevel.Data, "updateLocalText");
		}
	}

	@Action
	public void updateLocalBatch() {
		if (StrUtils.isNull(updateLocalText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String data = StrUtils.toDBC(updateLocalText).replaceAll(" ", "");
			String[] format = updateLocalText.split("\\n");
			String rex1 = "^[A-Za-z0-9]+:(([0-9][0-9][1-9])|([0-9][1-9]0)):(([0-9][0-9][1-9])|([0-9][1-9]0)):[1-9]\\d*$";
			for (int i = 0; i < format.length; i++) {
				String[] splitDate = format[i].split(",");
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

	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			importDataText = "";
			importDataWindow.show();
			this.update.markUpdate(UpdateLevel.Data, "importDataText");
		}
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_wmsindetail";
				String args = this.mPkVal + ",'"
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
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		if (isCheck) {
			selectedRowData.setCheckter(AppUtils.getUserSession().getUsercode());
			selectedRowData.setChecktime(Calendar.getInstance().getTime());
		}else {
			selectedRowData.setCheckter("");
			selectedRowData.setChecktime(null);
		}
		try {
			this.saveMaster();
			this.disableAllButton(isCheck);
		} catch (Exception e) {
			selectedRowData.setIscheck(false);
			selectedRowData.setCheckter("");
			selectedRowData.setChecktime(null);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			MessageUtils.showException(e);
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
				List<Map> cs = warenosChooseService
						.findJobsnos(qryWarenosKey,
								"AND (wmstype = 'I' OR  wmstype = 'G' OR  wmstype = 'C' OR wmstype = 'P')");
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
				.getJobsnosDataProvider(" AND ( wmstype = 'I' OR  wmstype = 'G' OR  wmstype = 'C' OR wmstype = 'P')");
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
			
			if(this.selectedRowData.getPolwarehouseid() != null){
				DatWarehouse datWarehouse = serviceContext.warehouseMgrService.datWarehouseDao.findById(this.selectedRowData.getPolwarehouseid());
				if(datWarehouse != null){
					this.polwarehouse = datWarehouse.getNamec();
				}
			}
			
			if(this.selectedRowData.getWarehouseid() != null){
				DatWarehouse datWarehouse = serviceContext.warehouseMgrService.datWarehouseDao.findById(this.selectedRowData.getWarehouseid());
				if(datWarehouse != null){
					this.warehouse = datWarehouse.getNamec();
				}
			}
		}catch(NoRowException e){
			
		}
	}
	
	@Bind
	public UIWindow showLocalWindow;
	@Bind
	private UIIFrame dtlIFrame;
	
	@Action
	public void showLocal() {
		String url = "./wmslocalchooser.xhtml?temp=" + Math.random();
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, showLocalWindow);
		showLocalWindow.show();
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
	
	
	@Bind
	public UIIFrame barcodeIframe;
	
	@Bind
	public UIWindow showBarcodeWindow;
	
	@Action
	public void qryBarcode() {
		Long refid = this.ddata.getId();
		barcodeIframe.load("../stock/wmsbarcode.xhtml?refid="+refid);
		showBarcodeWindow.show();
	}
	
	@Action
	public void generateBarcode() {
		try {
			Long refid = this.ddata.getId();
			Integer box = this.ddata.getBox();
			String  sql ="SELECT f_bus_barcode_gen('refid="+refid+";box="+box+"');";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 选客户提取出相应业务员
	 */
	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		Long salesid=null;
		Long deptid;
		String salesname;
		String tradeway;
		StringBuilder querySql = new StringBuilder();
		querySql.append("\n SELECT a.id AS salesid,a.namec AS salesname,deptid,corpid FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE ");
		querySql.append("\n  AND CASE WHEN EXISTS(SELECT a.id AS salesid,a.namec AS salesname FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE AND EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C' ");
		querySql.append("\n  AND EXISTS(SELECT * FROM ");
		querySql.append("\n  (SELECT xx.userid ");
		querySql.append("\n  FROM sys_custlib xx , sys_custlib_user y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND y.userid ="+AppUtils.getUserSession().getUserid()+" AND xx.libtype = 'S' AND xx.userid IS NOT NULL");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z where z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  UNION ");
		querySql.append("\n  SELECT xx.userid");
		querySql.append("\n  FROM sys_custlib xx, sys_custlib_role y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) AND xx.libtype = 'S'");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z WHERE z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  ) AS T");
		querySql.append("\n  WHERE T.userid = x.userid ");
		querySql.append("\n  ))) THEN EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C' ");
		querySql.append("\n  AND EXISTS(");
		querySql.append("\n  SELECT * FROM ");
		querySql.append("\n  (SELECT xx.userid ");
		querySql.append("\n  FROM sys_custlib xx , sys_custlib_user y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND y.userid ="+AppUtils.getUserSession().getUserid()+" AND xx.libtype = 'S' AND xx.userid IS NOT NULL");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z where z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n UNION ");
		querySql.append("\n  SELECT xx.userid");
		querySql.append("\n  FROM sys_custlib xx, sys_custlib_role y ");
		querySql.append("\n  WHERE y.custlibid = xx.id");
		querySql.append("\n  AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) AND xx.libtype = 'S'");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z WHERE z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  ) AS T");
		querySql.append("\n  WHERE T.userid = x.userid ");
		querySql.append("\n  ))");
		querySql.append("\n ELSE (EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C')");
		querySql.append("\n OR EXISTS(SELECT x.salesid FROM sys_corporation x WHERE x.id = "+customerid+" AND x.salesid = a.id))   END");
		querySql.append("\n LIMIT 1");
		String tradewaysql  = "SELECT  c.tradeway  FROM sys_corporation c WHERE c.id = "+customerid+" AND c.isdelete = false";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
			Map m_tradeway = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(tradewaysql);
			String salesidStr = StrUtils.getMapVal(m, "salesid");
			salesid = StrUtils.isNull(salesidStr)?0l:Long.valueOf(salesidStr);
			salesname = StrUtils.getMapVal(m, "salesname");
			this.selectedRowData.setSaleid(salesid);
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "saleid");
			Browser.execClientScript("initSalesByCustomer('"+salesname+"');");
		} catch (NoRowException e) {
			this.selectedRowData.setSaleid(0l);
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "saleid");
			Browser.execClientScript("initSalesByCustomer('');");
		} catch (Exception e) {
			e.printStackTrace();
		}
		update.markUpdate(true, UpdateLevel.Data, "cusales");
		this.selectedRowData.setCustomerid(Long.decode(customerid));
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
	
	private String getArgs() {
		String args = "";
		args += "&inid=" + this.mPkVal;
		return args;
	}
	
	@Bind
	public UIWindow showLogsWindow;
	
	@Action
	public void showLogs() {
		showLogsWindow.show();
	}
	
	@Action
	public void createSono() {
		this.bookgrid.reload();
		setBooknosWindow.show();
	}
	
	@Bind
	public UIDataGrid bookgrid;
	
	@Bind(id = "bookgrid", attribute = "dataProvider")
	protected GridDataProvider getBookDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".bookgrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
				.queryForList(sqlId, getQryClauseWhereBook(bookqryMap)).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".bookgrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhereBook(bookqryMap));
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
	public Map<String, Object> bookqryMap = new HashMap<String, Object>();
	
	public Map getQryClauseWhereBook(Map<String, Object> queryMap) {
		Map map = new HashMap();
		String qry = "\n EXISTS(SELECT 1 FROM dat_warehouse WHERE corpid = r.corpid AND id = "+this.selectedRowData.getWarehouseid()+")";
		map.put("qry", qry);
		return map;
	}
	
	@Bind
	public UIWindow setBooknosWindow;
	
	@SaveState
	public String codes = "wms_in";
	
	@Action
	public void chooseBooknos(){
		String[] ids = this.bookgrid.getSelectedIds();
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
		Date sodate = selectedRowData.getSodate();
		if(sodate == null){
			alert("订舱日期不能为空");
			setBooknosWindow.close();
			return;
		}
		if(!StrUtils.isNull(selectedRowData.getSono())){
			alert("订舱号已生成");
			setBooknosWindow.close();
			return;
		}
		try {
			SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd");
			String soDateStr = dayformat.format(sodate);
			this.selectedRowData.getWarehouseid();
			
			String querySqlw = "SELECT f_auto_busno('code="+codes+";date="+soDateStr+";corpid='||(SELECT corpid FROM dat_warehouse WHERE id = "+this.selectedRowData.getWarehouseid()+")||'') AS so;";
			//System.out.println("querySqlw:"+querySqlw);
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySqlw);
			String so = StrUtils.getMapVal(map, "so");
			Browser.execClientScript("sonoJsVar.setValue('"+so+"')");
			setBooknosWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void bookgrid_ondblclick() {
		chooseBooknos();
	}
	
	
	@Action
	public void save(){
		try {
			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
			this.add();
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
	}
	
	
	@Action
	public void edit() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		try {
			this.pkVal = getGridSelectId();
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
	
	
	
	@Action
	public void savedtl() {
		try {
			editGrid.commit();
			boolean isChangeFlag = false;
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
			if (isChangeFlag) {
				editGrid.reload();
			}
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.wmsIndtlMgrService().updateBatchEditGrid(modifiedData);
	}

	@Override
	protected void add(Object addedData) {
		serviceContext.wmsIndtlMgrService().addBatchEditGrid(addedData,this.mPkVal);
	}
	
	
	@Bind
	public UIWindow showPortWindow;
	
	@Bind
	public UIIFrame portIFrame;
	
	@Action
	public void showportAction() {
		String type = (String) AppUtils.getReqParam("type");
		portIFrame.setSrc("./popqrygrid.xhtml?type="+type.toString());
		update.markAttributeUpdate(portIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, portIFrame);
		
		showPortWindow.show();
	}
	
	@Action
	public void setportAction() {
		String type = (String) AppUtils.getReqParam("type");
		String id = (String) AppUtils.getReqParam("id");
		
		try {
			if("wmspol".equals(type)){
				DatWarehouse data = serviceContext.warehouseareaMgrService.warehouseDao.findById(Long.valueOf(id));
				Browser.execClientScript("polwarehouseJsvar.setValue("+data.getId()+")");
				Browser.execClientScript("$('#polwarehouse_input').val('"+data.getNamec()+"')");
			}
			
			if("wmspoa".equals(type)){
				DatWarehouse data = serviceContext.warehouseareaMgrService.warehouseDao.findById(Long.valueOf(id));
				Browser.execClientScript("warehouseJsvar.setValue("+data.getId()+")");
				Browser.execClientScript("$('#warehouse_input').val('"+data.getNamec()+"')");
			}
			
			if("lines".equals(type)){
				DatLine datLine = serviceContext.lineMgrService.datLineDao.findById(Long.valueOf(id));
				Browser.execClientScript("linesJsvar.setValue('"+datLine.getNamec()+"')");
				Browser.execClientScript("$('#lines_input').val('"+datLine.getNamec()+"')");
			}
			
			DatPort datPort = serviceContext.portyMgrService.datPortDao.findById(Long.valueOf(id));
			if("pol".equals(type)){
				Browser.execClientScript("polJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#pol_input').val('"+datPort.getNamee()+"')");
			}
			if("pod".equals(type)){
				Browser.execClientScript("podJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#pod_input').val('"+datPort.getNamee()+"')");
			}
			if("pdd".equals(type)){
				Browser.execClientScript("pddJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#pdd_input').val('"+datPort.getNamee()+"')");
			}
			if("poa".equals(type)){
				Browser.execClientScript("poaJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#poa_input').val('"+datPort.getNamee()+"')");
			}
			if("destination".equals(type)){
				Browser.execClientScript("destinationJsvar.setValue('"+datPort.getNamee()+"')");
				Browser.execClientScript("$('#destination_input').val('"+datPort.getNamee()+"')");
			}
			
			if("customer".equals(type)){
				SysCorporation data = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
				Browser.execClientScript("customeridJsVar.setValue('"+data.getId()+"')");
				Browser.execClientScript("$('#whcustomer_input').val('"+data.getAbbr()+"')");
			}
			
			if("factory".equals(type)){
				SysCorporation data = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
				Browser.execClientScript("factoryTextAreaJsVar.setValue('"+data.getNamec()+"')");
			}
			
			if("sales".equals(type)){
				SysUser data = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				Browser.execClientScript("saleidJsVar.setValue('"+data.getId()+"')");
				Browser.execClientScript("$('#sales_input').val('"+data.getNamec()+"')");
			}
			
			if("agentcnt".equals(type)){
				SysCorporation data = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
				Browser.execClientScript("agentcntTextAreaJsVar.setValue('"+data.getNamec()+"')");
			}
			if("agentname".equals(type)){
				SysCorporation data = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
				Browser.execClientScript("agentnameTextFiledJsVar.setValue('"+data.getNamec()+"')");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		showPortWindow.close();
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
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		String id = ids[0];
		Long cid = Long.valueOf(id);
		packListIframe.load("../stock/containerdetail.xhtml?cid=" + cid);
		showPackListWindow.show();
	}
	
}
