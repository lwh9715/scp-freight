package com.ufms.web.view.im;

import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;


@Component("imService")
public class ImService {

	Logger logger = Logger.getLogger(ImService.class.getName());
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveHistory(TransMessage transMessages) {
		if(transMessages != null){
			String csno = transMessages.getCsno();
			String sendid = transMessages.getSendid();
			String receiveid = transMessages.getReceiveid();
			String msg = transMessages.getMsg();
			String sendtime = transMessages.getSendTime();
			
			msg = msg.replaceAll("'", "''");
			
			String sql = "\nINSERT INTO im_history(id,csno,sendid,receiveid,msg,sendtime,receivetime) " +
					"						VALUES(getid_tmp(),'"+csno+"',"+sendid+","+receiveid+",'"+msg+"','"+sendtime+"',NOW());";
			daoIbatisTemplate.updateWithUserDefineSql(sql);
		}
		
	}

//	public void saveHistory(List<TransMessage> transMessages){
//		if(transMessages != null && transMessages.size() > 0){
//			StringBuilder stringBuilder = new StringBuilder();
//			for (int i = 0; i < transMessages.size(); i++) {
//				String csno = transMessages.get(i).getMsg();
//				String sendid = transMessages.get(i).getSendid();
//				String received = transMessages.get(i).getReceiveid();
//				String msg = transMessages.get(i).getMsg();
//				String sendtime = transMessages.get(i).getSendTime();
//				String sql = "\nINSERT INTO im_history(id,csno,sendid,received,msg,sendtime,receivetime) " +
//						"						VALUES(getid(),'"+csno+"',"+sendid+","+received+",'"+msg+"','"+sendtime+"',NOW());";
//				stringBuilder.append(sql);
//			}
//			if(stringBuilder.length()>0){
//				daoIbatisTemplate.execute(stringBuilder.toString());
//			}
//		}
//	}

//	public String getLastMsg(String sendid, String receiveid , String msgSize) {
//		if(StrUtils.isNull(msgSize))msgSize = "20";
//		String sql = "SELECT * FROM im_history WHERE sendid = "+sendid+" AND receiveid = "+receiveid+" ORDER BY sendtime DESC LIMIT "+msgSize+"";
//		List<Map<String , Object>> list = daoIbatisTemplate.queryForList(sql);
//		String ret = "";
//		for (int i = 0; i < list.size(); i++) {
//			ret += "{\"msg\": \""+list.get(i).get("msg")+"\",\"sendTime\":\""+list.get(i).get("sendtime")+"\"}";
//			if(i != list.size()-1)ret += ",";
//		}
//		return ret;
//	}

	public static void main(String[] args) {
		JSONArray array = JSONArray.fromObject("[{\"msg\":\"34534545\",\"sendtime\":\"2018/05/03 17:59:36\",\"id\":\"6319375888\"},{\"msg\":\"34534545\",\"sendtime\":\"2018/05/03 17:59:36\",\"id\":\"6319375888\"}]");
		StringBuilder stringBuilder = new StringBuilder();
		for (Object object : array) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			System.out.println(jsonObject.getString("id"));
		}
	}
	
	public String receiver(String sendid, String receiveid, String csno) {
		String ret = "[{\"msg\": \"NULL\"}]";
		String sql = "" +
				"\nSELECT array_to_json (ARRAY_AGG(row_to_json(Q))) :: TEXT AS json FROM"+
				"\n\t(SELECT "+
				"\n			msg AS  msg, to_char(sendtime, 'YYYY/MM/DD HH24:MI:SS') AS sendtime,id::TEXT AS id"+
				"\n\tFROM im_history u "+
				"\n\tWHERE sendid=" + sendid + 
				"\n\tAND receiveid=" + receiveid +
				"\n\tAND csno='" + csno + "'" +
				"\n\tAND isread = FALSE ORDER BY sendtime) AS Q";
		Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map == null || map.size() < 0){
		}else{
			String obj = (String) map.get("json");
			if(obj != null && !obj.equals("")){
				ret = obj;
				JSONArray array = JSONArray.fromObject(ret);
				StringBuilder stringBuilder = new StringBuilder();
				for (Object object : array) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					stringBuilder.append("\nUPDATE im_history set isread = TRUE WHERE id = " + jsonObject.getString("id")+";");
				}
				if(stringBuilder.length()>0){
					daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
				}
			}
		}
//		List<TransMessage> list = queueReceiver.receiver(sendid , receiveid);
//		if(list != null && list.size() > 0){
//			for (int i = 0; i < list.size(); i++) {
//				String msg = list.get(i).getMsg();
////				try {
//					//msg = URLDecoder.decode(msg,"utf-8");
//					ret += "{\"msg\": \""+msg+"\",\"sendTime\":\""+list.get(i).getSendTime()+"\"}";
//					if(i != list.size()-1)ret += ",";
////				} catch (UnsupportedEncodingException e) {
////					e.printStackTrace();
////				}
//			}
//			ret = "[" + ret + "]";
//			System.out.println(ret);
//			//serviceContext.saveHistory(list);
//		}else{
//			ret = "[{\"msg\": \"NULL\"}]";
//		}
		return ret;
	}
	
	public String getHistory(String sendid, String receiveid, String csno) {
		String ret = "[{\"msg\": \"NULL\"}]";
		String sql = "" +
				"\nSELECT array_to_json (ARRAY_AGG(row_to_json(Q))) :: TEXT AS json FROM"+
				"\n\t(SELECT * FROM " +
				"\n\t	(SELECT "+
				"\n			msg AS msg, to_char(sendtime, 'YYYY/MM/DD HH24:MI:SS') AS sendtime,id::TEXT AS id,sendid::TEXT AS sendid,receiveid::TEXT AS receiveid"+
				"\n\t	FROM im_history u "+
				"\n\t	WHERE ((sendid=" + sendid + " AND receiveid=" + receiveid + ") OR (sendid=" + receiveid + " AND receiveid=" + sendid + "))" +
				"\n\t		AND csno='" + csno + "'" +	" ORDER BY sendtime DESC LIMIT 20" +
				"\n\t	) AS T" +
				"\n\t	ORDER BY sendtime" +
				") AS Q";
		Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map == null || map.size() < 0){
		}else{
			String obj = (String) map.get("json");
			if(obj != null && !obj.equals("")){
				ret = obj;
			}
		}
		return ret;
	}
	
	public String getHistoryAll(String sendid, String receiveid, String csno) {
		String ret = "[{\"msg\": \"NULL\"}]";
		String sql = "" +
				"\nSELECT array_to_json (ARRAY_AGG(row_to_json(Q))) :: TEXT AS json FROM"+
				"\n\t(SELECT * FROM " +
				"\n\t	(SELECT "+
				"\n			msg AS msg, to_char(sendtime, 'YYYY/MM/DD HH24:MI:SS') AS sendtime,id::TEXT AS id,sendid::TEXT AS sendid,receiveid::TEXT AS receiveid"+
				"\n\t	FROM im_history u "+
				"\n\t	WHERE ((sendid=" + sendid + " AND receiveid=" + receiveid + ") OR (sendid=" + receiveid + " AND receiveid=" + sendid + "))" +
				"\n\t		AND csno='" + csno + "'" +	" ORDER BY sendtime DESC LIMIT 1000" +
				"\n\t	) AS T" +
				"\n\t	ORDER BY sendtime" +
				") AS Q";
		Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map == null || map.size() < 0){
		}else{
			String obj = (String) map.get("json");
			if(obj != null && !obj.equals("")){
				ret = obj;
			}
		}
		return ret;
	}

	public String existQueueMsg(String receiveid, String csno) {
		String ret = "{\"existqueuemsg\": \"false\"}";
		String sql = 
			"\nSELECT row_to_json(Q) AS json FROM	"+
			"\n(SELECT " +
			"		(CASE WHEN EXISTS(SELECT 1 FROM im_history u WHERE receiveid=" + receiveid +" AND isread = FALSE AND csno='" + csno + "'" +	") THEN 'true' ELSE 'false' END) AS existqueuemsg" +
			"		,(SELECT COUNT(1) FROM im_history u WHERE receiveid=" + receiveid +" AND isread = FALSE AND csno='" + csno + "') AS queuemsgsize"+
			"\n) AS Q";
		Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map == null || map.size() <= 0){
		}else{
			org.postgresql.util.PGobject obj = (org.postgresql.util.PGobject) map.get("json");
			if(obj != null && !obj.equals("")){
				ret = obj.toString();
				//System.out.println(obj);
			}
		}
//		MQTools mqTools = (MQTools)AppUtil.getBeanFromSpringIocJunit("mqTools");
//		
//		if(mqTools.existQueueMsg(receiveid)){
//			ret = "{\"existQueueMsg\": \"true\"}";
//		}else{
//			ret = "{\"existQueueMsg\": \"false\"}";
//		}
//		mqTools.destory();
		return ret;
	}
	/**
	 * 获取最新信息，包含自己发送的
	 * @param sendid
	 * @param receiveid
	 * @param csno
	 * @return
	 */
	public String getQueueLastMsg(String receiveid, String csno) {
		String ret = "{\"sendid\": \"-1\",\"msg\": \"\",\"sendtime\": \"\"}";
		String sql = 
			"\nSELECT array_to_json (ARRAY_AGG(row_to_json(Q))) :: TEXT AS json " +
			"\nFROM	f_im_getlastmsg('csno="+csno+";receiveid="+receiveid+"') Q";
		Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map == null || map.size() < 0){
		}else{
			String obj = (String) map.get("json");
			if(obj != null && !obj.equals("")){
				ret = obj;
			}
		}
		return ret;
	}

	public String getQueueMsgSize(String sendid, String receiveid, String csno) {
		String ret = "{\"queuemsgsize\": \"0\"}";
		String sql = 
			"\nSELECT row_to_json(Q) AS json FROM	"+
			"\n(SELECT COUNT(1) AS queuemsgsize FROM im_history u"+
			"\n\tWHERE sendid=" + sendid + 
			"\n\tAND csno='" + csno + "'" +
			"\n\tAND receiveid=" + receiveid +
			"\n\tAND isread = FALSE) AS Q";
		Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(map == null || map.size() < 0){
		}else{
			org.postgresql.util.PGobject obj = (org.postgresql.util.PGobject) map.get("json");
			if(obj != null && !obj.equals("")){
				ret = obj.toString();
			}
		}
//		MQTools mqTools = (MQTools)AppUtil.getBeanFromSpringIocJunit("mqTools");
//		long msgSize = mqTools.getQueueMsgSize(sendid , receiveid);
//		ret = "{\"queueMsgSize\": \""+msgSize+"\"}";
//		mqTools.destory();
		return ret;
	}

	public String getQueueMsgSizeAll(String sendid, String receiveid,
			String csno) {
		String ret = "{\"sendid\": \"-1\",\"queuemsgsize\": \"0\"}";
		String sql = 
			"\nSELECT array_to_json(array_agg(row_to_json(Q))) AS json FROM	"+
			"\n(SELECT sendid , COUNT(1) AS queuemsgsize FROM im_history u"+
			"\n\tWHERE 1=1" +
			//"\n\tAND sendid IN (" + sendid + "-9999)" +
			"\n\tAND csno='" + csno + "'" +
			"\n\tAND receiveid=" + receiveid +
			"\n\tAND isread = FALSE" +
			"\n\tGROUP BY sendid HAVING count(1) > 0" +
			"\n) AS Q";
		//System.out.println(sql);
		try {
			Map<String, Object> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map == null || map.size() < 0){
			}else{
				org.postgresql.util.PGobject obj = (org.postgresql.util.PGobject) map.get("json");
				if(obj != null && !obj.equals("")){
					ret = obj.toString();
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return ret;
	}

	

	
}
