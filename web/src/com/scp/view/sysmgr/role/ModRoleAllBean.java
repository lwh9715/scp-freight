package com.scp.view.sysmgr.role;

import java.util.ArrayList;
import java.util.List;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;

import com.scp.base.MultiLanguageBean;
import com.scp.service.sysmgr.RoleMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.role.modroleallBean", scope = ManagedBeanScope.REQUEST)
public class ModRoleAllBean extends GridSelectView {
	
	
	@ManagedProperty("#{roleMgrService}")
	public RoleMgrService roleMgrService;

	@Bind
	@SaveState
	private String userid;

	@Bind
	public UITree navTree0;
	@Bind
	public UITree navTree01;
	
	@Bind
	public UITree navTree1;
//	@Bind
//	public UITree navTree2;
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
	
	
	
	
	@Inject(value = "l")
	private MultiLanguageBean l;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			String userid = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(userid)) {
				this.userid = userid;
				navTree0
				.setValue(new TreeDataProvider(userid , l));
				
				
//				navTree01
//				.setValue(new TreeDataProviderAll(Long.valueOf("100500"), userid , l));
//				
//				navTree1
//				.setValue(new TreeDataProviderAll(Long.valueOf("200000"), userid , l));
////				navTree2
////					.setValue(new TreeDataProviderAll(Long.valueOf("3000"), roleid , l));
//				navTree3
//				.setValue(new TreeDataProviderAll(Long.valueOf("400000"), userid , l));
//				navTree4
//				.setValue(new TreeDataProviderAll(Long.valueOf("500000"), userid , l));
//				
//				navTree5
//				.setValue(new TreeDataProviderAll(Long.valueOf("510000"), userid , l));
//				navTree6
//				.setValue(new TreeDataProviderAll(Long.valueOf("700000"), userid , l));
//				navTree7
//				.setValue(new TreeDataProviderAll(Long.valueOf("800000"), userid , l));
//				
//				navTree9
//				.setValue(new TreeDataProviderAll(Long.valueOf("900000"), userid , l));
//				
//				navTree10
//				.setValue(new TreeDataProviderAll(Long.valueOf("955000"), userid , l));
//				
//				navTree11
//				.setValue(new TreeDataProviderAll(Long.valueOf("957000"), userid , l));
				
				// navTree1.setRootVisible(false);
				// navTree1.setExpandAll(true);
				// navTree1.repaint();
			}
		}
	}

	@Action
	public void saveModule() {

		List<UITree> trees = new ArrayList<UITree>();
		trees.add(navTree0);
		trees.add(navTree01);
		trees.add(navTree1);
//		trees.add(navTree2);
		trees.add(navTree3);
		trees.add(navTree4);
		trees.add(navTree5);
		trees.add(navTree6);
		trees.add(navTree7);
		trees.add(navTree9);
		trees.add(navTree10);
		trees.add(navTree11);

		roleMgrService.removeRoleModSelect(userid);
		
		String dmlSql = "";
		for (UITree uiTree : trees) {
			List<UITreeNode> uiTreeNodes = uiTree.getCheckedNodes();
			List<String> ids = new ArrayList<String>();
			for (UITreeNode uiTreeNode : uiTreeNodes) {
				TreeNode treeNode = (TreeNode) uiTreeNode.getUserData();
				ids.add(treeNode.getId());
			}

			uiTreeNodes = uiTree.getPartlyCheckedNodes();
			for (UITreeNode uiTreeNode : uiTreeNodes) {
				TreeNode treeNode = (TreeNode) uiTreeNode.getUserData();
				ids.add(treeNode.getId());
			}
			roleMgrService.saveRoleModSelect(userid, ids);
		}

		MessageUtils.alert("OK!");
		
//		if (!StrTools.isNull(dmlSql)) {
//			try {
//				String dmlSqlBefore = 
//					"\nDELETE FROM sys_modinrole WHERE " +
//					"\nmoduleid NOT IN (SELECT id FROM sys_module x WHERE (x.id = 1000 OR x.pid = 1000))"+
//					"\nAND roleid = "
//						+ roleid + ";";
//				AppDaoUtil.execute(dmlSqlBefore + dmlSql);
//				MsgUtil.alert("OK!");
//				// for (UITree uiTree : trees) {
//				// uiTree.repaint();
//				// }
//			} catch (Exception e) {
//				MsgUtil.showException(e);
//			}
//		}
	}
}
