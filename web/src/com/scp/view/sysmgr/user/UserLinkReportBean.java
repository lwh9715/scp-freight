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

@ManagedBean(name = "pages.sysmgr.user.userlinkreportBean", scope = ManagedBeanScope.REQUEST)
public class UserLinkReportBean extends GridSelectView {

	@Bind
	@SaveState
	private String userid ="-100";

	@Bind
	@SaveState
	private String reportType;
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			userid = (String) AppUtils.getReqParam("id");
			this.update.markUpdate(UpdateLevel.Data, "userid");
			reportType = "profit";
		}

	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = " modcode = '"+reportType+"'";
		map.put("filter", filter);
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
				"SELECT " +
				"\n	(CASE WHEN EXISTS ((SELECT 1 FROM sys_report_ctrl x WHERE x.reportid = a.id AND x.linktype = 'U' AND x.linkid = "+userid+")) THEN TRUE ELSE FALSE END) AS islink" +
				"\nFROM sys_report a " +
				"\nWHERE isdelete = FALSE " +
				"\nAND modcode = '"+reportType+"'";
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
	
	@Action(id="reportType",event="onselect")
	public void reportType_select(){
		if (grid != null) {
			gridLazyLoad = false;
			this.grid.repaint();
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
				sql += "\nINSERT INTO sys_report_ctrl(id,linkid,reportid,linktype)VALUES(getid(),"
						+ userid + "," + id + ",'U');";
		}
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "gridPanel");
		//MessageUtils.alert("授权成功!");
	}
	
	/**
	 * 先将选中的用户所有权套删除，在重新添加上去.
	 */
	public void delAll(String userid){
	}
}
