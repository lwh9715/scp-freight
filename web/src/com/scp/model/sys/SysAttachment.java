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
@Table(name = "sys_attachment")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysAttachment implements java.io.Serializable {

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
	@Column(name = "validdate", length = 29)
	private java.util.Date validdate;
	/**
	 *@generated
	 */
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "filename", length = 150)
	private java.lang.String filename;

	/**
	 *@generated
	 */
	@Column(name = "filepath", length = 200)
	private java.lang.String filepath;

	/**
	 *@generated
	 */
	@Column(name = "contenttype", length = 100)
	private java.lang.String contenttype;

	/**
	 *@generated
	 */
	@Column(name = "filesize")
	private java.math.BigDecimal filesize;

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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	
	@Column(name = "soaurl", length = 50)
	private java.lang.String soaurl;
	
	@Column(name = "isoa")
	private java.lang.Boolean isoa;
	
	
	@Column(name = "roleid")
	private java.lang.Long roleid;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "url")
	private java.lang.String url;
	
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
	public java.lang.String getFilename() {
		return this.filename;
	}

	/**
	 *@generated
	 */
	public void setFilename(java.lang.String value) {
		this.filename = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFilepath() {
		return this.filepath;
	}

	/**
	 *@generated
	 */
	public void setFilepath(java.lang.String value) {
		this.filepath = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getContenttype() {
		return this.contenttype;
	}

	/**
	 *@generated
	 */
	public void setContenttype(java.lang.String value) {
		this.contenttype = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getFilesize() {
		return this.filesize;
	}

	/**
	 *@generated
	 */
	public void setFilesize(java.math.BigDecimal value) {
		this.filesize = value;
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
	 * 取得带扩展名的名称Id+扩展名
	 * @return
	 */
	public String getFileNameDisk() {
		return getId() + "." + getFilename().substring(getFilename().lastIndexOf(".") + 1);
	}

	public java.lang.String getSoaurl() {
		return soaurl;
	}

	public void setSoaurl(java.lang.String soaurl) {
		this.soaurl = soaurl;
	}

	public java.lang.Boolean getIsoa() {
		return isoa;
	}

	public void setIsoa(java.lang.Boolean isoa) {
		this.isoa = isoa;
	}

	public java.lang.Long getRoleid() {
		return roleid;
	}

	public void setRoleid(java.lang.Long roleid) {
		this.roleid = roleid;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
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

	public java.lang.String getUrl() {
		return url;
	}

	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	public java.util.Date getValiddate() {
		return validdate;
	}

	public void setValiddate(java.util.Date validdate) {
		this.validdate = validdate;
	}

	@Override
	public String toString() {
		return "SysAttachment{" +
				"id=" + id +
				", validdate=" + validdate +
				", linkid=" + linkid +
				", filename='" + filename + '\'' +
				", filepath='" + filepath + '\'' +
				", contenttype='" + contenttype + '\'' +
				", filesize=" + filesize +
				", inputer='" + inputer + '\'' +
				", inputtime=" + inputtime +
				", isdelete=" + isdelete +
				", soaurl='" + soaurl + '\'' +
				", isoa=" + isoa +
				", roleid=" + roleid +
				", remarks='" + remarks + '\'' +
				", url='" + url + '\'' +
				", updater='" + updater + '\'' +
				", updatetime=" + updatetime +
				'}';
	}
}