package com.scp.view.module.stock.report;

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
 * 客户库存报表 按具体客户查询
 * 
 * @author Administrator
 * 
 */
@ManagedBean(name = "pages.module.stock.report.stockcustomerBean", scope = ManagedBeanScope.REQUEST)
public class StockCustomerBean extends GridView {





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
		if(this.getGridSelectId()==-1){
			MessageUtils.alert("客户不能为空!");
			return ;
			
		} else if (this.qryWarehouseCom == null) {
			MessageUtils.alert("仓库不能为空!");
			return;
		} else {
			String rpturl = AppUtils.getContextPath();
			String openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/stock/stock-customer.raq";
			AppUtils.openWindow("_arapReport", openUrl + getArgs());
		}
	}

	private String getArgs() {
		String args = "";

		args += "&warehouseid=" + this.qryWarehouseCom;
		args += "&customerid=" + this.getGridSelectId();
		return args;
	}

}
