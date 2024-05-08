package com.scp.view.module.finance.report;

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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.accountbalancesheetBean", scope = ManagedBeanScope.REQUEST)
public class AccountbalancesheetBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String yearfm;

	@Bind
	@SaveState
	public String periodfm;

	@Bind
	@SaveState
	public String yearto;

	@Bind
	@SaveState
	public String periodto;

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
	
	@Bind
	@SaveState
	public Boolean hideAst = true;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			
			// 根据当前登录用户，找对应的帐套，过滤兑换率列表
			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			String year = "";
			String period = "";
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				year = m.get("year").toString();
				period = m.get("period").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.yearfm = year;
			this.yearto = year;
			this.periodfm = period;
			this.periodto = period;
			this.actlevel = "1";
			this.currency = "*";
			this.comid = 1l;
			
			update.markUpdate(true, UpdateLevel.Data, "yearfm");
			update.markUpdate(true, UpdateLevel.Data, "yearto");
			update.markUpdate(true, UpdateLevel.Data, "periodfm");
			update.markUpdate(true, UpdateLevel.Data, "periodto");
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
		if(yearfm==null||yearto==null||actlevel==null||comid==null||periodfm==null||periodto==null||StrUtils.isNull(actcodefm)||StrUtils.isNull(actcodeto)||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		
		if(yearfm!=null ||!"".equals(yearfm)|| yearto!=null ||!"".equals(yearto)){
			if(!(yearfm).equals(yearto)){
				alert("不能跨年度查询!");
				return;
			}
		}
		
		if(periodfm !=null &&!"".equals(periodfm) && periodto !=null &&!"".equals(periodto)){
			if(Long.valueOf(periodfm).compareTo(Long.valueOf(periodto))==1){
				alert(" 起始日期要在截止日期之前!");
				return;
			}
		}
		
		String url = "";
		if("B".equals(currency)) {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/accountbalance_base.raq"
				+ getArgs();
		} else if("*".equals(currency)) {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/accountbalance_org.raq"
				+ getArgs();
		} else {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/accountbalance_org_filter.raq"
				+ getArgs1();
		}
		showReportIframe.load(url);
//		AppUtil.openWindow("accountbalance", url);
	}

	private String getArgs() {
		String args = "&yearfm=" + yearfm + "&yearto=" + yearto + "&periodfm="
				+ periodfm + "&periodto=" + periodto + "&actcodefm=" + actcodefm
				+ "&actcodeto=" + actcodeto + "&actlevel=" + actlevel;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		args+="&hideAst="+this.hideAst;
		return args;
	}
	
	private String getArgs1() {
		String args = "&yearfm=" + yearfm + "&yearto=" + yearto + "&periodfm="
				+ periodfm + "&periodto=" + periodto + "&actcodefm=" + actcodefm
				+ "&actcodeto=" + actcodeto + "&actlevel=" + actlevel + "&currency="
				+ currency;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		args+="&hideAst="+this.hideAst;
		return args;
	}
	
	// 币制
	@Bind(id="fscurrency")
    public List<SelectItem> getFscurrency() {
    	try {
    		List<SelectItem> rs = CommonComBoxBean.getComboxItems("d.code","d.code","_fs_currency AS d","","order by code");
    		rs.add(new SelectItem("B","本位币"));
			return rs;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Action(id="yearfm",event="onchange")
	public void yearfmChange(){
		this.yearto = this.yearfm;
	}
}
