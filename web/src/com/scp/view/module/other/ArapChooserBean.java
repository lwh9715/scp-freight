package com.scp.view.module.other;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.other.arapchooserBean", scope = ManagedBeanScope.REQUEST)
public class ArapChooserBean extends GridView {

	@SaveState
	@Bind
	public String jobid;

	public Long userid;

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			jobid = AppUtils.getReqParam("jobid");
			if(StrUtils.isNull(jobid))jobid="-1";
			if(qryMap.isEmpty()){
				qryMap.put("jobid$", jobid);
			}else{
				qryMap.remove("jobid$");
			}
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		if(qryMap.size()>1 && !StrUtils.isNull((String)qryMap.get("jobno"))){
			qryMap.remove("jobid$");
		}
		Map map = super.getQryClauseWhere(queryMap);
		
		String sql = "";
		String jobno = StrUtils.getSqlFormat(StrUtils.getMapVal(qryMap, "jobno"));
		
		sql = "\nAND t.jobid = ANY(select x.id FROM fina_jobs x where x.nos ILIKE '%"+jobno+"%')";
		
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		sql += "\nAND ( EXISTS(SELECT 1 FROM fina_jobs x WHERE  x.saleid = "+AppUtils.getUserSession().getUserid() + "AND x.id = t.jobid AND x.isdelete = false)" 
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.corpidop = "+AppUtils.getUserSession().getCorpid()+" AND x.id = t.jobid AND x.corpid <> x.corpidop ))"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and EXISTS(SELECT 1 FROM fina_jobs x WHERE z.id = x.saleid AND x.id = t.jobid AND x.isdelete = false) AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND EXISTS(SELECT 1 FROM _fina_jobs x WHERE s.linkid = x.shipid AND x.id = t.jobid AND x.isdelete = false) AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)" +
				"\n	AND (EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid='||t.jobid||'"+ ";userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"')  x WHERE x.id = t.id) " +
				" OR (t.corpid = 157970752274 AND EXISTS(SELECT 1 FROM fina_jobs j WHERE j.id = t.jobid AND j.isdelete = FALSE AND j.corpid = ANY(SELECT COALESCE(xx.corpid,0) from sys_user_corplink xx WHERE xx.userid = "+AppUtils.getUserSession().getUserid()+" and ischoose = true))))";
		map.put("filter", sql);
		return map;
	}
	
	@Bind
	@SaveState
	public String argsARAP;
	
	@Bind
	@SaveState
	public String argsCorpid;
	
	@Bind
	@SaveState
	public String argsCustomerid;
	
	@Bind
	@SaveState
	public Boolean iscomplete = false;
	
	@Action
	public void impConfirm() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		try {
			String sql = "\nSELECT f_fina_fee_impotherjobs('arapids=" + StrUtils.array2List(ids)+";iscomplete="+iscomplete
			+ ";tojobid="+this.jobid+";usercode="+AppUtils.getUserSession().getUsercode()+";arap="+argsARAP+";corpid="+argsCorpid+";customerid="+argsCustomerid+"')";
			Map m = this.serviceContext.arapMgrService.daoIbatisTemplate
			.queryWithUserDefineSql4OnwRow(sql);
			this.alert("OK!");
			Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Override
	public void qryRefresh() {
		if(StrUtils.isNull((String)qryMap.get("jobno"))&&StrUtils.isNull((String)qryMap.get("refno"))){
			MessageUtils.alert("检索条件(工作单号和参考号)不能同时为空!");
			return;
		}
		super.qryRefresh();
	}
}
