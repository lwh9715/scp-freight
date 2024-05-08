package com.scp.view.module.api.cosco;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.api.ApiCoscoBookData;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.api.cosco.bookingBean", scope = ManagedBeanScope.REQUEST)
public class BookingBean extends GridFormView{

	
	
	@SaveState
	@Accessible
	public ApiCoscoBookData selectedRowData = new ApiCoscoBookData();
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		return m;
	}
	
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.apiCoscoBookdataService.apiCoscoBookdataDao.findById(pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.apiCoscoBookdataService.saveData(selectedRowData);
		this.alert("OK");
		
	}
}
