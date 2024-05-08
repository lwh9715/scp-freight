package com.scp.model.price;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Date:2018-10-11 驳船费主表
 * 
 * @author hunk
 */
@Table(name = "price_fcl_bargefee")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceFclBargefee implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "namec", length = 30)
	private java.lang.String namec;
	
	@Column(name = "namee", length = 30)
	private java.lang.String namee;
	
	@Column(name = "shipping", length = 30)
	private java.lang.String shipping;
	
	@Column(name = "remark")
	private java.lang.String remark;
	
	@Column(name = "datefm")
	private java.util.Date datefm;
	
	@Column(name = "dateto")
	private java.util.Date dateto;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNamec() {
		return namec;
	}

	public void setNamec(java.lang.String namec) {
		this.namec = namec;
	}

	public java.lang.String getNamee() {
		return namee;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public java.util.Date getDatefm() {
		return datefm;
	}

	public void setDatefm(java.util.Date datefm) {
		this.datefm = datefm;
	}

	public java.util.Date getDateto() {
		return dateto;
	}

	public void setDateto(java.util.Date dateto) {
		this.dateto = dateto;
	}

	public java.lang.String getShipping() {
		return shipping;
	}

	public void setShipping(java.lang.String shipping) {
		this.shipping = shipping;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}
	
}
