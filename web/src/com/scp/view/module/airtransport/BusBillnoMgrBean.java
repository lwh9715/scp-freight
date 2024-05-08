package com.scp.view.module.airtransport;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.bus.BusBillnoMgr;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.airtransport.busBillnoMgrBean", scope = ManagedBeanScope.REQUEST)
public class BusBillnoMgrBean extends GridFormView {
	
	/*@ManagedProperty("#{busBillnoMgrService}")
	private BusBillnoMgrService busBillnoMgrService;*/
	
	@Bind
	@SaveState
	public String qryCarr;
	
	@Bind
	@SaveState
	public String code;
	
	@SaveState
	@Accessible
	public BusBillnoMgr selectedRowData = new BusBillnoMgr();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			
		}
	}
	
	@Action
	public void linkEdit(){
		String pkid = AppUtils.getReqParam("pkid");
		if(StrUtils.isNull(pkid)){
			this.alert("无效pkid");
			return;
		}
		this.pkVal = Long.parseLong(pkid);
		doServiceFindData();
		this.refreshForm();
		//Browser.execClientScript("setpol()");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
	@Action
	public void addForm(){
		this.pkVal = -1L;
		this.selectedRowData = new BusBillnoMgr();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Action
	public void close() {
		this.editWindow.close();
	}
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		this.serviceContext.busBillnoMgrService.removes(ids);
		this.refresh();
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.busBillnoMgrService.busBillnoMgrDao.findById(this.getGridSelectId());
	}
	
	@Action
	public void statistics() {
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/bus/rpt_bus_billnomgr.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl);
	}
	
	
	
	@Override
	public void clearQryKey() {
		if(!StrUtils.isNull(code)) {
			code = "";
		}
		if(!StrUtils.isNull(qryCarr)) {
			qryCarr = "";
		}
		if(qryMap != null){
			qryMap.clear();
		}
		super.clearQryKey();
	}


	@Override
	protected void doServiceSave() {
		try{
			this.serviceContext.busBillnoMgrService.busBillnoMgrDao.createOrModify(selectedRowData);
			this.pkVal = selectedRowData.getId();
			this.alert("OK!");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void saveMaster() {
		try {
			if (this.pkVal > 0) {
				this.serviceContext.busBillnoMgrService.busBillnoMgrDao.modify(this.selectedRowData);
			} else {
				this.serviceContext.busBillnoMgrService.busBillnoMgrDao.create(this.selectedRowData);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		
		this.refreshForm();
	}
	
	@Action
	public void refreshgrid(){
		this.grid.reload();
		update.markUpdate(true, UpdateLevel.Data, "grid");
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		
		if(!StrUtils.isNull(code))map.put("qry1", "AND  (t.billno LIKE '%"+code+"%' OR t.refno LIKE '%"+code+"%' OR t.remarks LIKE '%"+code+"%')");
		if(!StrUtils.isNull(qryCarr))map.put("qryCarr", "AND  t.carrierid = '"+qryCarr+"'");
		return map;
	}
	
	
	@Bind
	public UIWindow addbillnoWindow;
	
	@SaveState
	@Accessible
	@Bind
	public Long airlineid;
	
	@Bind
	@SaveState
	public String firstbillno;
	
	@SaveState
	@Accessible
	@Bind
	public Integer billnoscount;
	
	@SaveState
	@Accessible
	@Bind
	public String remarks;
	
	@SaveState
	@Accessible
	@Bind
	public String sort;
	
	@Action
	public void savebusbillno() {
		try {
			String sql = "SELECT f_bus_billnomgr_addbillno('firstbillno="+firstbillno+";billnoscount="+billnoscount+";airlineid="+airlineid+";remarks="+remarks+";sort="+sort+";usercode="+AppUtils.getUserSession().getUsercode()+";');";
			this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.addbillnoWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
	}
	
	
	@Action
	public void closebillnos() {
		this.addbillnoWindow.close();
	}
	
	
}
