package com.scp.view.module.analyse;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.analyse.analyse_profitBean", scope = ManagedBeanScope.REQUEST)
public class Analyse_profitBean  extends GridView{
	
	@Bind
	@SaveState
	public String year;
	
	@Bind
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	public String currencytype;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
			year = sdf.format(new Date());
			userid = AppUtils.getUserSession().getUserid();
			currencytype = "CNY";
			initUrl();
		}
	}
	
	private void initUrl(){
//		Browser.execClientScript("initData();");
	}
	
}
