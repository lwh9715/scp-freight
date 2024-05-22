package com.scp.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.org.apache.commons.fileupload.FileItemIterator;
import org.operamasks.org.apache.commons.fileupload.FileItemStream;
import org.operamasks.org.apache.commons.fileupload.FileUploadException;
import org.operamasks.org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.operamasks.org.apache.commons.fileupload.util.Streams;
import org.springframework.jdbc.core.JdbcTemplate;

import sun.misc.BASE64Encoder;

import com.google.gson.Gson;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysLogDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysReport;
import com.scp.model.sys.SysUser;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.ConfigUtils;
import com.scp.util.FileOperationUtil;
import com.scp.util.JSONUtil;
import com.scp.util.PDFUtil;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;

public class WebServerHandler {
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public JdbcTemplate jdbcTemplate;


	// 获取日志信息
	public String handle(String action, HttpServletRequest request, HttpServletResponse resp) {
		String result = "";
		String userid = request.getParameter("userid");
		String id = request.getParameter("id");//sys_attachment id
		if("uploadFile".equals(action)){
			String imgStr = request.getParameter("imgStr");
			String imgName;
			try {
				String imageName = getSignImgPath(request , userid);
				imgStr = imgStr.replace("data:image/png;base64,","");
				imgStr = imgStr.replace("base64,","");
				Base64.generateImage(imgStr, imageName);
				result = "OK";
			} catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("getFile".equals(action)){
			String imageName;
			try {
				imageName = getSignImgPath(request , userid);
				result = "data:image/png;base64,"+Base64.getImageStr(imageName);
			} catch (FileNotFoundException e){
				result = "ERROR";
			} catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("getFileImg".equals(action)){
			String imageName;
			try {
				imageName = getImgPath(request ,id);
				result = "data:image/png;base64,"+Base64.getImageStr(imageName);
			} catch (FileNotFoundException e){
				result = "ERROR FileNotFoundException";
			} catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("getFilePDF".equals(action)){
			String imageName;
			try {
				String imgName =URLDecoder.decode(id+getImgname(Long.parseLong(id!=null?id:"-1")));
				//System.out.println("imgName:"+imgName);
				return imgName;
			} catch (FileNotFoundException e){
				result = "ERROR";
			} catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("commonQuery".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String querySql = request.getParameter("querySql");
			System.out.println("querySql:"+querySql);
			querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM ("+querySql+") AS T";
			String ret = "";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			Map map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{}";
			}
			return ret;
		}else if("commonQueryByXml".equals(action)){
			return commonQueryByXml(request);
		}else if("saveOp2".equals(action)){
			return saveOp2(request);
		}else if("saveOp2".equals(action)){
			return saveOp2(request);
		}else if("getquoteamounttemp".equals(action)){
			return getquoteamounttemp(request);
		}else if("filedownload".equals(action)){
            return filedownload(request);
        }else if("getabbcodeinfo".equals(action)){
			return getabbcodeInfo(request);
		}else if("getFeeidata".equals(action)) {
			return queryGetFeeidata(request);
		}else if("getSalesArAmt".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String customerid = request.getParameter("id");
			String salesid = request.getParameter("salesid");
			String ret = "";
			
			String sqlId = "servlet.web.ff.salesArAmt.grid.page";
			Map parameter = new HashMap();
			parameter.put("customerid", customerid);
			parameter.put("salesid", salesid);
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, parameter);
			Map map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{\"label\": \"\"}";
			}
			return ret;
		}else if("getPouchlabel".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String jobid = request.getParameter("jobid");
			String ret = "";
			
			String sql = "SELECT (SELECT url FROM sys_attachment WHERE linkid = t.carrierid AND roleid = (SELECT id FROM sys_role d WHERE d.isdelete = false AND d.roletype = 'F' AND d.code = '099') AND isdelete = FALSE ORDER BY inputtime,updatetime DESC LIMIT 1) AS url" +
					", COALESCE(piece2,1) AS piece2, COALESCE(mawbno,'') AS mawbno, COALESCE(podcode,'') AS pod, COALESCE(polcode,'') AS pol, COALESCE(flightno1,'') AS flightno1, substring(COALESCE(flightdate1::TEXT,'')from 1 for 10) AS flightdate1, COALESCE(f_newline2(cneetitle),'') AS cneetitle from bus_air t WHERE jobid = "+jobid+" AND isdelete = FALSE";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			//Map map = list.get(0);
			if(map != null){
				ret = "{\"mawbno\":\""+map.get("mawbno")+"\",\"pod\":\""+map.get("pod")+"\",\"piece2\":\""+map.get("piece2")+"\",\"pol\":\""+map.get("pol")+"\",\"flightno1\":\""+map.get("flightno1")+"\",\"flightdate1\":\""+map.get("flightdate1").toString()+"\",\"cneetitle\":\""+map.get("cneetitle")+"\",\"url\":\""+map.get("url")+"\"}";
			}else {
				ret = "{}";
			}
			return ret;
		}else if("getBillAndPouchlabel".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String jobid = request.getParameter("jobid");
			String billid = request.getParameter("billid");
			String ret = "";
			
			String sql = "SELECT (SELECT url FROM sys_attachment WHERE linkid = t.carrierid AND roleid = (SELECT id FROM sys_role d WHERE d.isdelete = false AND d.roletype = 'F' AND d.code = '099') AND isdelete = FALSE ORDER BY inputtime,updatetime DESC LIMIT 1) AS url" +
					",COALESCE(t.piece2,1) AS piece2, COALESCE(t.mawbno,'') AS mawbno, COALESCE(bill.hawbno,'') AS hawbno, COALESCE(t.podcode,'') AS pod,COALESCE(t.polcode,'') AS pol, COALESCE(bill.piece::INTEGER,1) AS piecebill from bus_air t, bus_air_bill bill WHERE t.jobid = "+jobid+" AND t.jobid = bill.jobid AND bill.id = "+billid+" AND t.isdelete = FALSE AND bill.isdelete = FALSE";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			//Map map = list.get(0);
			if(map != null){
				ret = "{\"mawbno\":\""+map.get("mawbno")+"\",\"hawbno\":\""+map.get("hawbno")+"\",\"pod\":\""+map.get("pod")+"\",\"pol\":\""+map.get("pol")+"\",\"piece2\":\""+map.get("piece2")+"\",\"piecebill\":\""+map.get("piecebill")+"\",\"url\":\""+map.get("url")+"\"}";
			}else {
				ret = "{}";
			}
			return ret;
		}else if("getBill".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String jobid = request.getParameter("jobid");
			String ret = "";
			
			String sql = "SELECT id,hawbno FROM bus_air_bill WHERE jobid ="+jobid+" AND isdelete = FALSE";
			
			List<Map> maplist = daoIbatisTemplate.queryWithUserDefineSql(sql);
			
			//Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			//Map map = list.get(0);
			JSONArray jArray=JSONArray.fromObject(maplist);
			return jArray.toString();

			
		}else if("commonQueryByXmldash".equals(action)){

		}else if("commonQueryByXmldashbpm".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String ret = "";
			String sqlId = request.getParameter("sqlId");
			String qry = request.getParameter("qry");
			String qry2 = request.getParameter("qry2");
			Map parameter = new HashMap();
			parameter.put("qry", qry);
			parameter.put("qry2", qry2);
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, parameter);
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			Map map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{\"label\": \"\"}";
			}
			return ret;
		}else if("uploads".equals(action)){
			String linkids = request.getParameter("linkid"); //neo 20210516 批量上传，会传多个id，逗号分隔处理
			String roleid = request.getParameter("roleid");
			String userid2 = request.getParameter("userid");
			File file2local = null;
			ServletFileUpload upload = new ServletFileUpload();
			InputStream stream = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				if (ServletFileUpload.isMultipartContent(request)) {
					FileItemIterator iter = upload.getItemIterator(request);
					while (iter.hasNext()) {
						FileItemStream item = iter.next();
						stream = item.openStream();
						if (!item.isFormField()) {
							// 上传文件
							File file = new File(item.getName());
							String orFileName = file.getName();
							String originalFileName = orFileName;
							// 文件格式
							String suffix = "";
							if (originalFileName != null && !originalFileName.trim().equals("")) {
								String[] strArr = originalFileName.split("\\.");
								if (strArr != null && strArr.length == 2) {
									suffix = strArr[1];
								}
							}
							SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(userid2));
							//ApplicationUtils.debug("attach:" + Application.class.getResource("/").toURI());
//							String attachPath = AppUtils.getAttachFilePath();
							String serverName = request.getContextPath().replace("/", "");
							String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
							FileOperationUtil.newFolder(path);

							path += File.separator + serverName;
							FileOperationUtil.newFolder(path);

							path += File.separator + "attachfile" + File.separator;
							FileOperationUtil.newFolder(path);
							
							//neo 20210516 批量上传，会传多个id，逗号分隔处理，逗号转数组
							String[] linidArray;
							if(linkids.indexOf(",")>0){
								linidArray = linkids.split(",");
							}else{
								linidArray = new String[1];
								linidArray[0] = linkids;
							}
							
							//neo 20210516 批量上传，会传多个id，逗号分隔处理，第一个id接收从流里面获取。后面id的从第一个文件名copy，记录循环和第一个文件名
							int index  = 1;
							String firstFileName = "";
							long fileSize = 0l;
							for (String linkid : linidArray) {
								SysAttachment sysAttachment = new SysAttachment();
								sysAttachment.setContenttype(item.getContentType());
								sysAttachment.setLinkid(Long.valueOf(linkid));
								sysAttachment.setFilename(originalFileName);
								if(!StrUtils.isNull(roleid)){
									sysAttachment.setRoleid(Long.valueOf(roleid));
								}
								//sysAttachment.setInputer(sysUser.getCode());
								
								this.serviceContext.attachmentService.saveData(sysAttachment);
								// 复制文件到指定路径
								String fileNameNew = sysAttachment.getId() + originalFileName;
								
								file2local = new File(path + fileNameNew);
								
								if(index == 1){
									firstFileName = fileNameNew;
									bis = new BufferedInputStream(stream);
									bos = new BufferedOutputStream(new FileOutputStream(file2local));
									fileSize = Streams.copy(bis, bos, true);
								}else{
									FileOperationUtil.copyFile(firstFileName, fileNameNew);
								}
								index++;
								
//								this.getDaoProxy().refreshAttachFile(attachmentPkId, fileSize);
								sysAttachment.setFilesize(new BigDecimal(fileSize));
								
								String attachment_absurl = ConfigUtils.findSysCfgVal("sys_attachment_absurl");
								if("Y".equals(attachment_absurl)){
								    String url = request.getRequestURL().toString();//获得客户端发送请求的完整url
								    //System.out.println(url);
								    url = url.substring(0,url.indexOf("/service"));
									url = url + "/attachment/" + fileNameNew;
									//System.out.println(url);
									sysAttachment.setUrl(url) ;
								}

								//系统设置中开启后，so上传按不同船公司格式提取数据更新到海运委托表中，主要是船名航次，so，etd cls等信息
								if ("Y".equals(ConfigUtils.findSysCfgVal("sys_attachment_so_autogetdata"))) {
									SysLog sysLog = new SysLog();
									sysLog.setLogdesc(sysLog.getLogdesc() + ",手动上传文件,解析pdf开始");
									PDFUtil.parsePdf(file2local, linkid, roleid, sysAttachment, sysLog);

									sysLog.setInputer("uploads");
									sysLog.setLogtime(new Date());
									sysLog.setLogtype("DEBUG");
									sysLog.setLogdesc(sysLog.getLogdesc().replaceAll("'", "\""));
									SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
									sysLogDao.create(sysLog);
								}

								this.serviceContext.attachmentService.saveData(sysAttachment);
								
								//jsp中取不到jsf的context，之前是aop中处理的录入人录入时间，这里临时变通处理一下 neo 20170930
								String sql = "UPDATE sys_attachment set inputer = '"+sysUser.getCode()+"' WHERE id = " + sysAttachment.getId();
								this.serviceContext.attachmentService.sysAttachmentDao.executeSQL(sql);
								
								if("0".equals(linkid)&&"0".equals(roleid)){//网站设置中banner图片
									this.serviceContext.webConfigService.saveOnWebbanner(fileNameNew);
								}
								
								if("1".equals(linkid)&&"1".equals(roleid)){//网站设置中新闻图片
									this.serviceContext.webConfigService.saveOnWebnews(fileNameNew);
								}

							}
						}
					}
					resp.getWriter().write("{success:true}");
				}
			} catch (FileUploadException e) {
				result = "ERROR";
				e.printStackTrace();
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
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

			//PDFUtil.parsePdf(file2local,linkid,roleid);

//		}else if("getHead".equals(action)){
//			return getHead(request);
		}else if("sameJobsUploads".equals(action)){//多工作单批量上传
			String linkids = request.getParameter("linkid"); //neo 20210516 批量上传，会传多个id，逗号分隔处理
			String linkid2 = request.getParameter("linkid2");
			String jobids = request.getParameter("jobid");
			String roleid = request.getParameter("roleid");
			String userid2 = request.getParameter("userid");
			File file2local = null;
			ServletFileUpload upload = new ServletFileUpload();
			InputStream stream = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				if (ServletFileUpload.isMultipartContent(request)) {
					FileItemIterator iter = upload.getItemIterator(request);
					while (iter.hasNext()) {
						FileItemStream item = iter.next();
						stream = item.openStream();
						if (!item.isFormField()) {
							// 上传文件
							File file = new File(item.getName());
							String orFileName = file.getName();
							String originalFileName = orFileName.toLowerCase();
							// 文件格式
							String suffix = "";
							if (originalFileName != null && !originalFileName.trim().equals("")) {
								String[] strArr = originalFileName.split("\\.");
								if (strArr != null && strArr.length == 2) {
									suffix = strArr[1];
								}
							}
							SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(userid2));
							//ApplicationUtils.debug("attach:" + Application.class.getResource("/").toURI());
//							String attachPath = AppUtils.getAttachFilePath();
							String serverName = request.getContextPath().replace("/", "");
							String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
							FileOperationUtil.newFolder(path);

							path += File.separator + serverName;
							FileOperationUtil.newFolder(path);

							path += File.separator + "attachfile" + File.separator;
							FileOperationUtil.newFolder(path);
							
							//neo 20210516 批量上传，会传多个id，逗号分隔处理，逗号转数组
							String[] linidArray;
							if(linkids.indexOf(",")>0){
								linidArray = linkids.split(",");
							}else{
								linidArray = new String[1];
								linidArray[0] = linkids;
							}
							
							//neo 20210516 批量上传，会传多个id，逗号分隔处理，第一个id接收从流里面获取。后面id的从第一个文件名copy，记录循环和第一个文件名
							int index  = 1;
							String firstFileName = "";
							long fileSize = 0l;
							for (String linkid : linidArray) {
								SysAttachment sysAttachment = new SysAttachment();
								sysAttachment.setContenttype(item.getContentType());
								sysAttachment.setLinkid(Long.valueOf(linkid));
								sysAttachment.setFilename(originalFileName);
								if(!StrUtils.isNull(roleid)){
									sysAttachment.setRoleid(Long.valueOf(roleid));
								}
								//sysAttachment.setInputer(sysUser.getCode());
								
								this.serviceContext.attachmentService.saveData(sysAttachment);
								// 复制文件到指定路径
								String fileNameNew = sysAttachment.getId() + originalFileName;
								
								file2local = new File(path + fileNameNew);
								
								if(index == 1){
									firstFileName = file2local.getAbsolutePath();
									bis = new BufferedInputStream(stream);
									bos = new BufferedOutputStream(new FileOutputStream(file2local));
									fileSize = Streams.copy(bis, bos, true);
								}else{
									FileOperationUtil.copyFile(firstFileName, file2local.getAbsolutePath());
								}
								index++;
								
//								this.getDaoProxy().refreshAttachFile(attachmentPkId, fileSize);
								sysAttachment.setFilesize(new BigDecimal(fileSize));
								
								String attachment_absurl = ConfigUtils.findSysCfgVal("sys_attachment_absurl");
								if("Y".equals(attachment_absurl)){
								    String url = request.getRequestURL().toString();//获得客户端发送请求的完整url
								    //System.out.println(url);
								    url = url.substring(0,url.indexOf("/service"));
									url = url + "/attachment/" + fileNameNew;
									//System.out.println(url);
									sysAttachment.setUrl(url) ;
								}

								//系统设置中开启后，so上传按不同船公司格式提取数据更新到海运委托表中，主要是船名航次，so，etd cls等信息
								if ("Y".equals(ConfigUtils.findSysCfgVal("sys_attachment_so_autogetdata"))) {
									SysLog sysLog = new SysLog();
									sysLog.setLogdesc(sysLog.getLogdesc() + ",手动上传文件,解析pdf开始");
									PDFUtil.parsePdf(file2local, linkid, roleid, sysAttachment, sysLog);

									sysLog.setInputer("uploads");
									sysLog.setLogtime(new Date());
									sysLog.setLogtype("DEBUG");
									sysLog.setLogdesc(sysLog.getLogdesc().replaceAll("'", "\""));
									SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
									sysLogDao.create(sysLog);
								}

								this.serviceContext.attachmentService.saveData(sysAttachment);
								
								//jsp中取不到jsf的context，之前是aop中处理的录入人录入时间，这里临时变通处理一下 neo 20170930
								String sql = "UPDATE sys_attachment set inputer = '"+sysUser.getCode()+"' WHERE id = " + sysAttachment.getId();
								this.serviceContext.attachmentService.sysAttachmentDao.executeSQL(sql);
								
								if("0".equals(linkid)&&"0".equals(roleid)){//网站设置中banner图片
									this.serviceContext.webConfigService.saveOnWebbanner(fileNameNew);
								}
								
								if("1".equals(linkid)&&"1".equals(roleid)){//网站设置中新闻图片
									this.serviceContext.webConfigService.saveOnWebnews(fileNameNew);
								}

							}
						}
					}
					resp.getWriter().write("{success:true}");
				}
			} catch (FileUploadException e) {
			result = "ERROR";
			e.printStackTrace();
		}catch (Exception e) {
			result = "ERROR";
			e.printStackTrace();
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
	}else if("templateUploads".equals(action)){//上传提单图片
			String filename = request.getParameter("filename");//文件名
			File file2local = null;
			ServletFileUpload upload = new ServletFileUpload();
			InputStream stream = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				if (ServletFileUpload.isMultipartContent(request)) {
					FileItemIterator iter = upload.getItemIterator(request);
					while (iter.hasNext()) {
						FileItemStream item = iter.next();
						stream = item.openStream();
						if (!item.isFormField()) {
							// 上传文件
							File file = new File(item.getName());
							String orFileName = file.getName();
							String originalFileName = orFileName.toLowerCase();
							// 文件格式
//							String attachPath = AppUtils.getAttachFilePath();
							String path = AppUtils.getHblReportFilePath();
							String serverName = request.getContextPath().replace("/", "");
//							String path = AppUtils.getWebApplicationPath() + File.separator + "wtpwebapps"
//							+File.separator +"scp"+ File.separator+"reportEdit"+ File.separator+"file"+ File.separator+"images"+ File.separator;
							path+=File.separator+"file"+ File.separator+"images"+ File.separator;;
							FileOperationUtil.newFolder(path);
							System.out.println(path);
//							path += File.separator + serverName;
//							FileOperationUtil.newFolder(path);
//
//							path += File.separator + "attachfile" + File.separator;
//							FileOperationUtil.newFolder(path);
							
							// 复制文件到指定路径
							bis = new BufferedInputStream(stream);
//							file2local = new File(path + originalFileName);filename
							file2local = new File(path + filename);
							bos = new BufferedOutputStream(new FileOutputStream(file2local));
							long fileSize = Streams.copy(bis, bos, true);
						}
					}
					resp.getWriter().write("{success:true}");
				}
			} catch (FileUploadException e) {
				result = "ERROR";
				e.printStackTrace();
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
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
//		}else if("getHead".equals(action)){
//			return getHead(request);
		}else if("crm2".equals(action)){
			File file;
			ServletFileUpload upload = new ServletFileUpload();
			InputStream stream = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				if (ServletFileUpload.isMultipartContent(request)) {
					FileItemIterator iter = upload.getItemIterator(request);
					while (iter.hasNext()) {
						FileItemStream item = iter.next();
						stream = item.openStream();
						if (!item.isFormField()) {
							// 上传文件
							file = new File(item.getName());
							String orFileName = file.getName();
							String originalFileName = orFileName.toLowerCase();
							// 文件格式
							String suffix = "";
							if (originalFileName != null
									&& !originalFileName.trim().equals("")) {
								String[] strArr = originalFileName.split("\\.");
								if (strArr != null && strArr.length == 2) {
									suffix = strArr[1];
								}
							}
							String serverName = request.getContextPath().replace("/", "");
							String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
							FileOperationUtil.newFolder(path);

							path += File.separator + serverName;
							FileOperationUtil.newFolder(path);

							path += File.separator + "attachfile" + File.separator;
							FileOperationUtil.newFolder(path);
							
							bis = new BufferedInputStream(stream);
							bos = new BufferedOutputStream(new FileOutputStream(
									new File(path + originalFileName)));
							long fileSize = Streams.copy(bis, bos, true);
							// 对字节数组Base64编码
							String imageStr = Base64.getImageStr(path + originalFileName);
//					        CommonService commonService = (CommonService)AppUtils.getBeanFromSpringIoc("commonService");
//					        String card4ImgByOCR = commonService.card4ImgByOCR(imageStr);
//					        System.out.println(imageStr);
//							result = card4ImgByOCR;
							File filed = new File(path + originalFileName);
							if(filed.exists()){//用完后删除
								filed.delete();
							}
						}
					}
				}
			} catch (FileUploadException e) {
				result = "ERROR";
				e.printStackTrace();
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
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
//		}else if("getHead".equals(action)){
//			return getHead(request);
		}else if("crm".equals(action)){
			String uploadDir = System.getProperty("java.io.tmpdir");
			ServletFileUpload upload = new ServletFileUpload();
			InputStream inputStream = null;
			File newUploadFile = new File(uploadDir + "/" + UUID.randomUUID().toString());
			byte[] data = null;
			try {
				if (ServletFileUpload.isMultipartContent(request)) {
					FileItemIterator iter = upload.getItemIterator(request);
					while (iter.hasNext()) {
						FileItemStream item = iter.next();
						if (!item.isFormField()) {
							inputStream = item.openStream();
							
						    byte[] buffer = new byte[4096];
						    InputStream input = null;
						    OutputStream output = null;
						    try {
						        input = item.openStream();
						        output = new BufferedOutputStream(new FileOutputStream(newUploadFile));
						        for (;;) {
						            int n = input.read(buffer);
						            if (n==(-1))
						                break;
						            output.write(buffer, 0, n);
						        }
						    }
						    finally {
						        if (input!=null) {
						            try {
						                input.close();
						            }
						            catch (IOException e) {}
						        }
						        if (output!=null) {
						            try {
						                output.close();
						            }
						            catch (IOException e) {}
						        }
						    }
						}
					}
					
					inputStream = new FileInputStream(newUploadFile);
					data = new byte[inputStream.available()];
					inputStream.read(data);
					inputStream.close();
					
					//System.out.println("data:"+data.length);
					// 对字节数组Base64编码
			        BASE64Encoder encoder = new BASE64Encoder();
			        String encode = encoder.encode(data);
//			        CommonService commonService = (CommonService)AppUtils.getBeanFromSpringIoc("commonService");
//			        //System.out.println("encode:"+encode);
//			        String card4ImgByOCR = commonService.card4ImgByOCR(encode);
//			        //System.out.println("card4ImgByOCR:"+card4ImgByOCR);
////			        String card4ImgByOCR = "{\"outputs\":[{\"outputLabel\":\"ocr_business_card\",\"outputMulti\":{},\"outputValue\":{\"dataType\":50,\"dataValue\":\"{\\\"addr\\\":[\\\"深圳市罗湖区嘉宾路2018号深华商业大厦1801、1802室\\\"],\\\"company\\\":[\\\"深圳市泰德胜物流有限公司\\\"],\\\"department\\\":[],\\\"email\\\":[\\\"george@seaever.com\\\"],\\\"name\\\":\\\"张周辉\\\",\\\"request_id\\\":\\\"20180709144525_a8e112440078bdb0cefed063dbb665b7\\\",\\\"success\\\":true,\\\"tel_cell\\\":[\\\"8613902906969\\\"],\\\"tel_work\\\":[\\\"8675525153699\\\"],\\\"title\\\":[\\\"总经理\\\"]}\"}}]}";
//			        //System.out.println(card4ImgByOCR);
//			        result = card4ImgByOCR.indexOf("Error")>0?"Error":card4ImgByOCR;
				}
			} catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			} finally {
				if (newUploadFile != null) {
					FileOperationUtil.delFile(newUploadFile.getAbsolutePath());
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e) {
					}
				}
			}	
		}else if("crmsave".equals(action)){
			String savedate = request.getParameter("savedate");
			JSONObject date=JSONObject.fromObject(savedate); 
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				String decode = java.net.URLDecoder.decode(date.getString("namec"), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				SysCorporation repetitionTips = null;
				repetitionTips = serviceContext.customerMgrService.repeat(-1L , "code" , java.net.URLDecoder.decode(date.getString("code"),"UTF-8"));
				if(repetitionTips!=null){
					return "代码已经存在";
				}
				repetitionTips = serviceContext.customerMgrService.repeat(-1L , "namec" ,java.net.URLDecoder.decode(date.getString("namec"),"UTF-8"));
				if(repetitionTips!=null){
					return "中文名已经存在";
				}
				repetitionTips = new SysCorporation();
				repetitionTips.setCode(java.net.URLDecoder.decode(date.getString("code"), "UTF-8"));
				repetitionTips.setNamec(java.net.URLDecoder.decode(date.getString("namec"), "UTF-8"));
				repetitionTips.setAddressc(java.net.URLDecoder.decode(date.getString("addressc"), "UTF-8"));
				repetitionTips.setTel1(java.net.URLDecoder.decode(date.getString("tel1"), "UTF-8"));
				repetitionTips.setEmail1(java.net.URLDecoder.decode(date.getString("email1"), "UTF-8"));
				repetitionTips.setContact(java.net.URLDecoder.decode(date.getString("contact"), "UTF-8"));
				repetitionTips.setIsar(true);
				repetitionTips.setIsap(true);
				repetitionTips.setIsofficial(false);
				repetitionTips.setIscustomer(true);
				repetitionTips.setLevel("*");
				SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(date.getString("userid")));
				repetitionTips.setInputer(user.getCode());
				serviceContext.customerMgrService.sysCorporationDao.create(repetitionTips);
			} catch (CommonRuntimeException e) {
				e.printStackTrace();
//				String exception  = e.getLocalizedMessage();
				result = "ERROR";
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
			result = "OK";
		}else if("savego".equals(action)){
			String datejson = request.getParameter("datejson");
			String bid = request.getParameter("id");
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				String sql = "UPDATE bpm_process SET gojson = '"+datejson+"'::jsonb WHERE id = "+bid;
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
			result = "OK";
		}else if("selectgo".equals(action)){
			String bid = request.getParameter("id");
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				String sql = "SELECT gojson FROM bpm_process WHERE id = "+bid;
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(m!=null&&m.get("gojson")!=null){
					return m.get("gojson").toString();
				}
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("bpmtrace".equals(action)){
			String processinstanceid = request.getParameter("processinstanceid");
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				String sql = "SELECT gojson FROM bpm_process p , bpm_processinstance i WHERE p.id = i.processid AND i.id = "+processinstanceid;
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(m!=null&&m.get("gojson")!=null){
					JSONObject json = JSONObject.fromObject(m.get("gojson").toString());
					json.put("class","go.GraphLinksModel");
					//遍历替换json里面的值
			        for (Object key:json.keySet()) {
			        	if(key.toString().equals("nodeDataArray")){
			        		JSONArray jsonArray = json.getJSONArray(key.toString());
			        		int size = jsonArray.size();
			        		for(int i = 0; i < size; i++) {
			        			JSONObject jsonv2 = JSONObject.fromObject(jsonArray.get(i));
			        			String text = jsonv2.get("text").toString();
								String langugesql = "SELECT f_sys_getls('userid="+userid+";lskey="+text+"') AS langguge";
								try {
									Map langguge = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(langugesql);
									jsonv2.remove("text");
									jsonv2.put("text", langguge.get("langguge"));
								} catch (Exception e2) {
								}
			        			String sqltrace = "SELECT 1 FROM bpm_trace WHERE processinstanceid = " + processinstanceid+
			        					" AND '\"'||fromnode||'\"' = ('"+jsonArray.get(i)+"'::jsonb->'text')::TEXT";
			        			try {
									Map mtrace = serviceContext.daoIbatisTemplate
											.queryWithUserDefineSql4OnwRow(sqltrace);
								} catch (NoRowException e) {
									//再查一遍：fromnode中没有，tonode中有说明流程正处在这一步
									String sqltraceto = "SELECT 1 FROM bpm_trace WHERE processinstanceid = " + processinstanceid+
		        					" AND '\"'||tonode||'\"' = ('"+jsonArray.get(i)+"'::jsonb->'text')::TEXT LIMIT 1";
									Map mtrace2 = null;
									try {
										mtrace2 = serviceContext.daoIbatisTemplate
												.queryWithUserDefineSql4OnwRow(sqltraceto);
									} catch (NoRowException e2) {
										mtrace2 = null;
									} catch (MoreThanOneRowException e2) {
									}
									if(mtrace2!=null){
										if(text!=null&&text.equals("Start")){
											jsonv2.put("category", "Start-doing");
										}else if(text!=null&&text.equals("End")){
											jsonv2.put("category", "End-doing");
										}else{
											jsonv2.put("category", "defaults-doing");
										}
									}else{
										if(text!=null&&text.equals("Start")){
											jsonv2.put("category", "Start-Gray");
										}else if(text!=null&&text.equals("End")){
											jsonv2.put("category", "End-Gray");
										}else{
											jsonv2.put("category", "defaults-Gray");
										}
									}
									if(text.indexOf("main")>-1){
										String keys = jsonv2.get("key").toString();
										//查询json中路线的from或者to是bpm_trace中的fromnode或者tonode说明已经走过了
										String mainsql = "SELECT 1 FROM bpm_process p , bpm_processinstance i WHERE p.id = i.processid AND i.id = "+processinstanceid
														+"\nAND EXISTS(SELECT 1 FROM bpm_trace a WHERE processinstanceid = i.id"
														+"\nAND EXISTS(SELECT 1 FROM jsonb_populate_recordset(null::bpm_model_trace,(SELECT p.gojson->>'linkDataArray' FROM bpm_process WHERE id = p.id)::jsonb) b WHERE (\"from\" = "+keys+" OR \"to\" = "+keys+")"
														+"\nAND EXISTS(SELECT 1 FROM jsonb_populate_recordset(null::bpm_model_json_type,(SELECT gojson->>'nodeDataArray' FROM bpm_process WHERE id = p.id)::jsonb) c WHERE (c.\"text\" = a.fromnode OR c.\"text\" = a.tonode) AND (b.\"to\" = c.\"key\" OR b.\"from\" = c.\"key\"))));";
										try{
											Map main = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(mainsql);
											if(main!=null){
												jsonv2.put("category", "");
											}
										}catch(Exception e1){}
									}
								}catch(Exception e){}
								finally{
									jsonArray.set(i, jsonv2);
								}
			        		}
			        		json.put("nodeDataArray",jsonArray.toString());
			        	}
			        }
					return json.toString();
				}
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("getProcess".equals(action)){
			String processinstanceid = request.getParameter("processinstanceid");
			String lable = java.net.URLDecoder.decode(request.getParameter("lable").toString());
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			try {
				String sql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM" +
							"(SELECT f_sys_getls('userid="+userid+";lskey='||displayname) displayname" +
							",f_sys_getls('userid="+userid+";lskey='||taskname) taskname" +
							",COALESCE(to_char(taskcreatedtime,'yyyy-MM-dd HH24:MI'),'') taskcreatedtime" +
							",COALESCE(to_char(taskstartedtime,'yyyy-MM-dd HH24:MI'),'') taskstartedtime," +
							"COALESCE(to_char(taskendtime,'yyyy-MM-dd HH24:MI'),'') taskendtime,procecreateduser," +
							"(SELECT namec||'/'||namee||'/'||'/'||COALESCE((SELECT name FROM sys_department WHERE id = x.deptid),'') " +
							"FROM sys_user x WHERE code = t.procecreateduser) AS procecreatedusers,"+
							"COALESCE(to_char(procestartedtime,'yyyy-MM-dd HH24:MI'),'') procestartedtime" +
							",COALESCE(remarks,'') AS remarks" +
							" FROM _bpm_task t WHERE state <> 3 AND processinstanceid = " + processinstanceid+
							" and f_sys_getls('userid="+userid+";lskey='||taskname) = '"+lable+"') T";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(m!=null&&m.get("json")!=null){
					return m.get("json").toString();
				}
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("upladFile".equals(action)){
			return upladFile(request);
		}else if("getCurrencyType".equals(action)){
			String feecode = request.getParameter("feecode");
			try {
				daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
				Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COALESCE(currency,'USD') as currency FROM dat_feeitem WHERE code = '"
					+feecode+"' LIMIT 1");
				result = m.get("currency").toString();
			}catch(NoRowException e){
				result = "ERROR";
			}catch (Exception e) {
				result = "ERROR";
				e.printStackTrace();
			}
		}else if("importInvoiceInf".equals(action)){
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String ret = "";
			StringBuilder sb = new StringBuilder();
			try{
				BufferedReader reader = request.getReader();
				char[] buff = new char[1024];
				int len;
				while((len = reader.read(buff)) != -1) {
					sb.append(buff,0, len);
				}
			}catch (IOException e) {
			   e.printStackTrace();
			}
			JSONObject jo = JSONObject.fromObject(sb.toString());
//			System.out.print(jo.get("qry"));
			String sql = "SELECT * FROM array_to_json(f_sys_corpinv_imp('"+jo.get("qry")+"',0)) AS json";
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m != null && m.containsKey("json")&&m.get("json")!=null){
				ret = m.get("json").toString();
			}else {
				ret = "{\"label\": \"\"}";
			}
			return ret;
		}else if("getWebBanner".equals(action)){
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			String key = request.getParameter("key");
			return serviceContext.webConfigService.getWebBannerJson(key);
		}else if("getWebBannernews".equals(action)){
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			String key = request.getParameter("key");
			return serviceContext.webConfigService.getWebBannerJsonnews(key);
		}else if("saveWebBanner".equals(action)){
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			String key = request.getParameter("key");
			String jsonvalue = request.getParameter("jsonvalue");
			return serviceContext.webConfigService.saveWebBanner(key,jsonvalue);
		}else if("deleteWebBanner".equals(action)){
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			String key = request.getParameter("key");
			String filenames = request.getParameter("filenames");
			
			return serviceContext.webConfigService.deleteWebBanner(key, filenames);
		}
		return result;
	}
	
	
	/*
	 * 账单的jsp页面直接上传文件
	 */
	public String upladFile(HttpServletRequest request) {
	    	FileOutputStream output = null;
	    	ServletFileUpload upload = new ServletFileUpload();
			InputStream stream = null;
			InputStream ips = null;
	    	String report = request.getParameter("report");
	    	String msg = "文件上传成功！";
	        try {
	        	request.setCharacterEncoding("UTF-8");
	        	FileItemIterator iter = upload.getItemIterator(request);
	        	FileItemStream item = iter.next();
	        	
				if (!item.isFormField()) {
					String fileName = item.getName();
					String oldfilename = report.split("/")[report.split("/").length-1];
					if(!fileName.equals(oldfilename)){
						msg = "文件名错误！";
						return msg;
					}
					if(!fileName.endsWith(".raq")){
						msg = "文件类型错误！";
						return msg;
					}
				}
				stream = item.openStream();
	            String filepath = AppUtils.getReportFilePath() + report;
	            File f = new File(filepath);
	            output = new FileOutputStream(f);
	            byte[] buf = new byte[4096];
	            int length = UIFileUpload.END_UPLOADING;
	            while ((length = stream.read(buf)) != UIFileUpload.END_UPLOADING) {
	                output.write(buf, 0, length);
	            }
	            output.flush();
	            
	            //保存格式文件到数据库
	            serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
	            SysReport sy = serviceContext.sysReportMgrService.sysReportDao.findOneRowByClauseWhere("filename = '"+item.getName()+"'"); 
	        	File file = new File(filepath);
	        	ips = new FileInputStream(file);
	        	byte[] byteData = new byte[(int) file.length()];  
	        	ips.read(byteData, 0, byteData.length);  
	            sy.setFilebyte(byteData);
	            serviceContext.sysReportMgrService.saveData(sy);
	            
	         } catch (Exception e) {
	             throw new FacesException(e);
	         } finally {
	        	 if (stream != null){
	            	 try {
	            		 stream.close();
	                 } catch (IOException e) {
	                	 e.printStackTrace();
	                 }
	        	 }
	        	 if (output != null){
	            	 try {
	                     output.close();
	                 } catch (IOException e) {
	                	 e.printStackTrace();
	                 }
	        	 }
	        	 if(ips != null)
					try {
						ips.close();
					} catch (IOException e) {
						e.printStackTrace();
					}  
	         }
			return msg;
	    }
	

	private String commonQueryByXml(HttpServletRequest request) {
		daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String ret = "";
		String sqlId = request.getParameter("sqlId");
		String qry = request.getParameter("qry");
		String qry2 = request.getParameter("qry2");
		String qry3 = request.getParameter("qry3");
		Map parameter = new HashMap();
		parameter.put("qry", qry);
		parameter.put("qry2", qry2);
		parameter.put("qry3", qry3);
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, parameter);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			ret = map.get("json").toString();
		}else {
			ret = "{\"label\": \"\"}";
		}
		return ret;
	}

	private String saveOp2(HttpServletRequest request){
		serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		String corpidop2 = StrUtils.isNull(request.getParameter("corpidop2"))?null:request.getParameter("corpidop2");
		String id = request.getParameter("id");
		String sql = "UPDATE fina_jobs SET corpidop2 ="+corpidop2+" WHERE isdelete =FALSE AND id="+id;
		return "1";
	}



	private String getstatuseInfo(HttpServletRequest request) {
		daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String ret = "";
		String sqlId = request.getParameter("sqlId");
		String qry = request.getParameter("qry");
		String userid = request.getParameter("userid");
		if(StrUtils.isNull(userid)||userid.equals("null"))userid="81433600";//demo 为保留用户，没传时用这个
		Map parameter = new HashMap();
		String goodstrack_isShowIscs = ConfigUtils.findUserCfgVal("goodstrack_isShowIscs",Long.parseLong(userid));
		if(StrUtils.isNull(goodstrack_isShowIscs)||"false".equals(goodstrack_isShowIscs)){
			qry += "\nAND iscs = FALSE";
		}
		parameter.put("qry", qry);
		parameter.put("userid", userid);
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, parameter);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			ret = map.get("json").toString();
		}else {
			ret = "{\"label\": \"\"}";
		}
		return ret;
	}
	
	private String getabbcodeInfo(HttpServletRequest request) {
		daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String ret = "";
		String sqlId = request.getParameter("sqlId");
		String qry = request.getParameter("qry");
		Map parameter = new HashMap();
		parameter.put("qry", qry);
		List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, parameter);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			ret = map.get("json").toString();
		}else {
			ret = "{\"label\": \"\"}";
		}
		return ret;
	}
	

	private String getSignImgPath(HttpServletRequest request , String userid) throws Exception {
		String serverName = request.getContextPath().replace("/", "");
		String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
		FileOperationUtil.newFolder(path);

		path += File.separator + serverName;
		FileOperationUtil.newFolder(path);

		path += File.separator + "attachfile" + File.separator;
		FileOperationUtil.newFolder(path);
		
		String imgName =path +"sign_"+userid+".png";
		return imgName;
	}
	
	private String getImgPath(HttpServletRequest request , String id) throws Exception {
		String serverName = request.getContextPath().replace("/", "");
		String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
		FileOperationUtil.newFolder(path);

		path += File.separator + serverName;
		FileOperationUtil.newFolder(path);

		path += File.separator + "attachfile" + File.separator;
		FileOperationUtil.newFolder(path);
		
		String imgName =path +id+getImgname(Long.parseLong(id!=null?id:"-1"));
		return imgName;
	}
	
	public Map findAttachFile(Long attachFileId) throws Exception {
		String querySql = 
			"\nSELECT " +
			"\nfilepath " +
			"\n, * " +
			"\nFROM sys_attachment " +
			"\nWHERE filename IS NOT NULL " +
			"\nAND filename <> ''" +
			"\nAND isdelete = FALSE" +
			"\nAND (id = " + attachFileId + " OR linkid = " + attachFileId + ")" +
			"\nORDER BY inputtime DESC LIMIT 1;";
		daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
		.getBeanFromSpringIoc("daoIbatisTemplate");
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		return map;
	}
	
	public String getImgname(Long dPkVal) throws Exception{
		Map m = this.findAttachFile(dPkVal);
		String fileName = StrUtils.getMapVal(m, "filename");
		//fileName =  fileName.replaceAll(" ", "%20");
		
		//System.out.println(fileName);
		return fileName;
	}
	
//	private String getHead(HttpServletRequest request) {
//		String ret = "{\"label\": \"\"}";
//		String filepath = "";
//		String findUserCfgVal = "";
//		String userid = request.getParameter("userid");//此人的id
//		try {
//			findUserCfgVal = ConfigUtils.findUserCfgVal("head",Long.parseLong(userid));
//			if(findUserCfgVal==null){
//				findUserCfgVal = "";
//			}
////			filepath = AppUtils.getAttachFilePath() + findUserCfgVal;
//		} catch (Exception e) {
//			return ret;
//		}
//		return "{\"label\": \""+findUserCfgVal+"\"}";
//	}

	private String getquoteamounttemp(HttpServletRequest request){
		String quoteamount = request.getParameter("quoteamount");
		String quotecurrency = request.getParameter("quotecurrency");
		String currency = request.getParameter("currency");

		String quoteamounttemp = "";

		daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String sql = "select f_amtto(now(),'"+quotecurrency+"','"+currency+"', "+quoteamount+"::NUMERIC(18,2)) as quoteamounttemp";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map.containsKey("quoteamounttemp")){
			quoteamounttemp = String.valueOf(map.get("quoteamounttemp"));
		}

		return quoteamounttemp;
	}

    private String filedownload(HttpServletRequest request) {
        String lastRecordids = request.getParameter("lastRecordids");
        String roleid = request.getParameter("roleid");

        String sysAttachmentidstr = "";
        for (String jobid : lastRecordids.split(",")) {
            serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			String sql = "linkid = '" + jobid + "' AND isdelete = false";
			if (!StrUtils.isNull(roleid)) {
				sql += " and roleid=" + roleid;
			}
			List<SysAttachment> sysAttachment = serviceContext.sysAttachmentService.sysAttachmentDao.findAllByClauseWhere(sql);
			for (SysAttachment attachment : sysAttachment) {
                sysAttachmentidstr +=  (jobid + "_" + attachment.getId()) + ",";
            }
        }

        return sysAttachmentidstr;
    }

	private String queryGetFeeidata(HttpServletRequest request) {
		String filterBySaas = SaasUtil.filterByCorpid("d", request);
		String languge = request.getParameter("languge");
		String feeitemid = request.getParameter("feeitemid");
		String jobid = request.getParameter("jobid");
		boolean isen = languge != null && languge.equals("en") ? true : false;
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n	id,code as name," + (isen ? "namee" : "name") + " as namec,namee,currency,unit");
		sb.append("\n FROM");
		sb.append("\n	dat_feeitem d");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n" + filterBySaas);
		sb.append("\n	and id =" + feeitemid);
		ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		Map<String, String> result = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		String currency = result.get("currency");
		if (currency == null) {
			currency = "CNY";
		}
		String unit = result.get("unit");
		String piece = "";
		String xaingzisql = "";

		Map<String, String> result1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(xaingzisql.toString());
		if ("箱".equals(unit)) {
			piece = String.valueOf(result1.get("allpiece"));
		} else if ("票".equals(unit)) {
			piece = "1";
		} else if ("CBM".equals(unit)) {
			piece = String.valueOf(result1.get("allcbm"));
		} else if ("KGS".equals(unit)) {
			piece = String.valueOf(result1.get("allgrswgt"));
		} else if ("CHARGEWEIGHT".equals(unit)) {
			piece = String.valueOf(result1.get("allchargeweight"));
		} else {
			piece = "1";
		}

		return "{\"currency\":\"" + currency + "\",\"piece\":\"" + piece + "\"}";
	}
}