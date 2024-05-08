package com.scp.view.bpm;

import java.util.Map;

import com.scp.util.StrUtils;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "bpm.bpmtaskmgrBean", scope = ManagedBeanScope.REQUEST)
public class BpmTaskMgrBean extends GridView{
	
	public Long userid;
	
	public String language;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		language = AppUtils.getUserSession().getMlType().toString();
		if (!isPostBack) {
			super.applyGridUserDef();
			this.gridLazyLoad = true;
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", AppUtils.getUserSession().getUserid());
		map.put("filter", "\nAND state <> 3");
		return map;
	}
	
	@Action
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {

				//任务已完成时，非分公司管理员组不可以删除
				String isFin = "SELECT EXISTS(SELECT 1 FROM bpm_task t WHERE t.state = 9 AND t.id = "+id+") as isfin;";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isFin);
				if (StrUtils.getMapVal(map, "isfin").equals("true")) {
					String isRet = "SELECT EXISTS(SELECT 1 FROM sys_role r, sys_userinrole o WHERE r.id = o.roleid " +
							"AND r.code = 'admin_branch' AND o.userid = "+AppUtils.getUserSession().getUserid()+") as isret;";
					Map map1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isRet);
					if (StrUtils.getMapVal(map1, "isret").equals("false")) {
						MessageUtils.alert("流程已完成，不可再删除");
						return;
					}
				}

				serviceContext.bpmTaskService.removeDate(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	

	
	@Override
	public void grid_ondblclick(){
		
	}
	
	@Bind
	public UIWindow showAttachWindow;
	
	@Bind
	public UIIFrame attachIframe;
	
	@Action
	public void showLogs() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		String url = "./bpmlogs.xhtml?taskid="+ids[0];
		attachIframe.load(url);
		showAttachWindow.setTitle("Logs");
		showAttachWindow.show();
	}
	
}
