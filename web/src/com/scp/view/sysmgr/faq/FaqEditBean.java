package com.scp.view.sysmgr.faq;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysFaq;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

/**
 * 公告
 */
@ManagedBean(name = "pages.sysmgr.faq.faqeditBean", scope = ManagedBeanScope.REQUEST)
public class FaqEditBean extends FormView{
	
	@SaveState
	@Accessible
	public SysFaq selectedRowData = new SysFaq();

	@Bind
	@SaveState
	public String msgsubj;
	
	@Bind
	@SaveState
	public String classify;
	
	@Bind
	@SaveState
	public String keywords;

	@Bind
	@SaveState
	public String ckeditor;
	
	@Bind
	@SaveState
	public Long moduleid;
	
	@Bind
	public UIIFrame attachmentIframe;

	// 页面加载时执行方法
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)){
				this.selectedRowData = this.serviceContext.sysFaqService.sysFaqDao.findById(Long.valueOf(id));
				this.pkVal = selectedRowData.getId();
				msgsubj = selectedRowData.getSubject();
				ckeditor = selectedRowData.getContent();
				classify = selectedRowData.getClassify();
				keywords = selectedRowData.getKeywords();
				moduleid = selectedRowData.getModuleid();
					
				update.markUpdate(UpdateLevel.Data, "msgsubj");
				update.markUpdate(UpdateLevel.Data, "ckeditor");
				update.markUpdate(UpdateLevel.Data, "classify");
				update.markUpdate(UpdateLevel.Data, "keywords");
				//Browser.execClientScript("setValue('"+ckeditor+"');");
			}else {
				add();
			}
		}
	}
	@Action
	public void add() {
		selectedRowData = new SysFaq();
		msgsubj = "";
		ckeditor = "";
		classify = "";
		keywords = "";
		moduleid = null;
		update.markUpdate(UpdateLevel.Data, "msgsubj");
		update.markUpdate(UpdateLevel.Data, "ckeditor");
		update.markUpdate(UpdateLevel.Data, "classify");
		update.markUpdate(UpdateLevel.Data, "keywords");
		update.markUpdate(UpdateLevel.Data, "moduleid");
	}
	
	
	@Action
	public void saveAction() {
		msgsubj = AppUtils.getReqParam("msgsubj");
		classify = AppUtils.getReqParam("classify");
		keywords = AppUtils.getReqParam("keywords");
		try {
			moduleid = Long.parseLong(AppUtils.getReqParam("moduleid").toString());
		} catch (Exception e) {
			moduleid = null;
		}
		this.selectedRowData.setSubject(msgsubj);
		this.selectedRowData.setClassify(classify);
		this.selectedRowData.setKeywords(keywords);
		this.selectedRowData.setModuleid(moduleid);
		this.selectedRowData.setContent(AppUtils.getReqParam("editor1"));
		
		
		
		this.serviceContext.sysFaqService.saveData(selectedRowData);
		this.pkVal = selectedRowData.getId();
		update.markUpdate(true, UpdateLevel.Data, "formedt");
		MessageUtils.alert("OK!");
	}
	@Override
	public void refresh() {
		selectedRowData = this.serviceContext.sysFaqService.sysFaqDao.findById(this.pkVal);
		super.refresh();
	}
	
	
	@Action
	public void showAttachmentIframe() {
		try {
			if(this.pkVal==-1l){
				return;
			}else{
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.pkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
