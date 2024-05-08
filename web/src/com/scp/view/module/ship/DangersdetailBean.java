package com.scp.view.module.ship;

import java.util.Map;
import java.util.regex.Pattern;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;
@ManagedBean(name = "pages.module.ship.dangersdetailBean", scope = ManagedBeanScope.REQUEST)
public class DangersdetailBean extends EditGridView{
	
	@SaveState
	@Accessible
	@Bind
	public Long cid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("cid").trim();
		if(id!=null&&id!=""){
			cid = Long.valueOf(id);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry+="\n AND linkid =" + cid;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busContainerMgrService.updateDangerEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busContainerMgrService.addDangerEditGrid(addedData, cid);
	}
	@Override
	protected void remove(Object removedData) {
		serviceContext.busContainerMgrService.removedBatchEditGrid(removedData);
	}
	
	
	@Action(id = "removes")
    public void removes() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if(p.matcher(s).matches()){
				this.serviceContext.busContainerMgrService.removeDate(Long.parseLong(s));
			}
		}
		editGrid.remove();
    }
	
}
