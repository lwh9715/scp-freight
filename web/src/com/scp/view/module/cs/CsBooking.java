package com.scp.view.module.cs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "cs_booking")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class CsBooking implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "nos")
	private java.lang.String nos;

	@Column(name = "date")
	private java.util.Date date;

	@Column(name = "isreceived")
	private java.lang.Boolean isreceived;

	@Column(name = "receiveddate")
	private java.util.Date receiveddate;

	@Column(name = "receivedder", length = 30)
	private java.lang.String receivedder;

	@Column(name = "customerid")
	private java.lang.Long customerid;

	@Column(name = "pricefclid")
	private java.lang.Long pricefclid;

	@Column(name = "saleid")
	private java.lang.Long saleid;

	@Column(name = "jobid")
	private java.lang.Long jobid;

	@Column(name = "cnorname")
	private java.lang.String cnorname;

	@Column(name = "cneename")
	private java.lang.String cneename;

	@Column(name = "contractno")
	private java.lang.String contractno;

	@Column(name = "notifyname")
	private java.lang.String notifyname;

	@Column(name = "vessel", length = 30)
	private java.lang.String vessel;

	@Column(name = "voyage", length = 20)
	private java.lang.String voyage;

	@Column(name = "shipping", length = 30)
	private java.lang.String shipping;

	@Column(name = "polcode", length = 20)
	private java.lang.String polcode;

	@Column(name = "polid")
	private java.lang.Long polid;
	
	@Column(name = "pol", length = 50)
	private java.lang.String pol;

	@Column(name = "pddcode", length = 20)
	private java.lang.String pddcode;

	@Column(name = "pddid")
	private java.lang.Long pddid;
	
	@Column(name = "pdd", length = 50)
	private java.lang.String pdd;

	@Column(name = "podcode", length = 20)
	private java.lang.String podcode;

	@Column(name = "podid")
	private java.lang.Long podid;
	
	@Column(name = "pod", length = 50)
	private java.lang.String pod;

	@Column(name = "pretrans")
	private java.lang.String pretrans;

	@Column(name = "poa")
	private java.lang.String poa;

	@Column(name = "cls")
	private java.util.Date cls;

	@Column(name = "etd")
	private java.util.Date etd;

	@Column(name = "bltype", length = 1)
	private java.lang.String bltype;

	@Column(name = "carryitem", length = 15)
	private java.lang.String carryitem;

	@Column(name = "freightitem", length = 2)
	private java.lang.String freightitem;

	@Column(name = "piece")
	private java.lang.Integer piece;

	@Column(name = "packer")
	private java.lang.String packer;

	@Column(name = "grswgt")
	private java.lang.String grswgt;

	@Column(name = "cbm")
	private java.lang.String cbm;

	@Column(name = "marksno")
	private java.lang.String marksno;

	@Column(name = "goodsdesc")
	private java.lang.String goodsdesc;

	@Column(name = "remarks")
	private java.lang.String remarks;

	@Column(name = "ppcc", length = 2)
	private java.lang.String ppcc;

	@Column(name = "istrailer")
	private java.lang.Boolean istrailer;

	@Column(name = "isdeclare")
	private java.lang.Boolean isdeclare;

	@Column(name = "putertype", length = 1)
	private java.lang.String putertype;

	@Column(name = "blcontacts")
	private java.lang.String blcontacts;

	@Column(name = "phone")
	private java.lang.String phone;

	@Column(name = "email")
	private java.lang.String email;

	@Column(name = "atd")
	private java.util.Date atd;

	@Column(name = "refno")
	private java.lang.String refno;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

	@Column(name = "pkid_remote", length = 20)
	private java.lang.String pkid_remote;
	
	@Column(name = "customername", length = 100)
	private java.lang.String customername;
	
	@Column(name = "bustype", length = 3)
	private java.lang.String bustype;
	
	@Column(name = "portlanding", length = 100)
	private java.lang.String portlanding;
	
	@Column(name = "placedelivery", length = 100)
	private java.lang.String placedelivery;
	
	@Column(name = "porttogo", length = 100)
	private java.lang.String porttogo;
	
	@Column(name = "shippingcompanies", length = 100)
	private java.lang.String shippingcompanies;
	
	@Column(name = "flightdate")
	private java.util.Date flightdate;
	
	@Column(name = "goodsreadydate")
	private java.util.Date goodsreadydate;
	
	@Column(name = "isinvoice")
	private java.lang.Boolean isinvoice;
	
	@Column(name = "isbusinvoice")
	private java.lang.Boolean isbusinvoice;
	
	@Column(name = "payplace")
	private java.lang.String payplace;
	
	@Column(name = "linecode", length = 100)
	private java.lang.String linecode;
	
	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete = false;
	
	@Column(name = "bargecls")
	private java.util.Date bargecls;
	
	@Column(name = "bargeetd")
	private java.util.Date bargeetd;
	
	@Column(name = "routecode", length = 100)
	private java.lang.String routecode;
	
	@Column(name = "isdeclarevmg")
	private java.lang.Boolean isdeclarevmg;
	
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

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.lang.Boolean getIsreceived() {
		return isreceived;
	}

	public void setIsreceived(java.lang.Boolean isreceived) {
		this.isreceived = isreceived;
	}

	public java.util.Date getReceiveddate() {
		return receiveddate;
	}

	public void setReceiveddate(java.util.Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public java.lang.String getReceivedder() {
		return receivedder;
	}

	public void setReceivedder(java.lang.String receivedder) {
		this.receivedder = receivedder;
	}

	public java.lang.Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(java.lang.Long customerid) {
		this.customerid = customerid;
	}

	public java.lang.Long getSaleid() {
		return saleid;
	}

	public void setSaleid(java.lang.Long saleid) {
		this.saleid = saleid;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.String getCnorname() {
		return cnorname;
	}

	public void setCnorname(java.lang.String cnorname) {
		this.cnorname = cnorname;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public java.lang.String getVessel() {
		return vessel;
	}

	public void setVessel(java.lang.String vessel) {
		this.vessel = vessel;
	}

	public java.lang.String getVoyage() {
		return voyage;
	}

	public void setVoyage(java.lang.String voyage) {
		this.voyage = voyage;
	}

	public java.lang.String getShipping() {
		return shipping;
	}

	public void setShipping(java.lang.String shipping) {
		this.shipping = shipping;
	}

	public java.lang.String getPolcode() {
		return polcode;
	}

	public void setPolcode(java.lang.String polcode) {
		this.polcode = polcode;
	}

	public java.lang.Long getPolid() {
		return polid;
	}

	public void setPolid(java.lang.Long polid) {
		this.polid = polid;
	}

	public java.lang.String getPddcode() {
		return pddcode;
	}

	public void setPddcode(java.lang.String pddcode) {
		this.pddcode = pddcode;
	}

	public java.lang.Long getPddid() {
		return pddid;
	}

	public void setPddid(java.lang.Long pddid) {
		this.pddid = pddid;
	}

	public java.lang.String getPodcode() {
		return podcode;
	}

	public void setPodcode(java.lang.String podcode) {
		this.podcode = podcode;
	}

	public java.lang.Long getPodid() {
		return podid;
	}

	public void setPodid(java.lang.Long podid) {
		this.podid = podid;
	}

	public java.lang.String getPretrans() {
		return pretrans;
	}

	public void setPretrans(java.lang.String pretrans) {
		this.pretrans = pretrans;
	}

	public java.lang.String getPoa() {
		return poa;
	}

	public void setPoa(java.lang.String poa) {
		this.poa = poa;
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

	public java.lang.String getBltype() {
		return bltype;
	}

	public void setBltype(java.lang.String bltype) {
		this.bltype = bltype;
	}

	public java.lang.String getCarryitem() {
		return carryitem;
	}

	public void setCarryitem(java.lang.String carryitem) {
		this.carryitem = carryitem;
	}

	public java.lang.String getFreightitem() {
		return freightitem;
	}

	public void setFreightitem(java.lang.String freightitem) {
		this.freightitem = freightitem;
	}

	public java.lang.Integer getPiece() {
		return piece;
	}

	public void setPiece(java.lang.Integer piece) {
		this.piece = piece;
	}

	public java.lang.String getPacker() {
		return packer;
	}

	public void setPacker(java.lang.String packer) {
		this.packer = packer;
	}

	public java.lang.String getGrswgt() {
		return grswgt;
	}

	public void setGrswgt(java.lang.String grswgt) {
		this.grswgt = grswgt;
	}

	public java.lang.String getCbm() {
		return cbm;
	}

	public void setCbm(java.lang.String cbm) {
		this.cbm = cbm;
	}

	public java.lang.String getMarksno() {
		return marksno;
	}

	public void setMarksno(java.lang.String marksno) {
		this.marksno = marksno;
	}

	public java.lang.String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(java.lang.String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getPpcc() {
		return ppcc;
	}

	public void setPpcc(java.lang.String ppcc) {
		this.ppcc = ppcc;
	}

	public java.lang.Boolean getIstrailer() {
		return istrailer;
	}

	public void setIstrailer(java.lang.Boolean istrailer) {
		this.istrailer = istrailer;
	}

	public java.lang.Boolean getIsdeclare() {
		return isdeclare;
	}

	public void setIsdeclare(java.lang.Boolean isdeclare) {
		this.isdeclare = isdeclare;
	}

	public java.lang.String getPutertype() {
		return putertype;
	}

	/**
	 * 放货方式O 正本T电放
	 * 
	 * @param putertype
	 */
	public void setPutertype(java.lang.String putertype) {
		this.putertype = putertype;
	}

	public java.lang.String getBlcontacts() {
		return blcontacts;
	}

	public void setBlcontacts(java.lang.String blcontacts) {
		this.blcontacts = blcontacts;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.util.Date getAtd() {
		return atd;
	}

	public void setAtd(java.util.Date atd) {
		this.atd = atd;
	}

	public java.lang.String getRefno() {
		return refno;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
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

	public java.lang.String getContractno() {
		return contractno;
	}

	public void setContractno(java.lang.String contractno) {
		this.contractno = contractno;
	}

	public java.lang.Long getPricefclid() {
		return pricefclid;
	}

	public void setPricefclid(java.lang.Long pricefclid) {
		this.pricefclid = pricefclid;
	}

	public java.lang.String getPkid_remote() {
		return pkid_remote;
	}

	public void setPkid_remote(java.lang.String pkidRemote) {
		pkid_remote = pkidRemote;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getCustomername() {
		return customername;
	}

	public void setCustomername(java.lang.String customername) {
		this.customername = customername;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getPdd() {
		return pdd;
	}

	public void setPdd(java.lang.String pdd) {
		this.pdd = pdd;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.String getBustype() {
		return bustype;
	}

	public void setBustype(java.lang.String bustype) {
		this.bustype = bustype;
	}

	public java.lang.String getPortlanding() {
		return portlanding;
	}

	public void setPortlanding(java.lang.String portlanding) {
		this.portlanding = portlanding;
	}

	public java.lang.String getPlacedelivery() {
		return placedelivery;
	}

	public void setPlacedelivery(java.lang.String placedelivery) {
		this.placedelivery = placedelivery;
	}

	public java.lang.String getPorttogo() {
		return porttogo;
	}

	public void setPorttogo(java.lang.String porttogo) {
		this.porttogo = porttogo;
	}

	public java.lang.String getShippingcompanies() {
		return shippingcompanies;
	}

	public void setShippingcompanies(java.lang.String shippingcompanies) {
		this.shippingcompanies = shippingcompanies;
	}

	public java.util.Date getFlightdate() {
		return flightdate;
	}

	public void setFlightdate(java.util.Date flightdate) {
		this.flightdate = flightdate;
	}

	public java.util.Date getGoodsreadydate() {
		return goodsreadydate;
	}

	public void setGoodsreadydate(java.util.Date goodsreadydate) {
		this.goodsreadydate = goodsreadydate;
	}

	public java.lang.Boolean getIsinvoice() {
		return isinvoice;
	}

	public void setIsinvoice(java.lang.Boolean isinvoice) {
		this.isinvoice = isinvoice;
	}

	public java.lang.Boolean getIsbusinvoice() {
		return isbusinvoice;
	}

	public void setIsbusinvoice(java.lang.Boolean isbusinvoice) {
		this.isbusinvoice = isbusinvoice;
	}

	public java.lang.String getPayplace() {
		return payplace;
	}

	public void setPayplace(java.lang.String payplace) {
		this.payplace = payplace;
	}

	public java.lang.String getLinecode() {
		return linecode;
	}

	public void setLinecode(java.lang.String linecode) {
		this.linecode = linecode;
	}

	public java.util.Date getBargecls() {
		return bargecls;
	}

	public void setBargecls(java.util.Date bargecls) {
		this.bargecls = bargecls;
	}

	public java.util.Date getBargeetd() {
		return bargeetd;
	}

	public void setBargeetd(java.util.Date bargeetd) {
		this.bargeetd = bargeetd;
	}

	public java.lang.String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(java.lang.String routecode) {
		this.routecode = routecode;
	}

	public java.lang.Boolean getIsdeclarevmg() {
		return isdeclarevmg;
	}

	public void setIsdeclarevmg(java.lang.Boolean isdeclarevmg) {
		this.isdeclarevmg = isdeclarevmg;
	}

}