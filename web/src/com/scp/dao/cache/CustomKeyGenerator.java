package com.scp.dao.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomKeyGenerator implements KeyGenerator {
	@Override
	public Object generate(Object o, Method method, Object... objects) {
		StringBuilder sb = new StringBuilder();
		sb.append(o.getClass().getName());
		sb.append(":M:"+method.getName()+":A:");
		for (Object obj : objects) {
			sb.append(obj == null?"":obj.toString());
		}
		return sb.toString();
	}

}
