package com.scp.schedule;

import com.scp.dao.sys.SysUserDao;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;

/**
 * 
 * 自动执行fixbug函数
 * 
 * @author neo 20150109
 * 
 */
public class AutoFixBugJob {

	private static boolean isRun = false;

	public void execute() throws Exception {
		//if(AppUtils.isDebug)return;
		//AppUtils.debug("AutoFixBugJob Start:" + new Date());
		if (isRun) {
			System.out.print("AutoFixBugJob wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			fix();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}

	private void fix() throws Exception {
		String sql = "";
		SysUserDao sysUserDao;
		try {
			sysUserDao = (SysUserDao)ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
			AppUtils.checkSysModule();
			sql = "SELECT f_auto_fixbug('type=1');";
			sysUserDao.executeQuery(sql);
			sql = "SELECT f_auto_fixbug_common('type=0');";
			sysUserDao.executeQuery(sql);
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
		sysUserDao.executeQuery(sql);
//		sql = "SELECT f_auto_fix('type=0');";
//		sysUserDao.executeQuery(sql);
	}
}