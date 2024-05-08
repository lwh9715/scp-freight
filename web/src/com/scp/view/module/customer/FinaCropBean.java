package com.scp.view.module.customer;

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

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.vo.finance.FinaCorp;

@ManagedBean(name = "pages.module.customer.finacorpBean", scope = ManagedBeanScope.REQUEST)
public class FinaCropBean extends GridFormView{

	@SaveState
	@Accessible
	public FinaCorp selectedRowData = new FinaCorp();
	
	@Bind
	public UIWindow editWindow;
	
	@SaveState
	public String jobid;
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			jobid = AppUtils.getReqParam("jobid").trim();
			if(!StrUtils.isNull(jobid)){
				doServiceFindData();
			}
		}
	}
	
	@Override
	public void refresh() {
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	
//	protected void initAdd() {
//		selectedRowData = new FinaCorp();
//		Browser.execClientScript("$('#sales_input').val('')");
//		this.update.markUpdate(UpdateLevel.Data, "editPanel");
//	}

//	@Override
//	public void add() {
//		this.save();
//	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String sql = "";
		try {
			for(String id : ids) {
				sql += "UPDATE fina_corp SET isdelete = TRUE,updater = "+AppUtils.getUserSession().getUserid()+" WHERE id = " + Long.valueOf(id)+";";
			}
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
			this.grid.reload();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceFindData() {
		Long id = this.getGridSelectId();
		if(id<=0)return;
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT corpoptype,corpid FROM fina_corp WHERE isdelete = false and id = "+id);
//			Browser.execClientScript("corpopInit("+String.valueOf(m.get("corpoptype"))+","+Long.valueOf(String.valueOf(m.get("corpid")))+")");
			selectedRowData.setCorpoptype(String.valueOf(m.get("corpoptype")));
			selectedRowData.setCorpid(Long.valueOf(String.valueOf(m.get("corpid"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceSave() {
		if(selectedRowData.getCorpid() == 0){
			MessageUtils.alert("请选择操作公司!");
			return;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO fina_corp(id,jobid,corpid,inputer,corpoptype) values (getid(),"+jobid+","+selectedRowData.getCorpid()+","+AppUtils.getUserSession().getUserid()+",'"+selectedRowData.getCorpoptype()+"')");
		
		try {
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(buffer.toString());
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
		editWindow.close();
		this.grid.reload();
	}
	
	protected void doServiceUpdate() {
		if(selectedRowData.getCorpid() == 0){
			MessageUtils.alert("请选择操作公司!");
			return;
		}
		StringBuffer buffer = new StringBuffer();
//		buffer.append("INSERT INTO fina_corp(id,jobid,corpid,inputer,corpoptype) values (getid(),"+jobid+","+selectedRowData.getCorpid()+","+AppUtils.getUserSession().getUserid()+",'"+selectedRowData.getCorpoptype()+"')");
		buffer.append("UPDATE fina_corp SET corpoptype = '" + selectedRowData.getCorpoptype() + "' WHERE id = "+super.pkVal);
		try {
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(buffer.toString());
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
		editWindow.close();
		this.grid.reload();
	}
	
	@Override
	public void save() {
		if(super.pkVal > 0){
			doServiceUpdate();
		}else{
			doServiceSave();
		}
		
	}
	
	@Override
	public void grid_ondblclick(){
		this.pkVal = getGridSelectId();
//		this.data = getViewControl().findById(this.pkVal);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String jobsql = jobid;
		jobsql += "\n AND isdelete = FALSE";
		map.put("jobsql", jobsql);
		return map;
	}
	
	@Override
	public void add() {
		super.add();
	}
	
	@Action
	public	void checked(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			String sql = "UPDATE fina_jobs SET corpidop = (SELECT corpid FROM fina_corp WHERE isdelete = false AND id = "+Long.valueOf(ids[0])+" LIMIT 1) ,updater='"+AppUtils.getUserSession().getUsercode()+"' ,updatetime=NOW() WHERE isdelete = false and id = "+jobid;
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			Browser.execClientScript("window.parent.parent.location.reload();");
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
