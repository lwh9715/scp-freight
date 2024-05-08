package com.scp.view.module.del.dispatch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.data.DatCar;
import com.scp.model.data.DatCartype;
import com.scp.model.del.DelLoad;
import com.scp.model.del.DelLoadtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.del.dispatch.loadingdtlcheckBean", scope = ManagedBeanScope.REQUEST)
public class LoadingdtlCheckBean extends MastDtlView {

	@SaveState
	@Accessible
	public DelLoad selectedRowData = new DelLoad();

	@SaveState
	@Accessible
	public DelLoadtl ddata = new DelLoadtl();
	
	@SaveState
	@Accessible
	public DatCar car = new DatCar();
	
	@SaveState
	@Accessible
	public DatCartype cartype = new DatCartype();


	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if (selectedRowData.getIsubmit() == null) {

			} else {
				disableAllButton(selectedRowData.getIsubmit());
			}
		}
	}

	@Override
	public void init() {
		selectedRowData = new DelLoad();
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
		qry += "\nAND loadid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.delLoadMgrService.delLoadDao
				.findById(this.mPkVal);
		getCarType();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");

	}

	@Override
	protected void doServiceFindData() {
		this.ddata = serviceContext.delLoaddtlMgrService.delLoadtlDao
				.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		serviceContext.delLoaddtlMgrService.saveDtlData(ddata);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		this.alert("OK");
	}

	@Override
	public void doServiceSaveMaster() {
		if (selectedRowData.getCarid() == null
				|| selectedRowData.getSingtime() == null) {
			MessageUtils.alert("车辆,制单日期不能为空");
			return;
		} else {
			// 提交之前必须先要保存,必须子表中也有数据才能提交
			serviceContext.delLoaddtlMgrService.saveMasterData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			this.refreshMasterForm();
			this.alert("ok");
		}
	}


	private void disableAllButton(Boolean isCheck) {
	
		
	}

	/**
	 * 提交通用处理
	 */
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		Date date=new Date();
		String sql="";
		if (isCheck) {
			sql = "UPDATE del_load SET ischeck = TRUE ,checktime = '"+date+"',checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE del_load SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id ="+this.mPkVal+";";
		}
		try {
			serviceContext.delLoaddtlMgrService.delLoadtlDao.executeSQL(sql);
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

	@Override
	public void add() {
		if (this.mPkVal == -1L) {
			MessageUtils.alert("请先保存主表数据");
			return;
		} else {
			ddata = new DelLoadtl();
			ddata.setLoadid(this.mPkVal);
			super.add();
		}
	}

	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new DelLoad();
		this.mPkVal = -1l;
		selectedRowData.setSingtime(new Date());
		super.addMaster();
	}

	

	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.delLoaddtlMgrService.delBatch(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}

	@Override
	public void delMaster() {
		if (selectedRowData.getIsubmit()) {
			MessageUtils.alert("请先取消提交以后才能删除");
			return;
		} else {
			serviceContext.delLoadMgrService.removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
			refresh();
		}
	}
	
	/**
	 * 点击生成送货单
	 */
	@Action
	public void addDelivery(){
	
		try {
			String sql="SELECT f_del_insertdelivery("+this.mPkVal+")";
			serviceContext.delLoadMgrService.delLoadDao.executeQuery(sql);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Bind(id = "cardesc")
	public List<SelectItem> getCardesc() {
		try {
			return CommonComBoxBean.getComboxItems("c.id", "c.code||'/'||c.carcolor1||'/'||t.cartype||'/'||COALESCE(d.code,'')||'/'||COALESCE(c.remarks,'')",
					"dat_car c ,dat_cartype t,dat_driver d", "WHERE 1=1", "ORDER BY c.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	public void load() {
		
		if (this.mPkVal==-1l) {
			MessageUtils.alert("请先保存主表");
			return;
		}
		String url = AppUtils.getContextPath() + "/pages/module/pmw/dispatch/wmsoutdtlchooser.faces?loadid="+this.mPkVal;
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
	
	@Bind
	@SaveState
	@Accessible
	public Double carcbm;
	
	public void getCarType(){
		if(selectedRowData.getCarid()==null){
		}else{
		//cartypedesc=serviceContext.delLoadMgrService.getCarType(selectedRowData.getCarid());
		this.car=serviceContext.carMgrService.datCarDao.findById(selectedRowData.getCarid());
		this.cartype=serviceContext.carTypeMgrService.datCartypeDao.findById(car.getCartypeid());
		carcbm=cartype.getH().doubleValue()*cartype.getL().doubleValue()*cartype.getW().doubleValue();
		}
	}
	
	
	@Action 
	public void carAjaxSubmit(){
		getCarType();
		//update.markUpdate(true, UpdateLevel.Data, "cartypedesc");
//		update.markUpdate(true, UpdateLevel.Data, "cartype");
//		update.markUpdate(true, UpdateLevel.Data, "carriedwgt");
//		update.markUpdate(true, UpdateLevel.Data, "l");
//		update.markUpdate(true, UpdateLevel.Data, "w");
//		update.markUpdate(true, UpdateLevel.Data, "h");
//		update.markUpdate(true, UpdateLevel.Data, "carcbm");
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		
	}
}
