package com.scp.model.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "api_oocl_bookingdata")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiOoclBookData implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy")
	private long id;
	
	@Column(name = "productid")
	private String productid;

	@Column(name = "containertype")
	private String containertype;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "containertype2")
	private String containertype2;

	@Column(name = "quantity2")
	private Integer quantity2;

	@Column(name = "containertype3")
	private String containertype3;

	@Column(name = "quantity3")
	private Integer quantity3;

	@Column(name = "blquantity")
	private Integer blquantity;

	@Column(name = "amount")
	private Integer amount;

	@Column(name = "couponid")
	private String couponid;

	@Column(name = "includeInsurance")
	private Boolean includeInsurance;

	@Column(name = "shippername", length = 70)
	private String shippername;

	@Column(name = "shipperaddressLine1", length = 35)
	private String shipperaddressLine1;

	@Column(name = "shipperaddressLine2", length = 35)
	private String shipperaddressLine2;

	@Column(name = "shipperphone", length = 22)
	private String shipperphone;

	@Column(name = "shipperemail", length = 400)
	private String shipperemail;


	@Column(name = "consigneename", length = 70)
	private String consigneename;

	@Column(name = "consigneeaddressLine1", length = 35)
	private String consigneeaddressLine1;

	@Column(name = "consigneeaddressLine2", length = 35)
	private String consigneeaddressLine2;

	@Column(name = "consigneephone", length = 22)
	private String consigneephone;

	@Column(name = "consigneeemail", length = 400)
	private String consigneeemail;


	@Column(name = "notifyname", length = 70)
	private String notifyname;

	@Column(name = "notifyaddressLine1", length = 35)
	private String notifyaddressLine1;

	@Column(name = "notifyaddressLine2", length = 35)
	private String notifyaddressLine2;

	@Column(name = "notifyphone", length = 22)
	private String notifyphone;

	@Column(name = "notifyemail", length = 400)
	private String notifyemail;


	@Column(name = "bookername", length = 70)
	private String bookername;

	@Column(name = "bookeremail", length = 400)
	private String bookeremail;

	@Column(name = "bookermobile", length = 15)
	private String bookermobile;

	@Column(name = "bookerphone", length = 22)
	private String bookerphone;

	@Column(name = "bookeraddress", length = 245)
	private String bookeraddress;


	@Column(name = "cargodesc", length = 60)
	private String cargodesc;

	@Column(name = "cargopackagetype")
	private String cargopackagetype;

	@Column(name = "cargoquantity")
	private Integer cargoquantity;

	@Column(name = "cargoweight")
	private java.math.BigDecimal cargoweight;

	@Column(name = "cargovolume")
	private java.math.BigDecimal cargovolume;

	@Column(name = "cargoremarks")
	private String cargoremarks;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "inputer" , length = 100)
	private String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater" , length = 100)
	private String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

	@Column(name = "isdelete")
	private Boolean isdelete;

	@Column(name = "orderno")
	private String orderno;

	@Column(name = "brno")
	private String brno;

	@Column(name = "pol")
	private String pol;

	@Column(name = "pod")
	private String pod;

	@Column(name = "etd")
	private java.util.Date etd;

	@Column(name = "linecode")
	private String linecode;

	@Column(name = "vesvoy")
	private String vesvoy;

	@Column(name = "loadingserviceno")
	private String loadingserviceno;

	@Column(name = "dischargeserviceno")
	private String dischargeserviceno;

	@Column(name = "porcity")
	private String porcity;

	@Column(name = "fndcity")
	private String fndcity;

	@Column(name = "issplitbox")
	private Boolean issplitbox;



	@Column(name = "isusecoupon")
	private Boolean isusecoupon;

    @Column(name = "usecouponnum")
    private String usecouponnum;

    public String getUsecouponnum() {
        return usecouponnum;
    }

    public void setUsecouponnum(String usecouponnum) {
        this.usecouponnum = usecouponnum;
    }

    public Boolean getIsusecoupon() {
        return isusecoupon;
    }

    public void setIsusecoupon(Boolean isusecoupon) {
        this.isusecoupon = isusecoupon;
    }

    public Boolean getIssplitbox() {
		return issplitbox;
	}

	public void setIssplitbox(Boolean issplitbox) {
		this.issplitbox = issplitbox;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getContainertype() {
		return containertype;
	}

	public void setContainertype(String containertype) {
		this.containertype = containertype;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getBlquantity() {
		return blquantity;
	}

	public void setBlquantity(Integer blquantity) {
		this.blquantity = blquantity;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public Boolean getIncludeInsurance() {
		return includeInsurance;
	}

	public void setIncludeInsurance(Boolean includeInsurance) {
		this.includeInsurance = includeInsurance;
	}

	public String getShippername() {
		return shippername;
	}

	public void setShippername(String shippername) {
		this.shippername = shippername;
	}

	public String getShipperaddressLine1() {
		return shipperaddressLine1;
	}

	public void setShipperaddressLine1(String shipperaddressLine1) {
		this.shipperaddressLine1 = shipperaddressLine1;
	}

	public String getShipperaddressLine2() {
		return shipperaddressLine2;
	}

	public void setShipperaddressLine2(String shipperaddressLine2) {
		this.shipperaddressLine2 = shipperaddressLine2;
	}

	public String getShipperphone() {
		return shipperphone;
	}

	public void setShipperphone(String shipperphone) {
		this.shipperphone = shipperphone;
	}

	public String getShipperemail() {
		return shipperemail;
	}

	public void setShipperemail(String shipperemail) {
		this.shipperemail = shipperemail;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddressLine1() {
		return consigneeaddressLine1;
	}

	public void setConsigneeaddressLine1(String consigneeaddressLine1) {
		this.consigneeaddressLine1 = consigneeaddressLine1;
	}

	public String getConsigneeaddressLine2() {
		return consigneeaddressLine2;
	}

	public void setConsigneeaddressLine2(String consigneeaddressLine2) {
		this.consigneeaddressLine2 = consigneeaddressLine2;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneeemail() {
		return consigneeemail;
	}

	public void setConsigneeemail(String consigneeemail) {
		this.consigneeemail = consigneeemail;
	}

	public String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(String notifyname) {
		this.notifyname = notifyname;
	}

	public String getNotifyaddressLine1() {
		return notifyaddressLine1;
	}

	public void setNotifyaddressLine1(String notifyaddressLine1) {
		this.notifyaddressLine1 = notifyaddressLine1;
	}

	public String getNotifyaddressLine2() {
		return notifyaddressLine2;
	}

	public void setNotifyaddressLine2(String notifyaddressLine2) {
		this.notifyaddressLine2 = notifyaddressLine2;
	}

	public String getNotifyphone() {
		return notifyphone;
	}

	public void setNotifyphone(String notifyphone) {
		this.notifyphone = notifyphone;
	}

	public String getNotifyemail() {
		return notifyemail;
	}

	public void setNotifyemail(String notifyemail) {
		this.notifyemail = notifyemail;
	}

	public String getBookername() {
		return bookername;
	}

	public void setBookername(String bookername) {
		this.bookername = bookername;
	}

	public String getBookeremail() {
		return bookeremail;
	}

	public void setBookeremail(String bookeremail) {
		this.bookeremail = bookeremail;
	}

	public String getBookermobile() {
		return bookermobile;
	}

	public void setBookermobile(String bookermobile) {
		this.bookermobile = bookermobile;
	}

	public String getBookerphone() {
		return bookerphone;
	}

	public void setBookerphone(String bookerphone) {
		this.bookerphone = bookerphone;
	}

	public String getBookeraddress() {
		return bookeraddress;
	}

	public void setBookeraddress(String bookeraddress) {
		this.bookeraddress = bookeraddress;
	}

	public String getCargodesc() {
		return cargodesc;
	}

	public void setCargodesc(String cargodesc) {
		this.cargodesc = cargodesc;
	}

	public String getCargopackagetype() {
		return cargopackagetype;
	}

	public void setCargopackagetype(String cargopackagetype) {
		this.cargopackagetype = cargopackagetype;
	}

	public Integer getCargoquantity() {
		return cargoquantity;
	}

	public void setCargoquantity(Integer cargoquantity) {
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

	public String getCargoremarks() {
		return cargoremarks;
	}

	public void setCargoremarks(String cargoremarks) {
		this.cargoremarks = cargoremarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public String getContainertype2() {
		return containertype2;
	}

	public void setContainertype2(String containertype2) {
		this.containertype2 = containertype2;
	}

	public Integer getQuantity2() {
		return quantity2;
	}

	public void setQuantity2(Integer quantity2) {
		this.quantity2 = quantity2;
	}

	public String getContainertype3() {
		return containertype3;
	}

	public void setContainertype3(String containertype3) {
		this.containertype3 = containertype3;
	}

	public Integer getQuantity3() {
		return quantity3;
	}

	public void setQuantity3(Integer quantity3) {
		this.quantity3 = quantity3;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
	}

	public String getLinecode() {
		return linecode;
	}

	public void setLinecode(String linecode) {
		this.linecode = linecode;
	}

	public String getVesvoy() {
		return vesvoy;
	}

	public void setVesvoy(String vesvoy) {
		this.vesvoy = vesvoy;
	}

	public String getLoadingserviceno() {
		return loadingserviceno;
	}

	public void setLoadingserviceno(String loadingserviceno) {
		this.loadingserviceno = loadingserviceno;
	}

	public String getDischargeserviceno() {
		return dischargeserviceno;
	}

	public void setDischargeserviceno(String dischargeserviceno) {
		this.dischargeserviceno = dischargeserviceno;
	}

	public String getPorcity() {
		return porcity;
	}

	public void setPorcity(String porcity) {
		this.porcity = porcity;
	}

	public String getFndcity() {
		return fndcity;
	}

	public void setFndcity(String fndcity) {
		this.fndcity = fndcity;
	}

	
}
