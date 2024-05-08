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
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.data.DatCntype;
import com.scp.model.data.DatFeeitem;
import com.scp.model.finance.FinaJobs;
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

@ManagedBean(name = "pages.module.order.busorderfclBean", scope = ManagedBeanScope.REQUEST)
public class BusOrderFclBean extends GridFormView {

	@Resource(name="hibTtrTemplate")  
    private TransactionTemplate transactionTemplate;
	
	@SaveState
	@Accessible
	public BusOrder selectedRowData = new BusOrder();

	@SaveState
	@Accessible
	public BusShipBooking selectedRowDataBooking = new BusShipBooking();

	@Bind
	@SaveState
	public Long userid;

	@Bind
	@SaveState
	public String priceid = "-1";

	@Bind
	public UIWindow cancelBookingRemarksWindow;

	@Bind
	public UIButton createBooking;

	@Bind
	public UIButton cancelBooking;

	@Bind
	public UIButton sendMessageToCustomerService;

	@Bind
	public UIButton lookAtTheBooking;

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
	public UIIFrame constructIframe;//查看订舱

	@Bind
	public UITextArea cancelBookingRemarks;//备注
	
	@Bind
	public UICell bookingCellPanel;
	
	@Bind
	public UITextField sono;

	@Bind
	@SaveState
	public String jsonData = "''";//订单费用明细json
	
	@Bind
	@SaveState
	public String jsonCnyType = "''";//其它箱型json

	@Bind
	@SaveState
	public String templateComBo;

	@Bind
	@SaveState
	public String jsonDataStr = "''";

	@Bind
	@SaveState
	public String assigns;
	
	@Bind
	private UIIFrame busshipaddresiform;
	
	@Bind
	@SaveState
	public String language;
	
	@Bind
	@SaveState
	public String actionJsText;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.userid = AppUtils.getUserSession().getUserid();
			this.update.markUpdate(UpdateLevel.Data, "userid");
			this.language = AppUtils.getUserSession().getMlType().toString();
			initCtrl();
			
			String type = AppUtils.getReqParam("type");
			if (StrUtils.isNull(type)) {
				this.add();
			} else if ("fromPrice".equals(type)) { // 报价生成
				String priceidStr = AppUtils.getReqParam("priceid");
				if (!StrUtils.isNull(priceidStr)) {// 报价中生成订单,初始化报价相关数据
					this.priceid = priceidStr;
					this.initOtherCnyTypeForJson();
					initDataPrice();//
				}
			} else if ("edit".equals(type)) { // 编辑
				String id = AppUtils.getReqParam("id");
				if (!StrUtils.isNull(id)) {
					pkVal = Long.valueOf(id);
					this.initOtherCnyTypeForJson();
					this.initBusShipBooking(pkVal);
					this.refresh();
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
					this.initOtherCnyTypeForJson();
					this.initBusShipBooking(pkVal);
					this.refresh();
				}
			}else {
				this.add();
			}
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			
			if(!"BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				Browser.execClientScript("showcheck();");
			}
			
			String taskid = AppUtils.getReqParam("taskid");
			if(!StrUtils.isNull(taskid)){
				actionJsText = "";
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
				List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmTask.getProcessid()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'js'");
				for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
					actionJsText+=bpmWorkItem.getActions();
				}
				//System.out.println("actionJsText:"+actionJsText);
				update.markUpdate(true, UpdateLevel.Data, "actionJsText");
			}
			//initAgent();
			
		}
	}
	
	@Bind
	public UIWindow showSFTWindow;
	
	@Bind
	public UIWindow showCustomerWindow;
	
	@Bind
	public UIWindow showJobsnosWindow;
	
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
	
	@SaveState
	@Accessible
	public FinaJobs job = new FinaJobs();
	
	@Bind
	public UIIFrame assignIframe;
	
	@Bind
	public UIDataGrid ordernosGrid;
	
	@Bind
	private String qryJobsnosKey;
	
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
	
	@Bind
	@SaveState
	public String ordernos ;
	
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
					//initAgent();
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
	
	private void initCtrl() {
		add.setDisabled(true);
		del.setDisabled(true);
		createJobs.setDisabled(true);
//		createBooking.setDisabled(false);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_order.getValue())) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
			} else if (s.endsWith("_update")) {
			} else if (s.endsWith("_createjob")) {
				createJobs.setDisabled(false);
			} else if (s.endsWith("_booking")) {
//				createBooking.setDisabled(false);
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
	
/*	private void initAgent(){
		try {
			if(this.pkVal > 0){
				selectedRowData = serviceContext.busOrderMgrService.busOrderDao.findById(this.pkVal);
				if(selectedRowData != null && selectedRowData.getAgentid() > 0){
					String agentsql = "SELECT * FROM sys_corporation WHERE id = "+selectedRowData.getAgentid();
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(agentsql);
					if (m != null && m.get("abbr") != null) {
						agent = m.get("abbr").toString();
						update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	@Bind
	@SaveState
	public String selectcnytype;

	private void initDataPrice() {
		if(pkVal==null||!(pkVal>0)){
			this.pkVal = -1L;
			selectedRowData = new BusOrder();
			this.selectedRowData.setOrderdate(Calendar.getInstance().getTime());
			this.selectedRowData.setDeliverdate(Calendar.getInstance().getTime());
			this.selectedRowData.setBustype("FCL");
		}
		PriceFcl priceFcl = serviceContext.pricefclMgrService.pricefclDao
				.findById(Long.valueOf(priceid));
		this.selectedRowData.setCscode("");
		this.selectedRowData.setSales("");
		this.selectedRowData.setCustomercode("");
		this.selectedRowData.setCls(priceFcl.getCls());
		// this.selectedRowData.setEta(priceFcl.getEta());
		this.selectedRowData.setEtd(priceFcl.getEtd());
		if(this.selectedRowData.getDestination()==null){
			this.selectedRowData.setDestination("");
		}
		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		this.selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());
		try {
			String polsql = "SELECT id FROM dat_port WHERE namee = '"+StrUtils.getSqlFormat(priceFcl.getPol())+"'";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(polsql);
			if (m != null && m.get("id") != null) {
				this.selectedRowData.setPolid(Long.parseLong(m.get("id").toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String polsql = "SELECT id FROM dat_port WHERE namee = '"+StrUtils.getSqlFormat(priceFcl.getPod())+"'";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(polsql);
			if (m != null && m.get("id") != null) {
				this.selectedRowData.setPodid(Long.parseLong(m.get("id").toString()));
				this.selectedRowData.setPddid(Long.parseLong(m.get("id").toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2201 提单港口包含国家 =Y时 ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
		if(!StrUtils.isNull(findSysCfgVal)&&findSysCfgVal.equals("Y")){
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE " +
						"	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND namee = '"+StrUtils.getSqlFormat(priceFcl.getPol())+"') LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					this.selectedRowData.setPol(priceFcl.getPol()+","+m.get("namee").toString());
				}
			} catch (Exception e) {
				this.selectedRowData.setPol(priceFcl.getPol());
				e.printStackTrace();
			}
		}else{
			this.selectedRowData.setPol(priceFcl.getPol());
		}
		if(!StrUtils.isNull(findSysCfgVal)&&findSysCfgVal.equals("Y")){
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE " +
						"	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND namee = '"+StrUtils.getSqlFormat(priceFcl.getPod())+"') LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					this.selectedRowData.setPod(priceFcl.getPod()+","+m.get("namee").toString());
					this.selectedRowData.setPdd(priceFcl.getPod()+","+m.get("namee").toString());
				}
			} catch (Exception e) {
				this.selectedRowData.setPod(priceFcl.getPod());
				this.selectedRowData.setPdd(priceFcl.getPod());
				e.printStackTrace();
			}
		}else{
			this.selectedRowData.setPod(priceFcl.getPod());
			this.selectedRowData.setPdd(priceFcl.getPod());
		}
		this.selectedRowData.setShiping(priceFcl.getShipping());
		this.selectedRowData.setCntypeothercode(priceFcl.getCntypeothercode());
//		this.selectedRowData.setPieceother(priceFcl.getPieceother());
		this.selectedRowData.setContractno(priceFcl.getContractno());
		this.selectedRowData.setRemark_out(priceFcl.getRemark_out());
		this.selectedRowData.setPriceuserid(priceFcl.getPriceuserid());
		this.selectedRowData.setRoutecode(priceFcl.getShipline());
		
		this.selectedRowData.setLinecode(priceFcl.getLine());
		
		try {
			// AND NOT EXIST(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = thql.id)
			this.selectedRowData.setShipid(serviceContext.customerMgrService.sysCorporationDao.findOneRowByClauseWhere(" code = '" + priceFcl.getShipping() + "' AND isdelete = false AND iscarrier = true AND iscustomer = TRUE)" ).getId());
		} catch (NoRowException e) {
			e.printStackTrace();
		} catch(MoreThanOneRowException e){
			e.printStackTrace();
		}
		String cost20gp = priceFcl.getCost20()!=null?priceFcl.getCost20().toString():"0";
		String cost40gp = priceFcl.getCost40gp()!=null?priceFcl.getCost40gp().toString():"0";
		String cost40hq = priceFcl.getCost40hq()!=null?priceFcl.getCost40hq().toString():"0";
		String costother = (priceFcl.getPieceother()==null?"0":priceFcl.getPieceother()).toString();
		String cntypeothercode = (priceFcl.getCntypeothercode()==null?"-1":priceFcl.getCntypeothercode()).toString();
		DatCntype datCntype = null;
			datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(Long.parseLong((cntypeothercode!=null&&!cntypeothercode.equals(""))?cntypeothercode:"-1"));
			
			
		String csno = ConfigUtils.findSysCfgVal("CSNO");
//		if(priceFcl.getPieceother()>0){
//			this.selectedRowData.setPieceother(priceFcl.getPieceother()) ;
//		}
		if(datCntype!=null){
			selectcnytype = datCntype.getCode();
			this.selectedRowData.setCntypeothercode(selectcnytype);
		}
		this.selectedRowData.setSalesid(AppUtils.getUserSession().getUserid());
		
		if("2234".equals(csno)){
			Browser.execClientScript("addCostItem(-100,-100,2236256200,'OCF','海运费','PP','USD','USD','箱型',"+cost20gp+","+0+","+cost40gp+","+0+","+cost40hq+","+0+","+costother+","+0+",0,0,0,0,0,0,'');");
		}else{
			Browser.execClientScript("addCostItem(-100,-100,2236256200,'OCF','海运费','PP','USD','USD','箱型',"+cost20gp+","+cost20gp+","+cost40gp+","+cost40gp+","+cost40hq+","+cost40hq+","+costother+","+costother+",0,0,0,0,0,0,'');");
		}
		Browser.execClientScript("computer();");
//		try {
//			String sql = "SELECT DISTINCT templatename FROM price_fcl_feeadd AS d WHERE istemplate=true AND isdelete = FALSE AND templatename ILIKE '%"+this.selectedRowData.getShiping()+"%' ORDER BY templatename LIMIT 1";
//			Map map = this.serviceContext.busOrderMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//			if(map != null && map.containsKey("templatename")){
//				autoImportTemplate(map.get("templatename").toString());
//			}
//		}catch (NoRowException e) {
//			//模版名称未匹配上,不作任何处理
//		}catch (NullPointerException e) {
//		}catch (Exception e) {
//			MessageUtils.showException(e);
//		}
		
		
		
    	List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid="+priceid 
    			+ " AND isdelete = false ORDER BY id");
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
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
						.findById(priceFclFeeadd.getFeeitemid());
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
			fclFeeAdd.setAmtother(priceFclFeeadd.getAmtother());
			fclFeeAdd.setAmtother_ar(priceFclFeeadd.getAmtother());
			fclFeeAdd.setCurrency(priceFclFeeadd.getCurrency());
			fclFeeAdd.setCurrencyap(priceFclFeeadd.getCurrency());
			
			if("2234".equals(csno)){
				fclFeeAdd.setAmtar(new BigDecimal(0));
				fclFeeAdd.setAmt20ar(new BigDecimal(0));
				fclFeeAdd.setAmt40gpar(new BigDecimal(0));
				fclFeeAdd.setAmt40hqar(new BigDecimal(0));
				fclFeeAdd.setAmtother_ar(new BigDecimal(0));
			}
			
			
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
		if(this.selectedRowData !=null &&this.selectedRowData.getPriceuserid()!=null){
			SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
			Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
		}else{
			Browser.execClientScript("$('#priceuser_input').val('')");
		}
		update.markUpdate(UpdateLevel.Data, "templateComBo");
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		//Browser.execClientScript("init();");
	}
	/**
	 * 自动导入费用模版
	 * @param templatename
	 */
	public void autoImportTemplate(String templatename){
		if(templatename==null||templatename.isEmpty()){
			return;
		}
		this.templateComBo = templatename;
		this.jsonData = "";
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
				.findAllByClauseWhere("templatename='"
						+ templatename
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
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
						.findById(priceFclFeeadd.getFeeitemid());
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
			fclFeeAdd.setCurrency("CNY");
			fclFeeAdd.setCurrencyap("CNY");
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
		update.markUpdate(UpdateLevel.Data, "templateComBo");
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		Browser.execClientScript("init();");
	}
	/**
	 * 初始化订单费用明细json
	 * @return
	 */
	public String getJsonDatas() {
		try {
			String ret = "''";
			if ("-1".equals(priceid)) {
				ret = serviceContext.busOrderMgrService.getOrderdtlById(pkVal);
			} else {
				ret = serviceContext.busOrderMgrService.getPriceFeeById(Long
						.valueOf(priceid));
			}
			// //AppUtils.debug("getret:" + ret);
			return StrUtils.isNull(ret) ? "''" : ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "''";
		}
	}
	/**
	 * 初始化其它箱型
	 */
	private void initOtherCnyTypeForJson(){
		try {
			String ret = "''";
			ret = this.serviceContext.cntypeMgrService.getOtherCnyTypeForJson();
			////AppUtils.debug("cnyType:" + ret);
			this.jsonCnyType = ret == null ? "''" : ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.jsonCnyType = "''";
		} finally {
			update.markUpdate(true, UpdateLevel.Data, "jsonCnyType");
		}
		
	}
	/**
	 * 运费模版选择事件
	 */
	//@Action(id = "templateComBo", event = "onselect")
	@Action
	public void templateComBoAction() {
		String cntypeothercode = AppUtils.getReqParam("cntypeothercode");
		templateComBo = AppUtils.getReqParam("templateComBov");
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
			fclFeeAdd.setCurrencyap(priceFclFeeadd.getCurrency());
			fclFeeAdd.setFeeitemid(priceFclFeeadd.getFeeitemid());
			fclFeeAdd.setFeeitemcode(priceFclFeeadd.getFeeitemcode());
			try {
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
						.findById(priceFclFeeadd.getFeeitemid());
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
			Map map = null;
			try {
				map = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow("SELECT code FROM dat_cntype WHERE id ="
								+ priceFclFeeadd.getCntypeid());
			} catch (NoRowException e) {
				map = null;
			}
			//其他箱型一样就赋值
			if(map!=null&&map.get("code")!=null&&cntypeothercode!=null&&cntypeothercode.equals(map.get("code"))){
//				fclFeeAdd.setAmtother(new BigDecimal(0));
//				fclFeeAdd.setAmtother_ar(new BigDecimal(0));
				fclFeeAdd.setAmtother(priceFclFeeadd.getAmtother());
				fclFeeAdd.setAmtother_ar(priceFclFeeadd.getAmtother());
			}
//			fclFeeAdd.setCurrency("CNY");
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
					"WHERE istemplate=true AND isdelete=FALSE", "ORDER BY templatename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
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
	
	@Action
	private void changeCorpidOps(){
//		this.jsonDataStr = AppUtils.getReqParam("json");
		this.selectedRowData = this.serviceContext.busOrderMgrService.busOrderDao.findById(this.pkVal);
//		
		if(selectedRowData==null||selectedRowData.getBustype()==null){
			MessageUtils.alert("保存失败!");
			return;
		}
		if("FCL".equals(selectedRowData.getBustype())){
			this.serviceContext.busOrderMgrService.updateBusType(selectedRowData.getId(), "FCL");
		}
		List<Long> idsOldArray = new ArrayList<Long>();
		idsOldArray.clear();
		List<Long> idsNewArray = new ArrayList<Long>();
		idsNewArray.clear();
		
		// 保存明细数据
		if (!StrUtils.isNull(jsonDataStr) && !"null".equals(jsonDataStr) && !"''".equals(jsonDataStr)) {

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
				busorderdtl.setCustomeridar(li.getCustomeridar());
				busorderdtl.setCustomerar(li.getCustomerar());
				busorderdtl.setCustomeridap(li.getCustomeridap());
				busorderdtl.setCustomerap(li.getCustomerap());
				try {
					DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
							.findById(li.getFeeitemid());
					busorderdtl.setFeeitemname(datFeeitem.getName());
				} catch (Exception e) {
					busorderdtl.setFeeitemname(li.getFeeitemname());
				}
				busorderdtl.setPpcc(li.getPpcc());
				busorderdtl.setCurrency(li.getCurrency());
				busorderdtl.setCurrencyap(li.getCurrencyap());
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
			//MessageUtils.alert("OK!");
			Browser.execClientScript("showmsg()");
		}
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
		this.selectedRowData.setBustype("FCL");
		this.selectedRowData.setGoodsdesc("");
		this.selectedRowData.setCarryitem("CY-CY");
		this.selectedRowData.setFreightitem("PP");
		if(this.selectedRowData.getDestination()==null){
			this.selectedRowData.setDestination("");
		}
//		this.selectedRowData.setEtd(Calendar.getInstance().getTime());
		update.markUpdate(UpdateLevel.Data, "type");
		checkBoookingButtonState();
		this.initOtherCnyTypeForJson();
		this.assigns = "";
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		Browser.execClientScript("addInit();");
		String url = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
		busshipaddresiform.load(url);
//		busshipaddresiform.repaint();
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
			Browser.execClientScript("showmsg()");
			this.refresh();
			this.add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void refresh() {
		if(pkVal <= 0){
			return;
		}
		this.selectedRowData = serviceContext.busOrderMgrService.busOrderDao.findById(pkVal);
		if(selectedRowData.getDestination()==null){
			selectedRowData.setDestination("");
		}
		if(this.selectedRowData !=null && "FCL".equals(this.selectedRowData.getBustype())){
			this.jsonData = getJsonDatas();
		}else{
			this.selectedRowData.setBustype("FCL");
			this.jsonData = "''";
		}
		if(selectedRowData.getCorpidop()!=null && selectedRowData.getCorpidop() > 0) {
			this.sono.disable();
		}else {
			this.sono.enable();
		}
		this.assigns = this.serviceContext.busOrderMgrService.getAssigns(this.selectedRowData.getId());
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		//加载配送地址iform
		if(this.selectedRowData.getIsclc()){
			String url = AppUtils.getContextPath() + "/common/busshipaddres.xhtml?linkid=" + this.pkVal;
			busshipaddresiform.load(url);
			busshipaddresiform.repaint();
		}else{
			String url = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			busshipaddresiform.load(url);
		}
		if(this.selectedRowData !=null &&this.selectedRowData.getPriceuserid()!=null){
			SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
			Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
		}else{
			Browser.execClientScript("$('#priceuser_input').val('')");
		}
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
	     //   @Override  
	     //   public String doInTransaction(TransactionStatus status) { 
	        	try{
					if (StrUtils.isNull(String.valueOf(selectedRowData.getSalesid()))) {
						MessageUtils.alert("请先保存业务员！");
						return;
					}

	    			serviceContext.busOrderMgrService.busOrderDao.createOrModify(selectedRowData);
	    			
	    			//serviceContext.busBookingMgrService.busShipBookingDao.createOrModify(selectedRowDataBooking);
	    			
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
	    			refresh();
	    		} catch (Exception e) {
	    			MessageUtils.showException(e);
	    		}
	      //  	return "";
	     //   }
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
		constructIframe.load(url);
		showChildWindow.show();
		//AppUtils.openWindow("_showBooking_" + this.selectedRowData.getOrderno(), url);
	}

	@Action
	private void lookAtTheBooking() {
		showChildWindow.show();
		showConstruct();
		this.createBooking.setDisabled(true); 
	}

	@SaveState
	private boolean showConstruct;

	@Action
	public void showConstruct() {
		constructIframe.load("../salesmgr/booking.xhtml?type=O&orderid="
				+ this.pkVal);

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
				//update.markUpdate(true, UpdateLevel.Data, "deptcomb");
				update.markUpdate(true, UpdateLevel.Data, "corpid");
//				this.deptids = getqueryDepartid();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			Browser.execClientScript("showmsg()");
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
					+ "pages/module/order/busorderfcl.aspx?type=edit&id="
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
		querySql.append("\n SELECT salesid,salesname , contact , tel1 , email1, cnortitle ,cnorname , COALESCE(tradeway,'F') AS tradeway,COALESCE(impexp,'E') AS impexp FROM ");
		querySql.append("\n (SELECT  ");
		querySql.append("\n 	u.id AS salesid,u.namec AS salesname , c.contact , c.tel1 , c.email1 ");
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
		querySql.append("\n AND a.isdelete = false ");
		querySql.append("\n AND a.userid > 0");
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
		arrears(customerid);
	}
	/*
	 * 如果该客户欠款在欠账额度之内，不提示，返回空,提示：客户信息(简称，中英文名，欠账额度，当前欠款)
	 * 超过时是否允许接单：不允许：增加提示不允许，并将当前单的界面上id和名称清空，如果允许就不处理只提示
	 * */
	public void arrears(String customerid){
		//neo 20161118 if null return
		SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(Long.valueOf(customerid));
		if(sysCorporation.getIsalloworder() == null){
			return;
		}
		
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("customerid",customerid);
		String urlArgs2 = AppUtils.map2Url(argsMap, ";");
		try {

			Long loguserid = AppUtils.getUserSession().getUserid();
			//前海国际事业部
			Map deptmap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select deptid FROM SYS_USER WHERE isdelete = FALSE AND id = "+loguserid);
			if(AppUtils.getUserSession().getCorpidCurrent() != 13632274 && !"1464156732274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565620302274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565643442274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565621682274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565614052274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565616572274".equals(String.valueOf(deptmap.get("deptid")))){
				return;
			}
			String sqlQry = "SELECT * FROM f_sys_corporation_checkamtowe('"+urlArgs2+"') AS ret";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			String str = StrUtils.getMapVal(m, "ret");
			
			if(str.equals("1")){
			}else {
				if(sysCorporation.getIsalloworder()){
					MessageUtils.alert("提示(Tips):"+str);
				}else{
					MessageUtils.alert(str);
					Browser.execClientScript("customeridJsVar.setValue('')");
					Browser.execClientScript("customernameJsVar.setValue('')");
					Browser.execClientScript("$('#customer_input').val('')");
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
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
	
	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {

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
			Browser.execClientScript("showmsg()");
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
		if(busOrder.getBustype().equals("LCL")){
			String jsFunction = "sendRedirect('./busorderlcl.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		}else if(busOrder.getBustype().equals("AIR")){
			String jsFunction = "sendRedirect('./busorderair.aspx?id="+this.pkVal+"&type=edit');";
			Browser.execClientScript(jsFunction);
		}
	}
	
	@Action
	public void getsurcharge(){
		if(pkVal>0){
			String sql = "SELECT id FROM price_fcl a WHERE isdelete = FALSE AND EXISTS(SELECT 1 FROM bus_order x " 
						+"WHERE x.id = "+pkVal+" AND EXISTS(SELECT 1 FROM sys_corporation WHERE x.shipid = id AND abbr = a.shipping))"
						+"AND pol = '"+this.selectedRowData.getPol()+"' AND pod = '"+this.selectedRowData.getPod()+"' ORDER BY daterelease LIMIT 1;";
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(list.size()<1){
				MessageUtils.alert("未查到匹配的运价");
				return;
			}
			String priceidStr = list.get(0).get("id").toString();
			if (!StrUtils.isNull(priceidStr)) {// 报价中生成订单,初始化报价相关数据
				this.priceid = priceidStr;
				this.initOtherCnyTypeForJson();
				initDatagetsurcharge();//
			}
		}else{
			MessageUtils.alert("请先保存");
		}
	}
	
	private void initDatagetsurcharge() {
		PriceFcl priceFcl = serviceContext.pricefclMgrService.pricefclDao
				.findById(Long.valueOf(priceid));
		this.selectedRowData.setCntypeothercode(priceFcl.getCntypeothercode());
		String cost20gp = priceFcl.getCost20().toString();
		String cost40gp = priceFcl.getCost40gp().toString();
		String cost40hq = priceFcl.getCost40hq().toString();
		String costother = (priceFcl.getPieceother()==null?"0":priceFcl.getPieceother()).toString();
		String cntypeothercode = (priceFcl.getCntypeothercode()==null?"-1":priceFcl.getCntypeothercode()).toString();
		DatCntype datCntype = null;
		datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(Long.parseLong((cntypeothercode!=null&&!cntypeothercode.equals(""))?cntypeothercode:"-1"));
		if(datCntype!=null){
			selectcnytype = datCntype.getCode();
			this.selectedRowData.setCntypeothercode(selectcnytype);
		}
		Browser.execClientScript("addCostItem(-100,-100,2236256200,'OCF','海运费','PP','USD','箱型',"+cost20gp+","+cost20gp+","+cost40gp+","+cost40gp+","+cost40hq+","+cost40hq+","+costother+","+costother+",0,0,0,0,0,0,'');");
		Browser.execClientScript("computer();");
		try {
			String sql = "SELECT DISTINCT templatename FROM price_fcl_feeadd AS d WHERE istemplate=true AND isdelete = FALSE AND templatename ILIKE '%"+this.selectedRowData.getShiping()+"%' ORDER BY templatename LIMIT 1";
			Map map = this.serviceContext.busOrderMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map != null && map.containsKey("templatename")){
				autoImportTemplate(map.get("templatename").toString());
			}
		}catch (NoRowException e) {
			//模版名称未匹配上,不作任何处理
		}catch (NullPointerException e) {
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@SaveState
	public String number;
	
	@Bind
	@SaveState
	public String detailsContent;
	
	@Bind
	public UIWindow detailsWindow;
	
	@Bind
	@SaveState
	public String detailsTitle;
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.number = AppUtils.getReqParam("number");
		String content = AppUtils.getReqParam("content");
		if("1".equals(number)){
			this.detailsContent = content;
			Browser.execClientScript("type1()");
		} else if("2".equals(number)){
			this.detailsContent = content;
			Browser.execClientScript("type2()");
		} else if("3".equals(number)){
			this.detailsContent = content;
			Browser.execClientScript("type3()");
		} else if("4".equals(number)){
			this.detailsContent = content;
			Browser.execClientScript("type4()");
		} 
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		/*this.update.markUpdate(UpdateLevel.Data, "detailsTitle");*/
		
		this.detailsWindow.show();
	}
	
	
	public void setDetails(String number) {
		if("1".equals(number)){
			this.selectedRowData.setRemarks_op(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "remarks_op");
		} else if("2".equals(number)){
			this.selectedRowData.setRemarks(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "remarks");
		} else if("3".equals(number)){
			this.selectedRowData.setContractno(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "contractno");
		}else if("4".equals(number)){
			this.selectedRowData.setRemark_out(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "remark_out");
		} 
	}
	
	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.number);

		this.detailsWindow.close();
	}
	
	
	
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Bind
	@SaveState
	public String bpmremark = "";
	
	@Bind
	@SaveState
	public String agent = "";
	
	@Action
	public void applyBPM() {
		if(this.pkVal==null||!(pkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-8A56807E";
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
	public void applyBPMform() {
		if(pkVal==null||!(pkVal>0)){
			MessageUtils.alert("请先保存订单！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-8A56807E";
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
	
	@Action
	public void scanReport() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/orderBusiness.raq&orderid="+this.selectedRowData.getId();
		AppUtils.openWindow("orderBusiness", openUrl);
	}
	
	@Bind
	public UIWindow showAttachWindow;
	
	@Bind
	public UIIFrame attachIframe;
	
	@Action
	public void attachment(){
		if(this.pkVal <= 0){
			this.alert("请先保存!");
			return;
		}
		attachIframe.load("/scp/pages/module/common/attachment.xhtml?linkid="+this.pkVal);
		showAttachWindow.show();
	}
	
	@Bind
	@SaveState
	public String taskname;
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-8A56807E";
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
			//2201 提单港口包含国家 =Y时 ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
			String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
			if(!StrUtils.isNull(findSysCfgVal)&&findSysCfgVal.equals("Y")){
				String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE " +
							"	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "+podid+") LIMIT 1";
				try {
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
					if (m != null && m.get("namee") != null) {
						Browser.execClientScript("$('#pod_input').val(podJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
						Browser.execClientScript("podJsVar.setValue(podJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Action
	public void polajaxSubmit(){
		String polid = AppUtils.getReqParam("polid").toString();
		//2201 提单港口包含国家 =Y时 ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
		if(!StrUtils.isNull(findSysCfgVal)&&findSysCfgVal.equals("Y")){
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE " +
						"	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "+polid+") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser.execClientScript("$('#pol_input').val(polJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
					Browser.execClientScript("polJsVar.setValue(polJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Action
	public void destinationajaxSubmit(){
		String destinationid = AppUtils.getReqParam("destinationid").toString();
		//2201 提单港口包含国家 =Y时 ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
		if(!StrUtils.isNull(findSysCfgVal)&&findSysCfgVal.equals("Y")){
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE " +
						"	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "+destinationid+") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser.execClientScript("$('#destination_input').val(destinationJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
					Browser.execClientScript("destinationJsVar.setValue(destinationJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Action
	public void pddajaxSubmit(){
		String pddid = AppUtils.getReqParam("pddid").toString();
		//2201 提单港口包含国家 =Y时 ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
		String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
		if(!StrUtils.isNull(findSysCfgVal)&&findSysCfgVal.equals("Y")){
			String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE " +
						"	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "+pddid+") LIMIT 1";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
				if (m != null && m.get("namee") != null) {
					Browser.execClientScript("$('#pdd_input').val(pddJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
					Browser.execClientScript("pddJsVar.setValue(pddJsVar.getValue()+','+'"+m.get("namee").toString()+"')");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
	
	@Bind 
	@SaveState
	private Boolean priceuserconfirm;
	
	/**
	 * 询价员确认
	 */
	@Action
	public void confirmSubmit(){
		priceuserconfirm = null == selectedRowData.getPriceuserconfirm() ? false : selectedRowData.getPriceuserconfirm();
		if(null == selectedRowData || selectedRowData.getId() < 1){
//			priceuserconfirm = !priceuserconfirm;
			MessageUtils.alert("请先保存");
			this.refresh();
			return;
		}
		long userid = null == selectedRowData.getPriceuserid() ? 0L : selectedRowData.getPriceuserid();
		try {
			if (userid>0L || priceuserconfirm == true){
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("Update bus_order SET priceuserconfirm = "+!priceuserconfirm+",updater = '"+AppUtils.getUserSession().getUsercode()+"' WHERE isdelete = false and id = "+selectedRowData.getId());
			}else{
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("Update bus_order SET priceuserconfirm = "+!priceuserconfirm+",priceuserid = "+AppUtils.getUserSession().getUserid()+",updater = '"+AppUtils.getUserSession().getUsercode()+"' WHERE isdelete = false and id = "+selectedRowData.getId());
			}
		} catch (Exception e) {
			MessageUtils.alert("尚未有询价员确认或取消权限!");
			e.printStackTrace();
		}
		this.refresh();
		MessageUtils.alert("OK!");
	}
}
