package com.scp.service.website;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.website.WebConfigDao;
import com.scp.exception.NoRowException;
import com.scp.model.website.WebConfig;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.FileOperationUtil;
import com.scp.util.StrUtils;

@Component
public class WebConfigService{
	
	@Resource
	public WebConfigDao webConfigDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(WebConfig data) {
		if (0 == data.getId()) {
			webConfigDao.create(data);
		} else {
			webConfigDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		WebConfig data = webConfigDao.findById(id);
		webConfigDao.remove(data);
	}
	
	public void refreshWebConfig(String key,String val){
		try{
			WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = '"+key+"'");
			if(webconfig!=null){
				webconfig.setVal(val);
				saveData(webconfig);
			}
		}catch(NoRowException e){
			WebConfig webconfig = new WebConfig();
			webconfig.setId(0L);
			webconfig.setKey(key);
			webconfig.setVal(val);
			saveData(webconfig);
		}
			
	}
	
	public String findWebConfig(String key){
		try{
			WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = '"+key+"'");
			if(webconfig!=null){
				return webconfig.getVal();
			}else{
				return "";
			}
		}catch(NoRowException e){
			return "";
		}
			
	}
	
	/**
	 * @param filename 文件名
	 * 用于修改保存网站设置中的banner图片json字符串
	 */
	public void saveOnWebbanner(String filename){
		try{
			WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = 'webbanner'");
			JSONArray jsonArray = JSONArray.fromObject(webconfig.getVal());
			JSONObject jo = new JSONObject();
			jo.accumulate("filename", filename);
			jsonArray.add(jo);
			webconfig.setVal(jsonArray.toString());
			saveData(webconfig);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param filename 文件名
	 * 用于修改保存网站设置中的banner图片json字符串
	 */
	public void saveOnWebnews(String filename){
		try{
			WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = 'web_news_banner'");
			JSONArray jsonArray = JSONArray.fromObject(webconfig.getVal());
			JSONObject jo = new JSONObject();
			jo.accumulate("filename", filename);
			jsonArray.add(jo);
			webconfig.setVal(jsonArray.toString());
			saveData(webconfig);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param key web_config中的key
	 * @return 返回表格对应的json
	 */
	public String getWebBannerJson(String key){
		WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = 'webbanner'");
		String value = webconfig.getVal();
		String result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		if(StrUtils.isNull(value)||"[]".equals(value)){
			return "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		String querySql = "select filename,(SELECT val FROM web_config WHERE key = 'webfinabilladdress')||'/attachment/'||filename AS filenamepath"
						+ "\nfrom jsonb_to_recordset('"+value+"') AS x(filename TEXT)";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		try{
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			Map map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param key web_config中的key
	 * @return 返回表格对应的json
	 */
	public String getWebBannerJsonnews(String key){
		WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = 'web_news_banner'");
		String value = webconfig.getVal();
		String result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		if(StrUtils.isNull(value)||"[]".equals(value)){
			return "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		String querySql = "select filename,(SELECT val FROM web_config WHERE key = 'webfinabilladdress')||'/attachment/'||filename AS filenamepath"
						+ "\nfrom jsonb_to_recordset('"+value+"') AS x(filename TEXT)";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		try{
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			Map map = list.get(0);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param key web_config中的key
	 * @param json web_config中的val
	 */
	public String saveWebBanner(String key ,String json){
		try{
			WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = '"+key+"'");
			webconfig.setVal(json);
			saveData(webconfig);
		}catch(Exception e){
			return "{\"success\": \"false\"},\"msg\":\"erro\"";
		}
		return "{\"success\": \"true\"}";
	}
	
	/**
	 * @param key web_config中的key
	 * @param filename 文件名
	 * @return
	 */
	public String deleteWebBanner(String key ,String filename){
		try{
			String[] s = filename.split(",");
			for(String fnname:s){
				//同步删除磁盘上文件
				String path = ApplicationUtilBase.getWebApplicationPath() + File.separator + "webapps";
				path += File.separator + "upload";
				FileOperationUtil.newFolder(path);
				String filepath = path + File.separator + fnname;
				
//				System.out.println(filepath);
				
				File f = new File(filepath);
				if(f.exists()) {
					f.delete();
				}
//				//SOA文件删除
//				if("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_file_soa"))){
//					FileService fileService = (FileService) AppUtils.getBeanFromSpringIoc("fileService");
//					String csno = ConfigUtils.findSysCfgVal("CSNO");
//					fileService.delFile(csno, fnname);
//				}
				//删除web_config对应val中的值
				WebConfig webconfig = webConfigDao.findOneRowByClauseWhere("key = '"+key+"'");
				JSONArray jsonArray = JSONArray.fromObject(webconfig.getVal());
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jo = jsonArray.getJSONObject(i);
					if(fnname.equals(jo.get("filename"))){
						jsonArray.remove(i);
					}
				}
				webconfig.setVal(jsonArray.toString());
				saveData(webconfig);
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"success\": \"false\"},\"msg\":\"erro\"";
		}
		return "{\"success\": \"true\"}";
	}
	
}