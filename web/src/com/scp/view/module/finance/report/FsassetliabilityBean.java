package com.scp.view.module.finance.report;

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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.fs_asset_liabilityBean", scope = ManagedBeanScope.REQUEST)
public class FsassetliabilityBean extends GridView {

	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String period;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	@SaveState
	public String isbase;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			// 根据当前登录用户，找对应的帐套，过滤兑换率列表
			String sql = "SELECT year,period,isbase FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			String year = "";
			String period = "";
			String isbase = "";
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				year = m.get("year").toString();
				period = m.get("period").toString();
				isbase = m.get("isbase").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.year = year;
			this.period = period;
			this.isbase = isbase;
			
			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "period");
		}
	}
	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		String url = "";
		url = AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/fs_asset_liability.raq"
			+ getArgs();
		AppUtils.openWindow("fs_asset_liability", url);
	}
	
	@Action
	public void refresh() {
		try {
			String querySql = "SELECT * FROM f_fs_act_rpt_init('actsetid="+AppUtils.getUserSession().getActsetid()+";year="+year+";period="+period+"');";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql);
			this.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private String getArgs() {
		String actsetidRpt = StrUtils.isNull(actsetids)?(""+AppUtils.getUserSession().getActsetid()):actsetids;
		String args = "&year=" + year + "&period=" + period + "&actsetid=" + actsetidRpt;
		return args;
	}
	
	
	@Bind
	@SaveState
	public String actsetids;
	
	@Bind
	public UIDataGrid accountgrid;
	
	@Bind
	public UIWindow accountWindow;
	
	@Action
	public void merge() {
		if("true".equals(this.isbase)){
			accountWindow.show();
			this.accountgrid.reload();
		}else{
			MessageUtils.alert("当前账套不是基本账套！");
		}
	}
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMap = new HashMap<String, Object>();
	
	@Bind(id = "accountgrid", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.initconfig.accountBean.grid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMap), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.initconfig.accountBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMap));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		return super.getQryClauseWhere(queryMap);
	}

	@Action
	public void clearQryKeysc() {
		if (qryMap != null) {
			qryMap.clear();
			update.markUpdate(true, UpdateLevel.Data, "accountPanel");
			this.accountgrid.reload();
		}
	}
	
	@Action
	public void confirm() {
		String[] ids = this.accountgrid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("请勾选一条记录！");
			actsetids = "";
			update.markUpdate(true, UpdateLevel.Data, "actsetids");
			return;
		}
		actsetids = StrUtils.array2List(ids);
		update.markUpdate(true, UpdateLevel.Data, "actsetids");
		
	}

}
