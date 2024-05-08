package com.scp.model.bpm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bpm_processins_var")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmProcessinsVar implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "processinstanceid")
	private java.lang.Long processinstanceid;
	
	@Column(name = "name", length = 20)
	private java.lang.String name;
	
	@Column(name = "val", length = 100)
	private java.lang.String val;
	
	@Column(name = "vartype", length = 20)
	private java.lang.String vartype;
	
	@Column(name = "lable", length = 100)
	private java.lang.String lable;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(java.lang.Long processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getVal() {
		return val;
	}

	public void setVal(java.lang.String val) {
		this.val = val;
	}

	public java.lang.String getVartype() {
		return vartype;
	}

	public void setVartype(java.lang.String vartype) {
		this.vartype = vartype;
	}

	public java.lang.String getLable() {
		return lable;
	}

	public void setLable(java.lang.String lable) {
		this.lable = lable;
	}
	
}
