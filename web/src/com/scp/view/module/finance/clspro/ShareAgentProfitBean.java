package com.scp.view.module.finance.clspro;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
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

@ManagedBean(name = "pages.module.finance.clspro.shareagentprofitBean", scope = ManagedBeanScope.REQUEST)
public class ShareAgentProfitBean extends GridView {

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
	
	
	
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}

			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			String year = "";
			String period = "";
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				year = m.get("year").toString();
				period = m.get("period").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//设置 默认值    如果period = 1 那么 year -1 ,period = 12;
			if(Integer.valueOf(period)==1){
				this.year = (Integer.valueOf(year)-1)+"";
				this.period = 12+"";
			}else{
				this.year = (Integer.valueOf(year))+"";
				this.period =(Integer.valueOf(period)-1)+"";
			}
			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "period");
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
		}
	}

	@Override
	public void grid_ondblclick() {
		
	}
	
	
	@Action
	public void add() {
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//m.put("actsetid", AppUtil.getUserSession().getActsetid());
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND extract(year from j.jobdate) = " + this.year ;
		qry += "\nAND extract(month from j.jobdate) =" + this.period ;
		qry += "\nAND EXISTS(SELECT 1 FROM fs_actset_link xx WHERE xx.actsetid = "+AppUtils.getUserSession().getActsetid()+" AND xx.corpid = j.corpid)" ;
		//qry += "\nAND actsetid = " + AppUtil.getUserSession().getActsetid() ;
		m.put("qry", qry);
		return m;
	}

	
	/*
	 */
	@Action
	public void autoGenAgentBill() {
		try {
			String sql = "SELECT f_fs_agenbill_create('actsetid="+AppUtils.getUserSession().getActsetid()+";type=FCL;year="+this.year+";period="+this.period+"');";
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.alert("FCL OK");
			
			sql = "SELECT f_fs_agenbill_create('actsetid="+AppUtils.getUserSession().getActsetid()+";type=AIR;year="+this.year+";period="+this.period+"');";
			m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.alert("AIR OK");
			
			sql = "SELECT f_fs_agenbill_create('actsetid="+AppUtils.getUserSession().getActsetid()+";type=LCL;year="+this.year+";period="+this.period+"');";
			m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.alert("LCL OK");
			
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			return;
		}
	}
	
	
	/*
	 */
	@Action
	public void autoGenerateVch() {
		try {
			Long agentBillid = this.getGridSelectId();
			String sql = "SELECT f_fs_agenbill2vch('id="+agentBillid+";usercode="+AppUtils.getUserSession().getUsercode()+"') AS vchid;";
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			return;
		}
	}


}
