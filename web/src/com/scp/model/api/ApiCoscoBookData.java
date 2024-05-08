package com.scp.model.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "api_cosco_bookingdata")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiCoscoBookData implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "productid")
	private java.lang.String productid;
	
	@Column(name = "containertype")
	private java.lang.String containertype;
	
	@Column(name = "quantity")
	private java.lang.Integer quantity;
	
	@Column(name = "containertype2")
	private java.lang.String containertype2;
	
	@Column(name = "quantity2")
	private java.lang.Integer quantity2;
	
	@Column(name = "containertype3")
	private java.lang.String containertype3;
	
	@Column(name = "quantity3")
	private java.lang.Integer quantity3;
	
	@Column(name = "blquantity")
	private java.lang.Integer blquantity;
	
	@Column(name = "amount")
	private java.lang.Integer amount;
	
	@Column(name = "couponid")
	private java.lang.String couponid;
	
	@Column(name = "includeInsurance")
	private java.lang.Boolean includeInsurance;
	
	@Column(name = "shippername", length = 70)
	private java.lang.String shippername;
	
	@Column(name = "shipperaddressLine1", length = 35)
	private java.lang.String shipperaddressLine1;
	
	@Column(name = "shipperaddressLine2", length = 35)
	private java.lang.String shipperaddressLine2;
	
	@Column(name = "shipperphone", length = 22)
	private java.lang.String shipperphone;
	
	@Column(name = "shipperemail", length = 400)
	private java.lang.String shipperemail;
	
	
	@Column(name = "consigneename", length = 70)
	private java.lang.String consigneename;
	
	@Column(name = "consigneeaddressLine1", length = 35)
	private java.lang.String consigneeaddressLine1;
	
	@Column(name = "consigneeaddressLine2", length = 35)
	private java.lang.String consigneeaddressLine2;
	
	@Column(name = "consigneephone", length = 22)
	private java.lang.String consigneephone;
	
	@Column(name = "consigneeemail", length = 400)
	private java.lang.String consigneeemail;
	
	
	@Column(name = "notifyname", length = 70)
	private java.lang.String notifyname;
	
	@Column(name = "notifyaddressLine1", length = 35)
	private java.lang.String notifyaddressLine1;
	
	@Column(name = "notifyaddressLine2", length = 35)
	private java.lang.String notifyaddressLine2;
	
	@Column(name = "notifyphone", length = 22)
	private java.lang.String notifyphone;
	
	@Column(name = "notifyemail", length = 400)
	private java.lang.String notifyemail;
	
	
	@Column(name = "bookername", length = 70)
	private java.lang.String bookername;
	
	@Column(name = "bookeremail", length = 400)
	private java.lang.String bookeremail;
	
	@Column(name = "bookermobile", length = 15)
	private java.lang.String bookermobile;
	
	@Column(name = "bookerphone", length = 22)
	private java.lang.String bookerphone;
	
	@Column(name = "bookeraddress", length = 245)
	private java.lang.String bookeraddress;
	
	
	@Column(name = "cargodesc", length = 60)
	private java.lang.String cargodesc;
	
	@Column(name = "cargopackagetype")
	private java.lang.String cargopackagetype;
	
	@Column(name = "cargoquantity")
	private java.lang.Integer cargoquantity;
	
	@Column(name = "cargoweight")
	private java.math.BigDecimal cargoweight;
	
	@Column(name = "cargovolume")
	private java.math.BigDecimal cargovolume;
	
	@Column(name = "cargoremarks")
	private java.lang.String cargoremarks;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "inputer" , length = 100)
	private java.lang.String inputer;
	
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	@Column(name = "updater" , length = 100)
	private java.lang.String updater;
	
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "orderno")
	private java.lang.String orderno;
	
	@Column(name = "brno")
	private java.lang.String brno;
	
	@Column(name = "pol")
	private java.lang.String pol;
	
	@Column(name = "pod")
	private java.lang.String pod;
	
	@Column(name = "etd")
	private java.util.Date etd;
	
	@Column(name = "linecode")
	private java.lang.String linecode;
	
	@Column(name = "vesvoy")
	private java.lang.String vesvoy;
	
	@Column(name = "loadingserviceno")
	private java.lang.String loadingserviceno;
	
	@Column(name = "dischargeserviceno")
	private java.lang.String dischargeserviceno;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getProductid() {
		return productid;
	}

	public void setProductid(java.lang.String productid) {
		this.productid = productid;
	}

	public java.lang.String getContainertype() {
		return containertype;
	}

	public void setContainertype(java.lang.String containertype) {
		this.containertype = containertype;
	}

	public java.lang.Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(java.lang.Integer quantity) {
		this.quantity = quantity;
	}
	
	public java.lang.Integer getBlquantity() {
		return blquantity;
	}

	public void setBlquantity(java.lang.Integer blquantity) {
		this.blquantity = blquantity;
	}

	public java.lang.Integer getAmount() {
		return amount;
	}

	public void setAmount(java.lang.Integer amount) {
		this.amount = amount;
	}

	public java.lang.String getCouponid() {
		return couponid;
	}

	public void setCouponid(java.lang.String couponid) {
		this.couponid = couponid;
	}

	public java.lang.Boolean getIncludeInsurance() {
		return includeInsurance;
	}

	public void setIncludeInsurance(java.lang.Boolean includeInsurance) {
		this.includeInsurance = includeInsurance;
	}

	public java.lang.String getShippername() {
		return shippername;
	}

	public void setShippername(java.lang.String shippername) {
		this.shippername = shippername;
	}

	public java.lang.String getShipperaddressLine1() {
		return shipperaddressLine1;
	}

	public void setShipperaddressLine1(java.lang.String shipperaddressLine1) {
		this.shipperaddressLine1 = shipperaddressLine1;
	}

	public java.lang.String getShipperaddressLine2() {
		return shipperaddressLine2;
	}

	public void setShipperaddressLine2(java.lang.String shipperaddressLine2) {
		this.shipperaddressLine2 = shipperaddressLine2;
	}

	public java.lang.String getShipperphone() {
		return shipperphone;
	}

	public void setShipperphone(java.lang.String shipperphone) {
		this.shipperphone = shipperphone;
	}

	public java.lang.String getShipperemail() {
		return shipperemail;
	}

	public void setShipperemail(java.lang.String shipperemail) {
		this.shipperemail = shipperemail;
	}

	public java.lang.String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(java.lang.String consigneename) {
		this.consigneename = consigneename;
	}

	public java.lang.String getConsigneeaddressLine1() {
		return consigneeaddressLine1;
	}

	public void setConsigneeaddressLine1(java.lang.String consigneeaddressLine1) {
		this.consigneeaddressLine1 = consigneeaddressLine1;
	}

	public java.lang.String getConsigneeaddressLine2() {
		return consigneeaddressLine2;
	}

	public void setConsigneeaddressLine2(java.lang.String consigneeaddressLine2) {
		this.consigneeaddressLine2 = consigneeaddressLine2;
	}

	public java.lang.String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(java.lang.String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public java.lang.String getConsigneeemail() {
		return consigneeemail;
	}

	public void setConsigneeemail(java.lang.String consigneeemail) {
		this.consigneeemail = consigneeemail;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public java.lang.String getNotifyaddressLine1() {
		return notifyaddressLine1;
	}

	public void setNotifyaddressLine1(java.lang.String notifyaddressLine1) {
		this.notifyaddressLine1 = notifyaddressLine1;
	}

	public java.lang.String getNotifyaddressLine2() {
		return notifyaddressLine2;
	}

	public void setNotifyaddressLine2(java.lang.String notifyaddressLine2) {
		this.notifyaddressLine2 = notifyaddressLine2;
	}

	public java.lang.String getNotifyphone() {
		return notifyphone;
	}

	public void setNotifyphone(java.lang.String notifyphone) {
		this.notifyphone = notifyphone;
	}

	public java.lang.String getNotifyemail() {
		return notifyemail;
	}

	public void setNotifyemail(java.lang.String notifyemail) {
		this.notifyemail = notifyemail;
	}

	public java.lang.String getBookername() {
		return bookername;
	}

	public void setBookername(java.lang.String bookername) {
		this.bookername = bookername;
	}

	public java.lang.String getBookeremail() {
		return bookeremail;
	}

	public void setBookeremail(java.lang.String bookeremail) {
		this.bookeremail = bookeremail;
	}

	public java.lang.String getBookermobile() {
		return bookermobile;
	}

	public void setBookermobile(java.lang.String bookermobile) {
		this.bookermobile = bookermobile;
	}

	public java.lang.String getBookerphone() {
		return bookerphone;
	}

	public void setBookerphone(java.lang.String bookerphone) {
		this.bookerphone = bookerphone;
	}

	public java.lang.String getBookeraddress() {
		return bookeraddress;
	}

	public void setBookeraddress(java.lang.String bookeraddress) {
		this.bookeraddress = bookeraddress;
	}

	public java.lang.String getCargodesc() {
		return cargodesc;
	}

	public void setCargodesc(java.lang.String cargodesc) {
		this.cargodesc = cargodesc;
	}

	public java.lang.String getCargopackagetype() {
		return cargopackagetype;
	}

	public void setCargopackagetype(java.lang.String cargopackagetype) {
		this.cargopackagetype = cargopackagetype;
	}

	public java.lang.Integer getCargoquantity() {
		return cargoquantity;
	}

	public void setCargoquantity(java.lang.Integer cargoquantity) {
		this.cargoquantity = cargoquantity;
	}

	public java.math.BigDecimal getCargoweight() {
		return cargoweight;
	}

	public void setCargoweight(java.math.BigDecimal cargoweight) {
		this.cargoweight = cargoweight;
	}

	public java.math.BigDecimal getCargovolume() {
		return cargovolume;
	}

	public void setCargovolume(java.math.BigDecimal cargovolume) {
		this.cargovolume = cargovolume;
	}

	public java.lang.String getCargoremarks() {
		return cargoremarks;
	}

	public void setCargoremarks(java.lang.String cargoremarks) {
		this.cargoremarks = cargoremarks;
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

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getContainertype2() {
		return containertype2;
	}

	public void setContainertype2(java.lang.String containertype2) {
		this.containertype2 = containertype2;
	}

	public java.lang.Integer getQuantity2() {
		return quantity2;
	}

	public void setQuantity2(java.lang.Integer quantity2) {
		this.quantity2 = quantity2;
	}

	public java.lang.String getContainertype3() {
		return containertype3;
	}

	public void setContainertype3(java.lang.String containertype3) {
		this.containertype3 = containertype3;
	}

	public java.lang.Integer getQuantity3() {
		return quantity3;
	}

	public void setQuantity3(java.lang.Integer quantity3) {
		this.quantity3 = quantity3;
	}

	public java.lang.String getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.String orderno) {
		this.orderno = orderno;
	}

	public java.lang.String getBrno() {
		return brno;
	}

	public void setBrno(java.lang.String brno) {
		this.brno = brno;
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

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
	}

	public java.lang.String getLinecode() {
		return linecode;
	}

	public void setLinecode(java.lang.String linecode) {
		this.linecode = linecode;
	}

	public java.lang.String getVesvoy() {
		return vesvoy;
	}

	public void setVesvoy(java.lang.String vesvoy) {
		this.vesvoy = vesvoy;
	}

	public java.lang.String getLoadingserviceno() {
		return loadingserviceno;
	}

	public void setLoadingserviceno(java.lang.String loadingserviceno) {
		this.loadingserviceno = loadingserviceno;
	}

	public java.lang.String getDischargeserviceno() {
		return dischargeserviceno;
	}

	public void setDischargeserviceno(java.lang.String dischargeserviceno) {
		this.dischargeserviceno = dischargeserviceno;
	}

	
}
