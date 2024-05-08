package com.scp.model.sys;

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
@Table(name = "sys_custlib_user")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCustlibUser implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "custlibid")
	private java.lang.Long custlibid;

	/**
	 *@generated
	 */
	@Column(name = "userid")
	private java.lang.Long userid;

	/**
	 *@generated
	 */
	public long getId() {
		return this.id;
	}

	/**
	 *@generated
	 */
	public void setId(long value) {
		this.id = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCustlibid() {
		return this.custlibid;
	}

	/**
	 *@generated
	 */
	public void setCustlibid(java.lang.Long value) {
		this.custlibid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getUserid() {
		return this.userid;
	}

	/**
	 *@generated
	 */
	public void setUserid(java.lang.Long value) {
		this.userid = value;
	}
}