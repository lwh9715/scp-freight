package com.scp.view.sysmgr.mail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.LMapBase.MLType;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysEmail;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.ufms.web.view.sysmgr.LogBean;

import freemarker.template.Configuration;
import freemarker.template.Template;
@ManagedBean(name = "pages.sysmgr.mail.emaileditBean", scope = ManagedBeanScope.REQUEST)
public class EmaileditBean extends GridView {
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Bind
	public UIWindow attachmentWindow;
	
	@SaveState
	public Long mailid;
	
	@Bind
	public UIButton save;
	
	@SaveState
	public Long jobid;
	
	@SaveState
	public String type;
	
	
	@Bind
	@SaveState
	public String customEmail1="";
	
	@Bind
	@SaveState
	public String agentEmail1="";
	
	@SaveState
	@Accessible
	public SysEmail selectedRowData = new SysEmail();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			type = AppUtils.getReqParam("type");
			String jobidStr = AppUtils.getReqParam("jobid");
			if(StrUtils.isNull(jobidStr) || "null".equals(jobidStr))jobidStr = "-1";
			jobid = Long.parseLong(jobidStr);
			String subject = AppUtils.getReqParam("subject");
			
			String linkid = AppUtils.getReqParam("linkid");
			
			if(!StrUtils.isNull(id)) {
				this.mailid = Long.parseLong(id);
				if("-1".equals(id)){
					selectedRowData.setSubject(subject);
					this.serviceContext.sysEmailService.saveData(selectedRowData);
					this.mailid = this.selectedRowData.getId();
					String emailsign = ConfigUtils.findUserCfgVal("email_sign", AppUtils.getUserSession().getUserid());
					this.selectedRowData.setContent(emailsign);
				}else{
					selectedRowData = this.serviceContext.sysEmailService.sysEmailDao.findById(this.mailid);
				}
			}
			if(!StrUtils.isNull(linkid) && "bus_price".equals(type)){
				try {
					selectedRowData = this.serviceContext.sysEmailService.sysEmailDao.findOneRowByClauseWhere("isdelete = false and linkid = "+Long.parseLong(linkid)+"");
				} catch (Exception e) {
					alert("未生成邮件!");
					e.printStackTrace();
					return ;
				}
			}
			
			if("T".equals(type)) {
				this.save.setDisabled(true);
			}else if("union".equals(type)){// 并单邮件
				String sql = "SELECT f_sys_mail_generate('type=shipunion;id="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS eamilcontent;";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String eamilcontent = StrUtils.getMapVal(map, "eamilcontent");
				
				this.selectedRowData.setAddressee(eamilcontent.split("-\\.-\\.-")[1]);
				this.selectedRowData.setSubject(eamilcontent.split("-\\.-\\.-")[2]);
				this.selectedRowData.setContent(eamilcontent.split("-\\.-\\.-")[3]+(StrUtils.isNull(selectedRowData.getContent())?"":selectedRowData.getContent()));
			}else if("D".equals(type)){// 子单邮件
				String sql = "SELECT f_sys_mail_generate('type=shipping;id="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS eamilcontent;";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String eamilcontent = StrUtils.getMapVal(map, "eamilcontent");
				
				this.selectedRowData.setAddressee(eamilcontent.split("-\\.-\\.-")[1]);
				this.selectedRowData.setSubject(eamilcontent.split("-\\.-\\.-")[2]);
				this.selectedRowData.setContent(eamilcontent.split("-\\.-\\.-")[3]+(StrUtils.isNull(selectedRowData.getContent())?"":selectedRowData.getContent()));
				this.selectedRowData.setCopys(eamilcontent.split("-\\.-\\.-")[4]);
			}else if("A".equals(type)){// 指派邮件
				String userids = AppUtils.getReqParam("userids");
				String src = AppUtils.getReqParam("src");
				if("jobs".equals(src)){
					String sql = "\nSELECT array_to_string(ARRAY(SELECT (CASE WHEN email1 = '' THEN NULL ELSE email1 END) FROM sys_user x WHERE x.isdelete = FALSE " +
					 "\nAND EXISTS(SELECT 1 FROM bus_shipping a WHERE a.jobid =  " +jobid+
					 "\nAND EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = a.id AND userid = x.id" +
					 "\nAND id = ANY(array["+userids+"]) ))),';')AS eamilcontent;";
					Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					String eamilcontent = StrUtils.getMapVal(map, "eamilcontent");
					
					this.selectedRowData.setAddressee(eamilcontent);
					
					sql = "SELECT f_sys_mail_generate('type=assign;id="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS eamilcontent;";
					map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					eamilcontent = StrUtils.getMapVal(map, "eamilcontent");
					this.selectedRowData.setSubject(eamilcontent.split("-\\.-\\.-")[2]);
					this.selectedRowData.setContent(eamilcontent.split("-\\.-\\.-")[3]+(StrUtils.isNull(selectedRowData.getContent())?"":selectedRowData.getContent()));
					this.selectedRowData.setCopys(eamilcontent.split("-\\.-\\.-")[4]);
					//System.out.println(eamilcontent.split("-\\.-\\.-")[0]);
					//System.out.println(eamilcontent.split("-\\.-\\.-")[1]);
					//System.out.println(eamilcontent.split("-\\.-\\.-")[2]);
					//System.out.println(eamilcontent.split("-\\.-\\.-")[3]);
					//System.out.println(eamilcontent.split("-\\.-\\.-")[4]);
				}else{
					String orderid = AppUtils.getReqParam("orderid");
					if(StrUtils.isNull(orderid))orderid="-1";
					String sql = "\nSELECT array_to_string(ARRAY(SELECT email1 FROM sys_user x WHERE x.isdelete = FALSE " +
					 "\nAND EXISTS(SELECT 1 FROM bus_order a WHERE a.id =  " +orderid+
					 "\nAND EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = a.id AND userid = x.id" +
					 "\nAND id = ANY(array["+userids+"]) ))),';','')AS eamilcontent;";
					Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					String eamilcontent = StrUtils.getMapVal(map, "eamilcontent");
					
					this.selectedRowData.setAddressee(eamilcontent);
				}
				//this.selectedRowData.setAddressee(eamilcontent.split("-\\.-\\.-")[1]);
			}else if("invoice".equals(type)){// 发票邮件
				String sql = "SELECT f_sys_mail_generate('type=invoice;id="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS eamilcontent;";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String eamilcontent = StrUtils.getMapVal(map, "eamilcontent");
				
				this.selectedRowData.setAddressee(eamilcontent.split("-\\.-\\.-")[1]);
				this.selectedRowData.setSubject(eamilcontent.split("-\\.-\\.-")[2]);
				this.selectedRowData.setContent(eamilcontent.split("-\\.-\\.-")[3]+(StrUtils.isNull(selectedRowData.getContent())?"":selectedRowData.getContent()));
			}
			this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
			if(jobid!=null&&jobid>0){
				String sql = "SELECT email1 FROM sys_corporation x,fina_jobs y WHERE y.customerid = x.id AND x.isdelete = FALSE AND y.isdelete = FALSE AND y.id = "+jobid;
				List<Map> lsit = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
				if(lsit != null && lsit.size() > 0){
					//String email1 = lsit.size()>0&&lsit.get(0).get("email1")!=null?lsit.get(0).get("email1").toString():"";
					//customEmail1 = email1!=null&&!email1.equals("")?email1:"";
					customEmail1 = StrUtils.getMapVal(lsit.get(0), "email1");
				}
				
				String sql2 = "SELECT x.email1 FROM sys_corporation x,fina_jobs y,bus_shipping z WHERE z.agentid = x.id AND z.jobid = y.id AND x.isdelete = " +
						"FALSE AND y.isdelete = FALSE AND z.isdelete = FALSE AND y.id = "+jobid;
				List<Map> lsit2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql2);
				if(lsit2 != null && lsit2.size() > 0){
					//String email12 = lsit2.size()>0?lsit2.get(0).get("email1").toString():"";
					//agentEmail1 = email12!=null&&!email12.equals("")?email12:"";
					agentEmail1 = StrUtils.getMapVal(lsit2.get(0), "email1");
				}
			}
			MLType mlType = AppUtils.getUserSession().getMlType();
			if("en".equals(mlType.toString())){
				Browser.execClientScript("changelang();");
			}
			if(!StrUtils.isNull(AppUtils.getReqParam("addressee"))){
				selectedRowData.setAddressee(AppUtils.getReqParam("addressee"));
			}
		}
	}
	
	@Action
	protected void saveAction() {
		if(StrUtils.isNull(this.selectedRowData.getSender())){
			String sql = "SELECT val FROM sys_configuser WHERE key = 'email_pop3_account' AND userid  =  " + AppUtils.getUserSession().getUserid();
			try {
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				this.selectedRowData.setSender(map.get("val").toString());
			} catch (Exception e) {
				alert("无法获取您的邮箱地址,请查看是否设置邮箱!");
				e.printStackTrace();
				return ;
			}
		}
		
		String content = AppUtils.getReqParam("editor1");
		String addressee = AppUtils.getReqParam("addressee");
		String copys = AppUtils.getReqParam("copys");
		String subject = AppUtils.getReqParam("subject");
		
		this.selectedRowData.setId(this.mailid);
		this.selectedRowData.setContent(content);
		this.selectedRowData.setAddressee(addressee);
		this.selectedRowData.setCopys(copys);
		this.selectedRowData.setSubject(subject);
		this.selectedRowData.setEmailtype("S");
		this.selectedRowData.setIssent(false);
		this.selectedRowData.setIsautosend(false);
		try {
			this.serviceContext.sysEmailService.saveData(selectedRowData);
			this.mailid = this.selectedRowData.getId();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Action
	private void sendAction(){
		
		String sql = "SELECT val FROM sys_configuser WHERE key = 'email_pop3_account' AND userid  =  " + AppUtils.getUserSession().getUserid();
		try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.selectedRowData.setSender(map.get("val").toString());
		} catch (Exception e) {
			alert("无法获取您的邮箱地址,请查看是否设置邮箱!");
			e.printStackTrace();
			return ;
		}
		
		//先保存
		String content = AppUtils.getReqParam("editor1");
		String addressee = AppUtils.getReqParam("addressee");
		String copys = AppUtils.getReqParam("copys");
		String subject = AppUtils.getReqParam("subject");
		this.selectedRowData.setId(this.mailid);
		this.selectedRowData.setContent(content);
		this.selectedRowData.setAddressee(addressee);
		this.selectedRowData.setCopys(copys);
		this.selectedRowData.setSubject(subject);
		this.selectedRowData.setEmailtype("S");
		this.selectedRowData.setIssent(false);
		this.selectedRowData.setIsautosend(false);
		this.serviceContext.sysEmailService.saveData(selectedRowData);
		this.mailid = this.selectedRowData.getId();
		//再发送
		try { 
			Map<String,String> attachments = null;// 附件数组
			// 发送前查看是否有附件
			List<SysAttachment> sa = serviceContext.sysAttachmentService.sysAttachmentDao.findAllByClauseWhere("linkid = " + mailid);
			if(sa != null && sa.size() > 0) {
				attachments = new HashMap<String, String>();
				for(SysAttachment att : sa) {
					// 拼成完整路径(包含文件名)
					String filePath = att.getFilepath();
					if(StrUtils.isNull(filePath)){//特殊处理，工作单导入的附件，这个标记绝对路径保存，发送邮件的时候直接用这个路径带文件名发送文件
						filePath = AppUtils.getAttachFilePath() + att.getId() + att.getFilename();
					}
					attachments.put(att.getFilename(), filePath);
				}
			}
			this.serviceContext.sysEmailService.sendEmailHtml(mailid,attachments);
			MessageUtils.alert("OK!");
		} catch (javax.mail.SendFailedException e){
			MessageUtils.alert("501 Bad address syntax,please check! 配置错误,请检查邮箱配置!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void mgrAttachment() {
		if(this.mailid == -1L || this.mailid == null) {
			MessageUtils.alert("Please save first!");
			return;
		}
		this.attachmentWindow.show();
		attachmentIframe.load(AppUtils.getContextPath() + "/pages/module/common/attachment.xhtml?src=email&linkid=" + this.mailid);
	}
	
	@Action
	public void intoJobsEmail(){
		index = "in";
		qrySql = " AND linkid ="+jobid;
		this.emailGrid.reload();
		jobemailtemplateWindow.show();
		//serviceContext.sysEmailService.intoJobsEmail(jobid, this.selectedRowData.getId());
	}
	
	@Action
	public void delJobsEmail(){
		index = "out";
		qrySql = " AND linkid ="+this.selectedRowData.getId()
		+" AND EXISTS(SELECT 1 FROM sys_attachment WHERE isdelete = FALSE AND linkid = "+jobid +
		"AND filename = t.filename AND contenttype = t.contenttype AND filesize = t.filesize) ";
		this.emailGrid.reload();
		jobemailtemplateWindow.show();
//		serviceContext.sysEmailService.delJobsEmail(jobid, this.selectedRowData.getId());
	}
	
	@Bind
	public UIWindow emailtemplateWindow;
	
	@Bind
	public UIWindow jobemailtemplateWindow;
	
	@Action
	public void gettemplate(){
		this.emailtemplateWindow.show();
	}
	
	@Action
	public void refreshTemplate(){
		refresh();
	}
	
	
	@Action
	public void imputtemp() {
		try {
			Long tempid = getGridSelectId();
			String content = "";
			String sql0 = "SELECT * FROM sys_email_template WHERE id= " + tempid;
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql0);
			String subject = String.valueOf(map.get("namec"));
			if ("入货通知青岛".equals(subject)) {
				Map qingdaoDataMap = getEmailqingdaoData();
				String customernamec = String.valueOf(qingdaoDataMap.get("customernamec")).replace("null", "");
				String usernamec = String.valueOf(qingdaoDataMap.get("usernamec")).replace("null", "");
				String tel = String.valueOf(qingdaoDataMap.get("tel")).replace("null", "");
				String email = String.valueOf(qingdaoDataMap.get("email")).replace("null", "");
				String pol = String.valueOf(qingdaoDataMap.get("pol")).replace("null", "");
				String pod = String.valueOf(qingdaoDataMap.get("pod")).replace("null", "");
				String vessel = String.valueOf(qingdaoDataMap.get("vessel")).replace("null", "");
				String voyage = String.valueOf(qingdaoDataMap.get("voyage")).replace("null", "");
				String sono = String.valueOf(qingdaoDataMap.get("sono")).replace("null", "");
				String cntdescnew = String.valueOf(qingdaoDataMap.get("cntdescnew")).replace("null", "");
				String sidate = String.valueOf(qingdaoDataMap.get("sidate")).replace("null", "");
				String clstime = String.valueOf(qingdaoDataMap.get("clstime")).replace("null", "");
				String etd = String.valueOf(qingdaoDataMap.get("etd")).replace("null", "");
				String shipagent = String.valueOf(qingdaoDataMap.get("shipagent")).replace("null", "");
				String claim_truck = String.valueOf(qingdaoDataMap.get("claim_truck")).replace("null", "");
				String remark2 = String.valueOf(qingdaoDataMap.get("remark2")).replace("null", "");
				String mblno = String.valueOf(qingdaoDataMap.get("mblno")).replace("null", "");
				String carriercode = String.valueOf(qingdaoDataMap.get("carriercode")).replace("null", "");
				content = String.valueOf(map.get("content"));
				content = content.replace("${customernamec}", customernamec);
				content = content.replace("${usernamec}", usernamec);
				content = content.replace("${tel}", tel);
				content = content.replace("${email}", email);
				content = content.replace("${pol}", pol);
				content = content.replace("${pod}", pod);
				content = content.replace("${vessel}", vessel);
				content = content.replace("${voyage}", voyage);
				content = content.replace("${sono}", sono);
				content = content.replace("${cntdescnew}", cntdescnew);
				content = content.replace("${sidate}", sidate);
				content = content.replace("${clstime}", clstime);
				content = content.replace("${etd}", etd);
				content = content.replace("${shipagent}", shipagent);
				content = content.replace("${claim_truck}", claim_truck);
				content = content.replace("${remark2}", remark2);

				// 入货通知  MBLNO ETD 船公司 箱型箱量 目的港
				selectedRowData.setSubject("入货通知_" + mblno + "_" + etd + "_" + carriercode + "_" + cntdescnew + "_" + pod);
				this.update.markUpdate(UpdateLevel.Data, "subject");
			} else {
				String sqlsub = "SELECT f_email_template_subject('tempid=" + tempid + ";jobid=" + jobid + ";type=" + type + ";userid=" + AppUtils.getUserSession().getUserid() + "') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
				String sub = sm.get("rets").toString().replaceAll("\r|\n", "");
				this.selectedRowData.setSubject(sub);
				this.update.markUpdate(UpdateLevel.Data, "subject");
				String sql = "SELECT f_email_template_generate('tempid=" + tempid + ";jobid=" + jobid + ";type=" + type + ";userid=" + AppUtils.getUserSession().getUserid() + "') AS retg;";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());

				String sqlconton = "SELECT f_fina_jobs_cntdesc('jobid=" + jobid + ";') AS coton;";
				Map mcon = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlconton.toString());
				String conton = mcon.get("coton").toString().replace("'", "");

				BusShipping bs = new BusShipping();
				if ("D".equals(type)) {
					bs = serviceContext.busShippingMgrService.findByjobId(jobid);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				String ret = null;
				content = m.get("retg").toString();
				if (bs.getSidate() != null) {
					ret = getwd(sdf.format(bs.getSidate()).toString());
					content = content.replace("$箱型箱量$", conton);
				} else {
					content = content.replace("$箱型箱量$", conton);
				}
				if ("星期6".equals(ret)) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(bs.getSidate());
					calendar.add(calendar.DATE, -1);
					calendar.add(Calendar.HOUR, -3);
					calendar.getTime();
					content = content.replace("$格式为日期 24小时制时间，时间截点比提箱单上截舱单时间自动提前3小时，遇六日及节假日时间结点需提前到工作日$", sdf.format(calendar.getTime()));
				} else if ("星期7".equals(ret)) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(bs.getSidate());
					calendar.add(calendar.DATE, -2);
					calendar.add(Calendar.HOUR, -3);
					calendar.getTime();
					content = content.replace("$格式为日期 24小时制时间，时间截点比提箱单上截舱单时间自动提前3小时，遇六日及节假日时间结点需提前到工作日$", sdf.format(calendar.getTime()));
				} else {
					Calendar calendar = new GregorianCalendar();
					if (bs.getSidate() != null) {
						calendar.setTime(bs.getSidate());
						calendar.add(Calendar.HOUR, -3);
						calendar.getTime();
						content = content.replace("$格式为日期 24小时制时间，时间截点比提箱单上截舱单时间自动提前3小时，遇六日及节假日时间结点需提前到工作日$", sdf.format(calendar.getTime()));
					}
				}
			}

			emailTemplateContent = content.replaceAll("\r|\n", "");
			this.update.markUpdate(true, UpdateLevel.Data, "emailTemplateContent");
			//System.out.println("con--->" +con);
			//Browser.execClientScript("editor1.setValue('"+con.replaceAll("\r|\n", "")+"');");
			Browser.execClientScript("refreshEmailContent();");
			//this.emailtemplateWindow.close();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("委托信息不全，请先完善委托信息！");
		}
	}
	
	@Bind
	public String emailTemplateContent = "";
	
	@SaveState
	private String qrySql = "\nAND FALSE";
	
	@SaveState
	private String index = "";//标记是工作单导入还是清除
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid emailGrid;
	
	@Bind(id = "emailGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return this.getJobEmailDataProvider();
	}
	
	/**
	 * 工作单附件导入和清除
	 * @return
	 */
	public GridDataProvider getJobEmailDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n	 *" +
					"\n	,(SELECT x.name FROM sys_role x where x.id = t.roleid and x.isdelete = false AND x.roletype = 'F' limit 1) AS rolegroup" +
					"\n	,COALESCE((SELECT x.namec FROM sys_user x where x.code = t.inputer and x.isdelete = false limit 1),t.inputer) AS inputername " +
					"\nFROM sys_attachment t WHERE isdelete = FALSE "+
					 qrySql +
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String countSql = 
					"SELECT COUNT(*) AS counts FROM sys_attachment t WHERE isdelete = FALSE"+
					 qrySql +
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	@Action
	public void setEmail(){
		String[] ids = this.emailGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		try {
			if(index.equals("in")){
				serviceContext.sysEmailService.intoJobsEmail(ids, this.selectedRowData.getId());
			}else if(index.equals("out")){
				serviceContext.sysEmailService.delJobsEmail(ids, this.selectedRowData.getId());
			}
			jobemailtemplateWindow.close();
			Browser.execClientScript("showAttachmentFile('"+this.mailid+"')");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void delAttachment(){
		String[] ids = this.emailGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}		
		//serviceContext.sysEmailService.delJobsEmail(ids, this.selectedRowData.getId());
		Browser.execClientScript("showAttachmentFile('"+this.mailid+"')");
		
		
	}
	
	 public String getwd(String str) throws Exception {  
		 	String wday = null;
	        Calendar calendar = Calendar.getInstance();  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
	        calendar.setTime(sdf.parse(str));  
	        int i =calendar.get(Calendar.DAY_OF_WEEK);  
	        if(i == 1){  
	        	wday = "星期7";  
	        }else{  
	        	wday =  "星期"+(i-1);  
	        }
			return wday;  
	    }  
	 
	@Action
	public void addEmail(){
		if(jobid!=null&&jobid>0){
    		StringBuffer fileUrl = new StringBuffer();
    		//fileUrl.append(System.getProperty("java.io.tmpdir"));
    		fileUrl.append("JobsList");
    		fileUrl.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    		fileUrl.append("_");
    		fileUrl.append(AppUtils.getUserSession().getUsercode());
    		fileUrl.append(".xls");
    		File file = new File(fileUrl.toString());
    		SysAttachment sysAttachment = new SysAttachment();
    		sysAttachment.setLinkid(this.mailid);
    		sysAttachment.setContenttype("xls");
    		sysAttachment.setFilename(fileUrl.toString());
    		serviceContext.attachmentService.saveData(sysAttachment);
    		try {
    			StringBuffer sbsql = new StringBuffer();
    	    	sbsql.append("\n SELECT b.cnortitle");
    	    	sbsql.append("\n 	,b.cneetitle");
    	    	sbsql.append("\n 	,b.notifytitle");
    	    	sbsql.append("\n 	,b.claim_bill");
    	    	sbsql.append("\n	,b.vessel");
    	    	sbsql.append("\n 	,b.voyage");
    	    	sbsql.append("\n	,b.pol");
    	    	sbsql.append("\n 	,b.pdd");
    	    	sbsql.append("\n 	,b.destination");
    	    	sbsql.append("\n 	,b.marksno");
    	    	sbsql.append("\n 	,b.piece");
    	    	sbsql.append("\n 	,b.goodsdesc");
    	    	sbsql.append("\n 	,b.grswgt");
    	    	sbsql.append("\n	,b.cbm");
    	    	sbsql.append("\n	,(select string_agg(cntno,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS cntno ");
    	    	sbsql.append("\n	,(select string_agg(sealno,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS sealno ");
    	    	sbsql.append("\n	,(select string_agg(piece::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS piecee ");
    	    	sbsql.append("\n	,(select string_agg(grswgt::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS grswgtc ");
    	    	sbsql.append("\n	,(select string_agg(cbm::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS cbmc ");
    	    	sbsql.append("\n	,(select string_agg(vgm::TEXT,',') from bus_ship_container where isdelete = FALSE AND jobid = b.jobid) AS vgm ");
    	    	sbsql.append("\n FROM bus_shipping b");
    	    	sbsql.append("\n WHERE");
    	    	sbsql.append("\n b.isdelete = FALSE");
    	    	sbsql.append("\n AND b.jobid = "+ jobid);
    	    	Map map = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
    	    	//导出样单,指定的模板，
    	    	String exportFileName = "Jobslist.xls";
    	    	
    	    	//模版所在路径
    	    	String fromFileUrl = AppUtils.getHttpServletRequest().getSession().getServletContext().getRealPath("") + File.separator +
    	    			"upload" + File.separator +
    	    			"ship" + File.separator +
    	    			exportFileName;
				if(!ReadExcel.importJobsListForEmail(new File(fromFileUrl),file, map,sysAttachment.getId()+fileUrl.toString())){
					return;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
    	}else{
    		MessageUtils.alert("请先保存工作单!");
    		return;
    	}
		
	}
	
	@Bind
	public UIDataGrid gridHisEmail;
	
	@Bind
	public UIDataGrid gridAdress;
	
	@Bind
	public UIWindow addresslistWindow;
	
	@Bind
	public UIWindow hisEmailWindow;
	
	@Action
	public void showAddresslist() {
		addresslistWindow.show();
		this.gridAdress.reload();
	}

	@Action
	public void showHisEmail() {
		hisEmailWindow.show();
		this.gridHisEmail.reload();
	}
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapHis = new HashMap<String, Object>();
	
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
	
	@Bind(id = "gridHisEmail", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.sysmgr.mail.emailsendeditBean.gridHisEmail.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapHis), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.sysmgr.mail.emailsendeditBean.gridHisEmail.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapHis));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
		this.gridAdress.reload();
	}
	
	@SaveState
	@Bind
	public String queryStr;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapAdrr = new HashMap<String, Object>();
	
	public Map getQryClauseWhere3(Map<String, Object> queryMap){
		Map m = super.getQryClauseWhere(queryMap);
		String filter ="\nAND(EXISTS"
			+"\n	(SELECT" 
			+"\n		1 "
			+"\n	FROM sys_custlib x , sys_custlib_user y  "
			+"\n	WHERE y.custlibid = x.id  "
			+"\n		AND y.userid = "+AppUtils.getUserSession().getUserid()
			+"\n		AND x.libtype = 'S'  "
			//+"\n		AND (x.userid = g.salesid  OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linktype = 'C' AND linkid = g.id AND userid = x.userid AND roletype = 'S')))"
			+"\n		AND x.userid = ANY(SELECT g.salesid UNION(SELECT zzz.userid FROM sys_user_assign zzz WHERE zzz.linktype = 'C' AND zzz.linkid = g.id AND roletype = 'S')))"
			+"\nOR EXISTS"
			+"\n	(SELECT" 
			+"\n		1 "
			+"\n	FROM sys_custlib x , sys_custlib_role y " 
			+"\n	WHERE y.custlibid = x.id  "
			+"\n		AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
			+"\n		AND x.libtype = 'S'  "
			//+"\n		AND (x.userid = g.salesid  OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linktype = 'C' AND linkid = g.id AND userid = x.userid AND roletype = 'S'))))"
			+"\n		AND x.userid = ANY(SELECT g.salesid UNION(SELECT zzz.userid FROM sys_user_assign zzz WHERE zzz.linktype = 'C' AND zzz.linkid = g.id AND roletype = 'S'))))"
			;
			m.put("filter", filter);
		
		queryStr = StrUtils.getSqlFormat(queryStr);	
		String qryFilter = "\nAND (code ILIKE '%"+queryStr+"%' "+
			"\nOR namec ILIKE '%"+queryStr+"%' "+
			"\nOR customerabbr ILIKE '%"+queryStr+"%' "+
			"\nOR jobtitle ILIKE '%"+queryStr+"%' "+
			"\nOR email1 ILIKE '%"+queryStr+"%' "+
			"\nOR email2 ILIKE '%"+queryStr+"%' "+
			"\n)";
		if(StrUtils.isNull(queryStr)){
			qryFilter = "";
		}
			
		m.put("qryFilter", qryFilter);
			
		m.put("limit", limitsEmaile+"");
		m.put("start", startsEmaile+"");
		return m;
	}
	
	@SaveState
	public int startsEmaile=0;
	
	@SaveState
    public int limitsEmaile=100;
	
	@Bind(id = "gridAdress", attribute = "dataProvider")
	protected GridDataProvider getAridAdressDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				startsEmaile = start;
				limitsEmaile = limit;
				String sqlId = "pages.sysmgr.addresslist.addresslistBean.grid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere3(qryMapAdrr))
						.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.sysmgr.addresslist.addresslistBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere3(qryMapAdrr));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
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
		sql.append("SELECT email1 FROM _addresslist WHERE id =ANY(ARRAY["+StrUtils.array2List(ids)+"])");
		sql.append("\nUNION ALL");
		sql.append("\nSELECT email1 FROM sys_corporation WHERE id =ANY(ARRAY["+StrUtils.array2List(ids)+"])");
		List<String> emailList = null;
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
	public void addAddressee() {
		String[] ids = this.gridAdress.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = getEmailFromDB(ids);

		this.selectedRowData.setAddressee(StrUtils.isNull(selectedRowData.getAddressee()) ? emails
				: (selectedRowData.getAddressee() + ";" + emails));
		this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
	}

	@Action
	public void addCC() {
		String[] ids = this.gridAdress.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = getEmailFromDB(ids);

		selectedRowData.setCopys( StrUtils.isNull(selectedRowData.getCopys()) ? emails
				: (selectedRowData.getCopys() + ";" + emails));
		this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
	}

	@Action
	public void addHisAddressee() {
		String[] ids = this.gridHisEmail.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = geHistEmailFromDB(ids);

		selectedRowData.setAddressee(StrUtils.isNull(selectedRowData.getAddressee()) ? emails
				: (selectedRowData.getAddressee() + ";" + emails));
		this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
	}

	@Action
	public void addHisCC() {
		String[] ids = this.gridHisEmail.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}

		String emails = geHistCopysFromDB(ids);

		this.selectedRowData.setCopys(StrUtils.isNull(selectedRowData.getCopys()) ? emails
				: (selectedRowData.getCopys() + ";" + emails));
		this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
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

	@Override
	public void grid_ondblclick() {
		this.imputtemp();
	}

	@Bind
	public UIIFrame emailFastextIframe;
	
	@Action
	public void showEmailFastext() {
		emailFastextIframe.load(AppUtils.getContextPath() + "/pages/sysmgr/mail/emailfastext.xhtml");
	}


	@Action
	public void intoJobsEmailqingdao() {
		try {
			Map map = getEmailqingdaoData();
			String customernamec = String.valueOf(map.get("customernamec")).replace("null", "");
			String usernamec = String.valueOf(map.get("usernamec")).replace("null", "");
			String tel = String.valueOf(map.get("tel")).replace("null", "");
			String email = String.valueOf(map.get("email")).replace("null", "");
			String pol = String.valueOf(map.get("pol")).replace("null", "");
			String pod = String.valueOf(map.get("pod")).replace("null", "");
			String vessel = String.valueOf(map.get("vessel")).replace("null", "");
			String voyage = String.valueOf(map.get("voyage")).replace("null", "");
			String sono = String.valueOf(map.get("sono")).replace("null", "");
			String cntdescnew = String.valueOf(map.get("cntdescnew")).replace("null", "");
			String sidate = String.valueOf(map.get("sidate")).replace("null", "");
			String clstime = String.valueOf(map.get("clstime")).replace("null", "");
			String etd = String.valueOf(map.get("etd")).replace("null", "");
			String shipagent = String.valueOf(map.get("shipagent")).replace("null", "");
			String claim_truck = String.valueOf(map.get("claim_truck")).replace("null", "");
			String remark2 = String.valueOf(map.get("remark2")).replace("null", "");
			String mblno = String.valueOf(map.get("mblno")).replace("null", "");
			String carriercode = String.valueOf(map.get("carriercode")).replace("null", "");
			String nos = String.valueOf(map.get("nos")).replace("null", "");


			StringBuffer sb = new StringBuffer("intoJobsEmailqingdao开始");
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
			String templateLUrl = ProjectPath.getRootPath("com/scp/view/sysmgr/mail");
			sb.append(",templateLUrl为").append(templateLUrl);
			LogBean.insertLog(sb);
			configuration.setDirectoryForTemplateLoading(new File(templateLUrl));    //.xml 模板文件所在目录


			String filename = "入货通知_" + mblno + "_" + nos + ".doc";
			String filePath = AppUtils.getAttachFilePath() + filename;
			File outFile = new File(filePath);    // 输出文档路径及名称
			Template t = configuration.getTemplate("Incoming_Notice_moban2.xml", "utf-8");
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);

			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("customernamec", customernamec);
			dataMap.put("usernamec", usernamec);
			dataMap.put("tel", tel);
			dataMap.put("email", email);
			dataMap.put("pol", pol);
			dataMap.put("pod", pod);
			dataMap.put("vessel", vessel);
			dataMap.put("voyage", voyage);
			dataMap.put("sono", sono);
			dataMap.put("cntdescnew", cntdescnew);
			dataMap.put("sidate", sidate);
			dataMap.put("clstime", clstime);
			dataMap.put("etd", etd);
			dataMap.put("shipagent", shipagent);
			dataMap.put("claim_truck", claim_truck);
			dataMap.put("remark2", remark2);
			t.process(dataMap, out);
			out.close();

			String sqllength = "SELECT count(1) as attachmentlength  from sys_attachment WHERE linkid = " + mailid + " and filepath = '" + filePath + "';";
			Map sqllengthmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqllength);
			if ("0".equals(sqllengthmap.get("attachmentlength").toString())) {
				SysAttachment sysAttachment = new SysAttachment();
				sysAttachment.setLinkid(mailid);
				sysAttachment.setFilename(filename);
				sysAttachment.setFilepath(filePath);//特殊处理，附件文件已经存在，这个标记绝对路径保存，发送邮件的时候直接用这个路径带文件名发送文件
				sysAttachment.setFilesize(BigDecimal.valueOf(outFile.length()));
				serviceContext.sysAttachmentService.sysAttachmentDao.create(sysAttachment);
			}

			Browser.execClientScript("showAttachmentFile('" + this.mailid + "')");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	private Map getEmailqingdaoData() {
		String sql = "SELECT  \n" +
				"\tb.id,\n" +
				"\tb.hblno,\n" +
				"\t(select nos from fina_jobs fj where fj.id=b.jobid limit 1) AS nos,\n" +
				"\tb.billcount,\n" +
				"\tb.id AS shipid,\n" +
				"\tb.jobid,\n" +
				"\tb.cneeid,\n" +
				"\treplace(replace(replace(b.cneetitle, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS cneetitle,\n" +
				"\tb.cneename,\n" +
				"\tb.cnorid,\n" +
				"\treplace(replace(replace(b.cnortitle, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS cnortitle,\n" +
				"\tb.cnorname,\n" +
				"\tb.notifyid,\n" +
				"\treplace(replace(replace(b.notifytitle, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS notifytitle,\n" +
				"\tb.notifyname,\n" +
				"\tb.agenid,\n" +
				"\treplace(replace(replace(b.agentitle, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS agentitle,\n" +
				"\t\n" +
				"\treplace(replace(replace(b.cnortitlembl, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS cnortitlembl,\n" +
				"\treplace(replace(replace(b.cneetitlembl, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS cneetitlembl,\n" +
				"\treplace(replace(replace(b.notifytitlembl, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS notifytitlembl,\n" +
				"\t\n" +
				"\t\n" +
				"\tb.agenname,\n" +
				"\tb.pretrans,\n" +
				"\tb.poa,\n" +
				"\tb.hbltype,\n" +
				"\tb.carrierid,\n" +
				"\tb.vessel,\n" +
				"\tb.voyage,\n" +
				"\tb.polid,\n" +
				"\tb.pol,\n" +
				"\tb.podid,\n" +
				"\tb.pod,\n" +
				"\tb.pddid,\n" +
				"\tb.pdd,\n" +
				"\tto_char(b.atd, 'MON,DD,yyyy') AS atd,\n" +
				"\tto_char(b.etd, 'MON,DD,yyyy') AS etd,\n" +
				"\tb.piece,\n" +
				"\tb.grswgt,\n" +
				"\tb.cbm,\n" +
				"\tb.carryitem,\n" +
				"\tb.freightitem,\n" +
				"\tb.loaditem,\n" +
				"\tb.packer,\n" +
				"\tb.marksno,\n" +
				"\tb.goodsdesc,\n" +
				"\tb.totledesc,\n" +
				"\tb.corpid,\n" +
				"\tb.isdelete,\n" +
				"\tb.inputer,\n" +
				"\tb.inputtime,\n" +
				"\tb.updater,\n" +
				"\tb.updatetime,\n" +
				"\tb.mblno,\n" +
				"\tb.bltype,\n" +
				"\tb.destination,\n" +
				"\tb.signplace,\n" +
				"\ttrim(coalesce(b.remark2,'')) as remark2,\n" +
				"\tb.claim_truck as claim_truck,\n" +
				"\tto_char(b.cls,'MON,DD,yyyy hh24:mi') AS cls,\n" +
				"\tto_char(b.clstime,'MON,DD,yyyy hh24:mi') AS clstime,\n" +
				"\tto_char(b.sidate,'MON,DD,yyyy hh24:mi') AS sidate,\n" +
				"\t(select coalesce(su.mobilephone,su.tel1) from sys_user su where su.id ::text= " + AppUtils.getUserSession().getUserid() + " ::text limit 1) as  tel,\n" +
				"\t(select coalesce(su.email1,su.email2) from sys_user su where su.id ::text= " + AppUtils.getUserSession().getUserid() + " ::text limit 1) as email,\n" +
				"\t(select su.namec from sys_user su where su.id ::text= " + AppUtils.getUserSession().getUserid() + " ::text limit 1) as usernamec,\n" +
				"\tb.shipagent as shipagent,\n" +
				"\t\n" +
				"\t(SELECT namee FROM dat_filedata f WHERE b.freightitem = f.code AND f.isdelete = FALSE AND f.fkcode = 20)AS freightitemdesc,\n" +
				"\t(SELECT namee FROM dat_filedata f WHERE b.paymentitem = f.code AND f.isdelete = FALSE AND f.fkcode = 20)AS paymentitemdesc, \n" +
				"\t(SELECT UPPER(s.signplace)||' '||to_char(b.atd,'YYYY-mm-dd') FROM bus_shipping s WHERE b.id = s.id  AND  s.isdelete =FALSE)AS shippingdate,\n" +
				"\t(SELECT CASE WHEN b.isshowship THEN c.namee ELSE '' END FROM sys_corporation c WHERE b.carrierid =c.id ) AS carrierfullname,\t\t\t\t\n" +
				"\t(SELECT code FROM sys_corporation c WHERE b.carrierid =c.id ) AS carriercode,\t\t\t\t\n" +
				"\t(SELECT namec FROM sys_corporation c WHERE (select customerid from fina_jobs fj where fj.id=b.jobid limit 1) =c.id ) AS customernamec,\t\t\t\t\n" +
				"\t(b.piece||''||b.packer) AS piececount ,    --件数\n" +
				"\tb.isshowship,\n" +
				"\tb.sono AS sono,\n" +
				"\t(SELECT f_fina_jobs_cntdesc('jobid='::text||b.jobid)) AS cntdescnew,\n" +
				"\n" +
				"\n" +
				"\treplace(replace(replace(b.marksno, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS cntinfos,\n" +
				"\treplace(replace(replace(b.goodsdesc, CHR(10), ';'), '\\cx', ';'), '\\r', ';') AS goodsinfo,\n" +
				"\tb.payplace  AS plable,\n" +
				" (SELECT f_lists(DISTINCT cntno)FROM bus_ship_container WHERE jobid = b.jobid  and isdelete = false )AS cntno\n" +
				" ,( SELECT x.cntdesc FROM _fina_jobs_info x  WHERE (x.jobid = b.jobid)) AS cntdesc\n" +
				"FROM\n" +
				"\tbus_shipping b\n" +
				"WHERE\n" +
				"\tb.isdelete = FALSE\n" +
				"\tAND CAST(id AS VARCHAR) = (select id ::text from bus_shipping where jobid=" + jobid + " limit 1)";

		Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return map;
	}
}
