package com.scp.view.module.oa.paymgr;

import java.util.Calendar;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.paymgr.costshowBean", scope = ManagedBeanScope.REQUEST)
public class CostShowBean extends GridFormView {

	@Bind
	public String month;

	@Bind
	public String year;
	
	@Bind
	public String month2;

	@Bind
	public String year2;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			Calendar cal = Calendar.getInstance();
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);// 月
			year = String.valueOf(cal.get(Calendar.YEAR)); // 年
			month2 = String.valueOf(cal.get(Calendar.MONTH) + 1);// 月
			year2 = String.valueOf(cal.get(Calendar.YEAR)); // 年
			this.update.markUpdate(UpdateLevel.Data, "month");
			this.update.markUpdate(UpdateLevel.Data, "year");
			this.update.markUpdate(UpdateLevel.Data, "month2");
			this.update.markUpdate(UpdateLevel.Data, "year2");
		}
	}

	@Action
	public void report() {
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/costall.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
	}

	@Action
	public void queryOut() {

	}

	private String getArgs() {
		String arg = "&year1=" + year + "&month1=" + month+"&year2=" + year2 + "&month2=" + month2;
		return arg;
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}
}
