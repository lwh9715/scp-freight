package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "cs_user")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class CsUser implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private java.lang.Long id;

	@Column(name = "code", nullable = false)
	private java.lang.String code;

	@Column(name = "namec")
	private java.lang.String namec;

	@Column(name = "namee")
	private java.lang.String namee;

	@Column(name = "tel1")
	private java.lang.String tel1;

	@Column(name = "tel2")
	private java.lang.String tel2;

	@Column(name = "email1")
	private java.lang.String email1;

	@Column(name = "email2")
	private java.lang.String email2;

	@Column(name = "qq")
	private java.lang.String qq;

	@Column(name = "msn")
	private java.lang.String msn;

	@Column(name = "corpid")
	private java.lang.Long corpid;

	@Column(name = "remarks")
	private java.lang.String remarks;

	@Column(name = "ciphertext")
	private java.lang.String ciphertext;

	@Column(name = "secretkey")
	private java.lang.String secretkey;

	@Column(name = "valid")
	private java.lang.String valid;

	@Column(name = "inputer")
	private java.lang.String inputer;

	@Column(name = "inputtime")
	private java.util.Date inputtime;

	@Column(name = "updater")
	private java.lang.String updater;

	@Column(name = "updatetime")
	private java.util.Date updatetime;
	
	@Column(name = "openid")
	private java.lang.String openid;
	
	@Column(name = "issubscribeweixin")
	private Boolean issubscribeweixin;
	
	@Column(name = "issubscribesms")
	private Boolean issubscribesms;
	
	@Column(name = "issubscribeemail")
	private Boolean issubscribeemail;
	
	@Column(name = "salesid")
	private java.lang.Long salesid;
	
	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
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

	public java.lang.String getTel1() {
		return tel1;
	}

	public void setTel1(java.lang.String tel1) {
		this.tel1 = tel1;
	}

	public java.lang.String getTel2() {
		return tel2;
	}

	public void setTel2(java.lang.String tel2) {
		this.tel2 = tel2;
	}

	public java.lang.String getEmail1() {
		return email1;
	}

	public void setEmail1(java.lang.String email1) {
		this.email1 = email1;
	}

	public java.lang.String getEmail2() {
		return email2;
	}

	public void setEmail2(java.lang.String email2) {
		this.email2 = email2;
	}

	public java.lang.String getQq() {
		return qq;
	}

	public void setQq(java.lang.String qq) {
		this.qq = qq;
	}

	public java.lang.String getMsn() {
		return msn;
	}

	public void setMsn(java.lang.String msn) {
		this.msn = msn;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	@Column(updatable=false)
	public java.lang.String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(java.lang.String ciphertext) {
		this.ciphertext = ciphertext;
	}

	@Column(updatable=false)
	public java.lang.String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(java.lang.String secretkey) {
		this.secretkey = secretkey;
	}

	public java.lang.String getValid() {
		return valid;
	}

	public void setValid(java.lang.String valid) {
		this.valid = valid;
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

	public java.lang.String getOpenid() {
		return openid;
	}

	public void setOpenid(java.lang.String openid) {
		this.openid = openid;
	}

	public Boolean getIssubscribeweixin() {
		return issubscribeweixin;
	}

	public void setIssubscribeweixin(Boolean issubscribeweixin) {
		this.issubscribeweixin = issubscribeweixin;
	}

	public Boolean getIssubscribesms() {
		return issubscribesms;
	}

	public void setIssubscribesms(Boolean issubscribesms) {
		this.issubscribesms = issubscribesms;
	}

	public Boolean getIssubscribeemail() {
		return issubscribeemail;
	}

	public void setIssubscribeemail(Boolean issubscribeemail) {
		this.issubscribeemail = issubscribeemail;
	}

	public java.lang.Long getSalesid() {
		return salesid;
	}

	public void setSalesid(java.lang.Long salesid) {
		this.salesid = salesid;
	}

}
