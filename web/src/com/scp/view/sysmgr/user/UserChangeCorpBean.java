package com.scp.view.sysmgr.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.user.userchangecorpBean", scope = ManagedBeanScope.REQUEST)
public class UserChangeCorpBean extends GridSelectView {

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
				"\nid ,ischoose AS islink"+ 
				"\nFROM sys_user_corplink s"+
				"\nWHERE 1=1 "+
				"\n	AND userid = " + userid +
				"\nORDER BY id;";
			try {
				List<Map> list = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql(sql);
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

	@Override
	protected void save() {
		String[] ids = this.grid.getSelectedIds();
		if (userid ==null || "".equals(userid)) {
			MessageUtils.alert("请选择需要授权的用户!");
			return;
		}
		String sql = "\nUPDATE sys_user_corplink SET ischoose = FALSE WHERE userid = " + userid + ";";
		for (String id : ids) {
				sql += "\nUPDATE sys_user_corplink SET ischoose = TRUE WHERE id = " + id + ";";
		}
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "gridPanel");
		//MessageUtils.alert("授权成功!");
	}

	@Override
	public void grid_ondblclick() {
		long gridSelectId = this.getGridSelectId();
		String sql = "\nUPDATE sys_user_corplink SET ischoose = FALSE WHERE userid = " + userid + ";";
		sql += "\nUPDATE sys_user_corplink SET ischoose = TRUE WHERE id = " + gridSelectId + ";";
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "gridPanel");
	}
	
}
