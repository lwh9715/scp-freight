package com.scp.view.sysmgr.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.component.tree.base.TreeDataProviderAdapter;

import com.scp.base.MultiLanguageBean;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.base.TreeNode;

public class TreeDataProviderAll  extends TreeDataProviderAdapter {
	
	private List<TreeNode> treeNodesLevel1;
	private Map<Long , TreeNode> treeNodes;
	private DaoIbatisTemplate daoIbatisTemplate;
	
	private MultiLanguageBean l;
	
	public TreeDataProviderAll(String userid , MultiLanguageBean l) {
		try {
			this.l = l ;
			treeNodesLevel1 = new ArrayList<TreeNode>();
			treeNodes = new HashMap<Long, TreeNode>();
			
			daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			
			initTreeLevel1(0l , userid);
			String querySql = "SELECT id FROM sys_module where isctrl = 'N' and isdelete = false and pid = 0 ORDER BY modorder::TEXT";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			for (Map map : list) {
				String id = StrUtils.getMapVal(map, "id");
				initTree(Long.valueOf(id) , userid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initTreeLevel1(Long pid , String userid) throws Exception {
		String querySql = "pages.sysmgr.role.modroleallBean.level1";
		Map args = new HashMap();
		args.put("pid", pid);
		args.put("userid", userid);
		
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
		if(list == null || list.size() < 0) return;
		for (Map map : list) {
			if(map == null || map.size() < 1) return;
			
			Long parentid = Long.valueOf(StrUtils.getMapVal(map, "pid"));
			
			String nodename = StrUtils.getMapVal(map, "name");
			nodename = l.find(nodename);
			
			TreeNode treeNode = new TreeNode(nodename, StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "icon"), StrUtils.getMapVal(map, "check"));
			treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
			treeNodesLevel1.add(treeNode);
		}
	}
	

	private void initTree(Long pid, String userid) throws Exception {
		String querySql = "pages.sysmgr.role.modroleallBean.levelother";
		
		Map args = new HashMap();
		args.put("pid", pid);
		args.put("userid", userid);
		
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
		
		for (Map map : list) {
			Long parentid = Long.valueOf(StrUtils.getMapVal(map, "pid"));
			
			String nodename = StrUtils.getMapVal(map, "name");
			nodename = l.find(nodename);
			
			TreeNode treeNode = new TreeNode(nodename, StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "icon"), StrUtils.getMapVal(map, "check"));
			treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
			treeNode.setPid(parentid);
			treeNodes.get(parentid).addChildrens(treeNode);
			
			//递归，查子节点
			String haschild = StrUtils.getMapVal(map, "haschild");
			if("true".equals(haschild)){
				Long nodeid = Long.valueOf(StrUtils.getMapVal(map, "id"));
				initTree(nodeid , userid);
			}
		}
//		if(list != null && list.size()>0){
//			for (Map map : list) {
//				Long nodeid = Long.valueOf(StrUtils.getMapVal(map, "id"));
//				initTree(nodeid , userid);
//			}
//		}
	}



	public Object[] getChildren(Object node) {
        TreeNode treeNode = (TreeNode)node;
        if (treeNode == null){
            return new Object[] { new TreeNode("模块","0","N","","N") };
        }
        Long id = Long.valueOf(treeNode.getId());
        if(id==0){
        	return treeNodesLevel1.toArray();
        }else{
        	return treeNode.getChildrens().toArray();
        }
    }

    public Boolean isChecked(Object node) {
    	TreeNode treeNode = (TreeNode)node;
    	String check = treeNode.getCheck();
        return ("Y".equals(check)?true:false);
    }

    public boolean isCascade(Object node) {
        return true;
    }

    public String getIcon(Object node) {
        return null;
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
