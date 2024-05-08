package com.scp.login_new;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;

import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@ManagedBean(name="login_new.loginlinkBean", scope=ManagedBeanScope.REQUEST)
public class LoginLinkBean {
	
	
	@ManagedProperty("#{serviceContext}")
	public ServiceContext serviceContext;
	
	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if(!isPostback) {
			try {
				//String userid = AppUtils.getReqParam("userid");
				String remoteAddr = AppUtils.getHttpServletRequest().getRequestURL().toString();
				
				
				String uid = AppUtils.getReqParam("uid");
				if(StrUtils.isNull(uid)){
					MessageUtils.alert("invalid user!");
					return;
				}
				l.m.initLocal(MLType.ch);

				String language = AppUtils.getReqParam("l");
				if(language!=null && "en".equals(language)){
					l.m.setMlType(MLType.en);
				}else{
					l.m.setMlType(MLType.ch);
				}
				
				serviceContext.loginService.loginViewByDzzUid(uid);
				AppUtils.getUserSession().setMlType(l.m.getMlType());
				
//				//System.out.println(remoteAddr);
				String addr = remoteAddr.substring(0, remoteAddr.lastIndexOf(":"));
//				//System.out.println(addr);
//				if(remoteAddr.indexOf("com")>0){
//					AppUtils.getUserSession().setDzzUrl("http://gsitsystem.com/gsit/");
//					AppUtils.getUserSession().setScpUrl("http://gsitsystem.com:81/scp");
//				}else{
//					AppUtils.getUserSession().setDzzUrl("http://120.77.83.0/gsit/");
//					AppUtils.getUserSession().setScpUrl("http://120.77.83.0:81/scp");
//				}
				
				AppUtils.getUserSession().setDzzUrl(addr+"/gsit/");
				AppUtils.getUserSession().setScpUrl(addr+":81/scp");
				
				
//				//System.out.println(AppUtils.getDzzUrl());
//				//System.out.println(AppUtils.getScpUrl());
				
				if(!StrUtils.isNull(ConfigUtils.findUserCfgVal("corpidCurrent", AppUtils.getUserSession().getUserid()))){
					try {
						AppUtils.getUserSession().setCorpidCurrent(Long.parseLong(ConfigUtils.findUserCfgVal("corpidCurrent", AppUtils.getUserSession().getUserid())));
					} catch (NumberFormatException e) {
						AppUtils.getUserSession().setCorpidCurrent(AppUtils.getUserSession().getCorpid());
					} catch (Exception e) {
						MessageUtils.showException(e);
					}
				}
				
				initDzz(uid);
				//initToDo(uid);
				
				//FacesContext ctxt = FacesContext.getCurrentInstance();
				//SkinManager.getInstance(ctxt).setDefaultSkin("gray");
				
				//String url = AppUtil.getHttpServletRequest().getHeader("referer");
				StringBuffer url = AppUtils.getHttpServletRequest().getRequestURL();
				AppUtils.getHttpServletResponse().sendRedirect(AppUtils.getDzzUrl());
				
			} catch (Exception e) {
				MessageUtils.showException(e);
//				e.printStackTrace();
			}
		}
	}

	private void initDzz(String uid) throws SQLException{
		//http://127.0.0.1:8888/scp/login_new/loginlink.xhtml?userid=1&uid=2
		//http://127.0.0.1:8888/scp/pages/sysmgr/init/impinit.faces
		//http://122.10.95.18:81/scp/login_new/loginlink.xhtml?userid=1&uid=2
		
		String querySql =
			"\nWITH RECURSIVE cte AS"+
			"\n("+
					"\nSELECT "+
					"\n	DISTINCT m.*"+
					"\nFROM sys_module m"+
					"\nWHERE 1=1"+
					"\n	AND m.isctrl = 'N'"+
					"\n	AND m.pid = 0"+
					"\n	AND m.isdelete = false"+  
			"\nUNION ALL "+
			"\n		SELECT "+
			"\n			DISTINCT m.*"+
			"\n		FROM sys_module m JOIN cte ON(m.pid = cte.id)"+
			"\n		WHERE m.isctrl = 'N'"+
			"\n			AND m.isdelete = false"+  
			"\n)";
		
		String querySql2 =	"\nSELECT "+
			"\n	* "+
			"\n,(SELECT x.name FROM sys_module x where x.id = cte.pid) pname"+ 
			"\nFROM cte"+
			"\nWHERE " +
			"\n (CASE WHEN 'admin'='"+AppUtils.getUserSession().getUsercode() + "' THEN id =  900000 WHEN 'demo'='"+AppUtils.getUserSession().getUsercode() + "' THEN 1=1 ELSE " + 
			"\n EXISTS(SELECT 1 FROM sys_modinrole i, sys_role r, sys_userinrole o" +
			"\n	WHERE cte.id = i.moduleid"+
			"\n	AND i.roleid = r.id"+
			"\n	AND r.id = o.roleid"+
			"\n	AND cte.isctrl = 'N'"+
			"\n	AND o.userid = "+ AppUtils.getUserSession().getUserid()+
			"\n	AND cte.isdelete = false) END)"+
			"\n	ORDER BY pid, modorder DESC;";
		
		if(!AppUtils.isDebug && check(querySql , uid)){
			////AppUtils.debug("模块核对一致，无需重新插入~");
			String fix = "SELECT f_desktop_folder_fix("+uid+");";
//			serviceContext.dzzService.executeQuery(fix);
			return;
		}
		////AppUtils.debug("模块核对不一致，重新插入~");
		
		
		String clearSql1 = "\nDELETE FROM yos_folder where modid IS NOT NULL AND modid > 0 AND uid = "+uid+";";
		String clearSql2 = "\nDELETE FROM yos_icos where modid IS NOT NULL AND modid > 0 AND uid = "+uid+";";
		String clearSql3 = "\nDELETE FROM yos_source_link where modid IS NOT NULL AND modid > 0 AND uid = "+uid+";";

		Vector<String> sqlBatch = new Vector<String>();
		
		
		List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql + querySql2);
		String sql = "";
		
		sqlBatch.add(clearSql1);
		sqlBatch.add(clearSql2);
		sqlBatch.add(clearSql3);
		
		String treeView = "";//树形导航
		String helpView = "";//帮助文档
		
		for (Map map : list) {
			String isleaf  = StrUtils.getMapVal(map, "isleaf");
			if("Y".equals(isleaf)){
				//f_desktop_add_ico(_arg_uid INT , _tx_folderName TEXT , _tx_url TEXT , _tx_title TEXT)
				//SELECT f_desktop_add_ico(2,'桌面','http://122.10.95.18:81/scp/login_new/loginlink.xhtml?userid=81433500','ELS Login');
				String _tx_folderName = l.m.get(StrUtils.getMapVal(map, "pname")).toString();
				if(StrUtils.isNull(_tx_folderName))_tx_folderName = "桌面";
				String _tx_url = AppUtils.getScpUrl() + StrUtils.getMapVal(map, "url");
				String _tx_title = l.m.get(StrUtils.getMapVal(map, "name")).toString();
				String _tx_icon = StrUtils.getMapVal(map, "ico");
				//_tx_icon = "dzz/images/default/shipping.jpg";
				_tx_icon = "dzz/images/" + _tx_icon.substring(2, _tx_icon.length());
				String i8_modid = StrUtils.getMapVal(map, "id");
				String i8_modpid = StrUtils.getMapVal(map, "pid");
				sql = "\nSELECT f_desktop_add_ico("+uid+",'"+_tx_folderName+"','"+_tx_url+"','"+_tx_title+"','"+_tx_icon+"',"+i8_modid+","+i8_modpid+") AS t;";
				
				if(StrUtils.isNull(treeView)){
					_tx_folderName = "桌面";
					_tx_url = AppUtils.getScpUrl()+"/main/index.aspx";
					_tx_title = l.m.get("导航").toString();
					_tx_icon = "dzz/images/image/ie.png";
					i8_modid = "99999";
					i8_modpid = "99999";
					treeView = "\nSELECT f_desktop_add_ico("+uid+",'"+_tx_folderName+"','"+_tx_url+"','"+_tx_title+"','"+_tx_icon+"',"+i8_modid+","+i8_modpid+") AS t;";
					sqlBatch.add(treeView);
				}
				
				if(StrUtils.isNull(helpView)){
					_tx_folderName = "桌面";
					_tx_url = AppUtils.getScpUrl()+"/help/index.html";
					_tx_title = l.m.get("帮助文档").toString();
					_tx_icon = "dzz/images/image/ie.png";
					i8_modid = "99998";
					i8_modpid = "99998";
					helpView = "\nSELECT f_desktop_add_ico("+uid+",'"+_tx_folderName+"','"+_tx_url+"','"+_tx_title+"','"+_tx_icon+"',"+i8_modid+","+i8_modpid+") AS t;";
					sqlBatch.add(helpView);
				}
				
				sqlBatch.add(sql);
			}else{
				String _tx_pname = l.m.get(StrUtils.getMapVal(map, "pname")).toString();
				if(StrUtils.isNull(_tx_pname))_tx_pname = "桌面";
				String _tx_folderName = l.m.get(StrUtils.getMapVal(map, "name")).toString();
				String i8_modid = StrUtils.getMapVal(map, "id");
				String i8_modpid = StrUtils.getMapVal(map, "pid");
				sql = "\nSELECT f_desktop_add_folder("+uid+",'"+_tx_pname+"','"+_tx_folderName+"',"+i8_modid+","+i8_modpid+") As t;";
				sqlBatch.add(sql);
			}
		}
		//System.out.println(sqlBatch);
//		serviceContext.dzzService.executeQueryBatchByJdbc(sqlBatch);
		//serviceContext.daoIbatisTemplateMySql.queryWithUserDefineSql("CALL PROCEDURE_split("+stringBuffer.toString()+",';');CALL add_test()");
	}
	//初始化工作流桌面提醒文件夹
	private void initToDo(String uid) throws SQLException{
		String mainFolderName = "我的待办";
		String pname = "桌面";
		getFolderUid(uid,pname, mainFolderName);
		StringBuffer toDoType = new StringBuffer();
		toDoType.append("\n SELECT DISTINCT * FROM (");
		toDoType.append("\n SELECT ");
		toDoType.append("\n 	t.processname");
		toDoType.append("\n FROM _wf_jobs_fcl t");
		toDoType.append("\n WHERE (t.workitemState = 0 OR t.workitemState = 1)");
		toDoType.append("\n AND t.processId = 'ReleaseBillProcess'");
		toDoType.append("\n AND actor = '"+AppUtils.getUserSession().getUsercode()+"'");
		toDoType.append("\n ORDER BY t.processname,t.nos,t.workitemcreatedtime ) AS T");

		List<Map> list = null;
		List<String> toDoTypes = new ArrayList<String>();
		try {
			list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(toDoType.toString());
			if(list!=null && list.size() >0){
				//创建工作流种类文件夹
				for (Map m : list) {
					if(m!=null && m.containsKey("processname")){
						toDoTypes.add(m.get("processname").toString());
						getFolderUid(uid, mainFolderName, m.get("processname").toString());
					}
				}
			}
		} catch (NoRowException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(toDoTypes.size()>0){
			Map<String,String> iconMap = new HashMap<String, String>();
			StringBuffer iconSql = new StringBuffer();
			iconSql.append("\n SELECT ");
			iconSql.append("\n t.nos||'-'||t.taskdisplayname||'('||t.workitemstatedesc||')' AS iconname,t.processname");
			iconSql.append("\n FROM _wf_jobs_fcl t");
			iconSql.append("\n WHERE (t.workitemState = 0 OR t.workitemState = 1)");
			iconSql.append("\n AND t.processId = 'ReleaseBillProcess'");
			iconSql.append("\n AND actor = '"+AppUtils.getUserSession().getUsercode()+"'");
			iconSql.append("\n ORDER BY t.processname,t.nos,t.workitemcreatedtime");
			List<Map> iconlist = null;
			try {
				iconlist = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(iconSql.toString());
			} catch (NoRowException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(iconlist != null && iconlist.size() > 0){
				for (Map m : iconlist) {
					iconMap.put(m.get("iconname").toString(), m.get("processname").toString());
				}
			}
			Set<String> iconkeys = iconMap.keySet();
			for (String key : iconkeys) {
				creatingIconUid(uid, iconMap.get(key).toString(), AppUtils.getScpUrl(), key, "dzz/images/image/2-121003152928-50.png");
			}
		}
		
	}
	/**
	 * 创建文件夹方法
	 * @param uid 用户uid
	 * @param pname 父文件夹
	 * @param mainFolderName 要创建的文件夹
	 * @return
	 */
	private String getFolderUid(String uid,String pname,String mainFolderName){
		Map map = null;
		String checkMainFolder = "SELECT fid FROM yos_folder a WHERE a.isdelete = 0 AND a.uid = "+uid+" AND a.fname = '"+mainFolderName+"' AND EXISTS (SELECT * FROM yos_folder x WHERE x.isdelete = 0 AND x.uid = "+uid+" AND x.fid = a.pfid AND x.fname='"+pname+"')";
//		try {
//			map = serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(checkMainFolder);
//		} catch (NoRowException e) {
//			//AppUtils.debug("未找到文件夹:"+mainFolderName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if(map == null || !map.containsKey("uid")){
//			//创建我的待办文件夹
//			String createMainFolder = "SELECT f_desktop_add_folder("+uid+",'"+pname+"','"+mainFolderName+"',NULL,NULL)";
//			try {
//				serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(createMainFolder);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			//getFolderUid(uid,pname, mainFolderName);
//			return null;
//		}else{
//			return map.get("uid").toString();
//		}
		return null;
	}
	/**
	 * 创建图标
	 * @param uid dzz用户id
	 * @param folderName 文件夹名称
	 * @param url 图标url
	 * @param title 图标名称
	 * @param iconImageUrl 图片Url
	 */
	private void creatingIconUid(String uid,String folderName,String url,String title,String iconImageUrl){
//		String createsql = "SELECT f_desktop_add_ico("+uid+",'"+folderName+"','"+url+"','"+title+"','"+iconImageUrl+"',NULL,NULL) as a;";//指定列名不然会报错
//		try {
//			Map map = serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(createsql);
//		} catch (NoRowException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	private boolean check(String querySql, String uid) {
		String querySql2 =	
		"\nSELECT "+
		"\n	sum(id) - 99999 - 99998 AS ids"+ 
		"\nFROM cte"+
		"\nWHERE " +
		"\n (CASE WHEN 'admin'='"+AppUtils.getUserSession().getUsercode() + "' THEN id =  900000 WHEN 'demo'='"+AppUtils.getUserSession().getUsercode() + "' THEN 1=1 ELSE " +
		"\n EXISTS(SELECT 1 FROM sys_modinrole i, sys_role r, sys_userinrole o" +
		"\n	WHERE cte.id = i.moduleid"+
		"\n	AND i.roleid = r.id"+
		"\n	AND r.id = o.roleid"+
		"\n	AND cte.isctrl = 'N'"+
		"\n	AND o.userid = "+ AppUtils.getUserSession().getUserid()+
		"\n	AND cte.isdelete = false) END);";
		
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql + querySql2);
		String ids = StrUtils.getMapVal(m, "ids");
		
		////AppUtils.debug("pgsql:idsum:"+ids);
		
		String querySqlCheckMysql = "SELECT SUM(modid) AS ids FROM yos_icos where modid is NOT null and uid = "+uid;
//		m = serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(querySqlCheckMysql);
//		String ids2 = StrUtils.getMapVal(m, "ids");
		//return true;
		
		////AppUtils.debug("mysql:idsum:"+ids2);
		
//		if(ids.equals(ids2)){
			return true;
//		}else{
//			return false;
//		}
	}
}

