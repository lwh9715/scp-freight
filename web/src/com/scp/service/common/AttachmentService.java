package com.scp.service.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysAttachmentDao;
import com.scp.model.sys.SysAttachment;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

@Component
public class AttachmentService{
	
	@Resource
	public SysAttachmentDao sysAttachmentDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveData(SysAttachment data) {
		if(0 == data.getId()){
			sysAttachmentDao.create(data);
		}else{
			sysAttachmentDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysAttachment data = sysAttachmentDao.findById(id);
		sysAttachmentDao.remove(data);
	}

	public void removeDates(String[] ids, String user) throws Exception{
		//String id =StrTools.array2List(ids);
		for (int i = 0; i < ids.length; i++) {
//			//SOA文件删除
//			if("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_file_soa"))){
//				FileService fileService = (FileService) AppUtils.getBeanFromSpringIoc("fileService");
//				String csno = ConfigUtils.findSysCfgVal("CSNO");
//				fileService.delFile(csno, StrUtils.getMapVal(m, "id") + StrUtils.getMapVal(m, "filename"));
//			}
			
			String sql ="SELECT f_sys_attachment_del('id="+Long.valueOf(ids[i])+";user="+user+"');";
			sysAttachmentDao.executeQuery(sql);
			
			//同步删除磁盘上文件
//			Map m = this.findAttachFile(Long.valueOf(ids[i]));
//			String filepath = AppUtils.getAttachFilePath() + File.separator + StrUtils.getMapVal(m, "id") + StrUtils.getMapVal(m, "filename");
//			String fileName = StrUtils.getMapVal(m, "filename");
//			String contentType = StrUtils.getMapVal(m, "content_type");
//			File f = new File(filepath);System.out.println(filepath);
//			if(f.exists()) {
//				f.delete();
//			}
		}
		
		
	}

	public InputStream readFile(Long dPkVal) throws Exception {
		Map m = this.findAttachFile(dPkVal);
		String filepath;
		String attachmenturl = ConfigUtils.findSysCfgVal("sys_attachment_url");
		if(!StrUtils.isNull(attachmenturl)){
			filepath = attachmenturl + StrUtils.getMapVal(m, "id") + StrUtils.getMapVal(m, "filename");
		}else{
			filepath = AppUtils.getAttachFilePath() + StrUtils.getMapVal(m, "id") + StrUtils.getMapVal(m, "filename");
		}
		String fileName = StrUtils.getMapVal(m, "filename");
		String contentType = StrUtils.getMapVal(m, "content_type");
		File f = new File(filepath);
		//System.out.println("filepath:"+filepath);

		String overlaidUrl = AppUtils.getAttachFilePath() + StrUtils.getMapVal(m, "linkid") + StrUtils.getMapVal(m, "filename");
		if (new File(overlaidUrl).exists()) {
			f = new File(overlaidUrl);
			return new FileInputStream(f);
		}

		if(!f.exists()) {
			filepath = AppUtils.getAttachFilePath() + File.separator + StrUtils.getMapVal(m, "id");
			f = new File(filepath);
			if(!f.exists()) {
				File parentFile = new File(AppUtils.getAttachFilePath());
				File[] tempFile = parentFile.listFiles();
				//System.out.println("file id:"+StrUtils.getMapVal(m, "id"));
				for(int i = 0; i < tempFile.length; i++){
					//System.out.println("file Name:"+tempFile[i].getName());
					if(tempFile[i].getName().startsWith(StrUtils.getMapVal(m, "id"))){
						f = tempFile[i];
						return new FileInputStream(f);
					}
				}
				throw new Exception("Can't find the file:" + fileName + " path:"+filepath);
			}
		}
		InputStream input = new FileInputStream(f);
		return input;
	} 
	
	public Map findAttachFile(Long attachFileId) throws Exception {
		String querySql = 
			"\nSELECT " +
			"\nfilepath " +
			"\n, * " +
			"\nFROM sys_attachment " +
			"\nWHERE filename IS NOT NULL " +
			"\nAND filename <> ''" +
			"\nAND isdelete = FALSE" +
			"\nAND (id = " + attachFileId + " OR linkid = " + attachFileId + ")" +
			"\nORDER BY inputtime DESC LIMIT 1;";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		return map;
	}
	
	public String getremake(String id) {
		Long id1 = Long.valueOf(id);
		String sql = "select (CASE WHEN EXISTS (SELECT 1 FROM _wf_jobs_fcl WHERE refid = t.linkid AND isdelete = FALSE)  THEN '不可删除' ELSE '' END) AS remake FROM sys_attachment t where t.id="+id1+";";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("remake");
	}
}
