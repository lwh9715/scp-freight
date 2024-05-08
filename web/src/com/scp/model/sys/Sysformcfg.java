package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "sys_formcfg")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Sysformcfg implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private java.lang.Long id;
	
	@Column(name = "formid", length = 100)
	private java.lang.String formid;
	
	@Column(name = "formname", length = 100)
	private java.lang.String formname;
	
	@Column(name = "namec", length = 100)
	private java.lang.String namec;
	
	@Column(name = "namee", length = 100)
	private java.lang.String namee;
	
	@Column(name = "columnid",length = 50)
	private java.lang.String columnid;
	
	@Column(name="isrequired")
	private java.lang.Boolean isrequired;
	
	@Column(name = "color",length = 50)
	private java.lang.String color;
	
	@Column(name = "cfgtype")
	private java.lang.String cfgtype;
	
	@Column(name = "jsaction")
	private java.lang.String jsaction;
	
	@Column(name="ishide")
	private java.lang.Boolean ishide;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getFormid() {
		return formid;
	}

	public void setFormid(java.lang.String formid) {
		this.formid = formid;
	}

	public java.lang.String getFormname() {
		return formname;
	}

	public void setFormname(java.lang.String formname) {
		this.formname = formname;
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

	public java.lang.String getColumnid() {
		return columnid;
	}

	public void setColumnid(java.lang.String columnid) {
		this.columnid = columnid;
	}

	public java.lang.Boolean getIsrequired() {
		return isrequired;
	}

	public void setIsrequired(java.lang.Boolean isrequired) {
		this.isrequired = isrequired;
	}

	public java.lang.String getColor() {
		return color;
	}

	public void setColor(java.lang.String color) {
		this.color = color;
	}

	public java.lang.String getCfgtype() {
		return cfgtype;
	}

	public void setCfgtype(java.lang.String cfgtype) {
		this.cfgtype = cfgtype;
	}

	public java.lang.String getJsaction() {
		return jsaction;
	}

	public void setJsaction(java.lang.String jsaction) {
		this.jsaction = jsaction;
	}

	public java.lang.Boolean getIshide() {
		return ishide;
	}

	public void setIshide(java.lang.Boolean ishide) {
		this.ishide = ishide;
	}
	
}
