package com.scp.schedule;

import com.scp.service.sysmgr.SysSmsService;
import com.scp.util.ApplicationUtilBase;


public class SmsSendJob {
	
	private static boolean isRun = false;
	
	public void execute() throws Exception {
		
		//AppUtils.debug("SmsSendJob Start:"+ new Date());
		if(isRun) {
			//AppUtils.debug("@@@ SmsSendJob wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			sentSms();
		} catch (Exception e) {
			throw e;
		}finally {
			isRun = false;
		}
	}
	
	private void sentSms() throws Exception{
		SysSmsService smsService = (SysSmsService)ApplicationUtilBase.getBeanFromSpringIoc("sysSmsService");
		smsService.autoSent();
    }
}