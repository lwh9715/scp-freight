package com.scp.model.edi;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipBill;
import com.scp.model.ship.BusShipping;
import com.scp.model.ship.EdiTrans;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.edi.editransBean", scope = ManagedBeanScope.REQUEST)
public class EditransBean  extends GridView{
	
	@SaveState
	public String jobid;
	
	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();
	
	@SaveState
	@Accessible
	public BusShipping shipRowData = new BusShipping();
	
	@SaveState
	@Accessible
	public EdiTrans ediRowData = new EdiTrans();
	
	@Bind
	@SaveState
	public String bkagencode;
	
	@Bind
	@SaveState
	public String bkageninfo;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				String id = AppUtils.getReqParam("jobid");
				if(!StrUtils.isNull(id)) {
					this.jobid = id;
				}
				initjobs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void initjobs() {
		String id = AppUtils.getReqParam("jobid");
		if (!StrUtils.isNull(id)) {
			Long jobid = Long.parseLong(id);
			this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(jobid);
			if(this.selectedRowData == null){
				BusShipBill busShipBill = serviceContext.busShipBillMgrService.busShipBillDao.findById(jobid);
				if(busShipBill!=null){
					jobid = busShipBill.getJobid();
					this.jobid = busShipBill.getJobid().toString();  
					this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(jobid);
				}else{
					return;
				}
			}
			if("S".equals(selectedRowData.getJobtype())){
				this.shipRowData = serviceContext.busShippingMgrService.findByjobId(jobid);
				try {
					this.ediRowData = serviceContext.ediTransMgrService.findByjobId(jobid);
					
					if (shipRowData.getAgentid() != null && this.shipRowData.getAgentid() > 0) {
						SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(this.shipRowData.getAgentid());
						this.bkagencode = sysCorporation.getCode();
						this.bkageninfo = sysCorporation.getNamee()+"\n"+sysCorporation.getAddresse()+"\n"+sysCorporation.getContact()+"\n"+sysCorporation.getEmail1()+"\n"+sysCorporation.getTel1()+"\n"+sysCorporation.getFax1();
					}
				} catch (Exception e) {
					change();
					//MessageUtils.showException(e);
				}
			}
		} 
	}
	
	
	@Action
	public void saveshipper() {
		try {
			shipRowData.setCnorname(AppUtils.replaceStringByRegEx(shipRowData.getCnorname()));
			shipRowData.setCnortitle(AppUtils.replaceStringByRegEx(shipRowData.getCnortitle()));
			shipRowData.setCneename(AppUtils.replaceStringByRegEx(shipRowData.getCneename()));
			shipRowData.setCneetitle(AppUtils.replaceStringByRegEx(shipRowData.getCneetitle()));
			shipRowData.setNotifyname(AppUtils.replaceStringByRegEx(shipRowData.getNotifyname()));
			shipRowData.setNotifytitle(AppUtils.replaceStringByRegEx(shipRowData.getNotifytitle()));
			serviceContext.busShippingMgrService.saveData(shipRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "hblship");
		
	}
	
	
	
	@Action
	public void saveEditrans() {
		try {
			ediRowData.setCnorcode1(AppUtils.replaceStringByRegEx(ediRowData.getCnorcode1()));
			ediRowData.setCnorcode2(AppUtils.replaceStringByRegEx(ediRowData.getCnorcode2()));
			ediRowData.setCnorname(AppUtils.replaceStringByRegEx(ediRowData.getCnorname()));
			ediRowData.setCnoraddr(AppUtils.replaceStringByRegEx(ediRowData.getCnoraddr()));
			ediRowData.setCnorcontact(AppUtils.replaceStringByRegEx(ediRowData.getCnorcontact()));
			ediRowData.setCnortel(AppUtils.replaceStringByRegEx(ediRowData.getCnortel()));
			ediRowData.setCnoremail(AppUtils.replaceStringByRegEx(ediRowData.getCnoremail()));
			ediRowData.setCnorfax(AppUtils.replaceStringByRegEx(ediRowData.getCnorfax()));
			
			ediRowData.setCneecode1(AppUtils.replaceStringByRegEx(ediRowData.getCneecode1()));
			ediRowData.setCneecode2(AppUtils.replaceStringByRegEx(ediRowData.getCneecode2()));
			ediRowData.setCneename(AppUtils.replaceStringByRegEx(ediRowData.getCneename()));
			ediRowData.setCneeaddr(AppUtils.replaceStringByRegEx(ediRowData.getCneeaddr()));
			ediRowData.setCneecontact(AppUtils.replaceStringByRegEx(ediRowData.getCneecontact()));
			ediRowData.setCneetel(AppUtils.replaceStringByRegEx(ediRowData.getCneetel()));
			ediRowData.setCneeemail(AppUtils.replaceStringByRegEx(ediRowData.getCneeemail()));
			ediRowData.setCneefax(AppUtils.replaceStringByRegEx(ediRowData.getCneefax()));
			
			ediRowData.setNotifycode1(AppUtils.replaceStringByRegEx(ediRowData.getNotifycode1()));
			ediRowData.setNotifycode2(AppUtils.replaceStringByRegEx(ediRowData.getNotifycode2()));
			ediRowData.setNotifyname(AppUtils.replaceStringByRegEx(ediRowData.getNotifyname()));
			ediRowData.setNotifyaddr(AppUtils.replaceStringByRegEx(ediRowData.getNotifyaddr()));
			ediRowData.setNotifycontact(AppUtils.replaceStringByRegEx(ediRowData.getNotifycontact()));
			ediRowData.setNotifytel(AppUtils.replaceStringByRegEx(ediRowData.getNotifytel()));
			ediRowData.setNotifyemail(AppUtils.replaceStringByRegEx(ediRowData.getNotifyemail()));
			ediRowData.setNotifyfax(AppUtils.replaceStringByRegEx(ediRowData.getNotifyfax()));
			
			ediRowData.setAgencode1(AppUtils.replaceStringByRegEx(ediRowData.getAgencode1()));
			ediRowData.setAgencode2(AppUtils.replaceStringByRegEx(ediRowData.getAgencode2()));
			ediRowData.setAgenname(AppUtils.replaceStringByRegEx(ediRowData.getAgenname()));
			ediRowData.setAgenaddr(AppUtils.replaceStringByRegEx(ediRowData.getAgenaddr()));
			ediRowData.setAgencontact(AppUtils.replaceStringByRegEx(ediRowData.getAgencontact()));
			ediRowData.setAgentel(AppUtils.replaceStringByRegEx(ediRowData.getAgentel()));
			ediRowData.setAgenemail(AppUtils.replaceStringByRegEx(ediRowData.getAgenemail()));
			ediRowData.setAgenfax(AppUtils.replaceStringByRegEx(ediRowData.getAgenfax()));
			
			ediRowData.setBkagencode1(AppUtils.replaceStringByRegEx(ediRowData.getBkagencode1()));
			ediRowData.setBkagencode2(AppUtils.replaceStringByRegEx(ediRowData.getBkagencode2()));
			ediRowData.setBkagenname(AppUtils.replaceStringByRegEx(ediRowData.getBkagenname()));
			ediRowData.setBkagenaddr(AppUtils.replaceStringByRegEx(ediRowData.getBkagenaddr()));
			ediRowData.setBkagencontact(AppUtils.replaceStringByRegEx(ediRowData.getBkagencontact()));
			ediRowData.setBkagentel(AppUtils.replaceStringByRegEx(ediRowData.getBkagentel()));
			ediRowData.setBkagenemail(AppUtils.replaceStringByRegEx(ediRowData.getBkagenemail()));
			ediRowData.setBkagenfax(AppUtils.replaceStringByRegEx(ediRowData.getBkagenfax()));
			
			serviceContext.ediTransMgrService.saveData(ediRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "hblship");
		
	}
	
	@Action
	public void change() {
		try {
			ediRowData.setJobid(Long.parseLong(this.jobid));
			serviceContext.ediTransMgrService.saveData(ediRowData);
			serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_edi_trans_auto('jobid="+this.jobid+";')");
			this.ediRowData = serviceContext.ediTransMgrService.findByjobId(Long.parseLong(this.jobid));
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "hblship");
		update.markUpdate(true, UpdateLevel.Data, "mblship");
	}
	
	
	
	@Action
	public void saveshippermbl() {
		try {
			shipRowData.setCnortitlemblname(AppUtils.replaceStringByRegEx(shipRowData.getCnortitlemblname()));
			shipRowData.setCnortitlembl(AppUtils.replaceStringByRegEx(shipRowData.getCnortitlembl()));
			shipRowData.setCneetitlemblname(AppUtils.replaceStringByRegEx(shipRowData.getCneetitlemblname()));
			shipRowData.setCneetitlembl(AppUtils.replaceStringByRegEx(shipRowData.getCneetitlembl()));
			shipRowData.setNotifytitlemblname(AppUtils.replaceStringByRegEx(shipRowData.getNotifytitlemblname()));
			shipRowData.setNotifytitlembl(AppUtils.replaceStringByRegEx(shipRowData.getNotifytitlembl()));
			serviceContext.busShippingMgrService.saveData(shipRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mblship");
		
	}
	
	
	@Action
	public void saveEditransmbl() {
		try {
			ediRowData.setMblcnorcode1(AppUtils.replaceStringByRegEx(ediRowData.getMblcnorcode1()));
			ediRowData.setMblcnorcode2(AppUtils.replaceStringByRegEx(ediRowData.getMblcnorcode2()));
			ediRowData.setMblcnorname(AppUtils.replaceStringByRegEx(ediRowData.getMblcnorname()));
			ediRowData.setMblcnoraddr(AppUtils.replaceStringByRegEx(ediRowData.getMblcnoraddr()));
			ediRowData.setMblcnorcontact(AppUtils.replaceStringByRegEx(ediRowData.getMblcnorcontact()));
			ediRowData.setMblcnortel(AppUtils.replaceStringByRegEx(ediRowData.getMblcnortel()));
			ediRowData.setMblcnoremail(AppUtils.replaceStringByRegEx(ediRowData.getMblcnoremail()));
			ediRowData.setMblcnorfax(AppUtils.replaceStringByRegEx(ediRowData.getMblcnorfax()));
			
			ediRowData.setMblcneecode1(AppUtils.replaceStringByRegEx(ediRowData.getMblcneecode1()));
			ediRowData.setMblcneecode2(AppUtils.replaceStringByRegEx(ediRowData.getMblcneecode2()));
			ediRowData.setMblcneename(AppUtils.replaceStringByRegEx(ediRowData.getMblcneename()));
			ediRowData.setMblcneeaddr(AppUtils.replaceStringByRegEx(ediRowData.getMblcneeaddr()));
			ediRowData.setMblcneecontact(AppUtils.replaceStringByRegEx(ediRowData.getMblcneecontact()));
			ediRowData.setMblcneetel(AppUtils.replaceStringByRegEx(ediRowData.getMblcneetel()));
			ediRowData.setMblcneeemail(AppUtils.replaceStringByRegEx(ediRowData.getMblcneeemail()));
			ediRowData.setMblcneefax(AppUtils.replaceStringByRegEx(ediRowData.getMblcneefax()));
			
			ediRowData.setMblnotifycode1(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifycode1()));
			ediRowData.setMblnotifycode2(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifycode2()));
			ediRowData.setMblnotifyname(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifyname()));
			ediRowData.setMblnotifyaddr(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifyaddr()));
			ediRowData.setMblnotifycontact(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifycontact()));
			ediRowData.setMblnotifytel(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifytel()));
			ediRowData.setMblnotifyemail(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifyemail()));
			ediRowData.setMblnotifyfax(AppUtils.replaceStringByRegEx(ediRowData.getMblnotifyfax()));
			
			ediRowData.setMblagencode1(AppUtils.replaceStringByRegEx(ediRowData.getMblagencode1()));
			ediRowData.setMblagencode2(AppUtils.replaceStringByRegEx(ediRowData.getMblagencode2()));
			ediRowData.setMblagenname(AppUtils.replaceStringByRegEx(ediRowData.getMblagenname()));
			ediRowData.setMblagenaddr(AppUtils.replaceStringByRegEx(ediRowData.getMblagenaddr()));
			ediRowData.setMblagencontact(AppUtils.replaceStringByRegEx(ediRowData.getMblagencontact()));
			ediRowData.setMblagentel(AppUtils.replaceStringByRegEx(ediRowData.getMblagentel()));
			ediRowData.setMblagenemail(AppUtils.replaceStringByRegEx(ediRowData.getMblagenemail()));
			ediRowData.setMblagenfax(AppUtils.replaceStringByRegEx(ediRowData.getMblagenfax()));
			serviceContext.ediTransMgrService.saveData(ediRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mblship");
		
	}
	
	@Action
	public void changembl() {
		change();
	}
	
	
}
