package com.scp.dao.order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

/**
 * 多线程操作工具类
 */
public class ExecutorUtils{

	public static void main(String[] args) {
		// pgsql驱动
//		Connection con1 = null;
//		try {
//			con1 = (Connection) DriverManager.getConnection("jdbc:postgresql://dev.ufms.cn:5437/dev-cimc?application_name=scp中集世倡;serverTimezone=GMT%2B8",
//			        "admin", "Cimc@2021@zjsc@*1lo0W@6");
//			Statement ps =  (Statement) con1.createStatement();
//			System.out.println("数据准备中：");
//			long ll=new Date().getTime();
//			ResultSet rs = ps.executeQuery("SELECT f_fina_arap_mysqlinfo('year="+2021+";periods="+1+";periode="+12+";userid="+81433600+"') AS araps");
//			List<Map> list = new ArrayList<Map>();
//			while(rs.next()){
//				Map<String,String> m = new HashMap<String, String>();
//				m.put("jobs", rs.getString(1));
//				list.add(m);
//	        }
//	        // 完成后关闭
//	        con1.close();
//	        executeThreadPool2(list);
//	        System.out.println("共计耗时："+(ll-new Date().getTime())/1000+"S;");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	public static ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
//	
	public static void insertJbos(List<Map> list){
    	Connection con = null;
    	Statement ps = null;
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> m = list.get(i);
			if(null == m){
				continue;
			}
			sql = "INSERT INTO ufms_business ( bizSystemType, businessNo, businessDate, approvalDate, settleOfficeMdgCode, settleOfficeName, importOrExport, typeOfGoods, customerCode, customerMdgCode, customerName, clientCode, clientMdgCode, clientName, vessel, voyage, blno, branchblno, appointmentno, pol, pod, podcountry, line, tradeway, unit, unitquantity, goodsname, piece, weight, cbm, procode, proname, saledept, busid, period, year, actsetid) values (";
			sql +=  "'"+String.valueOf(m.get("jobs")).substring(1, String.valueOf(m.get("jobs")).length()-1).replace("'", "").replace(",", "','").replace("\"", "").replace("...", ",").replace("^", "''''") + "'); \n";
//				int r = ps.executeUpdate(sql);
			try {
//				if(i%100==0){
//					System.out.println("开始执行第"+(i+1)+"条...");
//				}
				ps.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(sql);
				e.printStackTrace();
			}
		}
		try {
			ps.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
	public static void insertArap(List<Map> list){
//		String sql = "";
//		for (int i = 0; i < list.size(); i++) {
//			Map<String,String> m = list.get(i);
//			if(null == m){
//				continue;
//			}
//			sql += "INSERT INTO ufms_arap ( settleOfficeName, settleOfficeMdgCode, araptype, customerMdgCode, customerName, busno, blno, businessDate, approvalDate, proname, currency, taxamount, notaxamount, clientName, taxinitialamount, initialamount) values (";
////			String.valueOf(m.get("jobs")).length();
//			sql +=  "'"+String.valueOf(m.get("arap")).substring(1, String.valueOf(m.get("arap")).length()-1).replace("'", "").replace(",", "','").replace("\"", "").replace("...", ",").replace("^", "''''") + "'); \n";
//			
//		}
		Connection con = null;
    	Statement ps = null;
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> m = list.get(i);
			if(null == m){
				continue;
			}
			sql = "INSERT INTO ufms_arapinfo ( settleofficename, settleofficemdgcode, araptype, customermdgcode, customername, busno, blno, businessdate, approvaldate, proname, currency, taxamount, notaxamount, taxinitialamount, initialamount, arapid, period, year, vchno, actsetid) values (";
			sql +=  "'"+String.valueOf(m.get("araps")).substring(1, String.valueOf(m.get("araps")).length()-1).replace("'", "").replace(",", "','").replace("\"", "").replace("...", ",").replace("^", "''''") + "'); \n";
//			if(i%100==0){
//				System.out.println("开始执行第"+(i+1)+"条...");
//			}
//			try {
//				serviceContext.daoIbatisTemplateMySql.updateWithUserDefineSql(sql);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			try {
				int r = ps.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println(sql);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			ps.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//
	// 一个线程最大处理数据量
	private final static int THREAD_COUNT_SIZE = 1000;

	public static void executeThreadPool(List<Map> pmsProductList) {
		long ll = new Date().getTime();
		// 线程数，以5000条数据为一个线程，总数据大小除以5000，再加1
		int round = pmsProductList.size() / THREAD_COUNT_SIZE + 1;
		// 程序计数器
		final CountDownLatch count = new CountDownLatch(round);
		// 创建线程
		ExecutorService executor = Executors.newFixedThreadPool(round);
		// 分配数据
		for (int i = 0; i < round; i++) {
			int startLen = i * THREAD_COUNT_SIZE;
			int endLen = ((i + 1) * THREAD_COUNT_SIZE > pmsProductList.size() ? pmsProductList.size()
					: (i + 1) * THREAD_COUNT_SIZE);
			final List<Map> threadList = pmsProductList.subList(startLen, endLen);
			final int k = i + 1;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					// 我这里用的是 jpa的直接添加一个list，当然这里可以把list，for循环然后调用
					// 接口添加数据库,或者进行其他数据操作
					insertJbos(threadList);
					// 计数器 -1(唤醒阻塞线程)
					count.countDown();
				}
			});
		}
		try {
			// 阻塞线程(主线程等待所有子线程 一起执行业务)
			count.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 终止线程池
			// 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。若已经关闭，则调用没有其他作用。
			executor.shutdown();
			System.out.println("共计耗时："+(new Date().getTime()-ll)/1000+"S;");
			System.out.println("预期增加条数："+pmsProductList.size());
			MessageUtils.alert("导入成功...共计耗时："+(new Date().getTime()-ll)/1000+"S;");
		}
	}
	
	public static void executeThreadPool2(List<Map> pmsProductList) {
		long ll = new Date().getTime();
		// 线程数，以1000条数据为一个线程，总数据大小除以1000，再加1
		int round = pmsProductList.size() / THREAD_COUNT_SIZE + 1;
		// 程序计数器
		final CountDownLatch count = new CountDownLatch(round);
		// 创建线程
		ExecutorService executor = Executors.newFixedThreadPool(round);
		// 分配数据
		for (int i = 0; i < round; i++) {
			int startLen = i * THREAD_COUNT_SIZE;
			int endLen = ((i + 1) * THREAD_COUNT_SIZE > pmsProductList.size() ? pmsProductList.size()
					: (i + 1) * THREAD_COUNT_SIZE);
			final List<Map> threadList = pmsProductList.subList(startLen, endLen);
			final int k = i + 1;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					// 我这里用的是 jpa的直接添加一个list，当然这里可以把list，for循环然后调用
					// 接口添加数据库,或者进行其他数据操作
					insertArap(threadList);
					// 计数器 -1(唤醒阻塞线程)
					count.countDown();
				}
			});
		}
		try {
			// 阻塞线程(主线程等待所有子线程 一起执行业务)
			count.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 终止线程池
			// 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。若已经关闭，则调用没有其他作用。
			executor.shutdown();
			System.out.println("增加共计耗时："+(new Date().getTime()-ll)/1000+"S;");
			System.out.println("预期增加条数："+pmsProductList.size());
			MessageUtils.alert("导入成功...共计耗时："+(new Date().getTime()-ll)/1000+"S;");
		}
	}
}
