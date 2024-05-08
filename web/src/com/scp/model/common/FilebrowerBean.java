package com.scp.model.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.widget.fileupload.impl.UIFileUploadDialog;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.apache.commons.fileupload.FileItemIterator;
import org.operamasks.org.apache.commons.fileupload.FileItemStream;
import org.operamasks.org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.operamasks.org.apache.commons.fileupload.util.Streams;

import com.scp.model.sys.SysAttachment;
import com.scp.util.AppUtils;
import com.scp.util.FileOperationUtil;
import com.scp.view.comp.GridView;

@ManagedBean(name = "common.ckeditor.filebrowerBean", scope = ManagedBeanScope.REQUEST)
public class FilebrowerBean extends GridView {

	
	@Accessible
	@Bind
	private UIFileUploadDialog fileUpload;
	
	@Bind(value = "#{getReqValue(\"ckeditor\")}")
	private UITextField ckeditor;
	
	@Bind
	private String response;
	
	@Override
	public void grid_ondblclick() {
		//Browser.execClientScript("alert(1)");
		selectFile();
	}


	@Action
	public void addFile() {
		fileUpload.show();
	}
	
	@Action
	public void selectFile() {
		Long id = this.getGridSelectId();
		if(id == -1)return;
		SysAttachment sysAttachment = this.serviceContext.sysAttachmentService.sysAttachmentDao.findById(id);
		String filepath = AppUtils.getContextPath() + sysAttachment.getFilepath() + sysAttachment.getFileNameDisk();
//		filepath = filepath.replaceAll("\\", "/");
		Browser.execClientScript("chooseImg('"+filepath+"')");
	}
	
	
	@Action
	public void fileUpload_onclose() {
		refresh();
	}
	
	public void processFileUpload (HttpServletRequest req, HttpServletResponse res) throws IOException {
		ServletFileUpload upload = new ServletFileUpload();
		InputStream stream = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			if (ServletFileUpload.isMultipartContent(req)) {
				if (fileUpload.getFileSizeMax() != null) {
					upload.setFileSizeMax(fileUpload.getFileSizeMax());
				}
				FileItemIterator iter = upload.getItemIterator(req);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (!item.isFormField()) {
						File uf = new File(item.getName());
						String filename = uf.getName();
						String filetype = item.getContentType();
						String filesize = "";
						String filepath = AppUtils.getUploadPath() + "oa";
						FileOperationUtil.newFolder(filepath);
						//取得文件保存的相对Web服务器的路径，文件分隔符转换为斜杠( / )
						String filepathDb = "/upload/oa/"; 
						
						
						SysAttachment sysAttachment = new SysAttachment();
						sysAttachment.setContenttype(item.getContentType());
						sysAttachment.setLinkid(Long.valueOf(-1));
						sysAttachment.setFilename(filename);
						sysAttachment.setFilepath(filepathDb);
						
						this.serviceContext.attachmentService.saveData(sysAttachment);
						
						// 复制文件到指定路径
						filename = sysAttachment.getFileNameDisk();
						
						bis = new BufferedInputStream(stream);
						bos = new BufferedOutputStream(new FileOutputStream(
								new File(filepath  + File.separator + filename)));
						long fileSize = Streams.copy(bis, bos, true);
						
						sysAttachment.setFilesize(new BigDecimal(fileSize));
						
						this.serviceContext.attachmentService.saveData(sysAttachment);
					}
				}
				res.getWriter().write("{success:true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.getWriter().write(String.format("{success:false,message:'%s'}", e.getMessage()));
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) { }
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) { }
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) { }
			}
		}
	}
	
	@Action
	public void delFile() {
		boolean needUpdate = false;
		Object[] selections = grid.getSelectedValues();
		for (Object o : selections) {
			//AppUtils.debug(o);
		}
		if (needUpdate) {
			refresh();
		}
	}
	
//	/**获取上传文件路径(绝对路径)
//	 * @throws Exception */
//	protected String getUploadDir() throws Exception {
//		
//		ServletContext sc = (ServletContext)FacesContextImpl.getCurrentInstance().getExternalContext().getContext();
//		//---取得服务器日期，以该日期的年份和月份分别建立目录
//		Date srvdate = this.serviceContext.commonDBService.getSrvDate();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(srvdate);
//		String yy = String.valueOf(cal.get(Calendar.YEAR));
//		String mm = String.valueOf(cal.get(Calendar.MONTH) + 1);
//		if (StrTools.isNull(mm)) mm = "";
//		if (mm.length() == 1) mm = "0" + mm;
//		//---
//		
//		String dir = "";
//		dir = sc.getRealPath(File.separator);
//		if(dir.endsWith(File.separator))
//			dir = dir.substring(0, dir.length() - 1);
//		dir = dir.substring(0, dir.lastIndexOf(File.separator));
//		String folder = File.separator + "upload" + File.separator;
//		dir = dir + folder;
//		if(!createDir(dir)){
//			return null;
//		}
//		dir += "default" + File.separator;
//		if(!createDir(dir)){
//			return null;
//		}
//		if (!StrTools.isNull(yy)) {
//			dir += yy + File.separator;
//			if(!createDir(dir)){
//				return null;
//			}
//			if (!StrTools.isNull(mm)) {
//				dir += mm + File.separator;
//				if(!createDir(dir)){
//					return null;
//				}
//			}
//		}
//				
//		return dir;
//	}
	
	
//	private boolean createDir(String fulldir) {
//		
//		if(fulldir == null || "".equals(fulldir)) return false;
//		boolean ret = true;
//		File f = new File(fulldir);
//		if(!f.exists()){
//			ret = f.mkdirs();
//		}
//		return ret;
//	}
//	/**
//	 * <p>根据以下规则编写新的文件名，以防止重复</p>
//	 * <p>规则：保留原文件后缀，并以当前服务器日期(yyyymmddhhmmss)</p>
//	 * @return
//	 * @throws Exception 
//	 */
//	private String genNewFileName(String srcName) throws Exception {
//		if (StrTools.isNull(srcName)) return "";
//		String fname = "";
//		Date now = this.serviceContext.commonDBService.getSrvDate();
//		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//		fname = sf.format(now);
//		Double dd = Math.random() * 1000;
//		String dstr = String.valueOf(dd.intValue());
//		if (dstr.length() == 1) {dstr = "00" + dstr;}
//		else if (dstr.length() == 2) {dstr = "0" + dstr;}
//		fname += dstr;
//		fname += srcName.substring(srcName.lastIndexOf("."));
//		return fname;
//	}
	
}
