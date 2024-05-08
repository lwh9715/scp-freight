package com.scp.view.module.air;

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
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;
@ManagedBean(name = "pages.module.air.airplanetype_linkBean", scope = ManagedBeanScope.REQUEST)
public class AirplanetypelinkBean extends GridSelectView {
	
	
	@SaveState
	@Bind
	public String corid;
	
	@SaveState
	@Bind
	public String planetype;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			String coridStr = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(coridStr))corid=coridStr;
		}
	}
	
	@Action
	public void save() {
		String[] ids = this.grid.getSelectedIds();
		StringBuffer stringBuffer = new StringBuffer();
		if (ids == null || ids.length <= 0) {
			stringBuffer.append("\nDELETE FROM dat_airplanetype_link WHERE corporationid = "+Long.parseLong(corid)+";");
		}else{
			stringBuffer.append("\nDELETE FROM dat_airplanetype_link WHERE corporationid = "+Long.parseLong(corid)+" AND airplanetypeid NOT IN ("+StrUtils.array2List(ids)+");");
		}
		for (String cid : ids) {
			stringBuffer.append("\nINSERT INTO dat_airplanetype_link(id, airplanetypeid, corporationid) SELECT  getid(), "+cid+", "+Long.parseLong(corid)+" FROM _virtual WHERE NOT EXISTS(SELECT 1 FROM dat_airplanetype_link WHERE airplanetypeid="+cid+" AND corporationid="+Long.parseLong(corid)+");");
		}
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			Browser.execClientScript("reloadPage()");
			this.grid.rebind();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("corid", corid);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(planetype)){
			qry += "\nAND t.planetype ILIKE '%"+planetype+"%'";
		}
		map.put("qry", qry);
		return map;
	}

	@Override
	public void clearQryKey() {
		planetype = "";	
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.qryRefresh();
	}
	
	
	@Override
	public int[] getGridSelIds() {
		String sql = "\nselect "+
			"\nexists(SELECT 1 FROM dat_airplanetype_link x WHERE x.airplanetypeid=t.id AND x.corporationid = "+Long.parseLong(corid)+") AS islink"+
			"\nFROM dat_airplanetype t"+ 
			"\nWHERE 1=1 "+
			"\n	AND isdelete = false"+
			"\n AND t.id IN (SELECT x.airplanetypeid FROM dat_airplanetype_link x, dat_airplanetype y, sys_corporation z WHERE z.id = x.corporationid AND x.corporationid = "+Long.parseLong(corid)+")"+
			"\n	ORDER BY islink DESC;";
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
