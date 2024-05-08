package com.scp.view.module.other;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.AuthenticationFailedException;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.sys.SysEmail;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.util.ConfigUtils.UsrCfgKey;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.other.sendemailBean", scope = ManagedBeanScope.REQUEST)
public class SendEmailBean extends GridFormView {

	@Bind
	@SaveState
	public String mail_attach;
	
	@Bind
	@SaveState
	public String addressee;
	
	@Bind
	@SaveState
	public String copys;
	
	@Bind
	@SaveState
	public String subject;

	@Bind
	public UIWindow addresslistWindow;

	@Bind
	public UIWindow hisEmailWindow;

	@Bind
	public UIDataGrid gridHisEmail;
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Bind
	public UIWindow attachmentWindow;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapHis = new HashMap<String, Object>();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String url = AppUtils.getReqParam("url");
			if(!StrUtils.isNull(url)){
				mail_attach = url;
			}
		}
	}

	@Action
	public void showAddresslist(){
		addresslistWindow.show();
		this.grid.reload();
	}
	
	@Action
	public void sendBtn(){
		
	}
	
	@Action
	public void mgrAttachment(){
		
	}
	
	@Action
	public void saveAction(){
		try {
			Map<String,String> m = ConfigUtils.findUserCfgVals(
					new String[]{
							UsrCfgKey.email_srv_smtp.name(),
							UsrCfgKey.email_srv_port.name(),
							UsrCfgKey.email_srv_pop3.name(),
							UsrCfgKey.email_pop3_account.name(),
							UsrCfgKey.email_pop3_pwd.name(),
							} , AppUtils.getUserSession().getUserid());
			
			EMailSendUtil email = new EMailSendUtil(
					m.get(UsrCfgKey.email_srv_smtp.name()), 
					Integer.parseInt(m.get(UsrCfgKey.email_srv_port.name())), 
					m.get(UsrCfgKey.email_pop3_account.name()), 
					m.get(UsrCfgKey.email_pop3_account.name()), 
					m.get(UsrCfgKey.email_pop3_pwd.name())
					);
			
			String content = AppUtils.getReqParam("body");
			String subject = AppUtils.getReqParam("subject");
			String acceptAddress = AppUtils.getReqParam("addressee");
			String ccAddress = AppUtils.getReqParam("copys");
			Map<String,String> attachments = new HashMap<String, String>();
			String filename = System.getProperty("java.io.tmpdir") + mail_attach;
			attachments.put(mail_attach, filename);
			
			email.sendEmailHtml(content, subject, acceptAddress, ccAddress, attachments);
			MessageUtils.alert("OK!");
		} catch (AuthenticationFailedException e) {
			MessageUtils.alert("Wrong password");
			e.printStackTrace();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	private void addHistory(String[] add,String[] cc,String subject,String body,String sender){
		if(add != null && add.length > 0){
			String ccstr = null;
			if(cc != null && cc.length > 0){
		        StringBuilder b = new StringBuilder();
		        for (int i = 0; i < cc.length ; i++) {
		            b.append(String.valueOf(cc[i]));
		            if (i != cc.length -1)
		            b.append(", ");
		        }
				ccstr = b.toString();
			}
			for (String s : add) {
				SysEmail email = new SysEmail();
				email.setAddressee(s);
				email.setCopys(subject);
				email.setSubject(ccstr);
				email.setContent(body);
				email.setSender(sender);
				email.setEmailtype("H");
				email.setIssent(true);
				this.serviceContext.sysEmailService.sysEmailDao.create(email);
			}
		}
	}
	
	@Action
	public void showHisEmail() {
		hisEmailWindow.show();
		this.gridHisEmail.reload();
	}
	
	
	@Bind(id = "gridHisEmail", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridHisEmail.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapHis), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridHisEmail.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapHis));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap){
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND sender LIKE  '%" + this.getPop3Email() + "%'";
		m.put("qry", qry);
		return m;
	}
	
	/**
	 * 获得POP3发送人邮箱地址
	 */
	public String getPop3Email(){
		String sql = "SELECT val FROM sys_configuser WHERE key = 'email_pop3_account' AND userid  =  " + AppUtils.getUserSession().getUserid();
		 try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			 return map.get("val").toString();
		} catch (Exception e) {
			MessageUtils.alert("无法获取您的邮箱地址,请查看是否设置邮箱!");
			return null;
			
		}
	}
	
	@Action
	public void addAddressee(){
		String[] ids = this.grid.getSelectedIds();
		
		if(ids == null || ids.length < 1){
			MessageUtils.alert("");
			return;
		}
		StrUtils.array2List(ids);
	}
	
	@Action
	public void addCC(){
		
	}

	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}
	

}
