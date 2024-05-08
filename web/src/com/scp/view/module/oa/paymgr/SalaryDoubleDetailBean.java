package com.scp.view.module.oa.paymgr;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.OaSalaryDouble;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.paymgr.salarydoubledetailBean", scope = ManagedBeanScope.REQUEST)
public class SalaryDoubleDetailBean extends MastDtlView {

	@Bind
	@SaveState
	@Accessible
	public OaSalaryDouble selectedRowData = new OaSalaryDouble();

	@Action
	public void delLeava() {
		if (mPkVal == -1L) {
			MessageUtils.alert("当前数据已被删除,不能重复操作!");
			return;
		}
		this.serviceContext.baseInfoService().removeDate(this.selectedRowData
				.getId());
		MessageUtils.alert("OK!");
		this.addMaster();
	}

	@Override
	public void doServiceSaveMaster() {
		Long userinfoid = -1L;
		try {
			userinfoid = this.selectedRowData.getUserinfoid();
			String name = this.getNname(userinfoid);
			selectedRowData.setNamec(name);
		} catch (Exception e) {
			MessageUtils.alert("姓名不能为空!");
			return;
		}
		serviceContext.salaryDoubleService().saveData(this.selectedRowData);
		this.mPkVal = this.selectedRowData.getId();
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		MessageUtils.alert("ok");
	}

	public String getNname(Long id) {
		String sql = "SELECT namec FROM oa_userinfo WHERE isdelete = FALSE AND id = "
				+ id;
		Map map = this.serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		String name = (String) map.get("namec");
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
	}

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.salaryDoubleService().salaryDoubleDao
				.findById(mPkVal);
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMaster() {
		selectedRowData = new OaSalaryDouble();
		selectedRowData.setCurrency("CNY");
		mPkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

}
