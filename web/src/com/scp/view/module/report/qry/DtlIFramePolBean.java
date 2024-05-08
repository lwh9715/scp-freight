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

@ManagedBean(name = "pages.module.report.qry.dtlIFramePolBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFramePolBean extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	 
	@Override
	public void clearQryKey() {
		this.qryValues = "";
		this.code = "";
		super.clearQryKey();
		Browser.execClientScript("parent.window","argpolJsVar.setValue('');");
		Browser.execClientScript("parent.window","setpolIds('');");
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
	
	@Bind
	@SaveState
	public String code;
	
	@Bind
	@SaveState
	public String qryValues="";//用于存所以的查询值
	
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
		if(!qryValues.equals("")){
			String sql = "";
			String[] split = qryValues.split(",");
			for(String s:split){
				if(sql.equals("")){
					sql+="\n ((code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR province ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR country ILIKE '%"+StrUtils.getSqlFormat(s)+"%')";
				}else{
					sql+="OR (code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR province ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR country ILIKE '%"+StrUtils.getSqlFormat(s)+"%')";
				}
			}
			sql+=")";
			m.put("qry",sql);
		}
		return m;
	}
	
	
}
