package com.scp.view.module.finance.rp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.rp.actpaycreatelinkBean", scope = ManagedBeanScope.REQUEST)
public class ActpayCreateLinkBean extends GridView {
	
	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind 
	@SaveState
	public Long corpid;
	
	@Bind 
	@SaveState
	public Long corpidop;
	
	
	@Bind 
	@SaveState
	public Date jobdateBefore;
	
	@Bind 
	@SaveState
	public Date jobdateEnd;
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
//		corpid = AppUtils.getUserSession().getCorpid();
		jobdateBefore =  (jobdateBefore==null?Calendar.getInstance().getTime():jobdateBefore);
		jobdateEnd = (jobdateEnd==null?Calendar.getInstance().getTime():jobdateEnd);
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
	}

	@Override
	public void refresh() {
		if(corpid == null){
			MessageUtils.alert("必须选择往来公司!");
			return;
		}
		if(corpidop == null){
			MessageUtils.alert("必须选择往来公司2!");
			return;
		}
		super.refresh();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		
		String extClauseWhere = "";
		String extClauseWhere1 = "";
		String extClauseWhere2 = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		extClauseWhere += "\nAND j.jobdate::DATE BETWEEN '"+sdf.format(jobdateBefore)+"' AND '"+sdf.format(jobdateEnd)+"'";
		//x.customerid = j.corpid AND x.corpid = j.corpidop
		//and x.customerid = j.corpidop AND x.corpid = j.corpid
		map.put("corpid", " ");
		map.put("corpid2", " ");
		map.put("corpidop", " ");
		map.put("corpidop2", " ");
		if(corpid != null && corpid > 0){
			extClauseWhere1 += "\nAND " + corpid + " = ANY(SELECT corpid UNION SELECT corpidop UNION SELECT corpidop2 UNION SELECT corpid FROM fina_corp WHERE isdelete =FALSE AND jobid = j.id)";
			map.put("corpid", " AND x.customerid = " + corpid);
			map.put("corpid2", " AND x.corpid = " + corpid);
		}
		
		if(corpidop != null && corpidop > 0){
			extClauseWhere2 += "\nAND " + corpidop + " = ANY(SELECT corpid UNION SELECT corpidop UNION SELECT corpidop2 UNION SELECT corpid FROM fina_corp WHERE isdelete =FALSE AND jobid = j.id)";
			map.put("corpidop", " AND x.corpid = " + corpidop);
			map.put("corpidop2", " and x.customerid = " + corpidop);
		}
		
		map.put("extClauseWhere", extClauseWhere);
		map.put("extClauseWhere1", extClauseWhere1);
		map.put("extClauseWhere2", extClauseWhere2);
		
		if(isfilter){
			map.put("filterAmtstl", " AND EXISTS(SELECT 1 FROM fina_arap x where x.jobid = j.id and x.isdelete = false and x.isfinish2 = false " +
					"and ((x.customerid = j.corpid AND x.araptype = 'AR' AND x.corpid = j.corpidop) OR (x.customerid = j.corpidop AND x.araptype = 'AP' AND x.corpid = j.corpid) ))");
		}
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( j.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (j.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND j.corpid <> j.corpidop AND j.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = j.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = j.saleid) " //组关联业务员的单，都能看到
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = j.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		map.put("filter", sql);
		
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = j.corpid OR x.corpid = j.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR j.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(j.saleid,0) <= 0)";
		map.put("corpfilter", corpfilter);
		return map;
	}

	@Bind
	public UIWindow showSendMessageWindow;
	
	@Bind
	public String hintMessage;
	
	@Bind
	public Set chooseRole;
	
	@Bind
	public String messageContext;
	
	
	@Bind
	public UICheckBox filterAmtstl;
	
	@Bind
	@SaveState
	@Accessible
	public Boolean isfilter = false;
	
	
	
	@Action
	public void genRP() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String serachsql = "SELECT f_fina_generate_rp_link('userid="
			+ AppUtils.getUserSession().getUserid()
			+ ";corpid="
			+ (corpid == null ? "" : corpid)
			+ ";corpidop="
			+ (corpidop == null ? "" : corpidop)
			+ ";jobdateBefore="
			+ sdf.format(jobdateBefore)
			+ ";') AS nos;";
		//System.out.println(serachsql);
		try {
			Map map = this.serviceContext.userMgrService.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(serachsql);
			if(map != null && map.containsKey("nos")){
				if(" | ".equals(map.get("nos").toString())){
					MessageUtils.alert("没有符合条件的费用，没有生成收付对账单!");
				}else{
					MessageUtils.alert("生成的收付对账单号为:"+map.get("nos").toString());
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void gencheckbill() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String serachsql = "SELECT f_fina_generate_checkbill_link('userid="
			+ AppUtils.getUserSession().getUserid()
			+ ";corpid="
			+ (corpid == null ? "" : corpid)
			+ ";corpidop="
			+ (corpidop == null ? "" : corpidop)
			+ ";jobdateBefore="
			+ sdf.format(jobdateBefore)
			+ ";') AS nos;";
		//System.out.println(serachsql);
		try {
			Map map = this.serviceContext.userMgrService.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(serachsql);
			if(map != null && map.containsKey("nos")){
				if(" | ".equals(map.get("nos").toString())){
					MessageUtils.alert("没有符合条件的费用，没有生成对账!");
				}else{
					MessageUtils.alert("生成的对账单号为:"+map.get("nos").toString());
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void sendMessageBtn() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择一条记录");
		} else {
			this.hintMessage = "您好,您本次操作一共选择了" + ids.length
					+ "条记录,推送需要一定时间,预计耗时" + ids.length + "秒,推送后请耐心等候系统提示。";
			update.markUpdate(true, UpdateLevel.Data, "sendMessagePanelGrid");
			showSendMessageWindow.show();
		}
	}

	@Action
	public void sendMessages() {
		if (this.chooseRole == null || this.chooseRole.size() == 0
				|| this.messageContext == null
				|| this.messageContext.trim().length() == 0) {
			MessageUtils.alert("至少勾选一个角色以及发送内容不能为空或者全空格!");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		String tmpid = "";
		for (String s : ids) {
			tmpid += s + ",";
		}
		tmpid = tmpid.substring(0, tmpid.length() - 1);
		String tmprole = "";
		for (Object c : chooseRole) {
			tmprole += c.toString() + ",";
		}
		tmprole = tmprole.substring(0, tmprole.length() - 1);
		String serachsql = "SELECT * FROM f_ship_cost_confirm_send_message('userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";roletypes="
				+ tmprole
				+ ";arapids="
				+ tmpid
				+ ";context="
				+ messageContext
				+ ";');";
		Map map = this.serviceContext.userMgrService.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(serachsql);
		String sql = map.get("f_ship_cost_confirm_send_message").toString();
		MessageUtils.alert("OK");
		showSendMessageWindow.close();
//		if (applicationConf.getIsUseDzz() && sql.length() > 0) {
//			serviceContext.dzzService.sendMessageOfShipCostConfirm(sql);
//		}

	}
}
