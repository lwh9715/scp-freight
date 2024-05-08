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
@Table(name = "sys_griddef")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysGridDef implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "gridid")
	private java.lang.String gridid;

	@Column(name = "userid")
	private java.lang.Long userid;

	@Column(name = "colkey")
	private java.lang.String colkey;

	@Column(name = "colwidth")
	private java.lang.String colwidth;

	@Column(name = "ishidden")
	private java.lang.String ishidden;
	
	@Column(name = "colorder")
	private java.lang.String colorder;
	
	@Column(name = "updater")
	private java.lang.String updater;
	
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;
	
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;
	
	@Column(name = "inputer")
	private java.lang.String inputer;

	@Column(name = "isselect")
	private java.lang.String isselect;

	@Column(name = "configurename")
	private java.lang.String configurename;


	public String getConfigurename() {
		return configurename;
	}

	public void setConfigurename(String configurename) {
		this.configurename = configurename;
	}

	public String getIsselect() {
		return isselect;
	}

	public void setIsselect(String isselect) {
		this.isselect = isselect;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getGridid() {
		return gridid;
	}

	public void setGridid(java.lang.String gridid) {
		this.gridid = gridid;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.String getColkey() {
		return colkey;
	}

	public void setColkey(java.lang.String colkey) {
		this.colkey = colkey;
	}

	public java.lang.String getColwidth() {
		return colwidth;
	}

	public void setColwidth(java.lang.String colwidth) {
		this.colwidth = colwidth;
	}

	public java.lang.String getIshidden() {
		return ishidden;
	}

	public void setIshidden(java.lang.String ishidden) {
		this.ishidden = ishidden;
	}

	public java.lang.String getColorder() {
		return colorder;
	}

	public void setColorder(java.lang.String colorder) {
		this.colorder = colorder;
	}
	
}
