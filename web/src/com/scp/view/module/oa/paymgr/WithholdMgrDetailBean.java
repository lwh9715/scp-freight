package com.scp.view.module.oa.paymgr;

import java.util.Date;
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

import com.scp.model.oa.OaFee;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.paymgr.withholdmgrdetailBean", scope = ManagedBeanScope.REQUEST)
public class WithholdMgrDetailBean extends MastDtlView {

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
	public OaFee selectedRowData = new OaFee();

	@Action
	public void delLeava() {
		this.serviceContext.oaFeeService().removeDate(this.selectedRowData
				.getId());
		MessageUtils.alert("OK!");
		this.addMaster();
	}

	@Override
	public void doServiceSaveMaster() {
		try {
			Long userinfoid = this.selectedRowData.getUserinfoid();
			String msg = getJobNo(userinfoid);
			selectedRowData.setJobno(msg.split(",")[0]);
			selectedRowData.setDutyer(msg.split(",")[1]);
		} catch (Exception e) {
			MessageUtils.alert("责任人不能为空!");
			return;
		}
		serviceContext.oaFeeService().saveData(this.selectedRowData);
		this.mPkVal = this.selectedRowData.getId();
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		MessageUtils.alert("ok");
	}
	
	public String getJobNo(Long userinfoid){
		String sql = "SELECT jobno,namec FROM oa_userinfo WHERE isdelete = FALSE AND id = " + userinfoid;
		 Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		  String jobno =(String)map.get("jobno");
		  String namec =(String)map.get("namec");
		  String message = jobno + "," + namec;
		  return message;
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
		;
		this.selectedRowData = serviceContext.oaFeeService().oaFeeDao
				.findById(mPkVal);
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMaster() {
		selectedRowData = new OaFee();
		selectedRowData.setCurrency("CNY");
		selectedRowData.setFeedate(new Date());
	}

}
