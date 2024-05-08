package com.scp.model.commerce;

import com.alibaba.druid.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CIMC
 */
public class BusPackListResponse {

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
     * #grossWeight 单件毛重
     */
    private String grswgt;
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
     * #SKU
     */
    private String sku;
    /**
     * #产品链接
     */
    private String producturl;

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

    public String getGrswgt() {
        return grswgt;
    }

    public void setGrswgt(String grswgt) {
        this.grswgt = grswgt;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    /**
     * 青岛装箱单
     *
     * @param row
     * @param username
     */
    public BusPackListResponse(Row row, String username) {

        this.shipmentId = getCellValue(row.getCell(0));
        this.fbaRef = getCellValue(row.getCell(1));
        this.pkgs = getCellValue(row.getCell(2));
        this.grswgt = getCellValue(row.getCell(3));
        this.length = getCellValue(row.getCell(4));
        this.width = getCellValue(row.getCell(5));
        this.height = getCellValue(row.getCell(6));
        this.cargoEnName = getCellValue(row.getCell(7));
        this.cargoName = getCellValue(row.getCell(8));
        this.piece = getCellValue(row.getCell(9));
        this.unitPrice = getCellValue(row.getCell(10));
        this.hsCode = translateToPlainStr(getCellValue(row.getCell(11)));
        this.material = getCellValue(row.getCell(12));
        this.useAge = getCellValue(row.getCell(13));
        this.brand = getCellValue(row.getCell(14));
        this.producturl = getCellValue(row.getCell(15));
        this.model = getCellValue(row.getCell(16));
        this.remarks = getCellValue(row.getCell(18));

    }

    /**
     * 途曦装箱单
     *
     * @param row
     */
    public BusPackListResponse(Row row) {
        this.fbaRef = getCellValue(row.getCell(0));
        this.shipmentId = getCellValue(row.getCell(1));
        this.cargoName = getCellValue(row.getCell(2));
        this.cargoEnName = getCellValue(row.getCell(3));
        this.piece = getCellValue(row.getCell(4));
        this.unitPrice = getCellValue(row.getCell(5));
        this.totalPrice = getCellValue(row.getCell(6));
        this.packAge = getCellValue(row.getCell(7));
        this.pkgs = getCellValue(row.getCell(8));
        this.length = getCellValue(row.getCell(9));
        this.width = getCellValue(row.getCell(10));
        this.height = getCellValue(row.getCell(11));
        this.volTtl = getCellValue(row.getCell(12));
        this.grossWeight = getCellValue(row.getCell(13));
        this.plGwTtl = getCellValue(row.getCell(14));
        this.hsCode = translateToPlainStr(getCellValue(row.getCell(15)));
        this.brand = getCellValue(row.getCell(16));
        this.model = getCellValue(row.getCell(17));
        this.material = getCellValue(row.getCell(18));
        this.useAge = getCellValue(row.getCell(19));
        this.essential = getCellValue(row.getCell(20));
        this.pic = getCellValue(row.getCell(21));
        this.remarks = getCellValue(row.getCell(22));
        this.sku = getCellValue(row.getCell(23));
    }

    /**
     * 途曦装箱单
     *
     * @param row
     */
    public BusPackListResponse(String id, Row row) {
        this.cargoName = getCellValue(row.getCell(0));
        this.cargoEnName = getCellValue(row.getCell(1));
        this.fbaRef = getCellValue(row.getCell(2));
        this.shipmentId = getCellValue(row.getCell(3));
        this.brand = getCellValue(row.getCell(4));
        this.model = getCellValue(row.getCell(6));
        this.material = getCellValue(row.getCell(8));
        this.useAge = getCellValue(row.getCell(10));
        this.hsCode = getCellValue(row.getCell(12));
        this.packAge = getCellValue(row.getCell(15));
        this.pkgs = getCellValue(row.getCell(16));
        this.piece = getCellValue(row.getCell(17));
        this.unitPrice = getCellValue(row.getCell(18));
        this.grossWeight = getCellValue(row.getCell(20));
        this.length = getCellValue(row.getCell(21));
        this.width = getCellValue(row.getCell(22));
        this.height = getCellValue(row.getCell(23));
        this.pic = getCellValue(row.getCell(24));
        this.remarks = getCellValue(row.getCell(25));
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

    /**
     * 将科学计数法文本转换成字符文本
     *
     * @param number
     * @return
     */
    public String translateToPlainStr(String number) {
        if (StringUtils.isEmpty(number)) {
            return number;
        }
        String regEx = "^([\\+|-]?\\d+(.{0}|.\\d+))[Ee]{1}([\\+|-]?\\d+)$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        Matcher matcher = pattern.matcher(number);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        // 判断是否为字符串的科学计数法
        if (rs) {
            // 科学计数法转数字
            BigDecimal originValue = new BigDecimal(number);
            // 数字转字符串
            return originValue.toPlainString();
        }
        return number;
    }


}
