package com.scp.view.sysmgr.memo;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysMemo;
import com.scp.service.sysmgr.SysMemoService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.sysmgr.memo.memoshowBean", scope = ManagedBeanScope.REQUEST)
public class MemoShowBean extends FormView {
	
	@ManagedProperty("#{sysMemoService}")
	public SysMemoService sysMemoService;
	
	@SaveState
	@Accessible
	public SysMemo selectedRowData = new SysMemo(); 
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String id = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)){
				this.pkVal = Long.parseLong(id);
				this.refresh();
			}
			
			
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		selectedRowData = this.sysMemoService.sysMemoDao.findById(this.pkVal);
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
}
