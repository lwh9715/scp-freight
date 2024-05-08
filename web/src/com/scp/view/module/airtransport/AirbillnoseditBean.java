package com.scp.view.module.airtransport;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;
@ManagedBean(name = "pages.module.airtransport.airbillnoseditBean", scope = ManagedBeanScope.REQUEST)
public class AirbillnoseditBean extends EditGridView{
	
	@SaveState
	@Accessible
	@Bind
	public Long airid;
	
	@SaveState
	@Accessible
	@Bind
	public Long jobid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("airid").trim();
		String id2 = AppUtils.getReqParam("jobid").trim();
		if(id!=null&&id!=""){
			airid = Long.valueOf(id);
		}
		if(id2!=null&&id2!=""){
			jobid = Long.valueOf(id2);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry+="\n AND busairid =" + airid;
		qry+="\n AND jobid =" + jobid;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busAirBillMgrService.updateBatchEditGrid(modifiedData);
		Browser.execClientScript("parent.getshowbillnos.submit();");
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busAirBillMgrService.addBatchEditGrid(addedData, airid,jobid);
		Browser.execClientScript("parent.getshowbillnos.submit();");
		this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_bus_air_addbill('airid="+airid+";jobid="+jobid+";');");
	}
	
	@Override
	protected void remove(Object removedData) {
		serviceContext.busAirBillMgrService.removedBatchEditGrid(removedData);
		Browser.execClientScript("parent.getshowbillnos.submit();");
	}
	
	
	@Action(id = "removes")
    public void removes() {
    	editGrid.remove();
    }
	
	
	/*@Action
    public void save() {
    	super.save();
    	Browser.execClientScript("parent.getshowbillnos.submit();");
    }
	*/
	
	@Action
    public void qryRefresh() {
    	super.qryRefresh();
    	Browser.execClientScript("parent.getshowbillnos.submit();");
    }
	
	
}
