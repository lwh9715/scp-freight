package com.scp.schedule;

import java.util.List;
import java.util.Map;

import com.scp.service.ServiceContext;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.ConfigUtils;

/**
 * 
 * 自动g获取发票信息
 * 
 * 
 */
public class AuotoInvoiceInfo {

	private static boolean isRun = false;

	public void execute() throws Exception {
		if (isRun) {
			System.out.print("AuotoInvoiceInfo wraning:another process is running!");
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
		try {
			String identity = ConfigUtils.findSysCfgVal("invoice_identity");
			ServiceContext serviceContext = (ServiceContext)ApplicationUtilBase.getBeanFromSpringIoc("serviceContext");
			
			String sql = 
				"\nSELECT "+
				"\n	 a.id::TEXT "+
				"\nFROM fina_invoice a "+
				"\nWHERE a.isdelete = FALSE "+
				"\nAND a.isdelete = FALSE "+ 
				"\nAND a.invstatus IN('开票中','开票成功签章中','开票失败') "+
				"\nORDER BY a.id DESC LIMIT 10 "+
				"\n";
			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(lists != null && lists.size()>0){
				String[] ids = new String[lists.size()];
				for (int i = 0; i < lists.size(); i++) {
					ids[i] = (String) (lists.get(i)).get("id");
				}
				serviceContext.invoiceMgrService.getInvoiceByOrderno(ids,identity);
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
}