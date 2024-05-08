package com.scp.view.module.customer;

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

import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.customer.cuseditcommonBean", scope = ManagedBeanScope.REQUEST)
public class CuseditCommonBean extends MastDtlView {

	

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();

	@Override
	public void refreshForm() {

	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			
		}
	}



	@Bind
	public UIIFrame arapIframe;
	
	@Bind
	public UIIFrame contactIframe;
	
	@Bind
	public UIIFrame memorialdayIframe;
	
	@Bind
	public UIIFrame custServiceIframe;
	
	@Bind
	public UIIFrame custCareIframe;
	
	@Bind
	public UIIFrame csUserIframe;

	@Action
	public void showArapEdit() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				arapIframe.load(blankUrl);
			}else{
//				String blankUrl = AppUtil.getContextPath() + "/pages/module/common/blank.xhtml";
//				arapIframe.load(blankUrl);
				arapIframe.load( "../customer/assigneduser.xhtml?id="+this.mPkVal
						+ "&linktyp = C");
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showContact() {
		try {
			if (this.mPkVal == -1l) {
				
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				contactIframe.load(blankUrl);
			}else{
				contactIframe.load( "../customer/contact.xhtml?customerid="+this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showMemorialday() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				memorialdayIframe.load(blankUrl);
				
			}else{
				memorialdayIframe.load( "../customer/memorialday.xhtml?customerid="+this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showCustService() {
		try {
			if (this.mPkVal == -1l) {
				
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				custServiceIframe.load(blankUrl);
			}else{
				custServiceIframe.load( "../customer/custservice.xhtml?customerid="+this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showCustCare() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				custCareIframe.load(blankUrl);
			}else{
				custCareIframe.load( "../customer/custcare.xhtml?customerid="+this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showCsUser() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				csUserIframe.load(blankUrl);
			}else{
				csUserIframe.load( "../customer/csuser.xhtml?customerid="+this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
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
			disableAllButton(false);
			this.selectedRowData = new SysCorporation();
			this.mPkVal = -1l;
			selectedRowData.setIsar(true);
			selectedRowData.setIsap(true);
			selectedRowData.setIsofficial(true);
			selectedRowData.setIscustomer(true);
			selectedRowData.setLevel("*");
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
	}

	@Override
	public void doServiceSaveMaster() {

		serviceContext.customerMgrService.saveData(selectedRowData);

		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");

	}
	
	@Bind
	public UIButton saveMaster;

	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
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
