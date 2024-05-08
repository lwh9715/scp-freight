package com.scp.view.module.api.maersk;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.api.ApiMaerskPort;
import com.scp.service.api.ApiMaerskService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.api.maersk.maerskportBean", scope = ManagedBeanScope.REQUEST)
public class MaerskportBean extends GridFormView{
	
	@ManagedProperty("#{apiMaerskService}")
	public ApiMaerskService maerskService;

	@Bind
	@SaveState
	public boolean ispol;
	
	@Bind
	@SaveState
	public boolean ispod;
	
	@Bind
	@SaveState
	public String rkstcode;
	
	@Bind
	@SaveState
	public String unloccode;
	
	@Bind
	@SaveState
	public String countryname;
	
	@Bind
	@SaveState
	public String cityname;
	
	@SaveState
	@Accessible
	public ApiMaerskPort selectedRowData = new ApiMaerskPort();
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
	@Bind
	public UIWindow importDataWindow;
	
	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				//this.serviceContext.priceTrainService.removePriceByPriceName(pricename);
				String callFunction = "f_imp_api_maersk_port";
				String args ="'"+AppUtils.getUserSession().getUsercode()+"'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(ispol){
			qry += "\nAND ispol = true";
		}
		if(ispod){
			qry += "\nAND ispod = true";
		}
		
		if(!StrUtils.isNull(rkstcode)){
			qry += "\nAND rkstcode LIKE '%"+rkstcode+"'%'";
		}
		if(!StrUtils.isNull(unloccode)){
			qry += "\nAND unloccode LIKE '%"+unloccode+"'%'";
		}
		if(!StrUtils.isNull(countryname)){
			qry += "\nAND countryname LIKE '%"+countryname+"'%'";
		}
		if(!StrUtils.isNull(cityname)){
			qry += "\nAND cityname LIKE '%"+cityname+"'%'";
		}
		
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void add() {
		selectedRowData = new ApiMaerskPort();
		super.add();
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			StringBuilder stringBuilder = new StringBuilder();
			for (String id : ids) {
				stringBuilder.append("\nUPDATE api_maersk_port set isdelete = TRUE WHERE id = " + id + ";");
			}
			if(stringBuilder.length() > 0){
				this.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
			}
			refresh();
		}
	}
	
	@Override
	public void del() {
		if(selectedRowData.getId()==0){
			this.add();	
		}else{
			maerskService.removeDate(selectedRowData.getId());
			refresh();
			this.add();
			this.alert("OK");
		}
	}
	
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = maerskService.apiMaerskPortDao.findById(pkVal);
	}

	@Override
	protected void doServiceSave() {
		maerskService.saveData(selectedRowData);
		this.alert("OK");
	}
}
