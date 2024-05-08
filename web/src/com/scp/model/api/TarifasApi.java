package com.scp.model.api;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * Date:2016-6-20 FCL运价主表：tarifas_api
 * 
 * @author bruce
 */
@Table(name = "tarifas_api")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class TarifasApi implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "email", length = 30)
	private String email;

	@Column(name = "carrier", length = 30)
	private String carrier;

	@Column(name = "pol", length = 30)
	private String pol;

	@Column(name = "pod", length = 30)
	private String pod;

	@Column(name = "viaport", length = 30)
	private String viaport;

	@Column(name = "tt", length = 30)
	private String tt;

	@Column(name = "currency", length = 30)
	private String currency;

	@Column(name = "rate20dv", length = 30)
	private String rate20dv;

	@Column(name = "rate40dv", length = 30)
	private String rate40dv;

	@Column(name = "rate40hc", length = 30)
	private String rate40hc;

	@Column(name = "rate40nor", length = 30)
	private String rate40nor;

	@Column(name = "extraperctnr", length = 30)
	private String extraperctnr;

	@Column(name = "extraperteu", length = 30)
	private String extraperteu;

	@Column(name = "extraperhbl", length = 30)
	private String extraperhbl;

	@Column(name = "effectivestartdate", length = 13)
	private java.util.Date effectivestartdate;

	@Column(name = "effectiveenddate", length = 13)
	private java.util.Date effectiveenddate;

	@Column(name = "condition", length = 30)
	private String condition;

	@Column(name = "ft_dc_hc", length = 30)
	private String ft_dc_hc;

	@Column(name = "ft_nor", length = 30)
	private String ft_nor;

	@Column(name = "agreement", length = 30)
	private String agreement;

	@Column(name = "ows_otherscharges", length = 30)
	private String ows_otherscharges;

	@Column(name = "remarks", length = 30)
	private String remarks;

	@Column(name = "otherdestinations")
	private String otherdestinations;


	@Column(name = "inputer", length = 30)
	private String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 30)
	private String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
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

	public String getViaport() {
		return viaport;
	}

	public void setViaport(String viaport) {
		this.viaport = viaport;
	}

	public String getTt() {
		return tt;
	}

	public void setTt(String tt) {
		this.tt = tt;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRate20dv() {
		return rate20dv;
	}

	public void setRate20dv(String rate20dv) {
		this.rate20dv = rate20dv;
	}

	public String getRate40dv() {
		return rate40dv;
	}

	public void setRate40dv(String rate40dv) {
		this.rate40dv = rate40dv;
	}

	public String getRate40hc() {
		return rate40hc;
	}

	public void setRate40hc(String rate40hc) {
		this.rate40hc = rate40hc;
	}

	public String getRate40nor() {
		return rate40nor;
	}

	public void setRate40nor(String rate40nor) {
		this.rate40nor = rate40nor;
	}

	public String getExtraperctnr() {
		return extraperctnr;
	}

	public void setExtraperctnr(String extraperctnr) {
		this.extraperctnr = extraperctnr;
	}

	public String getExtraperteu() {
		return extraperteu;
	}

	public void setExtraperteu(String extraperteu) {
		this.extraperteu = extraperteu;
	}

	public String getExtraperhbl() {
		return extraperhbl;
	}

	public void setExtraperhbl(String extraperhbl) {
		this.extraperhbl = extraperhbl;
	}

	public Date getEffectivestartdate() {
		return effectivestartdate;
	}

	public void setEffectivestartdate(Date effectivestartdate) {
		this.effectivestartdate = effectivestartdate;
	}

	public Date getEffectiveenddate() {
		return effectiveenddate;
	}

	public void setEffectiveenddate(Date effectiveenddate) {
		this.effectiveenddate = effectiveenddate;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getFt_dc_hc() {
		return ft_dc_hc;
	}

	public void setFt_dc_hc(String ft_dc_hc) {
		this.ft_dc_hc = ft_dc_hc;
	}

	public String getFt_nor() {
		return ft_nor;
	}

	public void setFt_nor(String ft_nor) {
		this.ft_nor = ft_nor;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getOws_otherscharges() {
		return ows_otherscharges;
	}

	public void setOws_otherscharges(String ows_otherscharges) {
		this.ows_otherscharges = ows_otherscharges;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOtherdestinations() {
		return otherdestinations;
	}

	public void setOtherdestinations(String otherdestinations) {
		this.otherdestinations = otherdestinations;
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
