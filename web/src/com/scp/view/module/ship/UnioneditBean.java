package com.scp.view.module.ship;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusShipSchedule;
import com.scp.model.ship.BusShipjoin;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;

@ManagedBean(name = "pages.module.ship.unioneditBean", scope = ManagedBeanScope.REQUEST)
public class UnioneditBean extends MastDtlView {
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;
	
	@SaveState
	@Accessible
	public Long jobid;
	
	
	@SaveState
	@Accessible
	public String sql="AND 1=1";
	
	@SaveState
	@Accessible
	public String portsql="AND 1=1";
	
	
	@SaveState
	@Accessible
	public String custype;
	
	@SaveState
	@Accessible
	public String porttype;
	
	@SaveState
	@Accessible
	public Long customerid;
	
	@SaveState
	@Accessible
	public String abbr;
	

	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();

	
	
	@Bind
	public UIIFrame shipcontainerIframe;
	
	@Bind
	public UIIFrame shipgoodsIframe;
	
	@Bind
	public UIIFrame shipapIframe;
	
	@Bind
	public UIIFrame arapIframe;
	
	@Bind
	public UIIFrame receiptIframe;
	
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Bind
	public UIIFrame docdefIframe;
	
	
	
	@Bind
	public UIIFrame msgIframe;
	
	@Bind
	public UIIFrame traceIframe;
	
	@Bind
	public  UIWindow cfgUrlWindow;
	
	@Bind
	@SaveState
	public String url;
	

	@SaveState
	@Accessible
	public BusShipjoin selectedRowData = new BusShipjoin();
	
	@Bind
	public UIWindow shipscheduleWindow;
	
	@Bind
	public UIDataGrid gridShipschedule;
	

	@SaveState
	@Accessible
	public BusShipSchedule shipschedule = new BusShipSchedule();

	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Bind
	@SaveState
	private String fileName;
	
	@Bind
	@SaveState
	private String contentType;

	@Bind
	public UIButton copyGetinfo;

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
	
	@Action
	public void shipcontainer() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			shipcontainerIframe.load(blankUrl);
		}else{
			shipcontainerIframe.load("../ship/shipjoinlink.xhtml?id=" + this.mPkVal);
		}
	}
	
	@Action
	public void shipgoods() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			shipgoodsIframe.load(blankUrl);
		} else {
			shipgoodsIframe.load("../ship/busshipjoingoods.xhtml?id=" + this.mPkVal);
		}
	}
		
	@Action
	public void showAttachmentIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			attachmentIframe.load(blankUrl);
		} else {
			attachmentIframe.load(AppUtils.getContextPath() + "/pages/module/common/attachment.xhtml?linkid=" + this.mPkVal);
		}
	}
	
	@Action
	public void shipap() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			shipapIframe.load(blankUrl);
		} else {
			shipapIframe.load("../ship/unionap.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showArapEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			arapIframe.load(blankUrl);
		}else{
			arapIframe.load("../finance/arapeditunion.xhtml?jobid=" + this.selectedRowData.getJobid());
		}
	}
	
	@Action
	public void showDocDef() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			docdefIframe.load(blankUrl);
		}else{
			docdefIframe.load("../bus/busdocdef.xhtml?linkid=" + this.mPkVal);
		}
	}
	
	@Action
	public void showMsg() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			msgIframe.load(blankUrl);
		}else{
			msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
		}
	}
	
	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		}else{
			traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
		}
	}


	@Override
	public void init() {
		selectedRowData = new BusShipjoin();
		String id = AppUtils.getReqParam("id");
		setOfficeheader();
		copyGetinfo.setDisabled(true);
		
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else{
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.busShipjoinMgrService.busShipjoinDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		shipcontainer();
		showArapEdit();
//		showDocDef();
//		showMsg();
//		showTrace();
//		shipgoods();
//		showAttachmentIframe();
		
	}

	@Override
	public void doServiceSaveMaster() {
		if(this.selectedRowData.getScheduleid()==null){
			//MessageUtils.alert("请选择船期");
			//return;
		}

		if (selectedRowData.getEtd() != null
				&& selectedRowData.getEta() != null
				&& selectedRowData.getEta()
				.before(selectedRowData.getEtd())) {
			MessageUtils.alert("ETA不能小于ETD");
			return;
		}

		serviceContext.busShipjoinMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");
		shipcontainer();
		
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
		this.selectedRowData = new BusShipjoin();
		this.selectedRowData.setFreeday(14);
		selectedRowData.setSingtime(new Date());
		selectedRowData.setMbltype("T");
		selectedRowData.setManifeststat("I");
		selectedRowData.setCneetitle("");
		selectedRowData.setCnortitle("");
		selectedRowData.setNotifytitle("SAME AS CONSIGNEE");
		this.mPkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		shipcontainer();
		showArapEdit();
		setOfficeheader();
//		showDocDef();
//		showMsg();
//		showTrace();
//		shipgoods();
//		showAttachmentIframe();
		

	}
	
	

	@Override
	public void delMaster() {
			try {
				serviceContext.busShipjoinMgrService.removeDate(this.mPkVal,
						AppUtils.getUserSession().getUsercode());
				this.addMaster();
				this.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {

	}
	
	
	
	
	
	@Bind
	public UIWindow showPortWindow;

	@Bind
	public UIDataGrid portGrid;
	
	@Bind
	private String qryPortKey;
	
	/**
	 * 
	 */
	@Action
	public void portQry() {
		this.portchooseserviceBean.qryPort(qryPortKey);
		this.portGrid.reload();
	}

	@Bind(id = "portGrid", attribute = "dataProvider")
	public GridDataProvider getportGridDataProvider() {
		return this.portchooseserviceBean.getPortDataProvider(portsql);
	}

	@Action
	public void showPortAction() {
		String portcode = (String) AppUtils.getReqParam("portcode");
		
		qryPortKey = portcode;
		int index = qryPortKey.indexOf("/");
		if (index > 1)
			qryPortKey = qryPortKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		
		porttype = (String) AppUtils.getReqParam("porttype");
		
		
		if("0".equals(porttype)){
			portsql="AND ispol = TRUE";
		}else if("1".equals(porttype)){
			portsql="AND ispod = TRUE";
		}else if("2".equals(porttype)){
			portsql="AND ispdd = TRUE";
		}
		
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryPortKey");
			showPortWindow.show();
			portQry();
			if("0".equals(porttype)){
				
				Browser.execClientScript("polButton.focus");
				
			}else if("1".equals(porttype)){
				
				Browser.execClientScript("podButton.focus");
			}else if("2".equals(porttype)){
				
				Browser.execClientScript("pddButton.focus");
			}
			return;
		}
		
		try {
			List<Map> cs = portchooseserviceBean.findPort(qryPortKey,portsql);
			if (cs.size() == 1) {
				if("0".equals(porttype)){
					this.selectedRowData.setPolid((Long) cs.get(0).get("id"));
					
					this.selectedRowData.setPol((String)cs.get(0).get("namee"));
					this.update.markUpdate(UpdateLevel.Data, "polid");
					
					this.update.markUpdate(UpdateLevel.Data, "pol");
				}else if("1".equals(porttype)){
					
					this.selectedRowData.setPodid((Long) cs.get(0).get("id"));
					
					this.selectedRowData.setPod((String)cs.get(0).get("namee"));
					this.update.markUpdate(UpdateLevel.Data, "podid");
					
					this.update.markUpdate(UpdateLevel.Data, "pod");
					
				}else if("2".equals(porttype)){
					
					this.selectedRowData.setPddid((Long) cs.get(0).get("id"));
				
					this.selectedRowData.setPdd((String)cs.get(0).get("namee"));
					this.update.markUpdate(UpdateLevel.Data, "pddid");
					
					this.update.markUpdate(UpdateLevel.Data, "pdd");
					
				}
				showPortWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryPortKey");
				showPortWindow.show();
				portQry();
				
				if("0".equals(porttype)){
					Browser.execClientScript("polButton.focus");
					
				}else if("1".equals(porttype)){
					Browser.execClientScript("podButton.focus");
				}else if("2".equals(porttype)){
					Browser.execClientScript("pddButton.focus");
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void portGrid_ondblclick() {
		Object[] objs = portGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if("0".equals(porttype)){
			this.selectedRowData.setPolid((Long) m.get("id"));
		
			this.selectedRowData.setPol((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "polid");
			
			this.update.markUpdate(UpdateLevel.Data, "pol");
		}else if("1".equals(porttype)){
			
			this.selectedRowData.setPodid((Long) m.get("id"));
		
			this.selectedRowData.setPod((String) m.get("namee"));
			this.update.markUpdate(UpdateLevel.Data, "podid");
			
			this.update.markUpdate(UpdateLevel.Data, "pod");
			
		}else if("2".equals(porttype)){
			this.selectedRowData.setPddid((Long) m.get("id"));
			
			this.selectedRowData.setPdd((String) m.get("namee"));
			
			this.update.markUpdate(UpdateLevel.Data, "pddid");
		
			this.update.markUpdate(UpdateLevel.Data, "pdd");
		}
		showPortWindow.close();
	}
	
	
	@Bind(id = "gridShipschedule", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.shipcosteditBean.gridShipschedule.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();
				
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.shipcosteditBean.gridShipschedule.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap){
		return super.getQryClauseWhere(queryMap);
	}

	@Action
	public void clearQryKeysc() {
		if (qryMapShip != null) {
			qryMapShip.clear();
			update.markUpdate(true, UpdateLevel.Data, "shipschedulePanel");
			this.gridShipschedule.reload();
		}
	}
	
	
	@Action
	public void qryRefreshShip() {
		this.gridShipschedule.reload();
	}
	
	@Action
	public void chooseShip() {
		shipscheduleWindow.show();
		this.gridShipschedule.reload();
	}
	
	/**
	 * 审核
	 */
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		try {
			if (isCheck) {
				this.serviceContext.busShipjoinMgrService.updateCheck(new String[]{this.mPkVal + ""});
			}else {
				this.serviceContext.busShipjoinMgrService.updateCancelCheck(new String[]{this.mPkVal + ""});
			}
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			
		}
	}
	
	/**
	 * 到港
	 */
	@Action
	public void isarriveAjaxSubmit() {
		Boolean isArrive = selectedRowData.getIsarrive();
		if(this.mPkVal==-1l){
			if(this.selectedRowData.getAtd()==null &&isArrive ==true ){
				this.selectedRowData.setAta(new Date());
				update.markUpdate(true, UpdateLevel.Data, "ata");
			}
		}else{
		
		String updater=AppUtils.getUserSession().getUsername();
		try {
			String sql = "UPDATE bus_shipjoin SET isarrive = "+isArrive+"  WHERE id ="+this.mPkVal;
			this.serviceContext.busShipjoinMgrService.busShipjoinDao.executeSQL(sql);
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			
		}
		}
	}
	/**
	 * 已发邮件
	 */
	@Action
	public void isemailsendAjaxSubmit() {
		Boolean isemailsend = selectedRowData.getIsemailsend();
		if(this.mPkVal==-1l){
		}else{
			String updater=AppUtils.getUserSession().getUsername();
			try {
				String sql = "UPDATE bus_shipjoin SET isemailsend = "+isemailsend+"  WHERE id ="+this.mPkVal;
				this.serviceContext.busShipjoinMgrService.busShipjoinDao.executeSQL(sql);
				refreshMasterForm();
			}catch (Exception e) {
				MessageUtils.showException(e);
				update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			}
		}
	}
	
	
//	@Action
//	public void setShip(){
//		String[] ids = this.gridShipschedule.getSelectedIds();
//		if (ids == null || ids.length == 0) {
//			MsgUtil.alert("请先勾选要修改的行");
//			return;
//		}else if(ids.length > 1){
//			MsgUtil.alert("请选择单行记录");
//			return;
//		}else{
//		try {
//			this.selectedRowData.setScheduleid(Long.valueOf(ids[0]));
//			
//			setShipschedule();
//			
//			this.selectedRowData.setSchedule_year(this.shipschedule.getYearno());
//			this.selectedRowData.setSchedule_week(this.shipschedule.getWeekno());
//			this.selectedRowData.setCarrierid(getCarrierid(this.shipschedule.getCarrier()));
//			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
//			
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//			return;
//		}
//		}
//	}
	
	
	@Action
	public void gridShipschedule_ondblclick() {
		this.selectedRowData.setScheduleid(Long.valueOf(this.gridShipschedule.getSelectedIds()[0]));
		setShipschedule();
		//this.selectedRowData.set(this.shipschedule.getYearno());
		//this.selectedRowData.setScheduleMonth(this.shipschedule.getMonthno());
		//this.selectedRowData.setScheduleWeek(this.shipschedule.getWeekno());
		this.selectedRowData.setCarrierid(getCarrierid(this.shipschedule.getCarrier()));
		this.selectedRowData.setVessel(this.shipschedule.getVes());
		this.selectedRowData.setVoyage(this.shipschedule.getVoy());
		this.selectedRowData.setCls(this.shipschedule.getCls());
		this.selectedRowData.setEtd(this.shipschedule.getEtd());
		this.selectedRowData.setEta(this.shipschedule.getEta());
		//代码都不设置
		if(getPolorPodId(this.shipschedule.getPol())==null){
			
		}else{
			this.selectedRowData.setPol(this.shipschedule.getPol());
			this.selectedRowData.setPolid(getPolorPodId(this.shipschedule.getPol()));
		}
		if(getPolorPodId(this.shipschedule.getPod())==null){
			
		}else{
			this.selectedRowData.setPod(this.shipschedule.getPod());
			this.selectedRowData.setPodid(getPolorPodId(this.shipschedule.getPod()));
			this.selectedRowData.setPdd(this.shipschedule.getPod());
			this.selectedRowData.setPddid(getPolorPodId(this.shipschedule.getPod()));
		}
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		shipscheduleWindow.close();
	}
	
	private Long getCarrierid(String carriercode) {
		
		try {
			String sql = "SELECT id FROM sys_corporation WHERE abbr = '"
					+ carriercode
					+ "' AND iscarrier = true AND isdelete = FALSE";
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			Long carrierid = (Long) m.get("id");
			
			return carrierid;
			
		} catch (Exception e) {
			MessageUtils.alert("没找到对应的船公司id,请手动下拉");
			return null;
		}
		
		
	}

	public void setShipschedule(){
		if(this.selectedRowData.getScheduleid()==null){
			shipschedule = new BusShipSchedule();
		}else{
			shipschedule = serviceContext.shipScheduleService.busShipScheduleDao.findById(selectedRowData.getScheduleid());
		}
		
		
	}
	
	
	/**
	 * @param data
	 * @return
	 */
	public Long getPolorPodId(String data){
		String qry="";
		if(StrUtils.isNull(data)){
			return null;
		}else{
			
			qry = "SELECT id FROM dat_port WHERE namee = '"+data+"' AND isdelete = FALSE AND isship = TRUE";
		}
		
		try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(qry);
			Long id = (Long) map.get("id");
			//ApplicationUtils.debug("id="+id);
			return id;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind
	private String qryCustomerKey;

	/**
	 * 
	 */
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider(sql);
	}

	@Action
	public void showCustomer() {
		String customercode = (String) AppUtils.getReqParam("customercode");
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");

		custype = (String) AppUtils.getReqParam("custype");
		
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			customerQry();
			//收货人
			if ("0".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'C' AND customerid = "+this.customerid;
				Browser.execClientScript("cneename.focus");
			
			//发货人
			} else if ("1".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'S' AND customerid = "+this.customerid;
				Browser.execClientScript("cnorname.focus");
			//通知人
			} else if ("2".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'N' AND customerid = "+this.customerid;
				Browser.execClientScript("notifyname.focus");
			//hbl代理
			} else if ("3".equals(custype)) {
				sql = " AND contactype = 'B' AND contactype2 = 'A' AND customerid = "+this.customerid;
				Browser.execClientScript("agenname.focus");

			}

			return;
		}

		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
				if ("0".equals(custype)) {
					this.selectedRowData.setCneeid((Long) cs.get(0).get("id"));
					this.selectedRowData.setCneename((String)cs.get(0).get("name"));
					this.selectedRowData.setCneetitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "cneeid");
					this.update.markUpdate(UpdateLevel.Data, "cneename");
					this.update.markUpdate(UpdateLevel.Data, "cneetitle");
				} else if ("1".equals(custype)) {

					this.selectedRowData.setCnorid((Long) cs.get(0).get("id"));
					this.selectedRowData.setCnorname((String)cs.get(0).get("name"));
					this.selectedRowData.setCnortitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "cnorid");
					this.update.markUpdate(UpdateLevel.Data, "cnorname");
					this.update.markUpdate(UpdateLevel.Data, "cnortitle");

				} else if ("2".equals(custype)) {

					this.selectedRowData
							.setNotifyid((Long) cs.get(0).get("id"));
					this.selectedRowData.setNotifyname((String)cs.get(0).get("name"));
					this.selectedRowData.setNotifytitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "notifyid");
					this.update.markUpdate(UpdateLevel.Data, "notifyname");
					this.update.markUpdate(UpdateLevel.Data, "notifytitle");

				} else if ("3".equals(custype)) {

//					this.selectedRowData.setAgenid((Long) cs.get(0).get("id"));
//					this.selectedRowData.setAgenname((String)cs.get(0).get("name"));;
//					this.selectedRowData.setAgentitle((String) cs.get(0).get(
//							"contactxt"));
//					this.update.markUpdate(UpdateLevel.Data, "agenid");
//					this.update.markUpdate(UpdateLevel.Data, "agenname");
//					this.update.markUpdate(UpdateLevel.Data, "agentitle");
				}
				showCustomerWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
				showCustomerWindow.show();
				customerQry();

				if ("0".equals(custype)) {
					Browser.execClientScript("cneename.focus");

				} else if ("1".equals(custype)) {
					Browser.execClientScript("cnorname.focus");
				} else if ("2".equals(custype)) {
					Browser.execClientScript("notifyname.focus");
				} else if ("3".equals(custype)) {
					Browser.execClientScript("agenname.focus");
				}

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
		if ("0".equals(custype)) {
			this.selectedRowData.setCneeid((Long) m.get("id"));
			this.selectedRowData.setCneename((String) m.get("name"));
			this.selectedRowData.setCneetitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cneeid");
			this.update.markUpdate(UpdateLevel.Data, "cneename");
			this.update.markUpdate(UpdateLevel.Data, "cneetitle");
		} else if ("1".equals(custype)) {

			this.selectedRowData.setCnorid((Long) m.get("id"));
			this.selectedRowData.setCnorname((String) m.get("name"));
			this.selectedRowData.setCnortitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cnorid");
			this.update.markUpdate(UpdateLevel.Data, "cnorname");
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");

		} else if ("2".equals(custype)) {
			this.selectedRowData.setNotifyid((Long) m.get("id"));
			this.selectedRowData.setNotifyname((String) m.get("name"));
			this.selectedRowData.setNotifytitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifyid");
			this.update.markUpdate(UpdateLevel.Data, "notifyid");
			this.update.markUpdate(UpdateLevel.Data, "notifyname");
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");
		} else if ("3".equals(custype)) {
			

		}
		showCustomerWindow.close();
	}
	
	
	
	
	
	
	@Action
	public void savecne() {
		String cneid = AppUtils.getReqParam("cneid").trim();
		String cnename = AppUtils.getReqParam("cnename").trim();
		String cnetitle = AppUtils.getReqParam("cnetitle").trim();
		String type = AppUtils.getReqParam("type").trim();

		try {
			// cneid 为空,说明是新增的用户 ,不为空 update
			if (StrUtils.isNull(cneid) && StrUtils.isNull(cnetitle)) {
				MessageUtils.alert("请输入正确信息");

			} else {
				if (type.equals("0")) {
				
				//保存收货人
				if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
					sysCorp = new SysCorpcontacts();
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerid(this.customerid);
					sysCorp.setContactype("B");
					sysCorp.setContactype2("C");
					String code = (StrUtils.isNull(cnename) ? (this.abbr
							+ "-C-" + getCusCode("1")) : cnename);
					sysCorp.setName(StrUtils.isNull(cnename) ? code
							: cnename);
					sysCorp.setCustomerabbr(this.abbr);
					sysCorp.setId(0);
					sysCorp.setSex("M");
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");
					selectedRowData.setCneeid(Long
							.valueOf(getCusdesc(code)[0]));
					selectedRowData.setCneename(code);
					update.markUpdate(true, UpdateLevel.Data,
							"masterEditPanel");

				} else if (!StrUtils.isNull(cneid)
						&& !StrUtils.isNull(cnetitle)) {
					sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
							.findById(Long.valueOf(cneid));
					sysCorp.setName(cnename);
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerabbr(this.abbr);
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");

				}
					//发货人
				} else if (type.equals("1")) {

					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("S");
						String code = (StrUtils.isNull(cnename) ? (this.abbr
								+ "-S-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.abbr);
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setCnorid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setCnorname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.abbr);
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}
					//hbl代理
				} else if (type.equals("2")) {
					
					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.abbr);
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}
					//通知人
				} else if (type.equals("3")) {
					
					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("N");
						String code = (StrUtils.isNull(cnename) ? (this.abbr
								+ "-N-" + getCusCode("3")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.abbr);
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setNotifyid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setNotifyname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.abbr);
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}
				}

			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	public Long getCusCode(String type) {
		String sql = "";
		if (type.equals("0")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'C' AND customerid = "+this.customerid;
		} else if (type.equals("1")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'S' AND customerid = "+this.customerid;
		} else if (type.equals("2")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'A' AND customerid = "+this.customerid;
		} else if(type.equals("3")){
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'N' AND customerid = "+this.customerid;
		}

		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("count");
	}

	public String[] getCusdesc(String code) {
		String[] re = new String[2];
		try {
			String sql = "SELECT name,id FROM sys_corpcontacts WHERE  name ='"+code+"' AND customerid = "+this.customerid+" LIMIT 1;";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			re[0] = ((Long) m.get("id")).toString();
			re[1] = (String) m.get("name");
		} catch (Exception e) {
			re[0] = "0";
			re[1] = "";
		}
		return re;

	}
	
	/**
	 * 设置customerid 为总公司id
	 */
	
	public void setOfficeheader(){
		
		String sql = "SELECT id ,abbr FROM sys_corporation WHERE iscustomer = FALSE AND parentid IS NULL AND isdelete = FALSE;";
		try {
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.customerid = (Long) m.get("id");
			this.abbr = (String) m.get("abbr");
		} catch (MoreThanOneRowException e) {
			MessageUtils.showException(e);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	@Action
	public void shipManifest(){
		
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/shipunioninfo.raq&id="+this.mPkVal;
		AppUtils.openWindow("_shipjoinReport", openUrl);
	}

	@Action
	public void packinglist_mbl(){
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/packinglist_mbl_shipunioninfo.raq&id="+this.mPkVal;
		AppUtils.openWindow("_shipjoinReport", openUrl);
	}

	
	@Bind
	public UIWindow showManifestWindow;
		
	@Bind
	@SaveState
	public String manifestmsgContent;
	
	@Action
	public void ediManifest(){
		this.selectedRowData.getEta();
		String sql = "SELECT f_edi_manifest('type=busshipjoin;id="+this.mPkVal+"') AS edi;";
		Map m;
		try {
			m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.manifestmsgContent = m.get("edi").toString();
			this.update.markUpdate(UpdateLevel.Data, "manifestmsgContent");
			showManifestWindow.show();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void syncMaster(){
		try {
			if(selectedRowData.getId() < 0){
				this.alert("Please save first!");
				return;
			}
			String sql = "SELECT f_bus_shipunion_sync('shipjoinid="+selectedRowData.getId()+"')";
			Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void outputMANIFEST(){
		this.fileName = "MFU_" + this.mPkVal + ".txt";
		this.contentType = "text/plain";
	}
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() throws Exception {
//		String newStr = new String(this.manifestmsgContent.getBytes(), " GB2312");
//		InputStream input = new ByteArrayInputStream(newStr.getBytes());
		InputStream input = new ByteArrayInputStream(this.manifestmsgContent.getBytes());
		return input;
    }
	
	@Action
	public void toPodNotify(){
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/podNotify.raq&id="+this.mPkVal;
		AppUtils.openWindow("_podNotifyReport", openUrl);
	}
	
	@Bind
	@SaveState
	public String detailsContent;
	
	@Bind
	@SaveState
	public String detailsTitle;
	
	@SaveState
	public String type;
	
	@Bind
	public UIWindow detailsWindow;
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");
		this.detailsWindow.show();
		
		if("1".equals(type)) { // 发货人大框
			this.detailsContent = content;
			this.detailsTitle = "发货人";
			
		} else if("2".equals(type)) { // 收货人大框
			this.detailsContent = content;
			this.detailsTitle = "收货人";
			
		} else if("3".equals(type)) { // 通知人大框
			this.detailsContent = content;
			this.detailsTitle = "通知人";
			
		} 
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
	}
	
	/**
	 * 输入框(大框)保存
	 */
	@Action
	public void saveDetails() {
		setDetails(this.type);
		this.saveMaster();
		
		this.detailsWindow.close();
	}
	
	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.type);
		
		this.detailsWindow.close();
	}
	
	public void setDetails(String type) {
		if("1".equals(type)) { // 发货人大框
			this.selectedRowData.setCnortitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");
			
		} else if("2".equals(type)) { // 收货人大框
			this.selectedRowData.setCneetitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "cneetitle");
			
		} else if("3".equals(type)) { // 通知人大框
			this.selectedRowData.setNotifytitle(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifytitle");
		}
	}
	
	@Action
	public void impTemplet() {
//		templetFrame.load("../other/feetemplatechooser.xhtml?jobid="+this.jobid);
//		showTempletWindow.show();
		
//		impFee(this.selectedRowData.getId());
		String winId = "_arapTemplet";
		String url = "../other/feetemplatechooser.xhtml?type=join&joinids="+this.selectedRowData.getId(); 
//		int width = 980;
//		int height = 600;
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void copyGetinfo(){
		if(this.selectedRowData==null){
			alert("请先保存");
		}
		try {
			String sqls = "SELECT f_bus_shipunion_getinfo('shipjoinid="
					+ this.selectedRowData.getId() + "')";
			serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sqls);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refreshMasterForm();
	}
	
	@Bind
	public UIIFrame emailIframe;
	
	@Bind
	public UIWindow showEmailWindow;
	
	/**
	 * 显示邮件页面
	 */
	@Action
	public void showEmail() {
		emailIframe.load("/scp/pages/sysmgr/mail/emailedit.aspx?type=union&id=-1&jobid="+this.selectedRowData.getId());
		showEmailWindow.show();
	}
	
	@Bind
	private String linecode;
	
	@Bind
	private String voyageagentcode;
	
	@Action
	public void saveaetchcode(){
		try {
			ConfigUtils.refreshSysCfg("edi_manifest_linecode", linecode, AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshSysCfg("edi_manifest_voyageagentcode", voyageagentcode, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void aetchcode(){
		linecode = ConfigUtils.findSysCfgVal("edi_manifest_linecode");
		voyageagentcode = ConfigUtils.findSysCfgVal("edi_manifest_voyageagentcode");
		update.markUpdate(true, UpdateLevel.Data, "linecode");
		update.markUpdate(true, UpdateLevel.Data, "voyageagentcode");
		Browser.execClientScript("aetchcodeWindow.show()");
	}




	//生成并单编号开始

	@Bind
	public UIWindow setnosWindow;

	@Action
	public void creatnos() {
		this.nosgrid.reload();
		setnosWindow.show();
	}

	@Bind
	public UIDataGrid nosgrid;

	@SaveState
	@Accessible
	public Map<String, Object> bookqryMap = new HashMap<String, Object>();

	public Map getQryClauseWhereBook(Map<String, Object> queryMap) {
		Map map = new HashMap();
		String qry = "\n 1=1";
		map.put("qry", qry);
		return map;
	}

	@Bind(id = "nosgrid", attribute = "dataProvider")
	protected GridDataProvider getBookDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".nosgrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhereBook(bookqryMap)).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".nosgrid.count";
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
	public String codes = "bus_shipjoin_nos";

	@Action
	public void choosenos(){
		String[] ids = this.nosgrid.getSelectedIds();
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
		Date sodate = selectedRowData.getSingtime();
		if(sodate == null){
			alert("并单日期不能为空");
			setnosWindow.close();
			return;
		}
		try {
			SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd");
			String soDateStr = dayformat.format(sodate);
			String querySqlw = "SELECT f_auto_busno('code="+codes+";date="+soDateStr+";corpid=100"+"') AS nos;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySqlw);
			String nos = StrUtils.getMapVal(map, "nos");
			Browser.execClientScript("nosJsVar.setValue('"+nos+"')");
			setnosWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void nosgrid_ondblclick() {
		choosenos();
	}

	//生成并单编号结束
}
