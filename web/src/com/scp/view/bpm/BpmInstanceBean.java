package com.scp.view.bpm;

import java.util.Map;

import com.scp.util.StrUtils;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "bpm.bpminstanceBean", scope = ManagedBeanScope.REQUEST)
public class BpmInstanceBean extends GridView{
	
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
		//map.put("filter", "\nAND state <> 3");
		return map;
	}
	
	@Action
	public void stop(){
		process("Stop");
	}
	
	@Action
	public void finish(){
		process("Finish");
	}
	
	@Action
	public void del(){
		process("Delete");
	}
	
	
	
	private void process(String type){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {

				if (type.equals("Delete")) {
					//任务已完成时，非分公司管理员组不可以删除
					String isFin = "SELECT EXISTS(SELECT 1 FROM bpm_processinstance t WHERE t.isdelete = FALSE AND t.state = 9 AND t.id = "+id+") as isfin;";
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
				}

				String sql = "SELECT f_bpm_processins_mgr('id="+id+";type="+type+";usercode="+AppUtils.getUserSession().getUsercode()+"');";
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			}
			MessageUtils.alert("OK!");
			this.grid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
	}
	

	
	@Override
	public void grid_ondblclick(){
		
	}
	
}
