package com.scp.view.module.finance.initconfig;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.finance.fs.FsAst;
import com.scp.model.finance.fs.FsAstref;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.initconfig.fsastuserchooseBean", scope = ManagedBeanScope.REQUEST)
public class FsAstUserChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public FsAst  fsast =new FsAst();
	
	
	@SaveState
	@Accessible
	public FsAstref  fsAstref =new FsAstref();
	
	
	@SaveState
	@Accessible
	public Long astypeid;
	
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("astypeid");
			astypeid=Long.valueOf(code);
		}
	}

	
	@Action
	public void importCustom(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
	   for(int i=0;i<ids.length;i++){
		   fsast=new FsAst();
		 Long  userid=Long.valueOf(ids[i]);
		 fsast.setAstypeid(astypeid);
		 fsast.setActsetid(AppUtils.getUserSession().getActsetid());
		 fsast.setCorpid(AppUtils.getUserSession().getCorpid());
		 fsast.setCode(getUser(userid)[0]);
		 fsast.setName(getUser(userid)[1]);
//		 fsast.setParentid(findAstByuserId(getUser(userid)[2]));
		 serviceContext.fsAstMgrService.saveData(fsast);
		 fsAstref =new FsAstref();
		 fsAstref.setActsetid(AppUtils.getUserSession().getActsetid());
		 fsAstref.setCorpid(AppUtils.getUserSession().getCorpid());
		 fsAstref.setAstid(fsast.getId());
		 fsAstref.setRefid(userid);
		 fsAstref.setAstypeid(astypeid);
		 serviceContext.fsAstrefMgrService.saveData(fsAstref);
	   }
	     MessageUtils.alert("OK");
	     refresh();
		
	}
	 
	@Override
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "\nAND  not exists (" ;
		filter +="SELECT 1 from fs_ast g,fs_astref f where f.astid = g.id AND  g.actsetid ="+AppUtils.getUserSession().getActsetid()+" AND g.isdelete =false AND f.refid = aa.id AND g.astypeid =" +astypeid+")";
		m.put("filter", filter);
		return m;
	}
	
	public String[] getUser(Long id){
		String sql="SELECT code ,namec,parentid FROM sys_user WHERE id ="+id+" AND isdelete = false";
		Map m = serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String[] cs=new String[3];
		cs[0]=StrUtils.getMapVal(m, "code");
		cs[1]=StrUtils.getMapVal(m, "namec");
		cs[2]=StrUtils.getMapVal(m, "parentid");
		return cs;
	}
	
	public Long findAstByuserId(String userid){
		String sql="SELECT g.id from fs_ast g,fs_astref f where f.astid = g.id AND  g.actsetid ="+AppUtils.getUserSession().getActsetid()+" AND g.isdelete =false AND f.refid = "+userid+" AND g.astypeid ="+astypeid;
		try{
			Map m = serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.get("id")!=null){
				return Long.parseLong(m.get("id").toString());
			}
		}catch(Exception e){
			return null;
		}
		return null;
	}

}
