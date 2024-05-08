package com.scp.vo.finance;

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
@Table(name = "fina_corp")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaCorp implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "jobid", nullable = false)
	private long jobid;
	
	/**
	 *@generated
	 */
	@Column(name = "corpid", nullable = false)
	private long corpid;
	
	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 100)
	private java.lang.String updater;
	
	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

	/**
	 *@generated
	 */
	@Column(name = "corpoptype", length = 100)
	private java.lang.String corpoptype;

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getJobid() {
		return jobid;
	}



	public void setJobid(long jobid) {
		this.jobid = jobid;
	}



	public long getCorpid() {
		return corpid;
	}



	public void setCorpid(long corpid) {
		this.corpid = corpid;
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


	public java.lang.String getCorpoptype() {
		return corpoptype;
	}

	public void setCorpoptype(java.lang.String corpoptype) {
		this.corpoptype = corpoptype;
	}

	@Override
	public String toString() {
		String toStringStr = "SysCorporation{" +
				"id=" + id +
				", jobid='" + jobid + '\'' +
				", corpid='" + corpid + '\'' +
				", inputer='" + inputer + '\'' +
				", corpoptype=" + corpoptype + '\'' +
				", inputtime=" + inputtime + '\'' +
				", updater='" + updater + '\'' +
				", updatetime=" + updatetime +

				'}';
		return toStringStr.replaceAll("'","''") ;
	}
}
