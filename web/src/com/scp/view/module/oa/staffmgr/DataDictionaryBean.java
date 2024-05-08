package com.scp.view.module.oa.staffmgr;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.ajax.ProgressAction;
import org.operamasks.faces.component.ajax.ProgressState;
import org.operamasks.faces.component.ajax.ProgressStatus;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.oa.OaDatFiledata;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.staffmgr.datadictionaryBean", scope = ManagedBeanScope.REQUEST)
public class DataDictionaryBean extends GridFormView {
	
	@SaveState
	@Accessible
	public OaDatFiledata selectedRowData = new OaDatFiledata();
	
	@SaveState
    private Long parentid;
	
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExp = new HashMap<String, Object>();
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind(id = "gridExp")
	public UIDataGrid gridExp;
	
	@Bind(id = "gridExp", attribute = "dataProvider")
	protected GridDataProvider getExpDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridExp.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMapExp), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridExp.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMapExp));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind(id = "grid", attribute = "dataProvider")
	protected GridDataProvider getGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere1(qryMap), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere1(qryMap));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Override
	public void add() {
		selectedRowData = new OaDatFiledata();
		selectedRowData.setParentid(this.parentid);
		selectedRowData.setIsleaf(true);
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.oaDatFiledataService().oaDatFiledataDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.oaDatFiledataService().saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.gridExp.reload();
	}

	@Override
	public void del() {
		try {
			serviceContext.oaDatFiledataService()
					.removeDate(this.getGridSelectId());
			//		this.add();
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action(id = "gridExp", event = "onrowselect")
	public void gridExp_onrowselect(){
		parentid = Long.parseLong(this.gridExp.getSelectedIds()[0]);
		
		this.grid.reload();
	}
	
	@Action
	public void clearQryKeyExp() {
		if(qryMapExp != null){
			qryMapExp.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.gridExp.reload();
		}
	}
	
	public Map getQryClauseWhere1(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND parentid = " + this.parentid;
		m.put("qry", qry);
		return m;
	}
	

	@Bind
    private UIWindow uploadWindow;
    @Action
    public void upload() {
    	uploadWindow.show();
    }
	
	private UIFileUpload fileUpload1;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFileUpload3WriteTo() {
        return getSavePath("uploadFile3");
    }

    /**
     * 可以在这里处理其它非file input的form元素
     */
    public void action() {
    
    }
    String ACCESS_ID = "fF69sdGrLXuYo5OB";
	
	String ACCESS_KEY = "UW3i8WntQ7C1B2x46FA39T6TIbnEDs";

	String bucketName = "ff-zone";
	String key=null;
        // 使用默认的OSS服务器地址创建OSSClient对象。
//    OSSClient client = new OSSClient("http://oss.aliyuncs.com/",ACCESS_ID, ACCESS_KEY);
   

    /**
     * processListener必须绑定在一个参数为FileUploadItem的无返回值方法上，通过FileUploadItem参数，我们可以拿到
     * file input框的客户端文件名，输入框的id等信息，并可以通过FileUploadItem.openStream()方法，获取上传的文件流。
     * 这个例子里，我们简单的将文件流另存为一个服务器上的文件，在实际的应用中，这里可以将文件数据流持久到数据库中，或者
     * 做任何你想要的处理。
     * @param fileUploadItem
     */
    public void processUpload(FileUploadItem fileUploadItem) {
//        if (fileUploadItem.getFieldName().equals("fileUpload1"))
//            deleteOldFiles();
//		selectedRowData = serviceContext.oaDatFiledataService().oaDatFiledataDao.findById(this.pkVal);
//        InputStream input = null;
//        FileOutputStream output = null;
//      
//        try {
//        	String picname = fileUploadItem.getFieldName();
//        	String fname = fileUploadItem.getName();
//        	 key = System.currentTimeMillis()+fname;
//	        input = fileUploadItem.openStream();
//	        ObjectMetadata objectMeta = new ObjectMetadata();
//		 //   objectMeta.setContentLength(input.available());
//		    // 可以在metadata中标记文件类型
//		    objectMeta.setContentType("image/jpeg");
//		    client.putObject(bucketName, key, input, objectMeta);
//		//	uploadFile(client, bucketName, key, filename);
//			if(picname.equals("fileUpload2")){
//				String pic1="http://img.ff-zone.cn/"+key;
//				selectedRowData.setPicurl(pic1);
//				//selectedRowData.setPic2(pic2);
//				serviceContext.oaDatFiledataService().saveData(selectedRowData);
//			}
//
//        } catch (Exception e) {
//  
//        } finally {
//            if (output != null)
//                try {
//                    output.close();
//                } catch (IOException e) {
//                }
//        }
    }

    // 获取在服务器端令存到的文件名，上传框fileUpload1另存为${java.io.tmpdir}/uploadFile1，
    // 上传框fileUpload2另存为${java.io.tmpdir}/uploadFile2。${java.io.tmpdir}是一个临时目录，
    // 在不同的操作系统和不同的应用服务器中，这个目录会有所不同，在tomcat中${java.io.tmpdir}目录为
    // ${TOMCAT_HOME}/temp
    private String getSaveToPath(FileUploadItem fileUploadItem) {
        return "fileUpload1".equals(fileUploadItem.getFieldName()) ? getSavePath("uploadFile1")
                : getSavePath("uploadFile2");
    }

    private String getSavePath(String fileName) {
    	FacesContext context = FacesContext.getCurrentInstance();
    	HttpServletRequest request = (HttpServletRequest) context
		.getExternalContext().getRequest();
    	String attachpath=request.getSession().getServletContext().getRealPath("/");
        return attachpath+  "/upload/" + fileName;
        
    }

    public UIFileUpload getFileUpload1() {
        return fileUpload1;
    }

    public void setFileUpload1(UIFileUpload fileUpload2) {
        this.fileUpload1 = fileUpload2;
    }


    /**
     * 这里处理progress组件的状态，关于如何使用progress，请参考相关文档和rcdemos里的例子。
     * @param status
     */
    public void progressAction(ProgressStatus status) {
        // progress启动
        if (status.getAction().ordinal() == ProgressAction._START) {
            setStatusToStart(status);
            // progress处于轮询状态，每隔1s会到服务器端查询一次
        } else if (status.getAction().ordinal() == ProgressAction._POLL) {
            // uploading出现错误，设置progress的错误提示，并通知progress组件监控结束
            if (isErrorStatus()) {
                setStatusToError(status);
                return;
            }

            // 正在等待uploading开始，设置progress提示等待，并通知progress组件继续监控
            if (isWaittingStatus()) {
                setWaittingStatus(status);
                return;
            }
                
            // uploading已经结束，设置progress显示上传结束，并通知progress组件监控结束
            if (isCompletedStatus()) {
                setCompletedStatus(status);
                return;
            }

            // 正在上传，设置progress显示上传进度，并通知progress继续监控
            setRunningStatus(status);
            // progress已经停止，设置progress显示结束信息，并通知progress监控结束
        } else if (isStoppedStatus(status)) {
            setStoppedStatus(status);
        }
    }

    // 通知progress组件监控结束
    private void setStoppedStatus(ProgressStatus status) {
        status.setState(ProgressState.STOPPED);
    }

    // progress组件是否已经得到监控结束的通知
    private boolean isStoppedStatus(ProgressStatus status) {
        return status.getAction().ordinal() == ProgressAction._STOP;
    }

    // 上传是否出现例外，可以通过调用FileUploadItem.getUploadingStatus.getError()，获取错误信息
    private boolean isErrorStatus() {
        return fileUpload1.getUploadingStatus().getError() != null;
    }

    // 上传是否已经正常结束
    private boolean isCompletedStatus() {
        return fileUpload1.getUploadingStatus().getContentLength().equals(
                fileUpload1.getUploadingStatus().getBytesRead());
    }

    // 是否在等待启动上传任务
    private boolean isWaittingStatus() {
        return fileUpload1.getUploadingStatus().getContentLength() == null
                || fileUpload1.getUploadingStatus().getContentLength() == 0;
    }

    // 设置上传状态提示，并通知progress继续监控
    private void setRunningStatus(ProgressStatus status) {
        status.setPercentage(getPercentage());
        status.setMessage("Total " + getTotal() + "k, " + getKilosRead() + "k("
                + getPercentage() + "%) have read");
        setWaittingStatus(status);
    }

    // 设置上传结束提示，并通知progress监控结束
    private void setCompletedStatus(ProgressStatus status) {
        status.setMessage("Uploading has completed. Total " + getTotal() + "k");
        status.setPercentage(100);
        status.setState(ProgressState.COMPLETED);
    }

    // 通知progress继续监控
    private void setWaittingStatus(ProgressStatus status) {
        status.setState(ProgressState.RUNNING);
    }

    // 设置上传错误提示，并通知progress监控结束
    private void setStatusToError(ProgressStatus status) {
        status.setMessage("Uploading error: " + fileUpload1.getUploadingStatus(
                ).getError().getCause().getMessage());
        status.setState(ProgressState.COMPLETED);
    }

    // 设置准备上传提示，并通知progress继续监控
    private void setStatusToStart(ProgressStatus status) {
        status.setMessage("Ready to upload files...");
        status.setPercentage(0);
        status.setState(ProgressState.RUNNING);
    }

    // 删除上次上传的文件
    private void deleteOldFiles() {
        for (int i = 1; i < 4; i++) {
            File file = new File(getSavePath("uploadFile" + i));
            if (file.exists())
                file.delete();
        }
    }

    // 获取文件数据已上传部分的尺寸（k）
    private long getKilosRead() {
        return bytesToKilo(fileUpload1.getUploadingStatus().getBytesRead());
    }

    // 获取上传文件的总尺寸（k）
    private long getTotal() {
        return bytesToKilo(fileUpload1.getUploadingStatus().getContentLength());
    }

    // 获取文件已经上传至服务器的比例
    private int getPercentage() {
        return (int)(100 * fileUpload1.getUploadingStatus().getBytesRead() /
                fileUpload1.getUploadingStatus().getContentLength());
    }
    
    // 转换数据尺寸为k
    public long bytesToKilo(long bytes) {
        return bytes / 1024 + ((bytes % 1024 > 0) ? 1 : 0);
    }
	
	
}
