package com.scp.view.module.suppliermgr;

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
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

/**
 * @author CIMC
 */
@ManagedBean(name = "pages.module.suppliermgr.boateditBean", scope = ManagedBeanScope.REQUEST)
public class BoatEditBean extends MastDtlView {

	@Bind
	@SaveState
	@Accessible
	public Boolean iswarehouse;

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();

	@Bind
	@SaveState
	@Accessible
	public SysCorporation repetitionTips;
	
	@Bind
	public UIWindow tipsWindow;
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Override
	public void refreshForm() {

	}

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}
	
	@Action
	private void checkRepeatAjaxSubmit() {
		String name = AppUtils.getReqParam("name");
		String value = AppUtils.getReqParam("value");
		if(StrUtils.isNull(value)){this.alert("不能为空!");return;}
		try {
			repetitionTips = serviceContext.customerMgrService.repeat(this.mPkVal , name , value);
			if(repetitionTips != null){
				tipsWindow.show();
				update.markUpdate(true, UpdateLevel.Data, "tipsPanel");
				if("abbr".equals(name)){
					Browser.execClientScript("abbrjsvar.setValue('');");
				}else if("namec".equals(name)){
					Browser.execClientScript("namecjsvar.setValue('');");
				}else if("namee".equals(name)){
					Browser.execClientScript("nameejsvar.setValue('');");
				}else if("code".equals(name)){
					Browser.execClientScript("codejsvar.setValue('');");
				}
			}
		} catch (CommonRuntimeException e) {
			String exception  = e.getLocalizedMessage();
			this.alert(exception);
		}
	}
	/**
	 * 钩选全包散货时,自动钩选仓储客户
	 */
	@Action
	public void isddpAjaxSubmit() {
		Boolean isddp = this.selectedRowData.getIsddp();
		if (isddp) {
			this.iswarehouse = true;
		} else {
			this.iswarehouse = false;
		}
		this.update.markUpdate(UpdateLevel.Data, "masterEditPanel");
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
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	public void doServiceSaveMaster() {
		try {
			serviceContext.customerMgrService.saveData(selectedRowData);
			MessageUtils.alert("OK!");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
	}

	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new SysCorporation();
		this.mPkVal = -1l;
		selectedRowData.setIsar(true);
		selectedRowData.setIsap(true);
		selectedRowData.setIsofficial(true);
		selectedRowData.setIscustomer(true);
		selectedRowData.setLevel("*");
		selectedRowData.setPaytype("B");
		selectedRowData.setIscarrier(true);
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

}
