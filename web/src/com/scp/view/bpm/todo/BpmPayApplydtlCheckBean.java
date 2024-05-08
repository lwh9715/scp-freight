package com.scp.view.bpm.todo;

import java.util.Calendar;
import java.util.List;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.module.finance.PayApplydtlCheckBean;

@ManagedBean(name = "bpm.todo.bpmpayapplydtlcheckBean", scope = ManagedBeanScope.REQUEST)
public class BpmPayApplydtlCheckBean extends PayApplydtlCheckBean {
	
	
	
	
	@Bind
	@SaveState
	public String actionJsText;
	
	@Bind
	@SaveState
	private String amtreqamttosum;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.beforeRender(isPostBack);
			String taskid = AppUtils.getReqParam("taskid");
			if(!StrUtils.isNull(taskid)){
				actionJsText = "";
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
				List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmTask.getProcessid()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'js'");
				for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
					actionJsText+=bpmWorkItem.getActions();
				}
				//System.out.println("actionJsText:"+actionJsText);
				update.markUpdate(true, UpdateLevel.Data, "actionJsText");
			}
		}
	}

	
	@Action
	public void generateRPBPM() {
		try {
			String[] ids = new String[1];
			ids[0] = this.mPkVal.toString();
			this.selectedRowData = serviceContext.reqMgrService.finaRpreqDao.findById(this.mPkVal);
			selectedRowData.setIscheck(true);
			selectedRowData.setCheckter("BPM");
			selectedRowData.setChecktime(Calendar.getInstance().getTime());
			serviceContext.reqMgrService.finaRpreqDao.createOrModify(selectedRowData);
			serviceContext.reqMgrService.createRPmany(ids , AppUtils.getUserSession().getUsercode());
			refreshMasterForm();
			showActpayrec();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void createRP() {
		try {
			super.generateRP();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	@Action
	public void checkBatch() {
		try {
			String[] ids = new String[1];
			ids[0] = this.mPkVal.toString();
			serviceContext.reqMgrService.updateCheck(ids, AppUtils.getUserSession().getUsername());
			MessageUtils.alert("审核成功");
			refreshMasterForm();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Bind
	public UIWindow arapDetailWindow;

	@Bind
	public UIIFrame arapDetailIFrame;

	@Bind
	@SaveState
	public String jobid;

	/**
	 * 查看费用详情
	 */
	@Action
	public void arapDetail() {
			arapDetailIFrame.setSrc("/scp/pages/module/finance/arapedit.xhtml?customerid=" + "100" + "&jobid=" + jobid);
			update.markAttributeUpdate(arapDetailIFrame, "src");
			if (arapDetailWindow != null) {
				arapDetailWindow.show();
			}
	}
}
