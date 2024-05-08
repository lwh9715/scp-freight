package com.scp.view.sysmgr.unit;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.unit.busunitchooserBean", scope = ManagedBeanScope.REQUEST)
public class BusUnitChooserBean extends GridView{
	
	@Bind
	@SaveState
	public String arapids;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)) {
				arapids = id;
			}
		}
	}

	@Action
	public void save(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		StringBuilder stringBuilder = new StringBuilder();
		String sql = "INSERT INTO sys_businessunit_joinfee(id, busunitid, arapid)VALUES (getid()";
		String[] arapid = arapids.split(",");
		for (int i = 0; i < ids.length; i++) {
			for (int j = 0; j < arapid.length; j++) {
				stringBuilder.append(sql + " , " + ids[i] + "," + arapid[j] + ");\n");
			}
		}
		
		//AppUtils.debug(stringBuilder.toString());
		this.serviceContext.userMgrService.sysUserDao.executeSQL(stringBuilder.toString());
		
		this.alert("OK");
		Browser.execClientScript("window.close();");
	}
}
