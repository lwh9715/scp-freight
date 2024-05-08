package com.scp.view.module.air;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusShipjoinlink;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.air.airjobschooseBean", scope = ManagedBeanScope.REQUEST)
public class AirjobschooseBean extends GridView{
	
	@SaveState
	@Accessible
	public BusShipjoinlink  busShipLink =new BusShipjoinlink();
	
	@SaveState
	@Accessible
	public Long parentid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("jobid").trim();
			parentid=Long.valueOf(code);
		}
	}
	
	
	@Action
	public void choosejobs(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要加入的行");
			return;
		}
		 try {
			 serviceContext.jobsMgrService.chooseAirjobs(ids,this.parentid,AppUtils.getUserSession().getUsercode(),"t");
			 MessageUtils.alert("OK");
			 refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void choosejobsnos(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要加入的行");
			return;
		}
		 try {
			 serviceContext.jobsMgrService.chooseAirjobs(ids,this.parentid,AppUtils.getUserSession().getUsercode(),"f");
			 MessageUtils.alert("OK");
			 refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//单独的子节点,不为空,不为自己,
		qry += "\nAND t.parentid IS NULL AND   t.id <> "+this.parentid;
		//没有子节点;
		qry +="\nAND NOT EXISTS (SELECT 1 from _fina_jobs j where  j.parentid =t.id ) ";
		m.put("qry", qry);
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}
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

		m.put("filter", sql);
		
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		m.put("corpfilter", corpfilter);

		return m;
		
	}
	
	
}
