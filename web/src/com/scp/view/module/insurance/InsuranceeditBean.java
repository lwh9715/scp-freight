package com.scp.view.module.insurance;

import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bus.BusInsurance;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.insurance.insuranceeditBean", scope = ManagedBeanScope.REQUEST)
public class InsuranceeditBean extends MastDtlView{
	
	@SaveState
	@Accessible
	public BusInsurance selectedRowData = new BusInsurance();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			refreshMasterForm();
		}
	}
	
	
	@Override
	public void init(){
		String jobid = AppUtils.getReqParam("jobid").trim();
		if (!StrUtils.isNull(jobid) && StrUtils.isNumber(jobid)){
			this.pkVal = Long.parseLong(jobid);
			this.selectedRowData = this.serviceContext.datInsuranceService.findByjobId(Long.parseLong(jobid));
		}
	}
	
	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub
		
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
	public void saveMaster() {
		try {
			if (this.pkVal > 0) {
				this.serviceContext.datInsuranceService.datInsurancetDao.modify(this.selectedRowData);
			} else {
				this.serviceContext.datInsuranceService.datInsurancetDao.create(this.selectedRowData);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refreshMasterForm();
	}
	
	@Action
	public void submitinsurance() {
		try {
			InsuranceUtils.sendInsurance(this.pkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refreshMasterForm();
	}
	
	@SaveState
	@Bind
	public String orderInfo;
	
	@Action
	public void showOrderInfo() {
		try {
			JSONObject jsonObject = InsuranceUtils.getOrderInfo("82020102152564910113");
			orderInfo = jsonObject.getString("data").toString();
			//System.out.println("orderInfo:"+orderInfo);
			update.markUpdate(true, UpdateLevel.Data, "orderInfo");
			Browser.execClientScript("showOrderInfo()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
