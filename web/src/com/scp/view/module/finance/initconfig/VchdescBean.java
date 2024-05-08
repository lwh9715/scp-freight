package com.scp.view.module.finance.initconfig;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsVchdesc;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.vchdescBean", scope = ManagedBeanScope.REQUEST)
public class VchdescBean extends GridFormView {

	
	@SaveState
	@Accessible
	public FsVchdesc selectedRowData = new FsVchdesc();
	
	
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
		selectedRowData = new FsVchdesc();
		super.add();
		
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.vchdescMgrService.fsVchdescDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		try {
			serviceContext.vchdescMgrService.saveData(selectedRowData);
			String sql = "UPDATE fs_vchdesc SET actsetid = "+ AppUtils.getUserSession().getActsetid()+"WHERE id="+selectedRowData.getId()+";";
			serviceContext.vchdescMgrService.fsVchdescDao.executeSQL(sql);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void del() {
		try {
			serviceContext.vchdescMgrService.fsVchdescDao.remove(selectedRowData);
			this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	public void removeDateForVchdescId(String[] ids) {
		String sql = "UPDATE fs_vchdesc SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id IN ("+StrUtils.array2List(ids)+");";
		serviceContext.vchdescMgrService.fsVchdescDao.executeSQL(sql);
	}

}
