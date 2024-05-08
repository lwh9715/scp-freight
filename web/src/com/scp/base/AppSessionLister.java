package com.scp.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.scp.dao.sys.SysUseronlineDao;
import com.scp.model.sys.SysUseronline;
import com.scp.util.AppUtils;

public class AppSessionLister implements HttpSessionListener {
	
	public static Map<String , HttpSession> sessionMap = new HashMap<String , HttpSession>();	//存所有会话列表
	
	public static Map onlineSessionMap = new HashMap();// 已登录的会话列表

	public static int activeSessions = 0;// SessionCount;
	
	public static int getOnLineUser(){
		return onlineSessionMap.size();
	}

	public AppSessionLister() {

	}

	// 会话创建后执行方法
	public void sessionCreated(HttpSessionEvent hse) {
		// 将在线人数加一
		activeSessions++;
		String sessionid = hse.getSession().getId();
		sessionMap.put(sessionid, hse.getSession());
		//System.out.println("sessionid:"+sessionid);
		UserSession userSession = (UserSession) hse.getSession().getAttribute("UserSession");
		if(userSession != null){
			//System.out.println("userSession:"+userSession.getUserid());
		}
		//System.out.println("sessionCreated"+activeSessions);
	}

	// 会话销毁执行方法
	public void sessionDestroyed(HttpSessionEvent hse) {
		// 获取即将删除的会话
		HttpSession session = hse.getSession();
		sessionMap.remove(session.getId());
		//System.out.println("sessionDestroyed:"+activeSessions);
		// 从在线会话map中根据登录用户ID移除会话,并让此会话失效。
		deleteSession(session);
		if (activeSessions > 0) {
			activeSessions--;
		}
	}

	/**
	 * 判断用户是否已经登录，如果已经登录则作出替换操作,替换成功返回true
	 * 
	 * @param session
	 * @param kskh
	 * @return
	 */
	public static boolean replaceSession(HttpSession newSession, String kskh) {
		boolean flag = false;
		if (onlineSessionMap.containsKey(kskh)) {
			// 根据用户ID在sessionMap中查找会话，如果存在并且不是同一个会话ID,则进入
			if (!isSameSession(newSession, kskh)) {
				// 如果会话ID不相等,说明是两人同时登录
				HttpSession oldSession = (HttpSession) onlineSessionMap.get(kskh);
				try {
					// 销毁之前的会话
					deleteSession(oldSession);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// 保存刚创建的会话。
				onlineSessionMap.put(kskh, newSession);
				flag = true;
			} else {
				// 如果会话ID相等,说明是同一个人登录,不做任何操作
				flag = false;
			}
		} else {
			// 如果会话列表中不包含该会话,则添加
			onlineSessionMap.put(kskh, newSession);
			flag = false;
		}
		return flag;
	}

	/**
	 * 比较新会话和旧会话的ID，如果ID相同，则表示是同一个用户,返回true
	 * @param session
	 * @param kskh
	 * @return boolean ID相同返回true,
	 */
	public static boolean isSameSession(HttpSession newSession, String kskh) {
		boolean flag = false;
		HttpSession oldSession = null;
		try {
			// 获得旧会话
			oldSession = (HttpSession) onlineSessionMap.get(kskh);
		} catch (Exception e) {
		}
		if (oldSession != null) {
			// 比较新会话和旧会话的ID，如果ID相同，则表示是同一个用户
			if (newSession.getId().equals(oldSession.getId())) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static boolean addSession(HttpSession session) {
		if (session != null) {
			try {
				UserSession usersession = (UserSession) session
						.getAttribute("UserSession");
				//System.out.println("sessionCreated userid:"+usersession.getUserid());
				if (usersession != null) {
					Long userid = usersession.getUserid();
					if (userid != null) {
						onlineSessionMap.put(userid , usersession);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}
	
//	public static SysUseronlineDao sysUseronlineDao;
	
	/**
	 * 从在线会话map中移除会话
	 * @param session
	 * @param kskh
	 * @return
	 */
	public static boolean deleteSession(HttpSession session) {
		if (session != null) {
			try {
				if (session.getAttribute("UserSession") != null) {
					UserSession usersession = (UserSession) session.getAttribute("UserSession");
					Long userid = usersession.getUserid();
					if (userid != null) {
						// 移除用户会话
						onlineSessionMap.remove(userid);
						SysUseronlineDao sysUseronlineDao = (SysUseronlineDao) AppUtils.getBeanFromSpringIoc("sysUseronlineDao");
						// 将sys_useronline中的isvalid 设为无效
						String sql = "isdelete = false AND sessionid = '" + session.getId()+"'";
						List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere(sql);
						for(int i = 0;i<sysUseronlines.size();i++){
							sysUseronlines.get(i).setIsvalid(false);
							sysUseronlines.get(i).setIsonline("N");
							sysUseronlineDao.createOrModify(sysUseronlines.get(i));
						}
					}
					// 删除用户会话
					session.removeAttribute("UserSession");
				}
				//session = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			AppSessionLister.sessionMap.remove(session.getId());
		}
		return true;
	}

	public static int getActiveSessions() {
		return activeSessions;
	}
}
