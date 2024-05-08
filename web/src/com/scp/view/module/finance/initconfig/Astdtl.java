package com.scp.view.module.finance.initconfig;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsAst;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.initconfig.astdtlBean", scope = ManagedBeanScope.REQUEST)
public class Astdtl extends GridFormView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@SaveState
	@Accessible
	public FsAst selectedRowData = new FsAst();
	
	
	@SaveState
	@Accessible
	public String astypecode;
	
	@Bind
	@SaveState
	@Accessible
	public Long arid = 113103L;
	
	
	@Bind
	@SaveState
	@Accessible
	public Long apid = 212103L;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	@SaveState
	public String src;

	@Bind
	@SaveState
	private String is_actcode_ar;

	@Bind
	@SaveState
	private String is_actcode_ap;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			astypecode = AppUtils.getReqParam("astypecode").trim();
			//if from chooser ,hide something... neo 20150112
			src = AppUtils.getReqParam("src").trim();
			if("C".equals(src)) {
				Browser.execClientScript("del.hide();add.hide();qryTable.style.display='none';");
				Browser.execClientScript("");
			}else {
				Browser.execClientScript("chooseConfirm.hide();");
			}
		}
	}
	

	
	
	
	@Override
	public void add() {
		selectedRowData = new FsAst();
		selectedRowData.setRptype("0");
		selectedRowData.setActidAr(1131L); 
		selectedRowData.setActidAp(2121L);
		
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.fsAstMgrService.fsAstDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		selectedRowData.setAstypeid(getAstypeid());
		selectedRowData.setActsetid(AppUtils.getUserSession().getActsetid());
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		
		serviceContext.fsAstMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
		
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			alert("请至少选项一行!");
			return;
		}
		try {
			serviceContext.fsAstMgrService.removeDate(ids , AppUtils.getUserSession().getUsercode());
			this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid()+"AND astypecode='"+astypecode+"'";

		if (is_actcode_ar != null && !StrUtils.isNull(is_actcode_ar.toString())) {
			if ("true".equals(is_actcode_ar)) {
				qry += "\n and actcode_ar is not null";
			} else {
				qry += "\n and actcode_ar is  null";
			}
		}
		if (is_actcode_ap != null && !StrUtils.isNull(is_actcode_ap.toString())) {
			if ("true".equals(is_actcode_ap)) {
				qry += "\n and actcode_ap is not null";
			} else {
				qry += "\n and actcode_ap is  null";
			}
		}
		m.put("qry", qry);
		return m;
	}
	
	@Action
	public void setAr(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			alert("请至少选项一行!");
			return;
		}
		serviceContext.fsAstMgrService.setAr(ids , AppUtils.getUserSession().getActsetid(),arid);
		this.alert("OK");
		refresh();
	}
	
	@Action
	public void setAp(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			alert("请至少选项一行!");
			return;
		}
		serviceContext.fsAstMgrService.setAp(ids , AppUtils.getUserSession().getActsetid(),apid);
		this.alert("OK");
		refresh();
	}
	
	@Action
	public void chooseCs() {
		
		String url = AppUtils.getContextPath() + "/pages/module/finance/initconfig/fsastcustomerchoose.xhtml?astypeid="+getAstypeid();
		//AppUtils.debug(url);
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
	
	public Long getAstypeid(){
		String sql="SELECT id FROM fs_astype WHERE actsetid ="+AppUtils.getUserSession().getActsetid()+" AND isdelete = false AND code ='"+astypecode+"'";
		Map m = serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return Long.valueOf(StrUtils.getMapVal(m, "id"));
		
	}
	
	/**
	 * 选择后，回填到父页面查询form中
	 */
	@Action
	public void chooseConfirm(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length != 1) {
			alert("请选项一行!");
			return;
		}
		Browser.execClientScript("parent.astdesc.setValue("+ids[0]+")");
	}
	
	@Action
	public void customerRefresh(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			alert("Please tick at least the line!");
		}
		String array2List = StrUtils.array2List(ids);
		//System.out.println(array2List);
		serviceContext.fsAstMgrService.customerRefreshs(array2List);
		this.alert("OK");
		refresh();
	}
	
	@Bind
	public UIWindow join2Window;
	
	
	@Action
	public void joinConfirm() {
		String ids[] = this.grid.getSelectedIds();
		try {
			String idto = join2Customerid;
			for (String id : ids) {
				//AppUtils.debug(id);
				String sql = new String();
				String idfm = id;
				if(idfm.equals(idto))continue;
				sql = "\nSELECT f_fs_astdtl_join('idfm="+idfm+";idto="+idto+";actsetid=" + AppUtils.getUserSession().getActsetid() + ";user="+AppUtils.getUserSession().getUsercode()+"');";
				//AppUtils.debug(sql);
				this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
			}
			this.alert("OK!");
			this.refresh();
			join2Window.close();
			ishow = false;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@SaveState
	private boolean ishow;
	
	@Bind
	@SaveState
	private String join2Customerid;
	
	/**
	 * 合并客户
	 */
	@Action
	public void join() {
		String ids[] = this.grid.getSelectedIds();

		if(ids == null || ids.length < 2) {
			this.alert("请至少选择两行!");
			return;
		}
		ishow = true;
		join2Window.show();
		this.update.markUpdate(UpdateLevel.Data, "join2Customerid");
		//ishow = false;
	}
	
	@Bind(id="join2Customer")
    public List<SelectItem> getJoin2Customer() {
		if(!ishow)return null;
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 2) {
			return null;
		}
		String id = StrUtils.array2List(ids);
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.code||'/'||COALESCE(name,'')","fs_ast AS d","WHERE d.id IN ("+id+")","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
}