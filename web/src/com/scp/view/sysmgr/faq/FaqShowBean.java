package com.scp.view.sysmgr.faq;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysFaq;
import com.scp.util.AppUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.sysmgr.faq.faqshowBean", scope = ManagedBeanScope.REQUEST)
public class FaqShowBean extends FormView {
	
	
	
	@SaveState
	@Accessible
	public SysFaq selectedRowData = new SysFaq();
	
	@Bind
	@SaveState
	private String title;
	
	@Bind
	@SaveState
	private String inputer;
	
	@Bind
	@SaveState
	private String content;

	@Bind
	@SaveState
	private String classify;

	@Bind
	@SaveState
	private String keywords;
	
	@Bind
	@SaveState
	private Long linkid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String id = (String) AppUtils.getReqParam("id");
			this.selectedRowData = this.serviceContext.sysFaqService.sysFaqDao.findById(Long.valueOf(id));
			
			content = (String)this.selectedRowData.getContent();
			inputer = (String)this.selectedRowData.getInputer();
			title = (String)this.selectedRowData.getSubject();
			
			classify = (String)this.selectedRowData.getClassify();
			keywords = (String)this.selectedRowData.getKeywords();
			linkid = this.selectedRowData.getId();
			
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			
			
			attachmentIframe.load(AppUtils.getContextPath()+ "/pages/module/common/attachshow.xhtml?linkid="+ id);
		}
	}

	
	@Bind
	public UIIFrame attachmentIframe;

}
