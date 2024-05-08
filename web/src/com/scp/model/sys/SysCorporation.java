package com.scp.model.sys;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parameter;

/**
 *@generated
 */
@Table(name = "sys_corporation")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysCorporation implements java.io.Serializable {

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
	@Column(name = "code",  length = 20)
	private java.lang.String code;
	
	/**
	 *@generated
	 */
	@Column(name = "helpcode",  length = 30)
	private java.lang.String helpcode;
	
	/**
	 *@generated
	 */
	@Column(name = "billinfo")
	private java.lang.String billinfo ;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 100)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 150)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "abbr", length = 100)
	private java.lang.String abbr;
	
	@Column(name = "abbcode", length = 10)
	private java.lang.String abbcode;

	/**
	 *@generated
	 */
	@Column(name = "addressc", length = 254)
	private java.lang.String addressc;

	/**
	 *@generated
	 */
	@Column(name = "addresse", length = 254)
	private java.lang.String addresse;

	/**
	 *@generated
	 */
	@Column(name = "tel1", length = 100)
	private java.lang.String tel1;
	
	

	/**
	 *@generated
	 */
	@Column(name = "tel2", length = 100)
	private java.lang.String tel2;
	
	
	/**
	 *@generated
	 */
	@Column(name = "phone1", length = 100)
	private java.lang.String phone1 ;
	
	

	/**
	 *@generated
	 */
	@Column(name = "phone2", length = 100)
	private java.lang.String phone2;

	/**
	 *@generated
	 */
	@Column(name = "fax1", length = 100)
	private java.lang.String fax1;

	/**
	 *@generated
	 */
	@Column(name = "fax2", length = 100)
	private java.lang.String fax2;

	/**
	 *@generated
	 */
	@Column(name = "email1", length = 100)
	private java.lang.String email1;

	/**
	 *@generated
	 */
	@Column(name = "email2", length = 100)
	private java.lang.String email2;

	/**
	 *@generated
	 */
	@Column(name = "homepage", length = 100)
	private java.lang.String homepage;

	/**
	 *@generated
	 */
	@Column(name = "contact")
	private java.lang.String contact;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "srctype")
	private java.lang.String srctype;

	/**
	 *@generated
	 */
	@Column(name = "isar")
	private java.lang.Boolean isar;

	/**
	 *@generated
	 */
	@Column(name = "isap")
	private java.lang.Boolean isap;
	

	/**
	 *@generated
	 */
	@Column(name = "iscustomer")
	private java.lang.Boolean iscustomer;
	
	@Column(name = "isairline")
	private java.lang.Boolean isairline;
	
	@Column(name = "iscarrier")
	private java.lang.Boolean iscarrier;
	
	@Column(name = "isagent")
	private java.lang.Boolean isagent;
	
	@Column(name = "iscustom")
	private java.lang.Boolean iscustom;
	
	@Column(name = "isclc")
	private java.lang.Boolean isclc;

	@Column(name = "iscommerce")
	private java.lang.Boolean iscommerce;
	
	
	@Column(name = "isfcl")
	private java.lang.Boolean isfcl;
	
	@Column(name = "isagentdes")
	private java.lang.Boolean isagentdes;
	
	@Column(name = "isddp")
	private java.lang.Boolean isddp;
	
	@Column(name = "islcl")
	private java.lang.Boolean islcl;
	
	@Column(name = "isair")
	private java.lang.Boolean isair;
	
	@Column(name = "istrain")
	private java.lang.Boolean istrain;
	
	@Column(name = "amtowe")
	private java.lang.Double amtowe;
	
	@Column(name = "currency", length = 3)
	private java.lang.String currency ;
	
	@Column(name = "isalloworder")
	private java.lang.Boolean isalloworder;
	/**
	 *@generated
	 */
	@Column(name = "ischannel")
	private java.lang.Boolean ischannel;
	

	/**
	 *@generated
	 */
	@Column(name = "istruck")
	private java.lang.Boolean istruck;
	
	/**
	 *@generated
	 */
	@Column(name = "iswarehouse")
	private java.lang.Boolean iswarehouse;
	
	
	@Column(name = "ishare")
	private java.lang.Boolean ishare;
	
	@Column(name = "isfactory")
	private java.lang.Boolean isfactory;
	
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	@Column(name = "checkter")
	private java.lang.String checkter;
	
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;
	
	@Column(name = "sop")
	private java.lang.String sop;
	
	@Column(name = "daystype")
	private java.lang.String daystype;
	
	
	@Column(name = "pkid_remote")
	private java.lang.String pkidRemote;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "customertype")
	private java.lang.String customertype;
	
	@Column(name = "isactset")
	private java.lang.Boolean isactset;
	
	@Column(name = "corpidlink")
	private java.lang.Long corpidlink;
	
	@Column(name = "billnoprefix")
	private java.lang.String billnoprefix;
	
	public java.lang.String getBillnoprefix() {
		return billnoprefix;
	}

	public void setBillnoprefix(java.lang.String billnoprefix) {
		this.billnoprefix = billnoprefix;
	}

	public java.lang.Boolean getIswarehouse() {
		return iswarehouse;
	}

	public void setIswarehouse(java.lang.Boolean iswarehouse) {
		this.iswarehouse = iswarehouse;
	}

	/**
	 *@generated
	 */
	@Column(name = "isdelivery")
	private java.lang.Boolean isdelivery;
	
	
	/**
	 *@generated
	 */
	@Column(name = "isshipping")
	private java.lang.Boolean isshipping;

	/**
	 *@generated
	 */
	@Column(name = "salesid")
	private java.lang.Long salesid;
	/**
	 *@generated
	 */
	@Column(name = "daysar")
	private java.lang.Integer daysar;

	/**
	 *@generated
	 */
	@Column(name = "oweamount")
	private java.math.BigDecimal oweamount;

	/**
	 *@generated
	 */
	@Column(name = "other1")
	private java.lang.String other1;

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
	
	@Column(name = "paytype")
	private java.lang.String paytype;
	
	
	 

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

	/**
	 *@generated
	 */
	@Column(name = "level", length = 10)
	private java.lang.String level;
	
	
	
	/**
	 *@generated
	 */
	@Column(name = "portdesc", length = 100)
	private java.lang.String portdesc;
	
	/**
	 *@generated
	 */
	@Column(name = "invoicetitle" )
	private java.lang.String invoicetitle;

	/**
	 *@generated
	 */
	@Column(name = "classify",  length = 1)
	private java.lang.String classify;

	/**
	 *@generated
	 */
	@Column(name = "isofficial")
	private java.lang.Boolean isofficial;

	/**
	 *@generated
	 */
	@Column(name = "isexpress")
	private java.lang.Boolean isexpress;

	/**
	 *@generated
	 */
	@Column(name = "issupplier")
	private java.lang.Boolean issupplier;

	/**
	 *@generated
	 */
	@Column(name = "iscooperative")
	private java.lang.Boolean iscooperative;
	
	
	@Column(name = "ishipper")
	private java.lang.Boolean ishipper;
	
	@Column(name = "isconsignee")
	private java.lang.Boolean isconsignee;

	/**
	 *@generated
	 */
	@Column(name = "parentid")
	private java.lang.Long parentid;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "basecurrency",  length = 3)
	private java.lang.String basecurrency;
	
	/**
	 *@generated
	 */
	@Column(name = "tradeway", length = 1)
	private java.lang.String tradeway;
	
	/**
	 *@generated
	 */
	@Column(name = "taxid")
	private java.lang.String taxid;
	
	
	@Column(name = "licno")
	private java.lang.String licno;
	
	@Column(name = "apikey")
	private java.lang.String apikey;

	@Column(name = "member")
	private java.lang.String member; //会员

	@Column(name = "memberamt")
	private java.lang.String memberamt;	//会员额度

	@Column(name = "membercode")
	private java.lang.String membercode;	//会员代码

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getMembercode() {
		return membercode;
	}

	public void setMembercode(String membercode) {
		this.membercode = membercode;
	}

	public String getMemberamt() {
		return memberamt;
	}

	public void setMemberamt(String memberamt) {
		this.memberamt = memberamt;
	}

	public java.lang.String getApikey() {
		return apikey;
	}

	public void setApikey(java.lang.String apikey) {
		this.apikey = apikey;
	}

	/**
	 *@generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "sysCorporation")
	@IndexColumn(name="id")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Fetch(FetchMode.SELECT)
	private java.util.List<SysDepartment> sysDepartments = new java.util.ArrayList<SysDepartment>();

	/**
	 *@generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "sysCorporation")
	@IndexColumn(name="id")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Fetch(FetchMode.SELECT)
	private java.util.List<SysUser> sysUsers = new java.util.ArrayList<SysUser>();
	
	/**
	 *@generated
	 */
	@Column(name = "impexp ", length = 1)
	private java.lang.String impexp ;
	
	@Column(name = "datear")
	private java.lang.Integer datear;
	
	@Column(name = "payremarks")
	private java.lang.String payremarks;
	
	@Column(name = "socialcreditno")
	private java.lang.String socialcreditno;
	
	@Column(name = "country",  length = 50)
	private java.lang.String country;
	
	@Column(name = "airlineno",  length = 50)
	private java.lang.String airlineno;
	
	@Column(name = "airlinecode",  length = 50)
	private java.lang.String airlinecode;
	
	@Column(name = "bookinginfo")
	private java.lang.String bookinginfo;
	
	@Column(name = "handinfo")
	private java.lang.String handinfo;

	@Column(name = "namecqry")
	private java.lang.String namecqry;


	public String getNamecqry() {
		return namecqry;
	}

	public void setNamecqry(String namecqry) {
		this.namecqry = namecqry;
	}

	/**
	 *@generated
	 */
	public long getId() {
		return this.id;
	}

	/**
	 *@generated
	 */
	public void setId(long value) {
		this.id = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 *@generated
	 */
	public void setCode(java.lang.String value) {
		this.code = value;
	}
	
	/**
	 *@generated
	 */
	public java.lang.String getHelpcode() {
		return this.helpcode;
	}

	/**
	 *@generated
	 */
	public void setHelpcode(java.lang.String value) {
		this.helpcode = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamec() {
		return this.namec;
	}

	/**
	 *@generated
	 */
	public void setNamec(java.lang.String value) {
		this.namec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamee() {
		return this.namee;
	}

	/**
	 *@generated
	 */
	public void setNamee(java.lang.String value) {
		this.namee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAbbr() {
		return this.abbr;
	}

	/**
	 *@generated
	 */
	public void setAbbr(java.lang.String value) {
		this.abbr = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAddressc() {
		return this.addressc;
	}

	/**
	 *@generated
	 */
	public void setAddressc(java.lang.String value) {
		this.addressc = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAddresse() {
		return this.addresse;
	}

	/**
	 *@generated
	 */
	public void setAddresse(java.lang.String value) {
		this.addresse = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTel1() {
		return this.tel1;
	}

	/**
	 *@generated
	 */
	public void setTel1(java.lang.String value) {
		this.tel1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTel2() {
		return this.tel2;
	}

	/**
	 *@generated
	 */
	public void setTel2(java.lang.String value) {
		this.tel2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFax1() {
		return this.fax1;
	}

	/**
	 *@generated
	 */
	public void setFax1(java.lang.String value) {
		this.fax1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFax2() {
		return this.fax2;
	}

	/**
	 *@generated
	 */
	public void setFax2(java.lang.String value) {
		this.fax2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmail1() {
		return this.email1;
	}

	/**
	 *@generated
	 */
	public void setEmail1(java.lang.String value) {
		this.email1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmail2() {
		return this.email2;
	}

	/**
	 *@generated
	 */
	public void setEmail2(java.lang.String value) {
		this.email2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getHomepage() {
		return this.homepage;
	}

	/**
	 *@generated
	 */
	public void setHomepage(java.lang.String value) {
		this.homepage = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getContact() {
		return this.contact;
	}

	/**
	 *@generated
	 */
	public void setContact(java.lang.String value) {
		this.contact = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}
	
	/**
	 *@generated
	 */
	public java.lang.Long getSalesid() {
		return this.salesid;
	}

	/**
	 *@generated
	 */
	public void setSalesid(java.lang.Long value) {
		this.salesid = value;
	}


	/**
	 *@generated
	 */
	public java.math.BigDecimal getOweamount() {
		return this.oweamount;
	}

	/**
	 *@generated
	 */
	public void setOweamount(java.math.BigDecimal value) {
		this.oweamount = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getOther1() {
		return this.other1;
	}

	/**
	 *@generated
	 */
	public void setOther1(java.lang.String value) {
		this.other1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getInputer() {
		return this.inputer;
	}

	/**
	 *@generated
	 */
	public void setInputer(java.lang.String value) {
		this.inputer = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getInputtime() {
		return this.inputtime;
	}

	/**
	 *@generated
	 */
	public void setInputtime(java.util.Date value) {
		this.inputtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getUpdater() {
		return this.updater;
	}

	/**
	 *@generated
	 */
	public void setUpdater(java.lang.String value) {
		this.updater = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getUpdatetime() {
		return this.updatetime;
	}

	/**
	 *@generated
	 */
	public void setUpdatetime(java.util.Date value) {
		this.updatetime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLevel() {
		return this.level;
	}

	/**
	 *@generated
	 */
	public void setLevel(java.lang.String value) {
		this.level = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClassify() {
		return this.classify;
	}

	/**
	 *@generated
	 */
	public void setClassify(java.lang.String value) {
		this.classify = value;
	}


	/**
	 *@generated
	 */
	public java.lang.Long getParentid() {
		return this.parentid;
	}

	/**
	 *@generated
	 */
	public void setParentid(java.lang.Long value) {
		this.parentid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsdelete() {
		return this.isdelete;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getBasecurrency() {
		return this.basecurrency;
	}

	/**
	 *@generated
	 */
	public void setBasecurrency(java.lang.String value) {
		this.basecurrency = value;
	}

	/**
	 *@generated
	 */
	public java.util.List<SysDepartment> getSysDepartments() {
		return this.sysDepartments;
	}

	/**
	 *@generated
	 */
	public void setSysDepartments(
			java.util.List<SysDepartment> value) {
		this.sysDepartments = value;
	}

	/**
	 *@generated
	 */
	public java.util.List<SysUser> getSysUsers() {
		return this.sysUsers;
	}

	/**
	 *@generated
	 */
	public void setSysUsers(
			java.util.List<SysUser> value) {
		this.sysUsers = value;
	}

	public java.lang.Boolean getIsar() {
		return isar;
	}

	public void setIsar(java.lang.Boolean isar) {
		this.isar = isar;
	}

	public java.lang.Boolean getIsap() {
		return isap;
	}

	public void setIsap(java.lang.Boolean isap) {
		this.isap = isap;
	}

	public java.lang.Boolean getIscustomer() {
		return iscustomer;
	}

	public void setIscustomer(java.lang.Boolean iscustomer) {
		this.iscustomer = iscustomer;
	}

	public java.lang.Boolean getIschannel() {
		return ischannel;
	}

	public void setIschannel(java.lang.Boolean ischannel) {
		this.ischannel = ischannel;
	}

	public java.lang.Boolean getIsdelivery() {
		return isdelivery;
	}

	public void setIsdelivery(java.lang.Boolean isdelivery) {
		this.isdelivery = isdelivery;
	}

	public java.lang.Boolean getIsofficial() {
		return isofficial;
	}

	public void setIsofficial(java.lang.Boolean isofficial) {
		this.isofficial = isofficial;
	}

	public java.lang.Boolean getIsexpress() {
		return isexpress;
	}

	public void setIsexpress(java.lang.Boolean isexpress) {
		this.isexpress = isexpress;
	}

	public java.lang.Boolean getIssupplier() {
		return issupplier;
	}

	public void setIssupplier(java.lang.Boolean issupplier) {
		this.issupplier = issupplier;
	}

	public java.lang.Boolean getIscooperative() {
		return iscooperative;
	}

	public void setIscooperative(java.lang.Boolean iscooperative) {
		this.iscooperative = iscooperative;
	}

	public java.lang.Boolean getIsshipping() {
		return isshipping;
	}

	public void setIsshipping(java.lang.Boolean isshipping) {
		this.isshipping = isshipping;
	}

	public java.lang.String getPaytype() {
		return paytype;
	}

	public void setPaytype(java.lang.String paytype) {
		this.paytype = paytype;
	}

	public java.lang.Boolean getIscarrier() {
		return iscarrier;
	}

	public java.lang.Boolean getIsagent() {
		return isagent;
	}

	public void setIscarrier(java.lang.Boolean iscarrier) {
		this.iscarrier = iscarrier;
	}

	public void setIsagent(java.lang.Boolean isagent) {
		this.isagent = isagent;
	}

	public java.lang.Boolean getIstruck() {
		return istruck;
	}

	public void setIstruck(java.lang.Boolean istruck) {
		this.istruck = istruck;
	}

	public java.lang.Boolean getIscustom() {
		return iscustom;
	}

	public void setIscustom(java.lang.Boolean iscustom) {
		this.iscustom = iscustom;
	}

	public java.lang.Boolean getIshipper() {
		return ishipper;
	}

	public void setIshipper(java.lang.Boolean ishipper) {
		this.ishipper = ishipper;
	}

	public java.lang.Boolean getIsconsignee() {
		return isconsignee;
	}

	public void setIsconsignee(java.lang.Boolean isconsignee) {
		this.isconsignee = isconsignee;
	}

	public java.lang.String getBillinfo() {
		return billinfo;
	}
	
	

	public java.lang.Boolean getIsfcl() {
		return isfcl;
	}

	public void setIsfcl(java.lang.Boolean isfcl) {
		this.isfcl = isfcl;
	}

	public java.lang.Boolean getIsddp() {
		return isddp;
	}

	public void setIsddp(java.lang.Boolean isddp) {
		this.isddp = isddp;
	}

	public java.lang.Boolean getIslcl() {
		return islcl;
	}

	public void setIslcl(java.lang.Boolean islcl) {
		this.islcl = islcl;
	}

	public java.lang.Boolean getIsair() {
		return isair;
	}

	public void setIsair(java.lang.Boolean isair) {
		this.isair = isair;
	}

	public void setBillinfo(java.lang.String billinfo) {
		this.billinfo = billinfo;
	}

	public java.lang.String getPhone1() {
		return phone1;
	}

	public void setPhone1(java.lang.String phone1) {
		this.phone1 = phone1;
	}

	public java.lang.String getPhone2() {
		return phone2;
	}

	public void setPhone2(java.lang.String phone2) {
		this.phone2 = phone2;
	}

	public java.lang.String getPortdesc() {
		return portdesc;
	}

	public void setPortdesc(java.lang.String portdesc) {
		this.portdesc = portdesc;
	}

	public java.lang.Boolean getIsagentdes() {
		return isagentdes;
	}

	public void setIsagentdes(java.lang.Boolean isagentdes) {
		this.isagentdes = isagentdes;
	}

	public java.lang.Boolean getIshare() {
		return ishare;
	}

	public void setIshare(java.lang.Boolean ishare) {
		this.ishare = ishare;
	}

	public java.lang.String getAbbcode() {
		return abbcode;
	}

	public void setAbbcode(java.lang.String abbcode) {
		this.abbcode = abbcode;
	}

	public java.lang.Boolean getIsfactory() {
		return isfactory;
	}

	public void setIsfactory(java.lang.Boolean isfactory) {
		this.isfactory = isfactory;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.String getCheckter() {
		return checkter;
	}

	public void setCheckter(java.lang.String checkter) {
		this.checkter = checkter;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public java.lang.Boolean getIsairline() {
		return isairline;
	}

	public void setIsairline(java.lang.Boolean isairline) {
		this.isairline = isairline;
	}
	
	/**
	 *@generated
	 */
	public java.lang.Integer getDaysar() {
		return this.daysar;
	}

	/**
	 *@generated
	 */
	public void setDaysar(java.lang.Integer value) {
		this.daysar = value;
	}
	
	public java.lang.Double getAmtowe() {
		return amtowe;
	}

	public void setAmtowe(java.lang.Double amtowe) {
		this.amtowe = amtowe;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.Boolean getIsalloworder() {
		return isalloworder;
	}

	public void setIsalloworder(java.lang.Boolean isalloworder) {
		this.isalloworder = isalloworder;
	}
	
	public java.lang.String getTradeway() {
		return tradeway;
	}

	public void setTradeway(java.lang.String tradeway) {
		this.tradeway = tradeway;
	}

	public java.lang.String getInvoicetitle() {
		return invoicetitle;
	}

	public void setInvoicetitle(java.lang.String invoicetitle) {
		this.invoicetitle = invoicetitle;
	}

	public java.lang.String getTaxid() {
		return taxid;
	}

	public void setTaxid(java.lang.String taxid) {
		this.taxid = taxid;
	}

	public java.lang.String getLicno() {
		return licno;
	}

	public void setLicno(java.lang.String licno) {
		this.licno = licno;
	}

	public java.lang.String getImpexp() {
		return impexp;
	}

	public void setImpexp(java.lang.String impexp) {
		this.impexp = impexp;
	}

	public java.lang.String getSop() {
		return sop;
	}

	public void setSop(java.lang.String sop) {
		this.sop = sop;
	}

	public java.lang.String getSrctype() {
		return srctype;
	}

	public void setSrctype(java.lang.String srctype) {
		this.srctype = srctype;
	}

	public java.lang.Boolean getIsclc() {
		return isclc;
	}

	public void setIsclc(java.lang.Boolean isclc) {
		this.isclc = isclc;
	}

	public java.lang.String getDaystype() {
		return daystype;
	}

	public void setDaystype(java.lang.String daystype) {
		this.daystype = daystype;
	}

	public java.lang.String getPkidRemote() {
		return pkidRemote;
	}

	public void setPkidRemote(java.lang.String pkidRemote) {
		this.pkidRemote = pkidRemote;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.Integer getDatear() {
		return datear;
	}

	public void setDatear(java.lang.Integer datear) {
		this.datear = datear;
	}

	public java.lang.String getPayremarks() {
		return payremarks;
	}

	public void setPayremarks(java.lang.String payremarks) {
		this.payremarks = payremarks;
	}

	public java.lang.String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(java.lang.String customertype) {
		this.customertype = customertype;
	}

	public java.lang.Boolean getIsactset() {
		return isactset;
	}

	public void setIsactset(java.lang.Boolean isactset) {
		this.isactset = isactset;
	}

	public java.lang.Long getCorpidlink() {
		return corpidlink;
	}

	public void setCorpidlink(java.lang.Long corpidlink) {
		this.corpidlink = corpidlink;
	}

	public java.lang.String getSocialcreditno() {
		return socialcreditno;
	}

	public void setSocialcreditno(java.lang.String socialcreditno) {
		this.socialcreditno = socialcreditno;
	}

	public java.lang.Boolean getIstrain() {
		return istrain;
	}

	public void setIstrain(java.lang.Boolean istrain) {
		this.istrain = istrain;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.lang.String getAirlineno() {
		return airlineno;
	}

	public void setAirlineno(java.lang.String airlineno) {
		this.airlineno = airlineno;
	}

	public java.lang.String getAirlinecode() {
		return airlinecode;
	}

	public void setAirlinecode(java.lang.String airlinecode) {
		this.airlinecode = airlinecode;
	}

	public java.lang.String getBookinginfo() {
		return bookinginfo;
	}

	public void setBookinginfo(java.lang.String bookinginfo) {
		this.bookinginfo = bookinginfo;
	}

	public java.lang.String getHandinfo() {
		return handinfo;
	}

	public void setHandinfo(java.lang.String handinfo) {
		this.handinfo = handinfo;
	}

	public Boolean getIscommerce() {
		return iscommerce;
	}

	public void setIscommerce(Boolean iscommerce) {
		this.iscommerce = iscommerce;
	}

	@Override
	public String toString() {
		String toStringStr = "SysCorporation{" +
				"id=" + id +
				", code='" + code + '\'' +
				", helpcode='" + helpcode + '\'' +
				", billinfo='" + billinfo + '\'' +
				", namec='" + namec + '\'' +
				", namee='" + namee + '\'' +
				", abbr='" + abbr + '\'' +
				", abbcode='" + abbcode + '\'' +
				", addressc='" + addressc + '\'' +
				", addresse='" + addresse + '\'' +
				", tel1='" + tel1 + '\'' +
				", tel2='" + tel2 + '\'' +
				", phone1='" + phone1 + '\'' +
				", phone2='" + phone2 + '\'' +
				", fax1='" + fax1 + '\'' +
				", fax2='" + fax2 + '\'' +
				", email1='" + email1 + '\'' +
				", email2='" + email2 + '\'' +
				", homepage='" + homepage + '\'' +
				", contact='" + contact + '\'' +
				", remarks='" + remarks + '\'' +
				", srctype='" + srctype + '\'' +
				", isar=" + isar +
				", isap=" + isap +
				", iscustomer=" + iscustomer +
				", isairline=" + isairline +
				", iscarrier=" + iscarrier +
				", isagent=" + isagent +
				", iscustom=" + iscustom +
				", isclc=" + isclc +
				", iscommerce=" + iscommerce +
				", isfcl=" + isfcl +
				", isagentdes=" + isagentdes +
				", isddp=" + isddp +
				", islcl=" + islcl +
				", isair=" + isair +
				", istrain=" + istrain +
				", amtowe=" + amtowe +
				", currency='" + currency + '\'' +
				", isalloworder=" + isalloworder +
				", ischannel=" + ischannel +
				", istruck=" + istruck +
				", iswarehouse=" + iswarehouse +
				", ishare=" + ishare +
				", isfactory=" + isfactory +
				", ischeck=" + ischeck +
				", checkter='" + checkter + '\'' +
				", checktime=" + checktime +
				", sop='" + sop + '\'' +
				", daystype='" + daystype + '\'' +
				", pkidRemote='" + pkidRemote + '\'' +
				", corpid=" + corpid +
				", customertype='" + customertype + '\'' +
				", isactset=" + isactset +
				", corpidlink=" + corpidlink +
				", billnoprefix='" + billnoprefix + '\'' +
				", isdelivery=" + isdelivery +
				", isshipping=" + isshipping +
				", salesid=" + salesid +
				", daysar=" + daysar +
				", oweamount=" + oweamount +
				", other1='" + other1 + '\'' +
				", inputer='" + inputer + '\'' +
				", inputtime=" + inputtime +
				", updater='" + updater + '\'' +
				", paytype='" + paytype + '\'' +
				", updatetime=" + updatetime +
				", level='" + level + '\'' +
				", portdesc='" + portdesc + '\'' +
				", invoicetitle='" + invoicetitle + '\'' +
				", classify='" + classify + '\'' +
				", isofficial=" + isofficial +
				", isexpress=" + isexpress +
				", issupplier=" + issupplier +
				", iscooperative=" + iscooperative +
				", ishipper=" + ishipper +
				", isconsignee=" + isconsignee +
				", parentid=" + parentid +
				", isdelete=" + isdelete +
				", basecurrency='" + basecurrency + '\'' +
				", tradeway='" + tradeway + '\'' +
				", taxid='" + taxid + '\'' +
				", licno='" + licno + '\'' +
				", apikey='" + apikey + '\'' +
				", member='" + member + '\'' +
				", memberamt='" + memberamt + '\'' +
				", membercode='" + membercode + '\'' +
				", impexp='" + impexp + '\'' +
				", datear=" + datear +
				", payremarks='" + payremarks + '\'' +
				", socialcreditno='" + socialcreditno + '\'' +
				", country='" + country + '\'' +
				", airlineno='" + airlineno + '\'' +
				", airlinecode='" + airlinecode + '\'' +
				", bookinginfo='" + bookinginfo + '\'' +
				", handinfo='" + handinfo + '\'' +
				", namecqry='" + namecqry + '\'' +
				'}';
		return toStringStr.replaceAll("'","''") ;
	}
}
