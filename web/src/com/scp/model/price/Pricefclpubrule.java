package com.scp.model.price;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Date:2016-6-20 FCL运价主表：price_fcl
 * 
 * @author bruce
 */
@Table(name = "price_fcl_pubrule")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Pricefclpubrule implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	

	@Column(name = "ruletype", length = 1)
	private java.lang.String ruletype;
	
	@Column(name = "level", length = 30)
	private java.lang.String level;
	
	/**
	 *@generated
	 */
	@Column(name = "refid")
	private java.lang.Long refid;
	
	@Column(name = "cost20")
	private java.math.BigDecimal cost20;

	@Column(name = "cost40gp")
	private java.math.BigDecimal cost40gp;

	@Column(name = "cost40hq")
	private java.math.BigDecimal cost40hq;
	
	@Column(name = "cost45hq")
	private java.math.BigDecimal cost45hq;
	
	@Column(name = "ispublic")
	private java.lang.Boolean ispublic;
	
	@Column(name = "lines")
	private java.lang.String lines;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getRuletype() {
		return ruletype;
	}

	public void setRuletype(java.lang.String ruletype) {
		this.ruletype = ruletype;
	}

	public java.lang.String getLevel() {
		return level;
	}

	public void setLevel(java.lang.String level) {
		this.level = level;
	}

	public java.lang.Long getRefid() {
		return refid;
	}

	public void setRefid(java.lang.Long refid) {
		this.refid = refid;
	}

	public java.math.BigDecimal getCost20() {
		return cost20;
	}

	public void setCost20(java.math.BigDecimal cost20) {
		this.cost20 = cost20;
	}

	public java.math.BigDecimal getCost40gp() {
		return cost40gp;
	}

	public void setCost40gp(java.math.BigDecimal cost40gp) {
		this.cost40gp = cost40gp;
	}

	public java.math.BigDecimal getCost40hq() {
		return cost40hq;
	}

	public void setCost40hq(java.math.BigDecimal cost40hq) {
		this.cost40hq = cost40hq;
	}

	public java.lang.Boolean getIspublic() {
		return ispublic;
	}

	public void setIspublic(java.lang.Boolean ispublic) {
		this.ispublic = ispublic;
	}

	public java.math.BigDecimal getCost45hq() {
		return cost45hq;
	}

	public void setCost45hq(java.math.BigDecimal cost45hq) {
		this.cost45hq = cost45hq;
	}

	public java.lang.String getLines() {
		return lines;
	}

	public void setLines(java.lang.String lines) {
		this.lines = lines;
	}
	
}
