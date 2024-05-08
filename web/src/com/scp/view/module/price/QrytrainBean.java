package com.scp.view.module.price;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.price.PriceTrain;
import com.scp.service.price.PriceTrainService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.price.qrytrainBean", scope = ManagedBeanScope.REQUEST)
public class QrytrainBean extends GridFormView{
	
	@ManagedProperty("#{priceTrainService}")
	public PriceTrainService priceTrainService;
	
	@SaveState
	@Accessible
	public PriceTrain selectedRowData = new PriceTrain();
	
	@Bind(id="qryPdd")
	public List<SelectItem> getQryPdd(){	
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT pdd","pdd","price_train AS d","WHERE 1=1","ORDER BY pdd");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}		
	}
	
	@Override
	public void clearQryKey() {
		super.clearQryKey();
	}
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = priceTrainService.priceTrainDao.findById(this.pkVal);
	}
	
	@Override
	protected void doServiceSave() {
		
	}
	
	@Bind
	@SaveState
	public String polcode;
	
	@Bind
	@SaveState
	public String podcode;
	
	@Bind
	@SaveState
	public Date datefm;
	
	@Bind
	@SaveState
	public Date dateto;
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		map.put("qry",qry);
		
		if(!StrUtils.isNull(polcode))map.put("pol", "AND  p.pol = '"+polcode+"'");
		if(!StrUtils.isNull(podcode))map.put("pod", "AND  p.pod = '"+podcode+"'");
		
		if(datefm == null && dateto == null){
			map.put("history","\nAND 1=1");
		}
		
		if(datefm != null && dateto != null){
			map.put("history","\nAND datefm >= '"+datefm+"' AND dateto <= '"+dateto+"' ");
		}
		
		return map;
	}
	
	@Override
	public void refresh() {
		super.refresh();
	}
	
	@Action
    private void clearQry2() {
		this.clearQryKey();
	}
	
	
	@Action
	public void linkEdit(){
		String pkid = AppUtils.getReqParam("pkid");
		if(StrUtils.isNull(pkid)){
			this.alert("无效pkid");
			return;
		}
		this.pkVal = Long.parseLong(pkid);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
	@Action
	public void createOrder(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		createOrderWindow.show();
		String blankUrl = AppUtils.chaosUrlArs("../order/busorderair.xhtml") + "&priceid="+ids[0] + "&type=fromPrice";
		orderIframe.load(blankUrl);
	}
	@Action
	public void close(){
		editWindow.close();
	}

	@Bind
	public UIWindow createOrderWindow;
	
	
	@Bind
	public UIIFrame orderIframe;
	
	
	
	
	@Override
	public void grid_ondblclick(){
		this.pkVal = getGridSelectId();
	}
}
