package com.scp.view.sysmgr.mail;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.dao.sys.SysEmailDao;
import com.scp.model.sys.SysEmail;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.mail.emailreceiveBean", scope = ManagedBeanScope.REQUEST)
public class EmailReceiveBean extends GridFormView {
	
	
	@SaveState
	@Accessible
	public SysEmail selectedRowData = new SysEmail();

	
	@Action
	public void receive(){
		try {
			this.serviceContext.sysEmailService.receiveEmail();
			this.refresh();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}




	@Override
	public void add() {
		selectedRowData = new SysEmail();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysEmailService.sysEmailDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		this.serviceContext.sysEmailService.saveData(selectedRowData);
		this.alert("OK");
	}

	@Resource
	private SysEmailDao sysEmailDao;
	@Action
	public void deleter() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择单行记录");
			return;
		} else {
			for(int i=0;i<ids.length;i++){
				long id = Long.parseLong(ids[i]);
				SysEmail data = sysEmailDao.findById(id);
				if(data == null)continue;
				sysEmailDao.remove(data);
			}
			this.alert("OK");
			refresh();
		}
	}

	@Override
	public void del() {
		if (selectedRowData.getId() == 0) {
			this.add();
		} else {
			this.serviceContext.sysEmailService.removeDate(selectedRowData.getId());
			refresh();
			this.add();
			this.alert("OK");
		}
	}

}
