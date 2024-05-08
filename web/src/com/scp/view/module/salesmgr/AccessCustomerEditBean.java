package com.scp.view.module.salesmgr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.fileupload.impl.UIFileUploadDialog;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.org.apache.commons.fileupload.FileItemIterator;
import org.operamasks.org.apache.commons.fileupload.FileItemStream;
import org.operamasks.org.apache.commons.fileupload.FileUploadException;
import org.operamasks.org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.operamasks.org.apache.commons.fileupload.util.Streams;

import com.scp.base.CommonComBoxBean;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysCorpvisit;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.SysUserAssign;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.salesmgr.accesscustomereditBean", scope = ManagedBeanScope.REQUEST)
public class AccessCustomerEditBean extends FormView {

	@SaveState
	@Accessible
	public SysCorpvisit selectedRowData = new SysCorpvisit();

	@SaveState
	@Accessible
	public Long ppkVal;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.customerid = AppUtils.getReqParam("customerid");
			if (StrUtils.isNull(AppUtils.getReqParam("id"))) {
				add();
			} else {
				pkVal = Long.valueOf(AppUtils.getReqParam("id"));
				this.ppkVal = pkVal;
				selectedRowData = serviceContext.corpvisitService.corpvisitDao
						.findById(pkVal);
				update.markUpdate(true, UpdateLevel.Data, "editPanel");
			}
			if (!StrUtils.isNull(this.customerid)) {
				SysCorporation sysCorporation = this.serviceContext.customerMgrService.sysCorporationDao
						.findById(Long.parseLong(this.customerid));
				if (sysCorporation != null) {
					selectedRowData.setCustomerid(sysCorporation.getId());
					selectedRowData.setCustomercode(sysCorporation.getCode());
					selectedRowData.setCustomernamec(sysCorporation.getNamec());
					selectedRowData.setCustomernamee(sysCorporation.getNamee());
					
					initCustomerInfo();
				}
			}
		}
	}

	@Bind
	public String popQryKey;

	@Bind
	public String customerid;

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomernamec((String) m.get("namec"));
		selectedRowData.setCustomernamee((String) m.get("namee"));
		selectedRowData.setCustomercode((String) m.get("code"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customernamec");
		this.update.markUpdate(UpdateLevel.Data, "customernamee");
		
		initCustomerInfo();
		
//		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		showCustomerWindow.close();
	}

	private void initCustomerInfo() {
		try {
			SysUserAssign sysUserAssign = this.serviceContext.sysUserAssignMgrService.sysUserAssignDao.findOneRowByClauseWhere("linkid = " + selectedRowData.getCustomerid() + " AND rolearea = 'D' AND roletype = 'C'" );
			if(sysUserAssign != null) {
				SysUser sysUser = this.serviceContext.userMgrService.sysUserDao.findById(sysUserAssign.getUserid());
				selectedRowData.setCs2(sysUser.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			selectedRowData.setCs2("");
		}
		this.update.markUpdate(UpdateLevel.Data, "cs2");
	}

	@Override
	public void add() {
		super.add();
		this.selectedRowData = new SysCorpvisit();
		this.selectedRowData.setVister(AppUtils.getUserSession().getUsercode());
		this.selectedRowData.setVisttime(Calendar.getInstance().getTime());
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Override
	public void del() {
		try {
			serviceContext.corpvisitService.corpvisitDao
					.remove(this.selectedRowData);
			MessageUtils.alert("ok");
			add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void save() {
		if(!check())return;
		super.save();
		serviceContext.corpvisitService.saveData(this.selectedRowData);
		MessageUtils.alert("ok");
	}

	private boolean check() {
		if(this.selectedRowData.getCustomerid() == null || this.selectedRowData.getCustomerid() < 0) {
			MessageUtils.alert("客户必填");
			return false;
		}
//		if(StrUtils.isNull(this.selectedRowData.getCs2())) {
//			MessageUtils.alert("迪拜客服必填");
//			return false;
//		}
		return true;
	}

	/**
	 * 附件上传事件
	 */
	@Action
	public void dtlAdd() {
		filesUpload.show();
	}

	/*********************** 文件上传处理 ********************/
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
			serviceContext.corpvisitService.saveData(this.selectedRowData);
			this.ppkVal = this.selectedRowData.getId();
			upload(request, response, filesUpload, Long.toString(ppkVal));
		} catch (Exception e) {
			MessageUtils.alert("上传失败,请尝试刷新页面,才重新上传试试!");
			MessageUtils.showException(e);
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @param response
	 * @param filesUpload
	 *            上传组件
	 * @param linkid
	 *            上传业务表主键
	 * @param path
	 *            上传服务器路径
	 * @throws IOException
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	public void upload(HttpServletRequest request,
			HttpServletResponse response, UIFileUploadDialog filesUpload,
			String linkid) throws IOException, Exception, FileNotFoundException {
		File file;
		ServletFileUpload upload = new ServletFileUpload();
		InputStream stream = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

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
						file = new File(item.getName());
						String originalFileName = file.getName();
						// 文件格式
						String suffix = "";
						if (originalFileName != null
								&& !originalFileName.trim().equals("")) {
							String[] strArr = originalFileName.split("\\.");
							if (strArr != null && strArr.length == 2) {
								suffix = strArr[1];
							}
						}

						String attachPath = AppUtils.getAttachFilePath();
						//System.out.print(attachPath);
						// AttachFile(this , linkid , originalFileName ,
						// item.getContentType());
						SysAttachment sysAttachment = new SysAttachment();
						sysAttachment.setContenttype(item.getContentType());
						sysAttachment.setLinkid(Long.valueOf(linkid));
						sysAttachment.setFilename(originalFileName);

						this.serviceContext.attachmentService
								.saveData(sysAttachment);
						// 复制文件到指定路径
						originalFileName = sysAttachment.getId()
								+ originalFileName;

						bis = new BufferedInputStream(stream);
						bos = new BufferedOutputStream(new FileOutputStream(
								new File(attachPath + originalFileName)));
						long fileSize = Streams.copy(bis, bos, true);

						// this.getDaoProxy().refreshAttachFile(attachmentPkId,
						// fileSize);
						sysAttachment.setFilesize(new BigDecimal(fileSize));

						this.serviceContext.attachmentService
								.saveData(sysAttachment);
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
	}

	@Bind
	@SaveState
	public String fileName;

	@Bind
	@SaveState
	public String contentType;

	@Bind
	@SaveState
	public Long dPkVal;

	@Bind(id = "filename")
	public List<SelectItem> getFilename() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			return CommonComBoxBean
					.getComboxItems("d.id",
							"d.filename ||'/'||d.contenttype ||'/'||d.inputer",
							"sys_attachment AS d",
							" WHERE isdelete = false AND linkid = "
									+ this.ppkVal + " ", "order by inputtime");
		} catch (Exception e) {
			MessageUtils.alert("提取上传文件名有误!");
			MessageUtils.showException(e);
			return null;
		}
	}

	/**
	 * 将选择的文件传入dPkVal
	 */
	@Action
	public void filenamechanged() {
		dPkVal = Long.valueOf(AppUtils.getReqParam("filenameid"));
	}

	@Action
	public void download() {
		if (dPkVal == null || dPkVal == 0) {
			MessageUtils.alert("请选择要下载文件!");
			return;
		} else {
			SysAttachment sysAttachment = this.serviceContext.sysAttachmentService.sysAttachmentDao
					.findById(dPkVal);
			this.fileName = sysAttachment.getFilename();
			this.contentType = sysAttachment.getContenttype();
			this.update.markUpdate(UpdateLevel.Data, "dPkVal");
		}
	}

	@Bind(id = "fileDownLoad", attribute = "src")
	private InputStream getDownload5() {
		try {
			return this.serviceContext.attachmentService.readFile(dPkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
}
