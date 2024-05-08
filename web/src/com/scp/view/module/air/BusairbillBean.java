package com.scp.view.module.air;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.bus.BusAirBill;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.air.busairbillBean", scope = ManagedBeanScope.REQUEST)
public class BusairbillBean extends GridFormView {
	
	@SaveState
	@Accessible
	public BusAirBill selectedRowData = new BusAirBill();

	@SaveState
	@Accessible
	public Long jobid;
	
	@Bind
	public UIButton addhbl;
	
	@Bind
	public UIButton addmbl;
	
	@SaveState
	public String workItemId;
	
	@Bind
	@SaveState
	public String jsworkItemId;

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Bind
	public UIButton del;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id").trim();
			jobid = Long.valueOf(id);
			workItemId = (String) AppUtils.getReqParam("workItemId");
			jsworkItemId = workItemId;
			qryMap.put("jobid$", this.jobid);
		}
	}
	
	@Override
	public void del() {
		try {
			serviceContext.busAirBillMgrService.removeDate(this.getGridSelectId());
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void addhbl() {
		super.grid_ondblclick();
		String winId = "busairbilledit";
		String url = "./busairbilledit.xhtml?jobid=" + jobid+"&bltype=H"+"&workItemId="+this.workItemId;
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
	
	
	@Action
	public void addmbl() {

		super.grid_ondblclick();
		String winId = "busairbilledit";
		String url = "./busairbilledit.xhtml?jobid=" + jobid+"&bltype=M";
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "busairbilledit";
		String url = "./busairbilledit.xhtml?id=" + this.getGridSelectId()
				+ "&jobid=" + jobid+"&workItemId="+this.workItemId;
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}

	@Override
	public void refresh() {
		this.grid.reload();
	}
	
	
}
