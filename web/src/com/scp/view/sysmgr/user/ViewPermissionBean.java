package com.scp.view.sysmgr.user;

import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.tree.impl.UITree;

import com.scp.base.MultiLanguageBean;
import com.scp.service.sysmgr.RoleMgrService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;
import com.scp.view.sysmgr.role.TreeDataProviderAll;

@ManagedBean(name = "pages.sysmgr.user.viewpermissionBean", scope = ManagedBeanScope.REQUEST)
public class ViewPermissionBean extends GridSelectView {
	
	
	@ManagedProperty("#{roleMgrService}")
	public RoleMgrService roleMgrService;

	@Bind
	@SaveState
	private String roleid;


//	@Bind
//	public UITree navTree0;
	@Bind
	public UITree navTree01;
	
	@Bind
	public UITree navTree1;

	@Bind
	public UITree navTree2;
	@Bind
	public UITree navTree3;
	@Bind
	public UITree navTree4;
	@Bind
	public UITree navTree5;
	
	@Bind
	public UITree navTree11;
	
	@Bind
	public UITree navTree6;
	@Bind
	public UITree navTree7;
	
	@Bind
	public UITree navTree9;
	
	@Bind
	public UITree navTree10;
	
	@Bind
	public UITree navTree12;
	
	
	@Inject(value = "l")
	private MultiLanguageBean l;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			String roleid = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(roleid)) {
				this.roleid = roleid;
//				navTree0
//				.setValue(new TreeDataProvider(Long.valueOf("100000"), roleid , l));
//				
				
				navTree01.setValue(new TreeDataProviderAll(roleid , l));
				
//				navTree1
//				.setValue(new TreeDataProviderAll(Long.valueOf("100900"), roleid , l));
//				
//				navTree2.setValue(new TreeDataProviderAll(Long.valueOf("250000100"), roleid , l));
//				navTree3
//				.setValue(new TreeDataProviderAll(Long.valueOf("400000"), roleid , l));
//				navTree4
//				.setValue(new TreeDataProviderAll(Long.valueOf("500000"), roleid , l));
//				
//				navTree5
//				.setValue(new TreeDataProviderAll(Long.valueOf("510000"), roleid , l));
//				navTree6
//				.setValue(new TreeDataProviderAll(Long.valueOf("700000"), roleid , l));
//				navTree7
//				.setValue(new TreeDataProviderAll(Long.valueOf("800000"), roleid , l));
//				
//				navTree9
//				.setValue(new TreeDataProviderAll(Long.valueOf("900000"), roleid , l));
//				
//				navTree10
//				.setValue(new TreeDataProviderAll(Long.valueOf("955000"), roleid , l));
//				
//				navTree11
//				.setValue(new TreeDataProviderAll(Long.valueOf("600000"), roleid , l));
//				
//				navTree12
//				.setValue(new TreeDataProviderAll(Long.valueOf("455000"), roleid , l));
				// navTree1.setRootVisible(false);
				// navTree1.setExpandAll(true);
				// navTree1.repaint();
			}
		}
	}
}
