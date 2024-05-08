package com.scp.view.sysmgr.mail;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysEmail;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.mail.emailrubbishBean", scope = ManagedBeanScope.REQUEST)
public class EmailRubbishBean extends GridFormView {
	
	@SaveState
	@Accessible
	public SysEmail selectedRowData = new SysEmail();
	
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysEmailService.sysEmailDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.sysEmailService.saveData(selectedRowData);
	}

}
