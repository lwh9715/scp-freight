package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysAttachmentDao;
import com.scp.model.sys.SysAttachment;

@Component
public class SysAttachmentService{
	
	@Resource
	public SysAttachmentDao sysAttachmentDao;

	public void saveData(SysAttachment data) {
		if(0 == data.getId()){
			sysAttachmentDao.create(data);
		}else{
			sysAttachmentDao.modify(data);
		}
	}

	public void removeQuoteDate(Long id) {
		SysAttachment data = sysAttachmentDao.findById(id);
		sysAttachmentDao.remove(data);
	}
	
	public void print(Long id, String user) {
		String sql = "\nUPDATE sys_attachment SET isprint = TRUE , printer = '"
				+ user + "' , printime = NOW() WHERE id = " + id + ";";
		sysAttachmentDao.executeSQL(sql);
	}
	
	public void detelePrint(Long id) {
		String sql = "\nUPDATE sys_attachment SET isprint = FALSE , printer = '' , printime = NULL WHERE id = " + id + ";";
		sysAttachmentDao.executeSQL(sql);
	}
	
}
