package com.ufms.web.view.im;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * jms消息bean
 * 用于系统内部消息发送，topic，点对点消息
 * @author neo
 */
public class TransMessage implements java.io.Serializable, Cloneable,Comparable<TransMessage> {

	private String csno;
	
	private String sendid;

	private String receiveid;

	private String msg;

	private Date sendTime;

	public TransMessage() {
	}

	public TransMessage(String sendid, String receiveid, String msg, String csno) {
		super();
		this.sendid = sendid;
		this.csno = csno;
		this.receiveid = receiveid;
		this.msg = msg;
		this.sendTime = Calendar.getInstance().getTime();
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public String getReceiveid() {
		return receiveid;
	}

	public void setReceiveid(String receiveid) {
		this.receiveid = receiveid;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSendTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(this.sendTime);
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Override
	public int compareTo(TransMessage o) {
		if (this.sendTime.getTime() > o.sendTime.getTime()) {
            return 1;
        } else if (this.sendTime.getTime() < o.sendTime.getTime()) {
            return -1;
        } else {
            return 0;
        }
	}

	public String getCsno() {
		return csno;
	}

	public void setCsno(String csno) {
		this.csno = csno;
	}
}
