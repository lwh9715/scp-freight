package com.scp.view.sysmgr.dbmgr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.alibaba.druid.pool.DruidDataSource;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.order.ExecutorUtils;
import com.scp.dao.ship.Demo;
import com.scp.dao.sys.SysUserDao;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.view.sysmgr.cfg.BaseCfgBean;
import com.ufms.base.db.DaoUtil;

@ManagedBean(name = "pages.sysmgr.dbmgr.dbmgrBean", scope = ManagedBeanScope.REQUEST)
public class DBMgrBean extends BaseCfgBean {

	@Inject
	protected PartialUpdateManager update;

	@BeforeRender
	protected void beforeRender(boolean isPostback) {
		if (!isPostback) {
			try {
				initData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Bind
	public String degugInfo = "";

	@Override
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add(ConfigUtils.SysCfgKey.dbbackup_dbname.name());
		vector.add(ConfigUtils.SysCfgKey.dbbackup_username.name());
		vector.add(ConfigUtils.SysCfgKey.dbbackup_backuppath.name());
		vector.add(ConfigUtils.SysCfgKey.dbbackup_dbpath.name());
		vector.add(ConfigUtils.SysCfgKey.dbbackup_time.name());
		vector.add(ConfigUtils.SysCfgKey.dbbackup_filecount.name());
		return vector;
	}

	@Action
	public void save() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				ConfigUtils.refreshSysCfg(key, this.getCfgDataMap().get(key),
						AppUtils.getUserSession().getUserid());
			}
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void backupDB() {
		try {
			// DbBackUpJob backUpJob = new DbBackUpJob();
			// String tip = backUpJob.dbBackUp();

			/*
			 * ZipFileWithPassword.encryptZipFile(backuppath + "temp\\",
			 * backuppath + filename, "linjing");
			 * stringBuffer.append("\nencrypt...");
			 * stringBuffer.append("\nclear...");
			 * stringBuffer.append("\nfinish...");
			 */
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows") >= 0) {
			} else {
				String shellCmd = "cd /www/dbbackup/" + "\n";
				shellCmd += "/www/dbbackup/dump" + "\n";
				System.out.println("backup:" + shellCmd);
				executeShell(shellCmd);
			}
			//degugInfo = "OK";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void backupRpt() {
		try {
			String csno = ConfigUtils.findSysCfgVal("CSNO");
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows") >= 0) {
			} else {
				String shellCmd = "cd /www/dbbackup/" + "\n";
				shellCmd += "/www/dbbackup/autobackrpt " + csno + "\n";
				System.out.println("backup:" + shellCmd);
				executeShell(shellCmd);
			}
			//degugInfo = "OK";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void restartRptDs() {
		try {
			com.alibaba.druid.pool.DruidDataSource dataSource = (DruidDataSource) AppUtils
					.getBeanFromSpringIoc("dsRpt");
			dataSource.restart();
			degugInfo = "报表数据源已重启";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void restartMainDs() {
		try {
			com.alibaba.druid.pool.DruidDataSource dataSource = (DruidDataSource) AppUtils
					.getBeanFromSpringIoc("dataSource");
			dataSource.restart();
			degugInfo = "主数据源已重启";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void analyse() {
		SysUserDao sysUserDao = (SysUserDao) AppUtils
				.getBeanFromSpringIoc("sysUserDao");
		String sql = "ANALYZE;";
		try {
			sysUserDao.executeSQL(sql);
			degugInfo = "ANALYZE Success!";
		} catch (Exception e) {
			e.printStackTrace();
		}

		sql = "VACUUM;";
		try {
			sysUserDao.executeSQL(sql);
			degugInfo += "VACUUM Success!";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getQuerySql() {
		return "\nSELECT * " + "\nFROM sys_config " + "\nWHERE 1=1 ";
	}

	@Bind
	@SaveState
	public String sqlText;

	@Bind
	@SaveState
	public String sqlResult;

	@Action
	public void execQuery() {
		try {
			String querySql = sqlText;
			System.out.println(sqlResult);
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
					.getBeanFromSpringIoc("daoIbatisTemplate");
			sqlResult = DaoUtil.queryForJsonArrays(querySql);
			update.markUpdate(true, UpdateLevel.Data, "sqlResult");
			Browser.execClientScript("showSqlResult()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Action
	public void restartWEB() {
		try {
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows") >= 0) {
			} else {
				String shellCmd = " /www/deploy-restart" + "\n";
				System.out.println("restartWEB:" + shellCmd);
				executeShell(shellCmd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Action
	public void restartDB() {
		try {
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows") >= 0) {
			} else {
				String shellCmd = "service postgresql restart" + "\n";
				System.out.println("restartDB:" + shellCmd);
				executeShell(shellCmd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void listBackFiles() {
		String filePath = "/www/dbbackup/";
        File file = new File(filePath);
        //判断文件或目录是否存在
        if(!file.exists()){
            return;
        }
        //获取该文件夹下所有的文件
        File[] fileArray= file.listFiles();
        File files = null;
        for(int i =0;i<fileArray.length;i++){
        	files = fileArray[i];
            String fileName = files.getName();
            if(fileName.indexOf(".zip")>1){
            	degugInfo += fileName + "\n";
            }
        }
	}
	
	@Bind
	@SaveState
	public Integer year;
	
	@Bind
	@SaveState
	public Integer period;
	
    @Action
    public void insertJobsToCIMC(){
    	long lo = (new Date()).getTime();
    	System.out.println(lo);
		StringBuffer sbsql = new StringBuffer();
		Connection con = null;
    	Statement ps = null;
		try {
			//根据一个往来单位、收付款、币种分组统计数据
			ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
			sbsql.append("SELECT f_fina_jobs_mysqlinfo2('year="+year+";period="+period+"') as jobs;");
			List<Map> list = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbsql.toString());
			if(list==null || list.size() == 0){
				System.out.println("无数据："+(new Date()).getTime());
				return;
			}
	    	try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	// mysql驱动
			try {
				con = (Connection) DriverManager.getConnection("jdbc:mysql://120.25.254.109:3306/sc?useUnicode=true&characterEncoding=utf8",
				        "scuser", "K%%3q7tj91");
				ps =  (Statement) con.createStatement();
				int r = ps.executeUpdate("delete from ufms_business where year="+year+" AND period="+period+";");
				System.out.println("已删除"+r+"条");
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ps.close();
				con.close();
			}
			ExecutorUtils.executeThreadPool(list);
		} catch (Exception e) {
			System.out.println("错误："+(new Date()).getTime());
			e.printStackTrace();
		}finally{
		}
		System.out.println("共计耗时（ms）："+((new Date()).getTime()-lo));
    }
    
    @Action
    public void insertArapToCIMC(){
    	long lo = (new Date()).getTime();
    	System.out.println(lo);
		StringBuffer sbsql = new StringBuffer();
		MessageUtils.alert("导入中...");
		Connection con = null;
    	Statement ps = null;
		try {
			//根据一个往来单位、收付款、币种分组统计数据
			ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
			sbsql.append("SELECT f_fina_arap_mysqlinfo2('year="+year+";period="+period+";userid="+AppUtils.getUserSession().getUserid()+";') AS araps");
			List<Map> list = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbsql.toString());
			if(list==null || list.size() == 0){
				System.out.println("无数据："+(new Date()).getTime());
				return;
			}
			Map<String,Long> m = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT actsetid FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()+" AND isdelete =FALSE");
	    	try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	// mysql驱动
//			try {
//				con = (Connection) DriverManager.getConnection("jdbc:mysql://120.25.254.109:3306/sc?useUnicode=true&characterEncoding=utf8",
//				        "scuser", "K%%3q7tj91");
//				ps =  (Statement) con.createStatement();
//				int r = ps.executeUpdate("delete from ufms_arapinfo where actsetid = "+m.get("actsetid")+";");
//				System.out.println("已删除"+r+"条");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}finally{
//				ps.close();
//				con.close();
//			}
			ExecutorUtils.executeThreadPool2(list);
		} catch (Exception e) {
			System.out.println("错误："+(new Date()).getTime());
			e.printStackTrace();
		}finally{
		}
		System.out.println("共计耗时（ms）："+((new Date()).getTime()-lo));
    }
    
    
    @Action
    public void importInfo(){
    	long lo = (new Date()).getTime();
		MessageUtils.alert("导入中...");
		try {
			String sql = Demo.readInfo();
			
			ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			System.out.println("错误："+(new Date()).getTime());
			e.printStackTrace();
		}finally{
		}
		System.out.println("共计耗时（ms）："+((new Date()).getTime()-lo));
    }
	

	@Action
	public void restartBackUpShell() {
		restartBackUpShellDB();
		restartBackUpShellRpt();
		degugInfo = "OK";
	}

	public void restartBackUpShellDB() {
		
	}

	@Action
	public void restartBackUpShellRpt() {
		
	}

//	public void executeShell(String shell) throws IOException, InterruptedException {
//		Runtime rt = Runtime.getRuntime();// 得到jvm的运行环境
//		executeShell executeShell;
//		// 调用 cmd:
//		// System.out.println(cmdbuf);
//		String OS = System.getProperty("os.name").toLowerCase();
//		System.out.println("OS:" + OS);
//		if (OS.indexOf("windows") >= 0) {
//			// executeShell = rt.exec("cmd /c " + cmdbuf.toString());
//			executeShell = rt.exec(new String[] { "cmd.exe", "/C", shell });
//		} else {
//			executeShell = rt.exec(new String[] { "/bin/sh", "-c", shell });
//		}
//		
//		if (executeShell != null) {
//			degugInfo += "进程号：" + executeShell.toString() + "\r\n";
//            executeShell.waitFor();
//        } else {
//        	degugInfo += "没有pid\r\n";
//        }
//	}
	
	public int executeShell(String shellCommand) throws IOException {
		
		//基本路径
	    String basePath = "/www/dbbackup/";
	    //记录Shell执行状况的日志文件的位置(绝对路径)
	    String executeShellLogFile = basePath + "executeShell.log";
		
        int success = 0;
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        //格式化日期时间，记录日志时使用  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");

        try {
            stringBuffer.append(dateFormat.format(new Date())).append("准备执行Shell命令 ").append(shellCommand).append(" \r\n");

            Process pid = null;
            String[] cmd = {"/bin/sh", "-c", shellCommand};
            //执行Shell命令
            pid = Runtime.getRuntime().exec(cmd);
            if (pid != null) {
                stringBuffer.append("进程号：").append(pid.toString()).append("\r\n");
                //bufferedReader用于读取Shell的输出内容 
                bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                pid.waitFor();
            } else {
                stringBuffer.append("没有pid\r\n");
            }
            stringBuffer.append(dateFormat.format(new Date())).append("Shell命令执行完毕\r\n执行结果为：\r\n");
            System.out.println("restartBackUpShell:" + stringBuffer.toString());
            String line = null;
            //读取Shell的输出内容，并添加到stringBuffer中
            while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
            	System.out.println("restartBackUpShell:" + line);
                //stringBuffer.append(line).append("\r\n");
            }
        } catch (Exception ioe) {
        	ioe.printStackTrace();
            stringBuffer.append("执行Shell命令时发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
        } finally {
            if (bufferedReader != null) {
                OutputStreamWriter outputStreamWriter = null;
                try {
                    bufferedReader.close();
                    //将Shell的执行情况输出到日志文件中
                    OutputStream outputStream = new FileOutputStream(executeShellLogFile);
                    outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    outputStreamWriter.write(stringBuffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    outputStreamWriter.close();
                }
            }
            success = 1;
        }
        degugInfo = stringBuffer.toString();
        return success;
    }
}
