package com.scp.model.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.component.widget.fileupload.impl.UIFileUploadDialog;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.apache.commons.fileupload.FileItemIterator;
import org.operamasks.org.apache.commons.fileupload.FileItemStream;
import org.operamasks.org.apache.commons.fileupload.FileUploadException;
import org.operamasks.org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.operamasks.org.apache.commons.fileupload.util.Streams;

import com.scp.base.CommonComBoxBean;
import com.scp.model.sys.SysAttachment;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.common.attachmentBean", scope = ManagedBeanScope.REQUEST)
public class AttachmentBean extends GridFormView {

	@Bind
	@SaveState
	private Long dPkVal;
	
	@Bind
	@SaveState
	private String linkid;
	
	@Bind
	@SaveState
	private String refid;
	
	@Bind
	@SaveState
	private Long userId;
	
	@Bind
	@SaveState
	private String code;
	
	@Bind
	@SaveState
	private String src;

	@Bind
	@SaveState
	private String jobflag;

	@Bind
	@SaveState
	private String tixin;

//	@Bind
//	@SaveState
//	private String testUrl;
	

	@Bind
	@SaveState
	private float attachmentsize;

	@Bind
	public UIButton dtlAddMany;
	@Bind
	public UIButton dragAddPhone;
	@Bind
	public UIButton dtlAddPhone;
	@Bind
	public UIButton dtlDel;
	@Bind
	public UIButton refresh;
	@Bind
	public UIButton showGroup;
	@Bind
	public UIButton emailNotify;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.code = AppUtils.getReqParam("code");
			src = AppUtils.getReqParam("src");
			this.linkid = AppUtils.getReqParam("linkid");
			this.refid = AppUtils.getReqParam("refid");
			this.jobflag = AppUtils.getReqParam("jobflag");
			if(StrUtils.isNull(refid) || "null".equalsIgnoreCase(refid)){
				this.refid = "-100";
			}
//			this.qryMap.put("linkid$", linkid);
			userId = AppUtils.getUserSession().getUserid();
			this.update.markUpdate(UpdateLevel.Data,"linkid");
			this.update.markUpdate(UpdateLevel.Data,"linkid2");
			this.grid.reload();
//			String serverHttPort = AppUtils.getServerHttPort();
//			this.testUrl = serverHttPort+"/scp/reportJsp/upload.jsp?refid="+linkid ;
			String src =AppUtils.getReqParam("src").trim();
			if("MyCustomer".equals(src)){
				if(ConfigUtils.findSysCfgVal("customer_management")!=null&&ConfigUtils.findSysCfgVal("customer_management").toString().equals("Y")){
					dtlDel.setDisabled(true);
				}
			}
			if("Y".equals(ConfigUtils.findSysCfgVal("attachment_filterby_role"))){
				needChooseRole = "Y";
			}
			
			String str = ConfigUtils.findSysCfgVal("sys_attachment_size");
			if(StrUtils.isNull(str)){
				attachmentsize = 5L;
			}else{
				float size = Float.parseFloat(str);
				attachmentsize = size;
			}

			String flag = AppUtils.getReqParam("flag");
			if ("CustomereditBean".equals(flag)) {
				String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
				try {
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
						dtlAddMany.setDisabled(true);
						dragAddPhone.setDisabled(true);
						dtlAddPhone.setDisabled(true);
						dtlDel.setDisabled(true);
						refresh.setDisabled(true);
						showGroup.setDisabled(true);
						emailNotify.setDisabled(true);
					}
				} catch (Exception e) {
				}
			}


			if ("ship".equals(jobflag)) {
				String sql = "select\n" +
						"('未上传附件类型 '\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id )\n" +
						"\tUNION\t( SELECT bo.ID FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = 'SO')  THEN '' ELSE ' SO ' END)\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id )\n" +
						"\tUNION\t( SELECT bo.ID FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = '托运单')  THEN '' ELSE ' 托运单 ' END)\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id )\n" +
						"\tUNION\t( SELECT bo.ID FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = '提单确认件')  THEN '' ELSE ' 提单确认件 ' END)\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id )\n" +
						"\tUNION\t( SELECT bo.ID FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = '提单正本扫描')  THEN '' ELSE ' 提单正本扫描 ' END)\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = '供应商账单')  THEN '' ELSE ' 供应商账单 ' END)\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = '账单确认件')  THEN '' ELSE ' 账单确认件 ' END)\n" +
						"|| (CASE WHEN EXISTS ( SELECT 1 FROM sys_attachment sa WHERE sa.isdelete = FALSE and linkid = ANY\n" +
						"\t(SELECT\tfj.id UNION ( SELECT y.ID FROM bpm_processinstance y WHERE y.refid = fj.id::text ) UNION ALL SELECT - 100 UNION ALL SELECT Concat(fj.id,100)::BIGINT\n" +
						"\tUNION\t( SELECT b.ID FROM bus_ship_booking b WHERE b.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id )\n" +
						"\tUNION\t( SELECT bo.ID FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.jobid = fj.id ))\n" +
						"\tand (SELECT x.NAME FROM sys_role x \tWHERE x.ID = sa.roleid AND x.isdelete = FALSE AND x.roletype = 'F' LIMIT 1 ) = '保函')  THEN '' ELSE ' 保函 ' END)\n" +
						") as tixing " +
						"from fina_jobs fj\n" +
						"where fj.isdelete = false\n" +
						"and id= "+linkid+"";
				try {
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					String tixing = StrUtils.getMapVal(m, "tixing");
					if (tixing.length() > 8) {
						tixin = StrUtils.getMapVal(m, "tixing");
					} else if (tixing.length() == 8) {
						tixin = "已上传全部类型附件";
					}
				} catch (Exception e) {
				}
			}
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap){
		Map m = super.getQryClauseWhere(queryMap);
//		String findrefid = "(select p.refid from t_ff_process_ref y,t_ff_rt_processinstance p where y.refid = "+linkid+" AND y.processinstance_id = p.id)";
//		m.put("linkrefid", findrefid);
////		
//		String frefid = "(select p.refid from t_ff_process_ref y,t_ff_rt_processinstance p where y.refid = "+refid+" AND y.processinstance_id = p.id)";
//		m.put("frefid", frefid);
		
		
		m.put("linkid", linkid);
		m.put("linkid2", linkid+"100");
		m.put("refid", refid);
		m.put("refid2", refid+"100");
		if("payapply".equals(this.code)){
			//将当前付款申请下面工作单里面对应的附件也显示出来
			//String bpmlinkid = "\nUNION ALL (select y.jobid from _fina_rpreqdtl y where y.rpreqid = '"+linkid+"' )";
			String bpmlinkid = "\nUNION (SELECT a.jobid from fina_rpreqdtl d, fina_arap a WHERE d.rpreqid = '"+linkid+"' and  d.arapid = a.id)";
			m.put("bpmlinkid", bpmlinkid);
		}else if("invoice".equals(this.code)){
			String bpmlinkid = "\nUNION (SELECT a.jobid from fina_arap a WHERE a.invoiceid = '"+linkid+"' and a.isdelete = false)";
			m.put("bpmlinkid", bpmlinkid);
		}else{
			//工作单显示流程中上传的附件
			String bpmlinkid = "\nUNION (select y.id from bpm_processinstance y where y.refid = '"+linkid+"' )";
			m.put("bpmlinkid", bpmlinkid);
		}
		
		//neo 20190103 附件按组权限过滤
		if("Y".equals(ConfigUtils.findSysCfgVal("attachment_filterby_role"))){
			String filter = 
					"\nAND (  " +
					"\n COALESCE(t.roleid,0) = 0  " +
					"\n OR EXISTS(SELECT 1 FROM sys_role r , sys_userinrole u where r.id = t.roleid AND r.isdelete = false AND r.id = u.roleid and u.userid = "+AppUtils.getUserSession().getUserid()+")" +
					"\n OR EXISTS(SELECT 1 FROM sys_role r , sys_userinrole u where r.name = '分公司管理员' AND r.isdelete = false AND r.id = u.roleid and u.userid = "+AppUtils.getUserSession().getUserid()+")" +
					"\n OR inputer = '"+AppUtils.getUserSession().getUsercode()+"'"+
					"\n )";
			m.put("filter", filter);
			
			m.put("shippersql", "\nAND 1=1");
		}
		
		//neo 20210516 工作单附件显示付款申请中水单
		if("Y".equals(ConfigUtils.findSysCfgVal("jobs_show_bankbill_attachment"))){
			String bankbill = "\nUNION ALL (SELECT DISTINCT x.id from fina_rpreq x, sys_attachment y, fina_arap z , fina_rpreqdtl zz WHERE x.id = y.linkid AND zz.isdelete = FALSE AND zz.amtreq <> 0 AND x.isdelete = FALSE AND z.id = zz.arapid AND zz.rpreqid = x.id AND z.jobid = "+linkid+")";
			m.put("bankbill", bankbill);
		}
		
		
		return m;
	}
	

	/*@Override
	public void grid_ondblclick(){
		this.dPkVal = this.getGridSelectId();
		this.update.markUpdate(UpdateLevel.Data,"dPkVal");
    	Browser.execClientScript("downloadBtn.fireEvent('submit');");
	}*/
	
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String contentType;
	
	@Bind
	@SaveState
	public Long selectid;
    
    @Action
	public void download(){
//    	this.dPkVal = this.getGridSelectId();
    	this.dPkVal = selectid;
    	if(dPkVal==-1l){
    		MessageUtils.alert("please choose one");
    		return;
    	}else{
	    	SysAttachment sysAttachment = this.serviceContext.sysAttachmentService.sysAttachmentDao.findById(dPkVal);
			this.fileName = sysAttachment.getFilename();
			this.contentType = sysAttachment.getContenttype();
	    	this.update.markUpdate(UpdateLevel.Data,"dPkVal");
    	}
	}
    
    
	@Bind(id="roleGroup")
    public List<SelectItem> getRoleGroup() {
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.code||'/'||COALESCE(name,'')","d.code||'/'||COALESCE(name,'')","sys_role d","WHERE d.isdelete = false AND d.roletype = 'F'","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Action
	public void emailNotify(){
		try {
			if(StrUtils.isNull(roleId)){
				MessageUtils.alert("Please choose group first!");
				return;
			}
			String querySql = "SELECT f_createmail_attachment('jobid="+linkid+";userid="+AppUtils.getUserSession().getUserid()+";roleid="+roleId+"');";
			this.serviceContext.attachmentService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Bind
	@SaveState
	public String roleId;
	
	
	@Bind
	@SaveState
	public String needChooseRole = "N";
    
    @Action
	public void saveRoleGroup(){
    	String[] ids = this.grid.getSelectedIds();
    	if(ids==null||ids.length<1){
    		alert("请至少选择一行");
    		return;
    	}
    	StringBuilder stringBuilder = new StringBuilder();
    	for (String id : ids) {
    		stringBuilder.append("\nUPDATE sys_attachment SET roleid = "+roleId+ " WHERE id = "+id+";");
		}
    	try {
			if(stringBuilder.toString() != ""){
				this.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
				this.refresh();
				this.alert("OK");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
    }
    
    
    
    @Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
    	try {
//    		//分布式文件存储情况下，用soa方式提取
//    		if("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_file_soa"))){;
//    			FileService fileService = (FileService)AppUtils.getBeanFromSpringIoc("fileService");
//    			String csno = ConfigUtils.findSysCfgVal("CSNO");
//    			byte[] datas = fileService.getFile(csno, dPkVal + fileName);
//    			return new ByteArrayInputStream(datas); 
//    		//传统方式下载文件
//    		}else{
    			try {
    				return this.serviceContext.attachmentService.readFile(dPkVal);
    			} catch (Exception e) {
    				MessageUtils.showException(e);
    				return null;
    			}
//    		}
		} catch (Exception soaExce) {
			try {
				return this.serviceContext.attachmentService.readFile(dPkVal);
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
		}
    }

	/**
	 * 附件上传事件
	 */
    @Action
	public void dtlAdd() {
		filesUpload.show();
	}
	
	/***********************文件上传处理********************/
	@Bind
	private UIFileUploadDialog filesUpload;
	
	/**
	 * 上传文件处理动作 
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void processFileUpload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			upload(request, response, filesUpload, this.linkid);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 文件上传
	 * @param request
	 * @param response
	 * @param filesUpload 上传组件
	 * @param linkid 上传业务表主键
	 * @throws IOException
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	public void upload(HttpServletRequest request,
			HttpServletResponse response, UIFileUploadDialog filesUpload,
			String linkid) throws IOException, Exception,
			FileNotFoundException {
		File file2local = null;
		ServletFileUpload upload = new ServletFileUpload();
		InputStream stream = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		SysAttachment sysAttachment = null;
		
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				if (filesUpload.getFileSizeMax() != null) {
					upload.setSizeMax(filesUpload.getFileSizeMax());
				}
				FileItemIterator iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();

					stream = item.openStream();
					if (!item.isFormField()) {
						// 上传文件
						File file = new File(item.getName());
						String orFileName = file.getName();

						String originalFileName = orFileName.toLowerCase().replaceAll(" |\"|#|%|&|(|)|,|/|:|;|<|=|>|@","");
						// 文件格式
						String suffix = "";
						if (originalFileName != null
								&& !originalFileName.trim().equals("")) {
							String[] strArr = originalFileName.split("\\.");
							if (strArr != null && strArr.length == 2) {
								suffix = strArr[1];
							}
						}
						
						//ApplicationUtils.debug("attach:" + Application.class.getResource("/").toURI());
						String attachPath = AppUtils.getAttachFilePath();
						
						//AttachFile(this , linkid , originalFileName , item.getContentType());
						sysAttachment = new SysAttachment();
						sysAttachment.setContenttype(item.getContentType());
						sysAttachment.setLinkid(Long.valueOf(linkid));
						sysAttachment.setFilename(originalFileName);
						
						this.serviceContext.attachmentService.saveData(sysAttachment);
						// 复制文件到指定路径
						originalFileName = sysAttachment.getId() + originalFileName;
						
						bis = new BufferedInputStream(stream);
						file2local = new File(attachPath + originalFileName);
						bos = new BufferedOutputStream(new FileOutputStream(file2local));
						long fileSize = Streams.copy(bis, bos, true);
						String attachmentsize = ConfigUtils.findSysCfgVal("sys_attachment_size");
						if(StrUtils.isNull(attachmentsize)){
							attachmentsize = "5";
						}
						float size = Float.parseFloat(attachmentsize)*1024*1024;
						if(fileSize > size){
							String arr[] = new String[1];
							arr[0]=String.valueOf(sysAttachment.getId());
							this.serviceContext.attachmentService.removeDates(arr, AppUtils.getUserSession().getUsercode());
							this.serviceContext.attachmentService.removeDate(sysAttachment.getId());
							throw new Exception("文件大小超出限制值["+attachmentsize+"MB]");
						}
//						this.getDaoProxy().refreshAttachFile(attachmentPkId, fileSize);
						sysAttachment.setFilesize(new BigDecimal(fileSize));
						if(!StrUtils.isNull(roleId)){
							sysAttachment.setRoleid(Long.valueOf(roleId));
						}
						this.serviceContext.attachmentService.saveData(sysAttachment);
					}
				}
				response.getWriter().write("{success:true}");
			}
		} catch (FileUploadException e) {
			response.getWriter().write(
					String.format("{success:false,message:'%s'}", e
							.getMessage()));
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
		}
		//SOA文件上传处理
//		if("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_file_soa"))){
//			FileService fileService = (FileService) AppUtils.getBeanFromSpringIoc("fileService");
//			String csno = ConfigUtils.findSysCfgVal("CSNO");
//			
//			FileInputStream inputStream = null;
//			try {
//				inputStream = new FileInputStream(file2local);
//				byte[] data = new byte[inputStream.available()];
//				inputStream.read(data);
//				inputStream.close();
//				fileService.addFile(csno, file2local.getName(), data);
//				sysAttachment.setIsoa(true);
//				sysAttachment.setSoaurl(fileService.getFileUrl(csno));
//				this.serviceContext.attachmentService.saveData(sysAttachment);
//				FileOperationUtil.delFile(file2local.getAbsolutePath());
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally{
//				if(inputStream != null)inputStream.close();
//			}
//		}
	}
	
	@Action
	public void dtlDel(){
		String[] ids = this.grid.getSelectedIds();
		//System.out.println("remake---->" +ramke);
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please chose one row first!");
			return;
		}else{
			String id = ids[0].toString();
			String ramke = this.serviceContext.attachmentService.getremake(id);
			if(code == "" || code == null){
				if("不可删除".equals(ramke)){
					MessageUtils.alert("流程中附件无法删除！");
					return;
				}else{
					try {
						this.serviceContext.attachmentService.removeDates(ids, AppUtils
								.getUserSession().getUsercode());
						MessageUtils.alert("OK");
						if("email".equals(this.src)){
							Browser.execClientScript("parent.delAttachment.submit();");
						}
					} catch (Exception e) {
						MessageUtils.showException(e);
					}
					this.grid.reload();
				}
			}else{
				try {
					this.serviceContext.attachmentService.removeDates(ids, AppUtils
							.getUserSession().getUsercode());
					MessageUtils.alert("OK");
					Browser.execClientScript("parent.delEmail.submit();");
				} catch (Exception e) {
					MessageUtils.showException(e);
				}
				this.grid.reload();
			}
		}
	}

	@SaveState
	public SysAttachment data = new SysAttachment();

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysAttachmentService.sysAttachmentDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.sysAttachmentService.saveData(data);
	}
	
	@Action
	public void compareFile(){
		String openUrl = "./attachmentcompare.xhtml?linkid="+linkid;
		AppUtils.openWindow(System.currentTimeMillis()+"", openUrl);
	}
}