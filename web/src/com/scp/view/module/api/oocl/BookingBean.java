package com.scp.view.module.api.oocl;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.model.api.ApiOoclBookData;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.api.oocl.bookingBean", scope = ManagedBeanScope.REQUEST)
public class BookingBean extends GridFormView {

	
	
	@SaveState
	@Accessible
	public ApiOoclBookData selectedRowData = new ApiOoclBookData();
	
	
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
		selectedRowData = serviceContext.apiOoclBookdataService.apiOoclBookdataDao.findById(pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.apiOoclBookdataService.saveData(selectedRowData);
		this.alert("OK");
		
	}
	
	@Bind
	public UIIFrame bookingIframe;

	@Override
	public void grid_ondblclick() {
//		this.pkVal = getGridSelectId();
//		bookingIframe.load("./bookingdata.xhtml?src=edit&id="+this.getGridSelectId());
//		if(editWindow != null)editWindow.show();
//		update.markUpdate(true, UpdateLevel.Data, "bookingIframe");
	}
	
	
}
