package com.scp.view.module.finance.report;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.trialbalanceBean", scope = ManagedBeanScope.REQUEST)
public class TrialbalanceBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String period;

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
			this.year = year;
			this.period = period;
			this.actlevel = "1";
			this.currency = "B";
			this.comid = 1l;
			
			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "period");
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
		if(year==null||period==null||actlevel==null||comid==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}

		String url = "";
		if("B".equals(currency)) {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/trialbalance_base.raq"
				+ getArgs();
		} else if("O".equals(currency)) {
			url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/trialbalance_org.raq"
				+ getArgs();
		} else {
			MessageUtils.alert("请选择币制!");
		}
		
		showReportIframe.load(url);
//		AppUtil.openWindow("trailbalance", url);
	}

	private String getArgs() {
		String args = "&year=" + year + "&period=" + period + "&actlevel=" + actlevel;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		return args;
	}

}
