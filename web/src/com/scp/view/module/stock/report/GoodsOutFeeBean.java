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
 * 已发货费用  客户按月 查询
 * @author Administrator
 *
 */
@ManagedBean(name = "pages.module.stock.report.goodsoutfeeBean", scope = ManagedBeanScope.REQUEST)
public class GoodsOutFeeBean extends GridView {

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
		}else if(this.getGridSelectId()==-1){
			MessageUtils.alert("客户不能为空!");
			return ;
			
		}else if(this.qryWarehouseCom==null){
			MessageUtils.alert("仓库不能为空!");
			return ;
		}
		else{
			String rpturl = AppUtils.getContextPath();
			String openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/stock/out-fee-check.raq";
			AppUtils.openWindow("_arapReport", openUrl + getArgs());
		}
	}



	private String getArgs() {
		String DATE_TIME_FORMAT = "yyyyMM";
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		long customer=this.getGridSelectId();
		String args = "";
		
		args += "&customerid=" + customer;
		args += "&warehouseid=" + this.qryWarehouseCom;
		args += "&datefrom=" + dateTimeFormat.format(datafrom);
		args += "&customerid2=" + customer;
		args += "&warehouseid2=" + this.qryWarehouseCom;
		args += "&datefrom2=" + dateTimeFormat.format(datafrom);
		return args;
	}
	
	
}
