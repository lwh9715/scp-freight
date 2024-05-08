package com.scp.view.module.air;

import java.util.Map;

import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.bus.BusAirExtfee;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.db.SqlObject;
@ManagedBean(name = "pages.module.air.airextfeeBean", scope = ManagedBeanScope.REQUEST)
public class AirextfeeBean extends GridFormView {
	
	
	@SaveState
	@Accessible
	public BusAirExtfee selectedRowData = new BusAirExtfee();
	
	@SaveState
	@Bind
	public Boolean isAdd = false;
	
	@SaveState
	@Bind
	public String type = "";
	
	@SaveState
	@Bind
	public String jobid = "";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			type = AppUtils.getReqParam("type");
			jobid = AppUtils.getReqParam("jobid");
			update.markUpdate(true, UpdateLevel.Data, "type");
			update.markUpdate(true, UpdateLevel.Data, "jobid");
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
		editDataInit();
	}
	
	@Action
	public void addForm(){
		this.pkVal = -1L;
		this.selectedRowData = new BusAirExtfee();
		/*update.markUpdate(true, UpdateLevel.Data, "editPanel");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true,UpdateLevel.Data,"jsonData");*/
		editDataInit();
		update.markUpdate(true, UpdateLevel.Data, "isAdd");
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
		this.serviceContext.busAirExtfeeService.removes(ids,AppUtils.getUserSession().getUsercode());
		MessageUtils.alert("OK");
		this.refresh();
	}
	
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.busAirExtfeeService.busAirExtfeeDao.findById(this.getGridSelectId());
	}


	@Override
	protected void doServiceSave() {
		try{
			this.serviceContext.busAirExtfeeService.busAirExtfeeDao.createOrModify(selectedRowData);
			this.pkVal = selectedRowData.getId();
			this.alert("OK!");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	
	@Action
	public void refreshgrid(){
		this.grid.reload();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		if("choose".equals(type)){
			String filter = "\nAND carrierid = ANY(SELECT x.carrierid FROM bus_air x WHERE x.jobid = "+this.jobid+" AND x.isdelete = FALSE)";
			map.put("filter", filter);
		}
		
		return map;
	}
	
	@Bind
	@SaveState
	private String jsonData;
	
	@Action
	public void btnRefresh(){
		editDataInit();
	}
	
	@Override
	public void grid_ondblclick(){
		if("choose".equals(type)){
			select();
			return;
		}
		super.grid_ondblclick();
		editDataInit();
	}
	
	public void editDataInit() {
		String ret = "''";
		ret = getJsonByJobid(pkVal);
		jsonData = StrUtils.isNull(ret) ? "''" : ret;
		this.update.markUpdate(UpdateLevel.Data, "jsonData");
		editWindow.show();
		Browser.execClientScript("initData();");
	}
	
	public String getJsonByJobid(Long jobid) {
		String sql = "SELECT f_bus_air_extfee('extfeeid="+this.pkVal+"') AS json";
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		//System.out.println(sql);
		if (map != null && 1 == map.size()) {
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}

	
	@Action
	private void saveAjaxSubmit() {
		try{
			StringBuilder sb = new StringBuilder();
			String dataMaster = AppUtils.getReqParam("dataMaster");
    		SqlObject sqlObject = new SqlObject("bus_air_extfee" , dataMaster , AppUtils.getUserSession().getUsercode());
    		
			JSONObject jsonObject = JSONObject.fromObject(dataMaster);
			String id = (String)jsonObject.get("id");
			this.pkVal = Long.valueOf(id);
			String isAdd = (String)jsonObject.get("isAdd");
			if("true".equals(isAdd))sqlObject.setOpType("I");
    			
    		sb.append(sqlObject.toSql());
    		String dataDetail = AppUtils.getReqParam("dataDetail");
    		sqlObject = new SqlObject("bus_air_extfeedtl" , dataDetail , AppUtils.getUserSession().getUsercode());
    		sb.append("\n"+sqlObject.toSqls());
    		//System.out.println("\n"+sb.toString());
			daoIbatisTemplate.updateWithUserDefineSql(sb.toString());
			MessageUtils.alert("OK");
			//Browser.execClientScript("sayOK();");
			editDataInit();
    		//Browser.execClientScript("saveJsVarr.fireEvent('click');");
    		refreshgrid();
			//update.markUpdate(UpdateLevel.Data, "isAdd");
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
	@Action
	public void select() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String updateairbus = "UPDATE bus_air SET extfeeid="+this.getGridSelectId()+" WHERE isdelete = false AND jobid="+this.jobid;
		daoIbatisTemplate.updateWithUserDefineSql(updateairbus);
		String sql = "SELECT f_bus_air_extfee_iteem('extfeeid="+this.getGridSelectId()+"')::TEXT AS iteem";
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if (map != null && 1 == map.size()) {
			String fee =  StrUtils.getMapVal(map, "iteem");
			String otherfeepp = "\"otherfeepp\"";
			String js = "window.opener.otherfeeinto('"+fee+"');";
			Browser.execClientScript(js);

		}
	}
	
	
}
