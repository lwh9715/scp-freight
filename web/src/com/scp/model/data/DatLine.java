package com.scp.model.data;

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
@Table(name = "dat_line")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatLine implements java.io.Serializable {

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
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", nullable = false, length = 30)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 30)
	private java.lang.String namee;
	
	@Column(name = "lintype", length = 1)
	private java.lang.String lintype;
	
	@Column(name = "parentid")
	private java.lang.Long parentid;
	
	@Column(name = "isleaf")
	private boolean isleaf;
	
	@Column(name = "isdelete")
	private boolean isdelete;
	
	@Column(name = "linegroup")
	private java.lang.String linegroup;

	public long getId() {
		return id;
	}

	public java.lang.Long getParentid() {
		return parentid;
	}

	public void setParentid(java.lang.Long parentid) {
		this.parentid = parentid;
	}

	public boolean isIsleaf() {
		return isleaf;
	}

	public void setIsleaf(boolean isleaf) {
		this.isleaf = isleaf;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
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

	public java.lang.String getLintype() {
		return lintype;
	}

	public void setLintype(java.lang.String lintype) {
		this.lintype = lintype;
	}

	public boolean isIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getLinegroup() {
		return linegroup;
	}

	public void setLinegroup(java.lang.String linegroup) {
		this.linegroup = linegroup;
	}
	
}