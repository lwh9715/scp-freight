package com.scp.view.module.finance.clspro;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsVch;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.finance.clspro.endclosingBean", scope = ManagedBeanScope.REQUEST)
public class EndclosingBean extends GridView {
	
	@Bind
	@SaveState
	public UIButton antiCheckoutAction;
	
	@Bind
	@SaveState
	public Boolean antiCheckoutBox = false;
	
	@Bind
	public UIButton openVchno;
	
	@Bind
	public String desc;
	
	@Bind
	public String actid;
	
	@Bind
	@SaveState
	public String year;
	
	@Bind
	@SaveState
	public String vchtype;
	
	@Bind
	@SaveState
	public String vchid;
	
	@Bind
	@SaveState
	public String period;
	
	
	
	@Bind
	@SaveState
	public String vchno = "";
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	public UIWindow showVchEditWindow;
	
	@Bind
	public UIIFrame vchIfFrame;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack) {
			this.init();
		}
	}
	public void init(){
		// 设置帐套名称
		try {
			actsetcode=AppUtils.getActsetcode();
		} catch (FsActSetNoSelectException e) {
			AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
			return;
		}
		this.antiCheckoutAction.setDisabled(true);
		Long actsetid = AppUtils.getUserSession().getActsetid();
		// 默认摘要为“结转损益”
		this.desc = "结转损益";
		// 默认科目为“3131/本年利润”
		String actSql = "SELECT id FROM fs_act WHERE isdelete = FALSE AND code = '3131' AND actsetid = " + actsetid;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(actSql);
			this.actid = m.get("id").toString();
		} catch (Exception e1) {
			this.actid = "";
			e1.printStackTrace();
		}
		initYearPeriod();
		if(this.year != null &&!this.year.isEmpty() && this.period != null && !this.period.isEmpty()){
			try {
				String sql = "actsetid = "+AppUtils.getUserSession().getActsetid()+" AND year = "+this.year+" AND period = "+this.period+" AND isdelete = FALSE AND srctype = 'T'";
				FsVch fsvch = this.serviceContext.vchMgrService.fsVchDao.findOneRowByClauseWhere(sql);
				this.vchno = fsvch.getNos();
				this.vchid = String.valueOf(fsvch.getId());
				this.openVchno.setDisabled(false);
			}catch (Exception e) {
				e.printStackTrace();
				this.vchno = "";
				this.vchid = "0";
				this.openVchno.setDisabled(true);
			}
		}
		update.markUpdate(UpdateLevel.Data,"vchno");
		update.markUpdate(UpdateLevel.Data,"vchid");
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	public void initYearPeriod() {
		Long actsetid = AppUtils.getUserSession().getActsetid();
		
		// 根据当前登录用户，找对应的帐套，过滤兑换率列表
		String sql = "SELECT year,period,(SELECT ID FROM fs_vchtype where actsetid = a.id and name = '*' and isdelete = false) AS vchtype FROM fs_actset a WHERE isdelete = FALSE AND id = " + actsetid;
		Map m1;
		try {
			m1 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.year = m1.get("year").toString();
			this.period = m1.get("period").toString();
			this.vchtype = StrUtils.getMapVal(m1, "vchtype");
		} catch (Exception e) {
			this.year = "";
			this.period = "";
			e.printStackTrace();
		}
	}
	
	/**
	 * 期末结账
	 */
	@Action
	public void endClosing() {
		String usercode = AppUtils.getUserSession().getUsercode();
		Long actsetid = AppUtils.getUserSession().getActsetid();
		String sql = "SELECT f_fs_trans('usercode="+usercode+";actsetid="+actsetid+"');";
		try {
			//AppUtils.debug(sql);
			this.serviceContext.fsAstMgrService.fsAstDao.executeQuery(sql);
			this.initYearPeriod();
			MessageUtils.alert("OK");
			
			this.update.markUpdate(UpdateLevel.Data, "editPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 结转损益
	 */
	@Action
	public void loss() {
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "SELECT f_fs_transprofit('usercode="+usercode+";vchno="+this.vchno+";vchtype="+this.vchtype+";actsetid="+AppUtils.getUserSession().getActsetid()+"') AS result;";
		try {
			//AppUtils.debug(sql);
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String result = m.get("result").toString();
			if(result.startsWith("vchno:")){
				String[] results = result.split(";");
				if(results != null && results.length == 2){
					String[] vchs = results[0].split(":");
					String[] vchids = results[1].split(":");
					this.vchno = vchs != null && vchs.length == 2 ? vchs[1] : "";
					
					this.vchid = vchids != null && vchids.length == 2 ? vchids[1] : "";
				}
				result = "已生成结转损益凭证，凭证号："+this.vchno;
				update.markUpdate(UpdateLevel.Data,"vchno");
				this.openVchno.setDisabled(false);
				this.showVch();
			}
			MessageUtils.alert(result);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void trans3131to3151() {
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "SELECT f_fs_trans3131to3151('usercode="+usercode+";vchno="+this.vchno+";vchtype="+this.vchtype+";actsetid="+AppUtils.getUserSession().getActsetid()+"') AS result;";
		try {
			//AppUtils.debug(sql);
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String result = m.get("result").toString();
			if(result.startsWith("vchno:")){
				String[] results = result.split(";");
				if(results != null && results.length == 2){
					String[] vchs = results[0].split(":");
					String[] vchids = results[1].split(":");
					this.vchno = vchs != null && vchs.length == 2 ? vchs[1] : "";
					
					this.vchid = vchids != null && vchids.length == 2 ? vchids[1] : "";
				}
				result = "已生成结转凭证，凭证号："+this.vchno;
				update.markUpdate(UpdateLevel.Data,"vchno");
				this.openVchno.setDisabled(false);
				this.showVch();
			}
			MessageUtils.alert(result);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 反结账
	 */
	@Action
	public void antiCheckoutAction() {
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "SELECT f_fs_trans('usercode="+usercode+";actsetid="+AppUtils.getUserSession().getActsetid()+";opposite=true');";
		try {
			//AppUtils.debug(sql);
			this.serviceContext.fsAstMgrService.fsAstDao.executeQuery(sql);
			initYearPeriod();
			this.update.markUpdate(UpdateLevel.Data, "editPanel");
			MessageUtils.alert("已经反结转，请重新调汇，结转损益并结账！");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void antiCheckoutAjaxSubmit() {
		if(this.antiCheckoutBox) {
			this.antiCheckoutAction.setDisabled(false);
		} else if(!this.antiCheckoutBox) {
			this.antiCheckoutAction.setDisabled(true);
		}
	}
	@Override
	public void refresh() {
		super.refresh();
		this.init();
	}
	@Action(id="openVchno",event="onclick")
	public void showVch(){
		if(this.vchid != null && !this.vchid.isEmpty()){
			showVchEditWindow.show();
			vchIfFrame.load("./../doc/indtl_readonly.aspx?id="+this.vchid);
		}
	}
}
