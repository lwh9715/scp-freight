package com.scp.view.module.lclload;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.data.DatCntype;
import com.scp.model.del.DelLoad;
import com.scp.model.del.DelLoadtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.lclload.shiplcldtlBean", scope = ManagedBeanScope.REQUEST)
public class ShipLcldtlBean extends MastDtlView {

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
		qry += "\nAND loadid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.delLoadMgrService.delLoadDao
				.findById(this.mPkVal);

		getCntType();
		getLoaddesc();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
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
		if (selectedRowData.getCntypeid() == null
				|| selectedRowData.getSingtime() == null||selectedRowData.getWarehouseid()==null) {
			MessageUtils.alert("仓库,箱型,制单日期不能为空");
			return;
		}
		
		if(StrUtils.isNull(selectedRowData.getLcltype())){
			MessageUtils.alert("请选择散货类型");
			return;
		}
			// 提交之前必须先要保存,必须子表中也有数据才能提交
			serviceContext.delLoaddtlMgrService.saveMasterData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			this.refreshMasterForm();
			this.alert("ok");
		
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	@Bind
	public UIButton load;

	@Bind
	public UIButton del;

	@Bind
	public UIButton delBatch;

	@Bind
	public UIButton chooseGoods;

	private void disableAllButton(Boolean issubmit) {
		saveMaster.setDisabled(issubmit);
		delMaster.setDisabled(issubmit);
		load.setDisabled(issubmit);
		delBatch.setDisabled(issubmit);
		chooseGoods.setDisabled(issubmit);

	}

	/**
	 * 提交通用处理
	 */
	@Action
	public void isubmitAjaxSubmit() {
		Boolean isSubmit = selectedRowData.getIsubmit();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (selectedRowData.getIscheck())
				throw new CommonRuntimeException(
						"Current is check,plese uncheck first!");
			if (isSubmit) {
				sql = "UPDATE del_load SET isubmit = TRUE ,submitime = NOW()"
						+ ",submiter = '" + updater + "' WHERE id ="
						+ this.mPkVal + ";";
			} else {
				sql = "UPDATE del_load SET isubmit = FALSE ,submitime = NULL,submiter = NULL WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.delLoaddtlMgrService.delLoadtlDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isSubmit);
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIsubmit(!isSubmit);
			selectedRowData.setSubmiter(isSubmit ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setSubmitime(isSubmit ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isSubmit);
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsubmit(!isSubmit);
			selectedRowData.setSubmiter(isSubmit ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setSubmitime(isSubmit ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isSubmit);
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
		selectedRowData.setLoadtype("S");
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
			try {
				serviceContext.delLoadMgrService.removeDate(this.mPkVal);
				this.addMaster();
				this.alert("OK");
				//refreshMasterForm();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Action
	public void load() {

		if (this.mPkVal == -1l) {
			MessageUtils.alert("请先保存主表");
			return;
		}
		String url = AppUtils.getContextPath()
				+ "/pages/module/lclload/wmsstockchooser.xhtml?loadid="
				+ this.mPkVal+"&warehouseid="+this.selectedRowData.getWarehouseid();
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

	public void getCntType() {
		if (selectedRowData.getCntypeid() == null) {
			this.cnt =  new DatCntype();
		} else {
			this.cnt = serviceContext.cntypeMgrService.datcntypeDao
					.findById(selectedRowData.getCntypeid());
		}
	}

	@Action
	public void carAjaxSubmit() {
		getCntType();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");

	}

	@Action
	public void chooseGoods() {
		try {
			serviceContext.delLoadMgrService.chooseGoods(this.mPkVal);
			MessageUtils.alert("OK");
			refreshMasterForm();
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
	
	@Override
	public void refresh() {
		
		super.refresh();
	}

}
