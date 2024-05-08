package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusFreightCharge;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.ship.busfreightchargeBean", scope = ManagedBeanScope.REQUEST)
public class BusfreightchargeBean extends GridFormView{

	@SaveState
	public BusFreightCharge data = new BusFreightCharge();
	
	@SaveState
	public Long jobid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack){
			String id = AppUtils.getReqParam("jobid");
			if (!StrUtils.isNull(id)){
				jobid = Long.parseLong(id);
			}else{
				jobid = -1L;
			}
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		map.put("qry",qry);
		map.put("jobid", ""+jobid+"");
		return map;
	}
	
	
	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.busCreightChargeMgrService.busFreightChargeDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busCreightChargeMgrService.saveData(data);
	}
	
	@Override
	public void add() {
		super.add();
		data = new BusFreightCharge();
		data.setJobid(this.jobid);
		data.setId(0l);
	}
	
	@Override
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.busCreightChargeMgrService.removeData(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
}
