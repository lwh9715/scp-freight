package com.scp.view.module.finance;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.modifylogsBean", scope = ManagedBeanScope.REQUEST)
public class ModifyLogsBean extends GridView {
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				//AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}//当前帐套信息
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			String jobid = AppUtils.getUserSession().getActsetid().toString();
			String linkid = AppUtils.getReqParam("linkid");
			
			if(StrUtils.isNull(jobid)) {
				this.qryMap.put("linkid$", linkid);
			}else {
				this.qryMap.put("jobid$", jobid);
			}
		}
	}
}

