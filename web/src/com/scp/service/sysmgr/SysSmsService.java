package com.scp.service.sysmgr;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysSmsDao;
import com.scp.model.sys.SysSms;
import com.scp.util.SmsUtil;
import com.scp.util.StrUtils;

@Component
public class SysSmsService{
	
	@Resource
	public SysSmsDao sysSmsDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(SysSms data) {
		if(0 == data.getId()){
			sysSmsDao.create(data);
		}else{
			sysSmsDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysSms data = sysSmsDao.findById(id);
		sysSmsDao.remove(data);
	}

	public void saveDatas(List<SysSms> datas) {
		for (SysSms sysSms : datas) {
			saveData(sysSms);
		}
	}

	public void autoSent() {
		List<SysSms> sysSmsList = sysSmsDao.findAllByClauseWhere("issent = false AND isdelete = false AND phone IS NOT NULL AND remarks IS NULL AND content IS NOT NULL ORDER BY inputtime DESC");
		
		if(sysSmsList == null || sysSmsList.size() == 0) {
			//AppUtils.debug("No sms need to send");
			return;
		}
		for (SysSms sysSms : sysSmsList) {
			String ret = SmsUtil.sent(sysSms.getPhone() , sysSms.getContent());
			//AppUtils.debug("ret:"+ret);
			if(!StrUtils.isNull(ret) && ret.startsWith("OK")) {
				sysSms.setIssent(true);
			}else {
				sysSms.setIssent(false);
			}
			sysSms.setSenttime(Calendar.getInstance().getTime());
			sysSms.setRemarks(SmsUtil.dealRetMsg(ret));
			saveData(sysSms);
		}
	}

	public String findAddressPhoneNos(String id) {
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql("select mobilephone FROM _addresslist t WHERE id in("+id+") ORDER BY code");
		if(list == null || list.size() == 0) {
			return "";
		}
		StringBuffer nosBuffer = new StringBuffer();
		for (Map m : list) {
			String nos = StrUtils.getMapVal(m, "mobilephone");
			if(!StrUtils.isNull(nos)) {
				nosBuffer.append(m.get("mobilephone"));
				if(list.size() != 1)nosBuffer.append("\n");
			}
		}
		return nosBuffer.toString();
	}

	public String[] findHisSms(String id) {
		String[] ret = new String[2];
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql("select phone , content FROM sys_sms t WHERE id in("+id+") ORDER BY issent DESC , senttime , inputtime DESC");
		if(list == null || list.size() == 0) {
			return ret;
		}
		StringBuffer nosBuffer = new StringBuffer();
		StringBuffer contextBuffer = new StringBuffer();
		for (Map m : list) {
			String nos = StrUtils.getMapVal(m, "phone");
			if(!StrUtils.isNull(nos)) {
				nosBuffer.append(nos);
				if(list.size() != 1)nosBuffer.append("\n");
			}
			String content = StrUtils.getMapVal(m, "content");
			if(!StrUtils.isNull(content)) {
				contextBuffer.append(content);
				if(list.size() != 1)contextBuffer.append("\n");
			}
		}
		ret[0] = nosBuffer.toString();
		ret[1] = contextBuffer.toString();
		return ret;
	} 
	
	public String findTemplet(String id) {
		String ret = new String();
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql("select content FROM sys_templet t WHERE id in("+id+") AND temtype = 'S' ORDER BY abstract , inputtime DESC");
		if(list == null || list.size() == 0) {
			return ret;
		}
		StringBuffer contextBuffer = new StringBuffer();
		for (Map m : list) {
			String content = StrUtils.getMapVal(m, "content");
			if(!StrUtils.isNull(content)) {
				contextBuffer.append(content);
				if(list.size() != 1)contextBuffer.append("\n");
			}
		}
		ret = contextBuffer.toString();
		return ret;
	} 
}
