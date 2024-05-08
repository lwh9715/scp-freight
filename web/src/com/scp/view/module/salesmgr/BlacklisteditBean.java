package com.scp.view.module.salesmgr;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.salesmgr.BlackList;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.salesmgr.blacklisteditBean", scope = ManagedBeanScope.REQUEST)
public class BlacklisteditBean extends MastDtlView{
	
	@SaveState
	@Accessible
	public BlackList selectedRowData = new BlackList();
	
	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}

	@Override
	public void init() {
		selectedRowData = new BlackList();
		String id = AppUtils.getReqParam("id");
		//System.out.println("id--->"+id);
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else {
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		//System.out.println("this.mPkVal--->"+this.mPkVal);
		this.selectedRowData = serviceContext.blackListService.blackListDao
				.findById(this.mPkVal);
		//System.out.println("this.selectedRowData--->"+this.selectedRowData.toString());
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void doServiceSaveMaster() {
		try {
			serviceContext.blackListService.saveData(selectedRowData);
			MessageUtils.alert("OK!");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
	}
	
	@Override
	public void addMaster() {
		this.selectedRowData = new BlackList();
		this.mPkVal = -1l;
		//selectedRowData.setLinkid(0L);
		selectedRowData.setIsstart(true);
		selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
	}
	
	@Override
	public void delMaster() {
		
		try {
			serviceContext.blackListService.delDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
}
