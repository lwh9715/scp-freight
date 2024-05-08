package com.scp.schedule;

import java.util.Date;

import com.scp.util.FtpUtils;

/**
 * 自动接收so edi
 */
public class AutoSoEdi {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        System.out.println("AutoSoEdi Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ AutoSoEdi wraning:another process is running!reTryCount:" + reTryCount);
            reTryCount++;
            if (reTryCount < 5) { //按2分钟一次，5次10分钟如果之前的还没响应，重新启动执行
                return;
            } else {
                reTryCount = 0;
            }
        }
        isRun = true;
        try {
            getSoEdi();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }


    private static void getSoEdi() {
        FtpUtils.parseSoEdi();  //inttra 暂停 20220722 neo
        FtpUtils.parseSoEdiCargosmart();
    }

}