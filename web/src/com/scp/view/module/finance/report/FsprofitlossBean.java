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
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.fs_profit_lossBean", scope = ManagedBeanScope.REQUEST)
public class FsprofitlossBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;


	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String period;

	@Bind
	@SaveState
	public Long comid;

	@Bind
	@SaveState
	public String isbase;

	@Bind
	@SaveState
	public String astypecode;
	
	@Bind
	@SaveState
	public String csno;

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
				m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				year = m.get("year").toString();
				period = m.get("period").toString();
				isbase = m.get("isbase").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.year = year;
			this.period = period;
			this.comid = 1l;
			this.isbase = isbase;
			
			this.csno = ConfigUtils.findSysCfgVal("CSNO");

			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "period");
		}
	}

	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		if(year==null||period==null||comid==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		String url = "";
//		url = AppUtils.getRptUrl()
//			+ "/reportJsp/showReport.jsp?raq=/finance/fs_profit_loss.raq"
//			+ getArgs();
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0){
			url = AppUtils.getRptUrl() + "/reportJsp/showReport.jsp?raq=/finance/fs_profit_loss.raq" + getArgs();
		}else{
			url = AppUtils.getRptUrl() + "/reportJsp/showReport.jsp?raq=/finance/fs_profit_loss_group.raq" + getArgs() + "&astids="+StrUtils.array2List(ids) + "&astypecode=" + astypecode;
		}
		AppUtils.openWindow("fs_profit_loss", url);
	}

	private String getArgs() {
		String actsetidRpt = StrUtils.isNull(actsetids)?(""+AppUtils.getUserSession().getActsetid()):actsetids;
		String args = "&year=" + year + "&period=" + period + "&actsetid=" + actsetidRpt;
		args+="&comid="+this.comid;
		args+="&astypecode="+this.astypecode;
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

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		String filter = "\nAND actsetid = " + AppUtils.getUserSession().getActsetid()+"AND astypecode IN ('C','D')";
		filter += "\nAND astypecode = '"+astypecode+"'";
		m.put("filter", filter);
		return m;
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
