package com.scp.view.module.jobs;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.jobs.jobsondeleteBean", scope = ManagedBeanScope.REQUEST)
public class JobsOnDelete extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	@Override
	public void grid_ondblclick() {
//		super.grid_ondblclick();
//		String winId = "_edit_jobsland";
//		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
//		AppUtils.openWindow(winId, url);
	}
	

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="
			+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0)";
		m.put("corpfilter", corpfilter);
		String qry = m.get("qry").toString();
		m.put("qry",qry);
		return m;
	}
	
	@Action
	public void recovery(){
		String jshide = "nosislockwindow.hide();$('#nosislock').html('');";
		Browser.execClientScript(jshide);
		String[] ids = grid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请至少选择一行！");
			return;
		}
		String sql = "SELECT f_fina_jobs_process('jobids="+StrUtils.array2List(ids)+";type=recovery;usercode="+AppUtils.getUserSession().getUsercode()+"') AS return;";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.get("return")!=null){
				String js = "nosislockwindow.show();$('#nosislock').html('"+m.get("return").toString()+"');";
				Browser.execClientScript(js);
				alert("OK! 恢复的工作单请核对费用");
			}
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void delMaster(){
		String js = "nosislockwindow.hide();$('#nosislock').html('');";
		Browser.execClientScript(js);
		String[] ids = grid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请至少选择一行！");
			return;
		}
		String sql = "SELECT f_fina_jobs_process('jobids="+StrUtils.array2List(ids)+";type=delete;usercode=demo') AS return;";
		try {
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			MessageUtils.alert("OK");
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void cleanDate(){
		String sql = "SELECT f_fina_jobs_cleardelete('') AS return;";
		try {
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			MessageUtils.alert("OK");
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
}
