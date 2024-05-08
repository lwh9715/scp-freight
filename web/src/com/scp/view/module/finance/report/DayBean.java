package com.scp.view.module.finance.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.base.MultiLanguageBean;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.dayBean", scope = ManagedBeanScope.REQUEST)
public class DayBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String datefm;

	@Bind
	@SaveState
	public String dateto;

	@Bind
	@SaveState
	public String actcodefm;

	@Bind
	@SaveState
	public String actcodeto;

	@Bind
	@SaveState
	public String actlevel;

	@Bind
	@SaveState
	public String currency;
	
	@Bind
	@SaveState
	public Long comid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			this.datefm = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			this.dateto = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			this.actlevel = "1";
			this.currency = "B";
			this.comid = 1l;
			

			update.markUpdate(true, UpdateLevel.Data, "datefm");
			update.markUpdate(true, UpdateLevel.Data, "dateto");
			update.markUpdate(true, UpdateLevel.Data, "actlevel");
			update.markUpdate(true, UpdateLevel.Data, "currency");
		}
	}

	@Bind
	public UIIFrame showReportIframe;
	
	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		if(datefm==null||dateto==null||actlevel==null||comid==null||StrUtils.isNull(actcodefm)||StrUtils.isNull(actcodeto)||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		
		if(datefm !=null ||!"".equals(datefm)||dateto !=null ||!"".equals(dateto)){
			 SimpleDateFormat date  = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if(date.format(date.parse(datefm)).split("-")[0].compareTo(date.format(date.parse(dateto)).split("-")[0])!=0){
					alert("不能跨年度查询!");
					return;
				}else if(date.parse(datefm).compareTo(date.parse(dateto))==1){
					alert(" 起始日期要在截止日期之前!");
					return;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		String url = "";
		if("B".equals(currency)) {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/day_base.raq"
				+ getArgs();
		} else {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/day_org_filter.raq"
				+ getArgs1();
		}

		showReportIframe.load(url);
//		AppUtil.openWindow("day", url);
	}

	private String getArgs() {
		String args = "&datefm=" + datefm + "&dateto=" + dateto + "&actcodefm="
				+ actcodefm + "&actcodeto=" + actcodeto + "&actlevel=" + actlevel;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		return args;
	}
	
	private String getArgs1() {
		String args = "&datefm=" + datefm + "&dateto=" + dateto + "&actcodefm="
				+ actcodefm + "&actcodeto=" + actcodeto + "&actlevel=" 
				+ actlevel + "&currency=" + currency;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		return args;
	}

	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	// 币制
	@Bind(id="fscurrency")
    public List<SelectItem> getFscurrency() {
    	try {
    		List<SelectItem> rs = CommonComBoxBean.getComboxItems("d.code","d.code","dat_currency AS d","","order by code");
    		rs.add(new SelectItem("B",l.find("综合本位币")));
			return rs;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	@Action(id="datefm",event="onchange")
	public void detafmChange(){
		if(datefm == null || datefm.isEmpty() || datefm.split("-") == null || datefm.split("-").length != 3){
			datefm = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		}
		String[] yearto = dateto.split("-");
		StringBuffer sb = new StringBuffer();
		sb.append(datefm.split("-")[0]);
		sb.append("-");
		sb.append(yearto[1]);
		sb.append("-");
		sb.append(yearto[2]);
		dateto = sb.toString();
	}
	
	@Action(id="dateto",event="onchange")
	public void datetoChange(){
		if(dateto == null || dateto.isEmpty() || dateto.split("-") == null || dateto.split("-").length != 3){
			dateto = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		}
		String[] yearto = datefm.split("-");
		StringBuffer sb = new StringBuffer();
		sb.append(dateto.split("-")[0]);
		sb.append("-");
		sb.append(yearto[1]);
		sb.append("-");
		sb.append(yearto[2]);
		datefm = sb.toString();
	}
}
