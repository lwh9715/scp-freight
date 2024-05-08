package com.scp.view.module.website;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.FacesException;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.website.WebNews;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.website.webnewsBean", scope = ManagedBeanScope.REQUEST)
public class WebnewsBean extends GridFormView{
	
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	public String ckeditorStr;
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		return map;
	}
	
	
	@SaveState
	public WebNews data = new WebNews();
	
	@Override
	public void add() {
		super.add();
		data = new WebNews();
		data.setId(0L);
		Browser.execClientScript("editor1.setValue('');");
	
	}

	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.webNewsService.removeDate(Long.parseLong(id));
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.webNewsService.webNewsDao.findById(this.pkVal);
		ckeditorStr = data.getContent();
		update.markUpdate(UpdateLevel.Data, "ckeditorStr");
		
	}
	
	@Override
	public void refreshForm() {
		String js = "refreshCkEditValue();";
		Browser.execClientScript(js);
	}
	

	@Override
	protected void doServiceSave() {
		serviceContext.webNewsService.saveData(data);
		
	}
	
	@Action
	public void saveAction() {
		this.data.setContent(AppUtils.getReqParam("editor1"));
		serviceContext.webNewsService.saveData(data);
		this.pkVal = data.getId();
		super.save();
		MessageUtils.alert("OK!");
	}
	
	public void processUpload1(FileUploadItem fileUploadItem) {
		InputStream input = null;
        FileOutputStream output = null;
        if(!(this.pkVal>0)){
        	alert("请先保存！");
        	return;
        }
        try {
        	String fileName = "web_news_imgurl_" +this.data.getId()+ fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getAttachFilePath() + fileName;
            File f = new File(filepath);
            //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
           //将文件放到报表images下面logo.png
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length); //写到附件中
            }
            this.data.setImgurl(fileName);
            serviceContext.webNewsService.saveData(data);
            output.flush();
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
       	 	if (input != null){
                try {
               	 input.close();
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
            refreshImgurl();
        }
	}
	
	@Action
	private void refreshImgurl() {
		this.update.markUpdate("imgeurl");
		this.grid.reload();
	}
	
	@Action
	private void clearImgurl() {
		try {
			String logopath = this.data.getImgurl();
			if(!StrUtils.isNull(logopath)){
	            File logofile = new File(AppUtils.getAttachFilePath() +File.separator+logopath);
	           // System.out.println(AppUtils.getReportFilePath() +File.separator+logopath);
	            logofile.delete();
			}
			this.data.setImgurl("");
			serviceContext.webNewsService.saveData(data);
			refreshImgurl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public String getImgurl(){
		String logoname = this.data.getImgurl();
		if(StrUtils.isNull(logoname))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(AppUtils.getAttachFilePath() + logoname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
}
