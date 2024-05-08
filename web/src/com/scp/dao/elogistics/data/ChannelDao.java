package com.scp.dao.elogistics.data;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.elogistics.data.Channel;

@Component
@Lazy(true)
public class ChannelDao extends BaseDaoImpl<Channel, Long>{
	
	protected ChannelDao(Class<Channel> persistancesClass) {
		super(persistancesClass);
	}

	public ChannelDao() {
		this(Channel.class);
	}
	
	
}
