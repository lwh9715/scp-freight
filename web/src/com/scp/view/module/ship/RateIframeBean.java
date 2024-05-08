package com.scp.view.module.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.org.json.simple.JSONArray;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.module.ship.rateiframeBean", scope = ManagedBeanScope.REQUEST)
public class RateIframeBean extends GridSelectView {
	
	@Bind
	@SaveState
	private Long jobid = -100L;
	
	@Bind
	public UIEditDataGrid rateGrid;
	
	@SaveState
	@Accessible
	public Map<String, Object> rateQryMap = new HashMap<String, Object>();
	
	@Bind
	@SaveState
	public String currency;
	
	@Bind
	@SaveState
	public String[] ids;
	
	@Bind
	@SaveState
	public String clientid;
	
	@Bind
	@SaveState
	public String invoiceid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			currency = AppUtils.getReqParam("currency");
			clientid = AppUtils.getReqParam("clientid");
			invoiceid = AppUtils.getReqParam("invoiceid");
			jobid = Long.parseLong(AppUtils.getReqParam("jobid"));
			ids = AppUtils.getReqParam("ids").replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
			initRateGridFilter();
		}
	}
	
	private void initRateGridFilter(){
		StringBuffer sb = new StringBuffer();
		sb.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
		rateQryMap.put("qry", sb.toString());
		rateQryMap.put("date","NOW()");
		rateQryMap.put("filter", "AND x.currencyto = '"+currency+"' AND x.currencyfm != '"+currency+"'");
	}
	
	@Bind(id="rateGrid")
	public List<Map> getRateGrids() throws Exception{
		List<Map> list = null;
		if(rateQryMap.size() > 0){
			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.finance.invoiceBean.grid.rate", rateQryMap);
		}
		return list;
	}
	
	@Action
	public void exchangeRate(){
		JSONArray modified = (JSONArray) this.rateGrid.getModifiedData();
		ArrayList<Object> value = (ArrayList<Object>) this.rateGrid.getValue();
		//汇率计算 如果勾选 则只计算勾选 如果无勾选则计算现有列表中ids
		if(ids == null || ids.length < 1){
			StringBuffer sbs = new StringBuffer();
			sbs.append("SELECT a.id FROM _fina_arap AS a WHERE 1=1 ");
			if(this.jobid > 0){
				sbs.append("\n AND a.jobid = "+this.jobid);
			}
			if(this.pkId > 0){
				sbs.append("\n AND (a.invoiceid = "+invoiceid+" OR invoiceid IS NULL) ");
			}else{
				sbs.append("\n AND (invoiceid IS NULL)");
			}
			if(clientid != null){
				sbs.append("\n AND a.customerid = "+clientid);
			}
			List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbs.toString());
			if(list != null){
				ids = new String[list.size()];
			}
			for (int i = 0;i<list.size();i++) {
				ids[i] = list.get(i).get("id").toString();
			}
		}
		this.serviceContext.arapMgrService.updateRateForInvoice(value, modified, currency, ids);
	}
}
