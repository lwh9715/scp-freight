package com.scp.model.knowledge;

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
@Table(name = "sys_knowledgelib")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysKnowledgelib implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy")
	private long id;
	
	@Column(name = "linkid")
	private long linkid;
	
	@Column(name = "linktbl", length = 30)
	private java.lang.String linktbl;
	
	@Column(name = "libtype", length = 1)
	private java.lang.String libtype;
	
	@Column(name = "context")
	private java.lang.String context;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "corpid")
	private long corpid;
	
	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "abstracts")
	private java.lang.String abstracts;
	
	@Column(name = "linkid2")
	private java.lang.Long linkid2;
	
	@Column(name = "linktbl2", length = 30)
	private java.lang.String linktbl2;
	
	@Column(name = "libtype2", length = 1)
	private java.lang.String libtype2;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLinkid() {
		return linkid;
	}

	public void setLinkid(long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.lang.String getLibtype() {
		return libtype;
	}

	public void setLibtype(java.lang.String libtype) {
		this.libtype = libtype;
	}

	public java.lang.String getContext() {
		return context;
	}

	public void setContext(java.lang.String context) {
		this.context = context;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public long getCorpid() {
		return corpid;
	}

	public void setCorpid(long corpid) {
		this.corpid = corpid;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public java.lang.String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(java.lang.String abstracts) {
		this.abstracts = abstracts;
	}

	public long getLinkid2() {
		return linkid2;
	}

	public void setLinkid2(long linkid2) {
		this.linkid2 = linkid2;
	}

	public java.lang.String getLinktbl2() {
		return linktbl2;
	}

	public void setLinktbl2(java.lang.String linktbl2) {
		this.linktbl2 = linktbl2;
	}

	public java.lang.String getLibtype2() {
		return libtype2;
	}

	public void setLibtype2(java.lang.String libtype2) {
		this.libtype2 = libtype2;
	}
	
}