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
@Table(name = "fina_billcntdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id")}) 
public class FinaBillCntdtl implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "billid")
	private Long billid;
	
	@Column(name = "cntid")
	private Long cntid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getBillid() {
		return billid;
	}

	public void setBillid(Long billid) {
		this.billid = billid;
	}

	public Long getCntid() {
		return cntid;
	}

	public void setCntid(Long cntid) {
		this.cntid = cntid;
	}
	
	
}