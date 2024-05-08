package com.scp.schedule;

import java.util.Date;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;

/**
 * 自动接收邮件
 *
 * @author Neo
 */
public class EmailReceiveJob {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        System.out.println("EmailReceiveJob Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ EmailReceiveJob wraning:another process is running!reTryCount:" + reTryCount);
            reTryCount++;
            if (reTryCount < 5) { //按2分钟一次，5次10分钟如果之前的还没响应，重新启动执行
                return;
            } else {
                reTryCount = 0;
            }
        }
        isRun = true;
        try {
            receive();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }

//    public static void main(String[] args) {
//        try {
//            receive();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static void receive() throws Exception {
        try {
        	 ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
        	 serviceContext.sysEmailService.receiveEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}