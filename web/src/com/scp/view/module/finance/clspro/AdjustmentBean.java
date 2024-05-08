package com.scp.view.module.finance.clspro;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.NoRowException;
import com.scp.model.finance.fs.FsVch;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.finance.clspro.adjustmentBean", scope = ManagedBeanScope.REQUEST)
public class AdjustmentBean extends GridView {

	@Bind
	@SaveState
	public String year;
	
	@Bind
	@SaveState
	public String period;
	
	@Bind
	@SaveState
	public String desc = "";
	
	@Bind
	public UIButton openVchno;
	
	@Bind
	@SaveState
	public String actcode = "";
	
	@Bind
	@SaveState
	public String vchtype;
	
	@Bind
	public boolean isUpdateNextRate = false;
	
	@Bind
	@SaveState
	public String vchno = "";
	
	@Bind
	@SaveState
	public String vchid;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	public UIWindow showVchEditWindow;
	
	@Bind
	public UIIFrame vchIfFrame;
	
	@Bind
	public UIEditDataGrid gridRate;
	
	@Bind(id = "gridRate", attribute = "addedData")
	protected Object addedData;
	
	@Bind(id = "gridRate", attribute = "modifiedData")
	protected Object modifiedData;
	
	@Bind(id = "gridRate", attribute = "removedData")
	protected Object removedData;
	
	@Bind(id = "gridRate", attribute = "dataProvider")
	protected GridDataProvider getDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridRate.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMap), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridRate.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMap));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack) {
			refresh();
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid() + "";
		qry += "\nAND yeardesc = '" + this.year + "'";
		qry += "\nAND perioddesc = '" + this.period + "'";
		m.put("qry", qry);
		return m;
	}
	
	
	@Override
	public void refresh() {
		super.refresh();
		
		// 设置帐套名称
		try {
			actsetcode=AppUtils.getActsetcode();
		} catch (FsActSetNoSelectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Long actsetid = AppUtils.getUserSession().getActsetid();
		// 默认摘要为“期末调汇”
		this.desc = "期末调汇";
		this.actcode = "5505";
		
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
		
		if(this.year != null &&!this.year.isEmpty() && this.period != null && !this.period.isEmpty()){
			try {
				String sqls = "actsetid = "+AppUtils.getUserSession().getActsetid()+" AND year = "+this.year+" AND period = "+this.period+" AND isdelete = FALSE AND srctype = 'B'";
				FsVch fsvch = this.serviceContext.vchMgrService.fsVchDao.findOneRowByClauseWhere(sqls);
				this.vchno = fsvch.getNos();
				this.vchid = String.valueOf(fsvch.getId());
				this.openVchno.setDisabled(false);
			}catch (Exception e) {
				if(!NoRowException.class.getName().equals(e.getClass().getName()))
					e.printStackTrace();
				this.vchno = "";
				this.vchid = "0";
				this.openVchno.setDisabled(true);
			}
		}
		update.markUpdate(UpdateLevel.Data,"vchno");
		update.markUpdate(UpdateLevel.Data,"vchid");
		
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
		this.gridRate.reload();
	}


	@Action
	public void updateRate() {
		String usercode = AppUtils.getUserSession().getUsercode();
		Long actsetid = AppUtils.getUserSession().getActsetid();
		
		String newRateString = this.serviceContext.actrateMgrService.getNewRateString(modifiedData);
		if(StrUtils.isNull(newRateString)) {
			MessageUtils.alert("请先设置兑换率！");
			return;
		}
		String sql = "SELECT f_fs_adjustrate('usercode="+usercode
		+";actsetid="+actsetid+"" 
		+";desc="+desc+"" 
		+";actcode="+actcode+"" 
		+";vchtype="+vchtype+"" 
		+";vchno="+vchno+";" 
		+ newRateString + ";isupdatenext="+this.isUpdateNextRate+";') AS result;";
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
				result = "已生成期末调汇凭证，凭证号："+this.vchno;
				update.markUpdate(UpdateLevel.Data,"vchno");
				this.openVchno.setDisabled(false);
				this.showVch();
			}
			MessageUtils.alert(result);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public String nextPriorRate;
	
	@Action
	public void priorRate() {
		initNextPeriodRate();
		Browser.execClientScript("priorNewRate();");
	}
	
	private void initNextPeriodRate(){
		Long actsetid = AppUtils.getUserSession().getActsetid();
		int yeartmp = Integer.parseInt(year);
		int periodtmp = Integer.parseInt(period) + 1;
		if(periodtmp==0) {
			periodtmp = 12;
			yeartmp -=1;
		}else if(periodtmp > 12){
			periodtmp = 1;
			yeartmp += 1;
		}
		List<Map> list = this.serviceContext.actrateMgrService.getPriorRate( yeartmp,periodtmp, actsetid);
		JSONArray jsonData = JSONArray.fromObject(list);
		nextPriorRate = jsonData.toString();
		update.markUpdate(UpdateLevel.Data,"nextPriorRate");
	}
	
	@Action(id="openVchno",event="onclick")
	public void showVch(){
		if(this.vchid != null && !this.vchid.isEmpty()){
			showVchEditWindow.show();
			vchIfFrame.load("./../doc/indtl_readonly.aspx?id="+this.vchid);
		}
	}
}
