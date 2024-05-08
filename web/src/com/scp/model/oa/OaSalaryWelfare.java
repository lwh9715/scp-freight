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
	@Table(name = "oa_salary_welfare")
	@SuppressWarnings("serial")
	@Entity
	@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
	@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
	@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
	public class OaSalaryWelfare implements java.io.Serializable {

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
		@Column(name = "jobno")
		private java.lang.String sno;

		@Column(name = "userinfoid")
		private java.lang.Long userinfoid;


		@Column(name = "salaryplace")
		private java.lang.String salaryplace;
		
		@Column(name = "yearno")
		private java.lang.Long yearno;
		
		@Column(name = "monthno")
		private java.lang.Long monthno;
		
		@Column(name = "costcenter", length = 30)
		private java.lang.String costcenter;
		
		@Column(name = "jobs")
		private java.lang.String jobs;
		
		@Column(name = "placejob")
		private java.lang.String placejob;
		
		@Column(name = "namec", length = 30)
		private java.lang.String namec;
		
		
		@Column(name = "ph_cost")
		private java.lang.Double ph_cost;
		
		@Column(name = "currency")
		private java.lang.String currency;
		
		
		@Column(name = "traff_cost")
		private java.lang.Double traff_cost;
		
		@Column(name = "fest_cost")
		private java.lang.Double fest_cost;
		
		@Column(name = "baby_cost")
		private java.lang.Double baby_cost;
		
		@Column(name = "ser_cost")
		private java.lang.Double ser_cost;
		
		@Column(name = "birth_cost")
		private java.lang.Double birth_cost;
		
		@Column(name = "sport_cost")
		private java.lang.Double sport_cost;
		@Column(name = "depart_cost")
		private java.lang.Double depart_cost;
		@Column(name = "unexpect_cost")
		private java.lang.Double unexpect_cost;
		@Column(name = "phy_cost")
		private java.lang.Double phy_cost;
		@Column(name = "travel_cost")
		private java.lang.Double travel_cost;
		@Column(name = "out_cost")
		private java.lang.Double out_cost;
		@Column(name = "airport_cost")
		private java.lang.Double airport_cost;
		@Column(name = "all_cost")
		private java.lang.Double all_cost;
		
		@Column(name = "remarks")
		private java.lang.String remarks;
		
		
		@Column(name = "isdelete")
		private java.lang.Boolean isdelete;

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

		
		
		
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public java.lang.String getSno() {
			return sno;
		}

		public void setSno(java.lang.String sno) {
			this.sno = sno;
		}

		public java.lang.Long getUserinfoid() {
			return userinfoid;
		}

		public void setUserinfoid(java.lang.Long userinfoid) {
			this.userinfoid = userinfoid;
		}
		
		

		public java.lang.Long getYearno() {
			return yearno;
		}

		public void setYearno(java.lang.Long yearno) {
			this.yearno = yearno;
		}

		public java.lang.Long getMonthno() {
			return monthno;
		}

		public void setMonthno(java.lang.Long monthno) {
			this.monthno = monthno;
		}

		public java.lang.String getSalaryplace() {
			return salaryplace;
		}

		public void setSalaryplace(java.lang.String salaryplace) {
			this.salaryplace = salaryplace;
		}

		public java.lang.String getCostcenter() {
			return costcenter;
		}

		public void setCostcenter(java.lang.String costcenter) {
			this.costcenter = costcenter;
		}

		public java.lang.String getJobs() {
			return jobs;
		}

		public void setJobs(java.lang.String jobs) {
			this.jobs = jobs;
		}

		public java.lang.String getPlacejob() {
			return placejob;
		}

		public void setPlacejob(java.lang.String placejob) {
			this.placejob = placejob;
		}

		public java.lang.String getNamec() {
			return namec;
		}

		public void setNamec(java.lang.String namec) {
			this.namec = namec;
		}
		
		public java.lang.Double getPh_cost() {
			return ph_cost;
		}

		public void setPh_cost(java.lang.Double phCost) {
			ph_cost = phCost;
		}
		
		public java.lang.String getCurrency() {
			return currency;
		}

		public void setCurrency(java.lang.String currency) {
			this.currency = currency;
		}

		public java.lang.Double getTraff_cost() {
			return traff_cost;
		}

		public void setTraff_cost(java.lang.Double traffCost) {
			traff_cost = traffCost;
		}

		public java.lang.Double getFest_cost() {
			return fest_cost;
		}

		public void setFest_cost(java.lang.Double festCost) {
			fest_cost = festCost;
		}

		public java.lang.Double getBaby_cost() {
			return baby_cost;
		}

		public void setBaby_cost(java.lang.Double babyCost) {
			baby_cost = babyCost;
		}

		public java.lang.Double getSer_cost() {
			return ser_cost;
		}

		public void setSer_cost(java.lang.Double serCost) {
			ser_cost = serCost;
		}

		public java.lang.Double getBirth_cost() {
			return birth_cost;
		}

		public void setBirth_cost(java.lang.Double birthCost) {
			birth_cost = birthCost;
		}

		public java.lang.Double getSport_cost() {
			return sport_cost;
		}

		public void setSport_cost(java.lang.Double sportCost) {
			sport_cost = sportCost;
		}

		public java.lang.Double getDepart_cost() {
			return depart_cost;
		}

		public void setDepart_cost(java.lang.Double departCost) {
			depart_cost = departCost;
		}

		public java.lang.Double getUnexpect_cost() {
			return unexpect_cost;
		}

		public void setUnexpect_cost(java.lang.Double unexpectCost) {
			unexpect_cost = unexpectCost;
		}

		public java.lang.Double getPhy_cost() {
			return phy_cost;
		}

		public void setPhy_cost(java.lang.Double phyCost) {
			phy_cost = phyCost;
		}

		public java.lang.Double getTravel_cost() {
			return travel_cost;
		}

		public void setTravel_cost(java.lang.Double travelCost) {
			travel_cost = travelCost;
		}

		public java.lang.Double getOut_cost() {
			return out_cost;
		}

		public void setOut_cost(java.lang.Double outCost) {
			out_cost = outCost;
		}

		public java.lang.Double getAirport_cost() {
			return airport_cost;
		}

		public void setAirport_cost(java.lang.Double airportCost) {
			airport_cost = airportCost;
		}

		public java.lang.Double getAll_cost() {
			return all_cost;
		}

		public void setAll_cost(java.lang.Double allCost) {
			all_cost = allCost;
		}

		public java.lang.String getRemarks() {
			return remarks;
		}

		public void setRemarks(java.lang.String remarks) {
			this.remarks = remarks;
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

