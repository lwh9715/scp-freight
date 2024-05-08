package com.scp.view.module.insurance;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.price.PriceTrainLcl;
import com.scp.service.price.PriceTrainLclService;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.insurance.insuranceBean", scope = ManagedBeanScope.REQUEST)
public class InsuranceBean extends GridFormView{
	
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
	
	@Bind
	@SaveState
	public String qryComcode = "";
	
	@Bind
	@SaveState
	public String qryCertificatetype ="";
	
	@Bind
	@SaveState
	public String qryCertificatetype2="";
	
	@Bind
	@SaveState
	public String nos="";
	
	@Bind
	@SaveState
	public String name="";
	
	@Bind
	@SaveState
	public String name2="";
	
	@Bind
	@SaveState
	public String tradeno="";
	
	@Override
	public void clearQryKey() {
		/*if (qryMapExt != null) {
			qryMapExt.clear();
		}
		super.clearQryKey();*/
		nos = "";
		qryComcode = "";
		qryCertificatetype = "";
		qryCertificatetype2 = "";
		name = "";
		name2 = "";
		tradeno = "";
		super.clearQryKey();
		this.refresh();
	}
	
	@Override
	protected void doServiceFindData() {
		//this.selectedRowData = priceTrainLclService.priceTrainLclDao.findById(this.pkVal);
	}
	
	@Override
	protected void doServiceSave() {
		
	}
	
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		if(!StrUtils.isNull(nos)){
			qry += "\nAND worknum LIKE '%"+nos+"%'";
		}
		if(!StrUtils.isNull(tradeno)){
			qry += "\nAND tradeno LIKE '%"+tradeno+"%'";
		}
		if(!StrUtils.isNull(qryComcode)){
			qry += "\nAND comcode = '"+qryComcode+"'";
		}
		if(!StrUtils.isNull(qryCertificatetype)){
			//qry += "\nAND certificatetype = '"+qryCertificatetype+"'";
			if(qryCertificatetype == "99"){
				qry += "\nAND (certificatetype = '"+qryCertificatetype+"' OR certificatetype='' OR certificatetype IS NULL)";
			}else{
				qry += "\nAND certificatetype = '"+qryCertificatetype+"'";
			}
		}
		if(!StrUtils.isNull(qryCertificatetype2)){
			if(qryCertificatetype2 == "99"){
				qry += "\nAND (certificatetype2 = '"+qryCertificatetype2+"' OR certificatetype2='' OR certificatetype2 IS NULL)";
			}else{
				qry += "\nAND certificatetype2 = '"+qryCertificatetype2+"'";
			}
		}
		if(!StrUtils.isNull(name)){
			qry += "\nAND name LIKE '%"+name+"%'";
		}
		if(!StrUtils.isNull(name2)){
			qry += "\nAND name2 LIKE '%"+name2+"%'";
		}
		
		map.put("qry", qry);
		return map;
	}
	
	@Override
	public void refresh() {
		super.refresh();
	}
	
}
