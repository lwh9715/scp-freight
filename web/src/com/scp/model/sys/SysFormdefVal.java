package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "sys_formdef_val")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysFormdefVal implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private java.lang.Long id;
	
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	@Column(name = "linktbl", length = 30)
	private java.lang.String linktbl;
	
	
	@Column(name = "beaname", length = 100)
	private java.lang.String beaname;
	
	@Column(name = "columnname", length = 30)
	private java.lang.String columnname;
	
	@Column(name = "objtype", length = 20)
	private java.lang.String objtype;

	@Column(name = "objval")
	private java.lang.String objval;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}


	public java.lang.String getObjtype() {
		return objtype;
	}

	public void setObjtype(java.lang.String objtype) {
		this.objtype = objtype;
	}

	public java.lang.String getObjval() {
		return objval;
	}

	public void setObjval(java.lang.String objval) {
		this.objval = objval;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.lang.String getColumnname() {
		return columnname;
	}

	public void setColumnname(java.lang.String columnname) {
		this.columnname = columnname;
	}

	public java.lang.String getBeaname() {
		return beaname;
	}

	public void setBeaname(java.lang.String beaname) {
		this.beaname = beaname;
	}

	
}
