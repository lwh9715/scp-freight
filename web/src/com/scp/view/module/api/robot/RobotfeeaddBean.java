package com.scp.view.module.api.robot;

import java.math.BigDecimal;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.api.ApiRobotFeeadd;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.api.robot.robotfeeaddBean", scope = ManagedBeanScope.REQUEST)
public class RobotfeeaddBean extends GridFormView {

	@SaveState
	@Accessible
	public ApiRobotFeeadd selectedRowData = new ApiRobotFeeadd();

	@SaveState
	@Accessible
	public BigDecimal sum;
	
	@SaveState
	@Accessible
	public BigDecimal count;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			refreshamt();
		}
	}

	public void refreshamt() {
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("select count(*) from api_robot_esi where isok = true AND isdelete = false");
			String count1 = m.get("count").toString();
			count = new BigDecimal(count1);
			BigDecimal price = new BigDecimal(5);
			BigDecimal result1 = count.multiply(price);
			Map m1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("select sum(amt) as sumamt from  api_robot_feeadd where isdelete = false");
			String amtSum = m1.get("sumamt").toString();
			BigDecimal amt = new BigDecimal(amtSum);
			sum = amt.subtract(result1);
			update.markUpdate(true, UpdateLevel.Data, "gridPanel2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void add() {
		try {
			super.add();
			selectedRowData.setId(0);
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void refresh(){
		super.refresh();
		refreshamt();
	}

	@Override
	public void del() {
		if (!"demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())) {
			this.alert("无权删除!");
			return;
		}

		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.robotFeeaddService.removeDate(ids);
			MessageUtils.alert("OK!");
			this.grid.reload();
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.robotFeeaddService.robotFeeaddDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.robotFeeaddService.saveData(selectedRowData);
		this.alert("OK");
	}
}
