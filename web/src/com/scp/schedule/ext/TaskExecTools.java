package com.scp.schedule.ext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Calendar;

import com.scp.dao.sys.SysTimeTaskDao;
import com.scp.model.sys.SysTimeTaskLog;
import com.scp.service.sysmgr.SysTimeTaskService;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;

public class TaskExecTools {

	public static void execQuery(ScheduleJob scheduleJob) {
		String querySql = scheduleJob.getJobCommond();
		// querySql = StrUtils.getSqlFormat(querySql);
		//System.out.println("Query:" + querySql + "------" + scheduleJob.getJobName());
		try {
			SysTimeTaskDao sysTimeTaskDao = (SysTimeTaskDao) ApplicationUtilBase.getBeanFromSpringIoc("sysTimeTaskDao");
			if(querySql.startsWith("VACUUM") || querySql.startsWith("ANALYSE")){
				sysTimeTaskDao.executeSQL(querySql);
				//logDB(scheduleJob, "OK", "Exec sql in db......");
				logDB(scheduleJob, "OK", "");
			}else{
				sysTimeTaskDao.executeQuery(querySql);
				//logDB(scheduleJob, "OK", "Exec Query in db......");
				logDB(scheduleJob, "OK", "");
			}
			//System.out.println("Exec Query in db......" + scheduleJob.getJobName());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			logDB(scheduleJob, "ERROR", e.getLocalizedMessage());
			return;
		}
	}

	public static void execShell(ScheduleJob scheduleJob) {
		//System.out.println("Shell:" + scheduleJob.getJobCommond() + "------" + scheduleJob.getJobName());

		String shellCmd = scheduleJob.getJobCommond();
		String os = System.getProperty("os.name").toLowerCase();
		String info = "";
		try {
			System.out.println(shellCmd);
			if (os.indexOf("windows") >= 0) {
				info += "exec in " + os + "cmd:" + shellCmd;
			} else {
				info += "exec in " + os + "shell:" + shellCmd;
			}
			System.out.println("Exec Shell......" + scheduleJob.getJobName()
					+ " " + info);
			
			Process process = Runtime.getRuntime().exec(shellCmd);
			logProcessMessage(process.getInputStream() , scheduleJob , "OK");
			logProcessMessage(process.getErrorStream() , scheduleJob , "ERROR");
		    int value = process.waitFor();
		    System.out.println(value);
			
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			logDB(scheduleJob, "ERROR", e.getLocalizedMessage());
			return;
		}
	}

	public static void logProcessMessage(final InputStream input , final ScheduleJob scheduleJob, final String infoType) {
		new Thread(new Runnable() {
			public void run() {
				String encode = "gbk";
				String os = System.getProperty("os.name").toLowerCase();
				if(os.indexOf("windows")>=0){
					encode = "gbk";
				}else{
					encode = "utf-8";
				}
				try {
					StringBuilder stringBuilder = new StringBuilder();
					Reader reader = new InputStreamReader(input,encode);
					BufferedReader bf = new BufferedReader(reader);
					String line = null;
					while ((line = bf.readLine()) != null) {
						System.out.println(line);
						stringBuilder.append(line+"\n");
					}
					logDB(scheduleJob, infoType, stringBuilder.toString());
				} catch (Exception e) {
					e.printStackTrace();
					logDB(scheduleJob, "ERROR", e.getLocalizedMessage());
				}
			}
		}).start();
	}

	public static void logDB(ScheduleJob scheduleJob, String status, String info) {
		//if(StrUtils.isNull(info))return;
		SysTimeTaskService sysTimeTaskService = (SysTimeTaskService) AppUtils
				.getBeanFromSpringIoc("sysTimeTaskService");
		SysTimeTaskLog sysTimeTaskLog = new SysTimeTaskLog();
		sysTimeTaskLog.setId(0l);
		sysTimeTaskLog.setLogtime(Calendar.getInstance().getTime());
		sysTimeTaskLog.setLogstatus(status);
		sysTimeTaskLog.setLoginfo(info);
		sysTimeTaskLog.setTimetaskid(scheduleJob.getJobId());
		sysTimeTaskService.saveDataTaskLog(sysTimeTaskLog);
	}

	/**
	 * 通过反射调用scheduleJob中定义的JAVA方法
	 * 
	 * @param scheduleJob
	 */
	public static void execJavaMethod(ScheduleJob scheduleJob) {
		Object object = null;
		Class clazz = null;
		boolean flag = true;
		StringBuilder loginfo = new StringBuilder();
		try {
			clazz = Class.forName(scheduleJob.getBeanClass());
			object = clazz.newInstance();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			loginfo.append(e.getMessage() + "\n");
		}

		if (object == null) {
			flag = false;
			loginfo.append("任务名称 = [" + scheduleJob.getJobName()
					+ "]---------------未启动成功，请检查是否配置正确！！！\n");
			logDB(scheduleJob, "ERROR", loginfo.toString());
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			flag = false;
			loginfo.append("任务名称 = [" + scheduleJob.getJobName()
					+ "]---------------未启动成功，方法名设置错误！！！" + scheduleJob.getMethodName() + "\n");
		} catch (SecurityException e) {
			e.printStackTrace();
			loginfo.append(e.getLocalizedMessage() + "\n");
		}
		if (method != null) {
			try {
				method.invoke(object);
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
				loginfo.append(e.getLocalizedMessage() + "\n");
			}
		}
		if (flag) {
			loginfo.append("任务名称 = [" + scheduleJob.getJobName()
					+ "]----------启动成功" + "\n");
			logDB(scheduleJob, "OK", "");
		}else{
			logDB(scheduleJob, "ERROR", loginfo.toString());
		}
		//System.out.println(loginfo.toString());
	}
}