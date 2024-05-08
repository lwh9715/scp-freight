	package com.scp.model.oa;

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
	@Table(name = "oa_user_transfer")
	@SuppressWarnings("serial")
	@Entity
	@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
	@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
	@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
	public class OaUserTransfer implements java.io.Serializable {

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
		@Column(name = "userinfoid")
		private java.lang.Long userinfoid;

		@Column(name = "transdate")
		private java.util.Date transdate;
		
		@Column(name = "transpalce")
		private java.lang.String transpalce;
		
		@Column(name = "transduty")
		private java.lang.String transduty;

		@Column(name = "isdelete")
		private java.lang.Boolean isdelete;

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

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public java.lang.Long getUserinfoid() {
			return userinfoid;
		}

		public void setUserinfoid(java.lang.Long userinfoid) {
			this.userinfoid = userinfoid;
		}

		public java.util.Date getTransdate() {
			return transdate;
		}

		public void setTransdate(java.util.Date transdate) {
			this.transdate = transdate;
		}

		public java.lang.String getTranspalce() {
			return transpalce;
		}

		public void setTranspalce(java.lang.String transpalce) {
			this.transpalce = transpalce;
		}

		public java.lang.String getTransduty() {
			return transduty;
		}

		public void setTransduty(java.lang.String transduty) {
			this.transduty = transduty;
		}

		public java.lang.Boolean getIsdelete() {
			return isdelete;
		}

		public void setIsdelete(java.lang.Boolean isdelete) {
			this.isdelete = isdelete;
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

	}

