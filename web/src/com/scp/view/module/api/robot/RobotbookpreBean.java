package com.scp.view.module.api.robot;


import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.api.ApiRobotBookDao;
import com.scp.dao.api.ApiRobotConfigDao;
import com.scp.model.api.ApiRobotBookPre;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.api.robot.robotbookpreBean", scope = ManagedBeanScope.REQUEST)
public class RobotbookpreBean extends GridFormView{

	@SaveState
	@Accessible
	public ApiRobotBookPre selectedRowData=new ApiRobotBookPre();
	
	@Action
	public void addForm(){
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	@Override
	public void add() {
		super.add();
		selectedRowData.setId(0);
		this.refresh();
	}

	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			//this.add();
		}
	}
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMap = new HashMap<String, Object>();
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		return m;
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.apiRobotBookpreService.apiRobotBookPreDao
		.findById(this.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {	
		try {
			
			this.serviceContext.apiRobotBookpreService.saveData(selectedRowData);
			editWindow.close();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	@Override
	public void del() {
		if(!"demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())){
    		this.alert("无权删除!");
    		return;
    	}
		super.del();
		String[] ids = this.grid.getSelectedIds();
		
		if (ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.apiRobotBookpreService.removeDate(ids);
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void importPost() {
		
		String[] ids = this.grid.getSelectedIds();
		
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			for(int i=0;i<ids.length;i++){
				String insertSql = "select f_imp_robotbook('id="+ids[i]+"')";
				this.serviceContext.busShippingMgrService.busShippingDao.executeQuery(insertSql);
			}
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
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
				String callFunction = "f_imp_robotbookpre";
				String args = "'"+AppUtils.getUserSession().getUsercode() + "'";
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
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}
	ApiRobotBookDao apiRobotBookDao=null;
	ApiRobotConfigDao apirobotconfigDao=null;
    @Action
    public void submit(){
    	long id =selectedRowData.getId();
    	if(id==0){
    		MessageUtils.alert("请先保存数据，再提交!");
    		return;
    	}else{
    		ApiRobotBookPre robotbookpre =this.serviceContext.apiRobotBookpreService.apiRobotBookPreDao
    			.findById(id);
    		if(robotbookpre.getPol().isEmpty()){
    			MessageUtils.alert("起始港不能为空!");
    			return;
    		}
    		if(robotbookpre.getPod().isEmpty()){
    			MessageUtils.alert("目的港不能为空!");
    			return;
    		}
    		if(robotbookpre.getEtd()==null){
    			MessageUtils.alert("ETD不能为空!");
    			return;
    		}
    		if(robotbookpre.getNum20gp()>0||robotbookpre.getNum40gp()>0||robotbookpre.getNum40hq()>0||robotbookpre.getNum45hq()>0){
    			String insertSql = "select f_imp_robotbook('id="+id+"')";
    			try{
    				this.serviceContext.busShippingMgrService.busShippingDao.executeQuery(insertSql);
    			}catch (Exception e) {
    				MessageUtils.alert("该订舱计划已经提交过，请勿重复提交!");
    				MessageUtils.showException(e);
    				return;
    			}
            	MessageUtils.alert("OK!");
            	
    		}else{
    			MessageUtils.alert("柜型至少有一个大于0");
    			return;
    		}
        	
 	      }
 	   
    }
}
