package com.scp.model.finance;

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
@Table(name = "fs_config_arap")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class FinaConfigarap implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "actsetid")
	private Long actsetid;
	
	@Column(name = "vchtypeid")
	private Long vchtypeid;
	
	@Column(name = "ar_actid")
	private Long ar_actid;
	
	@Column(name = "ap_actid")
	private Long ap_actid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getActsetid() {
		return actsetid;
	}

	public void setActsetid(Long actsetid) {
		this.actsetid = actsetid;
	}

	public Long getVchtypeid() {
		return vchtypeid;
	}

	public void setVchtypeid(Long vchtypeid) {
		this.vchtypeid = vchtypeid;
	}

	public Long getAr_actid() {
		return ar_actid;
	}

	public void setAr_actid(Long arActid) {
		ar_actid = arActid;
	}

	public Long getAp_actid() {
		return ap_actid;
	}

	public void setAp_actid(Long apActid) {
		ap_actid = apActid;
	}

	
}