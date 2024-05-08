package com.scp.service.sysmgr;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysMsgboardDao;
import com.scp.model.sys.SysMsgboard;

@Component
public class SysMsgBoardService{
	
	@Resource
	public SysMsgboardDao sysMsgboardDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveData(SysMsgboard data) {
		if(0 == data.getId()){
			sysMsgboardDao.create(data);
		}else{
			sysMsgboardDao.modify(data);
		}
	}

	public void removeDate(Long id , String userCode) {
		String sql="update sys_msgboard set isdelete = true ,updater ='"+userCode+"' where id="+id;
		sysMsgboardDao.executeSQL(sql);
	} 

	public List<Map> findMsgData(String faqid) {
		String qrySql =
			"\n SELECT *" +
			"\n FROM _sys_msgboard f " +
			"\n WHERE (id = %s OR f.parentid = %s) AND f.isdelete = FALSE" +
			"\n ORDER BY inputtime" ;
		qrySql = String.format(qrySql, faqid , faqid , faqid);
		return daoIbatisTemplate.queryWithUserDefineSql(qrySql);
	}
	
	
}
