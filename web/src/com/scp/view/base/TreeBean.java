package com.scp.view.base;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;
import org.operamasks.faces.component.widget.ExtConfig;
import org.operamasks.faces.component.widget.UIToolBar;

import com.scp.base.MultiLanguageBean;
import com.scp.service.LoginService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@ManagedBean(name = "main.treeBean", scope = ManagedBeanScope.REQUEST)
public class TreeBean {

	private static ExtConfig config = new ExtConfig();
	private static ExtConfig config2 = new ExtConfig();

	@ManagedProperty("#{loginService}")
	public LoginService loginService;

	static {
		config.set("closable", false);
		config.set("border", false);
		config.set("fit", true);
		config2.set("border", false);
		config2.set("fit", true);
	}

	@Bind
	public UITree navTree;

	@Inject(value = "l")
	private MultiLanguageBean l;

	@Action
	public void openNavTreeAjax() {
		String index = (String) AppUtils.getReqParam("index");
		TreeDataProvider dataProvider = new TreeDataProvider();
		dataProvider.initTreeAll(l);
		navTree.setValue(dataProvider);
		// navTree.setRootVisible(false);
		navTree.setLoadAllNodes(true);
		navTree.repaint();
		// update.markUpdate("navTree");
	}

	/**
	 * 绑定页面上的ToolBar组件
	 */
	@Bind
	public UIToolBar toolBar;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			TreeDataProvider dataProvider = new TreeDataProvider();
			dataProvider.initTreeAll(l);
			navTree.setValue(dataProvider);
			// navTree.repaint();
		}
	}

	@Action
	public void navTree_onclick() {
		UITreeNode node = navTree.getEventNode();
		if (node != null && node.getUserData() != null) {
			TreeNode selectedNode = (TreeNode) node.getUserData();

			String url = selectedNode.getUrl();
			if (StrUtils.isNull(url))
				return;
//			addTab(selectedNode.getName(), url, selectedNode.getIcon());
		}
	}
}
