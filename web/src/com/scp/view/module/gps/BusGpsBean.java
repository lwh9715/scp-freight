package com.scp.view.module.gps;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.gps.BusGps;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.gps.busgpsBean", scope = ManagedBeanScope.REQUEST)
public class BusGpsBean extends GridFormView {

	@SaveState
	@Accessible
	public BusGps selectedRowData = new BusGps();
	
	@SaveState
	@Accessible
	@Bind
	public String gpsidno;
	
	
	@Override
	public void add() {
		selectedRowData = new BusGps();
		selectedRowData.setRefno(gpsidno);
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.busGpsService.busGpsDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busGpsService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void del() {
		try {
			serviceContext.busGpsService.busGpsDao.remove(selectedRowData);
			this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			try {
				for (String id : ids) {
					BusGps gps = serviceContext.busGpsService.busGpsDao.findById(Long.parseLong(id));
					serviceContext.busGpsService.busGpsDao.remove(gps);
				}
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			refresh();
		}
	}

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			gpsidno = AppUtils.getReqParam("gpsidno");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = "\nAND refno = '"+gpsidno+"'";
		map.put("filter", filter);
		return map;
	}
	
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_busgpsdata";
				String args = "'" + AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
}
