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
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.ff.apply.jobsnotputerBean", scope = ManagedBeanScope.REQUEST)
public class JobsNotputerBean extends GridView {
	
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
	
	@Action
	public void under(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length <= 0) {
				MessageUtils.alert("Please choose row!");
				return ;
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (String jobid : ids) {
				stringBuilder.append("\nUPDATE bus_shipping SET isunder = TRUE , under = '"+AppUtils.getUserSession().getUsercode()+"' , undertime = NOW() WHERE jobid = " + jobid + ";");
			}
			
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
			MessageUtils.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
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
			update.markUpdate(true, UpdateLevel.Data, "jobids");
			update.markUpdate(true, UpdateLevel.Data, "processinstance_id");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String clauseWhere = "\nAND x.customerid = ANY(SELECT DISTINCT customerid FROM fina_jobs WHERE id IN ("+jobids+"))";
		clauseWhere += "\n AND x.saleid = ANY(SELECT DISTINCT saleid FROM fina_jobs WHERE id IN ("+jobids+"))";
		clauseWhere += "\n AND x.id not in ("+jobids+")";
		clauseWhere += "\nAND COALESCE(y.isput,FALSE) = FALSE";
		clauseWhere += "\nAND (COALESCE(y.putstatus,'') = '' OR (EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = x.id AND s.putstatus IS NOT NULL AND s.putstatus <> '' AND s.putstatus NOT ILIKE '%MBL%' )))";
		clauseWhere += "\nAND x.parentid IS NULL";
		clauseWhere += "\nAND COALESCE(x.nos,'') <> ''";
		clauseWhere += "\n AND ((( y.putstatus IS NULL OR ( y.putstatus NOT ILIKE'%批准:%' AND y.putstatus NOT ILIKE'%同意:%' ) )";
		clauseWhere += "\n AND ( y.putstatus IS NULL OR ( y.putstatus NOT ILIKE'%批准：%' AND y.putstatus NOT ILIKE'%同意：%' ) ))";
		clauseWhere += "\n	OR	( y.putstatus ILIKE'%HBL%' AND y.putstatus ILIKE'%HBL%' ))";
		map.put("clauseWhere", clauseWhere);
		return map;
	}
}
