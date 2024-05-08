package com.scp.view.module.ship;

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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bus.BusShipSchedule;
import com.scp.model.ship.BusShipBookdtl;
import com.scp.model.ship.BusShipBooking;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.data.PortChooseService;

@ManagedBean(name = "pages.module.ship.bookingeditBean", scope = ManagedBeanScope.REQUEST)
public class BusBookingEditBean extends MastDtlView {
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	
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
	public BusShipBooking selectedRowData = new BusShipBooking();
	
	@SaveState
	@Accessible
	public BusShipBookdtl ddata = new BusShipBookdtl();
	
	@Bind
	public UIWindow shipscheduleWindow;

	@Bind
	public UIDataGrid gridShipschedule;
	

	@SaveState
	@Accessible
	public BusShipSchedule shipschedule = new BusShipSchedule();

	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapSysuser = new HashMap<String, Object>();
	
	@Bind
	public UIWindow assignWindow;
	
	@Bind
	public UIDataGrid sysusergrid;

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
	public void init() {
		selectedRowData = new BusShipBooking();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else{
			addMaster();
		}
		//this.qryMap.put("bookingid$", this.mPkVal);
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.busBookingMgrService.busShipBookingDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.busBookingMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");
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
		this.selectedRowData = new BusShipBooking();
		//selectedRowData.setSingtime(new Date());
		this.mPkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
	}

	@Override
	public void delMaster() {
		try {
			serviceContext.busBookingMgrService.removeDate(this.mPkVal,AppUtils.getUserSession().getUsercode());
			this.addMaster();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
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
			serviceContext.busBookingMgrService.removedtlDate(ids,AppUtils.getUserSession().getUsercode());
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}

	
	@Override
	protected void doServiceFindData() {
		ddata = serviceContext.busBookingMgrService.busShipBookdtlDao.findById(this.pkVal);
	}
	
	
	

	@Override
	protected void doServiceSave() {
		this.ddata.setBookingid(this.mPkVal);
		serviceContext.busBookingMgrService.saveDataDtl(ddata);
		this.refresh();
		this.alert("OK");
	}
	
	
	@Override
	public void add() {
		this.ddata = new BusShipBookdtl();
		if(this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据！");
			return;
		}
		this.ddata.setBookingid(this.mPkVal);
		this.ddata.setBookstate("I");
		super.add();
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
		}
		showPortWindow.close();
	}
	
	
	
	@Bind
	public UIButton importData;
	
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
				String callFunction = "f_imp_ship_booking";
				String args = this.mPkVal + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
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
	
	@Bind(id = "sysusergrid", attribute = "dataProvider")
	protected GridDataProvider getSysusergridDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.bookingeditBean.sysusergrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapSysuser), start, limit)
						.toArray();
				
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.bookingeditBean.sysusergrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapSysuser));
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
	public void clearUserQryKey() {
		if (this.qryMapSysuser != null) {
			qryMapSysuser.clear();
			update.markUpdate(true, UpdateLevel.Data, "assignPanel");
			this.sysusergrid.reload();
		}
	}
	
	@Action
	public void qryRefreshUser() {
		this.sysusergrid.reload();
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
	
	@SaveState 
	private String[] ids; 
	/**
	 * @author Neo 
	 * 2014/5/5
	 * 分配
	 */
	@Action
	public void assign() {
		this.ids = this.grid.getSelectedIds(); 
		if(ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		if(this.assignWindow != null) this.assignWindow.show();
		this.sysusergrid.reload();
	}
	
	@Action
	public void sysusergrid_ondblclick() {
		String id = this.sysusergrid.getSelectedIds()[0];
		if(StrUtils.isNull(id)) {
			return;
		}
		this.serviceContext.busBookingMgrService.assignBooking(this.ids, id);
		this.refreshMasterForm();
		this.refresh();
		this.assignWindow.close();
	}
	
	/**
	 * @author Neo
	 * 2014/5/5
	 * 收回
	 */
	@Action
	public void withdraw() {
		String[] ids = this.grid.getSelectedIds(); 
		if(ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		this.serviceContext.busBookingMgrService.withdrawBooking(ids);
		this.refreshMasterForm();
		this.refresh();
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
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND bookingid =" + this.mPkVal;
		m.put("qry", qry);
		return m;
	}
	
	@Action
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}

	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 1);
	}*/
}
