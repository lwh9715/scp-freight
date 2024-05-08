package com.scp.schedule;

import java.util.Date;

/**
 * 自动EdiInttra
 */
public class AutoEdiInttraProcess {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        System.out.println("AutoEdiInttraProcess Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ AutoEdiInttraProcess wraning:another process is running!reTryCount:" + reTryCount);
            reTryCount++;
            if (reTryCount < 5) { //按2分钟一次，5次10分钟如果之前的还没响应，重新启动执行
                return;
            } else {
                reTryCount = 0;
            }
        }
        isRun = true;
        try {
            ediInttraProcess();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }


    private static void ediInttraProcess() {
        // FtpUtils.ediInttraProcess();
    }

}