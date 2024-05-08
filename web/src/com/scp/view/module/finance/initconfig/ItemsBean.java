package com.scp.view.module.finance.initconfig;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.fs.FsAst;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.ItemsBean", scope = ManagedBeanScope.REQUEST)
public class ItemsBean extends GridFormView {

	
	@SaveState
	@Accessible
	public FsAst selectedRowData = new FsAst();
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	@SaveState
	@Accessible
	public String astype;
	
//	public void init() {
//		actsetcode=getActsetcode();
//		update.markUpdate(true, UpdateLevel.Data, "actsetcode");
//		astype="B";
//		update.markUpdate(true, UpdateLevel.Data, "astype");
//		showAstChoosen(astype);
//	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			actsetcode=AppUtils.getUserSession().getActsetcode();
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			astype="B";
			update.markUpdate(true, UpdateLevel.Data, "astype");
			showAstChoosen(astype);
		}
		
	};
	
	@Override
	public void add() {
		
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	public String getActsetcode(){
		Long actsetid = AppUtils.getUserSession().getActsetid();
		if(actsetid ==null){
			return "";
		}else{
			String sql="SELECT name FROM fs_actset WHERE id ="+actsetid+" AND isdelete = false ";
			Map m = serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			return (String)StrUtils.getMapVal(m, "name");
			
		}
		
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		m.put("qry", qry);
		return m;
	}
	
	@Action
	public void showAstdtl(){
		String code=AppUtils.getReqParam("asttypecode");
		showAstChoosen(code);
	}
	
	
	@Bind
	private UIIFrame dtlIFrame;

	
	public void showAstChoosen(String code) {
		if(code.equals("B")){
			dtlIFrame.setSrc("astdtl.xhtml?astypecode= "+code);
		}else if(code.equals("D")){
			dtlIFrame.setSrc("astdtld.xhtml?astypecode= "+code);
		}else if(code.equals("F")){
			dtlIFrame.setSrc("astdtlf.xhtml?astypecode= "+code);
		}else{
			dtlIFrame.setSrc("astdtlb.xhtml?astypecode= "+code);
		}
		update.markAttributeUpdate(dtlIFrame, "src");
	}

	

	@Action(id = "dtlDialog",event="onclose")
	private void dtlEditDialogClose() {
		String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.html";
		dtlIFrame.setSrc(blankUrl);
		update.markAttributeUpdate(dtlIFrame, "src");
		
	}

	
}