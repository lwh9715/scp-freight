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
@Table(name = "edi_trans")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class EdiTrans implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "jobid")
	private java.lang.Long jobid;
	
	@Column(name = "cneecode1")
	private java.lang.String cneecode1;
	
	@Column(name = "cneecode2")
	private java.lang.String cneecode2;
	
	@Column(name = "cneename")
	private java.lang.String cneename;
	
	@Column(name = "cneeaddr")
	private java.lang.String cneeaddr;
	
	@Column(name = "cneecontact")
	private java.lang.String cneecontact;
	
	@Column(name = "cneetel")
	private java.lang.String cneetel;
	
	@Column(name = "cneeemail")
	private java.lang.String cneeemail;
	
	@Column(name = "cneefax")
	private java.lang.String cneefax;
	
	@Column(name = "cnorcode1")
	private java.lang.String cnorcode1;
	
	@Column(name = "cnorcode2")
	private java.lang.String cnorcode2;
	
	@Column(name = "cnorname")
	private java.lang.String cnorname;
	
	@Column(name = "cnoraddr")
	private java.lang.String cnoraddr;
	
	@Column(name = "cnorcontact")
	private java.lang.String cnorcontact;
	
	@Column(name = "cnortel")
	private java.lang.String cnortel;
	
	@Column(name = "cnoremail")
	private java.lang.String cnoremail;
	
	@Column(name = "cnorfax")
	private java.lang.String cnorfax;
	
	@Column(name = "notifycode1")
	private java.lang.String notifycode1;
	
	@Column(name = "notifycode2")
	private java.lang.String notifycode2;
	
	@Column(name = "notifyname")
	private java.lang.String notifyname;
	
	@Column(name = "notifyaddr")
	private java.lang.String notifyaddr;
	
	@Column(name = "notifycontact")
	private java.lang.String notifycontact;
	
	@Column(name = "notifytel")
	private java.lang.String notifytel;
	
	@Column(name = "notifyemail")
	private java.lang.String notifyemail;
	
	@Column(name = "notifyfax")
	private java.lang.String notifyfax;
	
	@Column(name = "mblcneecode1")
	private java.lang.String mblcneecode1;
	
	@Column(name = "mblcneecode2")
	private java.lang.String mblcneecode2;
	
	@Column(name = "mblcneename")
	private java.lang.String mblcneename;
	
	@Column(name = "mblcneeaddr")
	private java.lang.String mblcneeaddr;
	
	@Column(name = "mblcneecontact")
	private java.lang.String mblcneecontact;
	
	@Column(name = "mblcneetel")
	private java.lang.String mblcneetel;
	
	@Column(name = "mblcneeemail")
	private java.lang.String mblcneeemail;
	
	@Column(name = "mblcneefax")
	private java.lang.String mblcneefax;
	
	@Column(name = "mblcnorcode1")
	private java.lang.String mblcnorcode1;
	
	@Column(name = "mblcnorcode2")
	private java.lang.String mblcnorcode2;
	
	@Column(name = "mblcnorname")
	private java.lang.String mblcnorname;
	
	@Column(name = "mblcnoraddr")
	private java.lang.String mblcnoraddr;
	
	@Column(name = "mblcnorcontact")
	private java.lang.String mblcnorcontact;
	
	@Column(name = "mblcnortel")
	private java.lang.String mblcnortel;
	
	@Column(name = "mblcnoremail")
	private java.lang.String mblcnoremail;
	
	@Column(name = "mblcnorfax")
	private java.lang.String mblcnorfax;
	
	@Column(name = "mblnotifycode1")
	private java.lang.String mblnotifycode1;
	
	@Column(name = "mblnotifycode2")
	private java.lang.String mblnotifycode2;
	
	@Column(name = "mblnotifyname")
	private java.lang.String mblnotifyname;
	
	@Column(name = "mblnotifyaddr")
	private java.lang.String mblnotifyaddr;
	
	@Column(name = "mblnotifycontact")
	private java.lang.String mblnotifycontact;
	
	@Column(name = "mblnotifytel")
	private java.lang.String mblnotifytel;
	
	@Column(name = "mblnotifyemail")
	private java.lang.String mblnotifyemail;
	
	@Column(name = "mblnotifyfax")
	private java.lang.String mblnotifyfax;
	
	
	@Column(name = "agencode1")
	private java.lang.String agencode1;
	
	@Column(name = "agencode2")
	private java.lang.String agencode2;
	
	@Column(name = "agenname")
	private java.lang.String agenname;
	
	@Column(name = "agenaddr")
	private java.lang.String agenaddr;
	
	@Column(name = "agencontact")
	private java.lang.String agencontact;
	
	@Column(name = "agentel")
	private java.lang.String agentel;
	
	@Column(name = "agenemail")
	private java.lang.String agenemail;
	
	@Column(name = "agenfax")
	private java.lang.String agenfax;
	
	
	@Column(name = "mblagencode1")
	private java.lang.String mblagencode1;
	
	@Column(name = "mblagencode2")
	private java.lang.String mblagencode2;
	
	@Column(name = "mblagenname")
	private java.lang.String mblagenname;
	
	@Column(name = "mblagenaddr")
	private java.lang.String mblagenaddr;
	
	@Column(name = "mblagencontact")
	private java.lang.String mblagencontact;
	
	@Column(name = "mblagentel")
	private java.lang.String mblagentel;
	
	@Column(name = "mblagenemail")
	private java.lang.String mblagenemail;
	
	@Column(name = "mblagenfax")
	private java.lang.String mblagenfax;
	
	
	@Column(name = "bkagencode1")
	private java.lang.String bkagencode1;
	
	@Column(name = "bkagencode2")
	private java.lang.String bkagencode2;
	
	@Column(name = "bkagenname")
	private java.lang.String bkagenname;
	
	@Column(name = "bkagenaddr")
	private java.lang.String bkagenaddr;
	
	@Column(name = "bkagencontact")
	private java.lang.String bkagencontact;
	
	@Column(name = "bkagentel")
	private java.lang.String bkagentel;
	
	@Column(name = "bkagenemail")
	private java.lang.String bkagenemail;
	
	@Column(name = "bkagenfax")
	private java.lang.String bkagenfax;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.String getCneecode1() {
		return cneecode1;
	}

	public void setCneecode1(java.lang.String cneecode1) {
		this.cneecode1 = cneecode1;
	}

	public java.lang.String getCneecode2() {
		return cneecode2;
	}

	public void setCneecode2(java.lang.String cneecode2) {
		this.cneecode2 = cneecode2;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public java.lang.String getCneeaddr() {
		return cneeaddr;
	}

	public void setCneeaddr(java.lang.String cneeaddr) {
		this.cneeaddr = cneeaddr;
	}

	public java.lang.String getCneecontact() {
		return cneecontact;
	}

	public void setCneecontact(java.lang.String cneecontact) {
		this.cneecontact = cneecontact;
	}

	public java.lang.String getCneetel() {
		return cneetel;
	}

	public void setCneetel(java.lang.String cneetel) {
		this.cneetel = cneetel;
	}

	public java.lang.String getCneeemail() {
		return cneeemail;
	}

	public void setCneeemail(java.lang.String cneeemail) {
		this.cneeemail = cneeemail;
	}

	public java.lang.String getCneefax() {
		return cneefax;
	}

	public void setCneefax(java.lang.String cneefax) {
		this.cneefax = cneefax;
	}

	public java.lang.String getCnorcode1() {
		return cnorcode1;
	}

	public void setCnorcode1(java.lang.String cnorcode1) {
		this.cnorcode1 = cnorcode1;
	}

	public java.lang.String getCnorcode2() {
		return cnorcode2;
	}

	public void setCnorcode2(java.lang.String cnorcode2) {
		this.cnorcode2 = cnorcode2;
	}

	public java.lang.String getCnorname() {
		return cnorname;
	}

	public void setCnorname(java.lang.String cnorname) {
		this.cnorname = cnorname;
	}

	public java.lang.String getCnoraddr() {
		return cnoraddr;
	}

	public void setCnoraddr(java.lang.String cnoraddr) {
		this.cnoraddr = cnoraddr;
	}

	public java.lang.String getCnorcontact() {
		return cnorcontact;
	}

	public void setCnorcontact(java.lang.String cnorcontact) {
		this.cnorcontact = cnorcontact;
	}

	public java.lang.String getCnortel() {
		return cnortel;
	}

	public void setCnortel(java.lang.String cnortel) {
		this.cnortel = cnortel;
	}

	public java.lang.String getCnoremail() {
		return cnoremail;
	}

	public void setCnoremail(java.lang.String cnoremail) {
		this.cnoremail = cnoremail;
	}

	public java.lang.String getCnorfax() {
		return cnorfax;
	}

	public void setCnorfax(java.lang.String cnorfax) {
		this.cnorfax = cnorfax;
	}

	public java.lang.String getNotifycode1() {
		return notifycode1;
	}

	public void setNotifycode1(java.lang.String notifycode1) {
		this.notifycode1 = notifycode1;
	}

	public java.lang.String getNotifycode2() {
		return notifycode2;
	}

	public void setNotifycode2(java.lang.String notifycode2) {
		this.notifycode2 = notifycode2;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public java.lang.String getNotifyaddr() {
		return notifyaddr;
	}

	public void setNotifyaddr(java.lang.String notifyaddr) {
		this.notifyaddr = notifyaddr;
	}

	public java.lang.String getNotifycontact() {
		return notifycontact;
	}

	public void setNotifycontact(java.lang.String notifycontact) {
		this.notifycontact = notifycontact;
	}

	public java.lang.String getNotifytel() {
		return notifytel;
	}

	public void setNotifytel(java.lang.String notifytel) {
		this.notifytel = notifytel;
	}

	public java.lang.String getNotifyemail() {
		return notifyemail;
	}

	public void setNotifyemail(java.lang.String notifyemail) {
		this.notifyemail = notifyemail;
	}

	public java.lang.String getNotifyfax() {
		return notifyfax;
	}

	public void setNotifyfax(java.lang.String notifyfax) {
		this.notifyfax = notifyfax;
	}

	public java.lang.String getMblcneecode1() {
		return mblcneecode1;
	}

	public void setMblcneecode1(java.lang.String mblcneecode1) {
		this.mblcneecode1 = mblcneecode1;
	}

	public java.lang.String getMblcneecode2() {
		return mblcneecode2;
	}

	public void setMblcneecode2(java.lang.String mblcneecode2) {
		this.mblcneecode2 = mblcneecode2;
	}

	public java.lang.String getMblcneename() {
		return mblcneename;
	}

	public void setMblcneename(java.lang.String mblcneename) {
		this.mblcneename = mblcneename;
	}

	public java.lang.String getMblcneeaddr() {
		return mblcneeaddr;
	}

	public void setMblcneeaddr(java.lang.String mblcneeaddr) {
		this.mblcneeaddr = mblcneeaddr;
	}

	public java.lang.String getMblcneecontact() {
		return mblcneecontact;
	}

	public void setMblcneecontact(java.lang.String mblcneecontact) {
		this.mblcneecontact = mblcneecontact;
	}

	public java.lang.String getMblcneetel() {
		return mblcneetel;
	}

	public void setMblcneetel(java.lang.String mblcneetel) {
		this.mblcneetel = mblcneetel;
	}

	public java.lang.String getMblcneeemail() {
		return mblcneeemail;
	}

	public void setMblcneeemail(java.lang.String mblcneeemail) {
		this.mblcneeemail = mblcneeemail;
	}

	public java.lang.String getMblcneefax() {
		return mblcneefax;
	}

	public void setMblcneefax(java.lang.String mblcneefax) {
		this.mblcneefax = mblcneefax;
	}

	public java.lang.String getMblcnorcode1() {
		return mblcnorcode1;
	}

	public void setMblcnorcode1(java.lang.String mblcnorcode1) {
		this.mblcnorcode1 = mblcnorcode1;
	}

	public java.lang.String getMblcnorcode2() {
		return mblcnorcode2;
	}

	public void setMblcnorcode2(java.lang.String mblcnorcode2) {
		this.mblcnorcode2 = mblcnorcode2;
	}

	public java.lang.String getMblcnorname() {
		return mblcnorname;
	}

	public void setMblcnorname(java.lang.String mblcnorname) {
		this.mblcnorname = mblcnorname;
	}

	public java.lang.String getMblcnoraddr() {
		return mblcnoraddr;
	}

	public void setMblcnoraddr(java.lang.String mblcnoraddr) {
		this.mblcnoraddr = mblcnoraddr;
	}

	public java.lang.String getMblcnorcontact() {
		return mblcnorcontact;
	}

	public void setMblcnorcontact(java.lang.String mblcnorcontact) {
		this.mblcnorcontact = mblcnorcontact;
	}

	public java.lang.String getMblcnortel() {
		return mblcnortel;
	}

	public void setMblcnortel(java.lang.String mblcnortel) {
		this.mblcnortel = mblcnortel;
	}

	public java.lang.String getMblcnoremail() {
		return mblcnoremail;
	}

	public void setMblcnoremail(java.lang.String mblcnoremail) {
		this.mblcnoremail = mblcnoremail;
	}

	public java.lang.String getMblcnorfax() {
		return mblcnorfax;
	}

	public void setMblcnorfax(java.lang.String mblcnorfax) {
		this.mblcnorfax = mblcnorfax;
	}

	public java.lang.String getMblnotifycode1() {
		return mblnotifycode1;
	}

	public void setMblnotifycode1(java.lang.String mblnotifycode1) {
		this.mblnotifycode1 = mblnotifycode1;
	}

	public java.lang.String getMblnotifycode2() {
		return mblnotifycode2;
	}

	public void setMblnotifycode2(java.lang.String mblnotifycode2) {
		this.mblnotifycode2 = mblnotifycode2;
	}

	public java.lang.String getMblnotifyname() {
		return mblnotifyname;
	}

	public void setMblnotifyname(java.lang.String mblnotifyname) {
		this.mblnotifyname = mblnotifyname;
	}

	public java.lang.String getMblnotifyaddr() {
		return mblnotifyaddr;
	}

	public void setMblnotifyaddr(java.lang.String mblnotifyaddr) {
		this.mblnotifyaddr = mblnotifyaddr;
	}

	public java.lang.String getMblnotifycontact() {
		return mblnotifycontact;
	}

	public void setMblnotifycontact(java.lang.String mblnotifycontact) {
		this.mblnotifycontact = mblnotifycontact;
	}

	public java.lang.String getMblnotifytel() {
		return mblnotifytel;
	}

	public void setMblnotifytel(java.lang.String mblnotifytel) {
		this.mblnotifytel = mblnotifytel;
	}

	public java.lang.String getMblnotifyemail() {
		return mblnotifyemail;
	}

	public void setMblnotifyemail(java.lang.String mblnotifyemail) {
		this.mblnotifyemail = mblnotifyemail;
	}

	public java.lang.String getMblnotifyfax() {
		return mblnotifyfax;
	}

	public void setMblnotifyfax(java.lang.String mblnotifyfax) {
		this.mblnotifyfax = mblnotifyfax;
	}

	public java.lang.String getAgencode1() {
		return agencode1;
	}

	public void setAgencode1(java.lang.String agencode1) {
		this.agencode1 = agencode1;
	}

	public java.lang.String getAgencode2() {
		return agencode2;
	}

	public void setAgencode2(java.lang.String agencode2) {
		this.agencode2 = agencode2;
	}

	public java.lang.String getAgenname() {
		return agenname;
	}

	public void setAgenname(java.lang.String agenname) {
		this.agenname = agenname;
	}

	public java.lang.String getAgenaddr() {
		return agenaddr;
	}

	public void setAgenaddr(java.lang.String agenaddr) {
		this.agenaddr = agenaddr;
	}

	public java.lang.String getAgencontact() {
		return agencontact;
	}

	public void setAgencontact(java.lang.String agencontact) {
		this.agencontact = agencontact;
	}

	public java.lang.String getAgentel() {
		return agentel;
	}

	public void setAgentel(java.lang.String agentel) {
		this.agentel = agentel;
	}

	public java.lang.String getAgenemail() {
		return agenemail;
	}

	public void setAgenemail(java.lang.String agenemail) {
		this.agenemail = agenemail;
	}

	public java.lang.String getAgenfax() {
		return agenfax;
	}

	public void setAgenfax(java.lang.String agenfax) {
		this.agenfax = agenfax;
	}

	public java.lang.String getMblagencode1() {
		return mblagencode1;
	}

	public void setMblagencode1(java.lang.String mblagencode1) {
		this.mblagencode1 = mblagencode1;
	}

	public java.lang.String getMblagencode2() {
		return mblagencode2;
	}

	public void setMblagencode2(java.lang.String mblagencode2) {
		this.mblagencode2 = mblagencode2;
	}

	public java.lang.String getMblagenname() {
		return mblagenname;
	}

	public void setMblagenname(java.lang.String mblagenname) {
		this.mblagenname = mblagenname;
	}

	public java.lang.String getMblagenaddr() {
		return mblagenaddr;
	}

	public void setMblagenaddr(java.lang.String mblagenaddr) {
		this.mblagenaddr = mblagenaddr;
	}

	public java.lang.String getMblagencontact() {
		return mblagencontact;
	}

	public void setMblagencontact(java.lang.String mblagencontact) {
		this.mblagencontact = mblagencontact;
	}

	public java.lang.String getMblagentel() {
		return mblagentel;
	}

	public void setMblagentel(java.lang.String mblagentel) {
		this.mblagentel = mblagentel;
	}

	public java.lang.String getMblagenemail() {
		return mblagenemail;
	}

	public void setMblagenemail(java.lang.String mblagenemail) {
		this.mblagenemail = mblagenemail;
	}

	public java.lang.String getMblagenfax() {
		return mblagenfax;
	}

	public void setMblagenfax(java.lang.String mblagenfax) {
		this.mblagenfax = mblagenfax;
	}

	public java.lang.String getBkagencode1() {
		return bkagencode1;
	}

	public void setBkagencode1(java.lang.String bkagencode1) {
		this.bkagencode1 = bkagencode1;
	}

	public java.lang.String getBkagencode2() {
		return bkagencode2;
	}

	public void setBkagencode2(java.lang.String bkagencode2) {
		this.bkagencode2 = bkagencode2;
	}

	public java.lang.String getBkagenname() {
		return bkagenname;
	}

	public void setBkagenname(java.lang.String bkagenname) {
		this.bkagenname = bkagenname;
	}

	public java.lang.String getBkagenaddr() {
		return bkagenaddr;
	}

	public void setBkagenaddr(java.lang.String bkagenaddr) {
		this.bkagenaddr = bkagenaddr;
	}

	public java.lang.String getBkagencontact() {
		return bkagencontact;
	}

	public void setBkagencontact(java.lang.String bkagencontact) {
		this.bkagencontact = bkagencontact;
	}

	public java.lang.String getBkagentel() {
		return bkagentel;
	}

	public void setBkagentel(java.lang.String bkagentel) {
		this.bkagentel = bkagentel;
	}

	public java.lang.String getBkagenemail() {
		return bkagenemail;
	}

	public void setBkagenemail(java.lang.String bkagenemail) {
		this.bkagenemail = bkagenemail;
	}

	public java.lang.String getBkagenfax() {
		return bkagenfax;
	}

	public void setBkagenfax(java.lang.String bkagenfax) {
		this.bkagenfax = bkagenfax;
	}
	
	
}