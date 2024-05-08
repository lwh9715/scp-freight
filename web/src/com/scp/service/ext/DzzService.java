package com.scp.service.ext;

import java.sql.SQLException;
import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplateMySql;
import com.scp.util.StrUtils;

@Component
public class DzzService {

	@Resource
	public DaoIbatisTemplateMySql daoIbatisTemplateMySql;

	public void executeQuery(String sql) {
		////AppUtils.debug(sql);
		daoIbatisTemplateMySql.queryWithUserDefineSql(sql);
	}

	public void executeQueryBatchByJdbc(Vector<String> sqlBatch)
			throws SQLException {
		////AppUtils.debug(sqlBatch);
		daoIbatisTemplateMySql.executeQueryBatchByJdbc(sqlBatch);
	}

	/**
	 * DZZ发送消息
	 * 
	 * @param userId
	 *            发送人id
	 * @param toUserId
	 *            接收人id
	 * @param sendContext
	 *            消息内容
	 * @param remindTitle
	 *            消息提示标题
	 * @param remindContext
	 *            消息提示内容
	 * @param url
	 *            消息跳转url
	 */
	public void sendMessage(Long userId, String toUserIds, String sendContext,
			String remindTitle, String remindContext, String url) {

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT f_desktop_sendmessage(");
		sb.append(userId + "," + toUserIds + ",'" + sendContext + "','"
				+ remindTitle + "','" + remindContext + "','" + url + "')");
		Vector v = new Vector<String>();
		v.add(sb.toString());
		try {
			daoIbatisTemplateMySql.executeQueryBatchByJdbc(v);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 商务中心费用确认消息发送
	 * 
	 * @param sqls
	 */
	public void sendMessageOfShipCostConfirm(String sqls) {
		Vector v = new Vector<String>();
		String[] sql = sqls.split("\n");
		for (String s : sql) {
			if (!StrUtils.isNull(s)) {
				v.add(s.replaceAll("\r|\n", ""));
				try {
					daoIbatisTemplateMySql.executeQueryBatchByJdbc(v);
					try {
						Thread.sleep(1100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						e.notify();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				v.clear();
			}
		}
	}
}
