package com.scp.view.module.carmgr;

import java.util.Calendar;
import java.util.Date;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.car.CarRepaireRecord;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.carmgr.carrepairedtlBean", scope = ManagedBeanScope.REQUEST)
public class CarRepaireDtlBean extends MastDtlView {

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Accessible
	public CarRepaireRecord selectedRowData = new CarRepaireRecord();

	@Override
	public void refreshForm() {
		

	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(selectedRowData.getIscheck()==null){
				
			}else{
				disableAllButton(selectedRowData.getIscheck());
			}
		}
	}

	@Action
	public void showAttachmentIframe() {
		try {
			if(this.mPkVal==-1l){
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				attachmentIframe.load(blankUrl);
			}else{
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIIFrame arapIframe;

	@Action
	public void showArapEdit() {
		
		
		try {
			
			if(this.mPkVal==-1l){
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				arapIframe.load(blankUrl);
			}else{
			arapIframe.load("../finance/arapedit.xhtml?id=" + this.mPkVal
					+ "&jobid="+findJobId());
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	private Long findJobId() {
		Long jobid = selectedRowData.getJobid();
		return jobid;
	}

	
	@Override
	public void init() {
		selectedRowData = new CarRepaireRecord();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();

		} else {
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.carRepaireRecordMgrService.carRepaireRecordDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
		showAttachmentIframe();
	}

	@Override
	public void doServiceSaveMaster() {

		serviceContext.carRepaireRecordMgrService.saveData(selectedRowData);

		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");

	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	

	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
	}

	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater = AppUtils.getUserSession().getUsername();
		Date date = new Date();
		String sql = "";
		if (this.mPkVal == -1l)
			throw new CommonRuntimeException("Plese save first!");
		if (isCheck) {
			sql = "UPDATE car_repaire_record SET ischeck = TRUE ,checktime = '"
					+ date + "',checkter = '" + updater + "' WHERE id ="
					+ this.mPkVal + ";";
		} else {
			sql = "UPDATE car_repaire_record SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id ="
					+ this.mPkVal + ";";
		}
		try {
			serviceContext.carOilRecordMgrService.carOliRecordDao
					.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isCheck);
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);

		}
	}


	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new CarRepaireRecord();
		this.mPkVal = -1l;
		selectedRowData.setSingtime(new Date());
		selectedRowData.setCurrency(AppUtils.getUserSession().getBaseCurrency());
		showArapEdit();
		showAttachmentIframe();
		
	}

	@Override
	public void delMaster() {
		if (selectedRowData.getIscheck()) {
			MessageUtils.alert("请先取消审核以后才能删除");
			return;
		} else {
			serviceContext.carRepaireRecordMgrService.removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");

		}
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

}
