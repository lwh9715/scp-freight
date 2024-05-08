package com.scp.service.customer;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysCorpaccountDao;
import com.scp.dao.sys.SysCorporationDao;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.spreada.utils.chinese.ZHConverter;

@Component
public class CustomerMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	@Resource
	public SysCorporationDao sysCorporationDao; 
	@Resource
	public SysCorpaccountDao sysCorpaccountDao; 
	
	
	/*
	 * 检查是否重复
	 * */
	public SysCorporation repeat(Long pkVal , String name ,String value){
		try {
			String regex = "[\u3400-\u4DB5\u4E00-\u9FA5\u9FA6-\u9FBB\uF900-\uFA2D\uFA30-\uFA6A\uFA70-\uFAD9\uFF00-\uFFEF\u2E80-\u2EFF\u3000-\u303F\u31C0-\u31EF]+";
			if(value.matches(regex)){
				//如果是中文将输入值转换成简体
				ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);  
				String simplified = converter.convert(StrUtils.getSqlFormat(value));  
				String str = simplified.replaceAll("[\\pP‘’“”]", "");
				String sql = "(UPPER(TRIM("+name+")) = UPPER('"+StrUtils.getSqlFormat(simplified)+"') OR UPPER(translate("+name+",'（）()-''{}[] ','')) = UPPER('"+StrUtils.getSqlFormat(str)+"')) AND isdelete = false AND id <> "+pkVal;
				if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
					sql += "\nAND (corpid IS NULL OR corpid ="+AppUtils.getUserSession().getCorpid()+" )";//非saas模式不控制
				}
				List<SysCorporation> list = sysCorporationDao.findAllByClauseWhere(sql);
				if(list != null && list.size() > 0){
					return list.get(0);
				}else if(list.size() == 0){
					//将输入值转换成繁体
					String traditional = ZHConverter.convert(StrUtils.getSqlFormat(value), ZHConverter.TRADITIONAL);  
					String str2 = traditional.replaceAll("[\\pP‘’“”]", "");
					String sql2 = "(UPPER(TRIM("+name+")) = UPPER('"+StrUtils.getSqlFormat(traditional)+"') OR UPPER(translate("+name+",'（）()-''{}[] ','')) = UPPER('"+StrUtils.getSqlFormat(str2)+"')) AND isdelete = false AND id <> "+pkVal;
					if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
						sql2 += "\nAND (corpid IS NULL OR corpid ="+AppUtils.getUserSession().getCorpid()+" )";//非saas模式不控制
					}
					List<SysCorporation> list2 = sysCorporationDao.findAllByClauseWhere(sql2);
					if(list2 != null && list2.size() > 0){
						return list2.get(0);
					}
				}
			}else{
				//如果输入值是英文，直接比较，不做处理
				String str3 = value.replaceAll("[\\pP‘’“”]", "");
				String sql3 = "(UPPER(TRIM("+name+")) = UPPER('"+StrUtils.getSqlFormat(value)+"') OR UPPER(translate("+name+",'（）()-''{}[] ','')) = UPPER('"+StrUtils.getSqlFormat(str3)+"')) AND isdelete = false AND id <> "+pkVal;
				if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
					sql3 += "\nAND (corpid IS NULL OR corpid ="+AppUtils.getUserSession().getCorpid()+" )";//非saas模式不控制
				}
				List<SysCorporation> list3 = sysCorporationDao.findAllByClauseWhere(sql3);
				if(list3 != null && list3.size() > 0){
					return list3.get(0);
				}
			}
		} catch (NoRowException e) {
			return null;
		} 
		return null;
	}

	public void saveData(SysCorporation data) {
		if(0 == data.getId()){
			sysCorporationDao.create(data);

			String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
					+ "\nVALUES (getid(),'sys_corporation','新增','新增后数据为" + data.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
			daoIbatisTemplate.updateWithUserDefineSql(operatesql);
		}else{
			sysCorporationDao.modify(data);

			SysCorporation dataold = sysCorporationDao.findById(data.getId());
			String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
					+ "\nVALUES (getid(),'sys_corporation','修改','修改前数据为" + dataold.toString() + "\r\n\r\n 修改后数据为" + data.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
			daoIbatisTemplate.updateWithUserDefineSql(operatesql);
		}
	}

	public void removeDate(Long id) {
		SysCorporation data = sysCorporationDao.findById(id);
		sysCorporationDao.remove(data);
	} 
	
	public void delDate(Long id){
		SysCorporation dataold = sysCorporationDao.findById(id);
		String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
				+ "\nVALUES (getid(),'sys_corporation','删除','删除前数据为" + dataold.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
		daoIbatisTemplate.updateWithUserDefineSql(operatesql);

		String sql = "UPDATE sys_corporation SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE id = "+id;
		//String sql = "DELETE FROM sys_corporation WHERE id = "+id;
		sysCorporationDao.executeSQL(sql);



	}
	
	public void checkFactorys(String[] ids,String usercode) {
		String sbIds = " AND id = any(array[" + StrUtils.array2List(ids) + "])";
		String sql = "UPDATE sys_corporation SET ischeck = TRUE,checkter = '"+usercode+"',checktime = now() WHERE isdelete = FALSE AND isfactory = TRUE "+sbIds;
		sysCorporationDao.executeSQL(sql);
	}
	
	public void reverseCheckFactorys(String[] ids,String usercode) {
		String sbIds = " AND id = any(array[" + StrUtils.array2List(ids) + "])";
		String sql = "UPDATE sys_corporation SET ischeck = FALSE,checkter = '"+usercode+"',checktime = now() WHERE isdelete = FALSE AND isfactory = TRUE "+sbIds;
		sysCorporationDao.executeSQL(sql);
	}
	
	public List<Map> findJoinCorporationById(Long id){
		List<Map> list = null;
		try{
			String sql = "SELECT id FROM sys_corporation_join WHERE idfm = "+id+" AND jointype = 'J'";
			list = daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(list!=null && list.size() >0){
				return list;
			}else{
				return null;
			}
		}catch(NoRowException e){
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**海运：应付默认订舱代理，船公司	，空运：应付默认订舱代理，航空公司
	 * @param jobid
	 * @return
	 */
	public List<Map> findAgentorcarrById(Long jobid){
		List<Map> list = null;
		try{
			String sql = 
				"\nSELECT "+
				"\n	c.id,c.code ,c.abbr,c.namec  "+
				"\nFROM sys_corporation c , fina_jobs j , bus_shipping s  "+
				"\nWHERE c.isdelete = FALSE AND j.isdelete = FALSE AND s.isdelete = FALSE "+
				"\n	AND j.id = "+ jobid +
				"\n	AND j.id = s.jobid "+
				"\n	AND (CASE WHEN COALESCE(s.agentid,0)>0 THEN s.agentid = c.id ELSE COALESCE(s.carrierid,0) = c.id END) "+
				"\nUNION ALL "+
				"\nSELECT  "+
				"\n	c.id,c.code ,c.abbr,c.namec  "+
				"\nFROM sys_corporation c , fina_jobs j , bus_air s  "+
				"\nWHERE c.isdelete = FALSE AND j.isdelete = FALSE AND s.isdelete = FALSE "+
				"\n	AND j.id = " + jobid + 
				"\n	AND j.id = s.jobid "+
				"\n	AND (CASE WHEN COALESCE(s.agentid,0)>0 THEN s.agentid = c.id ELSE COALESCE(s.carrierid,0) = c.id END) "+
				"\nLIMIT 1";
			list = daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(list!=null && list.size() >0){
				return list;
			}else{
				return null;
			}
		}catch(NoRowException e){
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
