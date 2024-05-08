package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipjoin;
import com.scp.model.ship.BusShipjoinlink;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipbillcopychoosemblBean", scope = ManagedBeanScope.REQUEST)
public class ShipBillCopyChoosemblBean extends GridView {

	@SaveState
	@Accessible
	public BusShipjoinlink busShipLink = new BusShipjoinlink();

	@SaveState
	@Accessible
	public BusShipjoin busShipjoin = new BusShipjoin();

	@SaveState
	@Accessible
	public Long jobid;
	
	@SaveState
	@Accessible
	public String type;
	
	@SaveState
	@Accessible
	public String pod;
	
	@SaveState
	@Accessible
	public String customerabbr;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("jobid").trim();
			type = AppUtils.getReqParam("type");
			jobid = Long.valueOf(code);
			Map m = this.getCustomerabbrpod(jobid);
			this.pod = (String)m.get("pod");
			this.customerabbr = (String)m.get("customerabbr");
			this.qryMap.put("customerabbr",customerabbr);
			this.qryMap.put("pod",pod);
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		}
	}

	@Action
	public void copybill() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		} else if (ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		if(this.getConsistency(ids)==true){
			 serviceContext.busShipBillMgrService.copybill(type,ids,this.jobid,getshipid(this.jobid));
		     MessageUtils.alert("OK");
		     refresh();
			}
	}

	public Long getshipid(Long id) {
		String sql = "SELECT id FROM bus_shipping where isdelete =false AND jobid= "
				+ id;
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("id");

	}
	
	public Map  getCustomerabbrpod(Long id){
		String sql="SELECT customerabbr,pod FROM bus_shipping where isdelete =false AND jobid= "+id;
		return serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
	}
	
	/**
	 * 获取所选择复制的单号的pod,customerabbr
	 */
	public Map getCopyCustomerabbrpod (String ids[]){
		String sql="SELECT t.pod,b.customerabbr FROM bus_shipping b,bus_ship_bill t where t.jobid = b.jobid AND  b.isdelete = FALSE AND  t.isdelete =false AND t.id= "+Long.valueOf(ids[0]);
		return  serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
	}
	
	/**
	 * 比较当前工作单号与复制单号所对应pod，customerabbr是否一致
	 */
	public boolean getConsistency(String ids[]){
		Map m = this.getCopyCustomerabbrpod(ids);
		String copypod = (String) m.get("pod");
		String copycustmer = (String) m.get("customerabbr");
		if (this.pod == null || "".equals(this.pod)) {
			alert("委托界面下目的港信息为空,请填写完整再复制新增!");
			return false;
		} else {
			if (this.pod.contains(copypod) == false
					&& copypod.contains(this.pod) == false
					&& (this.customerabbr.equals(copycustmer) == false)) {
				alert("选择复制的提单号与当前单号目的港，委托人不一致！");
			} else if (this.pod.contains(copypod) == false
					&& copypod.contains(this.pod) == false
					&& (this.customerabbr.equals(copycustmer) == true)) {
				alert("选择复制的提单号与当前单号目的港不一致！");
			} else if ((this.pod.contains(copypod) == true || copypod
					.contains(this.pod) == true)
					&& (this.customerabbr.equals(copycustmer) == false)) {
				alert("选择复制的提单号与当前单号委托人不一致！");
			}
			return true;
		}
	}
}
