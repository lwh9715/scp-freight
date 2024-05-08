package com.scp.schedule;

import com.scp.util.SFTPUtils;
import com.ufms.web.view.sysmgr.LogBean;

import java.io.File;

/**
 * =
 */
public class AuotoPrcessSFTPFile {

    public static void main(String[] args) throws Exception {
        AuotoPrcessSFTPFile AuotoPrcessSFTPFile = new AuotoPrcessSFTPFile();
        AuotoPrcessSFTPFile.running();
    }

    private static boolean isRun = false;

    public void execute() throws Exception {
        if (isRun) {
            System.out.print("AuotoPrcessSFTPFile wraning:another process is running!");
            return;
        }
        isRun = true;
        try {
            running();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
        }
    }


    private void running() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        SFTPUtils sftp = null;
        try {
            stringBuffer.append("AuotoPrcessSFTPFile开始");
            sftp = new SFTPUtils("35.195.8.227", 2222, "cimc", "nh4vzUFMVGyr3cGdMFBp");
            sftp.connect();
            String localfilename = System.getProperty("java.io.tmpdir") + File.separator + "upload_prod_temp";
            SFTPUtils.bacthUploadFile("upload_prod", localfilename, true, stringBuffer);
        } catch (Exception e) {
            stringBuffer.append("，running异常").append(e.getMessage());
            isRun = false;
        } finally {
            sftp.disconnect();
        }
        LogBean.insertLastingLog2(stringBuffer, "AuotoPrcessSFTPFile");
    }
}