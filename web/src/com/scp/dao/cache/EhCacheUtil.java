package com.scp.dao.cache;

import java.io.Serializable;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.cache.ehcache.EhCacheCacheManager;

import com.scp.util.AppUtils;

public class EhCacheUtil {

	public static void put(Serializable key, Serializable value) {
		EhCacheCacheManager cacheManager = (EhCacheCacheManager) AppUtils.getBeanFromSpringIoc("cacheManager");
		Cache cache = cacheManager.getCacheManager().getCache("cacheCommon");
		// 使用缓存
		Element element = new Element(key, value);
		cache.put(element);
	}

	public static Serializable getcache(Serializable key) {
		EhCacheCacheManager cacheManager = (EhCacheCacheManager) AppUtils.getBeanFromSpringIoc("cacheManager");
		Cache cache = cacheManager.getCacheManager().getCache("cacheCommon");
		return (Serializable) cache.get(key).getObjectValue();
	}
	
	public static void clear() {
		try {
			//清空Eh
			EhCacheCacheManager cacheManager = (EhCacheCacheManager) AppUtils.getBeanFromSpringIoc("cacheManager");
			if(cacheManager != null){
				cacheManager.getCacheManager().clearAll();
				System.out.println("cacheManager clearAll.....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void switchState(boolean start) {
		EhCacheCacheManager cacheManager = (EhCacheCacheManager) AppUtils.getBeanFromSpringIoc("cacheManager");
		if(!start){
			Cache cache = cacheManager.getCacheManager().getCache("cacheCommon");
			cacheManager.getCacheManager().shutdown();
		}else{
			cacheManager.getCacheManager().create(cacheManager.getCacheManager().getConfiguration());
		}
	}
	
	public static StringBuilder showCache(){
		StringBuilder stringBuilder = new StringBuilder();
		EhCacheCacheManager cacheManager = (EhCacheCacheManager) AppUtils.getBeanFromSpringIoc("cacheManager");
		String names[] = cacheManager.getCacheManager().getCacheNames();  

		String name;
        for(int i=0;i<names.length;i++){  
        	name = names[i];
            //System.out.println(name); 
            Cache cache = cacheManager.getCacheManager().getCache(name);
            stringBuilder.append("cache:" + name + " numbers:" + cache.getSize() + "\n");
            List<Serializable> keys = cache.getKeys();
            for (Serializable key : keys) {
            	Element element = cache.get(key);
            	Serializable value = null;
            	if(element != null){
            		value = (Serializable) element.getObjectValue();
            		if(value instanceof java.lang.String){
            			value = value.toString();
            		}else{
            			value = value.hashCode();
            		}
            	}
            	//System.out.println("key:" + key + " value:"+value.hashCode());  
            	stringBuilder.append("key:" + key + " value:"+value + "\n");  
			}
        }  
        
        return stringBuilder;
	}
}
