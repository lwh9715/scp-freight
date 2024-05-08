package com.scp.model.ship;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_ship_book_vesvoy")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Voyage implements java.io.Serializable {

    @Column(name = "id")
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;
	
	
	@Column(name = "linkid")
	private Long linkid;

	@Column(name = "ves")
	private String ves;

	@Column(name = "voy")
	private String voy;

	@Column(name = "pol")
	private String pol;

	@Column(name = "pod")
	private String pod;

	@Column(name = "polname")
	private String polname;

	@Column(name = "podname")
	private String podname;


	@Column(name = "etd")
	private Date etd;

	@Column(name = "atd")
	private Date atd;

	@Column(name = "atdais")
	private Date atdais;

	@Column(name = "eta")
	private Date eta;

	@Column(name = "ata")
	private Date ata;

	@Column(name = "ataais")
	private Date ataais;

	@Column(name = "imo")
	private String imo;

	@Column(name = "mmsi")
	private String mmsi;

	@Column(name = "lloyd")
	private String lloyd;

	@Column(name = "transtype")
	private String transtype;




	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;


	@Column(name = "voyagenamec")
	private String voyagenamec;

	@Column(name = "voyagenumber")
	private Long voyagenumber;

	@Column(name = "transportmode")
	private String transportmode;

	@Column(name = "transporttype")
	private String transporttype;

	@Column(name = "carrierid")
	private String carrierid;


	public String getVoyagenamec() {
		return voyagenamec;
	}

	public void setVoyagenamec(String voyagenamec) {
		this.voyagenamec = voyagenamec;
	}

	public Long getVoyagenumber() {
		return voyagenumber;
	}

	public void setVoyagenumber(Long voyagenumber) {
		this.voyagenumber = voyagenumber;
	}

	public String getTransportmode() {
		return transportmode;
	}

	public void setTransportmode(String transportmode) {
		this.transportmode = transportmode;
	}

	public String getTransporttype() {
		return transporttype;
	}

	public void setTransporttype(String transporttype) {
		this.transporttype = transporttype;
	}

	public String getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getLinkid() {
		return linkid;
	}

	public void setLinkid(Long linkid) {
		this.linkid = linkid;
	}

	public String getVes() {
		return ves;
	}

	public void setVes(String ves) {
		this.ves = ves;
	}

	public String getVoy() {
		return voy;
	}

	public void setVoy(String voy) {
		this.voy = voy;
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

	public String getPolname() {
		return polname;
	}

	public void setPolname(String polname) {
		this.polname = polname;
	}

	public String getPodname() {
		return podname;
	}

	public void setPodname(String podname) {
		this.podname = podname;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getAtd() {
		return atd;
	}

	public void setAtd(Date atd) {
		this.atd = atd;
	}

	public Date getAtdais() {
		return atdais;
	}

	public void setAtdais(Date atdais) {
		this.atdais = atdais;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getAta() {
		return ata;
	}

	public void setAta(Date ata) {
		this.ata = ata;
	}

	public Date getAtaais() {
		return ataais;
	}

	public void setAtaais(Date ataais) {
		this.ataais = ataais;
	}

	public String getImo() {
		return imo;
	}

	public void setImo(String imo) {
		this.imo = imo;
	}

	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}

	public String getLloyd() {
		return lloyd;
	}

	public void setLloyd(String lloyd) {
		this.lloyd = lloyd;
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}
