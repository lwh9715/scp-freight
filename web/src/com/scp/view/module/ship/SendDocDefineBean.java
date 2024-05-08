package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.senddocdefineBean", scope = ManagedBeanScope.REQUEST)
public class SendDocDefineBean extends GridView {
	
	
	
//	@SaveState
//	@Accessible
//	public BusDocexpLink  busDocexpLink =new BusDocexpLink();
//	
//	@SaveState
//	@Accessible
//	public Long docexpid;
//	
//	@Override
//	public void beforeRender(boolean isPostBack) {
//		super.beforeRender(isPostBack);
//		if (!isPostBack) {
//			String code = AppUtil.getReqParam("sendid").trim();
//			docexpid=Long.valueOf(code);
//		}
//	}

	
	@Bind(id = "grid", attribute = "mergedColumns")
	private String[] mergedColumns = new String[] { 
			"jobno" };
	
	@Action
	public void importShip(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
	    String sql = "UPDATE bus_docdef SET ischoose = TRUE WHERE id IN("+StrUtils.array2List(ids)+")";
		 serviceContext.busDocdefMgrService.choose(ids);
	     MessageUtils.alert("OK");
	     refresh();
	}
	 
//	@Override
//	
//	public Map getQryClauseWhere(Map<String, Object> queryMap) {
//		
//		Map m = super.getQryClauseWhere(queryMap);
//		String qry = StrTools.getMapVal(m, "qry");
//		qry += "\nAND  not exists (" ;
//		qry +="SELECT 1 from bus_docexp_link g where  g.linkid = t.id)";
//		m.put("qry", qry);
//		return m;
//	}
	
	
}
