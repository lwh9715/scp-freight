package com.scp.model.commerce;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 货物混装表
 *
 * @author CIMC
 */
@Table(name = "bus_commerce_cargo_mixed")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_id")})
public class BusCommerceCargoMixed {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator = "idStrategy")
    private long id;

    /**
     * 货物明细表关联ID
     */
    @Column(name = "ctnsid")
    private String ctnsid;

    /**
     * 产品名
     */
    @Column(name = "cargoname")
    private String cargoname;
    /**
     * 产品英文名
     */
    @Column(name = "cargonameen")
    private String cargonameen;
    /**
     * 单价
     */
    @Column(name = "unitprice")
    private Double unitprice;
    /**
     * 商品数量
     */
    @Column(name = "unitprice")
    private Double pieces;
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
     * 关税/率
     */
    @Column(name = "dutyrate")
    private Double dutyrate;
    /**
     * 关税
     */
    @Column(name = "dutyamt")
    private Double dutyamt;
    /**
     * 毛重
     */
    @Column(name = "gwttl")
    private Double gwttl;
    /**
     * 体积
     */
    @Column(name = "volttl")
    private Double volttl;
    /**
     * 材质
     */
    @Column(name = "material")
    private String material;
    /**
     * 用途
     */
    @Column(name = "useage")
    private String useage;
    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;
    /**
     * 型号
     */
    @Column(name = "model")
    private String model;
    /**
     * 进口国
     */
    @Column(name = "importcountrycode")
    private String importcountrycode;

    /**
     * 货物明细表关联ID
     */
    public String getCtnsid() {
        return this.ctnsid;
    }

    /**
     * 货物明细表关联ID
     */
    public void setCtnsid(String ctnsid) {
        this.ctnsid = ctnsid;
    }

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
    public String getCargonameen() {
        return this.cargonameen;
    }

    /**
     * 产品英文名
     */
    public void setCargonameen(String cargonameen) {
        this.cargonameen = cargonameen;
    }

    /**
     * 单价
     */
    public Double getUnitprice() {
        return this.unitprice;
    }

    /**
     * 单价
     */
    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    /**
     * 商品数量
     */
    public Double getPieces() {
        return this.pieces;
    }

    /**
     * 商品数量
     */
    public void setPieces(Double pieces) {
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
     * 关税/率
     */
    public Double getDutyrate() {
        return this.dutyrate;
    }

    /**
     * 关税/率
     */
    public void setDutyrate(Double dutyrate) {
        this.dutyrate = dutyrate;
    }

    /**
     * 关税
     */
    public Double getDutyamt() {
        return this.dutyamt;
    }

    /**
     * 关税
     */
    public void setDutyamt(Double dutyamt) {
        this.dutyamt = dutyamt;
    }

    /**
     * 毛重
     */
    public Double getGwttl() {
        return this.gwttl;
    }

    /**
     * 毛重
     */
    public void setGwttl(Double gwttl) {
        this.gwttl = gwttl;
    }

    /**
     * 体积
     */
    public Double getVolttl() {
        return this.volttl;
    }

    /**
     * 体积
     */
    public void setVolttl(Double volttl) {
        this.volttl = volttl;
    }

    /**
     * 材质
     */
    public String getMaterial() {
        return this.material;
    }

    /**
     * 材质
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * 用途
     */
    public String getUseage() {
        return this.useage;
    }

    /**
     * 用途
     */
    public void setUseage(String useage) {
        this.useage = useage;
    }

    /**
     * 品牌
     */
    public String getBrand() {
        return this.brand;
    }

    /**
     * 品牌
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 型号
     */
    public String getModel() {
        return this.model;
    }

    /**
     * 型号
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 进口国
     */
    public String getImportcountrycode() {
        return this.importcountrycode;
    }

    /**
     * 进口国
     */
    public void setImportcountrycode(String importcountrycode) {
        this.importcountrycode = importcountrycode;
    }
}
