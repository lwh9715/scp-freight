package com.scp.view.module.analyse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.dao.cache.CommonDBCache;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.analyse.analyse_branchBean", scope = ManagedBeanScope.REQUEST)
public class Analyse_branchBean  extends GridView{
	
	@Bind
	@SaveState
	public String year;
	
	@Bind
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	public Long corpid = 100L;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
			year = sdf.format(new Date());
			userid = AppUtils.getUserSession().getUserid();
			initUrl();
		}
	}
	
	private void initUrl(){
//		Browser.execClientScript("initData();");
	}
	
	/**
	 * 分公司信息
	 * @return
	 */
	@Bind(id="branchcompany")
    public List<SelectItem> getBranchcompany() {
    	try {
    		List<SelectItem> lists =  commonDBCache.getComboxItems("d.corpid","(SELECT COALESCE(x.code,'') ||'/'|| COALESCE(x.abbr,'') FROM sys_corporation x where x.id = d.corpid)","sys_user_corplink AS d","WHERE userid = 81433600","ORDER BY id");
    		return lists;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Resource
	public CommonDBCache commonDBCache;
}
