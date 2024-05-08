package com.scp.view.module.airtransport;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.bus.BusAir;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerConChooseBean;
@ManagedBean(name = "pages.module.airtransport.arrivalinfoBean", scope = ManagedBeanScope.REQUEST)
public class ArrivalinfoBean extends MastDtlView{
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@SaveState
	@Accessible
	public BusAir selectedRowData = new BusAir();
	
	@SaveState
	@Accessible
	public FinaJobs finajobs = new FinaJobs();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			refreshMasterForm();
			super.applyGridUserDef();
		}
	}
	
	@Override
	public void init(){
		String id = AppUtils.getReqParam("jobid").trim();
		if (!StrUtils.isNull(id) && StrUtils.isNumber(id)){
			this.pkVal = Long.parseLong(id);
			this.selectedRowData = this.serviceContext.busAirMgrService.findjobsByJobid(Long.parseLong(id));
			this.finajobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.parseLong(id));
			if(StrUtils.isNull(this.selectedRowData.getInputer2())){
				this.selectedRowData.setInputer2(AppUtils.getUserSession().getUsername());
			}
			if(this.selectedRowData.getArrivaltime() == null){
				this.selectedRowData.setArrivaltime(new Date());
			}
			getarrivalinfo();
		}
	}
	
	@Action
	public void saveMaster() {
		try {
			if (this.pkVal > 0) {
				this.serviceContext.busAirMgrService.busAirDao.modify(this.selectedRowData);
			} else {
				this.serviceContext.busAirMgrService.busAirDao.create(this.selectedRowData);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		
		this.refreshMasterForm();
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
	public void getarrivalinfo(){
		String sql = "SELECT f_bus_air_arrivalinfo('jobid="+this.pkVal+"') AS info";
		try{
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.get("info")!=null){
				this.selectedRowData.setInfo(m.get("info").toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	@Action
	public void refreshs(){
		getarrivalinfo();
		update.markUpdate(true, UpdateLevel.Data, "piece4");
		update.markUpdate(true, UpdateLevel.Data, "info");
	}
	
	
}
