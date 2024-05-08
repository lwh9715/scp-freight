package com.scp.view.module.order;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import org.operamasks.faces.component.layout.impl.UITabLayout;
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
import com.scp.base.LMapBase;
import com.scp.base.ConstantBean.Module;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.data.DatFeeitem;
import com.scp.model.order.BusOrder;
import com.scp.model.order.BusOrderdtl;
import com.scp.model.price.PriceFcl;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.model.ship.BusShipBooking;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.vo.price.FeeAdd;

@ManagedBean(name = "pages.module.order.busorderlclBean", scope = ManagedBeanScope.REQUEST)
public class BusOrderLclBean extends GridFormView {

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
	public UITabLayout tabLayout;
	
//	public UIIFrame iFrameBusType;
	
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
					this.initBusShipBooking(pkVal);
					this.refresh();
					//this.initDatas();
				} else {
					this.add();
				}
			}else if ("bpm".equals(type)) { // BPM
				String id = AppUtils.getReqParam("id");
				if (!StrUtils.isNull(id)) {
					try {
						//neo 20201216 处理订单流程生成工作单后，refid变成订单id+工作单id情况查询
						this.pkVal = serviceContext.busOrderMgrService.busOrderDao.findOneRowByClauseWhere("isdelete = false AND id IN ("+id+")").getId();
					} catch (Exception e) {
						e.printStackTrace();
						this.pkVal = Long.parseLong(id);
					}
					this.initBusShipBooking(pkVal);
					this.refresh();
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
		this.selectedRowData.setBustype("FCL");
		PriceFcl priceFcl = serviceContext.pricefclMgrService.pricefclDao
				.findById(Long.valueOf(priceid));
		this.selectedRowData.setCscode("");
		this.selectedRowData.setSales("");
		this.selectedRowData.setCustomercode("");
		this.selectedRowData.setCls(priceFcl.getCls());
		// this.selectedRowData.setEta(priceFcl.getEta());
		this.selectedRowData.setEtd(priceFcl.getEtd());

		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());

		this.selectedRowData.setPol(priceFcl.getPol());
		this.selectedRowData.setPod(priceFcl.getPod());
		this.selectedRowData.setPdd(priceFcl.getPod());
		this.selectedRowData.setShiping(priceFcl.getShipping());

		try {
			this.selectedRowData
					.setShipid(serviceContext.customerMgrService.sysCorporationDao
							.findOneRowByClauseWhere(
									" code = '" + priceFcl.getShipping() + "'")
							.getId());
		} catch (NoRowException e) {
			e.printStackTrace();
		}

		this.selectedRowData.setSalesid(AppUtils.getUserSession().getUserid());
	}
	
	private void initDatas(){
			this.selectedRowData = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);
			if(this.selectedRowData !=null && "LCL".equals(this.selectedRowData.getBustype())){
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
		this.selectedRowData.setBustype("LCL");
		this.selectedRowData.setCarryitem("CFS-CFS");
		this.selectedRowData.setFreightitem("PP");
//		this.selectedRowData.setEtd(Calendar.getInstance().getTime());
		this.assigns = "";
		update.markUpdate(UpdateLevel.Data, "type");
		checkBoookingButtonState();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		Browser.execClientScript("addInit();");
//		iFrameBusType.load("./busorderfcl.xhtml?orderid="+this.selectedRowData.getId());
//		update.markAttributeUpdate(iFrameBusType, "src");
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
			this.refresh();
			this.add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public void refresh() {
		selectedRowData = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);
		if(this.selectedRowData !=null && "LCL".equals(this.selectedRowData.getBustype())){
			this.jsonData = getJsonDatas();
		}else{
			this.jsonData = "''";
		}
		if(this.selectedRowData !=null && selectedRowData.getCorpidop()!=null && selectedRowData.getCorpidop() > 0) {
			this.sono.disable();
		}else {
			this.sono.enable();
		}
		if (selectedRowData!=null&&selectedRowData.getBustype()!="LCL") {
			selectedRowData.setBustype("LCL");
		}
		if (selectedRowData!=null){
			this.assigns = this.serviceContext.busOrderMgrService.getAssigns(this.selectedRowData.getId());	
		}
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		//Browser.execClientScript("clearRows();");
		//Browser.execClientScript("init();");
		
		if(this.selectedRowData != null && this.selectedRowData.getAgentid() != null){
			SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(this.selectedRowData.getAgentid());
			if(sysCorporation != null)Browser.execClientScript("$('#agent_input').val('"+sysCorporation.getAbbr()+"')");
		}else{
			Browser.execClientScript("$('#agent_input').val('')");
		}
	}
	
	@Action
	public void refreshsub(){
		refresh();
	}
	
	@Override
	@Transactional
	public void save() {
		//Object id = transactionTemplate.execute(new TransactionCallback() {
	   //     @Override  
	    //    public String doInTransaction(TransactionStatus status) { 
	     //   	if(selectedRowData.getBustype().equals("AIR")){
	    //			MessageUtils.alert("~~~↓~~");
	    //			return "";
	    //		}
	    		try{
	    			serviceContext.busOrderMgrService.busOrderDao
	    					.createOrModify(selectedRowData);
	    			pkVal = selectedRowData.getId();
	    			update.markUpdate(true, UpdateLevel.Data, "pkVal");
	    			selectedRowData = serviceContext.busOrderMgrService.busOrderDao
	    					.findById(pkVal);
	    			update.markUpdate(true, UpdateLevel.Data, "orderno");
	    			checkBoookingButtonState();
	    			
	    			changeCorpidOps();
	    			Long shipid = selectedRowData.getShipid();
	    			if(shipid==null){
	    				Browser.execClientScript("shipidJsvar.setValue('')");
	    			}
	    			//Browser.execClientScript("parent.frames['busTypeIFrameName']","changeCorpidOpsJsvar.submit();");
//	    			Browser.execClientScript("parent.frames['busTypeIFrameName']","saveByParent("+this.pkVal+");");
	    		} catch (Exception e) {
	    			MessageUtils.showException(e);
	    		}
	    //    	return "";
	    //    }
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
			MessageUtils.alert("工作单已生成，稍后会直接打开该工作单(工作单号确定后，请在单号旁边按钮，取一个正式的号码)");
			this.refresh();
			String url = AppUtils.chaosUrlArs("../ship/jobsedit.xhtml") + "&id="
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
		String url = AppUtils.chaosUrlArs("../ship/jobsedit.xhtml") + "&id="
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
				+ "pages/module/order/busorderlcl.aspx?type=edit&id="
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
		querySql.append("\n SELECT salesid,salesname, contact , tel1 , email1, cnortitle ,cnorname,COALESCE(tradeway,'F') AS tradeway,COALESCE(impexp,'E') AS impexp FROM ");
		querySql.append("\n (SELECT u.id AS salesid,u.namec AS salesname, c.contact , c.tel1 , c.email1 ");
		querySql.append("\n 	,(SELECT x.contactxt FROM sys_corpcontacts x WHERE x.customerid = c.id AND x.contactype2 = 'S' AND x.contactype = 'B' LIMIT 1) AS cnortitle");
		querySql.append("\n 	,(SELECT x.name FROM sys_corpcontacts x WHERE x.customerid = c.id AND x.contactype2 = 'S' AND x.contactype = 'B' LIMIT 1) AS cnorname");
		querySql.append("\n 	,(CASE WHEN c.tradeway='' OR c.tradeway IS NULL THEN 'F' ELSE tradeway END) AS tradeway,(CASE WHEN c.impexp='' OR c.impexp IS NULL THEN 'E' ELSE impexp END) AS impexp ");
		querySql.append("\n FROM sys_corporation c left join sys_user u ON( u.id = c.salesid  AND u.isdelete = false )");
		querySql.append("\n WHERE  ");
		querySql.append("\n 	c.id ="+customerid);
		querySql.append("\n UNION ");
		querySql.append("\n SELECT userid AS salesid,(SELECT namec FROM sys_user WHERE id = a.userid) AS salesname,NULL,NULL,NULL,NULL,NULL,NULL,NULL");
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
			
			this.selectedRowData.setCustomercontacter(StrUtils.getMapVal(m, "contact"));
			this.selectedRowData.setCustomertel(StrUtils.getMapVal(m, "tel1"));
			this.selectedRowData.setCustomeremail(StrUtils.getMapVal(m, "email1"));
			
			this.selectedRowData.setCnorname(StrUtils.getMapVal(m, "cnorname"));
			this.selectedRowData.setCnortitle(StrUtils.getMapVal(m, "cnortitle"));
			
			update.markUpdate(true, UpdateLevel.Data, "tradeway");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			update.markUpdate(true, UpdateLevel.Data, "impexp");
			update.markUpdate(true, UpdateLevel.Data, "customercontacter");
			update.markUpdate(true, UpdateLevel.Data, "customertel");
			update.markUpdate(true, UpdateLevel.Data, "customeremail");
			
			update.markUpdate(true, UpdateLevel.Data, "cnortitlemblname");
			update.markUpdate(true, UpdateLevel.Data, "cnortitlembl");
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
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("templatename='"
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
			try {
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao.findById(priceFclFeeadd.getFeeitemid());
				fclFeeAdd.setFeeitemname((AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en))?datFeeitem.getNamee():datFeeitem.getName());
			} catch (Exception e) {
				fclFeeAdd.setFeeitemname(priceFclFeeadd.getFeeitemname());
			}
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
			fclFeeAdd.setCurrencyap(priceFclFeeadd.getCurrency());
			Short tmps = 0;
			fclFeeAdd.setPiece(tmps);
			
			if("体积".equals(priceFclFeeadd.getUnit())){
				fclFeeAdd.setCbm(this.selectedRowData.getCbm());
			}else{
				fclFeeAdd.setPiece(new Short("1"));
				fclFeeAdd.setCbm(new BigDecimal(1));
			}
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
					"WHERE istemplate=true AND isdelete = FALSE", "ORDER BY templatename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Action
	public void changeCorpidOps(){
//		this.jsonDataStr = AppUtils.getReqParam("json");
//		this.pkVal = Long.parseLong(AppUtils.getReqParam("pk"));
		this.selectedRowData = this.serviceContext.busOrderMgrService.busOrderDao.findById(this.pkVal);
		if(selectedRowData==null||selectedRowData.getBustype()==null){
			MessageUtils.alert("保存失败!");
			return;
		}
		if("LCL".equals(selectedRowData.getBustype())){
			this.serviceContext.busOrderMgrService.updateBusType(selectedRowData.getId(), "LCL");
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
//				if (li.getFeeitemid() < 1) {
//					continue;
//				}
//				if (li.getAmt() == null) {
//					continue;
//				}
				
				BusOrderdtl busorderdtl = new BusOrderdtl();
				if (li.getId() > 0) {
					busorderdtl = serviceContext.busOrderMgrService.busOrderdtlDao
							.findById(li.getId());
					idsNewArray.add(li.getId());
				}
				busorderdtl.setOrderid(pkVal);
				busorderdtl.setFeeitemid(li.getFeeitemid());
				busorderdtl.setFeeitemcode(li.getFeeitemcode());
				try {
					DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
							.findById(li.getFeeitemid());
					busorderdtl.setFeeitemname((AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en))?datFeeitem.getNamee():datFeeitem.getName());
				} catch (Exception e) {
					busorderdtl.setFeeitemname(li.getFeeitemname());
				}
				
				busorderdtl.setCustomeridar(li.getCustomeridar());
				busorderdtl.setCustomerar(li.getCustomerar());
				busorderdtl.setCustomeridap(li.getCustomeridap());
				busorderdtl.setCustomerap(li.getCustomerap());
				
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
				busorderdtl.setCurrencyap(li.getCurrencyap());
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
	
	@Action
	private void saveAjaxSubmit() {
		this.selectedRowData = this.serviceContext.busOrderMgrService.busOrderDao.findById(this.pkVal);
		if(selectedRowData==null||selectedRowData.getBustype()==null){
			MessageUtils.alert("保存失败!");
			return;
		}
		if(!"LCL".equals(selectedRowData.getBustype())){
			this.serviceContext.busOrderMgrService.updateBusType(selectedRowData.getId(), "LCL");
		}
		
		List<Long> idsOldArray = new ArrayList<Long>();
		idsOldArray.clear();
		List<Long> idsNewArray = new ArrayList<Long>();
		idsNewArray.clear();
		
		// 保存明细数据
		if (!StrUtils.isNull(jsonDataStr) && !"null".equals(jsonDataStr)) {

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
				if (li.getAmt20() == null && li.getAmt40gp() == null
						&& li.getAmt40hq() == null && li.getAmt() == null
						&& li.getAmt20ar() == null && li.getAmt40gpar() == null
						&& li.getAmt40hqar() == null && li.getAmtar() == null) {
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
				try {
					DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
							.findById(li.getFeeitemid());
					busorderdtl.setFeeitemname((AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en))?datFeeitem.getNamee():datFeeitem.getName());
				} catch (Exception e) {
					busorderdtl.setFeeitemname(li.getFeeitemname());
				}
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
				busorderdtl.setCurrencyap(li.getCurrencyap());
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
	public void showCustomer(){
		custype = (String) AppUtils.getReqParam("custype");
//		//发货人
//		if ("1".equals(custype)) {
//			sql = " AND contactype = 'B' AND contactype2 = 'X'";
//			Browser.execClientScript("cnortitlemblname.focus");
//		//收货人
//		}else if("2".equals(custype)) {
//			sql = " AND contactype = 'B' AND contactype2 = 'Y'";
//			Browser.execClientScript("cneetitlemblname.focus");
//		}//通知人
//		else if ("3".equals(custype)) {
//			sql = " AND contactype = 'B' AND contactype2 = 'Z'";
//			Browser.execClientScript("notifytitlemblname.focus");
//		}
		if(this.customerid == null)this.customerid=0l;
		// 收货人
		if ("0".equals(custype)) {
			sql = " AND contactype = 'B' AND contactype2 = 'C' " +
			// 1947 系统设置增加，收发通不按委托人提取
					" AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
					// 或者对应客户勾选了收货人
					+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)" 
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=C;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
					;
			Browser.execClientScript("cneetitlemblname.focus");

			// 发货人
		} else if ("1".equals(custype)) {
			sql = " AND contactype = 'B' AND contactype2 = 'S' "
					+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
					// 或者对应客户勾选了发货人
					+ this.customerid + " OR (ishipper = TRUE AND salesid IS NULL)) END)"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=S;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
					;
			Browser.execClientScript("cnortitlemblname.focus");
			// 通知人
		} else if ("2".equals(custype)) {
			sql = " AND contactype = 'B' AND (contactype2 = 'N' OR contactype2 = 'C') "
					+ " AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'SFT_filterby_client' AND val = 'Y') THEN TRUE ELSE (customerid = "
					+ this.customerid + " OR (isconsignee = TRUE AND salesid IS NULL)) END)"
					+ "\nAND a.id = ANY(SELECT id FROM f_sys_corpcontacts_filter('srctype=N;customerid="+this.customerid+";userid="+AppUtils.getUserSession().getUserid()+"'))"
					;
			Browser.execClientScript("notifytitlemblname.focus");
			// hbl代理
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
	
	//bustype改变页面跳转
	public void bustypeSendRedirect(){
		BusOrder busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);
		if(busOrder.getBustype().equals("AIR")){
			String jsFunction = "sendRedirect('./busorderair.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		}else if(busOrder.getBustype().equals("FCL")){
			String jsFunction = "sendRedirect('./busorderfcl.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		}
	}
	
	
	@Action
	private void createJobsin() {
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
			String jobno = this.serviceContext.busOrderMgrService.createJobsin(
					this.pkVal, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("入仓单已生成，稍后会直接打开该工作单");
			this.refresh();
			String url = AppUtils.chaosUrlArs("../stock/wmsinedit.xhtml") + "&id="
			+ this.selectedRowData.getWmsinid();
			AppUtils.openWindow("_showJobno_" + this.selectedRowData.getJobno(),
			url);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			
		}
	}
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Bind
	@SaveState
	public String bpmremark = "";
	
	
	@Action
	public void applyBPMform() {
		if(pkVal==null||!(pkVal>0)){
			MessageUtils.alert("请先保存订单！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-54054AD1";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+this.selectedRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str =  s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
				}
				Browser.execClientScript("bpmWindowJsVar.show();");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	@SaveState
	public String taskname;
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-54054AD1";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
				lists.add(selectItem);
			}
		}catch(NoRowException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser.contains(id)){
				this.nextAssignUser = nextAssignUser + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void applyBPM() {
		if(this.pkVal==null||!(pkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-54054AD1";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+bpmremark+";taskname="+taskname+";refno="+this.selectedRowData.getOrderno()+";refid="+this.selectedRowData.getId()+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
				String sub =  sm.get("rets").toString();
				MessageUtils.alert("OK");
				Browser.execClientScript("bpmWindowJsVar.hide();");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
	
	@Action
	public void scanReport() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/orderBusiness_LCL.raq&orderid="+this.selectedRowData.getId();
		AppUtils.openWindow("orderBusiness", openUrl);
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
	
	@Action
	public void podajaxSubmit(){
		String podid = AppUtils.getReqParam("podid").toString();
		if(!StrUtils.isNull(podid)){
			// 2221 目的港带航线地方修改 如果港口里面有设置二级航线，这里显示二级航线，没有二级航线则显示港口的航线
			String sql = "SELECT code FROM dat_line x WHERE x.isdelete = false AND EXISTS(SELECT 1 FROM dat_port "
					+ "WHERE (CASE WHEN line2<>'' AND line2 IS NOT NULL THEN line2 = x.namec ELSE line = x.namec END) AND id ="
					+ podid + ") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if (m != null && m.get("code") != null) {
					this.selectedRowData.setLinecode(m.get("code").toString());
					update.markUpdate(UpdateLevel.Data, "linecode");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
