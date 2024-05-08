package com.scp.view.module.gps;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.gps.BusGpsRef;
import com.scp.schedule.AuotoGetGpsTrack;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.gps.busGpsRefBean", scope = ManagedBeanScope.REQUEST)
public class BusGpsRefBean extends GridFormView {

	@SaveState
	@Accessible
	public BusGpsRef selectedRowData = new BusGpsRef();
	
	@Override
	public void add() {
		selectedRowData = new BusGpsRef();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.busGpsRefService.busGpsRefDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busGpsRefService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void del() {
		try {
			serviceContext.busGpsRefService.busGpsRefDao.remove(selectedRowData);
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
					BusGpsRef gps = serviceContext.busGpsRefService.busGpsRefDao.findById(Long.parseLong(id));
					serviceContext.busGpsRefService.busGpsRefDao.remove(gps);
				}
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			refresh();
		}
	}
	
	@Action
	public void getGpsDataFromAPI(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			try {
				for (String id : ids) {
					BusGpsRef gps = serviceContext.busGpsRefService.busGpsRefDao.findById(Long.parseLong(id));
					AuotoGetGpsTrack auotoGetGps = new AuotoGetGpsTrack();
					String gpsno = gps.getGpsidno();
					String getGpsreturn = auotoGetGps.getGpsInfo(gpsno);
					//System.out.println(getGpsreturn);
					JSONObject getGpsreturnjson = JSONObject.fromObject(getGpsreturn);
					JSONObject json2 = JSONObject.fromObject(getGpsreturnjson.get("result"));
					
					if(getGpsreturnjson!=null && "1".equals(json2.get("code").toString())){//请求成功
						JSONArray jsonArray = (JSONArray) json2.get("data");
						for (int i = 0; i < jsonArray.size(); i++) {
							String data = jsonArray.get(i).toString();
							data = StrUtils.getSqlFormat(data);
							String sql = "\nSELECT f_bus_gps_creatbusgps_zhb('"+gpsno+"','["+data+"]');";
							//System.out.println(sql);
							daoIbatisTemplate.queryWithUserDefineSql(sql);
						}
					}else{
						System.out.println("发送post获取追货宝轨迹信息失败:-->"+getGpsreturn);
					}
				}
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			MessageUtils.alert("OK");
			refresh();
		}
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
				String callFunction = "f_imp_busgpsref";
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
	
	
	@Action
	public void stop() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		String sql = "UPDATE bus_gps_ref SET status = 'S' WHERE id IN ("+StrUtils.array2List(ids)+");";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
		MessageUtils.alert("OK");
		grid.reload();
	}
	
	@Action
	public void start() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		String sql = "UPDATE bus_gps_ref SET status = 'R' WHERE id IN ("+StrUtils.array2List(ids)+");";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
		MessageUtils.alert("OK");
		grid.reload();
	}
	
	
}
