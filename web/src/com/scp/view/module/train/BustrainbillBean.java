package com.scp.view.module.train;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.ConstantBean.Module;
import com.scp.model.bus.BusTrainBill;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.train.bustrainbillBean", scope = ManagedBeanScope.REQUEST)
public class BustrainbillBean extends GridFormView {
	

	@SaveState
	@Accessible
	public BusTrainBill selectedRowData = new BusTrainBill();

	@SaveState
	@Accessible
	public Long jobid;
	
	@SaveState
	public String workItemId;
	
	@Bind
	@SaveState
	public String jsworkItemId;
	
	@Bind
	public UIButton addhbl;
	
	@Bind
	public UIButton addmbl;
	
	@Bind
	public UIButton copyadd;
	
	@Bind
	public UIButton copyaddmbl;
	
	@Bind
	public UIButton del;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			initCtrl();
			String id = AppUtils.getReqParam("id").trim();
			jobid = Long.valueOf(id);
			workItemId = (String) AppUtils.getReqParam("workItemId");
			jsworkItemId = workItemId;
			qryMap.put("jobid$", this.jobid);
		}
	}

	private void initCtrl() {
		addhbl.setDisabled(true);
		addmbl.setDisabled(true);
		copyadd.setDisabled(true);
		copyaddmbl.setDisabled(true);
		del.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_ship_bill.getValue())) {
			if (s.endsWith("_add")) {
				addhbl.setDisabled(false);
				addmbl.setDisabled(false);
				copyadd.setDisabled(false);
				copyaddmbl.setDisabled(false);
			}else if (s.endsWith("_delete")) {
				del.setDisabled(false);
			}
		}
	}

	@Override
	public void del() {
		try {
			serviceContext.busTrainBillMgrService.removeDate(this
					.getGridSelectId());
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_busshipbilledit";
		String url = "./bustrainbilledit.xhtml?id=" + this.getGridSelectId()
				+ "&jobid=" + jobid+"&workItemId="+this.workItemId;
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}

	@Action
	public void addhbl() {

		super.grid_ondblclick();
		String winId = "bustrainbilledit";
		String url = "./bustrainbilledit.xhtml?jobid=" + jobid+"&bltype=H"+"&workItemId="+this.workItemId;
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}

	@Action
	public void addmbl() {

		super.grid_ondblclick();
		String winId = "bustrainbilledit";
		String url = "./bustrainbilledit.xhtml?jobid=" + jobid+"&bltype=M";
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}

	@Override
	public void refresh() {
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Action
	public void copyadd() {
		String url = AppUtils.getContextPath()
				+ "/pages/module/ship/shipbillcopychoose.xhtml?jobid="
				+ this.jobid+"&type=H";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	@Action
	public void copyaddmbl() {
		String url = AppUtils.getContextPath()
				+ "/pages/module/ship/shipbillcopychoosembl.xhtml?jobid="
				+ this.jobid+"&type=M";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;
	
	
	@Bind
	private UIIFrame billtraceIframe;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	@Override
	public void grid_onrowselect() {	
		//super.grid_onrowselect();
		
		//String id = this.grid.getSelectedIds()[0];
		//BusTrainBill busTrainBill  = this.serviceContext.busTrainBillMgrService.busTrainBillDao.findById(Long.valueOf(id));
		//String url = "";
		//if(busTrainBill.getBltype().equals("M")){
			//url = "./busshipbilltracembl.xhtml?billid="+this.getGridSelectId();
		//}else{
			//url = "./busshipbilltrace.xhtml?billid="+this.getGridSelectId();
		//}
		//billtraceIframe.setSrc(url);
		//update.markAttributeUpdate(billtraceIframe, "src");
	}
}
