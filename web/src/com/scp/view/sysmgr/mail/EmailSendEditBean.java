package com.scp.view.sysmgr.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysEmail;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.mail.emailsendeditBean", scope = ManagedBeanScope.REQUEST)
public class EmailSendEditBean extends GridFormView {

	@SaveState
	@Accessible
	public SysEmail selectedRowData = new SysEmail();

	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapHis = new HashMap<String, Object>();

	@Bind
	@SaveState
	public String addressee;

	@Bind
	@SaveState
	public String copys;
	
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
	
	@SaveState
	@Accessible
	public Long emailid;

	@Action
	public void showAddresslist() {
		addresslistWindow.show();
		this.grid.reload();
	}

	@Action
	public void showHisEmail() {
		hisEmailWindow.show();
		this.gridHisEmail.reload();
	}

	@Action
	public void addAddressee() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = getEmailFromDB(ids);

		this.addressee = StrUtils.isNull(this.addressee) ? emails
				: (this.addressee + ";" + emails);
		this.update.markUpdate(UpdateLevel.Data, "addressee");
	}

	@Action
	public void addCC() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = getEmailFromDB(ids);

		this.copys = StrUtils.isNull(this.copys) ? emails
				: (this.copys + ";" + emails);
		this.update.markUpdate(UpdateLevel.Data, "copys");
	}

	@Action
	public void addHisAddressee() {
		String[] ids = this.gridHisEmail.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = geHistEmailFromDB(ids);

		this.addressee = StrUtils.isNull(this.addressee) ? emails
				: (this.addressee + ";" + emails);
		this.update.markUpdate(UpdateLevel.Data, "addressee");
	}

	@Action
	public void addHisCC() {
		String[] ids = this.gridHisEmail.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = geHistCopysFromDB(ids);

		this.copys = StrUtils.isNull(this.copys) ? emails
				: (this.copys + ";" + emails);
		this.update.markUpdate(UpdateLevel.Data, "copys");
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
		String sender = this.getPop3Email();
		if(!StrUtils.isNull(sender)){
			qry += "\nAND sender LIKE  '%" + sender + "%'";
		}else{
			qry += "\nAND false";
		}
		m.put("qry", qry);
		return m;
	}

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String type = AppUtils.getReqParam("type");
			String id = AppUtils.getReqParam("id");

			this.add();
			// 委托单页面发送
			if (type.equals("shipping")) {
				String[] mailInfos = serviceContext.busBookingMgrService
						.generateMailInfo(Long.valueOf(id), AppUtils
								.getUserSession().getUserid());
				String sender = mailInfos[0];
				String address = mailInfos[1];
				String title = mailInfos[2];
				String content = mailInfos[3];

				selectedRowData.setSender(sender);
				selectedRowData.setAddressee(address);
				selectedRowData.setSubject(title);
				selectedRowData.setContent(content);
				selectedRowData.setEmailtype("S");
			} else if ("csuser".equals(type)) {
				if((id ==null)==false && ("".equals(id))==false){
					this.addressee = this.getEmail(id);
					if(addressee != null){
						selectedRowData.setAddressee(addressee);
						selectedRowData.setSubject("网上客服系统账号通知");
						Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_emaile_send('id="+id+"') AS content");
						if(m!=null&&m.get("content")!=null){
							selectedRowData.setContent(m.get("content").toString());
						};
						
					}else{
						alert("此用户邮箱地址为空或不存在此客户,无法默认收件人邮箱地址,请核实!");
					}
				}
				// 并单页面发送
			} else if ("shipunion".equals(type)) {
				String[] mailInfos = serviceContext.busShipjoinMgrService
						.generateMailInfo(Long.valueOf(id), AppUtils
								.getUserSession().getUserid());
				String sender = mailInfos[0];
				String address = mailInfos[1];
				String title = mailInfos[2];
				String content = mailInfos[3];

				selectedRowData.setSender(sender);
				selectedRowData.setAddressee(address);
				selectedRowData.setSubject(title);
				selectedRowData.setContent(content);
				selectedRowData.setEmailtype("S");

				// 寄单页面发送
			} else if ("shipdocsend".equals(type)) {

				String[] mailInfos = serviceContext.busDocdefMgrService
						.generateMailInfo(Long.valueOf(id), AppUtils
								.getUserSession().getUserid());
				String sender = mailInfos[0];
				String address = mailInfos[1];
				String title = mailInfos[2];
				String content = mailInfos[3];

				selectedRowData.setSender(sender);
				selectedRowData.setAddressee(address);
				selectedRowData.setSubject(title);
				selectedRowData.setContent(content);
				selectedRowData.setEmailtype("S");
				//工作报告发送已生成邮件
			}else if ("workreoport".equals(type)) {
				this.emailid = Long.valueOf(id);
				selectedRowData = serviceContext.sysEmailService.sysEmailDao.findById(this.emailid);
				this.addressee = selectedRowData.getAddressee();
			}
		}
	}

	@Action
	public void send() {
		try {
			this.save();
			this.serviceContext.sysEmailService.sendEmail(this.pkVal);
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void saveAction() {
		try {
			String usercode = AppUtils.getUserSession().getUsercode();
			selectedRowData.setAddressee(AppUtils.getReqParam("addressee"));
			selectedRowData.setCopys(AppUtils.getReqParam("copys"));
			selectedRowData.setContent(AppUtils.getReqParam("editor1"));
			selectedRowData.setSubject(AppUtils.getReqParam("subject"));
			
			serviceContext.sysEmailService.saveData(selectedRowData);
			selectedRowData = serviceContext.sysEmailService.sysEmailDao.findById(selectedRowData.getId());
			pkVal = selectedRowData.getId();
			
			// 附件数组
			Map<String,String> attachments = null;
			// 发送前查看是否有附件
			List<SysAttachment> sa = serviceContext.sysAttachmentService.sysAttachmentDao
					.findAllByClauseWhere("linkid = " + this.pkVal);
			if(sa != null && sa.size() > 0) {
				attachments = new HashMap<String, String>();
				for(SysAttachment att : sa) {
					// 拼成完整路径(包含文件名)
					attachments.put(att.getFilename(), AppUtils.getAttachFilePath() + att.getId() + att.getFilename());
				}
			}
			//AppUtils.debug("pkval"+this.pkVal);
			//AppUtils.debug("attachments"+attachments);
			this.serviceContext.sysEmailService.sendEmailHtml(this.pkVal, attachments);
//			super.save();
			
			if(this.emailid !=null && !"".equals(this.emailid)){//说明有工作报告邮件
				String sql = " UPDATE  oa_workreport  SET isemailsent = TRUE  WHERE id  =  " + selectedRowData.getLinkid();
				serviceContext.sysAttachmentService.sysAttachmentDao.executeSQL(sql);
//				ApplicationUtils.debug("要发送的报告id"+selectedRowData.getLinkid());
				alert("工作报告发送成功!");
			}else{
				alert("ok");
			}
			add();
		} catch (java.lang.NullPointerException e){
			MessageUtils.alert("Error,please check! 错误,请检查!");
		} catch (javax.mail.SendFailedException e){
			MessageUtils.alert("501 Bad address syntax,please check! 收件人地址错误,请检查!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void add() {
		selectedRowData = new SysEmail();
		this.addressee = "";
		this.copys = "";
		super.add();
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {
	
	}

	/**
	 * 根据id查询email地址
	 * 
	 * @param ids
	 * @return
	 */
	public String getEmailFromDB(String[] ids) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT email1 FROM _addresslist WHERE id IN(");
		List<String> emailList = null;
		for (int i = 0; i < ids.length; i++) {
			if (i == (ids.length - 1)) {
				sql.append(ids[i]);
			} else {
				sql.append(ids[i] + ", ");
			}

		}
		sql.append(")");
		emailList = this.serviceContext.userMgrService.sysUserDao
				.executeQuery(sql.toString());
		for (int i = 0; i < emailList.size(); i++) {
			if (emailList != null) {
				if (i == (emailList.size() - 1)) {
					sb.append(emailList.get(i));
				} else {
					if (emailList.get(i) == null || emailList.get(i).equals("")) {

					} else {
						sb.append(emailList.get(i) + ";");
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 根据id查询历史email地址
	 * 
	 * @param ids
	 * @return
	 */
	public String geHistEmailFromDB(String[] ids) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT addressee FROM sys_email WHERE id IN(");
		List<String> emailList = null;
		for (int i = 0; i < ids.length; i++) {
			if (i == (ids.length - 1)) {
				sql.append(ids[i]);
			} else {
				sql.append(ids[i] + ", ");
			}

		}
		sql.append(")");
		emailList = this.serviceContext.userMgrService.sysUserDao
				.executeQuery(sql.toString());
		for (int i = 0; i < emailList.size(); i++) {
			if (emailList != null) {
				if (i == (emailList.size() - 1)) {
					sb.append(emailList.get(i));
				} else {
					if (emailList.get(i) == null || emailList.get(i).equals("")) {

					} else {
						sb.append(emailList.get(i) + ";");
					}
				}
			}
		}

		return sb.toString();
	}
	
	/**
	 * 根据id查询历史copys地址
	 * 
	 * @param ids
	 * @return
	 */
	public String geHistCopysFromDB(String[] ids) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT copys FROM sys_email WHERE id IN(");
		List<String> emailList = null;
		for (int i = 0; i < ids.length; i++) {
			if (i == (ids.length - 1)) {
				sql.append(ids[i]);
			} else {
				sql.append(ids[i] + ", ");
			}

		}
		sql.append(")");
		emailList = this.serviceContext.userMgrService.sysUserDao
				.executeQuery(sql.toString());
		for (int i = 0; i < emailList.size(); i++) {
			if (emailList != null) {
				if (i == (emailList.size() - 1)) {
					sb.append(emailList.get(i));
				} else {
					if (emailList.get(i) == null || emailList.get(i).equals("")) {

					} else {
						sb.append(emailList.get(i) + ";");
					}
				}
			}
		}

		return sb.toString();
	}

	@Action
	public void qryRefreshHis() {
		if (this.gridHisEmail != null) {
			this.gridHisEmail.reload();
		}
	}

	@Action
	public void clearQryKeyHis() {
		if (qryMapHis != null) {
			qryMapHis.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.gridHisEmail.reload();
		}
	}
	
	@Action
	public void mgrAttachment() {
		if(StrUtils.isNull(this.addressee)) {
			MessageUtils.alert("请先填写收件人等相关信息！");
			return;
		}
		this.attachmentWindow.show();
		serviceContext.sysEmailService.saveData(selectedRowData);
		selectedRowData = serviceContext.sysEmailService.sysEmailDao.findById(selectedRowData.getId());
		pkVal = selectedRowData.getId();
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid=" + this.pkVal);
	}
	
	public String getEmail(String id){
		String sql = "SELECT email1, email2 FROM cs_user WHERE id = "+ Long.valueOf(id);
		 Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		 String eamil1 = map.get("email1").toString();
		 String eamil2 = map.get("email2").toString();
		 if(!StrUtils.isNull(eamil1)){//以eamil1为主
			 return eamil1;
		 }else if(!StrUtils.isNull(eamil2)){
			 return eamil2;
		 }else{return null;}
	}
	/**
	 * 正文默认内容
	 */
	public String getContent(String id){
		String sql = "SELECT code, email2 FROM cs_user WHERE id = "+ Long.valueOf(id);
		 Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		 String code = map.get("code").toString();
		 String url = ConfigUtils.findSysCfgVal("cs_url_base");
		String context = " 恭喜您!您已成功申请我司网上客服系统账号."
			      +"\n 您的帐号："+code+"，默认密码:.(第一次登录系统请重新设置密码)"
			      +"\n 地址："+url+""
			      +"\n ";
		return context;
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
			alert("无法获取您的邮箱地址,请查看是否设置邮箱!");
			e.printStackTrace();
			return null;
		}
	}
}
