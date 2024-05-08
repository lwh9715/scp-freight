package com.scp.schedule;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.scp.dao.sys.SysUserDao;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.StrUtils;

/**
 * 
 * 自动gps硬件信息获取
 * 航迅找的供应商，测试及演示用
 * @author neo 20150109
 * 
 */
public class AuotoGetGpsDemo {

	private static boolean isRun = false;

	public void execute() throws Exception {
		if (isRun) {
			System.out.print("Auotopotgetbus wraning:another process is running!");
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
		SysUserDao sysUserDao;
		try {
			List<Map> maps = AppUtils.getServiceContext().daoIbatisTemplate
				.queryWithUserDefineSql("SELECT DISTINCT refno FROM bus_gps WHERE COALESCE(refno,'') <> '';");
			if(maps!=null&&maps.size()>0){
				for(int i=0;i<maps.size();i++){
					//车辆登录   (获得车辆ID vid和车辆授权码vKey用于查询)
					String refno = maps.get(i).get("refno").toString();
					String returnpara = AppUtils.sendPost("http://112.74.96.123:89//gpsonline/GPSAPI"
							, "version=1&method=vLoginSystem&name="+refno+"&pwd=000000");
					if(!StrUtils.isNull(returnpara)){
						JSONObject prasejson = JSONObject.fromObject(returnpara);
						if(prasejson!=null&&Boolean.parseBoolean((prasejson.get("success").toString()))==true){//请求成功
							String vid = prasejson.get("vid").toString();
							String vKey = prasejson.get("vKey").toString();
							String getGpsreturn = AppUtils.sendPost("http://112.74.96.123:89//gpsonline/GPSAPI"
									, "version=1&method=loadLocation&vid="+vid+"&vKey="+vKey);
							JSONObject getGpsreturnjson = JSONObject.fromObject(getGpsreturn);
							if(getGpsreturnjson!=null&&Boolean.parseBoolean((getGpsreturnjson.get("success").toString()))==true){//请求成功
								System.out.println("发送post获取gps硬件信息成功:"+refno);
								sysUserDao = (SysUserDao)ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
								String sql = "SELECT f_bus_gps_creatbusgps('"+getGpsreturnjson.get("locs")+"');";
								sysUserDao.executeQuery(sql);
							}else{System.out.println("发送post获取gps硬件信息失败:"+refno+"-->"+getGpsreturn);}
						}else{System.out.println("获得车辆ID请求失败："+refno+"-->"+returnpara);}
					}else{System.out.println("获得车辆ID没有返回数据："+refno);}
				}
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
}