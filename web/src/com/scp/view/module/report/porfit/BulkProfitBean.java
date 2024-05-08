package com.scp.view.module.report.porfit;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@ManagedBean(name = "pages.module.report.profit.bulkprofitBean", scope = ManagedBeanScope.REQUEST)
public class BulkProfitBean {
	
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;

	@Bind
	public Date dateFrom;

	@Bind
	public Date dateTo;

	@Bind
	public String hkdto = "*";

	@Bind
	public BigDecimal hkdrat;

	@Bind
	public String usdto = "*";

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
	
	@Bind
	@SaveState
	public String reporttype = "profit-amend.raq";

	@Action
	public void report() {
		if (!check())
			return;
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/profit/"+reporttype;
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
	}
	
	
	
	@Action
	public void getRate() {
		Calendar cal=Calendar.getInstance();//使用日历类
		cal.setTime(dateTo);
		int year=cal.get(Calendar.YEAR);//得到年
		int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		String sql = "\nSELECT "+
					"\n		currencyfm,xtype,rate"+ 
					"\nFROM fs_xrate x , fs_actset a"+
					"\nWHERE x.actsetid  = a.id "+
					"\nAND a.isbase = true"+
					"\nAND x.isdelete = false"+
					"\nAND a.isdelete = false"+
					"\nAND x.periodid = (SELECT id FROM fs_period z WHERE actsetid = a.id AND z.year = "+year+" AND z.period = "+month+" AND z.isdelete = false);";
		List<Map> lists = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		for (Map map : lists) {
			String currencyfm = StrUtils.getMapVal(map, "currencyfm");
			String xtype = StrUtils.getMapVal(map, "xtype");
			String rate = StrUtils.getMapVal(map, "rate");
			if("HKD".equals(currencyfm)){
				this.hkdto = xtype;
				this.hkdrat = new BigDecimal(rate);
			}
			if("USD".equals(currencyfm)){
				this.usdto = xtype;
				this.cnyrat = new BigDecimal(rate);		
			}
			if("SAR".equals(currencyfm)){
				this.sarto = xtype;
				this.sarrat = new BigDecimal(rate);		
			}
			if("DHS".equals(currencyfm)){
				this.dhsto = xtype;
				this.dhsrat = new BigDecimal(rate);		
			}
		}
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
		+ "&hkd2usd_xratetype=" + hkdto + "&rmb2usd_xratetype=" + usdto
		+ "&dhs2usd_xratetype=" + dhsto + "&sr2usd_xratetype=" + sarto;
		return arg;
	}
}
