package com.scp.schedule;

import java.util.List;
import java.util.Map;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.StrUtils;
import com.scp.view.module.api.vgm.ApiVgmTools;

public class AutoCheckVgmStatus {
	
	private static boolean isRun = false;

	public void execute() throws Exception {
		System.out.print("AutoCheckVgmStatus execute....");
		if (isRun) {
			System.out.print("AutoCheckApiRobot wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			checkVgmStatus();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}
	

	private static void checkVgmStatus() throws Exception {
		try {  
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
		    String sql = 
		    		"\nSELECT d.vgmid  " +
		    		"\nFROM api_vgmdtl d " +
		    		"\nWHERE d.isdelete = FALSE " +
		    		"\nand d.status IS NOT NULL " +
		    		"\nAND d.status <> '' " +
		    		"\nAND d.status <> '100' " +
		    		"\nAND EXISTS(SELECT 1 FROM bus_shipping x , bus_ship_container y WHERE x.jobid = y.jobid AND y.cntno = d.containerno AND x.cls > (NOW() + '-1MONTH'))"+
		    		"\nORDER BY id DESC LIMIT 10";
		    List<Map> lists = daoIbatisTemplate.queryWithUserDefineSql(sql);
		    for (Map map : lists) {
		    	String vgmid = StrUtils.getMapVal(map, "vgmid");
				if(!StrUtils.isNull(vgmid)){
					ApiVgmTools.getVgmStatus(vgmid, "-100");
				}
			}
		}catch(NoRowException e){
		}catch(Exception e){  
			e.printStackTrace();  
		}
	}

}
