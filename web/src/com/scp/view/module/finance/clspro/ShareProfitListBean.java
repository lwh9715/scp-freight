package com.scp.view.module.finance.clspro;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.clspro.shareprofitlistBean", scope = ManagedBeanScope.REQUEST)
public class ShareProfitListBean extends GridView {


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


	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			this.period = AppUtils.getReqParam("period");
			this.year = AppUtils.getReqParam("year");
			actsetcode = AppUtils.getActsetcode2()+"["+this.year+"]["+this.period+"]";
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			this.qryMap.put("year$", Integer.valueOf(year));
			this.qryMap.put("period$", Integer.valueOf(period));
			this.qryMap.put("actsetid$", AppUtils.getUserSession().getActsetid());
			this.grid.reload();
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//m.put("actsetid", AppUtil.getUserSession().getActsetid());
//		String qry = StrTools.getMapVal(m, "qry");
//		qry += "\nAND actsetid = " + AppUtil.getUserSession().getActsetid();
//		m.put("qry", qry);
		return m;
	}

	/*
	 */
	@Action
	public void autoGenerate() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		for (String id : ids) {
			String sql = "\nSELECT f_fs_arap2vch('jobid="+id+";usercode="+usercode+"');";
			try {
				//AppUtils.debug(sql);
				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
		MessageUtils.alert("OK");
		this.refresh();
	}

}
