package com.scp.service.sysmgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysReportDao;
import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class SysReportMgrService{
	
	@Resource
	public SysReportDao sysReportDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(SysReport data) {
		if(0 == data.getId()){
			sysReportDao.create(data);
		}else{
			sysReportDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysReport data = sysReportDao.findById(id);
		sysReportDao.remove(data);
	} 
	
	public InputStream readFile(Long dPkVal) throws Exception {
		Map m = this.findReportFile(dPkVal);
		String filepath = AppUtils.getReportFilePath() + File.separator + StrUtils.getMapVal(m, "filepath");
		String fileName = StrUtils.getMapVal(m, "filename");
		File f = new File(filepath);
		if(!f.exists()) {
			throw new Exception("Can't find the file:" + fileName);
		}
		InputStream input = new FileInputStream(f);
		return input;
	} 
	
	public Map findReportFile(Long reportFileid) throws Exception {
		String querySql = 
			"\nSELECT " +
			"\nfilepath " +
			"\n, * " +
			"\nFROM sys_report " +
			"\nWHERE filename IS NOT NULL " +
			"\nAND filename <> ''" +
			"\nAND isdelete = FALSE" +
			"\nAND id = " + reportFileid + "" +
			"\nORDER BY code;";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		return map;
	}
	
}
