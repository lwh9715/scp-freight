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
@Table(name = "sys_user_assign")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysUserAssign implements java.io.Serializable {

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
	@Column(name = "linkid")
	
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "linktype", nullable = false, length = 1)
	private java.lang.String linktype;

	/**
	 *@generated
	 */
	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "polid")
	private java.lang.Long polid;
	

	/**
	 *@generated
	 */
	@Column(name = "rolearea", nullable = false, length = 1)
	private java.lang.String rolearea;

	/**
	 *@generated
	 */
	@Column(name = "roletype", nullable = false, length = 1)
	private java.lang.String roletype;

	@Column(name = "isdefault")
	private Boolean isdefault;
	
	@Column(name = "ispricer")
	private Boolean ispricer;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	
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
	public java.lang.Long getLinkid() {
		return this.linkid;
	}

	/**
	 *@generated
	 */
	public void setLinkid(java.lang.Long value) {
		this.linkid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLinktype() {
		return this.linktype;
	}

	/**
	 *@generated
	 */
	public void setLinktype(java.lang.String value) {
		this.linktype = value;
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

	/**
	 *@generated
	 */
	public java.lang.String getRolearea() {
		return this.rolearea;
	}

	/**
	 *@generated
	 */
	public void setRolearea(java.lang.String value) {
		this.rolearea = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRoletype() {
		return this.roletype;
	}

	/**
	 *@generated
	 */
	public void setRoletype(java.lang.String value) {
		this.roletype = value;
	}

	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public java.lang.Long getPolid() {
		return polid;
	}

	public void setPolid(java.lang.Long polid) {
		this.polid = polid;
	}

	public Boolean getIspricer() {
		return ispricer;
	}

	public void setIspricer(Boolean ispricer) {
		this.ispricer = ispricer;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
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

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
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

	@Override
	public String toString() {
        String toStringStr = "SysUserAssign{" +
                "id=" + id +
                ", linkid=" + linkid +
                ", linktype='" + linktype + '\'' +
                ", userid=" + userid +
                ", polid=" + polid +
                ", rolearea='" + rolearea + '\'' +
                ", roletype='" + roletype + '\'' +
                ", isdefault=" + isdefault +
                ", ispricer=" + ispricer +
                ", isdelete=" + isdelete +
                ", updater='" + updater + '\'' +
                ", updatetime=" + updatetime +
                ", remarks='" + remarks + '\'' +
                ", inputer='" + inputer + '\'' +
                ", inputtime=" + inputtime +
                '}';
        return toStringStr.replaceAll("'","''") ;
	}
}