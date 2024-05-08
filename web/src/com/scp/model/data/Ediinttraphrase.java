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
@Table(name = "edi_inttra_phrase")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
        , parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class Ediinttraphrase implements java.io.Serializable {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    @Column(name = "segment", length = 50)
    private String segment;

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "desc_seg", length = 100)
    private String desc_seg;

    @Column(name = "desc_code", length = 100)
    private String desc_code;

    @Column(name = "srcfile", length = 100)
    private String srcfile;

    @Column(name = "tblname", length = 100)
    private String tblname;

    @Column(name = "colname", length = 100)
    private String colname;

    @Column(name = "inputer", length = 100)
    private String inputer;

    @Column(name = "inputtime", length = 29)
    private java.util.Date inputtime;

    @Column(name = "updater", length = 100)
    private String updater;

    @Column(name = "updatetime", length = 29)
    private java.util.Date updatetime;


    @Column(name = "socolname", length = 100)
    private String socolname;

    @Column(name = "socolrule", length = 100)
    private String socolrule;

    public String getSocolname() {
        return socolname;
    }

    public void setSocolname(String socolname) {
        this.socolname = socolname;
    }

    public String getSocolrule() {
        return socolrule;
    }

    public void setSocolrule(String socolrule) {
        this.socolrule = socolrule;
    }

    public String getColname() {
        return colname;
    }

    public void setColname(String colname) {
        this.colname = colname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc_seg() {
        return desc_seg;
    }

    public void setDesc_seg(String desc_seg) {
        this.desc_seg = desc_seg;
    }

    public String getDesc_code() {
        return desc_code;
    }

    public void setDesc_code(String desc_code) {
        this.desc_code = desc_code;
    }

    public String getSrcfile() {
        return srcfile;
    }

    public void setSrcfile(String srcfile) {
        this.srcfile = srcfile;
    }

    public String getTblname() {
        return tblname;
    }

    public void setTblname(String tblname) {
        this.tblname = tblname;
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