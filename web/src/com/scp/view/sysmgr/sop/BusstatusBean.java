package com.scp.view.sysmgr.sop;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.MultiLanguageBean;
import com.scp.model.sys.SysDepartment;
import com.scp.view.comp.GridSelectView;
import com.scp.view.sysmgr.company.TreeNode;

@ManagedBean(name = "pages.sysmgr.sop.busstatusBean", scope = ManagedBeanScope.REQUEST)
public class BusstatusBean extends GridSelectView {
	

	@Bind
	public UITree navTree01;
	
	@Inject(value = "l")
	private MultiLanguageBean l;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			refresh();
		}
//		if(StrUtils.isNull(AppUtils.getReqParam("id"))==false){
//			refresh();
//		}
	}
	
	@Override
	public void refresh() {
		navTree01.setValue(new SopTreeDataProvider("" , l, ""));
		navTree01.setLoadAllNodes(true);
		navTree01.repaint();
	}
	
	@Bind
	public UIIFrame dtlIFrame;
	
	@Action
	public void navTree01_onclick() {
		UITreeNode node = navTree01.getEventNode();
		if (node != null && node.getUserData() != null) {
			TreeNode selectedNode = (TreeNode) node.getUserData();
			String id = selectedNode.getId();
			dtlIFrame.setSrc("/scp/pages/sysmgr/sop/busstatusedit.aspx?src=busstatusedit&id="+id);
			update.markAttributeUpdate(dtlIFrame, "src");
			update.markUpdate(true, UpdateLevel.Data, dtlIFrame);
			
//			Browser.execClientScript("authorWindowJsVar.hide()");
//			Browser.execClientScript("editDeptWindowJsVar.hide()");
		}
	}
	
	
	@SaveState
	public SysDepartment dept;
	
	
	@Bind
	public String deptPkVal;
	
	@Action
	public void addDept(){
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		dtlIFrame.setSrc("/scp/pages/sysmgr/sop/busstatusedit.aspx?pid="+ ("2".equals(treeNode.getLevel())?treeNode.getPid():treeNode.getId()));
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlIFrame);
	}
	
	
	
	@Action
	public void delDept(){
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		try {
			daoIbatisTemplate.updateWithUserDefineSql("UPDATE sop_bustate SET isdelete = true WHERE NOT EXISTS (SELECT 1 FROM sop_bustate WHERE pid = "+id+") AND id = " + id);
			refresh();
		} catch (Exception e) {
			this.alert("删除失败!");
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIIFrame authorIFrame;
	
	@Action
	public void author(){
		Browser.execClientScript("authorWindowJsVar.hide()");
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		String type = "dept";
		if("0".equals(treeNode.getLevel())){//公司
			type = "org";
		}else{//部门
			type = "dept";
		}
		authorIFrame.setSrc("/scp/pages/sysmgr/role/modrole.aspx?src=orgframework&type="+type+"&refid="+id);
		update.markAttributeUpdate(authorIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, authorIFrame);
		
		Browser.execClientScript("authorWindowJsVar.show()");
	}
	
	@Action
	public void linkSales(){
		Browser.execClientScript("authorWindowJsVar.hide()");
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		String type = "dept";
		if("0".equals(treeNode.getLevel())){//公司
			type = "org";
		}else{//部门
			type = "dept";
		}
		authorIFrame.setSrc("/scp/pages/sysmgr/user/rolelinksales.aspx?src=orgframework&type="+type+"&refid="+id);
		update.markAttributeUpdate(authorIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, authorIFrame);
		
		Browser.execClientScript("authorWindowJsVar.show()");
	}
	

	@Action
	public void saveDept() {
		this.serviceContext.sysDeptService.sysDepartmentDao.createOrModify(dept);
		Browser.execClientScript("editDeptWindowJsVar.hide()");
		this.alert("OK");
		this.refresh();
		
	}



	
}

