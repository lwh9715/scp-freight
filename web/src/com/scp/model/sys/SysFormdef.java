package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "sys_formdef")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysFormdef implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private java.lang.Long id;
	
	@Column(name = "beaname", length = 100)
	private java.lang.String beaname;
	
	@Column(name = "inputype", length = 20)
	private java.lang.String inputype;
	
	@Column(name = "columnname", length = 20)
	private java.lang.String columnname;
	
	@Column(name = "inputlable", length = 20)
	private java.lang.String inputlable;
	
	@Column(name = "orderno")
	private java.lang.Integer orderno;
	
	@Column(name = "defvalue")
	private java.lang.String defvalue;
	
	@Column(name = "isrequired")
	private java.lang.Boolean isrequired;
	
	@Column(name = "color",length=50)
	private java.lang.String color;
	
	@Column(name = "formtype",length=1)
	private java.lang.String formtype;
	
	@Column(name = "colx")
	private java.lang.Integer colx;
	
	@Column(name = "coly")
	private java.lang.Integer coly;
	
	@Column(name = "colw")
	private java.lang.Integer colw;
	
	@Column(name = "colh")
	private java.lang.Integer colh;
	
	@Column(name = "colstyle")
	private java.lang.String colstyle;
	
	
	@Column(name = "srcdata")
	private java.lang.String srcdata;
	
	@Column(name = "ishidden")
	private java.lang.Boolean ishidden;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getBeaname() {
		return beaname;
	}

	public void setBeaname(java.lang.String beaname) {
		this.beaname = beaname;
	}

	public java.lang.String getInputype() {
		return inputype;
	}

	public void setInputype(java.lang.String inputype) {
		this.inputype = inputype;
	}

	public java.lang.String getColumnname() {
		return columnname;
	}

	public void setColumnname(java.lang.String columnname) {
		this.columnname = columnname;
	}

	public java.lang.String getInputlable() {
		return inputlable;
	}

	public void setInputlable(java.lang.String inputlable) {
		this.inputlable = inputlable;
	}

	public java.lang.Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.Integer orderno) {
		this.orderno = orderno;
	}

	public java.lang.String getDefvalue() {
		return defvalue;
	}

	public void setDefvalue(java.lang.String defvalue) {
		this.defvalue = defvalue;
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

	public java.lang.String getFormtype() {
		return formtype;
	}

	public void setFormtype(java.lang.String formtype) {
		this.formtype = formtype;
	}

	public java.lang.Integer getColx() {
		return colx;
	}

	public void setColx(java.lang.Integer colx) {
		this.colx = colx;
	}

	public java.lang.Integer getColy() {
		return coly;
	}

	public void setColy(java.lang.Integer coly) {
		this.coly = coly;
	}

	public java.lang.Boolean getIshidden() {
		return ishidden;
	}

	public void setIshidden(java.lang.Boolean ishidden) {
		this.ishidden = ishidden;
	}

	public java.lang.Integer getColw() {
		return colw;
	}

	public void setColw(java.lang.Integer colw) {
		this.colw = colw;
	}

	public java.lang.Integer getColh() {
		return colh;
	}

	public void setColh(java.lang.Integer colh) {
		this.colh = colh;
	}

	public java.lang.String getColstyle() {
		return colstyle;
	}

	public void setColstyle(java.lang.String colstyle) {
		this.colstyle = colstyle;
	}

	public java.lang.String getSrcdata() {
		return srcdata;
	}

	public void setSrcdata(java.lang.String srcdata) {
		this.srcdata = srcdata;
	}

}
