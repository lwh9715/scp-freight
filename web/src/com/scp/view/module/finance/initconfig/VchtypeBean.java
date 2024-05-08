package com.scp.view.module.finance.initconfig;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsVchtype;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.vchtypeBean", scope = ManagedBeanScope.REQUEST)
public class VchtypeBean extends GridFormView {

	
	@SaveState
	@Accessible
	public FsVchtype selectedRowData = new FsVchtype();
	
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			this.qryMap.put("actsetid$", AppUtils.getUserSession().getActsetid());
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
		}
	}
	
	@Override
	public void add() {
		selectedRowData = new FsVchtype();
		super.add();
		
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.vchtypeMgrService.fsVchtypeDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		try {
			serviceContext.vchtypeMgrService.saveData(selectedRowData);
			String sql = "UPDATE fs_vchtype SET actsetid = "+ AppUtils.getUserSession().getActsetid()+",issysdata = FALSE WHERE id="+selectedRowData.getId()+";";
			serviceContext.vchtypeMgrService.fsVchtypeDao.executeSQL(sql);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void del() {
		try {
			String sql = "UPDATE fs_vchtype SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = "+selectedRowData.getId()+");";
			serviceContext.vchtypeMgrService.fsVchtypeDao.executeSQL(sql);
			this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
