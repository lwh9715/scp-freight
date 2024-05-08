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

@ManagedBean(name = "pages.module.salesmgr.workreportBean", scope = ManagedBeanScope.REQUEST)
public class WorkReportBean extends GridView {

	@Bind
	@SaveState
	public Date jobdatefm;

	@Bind
	@SaveState
	public Date jobdateto;
	
	@Bind
	@SaveState
	public String reportRpt = "F";
	
	@Bind
	@SaveState
	public String datetype = "J";

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
		if(reportRpt.equals("F")){
			if(datetype.equals("J")){
				url = AppUtils.getRptUrl()
						+ "/reportJsp/showReport.jsp?raq=/static/analyze/WORK_REPORT.raq"
						+ getArgs();
			}else if(datetype.equals("E")){
				url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/static/analyze/WORK_REPORT_ETD.raq"
				+ getArgs();
			}
			showReportIframe.load(url);
		}else if(reportRpt.equals("L")){
			if(datetype.equals("J")){
				url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/static/analyze/WORK_REPORT_LCL.raq"
				+ getArgs();
			}else if(datetype.equals("E")){
				url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/static/analyze/WORK_REPORT_LCL_ETD.raq"
				+ getArgs();
			}
			showReportIframe.load(url);
		}
		
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map
				.put(
						"filter",
						" AND EXISTS (SELECT 1 FROM fina_jobs x WHERE a.id = x.customerid )"
						//关联的业务员的，都能看到
						+ "\n	AND EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_user y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = a.salesid AND z.isdelete = false) "
						+ ")"
								);
		return map;
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(jobdatefm);
		String datTo = f1.format(jobdateto);
		String args = "&customerid=" + Long.toString(this.getGridSelectId())
				+ "&jobdatefm=" + datfrom + "&jobdateto=" + datTo + "&userCorpid=" + AppUtils.getUserSession().getCorpid();
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
	public void report(){
		grid_onrowselect();
	}

	@Override
	public void grid_ondblclick() {
		grid_onrowselect();
	}
	
	
}
