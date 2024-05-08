package com.scp.view.sysmgr.message;

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
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.sysmgr.message.getmsguserBean", scope = ManagedBeanScope.REQUEST)
public class GetmsguserBean extends GridView{
	
	@Bind
	@SaveState
	String messageid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			messageid = (String)AppUtils.getReqParam("messageid");
			this.update.markUpdate(UpdateLevel.Data, "messageid");
		}
	}
	
	@Action
	public void saveUser() {
		if(StrUtils.isNull(messageid)){
			MessageUtils.alert("请先保存消息，再指派!");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择指派用户!");
			return;
		}
		StringBuilder sql = new StringBuilder();
		for (String cid : ids) {
			sql.append("\nINSERT INTO sys_message_ref(id, messageid, userid,isread)VALUES( getid(),"+messageid+", "+cid+",false);");
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql.toString());
		}
		Browser.execClientScript("parent.chooseAssignUserAfter()");
		//MessageUtils.alert("OK");
	}
}
