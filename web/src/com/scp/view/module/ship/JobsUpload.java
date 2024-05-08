package com.scp.view.module.ship;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;

import com.scp.util.AppUtils;
import com.scp.util.ReadNinboCostExcle;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.jobsuploadBean", scope = ManagedBeanScope.REQUEST)
public class JobsUpload extends GridView {

	
	private UIFileUpload fileUpload1;
	public UIFileUpload getFileUpload1() {
        return fileUpload1;
    }
    public void setFileUpload1(UIFileUpload fileUpload2) {
        this.fileUpload1 = fileUpload2;
    }
    
    /**
     * processListener必须绑定在一个参数为FileUploadItem的无返回值方法上，通过FileUploadItem参数，我们可以拿到
     * file input框的客户端文件名，输入框的id等信息，并可以通过FileUploadItem.openStream()方法，获取上传的文件流。
     * 这个例子里，我们简单的将文件流令存为一个服务器上的文件，在实际的应用中，这里可以将文件数据流持久到数据库中，或者
     * 做任何你想要的处理。
     * @param fileUploadItem
     */
    public void processUpload(FileUploadItem fileUploadItem) {
		if (fileUploadItem.getFieldName().equals("fileUpload1"))
		deleteOldFiles();
		InputStream input = null;
		FileOutputStream output = null;
		try {
			input = fileUploadItem.openStream();
			File file = new File(getSaveToPath(fileUploadItem));
			output = new FileOutputStream(file);
			byte[] buf = new byte[4096];
			// UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
			int length = UIFileUpload.END_UPLOADING;
			while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
				output.write(buf, 0, length);
			}
			AppUtils.debug(file.getCanonicalPath());
			importNinboCostRpt(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		if (input != null){
			try {
				input.close();
			} catch (IOException e) {
			}
		}
		if (output != null){
			try {
				output.close();
			} catch (IOException e) {
			}
		}
    }
    
    // 获取在服务器端令存到的文件名，上传框fileUpload1另存为${java.io.tmpdir}/uploadFile1，
    // 上传框fileUpload2另存为${java.io.tmpdir}/uploadFile2。${java.io.tmpdir}是一个临时目录，
    // 在不同的操作系统和不同的应用服务器中，这个目录会有所不同，在tomcat中${java.io.tmpdir}目录为
    // ${TOMCAT_HOME}/temp
    private String getSaveToPath(FileUploadItem fileUploadItem) {
        return getSavePath(fileUploadItem.getName());
    }
    
    // 删除上次上传的文件
    private void deleteOldFiles() {
        for (int i = 1; i < 4; i++) {
            File file = new File(getSavePath("uploadFile" + i));
            if (file.exists())
                file.delete();
        }
    }
    
    private String getSavePath(String fileName) {
        return System.getProperty("java.io.tmpdir") + "/" + fileName;
    }
    
    private void importNinboCostRpt(File file){
    	ReadNinboCostExcle readEx = new ReadNinboCostExcle();
    	Map result = null;
    	List<String[]> list = new ArrayList<String[]>();
    	StringBuffer sbsql = new StringBuffer();
    	String[] str = null;
    	list = readEx.readMainfest(file);//解析excle得到数据
    	String args = "";
    	
    	for (int i = 0; i < list.size(); i++) {
    		if("".equals(args)){
    			args = list.get(i)[4]+","+list.get(i)[12]+","+list.get(i)[13]+","+AppUtils.getUserSession().getUserid();
    		}else{
    			args = args+";"+list.get(i)[4]+","+list.get(i)[12]+","+list.get(i)[13]+","+AppUtils.getUserSession().getUserid();
    		}
		}
    	
    	sbsql.append("SELECT f_fina_jobs_cost_ninbo('"+args+"');");
    	result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
    }
	
}
