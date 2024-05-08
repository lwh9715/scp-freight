package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipContainer;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.busshipcontainerBean", scope = ManagedBeanScope.REQUEST)
public class BusShipContainerBean extends GridFormView {

	@SaveState
	@Accessible
	public BusShipContainer selectedRowData = new BusShipContainer();
	
	
	@SaveState
	@Accessible
	public Long jobid;
	
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			jobid=Long.valueOf(id);
			qryMap.put("jobid$", this.jobid);
			initAdd();
			// this.grid.repaint();
		}
	}

	

	

	protected void initAdd() {
		selectedRowData =  new BusShipContainer();
		selectedRowData.setJobid(this.jobid);
		
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Override
	public void add() {

		super.add();
		
		initAdd();
	}

	

	@Override
	public void del() {
		serviceContext.sysUserAssignMgrService.removeDate(this.pkVal);
		this.alert("OK");
		this.add();
		this.grid.reload();
	}

	

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.busShipContainerMgrService.busShipContainerDao.findById(this
				.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busShipContainerMgrService.saveData(selectedRowData);
		alert("OK");
		refresh();
	}

	
}
