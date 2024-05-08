package com.scp.model.sys;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parameter;

import com.scp.util.StrUtils;

/**
 *@generated
 */
@Table(name = "sys_user")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysUser implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "issales")
	private Boolean issales;
	
	@Column(name = "isop ")
	private Boolean isop ;
	
	@Column(name = "iscs ")
	private Boolean iscs ;
	
	@Column(name = "isdoc ")
	private Boolean isdoc ;

	/**
	 *@generated
	 */
	@Column(name = "code", length = 20)
	private java.lang.String code;
	
	
	@Column(name = "actsetid ")
	private Long actsetid ;

	/**S
	 *@generated
	 */
	@Column(name = "namec", length = 100)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 100)
	private java.lang.String namee;
	
	@Column(name = "securitylevel")
	private int securitylevel;
	
	@Column(name = "iscsuser ")
	private java.lang.Boolean iscsuser ;
	
	@Column(name = "isinvalid")
	private java.lang.Boolean isinvalid;
	
	
	@Column(name = "isaccuser")
	private java.lang.Boolean isaccuser;
	
	
	@Column(name = "customerid")
	private BigDecimal customerid ;
	
	
	@Column(name = "logincount")
	private Integer logincount;
	
	@Column(name = "onlinecount")
	private Integer onlinecount;

	/**
	 *@generated
	 */
	@Column(name = "tel1", length = 20)
	private java.lang.String tel1;

	/**
	 *@generated
	 */
	@Column(name = "tel2", length = 20)
	private java.lang.String tel2;

	/**
	 *@generated
	 */
	@Column(name = "email1", length = 30)
	private java.lang.String email1;

	/**
	 *@generated
	 */
	@Column(name = "email2", length = 30)
	private java.lang.String email2;

	/**
	 *@generated
	 */
	@Column(name = "qq", length = 30)
	private java.lang.String qq;

	/**
	 *@generated
	 */
	@Column(name = "msn", length = 30)
	private java.lang.String msn;

	/**
	 *@信用额度
	 */
	@Column(name = "creditlimit")
	private java.lang.Integer creditlimit;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "jobdesc")
	private java.lang.String jobdesc;
	 
	
	
	/**
	 *@generated
	 */
	@Column(name = "customerabbr")
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "ciphertext")
	private java.lang.String ciphertext;

	/**
	 *@generated
	 */
	@Column(name = "secretkey")
	private java.lang.String secretkey;

	/**
	 *@generated
	 */
	@Column(name = "valid", length = 1)
	private java.lang.String valid;

	/**
	 *@generated
	 */
	@Column(name = "isadmin", length = 1)
	private java.lang.String isadmin;

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

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "isys")
	private java.lang.Boolean isys;

	/**
	 *@generated
	 */
	@Column(name = "mobilephone", length = 30)
	private java.lang.String mobilephone;

	/**
	 *@generated
	 */
	@Column(name = "fax", length = 30)
	private java.lang.String fax;
	
	@Column(name = "fmsid")
	private java.lang.Long fmsid;
	

	/**
	 *@generated
	 */
	@Column(name = "skype", length = 30)
	private java.lang.String skype;
	
	
	@Column(name = "depter", length = 30)
	private java.lang.String depter;

	@Column(name = "deptid",insertable=false, updatable=false)
	private java.lang.Long deptid;
	
	@Column(name = "pkid_remote", length = 20)
	private java.lang.String pkid_remote;
	
	@Column(name = "opneid", length = 50)
	private java.lang.String opneid;
	
	/**
	 *@generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "sysUser")
	@IndexColumn(name="id")
	@LazyCollection(LazyCollectionOption.TRUE)
	public java.util.List<SysUserinrole> sysUserinroles = new java.util.ArrayList<SysUserinrole>();

	/**
	 *@generated
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "corpid")
	@ForeignKey(name = "SYS_CORPORATION_SYS_USER")
	public SysCorporation sysCorporation;

	/**
	 *@generated
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "deptid")
	@ForeignKey(name = "SYS_DEPARTMENT_SYS_USER")
	public SysDepartment sysDepartment;

	@Column(name = "parentid")
	private java.lang.Long parentid ;
	
	@Column(name = "idcode", length = 30)
	private java.lang.String idcode;
	
	@Column(name = "entrydate")
	private java.util.Date entrydate;
	
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
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 *@generated
	 */
	public void setCode(java.lang.String value) {
		this.code = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamec() {
		return this.namec;
	}

	public java.lang.Boolean getIscsuser() {
		return iscsuser;
	}

	public void setIscsuser(java.lang.Boolean iscsuser) {
		this.iscsuser = iscsuser;
	}

	

	public BigDecimal getCustomerid() {
		if(StrUtils.isNull(String.valueOf(customerid))){
			this.customerid=new BigDecimal(-1);
		}
		return customerid;
	}

	public void setCustomerid(BigDecimal customerid) {
		this.customerid = customerid;
	}

	/**
	 *@generated
	 */
	public void setNamec(java.lang.String value) {
		this.namec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamee() {
		return this.namee;
	}

	/**
	 *@generated
	 */
	public void setNamee(java.lang.String value) {
		this.namee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTel1() {
		return this.tel1;
	}

	/**
	 *@generated
	 */
	public void setTel1(java.lang.String value) {
		this.tel1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTel2() {
		return this.tel2;
	}

	/**
	 *@generated
	 */
	public void setTel2(java.lang.String value) {
		this.tel2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmail1() {
		return this.email1;
	}

	/**
	 *@generated
	 */
	public void setEmail1(java.lang.String value) {
		this.email1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmail2() {
		return this.email2;
	}

	/**
	 *@generated
	 */
	public void setEmail2(java.lang.String value) {
		this.email2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getQq() {
		return this.qq;
	}

	/**
	 *@generated
	 */
	public void setQq(java.lang.String value) {
		this.qq = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMsn() {
		return this.msn;
	}

	/**
	 *@generated
	 */
	public void setMsn(java.lang.String value) {
		this.msn = value;
	}

	

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCiphertext() {
		return this.ciphertext;
	}

	/**
	 *@generated
	 */
	public void setCiphertext(java.lang.String value) {
		this.ciphertext = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSecretkey() {
		return this.secretkey;
	}

	/**
	 *@generated
	 */
	public void setSecretkey(java.lang.String value) {
		this.secretkey = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getValid() {
		return this.valid;
	}

	/**
	 *@generated
	 */
	public void setValid(java.lang.String value) {
		this.valid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIsadmin() {
		return this.isadmin;
	}

	/**
	 *@generated
	 */
	public void setIsadmin(java.lang.String value) {
		this.isadmin = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getInputer() {
		return this.inputer;
	}

	/**
	 *@generated
	 */
	public void setInputer(java.lang.String value) {
		this.inputer = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getInputtime() {
		return this.inputtime;
	}

	/**
	 *@generated
	 */
	public void setInputtime(java.util.Date value) {
		this.inputtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getUpdater() {
		return this.updater;
	}

	/**
	 *@generated
	 */
	public void setUpdater(java.lang.String value) {
		this.updater = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getUpdatetime() {
		return this.updatetime;
	}

	/**
	 *@generated
	 */
	public void setUpdatetime(java.util.Date value) {
		this.updatetime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsdelete() {
		return this.isdelete;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMobilephone() {
		return this.mobilephone;
	}

	/**
	 *@generated
	 */
	public void setMobilephone(java.lang.String value) {
		this.mobilephone = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFax() {
		return this.fax;
	}

	/**
	 *@generated
	 */
	public void setFax(java.lang.String value) {
		this.fax = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSkype() {
		return this.skype;
	}

	/**
	 *@generated
	 */
	public void setSkype(java.lang.String value) {
		this.skype = value;
	}

	/**
	 *@generated
	 */
	public java.util.List<SysUserinrole> getSysUserinroles() {
		return this.sysUserinroles;
	}

	/**
	 *@generated
	 */
	public void setSysUserinroles(
			java.util.List<SysUserinrole> value) {
		this.sysUserinroles = value;
	}

	/**
	 *@generated
	 */
	public SysCorporation getSysCorporation() {
		return this.sysCorporation;
	}

	/**
	 *@generated
	 */
	public void setSysCorporation(SysCorporation value) {
		this.sysCorporation = value;
	}

	public SysDepartment getSysDepartment() {
		return sysDepartment;
	}

	public void setSysDepartment(
			SysDepartment sysDepartment) {
		this.sysDepartment = sysDepartment;
	}

	public int getSecuritylevel() {
		return securitylevel;
	}

	public void setSecuritylevel(int securitylevel) {
		this.securitylevel = securitylevel;
	}

	public java.lang.Boolean getIsinvalid() {
		return isinvalid;
	}

	public void setIsinvalid(java.lang.Boolean isinvalid) {
		this.isinvalid = isinvalid;
	}
	
	public java.lang.Boolean getIsaccuser() {
		return isaccuser;
	}

	public void setIsaccuser(java.lang.Boolean isaccuser) {
		this.isaccuser = isaccuser;
	}

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
	}

	public Long getActsetid() {
		return actsetid;
	}

	public void setActsetid(Long actsetid) {
		this.actsetid = actsetid;
	}

	public Boolean getIssales() {
		return issales;
	}

	public Boolean getIsop() {
		return isop;
	}

	public Boolean getIscs() {
		return iscs;
	}

	public Boolean getIsdoc() {
		return isdoc;
	}

	public void setIssales(Boolean issales) {
		this.issales = issales;
	}

	public void setIsop(Boolean isop) {
		this.isop = isop;
	}

	public void setIscs(Boolean iscs) {
		this.iscs = iscs;
	}

	public void setIsdoc(Boolean isdoc) {
		this.isdoc = isdoc;
	}

	public java.lang.Boolean getIsys() {
		return isys;
	}

	public void setIsys(java.lang.Boolean isys) {
		this.isys = isys;
	}

	public java.lang.String getJobdesc() {
		return jobdesc;
	}

	public void setJobdesc(java.lang.String jobdesc) {
		this.jobdesc = jobdesc;
	}

	public Integer getLogincount() {
		return logincount;
	}

	public void setLogincount(Integer logincount) {
		this.logincount = logincount;
	}
	public java.lang.Long getFmsid() {
		return fmsid;
	}

	public void setFmsid(java.lang.Long fmsid) {
		this.fmsid = fmsid;
	}

	public java.lang.String getDepter() {
		return depter;
	}

	public void setDepter(java.lang.String depter) {
		this.depter = depter;
	}

	public java.lang.Long getDeptid() {
		return deptid;
	}

	public void setDeptid(java.lang.Long deptid) {
		this.deptid = deptid;
	}

	public java.lang.String getPkid_remote() {
		return pkid_remote;
	}

	public void setPkid_remote(java.lang.String pkidRemote) {
		pkid_remote = pkidRemote;
	}

	public Integer getOnlinecount() {
		return onlinecount;
	}

	public void setOnlinecount(Integer onlinecount) {
		this.onlinecount = onlinecount;
	}

	public java.lang.String getOpneid() {
		return opneid;
	}

	public void setOpneid(java.lang.String opneid) {
		this.opneid = opneid;
	}

	public java.lang.Long getParentid() {
		return parentid;
	}

	public void setParentid(java.lang.Long parentid) {
		this.parentid = parentid;
	}

	public java.lang.String getIdcode() {
		return idcode;
	}

	public void setIdcode(java.lang.String idcode) {
		this.idcode = idcode;
	}

	public java.util.Date getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(java.util.Date entrydate) {
		this.entrydate = entrydate;
	}

	public java.lang.Integer getCreditlimit() {
		return creditlimit;
	}

	public void setCreditlimit(java.lang.Integer creditlimit) {
		this.creditlimit = creditlimit;
	}
	
}
