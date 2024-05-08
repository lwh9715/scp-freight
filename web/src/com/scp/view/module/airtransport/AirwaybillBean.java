package com.scp.view.module.airtransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.base.CommonComBoxBean;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.bus.BusAir;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.airtransport.airwaybillBean", scope = ManagedBeanScope.REQUEST)
public class AirwaybillBean extends MastDtlView{
	
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
			this.billnoCombo = String.valueOf(this.billid);
		}
	}
	
	@Bind
	public UIIFrame airwaybillIframe;
	
	@Bind
	public UIWindow showAirwaybillWindow;
	
	@Bind
	@SaveState
	public String airmodel = "singletempate";
	
	@Bind(id = "airbillmodel")
	public List<SelectItem> getAirbillmodel() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='airmodel' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	@SaveState
	public String billnoCombo;
	
	@Bind(id = "billnos")
	public List<SelectItem> getBillnos() {
		try {
			String sql =
				"\nSELECT a.id AS value, mawbno AS lable FROM bus_air a WHERE jobid = "+this.pkVal+" AND isdelete = FALSE"+
				"\nUNION ALL"+
				"\nSELECT b.id AS value, b.hawbno AS lable FROM bus_air_bill b WHERE jobid = "+this.pkVal+" AND isdelete = FALSE";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			List<SelectItem> items = new ArrayList<SelectItem>();
	    	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
	    	if(list!=null){
	    		Object value = null;
	    		Object lable = null;
	    		for (Map dept : list) {
	    			lable = dept.get("lable");
	    			value = dept.get("value");
					items.add(new SelectItem(String.valueOf(value),
							String.valueOf(lable)));
	    		}	
	    	}
			return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	@Bind
	@SaveState
	public String datatype = "CD";
	
	@Action
	public void qryairbill() {
		if (airmodel == null || "".equals(airmodel)) {
			MessageUtils.alert("请选择格式！");
			return;
		};
//		airwaybillIframe.setSrc("/scp/common/blank.html");
//		airwaybillIframe.repaint();
		//System.out.println(billnoCombo);
		airwaybillIframe.setSrc("/scp/reportEdit/file/printairnew.jsp?rp="+airmodel+"&b="+this.billnoCombo+"&u="+this.userid+"&reporttype=airmodel&datatype="+datatype+"&jobid="+this.pkVal);
		airwaybillIframe.repaint();
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
