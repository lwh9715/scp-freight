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
@Table(name = "edi_inttra_report")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
        , parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class Ediinttrareport implements java.io.Serializable {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    @Column(name = "editype", length = 50)
    private String editype;

    @Column(name = "sono", length = 100)
    private String sono;

    @Column(name = "filename", length = 100)
    private String filename;

    @Column(name = "editext", length = 100)
    private String editext;
    
    @Column(name = "inputtime", length = 29)
    private java.util.Date inputtime;

    @Column(name = "mblno", length = 100)
    private String mblno;


    public String getMblno() {
        return mblno;
    }

    public void setMblno(String mblno) {
        this.mblno = mblno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEditype() {
        return editype;
    }

    public void setEditype(String editype) {
        this.editype = editype;
    }

    public String getSono() {
        return sono;
    }

    public void setSono(String sono) {
        this.sono = sono;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getEditext() {
        return editext;
    }

    public void setEditext(String editext) {
        this.editext = editext;
    }
    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }
}