package com.scp.model.bpm;

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
@Table(name = "bpm_assign")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmAssign  implements java.io.Serializable{
	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "process_id",length = 100)
	private long process_id;
	
	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "tousercode")
	private java.lang.String tousercode;
	
	@Column(name = "taskid")
	private java.lang.String taskId;
	
	@Column(name = "taskname")
	private java.lang.String taskname;
	
	@Column(name = "step")
	private java.lang.Integer step;
	
	@Column(name = "url")
	private java.lang.String url;
	
	@Column(name = "formview")
	private java.lang.String formview;
	
	@Column(name = "is2submit")
	private java.lang.Boolean is2submit;
	
	@Column(name = "isauto")
	private java.lang.Boolean isauto;
	
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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "isalert")
	private java.lang.Boolean isalert;
	
	@Column(name = "alertafter")
	private java.math.BigDecimal alertafter;
	
	@Column(name = "aftertype")
	private java.lang.String aftertype;
	
	@Column(name = "ismatchassign")
	private java.lang.Boolean ismatchassign;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProcess_id() {
		return process_id;
	}

	public void setProcess_id(long processId) {
		process_id = processId;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.String getTousercode() {
		return tousercode;
	}

	public void setTousercode(java.lang.String tousercode) {
		this.tousercode = tousercode;
	}

	public java.lang.String getTaskId() {
		return taskId;
	}

	public void setTaskId(java.lang.String taskId) {
		this.taskId = taskId;
	}

	public java.lang.String getTaskname() {
		return taskname;
	}

	public void setTaskname(java.lang.String taskname) {
		this.taskname = taskname;
	}

	public java.lang.String getUrl() {
		return url;
	}

	public void setUrl(java.lang.String url) {
		this.url = url;
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

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.Integer getStep() {
		return step;
	}

	public void setStep(java.lang.Integer step) {
		this.step = step;
	}

	public java.lang.String getFormview() {
		return formview;
	}

	public void setFormview(java.lang.String formview) {
		this.formview = formview;
	}

	public java.lang.Boolean getIs2submit() {
		return is2submit;
	}

	public void setIs2submit(java.lang.Boolean is2submit) {
		this.is2submit = is2submit;
	}

	public java.lang.Boolean getIsauto() {
		return isauto;
	}

	public void setIsauto(java.lang.Boolean isauto) {
		this.isauto = isauto;
	}

	public java.lang.Boolean getIsalert() {
		return isalert;
	}

	public void setIsalert(java.lang.Boolean isalert) {
		this.isalert = isalert;
	}

	public java.math.BigDecimal getAlertafter() {
		return alertafter;
	}

	public void setAlertafter(java.math.BigDecimal alertafter) {
		this.alertafter = alertafter;
	}

	public java.lang.String getAftertype() {
		return aftertype;
	}

	public void setAftertype(java.lang.String aftertype) {
		this.aftertype = aftertype;
	}

	public java.lang.Boolean getIsmatchassign() {
		return ismatchassign;
	}

	public void setIsmatchassign(java.lang.Boolean ismatchassign) {
		this.ismatchassign = ismatchassign;
	}
	
}
