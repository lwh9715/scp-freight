package com.scp.schedule;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.StrUtils;

/**
 * 
 * 自动执行fixbug函数
 * 
 * @author neo 201601
 * 
 */
public class AutoPerformDMLPool {

	private static boolean isDmlRun = false;
	
	private SqlMapClient sqlMapClient;
	
	public AutoPerformDMLPool() {
		super();
		//System.out.println("AutoPerformDMLPool ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public void execute() throws Exception {
		//if(AppUtils.isDebug)return;
		////AppUtils.debug("AutoPerformDMLPool Start:" + new Date());
		if (isDmlRun) {
			//System.out.println("@@@ AutoPerformDMLPool wraning:another process is running!");
			return;
		}
		isDmlRun = true;
		try {
			fix();
		} catch (Exception e) {
			throw e;
		} finally {
			isDmlRun = false;
		}
	}

	private void fix() throws Exception {
		
		DaoIbatisTemplate daoIbatisTemplate;
		try {
			daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
		} catch (Exception e1) {
			//System.out.println(e1.getLocalizedMessage());
			isDmlRun = false;
			return;
		}
		
		sqlMapClient = daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
		String id = "-1";
		try {  
		     
		    String sql = "SELECT id , dmlsql FROM sys_dml_pool where isperform = false ORDER BY COALESCE(level,0) DESC,inputtime LIMIT 1;";
		    
		    Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		    
		    sqlMapClient.startTransaction(); 
		    
		    String dmlSql = StrUtils.getMapVal(m, "dmlsql");
		    id = StrUtils.getMapVal(m, "id");
		    
		    sqlMapClient.getCurrentConnection().createStatement().execute(dmlSql);
		    
		    sqlMapClient.commitTransaction();  
		    sqlMapClient.endTransaction();  
		    
		    String res = "UPDATE sys_dml_pool SET isperform = true , response = 'OK', updatetime = NOW() WHERE id = " + id + ";";
			daoIbatisTemplate.updateWithUserDefineSql(res);
		}catch(NoRowException e){
			
		}catch(Exception e){  
			String res = "UPDATE sys_dml_pool SET isperform = true , response = '" + e.getLocalizedMessage().replace("'", "''") + "' , updatetime = NOW() WHERE id = " + id + ";";
			daoIbatisTemplate.updateWithUserDefineSql(res);
			throw new RuntimeException (e);  
		}finally{  
	        try {  
	            sqlMapClient.endTransaction();  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	            throw new RuntimeException(e);  
	        }  
		} 
	}
}