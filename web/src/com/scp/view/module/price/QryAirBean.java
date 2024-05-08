package com.scp.view.module.price;

import java.util.Date;
import java.util.Map;

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

import com.scp.model.price.PriceAir;
import com.scp.service.price.PriceAirService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.price.qryairBean", scope = ManagedBeanScope.REQUEST)
public class QryAirBean extends GridFormView{
    
	 @ManagedProperty("#{priceAirService}")
	public PriceAirService priceAirService;
    
	 @SaveState
	@Accessible
	public PriceAir selectedRowData = new PriceAir();
    
    @Bind
    public UIIFrame chartsIframe;
    
    @Bind
    public UIWindow chartsWindow;
//    @Override
//	public void export(){
//		ActionGridExport actionGridExport = new ActionGridExport();
//		actionGridExport.setKeys((String) ApplicationUtils.getReqParam("key"));
//		actionGridExport.setVals((String) ApplicationUtils.getReqParam("value"));
//		try {
//			actionGridExport.execute(getGridList());
//			Browser.execClientScript("simulateExport.fireEvent('click');");
//			qryRefresh();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
//	}
	
	
	@Override
	public void clearQryKey() {
		super.clearQryKey();
	}
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = priceAirService.priceAirDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	
	@Bind(id = "grid", attribute = "mergedColumns")
    private String[] mergedColumns = new String[] { "line", "carrier", "router"};
	
	
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
			//map.put("history","\nAND isvalid = 'R'");
			map.put("history","\nAND 1=1");
		}
		
		if(datefm != null && dateto != null){
			map.put("history","\nAND datefm >= '"+datefm+"' AND dateto <= '"+dateto+"' ");
		}
		
		return map;
	}
	
	
	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 1);
		//AppUtils.debug(importDataText);
	}*/
	

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
	
	@Action
	public void showChartsWindow(){
		String pol_rp =  AppUtils.getReqParam("pol");
		String pod_rp =  AppUtils.getReqParam("pod");
		String shipping_rp =  AppUtils.getReqParam("shipping");
		String dateto_rp =  AppUtils.getReqParam("dateto");
		if(StrUtils.isNull(pol_rp)||StrUtils.isNull(pod_rp)||StrUtils.isNull(shipping_rp)||StrUtils.isNull(dateto_rp)){
			MessageUtils.alert("Data error,please contact Manager!");
			return;
		}
		StringBuffer chartsUrl = new StringBuffer();
		chartsUrl.append("./chartsiframe.xhtml?pol=");
		chartsUrl.append(pol_rp);
		chartsUrl.append("&pod=");
		chartsUrl.append(pod_rp);
		chartsUrl.append("&shipping=");
		chartsUrl.append(shipping_rp);
		chartsUrl.append("&dateto=");
		chartsUrl.append(dateto_rp);
		chartsIframe.load(chartsUrl.toString());
		chartsWindow.show();
	}
	
	
	@Override
	public void grid_ondblclick(){
		this.pkVal = getGridSelectId();
	}
	
	
}
