package com.scp.model.sys;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sys_contract")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class SysContract implements java.io.Serializable {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    @Column(name = "customerid")
    private Long customerid;

    @Column(name = "contnamec", length = 255)
    private String contnamec;

    @Column(name = "contnamee", length = 255)
    private String contnamee;

    @Column(name = "receivedate", length = 13)
    private java.util.Date receivedate;

    @Column(name = "signdate", length = 13)
    private java.util.Date signdate;

    @Column(name = "validstartdate", length = 13)
    private java.util.Date validstartdate;

    @Column(name = "validenddate", length = 13)
    private java.util.Date validenddate;

    @Column(name = "valid", length = 100)
    private Integer valid;

    @Column(name = "settletype", length = 3)
    private String settletype;

    @Column(name = "remarks", length = 300)
    private String remarks;

    @Column(name = "inputer")
    private String inputer;

    @Column(name = "inputtime", length = 255)
    private Date inputtime;

    @Column(name = "updater", length = 29)
    private String updater;

    @Column(name = "updatetime", length = 255)
    private Date updatetime;

    @Column(name = "isdelete")
    private Boolean isdelete;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public String getContnamec() {
        return contnamec;
    }

    public void setContnamec(String contnamec) {
        this.contnamec = contnamec;
    }

    public String getContnamee() {
        return contnamee;
    }

    public void setContnamee(String contnamee) {
        this.contnamee = contnamee;
    }

    public Date getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(Date receivedate) {
        this.receivedate = receivedate;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public Date getValidstartdate() {
        return validstartdate;
    }

    public void setValidstartdate(Date validstartdate) {
        this.validstartdate = validstartdate;
    }

    public Date getValidenddate() {
        return validenddate;
    }

    public void setValidenddate(Date validenddate) {
        this.validenddate = validenddate;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getSettletype() {
        return settletype;
    }

    public void setSettletype(String settletype) {
        this.settletype = settletype;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Boolean getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Boolean isdelete) {
        this.isdelete = isdelete;
    }
}
