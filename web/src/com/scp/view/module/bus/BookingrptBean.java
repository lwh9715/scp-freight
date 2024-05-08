package com.scp.view.module.bus;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.bus.bookingrptBean", scope = ManagedBeanScope.REQUEST)
public class BookingrptBean extends GridView {

	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	public UIButton addMaster;
	
	@Bind
	public UIButton delMaster;
	
	@Bind
	@SaveState
	public String sale;
	
	@Bind
	@SaveState
	public String dept;
	
	@Bind
	@SaveState
	public String corporation;
	
	@Bind
	@SaveState
	public String pol;
	
	@Bind
	@SaveState
	public String pod;
	
	@Bind
	@SaveState
	public String carrier;
	
	@Bind
	@SaveState
	public boolean issubmit;
	
	@Bind
	public UIButton submitMaster;
	
	@Bind
	public UIIFrame bookingrptIframe;


	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String gridid  = this.getMBeanName() + ".grid";
			String gridJsvar = "gridJsvar";
			applyGridUserDef(gridid , gridJsvar);
			initCtrl();
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(issubmit){
			qry += "\nAND t.issubmit = "+issubmit+"";
		}
		if(!StrUtils.isNull(sale)){
			qry += "\nAND t.saleid in (SELECT u.id FROM sys_user u WHERE u.code LIKE'%"+sale+"%' OR u.namec LIKE '%"+sale+"%' OR u.namee LIKE '%"+sale+"%')";
		}
		if(!StrUtils.isNull(dept)){
			qry += "\nAND t.deptid in (SELECT u.id FROM sys_department u WHERE u.code LIKE'%"+dept+"%' OR name LIKE '%"+dept+"%')";
		}
		if(!StrUtils.isNull(pol)){
			qry += "\nAND t.polid in (SELECT u.id FROM dat_port u WHERE u.code LIKE'"+pol+"%' OR namec LIKE '%"+pol+"%' OR namee LIKE '%"+pol+"%')";
		}
		if(!StrUtils.isNull(pod)){
			qry += "\nAND t.podid in (SELECT u.id FROM dat_port u WHERE u.code LIKE'"+pod+"%' OR namec LIKE '%"+pod+"%' OR namee LIKE '%"+pod+"%')";
		}
		if(!StrUtils.isNull(carrier)){
			qry += "\nAND t.carrierid in (SELECT u.id FROM sys_corporation u WHERE u.code LIKE'%"+carrier+"%' OR namec LIKE '%"+carrier+"%' OR namee LIKE '%"+carrier+"%' AND iscarrier = true)";
		}
		m.put("qry", qry);
		
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid " //组关联业务员的单，都能看到
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		
		return m;
	}
	
	
	
	@Action
	public void addMaster() {
		bookingrptIframe.load("./bookingrptedit.xhtml");
		editWindow.show();
	}
	
	@Action
	public void submitMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.busBookingrptService.submitDate(ids,
					AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.busBookingrptService.removeDate(ids,
					AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Override
	@Action
	public void clearQryKey() {
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
			issubmit = false;
			pod = "";
			pol = "";
			carrier = "";
			sale = "";
			dept = "";
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefresh();
		}
	}
	
	private void initCtrl() {
		submitMaster.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(100900900L)) {
			if (s.endsWith("_submitButton")) {
				submitMaster.setDisabled(false);
			} 
		}
	}
	
	@Override
	public void grid_ondblclick() {
		try {
			bookingrptIframe.load("./bookingrptedit.xhtml?id="+this.getGridSelectId());
			editWindow.show();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	

}
