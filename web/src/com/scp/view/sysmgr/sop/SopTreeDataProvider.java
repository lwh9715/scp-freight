package com.scp.view.sysmgr.sop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.component.tree.base.TreeDataProviderAdapter;

import com.scp.base.MultiLanguageBean;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.sysmgr.company.TreeNode;


public class SopTreeDataProvider  extends TreeDataProviderAdapter {
	
	private List<TreeNode> treeNodesLevel1;
	private Map<Long , TreeNode> treeNodes;
	private DaoIbatisTemplate daoIbatisTemplate;
	
	private MultiLanguageBean l;
	
	public SopTreeDataProvider(String roleid , MultiLanguageBean l,String company) {
		try {
			this.l = l ;
			treeNodesLevel1 = new ArrayList<TreeNode>();
			treeNodes = new HashMap<Long, TreeNode>();
			daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			initTreeLevel1(0l , roleid, company);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void initTreeLevel1(Long pid , String roleid, String company) throws Exception {
		String querySql = "SELECT id,pid,namec AS nodename," +
				"(CASE WHEN pid= 0 THEN 0 WHEN pid= ANY(SELECT id FROM sop_bustate WHERE isdelete = FALSE AND pid = 0) THEN 1 ELSE 2 END) as nodelevel ," +
				"'/scp/main/img/organizationcfg.gif' as url,'' AS icon " +
				"FROM sop_bustate WHERE isdelete = FALSE;";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		if(list == null || list.size() < 1) return;
		for (Map map : list) {
			Long parentid = Long.valueOf(StrUtils.getMapVal(map, "pid"));
			String nodename = StrUtils.getMapVal(map, "nodename");
			nodename = l.find(nodename);
			if(parentid == 0){
				TreeNode treeNode = new TreeNode(nodename, StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "icon"), StrUtils.getMapVal(map, "check"), StrUtils.getMapVal(map, "nodelevel"));
				treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
				treeNodesLevel1.add(treeNode);
			}else{
				TreeNode treeNode = new TreeNode(nodename, StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "icon"), StrUtils.getMapVal(map, "check"), StrUtils.getMapVal(map, "nodelevel"));
				treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
				treeNode.setPid(parentid);
				treeNodes.get(parentid).addChildrens(treeNode);
			}
		}
	}
	

	public Object[] getChildren(Object node) {
    	TreeNode treeNode = (TreeNode)node;
        if (treeNode == null){
        	return new Object[] { new TreeNode("模块","0","N","","N","-1") };
        }
        Long id = Long.valueOf(treeNode.getId());
        if(id==0){
        	return treeNodesLevel1.toArray();
        }else{
        	return treeNode.getChildrens().toArray();
        }
    }

    /*public Boolean isChecked(Object node) {
    	TreeNode treeNode = (TreeNode)node;
    	String check = treeNode.getCheck();
        return ("Y".equals(check)?true:false);
    }*/

    public boolean isCascade(Object node) {
        return true;
    }

    public String getText(Object node) {
        return node.toString();
    }

    public String getHref(Object node) {
        return null;
    }

    public String getHrefTarget(Object node) {
        return null;
    }

    public boolean isExpanded(Object arg0) {
        return false;
    }
    @Override
    public String getId(Object userData) {
    	TreeNode treeNode = (TreeNode)userData;
        return "A"+treeNode.getId();
    }

//	@Override
//	public String getCls(Object userData) {
//		// TODO Auto-generated method stub
////		return super.getCls(userData);
//		return "treenodecss";
//	}
    
    
}
