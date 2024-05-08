package com.scp.view.sysmgr.msgboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.DataModel;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIDataView;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.MultiLanguageBean;
import com.scp.model.sys.SysMsgboard;
import com.scp.service.sysmgr.SysMsgBoardService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@ManagedBean(name="pages.sysmgr.msgboard.msgboarddtlmgrBean",scope=ManagedBeanScope.REQUEST)
public class MsgDoardDtlMgr{
	
	@Inject
	public PartialUpdateManager update;
	
	@Inject(value = "l")
	private MultiLanguageBean l;
	
	@Bind
	private UIDataView msgView;
	
	@SaveState
	private String faqid;
	
	@Bind
	@SaveState
	private String subject;
	
//	@Action
//	public void doUntie() {
//		String sql = "SELECT * FROM _sys_msgboard WHERE id=" + faqid;
//		try {
//			Map map = AppDaoUtil.qry4OneRow(sql);
//			String ins = StrTools.getMapVal(map, "inputer");
//			if(!StrTools.isNull(ins)) {
//				
//				long insuserid = Long.parseLong(ins);
//				String state = StrTools.getMapVal(map, "state");
//				long userid = AppUtil.getUserSession().getUserid();
//				if(userid == insuserid || userid == 107222501) {
//					sql = "UPDATE sys_msgboard SET state = 'B' WHERE id = " + faqid;
//					AppDaoUtil.execute(sql);
//					MsgUtil.alert("此操作已成功！");
//					subject = subject + "(已解决)";
//					update.markUpdate(UpdateLevel.Data,"subject");
//				} else {
//					MsgUtil.alert("非版主不能进行此操作！");
//				}
//			}else {
//				MsgUtil.alert("非版主不能进行此操作！");
//			}
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//		}
//	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack) {
			faqid = AppUtils.getHttpServletRequest().getParameter("faqid");
			SysMsgboard sysMsgboard  = sysMsgBoardService.sysMsgboardDao.findById(Long.parseLong(faqid));
//			String qryInfo = "SELECT subject,state FROM _sys_msgboard WHERE id =" + faqid;
//			Map map = null;
//			try {
//				map = AppDaoUtil.qry4OneRow(qryInfo);
//			} catch (Exception e) {
//				MsgUtil.showException(e);
//			}
			subject = l.find("主题")+":" + sysMsgboard.getSubject();
			
			String state = sysMsgboard.getState();

			if(state.equals("B")) {
				subject = subject + "("+l.find("已解决")+")";
				Browser.execClientScript("doUntie.setDisabled(true)");
			}
			update.markUpdate(UpdateLevel.Data,"subject");
			update.markUpdate(UpdateLevel.Data,"parentid");
			String newest = AppUtils.getHttpServletRequest().getParameter("newest");
			if(!StrUtils.isNull(newest) && newest.equals("Y")) {
				Browser.execClientScript("scrollCtrl(1)");
			}
		}
	}
	
	@ManagedProperty("#{sysMsgBoardService}")
	public SysMsgBoardService sysMsgBoardService;
	
	@DataModel(id="msgView")
	public List<Map> getMsgView() {
		List<Map> list = sysMsgBoardService.findMsgData(faqid);;
//		try {
//			list = AppDaoUtil.query(qrySql);
////			for(Map m : list) {
////				String insdate = StrTools.getMapVal(m, "inputtime");
////				insdate = getOtherTimeFormat(insdate);
////				m.put("inputtime", insdate);
////			}
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//		}
		return list;
	}
	
	public String getOtherTimeFormat(String insdate) throws ParseException {
		//先获取日期距离今天的天数
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		//读取回复日期的年月日
		Date l_date = format.parse(insdate);
		//读取现在时刻的年月日
		Date n_date = format.parse(format.format(now));
		
		//取出回复日期距今天数
		float res_date = n_date.getTime() - l_date.getTime();
		double day = Math.floor(res_date/(24*60*60*1000));
		
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date last = format.parse(insdate);
		float l=now.getTime()-last.getTime();
		if(day > 2) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			return f.format(last);
		} else if(day == 2 ){
			SimpleDateFormat f = new SimpleDateFormat("HH:mm");
			return this.l.find("前天");
		} else if (day <= 1) {
			double hour=Math.floor(l/(60*60*1000));
			if(hour > 24) {
				return this.l.find("昨天");
			} else if(hour > 0) {
				return (int)hour + this.l.find("小时前"); 
			} else {
				double m = Math.floor(l/(60*1000));
				if( m > 0 ) {
					return (int)m + this.l.find("分钟前");
				} else {
					return this.l.find("刚刚");
				}
			}
		} 
		return null;
	}

	@Action
	public void refresh() {
		msgView.reload();
//		Browser.execClientScript("var task = new Ext.util.DelayedTask(tmpLoad); task.delay(50)");
	}
	
	@Bind
	private UIWindow respWin;
	
	@Bind
	private UIIFrame respIfram;
	
	
	@Action
	public void doResponse() {
		String id = AppUtils.getHttpServletRequest().getParameter("id");
		String url = "msgboardresponse.xhtml?id=" + faqid;
		respWin.show();
		respIfram.load(url);
	}
}
