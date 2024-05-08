package com.scp.view.ff.apply;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.ff.apply.assignuserBean", scope = ManagedBeanScope.REQUEST)
public class AssignUser extends GridView {
	
	@Override
	public void grid_onrowselect() {
		refershs();
	}
	
	@Action
	public void grid_onrowdeselect() {
		refershs();
	}
	
	private void refershs() {
		String args = "";
		if (this.grid.getSelectedIds().length > 0
				&& this.grid.getSelectedIds() != null) {
			args = StrUtils.array2List(this.grid.getSelectedIds());
		}
		Browser.execClientScript("parent.window","argsSaleJsVar.setValue('"+args+"');");
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//分公司过滤
		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
			m.put("corpfilter", "AND corpid = "+AppUtils.getUserSession().getCorpidCurrent());
		}
		return m;
		
	}
	
	
}
