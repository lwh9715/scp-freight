package com.scp.view.module.report.porfit;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

@ManagedBean(name = "pages.module.report.profit.profitluyunBean", scope = ManagedBeanScope.REQUEST)
public class ProfitLuyunBean {

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;

	@Bind
	public String hkdto = "*";

	@Bind
	public BigDecimal hkdrat;

	@Bind
	public String cnyto = "*";

	@Bind
	public BigDecimal cnyrat;

	@Bind
	public String dhsto = "*";

	@Bind
	public BigDecimal dhsrat;

	@Bind
	public String sarto = "*";

	@Bind
	public BigDecimal sarrat;

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
				+ "/reportJsp/showReport.jsp?raq=/static/profit/profit-luyun.raq";
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
		if (hkdrat == null || cnyrat == null || dhsrat == null
				|| sarrat == null) {
			MessageUtils.alert("兑换率不能为空!");
			return false;
		}
		return true;
	}

	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(dateFrom);
		String datTo = f1.format(dateTo);
		String arg = "&dateFrom=" + datfrom
				+ "&dateTo=" + datTo + "&hkd2usd=" + hkdrat + "&rmb2usd="
				+ cnyrat + "&dhs2usd=" + dhsrat + "&sr2usd=" + sarrat
				+ "&hkd2usd_xratetype=" + hkdto + "&rmb2usd_xratetype=" + cnyto
				+ "&dhs2usd_xratetype=" + dhsto + "&sr2usd_xratetype=" + sarto;
		return arg;
	}
}
