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

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.user.linksalesBean", scope = ManagedBeanScope.REQUEST)
public class LinkSalesBean extends GridSelectView  {
	
	@SaveState
	@Bind
	public String userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			String useridStr = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(useridStr))userid=useridStr;
			////System.out.println(useridStr);
		}
	}
	
	
	@Action
	public void save() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择关联业务员!");
			return;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\nDELETE FROM sys_custlib_user WHERE userid="+userid+" AND NOT EXISTS(SELECT 1 FROm sys_custlib x WHERE x.id = sys_custlib_user.custlibid AND libtype = 'L');");
		for (String cid : ids) {
			stringBuffer.append("\nINSERT INTO sys_custlib_user(id, custlibid, userid) SELECT  getid(), "+cid+", "+userid+" FROM _virtual WHERE NOT EXISTS(SELECT 1 FROM sys_custlib_user WHERE custlibid="+cid+" AND userid="+userid+");");
		}
		////System.out.println(stringBuffer);
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			//MessageUtils.alert("OK");
			this.grid.repaint();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", userid);
		
		String filter = "";
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x , sys_user y WHERE x.corpid = y.corpid AND y.id = t.userid AND x.ischoose = TRUE AND x.userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())filter += qry;//非saas模式不控制
		map.put("filter", filter);
		
		return map;
	}
	
	@Override
	public int[] getGridSelIds() {
		String sql = 
			"\nselect "+
			"\n  exists(SELECT 1 FROM sys_custlib_user x WHERE x.custlibid = t.id AND x.userid = "+userid+") AS islink"+
			"\nFROM sys_custlib t"+ 
			"\nWHERE 1=1 "+
			"\n	AND libtype = 'S'"+
			"\n	ORDER BY islink DESC,corper,depter,jobdesc,code;";
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
	
	
}
