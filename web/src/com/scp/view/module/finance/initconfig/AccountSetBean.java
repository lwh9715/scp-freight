package com.scp.view.module.finance.initconfig;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsActset;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.accountsetBean", scope = ManagedBeanScope.REQUEST)
public class AccountSetBean extends GridFormView {

	@SaveState
	@Accessible
	public FsActset selectedRowData = new FsActset();
	
	@Bind
	@SaveState
	@Accessible
	public Long actSetid;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	
	public void init() {
	
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			doServiceFindData();
			this.refreshForm();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
		
	};
	
	@Override
	public void refresh(){
		this.refreshForm();
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.accountsetMgrService.fsActsetDao.findById(AppUtils.getUserSession().getActsetid());
	}


	@Override
	protected void doServiceSave() {
		serviceContext.accountsetMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refreshForm();
	}

	@Override
	public void save() {
		try {
			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void checkdata() {
		try {
			serviceContext.accountsetMgrService.checkData(selectedRowData.getId());
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

}
