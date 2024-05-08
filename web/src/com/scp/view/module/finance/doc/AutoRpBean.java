package com.scp.view.module.finance.doc;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.doc.autorpBean", scope = ManagedBeanScope.REQUEST)
public class AutoRpBean extends GridView {

	@Bind
	public UIIFrame iframeConfig;

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@SaveState
	public Long actsetid;
	
	@Bind
	@SaveState
	@Accessible
	public String startDate;
	
	@Bind
	@SaveState
	@Accessible
	public String endDate;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			actsetid = AppUtils.getUserSession().getActsetid();
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			this.grid.reload();
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		m.put("actsetid", AppUtils.getUserSession().getActsetid());
		String qry = StrUtils.getMapVal(m, "qry");
//		String qry = StrTools.getMapVal(m, "qry");
		qry += "\nAND EXISTS(SELECT 1 FROM fs_actset x WHERE x.corpid = t.corpid AND x.id = " + AppUtils.getUserSession().getActsetid() + ")";
//		m.put("qry", qry);
		if(endDate != null || startDate!= null) {
			qry += "\nAND rpdate  BETWEEN '" + (startDate==null||startDate=="" ? "0001-01-01" : startDate)
			+ "' AND '" + (endDate==null||endDate=="" ? "9999-12-31": endDate) + "'";
		}
		m.put("qry", qry);
		return m;
	}


	/*
	 */
	@Action
	public void autoGenerate() {
		actsetid = AppUtils.getUserSession().getActsetid();
		if(actsetid == null) {
			MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		for (String id : ids) {
			String sql = "\nSELECT f_fs_rp2vch('rpid="+id+";usercode="+usercode+"');";
			try {
				//ApplicationUtils.debug(sql);
				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
		MessageUtils.alert("OK");
		this.refresh();
	}
	
	@Action
	public void config() {
		iframeConfig.load("configrp.xhtml");
		this.editWindow.show();
	}

}
