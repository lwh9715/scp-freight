package com.scp.view.module.oa.staffmgr;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysDepartment;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.staffmgr.jobnomgrBean", scope = ManagedBeanScope.REQUEST)
public class JobnomgrBean extends GridFormView {
	
	
	@Bind
	public Long company;
	
	@SaveState
	public SysDepartment data;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)) {
	
			}
		}
	}

	@Override
	public void grid_ondblclick() {
	
	}
	
	
	@Override
	public void add() {

	}
	
	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	
	}

	@Override
	public void del(){
	
	}

	
}
