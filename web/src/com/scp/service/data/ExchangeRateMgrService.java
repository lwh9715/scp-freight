package com.scp.service.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.data.DatExchangerateDao;
import com.scp.model.data.DatExchangerate;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

@Component
public class ExchangeRateMgrService{
	
	@Resource
	public DatExchangerateDao datExchangerateDao; 

	public void saveData(DatExchangerate data) {
		if(0 == data.getId()){
			datExchangerateDao.create(data);
		}else{
			datExchangerateDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatExchangerate data = datExchangerateDao.findById(id);
		datExchangerateDao.remove(data);
	}

	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		DatExchangerate data = new DatExchangerate();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = datExchangerateDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			
			if(jsonObject.get("currencyfm")!=null){
				data.setCurrencyfm(String.valueOf(jsonObject.get("currencyfm")));
			}else{
				data.setCurrencyfm(null);
			}
			if(jsonObject.get("currencyto")!=null){
				data.setCurrencyto(String.valueOf(jsonObject.get("currencyto")));
			}else{
				data.setCurrencyto(null);
			}
			if(jsonObject.get("xtype")!=null){
				data.setXtype(String.valueOf(jsonObject.get("xtype")));
			}else{
				data.setXtype(null);
			}
			if(jsonObject.get("rate")!=null && !String.valueOf(jsonObject.get("rate")).isEmpty()){
				data.setRate(new BigDecimal(String.valueOf(jsonObject.get("rate"))));
			}else{
				data.setRate(new BigDecimal(0));
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("datafm")!=null){
				try {
					data.setDatafm(sdf.parse((jsonObject.get("datafm")).toString()));
				} catch (ParseException e) {
					data.setDatafm(null);
					e.printStackTrace();
				}
			}else{
				data.setDatafm(null);
			}
			
			if(jsonObject.get("datato")!=null){
				try {
					data.setDatato(sdf.parse((jsonObject.get("datato")).toString()));
				} catch (ParseException e) {
					data.setDatato(null);
					e.printStackTrace();
				}
			}else{
				data.setDatato(null);
			}
			
			saveData(data);
		}		
	}

	public void addBatchEditGrid(Object addedData, long id) {
		JSONArray jsonArray = (JSONArray) addedData;
		DatExchangerate data = new DatExchangerate();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("currencyfm")!=null){
				data.setCurrencyfm(String.valueOf(jsonObject.get("currencyfm")));
			}else{
				data.setCurrencyfm(null);
			}
			if(jsonObject.get("currencyto")!=null){
				data.setCurrencyto(String.valueOf(jsonObject.get("currencyto")));
			}else{
				data.setCurrencyto(null);
			}
			if(jsonObject.get("xtype")!=null){
				data.setXtype(String.valueOf(jsonObject.get("xtype")));
			}else{
				data.setXtype(null);
			}
			if(jsonObject.get("ratetype")!=null){
				data.setRatetype(String.valueOf(jsonObject.get("ratetype")));
			}else{
				data.setRatetype("bus");
			}
			if(jsonObject.get("rate")!=null && !String.valueOf(jsonObject.get("rate")).isEmpty()){
				data.setRate(new BigDecimal(String.valueOf(jsonObject.get("rate"))));
			}else{
				data.setRate(new BigDecimal(0));
			}
			if(jsonObject.get("corpid")!=null && !String.valueOf(jsonObject.get("corpid")).isEmpty()){
				data.setCorpid(Long.valueOf(String.valueOf(jsonObject.get("corpid"))));
			}else{
				data.setCorpid(0l);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("datafm")!=null){
				try {
					data.setDatafm(sdf.parse((jsonObject.get("datafm")).toString()));
				} catch (ParseException e) {
					data.setDatafm(null);
					e.printStackTrace();
				}
			}else{
				data.setDatafm(null);
			}
			
			if(jsonObject.get("datato")!=null){
				try {
					data.setDatato(sdf.parse((jsonObject.get("datato")).toString()));
				} catch (ParseException e) {
					data.setDatato(null);
					e.printStackTrace();
				}
			}else{
				data.setDatato(null);
			}


			if (data.getDatafm().after(data.getDatato())) {
				MessageUtils.alert("期间起必须在期间止之前");
				return;
			}
			List<DatExchangerate> datExchangerateList =
					datExchangerateDao.findAllByClauseWhere("1=1 and isdelete = false" +
							" and ratetype = '" + data.getRatetype() + "'" +
							" and currencyfm='" + data.getCurrencyfm() + "'" +
							" and currencyto='" + data.getCurrencyto() + "' " +
							"");
			if (!datExchangerateList.isEmpty()) {
				Collections.sort(datExchangerateList, new Comparator<DatExchangerate>() {
					@Override
					public int compare(DatExchangerate o1, DatExchangerate o2) {
						return o2.getDatato().compareTo(o1.getDatato());
					}
				});
				DatExchangerate firstDatExchangerate = datExchangerateList.get(0);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(firstDatExchangerate.getDatato());
				calendar.add(calendar.DATE, 1);

				if (!data.getDatafm().equals(calendar.getTime())) {
					MessageUtils.alert("期间起只能是" + sdf.format(calendar.getTime()));
					return;
				}
			}


			saveData(data);
		}		
	}

	public void removedBatchEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		DatExchangerate data = new DatExchangerate();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "UPDATE dat_exchangerate SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW() WHERE id IN ("+ids+");";
			datExchangerateDao.executeSQL(sql);
		}
		
	} 
	
}
