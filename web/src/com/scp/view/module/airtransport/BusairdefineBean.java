package com.scp.view.module.airtransport;

import java.util.List;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.model.bus.BusAir;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
@ManagedBean(name = "pages.module.airtransport.busairdefineBean", scope = ManagedBeanScope.REQUEST)
public class BusairdefineBean extends FormView{
	
	@SaveState
	@Accessible
	public BusAir selectedRowData = new BusAir();
	
	@SaveState
	@Bind
	public String billid;

	@SaveState
	@Bind
	public Long userid;
	
	@Bind
	@SaveState
	public String control;
	
	@SaveState
	public String language;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("id");
		initControl();
		this.userid = AppUtils.getUserSession().getUserid();
		update.markUpdate(UpdateLevel.Data, "userid");
		language = AppUtils.getUserSession().getMlType().toString();
		if(!StrUtils.isNull(id)){
			billid = id;
			selectedRowData = this.serviceContext.busAirMgrService.busAirDao.findById(Long.parseLong(billid));
			
			this.refresh();
		}
	}	
	
	@Bind
	@SaveState
	public String hblRpt;
	
	public void initControl(){
		this.control = AppUtils.base64Encoder("false");
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_ship_bill.getValue())) {
			if (s.endsWith("_templete")) {
				this.control = AppUtils.base64Encoder("true");
			}
		}
		update.markUpdate(UpdateLevel.Data,"control");	
	}
	
	@Action
	public void savelastbill(){
		//String rpturl = AppUtils.getRptUrl();
		//String upg = "UPDATE bus_shipping set hblrpt = '" + hblRpt + "' where id = " + billid + ";";
		//this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
	}
	
	
	@SaveState
	@Bind
	public String mblRpt;
	
	@SaveState
	@Bind
	public String orderRpt;
	
	@SaveState
	@Bind
	public String bookRpt;
	
	private String getArgs() {
		String args = "";
		args += "&id=" + this.billid;
		return args;
	}
	
	@Bind(id = "hblrpt")
	public List<SelectItem> getHblrpt() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='air' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind(id = "mblrpt")
	public List<SelectItem> getMblrpt() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='airmbl' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Override
	public void save() {
		super.save();
		this.serviceContext.busAirMgrService.saveData(this.selectedRowData);
	}
	
	@Bind(id = "orderrpt")
	public List<SelectItem> getOrderrpt() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='airorder' AND isdelete = FALSE",
			"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	@Action
	public void printOrder() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ orderRpt;
		String url = openUrl + getArgsusr();
		AppUtils.openWindow("_airOrderReport",url);
	}
	private String getArgsusr() {
		BusAir bs = serviceContext.busAirMgrService.busAirDao.findById(Long.parseLong(billid));
		String args = "&para=jobid="+bs.getJobid()+":userid="+AppUtils.getUserSession().getUserid()+":corpidcurrent="+AppUtils.getUserSession().getCorpid()+":";
		args = args+"&id=" + this.billid;
		return args;
	}
}
