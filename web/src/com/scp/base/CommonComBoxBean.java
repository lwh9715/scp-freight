package com.scp.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.springframework.cache.annotation.Cacheable;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.cache.CommonDBCache;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;

@ManagedBean(name = "comboxBean", scope = ManagedBeanScope.REQUEST)
public class CommonComBoxBean{
	
	@Resource
	public CommonDBCache commonDBCache;
	
	@Resource
	public ApplicationConf applicationConf;
	
	/**
	 * @param valCol val栏目名	
	 * @param labColEn lab栏目英文名
	 * @param labColZh lab栏目中文名
	 * @param tblName 表名
	 * @param whereSql 条件语句@SQL语法
	 * @param orderSql 排序语句@SQL语法
	 * @return
	 * @throws Exception
	 */
	public static List<SelectItem> getComboxItems(String valCol, String labColEn , String labColZh, 
			String tblName, String whereSql , String orderSql) throws Exception {
		String comboSqlFormat = "base.combox.qry";
		return getComboxItems(valCol, AppUtils.getColumnByCurrentLanguage(labColZh,labColEn), tblName, whereSql, orderSql);
	}
	
	public static List<SelectItem> getComboxItems(String valCol, String labCol,
			String tblName, String whereSql , String orderSql) throws Exception {
		String comboSqlFormat = "base.combox.qry";
		Map args = new HashMap();
		args.put("v", valCol);
		args.put("l", labCol);
		args.put("t", tblName);
		args.put("w", whereSql);
		args.put("o", orderSql);
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		List<SelectItem> items = new ArrayList<SelectItem>();
    	List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(comboSqlFormat,args);
    	if(list!=null){
    		Object value = null;
    		Object lable = null;
    		for (Map dept : list) {
    			lable = dept.get("lable");
    			value = dept.get("value");
				items.add(new SelectItem(String.valueOf(value),
						String.valueOf(lable)));
    		}	
    	}
		return items;
	}
	
	
	public static String getComboxItemsAsJson(String valCol,
			String tblName, String whereSql , String orderSql) throws Exception {
		String comboSqlFormat = "base.combox.qry.json";
		Map args = new HashMap();
		args.put("c", valCol);
		args.put("t", tblName);
		args.put("w", whereSql);
		args.put("o", orderSql);
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
    	List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(comboSqlFormat,args);
    	if(list != null && list.size() != 0){
    		return StrUtils.getMapVal(list.get(0), "json");
    	}
    	
		return "";
	}
	
	/**
	 * 登录用户
	 * @return
	 */
	@Bind(id="loginUser")
    public List<SelectItem> getLoginUser() {
    	try {
			return getComboxItems("d.code","d.code||'/'||COALESCE(namee,'')","d.code||'/'||COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false AND isys = TRUE","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 根据所选分公司获取登录用户
	 * @return
	 */
	@Bind(id="loginUserByBranchCom")
    public List<SelectItem> getLoginUserByBranchCom(Long corpid) {
    	try {
    		String whereSql = "WHERE corpid=" + corpid + " AND isinvalid = TRUE AND iscsuser = false AND isys = TRUE and isdelete = false";
			return getComboxItems("d.code","d.code","sys_user AS d",whereSql,"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Bind(id="loginUserId")
    public List<SelectItem> getLoginUserId() {
    	try {
			return getComboxItems("d.id","d.code||'/'||COALESCE(namee,'')","d.code||'/'||COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false and isdelete = false AND isadmin = 'N'","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="loginUserCode")
    public List<SelectItem> getLoginUserCode() {
    	try {
			return getComboxItems("d.code","d.code||'/'||COALESCE(namee,'')","d.code||'/'||COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false and isdelete = false AND isadmin = 'N'","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="allUser")
    public List<SelectItem> getAllUser() {
    	try {
			return getComboxItems("d.id","d.code||'/'||COALESCE(namee,'')","d.code||'/'||COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false and isdelete = false ","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="allUsernamec")
    public List<SelectItem> getAllUsernamec() {
    	try {
			return getComboxItems("d.namec","COALESCE(namec,'')","COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false and isdelete = false ","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * @return 提取部门名称包含 财务 的人员
	 */
	@Bind(id="financeUsernamec")
    public List<SelectItem> getFinanceUsernamec() {
    	try {
			return getComboxItems("d.namec","COALESCE(namec,'')","COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false and isdelete = false AND EXISTS(SELECT 1 FROM sys_department WHERE id = d.deptid AND name LIKE '%财务%')","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="userHaveDelAct")
    public List<SelectItem> getUserHaveDelAct() {
    	try {
			return getComboxItems("d.code","d.code||'/'||COALESCE(namee,'')","d.code||'/'||COALESCE(namec,'')","sys_user AS d","WHERE d.isinvalid = TRUE AND d.iscsuser = false and isdelete = false AND EXISTS(SELECT 1 FROM sys_role r ,sys_userinrole u WHERE u.roleid = r.id AND (r.code = 'financialMag' OR r.name = '财务经理') AND d.id =u.userid AND u.isdelete = FALSE AND r.isdelete = FALSE)","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 币制
	 * @return
	 */
	@Bind(id="currency")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCurrency() {
    	try {
    		//System.out.println("getCurrency from db....");
    		String qry = SaasUtil.filterByCorpid("d");
			return commonDBCache.getComboxItems("d.code","d.code","_dat_currency d","WHERE 1=1 "+ qry,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 本公司信息
	 * @return
	 */
	@Bind(id="companyAbbr")
    public List<SelectItem> getCompanyAbbr() {
    	try {
    		String qry = SaasUtil.filterById("d");
			return commonDBCache.getComboxItems("d.id","COALESCE(d.abbcode,'')","sys_corporation d","WHERE iscustomer=false"+ qry,"ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 本公司信息
	 * @return
	 */
	@Bind(id="company")
    public List<SelectItem> getCompany() {
    	try {
    		String qry = SaasUtil.filterById("d");
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation d","WHERE iscustomer=false" + qry,"ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 入账公司
	 * @return
	 */
	@Bind(id="actSetCompany")
    public List<SelectItem> getActSetCompany() {
    	try {
    		String qry = SaasUtil.filterById("d");
    		String filter = "\n AND isactset = TRUE";
    		filter = "";
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation d, fs_actset s","WHERE iscustomer=false  AND d.id = s.corpid" + qry + filter,"ORDER BY d.id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 按权限显示公司信息
	 * @return
	 */
	@Bind(id="companyFilter")
    public List<SelectItem> getCompanyFilter() {
    	try {
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.id AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation AS d","WHERE iscustomer=false " + qry ,"ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 分公司信息
	 * @return
	 */
	@Bind(id="branchcompany")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getBranchcompany() {
    	try {
    		List<SelectItem> lists =  commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation AS d","WHERE iscustomer=false","ORDER BY id");
    		SelectItem selectItem = new SelectItem();
    		selectItem.setLabel("全部");
    		selectItem.setValue(-999l);
    		lists.add(selectItem);
    		return lists;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * 操作公司
	 * @return
	 */
	@Bind(id="opCompany")
    public List<SelectItem> getOpCompany() {
    	try {
    		String qry = SaasUtil.filterById("d");
    		return commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation d","WHERE iscustomer=false" + qry,"ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 订单操作公司
	 * @return
	 */
	@Bind(id="opCompanyOrder")
    public List<SelectItem> getOpCompanyOrder() {
    	try {
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.id AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		if(!applicationConf.isSaas())qry = "";//非saas模式不控制
    		List<SelectItem> lists = commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation AS d","WHERE iscustomer=false" + qry,"ORDER BY id");
    		SelectItem selectItem = new SelectItem();
    		selectItem.setLabel("其他");
    		selectItem.setValue(-999l);
    		lists.add(selectItem);
    		return lists;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * 本公司信息
	 * @return
	 */
	@Bind(id="allcompany")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getAllcompany() {
    	try {
    		List<SelectItem> rs= commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation AS d","WHERE iscustomer=false","ORDER BY parentid desc , code");
    		rs.add(new SelectItem("1","All"));
			return rs;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 报关公司
	 * @return
	 */
	@Bind(id="customcompany")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCustomcompany() {
    	try {
    		List<SelectItem> items = commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation AS d"
    				,"WHERE iscustom=true AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = d.id AND jointype = 'J')","ORDER BY abbr");
    		items.add(new SelectItem(null, ""));
    		return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 报关方式
	 * @return
	 */
	@Bind(id="custype")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCustype() {
		try {
			List<SelectItem> items = commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.code) ","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 40","order by code");
			items.add(new SelectItem(null, ""));
			return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 报关类型
	 * @return
	 */
	@Bind(id="cusclass")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCusclass() {
		try {
			List<SelectItem> items = commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.code) ","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 45","order by code");
			items.add(new SelectItem(null, ""));
			return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 拖车公司
	 * @return
	 */
	@Bind(id="truckcompany")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getTruckcompany() {
    	try {
    		List<SelectItem> items = commonDBCache.getComboxItems("d.id","COALESCE(d.code,'') ||'/'|| COALESCE(d.namee,'')","COALESCE(d.code,'') ||'/'|| COALESCE(d.abbr,'')","sys_corporation AS d"
    				,"WHERE istruck=true AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = d.id AND jointype = 'J')","ORDER BY abbr");
			items.add(new SelectItem(null, ""));
    		return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 部门信息
	 * @return
	 */
	@Bind(id="dept")
    public List<SelectItem> getDept() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| COALESCE(d.namee,'')","d.code ||'/'|| COALESCE(d.name,'')","sys_department AS d","order by code","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 根据公司id获得部门信息
	 * @return
	 */
	@Bind(id="deptByCorpid")
    public List<SelectItem> getDeptByCorpid(Long corpid) {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| COALESCE(d.namee,'')","d.code ||'/'|| COALESCE(d.name,'')","sys_department AS d","WHERE corpid=" + corpid,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 业务员
	 * @return
	 */
	@Bind(id="sales")
    public List<SelectItem> getSales() {
    	try {
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		if(!applicationConf.isSaas())qry = "";//非saas模式不控制
    		
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| d.namee","d.code ||'/'|| d.namec","sys_user AS d","WHERE iscsuser=FALSE AND isinvalid = TRUE AND issales = true" + qry,"ORDER BY code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 业务员中文名，英文名，部门
	 * @return
	 */
	@Bind(id="salesDept")
    public List<SelectItem> getSalesDept() {
    	try {
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		if(!applicationConf.isSaas())qry = "";//非saas模式不控制
    		
			return commonDBCache.getComboxItems("d.id","COALESCE(d.namec,'') ||'/'|| COALESCE(d.namee,'') ||'/'|| COALESCE(d.department,'')","COALESCE(d.namec,'') ||'/'|| COALESCE(d.namee,'') ||'/'|| COALESCE(d.department,'')","_sys_user AS d","WHERE iscsuser=FALSE AND isinvalid = TRUE AND issales = true" + qry,"ORDER BY d.namec");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 文件操作
	 * @return
	 */
	@Bind(id="inputers")
    public List<SelectItem> getInputers() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| d.namee","d.code ||'/'|| d.namec","sys_user AS d","WHERE isadmin = 'N' AND isdelete = false","ORDER BY code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 操作员
	 * @return
	 */
	@Bind(id="opr")
    public List<SelectItem> getOpr() {
		try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| d.namee","d.code ||'/'|| d.namec","sys_user AS d","WHERE 1=1","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 客户
	 * @return
	 */
	@Bind(id="customer")
    public List<SelectItem> getCustomer() {
    	try {
			//return getComboxItems("d.id","d.code ||'/'|| COALESCE(d.abbr , '')","sys_corporation AS d","WHERE iscustomer='Y'","");
			return commonDBCache.getComboxItems("d.id","COALESCE(d.abbr , '') ||'/'||d.code ","sys_corporation AS d","WHERE iscustomer='Y'","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 客户Json
	 * @return
	 */
	@Bind(id="customerJson")
    public String getCustomerJson() {
		try {
			String json =  "{\"results\":"+commonDBCache.getComboxItemsAsJson("d.id , COALESCE(d.abbr,'') As name , COALESCE(d.namec,'') AS namec , COALESCE(d.namee,'') AS namee","sys_corporation AS d","WHERE d.isdelete = false AND iscustomer = true ","ORDER BY code")+"}";
			////AppUtils.debug(json);
			return json;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return "''";
		}
    }
	
	
	/**
	 * 费用名称
	 * @return
	 */
	@Bind(id="feeItem")
    public List<SelectItem> getFeeItem() {
    	try {
    		String qry = SaasUtil.filterByCorpid("d");
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| d.namee","d.code ||'/'|| d.name","_dat_feeitem AS d","WHERE 1=1"+qry,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 费用名称Json
	 * @return
	 */
	@Bind(id="feeItemJson")
    public String getFeeItemJson() {
		try {
			String qry = SaasUtil.filterByCorpid("d");
			String json =  "{\"results\":"+commonDBCache.getComboxItemsAsJson("d.id , COALESCE(d.code,'') As name , COALESCE(d.name,'') AS namec , COALESCE(d.namee,'') AS namee","_dat_feeitem AS d","WHERE d.isdelete = false "+qry,"ORDER BY code")+"}";
			////AppUtils.debug(json);
			return json;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return "''";
		}
    }
	
	
	/**
	 * 银行
	 * @return
	 */
	@Bind(id="bank")
    public List<SelectItem> getBank() {
    	try {
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		if(!applicationConf.isSaas())qry = "";//非saas模式不控制
			return commonDBCache.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.abbr,'')","_dat_bank AS d","WHERE 1=1 " + qry,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 航线
	 * @return
	 */
	@Bind(id="lineAll")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLineAll() {
    	try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namee,'')","COALESCE(d.namec,'')","dat_line AS d","WHERE d.isdelete = false","order by convert_to(namec,'GBK')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 航线
	 * @return
	 */
	@Bind(id="line")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLine() {
    	try {
    		
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namee,'')","COALESCE(d.namec,'')","dat_line AS d","WHERE d.isdelete = false AND d.lintype = 'S'","order by convert_to(namec,'GBK')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	

	
	/**
	 * 航线代码
	 * 航线代码和航线是不同的概念，此航线代码非航线档案中的代码
	 * @return
	 */
	@Bind(id="linecode")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLinecode() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","d.code","d.code","dat_linecode AS d","","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 航线
	 * @return
	 */
	@Bind(id="lineid")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLineid() {
    	try {
    		
			return commonDBCache.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.namee,'')","d.code ||'/'||  COALESCE(d.namec,'')","dat_line AS d","","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 航线
	 * @return
	 */
	@Bind(id="lineAir")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLineAir() {
    	try {
    		
			return commonDBCache.getComboxItems("d.namec","d.code ||'/'||  COALESCE(d.namee,'')","d.code ||'/'||  COALESCE(d.namec,'')","dat_line AS d","WHERE isdelete = false AND lintype = 'A'","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 航线 代码，英文，中文，处理海运委托下面，航线选择英文界面问题
	 * @return
	 */
	@Bind(id="lineShipping")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLineShipping() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,'')","COALESCE(d.namec,'')","dat_line AS d","WHERE isdelete = false and lintype = 'S'","ORDER BY code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 账号
	 * @return
	 */
	@Bind(id="account")
    public List<SelectItem> getAccount() {
    	try {
    		String filter = ConfigUtils.findSysCfgVal("fina_invoice_bank_filterbycorpid");
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		String isBycorpid = ConfigUtils.findSysCfgVal("fina_invoice_accountid_by_corpid");
    		if(applicationConf.isSaas()||"Y".equals(isBycorpid)){
    			return commonDBCache.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.accountno,'')","_dat_account AS d","WHERE 1=1 "+qry," ORDER BY d.code");
    		}else{//非saas模式不控制
    			return commonDBCache.getComboxItems("d.id","d.code||'/'||  COALESCE(d.banknamec,'') ||'/'||  COALESCE(d.accountno,'')","_dat_account AS d",""," ORDER BY d.code");
    		}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 货类
	 * @return
	 */
	@Bind(id="goodstype")
    public List<SelectItem> getGoodstype() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.namee,'')","d.code ||'/'||  COALESCE(d.namec,'')","_dat_goodstype AS d","","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 货品
	 * @return
	 */
	@Bind(id="goods")
    public List<SelectItem> getGoods() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.namec,'') ||'/'|| COALESCE(d.goodssize,'')","_dat_goods AS d","","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 单位
	 * @return
	 */
	@Bind(id="unit")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getUnit() {
    	try {
    		String qry = SaasUtil.filterByCorpid("d");
			return commonDBCache.getComboxItems("d.code","d.code","_dat_unit AS d","WHERE 1=1" + qry,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 仓库
	 * @return
	 */
	@Bind(id="warehouse")
    public List<SelectItem> getWarehouse() {
    	try {
    		String whereSql = "WHERE id = ANY(SELECT linkid FROM sys_user_assign WHERE userid=" + AppUtils.getUserSession().getUserid() + " AND linktype='W')";
			return commonDBCache.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.namec,'') ||'/'|| COALESCE(d.namee,'')","dat_warehouse AS d",whereSql,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 所有仓库
	 * @return
	 */
	@Bind(id="allwarehouse")
    public List<SelectItem> getAllwarehouse() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.comlabel,'')","_dat_warehouse_combox AS d","WHERE isdelete = FALSE","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 箱型
	 * @return
	 */
	@Bind(id="cntype")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCntype() {
    	try {
    		String qry = SaasUtil.filterByCorpid("d");
			return commonDBCache.getComboxItems("d.id","d.code","dat_cntype AS d","WHERE COALESCE(istop,FALSE) = FALSE" + qry,"ORDER BY CASE WHEN code LIKE '20''GP' THEN 0 WHEN code LIKE '40''GP' THEN 1 WHEN code LIKE '40''HQ' THEN 2 ELSE 3 END,code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 箱型(code)
	 * @return
	 */
	@Bind(id="cntypeCode")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCntypeCode() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.code","dat_cntype AS d","","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 包装
	 * @return
	 */
	@Bind(id="package")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getPackage() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.namee","dat_package AS d","WHERE isdelete = FALSE","ORDER BY d.namee");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 包装
	 * @return
	 */
	@Bind(id="packagedesc")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getPackagedesc() {
    	try {
			return commonDBCache.getComboxItems("d.namee","d.namee","dat_package AS d","WHERE isdelete = FALSE ","ORDER BY d.namee");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * 包装中文
	 * @return
	 */
	@Bind(id="packagedescc")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getPackagedescc() {
    	try {
			return commonDBCache.getComboxItems("d.namec","d.namec","dat_package AS d","WHERE isdelete = FALSE ","ORDER BY d.namec");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * hscode
	 * @return
	 */
	@Bind(id="hscode")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getHscode() {
    	try {
			return commonDBCache.getComboxItems("d.hscode","d.hscode || '/' || d.item","dat_hscode AS d","WHERE isdelete = FALSE ","ORDER BY d.hscode");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * hscode
	 * @return
	 */
	@Bind(id="hsgoodsname")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getHsgoodsname() {
    	try {
			return commonDBCache.getComboxItems("d.item"," d.item || '/' || d.hscode","dat_hscode AS d","WHERE isdelete = FALSE ","ORDER BY d.item");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 等级
	 * @return
	 */
	@Bind(id="level")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLevel() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("*", "★"));
		items.add(new SelectItem("**", "★★"));
		items.add(new SelectItem("***", "★★★"));
		items.add(new SelectItem("****", "★★★★"));
		items.add(new SelectItem("*****", "★★★★★"));
		return items;
    }
	
	@Bind(id="accountTitleType")
    public List<SelectItem> getAccountTitleType() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("A", "现金"));
		items.add(new SelectItem("B", "银行存款"));
		items.add(new SelectItem("C", "存货"));
		items.add(new SelectItem("D", "坏账准备"));
		items.add(new SelectItem("E", "流动资产"));
		items.add(new SelectItem("F", "固定资产"));
		items.add(new SelectItem("G", "累计折旧"));
		items.add(new SelectItem("H", "非流动资产"));
		items.add(new SelectItem("I", "流动负债"));
		items.add(new SelectItem("J", "非流动负债"));
		items.add(new SelectItem("K", "资本"));
		items.add(new SelectItem("L", "累计盈余"));
		items.add(new SelectItem("M", "生产成本"));
		items.add(new SelectItem("N", "收入"));
		items.add(new SelectItem("O", "其他收入"));
		items.add(new SelectItem("P", "销售成本"));
		items.add(new SelectItem("Q", "费用"));
		items.add(new SelectItem("R", "其他费用"));
		return items;
    }
	//科目类别 
	@Bind(id="actypeiddesc")
    public List<SelectItem> getActypeiddesc() {
		try {
			return commonDBCache.getComboxItems("d.id","d.name","fs_actype AS d","WHERE isdelete = false AND actsetid ="+AppUtils.getUserSession().getActsetid(),"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		} 
    }
	
	@Bind(id="accountTitleDir")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getAccountTitleDir() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("+", "借"));
		items.add(new SelectItem("-", "贷"));
		return items;
    }
	
	
	//帐套
	@Bind(id="actSetid")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getActSetid() {
		try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| COALESCE(d.name,'')||'/'||d.year||'-'||d.period","fs_actset AS d","","ORDER BY code , d.year desc");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//凭证字
	@Bind(id="vchtype")
    public List<SelectItem> getVchtype() {
		try {
			return commonDBCache.getComboxItems("d.id","d.name","fs_vchtype AS d","WHERE d.isdelete = false AND d.actsetid="+AppUtils.getUserSession().getActsetid(),"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	//凭证摘要
	@Bind(id="vchdesc")
    public List<SelectItem> getVchdesc() {
		try {
			return commonDBCache.getComboxItems("d.id","d.name","fs_vchdesc AS d","","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//科目ID
	@Bind(id="actid")
    public List<SelectItem> getActid() {
		try {
			return commonDBCache.getComboxItems("d.id","d.code||'/'||d.name ","fs_act AS d","WHERE d.isdelete = FALSE AND d.actsetid="+AppUtils.getUserSession().getActsetid(),"ORDER BY CAST(d.code AS VARCHAR)");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//科目代码 1001 1131
	@Bind(id="actcode")
    public List<SelectItem> getActcode() {
		try {
			return commonDBCache.getComboxItems("d.code","d.code||'/'||d.name ","fs_act AS d","WHERE d.isdelete = FALSE AND d.actsetid="+AppUtils.getUserSession().getActsetid(),"ORDER BY CAST(d.code AS VARCHAR)");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//科目代码 提取一级
	@Bind(id="actcodeLevel1")
    public List<SelectItem> getActcodeLevel1() {
		try {
			return commonDBCache.getComboxItems("d.code","d.code||'/'||d.name ","fs_act AS d","WHERE d.isdelete = FALSE and level = 1 AND d.actsetid="+AppUtils.getUserSession().getActsetid(),"ORDER BY CAST(d.code AS VARCHAR)");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	//核算项目分类
	@Bind(id="astypeid")
    public List<SelectItem> getAstypeid() {
		try {
			return commonDBCache.getComboxItems("d.id","d.name ","fs_astype AS d","","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//核算项目
	@Bind(id="astid")
    public List<SelectItem> getAstid() {
		try {
			return getComboxItems("d.id","d.name","fs_ast AS d","","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	// 财务币值 拼接*  * 表示没确定
	@Bind(id="fscurrency")
    public List<SelectItem> getFscurrency() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.code","_fs_currency AS d","","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	
	//起运港Air
	@Bind(id="polAir")
    public List<SelectItem> getPolAir() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.code||'/'||d.namee||'/'||d.namec,'')","dat_port AS d","WHERE d.ispol = TRUE AND d.isdelete = false AND isair = TRUE","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//目的港Air
	@Bind(id="podAir")
    public List<SelectItem> getPodAir() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.code||'/'||d.namee||'/'||d.namec,'')","dat_port AS d","WHERE d.ispod = TRUE AND d.isdelete = false AND isair = TRUE","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//卸货港Air
	@Bind(id="pddAir")
    public List<SelectItem> getPddAir() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.code||'/'||d.namee||'/'||d.namec,'')","dat_port AS d","WHERE d.ispdd = TRUE AND d.isdelete = false AND isair = TRUE","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	//起运港
	@Bind(id="pol")
    public List<SelectItem> getPol() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.ispol = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//海运起运港
	@Bind(id="shippol")
    public List<SelectItem> getShippol() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.ispol = TRUE AND isship = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//起运港中文
	@Bind(id="polc")
    public List<SelectItem> getPolc() {
    	try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namec,'')","dat_port AS d","WHERE d.ispol = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	//起运港
	@Bind(id="polid")
    public List<SelectItem> getPolid() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.namec,'')","dat_port AS d","WHERE d.ispol = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//目的港
	@Bind(id="pod")
    public List<SelectItem> getPod() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.ispod = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//海运目的港
	@Bind(id="shippod")
    public List<SelectItem> getShippod() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.ispod = TRUE AND isship = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	//起运港中文
	@Bind(id="shippodc")
	public List<SelectItem> getShippodc() {
		try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namec,'')","dat_port AS d","WHERE d.ispod = TRUE AND isship = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	//目的地
	@Bind(id="destination")
    public List<SelectItem> getDestination() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.namee,'')","dat_port AS d","WHERE d.isdestination = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//目的地
	@Bind(id="destination1")
    public List<SelectItem> getDestination1() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.isdestination = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//卸货港
	@Bind(id="pdd")
    public List<SelectItem> getPdd() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.ispdd = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//海运卸货港
	@Bind(id="shippdd")
    public List<SelectItem> getShippdd() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.ispdd = TRUE AND isship = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//起运港Json
	@Bind(id="polJson")
    public String getPolJson() {
		try {
			String json =  "{\"results\":"+commonDBCache.getComboxItemsAsJson("d.id , COALESCE(d.code,'') As name , COALESCE(d.namec,'') AS namec , COALESCE(d.namee,'') AS namee","dat_port AS d","WHERE d.ispol = TRUE AND d.isdelete = false ","ORDER BY code LIMIT 10")+"}";
			////AppUtils.debug(json);
			return json;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return "";
		}
    }
	
	//目的港Json
	@Bind(id="podJson")
    public String getPodJson() {
		try {
			String json =  "{\"results\":"+commonDBCache.getComboxItemsAsJson("d.id , COALESCE(d.code,'') As name , COALESCE(d.namec,'') AS namec , COALESCE(d.namee,'') AS namee","dat_port AS d","WHERE d.ispod = TRUE AND d.isdelete = false","ORDER BY code LIMIT 10")+"}";
			////AppUtils.debug(json);
			return json;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return "";
		}
    }
	
	//卸货港Json
	@Bind(id="pddJson")
    public String getPddJson() {
		try {
			String json =  "{\"results\":"+commonDBCache.getComboxItemsAsJson("d.id , COALESCE(d.code,'') As name , COALESCE(d.namec,'') AS namec , COALESCE(d.namee,'') AS namee","dat_port AS d","WHERE d.ispdd = TRUE AND d.isdelete = false","ORDER BY code LIMIT 10")+"}";
			////AppUtils.debug(json);
			return json;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return "";
		}
    }
	
	//船公司
	@Bind(id="shipcarrier")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getShipcarrier() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code , '') ||'/'||d.abbr ","sys_corporation AS d","WHERE iscarrier = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//客户类型
	@Bind(id="customerType")
    public List<SelectItem> getCustomerType() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec , '') ","dat_filedata AS d","WHERE isleaf = TRUE AND isdelete = false AND fkcode = 95 ","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
	//客户类型
	@Bind(id="associatedtype")
    public List<SelectItem> getAssociatedType() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec , '') ","dat_filedata AS d","WHERE isleaf = TRUE AND isdelete = false AND fkcode = 1340 ","order by orderno,code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//航空公司
	@Bind(id="airline")
    public List<SelectItem> getAirline() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code , '') ||'/'||COALESCE(d.abbr , '') ","sys_corporation AS d","WHERE isairline = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//航空公司
	@Bind(id="airAbbr")
    public List<SelectItem> getAirAbbr() {
    	try {
			return commonDBCache.getComboxItems("d.abbr","COALESCE(d.code , '') ||'/'||COALESCE(d.abbr , '') ","sys_corporation AS d","WHERE isairline = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//船公司简称
	@Bind(id="shipAbbr")
    public List<SelectItem> getShipAbbr() {
    	try {
			return commonDBCache.getComboxItems("d.abbr","COALESCE(d.abbr , '')","sys_corporation AS d","WHERE iscarrier = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by abbr");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	//订舱代理
	@Bind(id="shipagent")
    public List<SelectItem> getShipagent() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code , '') ||'/'||COALESCE(d.abbr , '') ","sys_corporation AS d","WHERE isagent = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//订舱代理简称
	@Bind(id="shipAgentAbbr")
    public List<SelectItem> getShipAgentAbbr() {
    	try {
			return commonDBCache.getComboxItems("d.abbr","COALESCE(d.abbr , '')","sys_corporation AS d","WHERE isagent = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	//订舱代理全称
	@Bind(id="shipAgentNamec")
	public List<SelectItem> getShipAgentNamec() {
		try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namec , '')","sys_corporation AS d","WHERE isagent = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	//清关行
	@Bind(id="clearancecus")
    public List<SelectItem> getClearancecus() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.code , '') ||'/'||COALESCE(d.abbr , '') ","sys_corporation AS d","WHERE isclc = TRUE AND isdelete = false AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 海运签单地点
	 * signplace
	 */
	@Bind(id="signplace")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getSignplace() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namee ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 10","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 客户来源
	 * srctype
	 */
	@Bind(id="srctype")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getSrctype() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namee ","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 90","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 报关状态
	 * ststus
	 */
	@Bind(id="ststus")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getStstus() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 100","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	
	/**
	 * 拖车方式
	 * trucktype
	 */
	@Bind(id="trucktype")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getTrucktype() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 110","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 付款地点
	 * signplace
	 */
	@Bind(id="payable")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getPayable() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namee ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 60","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * 运费条款
	 * freightitem
	 */
	@Bind(id="freightitem")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getFreightitem() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namee ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 20","order by orderno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 船名
	 */
	@Bind(id="vessels")
	public List<SelectItem> getVessels() {
    	try {
    		return commonDBCache.getComboxItems("d.vessel","d.vessel","bus_shipping AS d","WHERE d.isdelete = FALSE AND  d.vessel <> '' AND d.vessel IS NOT NULL ","GROUP BY vessel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 航次
	 */
	@Bind(id="voyages")
	public List<SelectItem> getVoyages() {
    	try {
    		return commonDBCache.getComboxItems("d.voyage","d.voyage","bus_shipping AS d","WHERE d.isdelete = FALSE AND  d.voyage <> '' AND d.voyage IS NOT NULL ","GROUP BY voyage");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 运输条款
	 * @return
	 */
	@Bind(id="carryitem")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getCarryitem() {
		try {
			return commonDBCache.getComboxItems("d.code","d.namee ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 30","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 提单运输条款
	 * @return
	 */
	@Bind(id="hblCarryitem")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getHblCarryitem() {
		try {
			return commonDBCache.getComboxItems("d.code","d.namee ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 30","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 散货类型
	 * @return
	 */
	@Bind(id="lcltype")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getLcltype() {
		try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 50","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	/**
	 * Neo 2014/5/26 9:27
	 * 工厂
	 * @return
	 */
	@Bind(id="factory")
	public List<SelectItem> getFactory() {
		try {
			return getComboxItems("d.id","COALESCE(d.abbr , '') ||'/'||d.code ","sys_corporation AS d","WHERE iscustomer = TRUE AND abbr LIKE '%工厂%'","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	/*
	 * 年
	 */
	@Bind(id="year")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getYear() {
		return commonDBCache.getYear();
	}
	
	@Bind(id="week")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getWeek() {
		return commonDBCache.getWeek();
	}
	
	
	
	/*
	 * 期间
	 */
	@Bind(id="period")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getPeriod() {
		return commonDBCache.getPeriod();
	}
	
	
	/**
	 * 登录用户
	 * @return
	 */
	@Bind(id="shipprofcl")
    public List<SelectItem> getShipprofcl() {
    	try {
			return getComboxItems("d.activityid","d.activitydesc","_t_ff_rt_taskinstance_desc AS d","WHERE processdesc = '海运流程-FCL'","ORDER BY d.orderno DESC");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//目的港代理
	@Bind(id="agentdes")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getAgentdes() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.abbr , '') ||'/'||d.code ","sys_corporation AS d","WHERE isagentdes  = TRUE AND isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/*
	 * 凭证摘要
	 */
	@Bind(id="vchdesc1")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getVchdesc1() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("A", "客户代码/简称"));
		items.add(new SelectItem("B", "收付款编号"));
		items.add(new SelectItem("C", "收付款摘要"));
		items.add(new SelectItem("D", "支票号"));
		items.add(new SelectItem("E", "工作号"));
		items.add(new SelectItem("F", "工作号/收据号"));
		items.add(new SelectItem("N", "无"));
		return items;
    }
	
	/*
	 * 代理账单agenid
	 */
	@Bind(id="agencode")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getAgencode() {
		try {
			return getComboxItems("d.id","COALESCE(d.abbr , '') ||'/'||d.code ","sys_corporation AS d","WHERE iscustomer=false and isagentdes =true","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/*
	 * 订舱状态
	 */
	@Bind(id="bookState")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getBookState(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
		items.add(new SelectItem("I", (String)l.get("订舱中")));
		items.add(new SelectItem("F", (String)l.get("订舱完成")));
		items.add(new SelectItem("C", (String)l.get("取消订舱")));
		return items;
	}
	
	/*
	 * 分派公司选择
	 */
	@Bind(id="rolearea")
    public List<SelectItem> getRolearea() {
		try {
			String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.id AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		if(!applicationConf.isSaas())qry = "";//非saas模式不控制
			return commonDBCache.getComboxItems("COALESCE(d.abbcode,'')","COALESCE(d.abbr,'')","sys_corporation AS d","WHERE iscustomer=false" + qry,"ORDER BY parentid desc , code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/*
	 * 多个操作公司选择
	 */
	@Bind(id="cropop")
    public List<SelectItem> getCropop() {
		try {
			List<SelectItem> list = commonDBCache.getComboxItems("d.id","COALESCE(d.abbr,'')","sys_corporation AS d","WHERE iscustomer=false","ORDER BY parentid desc , code");
			SelectItem sel = new SelectItem();
			//增加一组数据防止页面显示0
			sel.setLabel("");
			sel.setValue("0");
			list.add(sel);
			return list;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
	/**
	 * ERP用户
	 * @return
	 */
	@Bind(id="erpUser")
    public List<SelectItem> getErpUser() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code||'/'||COALESCE(namec,'')","sys_user AS d","WHERE isinvalid = TRUE AND iscsuser = false AND isys = TRUE","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * OA用户
	 * @return
	 */
	@Bind(id="oaUser")
    public List<SelectItem> getOaUser() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(namec,'')||'/'||COALESCE(namee,'')","_oa_userinfo AS d","WHERE isdelete=false","ORDER BY d.jobnos");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="staffMessge")
    public List<SelectItem> getStaffMessge() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(namee,'')||'/'||COALESCE(namec,'')","oa_userinfo AS d","WHERE isdelete = false","ORDER BY d.sno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="invoicesReport")
	public List<SelectItem> getInvoicesReport() {
    	try {
			return commonDBCache.getComboxItems("filename","COALESCE(code,'')||'/'||COALESCE(remarks,'')","sys_report","WHERE isdelete=false AND modcode = 'inv'","ORDER BY code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/*
	 * URL有效时间
	 */
	@Bind(id="csurlbasetime")
	public List<SelectItem> getCsurlbasetime(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		for(int i = 1;i<121;i++){
			items.add(new SelectItem(i+"", i+""));
		}
		return items;
	}
	
	/*
	 * 时区
	 */
	@Bind(id="timezone")
	public List<SelectItem> getTimezone(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		for(int i = -12;i<13;i++){
			items.add(new SelectItem(i<0?"GMT"+i:"GMT"+"+"+i, i<0?"GMT"+i:"GMT"+"+"+i));
		}
		return items;
	}
	
	/*
	 * 进口清关Cargo Location
	 * */
	@Bind(id="cargo")
	public List<SelectItem> getCargo() {
    	try {
			return getComboxItems("cargolocation","cargolocation","bus_customs","WHERE cargolocation IS NOT NULL","group BY cargolocation");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 贸易方式
	 * @return
	 */
	@Bind(id="tradeway")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getTradeway() {
		try {
			return commonDBCache.getComboxItems("d.code","d.namee ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 80","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind(id="cargotype")
    public List<SelectItem> getCargotype() {
		try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 370","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	/**
	 * 包清状态
	 * freightitem
	 */
	@Bind(id="clearingstate")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getClearingstate() {
    	try {

			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec) ","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 120","order by orderno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 货品类别
	 * @return
	 */
	@Bind(id="datgoodstype")
    public List<SelectItem>  getDatgoodstype() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.namec,'')","dat_goodstype AS d","","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * HBL类型
	 */
	@Bind(id="hbltype")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getHbltype() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 140","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * 付款方式类型
	 */
	@Bind(id="payment")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getPayment() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec,d.namee)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 330","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * MBL类型
	 */
	@Bind(id="mbltype")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getMbltype() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 150","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 分派类别
	 */
	@Bind(id="roletype")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getRoletype() {
    	try {
    		
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 160","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 进出口
	 * @return
	 */
	@Bind(id="impexp")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getImpexp() {
		//System.out.println("no cacheCombox.....getImpexp()");
		try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 170","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 跟踪流程类别
	 */
	@Bind(id="tmptypes")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getTmptypes() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 180","order by orderno , code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 驳船POL
	 */
	@Bind(id="bargepol")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getBargepol() {
    	try {
			List<SelectItem> lista = CommonComBoxBean.getComboxItems("d.polnamee","d.polnamee","price_fcl_bargefeedtl AS d","","group by polnamee order by polnamee");
			List<SelectItem> listb = CommonComBoxBean.getComboxItems("DISTINCT d.namee ", "d.namee", "dat_port AS d", "WHERE isdelete = false AND isbarge = TRUE", "order by namee");
			lista.addAll(listb);
			return lista;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="wmsinpoa")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getWmsinpoa() {
    	try {
			List<SelectItem> lista = CommonComBoxBean.getComboxItems("d.polnamee","d.polnamee||'/'||d.polnamec","price_fcl_bargefeedtl AS d","","group by polnamee,polnamec order by polnamee");
			List<SelectItem> listb = CommonComBoxBean.getComboxItems("DISTINCT d.namee ", "d.namee||'/'||d.namec", "dat_port AS d", "WHERE isdelete = false AND isbarge = TRUE", "order by namee");
			lista.addAll(listb);
			return lista;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	


	/**
	 * 驳船起运港英文
	 */
	@Bind(id="polBargee")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getPolBargee() {
    	try {
			return commonDBCache.getComboxItems("d.namee","COALESCE(d.namee,'')","dat_port AS d","WHERE d.isbarge = TRUE AND d.ispol = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 驳船起运港中文
	 */
	@Bind(id="polBargec")
    public List<SelectItem> getPolBargec() {
    	try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namec,'')","dat_port AS d","WHERE d.isbarge = TRUE AND d.ispol = TRUE AND d.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 中文名/分公司abbcode
	 */
	@Bind(id="usergetmbl")
    public List<SelectItem> getUsergetmbl() {
    	try {
			return commonDBCache.getComboxItems("u.code","COALESCE(u.namec,'')||'/'||COALESCE((select abbcode from sys_corporation where id = u.corpid),'')","sys_user AS u","WHERE u.isdelete = false","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 提单格式
	 * @return
	 */
	@Bind(id="billtype")
    public List<SelectItem> getBilltype() {
		try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 190","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 电放方式
	 * freightitem
	 */
	@Bind(id="telreltype")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getTelreltype() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 200","order by orderno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 电放格式
	 * freightitem
	 */
	@Bind(id="telrelrptidcode")
	public List<SelectItem> getTelrelrptidcode() {
    	try {
			return commonDBCache.getComboxItems("d.id","d.code ","sys_report AS d","WHERE code IS NOT NULL AND modcode = 'telrel'","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 驳船运价有效期类型
	 * bargeonboard
	 */
	@Bind(id="bargeonboard")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getBargeonboard() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 210","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 大船运价有效期类型
	 * onboard
	 */
	@Bind(id="onboard")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getOnboard() {
    	try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 220","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//模块id
	@Bind(id="moduleid")
    public List<SelectItem> getModuleid() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.name , '') ","sys_module AS d","WHERE isdelete = false and isctrl = 'N' and pid <> 0 and COALESCE(url,'')<>'' AND isleaf = 'Y'","order by id::text");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 部门信息
	 * @return
	 */
	@Bind(id="pilbookingcode")
    public List<SelectItem> getPilbookingcode() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec,'')","COALESCE(d.namec,'')","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 230","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * SALES CODE
	 * @return
	 */
	@Bind(id="salescode")
    public List<SelectItem> getSalescode() {
		try {
			return commonDBCache.getComboxItems("d.code","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 240","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 装箱类型
	 * @return
	 */
	@Bind(id="ldtype")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLdtype() {
		try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 260","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 费用名称英文
	 * @return
	 */
	@Bind(id="feeItemnamee")
    public List<SelectItem> getFeeItemnamee() {
    	try {
    		String qry = SaasUtil.filterByCorpid("d");
			return commonDBCache.getComboxItems("d.namee","d.code ||'/'|| d.namee","d.code ||'/'|| d.name","_dat_feeitem AS d","WHERE 1=1"+qry,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 职务
	 * @return
	 */
	@Bind(id="jobdesc")
    public List<SelectItem> getJobdesc() {
		try {
			List<SelectItem> items = commonDBCache.getComboxItems("d.namec","COALESCE(d.namee,d.code) ","d.namec ","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 270","order by code");
			items.add(new SelectItem(null, ""));
			return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 页面管理URL
	 * @return
	 */
	@Bind(id="url")
    public List<SelectItem> getUrl() {
    	try {
    		return commonDBCache.getComboxItems("d.url","d.url","d.url","web_pages AS d","","order by url");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 菜单管理
	 * @return
	 */
	@Bind(id="webmenu")
    public List<SelectItem> getWebmenu(Long id) {
    	try {
			return commonDBCache.getComboxItems("d.id","d.namec ||'/'|| COALESCE(d.namee,'')","d.namec ||'/'|| COALESCE(d.namee,'')","web_menu AS d","WHERE ("+id+" < 0 OR id <> "+id+")","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 铁运航线
	 * @return
	 */
	@Bind(id="lineTrain")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLineTrain() {
    	try {
			return commonDBCache.getComboxItems("trim(d.namec)","trim(COALESCE(d.namec,''))","dat_line AS d","WHERE isdelete = false AND lintype = 'T'","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 出境口岸(仓储)
	 * @return
	 */
	@Bind(id="export")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getExport() {
    	try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namec,'')","dat_filedata AS d","WHERE isdelete = false AND fkcode = 310 and isleaf = true","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 铁运出发城市
	 * @return
	 */
	@Bind(id="poaTrain")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getPoaTrain() {
    	try {
    		return CommonComBoxBean.getComboxItems("DISTINCT poa","poa","price_train AS d","WHERE poa is not null","ORDER BY poa");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 全部客户中文名
	 * @return
	 */
	@Bind(id="customernamec")
    public List<SelectItem> getCustomernamec() {
    	try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namec , d.namee)","sys_corporation AS d","WHERE iscustomer=true and isdelete = false","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 入仓方式(空运)
	 * ststus
	 */
	@Bind(id="entrytype")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getEntrytype() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec,d.namee)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 280","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 入仓货站(空运)
	 * ststus
	 */
	@Bind(id="freightstation")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getFreightstation() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec,d.namee)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 290","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	
	/**
	 * 空运航线
	 * @return
	 */
	@Bind(id="lineair")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getLineair() {
    	try {
    		
			return commonDBCache.getComboxItems("d.namec","COALESCE(d.namee,'')","COALESCE(d.namec,'')","dat_line AS d","WHERE d.isdelete = false AND d.lintype = 'A'","order by convert_to(namec,'GBK')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	
	/**
	 * 订舱代码(空运)
	 * ststus
	 */
	@Bind(id="bookingcode")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getBookingcode() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec,d.namee)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 300","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * 渠道
	 * ststus
	 */
	@Bind(id="channel")
	@Cacheable(value="cacheCombox")
    public List<SelectItem> getChannel() {
    	try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.channel,'')","dat_channel AS d","WHERE d.isdelete = false","order by id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 */
	@Bind(id="busstatus")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getBusstatus() {
    	try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 320","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	/**
	 * iftmbf报文类型
	 */
	@Bind(id="iftmbFileName")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getIftmbFileName() {
		List<SelectItem> list = new ArrayList<SelectItem>();
		SelectItem selectItem = null;
		try {
			String jsonSql = ConfigUtils.findSysCfgVal("esi_config_json");
			JSONArray json = JSONArray.fromObject(jsonSql);
			for(int i=0; i<json.size(); i++){
				selectItem = new SelectItem();
				
				JSONObject job = json.getJSONObject(i);
				if(null != job.get("edi") && "IFTMBF".equalsIgnoreCase((String) job.get("edi"))){
					selectItem.setLabel(null != job.get("rptname") ? String.valueOf(job.get("rptname")) : null);
					selectItem.setValue(job.get("rptname"));
					list.add(selectItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		return list;
    }
	
	/**
	 * iftmin报文类型
	 */
	@Bind(id="iftminFileName")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getIftminFileName() {
		List<SelectItem> list = new ArrayList<SelectItem>();
		SelectItem selectItem = null;
		try {
			String jsonSql = ConfigUtils.findSysCfgVal("esi_config_json");
			JSONArray json = JSONArray.fromObject(jsonSql);
			for(int i=0; i<json.size(); i++){
				selectItem = new SelectItem();
				
				JSONObject job = json.getJSONObject(i);
				if(null != job.get("edi") && "IFTMIN".equalsIgnoreCase((String) job.get("edi"))){
					selectItem.setLabel(null != job.get("rptname") ? String.valueOf(job.get("rptname")) : null);
					selectItem.setValue(job.get("rptname"));
					list.add(selectItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		return list;
    }


	/**
	 * 国家
	 * @return
	 */
	@Bind(id="localdtl")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getLocaldtl() {
		try {
			return commonDBCache.getComboxItems("d.id","COALESCE(code,'')||'/'||COALESCE(namec,'')","dat_country AS d","WHERE 1=1 AND isdelete=false","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	

	@Bind(id="countrync")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getCountrync() {
		try {
			return commonDBCache.getComboxItems("d.namec","COALESCE(namec,'')","dat_country AS d","WHERE 1=1 AND isdelete=false AND d.namec IS NOT NULL","ORDER BY d.namec");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}


	/**
	 * 空运机场处理到货录入接货人
	 */
	@Bind(id="receiver")
	@Cacheable(value="cacheCombox")
	public List<SelectItem> getReceiver() {
    	try {
			return commonDBCache.getComboxItems("u.namec||' '||u.mobilephone","u.namec||' '||u.mobilephone","sys_user u, sys_department d","WHERE u.isdelete = FALSE AND u.isinvalid = TRUE AND u.deptid = d.id AND d.name = '深圳空运机场办' AND (u.jobdesc = '操作' OR u.namec = '陈荣权')","order by u.namec");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	//科目ID
	@Bind(id="actidreceivable")
	public List<SelectItem> getActidreceivable() {
		try {
			return commonDBCache.getComboxItems("d.id","d.code||'/'||d.name ","fs_act AS d","WHERE d.isdelete = FALSE and code in ('113101','113103') AND d.actsetid="+AppUtils.getUserSession().getActsetid(),"ORDER BY CAST(d.code" +
					" AS VARCHAR)");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	//科目ID
	@Bind(id="actidhandle")
	public List<SelectItem> getActidhandle() {
		try {
			return commonDBCache.getComboxItems("d.id","d.code||'/'||d.name ","fs_act AS d","WHERE d.isdelete = FALSE and code in ('212101','212103') AND d.actsetid="+AppUtils.getUserSession().getActsetid(),"ORDER BY CAST(d.code" +
					" AS VARCHAR)");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	//运输方式
	@Bind(id="transportmode")
	public List<SelectItem> getTransportmode() {
		try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 350","order by orderno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	//运输类型
	@Bind(id="transporttype")
	public List<SelectItem> getTransporttype() {
		try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namee,d.namec)","d.namec","dat_filedata AS d","WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 360","order by orderno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	/*
	 * 操作公司类型
	 */
	@Bind(id="corpoptype")
    public List<SelectItem> getCorpoptype() {
		CommonDBCache commonDBCache = new CommonDBCache();
		try {
			return commonDBCache.getComboxItems("d.code","COALESCE(d.namec,'')","dat_filedata AS d","WHERE fkcode = 1320 and isdelete = FALSE AND isleaf = TRUE","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/*
	 * 获取所有流程ID及名称
	 */
	@Bind(id="allProcess")
    public List<SelectItem> getAllProcess() {
		CommonDBCache commonDBCache = new CommonDBCache();
		try {
			return commonDBCache.getComboxItems("d.id","COALESCE(d.namec,'')","bpm_process AS d"," WHERE 1=1 ","");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
}