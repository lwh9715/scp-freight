package com.scp.view.module.cs;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.cs.csbookingListBean", scope = ManagedBeanScope.REQUEST)
public class CsbookingListBean extends GridView {
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}

	@Action
	public void setReceived() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要受理的行");
			return;
		}
		try {
			String id = StrUtils.array2List(ids);
			String sql = "UPDATE cs_booking SET isreceived = true,receiveddate=now(),receivedder='"
						+AppUtils.getUserSession().getUsercode()+"' WHERE id IN ("+id+");"; 
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.alert("受理成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void setNotReceived() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要取消受理的行");
			return;
		}
		try {
			String id = StrUtils.array2List(ids);
			String sql= "UPDATE cs_booking SET isreceived = false,receiveddate=now(),receivedder='"
						+AppUtils.getUserSession().getUsercode()+"' WHERE id IN ("+id+");"; 
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.alert("取消受理成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void delBatch(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要取消受理的行");
			return;
		}
		try {
			String id = StrUtils.array2List(ids);
			String sql= "UPDATE cs_booking SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id IN ("+id+");"; 
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_booking";
		String url = "./csbookingedit.xhtml?id=" + this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
//		String corpfilter = " AND o.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") ";
		String corpfilter = "\n	AND( o.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n OR o.saleid IS NULL"
				+ "\n OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = o.saleid " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = o.saleid " //组关联业务员的单，都能看到
				+ "))";
		m.put("corpfilter", corpfilter);
		return m;
	}
}
