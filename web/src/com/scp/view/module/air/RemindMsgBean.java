package com.scp.view.module.air;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.knowledge.SysKnowledgelib;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.air.remindMsgBean", scope = ManagedBeanScope.REQUEST)
public class RemindMsgBean extends GridFormView {
	
	@SaveState
	@Accessible
	public SysKnowledgelib selectedRowData = new SysKnowledgelib();
	
	@Bind
	@SaveState
	@Accessible
	public Long linkid;
	
	@SaveState
	@Accessible
	public String tablename = "";
	
	@Bind
	public UIButton add;
	
	@Bind
	public UIButton deleter;
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton save;
	
	@Bind
	@SaveState
	@Accessible
	public Long linkid2;
	
	@SaveState
	@Accessible
	public String tablename2 = "";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			initCtrl();
			String id = AppUtils.getReqParam("linkid").trim();
			String name = AppUtils.getReqParam("tablename").trim();
			String id2 = AppUtils.getReqParam("linkid2").trim();
			String name2 = AppUtils.getReqParam("tablename2").trim();
			if(!StrUtils.isNull(name)){
				tablename= name;
			}
			if(!StrUtils.isNull(name2)){
				tablename2= name2;
			}
			if(!StrUtils.isNull(id) && !StrUtils.isNull(id2)){
				try {
					linkid = Long.valueOf(id);
					qryMap.put("linkid$", this.linkid);
					linkid2 = Long.valueOf(id2);
					qryMap.put("linkid2$", this.linkid2);
				} catch (Exception e) {
					linkid = null;
				}
			}else if(!StrUtils.isNull(id) && StrUtils.isNull(id2)){
				linkid = Long.valueOf(id);
				qryMap.put("linkid$", this.linkid);
				linkid2 = -1L;
			}
			else{
				linkid = -1L;
				linkid2 = -1L;
				//this.add();
			}
		}
	}

	@Override
	public void add() {
 		selectedRowData = new SysKnowledgelib();
		super.add();
		context = "";
		libtype = "";
		abstracts = "";
		context = "";
		linknamec = "";
		//libtype2 = "";
		update.markUpdate(UpdateLevel.Data, "libtype");
		//update.markUpdate(UpdateLevel.Data, "libtype2");
		update.markUpdate(UpdateLevel.Data, "ckeditor");
		Browser.execClientScript("setValue('');");
	}

	@Bind
	@SaveState
	public String context;
	
	

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysKnowledgelibService.sysKnowledgelibDao.findById(this.pkVal);
		
	}
	
	@Action
	public void saveAction(){
		String editor1 = AppUtils.getReqParam("editor1");
		String libtype = "F";
		String abstracts = AppUtils.getReqParam("abstracts");
		String linkids = AppUtils.getReqParam("linkid");
		selectedRowData.setContext(editor1);
		selectedRowData.setLibtype(libtype);
		selectedRowData.setAbstracts(abstracts);
		selectedRowData.setLinktbl(tablename);
		if(libtype.equals("A")){
			selectedRowData.setLinktbl("sys_corporation");
		}else if(libtype.equals("B")){
			selectedRowData.setLinktbl("sys_corporation");
		}else if(libtype.equals("C")){
			selectedRowData.setLinktbl("sys_corporation");
		}else if(libtype.equals("D")){
			selectedRowData.setLinktbl("dat_line");
		}else if(libtype.equals("E")){
			selectedRowData.setLinktbl("dat_port");
		}else if(libtype.equals("F")){
			selectedRowData.setLinktbl("sys_corporation");
		}else if(libtype.equals("G")){
			selectedRowData.setLinktbl("sys_corporation");
		}else if(libtype.equals("H")){
			selectedRowData.setLinktbl("sys_corporation");
		}else if(libtype.equals("I")){
			selectedRowData.setLinktbl("sys_corporation");
		}
		if(!StrUtils.isNull(linkids)){
			linkid = Long.parseLong(linkids);
		}
		if(linkid!=null&&linkid>0){
			selectedRowData.setLinkid(linkid);
		}else{
			if(StrUtils.isNull(linkids)){
				this.alert("请选择关联数据!");
				return;
			}
			selectedRowData.setLinkid(Long.parseLong(linkids));
		}
		
		
		/*String libtype2 = AppUtils.getReqParam("libtype2");
		String linkids2 = AppUtils.getReqParam("linkid2");
		selectedRowData.setLibtype2(libtype2);
		selectedRowData.setLinktbl2(tablename2);
		if(libtype2.equals("A")){
			selectedRowData.setLinktbl2("sys_corporation");
		}else if(libtype2.equals("B")){
			selectedRowData.setLinktbl2("sys_corporation");
		}else if(libtype2.equals("C")){
			selectedRowData.setLinktbl2("sys_corporation");
		}else if(libtype2.equals("D")){
			selectedRowData.setLinktbl2("dat_line");
		}else if(libtype2.equals("E")){
			selectedRowData.setLinktbl2("dat_port");
		}else if(libtype2.equals("F")){
			selectedRowData.setLinktbl2("sys_corporation");
		}else if(libtype2.equals("G")){
			selectedRowData.setLinktbl2("sys_corporation");
		}else if(libtype2.equals("H")){
			selectedRowData.setLinktbl2("sys_corporation");
		}else if(libtype2.equals("I")){
			selectedRowData.setLinktbl2("sys_corporation");
		}
		if(!StrUtils.isNull(linkids2)){
			linkid2 = Long.parseLong(linkids2);
		}
		if(linkid2!=null&&linkid2>0){
			selectedRowData.setLinkid2(linkid2);
		}else{
			if(StrUtils.isNull(linkids2)){
				this.alert("请选择关联数据!");
				return;
			}
			selectedRowData.setLinkid2(Long.parseLong(linkids2));
		}*/
		this.serviceContext.sysKnowledgelibService.saveData(selectedRowData);
		this.alert("OK");
		refresh();
	}
	
	@Bind
	@SaveState
	public String libtype;
	
	@Bind
	@SaveState
	public String libtype2;
	
	@Bind
	@SaveState
	public String abstracts;

	@Bind
	@SaveState
	public String linknamec;
	
	@Bind
	@SaveState
	public String linknamec2;

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		doServiceFindData();
		libtype=selectedRowData.getLibtype();
		if(editWindow != null)editWindow.show();
		context = selectedRowData.getContext();
		abstracts = selectedRowData.getAbstracts();
		linkid = selectedRowData.getLinkid();
		String linktablev = "";
		/*if(libtype.equals("A")){
			linktablev = "sys_corporation";
		}else if(libtype.equals("B")){
			linktablev = "sys_corporation";
		}else if(libtype.equals("C")){
			linktablev = "sys_corporation";
		}else if(libtype.equals("D")){
			linktablev = "dat_line";
		}else if(libtype.equals("E")){
			linktablev = "dat_port";
		}else */
		if(libtype.equals("F")){
			linktablev = "sys_corporation";
		}/*else if(libtype.equals("G")){
			linktablev = "sys_corporation";
		}else if(libtype.equals("H")){
			linktablev = "sys_corporation";
		}else if(libtype.equals("I")){
			linktablev = "sys_corporation";
		}*/
		try{
			String sql = "SELECT f_common_get_table_value('colum=namec;tabname="+linktablev+";id="+linkid+"') AS tablename";
			 Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
			 if(m!=null&&m.get("tablename")!=null){
				 linknamec = m.get("tablename").toString();
			 }
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		
		/*libtype2=selectedRowData.getLibtype2();
		linkid2 = selectedRowData.getLinkid2();
		String linktablev2 = "";
		if(!StrUtils.isNull(libtype2)){
			if(libtype2.equals("A")){
				linktablev2 = "sys_corporation";
			}else if(libtype2.equals("B")){
				linktablev2 = "sys_corporation";
			}else if(libtype2.equals("C")){
				linktablev2 = "sys_corporation";
			}else if(libtype2.equals("D")){
				linktablev2 = "dat_line";
			}else if(libtype2.equals("E")){
				linktablev2 = "dat_port";
			}else if(libtype2.equals("F")){
				linktablev2 = "sys_corporation";
			}else if(libtype2.equals("G")){
				linktablev2 = "sys_corporation";
			}else if(libtype2.equals("H")){
				linktablev2 = "sys_corporation";
			}else if(libtype2.equals("I")){
				linktablev2 = "sys_corporation";
			}
		}
		if(linkid2 > 0){
			try{
				String sql = "SELECT f_common_get_table_value('colum=namec;tabname="+linktablev2+";id="+linkid2+"') AS tablename2";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(m!=null&&m.get("tablename2")!=null){
					linknamec2 = m.get("tablename2").toString();
				}
			}catch(Exception e){
				e.printStackTrace();
				MessageUtils.showException(e);
			}
			update.markUpdate(UpdateLevel.Data, "libtype2");
			Browser.execClientScript("libtype2Js.setValue('"+libtype2+"');setValue();");
		}else{
			linknamec2="";
		}*/
		update.markUpdate(UpdateLevel.Data, "libtype");
		update.markUpdate(UpdateLevel.Data, "context");
		update.markUpdate(UpdateLevel.Data, "abstracts");
		update.markUpdate(true,UpdateLevel.Data, "editPanel");
		Browser.execClientScript("libtypeJs.setValue('"+libtype+"');setValue();");
	}

	@Override
	protected void doServiceSave() {
//		this.serviceContext.sysKnowledgelibService.saveData(selectedRowData);
		update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			this.serviceContext.sysKnowledgelibService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Override
	public void del() {
		if(selectedRowData.getId()==0){
			this.add();	
		}else{
			this.serviceContext.sysKnowledgelibService.removeDate(selectedRowData.getId());
		refresh();
		this.add();
		this.alert("OK");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		Map m =  super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//2250  如果是委托人就按权限控制显示
		qry += "\nAND(CASE WHEN T.libtype = 'A' THEN"
				//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
				+ "\n ( EXISTS(SELECT 1 FROM sys_corporation WHERE id = T.linkid AND salesid = "+AppUtils.getUserSession().getUserid()+")"//委托人的业务员能看到
				+ "\n	OR (T.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false " //关联的业务员的单，都能看到
				+ "\n 							AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = T.linkid AND z.id = salesid ))"
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false " //组关联业务员的单，都能看到
				+ "\n 							AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = T.linkid AND z.id = salesid ))"
				+ ")"
				+ "\n)"
				+"\n ELSE TRUE END)";
		m.put("qry", qry);
		return m;
	}
	
	
	//新增删除修改按钮权限
	private void initCtrl() {
		add.setDisabled(true);
		save.setDisabled(true);
		deleter.setDisabled(true);
		del.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(187000L)) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
				save.setDisabled(false);
			} else if (s.endsWith("_update")) {
				save.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				deleter.setDisabled(false);
				del.setDisabled(false);
			}
		}
		
	}

}

