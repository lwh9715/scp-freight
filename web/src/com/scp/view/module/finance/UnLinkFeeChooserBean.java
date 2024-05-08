package com.scp.view.module.finance;

import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.unlinkfeechooserBean", scope = ManagedBeanScope.REQUEST)
public class UnLinkFeeChooserBean extends GridView {
	

	@SaveState
	@Bind
	public String customerid;
	
	@SaveState
	@Bind
	public String reqtype;

	@Bind
	@SaveState
	public Long pkVal;


	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			pkVal = Long.valueOf((String) AppUtils.getReqParam("id"));
			customerid = (String) AppUtils.getReqParam("customerid");
			reqtype = (String) AppUtils.getReqParam("reqtype");
		}
	}
	
	
	@Bind
	public UIEditDataGrid arapGrid;
	
	
	@Action
	public void del(){
		this.serviceContext.reqMgrService.removeDate(this.pkVal);
		this.alert("OK!");
	}
	
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map map = super.getQryClauseWhere(queryMap);
		map.put("customerid", customerid);
		map.put("reqtype", "'"+reqtype+"'");
		String qry = StrUtils.getMapVal(map, "qry");
		//qry += "\nAND NOT EXISTS(SELECT 1 FROM fina_rpreqdtl x WHERE x.arapid = t.arapid AND x.isdelete = FALSE  AND x.rpreqid = "+pkVal+")";
		//if(this.reqtype.equals("Q")){
		//	qry+=" AND t.araptype = 'AP'";
		//}
		map.put("qry", qry);
		
		
		String filter = "  AND EXISTS (SELECT 1 FROM sys_user_corplink x , fina_arap y WHERE x.corpid = y.corpid AND y.jobid = t.jobid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+") " +
		"AND (EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.jobid AND x.linktype = 'J' AND x.userid ="
		+ AppUtils.getUserSession().getUserid() + ")" +
				"OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_user y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = t.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //关联的业务员的单，都能看到
		+ ")"
		+ "\n	OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_role y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = t.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //组关联业务员的单，都能看到
		+ "))";
		
		map.put("filter", filter);
		return map;
	}
	

	
	@Action
	public void grid_ondblclick() {
//		Long id = this.getGridSelectId();
//		this.selectedRowDataArap = this.serviceContext.arapMgrService.finaArapDao.findById(id);
//		showEditBillDtlWin.show();
	}


	
		
	@Action
	public void save() {
		if(this.pkVal < 1){
			MessageUtils.alert("Please save first!请先保存付款申请单!");
			return;
		}
		String ids[] = this.grid.getSelectedIds();
		String sql = "";
		for (String id : ids) {
			String jobid = id.substring(0, id.indexOf("."));
			String arapids = "SELECT x.id AS arapid FROM fina_arap x WHERE x.jobid = "+jobid+" AND x.isdelete = false AND x.rptype = 'G' AND x.customerid = "+customerid+" AND x.corpid = ANY (SELECT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
			List<Map> lists = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(arapids);
			for (Map map : lists) {
				String arapid = StrUtils.getMapVal(map, "arapid");
				sql += "\n INSERT INTO fina_rpreqdtl(id,rpreqid,arapid,corpid,inputer,inputtime) VALUES(getid(),"+this.pkVal+","+arapid+","+AppUtils.getUserSession().getCorpid()+",'"+AppUtils.getUserSession().getUsercode()+"',now());";
			}
			//			FinaRpreqdtl finaRpreqdtl = new FinaRpreqdtl();
//			finaRpreqdtl.setRpreqid(pkVal);
//			finaRpreqdtl.setArapid(Long.valueOf(id));
//			this.serviceContext.reqMgrService.saveData(finaRpreqdtl);
		}
		try {
			//System.out.println(sql);
			this.serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			MessageUtils.alert("OK");
			this.qryRefresh();
			//Browser.execClientScript("parent.chooseFeeWindows.hide()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	@Action
	public void setData() {
		
	}
	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
	}

}
