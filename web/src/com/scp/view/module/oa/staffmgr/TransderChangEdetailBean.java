package com.scp.view.module.oa.staffmgr;

import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.OaJobChange;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.staffmgr.transderchangedetailBean", scope = ManagedBeanScope.REQUEST)
public class TransderChangEdetailBean extends MastDtlView {

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

	@Bind
	@SaveState
	public UIButton saveMaster;
	
	@Bind
	@SaveState
	public String userid;

	@Bind
	@SaveState
	@Accessible
	public OaJobChange selectedRowData = new OaJobChange();

	@Action
	public void deltrans() {
		this.serviceContext.jobChangeService()
				.removeDate(this.selectedRowData.getId());
		MessageUtils.alert("OK!");
		this.addMaster();
		queryStarte();
	}

	@Override
	public void doServiceSaveMaster() {
		try {
			Long userinfoid = this.selectedRowData.getUserinfoid();
			String name = this.getNname(userinfoid);
			selectedRowData.setName(name);
			serviceContext.jobChangeService().saveData(this.selectedRowData);
			this.mPkVal = this.selectedRowData.getId();
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			MessageUtils.alert("ok");
		} catch (Exception e) {
			MessageUtils.alert("姓名不能为空!");
			return;
		}
	}
	
	public String getNname(Long id){
		String sql = "SELECT namec FROM oa_userinfo WHERE isdelete = FALSE AND id = " + id;
		 Map map= this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		  String name =(String)map.get("namec");
		  return name;
	}

	@Override
	public void init() {
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.valueOf(id);
			doServiceFindData();
		} else {
			this.addMaster();
		}
		newStateMsg();
		queryStarte();
	}

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceFindData() {
		;
		this.selectedRowData = serviceContext.jobChangeService().jobChangeDao
				.findById(mPkVal);
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMaster() {
		selectedRowData = new OaJobChange();
		selectedRowData.setChangetype("A");
	}

	@Action
	public void startLeave() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("请先保存,在启动流程!");
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "daytype");
		pass("TransChange");
		MessageUtils.alert("变更转职通知流程启动成功!");
		newStateMsg();
		startLeave.setDisabled(true);
		saveMaster.setDisabled(true);
	}

	public void pass(String processid) {
//		Map<String, Object> m = new HashMap<String, Object>();
//		m.put("id", this.selectedRowData.getId());
//		m.put("sn", this.selectedRowData.getName());
//		// m.put("datechange",daytype);// 设置参数
//		try {
//			// WorkFlowUtil.startFF("ShipFCLProcess0712", m, AppUtils
//			// .getUserSession().getUsercode());
//			WorkFlowUtil.startFF(processid, m, AppUtils.getUserSession()
//					.getUsercode());
//
//		} catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
	}

	public void newStateMsg() {
		String sql = " SELECT "
				+ "\n string_agg((CASE WHEN (workitemstate = 7 OR workitemstate = 9) THEN actor||'('||taskdisplayname||')'||'已批阅通过'||' '||'日期:'|| to_char(workitemendtime,'yyyy-MM-dd') WHEN "
				+ "\n	(workitemstate = 0 OR workitemstate = 1) THEN actor||'('||taskdisplayname||')'||'尚未批阅,请知悉!' ELSE '未知错误' END),'\n')AS essage "
				+ "\nFROM _wf_jobs t" + "\nWHERE" + "\nt.processcreatorid = '"
				+ AppUtils.getUserSession().getUsercode() + "'"
				+ "\nAND t.refid = '" + this.selectedRowData.getId() + "'"
				+ "\nAND t.processid like 'TransChange%'";
		Map map = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		String msg = (String) map.get("essage");
		messgae = msg + endAddMsg(msg);
		update.markUpdate(true, UpdateLevel.Data, "messgae");
	}

	public String endAddMsg(String message) {

		if (!StrUtils.isNull(message) && message.contains("(部门人事审批)已批阅通过")) {
			return "\n已通过所有部门批阅!";
		} else {
			return "";
		}
	}

	public void queryStarte() {
		String sql = "SELECT v.value FROM t_ff_rt_taskinstance t,t_ff_rt_procinst_var v WHERE v.processinstance_id = t.processinstance_id AND t.process_id = 'TransChange' AND v.name = 'id' AND v.value LIKE 'java.lang.Long#%' AND  CAST(replace(v.value,'java.lang.Long#','') AS BIGINT) = "
				+ selectedRowData.getId() + ";";
		List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
		if (list.size() <= 0 || "".equals(list)) {
			startLeave.setDisabled(false);
			saveMaster.setDisabled(false);
		} else {
			startLeave.setDisabled(true);
			saveMaster.setDisabled(true);
		}
	}

}
