package com.scp.model.order;

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
@Table(name = "bus_order")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusOrder implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "cnorname")
	private java.lang.String cnorname;
	/**
	 *@generated
	 */
	@Column(name = "cnorid")
	private java.lang.Long cnorid;
	/**
	 *@generated
	 */
	@Column(name = "cnortitle")
	private java.lang.String cnortitle;
	/**
	 *@generated
	 */
	@Column(name = "cneename")
	private java.lang.String cneename;
	/**
	 *@generated
	 */
	@Column(name = "cneeid")
	private java.lang.Long cneeid;
	/**
	 *@generated
	 */
	@Column(name = "cneetitle")
	private java.lang.String cneetitle;
	/**
	 *@generated
	 */
	@Column(name = "notifyname")
	private java.lang.String notifyname;
	/**
	 *@generated
	 */
	@Column(name = "notifyid")
	private java.lang.Long notifyid;
	/**
	 *@generated
	 */
	@Column(name = "notifytitle")
	private java.lang.String notifytitle;
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
	@Column(name = "orderno", length = 30)
	private java.lang.String orderno;

	/**
	 *@generated
	 */
	@Column(name = "orderdate", length = 13)
	private java.util.Date orderdate;

	/**
	 *@generated
	 */
	@Column(name = "deliverdate", length = 13)
	private java.util.Date deliverdate;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customercode", length = 100)
	private java.lang.String customercode;

	/**
	 *@generated
	 */
	@Column(name = "customername", length = 100)
	private java.lang.String customername;

	/**
	 *@generated
	 */
	@Column(name = "customertel", length = 100)
	private java.lang.String customertel;

	/**
	 *@generated
	 */
	@Column(name = "customercontacter", length = 100)
	private java.lang.String customercontacter;

	/**
	 *@generated
	 */
	@Column(name = "salesid")
	private java.lang.Long salesid;

	/**
	 *@generated
	 */
	@Column(name = "shiping", length = 50)
	private java.lang.String shiping;

	/**
	 *@generated
	 */
	@Column(name = "shipid")
	private java.lang.Long shipid;

	/**
	 *@generated
	 */
	@Column(name = "vessel", length = 30)
	private java.lang.String vessel;

	/**
	 *@generated
	 */
	@Column(name = "voyage", length = 20)
	private java.lang.String voyage;

	/**
	 *@generated
	 */
	@Column(name = "polid")
	private java.lang.Long polid;
	
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "corpidop")
	private java.lang.Long corpidop;
	

	/**
	 *@generated
	 */
	@Column(name = "pol", length = 50)
	private java.lang.String pol;

	/**
	 *@generated
	 */
	@Column(name = "podid")
	private java.lang.Long podid;

	/**
	 *@generated
	 */
	@Column(name = "pod", length = 50)
	private java.lang.String pod;

	/**
	 *@generated
	 */
	@Column(name = "pddid")
	private java.lang.Long pddid;

	/**
	 *@generated
	 */
	@Column(name = "pdd", length = 50)
	private java.lang.String pdd;
	
	/**
	 *@generated
	 */
	@Column(name = "destination", length = 100)
	private java.lang.String destination;

	/**
	 *@generated
	 */
	@Column(name = "jobid",updatable=false)
	private java.lang.Long jobid;
	
	@Column(name = "jobno", length = 30,updatable=false)
	private java.lang.String jobno;

	/**
	 *@generated
	 */
	@Column(name = "refno", length = 30)
	private java.lang.String refno;

	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.Short piece;

	/**
	 *@generated
	 */
	@Column(name = "grswgt")
	private java.math.BigDecimal grswgt;

	/**
	 *@generated
	 */
	@Column(name = "cbm")
	private java.math.BigDecimal cbm;

	/**
	 *@generated
	 */
	@Column(name = "packid")
	private java.lang.Long packid;

	/**
	 *@generated
	 */
	@Column(name = "carryitem", length = 15)
	private java.lang.String carryitem;

	/**
	 *@generated
	 */
	@Column(name = "freightitem", length = 2)
	private java.lang.String freightitem;

	/**
	 *@generated
	 */
	@Column(name = "goodsdesc")
	private java.lang.String goodsdesc;

	/**
	 *@generated
	 */
	@Column(name = "cls", length = 13)
	private java.util.Date cls;

	/**
	 *@generated
	 */
	@Column(name = "etd", length = 13)
	private java.util.Date etd;

	/**
	 *@generated
	 */
	@Column(name = "eta", length = 13)
	private java.util.Date eta;

	/**
	 *@generated
	 */
	@Column(name = "bustype", nullable = false, length = 3)
	private java.lang.String bustype;

	/**
	 *@generated
	 */
	@Column(name = "piece20")
	private java.lang.Short piece20;

	/**
	 *@generated
	 */
	@Column(name = "piece40gp")
	private java.lang.Short piece40gp;

	/**
	 *@generated
	 */
	@Column(name = "piece40hq")
	private java.lang.Short piece40hq;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;
	
	@Column(name = "cscode")
	private java.lang.String cscode;
	
	@Column(name = "sono")
	private java.lang.String sono;
	
	@Column(name = "pieceother")
	private java.lang.Short pieceother;
	
	@Column(name = "cntypeothercode")
	private java.lang.String cntypeothercode;
	
	@Column(name = "priceuserid")
	private java.lang.Long priceuserid;
	
	@Column(name = "agentid")
	private java.lang.Long agentid;
	
	/**
	 *@generated
	 */
	@Column(name = "impexp ", length = 1)
	private java.lang.String impexp ;
	
	@Column(name = "contractno", length = 30)
	private java.lang.String contractno;
	
	@Column(name = "remark_out")
	private java.lang.String remark_out;
	
	@Column(name = "remarks_op")
	private java.lang.String remarks_op;
	
	@Column(name = "isinvoice")
	private java.lang.Boolean isinvoice;
	
	@Column(name = "isclc")
	private java.lang.Boolean isclc;
	
	@Column(name = "trucktype")
	private java.lang.String trucktype;
	
	@Column(name = "loadaddress")
	private java.lang.String loadaddress;
	
	@Column(name = "loadtime", length = 29)
	private java.util.Date loadtime;
	
	@Column(name = "loadcontact", length = 50)
	private java.lang.String loadcontact;
	
	@Column(name = "contacttel", length = 30)
	private java.lang.String contacttel;
	
	@Column(name = "istruck")
	private java.lang.Boolean istruck;
	
	@Column(name = "iscus")
	private java.lang.Boolean iscus;
	
	@Column(name = "custype", length = 1)
	private java.lang.String custype;
	
	/**
	 *@generated
	 */
	@Column(name = "agentdesid")
	private java.lang.Long agentdesid;
	
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	/**
	 *@generated
	 */
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;

	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 35)
	private java.util.Date checktime;
	
	@Column(name = "routecode", length = 20)
	private java.lang.String routecode;
	
	@Column(name = "linecode", length = 100)
	private java.lang.String linecode;
	
	
	@Column(name = "polcode", length = 10)
	private java.lang.String polcode;
	
	@Column(name = "pddcode", length = 10)
	private java.lang.String pddcode;
	
	@Column(name = "wmsinid")
	private java.lang.Long wmsinid;
	
	@Column(name = "wmsinnos", length = 30,updatable=false)
	private java.lang.String wmsinnos;
	
	@Column(name = "goodsready")
	private java.util.Date goodsready;
	
	@Column(name = "agentcontact")
	private java.lang.String agentcontact;
	
	@Column(name = "customeremail")
	private java.lang.String customeremail;
	
	@Column(name = "priceuserconfirm")
	private java.lang.Boolean priceuserconfirm;
	
	public java.lang.String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(java.lang.String routecode) {
		this.routecode = routecode;
	}

	public java.lang.String getContractno() {
		return contractno;
	}

	public void setContractno(java.lang.String contractno) {
		this.contractno = contractno;
	}

	public java.lang.String getRemark_out() {
		return remark_out;
	}

	public void setRemark_out(java.lang.String remarkOut) {
		remark_out = remarkOut;
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

	/**
	 *@generated
	 */
	public java.lang.String getOrderno() {
		return this.orderno;
	}

	/**
	 *@generated
	 */
	public void setOrderno(java.lang.String value) {
		this.orderno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getOrderdate() {
		return this.orderdate;
	}

	/**
	 *@generated
	 */
	public void setOrderdate(java.util.Date value) {
		this.orderdate = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getDeliverdate() {
		return this.deliverdate;
	}

	/**
	 *@generated
	 */
	public void setDeliverdate(java.util.Date value) {
		this.deliverdate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCustomerid() {
		return this.customerid;
	}

	/**
	 *@generated
	 */
	public void setCustomerid(java.lang.Long value) {
		this.customerid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomercode() {
		return this.customercode;
	}

	/**
	 *@generated
	 */
	public void setCustomercode(java.lang.String value) {
		this.customercode = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomername() {
		return this.customername;
	}

	/**
	 *@generated
	 */
	public void setCustomername(java.lang.String value) {
		this.customername = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomertel() {
		return this.customertel;
	}

	/**
	 *@generated
	 */
	public void setCustomertel(java.lang.String value) {
		this.customertel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomercontacter() {
		return this.customercontacter;
	}

	/**
	 *@generated
	 */
	public void setCustomercontacter(java.lang.String value) {
		this.customercontacter = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getSalesid() {
		return this.salesid;
	}

	/**
	 *@generated
	 */
	public void setSalesid(java.lang.Long value) {
		this.salesid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getShiping() {
		return this.shiping;
	}

	/**
	 *@generated
	 */
	public void setShiping(java.lang.String value) {
		this.shiping = value;
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
	public java.lang.String getVessel() {
		return this.vessel;
	}

	/**
	 *@generated
	 */
	public void setVessel(java.lang.String value) {
		this.vessel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getVoyage() {
		return this.voyage;
	}

	/**
	 *@generated
	 */
	public void setVoyage(java.lang.String value) {
		this.voyage = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getPolid() {
		return this.polid;
	}

	/**
	 *@generated
	 */
	public void setPolid(java.lang.Long value) {
		this.polid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPol() {
		return this.pol;
	}

	/**
	 *@generated
	 */
	public void setPol(java.lang.String value) {
		this.pol = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getPodid() {
		return this.podid;
	}

	/**
	 *@generated
	 */
	public void setPodid(java.lang.Long value) {
		this.podid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPod() {
		return this.pod;
	}

	/**
	 *@generated
	 */
	public void setPod(java.lang.String value) {
		this.pod = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getPddid() {
		return this.pddid;
	}

	/**
	 *@generated
	 */
	public void setPddid(java.lang.Long value) {
		this.pddid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPdd() {
		return this.pdd;
	}

	/**
	 *@generated
	 */
	public void setPdd(java.lang.String value) {
		this.pdd = value;
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
	public java.lang.String getRefno() {
		return this.refno;
	}

	/**
	 *@generated
	 */
	public void setRefno(java.lang.String value) {
		this.refno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece() {
		return this.piece;
	}

	/**
	 *@generated
	 */
	public void setPiece(java.lang.Short value) {
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
	public java.lang.Long getPackid() {
		return this.packid;
	}

	/**
	 *@generated
	 */
	public void setPackid(java.lang.Long value) {
		this.packid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCarryitem() {
		return this.carryitem;
	}

	/**
	 *@generated
	 */
	public void setCarryitem(java.lang.String value) {
		this.carryitem = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFreightitem() {
		return this.freightitem;
	}

	/**
	 *@generated
	 */
	public void setFreightitem(java.lang.String value) {
		this.freightitem = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsdesc() {
		return this.goodsdesc;
	}

	/**
	 *@generated
	 */
	public void setGoodsdesc(java.lang.String value) {
		this.goodsdesc = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getCls() {
		return this.cls;
	}

	/**
	 *@generated
	 */
	public void setCls(java.util.Date value) {
		this.cls = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getEtd() {
		return this.etd;
	}

	/**
	 *@generated
	 */
	public void setEtd(java.util.Date value) {
		this.etd = value;
	}
	
	/**
	 *@generated
	 */
	@Column(name = "sales", length = 100)
	private java.lang.String sales;
	
	public java.lang.String getSales() {
		return this.sales;
	}
	
	public void setSales(java.lang.String value) {
		this.sales = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getEta() {
		return this.eta;
	}

	/**
	 *@generated
	 */
	public void setEta(java.util.Date value) {
		this.eta = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getBustype() {
		return this.bustype;
	}

	/**
	 *@generated
	 */
	public void setBustype(java.lang.String value) {
		this.bustype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece20() {
		return this.piece20;
	}

	/**
	 *@generated
	 */
	public void setPiece20(java.lang.Short value) {
		this.piece20 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece40gp() {
		return this.piece40gp;
	}

	/**
	 *@generated
	 */
	public void setPiece40gp(java.lang.Short value) {
		this.piece40gp = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece40hq() {
		return this.piece40hq;
	}

	/**
	 *@generated
	 */
	public void setPiece40hq(java.lang.Short value) {
		this.piece40hq = value;
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
	
	@Column(name = "tradeway", length = 1)
	private java.lang.String tradeway;

	public String getTradeway() {
		return tradeway;
	}

	public void setTradeway(String tradeway) {
		this.tradeway = tradeway;
	}

	
	
	
	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.String getJobno() {
		return jobno;
	}

	public void setJobno(java.lang.String jobno) {
		this.jobno = jobno;
	}

	public java.lang.Long getCorpidop() {
		return corpidop;
	}

	public void setCorpidop(java.lang.Long corpidop) {
		this.corpidop = corpidop;
	}
	
	public java.lang.String getCscode() {
		return cscode;
	}

	public void setCscode(java.lang.String cscode) {
		this.cscode = cscode;
	}

	public java.lang.String getSono() {
		return sono;
	}

	public void setSono(java.lang.String sono) {
		this.sono = sono;
	}

	public java.lang.String getCnorname() {
		return cnorname;
	}

	public void setCnorname(java.lang.String cnorname) {
		this.cnorname = cnorname;
	}

	public java.lang.Long getCnorid() {
		return cnorid;
	}

	public void setCnorid(java.lang.Long cnorid) {
		this.cnorid = cnorid;
	}

	public java.lang.String getCnortitle() {
		return cnortitle;
	}

	public void setCnortitle(java.lang.String cnortitle) {
		this.cnortitle = cnortitle;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public java.lang.Long getCneeid() {
		return cneeid;
	}

	public void setCneeid(java.lang.Long cneeid) {
		this.cneeid = cneeid;
	}

	public java.lang.String getCneetitle() {
		return cneetitle;
	}

	public void setCneetitle(java.lang.String cneetitle) {
		this.cneetitle = cneetitle;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public java.lang.Long getNotifyid() {
		return notifyid;
	}

	public void setNotifyid(java.lang.Long notifyid) {
		this.notifyid = notifyid;
	}

	public java.lang.String getNotifytitle() {
		return notifytitle;
	}

	public void setNotifytitle(java.lang.String notifytitle) {
		this.notifytitle = notifytitle;
	}

	public java.lang.Short getPieceother() {
		return pieceother;
	}

	public void setPieceother(java.lang.Short pieceother) {
		this.pieceother = pieceother;
	}

	public java.lang.String getCntypeothercode() {
		return cntypeothercode;
	}

	public void setCntypeothercode(java.lang.String cntypeothercode) {
		this.cntypeothercode = cntypeothercode;
	}

	public java.lang.String getImpexp() {
		return impexp;
	}

	public void setImpexp(java.lang.String impexp) {
		this.impexp = impexp;
	}

	public java.lang.String getRemarks_op() {
		return remarks_op;
	}

	public void setRemarks_op(java.lang.String remarksOp) {
		remarks_op = remarksOp;
	}

	public java.lang.Boolean getIsinvoice() {
		return isinvoice;
	}

	public void setIsinvoice(java.lang.Boolean isinvoice) {
		this.isinvoice = isinvoice;
	}

	public java.lang.Boolean getIsclc() {
		return isclc;
	}

	public void setIsclc(java.lang.Boolean isclc) {
		this.isclc = isclc;
	}

	public java.lang.String getTrucktype() {
		return trucktype;
	}

	public void setTrucktype(java.lang.String trucktype) {
		this.trucktype = trucktype;
	}

	public java.lang.String getLoadaddress() {
		return loadaddress;
	}

	public void setLoadaddress(java.lang.String loadaddress) {
		this.loadaddress = loadaddress;
	}

	public java.util.Date getLoadtime() {
		return loadtime;
	}

	public void setLoadtime(java.util.Date loadtime) {
		this.loadtime = loadtime;
	}

	public java.lang.String getLoadcontact() {
		return loadcontact;
	}

	public void setLoadcontact(java.lang.String loadcontact) {
		this.loadcontact = loadcontact;
	}

	public java.lang.String getContacttel() {
		return contacttel;
	}

	public void setContacttel(java.lang.String contacttel) {
		this.contacttel = contacttel;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.Boolean getIstruck() {
		return istruck;
	}

	public void setIstruck(java.lang.Boolean istruck) {
		this.istruck = istruck;
	}

	public java.lang.Boolean getIscus() {
		return iscus;
	}

	public void setIscus(java.lang.Boolean iscus) {
		this.iscus = iscus;
	}

	public java.lang.String getCustype() {
		return custype;
	}

	public void setCustype(java.lang.String custype) {
		this.custype = custype;
	}

	public java.lang.Long getAgentdesid() {
		return agentdesid;
	}

	public void setAgentdesid(java.lang.Long agentdesid) {
		this.agentdesid = agentdesid;
	}

	public java.lang.Long getPriceuserid() {
		return priceuserid;
	}

	public void setPriceuserid(java.lang.Long priceuserid) {
		this.priceuserid = priceuserid;
	}
	
	public java.lang.Long getAgentid() {
		return agentid;
	}
	public void setAgentid(java.lang.Long agentid) {
		this.agentid = agentid;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.String getCheckter() {
		return checkter;
	}

	public void setCheckter(java.lang.String checkter) {
		this.checkter = checkter;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public java.lang.String getPolcode() {
		return polcode;
	}

	public void setPolcode(java.lang.String polcode) {
		this.polcode = polcode;
	}

	public java.lang.String getPddcode() {
		return pddcode;
	}

	public void setPddcode(java.lang.String pddcode) {
		this.pddcode = pddcode;
	}

	public java.lang.Long getWmsinid() {
		return wmsinid;
	}

	public void setWmsinid(java.lang.Long wmsinid) {
		this.wmsinid = wmsinid;
	}

	public java.lang.String getWmsinnos() {
		return wmsinnos;
	}

	public void setWmsinnos(java.lang.String wmsinnos) {
		this.wmsinnos = wmsinnos;
	}

	public java.util.Date getGoodsready() {
		return goodsready;
	}

	public void setGoodsready(java.util.Date goodsready) {
		this.goodsready = goodsready;
	}

	public java.lang.String getAgentcontact() {
		return agentcontact;
	}

	public void setAgentcontact(java.lang.String agentcontact) {
		this.agentcontact = agentcontact;
	}

	public java.lang.String getCustomeremail() {
		return customeremail;
	}

	public void setCustomeremail(java.lang.String customeremail) {
		this.customeremail = customeremail;
	}

	public java.lang.String getLinecode() {
		return linecode;
	}

	public void setLinecode(java.lang.String linecode) {
		this.linecode = linecode;
	}

	public java.lang.Boolean getPriceuserconfirm() {
		return priceuserconfirm;
	}

	public void setPriceuserconfirm(java.lang.Boolean priceuserconfirm) {
		this.priceuserconfirm = priceuserconfirm;
	}
	
}