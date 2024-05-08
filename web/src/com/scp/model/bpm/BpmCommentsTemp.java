package com.scp.model.bpm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
/**
 *@generated
 */
@Table(name = "bpm_comments_temp")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmCommentsTemp  implements java.io.Serializable{
	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "processid")
	private java.lang.Long processid;
	
	@Column(name = "taskname")
	private java.lang.String taskname;
	
	@Column(name = "comments")
	private java.lang.String comments;
	
	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "ispublic")
	private java.lang.Boolean ispublic;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getProcessid() {
		return processid;
	}

	public void setProcessid(java.lang.Long processid) {
		this.processid = processid;
	}

	public java.lang.String getTaskname() {
		return taskname;
	}

	public void setTaskname(java.lang.String taskname) {
		this.taskname = taskname;
	}

	public java.lang.String getComments() {
		return comments;
	}

	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.Boolean getIspublic() {
		return ispublic;
	}

	public void setIspublic(java.lang.Boolean ispublic) {
		this.ispublic = ispublic;
	}
	
}
