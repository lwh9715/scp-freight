package com.scp.model.ship;

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
@Table(name = "bus_ship_container")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipContainer implements java.io.Serializable {


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
	@Column(name = "shipid")
	private java.lang.Long shipid;
	
	@Column(name = "bookdtlid")
	private java.lang.Long bookdtlid;
	
	@Column(name = "billid")
	private java.lang.Long billid;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "cntypeid")
	private java.lang.Long cntypeid;
	
	@Column(name = "ldtype", length = 1)
	private java.lang.String ldtype;
	

	/**
	 *@generated
	 */
	@Column(name = "cntno", length = 20)
	private java.lang.String cntno;
	
	@Column(name = "wghtime")
	private java.util.Date wghtime;
	
	public java.util.Date getWghtime() {
		return wghtime;
	}

	public void setWghtime(java.util.Date wghtime) {
		this.wghtime = wghtime;
	}

	/**
	 *@generated
	 */
	@Column(name = "sealno", length = 20)
	private java.lang.String sealno;

	/**
	 *@generated
	 */
	@Column(name = "sealno2", length = 20)
	private java.lang.String sealno2;

	/**
	 *@generated
	 */
	@Column(name = "sono", length = 20)
	private java.lang.String sono;

	/**
	 *@generated
	 */
	@Column(name = "goodsname")
	private java.lang.String goodsname;

	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.Integer piece;

	/**
	 *@generated
	 */
	@Column(name = "grswgt")
	private java.math.BigDecimal grswgt;
	
	@Column(name = "vgm")
	private java.math.BigDecimal vgm;
	
	@Column(name = "spacecbm")
	private java.math.BigDecimal spacecbm;
	
	@Column(name = "ispace")
	private java.lang.Boolean ispace;
	
	@Column(name = "netwgt")
	private java.math.BigDecimal netwgt;
	
	@Column(name = "chargwgt")
	private java.math.BigDecimal chargwgt;
	
	@Column(name = "price")
	private java.math.BigDecimal price;
	
	@Column(name = "goodsvalue")
	private java.math.BigDecimal goodsvalue;


	/**
	 *@generated
	 */
	@Column(name = "cbm")
	private java.math.BigDecimal cbm;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

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
	
	@Column(name = "currency", length = 20)
	private java.lang.String currency;
	
	@Column(name = "goodsnamee")
	private java.lang.String goodsnamee ;

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
	
	@Column(name = "l")
	private java.math.BigDecimal l;
	
	@Column(name = "w")
	private java.math.BigDecimal w;
	
	@Column(name = "h")
	private java.math.BigDecimal h;
	
	@Column(name = "markno")
	private java.lang.String markno;
	
	
	@Column(name = "loc")
	private java.lang.String loc;
	
	
	@Column(name = "packagee")
	private java.lang.String packagee;
	
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	
	
	@Column(name = "material")
	private java.lang.String material;
	
	/**
	 *@generated
	 */
	@Column(name = "parentid")
	private java.lang.Long parentid;
	
	
	@Column(name = "isselect")
	private java.lang.Boolean isselect;
	
	
	@Column(name = "declareno")
	private java.lang.String declareno;
	
	/**
	 *@generated
	 */
	@Column(name = "hscode", length = 30)
	private java.lang.String hscode;
	
	
	@Column(name = "orderno")
	private java.lang.Integer orderno;
	
	/**
	 *@generated
	 */
	@Column(name = "netcbm")
	private java.math.BigDecimal netcbm;
	
	@Column(name = "styleno")
	private java.lang.String styleno;
	
	/**
	 *@generated
	 */
	@Column(name = "temperatureUOM", length = 1)
	private java.lang.String temperatureUOM;
	
	@Column(name = "minTemperature")
	private java.math.BigDecimal minTemperature;
	
	@Column(name = "maxTemperature")
	private java.math.BigDecimal maxTemperature;
	
	@Column(name = "presetTemperature")
	private java.math.BigDecimal presetTemperature;
	
	/**
	 *@generated
	 */
	@Column(name = "isGensets")
	private java.lang.Boolean isGensets;
	
	
	@Column(name = "ventOpenValue")
	private java.math.BigDecimal ventOpenValue;
	
	/**
	 *@generated
	 */
	@Column(name = "isPretrip")
	private java.lang.Boolean isPretrip;
	
	/**
	 *@generated
	 */
	@Column(name = "reeferVent", length = 1)
	private java.lang.String reeferVent;
	
	/**
	 *@generated
	 */
	@Column(name = "isTemperature")
	private java.lang.Boolean isTemperature;
	
	/**
	 *@generated
	 */
	@Column(name = "iself")
	private java.lang.Boolean iself;
	
	/**
	 *@generated
	 */
	@Column(name = "send_sealno", length = 30)
	private java.lang.String send_sealno;
	
	/**
	 *@generated
	 */
	@Column(name = "send_caiid", length = 20)
	private java.lang.String send_caiid;
	
	/**
	 *@generated
	 */
	@Column(name = "send_pws", length = 20)
	private java.lang.String send_pws;
	
	/**
	 *@generated
	 */
	@Column(name = "send_driver", length = 30)
	private java.lang.String send_driver;
	
	/**
	 *@generated
	 */
	@Column(name = "send_drivertel", length = 30)
	private java.lang.String send_drivertel;
	
	@Column(name = "date_gatein")
	private java.util.Date date_gatein;
	
	@Column(name = "emergencycontact", length = 50)
	private java.lang.String emergencycontact;
	
	@Column(name = "emergencytel", length = 50)
	private java.lang.String emergencytel;
	
	@Column(name = "emergencyemail", length = 100)
	private java.lang.String emergencyemail;
	
	@Column(name = "emergencyreference", length = 50)
	private java.lang.String emergencyreference;
	
	@Column(name = "unnumber", length = 50)
	private java.lang.String unnumber;
	
	@Column(name = "imoclass", length = 50)
	private java.lang.String imoclass;
	
	@Column(name = "uom", length = 50)
	private java.lang.String uom;
	
	@Column(name = "marinepollutant")
	private java.lang.Boolean marinepollutant;
	
	@Column(name = "flashpoint", length = 50)
	private java.lang.String flashpoint;
	
	@Column(name = "outerpackagecode", length = 50)
	private java.lang.String outerpackagecode;
	
	@Column(name = "outerquantity", length = 50)
	private java.lang.String outerquantity;
	
	@Column(name = "tnnerpackagecode", length = 50)
	private java.lang.String tnnerpackagecode;
	
	@Column(name = "innerquantity", length = 50)
	private java.lang.String innerquantity;
	
	@Column(name = "chemicalname", length = 50)
	private java.lang.String chemicalname;
	
	@Column(name = "isdangers")
	private java.lang.Boolean isdangers;
	
	@Column(name = "grswgtempty")
	private java.math.BigDecimal grswgtempty;
	
	@Column(name = "carriageTemperature")
	private java.math.BigDecimal carriageTemperature;
	
	
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	@Column(name = "linktbl", length = 30)
	private java.lang.String linktbl;
	
	@Column(name = "vgmmethod", length = 10)
	private java.lang.String vgmmethod;
	
	@Column(name = "vgmsignature", length = 20)
	private java.lang.String vgmsignature;
	
	@Column(name = "vgmtelephone", length = 100)
	private java.lang.String vgmtelephone;
	
	@Column(name = "vgmemail", length = 100)
	private java.lang.String vgmemail;
	
	@Column(name = "vgmaddress", length = 100)
	private java.lang.String vgmaddress;
	
	@Column(name = "boxbelong")
	private java.lang.String boxbelong;
	
	@Column(name = "boxbelongcopr")
	private java.lang.String boxbelongcopr;
	
	@Column(name = "rentboxso")
	private java.lang.String rentboxso;
	
	@Column(name = "emptycabinettime")
	private java.util.Date emptycabinettime;
	
	@Column(name = "loadtime")
	private java.util.Date loadtime;
	
	@Column(name = "arrivaldate")
	private java.util.Date arrivaldate;
	
	@Column(name = "rentboxplace")
	private java.lang.String rentboxplace;
	
	@Column(name = "retrunboxplace")
	private java.lang.String retrunboxplace;
	
	@Column(name = "isautosynbyso")
	private java.lang.Boolean isautosynbyso;

	@Column(name = "invoicenumber")
	private java.lang.String invoicenumber;

	@Column(name = "packinglistnumber")
	private java.lang.String packinglistnumber;


	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getPackinglistnumber() {
		return packinglistnumber;
	}

	public void setPackinglistnumber(String packinglistnumber) {
		this.packinglistnumber = packinglistnumber;
	}

	public java.lang.Boolean getIsautosynbyso() {
		return isautosynbyso;
	}

	public void setIsautosynbyso(java.lang.Boolean isautosynbyso) {
		this.isautosynbyso = isautosynbyso;
	}

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
	
	

	public java.util.Date getArrivaldate() {
		return arrivaldate;
	}

	public void setArrivaldate(java.util.Date arrivaldate) {
		this.arrivaldate = arrivaldate;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getShipid() {
		return this.shipid;
	}

	/**
	 *@generated
	 */
	public void setShipid(java.lang.Long value) {
		this.shipid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getJobid() {
		return this.jobid;
	}
	
	


	/**
	 *@generated
	 */
	public void setJobid(java.lang.Long value) {
		this.jobid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCntypeid() {
		return this.cntypeid;
	}

	/**
	 *@generated
	 */
	public void setCntypeid(java.lang.Long value) {
		this.cntypeid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCntno() {
		return this.cntno;
	}

	/**
	 *@generated
	 */
	public void setCntno(java.lang.String value) {
		this.cntno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSealno() {
		return this.sealno;
	}

	/**
	 *@generated
	 */
	public void setSealno(java.lang.String value) {
		this.sealno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSealno2() {
		return this.sealno2;
	}

	/**
	 *@generated
	 */
	public void setSealno2(java.lang.String value) {
		this.sealno2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSono() {
		return this.sono;
	}

	/**
	 *@generated
	 */
	public void setSono(java.lang.String value) {
		this.sono = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsname() {
		return this.goodsname;
	}

	/**
	 *@generated
	 */
	public void setGoodsname(java.lang.String value) {
		this.goodsname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getPiece() {
		return this.piece;
	}

	/**
	 *@generated
	 */
	public void setPiece(java.lang.Integer value) {
		this.piece = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt() {
		return this.grswgt;
	}

	/**
	 *@generated
	 */
	public void setGrswgt(java.math.BigDecimal value) {
		this.grswgt = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getCbm() {
		return this.cbm;
	}

	/**
	 *@generated
	 */
	public void setCbm(java.math.BigDecimal value) {
		this.cbm = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCorpid() {
		return this.corpid;
	}

	/**
	 *@generated
	 */
	public void setCorpid(java.lang.Long value) {
		this.corpid = value;
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

	public java.lang.Long getBookdtlid() {
		return bookdtlid;
	}

	public java.lang.Long getBillid() {
		return billid;
	}

	public void setBookdtlid(java.lang.Long bookdtlid) {
		this.bookdtlid = bookdtlid;
	}

	public void setBillid(java.lang.Long billid) {
		this.billid = billid;
	}

	public java.lang.String getLdtype() {
		return ldtype;
	}

	public void setLdtype(java.lang.String ldtype) {
		this.ldtype = ldtype;
	}

	public java.math.BigDecimal getL() {
		return l;
	}

	public java.math.BigDecimal getW() {
		return w;
	}

	public java.math.BigDecimal getH() {
		return h;
	}

	public java.lang.String getMarkno() {
		return markno;
	}

	public java.lang.String getLoc() {
		return loc;
	}

	public java.lang.String getPackagee() {
		return packagee;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setL(java.math.BigDecimal l) {
		this.l = l;
	}

	public void setW(java.math.BigDecimal w) {
		this.w = w;
	}

	public void setH(java.math.BigDecimal h) {
		this.h = h;
	}

	public void setMarkno(java.lang.String markno) {
		this.markno = markno;
	}

	public void setLoc(java.lang.String loc) {
		this.loc = loc;
	}

	public void setPackagee(java.lang.String packagee) {
		this.packagee = packagee;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.Long getParentid() {
		return parentid;
	}

	public void setParentid(java.lang.Long parentid) {
		this.parentid = parentid;
	}
	public java.math.BigDecimal getNetwgt() {
		return netwgt;
	}

	public void setNetwgt(java.math.BigDecimal netwgt) {
		this.netwgt = netwgt;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.math.BigDecimal getGoodsvalue() {
		return goodsvalue;
	}

	public void setGoodsvalue(java.math.BigDecimal goodsvalue) {
		this.goodsvalue = goodsvalue;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.String getGoodsnamee() {
		return goodsnamee;
	}

	public void setGoodsnamee(java.lang.String goodsnamee) {
		this.goodsnamee = goodsnamee;
	}

	public java.lang.String getMaterial() {
		return material;
	}

	public void setMaterial(java.lang.String material) {
		this.material = material;
	}

	public java.math.BigDecimal getChargwgt() {
		return chargwgt;
	}

	public void setChargwgt(java.math.BigDecimal chargwgt) {
		this.chargwgt = chargwgt;
	}

	public java.lang.Boolean getIsselect() {
		return isselect;
	}

	public void setIsselect(java.lang.Boolean isselect) {
		this.isselect = isselect;
	}

	public java.lang.String getDeclareno() {
		return declareno;
	}

	public void setDeclareno(java.lang.String declareno) {
		this.declareno = declareno;
	}

	public java.lang.String getHscode() {
		return hscode;
	}

	public void setHscode(java.lang.String hscode) {
		this.hscode = hscode;
	}

	public java.lang.Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.Integer orderno) {
		this.orderno = orderno;
	}

	public java.math.BigDecimal getNetcbm() {
		return netcbm;
	}

	public void setNetcbm(java.math.BigDecimal netcbm) {
		this.netcbm = netcbm;
	}

	public java.lang.String getStyleno() {
		return styleno;
	}

	public void setStyleno(java.lang.String styleno) {
		this.styleno = styleno;
	}

	public java.lang.String getTemperatureUOM() {
		return temperatureUOM;
	}

	public void setTemperatureUOM(java.lang.String temperatureUOM) {
		this.temperatureUOM = temperatureUOM;
	}

	public java.math.BigDecimal getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(java.math.BigDecimal minTemperature) {
		this.minTemperature = minTemperature;
	}

	public java.math.BigDecimal getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(java.math.BigDecimal maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public java.lang.Boolean getIsGensets() {
		return isGensets;
	}

	public void setIsGensets(java.lang.Boolean isGensets) {
		this.isGensets = isGensets;
	}

	public java.math.BigDecimal getVentOpenValue() {
		return ventOpenValue;
	}

	public void setVentOpenValue(java.math.BigDecimal ventOpenValue) {
		this.ventOpenValue = ventOpenValue;
	}

	public java.lang.Boolean getIsPretrip() {
		return isPretrip;
	}

	public void setIsPretrip(java.lang.Boolean isPretrip) {
		this.isPretrip = isPretrip;
	}

	public java.lang.String getReeferVent() {
		return reeferVent;
	}

	public void setReeferVent(java.lang.String reeferVent) {
		this.reeferVent = reeferVent;
	}

	public java.lang.Boolean getIsTemperature() {
		return isTemperature;
	}

	public void setIsTemperature(java.lang.Boolean isTemperature) {
		this.isTemperature = isTemperature;
	}

	public java.lang.Boolean getIself() {
		return iself;
	}

	public void setIself(java.lang.Boolean iself) {
		this.iself = iself;
	}

	public java.math.BigDecimal getVgm() {
		return vgm;
	}

	public void setVgm(java.math.BigDecimal vgm) {
		this.vgm = vgm;
	}

	public java.math.BigDecimal getSpacecbm() {
		return spacecbm;
	}

	public void setSpacecbm(java.math.BigDecimal spacecbm) {
		this.spacecbm = spacecbm;
	}

	public java.lang.Boolean getIspace() {
		return ispace;
	}

	public void setIspace(java.lang.Boolean ispace) {
		this.ispace = ispace;
	}

	public java.lang.String getSend_sealno() {
		return send_sealno;
	}

	public void setSend_sealno(java.lang.String sendSealno) {
		send_sealno = sendSealno;
	}

	public java.lang.String getSend_caiid() {
		return send_caiid;
	}

	public void setSend_caiid(java.lang.String sendCaiid) {
		send_caiid = sendCaiid;
	}

	public java.lang.String getSend_pws() {
		return send_pws;
	}

	public void setSend_pws(java.lang.String sendPws) {
		send_pws = sendPws;
	}

	public java.lang.String getSend_driver() {
		return send_driver;
	}

	public void setSend_driver(java.lang.String sendDriver) {
		send_driver = sendDriver;
	}

	public java.lang.String getSend_drivertel() {
		return send_drivertel;
	}

	public void setSend_drivertel(java.lang.String sendDrivertel) {
		send_drivertel = sendDrivertel;
	}

	public java.util.Date getDate_gatein() {
		return date_gatein;
	}

	public void setDate_gatein(java.util.Date dateGatein) {
		date_gatein = dateGatein;
	}

	public java.lang.String getEmergencycontact() {
		return emergencycontact;
	}

	public void setEmergencycontact(java.lang.String emergencycontact) {
		this.emergencycontact = emergencycontact;
	}

	public java.lang.String getEmergencytel() {
		return emergencytel;
	}

	public void setEmergencytel(java.lang.String emergencytel) {
		this.emergencytel = emergencytel;
	}

	public java.lang.String getEmergencyemail() {
		return emergencyemail;
	}

	public void setEmergencyemail(java.lang.String emergencyemail) {
		this.emergencyemail = emergencyemail;
	}

	public java.lang.String getEmergencyreference() {
		return emergencyreference;
	}

	public void setEmergencyreference(java.lang.String emergencyreference) {
		this.emergencyreference = emergencyreference;
	}

	public java.lang.String getUnnumber() {
		return unnumber;
	}

	public void setUnnumber(java.lang.String unnumber) {
		this.unnumber = unnumber;
	}

	public java.lang.String getImoclass() {
		return imoclass;
	}

	public void setImoclass(java.lang.String imoclass) {
		this.imoclass = imoclass;
	}

	public java.lang.String getUom() {
		return uom;
	}

	public void setUom(java.lang.String uom) {
		this.uom = uom;
	}

	public java.lang.Boolean getMarinepollutant() {
		return marinepollutant;
	}

	public void setMarinepollutant(java.lang.Boolean marinepollutant) {
		this.marinepollutant = marinepollutant;
	}

	public java.lang.String getFlashpoint() {
		return flashpoint;
	}

	public void setFlashpoint(java.lang.String flashpoint) {
		this.flashpoint = flashpoint;
	}

	public java.lang.String getOuterpackagecode() {
		return outerpackagecode;
	}

	public void setOuterpackagecode(java.lang.String outerpackagecode) {
		this.outerpackagecode = outerpackagecode;
	}

	public java.lang.String getOuterquantity() {
		return outerquantity;
	}

	public void setOuterquantity(java.lang.String outerquantity) {
		this.outerquantity = outerquantity;
	}

	public java.lang.String getTnnerpackagecode() {
		return tnnerpackagecode;
	}

	public void setTnnerpackagecode(java.lang.String tnnerpackagecode) {
		this.tnnerpackagecode = tnnerpackagecode;
	}

	public java.lang.String getInnerquantity() {
		return innerquantity;
	}

	public void setInnerquantity(java.lang.String innerquantity) {
		this.innerquantity = innerquantity;
	}

	public java.lang.String getChemicalname() {
		return chemicalname;
	}

	public void setChemicalname(java.lang.String chemicalname) {
		this.chemicalname = chemicalname;
	}

	public java.lang.Boolean getIsdangers() {
		return isdangers;
	}

	public void setIsdangers(java.lang.Boolean isdangers) {
		this.isdangers = isdangers;
	}

	public java.math.BigDecimal getGrswgtempty() {
		return grswgtempty;
	}

	public void setGrswgtempty(java.math.BigDecimal grswgtempty) {
		this.grswgtempty = grswgtempty;
	}

	public java.math.BigDecimal getCarriageTemperature() {
		return carriageTemperature;
	}

	public void setCarriageTemperature(java.math.BigDecimal carriageTemperature) {
		this.carriageTemperature = carriageTemperature;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.math.BigDecimal getPresetTemperature() {
		return presetTemperature;
	}

	public void setPresetTemperature(java.math.BigDecimal presetTemperature) {
		this.presetTemperature = presetTemperature;
	}

	public java.lang.String getVgmmethod() {
		return vgmmethod;
	}

	public void setVgmmethod(java.lang.String vgmmethod) {
		this.vgmmethod = vgmmethod;
	}

	public java.lang.String getVgmsignature() {
		return vgmsignature;
	}

	public void setVgmsignature(java.lang.String vgmsignature) {
		this.vgmsignature = vgmsignature;
	}

	public java.lang.String getVgmtelephone() {
		return vgmtelephone;
	}

	public void setVgmtelephone(java.lang.String vgmtelephone) {
		this.vgmtelephone = vgmtelephone;
	}

	public java.lang.String getVgmemail() {
		return vgmemail;
	}

	public void setVgmemail(java.lang.String vgmemail) {
		this.vgmemail = vgmemail;
	}

	public java.lang.String getVgmaddress() {
		return vgmaddress;
	}

	public void setVgmaddress(java.lang.String vgmaddress) {
		this.vgmaddress = vgmaddress;
	}

	public java.lang.String getBoxbelong() {
		return boxbelong;
	}

	public void setBoxbelong(java.lang.String boxbelong) {
		this.boxbelong = boxbelong;
	}

	public java.lang.String getBoxbelongcopr() {
		return boxbelongcopr;
	}

	public void setBoxbelongcopr(java.lang.String boxbelongcopr) {
		this.boxbelongcopr = boxbelongcopr;
	}

	public java.lang.String getRentboxso() {
		return rentboxso;
	}

	public void setRentboxso(java.lang.String rentboxso) {
		this.rentboxso = rentboxso;
	}

	public java.util.Date getEmptycabinettime() {
		return emptycabinettime;
	}

	public void setEmptycabinettime(java.util.Date emptycabinettime) {
		this.emptycabinettime = emptycabinettime;
	}

	public java.util.Date getLoadtime() {
		return loadtime;
	}

	public void setLoadtime(java.util.Date loadtime) {
		this.loadtime = loadtime;
	}

	public java.lang.String getRentboxplace() {
		return rentboxplace;
	}

	public void setRentboxplace(java.lang.String rentboxplace) {
		this.rentboxplace = rentboxplace;
	}

	public java.lang.String getRetrunboxplace() {
		return retrunboxplace;
	}

	public void setRetrunboxplace(java.lang.String retrunboxplace) {
		this.retrunboxplace = retrunboxplace;
	}
	
}