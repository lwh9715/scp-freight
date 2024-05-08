package com.scp.view.module.api.oocl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.api.ApiTools;

@ManagedBean(name = "pages.module.api.oocl.priceqryBean", scope = ManagedBeanScope.REQUEST)
public class PriceqryBean extends GridView {
	@Bind
	@SaveState
	public String priceInfo;
	
	@Bind
	@SaveState
	public String qryArgs;
	
	@Bind
	@SaveState
	public Date startDate;
	
	@Bind
	@SaveState
	public Date endDate;
	
	@Bind
	@SaveState
	public java.math.BigDecimal mton;
	
	@Bind
	@SaveState
	public String apiServerUrl;
	
	@Bind
	@SaveState
	public String apiKey;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			startDate = new Date();

			Calendar cld = Calendar.getInstance();
			cld.setTime(new Date());
			cld.add(Calendar.MONTH, +1);
			endDate = cld.getTime();

			mton= new java.math.BigDecimal(10);
			apiKey = ApiTools.getApiKey("oocl", AppUtils.getUserSession().getUserid());
			apiServerUrl = ApiTools.getApiUrl();

			//HUANGPU TO OSAKA
			// YANTIAN TO LE HAVRE
			// YANTIAN TO HAMBURG
		}
	}
	
	@Action
	public void getPriceAction(){
		this.priceInfo = AppUtils.getReqParam("priceInfo");
		if(priceInfo.equals("[]")){
			MessageUtils.alert("此航线无运价信息!");
			return;
		}
		//假设已经从cosco-api获取到content数据
		//this.priceInfo ="{\"effectiveEndDate\":\"2020-12-31T23:59:59.000Z\",\"fndFacilityName\":\"Karachi Intl Container Terminal\",\"serviceCode\":\"PMX\",\"tradeArea\":\"LATD\",\"inventory\":9213,\"eta\":\"2021-01-18 22:00\",\"etd\":\"2021-01-02 11:00\",\"porCity\":{\"unlocode\":\"CNSHA\",\"cityName\":\"Shanghai\",\"ctryRegionCode\":\"CN\",\"stateName\":\"Shanghai\",\"ctryRegionName\":\"China\",\"cityFullNameEn\":\"Shanghai\",\"cntyName\":\"Shanghai\",\"cityFullNameCn\":\"上海\",\"stateCode\":\"SH\",\"id\":\"738872886232873\"},\"polPort\":{\"portFullNameEn\":\"Shanghai\",\"portFullNameCn\":\"上海\",\"city\":{\"unlocode\":\"CNSHA\",\"cityName\":\"Shanghai\",\"ctryRegionCode\":\"CN\",\"stateName\":\"Shanghai\",\"ctryRegionName\":\"China\",\"cityFullNameEn\":\"Shanghai\",\"cntyName\":\"Shanghai\",\"cityFullNameCn\":\"上海\",\"stateCode\":\"SH\",\"id\":\"738872886232873\"},\"id\":\"349657045012458\",\"portName\":\"Shanghai\",\"portCode\":\"SHA\"},\"effectiveStartDate\":\"2020-12-02T00:00:00.000Z\",\"firstPolPort\":{\"portFullNameEn\":\"Shanghai\",\"portFullNameCn\":\"上海\",\"city\":{\"unlocode\":\"CNSHA\",\"cityName\":\"Shanghai\",\"ctryRegionCode\":\"CN\",\"stateName\":\"Shanghai\",\"ctryRegionName\":\"China\",\"cityFullNameEn\":\"Shanghai\",\"cntyName\":\"Shanghai\",\"cityFullNameCn\":\"上海\",\"stateCode\":\"SH\",\"id\":\"738872886232873\"},\"id\":\"349657045012458\",\"portName\":\"Shanghai\",\"portCode\":\"SHA\"},\"id\":\"8aaa48bd76219b54017622c17857001e\",\"scheduleData\":{\"voyageNo\":\"161\",\"ltdAtPor\":\"2020-12-30 11:00\",\"serviceCode\":\"PMX\",\"origin\":{\"unlocode\":\"CNSHA\",\"cityName\":\"Shanghai\",\"ctryRegionCode\":\"CN\",\"stateName\":\"Shanghai\",\"ctryRegionName\":\"China\",\"cityFullNameEn\":\"Shanghai\",\"cntyName\":\"Shanghai\",\"cityFullNameCn\":\"上海\",\"stateCode\":\"SH\",\"id\":\"738872886232873\"},\"destination\":{\"unlocode\":\"PKKAR\",\"cityName\":\"Karachi\",\"ctryRegionCode\":\"PK\",\"stateName\":\"Sindh\",\"ctryRegionName\":\"Pakistan\",\"cityFullNameEn\":\"Karachi\",\"cityFullNameCn\":\"卡拉奇\",\"id\":\"738872886233842\"},\"estimatedTransitTimeInDays\":16,\"etaAtFnd\":\"2021-01-18 22:00\",\"fndFacilityCode\":\"KHI02\",\"cutOffLocalDate\":\"2020-12-28 01:00\",\"etdAtPor\":\"2021-01-02 11:00\",\"legs\":[{\"pod\":{\"facilityCode\":\"KHI02\",\"eta\":\"2021-01-18 22:00\",\"etd\":\"2021-01-20 20:00\",\"port\":{\"portFullNameEn\":\"Karachi\",\"portFullNameCn\":\"Karachi\",\"id\":\"349645770723431\",\"portName\":\"Karachi\",\"portCode\":\"KHI\"}},\"service\":{\"serviceCode\":\"PMX\"},\"vessel\":{\"vesselCode\":\"CBK\",\"vesselName\":\"COSCO ROTTERDAM\"},\"legSequence\":1,\"externalVoyageNumber\":\"161W\",\"transportMode\":\"Feeder\",\"pol\":{\"facilityCode\":\"SHA04\",\"eta\":\"2021-01-01 13:00\",\"etd\":\"2021-01-02 11:00\",\"port\":{\"portFullNameEn\":\"Shanghai\",\"portFullNameCn\":\"上海\",\"id\":\"349657045012458\",\"portName\":\"Shanghai\",\"portCode\":\"SHA\"}},\"voyageCode\":\"161\",\"internalVoyageNumber\":\"161\",\"direction\":\"W\"}],\"porFacilityCode\":\"SHA04\",\"vesselName\":\"COSCO    ROTTERDAM\",\"direction\":\"W\"},\"direction\":\"W\",\"voyageNo\":\"161\",\"companyDiscountsInfoDTO\":[{\"currencyType\":\"USD\",\"sourceKey\":\"bd9770ff72c94c25a66d1d29ccae93de\",\"amount\":10,\"channelCntr\":\"40GP\",\"source\":\"SYSTEM\"},{\"currencyType\":\"USD\",\"sourceKey\":\"c4176306da094ade805a4c4cc8dd2088\",\"amount\":10,\"channelCntr\":\"40HQ\",\"source\":\"SYSTEM\"},{\"currencyType\":\"USD\",\"sourceKey\":\"5a2b30bcb1c3419091ace9a44a19053f\",\"amount\":10,\"channelCntr\":\"20GP\",\"source\":\"SYSTEM\"}],\"porFacilityName\":\"ShanghaiPort Ctn Waigaoqiao Tml Brh\",\"estimatedTransitTimeInDays\":16,\"lastPodPort\":{\"portFullNameEn\":\"Karachi\",\"portFullNameCn\":\"Karachi\",\"city\":{\"unlocode\":\"PKKAR\",\"cityName\":\"Karachi\",\"ctryRegionCode\":\"PK\",\"stateName\":\"Sindh\",\"ctryRegionName\":\"Pakistan\",\"cityFullNameEn\":\"Karachi\",\"cityFullNameCn\":\"卡拉奇\",\"id\":\"738872886233842\"},\"id\":\"349645770723431\",\"portName\":\"Karachi\",\"portCode\":\"KHI\"},\"podPort\":{\"portFullNameEn\":\"Karachi\",\"portFullNameCn\":\"Karachi\",\"city\":{\"unlocode\":\"PKKAR\",\"cityName\":\"Karachi\",\"ctryRegionCode\":\"PK\",\"stateName\":\"Sindh\",\"ctryRegionName\":\"Pakistan\",\"cityFullNameEn\":\"Karachi\",\"cityFullNameCn\":\"卡拉奇\",\"id\":\"738872886233842\"},\"id\":\"349645770723431\",\"portName\":\"Karachi\",\"portCode\":\"KHI\"},\"fndFacilityCode\":\"KHI02\",\"haulageType\":\"CY-CY\",\"tradeLaneCode\":\"AAW\",\"promotions\":{\"detail\":[{\"summary\":\"订单下箱型20GP每UNIT立减1USD。\",\"promotionType\":\"OFF\",\"remnantInventory\":15,\"priority\":1}]},\"fndCity\":{\"unlocode\":\"PKKAR\",\"cityName\":\"Karachi\",\"ctryRegionCode\":\"PK\",\"stateName\":\"Sindh\",\"ctryRegionName\":\"Pakistan\",\"cityFullNameEn\":\"Karachi\",\"cityFullNameCn\":\"卡拉奇\",\"id\":\"738872886233842\"},\"routeProductPricingList\":[{\"cntrType\":\"40GP\",\"price\":222.00,\"currency\":\"USD\",\"tradePrice\":212.00},{\"cntrType\":\"40HQ\",\"price\":333.00,\"currency\":\"USD\",\"tradePrice\":323.00},{\"cntrType\":\"20GP\",\"price\":111.00,\"currency\":\"USD\",\"tradePrice\":100.00}],\"porFacilityCode\":\"SHA04\",\"vesselName\":\"COSCO ROTTERDAM\"}";
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
					 "\nselect  vessel||'('|| voyage ||')' AS vesvoy,  ocfamt1 " +
							 "||'/'|| ocfamt2 " +
							 "||'/'|| ocfamt3 " +
							 "|| f_newline() " +
							 "||ocfamt4 " +
							 "||'/'|| ocfamt5 " +
							 "||'/'|| ocfamt6 " +
							 "|| f_newline() " +
							 "||ocfamt7 " +
							 "||'/'|| ocfamt8 " +
							 "||'/'|| ocfamt9  " +
							 "||'/'|| ocfamt10 " +
							 "AS ocfamt,  * from f_priceqry_json_to_recordset_cosco('"+ StrUtils.getSqlFormat(priceInfo)+"')"
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
					+"\nselect row_number() OVER() AS id,* from f_priceqry_json_to_recordset('"+ StrUtils.getSqlFormat(priceInfo)+"')"
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
	
	
}
