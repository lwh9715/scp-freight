package com.scp.view.module.report.arap;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@ManagedBean(name = "pages.module.report.arap.receivablecustomerBean", scope = ManagedBeanScope.REQUEST)
public class ReceivableCustomerBean {

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;

	@Bind
	public String jobno_prefix;

	@Bind
	public String ppccType;

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
				+ "/reportJsp/showReport.jsp?raq=/static/arap/ap-all-custom.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
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
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(dateFrom);
		String datTo = f1.format(dateTo);
		String arg = "&ppccType=" + ppccType + "&dateFrom=" + datfrom
				+ "&dateTo=" + datTo;
		if (!StrUtils.isNull(jobno_prefix)) {
			String[] prefixs = jobno_prefix.split("\n");
			for (int i = 0; i < prefixs.length; i++) {
				arg += "&jobno" + (i + 1) + "=" + prefixs[i];
			}
		}
		return arg;
	}
}
