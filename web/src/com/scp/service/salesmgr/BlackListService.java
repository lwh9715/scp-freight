package com.scp.service.salesmgr;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.salesmgr.BlackListDao;
import com.scp.exception.NoRowException;
import com.scp.model.salesmgr.BlackList;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.spreada.utils.chinese.ZHConverter;

@Component
public class BlackListService{
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	@Resource
	public BlackListDao blackListDao;
	
	/*
	 * 检查是否重复
	 * */
	public BlackList repeat(Long pkVal , String name ,String value){
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
				List<BlackList> list = blackListDao.findAllByClauseWhere(sql);
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
					List<BlackList> list2 = blackListDao.findAllByClauseWhere(sql2);
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
				List<BlackList> list3 = blackListDao.findAllByClauseWhere(sql3);
				if(list3 != null && list3.size() > 0){
					return list3.get(0);
				}
			}
		} catch (NoRowException e) {
			return null;
		} 
		return null;
	}

	public void saveData(BlackList data) {
		if(0 == data.getId()){
			blackListDao.create(data);
		}else{
			blackListDao.modify(data);
		}
	}

	public void delDate(Long id){
		String sql = "UPDATE sys_corporation_black SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE id = "+id;
		blackListDao.executeSQL(sql);
	}

	public void startTrue(long id) {
		String sql = "UPDATE sys_corporation_black SET isstart = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE id = "+id;
		blackListDao.executeSQL(sql);
	}

	public void startFalse(long id) {
		String sql = "UPDATE sys_corporation_black SET isstart = FALSE , updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE id = "+id;
		blackListDao.executeSQL(sql);
	}

}
