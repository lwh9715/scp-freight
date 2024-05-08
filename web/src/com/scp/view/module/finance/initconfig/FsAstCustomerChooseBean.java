package com.scp.view.module.finance.initconfig;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.finance.fs.FsAst;
import com.scp.model.finance.fs.FsAstref;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.initconfig.fsastcustomerchooseBean", scope = ManagedBeanScope.REQUEST)
public class FsAstCustomerChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public FsAst  fsast =new FsAst();
	
	
	@SaveState
	@Accessible
	public FsAstref  fsAstref =new FsAstref();
	
	
	@SaveState
	@Accessible
	public Long astypeid;
	
	@Bind
	@SaveState
	@Accessible
	public Long arid;
	
	
	@Bind
	@SaveState
	@Accessible
	public Long apid;
	
	
	
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
		if(arid==null ||apid == null){
			MessageUtils.alert("请选择应收和应付科目");
			return;	
		}
	   for(int i=0;i<ids.length;i++){
		 fsast=new FsAst();
		 Long  customid = new Long(ids[i]);
		 fsast.setRptype("0");
		 fsast.setActidAr(arid); 
		 fsast.setActidAp(apid);
		 fsast.setAstypeid(astypeid);
		 fsast.setActsetid(AppUtils.getUserSession().getActsetid());
		 fsast.setCorpid(AppUtils.getUserSession().getCorpid());
		 fsast.setCode(getCustomer(customid)[0]);
		 fsast.setName(getCustomer(customid)[1]);
		 serviceContext.fsAstMgrService.saveData(fsast);
		 fsAstref =new FsAstref();
		 fsAstref.setActsetid(AppUtils.getUserSession().getActsetid());
		 fsAstref.setCorpid(AppUtils.getUserSession().getCorpid());
		 fsAstref.setAstid(fsast.getId());
		 fsAstref.setRefid(customid);
		 fsAstref.setAstypeid(astypeid);
		 serviceContext.fsAstrefMgrService.saveData(fsAstref);
	   }
	     MessageUtils.alert("OK");
	     refresh();
		
	}
	 
	@Override
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND  not exists (" ;
		qry +="SELECT 1 from fs_ast g,fs_astref f where f.astid = g.id AND  g.actsetid ="+AppUtils.getUserSession().getActsetid()+" AND g.isdelete =false AND f.refid = a.id AND g.astypeid =" +astypeid+")";
		m.put("qry", qry);
		return m;
	}
	
	public String[] getCustomer(Long id){
		String sql="SELECT code ,namec FROM sys_corporation WHERE id ="+id+" AND isdelete = false";
		Map m = serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String[] cs=new String[2];
		cs[0]=StrUtils.getMapVal(m, "code");
		cs[1]=StrUtils.getMapVal(m, "namec");
		return cs;
		
	}
}
