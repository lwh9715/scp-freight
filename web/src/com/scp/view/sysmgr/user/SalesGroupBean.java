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

@ManagedBean(name = "pages.sysmgr.user.salesgroupBean", scope = ManagedBeanScope.REQUEST)
public class SalesGroupBean extends GridSelectView {
	
	
	@SaveState
	@Bind
	public String libid;
	
	@SaveState
	@Bind
	public String userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			String idStr = (String) AppUtils.getReqParam("libid");
			userid = (String) AppUtils.getReqParam("userid");
			if(!StrUtils.isNull(idStr))libid=idStr;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Action
	public void save() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			//MessageUtils.alert("请选择角色!");
			//return;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\nDELETE FROM sys_custlib_role WHERE custlibid="+libid+";");
		for (String cid : ids) {
			stringBuffer.append("\nINSERT INTO sys_custlib_role(id, roleid, custlibid) SELECT  getid(), "+cid+", "+libid+" FROM _virtual WHERE NOT EXISTS(SELECT 1 FROM sys_custlib_role WHERE roleid="+cid+" AND custlibid="+libid+");");
		}
		//System.out.println(stringBuffer);
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			//MessageUtils.alert("OK");
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
			"\nEXISTS (SELECT 1 FROM sys_custlib_role x , sys_custlib y WHERE x.roleid=t.id AND y.userid = "+userid+" AND x.custlibid = y.id) As islink"+
			"\nFROM sys_role t"+ 
			"\nWHERE 1=1 "+
			"\n	AND isdelete = false"+
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
