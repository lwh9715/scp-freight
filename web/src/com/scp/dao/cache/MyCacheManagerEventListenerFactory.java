package com.scp.dao.cache;

import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.event.CacheManagerEventListener;
import net.sf.ehcache.event.CacheManagerEventListenerFactory;

public class MyCacheManagerEventListenerFactory extends CacheManagerEventListenerFactory {

	@Override
	public CacheManagerEventListener createCacheManagerEventListener(CacheManager cacheManager, Properties properties) {
		return new MyCacheManagerEventListener(cacheManager);
	}

}