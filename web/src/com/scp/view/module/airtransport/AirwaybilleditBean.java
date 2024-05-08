package com.scp.view.module.airtransport;

import java.util.List;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.base.CommonComBoxBean;
import com.scp.model.bus.BusAir;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.airtransport.airwaybilleditBean", scope = ManagedBeanScope.REQUEST)
public class AirwaybilleditBean extends MastDtlView{

	@SaveState
	@Accessible
	public BusAir selectedRowData = new BusAir();
	
	@SaveState
	@Bind
	public Long userid;
	
	@SaveState
	@Bind
	public Long billid;
	
	@SaveState
	@Bind
	public Long hawbnoid;
	
	@SaveState
	@Bind
	public String jobno;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			this.userid = AppUtils.getUserSession().getUserid();
			refreshMasterForm();
			super.applyGridUserDef();
			qryairbill();
		}
	}

	@Override
	public void init(){
		String id = AppUtils.getReqParam("jobid").trim();
		if (!StrUtils.isNull(id) && StrUtils.isNumber(id)){
			this.pkVal = Long.parseLong(id);
			this.selectedRowData = this.serviceContext.busAirMgrService.findjobsByJobid(Long.parseLong(id));
			this.jobno = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.parseLong(id)).getNos();
			this.billid = selectedRowData.getId();
			this.hawbnoid = selectedRowData.getId();
		}
	}
	
	@Bind
	public UIIFrame airwaybillIframe;
	
	@Bind
	@SaveState
	public String datatype = "CD";
	
	@Action
	public void qryairbill() {
		String id = AppUtils.getReqParam("airbillid");
		if(id!="" && id != null){
			this.hawbnoid = Long.parseLong(id);
		}
		airwaybillIframe.load("/scp/reportEdit/file/printairedit.jsp?rp=HAB.raq&b="+this.hawbnoid+"&u="+this.userid+"&reporttype=airmodel&datatype="+datatype+"&datain=datain&jobid="+this.pkVal);
	}
	
	
	@Bind(id="hawbnos")
    public List<SelectItem> getHawbnos() {
    	try {
    		List<SelectItem> lista = CommonComBoxBean.getComboxItems("id","mawbno","bus_air AS d","WHERE isdelete = false and jobid = "+this.pkVal+"","ORDER BY mawbno");
			List<SelectItem> listb = CommonComBoxBean.getComboxItems("id","hawbno","bus_air_bill AS d","WHERE isdelete = false and jobid = "+this.pkVal+"","ORDER BY hawbno");
			lista.addAll(listb);
			return lista;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
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
}
