package com.scp.view.sysmgr.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.user.useroleBean", scope = ManagedBeanScope.REQUEST)
public class UseRoleBean extends GridSelectView {


	@SaveState
	@Bind
	public String userid;

	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			String useridStr = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(useridStr))userid=useridStr;
		}
	}


	@SuppressWarnings("deprecation")
	@Action
	public void save() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
//			MessageUtils.alert("请选择角色!");
//			return;
		}

		//找出新增的权限，添加授权日志
        for (String cid : ids) {
            String atsql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole WHERE userid = " + userid + " AND roleid = " + cid + " AND isdelete = false) as auth";
            Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(atsql);
            if ("false".equals(map.get("auth").toString())) {
                String atsql2 = "SELECT name FROM sys_role WHERE id = " + cid + " AND isdelete = false LIMIT 1;";
                Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(atsql2);
                String sql2 = "SELECT f_auto_optrace('jobid=" + userid + ";linkid=0;usr=" + AppUtils.getUserSession().getUsername() +
						";tbl=sys_userinrole;col=roleid;vnew=" + map2.get("name").toString() + ";remarks=新增授权：" + map2.get("name").toString() + "');";
                serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql2);
            }
        }

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\nDELETE FROM sys_userinrole WHERE userid=" + userid + " AND isdelete = false AND orgid is NULL AND parentid is null " +
				"AND id NOT IN(SELECT DISTINCT i.id FROM sys_userinrole i , sys_role r , sys_user u WHERE u.code = r.code and r.roletype = 'C'" +
				" AND i.userid = u.id AND i.roleid = r.id AND u.id =" + userid + ");");

		for (String cid : ids) {
			stringBuffer.append("\nINSERT INTO sys_userinrole(id, roleid, userid) SELECT  getid(), " + cid + ", " + userid +
					" FROM _virtual WHERE NOT EXISTS(SELECT 1 FROM sys_userinrole WHERE roleid = " + cid + " AND userid=" + userid +
					" AND isdelete = false);");
		}
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			this.grid.rebind();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		queryMap.put("roletype", "M");
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", userid);
		return map;
	}

	@Override
	public int[] getGridSelIds() {
		String sql =
			"\nselect "+
			"\nexists(SELECT 1 FROM sys_userinrole x WHERE x.isdelete = false AND x.roleid=t.id AND x.userid = "+userid+") AS islink"+
			"\nFROM sys_role t"+
			"\nWHERE 1=1 "+
			"\n	AND isdelete = false"+
			"\n AND t.id NOT IN(SELECT r.id FROM sys_role r , sys_user u where u.code = r.code AND r.roletype = 'C' AND u.id = "+userid+")" +
			"\n	ORDER BY islink DESC,code;";
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if((Boolean)map.get("islink"))rowList.add(rownum);
				rownum++;
			}
			int row[] = new int[rowList.size()];
			for (int i = 0; i < rowList.size(); i++) {
				row[i] = rowList.get(i);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Action
	public void linkUser() {
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String winId = "_userdtl";
		String url = "./userdtl.xhtml?id=" + this.getGridSelectId();
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}

	@Action
	public void linkModule() {
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String winId = "_modrole";
		String url = "./modrole.xhtml?id=" + this.getGridSelectId();
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
}
