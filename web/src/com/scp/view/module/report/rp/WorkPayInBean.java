package com.scp.view.module.report.rp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

@ManagedBean(name = "pages.module.report.rp.workpayinBean", scope = ManagedBeanScope.REQUEST)
public class WorkPayInBean {

	@Bind
	@SaveState
	public Date dateFrom;
	
	@Bind
	@SaveState
	public Date dateTo;
	
	@Bind
	@SaveState
	public String salesid = "";
	
	@Bind
	public String ppccType;
	
	@Bind
	public String typedata = "account";
	
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			dateFrom = new Date();
			dateTo = new Date();
		}
	}

	@Action
	public void report() {
		if (!check())
			return;
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/rp/jobrp.raq";
		AppUtils.openWindow("_jobsrpReport", openUrl + getArgs());
	}

	private boolean check() {
		if (dateFrom == null) {
			MessageUtils.alert("日期不能为空!");
			return false;
		}
		if (dateTo == null) {
			MessageUtils.alert("日期不能为空!");
			return false;
		}
		return true;
	}

	private String getArgs() {
		//AppUtils.debug("typedata：" + typedata);
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(dateFrom);
		String datTo = f1.format(dateTo);
		String arg =  "&salesid="+ salesid + "&dateFrom=" + datfrom + "&dateTo=" + datTo + "&ppcc="+ppccType + "&typedata=" + typedata;
		return arg;
	}
}
