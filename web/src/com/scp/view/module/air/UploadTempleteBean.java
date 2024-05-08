package com.scp.view.module.air;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.ajax.ProgressAction;
import org.operamasks.faces.component.ajax.ProgressState;
import org.operamasks.faces.component.ajax.ProgressStatus;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.air.uploadtempleteBean", scope = ManagedBeanScope.REQUEST)
public class UploadTempleteBean extends GridView {

    @Bind
    @SaveState
    public String method;

    @Bind
    @SaveState
    public String jobid;

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            this.method = AppUtils.getReqParam("method");
            this.jobid = AppUtils.getReqParam("jobid");
            update.markUpdate(UpdateLevel.Data, "hidenvalue");
        }
    }


    private UIFileUpload fileUpload1;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * processListener必须绑定在一个参数为FileUploadItem的无返回值方法上，通过FileUploadItem参数，我们可以拿到
     * file input框的客户端文件名，输入框的id等信息，并可以通过FileUploadItem.openStream()方法，获取上传的文件流。
     * 这个例子里，我们简单的将文件流令存为一个服务器上的文件，在实际的应用中，这里可以将文件数据流持久到数据库中，或者
     * 做任何你想要的处理。
     *
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
            importMethod(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {

            e1.printStackTrace();
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

    // 删除上次上传的文件
    private void deleteOldFiles() {
        for (int i = 1; i < 4; i++) {
            File file = new File(getSavePath("uploadFile" + i));
            if (file.exists())
                file.delete();
        }
    }

    @Bind
    @SaveState
    private String types;
    private void importMethod(File file) throws FileNotFoundException, IOException {
        if (this.method == null || this.method.isEmpty()) {

        } else if ("importair".equals(this.method)) {
            if ("1".equals(types)) {
                importShipping(file);
            } else {
                MessageUtils.alert("请选择格式!");
            }
        }
    }

    //导入托书
    private void importShipping(File file) throws FileNotFoundException, IOException {
        Map<String, String> map = ReadExcel.importJobsForExcelAir(file);
        if (map != null && map.size() > 0) {
            StringBuffer sbsql = new StringBuffer();
            sbsql.append("SELECT f_rpt_import_airlist('cnortitle=" + map.get("cnortitle").replaceAll("'", "''") + ";");
            sbsql.append("cneetitle=" + map.get("cneetitle").replaceAll("'", "''") + ";");
            sbsql.append("notifytitle=" + map.get("notifytitle").replaceAll("'", "''") + ";");
            sbsql.append("pretrans=" + map.get("pretrans").replaceAll("'", "''") + ";");    //前程运输  ???
            sbsql.append("poa=" + map.get("poa").replaceAll("'", "''") + ";");  //收货地，对应提单PLACE OF RECEIPT  ??
//            sbsql.append("airline=" + map.get("airline").replaceAll("'", "''") + ";");    //mbl船名 ???   --->航空公司
            sbsql.append("voyage=" + map.get("voyage").replaceAll("'", "''") + ";");    //mbl船次 ???
            sbsql.append("pol=" + map.get("pol").replaceAll("'", "''") + ";");  //起飞地点
//            sbsql.append("podcode=" + map.get("podcode").replaceAll("'", "''") + ";");  //降落地点    ???
            sbsql.append("pod=" + map.get("pod").replaceAll("'", "''") + ";");
            sbsql.append("destination=" + map.get("destination").replaceAll("'", "''") + ";");
            sbsql.append("cntinfos=" + map.get("cntinfos").replaceAll("'", "''") + ";");    //标记与号码 ???
            sbsql.append("goodsinfo=" + map.get("goodsinfo").replaceAll("'", "''") + ";");  //货物描述  ???
            sbsql.append("grswgt=" + map.get("grswgt").replaceAll("'", "''") + ";");    //毛重    ???
            sbsql.append("cbm=" + map.get("cbm").replaceAll("'", "''") + ";");  //体积    ???
            sbsql.append("agentdescode=" + map.get("agentdescode").replaceAll("'", "''") + ";");  //MBL代理 ???
            sbsql.append("jobid=" + this.jobid + ";corpid=" + AppUtils.getUserSession().getCorpidCurrent() + "') AS jobid");
            Map result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
        }
    }


    // 获取在服务器端令存到的文件名，上传框fileUpload1另存为${java.io.tmpdir}/uploadFile1，
    // 上传框fileUpload2另存为${java.io.tmpdir}/uploadFile2。${java.io.tmpdir}是一个临时目录，
    // 在不同的操作系统和不同的应用服务器中，这个目录会有所不同，在tomcat中${java.io.tmpdir}目录为
    // ${TOMCAT_HOME}/temp
    private String getSaveToPath(FileUploadItem fileUploadItem) {
        return getSavePath(fileUploadItem.getName());
    }

    private String getSavePath(String fileName) {
        return System.getProperty("java.io.tmpdir") + "/" + fileName;
    }

    public UIFileUpload getFileUpload1() {
        return fileUpload1;
    }

    public void setFileUpload1(UIFileUpload fileUpload2) {
        this.fileUpload1 = fileUpload2;
    }

    /**
     * 可以在这里处理其它非file input的form元素
     */
    public void action() {
        ////System.out.println("You can process other non-fileInput fields here");
    }

    /**
     * 这里处理progress组件的状态，关于如何使用progress，请参考相关文档和rcdemos里的例子。
     *
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
        return (int) (100 * fileUpload1.getUploadingStatus().getBytesRead() /
                fileUpload1.getUploadingStatus().getContentLength());
    }

    // 转换数据尺寸为k
    public long bytesToKilo(long bytes) {
        return bytes / 1024 + ((bytes % 1024 > 0) ? 1 : 0);
    }


    //拆分子单
    @Bind
    @SaveState
    public Integer splitNum;
    @Action
    public void importSplit() {
        try {
            String querySql = "SELECT f_fina_jobs_spilt('jobid=" + this.jobid + ";inputer=" + AppUtils.getUserSession().getUsercode() + ";num=" + splitNum + "')";
            this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            this.alert("OK!");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
        }
    }
}

