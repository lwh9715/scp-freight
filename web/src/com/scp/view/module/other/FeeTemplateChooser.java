package com.scp.view.module.other;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.other.feetemplatechooserBean", scope = ManagedBeanScope.REQUEST)
public class FeeTemplateChooser extends GridView {
	
	
	@Bind
	@SaveState
	public String jobid;
	
	@Bind
	@SaveState
	public String type;
	
	@Bind
	@SaveState
	public String joinid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		
		String jobid = AppUtils.getReqParam("jobid");
		String type = AppUtils.getReqParam("type");
		String joinid = AppUtils.getReqParam("joinids");
		if(!StrUtils.isNull(jobid)) {
			this.jobid = jobid;
		}
		if(!StrUtils.isNull(type)) {
			this.type = type;
		}
		if(!StrUtils.isNull(joinid)) {
			this.joinid = joinid;
		}
		this.update.markUpdate("jobid");
		this.update.markUpdate("type");
		this.update.markUpdate("joinid");
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		qryMapDtl.put("templetid$", this.getGridSelectId());
		gridtl.reload();
	}

	@Override
	public void clearQryKey() {
		if(this.qryMapDtl != null) {
			this.qryMapDtl.clear();
		}
		this.gridtl.reload();
		super.clearQryKey();
	}
	
	@Action
	public void clearQryKey1() {
		if(this.qryMapDtl != null) {
			this.qryMapDtl.clear();
			this.gridtl.reload();
		}
	}
	
	@Action
	public void mgrTemplete() {
		AppUtils.openWindow("", "./feetemplate.xhtml");
	}
		
	
	@Action
	public void refreshDtl() {
		this.gridtl.reload();
	}

	@Action
	public void confirm() {
		String[] ids = this.gridtl.getSelectedIds();
		if(ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one!");
		}
		try {
			
			if(StrUtils.isNull(type)&&StrUtils.isNull(joinid)){
				this.serviceContext.feeTemplateMgrService.importFeeFromTemplet(ids , jobid , AppUtils.getUserSession().getUsercode());
			}else{
				this.serviceContext.feeTemplateMgrService.importFeeFromTemplet(ids , type , jobid , joinid , AppUtils.getUserSession().getUsercode());
			}
			this.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid gridtl;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapDtl = new HashMap<String, Object>();

	@Bind(id = "gridtl", attribute = "dataProvider")
	protected GridDataProvider getGridDtlDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridtl.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere1(qryMapDtl), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridtl.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere1(qryMapDtl));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
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
		+"'AND roleid = ANY(SELECT id FROM sys_role WHERE code = 'dev'))"
		+")";
		m.put("filter", filter);
		return m;
	}
	
	public Map getQryClauseWhere1(Map<String, Object> qryMapDtl) {
		Map m = super.getQryClauseWhere(qryMapDtl);
		//过滤个人和dev组里面的都能看到
		String filter = "AND (ispublic OR inputer = '"+AppUtils.getUserSession().getUsercode()
		+"' OR EXISTS(SELECT 1 FROM sys_userinrole WHERE isdelete = FALSE AND userid ='"+AppUtils.getUserSession().getUserid()
		+"'AND roleid = ANY(SELECT id FROM sys_role WHERE code = 'dev'))"
		+")";
		m.put("filter", filter);
		return m;
	}
	
}
