package com.scp.view.module.other;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.other.feetemplateBean", scope = ManagedBeanScope.REQUEST)
public class FeeTemplate extends GridView {
	
	

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}


	@Action
	public void add() {
		String winId = "_edit_feetemplate";
		String url = "./feetemplatedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_feetemplate";
		String url = "./feetemplatedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String orderby = StrUtils.getMapVal(m, "orderby");
		String usercode = AppUtils.getUserSession().getUsercode();
		// 列表排序:将个人并且是自己录入的放到最上面。然后是公共的，然后是其他人录入的
		orderby += "inputer = '" + usercode + "' DESC, isprivate DESC, ispublic DESC,";
		m.put("orderby", orderby);
		
		//过滤个人和dev组里面的都能看到
		String filter = "AND (ispublic OR inputer = '"+AppUtils.getUserSession().getUsercode()
		+"' OR EXISTS(SELECT 1 FROM sys_userinrole WHERE isdelete = FALSE AND userid ='"+AppUtils.getUserSession().getUserid()
		+"'AND roleid =(SELECT id FROM sys_role WHERE code = 'dev'))"
		+")";
		
		AppUtils.filterByCorperChoose(m , "t");
		
		m.put("filter", filter);
		return m;
	}
	
	
	@Action
	public void copyAdd() {
		Long id = this.getGridSelectId();
		if(id == null ){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.feeItemMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_copy_fina_arap_templet('id="+this.getGridSelectId()+";userid="+AppUtils.getUserSession().getUserid()+"');");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.grid.reload();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
}
