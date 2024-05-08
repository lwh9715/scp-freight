package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_ship_bill_reg")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipBillreg implements java.io.Serializable {

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
	@Column(name = "code")
	private java.lang.String code;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	 

	/**
	 *@generated
	 */
	@Column(name = "billid")
	private java.lang.Long billid;
	
	@Column(name = "billno")
	private java.lang.String billno;

	/**
	 *@generated
	 */
	@Column(name = "iscancel")
	private java.lang.Boolean iscancel;
	
	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime")
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime")
	private java.util.Date updatetime;
	
	/**
	 *@generated
	 */
	@Column(name = "billlading", length = 30)
	private java.lang.String billlading;
	
	/**
	 *@generated
	 */
	@Column(name = "agentname", length = 50)
	private java.lang.String agentname;
	
	/**
	 *@generated
	 */
	@Column(name = "sales", length = 50)
	private java.lang.String sales;
	
	/**
	 *@generated
	 */
	@Column(name = "isguarantee")
	private java.lang.Boolean isguarantee;
	
	/**
	 *@generated
	 */
	@Column(name = "guarantee", length = 50)
	private java.lang.String guarantee;
	
	public java.lang.String getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(java.lang.String guarantee) {
		this.guarantee =guarantee;
	}
	
	/**
	 *@generated
	 */
	@Column(name = "printingcode", length = 50)
	private java.lang.String printingcode;
	
	/**
	 *@generated
	 */
	@Column(name = "hblrecipient", length = 50)
	private java.lang.String hblrecipient;
	
	/**
	 *@generated
	 */
	@Column(name = "hblrecipientdate")
	private java.util.Date hblrecipientdate;
	
	/**
	 *@generated
	 */
	@Column(name = "hblinvaliddate")
	private java.util.Date hblinvaliddate;
	
	/**
	 *@generated
	 */
	@Column(name = "returndate")
	private java.util.Date returndate;
	
	/**
	 *@generated
	 */
	@Column(name = "isrecall")
	private java.lang.Boolean isrecall;
	
	/**
	 *@generated
	 */
	@Column(name = "recalldate")
	private java.util.Date recalldate;
	
	/**
	 *@generated
	 */
	@Column(name = "recalluser", length = 30)
	private java.lang.String recalluser;
	
	public java.util.Date getReturndate() {
		return returndate;
	}

	public void setReturndate(java.util.Date returndate) {
		this.returndate = returndate;
	}
	
	public java.util.Date getHblinvaliddate() {
		return hblinvaliddate;
	}

	public void setHblinvaliddate(java.util.Date hblinvaliddate) {
		this.hblinvaliddate = hblinvaliddate;
	}
	
	public java.util.Date getHblrecipientdate() {
		return hblrecipientdate;
	}

	public void setHblrecipientdate(java.util.Date hblrecipientdate) {
		this.hblrecipientdate = hblrecipientdate;
	}
	
	public java.lang.String getHblrecipient() {
		return hblrecipient;
	}

	public void setHblrecipient(java.lang.String hblrecipient) {
		this.hblrecipient =hblrecipient;
	}
	
	public java.lang.String getPrintingcode() {
		return printingcode;
	}

	public void setPrintingcode(java.lang.String printingcode) {
		this.printingcode =printingcode;
	}
	
	public java.lang.Boolean getIsguarantee() {
		return isguarantee;
	}

	public void setIsguarantee(java.lang.Boolean isguarantee) {
		this.isguarantee = isguarantee;
	}
	
	public java.lang.String getSales() {
		return sales;
	}

	public void setSales(java.lang.String sales) {
		this.sales = sales;
	}
	
	public java.lang.String getAgentname() {
		return agentname;
	}

	public void setAgentname(java.lang.String agentname) {
		this.agentname = agentname;
	}
	
	public java.lang.String getBilllading() {
		return billlading;
	}

	public void setBilllading(java.lang.String billlading) {
		this.billlading = billlading;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.Long getBillid() {
		return billid;
	}

	public void setBillid(java.lang.Long billid) {
		this.billid = billid;
	}

	public java.lang.Boolean getIscancel() {
		return iscancel;
	}

	public void setIscancel(java.lang.Boolean iscancel) {
		this.iscancel = iscancel;
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

	public java.lang.String getBillno() {
		return billno;
	}

	public void setBillno(java.lang.String billno) {
		this.billno = billno;
	}

	public java.lang.Boolean getIsrecall() {
		return isrecall;
	}

	public void setIsrecall(java.lang.Boolean isrecall) {
		this.isrecall = isrecall;
	}

	public java.util.Date getRecalldate() {
		return recalldate;
	}

	public void setRecalldate(java.util.Date recalldate) {
		this.recalldate = recalldate;
	}

	public java.lang.String getRecalluser() {
		return recalluser;
	}

	public void setRecalluser(java.lang.String recalluser) {
		this.recalluser = recalluser;
	}

}