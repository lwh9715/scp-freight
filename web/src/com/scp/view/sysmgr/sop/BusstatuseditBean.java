package com.scp.view.sysmgr.sop;

import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.sop.busstatuseditBean", scope = ManagedBeanScope.REQUEST)
public class BusstatuseditBean extends GridSelectView {
	private static String pid;
	
	@Bind
	public UIIFrame dtlIFrame;
	
	@Bind
	public UIWindow showAssignWindow;
	
	@Bind
	public UIIFrame assignIframe;
	
	@SaveState
	private boolean showAssign;
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			String id = AppUtils.getReqParam("id");
			if(null == id || "".equals(id)){
				Browser.execClientScript("hideButton(saveJsVar,'save')");
				pid = AppUtils.getReqParam("pid");
				return;
			}else{
				Browser.execClientScript("hideButton(addJsVar,'add')");
			}
			String sql = "SELECT id,namec,namee,code,bustype,status,COALESCE(isinit,FALSE) AS isinit,remark,COALESCE(alertopen,FALSE) AS alertopen" +
			",alertype,alertimetype,alertimehours,alertevel,statechgopen,scnotifybyassign,scnotifyemails,scnotifytype" +
			" FROM sop_bustate WHERE isdelete = FALSE AND id = " + Long.valueOf(id);
			
			try {
				qryMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			refresh();
		}
	}
	
	@Action(id = "del")
	public void del() {
		Object id = qryMap.get("id");
		return;
	}
	
	//TODO 待解决，未知原因新增@Action方法无法访问接口，先采用重写原有方法调用接口
	@Override
	public void doPrint() {
		showAssignWindow.show();
	}
	
	//TODO 待解决，未知原因新增@Action方法无法访问接口，先采用重写原有方法调用接口
	@Override
	public void doDel() {
		Object id = qryMap.get("id");
//		assignIframe.setSrc("/scp/pages/module/customer/assigneduser.aspx?linktype=J&id="+id);
		assignIframe.load("/scp/pages/module/customer/assigneduser.aspx?linktype=J&id="+id);
	}
	
	@Override
	public void save(){
		showAssignWindow.show();
		Object remark = qryMap.get("remark");
		if("0".equals(remark.toString())){
			return;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("UPDATE sop_bustate SET ");
		if(null != qryMap.get("namec")){
			buf.append(" namec = '"+qryMap.get("namec")+"'");
		}
		if(null != qryMap.get("namee")){
			buf.append(",namee ='"+qryMap.get("namee")+"'");
		}
		if(null != qryMap.get("code")){
			buf.append(",code ='"+qryMap.get("code")+"'");
		}
		if(null != qryMap.get("bustype")){
			buf.append(",bustype ='"+qryMap.get("bustype")+"'");
		}
		if(null != qryMap.get("status")){
			buf.append(",status ='"+qryMap.get("status")+"'");
		}
		if(null != qryMap.get("isinit")){
			buf.append(",isinit ="+qryMap.get("isinit"));
		}
		if(null != qryMap.get("remark")){
			buf.append(",remark ='"+qryMap.get("remark")+"'");
		}
		if(null != qryMap.get("alertopen")){
			buf.append(",alertopen ="+qryMap.get("alertopen"));
		}
		if(null != qryMap.get("alertype")){
			buf.append(",alertype ='"+qryMap.get("alertype")+"'");
		}
		if(null != qryMap.get("alertimetype")){
			buf.append(",alertimetype ='"+qryMap.get("alertimetype")+"'");
		}
		if(null != qryMap.get("alertimehours") && StrUtils.isNull(String.valueOf(qryMap.get("alertimehours"))) == false && Integer.valueOf(String.valueOf(qryMap.get("alertimehours")))>0){
			buf.append(",alertimehours ="+qryMap.get("alertimehours"));
		}else{
			buf.append(",alertimehours ="+0);
		}
		if(null != qryMap.get("alertevel")){
			buf.append(",alertevel ='"+qryMap.get("alertevel")+"'");
		}
		if(null != qryMap.get("statechgopen")){
			buf.append(",statechgopen ="+qryMap.get("statechgopen"));
		}
		if(null != qryMap.get("scnotifybyassign")){
			buf.append(",scnotifybyassign ="+qryMap.get("scnotifybyassign"));
		}
		if(null != qryMap.get("scnotifyemails")){
			buf.append(",scnotifyemails ='"+qryMap.get("scnotifyemails")+"'");
		}
		if(null != qryMap.get("scnotifytype")){
			buf.append(",scnotifytype ='"+qryMap.get("scnotifytype")+"'");
		}
		if(null != qryMap.get("id")){
			buf.append(" WHERE isdelete = FALSE AND id = " + qryMap.get("id"));
		}else{
			MessageUtils.alert("参数异常，保存失败!");
			return;
		}
		try {
			daoIbatisTemplate.updateWithUserDefineSql(buf.toString());
			MessageUtils.alert("保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		refresh();
	}
	
	@Action(id = "add")
	public void add(){
		Object remark = qryMap.get("remark");
		boolean[] flag = {false,false,false,false,false};
		StringBuffer buf = new StringBuffer();
		buf.append("INSERT INTO sop_bustate (id,pid,ordno,namec,namee,code,bustype,status,remark," +
					"alertype,alertimetype,alertevel,scnotifyemails,scnotifytype,isleaf,isdelete");
		if(null != qryMap.get("isinit") && StrUtils.isNull(String.valueOf(qryMap.get("isinit"))) == false){
			buf.append(",isinit");
			flag[0] = true;
		}
		if(null != qryMap.get("alertopen") && StrUtils.isNull(String.valueOf(qryMap.get("alertopen"))) == false){
			buf.append("alertopen");
			flag[1] = true;
		}
		if(null != qryMap.get("alertimehours") && StrUtils.isNull(String.valueOf(qryMap.get("alertimehours"))) == false && Integer.valueOf(String.valueOf(qryMap.get("alertimehours")))>0){
			Object a = qryMap.get("alertimehours");
			buf.append(",alertimehours");
			flag[2] = true;
		}
		if(null != qryMap.get("statechgopen") && StrUtils.isNull(String.valueOf(qryMap.get("statechgopen"))) == false){
			buf.append(",statechgopen");
			flag[3] = true;
		}
		if(null != qryMap.get("scnotifybyassign") && StrUtils.isNull(String.valueOf(qryMap.get("scnotifybyassign"))) == false){
			buf.append(",scnotifybyassign");
			flag[4] = true;
		}
		Long id = uuid();
		buf.append(") VALUES ("+uuid()+",");
		qryMap.put("id", id);
		if(null == pid){
			MessageUtils.alert("参数异常，新增失败!");
			return;
		}else{
			buf.append(pid+",");
		}
		buf.append("10,");
		buf.append("'"+qryMap.get("namec")+"',");
		buf.append("'"+qryMap.get("namee")+"',");
		buf.append("'"+qryMap.get("code")+"',");
		buf.append("'"+qryMap.get("bustype")+"',");
		buf.append("'"+qryMap.get("status")+"',");
		buf.append("'"+qryMap.get("remark")+"',");
		buf.append("'"+qryMap.get("alertype")+"',");
		buf.append("'"+qryMap.get("alertimetype")+"',");
		buf.append("'"+qryMap.get("alertevel")+"',");
		buf.append("'"+qryMap.get("scnotifyemails")+"',");
		buf.append("'"+qryMap.get("scnotifytype")+"',");
		buf.append("'Y',");
		buf.append("FALSE");

		if(flag[0]){
			buf.append(","+qryMap.get("isinit"));
		}
		if(flag[1]){
			buf.append(","+qryMap.get("alertopen"));
		}
		if(flag[2]){
			buf.append(","+qryMap.get("alertimehours"));
		}
		if(flag[3]){
			buf.append(","+qryMap.get("statechgopen"));
		}
		if(flag[4]){
			buf.append(","+qryMap.get("scnotifybyassign"));
		}
		buf.append(")");
		try {
			daoIbatisTemplate.updateWithUserDefineSql(buf.toString());
//			dtlIFrame.setSrc("/scp/pages/sysmgr/sop/busstatus.aspx?id=0");
//			update.markAttributeUpdate(dtlIFrame, "src");
//			update.markUpdate(true, UpdateLevel.Data, dtlIFrame);
			Browser.execClientScript("hideButton(addJsVar,'add')");
			refresh();
			MessageUtils.alert("新增成功!");
		} catch (Exception e) {
			MessageUtils.alert("新增失败!");
			e.printStackTrace();
		}
	}
	
	public Long uuid(){
		Date date = new Date();
		Long time = date.getTime();
		return time;
	}

}

