package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.jobschildsplitBean", scope = ManagedBeanScope.REQUEST)
public class JobsChildsplitBean extends GridFormView {

	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();
	
	@Bind
	@SaveState
	@Accessible
	public Long qryWarehouseCom;
	
	@SaveState
	@Accessible
	public Long parentid;
	
	@Bind
	@SaveState
	@Accessible
	public Long thisid;
	
	@Bind
	@SaveState
	@Accessible
	public Boolean freewms=true;
	
	
	
	@SaveState
	@Accessible
	public String linktype;
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			linktype =AppUtils.getReqParam("linktype").trim();
			parentid=Long.valueOf(id);
			thisid=parentid;
			update.markUpdate(true, UpdateLevel.Data, "thisid");
			update.markUpdate(true, UpdateLevel.Data, "freewms");
			//qryMap.put("parentid$", this.parentid);
			initAdd();
			//this.grid.reload();
			// this.grid.repaint();
		}
	}

	

	

	protected void initAdd() {
		selectedRowData = (FinaJobs) new FinaJobs();
		
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Override
	public void add() {

		super.add();
		initAdd();
	}

	

	@Override
	public void del() {
//		serviceContext.sysUserAssignMgrService.removeDate(this.pkVal);
//		this.alert("OK");
//		this.add();
//		this.grid.reload();
	}

	

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this
				.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
	
		//serviceContext.jobsMgrService.saveData(selectedRowData);
	}
 
	
	@Override
	public void grid_ondblclick() {
//		String winId =System.currentTimeMillis()+"";
//		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
//		AppUtil.openWindow(winId, url);
	}
	
	@Action
	public void jobschoose() {
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipjobschoose.xhtml?jobid="+this.parentid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
 
	

	@Action
	public void cancel() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.jobsMgrService.cancel(ids);
			MessageUtils.alert("cancel success");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String sql="id="+this.parentid;
		String sql2="id <> "+this.parentid;
		m.put("child", sql);
		m.put("child2", sql2);
		return m;
	}
	
	@Action
	public void createwmsin(){
		if(this.qryWarehouseCom==null){
			MessageUtils.alert("请选择一个仓库");
			return;
		}
		
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
		try {
			serviceContext.jobsMgrService.createsplitwmsin(this.parentid,ids,AppUtils.getUserSession().getUsercode(),this.qryWarehouseCom,this.freewms);
			this.refresh();
			MessageUtils.alert("OK");
		
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
