package com.scp.model.api;

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
@Table(name = "api_data")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native" , parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class ApiData implements java.io.Serializable {


    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    @Column(name = "code", length = 30)
    private java.lang.String code;


    @Column(name = "namec", length = 1000)
    private java.lang.String namec;


    @Column(name = "namee", length = 1000)
    private java.lang.String namee;

    @Column(name = "srctype", length = 1000)
    private java.lang.String srctype;

    @Column(name = "maptype", length = 1000)
    private java.lang.String maptype;


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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNamec() {
        return namec;
    }

    public void setNamec(String namec) {
        this.namec = namec;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public String getSrctype() {
        return srctype;
    }

    public void setSrctype(String srctype) {
        this.srctype = srctype;
    }

    public String getMaptype() {
        return maptype;
    }

    public void setMaptype(String maptype) {
        this.maptype = maptype;
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