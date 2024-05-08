package com.scp.view.module.price;

import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.pricegroupleditBean", scope = ManagedBeanScope.REQUEST)
public class PricegrouplEditBean extends GridView{
	
	@Bind
	public UIIFrame userIframe;
	
	@Bind
	public UIIFrame custIframe;
	
	
	
	/**
	 * @param isPostBack
	 */
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String libid = AppUtils.getReqParam("libid");
			String tips = AppUtils.getReqParam("tips");
			if(!StrUtils.isNull(libid)) {
				userIframe.load("./priceuser.xhtml?libid="+libid+"&tips="+tips);
				custIframe.load("./pricecust.xhtml?libid="+libid);
			}
		}
	}

}
