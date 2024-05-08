package com.scp.view.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.operamasks.faces.component.tree.base.TreeDataProviderAdapter;

import com.scp.base.MultiLanguageBean;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoSessionException;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

public class TreeDataProvider  extends TreeDataProviderAdapter{
	
	private List<TreeNode> treeNodesLevel1;
	private Map<Long , TreeNode> treeNodes;
	
	private MultiLanguageBean l;
	
	private List<TreeNode> leafNodes;
	private DaoIbatisTemplate daoIbatisTemplate;
	
	public TreeDataProvider() {
		super();
		treeNodesLevel1 = new ArrayList<TreeNode>();
		treeNodes = new HashMap<Long, TreeNode>();
		
		leafNodes = new ArrayList<TreeNode>();
		daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
	}
	
	public void initTreeAll(MultiLanguageBean l){
		String sysctrl = ConfigUtils.findSysCfgVal("system_ctrl_only");
		String querySql = "SELECT id FROM sys_module where isctrl = 'N' and isdelete = false and pid = 0 ORDER BY modorder::TEXT";
		if("Y".equals(sysctrl)){ //新客户中默认设置：system 只有 系统管理模块 , 其他账号无系统管理模块
			if("system".equals(AppUtils.getUserSession().getUsercode())){
				querySql = "SELECT id FROM sys_module where isctrl = 'N' and isdelete = false and pid = 0 AND id = 600000 ORDER BY modorder::TEXT";
			}else{
				querySql = "SELECT id FROM sys_module where isctrl = 'N' and isdelete = false and pid = 0 AND id <> 600000 ORDER BY modorder::TEXT";
			}
		}
		if(AppUtils.getUserSession().getUserid()==81433600L){ //demo账号特权，所有都有
			querySql = "SELECT id FROM sys_module where isctrl = 'N' and isdelete = false and pid = 0 ORDER BY modorder::TEXT";
		}
		
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		for (Map map : list) {
			String id = StrUtils.getMapVal(map, "id");
			initTree(Long.valueOf(id), l);
		}
	}
	

	public void initTree(Long pid , MultiLanguageBean l) {
		try {
			this.l = l ;
			initTreeLevel1(pid);
			initTree(pid);
//			ApplicationUtils.debug(treeNodes);
		} catch (NoSessionException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initTreeLevel1(Long pid) throws Exception {
		String querySql = "";
		Map args = new HashMap();
		String sysctrl = ConfigUtils.findSysCfgVal("system_ctrl_only");
		if(AppUtils.getUserSession().isAdmin()){			
			querySql = "exp.tree.qry.admin.level1";
			args.put("pid", pid);
		}else{
			querySql = "exp.tree.qry.level1";
			args.put("pid", pid);
			args.put("userid", AppUtils.getUserSession().getUserid());
			if("Y".equals(sysctrl)){
				args.put("sysctrl", "(CASE WHEN ("+AppUtils.getUserSession().getUserid()+"=81433600 OR '"+AppUtils.getUserSession().getUsercode()+"'='system') THEN 1=1 ELSE m.id != 600000 END)");
			}else{
				args.put("sysctrl", "1=1");
			}
		}
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
		
		//Map map = AppDaoUtil.qry4OneRow(querySql);
		if(list == null || list.size() <= 0) return;
		Map map = list.get(0);
		if(map == null || map.size() < 1) return;
		
		Long parentid = Long.valueOf(StrUtils.getMapVal(map, "pid"));
		
		String nodename = StrUtils.getMapVal(map, "name");
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	    Matcher m = p.matcher(nodename);
		nodename = m.find()?l.find(nodename):nodename;//排除中文
//		ApplicationUtils.debug(nodename);

		TreeNode treeNode = new TreeNode(nodename, StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "ico"), StrUtils.getMapVal(map, "check"));
		treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
		treeNodesLevel1.add(treeNode);
		
	}
	

	private void initTree(Long pid) throws Exception {
		String querySql = "";
		Map args = new HashMap();
		String sysctrl = ConfigUtils.findSysCfgVal("system_ctrl_only");
		if(AppUtils.getUserSession().isAdmin()){			
			querySql = "exp.tree.qry.admin.levelother";
			args.put("pid", pid);
		}else{
			querySql = "exp.tree.qry.levelother";
			args.put("pid", pid);
			args.put("userid", AppUtils.getUserSession().getUserid());
			if("Y".equals(sysctrl)){
				args.put("sysctrl", "(CASE WHEN ("+AppUtils.getUserSession().getUserid()+"=81433600 OR '"+AppUtils.getUserSession().getUsercode()+"'='system') THEN 1=1 ELSE m.id != 600000 END)");
			}else{
				args.put("sysctrl", "1=1");
			}
		}
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
		
		for (Map map : list) {
			Long parentid = Long.valueOf(StrUtils.getMapVal(map, "pid"));
			
			String nodename = StrUtils.getMapVal(map, "name");
			Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		    Matcher m = p.matcher(nodename);
			nodename =  m.find()?l.find(nodename):nodename;//排除中文
			////AppUtils.debug(nodename);
			TreeNode treeNode = new TreeNode(nodename , StrUtils.getMapVal(map, "id"), StrUtils.getMapVal(map, "url"), StrUtils.getMapVal(map, "ico"), StrUtils.getMapVal(map, "check"));
			treeNodes.put(Long.valueOf(StrUtils.getMapVal(map, "id")), treeNode);
			treeNode.setPid(parentid);
			treeNodes.get(parentid).addChildrens(treeNode);
			
			String isleaf = StrUtils.getMapVal(map, "isleaf");
			if("Y".equals(isleaf)){
				leafNodes.add(treeNode);
			}
			//递归，查子节点
			String haschild = StrUtils.getMapVal(map, "haschild");
			if("true".equals(haschild)){
				Long nodeid = Long.valueOf(StrUtils.getMapVal(map, "id"));
				initTree(nodeid);
			}
		}
//		if(list != null && list.size()>0){
//			for (Map map : list) {
//				
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

//    public Boolean isChecked(Object node) {
//    	TreeNode treeNode = (TreeNode)node;
//    	String check = treeNode.getCheck();
//        return ("Y".equals(check)?true:false);
//    }

    public boolean isCascade(Object node) {
        return true;
    }

    public String getIcon(Object node) {
    	TreeNode treeNode = (TreeNode)node;
        if (treeNode == null){
        	 return "./img/1.png";
        }
        return treeNode.getIcon();
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

	public List<TreeNode> getTreeNodesLevel1() {
		return treeNodesLevel1;
	}

	public Map<Long, TreeNode> getTreeNodes() {
		return treeNodes;
	}

	public List<TreeNode> getLeafNodes() {
		return leafNodes;
	}
	
}
