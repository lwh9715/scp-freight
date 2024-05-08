package com.scp.view.module.salesmgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.salesmgr.analyzeoutBean", scope = ManagedBeanScope.REQUEST)
public class AnalyzeOutBean extends GridView {

	@Bind
	@SaveState
	public Date jobdatefm;

	@Bind
	@SaveState
	public Date jobdateto;

	@Bind
	@SaveState
	public String reportRpt = "F";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			getWeekDate();
		}
	}

	@Bind
	public UIIFrame showReportIframe;

	@Override
	public void grid_onrowselect() {
		if (jobdatefm == null || jobdateto == null) {
			MessageUtils.alert("检索条件不能为空!请检查");
			return;
		}
		String url = "";
		String raqname = "L".equalsIgnoreCase(reportRpt) ? "customer_dl.raq" : "customer_list.raq";
		url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/static/analyze/"+raqname + getArgs();
		showReportIframe.load(url);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("filter"," AND EXISTS (SELECT 1 FROM fina_jobs x WHERE a.id = x.customerid AND x.saleid = "
				+ AppUtils.getUserSession().getUserid() + ")");
		return map;
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(jobdatefm);
		String datTo = f1.format(jobdateto);
		String args = "&customerid=" + (this.getGridSelectId() == -1L && "F".equalsIgnoreCase(reportRpt) ? "" : Long.toString(this.getGridSelectId())) 
				+ "&jobdatefm=" + datfrom + "&jobdateto=" + datTo+"&userid="+AppUtils.getUserSession().getUserid();
		return args;
	}

	/**
	 * 获取本周日期范围
	 */
	public void getWeekDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 获得本周周一
		jobdatefm = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		jobdateto = cal.getTime();// 获得本周周末日期
	}
	
	@Action
	public void report() {
		grid_onrowselect();
	}
}
