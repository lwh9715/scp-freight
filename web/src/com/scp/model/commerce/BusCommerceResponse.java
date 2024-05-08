package com.scp.model.commerce;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author CIMC
 */
public class BusCommerceResponse {

    /**
     * #customerid 客户id
     */
    private String customerid;
    /**
     * #PO 采购订单号
     */
    private String po;
    /**
     * #productName 产品渠道
     */
    private String productName;
    /**
     * #whCode FBA仓库代码
     */
    private String whCode;
    /**
     * #fbaRef Reference Id
     */
    private String fbaRef;
    /**
     * #shipmentId Shipment ID
     */
    private String shipmentId;
    /**
     * #cargoName 中文品名
     */
    private String cargoName;
    /**
     * #cargoEnName 英文品名
     */
    private String cargoEnName;
    /**
     * #piece PCS（商品数量）
     */
    private String piece;
    /**
     * #unitPrice USD（单价）
     */
    private String unitPrice;
    /**
     * #totalPrice USD（总价）
     */
    private String totalPrice;
    /**
     * #package 包装类型
     */
    private String packAge;
    /**
     * #pkgs 箱数
     */
    private String pkgs;
    /**
     * #dimension 外箱尺寸-长
     */
    private String length;
    /**
     * #dimension 外箱尺寸-宽
     */
    private String width;
    /**
     * #dimension 外箱尺寸-高
     */
    private String height;
    /**
     * #volTtl SIZE(总体积)
     */
    private String volTtl;
    /**
     * #grossWeight 单箱重
     */
    private String grossWeight;
    /**
     * #plGwTtl 总重
     */
    private String plGwTtl;
    /**
     * #hsCode HS CODE
     */
    private String hsCode;
    /**
     * #brand 品牌
     */
    private String brand;
    /**
     * #model 型号
     */
    private String model;
    /**
     * #material 材质
     */
    private String material;
    /**
     * #useAge 用途
     */
    private String useAge;
    /**
     * #essential 其他要素
     */
    private String essential;
    /**
     * #pic 图片
     */
    private String pic;
    /**
     * #remarks 特殊（带磁/带电）
     */
    private String remarks;
    /**
     * 录入人
     */
    private String inputer;
    /**
     * 工作单类型
     */
    private String jobtype;
    /**
     * 报关类型
     */
    private String declaretype;
    /**
     * 交税类型
     */
    private String paytaxestype;
    /**
     * 是否保险
     */
    private boolean fumigationrequired;
    /**
     * 保险货值及币种
     */
    private String beneficiary;
    /**
     * 送货地
     */
    private String deliverynamec;
    /**
     * 送货地址
     */
    private String deliverybl;
    /**
     * 收件人code
     */
    private String shiptopostcode;
    /**
     * 公司名
     */
    private String companyname;
    /**
     * 地址
     */
    private String bookingtoaddress;
    /**
     * 城市
     */
    private String bookingtocity;
    /**
     * 省份
     */
    private String bookingtofax;
    /**
     * 邮编
     */
    private String postcode;
    /**
     * 目的国
     */
    private String destcountrycode;
    /**
     * 电话
     */
    private String tel;


    //途曦模板
    public BusCommerceResponse(Row row, String usercode, String jobtype) {
        this.po = getCellValue(row.getCell(0));
        this.productName = getCellValue(row.getCell(1));
        this.whCode = getCellValue(row.getCell(2));
        this.fbaRef = getCellValue(row.getCell(3));
        this.shipmentId = getCellValue(row.getCell(4));
        this.cargoName = getCellValue(row.getCell(5));
        this.cargoEnName = getCellValue(row.getCell(6));
        this.piece = getCellValue(row.getCell(7));
        this.unitPrice = getCellValue(row.getCell(8));
        this.totalPrice = getCellValue(row.getCell(9));
        this.packAge = getCellValue(row.getCell(10));
        this.pkgs = getCellValue(row.getCell(11));
        this.length = getCellValue(row.getCell(12));
        this.width = getCellValue(row.getCell(13));
        this.height = getCellValue(row.getCell(14));
        this.volTtl = getCellValue(row.getCell(15));
        this.grossWeight = getCellValue(row.getCell(16));
        this.plGwTtl = getCellValue(row.getCell(17));
        this.hsCode = getCellValue(row.getCell(18));
        this.brand = getCellValue(row.getCell(19));
        this.model = getCellValue(row.getCell(20));
        this.material = getCellValue(row.getCell(21));
        this.useAge = getCellValue(row.getCell(22));
        this.essential = getCellValue(row.getCell(23));
        this.pic = getCellValue(row.getCell(24));
        this.remarks = getCellValue(row.getCell(25));
        this.inputer = usercode;
        this.jobtype = jobtype;
    }

    public BusCommerceResponse() {
    }

    public String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        String valueStr = "";
        switch (cell.getCellType()) {
            // 读取到的是公式
            case Cell.CELL_TYPE_FORMULA:
                try {
                    valueStr = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    valueStr = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case Cell.CELL_TYPE_NUMERIC:
                valueStr = cell.toString();
                break;
            case Cell.CELL_TYPE_STRING:
                valueStr = cell.toString();
                break;
        }
        return valueStr;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getFbaRef() {
        return fbaRef;
    }

    public void setFbaRef(String fbaRef) {
        this.fbaRef = fbaRef;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoEnName() {
        return cargoEnName;
    }

    public void setCargoEnName(String cargoEnName) {
        this.cargoEnName = cargoEnName;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPackAge() {
        return packAge;
    }

    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }

    public String getPkgs() {
        return pkgs;
    }

    public void setPkgs(String pkgs) {
        this.pkgs = pkgs;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVolTtl() {
        return volTtl;
    }

    public void setVolTtl(String volTtl) {
        this.volTtl = volTtl;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getPlGwTtl() {
        return plGwTtl;
    }

    public void setPlGwTtl(String plGwTtl) {
        this.plGwTtl = plGwTtl;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUseAge() {
        return useAge;
    }

    public void setUseAge(String useAge) {
        this.useAge = useAge;
    }

    public String getEssential() {
        return essential;
    }

    public void setEssential(String essential) {
        this.essential = essential;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getDeclaretype() {
        return declaretype;
    }

    public void setDeclaretype(String declaretype) {
        this.declaretype = declaretype;
    }

    public String getPaytaxestype() {
        return paytaxestype;
    }

    public void setPaytaxestype(String paytaxestype) {
        this.paytaxestype = paytaxestype;
    }

    public boolean isFumigationrequired() {
        return fumigationrequired;
    }

    public void setFumigationrequired(boolean fumigationrequired) {
        this.fumigationrequired = fumigationrequired;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getDeliverynamec() {
        return deliverynamec;
    }

    public void setDeliverynamec(String deliverynamec) {
        this.deliverynamec = deliverynamec;
    }

    public String getDeliverybl() {
        return deliverybl;
    }

    public void setDeliverybl(String deliverybl) {
        this.deliverybl = deliverybl;
    }

    public String getShiptopostcode() {
        return shiptopostcode;
    }

    public void setShiptopostcode(String shiptopostcode) {
        this.shiptopostcode = shiptopostcode;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getBookingtoaddress() {
        return bookingtoaddress;
    }

    public void setBookingtoaddress(String bookingtoaddress) {
        this.bookingtoaddress = bookingtoaddress;
    }

    public String getBookingtocity() {
        return bookingtocity;
    }

    public void setBookingtocity(String bookingtocity) {
        this.bookingtocity = bookingtocity;
    }

    public String getBookingtofax() {
        return bookingtofax;
    }

    public void setBookingtofax(String bookingtofax) {
        this.bookingtofax = bookingtofax;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDestcountrycode() {
        return destcountrycode;
    }

    public void setDestcountrycode(String destcountrycode) {
        this.destcountrycode = destcountrycode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "BusCommerceResponse{" +
                "po='" + po + '\'' +
                ", productName='" + productName + '\'' +
                ", whCode='" + whCode + '\'' +
                ", fbaRef='" + fbaRef + '\'' +
                ", shipmentId='" + shipmentId + '\'' +
                ", cargoName='" + cargoName + '\'' +
                ", cargoEnName='" + cargoEnName + '\'' +
                ", piece='" + piece + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", packAge='" + packAge + '\'' +
                ", pkgs='" + pkgs + '\'' +
                ", length='" + length + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", volTtl='" + volTtl + '\'' +
                ", grossWeight='" + grossWeight + '\'' +
                ", plGwTtl='" + plGwTtl + '\'' +
                ", hsCode='" + hsCode + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", material='" + material + '\'' +
                ", useAge='" + useAge + '\'' +
                ", essential='" + essential + '\'' +
                ", pic='" + pic + '\'' +
                ", remarks='" + remarks + '\'' +
                ", inputer='" + inputer + '\'' +
                ", jobtype='" + jobtype + '\'' +
                '}';
    }
}
