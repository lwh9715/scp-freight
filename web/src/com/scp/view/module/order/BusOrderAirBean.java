package com.scp.view.module.order;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UITextArea;
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.UICell;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.data.DatFeeitem;
import com.scp.model.order.BusOrder;
import com.scp.model.order.BusOrderdtl;
import com.scp.model.price.PriceAir;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.model.ship.BusShipBooking;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.vo.price.FeeAdd;

@ManagedBean(name = "pages.module.order.busorderairBean", scope = ManagedBeanScope.REQUEST)
public class BusOrderAirBean extends GridFormView {

	@Resource(name="hibTtrTemplate")   
    private TransactionTemplate transactionTemplate;
	
	@SaveState
	@Accessible
	public BusOrder selectedRowData = new BusOrder();
	
	@SaveState
	@Accessible
	public BusShipBooking selectedRowDataBooking = new BusShipBooking();

	public Long userid;
	
	@Bind
	public UIWindow cancelBookingRemarksWindow;

	@Bind
	public UIButton createBooking;

	@Bind
	public UIButton cancelBooking;

	@Bind
	public UIButton sendMessageToCustomerService;

	@Bind
	public UIButton add;

	@Bind
	public UIButton del;

	@Bind
	public String type;

	@Bind
	public UIButton createJobs;
	
	@Bind
	public UIWindow showChildWindow;

	@Bind
	public UIIFrame constructIframe;

	@Bind
	public UITextArea cancelBookingRemarks;
	
	@Bind
	public UICell bookingCellPanel;
	
	@Bind
	public UITextField sono;
	
	@Bind
	@SaveState
	public String priceid = "-1";

	@Bind
	@SaveState
	public String jsonData = "''";//订单费用明细json
	
	@Bind
	@SaveState
	public String templateComBo;

	@Bind
	@SaveState
	public String jsonDataStr = "''";

	@Bind
	public UIButton lookAtTheBooking;

	@Bind
	public UIButton saveAjaxSubmit;

	@Bind
	public UIWindow showSFTWindow;
	
	@Bind
	public UIWindow showCustomerWindow;
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@Bind
	private String qryCustomerKey;
	@Bind
	public UIDataGrid customerGrid;
	
	@SaveState
	@Accessible
	public String sql = "AND 1=1";
	
	@SaveState
	@Accessible
	public String custype;
	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	@Bind
	public UIIFrame assignIframe;
	
	@Bind
	@SaveState
	public String assigns;
	
	@Bind
	@SaveState
	public String ordernos ;
	
	@Bind
	private String qryJobsnosKey;
	
	@Bind
	public UIWindow showJobsnosWindow;
	
	@Bind
	public UIDataGrid ordernosGrid;
	
	@Bind(id = "ordernosGrid", attribute = "dataProvider")
	public GridDataProvider getJobnosGridDataProvider() {
		return this.serviceContext.busOrderMgrService
				.getJobsnosDataProvider("AND (1=1)");
	}
	
	@Action
	public void ordersQry() {
		this.serviceContext.busOrderMgrService.qryOrders(qryJobsnosKey);
		this.ordernosGrid.rebind();
	}
	
	@Action
	public void ordernosGrid_ondblclick() {
		Object[] objs = ordernosGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.pkVal = ((Long) m.get("id"));
		this.update.markUpdate(UpdateLevel.Data, "ordernos");
		this.update.markUpdate(UpdateLevel.Data, "pkVal");
		this.showJobsnosWindow.close();
		this.refresh();
		bustypeSendRedirect();
	}
	
	@Action
	public void showorders() {
		qryJobsnosKey = ordernos;
		int index = qryJobsnosKey.indexOf("/");
		if (index > 1)
			qryJobsnosKey = qryJobsnosKey.substring(0, index);
			try {
				List<Map> cs = this.serviceContext.busOrderMgrService.findorders(qryJobsnosKey);
				if (cs.size() == 1) {
					this.pkVal = ((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "ordernos");
					this.update.markUpdate(UpdateLevel.Data, "pkVal");
					this.showJobsnosWindow.close();
					this.refresh();
					bustypeSendRedirect();
				} else {
					this.update.markUpdate(UpdateLevel.Data, "qryJobsnosKey");
					ordersQry();
					showJobsnosWindow.show();
				}

			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	
	@Action
	public void showorderAction(){
		ordernos = (String) AppUtils.getReqParam("orders");
		showorders();	
	}
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.userid = AppUtils.getUserSession().getUserid();
			initCtrl();
			String type = AppUtils.getReqParam("type");
			if (StrUtils.isNull(type)) {
				this.add();
			} else if ("fromPrice".equals(type)) { // 报价生成
				String priceidStr = AppUtils.getReqParam("priceid");
				if (!StrUtils.isNull(priceidStr)) {// 报价中生成订单,初始化报价相关数据
					this.priceid = priceidStr;
					initDataPrice();//
				}
			} else if ("edit".equals(type)) { // 编辑
				String id = AppUtils.getReqParam("id");
				if (!StrUtils.isNull(id)) {
					pkVal = Long.valueOf(id);
					if(this.pkVal < 1){
						add();
					}
					this.initBusShipBooking(pkVal);
					this.refresh();
				} else {
					this.add();
				}
			} else {
				this.add();
			}
		}
	}
	
	private void initCtrl() {
		add.setDisabled(true);
		del.setDisabled(true);
		createJobs.setDisabled(true);
		createBooking.setDisabled(false);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_order.getValue())) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
			} else if (s.endsWith("_update")) {
			} else if (s.endsWith("_createjob")) {
				createJobs.setDisabled(false);
			} else if (s.endsWith("_booking")) {
				createBooking.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				del.setDisabled(false);
			}
		}
	}
	
	private void initBusShipBooking(Long orderId) {
		BusShipBooking busShipBooking = serviceContext.busBookingMgrService
				.findBookingByOrderId(orderId);
		MLType mlType = AppUtils.getUserSession().getMlType();
		if (null == busShipBooking) {
			this.cancelBookingRemarks.setHidden(true);
			this.createBooking.setDisabled(false);
			this.cancelBooking.setDisabled(true);
			if("ch".equals(mlType.toString())){
				this.selectedRowDataBooking.setBookstate("未订舱");
			}else{
				this.selectedRowDataBooking.setBookstate("Unbooked");
			}
			
			this.selectedRowDataBooking.setSono("");
			this.lookAtTheBooking.setDisabled(true);
			initCtrl();
		} else {
			if ("C".equals(busShipBooking.getBookstate())) {
				this.cancelBooking.setDisabled(true);
				this.cancelBookingRemarks.setHidden(false);
			} else {
				this.cancelBooking.setDisabled(false);
				this.cancelBookingRemarks.setHidden(true);
			}
			this.lookAtTheBooking.setDisabled(false);
			this.createBooking.setDisabled(false);
			this.selectedRowDataBooking = busShipBooking;
		}
	}

	private void initDataPrice() {
		this.pkVal = -1L;
		selectedRowData = new BusOrder();
		this.selectedRowData.setOrderdate(Calendar.getInstance().getTime());
		this.selectedRowData.setDeliverdate(Calendar.getInstance().getTime());
		this.selectedRowData.setBustype("AIR");
		PriceAir priceAir = serviceContext.priceAirService.priceAirDao
				.findById(Long.valueOf(priceid));
		this.selectedRowData.setCscode("");
		this.selectedRowData.setSales("");
		this.selectedRowData.setCustomercode("");
//		this.selectedRowData.setCls(priceFcl.getCls());
		// this.selectedRowData.setEta(priceFcl.getEta());
//		this.selectedRowData.setEtd(priceFcl.getEtd());

		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		this.selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());

		this.selectedRowData.setPol(priceAir.getPol());
		this.selectedRowData.setPod(priceAir.getPod());
		this.selectedRowData.setPdd(priceAir.getPod());
		//this.selectedRowData.setShiping(priceFcl.getShipping());

//		try {
//			this.selectedRowData
//					.setShipid(serviceContext.customerMgrService.sysCorporationDao
//							.findOneRowByClauseWhere(
//									" code = '" + priceFcl.getShipping() + "'")
//							.getId());
//		} catch (NoRowException e) {
//			e.printStackTrace();
//		}

		this.selectedRowData.setSalesid(AppUtils.getUserSession().getUserid());
		
		
		if(priceAir.getRy() != null && priceAir.getRy().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '燃油附加费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//燃油附加费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','燃油附加费','PP','CNY','票',"+priceAir.getRy()+","+priceAir.getRy()+","+priceAir.getRy()+","+priceAir.getRy()+");");
			}
		}
		if(priceAir.getZx() != null && priceAir.getZx().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '战争险'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//战争险
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','战争险','PP','CNY','票',"+priceAir.getZx()+","+priceAir.getZx()+","+priceAir.getZx()+","+priceAir.getZx()+");");
			}
		}
		if(priceAir.getGzf() != null && priceAir.getGzf().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '过站费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//过站费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','过站费','PP','CNY','票',"+priceAir.getGzf()+","+priceAir.getGzf()+","+priceAir.getGzf()+","+priceAir.getGzf()+");");
			}
		}
		if(priceAir.getBt() != null && priceAir.getBt().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '补贴费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//补贴费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','补贴费','PP','CNY','票',"+priceAir.getBt()+","+priceAir.getBt()+","+priceAir.getBt()+","+priceAir.getBt()+");");
			}
		}
		if(priceAir.getTruck() != null && priceAir.getTruck().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '卡车费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//卡车费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','卡车费','PP','CNY','票',"+priceAir.getTruck()+","+priceAir.getTruck()+","+priceAir.getTruck()+","+priceAir.getTruck()+");");
			}
		}
		if(priceAir.getBgf() != null && priceAir.getBgf().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '报关费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//报关费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','报关费','PP','CNY','票',"+priceAir.getBgf()+","+priceAir.getBgf()+","+priceAir.getBgf()+","+priceAir.getBgf()+");");
			}
		}
		if(priceAir.getRcf() != null && priceAir.getRcf().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '入仓费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//入仓费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','入仓费','PP','CNY','票',"+priceAir.getRcf()+","+priceAir.getRcf()+","+priceAir.getRcf()+","+priceAir.getRcf()+");");
			}
		}
		if(priceAir.getAmsens() != null && priceAir.getAmsens().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = 'AMS/ENS'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//AMS/ENS
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','AMS/ENS','PP','CNY','票',"+priceAir.getAmsens()+","+priceAir.getAmsens()+","+priceAir.getAmsens()+","+priceAir.getAmsens()+");");
			}
		}
		if(priceAir.getByf() != null && priceAir.getByf().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '搬运费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//搬运费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','搬运费','PP','CNY','票',"+priceAir.getByf()+","+priceAir.getByf()+","+priceAir.getByf()+","+priceAir.getByf()+");");
			}
		}
		if(priceAir.getOther() != null && priceAir.getOther().doubleValue() > 0){
			List<DatFeeitem> datFeeitems = serviceContext.feeItemMgrService.datFeeitemDao.findAllByClauseWhere("name = '其它费'");
			if(datFeeitems != null && datFeeitems.size() > 0){
				//其它费
				Browser.execClientScript("addCostItem(-100,-100,"+datFeeitems.get(0).getId()+",'"+datFeeitems.get(0).getCode()+"','其它费','PP','CNY','票',"+priceAir.getOther()+","+priceAir.getOther()+","+priceAir.getOther()+","+priceAir.getOther()+");");
			}
		}
		
		
		//initDatas();
	}
	
	private void initDatas(){
			this.selectedRowData = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);
			if(this.selectedRowData !=null && "AIR".equals(this.selectedRowData.getBustype())){
				this.jsonData = getJsonDatas();
			}else{
				this.jsonData = "''";
			}
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	
	
	@Override
	public void add() {
		this.pkVal = -1L;
		selectedRowData = new BusOrder();
		this.selectedRowData.setOrderdate(Calendar.getInstance().getTime());
		this.selectedRowData.setDeliverdate(Calendar.getInstance().getTime());
		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		this.selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());
		this.selectedRowData.setCscode("");
		this.selectedRowData.setSales("");
		this.selectedRowData.setBustype("AIR");
		this.selectedRowData.setCarryitem("CY-CY");
		this.selectedRowData.setFreightitem("PP");
//		this.selectedRowData.setEtd(Calendar.getInstance().getTime());
		this.assigns = "";
		update.markUpdate(UpdateLevel.Data, "type");
		checkBoookingButtonState();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		Browser.execClientScript("addInit();");
	}
	
	private void checkBoookingButtonState() {
		MLType mlType = AppUtils.getUserSession().getMlType();
		if (-1 == this.pkVal) {
			this.createBooking.setDisabled(false);
			this.cancelBooking.setDisabled(true);
			this.cancelBookingRemarks.setHidden(true);
			if("ch".equals(mlType.toString())){
				this.selectedRowDataBooking.setBookstate("未订舱");
			}else{
				this.selectedRowDataBooking.setBookstate("Unbooked");
			}
			this.selectedRowDataBooking.setSono("");
		} else {
			this.initBusShipBooking(this.pkVal);
		}
	}
	
	@Override
	public void del() {
		try {
			List<Long> list = new ArrayList<Long>();
			list.add(selectedRowData.getId());
			serviceContext.busOrderMgrService.deleteOrderById(list);
			MessageUtils.alert("OK!");
			this.add();
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public void refresh() {
		selectedRowData = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);	
		if(this.selectedRowData !=null && "AIR".equals(this.selectedRowData.getBustype())){
			this.jsonData = getJsonDatas();
		}else{
			this.jsonData = "''";
		}
		if(selectedRowData!=null&&selectedRowData.getCorpidop()!=null && selectedRowData.getCorpidop() > 0) {
			this.sono.disable();
		}else {
			this.sono.enable();
		}
		if (selectedRowData!=null&&selectedRowData.getBustype()!="AIR") {
			selectedRowData.setBustype("AIR");
		}
		if (selectedRowData!=null){
			this.assigns = this.serviceContext.busOrderMgrService.getAssigns(this.selectedRowData.getId());//neo ################
		}
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		//Browser.execClientScript("clearRows();");
		//Browser.execClientScript("init();");前台js Ext.onReady
		
	}
	@Action
	public void refreshsub(){
		refresh();
	}
	
	@Override
	@Transactional
	public void save() {
		//Object id = transactionTemplate.execute(new TransactionCallback() {
	      //  @Override  
	      //  public String doInTransaction(TransactionStatus status) { 
	        //	if(selectedRowData.getBustype().equals("AIR")){
	    			
	    	//	}
	    		try{
	    			serviceContext.busOrderMgrService.busOrderDao.createOrModify(selectedRowData);
	    			pkVal = selectedRowData.getId();
	    			update.markUpdate(true, UpdateLevel.Data, "pkVal");
	    			selectedRowData = serviceContext.busOrderMgrService.busOrderDao
	    					.findById(pkVal);
	    			update.markUpdate(true, UpdateLevel.Data, "orderno");
	    			checkBoookingButtonState();
	    			changeCorpidOps();
	    		} catch (Exception e) {
	    			MessageUtils.showException(e);
	    		}
	        	//return "";
	       // }
		//});
	}
	
	@Action
	private void createJobs() {
		if (this.pkVal < 0) {
			MessageUtils.alert("请先保存数据！");
			return;
		}
		BusOrder busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(this.pkVal);
		//避免修改后跳转到工作单页面报错
		if(busOrder!=null&&!busOrder.getBustype().equals(this.selectedRowData.getBustype())){
			MessageUtils.alert("修改业务类型后请先保存数据！");
			return;
		}
		try {
			String jobno = this.serviceContext.busOrderMgrService.createJobs(
					this.pkVal, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("工作单已生成，稍后会直接打开该工作单(工单号确定后，请在单号旁边按钮，取一个正式的号码)");
			this.refresh();
			String url = AppUtils.chaosUrlArs("../air/jobsedit.xhtml") + "&id="
			+ this.selectedRowData.getJobid();
			AppUtils.openWindow("_showJobno_" + this.selectedRowData.getJobno(),
			url);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			
		}
	}

	@Action
	private void showJobs() {
		if (this.pkVal <= 0) {
			return;
		}
		if (this.selectedRowData.getJobid() == null || this.selectedRowData.getJobid() <= 0) {
			MessageUtils.alert("请先生成工作单！");
			return;
		}
		String url = AppUtils.chaosUrlArs("../air/jobsedit.xhtml") + "&id="
				+ this.selectedRowData.getJobid();
		AppUtils.openWindow("_showJobno_" + this.selectedRowData.getJobno(),
				url);
	}

	@Action 
	private void createBooking() {
		if (this.pkVal < 0) {
			return;
		}
		String url = AppUtils.chaosUrlArs("../salesmgr/booking.xhtml")
				+ "&type=O&orderid=" + this.pkVal + "&r="
				+ AppUtils.base64Encoder("edit");
		AppUtils.openWindow(
				"_showBooking_" + this.selectedRowData.getOrderno(), url);
	}

	@Action
	private void lookAtTheBooking() {
		showChildWindow.show();
		this.createBooking.setDisabled(true); 
	}

	@SaveState
	private boolean showConstruct;

	@Action
	public void showConstruct() {
		constructIframe.load("../salesmgr/booking.xhtml?type=O&orderid="
				+ this.pkVal);

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
	private void cancelBooking() {
		cancelBookingRemarksWindow.show();
	}

	@Action
	private void ajaxRemarksSubmit() {
		String remarks = AppUtils.getReqParam("remarks");
		if (remarks.isEmpty()) {
			MessageUtils.alert("取消订舱的备注不能为空,请填写取消订舱的备注!");
		} else {
			this.selectedRowDataBooking.setRemarks_cancel(remarks);
			this.selectedRowDataBooking.setBookstate("C");
			this.cancelBooking.setDisabled(true);
			this.cancelBookingRemarks.setHidden(false);
			update.markUpdate(true, UpdateLevel.Data, "bookingStatePanel");
			serviceContext.busBookingMgrService.busShipBookingDao
					.modify(selectedRowDataBooking);
			MessageUtils.alert("OK!");
			messageServiceOfCancelBooking();
			cancelBookingRemarksWindow.close();
		}
	}

	private void messageServiceOfCancelBooking() {
		if(selectedRowDataBooking==null|| selectedRowDataBooking.getUserbook() == null){
			return;
		}
		if (selectedRowDataBooking.getUserbook().isEmpty()
				|| this.selectedRowData.getSalesid() == null) {
			MessageUtils.alert("订舱负责人或业务员为空,请尝试保存相关信息后刷新页面再次发送!");
			return;
		}
		Long salesid = this.selectedRowData.getSalesid();
		List<String> receiveCodes = new ArrayList<String>();
		receiveCodes.add(selectedRowDataBooking.getUserbook());
		String remindTitle = "订舱消息提醒";
		String remindContext = AppUtils.getUserSession().getUsername()
				+ "发来的订舱取消提醒";
		String url = AppUtils.getBasePath()
				+ "pages/module/order/busorder.aspx?type=edit&id="
				+ selectedRowData.getId();
		SysUser su = serviceContext.userMgrService.sysUserDao.findById(salesid);
		String sendContext = "业务员" + su.getNamee() + "(" + su.getNamee() + ")"
				+ "的订单[url=" + url + "]" + selectedRowData.getOrderno()
				+ "[/url],SO:" + selectedRowDataBooking.getSono() + "取消订舱,原因:"
				+ selectedRowDataBooking.getRemarks_cancel();
		AppUtils.sendMessage(receiveCodes, remindTitle, remindContext, url,
				sendContext);
		MessageUtils.alert("消息已推送!");

	}

	@Action
	private void sendMessageToCustomerService() {
		if (selectedRowData.getCscode().isEmpty()
				|| this.selectedRowData.getSalesid() == null) {
			MessageUtils.alert("业务员或客服为空,请尝试保存相关信息后刷新页面再次发送!");
			return;
		}
		Long salesid = this.selectedRowData.getSalesid();
		List<String> receiveCodes = new ArrayList<String>();
		receiveCodes.add(selectedRowData.getCscode());
		String remindTitle = "订舱消息提醒";
		String remindContext = AppUtils.getUserSession().getUsername()
				+ "发来的订舱跟进提醒";
		String url = AppUtils.getBasePath() 
					+ "pages/module/order/busorderair.aspx?type=edit&id=" 
					+ selectedRowData.getId();
		SysUser su = serviceContext.userMgrService.sysUserDao.findById(salesid);
		String sendContext = "业务员" + su.getNamee() + "(" + su.getNamee() + ")"
				+ "的订单[url=" + url + "]" + selectedRowData.getOrderno()
				+ "[/url]已生成,请跟进!";
		AppUtils.sendMessage(receiveCodes, remindTitle, remindContext, url,
				sendContext);
		MessageUtils.alert("消息已推送!");

	}

	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		Long salesid;
		String salesname;
		String tradeway;
		StringBuilder querySql = new StringBuilder();
		querySql.append("\n SELECT salesid,salesname,COALESCE(tradeway,'F') AS tradeway,COALESCE(impexp,'E') AS impexp FROM ");
		querySql.append("\n (SELECT u.id AS salesid,u.namec AS salesname,(CASE WHEN c.tradeway='' OR c.tradeway IS NULL THEN 'F' ELSE tradeway END) AS tradeway,(CASE WHEN c.impexp='' OR c.impexp IS NULL THEN 'E' ELSE impexp END) AS impexp ");
		querySql.append("\n FROM sys_corporation c , sys_user u  ");
		querySql.append("\n WHERE  ");
		querySql.append("\n 	c.id ="+customerid);
		querySql.append("\n AND u.id = c.salesid  ");
		querySql.append("\n AND u.isdelete = false ");
		querySql.append("\n UNION ");
		querySql.append("\n SELECT userid AS salesid,(SELECT namec FROM sys_user WHERE id = a.userid) AS salesname,NULL,NULL");
		querySql.append("\n FROM sys_user_assign a ");
		querySql.append("\n WHERE linkid = "+customerid);
		querySql.append("\n AND roletype = 'S' ");
		querySql.append("\n AND linktype = 'C' ");
		querySql.append("\n ) AS T ");
		querySql.append("\n ORDER BY salesid = "+AppUtils.getUserSession().getUserid()+" DESC ");
		querySql.append("\n LIMIT 1 ");
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
			String salesidStr = StrUtils.getMapVal(m, "salesid");
			salesid = StrUtils.isNull(salesidStr)?0l:Long.valueOf(salesidStr);
			salesname = StrUtils.getMapVal(m, "salesname");
			this.selectedRowData.setSalesid(salesid);
			this.selectedRowData.setSales(salesname);
			tradeway = StrUtils.getMapVal(m, "tradeway");
			String impexp = StrUtils.getMapVal(m, "impexp");
			if(!StrUtils.isNull(tradeway)){
				this.selectedRowData.setTradeway(tradeway);
			}else{
				this.selectedRowData.setTradeway("F");
			}
			if(!StrUtils.isNull(impexp)){
				this.selectedRowData.setImpexp(impexp);
			}else{
				this.selectedRowData.setImpexp("E");
			}
			update.markUpdate(true, UpdateLevel.Data, "tradeway");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			update.markUpdate(true, UpdateLevel.Data, "impexp");
			Browser.execClientScript("initSalesByCustomer('"+salesname+"','"+salesid+"');");
			
		} catch (NoRowException e) {
			this.selectedRowData.setSalesid(0l);
			this.selectedRowData.setSales("");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("initSalesByCustomer('','');");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void changeCorpidOp() {
		String corpidop = AppUtils.getReqParam("corpidop");
		if(Long.parseLong(corpidop)>0){
			this.sono.disable();
			this.bookingCellPanel.setStyle("visibility: block;");
		}else {//其它操作
			this.selectedRowDataBooking.setBookstate("同行");
			this.sono.enable();
			this.bookingCellPanel.setStyle("visibility: hidden;");
		}
	}
	
	/**
	 * 初始化订单费用明细json
	 * @return
	 */
	public String getJsonDatas() {
		try {
			String ret = "''";
			if ("-1".equals(priceid)) {
				ret = serviceContext.busOrderMgrService.getOrderdtlLclById(pkVal);
			} else {
				ret = serviceContext.busOrderMgrService.getPriceFeeById(Long
						.valueOf(priceid));
			}
			return StrUtils.isNull(ret) ? "''" : ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "''";
		}
	}
	/**
	 * 运费模版选择事件
	 */
	@Action(id = "templateComBo", event = "onselect")
	public void doselect() {
		this.jsonData = "";
		update.markUpdate(true, UpdateLevel.Data, "jsonData");

		if (StrUtils.isNull(templateComBo))
			return;
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
		.findAllByClauseWhere("templatename='"
				+ templateComBo
				+ "' AND isdelete = false AND istemplate = TRUE ORDER BY id");

		List<FeeAdd> voList = new ArrayList<FeeAdd>();
		for (PriceFclFeeadd priceFclFeeadd : list) {
			FeeAdd fclFeeAdd = new FeeAdd();
			fclFeeAdd.setId(-100L);
			fclFeeAdd.setOrderid(-100L);
			fclFeeAdd.setAmt(priceFclFeeadd.getAmt());
			fclFeeAdd.setAmt20(priceFclFeeadd.getAmt20());
			fclFeeAdd.setAmt40gp(priceFclFeeadd.getAmt40gp());
			fclFeeAdd.setAmt40hq(priceFclFeeadd.getAmt40hq());
			fclFeeAdd.setCurrency(priceFclFeeadd.getCurrency());
			fclFeeAdd.setFeeitemid(priceFclFeeadd.getFeeitemid());
			fclFeeAdd.setFeeitemcode(priceFclFeeadd.getFeeitemcode());
			fclFeeAdd.setFeeitemname(priceFclFeeadd.getFeeitemname());
			fclFeeAdd.setPpcc(priceFclFeeadd.getPpcc());
			fclFeeAdd.setUnit(priceFclFeeadd.getUnit());
			fclFeeAdd.setAmt(priceFclFeeadd.getAmt());
			fclFeeAdd.setAmtar(priceFclFeeadd.getAmt());
			fclFeeAdd.setAmt20(priceFclFeeadd.getAmt20());
			fclFeeAdd.setAmt20ar(priceFclFeeadd.getAmt20());
			fclFeeAdd.setAmt40gp(priceFclFeeadd.getAmt40gp());
			fclFeeAdd.setAmt40gpar(priceFclFeeadd.getAmt40gp());
			fclFeeAdd.setAmt40hq(priceFclFeeadd.getAmt40hq());
			fclFeeAdd.setAmt40hqar(priceFclFeeadd.getAmt40hq());
			fclFeeAdd.setAmtother(new BigDecimal(0));
			fclFeeAdd.setAmtother_ar(new BigDecimal(0));
			fclFeeAdd.setCurrency("CNY");
			Short tmps = 0;
			fclFeeAdd.setPiece(tmps);
			fclFeeAdd.setPiece20(tmps);
			fclFeeAdd.setPiece40gp(tmps);
			fclFeeAdd.setPiece40hq(tmps);
			fclFeeAdd.setPieceother(tmps);
			voList.add(fclFeeAdd);
		}
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		for (FeeAdd vo : voList) {
			String str = gson.toJson(vo);
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(str);
			jsonArray.add(el);
		}

		this.jsonData = jsonArray.toString();

		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		Browser.execClientScript("init();");
	}
	
	/**
	 * 运费模版数据源
	 * @return
	 */
	@Bind(id = "template")
	public List<SelectItem> getTemplate() {
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT templatename",
					"templatename", "price_fcl_feeadd AS d",
					"WHERE istemplate=true", "ORDER BY templatename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Action
	public void changeCorpidOps(){
		if(selectedRowData==null||selectedRowData.getBustype()==null){
			MessageUtils.alert("保存失败!");
			return;
		}
		//删除除air以外订单明细
		if("AIR".equals(selectedRowData.getBustype())){
			this.serviceContext.busOrderMgrService.updateBusType(selectedRowData.getId(), "AIR");
		}
		List<Long> idsOldArray = new ArrayList<Long>();
		idsOldArray.clear();
		List<Long> idsNewArray = new ArrayList<Long>();
		idsNewArray.clear();
		
		// 保存明细数据
		if (!StrUtils.isNull(jsonDataStr) && !"null".equals(jsonDataStr)&& !"''".equals(jsonDataStr)) {

			Type listType = new TypeToken<ArrayList<com.scp.vo.order.BusOrderdtl>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();

			ArrayList<com.scp.vo.order.BusOrderdtl> list = gson.fromJson(
					jsonDataStr, listType);// 使用该class属性，获取的对象均是list类型的

			ArrayList<com.scp.vo.order.BusOrderdtl> listOld = gson.fromJson(
					serviceContext.busOrderMgrService.getOrderdtlById(pkVal),
					listType);

			List<BusOrderdtl> busOrderDtlList = new ArrayList<BusOrderdtl>();

			for (com.scp.vo.order.BusOrderdtl li : list) {
				if (li.getFeeitemid() < 1) {
					continue;
				}
				if (li.getAmt20() == null && li.getAmt() == null
						&& li.getAmt20ar() == null && li.getAmtar() == null) {
					continue;
				}
				BusOrderdtl busorderdtl = new BusOrderdtl();
				if (li.getId() > 0) {
					busorderdtl = serviceContext.busOrderMgrService.busOrderdtlDao
							.findById(li.getId());
					idsNewArray.add(li.getId());
				}
				busorderdtl.setOrderid(pkVal);
				busorderdtl.setFeeitemid(li.getFeeitemid());
				busorderdtl.setFeeitemcode(li.getFeeitemcode());
				busorderdtl.setFeeitemname(li.getFeeitemname());
				busorderdtl.setPpcc(li.getPpcc());
				busorderdtl.setCurrency(li.getCurrency());
				busorderdtl.setUnit(li.getUnit());
				busorderdtl.setAmt20(li.getAmt20());
				busorderdtl.setAmt40gp(li.getAmt40gp());
				busorderdtl.setAmt40hq(li.getAmt40hq());
				busorderdtl.setAmtother(li.getAmtother());
				busorderdtl.setAmt(li.getAmt());
				busorderdtl.setAmt20ar(li.getAmt20ar());
				busorderdtl.setAmt40gpar(li.getAmt40gpar());
				busorderdtl.setAmt40hqar(li.getAmt40hqar());
				busorderdtl.setAmtother_ar(li.getAmtother_ar());
				busorderdtl.setAmtar(li.getAmtar());
				busorderdtl.setPiece20(li.getPiece20());
				busorderdtl.setPiece40gp(li.getPiece40gp());
				busorderdtl.setPiece40hq(li.getPiece40hq());
				busorderdtl.setPieceother(li.getPieceother());
				busorderdtl.setPiece(li.getPiece());
				busorderdtl.setRemarks(li.getRemarks());
				busorderdtl.setCntypeothercode(li.getCntypeothercode());
				busorderdtl.setCbm(li.getCbm());
				busOrderDtlList.add(busorderdtl);
			}
			serviceContext.busOrderMgrService.saveOrModify(busOrderDtlList);

			if (listOld != null && listOld.size() > 0) {
				for (com.scp.vo.order.BusOrderdtl li : listOld) {
					idsOldArray.add(li.getId());
				}
				List<Long> lists = getDiffrent(idsOldArray, idsNewArray);

				if (!lists.isEmpty()) {
					serviceContext.busOrderMgrService.removes(lists);
				}
			}
			this.jsonData = getJsonDatas();
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
			MessageUtils.alert("OK!");
		}
		
	}

	/**
	 * 获取两个List的不同元素
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
		long st = System.nanoTime();
		List<Long> diff = new ArrayList<Long>();
		for (Long str : list1) {
			if (!list2.contains(str)) {
				diff.add(str);
			}
		}
		return diff;
	}

	
	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}
	
	@Action
	public void showSFT() {
		showSFTWindow.show();
	}
	
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}
	
	@Action
    public void deleteCustomer(){
    	String[] ids = this.customerGrid.getSelectedIds();
    	if(ids == null || ids.length > 1){
    		MessageUtils.alert("请选中一行!");
    		return;
    	}
    	SysCorpcontacts cor = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findById(Long.parseLong(ids[0]));
    	cor.setIsdelete(true);
    	this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.modify(cor);
    	this.customerGrid.reload();
    }
	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if("1".equals(custype)){
			this.selectedRowData.setCnorid((Long) m.get("id"));
			this.selectedRowData.setCnorname((String) m.get("name"));
			this.selectedRowData.setCnortitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cnortitlemblid");
			this.update.markUpdate(UpdateLevel.Data, "cnortitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "cnortitlembl");
		}
		else if("2".equals(custype)){
			this.selectedRowData.setCneeid((Long) m.get("id"));
			this.selectedRowData.setCneename((String) m.get("name"));
			this.selectedRowData.setCneetitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembid");
			this.update.markUpdate(UpdateLevel.Data, "cneetitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "cneetitlembl");
		}
		else if("3".equals(custype)){
			this.selectedRowData.setNotifyid((Long) m.get("id"));
			this.selectedRowData.setNotifyname((String) m.get("name"));
			this.selectedRowData.setNotifytitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifytitlemblid");
			this.update.markUpdate(UpdateLevel.Data, "notifytitlemblname");
			this.update.markUpdate(UpdateLevel.Data, "notifytitlembl");
		}
		showCustomerWindow.close();
	}
	@Action
	public void saveMaster(){
		try {
			serviceContext.busOrderMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}
	
	@Action
	public void saveandclose(){
		saveMaster();
		showSFTWindow.close();
	}
	
	
	@Action
	public void showCustomer(){
		custype = (String) AppUtils.getReqParam("custype");
		//发货人
		if ("1".equals(custype)) {
			sql = " AND contactype = 'B' AND contactype2 = 'X'";
			Browser.execClientScript("cnortitlemblname.focus");
		//收货人
		}else if("2".equals(custype)) {
			sql = " AND contactype = 'B' AND contactype2 = 'Y'";
			Browser.execClientScript("cneetitlemblname.focus");
		}//通知人
		else if ("3".equals(custype)) {
			sql = " AND contactype = 'B' AND contactype2 = 'Z'";
			Browser.execClientScript("notifytitlemblname.focus");
		} 
		showCustomerWindow.show();
		this.customerGrid.reload();
	}
	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider(sql);
	}
	
	@Action
	public void savecne() {
		String cneid = AppUtils.getReqParam("cneid").trim();
		String cnename = AppUtils.getReqParam("cnename").trim();
		String cnetitle = AppUtils.getReqParam("cnetitle").trim();
		String type = AppUtils.getReqParam("type").trim();
		serviceContext.busOrderMgrService.saveCne(cneid,cnename,cnetitle,type,this.selectedRowData);
//		selectedRowData.setCnorid(Long.
//				.valueOf(getCusdesc(code)[0]));
//		selectedRowData.setCnorname(code);
		update.markUpdate(true, UpdateLevel.Data,
				"masterEditPanel");
		
	}
	
	@SaveState
	@Accessible
	@Bind
	public Long customerid;
	
	
	public String[] getCusdesc(String code) {
		String[] re = new String[2];
		String sql = "SELECT name,id FROM sys_corpcontacts WHERE  name ='"+code+"' AND customerid = "+this.customerid+" LIMIT 1;";
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
	
	@Action
	public void showAssign() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			assignIframe.load(blankUrl);
		} else {
			// if(!showAssign)
			assignIframe.load("../customer/assigneduser.xhtml?id="
					+ this.pkVal + "&linktype=D");
			// showAssign = true;
		}
	}
	
	@Bind
	@SaveState
	public String copyNo;
	
	@Action
	public void copyData() {
		String querySql  = "SELECT f_bus_ordr_copy2this('nos="+copyNo+";user="+AppUtils.getUserSession().getUsername()+";oderid="+this.pkVal+"') AS nos;";
		
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			this.update.markUpdate(UpdateLevel.Data, "masterEditPanel");
			MessageUtils.alert("OK");
			this.refresh();
			String jsFunction = "sendRedirect('./busorderfcl.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Bind
	@SaveState
	@Accessible
	public Long salescorpid = null;

	@Action
	public void salesChangeAjaxSubmit() {
		String salesid = AppUtils.getReqParam("salesid");
		String sql  = "SELECT deptid,corpid FROM sys_user WHERE id = " + salesid;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String cid = StrUtils.getMapVal(m, "corpid");

			String isDeptNotChangebySales = ConfigUtils.findSysCfgVal("jobs_dept_notchangeby_sales");//系统设置，接单公司不按业务员自动联动
			if("Y".equals(isDeptNotChangebySales)){
			}else{
				if(!StrUtils.isNull(cid)){
					this.salescorpid = Long.valueOf(cid);
					this.selectedRowData.setCorpid(StrUtils.isNull(cid)?0L:Long.valueOf(cid));
				}
				update.markUpdate(true, UpdateLevel.Data, "corpid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//bustype改变页面跳转
	public void bustypeSendRedirect(){
		BusOrder busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);
		if(busOrder.getBustype().equals("LCL")){
			String jsFunction = "sendRedirect('./busorderlcl.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		}else if(busOrder.getBustype().equals("FCL")){
			String jsFunction = "sendRedirect('./busorderfcl.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		}
	}
}
