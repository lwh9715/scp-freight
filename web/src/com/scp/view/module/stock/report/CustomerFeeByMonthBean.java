package com.scp.view.module.stock.report;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
/**
 * 已发货费用  按月查询所有客户
 * @author Administrator
 *
 */
@ManagedBean(name = "pages.module.stock.report.customerfeebymonthBean", scope = ManagedBeanScope.REQUEST)
public class CustomerFeeByMonthBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public Date datafrom;
	
	@Bind
	@SaveState
	@Accessible
	public Long qryWarehouseCom;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {

		}
	}

	@Action
	public void report() {
		if(this.datafrom==null){
			MessageUtils.alert("时间不能为空!");
			return ;
		}else if(this.qryWarehouseCom==null){
			MessageUtils.alert("仓库不能为空!");
			return ;
		}else{
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/stock/cusfee_by_month.raq";
		AppUtils.openWindow("_arapReport", openUrl + getArgs());
		}
	}



	private String getArgs() {
		String DATE_TIME_FORMAT = "yyyyMM";
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		String args = "";
		
		args += "&warehouseid=" + this.qryWarehouseCom;
		args += "&datefrom=" + dateTimeFormat.format(datafrom);
		return args;
	}

}
