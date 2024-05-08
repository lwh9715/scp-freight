package com.scp.view.module.crm;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.crm.businesscardBean", scope = ManagedBeanScope.REQUEST)
public class BusinesscardBean extends MastDtlView {

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();
	
	
	@Override
	public void refreshForm() {

	}

	@Bind
	private Long userId;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			userId = AppUtils.getUserSession().getUserid();
		}
	}

	@Override
	public void init() {
		selectedRowData = new SysCorporation();
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
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	public void doServiceSaveMaster() {
		try {
			SysCorporation repetitionTips = serviceContext.customerMgrService.repeat(this.mPkVal , "code" , this.selectedRowData.getCode());
			if(repetitionTips != null){
				MessageUtils.alert("代码已存在");
				return;
			}
			SysCorporation repetitionTip2s = serviceContext.customerMgrService.repeat(this.mPkVal , "namec" , this.selectedRowData.getNamec());
			if(repetitionTip2s != null){
				MessageUtils.alert("中文名已存在");
				return;
			}
			serviceContext.customerMgrService.saveData(selectedRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");

	}

	@Override
	public void addMaster() {
		this.selectedRowData = new SysCorporation();
		this.mPkVal = -1l;
		selectedRowData.setIsar(true);
		selectedRowData.setIsap(true);
		selectedRowData.setIsofficial(false);
		selectedRowData.setIscustomer(true);
		selectedRowData.setLevel("*");
	}

	@Override
	public void delMaster() {

		try {
			serviceContext.customerMgrService.delDate(this.mPkVal);
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

}
