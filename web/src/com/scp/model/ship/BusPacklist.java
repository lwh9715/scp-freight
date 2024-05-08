package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Table(name = "bus_packlist")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusPacklist implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	/**
	 *@generated
	 */
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	/**
	 *@generated
	 */
	@Column(name = "linktbl")
	private java.lang.String linktbl;
	
	/**
	 *@generated
	 */
	@Column(name = "factoryname")
	private java.lang.String factoryname;
	/**
	 *@generated
	 */
	@Column(name = "factorynamee")
	private java.lang.String factorynamee;
	
	
	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.Long piece;
	
	/**
	 *@generated
	 */
	@Column(name = "packagee")
	private java.lang.String packagee;
	
	/**
	 *@generated
	 */
	@Column(name = "piece2")
	private java.lang.Long piece2;
	
	/**
	 *@generated
	 */
	@Column(name = "amt")
	private java.math.BigDecimal amt;
	/**
	 *@generated
	 */
	@Column(name = "packagee2")
	private java.lang.String packagee2;

	@Column(name = "grswgt")
	private java.math.BigDecimal grswgt;

	@Column(name = "netwgt")
	private java.math.BigDecimal netwgt;
	
	@Column(name = "cbm")
	private java.math.BigDecimal cbm;
	
	@Column(name = "cbm2")
	private java.math.BigDecimal cbm2;
	
	@Column(name = "marinepollutant")
	private java.lang.Boolean marinepollutant;
	
	
	@Column(name = "emergencycontact",length=50)
	private java.lang.String emergencycontact;
	
	@Column(name = "emergencytel",length=50)
	private java.lang.String emergencytel;
	
	@Column(name = "emergencyemail",length=100)
	private java.lang.String emergencyemail;
	
	@Column(name = "emergencyreference",length=50)
	private java.lang.String emergencyreference;
	
	@Column(name = "unnumber",length=50)
	private java.lang.String unnumber;
	
	@Column(name = "imoclass",length=50)
	private java.lang.String imoclass;
	
	@Column(name = "uom",length=50)
	private java.lang.String uom;
	
	@Column(name = "flashpoint",length=50)
	private java.lang.String flashpoint;
	
	@Column(name = "outerpackagecode",length=50)
	private java.lang.String outerpackagecode;
	
	@Column(name = "outerquantity",length=50)
	private java.lang.String outerquantity;
	
	@Column(name = "tnnerpackagecode",length=50)
	private java.lang.String tnnerpackagecode;
	
	@Column(name = "innerquantity",length=50)
	private java.lang.String innerquantity;
	
	@Column(name = "chemicalname",length=50)
	private java.lang.String chemicalname;
	
	@Column(name = "netweight")
	private java.math.BigDecimal netweight;
	
	@Column(name = "grossweight")
	private java.math.BigDecimal grossweight;
	
	@Column(name = "hscode", length = 30)
	private java.lang.String hscode;
	
	@Column(name = "currency", length = 3)
	private java.lang.String currency;
	
	@Column(name = "price")
	private java.math.BigDecimal price;
	
	@Column(name = "markno")
	private java.lang.String markno;
	
	@Column(name = "goodsname2")
	private java.lang.String goodsname2;
	
	
	
	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.String getFactorynamee() {
		return factorynamee;
	}

	public void setFactorynamee(java.lang.String factorynamee) {
		this.factorynamee = factorynamee;
	}

	public java.math.BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(java.math.BigDecimal amt) {
		this.amt = amt;
	}

	public java.math.BigDecimal getNetwgt() {
		return netwgt;
	}

	public void setNetwgt(java.math.BigDecimal netwgt) {
		this.netwgt = netwgt;
	}

	public java.lang.String getHscode() {
		return hscode;
	}

	public void setHscode(java.lang.String hscode) {
		this.hscode = hscode;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public java.lang.String getFactoryname() {
		return factoryname;
	}

	public void setFactoryname(java.lang.String factoryname) {
		this.factoryname = factoryname;
	}

	public java.lang.Long getPiece() {
		return piece;
	}

	public void setPiece(java.lang.Long piece) {
		this.piece = piece;
	}

	public java.lang.String getPackagee() {
		return packagee;
	}

	public void setPackagee(java.lang.String packagee) {
		this.packagee = packagee;
	}

	public java.lang.Long getPiece2() {
		return piece2;
	}

	public void setPiece2(java.lang.Long piece2) {
		this.piece2 = piece2;
	}

	public java.lang.String getPackagee2() {
		return packagee2;
	}

	public void setPackagee2(java.lang.String packagee2) {
		this.packagee2 = packagee2;
	}

	public java.math.BigDecimal getGrswgt() {
		return grswgt;
	}

	public void setGrswgt(java.math.BigDecimal grswgt) {
		this.grswgt = grswgt;
	}

	public java.math.BigDecimal getCbm() {
		return cbm;
	}

	public void setCbm(java.math.BigDecimal cbm) {
		this.cbm = cbm;
	}

	public java.math.BigDecimal getCbm2() {
		return cbm2;
	}

	public void setCbm2(java.math.BigDecimal cbm2) {
		this.cbm2 = cbm2;
	}

	public java.lang.Boolean getMarinepollutant() {
		return marinepollutant;
	}

	public void setMarinepollutant(java.lang.Boolean marinepollutant) {
		this.marinepollutant = marinepollutant;
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

	public java.math.BigDecimal getNetweight() {
		return netweight;
	}

	public void setNetweight(java.math.BigDecimal netweight) {
		this.netweight = netweight;
	}

	public java.math.BigDecimal getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(java.math.BigDecimal grossweight) {
		this.grossweight = grossweight;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.lang.String getMarkno() {
		return markno;
	}

	public void setMarkno(java.lang.String markno) {
		this.markno = markno;
	}

	public java.lang.String getGoodsname2() {
		return goodsname2;
	}

	public void setGoodsname2(java.lang.String goodsname2) {
		this.goodsname2 = goodsname2;
	}
	
}