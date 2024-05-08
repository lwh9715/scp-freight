package com.scp.view.base;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;
import org.operamasks.faces.component.widget.ExtConfig;
import org.operamasks.faces.component.widget.UIToolBar;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.MultiLanguageBean;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.util.TagPanelUtil;

@ManagedBean(name = "main.adminindexBean", scope = ManagedBeanScope.REQUEST)
public class AdminIndexBean{

	private static ExtConfig config = new ExtConfig();
	private static ExtConfig config2 = new ExtConfig();

	static {
		config.set("closable", false);
		config.set("border", false);
		config2.set("border", false);
	}

	@Bind
	public UITree navTree;
	
	
	@Action
	public void openNavTreeAjax(){
		String index = (String)AppUtils.getReqParam("index");
		
//		if("0".equals(index)){
//			this.desktop();
//			return;
//		}
//		
//		if("1000".equals(index)){
//			this.desktop();
//		}
		TreeDataProvider dataProvider = new TreeDataProvider();
		dataProvider.initTree(600000L, l);
		navTree.setValue(dataProvider);
		navTree.setRootVisible(false);
		navTree.setExpandAll(true);
		navTree.repaint();
//		update.markUpdate("navTree");
	}
	
	/**
	 * 绑定页面上的ToolBar组件
	 */
	@Bind
	public UIToolBar toolBar;

	@Bind
	public UITabLayout centerPanel;

	@Bind
	public UIPanel center;

	@Inject(value = "constantBean.getContextPath()")
	protected String contexPath;
	
	@Inject(value = "l")
	private MultiLanguageBean l;


	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			
			try {
				if(!AppUtils.isLogin()){
					Browser.execClientScript("openNewFrame('../login/login.faces');");
					return;
				}
			} catch (Exception e) {
				//e.printStackTrace();
				Browser.execClientScript("openNewFrame('../login/login.faces');");
			}
			
			String id = (String)AppUtils.getReqParam("id");
			if(StrUtils.isNull(id))id="600000";
			TreeDataProvider dataProvider = new TreeDataProvider();
			dataProvider.initTree(600000L, l);
			navTree.setValue(dataProvider);
//			this.desktop();
		}
	}

	
	@Action
	public void navTree_onclick() {
		UITreeNode node = navTree.getEventNode();
		if (node != null && node.getUserData() != null) {
			TreeNode selectedNode = (TreeNode) node.getUserData();
			String url = selectedNode.getUrl();
			if(StrUtils.isNull(url))return;
//			centerPanel.addTab(selectedNode.getName(), url, TagPanelUtil
//					.getTabPanelIconCls(selectedNode.getIcon()), config2);
			addTab(selectedNode.getName(), url, selectedNode.getIcon());
		}
	}
	
	
	public void addTab(String tabName , String url , String ico){
		centerPanel.addTab(tabName, url, TagPanelUtil
				.getTabPanelIconCls(ico), config2);
	}
	

	@Inject
	private PartialUpdateManager update;

	@Bind
	public UIIFrame contentPanelFrame;

	@Bind(id = "contentPanelFrame", attribute = "src")
	public String contentSrc = null;

	private void openWindow(String openUrl) {
		contentSrc = contexPath + "/common/blank.html";
		long winId = System.currentTimeMillis();
		update.markAttributeUpdate(contentPanelFrame, "src");
		Browser.execClientScript("window.open('" + openUrl + "','" + winId
				+ "','toolbar=no, menubar=no, "
				+ "scrollbars=yes, resizable=yes, location=no, status=no');");
	}


	public void openTab(String code) {
	}
}
