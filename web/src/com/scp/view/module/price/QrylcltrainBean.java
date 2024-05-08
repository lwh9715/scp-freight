package com.scp.view.module.price;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.scp.model.price.PriceTrainLcl;
import com.scp.service.price.PriceTrainLclService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.price.qrylcltrainBean", scope = ManagedBeanScope.REQUEST)
public class QrylcltrainBean extends GridFormView{
	
	@ManagedProperty("#{priceTrainLclService}")
	public PriceTrainLclService priceTrainLclService;
	
	@SaveState
	@Accessible
	public PriceTrainLcl selectedRowData = new PriceTrainLcl();
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExt = new HashMap<String, Object>();
	
	@Bind(id="qryPoa")
	public List<SelectItem> getQryPoa(){	
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT poa","poa","price_train_lcl AS d","WHERE 1=1","ORDER BY poa");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}		
	}
	
	@Bind(id="qryEtd")
	public List<SelectItem> getQryEtd(){	
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT etd","etd","price_train_lcl AS d","WHERE 1=1","ORDER BY etd");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}		
	}
	
	@Override
	public void clearQryKey() {
		if (qryMapExt != null) {
			qryMapExt.clear();
		}
		super.clearQryKey();
	}
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = priceTrainLclService.priceTrainLclDao.findById(this.pkVal);
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
	
	/*@Bind
	@SaveState
	public Date datefm;*/
	
	/*@Bind
	@SaveState
	public Date dateto;*/
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		qry = qry.replace("AND CAST(datefm AS DATE) =", "AND CAST(datefm AS DATE) >");
		qry = qry.replace("AND CAST(dateto AS DATE) =", "AND CAST(dateto AS DATE) <");
		map.put("qry",qry);
		
		if(!StrUtils.isNull(polcode))map.put("pol", "AND  t.pol = '"+polcode+"'");
		if(!StrUtils.isNull(podcode))map.put("pod", "AND  t.pod = '"+podcode+"'");
		return map;
	}
	
	@Override
	public void refresh() {
		super.refresh();
		if(!qryMapExt.isEmpty()){
			Set<String> set = qryMapExt.keySet();
			for (String object : set) {
				this.qryMap.put(object, qryMapExt.get(object));
			}
		}
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
