package com.scp.view.module.oa.ff;

import java.util.ArrayList;
import java.util.List;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.AppTravel;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.ff.apptravelflowBean", scope = ManagedBeanScope.REQUEST)
public class AppTravelFlowBean extends MastDtlView {

	@Bind
	public UIIFrame billIframe;

	@Bind
	@SaveState
	public String datetype;

	@Bind
	@SaveState
	public String daytype;

	@Bind
	@SaveState
	public String messgae;

	@Bind
	@SaveState
	public UIButton startLeave;

	@SaveState
	public String processInstanceId;

	@SaveState
	public String workItemId;

	@Bind
	public Long assign;

	@Bind
	@SaveState
	public UIButton saveMaster;

	@Bind
	@SaveState
	public String userid;

	@Bind
	@SaveState
	public String province;

	@Bind
	@SaveState
	public String city;

	@Bind
	@SaveState
	public String area;

	@Bind
	@SaveState
	@Accessible
	public AppTravel selectedRowData = new AppTravel();

	@Override
	public void init() {
		String id = AppUtils.getReqParam("id");
		processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
		workItemId = (String) AppUtils.getReqParam("workItemId");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.valueOf(id);
			doServiceFindData();
		} else {
			this.addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub

	}

	@Action
	public void pass() {
		List actorIds = new ArrayList();
		if (this.assign == null) {

		} else {
			actorIds.add(AppUtils.getUserCode(this.assign));
		}
		if (!StrUtils.isNull(processInstanceId)) {
//			Map<String, Object> m = new HashMap<String, Object>();
//			try {
//				WorkFlowUtil.passProcess(processInstanceId, workItemId, m,
//						actorIds);
//				MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
		}
	}

	@Action
	public void notpass() {
		if (!StrUtils.isNull(processInstanceId)) {
//			Map<String, Object> m = new HashMap<String, Object>();
//			// 只要单号不变不需要 进行update
//			// m.put("id",this.selectedRowData.getId());// 设置id
//			// m.put("sn",this.selectedRowData.getNos());// 设置流水号
//			try {
//				WorkFlowUtil.notPassProcess(processInstanceId, workItemId, m);
//				MessageUtils.alert("NotPass OK");
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
		}
	}

	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.appTravelService().appTravelDao
				.findById(mPkVal);
		String[] toplace = this.selectedRowData.getToplace().split(" ");
		if (toplace.length == 3) {
			province = toplace[0];
			city = toplace[1];
			area = toplace[2];
			update.markUpdate(true, UpdateLevel.Data, "province");
			update.markUpdate(true, UpdateLevel.Data, "city");
			update.markUpdate(true, UpdateLevel.Data, "area");
		}
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMaster() {
		selectedRowData = new AppTravel();
		selectedRowData.setSalaryfm("D");
	}
	
	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub

	}

}
