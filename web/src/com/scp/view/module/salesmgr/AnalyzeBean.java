package com.scp.view.module.salesmgr;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.salesmgr.analyzeBean", scope = ManagedBeanScope.REQUEST)
public class AnalyzeBean extends GridView {
	
	@Bind
	@SaveState
	public Date jobdatefm;

	@Bind
	@SaveState
	public Date jobdateto;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			getWeekDate();
//			actsetcode = AppUtil.getActsetcode();
//
//			// 根据当前登录用户，找对应的帐套，过滤兑换率列表
//			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
//					+ AppUtil.getUserSession().getActsetid();
//			Map m;
//			String year = "";
//			String period = "";
//			try {
//				m = this.serviceContext.daoIbatisTemplate
//						.queryWithUserDefineSql4OnwRow(sql);
//				year = m.get("year").toString();
//				period = m.get("period").toString();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			this.year = year;
//			this.period = period;
//			this.actlevel = "1";
//			this.currency = "B";
//			this.comid = 1l;
//			update.markUpdate(true, UpdateLevel.Data, "jobdatefm");
//			update.markUpdate(true, UpdateLevel.Data, "jobdateto");
		}
	}

	@Bind
	public UIIFrame showReportIframe;

	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		if (jobdatefm == null || jobdateto == null) {
			MessageUtils.alert("检索条件不能为空!请检查");
			return;
		}

		String url = "";
		url = AppUtils.getRptUrl()
		+ "/reportJsp/showReport.jsp?raq=/static/analyze/top_ph.raq"
		+ getArgs();
//		if ("B".equals(currency)) {
//			url = AppUtil.getContextPath()
//					+ "/reportJsp/showReport.jsp?raq=/finance/trialbalance_base.raq"
//					+ getArgs();
//		} else if ("O".equals(currency)) {
//			url = AppUtil.getContextPath()
//					+ "/reportJsp/showReport.jsp?raq=/finance/trialbalance_org.raq"
//					+ getArgs();
//		} else {
//			MsgUtil.alert("请选择币制!");
//		}

		showReportIframe.load(url);
		// AppUtil.openWindow("trailbalance", url);
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(jobdatefm);
		String datTo = f1.format(jobdateto);
		String args = "&jobdatefm=" + datfrom + "&jobdateto=" + datTo+"&saleid="+AppUtils.getUserSession().getUserid();
		return args;
	}
	/**
	 * 获取本周日期范围
	 */
	
	public void getWeekDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//获得本周周一
		jobdatefm = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		jobdateto = cal.getTime();// 获得本周周末日期
	}

}
