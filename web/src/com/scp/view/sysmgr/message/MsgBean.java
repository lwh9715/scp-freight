package com.scp.view.sysmgr.message;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.message.msgBean", scope = ManagedBeanScope.REQUEST)
public class MsgBean extends GridView {
	
	
	@SaveState
	private String modulecode;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(!StrUtils.isNull(AppUtils.getReqParam("modulecode"))){
				modulecode = AppUtils.getReqParam("modulecode");
			}
			super.applyGridUserDef();
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		m.put("filter", "\n AND modulecode = '"+modulecode+"'");
		return m;
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_msgshow";
		String url = "./msgshow.xhtml?id="+this.getGridSelectId();
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
}
