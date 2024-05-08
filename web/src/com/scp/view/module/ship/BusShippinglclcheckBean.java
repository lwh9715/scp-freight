package com.scp.view.module.ship;

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
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.MoreThanOneRowException;
import com.scp.model.bus.BusShipSchedule;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.scp.view.module.data.PortChooseService;

@ManagedBean(name = "pages.module.ship.busshippinglclcheckBean", scope = ManagedBeanScope.REQUEST)
public class BusShippinglclcheckBean extends MastDtlView {

	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;

	@SaveState
	@Accessible
	public BusShipping selectedRowData = new BusShipping();
	
	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();

	@SaveState
	@Accessible
	public Long jobid;
	
	@SaveState
	@Accessible
	public Long customerid;
	
	@SaveState
	@Accessible
	public String custype;

	@SaveState
	@Accessible
	public String sql = "AND 1=1";
	
	@SaveState
	@Accessible
	public FinaJobs job = new FinaJobs();
	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	
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
	@Accessible
	public String wmsinfilename = "BG_GZ";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id").trim();
			jobid = Long.valueOf(id);
			this.refreshMasterForm();
			refresh();
		}
	}
	
	@Override
	public void init() {
		String id = AppUtils.getReqParam("id").trim();
		jobid = Long.valueOf(id);
		this.job = serviceContext.jobsMgrService.finaJobsDao
		.findById(this.jobid);
		this.customerid=job.getCustomerid();
		
	}


	
	@Override
	public void add(){
		dtlData =  new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setLdtype(selectedRowData.getLdtype());
		this.dtlData.setShipid(this.selectedRowData.getId());
		super.add();
	}
	
	@Override
	public void addMaster() {
		this.selectedRowData = new BusShipping();
		this.mPkVal = -1l;
		super.addMaster();
	}
	

	@Override
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			serviceContext.busShippingMgrService.removeDate(selectedRowData
					.getId());
			
			this.addMaster();
			refreshMasterForm();
			this.alert("OK");
		}
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		
		try {
			serviceContext.busShipContainerMgrService.removeDate(ids);
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao.findById(this.pkVal);
	}

	/*
	 * 保存新增
	 */
	@Override
	protected void doServiceSave() {
		serviceContext.busShipContainerMgrService.saveData(dtlData);
		refresh();
		this.add();
	}
	
	/**
	 * 保存关闭
	 */
	@Action
	protected void save2() {
		serviceContext.busShipContainerMgrService.saveData(dtlData);
		refresh();
		this.editWindow.close();
	}
	
	@Bind(id = "wmsinformat")
	public List<SelectItem> getWmsinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.code", "d.namec ",
					"sys_report AS d", "WHERE modcode='shipping' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	/**
	 * 预览报表 --没有控制
	 */
	@Action
	public void scanReport() {

		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"
				+ this.wmsinfilename;
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}
	
	/**
	 * Neo 2014/5/7
	 * 延船
	 *//*
	@Action
	public void delayShip() {
		String winId = "_ship_delay";
		String url = "./busshipdelay.xhtml?id=" + this.mPkVal;
		AppUtil.openWindow(winId, url);
	}
	*//**
	 * Neo 2014/5/8
	 * 甩柜
	 *//*
	@Action
	public void dropCnt() {
		String winId = "_drop_cnt";
		String url = "./busshipdropcnt.xhtml?id=" + this.mPkVal;
		AppUtil.openWindow(winId, url);
	}*/

	
	@Override
	public void refresh() {
		super.refresh();
		refreshMasterForm();
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.busShippingMgrService.saveData(selectedRowData);
		this.pkVal = selectedRowData.getId();
		alert("OK");
		refresh();
	}

	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND jobid = " + jobid;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void refreshMasterForm() {
		
		try {
			String sql = " isdelete = false AND jobid =" + this.jobid;
			this.selectedRowData = serviceContext.busShippingMgrService.busShippingDao
					.findOneRowByClauseWhere(sql);
			
		} catch (MoreThanOneRowException e) {
			selectedRowData = new BusShipping();
			selectedRowData.setJobid(this.jobid);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			this.mPkVal=this.selectedRowData.getId();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			this.grid.reload();
		}
	}
	
	
	@SaveState
	@Accessible
	public String portsql="AND 1=1";
	
	
	
	
	@SaveState
	@Accessible
	public String porttype;
	
	
	@Bind
	public UIWindow showPortWindow;

	@Bind
	public UIDataGrid portGrid;
	
	@Bind
	private String qryPortKey;
	
	@ManagedProperty("#{portchooseserviceBean}")
	private PortChooseService portchooseserviceBean;
	
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
	
//	@Action
//	public void chooseBook() {
//		
//		String[] ids = this.grid.getSelectedIds();
//		if(ids==null||ids.length==0||ids.length>1){
//			MsgUtil.alert("请选择单行记录");
//			return;
//		}
//		String url = AppUtil.getContextPath() + "/pages/module/ship/shipbookingchoose.xhtml?containerid="+ids[0]+"&shipid="+this.mPkVal;
//		dtlIFrame.setSrc(url);
//		update.markAttributeUpdate(dtlIFrame, "src");
//		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
//		dtlDialog.show();
//	}
	
	
	@Action
	public void chooseBook() {
		
		
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipbookingchoose.xhtml?shipid="+this.mPkVal;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
	
	@Action
	public void cancelBook() {
		
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0||ids.length<1){
			MessageUtils.alert("请勾选行");
			return;
		}
		try {
			serviceContext.busBookingMgrService.cancelBook(ids, AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void returnCus() {
		
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0||ids.length<1){
			MessageUtils.alert("请勾选行");
			return;
		}
		try {
			serviceContext.busBookingMgrService.returnCus(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 发送邮件
	 * neo 2014-04-11
	 */
	@Action
	public void sendMail() {
		String url = AppUtils.getContextPath() + "/pages/sysmgr/mail/emailsendedit.xhtml?type=shipping&id="+this.mPkVal;
		AppUtils.openWindow("_sendMail_shipping", url);
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
	public void qryRefreshShip2() {
		this.gridShipschedule.reload();
	}
	
	@Action
	public void chooseShip() {
		shipscheduleWindow.show();
		this.gridShipschedule.reload();
	}
	

	
	@Action
	public void gridShipschedule_ondblclick() {
		this.selectedRowData.setScheduleid(Long.valueOf(this.gridShipschedule.getSelectedIds()[0]));
		setShipschedule();
		this.selectedRowData.setScheduleYear(this.shipschedule.getYearno());
		this.selectedRowData.setScheduleMonth(this.shipschedule.getMonthno());
		this.selectedRowData.setScheduleWeek(this.shipschedule.getWeekno());
		this.selectedRowData.setCarrierid(getCarrierid(this.shipschedule.getCarrier()));
		this.selectedRowData.setVessel(this.shipschedule.getVes());
		this.selectedRowData.setVoyage(this.shipschedule.getVoy());
		this.selectedRowData.setCls(this.shipschedule.getCls());
		this.selectedRowData.setEtd(this.shipschedule.getEtd());
		this.selectedRowData.setEta(this.shipschedule.getEta());
		this.selectedRowData.setPol(this.shipschedule.getPol());
		this.selectedRowData.setPolid(getPolorPodId(this.shipschedule.getPol()));
		this.selectedRowData.setPod(this.shipschedule.getPod());
		this.selectedRowData.setPodid(getPolorPodId(this.shipschedule.getPod()));
		if(StrUtils.isNull(this.selectedRowData.getPdd())){
			this.selectedRowData.setPdd(this.selectedRowData.getPod());
			this.selectedRowData.setPddid(this.selectedRowData.getPodid());
		}else{
			
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
			
			qry = "SELECT id FROM dat_port WHERE code = "+data+" AND isdelete = FALSE AND isship = TRUE";
		}
		
		try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(qry);
			String id = (String) map.get("id");
			return Long.valueOf(id);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	
	
	
	@Action
	public void showReport() {
		
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/ship/booking.raq";
		AppUtils.openWindow("_ship_bookink", openUrl + getArgs());
	}

	

	private String getArgs() {
		String args="";
		args+="&id="+this.selectedRowData.getJobid();
		return args;
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

					this.selectedRowData.setAgenid((Long) cs.get(0).get("id"));
					this.selectedRowData.setAgenname((String)cs.get(0).get("name"));;
					this.selectedRowData.setAgentitle((String) cs.get(0).get(
							"contactxt"));
					this.update.markUpdate(UpdateLevel.Data, "agenid");
					this.update.markUpdate(UpdateLevel.Data, "agenname");
					this.update.markUpdate(UpdateLevel.Data, "agentitle");
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
			this.selectedRowData.setAgenid((Long) m.get("id"));
			this.selectedRowData.setAgenname((String) m.get("name"));
			this.selectedRowData.setAgentitle((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "agenid");
			this.update.markUpdate(UpdateLevel.Data, "agenname");
			this.update.markUpdate(UpdateLevel.Data, "agentitle");

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
					String code = (StrUtils.isNull(cnename) ? (this.job
							.getCustomerabbr()
							+ "-C-" + getCusCode("1")) : cnename);
					sysCorp.setName(StrUtils.isNull(cnename) ? code
							: cnename);
					sysCorp.setCustomerabbr(this.job.getCustomerabbr());
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
					sysCorp.setCustomerabbr(this.job.getCustomerabbr());
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
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-S-" + getCusCode("1")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
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
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}
					//hbl代理
				} else if (type.equals("2")) {
					
					if (StrUtils.isNull(cneid) && !StrUtils.isNull(cnetitle)) {
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("A");
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-A-" + getCusCode("2")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						selectedRowData.setAgenid(Long
								.valueOf(getCusdesc(code)[0]));
						selectedRowData.setAgenname(code);
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");

					} else if (!StrUtils.isNull(cneid)
							&& !StrUtils.isNull(cnetitle)) {
						sysCorp = serviceContext.customerContactsMgrService.sysCorpcontactsDao
								.findById(Long.valueOf(cneid));
						sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
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
						String code = (StrUtils.isNull(cnename) ? (this.job
								.getCustomerabbr()
								+ "-N-" + getCusCode("3")) : cnename);
						sysCorp.setName(StrUtils.isNull(cnename) ? code
								: cnename);
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
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
						sysCorp.setCustomerabbr(this.job.getCustomerabbr());
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
	
	
	@Bind(id="sonodesc")
    public List<SelectItem> getSonodesc() {
		try {
			//ApplicationUtils.debug(this.dtlData.getBookdtlid());
			return CommonComBoxBean.getComboxItems("d.dtlid","d.bookno","_bus_booking_choose AS d","WHERE ( d.dtlid = "+this.dtlData.getBookdtlid()+" OR (d.bookstate= 'A' OR d.bookstate= 'I'))","ORDER BY d.bookno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
//	
//	//更新舱位  是一个grid,加载缓慢 换掉
//	
//	
//	@Bind
//	public UIWindow bookWindow;
//
//	@Bind
//	public UIDataGrid bookgrid;
//	
//
//	
//	/**
//	 * 页面对应的queryMap
//	 */
//	@SaveState
//	@Accessible
//	public Map<String, Object> qrybookMap = new HashMap<String, Object>();
//	
//	@Bind(id = "bookgrid", attribute = "dataProvider")
//	protected GridDataProvider getBookgridDataProvider() {
//		return new GridDataProvider() {
//
//			@Override
//			public Object[] getElements() {
//				String sqlId = "pages.module.ship.busshippingBean.gridbooking.page";
//				return serviceContext.daoIbatisTemplate
//						.getSqlMapClientTemplate().queryForList(sqlId,
//								getQryClauseWhere3(qrybookMap), start, limit)
//						.toArray();
//				
//			}
//
//			@Override
//			public int getTotalCount() {
//				String sqlId = "pages.module.ship.busshippingBean.gridbooking.count";
//				List<Map> list = serviceContext.daoIbatisTemplate
//						.getSqlMapClientTemplate().queryForList(sqlId,
//								getQryClauseWhere3(qrybookMap));
//				Long count = (Long) list.get(0).get("counts");
//				return count.intValue();
//			}
//		};
//	}
//	
//	
//	public Map getQryClauseWhere3(Map<String, Object> queryMap){
//		Map m = super.getQryClauseWhere(queryMap);
//		String qry2="\n userid_assign ="+AppUtil.getUserSession().getUserid();
//		String qry3="\n userid_assign <> "+AppUtil.getUserSession().getUserid();
//		m.put("qry2", qry2);
//		m.put("qry3", qry3);
//		return m;
//	}
//
//	@Action
//	public void clearQryKeybook() {
//		if (qrybookMap != null) {
//			qrybookMap.clear();
//			update.markUpdate(true, UpdateLevel.Data, "bookPanel");
//			this.bookgrid.reload();
//		}
//	}
//	
//	
//	@Action
//	public void qryRefreshBook() {
//		this.bookgrid.reload();
//	}
//	
//	@Action
//	public void qryRefreshBook2() {
//		this.bookgrid.reload();
//	}
//	
//	
//	
//	@Action
//	public void upsono() {
//		bookWindow.show();
//		
//		this.qrybookMap.put("vessel", this.selectedRowData.getVessel());
//		this.qrybookMap.put("voyage", this.selectedRowData.getVoyage());
//		this.qrybookMap.put("carrierdesc",getCarrierdesc(this.selectedRowData.getCarrierid()));
//		this.qrybookMap.put("scheduleyear$",this.selectedRowData.getScheduleYear());
//		this.qrybookMap.put("schedulemonth$", this.selectedRowData.getScheduleMonth());
//		update.markUpdate(true, UpdateLevel.Data, "bookPanel");	
//		this.bookgrid.reload();
//	}
//	
//
//	
//	@Action
//	public void bookgrid_ondblclick() {
//		String id = this.bookgrid.getSelectedIds()[0];
//		
//		this.dtlData.setBookdtlid(Long.valueOf(id));
//		this.dtlData.setSono(getSono(Long.valueOf(id)));
//		update.markUpdate(true, UpdateLevel.Data, "sono");	
//		bookWindow.close();
//	}
//	
//	
//	private String getSono(Long bookid) {
//		String sql ="SELECT bookno FROM bus_ship_bookdtl c  WHERE  c.id="+bookid;
//		Map m;
//		try {
//			m = serviceContext.daoIbatisTemplate
//					.queryWithUserDefineSql4OnwRow(sql);
//			return ((String)m.get("bookno")).trim();
//		} catch (MoreThanOneRowException e) {
//			return "";
//		}
//	}
//
//	public String getCarrierdesc(Long id){
//		String sql ="SELECT COALESCE(c.abbr,'')||'/'||c.code AS carrierdesc FROM sys_corporation c  WHERE  c.id="+id;
//		Map m;
//		try {
//			m = serviceContext.daoIbatisTemplate
//					.queryWithUserDefineSql4OnwRow(sql);
//			return ((String)m.get("carrierdesc")).trim();
//		} catch (MoreThanOneRowException e) {
//			return "";
//		}
//		
//	}
}
