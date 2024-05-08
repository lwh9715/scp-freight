package com.scp.model.commerce;

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
 * 电商订单表
 *
 * @author CIMC
 */

@Table(name = "bus_commerce")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class BusCommerce implements java.io.Serializable {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;
    /**
     * 工作单关联ID
     */
    @Column(name = "jobid")
    private java.lang.Long jobid;
    /**
     * 工作单号
     */
    @Column(name = "nos")
    private String nos;
    /**
     * PO号
     */
    @Column(name = "ponum")
    private String ponum;
    /**
     * Reference Id
     */
    @Column(name = "fbaref")
    private String fbaref;
    /**
     * Shipment Id
     */
    @Column(name = "shipmentid")
    private String shipmentid;
    /**
     * SO/入仓号
     */
    @Column(name = "sonum")
    private String sonum;
    /**
     * 转单号
     */
    @Column(name = "mblnum")
    private String mblnum;
    /**
     * 离港日期
     */
    @Column(name = "etd")
    private Date etd;
    /**
     * 到港日期
     */
    @Column(name = "eta")
    private Date eta;
    /**
     * 货好/入仓日期
     */
    @Column(name = "wmsindate")
    private Date wmsindate;
    /**
     * 船名
     */
    @Column(name = "vsl")
    private String vsl;
    /**
     * 航次
     */
    @Column(name = "voy")
    private String voy;
    /**
     * 航班
     */
    @Column(name = "flight")
    private String flight;
    /**
     * 货物类型
     */
    @Column(name = "cargotype")
    private String cargotype;

    /**
     * 计费重
     */
    @Column(name = "chargeweight")
    private String chargeweight;
    /**
     * 材积换算系数
     */
    @Column(name = "bkdivvolby")
    private Integer bkdivvolby;
    /**
     * 材积重
     */
    @Column(name = "vw")
    private Integer vw;
    /**
     * 分泡(%)
     */
    @Column(name = "volumeshare")
    private Integer volumeshare;
    /**
     * 预报件数
     */
    @Column(name = "bkpkgnum")
    private Integer bkpkgnum;

    /**
     * 预报重量
     */
    @Column(name = "bkgw")
    private Integer bkgw;

    /**
     * 预报体积
     */
    @Column(name = "bkvol")
    private Integer bkvol;

    /**
     * 货物品名
     */
    @Column(name = "cargoname")
    private String cargoname;
    /**
     * 商品编码
     */
    @Column(name = "hscode")
    private String hscode;
    /**
     * 商品数量
     */
    @Column(name = "pieces")
    private String pieces;
    /**
     * 贸易条款
     */
    @Column(name = "tradeterm")
    private String tradeterm;
    /**
     * 申报货值
     */
    @Column(name = "cargovalue")
    private String cargovalue;
    /**
     * 对账日期
     */
    @Column(name = "billdate")
    private Date billdate;
    /**
     * 预计派送日期
     */
    @Column(name = "deliverydate")
    private Date deliverydate;
    /**
     * 付款方式
     */
    @Column(name = "freightterm")
    private String freightterm;
    /**
     * 服务条款
     */
    @Column(name = "serviceterm")
    private String serviceterm;
    /**
     * 参考号
     */
    @Column(name = "refnum")
    private String refnum;
    /**
     * CTN/MAWB#
     */
    @Column(name = "sayctns")
    private String sayctns;

    /**
     * 唛头
     */
    @Column(name = "mark")
    private String mark;
    /**
     * 货物描述
     */
    @Column(name = "goodsdiscription")
    private String goodsdiscription;
    /**
     * 送货地址
     */
    @Column(name = "deliverybl")
    private String deliverybl;
    /**
     * 发货人
     */
    @Column(name = "shippername")
    private String shippername;
    /**
     * 发货人详情
     */
    @Column(name = "shipperbl")
    private String shipperbl;
    /**
     * 收货人
     */
    @Column(name = "cneename")
    private String cneename;
    /**
     * 收货人详情
     */
    @Column(name = "cneebl")
    private String cneebl;
    /**
     * 通知人
     */
    @Column(name = "notifyname")
    private String notifyname;
    /**
     * 通知人详情
     */
    @Column(name = "notifybl")
    private String notifybl;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getJobid() {
        return jobid;
    }

    public void setJobid(Long jobid) {
        this.jobid = jobid;
    }

    public String getNos() {
        return nos;
    }

    public void setNos(String nos) {
        this.nos = nos;
    }

    public String getPonum() {
        return ponum;
    }

    public void setPonum(String ponum) {
        this.ponum = ponum;
    }

    public String getFbaref() {
        return fbaref;
    }

    public void setFbaref(String fbaref) {
        this.fbaref = fbaref;
    }

    public String getShipmentid() {
        return shipmentid;
    }

    public void setShipmentid(String shipmentid) {
        this.shipmentid = shipmentid;
    }

    public String getSonum() {
        return sonum;
    }

    public void setSonum(String sonum) {
        this.sonum = sonum;
    }

    public String getMblnum() {
        return mblnum;
    }

    public void setMblnum(String mblnum) {
        this.mblnum = mblnum;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getWmsindate() {
        return wmsindate;
    }

    public void setWmsindate(Date wmsindate) {
        this.wmsindate = wmsindate;
    }

    public String getVsl() {
        return vsl;
    }

    public void setVsl(String vsl) {
        this.vsl = vsl;
    }

    public String getVoy() {
        return voy;
    }

    public void setVoy(String voy) {
        this.voy = voy;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getCargotype() {
        return cargotype;
    }

    public void setCargotype(String cargotype) {
        this.cargotype = cargotype;
    }

    public String getChargeweight() {
        return chargeweight;
    }

    public void setChargeweight(String chargeweight) {
        this.chargeweight = chargeweight;
    }

    public Integer getBkdivvolby() {
        return bkdivvolby;
    }

    public void setBkdivvolby(Integer bkdivvolby) {
        this.bkdivvolby = bkdivvolby;
    }

    public Integer getVw() {
        return vw;
    }

    public void setVw(Integer vw) {
        this.vw = vw;
    }

    public Integer getVolumeshare() {
        return volumeshare;
    }

    public void setVolumeshare(Integer volumeshare) {
        this.volumeshare = volumeshare;
    }

    public Integer getBkpkgnum() {
        return bkpkgnum;
    }

    public void setBkpkgnum(Integer bkpkgnum) {
        this.bkpkgnum = bkpkgnum;
    }

    public Integer getBkgw() {
        return bkgw;
    }

    public void setBkgw(Integer bkgw) {
        this.bkgw = bkgw;
    }

    public Integer getBkvol() {
        return bkvol;
    }

    public void setBkvol(Integer bkvol) {
        this.bkvol = bkvol;
    }

    public String getCargoname() {
        return cargoname;
    }

    public void setCargoname(String cargoname) {
        this.cargoname = cargoname;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getTradeterm() {
        return tradeterm;
    }

    public void setTradeterm(String tradeterm) {
        this.tradeterm = tradeterm;
    }

    public String getCargovalue() {
        return cargovalue;
    }

    public void setCargovalue(String cargovalue) {
        this.cargovalue = cargovalue;
    }

    public Date getBilldate() {
        return billdate;
    }

    public void setBilldate(Date billdate) {
        this.billdate = billdate;
    }

    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getFreightterm() {
        return freightterm;
    }

    public void setFreightterm(String freightterm) {
        this.freightterm = freightterm;
    }

    public String getServiceterm() {
        return serviceterm;
    }

    public void setServiceterm(String serviceterm) {
        this.serviceterm = serviceterm;
    }

    public String getRefnum() {
        return refnum;
    }

    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

    public String getSayctns() {
        return sayctns;
    }

    public void setSayctns(String sayctns) {
        this.sayctns = sayctns;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getGoodsdiscription() {
        return goodsdiscription;
    }

    public void setGoodsdiscription(String goodsdiscription) {
        this.goodsdiscription = goodsdiscription;
    }

    public String getDeliverybl() {
        return deliverybl;
    }

    public void setDeliverybl(String deliverybl) {
        this.deliverybl = deliverybl;
    }

    public String getShippername() {
        return shippername;
    }

    public void setShippername(String shippername) {
        this.shippername = shippername;
    }

    public String getShipperbl() {
        return shipperbl;
    }

    public void setShipperbl(String shipperbl) {
        this.shipperbl = shipperbl;
    }

    public String getCneename() {
        return cneename;
    }

    public void setCneename(String cneename) {
        this.cneename = cneename;
    }

    public String getCneebl() {
        return cneebl;
    }

    public void setCneebl(String cneebl) {
        this.cneebl = cneebl;
    }

    public String getNotifyname() {
        return notifyname;
    }

    public void setNotifyname(String notifyname) {
        this.notifyname = notifyname;
    }

    public String getNotifybl() {
        return notifybl;
    }

    public void setNotifybl(String notifybl) {
        this.notifybl = notifybl;
    }
}
