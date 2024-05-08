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
@Table(name = "dat_goodstrack")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
        , parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class DatGoodstrack implements java.io.Serializable {

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
    @Column(name = "code", length = 50)
    private String code;

    /**
     * @generated
     */
    @Column(name = "corpid", length = 30)
    private String corpid;

    /**
     * @generated
     */
    @Column(name = "namec", length = 300)
    private String namec;

    /**
     * @generated
     */
    @Column(name = "namee", length = 300)
    private String namee;

    /**
     * @generated
     */
    @Column(name = "content", length = 300)
    private String content;

    /**
     * @generated
     */
    @Column(name = "node", length = 100)
    private String node;

    /**
     * @generated
     */
    @Column(name = "inputer", length = 100)
    private String inputer;

    /**
     * @generated
     */
    @Column(name = "intputtime", length = 29)
    private Date intputtime;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
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

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    public Date getIntputtime() {
        return intputtime;
    }

    public void setIntputtime(Date intputtime) {
        this.intputtime = intputtime;
    }

    public Boolean getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Boolean isdelete) {
        this.isdelete = isdelete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}