package com.scp.view.module.customer;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.sysmgr.filedownload.FiledownloadBean;

@ManagedBean(name = "pages.module.customer.customerdeployBean", scope = ManagedBeanScope.REQUEST)
public class CustomerDeployBean extends GridView {
	
	@Bind
	@SaveState
	public String assignsalesnamecqry;
	
	@Bind
	@SaveState
	public String operationnamecqry;
	
	@Bind
	@SaveState
	public String servicenamecqry;
	
	@Bind
	@SaveState
	public String filesnamecqry;

	@Bind
	@SaveState
	public String namecstr;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			qryMap.put("iscustomer$", true);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("filter", AppUtils.custCtrlClauseWhere());
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(namecstr)){
			map.put("namecstr", FiledownloadBean.getInConditionilike3(namecstr, "namec"));
		}
		if(!StrUtils.isNull(assignsalesnamecqry)){
			qry += "\n AND EXISTS(SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign y WHERE y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=x.id)" +
					"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(assignsalesnamecqry)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(assignsalesnamecqry)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(assignsalesnamecqry)+"%'))";
		}
		if(!StrUtils.isNull(operationnamecqry)){
			qry += "\n AND EXISTS(SELECT 1 FROM sys_user x , sys_user_assign y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='O' AND y.userid=x.id" +
			"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(operationnamecqry)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(operationnamecqry)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(operationnamecqry)+"%'))";
		}
		if(!StrUtils.isNull(servicenamecqry)){
			qry += "\n AND EXISTS(SELECT 1 FROM sys_user x , sys_user_assign y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='C' AND y.userid=x.id" +
			"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(servicenamecqry)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(servicenamecqry)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(servicenamecqry)+"%'))";
		}
		if(!StrUtils.isNull(filesnamecqry)){
			qry += "\n AND EXISTS(SELECT 1 FROM sys_user x , sys_user_assign y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='D' AND y.userid=x.id" +
			"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(filesnamecqry)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(filesnamecqry)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(filesnamecqry)+"%'))";
		}
		map.put("qry",qry);
		return map;
	}
	
	@Bind
	@SaveState
	public String saleid;
	
	@Action
	public void exchangeCustomer(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(saleid)){
			MessageUtils.alert("必须选择业务员和勾选需要调配的客户!");
			return;
		}
		int iMax = ids.length - 1;
	    StringBuilder b = new StringBuilder();
	    for (int i = 0;i<=iMax; i++) {
	    	b.append(String.valueOf(ids[i]));
	        if (i != iMax)
		    b.append(",");
	    }
		String sql = "SELECT f_fina_customer_deploy('customerids="+b.toString()+";saleid="+saleid+";userid="+AppUtils.getUserSession().getUserid()+";');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}

	@Action
	public void deletesale() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || StrUtils.isNull(saleid)) {
			MessageUtils.alert("必须选择业务员和勾选需要调配的客户!");
			return;
		}
		int iMax = ids.length - 1;
		StringBuilder b = new StringBuilder();
		for (int i = 0; i <= iMax; i++) {
			b.append(String.valueOf(ids[i]));
			if (i != iMax)
				b.append(",");
		}

		for (String id : ids) {
			String sql = "update sys_user_assign sua set isdelete=true,updater = " + AppUtils.getUserSession().getUserid() + ",updatetime=now() where isdelete=false and linkid= " + id + " and linktype='C' and " +
					"roletype='S'  and userid=" + saleid + ";";
			try {
				this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}

		MessageUtils.alert("OK!");
		this.refresh();
	}

	@Bind
	@SaveState
	public String serviceid;
	
	@Bind
	@SaveState
	public String operationid;
	
	@Bind
	@SaveState
	public String filesid;
	
	@Action
	public void coverServiceid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(serviceid)){
			MessageUtils.alert("必须选择客服和勾选需要覆盖的客户!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_customer_assign('customerids="+id+";saleid="+serviceid+";type="+"C;roletype="+"C');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	@Action
	public void addServiceid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(serviceid)){
			MessageUtils.alert("必须选择客服和勾选需要追加的客户!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_customer_assign('customerids="+id+";saleid="+serviceid+";type="+";roletype="+"C');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	@Action
	public void coverOperationid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(operationid)){
			MessageUtils.alert("必须选择操作和勾选需要覆盖的客户!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_customer_assign('customerids="+id+";saleid="+operationid+";type="+"C;roletype="+"O');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	@Action
	public void addOperationid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(operationid)){
			MessageUtils.alert("必须选择操作和勾选需要追加的客户!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_customer_assign('customerids="+id+";saleid="+operationid+";type="+";roletype="+"O');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	@Action
	public void coverFilesid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(filesid)){
			MessageUtils.alert("必须选择文件和勾选需要覆盖的客户!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_customer_assign('customerids="+id+";saleid="+filesid+";type="+"C;roletype="+"D');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	@Action
	public void addFilesid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0 || StrUtils.isNull(filesid)){
			MessageUtils.alert("必须选择文件和勾选需要追加的客户!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_customer_assign('customerids="+id+";saleid="+filesid+";type="+";roletype="+"D');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
}
