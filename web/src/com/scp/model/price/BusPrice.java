package com.scp.model.price;

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
@Table(name = "bus_price")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusPrice implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "nos", length = 30)
	private java.lang.String nos;
	
	@Column(name = "pricefclid")
	private java.lang.Long pricefclid;
	
	@Column(name = "customerid")
	private java.lang.Long customerid;
	
	@Column(name = "salesid")
	private java.lang.Long salesid;
	
	@Column(name = "salesname", length = 30)
	private java.lang.String salesname;
	
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "pod", length = 30)
	private java.lang.String pod;
	
	@Column(name = "cls")
	private java.util.Date cls;
	
	@Column(name = "etd")
	private java.util.Date etd;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "contact")
	private java.lang.String contact;
	
	@Column(name = "email", length = 100)
	private java.lang.String email;
	
	@Column(name = "qq", length = 100)
	private java.lang.String qq;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	
	
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNos() {
		return nos;
	}

	public void setNos(java.lang.String nos) {
		this.nos = nos;
	}

	public java.lang.Long getPricefclid() {
		return pricefclid;
	}

	public void setPricefclid(java.lang.Long pricefclid) {
		this.pricefclid = pricefclid;
	}

	public java.lang.Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(java.lang.Long customerid) {
		this.customerid = customerid;
	}

	public java.lang.Long getSalesid() {
		return salesid;
	}

	public void setSalesid(java.lang.Long salesid) {
		this.salesid = salesid;
	}

	public java.lang.String getSalesname() {
		return salesname;
	}

	public void setSalesname(java.lang.String salesname) {
		this.salesname = salesname;
	}

	public java.lang.Long getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(java.lang.Long carrierid) {
		this.carrierid = carrierid;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getContact() {
		return contact;
	}

	public void setContact(java.lang.String contact) {
		this.contact = contact;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getQq() {
		return qq;
	}

	public void setQq(java.lang.String qq) {
		this.qq = qq;
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
	

}