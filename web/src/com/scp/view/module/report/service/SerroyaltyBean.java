package com.scp.view.module.report.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

@ManagedBean(name = "pages.module.report.service.serroyaltyBean", scope = ManagedBeanScope.REQUEST)
public class SerroyaltyBean {

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;
	
	@Bind
	public Double hkd2usd;

	@Bind
	public Double rmb2usd;

	@Bind
	public Double dhs2usd;
	
	@Bind
	public Double sr2usd;

	@Bind
	public String hkd2usd_xratetype = "/";

	@Bind
	public String rmb2usd_xratetype = "/";

	@Bind
	public String dhs2usd_xratetype = "/";
	
	@Bind
	public String sr2usd_xratetype = "/";

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
				+ "/reportJsp/showReport.jsp?raq=/static/service/ser_royalty.raq";
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
		if (hkd2usd == null || hkd2usd <= 0) {
			MessageUtils.alert("兑换率不能为空!");
			return false;
		}
		if (rmb2usd == null || rmb2usd <= 0) {
			MessageUtils.alert("兑换率不能为空!");
			return false;
		}
		if (dhs2usd == null || dhs2usd <= 0) {
			MessageUtils.alert("兑换率不能为空!");
			return false;
		}if(sr2usd==null|| sr2usd <= 0){
			MessageUtils.alert("兑换率不能为空!");
			return false;
		}
		return true;
	}

	private String getArgs() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String arg = "&dateFrom="
				+ formatter.format(dateFrom) + "&dateTo="
				+ formatter.format(dateTo);
		arg += "&hkd2usd=" + hkd2usd + "&rmb2usd=" + rmb2usd + "&dhs2usd="
				+ dhs2usd+"&sr2usd=" + sr2usd;
		arg += "&hkd2usd_xratetype=" + hkd2usd_xratetype
				+ "&rmb2usd_xratetype=" + rmb2usd_xratetype
				+ "&dhs2usd_xratetype=" + dhs2usd_xratetype
				+ "&sr2usd_xratetype=" + sr2usd_xratetype;
		return arg;
	}
}
