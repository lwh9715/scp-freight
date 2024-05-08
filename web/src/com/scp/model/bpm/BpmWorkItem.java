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
@Table(name = "bpm_workitem")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmWorkItem  implements java.io.Serializable{
	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "process_id",length = 100)
	private long process_id;
	
	
	@Column(name = "taskid")
	private java.lang.String taskId;
	
	@Column(name = "taskname")
	private java.lang.String taskname;
	
	@Column(name = "actiontype")
	private java.lang.String actiontype;
	
	@Column(name = "actions")
	private java.lang.String actions;
	
	@Column(name = "actionwhen")
	private java.lang.String actionwhen;

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

	public java.lang.String getActiontype() {
		return actiontype;
	}

	public void setActiontype(java.lang.String actiontype) {
		this.actiontype = actiontype;
	}

	public java.lang.String getActions() {
		return actions;
	}

	public void setActions(java.lang.String actions) {
		this.actions = actions;
	}

	public java.lang.String getActionwhen() {
		return actionwhen;
	}

	public void setActionwhen(java.lang.String actionwhen) {
		this.actionwhen = actionwhen;
	}
	
	
}
