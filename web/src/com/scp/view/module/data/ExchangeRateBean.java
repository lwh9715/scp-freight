package com.scp.view.module.data;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.data.DatExchangerate;
import com.scp.service.data.ExchangeRateMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.data.exchangerateBean", scope = ManagedBeanScope.REQUEST)
public class ExchangeRateBean extends EditGridView {
	
	
	@ManagedProperty("#{exchangeRateMgrService}")
	public ExchangeRateMgrService exchangeRateMgrService;
	
	
	@SaveState
	@Accessible
	public DatExchangerate selectedRowData = new DatExchangerate();
	
	
//	@Override
//	public void add() {
//		
//		selectedRowData = new DatExchangerate();
//		super.add();
//		
//		
//		this.selectedRowData.setDatafm(new Date());
//		this.selectedRowData.setXtype("*");
//		this.selectedRowData.setDatato(new Date());
//		
////		String[] currencys = exchangeRate.split("/");
////		String currencyfm = currencys[0];
////		String currencyto = currencys[1];
//		
//		this.selectedRowData.setCurrencyfm(currencyfm);
//		this.selectedRowData.setCurrencyto(currencyto);
//	}

	
	@Bind
	public String currencyfm = "";
	
	@Bind
	public String currencyto = "CNY";

	@Action
	public void gridQryAction(){
		this.qryMap.put("currencyfm", currencyfm);
		this.qryMap.put("currencyto", currencyto);
		if(StrUtils.isNull(currencyfm))this.qryMap.remove("currencyfm");
		if(StrUtils.isNull(currencyto))this.qryMap.remove("currencyto");
		
		this.editGrid.reload();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		//this.add();
	}
	
	@Bind
	@SaveState
	public String ratetype = "bus";
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = "\nAND ratetype = '"+ratetype+"'";
		filter += SaasUtil.filterByCorpid("t");
		map.put("filter", filter);
		return map;
	}
	
	/**
	 * 币制/币制
	 * @return
	 */
	@Bind(id="exchangeRateCurrency")
    public List<SelectItem> getExchangeRateCurrency() {
    	try {
    		String filterBySaas = SaasUtil.filterByCorpid("x");
			return CommonComBoxBean.getComboxItems("x.code","x.code","_dat_currency x " , "WHERE 1=1 " + filterBySaas,"ORDER BY value");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }


//	@Override
//	protected void doServiceFindData() {
//		selectedRowData = exchangeRateMgrService.datExchangerateDao.findById(this.pkVal);
//	}
//
//
//	@Override
//	protected void doServiceSave() {
//		exchangeRateMgrService.saveData(selectedRowData);
//		this.alert("OK");
//	}

	@Action
	public void deleter(){
//		String[] ids = this.editGrid.getSelectedIds();
//		if (ids == null || ids.length == 0 || ids.length > 1) {
//			MessageUtils.alert("请选择单行记录");
//			return;
//		}else{
//			exchangeRateMgrService.removeDate(getGridSelectId());
//			refresh();
//		}
		
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if(p.matcher(s).matches()){
				this.serviceContext.exchangeRateMgrService.removeDate(Long.parseLong(s));
			}
		}
		editGrid.remove();
	}
	
//	@Override
//	public void del() {
//		if(selectedRowData.getId()==0){
//			this.add();	
//		}else{
//			exchangeRateMgrService.removeDate(selectedRowData.getId());
//		refresh();
//		this.add();
//		this.alert("OK");
//		}
//	}
	
	@Override
    public void insert() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			//MessageUtils.alert("请选择单行记录");
			selectedRowData = new DatExchangerate();
			selectedRowData.setCurrencyfm(currencyfm);
			selectedRowData.setCurrencyto(currencyto);
			selectedRowData.setXtype("*");
			selectedRowData.setRate(new BigDecimal(1));
		}else{
			Long id = Long.valueOf(ids[0]);
			DatExchangerate old = exchangeRateMgrService.datExchangerateDao.findById(id);
			selectedRowData = new DatExchangerate();
			selectedRowData.setCurrencyfm(old.getCurrencyfm());
			selectedRowData.setCurrencyto(old.getCurrencyto());
			selectedRowData.setXtype(old.getXtype());
			selectedRowData.setRate(old.getRate());
		}
		
		selectedRowData.setRatetype(ratetype);
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		
		selectedRowData.setDatafm(Calendar.getInstance().getTime());
		Calendar cld = Calendar.getInstance();  
		cld.setTime(Calendar.getInstance().getTime());  
		cld.add(Calendar.MONTH, 24);  
		selectedRowData.setDatato(cld.getTime());
		editGrid.appendRow(selectedRowData);
    }
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.exchangeRateMgrService.updateBatchEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.exchangeRateMgrService.addBatchEditGrid(addedData ,selectedRowData.getId());
	}
	@Override
	protected void remove(Object removedData) {
		serviceContext.exchangeRateMgrService.removedBatchEditGrid(removedData);
	}
}
