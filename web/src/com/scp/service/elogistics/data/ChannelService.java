package com.scp.service.elogistics.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.elogistics.data.ChannelDao;
import com.scp.model.elogistics.data.Channel;
import com.scp.util.StrUtils;

@Component
public class ChannelService {
	
	@Resource
	public ChannelDao channelDao; 

	public void saveData(Channel data) {
		if(0 == data.getId()){
			channelDao.create(data);
		}else{
			channelDao.modify(data);
		}
	}

	public void removeDate(String[] ids, String usercode) {
		String sql = "UPDATE dat_channel SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		channelDao.executeSQL(sql);
	}
	
}
