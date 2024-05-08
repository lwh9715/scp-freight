	package com.scp.view.module.report.porfit;

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

	@ManagedBean(name = "pages.module.report.profit.grossBean", scope = ManagedBeanScope.REQUEST)
	public class GrossBean {

		@Bind
		public Date dateFrom;
		
		@Bind
		public Date dateTo;
		
		@Bind
		@SaveState
		public Long comid;
		
		@BeforeRender
		public void beforeRender(boolean isPostback) {
			if (!isPostback) {
				dateFrom = new Date();
				dateTo = new Date();
				this.comid = 1L;
			}
		}

		@Action
		public void reportin() {
			if (!check())
				return;
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/static/profit/incomelist.raq";
			AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
		}
		
		@Action
		public void reportcost() {
			if (!check())
				return;
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/static/profit/costlist.raq";
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
			if(comid == null){
				MessageUtils.alert("公司名称不能为空!");
				return false;
			}
			return true;
		}

		private String getArgs() {
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			String datfrom = f1.format(dateFrom);
			String datTo = f1.format(dateTo);
			String arg =  "&comid="+ comid + "&dateFrom=" + datfrom + "&dateTo=" + datTo;
			return arg;
		}
	}


