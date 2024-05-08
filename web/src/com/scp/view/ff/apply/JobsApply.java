package com.scp.view.ff.apply;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.ff.apply.jobsapplyBean", scope = ManagedBeanScope.REQUEST)
public class JobsApply extends GridView {
	
	@Bind
	@SaveState
	public String jobids = "-1";
	
	@Bind
	@SaveState
	public String processinstance_id = "-1";
	
	
	@Bind
	public UIButton hold;
	
	@Bind
	public UIButton release;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			String type = AppUtils.getReqParam("type");
			String ctrl = AppUtils.getReqParam("ctrl");
			super.applyGridUserDef();
			if("apply".equals(type)) {
				jobids = id;
			}else{
				processinstance_id = id;
			}
			hold.setDisabled(true);
			release.setDisabled(true);
			if("ctrl".equals(ctrl)) {
				hold.setDisabled(false);
				release.setDisabled(false);
			}
			
			update.markUpdate(true, UpdateLevel.Data, "jobids");
			update.markUpdate(true, UpdateLevel.Data, "processinstance_id");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String clauseWhere = "AND (j.id IN("+jobids+") OR r.processinstance_id = '"+processinstance_id+"')";
		map.put("clauseWhere", clauseWhere);
		return map;
	}
	
	
	@Action
	public void hold(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			alert("请勾选要选择的行!");
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			stringBuilder.append("\nUPDATE t_ff_process_ref set state = 'H' WHERE refid = "+id+";"); 
		}
		if(stringBuilder.length()>0){
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
		}
		this.refresh();
	}
	
	@Action
	public void release(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			alert("请勾选要选择的行!");
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			stringBuilder.append("\nUPDATE t_ff_process_ref set state = 'R' WHERE refid = "+id+";"); 
		}
		if(stringBuilder.length()>0){
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
		}
		this.refresh();
	}
	
	
	
	@Action
	public void del(){
		if(processinstance_id.equals("-1")){
			alert("还未提交，请关掉当前页面，重新申请!");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if(ids != null && ids.length>0){
			StringBuilder stringBuilder = new StringBuilder();
			for (String id : ids) {
				String sql = "\nDELETE FROM t_ff_process_ref WHERE refid = "+id+" AND processinstance_id = '"+processinstance_id+"';";
				stringBuilder.append(sql);
			}
			if(stringBuilder.length()>0){
				this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
			}
			this.refresh();
		}
	}
}
