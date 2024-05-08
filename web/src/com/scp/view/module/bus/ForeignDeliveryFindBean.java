package com.scp.view.module.bus;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.base.CommonComBoxBean;
import com.scp.model.ship.BusCustoms;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.bus.foreigndeliveryfindBean", scope = ManagedBeanScope.REQUEST)
public class ForeignDeliveryFindBean extends GridView {
	
	@Bind
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	public Long customcompanyid;
	
	@Bind
	@SaveState
	public Long customerid;
	
	@Bind
	@SaveState
	public Long customersid;
	
	@Bind
	@SaveState
	public String jobno;
	
	@Bind
	@SaveState
	public String clearancedatebe;
	
	@Bind
	@SaveState
	public String clearancedatest;
	
	@Bind
	@SaveState
	public String staircaseetastar;
	
	@Bind
	@SaveState
	public String staircaseetaend;
	
	@Bind
	@SaveState
	public boolean waterwet;
	
	@Bind
	@SaveState
	public boolean lessgoods;
	
	@Bind
	@SaveState
	public boolean moregoods;
	
	@Bind
	@SaveState
	public boolean stealgoods;
	
	@Bind
	@SaveState
	public boolean cabinet;
	
	@Bind
	@SaveState
	public boolean othergoods;
	
	@Bind
	@SaveState
	public Long salesid;
	
	@Bind
	@SaveState
	public String podv;
	
	@Bind
	@SaveState
	public String poa;
	
	@Bind
	@SaveState
	public String etastarte;
	
	@Bind
	@SaveState
	public String etaend;
	
	@Bind
	@SaveState
	public String etdbstarte;
	
	@Bind
	@SaveState
	public String etdend;
	
	@Bind
	@SaveState
	public String onboarddatestart;
	
	@Bind
	@SaveState
	public String onboarddateend;
	
	@Bind
	@SaveState
	public String clearingstate;
	
	@Bind
	@SaveState
	public Long company;
	
	@Bind
	@SaveState
	public String iscl;
	
	@Bind
	@SaveState
	public String ldtype;
	
	@Bind
	@SaveState
	public String destinationv;
	
	@Bind
	@SaveState
	public String cntno;
	
	@Bind
	@SaveState
	public String storehousedatestar;
	
	@Bind
	@SaveState
	public String storehousedatend;
	
	@Bind
	@SaveState
	public String jobdatestar;
	
	@Bind
	@SaveState
	public String jobdatend;
	
	
	
	

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			userid = AppUtils.getUserSession().getUserid();
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String findcustmer = "";
		if(customcompanyid!=null&&customcompanyid>0){
			findcustmer += "\n AND t.clearancecusid="+customcompanyid ;
		}
		if(customerid!=null&&customerid>0){
			findcustmer += "\n AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND customerid="+customerid +")";
		}
		if(customersid!=null&&customersid>0){//船公司
			findcustmer += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.jobid AND isdelete = FALSE AND carrierid="+customersid +")";
		}
		if(jobno!=null&&jobno!=""){
			findcustmer += "\n AND EXISTS(SELECT 1 FROM fina_jobs j WHERE j.id = t.jobid AND j.isdelete = FALSE AND j.nos LIKE '%"+jobno+"%')";
		}
		if(!StrUtils.isNull(clearancedatebe)||!StrUtils.isNull(clearancedatest)){
			findcustmer+= "\nAND t.clearancedate::DATE BETWEEN '"
			+ (StrUtils.isNull(clearancedatebe) ? "0001-01-01" : clearancedatebe)
			+ "' AND '"
			+ (StrUtils.isNull(clearancedatest) ? "9999-12-31" : clearancedatest)
			+ "'";
		}
		if(!StrUtils.isNull(staircaseetastar)||!StrUtils.isNull(staircaseetaend)){
			findcustmer+= "\nAND t.staircaseeta::DATE BETWEEN '"
			+ (StrUtils.isNull(staircaseetastar) ? "0001-01-01" : staircaseetastar)
			+ "' AND '"
			+ (StrUtils.isNull(staircaseetaend) ? "9999-12-31" : staircaseetaend)
			+ "'";
		}
		if(!StrUtils.isNull(storehousedatestar)||!StrUtils.isNull(clearancedatest)){
			findcustmer+= "\nAND t.storehousedate::DATE BETWEEN '"
			+ (StrUtils.isNull(storehousedatestar) ? "0001-01-01" : storehousedatestar)
			+ "' AND '"
			+ (StrUtils.isNull(storehousedatend) ? "9999-12-31" : storehousedatend)
			+ "'";
		}
		if(!StrUtils.isNull(jobdatestar)||!StrUtils.isNull(jobdatend)){
			findcustmer+= "\nAND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND jobdate::DATE BETWEEN '"
			+ (StrUtils.isNull(jobdatestar) ? "0001-01-01" : jobdatestar)
			+ "' AND '"
			+ (StrUtils.isNull(jobdatend) ? "9999-12-31" : jobdatend)
			+ "')";
		}
		
		
		if(waterwet){
			findcustmer+="\n AND t.waterwet ="+waterwet;
		}
		if(lessgoods){
			findcustmer+="\n AND t.lessgoods ="+lessgoods;
		}
		if(stealgoods){
			findcustmer+="\n AND t.stealgoods ="+stealgoods;
		}
		if(moregoods){
			findcustmer+="\n AND t.moregoods ="+moregoods;
		}
		if(cabinet){
			findcustmer+="\n AND t.cabinet ="+cabinet;
		}if(othergoods){
			findcustmer+="\n AND t.othergoods ="+othergoods;
		}
		if(salesid!=null&&salesid>0){
			findcustmer+= "\n AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.saleid ="+salesid+")";
		}
		if(!StrUtils.isNull(podv)){
			findcustmer += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.jobid AND isdelete = FALSE AND pod='"+podv +"')";
		}
		if(!StrUtils.isNull(destinationv)){
			findcustmer += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.jobid AND isdelete = FALSE AND destination='"+destinationv +"')";
		}
		if(!StrUtils.isNull(poa)){
			findcustmer += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.jobid AND isdelete = FALSE AND poa='"+poa +"')";
		}
		if(!StrUtils.isNull(etastarte)||!StrUtils.isNull(etaend)){
			findcustmer+= "\nAND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.jobid AND isdelete = FALSE AND ata::DATE BETWEEN '"
			+ (StrUtils.isNull(etastarte) ? "0001-01-01" : etastarte)
			+ "' AND '"
			+ (StrUtils.isNull(etaend) ? "9999-12-31" : etaend)
			+ "')";
		}
		if(!StrUtils.isNull(etdbstarte)||!StrUtils.isNull(etdend)){
			findcustmer+= "\nAND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.jobid AND isdelete = FALSE AND atd::DATE BETWEEN '"
			+ (StrUtils.isNull(etdbstarte) ? "0001-01-01" : etdbstarte)
			+ "' AND '"
			+ (StrUtils.isNull(etdend) ? "9999-12-31" : etdend)
			+ "')";
		}
		if(!StrUtils.isNull(onboarddatestart)||!StrUtils.isNull(onboarddateend)){
			findcustmer+= "\nAND EXISTS(SELECT 1 FROM bus_truck WHERE jobid = t.jobid AND isdelete = FALSE AND loadtime::DATE BETWEEN '"
			+ (StrUtils.isNull(onboarddatestart) ? "0001-01-01" : onboarddatestart)
			+ "' AND '"
			+ (StrUtils.isNull(onboarddateend) ? "9999-12-31" : onboarddateend)
			+ "')";
		}
		if(!StrUtils.isNull(clearingstate)){
			findcustmer+="\n AND t.clearingstate ='"+clearingstate+"'";
		}
		if(company!=null&&company>0){
			findcustmer+= "\n AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.corpid ="+company+")";
		}
		if(iscl!=null&&iscl.equals("1")){
			findcustmer+= "\n AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.isclc=FALSE)";
		}else if(iscl!=null&&iscl.equals("2")){
			findcustmer+= "\n AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.isclc=TRUE)";
		}
		if(ldtype!=null&&ldtype.equals("F")){
			findcustmer+= "\n AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.ldtype ='F')";
		}else if(ldtype!=null&&ldtype.equals("L")){
			findcustmer+= "\n AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.ldtype ='L')";
		}
		if(cntno!=null&&cntno!=""){//箱号
			findcustmer += "\n AND EXISTS(SELECT 1 FROM bus_ship_container WHERE jobid = t.jobid AND isdelete = FALSE AND cntno LIKE '%"+cntno+"%')";
		}
		
		m.put("findcustmer", findcustmer);
		return m;
	}
	
	@Override
	public void grid_ondblclick() {
		BusCustoms bc =serviceContext.busCustomsMgrService.busCustomsDao.findById(this.getGridSelectId());
		String winId = "_edit_jobs";
		String url = "/scp/pages/module/ship/jobsedit.xhtml?id=" + bc.getJobid();
		AppUtils.openWindow(winId, url);
	}
	
	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'ClearDelivery' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
}
