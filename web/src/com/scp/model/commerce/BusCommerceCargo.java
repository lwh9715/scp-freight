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
 * 货物明细表
 *
 * @author CIMC
 */
@Table(name = "bus_commerce_cargo")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class BusCommerceCargo {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;
    /**
     * 产品货物关联ID
     */
    @Column(name = "jobid")
    private String jobid;
    /**
     * 产品名
     */
    @Column(name = "cargoname")
    private String cargoname;
    /**
     * 产品英文名
     */
    @Column(name = "cargonameen")
    private Date cargonameen;
    /**
     * 唛头
     */
    @Column(name = "mark")
    private String mark;
    /**
     * SKU
     */
    @Column(name = "sku")
    private Date sku;
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
     * 包装件数
     */
    @Column(name = "pkgnum")
    private String pkgnum;
    /**
     * 包装类型
     */
    @Column(name = "pkgtype")
    private String pkgtype;
    /**
     * 托盘数
     */
    @Column(name = "pallets")
    private String pallets;
    /**
     * 柜型
     */
    @Column(name = "ctntype")
    private String ctntype;
    /**
     * 毛重
     */
    @Column(name = "gw")
    private String gw;
    /**
     * 总毛重
     */
    @Column(name = "gwttl")
    private String gwttl;
    /**
     * 长
     */
    @Column(name = "length")
    private String length;
    /**
     * 宽
     */
    @Column(name = "width")
    private String width;
    /**
     * 高
     */
    @Column(name = "height")
    private String height;
    /**
     * 体积
     */
    @Column(name = "vol")
    private String vol;
    /**
     * 总体积
     */
    @Column(name = "volttl")
    private String volttl;
    /**
     * 产品链接
     */
    @Column(name = "producturl")
    private String producturl;
    /**
     * 单价
     */
    @Column(name = "unitprice")
    private String unitprice;
    /**
     * 商品数量
     */
    @Column(name = "pieces")
    private String pieces;
    /**
     * 总价
     */
    @Column(name = "totalprice")
    private String totalprice;
    /**
     * 商品编码
     */
    @Column(name = "hscode")
    private String hscode;
    /**
     * 关税金额
     */
    @Column(name = "dutyamt")
    private String dutyamt;
    /**
     * 产品数据库
     */
    @Column(name = "idcode")
    private String idcode;
    /**
     * 材质（中文）
     */
    @Column(name = "material")
    private String material;
    /**
     * 材质（英文）
     */
    @Column(name = "materialen")
    private String materialen;
    /**
     * 用途（中文）
     */
    @Column(name = "useage")
    private String useage;
    /**
     * 用途（英文）
     */
    @Column(name = "useageen")
    private String useageen;
    /**
     * 品牌 （中文）
     */
    @Column(name = "brand")
    private String brand;
    /**
     * 品牌 （英文）
     */
    @Column(name = "branden")
    private String branden;
    /**
     * 型号（中文）
     */
    @Column(name = "model")
    private String model;
    /**
     * 型号（英文）
     */
    @Column(name = "modelen")
    private String modelen;
    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 唯一值
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 唯一值
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 产品货物关联ID
     */
    public String getJobid() {
        return this.jobid;
    }

    /**
     * 产品货物关联ID
     */
    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    /**
     * 产品名
     */
    public String getCargoname() {
        return this.cargoname;
    }

    /**
     * 产品名
     */
    public void setCargoname(String cargoname) {
        this.cargoname = cargoname;
    }

    /**
     * 产品英文名
     */
    public Date getCargonameen() {
        return this.cargonameen;
    }

    /**
     * 产品英文名
     */
    public void setCargonameen(Date cargonameen) {
        this.cargonameen = cargonameen;
    }

    /**
     * 唛头
     */
    public String getMark() {
        return this.mark;
    }

    /**
     * 唛头
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * SKU
     */
    public Date getSku() {
        return this.sku;
    }

    /**
     * SKU
     */
    public void setSku(Date sku) {
        this.sku = sku;
    }

    /**
     * Reference Id
     */
    public String getFbaref() {
        return this.fbaref;
    }

    /**
     * Reference Id
     */
    public void setFbaref(String fbaref) {
        this.fbaref = fbaref;
    }

    /**
     * Shipment Id
     */
    public String getShipmentid() {
        return this.shipmentid;
    }

    /**
     * Shipment Id
     */
    public void setShipmentid(String shipmentid) {
        this.shipmentid = shipmentid;
    }

    /**
     * 包装件数
     */
    public String getPkgnum() {
        return this.pkgnum;
    }

    /**
     * 包装件数
     */
    public void setPkgnum(String pkgnum) {
        this.pkgnum = pkgnum;
    }

    /**
     * 包装类型
     */
    public String getPkgtype() {
        return this.pkgtype;
    }

    /**
     * 包装类型
     */
    public void setPkgtype(String pkgtype) {
        this.pkgtype = pkgtype;
    }

    /**
     * 托盘数
     */
    public String getPallets() {
        return this.pallets;
    }

    /**
     * 托盘数
     */
    public void setPallets(String pallets) {
        this.pallets = pallets;
    }

    /**
     * 柜型
     */
    public String getCtntype() {
        return this.ctntype;
    }

    /**
     * 柜型
     */
    public void setCtntype(String ctntype) {
        this.ctntype = ctntype;
    }

    /**
     * 毛重
     */
    public String getGw() {
        return this.gw;
    }

    /**
     * 毛重
     */
    public void setGw(String gw) {
        this.gw = gw;
    }

    /**
     * 总毛重
     */
    public String getGwttl() {
        return this.gwttl;
    }

    /**
     * 总毛重
     */
    public void setGwttl(String gwttl) {
        this.gwttl = gwttl;
    }

    /**
     * 长
     */
    public String getLength() {
        return this.length;
    }

    /**
     * 长
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * 宽
     */
    public String getWidth() {
        return this.width;
    }

    /**
     * 宽
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * 高
     */
    public String getHeight() {
        return this.height;
    }

    /**
     * 高
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * 体积
     */
    public String getVol() {
        return this.vol;
    }

    /**
     * 体积
     */
    public void setVol(String vol) {
        this.vol = vol;
    }

    /**
     * 总体积
     */
    public String getVolttl() {
        return this.volttl;
    }

    /**
     * 总体积
     */
    public void setVolttl(String volttl) {
        this.volttl = volttl;
    }

    /**
     * 产品链接
     */
    public String getProducturl() {
        return this.producturl;
    }

    /**
     * 产品链接
     */
    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    /**
     * 单价
     */
    public String getUnitprice() {
        return this.unitprice;
    }

    /**
     * 单价
     */
    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    /**
     * 商品数量
     */
    public String getPieces() {
        return this.pieces;
    }

    /**
     * 商品数量
     */
    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    /**
     * 总价
     */
    public String getTotalprice() {
        return this.totalprice;
    }

    /**
     * 总价
     */
    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * 商品编码
     */
    public String getHscode() {
        return this.hscode;
    }

    /**
     * 商品编码
     */
    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    /**
     * 关税金额
     */
    public String getDutyamt() {
        return this.dutyamt;
    }

    /**
     * 关税金额
     */
    public void setDutyamt(String dutyamt) {
        this.dutyamt = dutyamt;
    }

    /**
     * 产品数据库
     */
    public String getIdcode() {
        return this.idcode;
    }

    /**
     * 产品数据库
     */
    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    /**
     * 材质（中文）
     */
    public String getMaterial() {
        return this.material;
    }

    /**
     * 材质（中文）
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * 材质（英文）
     */
    public String getMaterialen() {
        return this.materialen;
    }

    /**
     * 材质（英文）
     */
    public void setMaterialen(String materialen) {
        this.materialen = materialen;
    }

    /**
     * 用途（中文）
     */
    public String getUseage() {
        return this.useage;
    }

    /**
     * 用途（中文）
     */
    public void setUseage(String useage) {
        this.useage = useage;
    }

    /**
     * 用途（英文）
     */
    public String getUseageen() {
        return this.useageen;
    }

    /**
     * 用途（英文）
     */
    public void setUseageen(String useageen) {
        this.useageen = useageen;
    }

    /**
     * 品牌 （中文）
     */
    public String getBrand() {
        return this.brand;
    }

    /**
     * 品牌 （中文）
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 品牌 （英文）
     */
    public String getBranden() {
        return this.branden;
    }

    /**
     * 品牌 （英文）
     */
    public void setBranden(String branden) {
        this.branden = branden;
    }

    /**
     * 型号（中文）
     */
    public String getModel() {
        return this.model;
    }

    /**
     * 型号（中文）
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 型号（英文）
     */
    public String getModelen() {
        return this.modelen;
    }

    /**
     * 型号（英文）
     */
    public void setModelen(String modelen) {
        this.modelen = modelen;
    }

    /**
     * 备注
     */
    public String getRemarks() {
        return this.remarks;
    }

    /**
     * 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
