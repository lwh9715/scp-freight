package com.scp.view.module.lclload;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.data.DatCntype;
import com.scp.model.del.DelLoad;
import com.scp.model.del.DelLoadtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.lclload.trainlcldtlcheckBean", scope = ManagedBeanScope.REQUEST)
public class TrainLcldtlCheckBean extends MastDtlView {

	@SaveState
	@Accessible
	public DelLoad selectedRowData = new DelLoad();

	@SaveState
	@Accessible
	public DelLoadtl ddata = new DelLoadtl();
	
	@SaveState
	@Accessible
	public DatCntype cnt = new DatCntype();
	
	@Bind
	@SaveState
	@Accessible
	public BigDecimal loadcbm;

	@Bind
	@SaveState
	@Accessible
	public BigDecimal loadwgt;


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
		m.put("qry", qry);
		
		String filter = "\nAND i.id = ANY(SELECT DISTINCT z.inid FROM  del_loadtl x , wms_outdtlref y , wms_indtl z WHERE x.loadid = "+ mPkVal+" AND x.outdtlid = y.outdtlid AND y.indtlid = z.id)";
		m.put("filter", filter);
		
		return m;
	}
	

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.delLoadMgrService.delLoadDao
				.findById(this.mPkVal);
		getCntType();
		getLoaddesc();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "loadcbm");
		update.markUpdate(true, UpdateLevel.Data, "loadwgt");

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
		String sql="";
		if (isCheck) {
			sql = "UPDATE del_load SET ischeck = TRUE ,checktime = NOW(),checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
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
	
	

	
	
	public void getCntType(){
		if(selectedRowData.getCntypeid()==null){
		}else{
		this.cnt=serviceContext.cntypeMgrService.datcntypeDao.findById(selectedRowData.getCntypeid());
		}
	}
	
	
	@Action 
	public void carAjaxSubmit(){
		getCntType();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		
	}
	
	private void getLoaddesc() {

		if (this.mPkVal == -1l) {
			this.loadcbm = new BigDecimal(0.0);
			this.loadwgt = new BigDecimal(0.0);
		}
		BigDecimal[] re = new BigDecimal[2];
		re = serviceContext.delLoadMgrService.getLoaddesc(this.mPkVal);
		this.loadcbm = re[0];
		this.loadwgt = re[1];

	}
	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename;
	
	@Action
	public void preview() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要预览的行");
			return;
		}
		String args = "&ids=" + StrUtils.array2List(ids)+"&dtlloadid="+this.mPkVal;
		if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
			MessageUtils.alert("请选择格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/train/"
				+ showwmsinfilename;
		AppUtils.openWindow("_shipbillReport", openUrl + args
				+ "&userid="
				+ AppUtils.getUserSession().getUserid());
	}

}
