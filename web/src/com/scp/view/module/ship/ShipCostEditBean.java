package com.scp.view.module.ship;

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
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bus.BusShipSchedule;
import com.scp.model.ship.BusShipCost;
import com.scp.model.ship.BusShipCostdtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.ship.shipcosteditBean", scope = ManagedBeanScope.REQUEST)
public class ShipCostEditBean extends MastDtlView {

	@Bind
	public UIWindow shipscheduleWindow;

	@Bind
	public UIDataGrid gridShipschedule;

	

	@SaveState
	@Accessible
	public BusShipCost selectedRowData = new BusShipCost();

	@SaveState
	@Accessible
	public BusShipCostdtl ddata = new BusShipCostdtl();

	@SaveState
	@Accessible
	public BusShipSchedule shipschedule = new BusShipSchedule();

	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}

	@Override
	public void refreshForm() {
		super.refreshForm();

	}

	@Override
	public void grid_ondblclick() {

		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		if (editWindow != null)
			editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND costid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}
	
	
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap){
		return super.getQryClauseWhere(queryMap);
	}
	
	

	@Override
	public void init() {
		selectedRowData = new BusShipCost();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh(); // 从表也刷新

		} else {
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.busShipCostMgrService.busShipCostDao
				.findById(this.mPkVal);
		setShipschedule();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = serviceContext.busShipCostdtlMgrService.busShipCostdtlDao
				.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		//AppUtils.debug(this.ddata.getCntypeid());
		serviceContext.busShipCostdtlMgrService.saveDtlData(this.ddata);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		this.alert("OK");
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.busShipCostMgrService.saveMasterData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");
	}

	@Override
	public void add() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			ddata = new BusShipCostdtl();
			ddata.setCostid(this.mPkVal);
			super.add();

		}
	}

	@Override
	public void addMaster() {
		this.selectedRowData = new BusShipCost();
		this.selectedRowData.setPricetype("CIF");
		this.mPkVal = -1l;
		setShipschedule();
		super.addMaster();
		
	}

	@Action
	public void delMaster() {
		if (this.mPkVal == -1l) {
			this.addMaster();
		} else {

			try {
				serviceContext.busShipCostMgrService.removeDate(this.mPkVal,
						AppUtils.getUserSession().getUsercode(), new Date());
				MessageUtils.alert("OK");
				this.addMaster();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}

		}
	}

	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.busShipCostdtlMgrService.delBatch(ids, AppUtils
					.getUserSession().getUsercode(), new Date());
			MessageUtils.alert("删除成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
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
//			doServiceSaveMaster();
//			
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//			return;
//		}
//		}
//	}
//	
	
	@Action
	public void gridShipschedule_ondblclick() {
		this.selectedRowData.setScheduleid(Long.valueOf(this.gridShipschedule.getSelectedIds()[0]));
		
//		String tt = serviceContext.shipScheduleService.busShipScheduleDao.findById(Long.valueOf(this.gridShipschedule.getSelectedIds()[0])).getTt();
//		this.selectedRowData.setTt(tt);
//		ApplicationUtils.debug(tt);
		setShipschedule();
		this.selectedRowData.setTt(this.shipschedule.getTt());
		shipscheduleWindow.close();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	}
	
	
	
	public void setShipschedule(){
		if(this.selectedRowData.getScheduleid()==null){
			shipschedule = new BusShipSchedule();
		}else{
			shipschedule = serviceContext.shipScheduleService.busShipScheduleDao.findById(selectedRowData.getScheduleid());
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
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if(this.mPkVal==-1l){
			MessageUtils.alert("请先保存主表");
			return;
		}
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				//importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_imp_busshipcostdtl";
				String args = this.mPkVal+"";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
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
