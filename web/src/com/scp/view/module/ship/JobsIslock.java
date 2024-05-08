package com.scp.view.module.ship;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.jobsislockBean", scope = ManagedBeanScope.REQUEST)
public class JobsIslock extends GridView {
	
	@Bind
	@SaveState
	@Accessible
	public String startDate;
	
	@Bind
	@SaveState
	@Accessible
	public String endDate;
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			Calendar nowDate = Calendar.getInstance();
			nowDate.setTime(new Date());
			int lastDay = nowDate.getActualMaximum(Calendar.DAY_OF_MONTH);
			// 当前月第一天
			this.startDate = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01";
			// 当前月最后一天
			this.endDate = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-" + lastDay;
			// 初始化关账的开始日期和截止日期
			this.update.markUpdate(UpdateLevel.Data, "startDate");
			this.update.markUpdate(UpdateLevel.Data, "endDate");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(new Date());
		int lastDay = nowDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 当前月第一天
		String startDates = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01";
		// 当前月最后一天
		String endDates = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-" + lastDay;
		if(endDate != null || startDate!= null) {
			qry += "\nAND jobdate  BETWEEN '" + (startDate==null||startDate=="" ? startDates : startDate)
			+ "' AND '" + (endDate==null||endDate=="" ? endDates : endDate) + "'";
		}
		m.put("qry", qry);
		return m;		
	}
	
	
	/**
	 * 关账
	 */
	@Action
	public void close() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			
			this.serviceContext.jobsMgrService.closeIslock(ids,AppUtils.getUserSession().getUsername());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refresh();
		MessageUtils.alert("OK");
	}
	
	/**
	 * 批量关账
	 */
	@Action
	public void closes() {
		try {
			// 将所有工作单的日期处在设定日期范围内的工作单的islock变为true,同时记录关账时间
			this.serviceContext.jobsMgrService.closesIslock(this.startDate,this.endDate,AppUtils.getUserSession().getUsername());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refresh();
		MessageUtils.alert("OK");
	}
	
	/**
	 * 开账
	 */
	@Action
	public void open() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			this.serviceContext.jobsMgrService.opeanIslock(ids,AppUtils.getUserSession().getUsername());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.refresh();
		MessageUtils.alert("OK");
	}
}
