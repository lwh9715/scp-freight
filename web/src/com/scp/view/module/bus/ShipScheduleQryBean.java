package com.scp.view.module.bus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;

import com.scp.base.CommonComBoxBean;
import com.scp.service.bus.ShipScheduleService;
import com.scp.util.AppUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.bus.shipscheduleqryBean", scope = ManagedBeanScope.REQUEST)
public class ShipScheduleQryBean extends GridFormView {
	
	@ManagedProperty("#{shipScheduleService}")
	public ShipScheduleService shipScheduleService;
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}
	
	@Bind
	@SaveState
	public UICombo shipLineCom;
	
	@Bind
	@SaveState
	public UICombo carrierCom;
	
	@Bind
	@SaveState
	public UICombo polCom;
	
	@Bind
	@SaveState
	public UICombo yearCom;
	
	@Bind
	@SaveState
	public UICombo monthCom;
	
	
	@Action
	public void showReport() {
		String rpturl = AppUtils.getRptUrl(); 
		StringBuffer args = new StringBuffer();
		String shipline = shipLineCom.getValue().toString();
		if(StrUtils.isNull(shipline)) {
			MessageUtils.alert("Please choose shipping line first!");
			return;
		}
		
		args.append("&");
		String carrier = carrierCom.getValue().toString();
		if(StrUtils.isNull(carrier)) {
			args.append("carrier=%25");
		}else {
			args.append("carrier=%25"+carrier+"%25");
		}
		args.append("&");
		
		String pol = polCom.getValue().toString();
		if(StrUtils.isNull(pol)) {
			args.append("pol=%25");
		}else {
			args.append("pol=%25"+pol+"%25");
		}
		args.append("&");
		
		String year = yearCom.getValue().toString();
		if(StrUtils.isNull(year)) {
			args.append("yearno=%25");
		}else {
			args.append("yearno=%25"+year+"%25");
		}
		args.append("&");
		
		String month = monthCom.getValue().toString();
		if(StrUtils.isNull(month)) {
			args.append("monthno=%25");
		}else {
			args.append("monthno=%25"+month);
		}
		args.append("&");
		
		String ves = StrUtils.getMapVal(this.qryMap, "ves");
		if(StrUtils.isNull(ves)) {
			args.append("ves=%25");
		}else {
			args.append("ves=%25"+ves+"%25");
		}
		args.append("&");
		
		String voy = StrUtils.getMapVal(this.qryMap, "voy");
		if(StrUtils.isNull(voy)) {
			args.append("voy=%25");
		}else {
			args.append("voy=%25"+voy+"%25");
		}
		
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/schedule.raq&isencode=N&shipline="+shipline+args.toString();
		AppUtils.openWindow("_shceduleReport", openUrl , 800 , 600);
	}
	
	@Bind
	@SaveState
	public Date cls_start;
	
	@Bind
	@SaveState
	public Date cls_end;
	
	@Bind
	@SaveState
	public Date etd_start;
	
	@Bind
	@SaveState
	public Date etd_end;
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			Calendar cal = Calendar.getInstance();//当前日期
			
			Calendar cal_temp = Calendar.getInstance();
			cal_temp.clear();
			cal_temp.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+2,1);
			cal_temp.add(Calendar.DAY_OF_MONTH,-1);//减一天
			
			cls_start = cal.getTime();
			cls_end = cal_temp.getTime();
			
			etd_start = cal.getTime();
			etd_end = cal_temp.getTime();
			
		}
		super.beforeRender(isPostBack);
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = (String) map.get("qry");
		if(cls_start != null && cls_end != null && etd_start != null && etd_end != null) {
			qry += "\n AND ((cls between '"+DateTimeUtil.getFormatedDateString("GMT+8:00" , cls_start)+"' AND '"+DateTimeUtil.getFormatedDateString("GMT+8:00" , cls_end)+"')";
			qry += "\n OR (etd between '"+DateTimeUtil.getFormatedDateString("GMT+8:00" , etd_start)+"' AND '"+DateTimeUtil.getFormatedDateString("GMT+8:00" , etd_end)+"'))";
		}
		//AppUtils.debug(qry);
		
		//AppUtils.debug("$$$$:"+DateFormat.getDateInstance().format(cls_start));
		
		map.put("qry", qry);
		return map;
	}


	@Action
	public void query() {
		qryRefresh();
	}
	
	@Bind(id="shipLine")
    public List<SelectItem> getShipLine() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT shipline","shipline","bus_shipschedule AS d","WHERE isdelete = false","ORDER BY shipline");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="carrier")
    public List<SelectItem> getCarrier() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT carrier","carrier","bus_shipschedule AS d","WHERE isdelete = false","ORDER BY carrier");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="pol")
    public List<SelectItem> getPol() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT right(pol,2) ","right(pol,2) ","bus_shipschedule AS d","WHERE isdelete = false AND pol IS NOT NULL","ORDER BY value");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Bind(id="yearno")
    public List<SelectItem> getYearno() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT yearno","yearno","bus_shipschedule AS d","WHERE isdelete = false","ORDER BY yearno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="monthno")
    public List<SelectItem> getMonthno() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT monthno","monthno","bus_shipschedule AS d","WHERE isdelete = false ","ORDER BY monthno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }



	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}
}
