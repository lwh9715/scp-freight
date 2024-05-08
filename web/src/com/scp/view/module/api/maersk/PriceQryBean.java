package com.scp.view.module.api.maersk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.api.ApiMaerskPriceSub;
import com.scp.model.api.ApiMaerskPriceSubDtl;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.api.maersk.priceqryBean", scope = ManagedBeanScope.REQUEST)
public class PriceQryBean extends GridView{
	
	@Bind
	@SaveState
	public String qrypol;
	
	@Bind
	@SaveState
	public String qrypod;
	
	@Bind
	@SaveState
	public String priceInfo;
	
	@Bind
	@SaveState
	public String cntInfo;
	
	@Bind
	@SaveState
	public String qryArgs;
	
	@Bind
	@SaveState
	public Date departureDate;
	
	@Bind
	@SaveState
	public java.math.BigDecimal mton;
	
	@Bind
	@SaveState
	public String qryUrl;
	
	@Bind
	@SaveState
	public String qryKey;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			departureDate = new Date();
			qrypol="CNNAN";
			qrypod="NGON1";
			cntInfo = "1x40HDRYx16000";
			mton= new java.math.BigDecimal(10);
			qryUrl = ConfigUtils.findSysCfgVal("sys_maersk_qryprice_url");
			qryKey = ConfigUtils.findSysCfgVal("sys_maersk_qryprice_key");
		}
	}

	@Action
	public void qryPrice(){
		try{
			String format = new SimpleDateFormat("yyyy-MM-dd").format(departureDate);
			String returnjson = AppUtils.sendGetToMSJ(qryUrl+format
					,"originServiceMode=CY&destinationServiceMode=CY&containers=2x40DRY&originRkstCode="+qrypol+"&destinationRkstCode="+qrypod);
			this.priceInfo = returnjson;//System.out.println(priceInfo);
			this.priceApiGrid.reload();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void getPriceAction(){
		this.priceInfo = AppUtils.getReqParam("priceInfo");
		this.priceApiGrid.reload();
	}
	
	@Bind
	public UIDataGrid priceApiGrid;
	
	@Bind(id = "priceApiGrid", attribute = "dataProvider")
	public GridDataProvider getPriceApiGridProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(priceInfo)){
					return new Object[]{};
				}
				String querySql = 
					 "\nselect row_number() OVER() AS id, vessel||'('|| voyage ||')' AS vesvoy,* from f_priceqry_json_to_recordset('"+StrUtils.getSqlFormat(priceInfo)+"')"
					+"\nORDER BY etd , id"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object[] getElementsById(String[] id) {
//				String querySql = 
//					"\nSELECT " +
//					"\n* " +
//					"\nFROM _dat_goods a " +
//					"\nWHERE id= " +id[0];
//				try {
//					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
//					if(list==null) return null;
//					return list.toArray(); 
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
				return null;
			}

			@Override
			public int getTotalCount() {
				if(StrUtils.isNull(priceInfo)){
					return 0;
				}
				String countSql = 
					"\nSELECT COUNT(*) AS counts FROM("
					+"\nselect row_number() OVER() AS id,* from f_priceqry_json_to_recordset('"+StrUtils.getSqlFormat(priceInfo)+"')"
					+"\n) t";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
	@Action
	public void importInvoice(){
		String[] ids = this.priceApiGrid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请勾选需要导入的行！");
			return;
		}
		String sql = "SELECT * FROM f_import_onvoice_corpinv('jsonarray="+StrUtils.getSqlFormat(priceInfo)+";ids="+StrUtils.array2List(ids)+"')";
		try{
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			alert("OK");
			Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
		}catch(Exception e){
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void imporMaerskPortPol(){
		try{
			serviceContext.apiMaerskService.imporMaerskPortPol();
			Browser.execClientScript("qryPol()");
			alert("OK");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void imporMaerskPortPod(){
		try{
			serviceContext.apiMaerskService.imporMaerskPortPod();
			Browser.execClientScript("qryPod()");
			alert("OK");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void subscribePrice(){
		String[] ids = this.priceApiGrid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请勾选需要订阅运价的行！");
			return;
		}
		try{
			JSONObject pricejson = JSONObject.fromObject(priceInfo);
			for(String id:ids){
				String querySql = "SELECT * FROM("
					+"\nselect row_number() OVER() AS id,* from f_priceqry_json_to_recordset('"+StrUtils.getSqlFormat(priceInfo)+"')"
					+"\nORDER BY id"
					+"\n)t WHERE id = "+id;
				//System.out.println(querySql);
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				String vesseli = m.get("vessel").toString();
				String voyagei = m.get("voyage").toString();
				JSONArray offers = JSONArray.fromObject(pricejson.get("offers"));
				for(int i=0;i<offers.size();i++){
					String offersjson = offers.get(i).toString();
					String productOffer = JSONObject.fromObject(offersjson).getString("productOffer");
					String routeSchedulesWithPrices = JSONObject.fromObject(offersjson).getString("routeSchedulesWithPrices");
					JSONArray routeSchedulesWithPricess = JSONArray.fromObject(routeSchedulesWithPrices);
					int offerId = JSONObject.fromObject(productOffer).getInt("offerId");
					for(int j=0;j<routeSchedulesWithPricess.size();j++){
//					if(id.equals(offerId+"")){
						String vessel = JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(routeSchedulesWithPricess.get(j)).get("routeScheduleFull")).get("vessel")).getString("name");
						String voyage = JSONObject.fromObject(JSONObject.fromObject(routeSchedulesWithPricess.get(j)).get("routeScheduleFull")).getString("voyageNumber");
						if(vesseli.equals(vessel)&&voyagei.equals(voyage)){
							List<ApiMaerskPriceSub> spiMaerskPricesubs = serviceContext.apiMaerskService.apiMaerskPricesubDao
									.findAllByClauseWhere(" pol = '"+qrypol+"' AND pod = '"+qrypod+"' AND cntcode = '"+cntInfo+"' AND vessel = '"+vessel+"' AND voyage = '"+voyage+"' ");
							if(spiMaerskPricesubs!=null&&spiMaerskPricesubs.size()>0){
								alert("此运价已订阅，请勿重复操作！");
							}else{
								ApiMaerskPriceSub apiMaerskPricesub = new ApiMaerskPriceSub();
								apiMaerskPricesub.setCntcode(cntInfo);
								apiMaerskPricesub.setPol(qrypol);
								apiMaerskPricesub.setPod(qrypod);
								apiMaerskPricesub.setArgs(qryArgs);
								apiMaerskPricesub.setVessel(vessel);
								apiMaerskPricesub.setVoyage(voyage);
								serviceContext.apiMaerskService.apiMaerskPricesubDao.create(apiMaerskPricesub);
								
								ApiMaerskPriceSubDtl apiMaerskPriceSubDtl = new ApiMaerskPriceSubDtl();
								apiMaerskPriceSubDtl.setSubid(apiMaerskPricesub.getId());
								apiMaerskPriceSubDtl.setOffersjson(priceInfo);
								serviceContext.apiMaerskService.apiMaerskPriceSubDtlDao.create(apiMaerskPriceSubDtl);
								alert("OK");
							}
						}
					}
//					}
				}
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	
}
