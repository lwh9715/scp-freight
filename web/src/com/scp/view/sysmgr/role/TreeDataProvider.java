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


public class TreeDataProvider  extends TreeDataProviderAdapter {
	
	private List<TreeNode> treeNodesLevel1;
	private Map<Long , TreeNode> treeNodes;
	private DaoIbatisTemplate daoIbatisTemplate;
	
	private MultiLanguageBean l;
	private boolean isSaas = false;
	
	public TreeDataProvider(String roleid , MultiLanguageBean l) {
		try {
			this.isSaas = false;
			this.l = l ;
			treeNodesLevel1 = new ArrayList<TreeNode>();
			treeNodes = new HashMap<Long, TreeNode>();
			
			daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			
			
			initTreeLevel1(0l , roleid);
			String querySql = "SELECT id FROM sys_module where isctrl = 'N' and isdelete = false and pid = 0 ORDER BY modorder::TEXT";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			for (Map map : list) {
				String id = StrUtils.getMapVal(map, "id");
				initTree(Long.valueOf(id) , roleid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author neo
	 * SAAS模式下权限重构
	 */
	public TreeDataProvider(String roleid , MultiLanguageBean l , boolean isSaas) {
		try {
			this.l = l ;
			treeNodesLevel1 = new ArrayList<TreeNode>();
			treeNodes = new HashMap<Long, TreeNode>();
			this.isSaas = isSaas;
			
			daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			
			
			initTreeLevel1(0l , roleid);
			String querySql = 
				"SELECT " +
				"	id " +
				"\nFROM sys_module m " +
				"\nwhere m.isctrl = 'N' " +
				"\n	and isdelete = false " +
				"\n	and pid = 0 " +
				(isSaas?"\n	AND EXISTS(SELECT 1 FROM sys_role x , sys_modinrole y where x.code = 'SAAS' AND x.id = y.roleid AND y.moduleid = m.id)":"") +
				"\nORDER BY modorder::TEXT";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			for (Map map : list) {
				String id = StrUtils.getMapVal(map, "id");
				initTree(Long.valueOf(id) , roleid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initTreeLevel1(Long pid , String roleid) throws Exception {
		String querySql = "pages.sysmgr.role.modroleBean.level1";
		Map args = new HashMap();
		args.put("pid", pid);
		args.put("roleid", roleid);
		if(this.isSaas){
			args.put("filter", "\n AND EXISTS(SELECT 1 FROM sys_role x , sys_modinrole y where x.code = 'SAAS' AND x.id = y.roleid AND y.moduleid = m.id)");
		}
		
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
		if(list == null || list.size() < 1) return;
		for (Map map : list) {
			Long parentid = Long.valueOf(StrUtils.getMapVal(map, "pid"));
			
			String nodename = StrUtils.getMapVal(map, "name");
			nodename = l.find(nodename);
			
			TreeNode treeNode = new TreeNode(nodename, StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "icon"), StrUtils.getMapVal(map, "check"));
			treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
			treeNodesLevel1.add(treeNode);
		}
	}
	

	private void initTree(Long pid, String roleid) throws Exception {
		String querySql = "pages.sysmgr.role.modroleBean.levelother";
		
		Map args = new HashMap();
		args.put("pid", pid);
		args.put("roleid", roleid);
		
		if(this.isSaas){
			args.put("filter", "\n AND EXISTS(SELECT 1 FROM sys_role x , sys_modinrole y where x.code = 'SAAS' AND x.id = y.roleid AND y.moduleid = m.id)");
		}
		
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
				initTree(nodeid , roleid);
			}
		}
//		if(list != null && list.size()>0){
//			for (Map map : list) {
//				Long nodeid = Long.valueOf(StrUtils.getMapVal(map, "id"));
//				initTree(nodeid , roleid);
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
