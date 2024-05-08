package com.scp.model.website;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "web_config")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WebConfig implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private java.lang.Long id;

	@Column(name = "key",length=30)
	private java.lang.String key;

	@Column(name = "val")
	private java.lang.String val;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getKey() {
		return key;
	}

	public void setKey(java.lang.String key) {
		this.key = key;
	}

	public java.lang.String getVal() {
		return val;
	}

	public void setVal(java.lang.String val) {
		this.val = val;
	}

}
