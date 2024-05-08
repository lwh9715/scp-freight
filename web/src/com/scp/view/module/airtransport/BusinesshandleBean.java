package com.scp.view.module.airtransport;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.airtransport.businesshandleBean", scope = ManagedBeanScope.REQUEST)
public class BusinesshandleBean extends GridView{

	
	@SaveState
	private String type;
	
	@SaveState
	@Bind
	private String qryAgent;
	
	@SaveState
	@Bind
	private String qryBookingcode;
	
	@SaveState
	@Bind
	private String qryPolcode;
	
	@SaveState
	@Bind
	private String qryPodcode;
	
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	@Override
	public void clearQryKey() {
		qryAgent = "";
		qryBookingcode = "";
		qryPolcode = "";
		qryPodcode = "";
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefresh();
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" 
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
									"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpid()+")"+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) "
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
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		
		if("Q".equals(type)){
		}else if("Y".equals(type)){//已处理
			sql +="\nAND EXISTS(select 1 from _bpm_task where refid = t.id::text and state = '9' AND taskname IN ('商务审核','订舱审核'))";
		}else if("N".equals(type)){//待处理
			sql +="\nAND EXISTS(select 1 from _bpm_task where refid = t.id::text and state <> '3' and state <> '9' AND taskname IN ('商务审核','订舱审核'))";
		}
		
		if(!StrUtils.isNull(qryAgent)){
			sql += "\nAND EXISTS(SELECT 1 FROM sys_corporation c, bus_air b WHERE c.abbr ILIKE '%"+qryAgent+"%' AND b.jobid = t.id AND c.isagent = TRUE AND c.id = b.agentid)";
		}
		
		if(!StrUtils.isNull(qryBookingcode)){
			sql += "\nAND EXISTS(SELECT 1 FROM bus_air b WHERE b.bookingcode ILIKE '%"+qryBookingcode+"%' AND b.jobid = t.id)";
		}
		
		if(!StrUtils.isNull(qryPodcode)){
			sql += "\nAND EXISTS(SELECT 1 FROM bus_air b WHERE isdelete = FALSE AND b.jobid = t.id and b.podcode ILIKE '%"+qryPodcode+"%')";
		}
		
		if(!StrUtils.isNull(qryPolcode)){
			sql += "\nAND EXISTS(SELECT 1 FROM bus_air b WHERE isdelete = FALSE AND b.jobid = t.id and b.polcode ILIKE '%"+qryPolcode+"%')";
		}
		
		//neo 20201230 排除之前单及无流程的单
		sql += AirUtil.getCommonFilter();
		
		m.put("filter", sql);
		m.put("icare","1=1");
		
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+"  OR COALESCE(t.saleid,0) <= 0) ";
		m.put("corpfilter", corpfilter);
		m.put("userid", AppUtils.getUserSession().getUserid());
		return m;
	}
	
	
	@Bind
	public UIIFrame taskCommentsIframe;
	
	@Action
	public void showTaskCheckInfo(){
		String jobid = AppUtils.getReqParam("jobid");
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+jobid+"' AND isdelete = FALSE ORDER BY id DESC LIMIT 1";
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
		}
		taskCommentsIframe.load("../../../bpm/bpmshowcomments.jsp?processinstanceid="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		
		Browser.execClientScript("taskCheckInfoWindowJsVar.show()");
	}
	
	@Action
	public void quyall() {
		this.type = "Q";
		super.refresh();
	}
	
	@Action
	public void untreated() {
		this.type = "N";
		super.refresh();
	}
	
	@Action
	public void processed() {
		this.type = "Y";
		super.refresh();
	}
	
}
