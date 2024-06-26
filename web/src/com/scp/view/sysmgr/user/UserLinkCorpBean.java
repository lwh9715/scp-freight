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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.user.userlinkcorpBean", scope = ManagedBeanScope.REQUEST)
public class UserLinkCorpBean extends GridSelectView {

	@Bind
	@SaveState
	private String userid ="";


	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			userid = (String) AppUtils.getReqParam("id");
			this.update.markUpdate(UpdateLevel.Data, "userid");
		}

	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", userid);
		return map;
	}

	@Override
	public int[] getGridSelIds() {
		String sql;
		if ("-1".equals(userid)) {
			return null;
		} else if (!StrUtils.isNull(userid)) {
			sql =
					"\nSELECT "+
							"\ncorpid ,true AS islink"+
							"\nFROM sys_user_corplink s"+
							"\nWHERE 1=1 "+
							"\n	AND userid = " + userid +
							"\nUNION ALL"+
							"\nSELECT "+
							"\nid As corpid , false AS islink"+
							"\nFROM sys_corporation c "+
							"\nWHERE 1=1 "+
							"\n	AND c.isdelete = false and c.iscustomer = false"+
							"\n	AND NOT EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = c.id AND x.userid = " + userid + ")"+
							"\nORDER BY corpid;";
			try {
				List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
				List<Integer> rowList = new ArrayList<Integer>();
				int rownum = 0;
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					if ((Boolean) map.get("islink"))
						rowList.add(rownum);
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
		}

		return null;
	}



	@Action
	protected void autoChoose() {
		String sql = "UPDATE sys_user_corplink SET ischoose = true WHERE userid = "+userid;
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.alert("删除异常!");
			e.printStackTrace();
		}
	}

	@Override
	protected void save() {
		String[] ids = this.grid.getSelectedIds();

		if (userid ==null || "".equals(userid)) {
			MessageUtils.alert("请选择需要授权的用户!");
			return;
		}

		this.delAll(userid);//此方法作用重置授权
		String sql = "";
		for (String id : ids) {
			sql += "\nINSERT INTO sys_user_corplink(id,userid,corpid)VALUES(getid()," + userid + "," + id + ");";
		}
		if (!"".equals(sql)) {
			try {
				this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			} catch (Exception e) {
				MessageUtils.alert("操作异常!");
				e.printStackTrace();
				return;
			}
		}
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "gridPanel");
		//MessageUtils.alert("授权成功!");
	}

	/**
	 * 先将选中的用户所有权套删除，在重新添加上去.
	 */
	public void delAll(String userid){
		String sql = "DELETE FROM sys_user_corplink WHERE userid = "+userid;
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
		} catch (Exception e) {
			MessageUtils.alert("删除异常!");
			e.printStackTrace();
		}
	}
}
