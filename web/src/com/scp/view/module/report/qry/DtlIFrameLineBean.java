package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameLineBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameLineBean extends GridView {
	@Bind
	@SaveState
	public String code;
	
	@Bind
	@SaveState
	public String qryValues="";//用于存所以的查询值
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	 
	@Override
	public void clearQryKey() {
		super.clearQryKey();
		this.qryValues = "";
		this.code = "";
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
	
	@Action
	public void qrysAction(){
		if(code!=null&&!code.equals("")){
			qryValues += code+",";
		}
		update.markUpdate(UpdateLevel.Data, "qryValues");
		this.grid.reload();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String sql = m.get("qry").toString();
		if(!qryValues.equals("")){
			String qrysql = "";
			String[] split = qryValues.split(",");
			for(String s:split){
				if(qrysql.equals("")){
					qrysql+="\nAND ((code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%') ";
				}else{
					qrysql+=" OR(code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%') ";
				}
			}
			qrysql+=")";
			m.put("qrysql",qrysql);
		}
		return m;
		
	}
}
