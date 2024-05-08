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
@Table(name = "dat_hscode")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native" , parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class Hscode implements java.io.Serializable {


    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    @Column(name = "hscode", length = 30)
    private java.lang.String hscode;


    @Column(name = "item", length = 1000)
    private java.lang.String item;


    @Column(name = "remark", length = 1000)
    private java.lang.String remark;


    @Column(name = "isdelete")
    private java.lang.Boolean isdelete;

    @Column(name = "inputer", length = 100)
    private java.lang.String inputer;

    @Column(name = "inputtime", length = 29)
    private java.util.Date inputtime;


    @Column(name = "updater", length = 100)
    private java.lang.String updater;


    @Column(name = "updatetime", length = 29)
    private java.util.Date updatetime;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Boolean isdelete) {
        this.isdelete = isdelete;
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
}