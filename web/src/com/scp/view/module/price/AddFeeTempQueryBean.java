package com.scp.view.module.price;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.base.CommonComBoxBean;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.addfeetempqueryBean", scope = ManagedBeanScope.REQUEST)
public class AddFeeTempQueryBean extends GridView {

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	@Bind(id="template")
    public List<SelectItem> getTemplate() {
    	try {
    		Map m = serviceContext.daoIbatisTemplate.
    		queryWithUserDefineSql4OnwRow("SELECT string_agg(corpid||'' , ',') AS corpid FROM sys_user_corplink  WHERE  ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid());
        	String corpid = m.get("corpid").toString();
			return CommonComBoxBean.getComboxItems("DISTINCT templatename","templatename","price_fcl_feeadd AS d","WHERE istemplate=true AND isdelete = false AND templatename is not null AND corpid in ("+corpid+")","ORDER BY templatename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
//	@Action
//	public void report() {
//		String openUrl = AppUtils.getRptUrl()
//		+ "/reportJsp/showReport.jsp?raq=/static/rp/addfeetempquery.raq";
//		AppUtils.openWindow("_apAllCustomReport", openUrl);
//	}
	
}
