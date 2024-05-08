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
@Table(name = "bpm_assign_ref")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmAssignRef  implements java.io.Serializable{
	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "assignid")
	private java.lang.Long assignid;
	
	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "expression")
	private java.lang.String expression;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getAssignid() {
		return assignid;
	}

	public void setAssignid(java.lang.Long assignid) {
		this.assignid = assignid;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.String getExpression() {
		return expression;
	}

	public void setExpression(java.lang.String expression) {
		this.expression = expression;
	}
	
	
}
