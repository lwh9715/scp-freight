package com.scp.model.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @generated
 */
@Table(name = "dat_address")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
        , parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class DatAddress implements java.io.Serializable {

    /**
     * @generated
     */
    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    /**
     * @generated
     */
    @Column(name = "companyname", length = 50)
    private String companyname;

    /**
     * @generated
     */
    @Column(name = "addressone", length = 300)
    private String addressone;

    /**
     * @generated
     */
    @Column(name = "addresstwo", length = 300)
    private String addresstwo;

    /**
     * @generated
     */
    @Column(name = "addressthree", length = 300)
    private String addressthree;

    /**
     * @generated
     */
    @Column(name = "code", length = 50)
    private String code;

    /**
     * @generated
     */
    @Column(name = "attn", length = 30)
    private String attn;

    /**
     * @generated
     */
    @Column(name = "postcode", length = 30)
    private String postcode;

    /**
     * @generated
     */
    @Column(name = "countrycode", length = 30)
    private String countrycode;


    /**
     * @generated
     */
    @Column(name = "province", length = 100)
    private String province;

    /**
     * @generated
     */
    @Column(name = "city", length = 100)
    private String city;

    /**
     * @generated
     */
    @Column(name = "tel", length = 100)
    private String tel;

    /**
     * @generated
     */
    @Column(name = "email", length = 29)
    private String email;

    /**
     * @generated
     */
    @Column(name = "zone", length = 29)
    private String zone;

    /**
     * @generated
     */
    @Column(name = "inputer", length = 100)
    private String inputer;

    /**
     * @generated
     */
    @Column(name = "inputtime", length = 29)
    private Date inputtime;

    /**
     * @generated
     */
    @Column(name = "isdelete")
    private Boolean isdelete;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getAddressone() {
        return addressone;
    }

    public void setAddressone(String addressone) {
        this.addressone = addressone;
    }

    public String getAddresstwo() {
        return addresstwo;
    }

    public void setAddresstwo(String addresstwo) {
        this.addresstwo = addresstwo;
    }

    public String getAddressthree() {
        return addressthree;
    }

    public void setAddressthree(String addressthree) {
        this.addressthree = addressthree;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public Boolean getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Boolean isdelete) {
        this.isdelete = isdelete;
    }
}