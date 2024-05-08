package com.scp.model.commerce;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author CIMC
 */
public class BusCommerceArApResponse {

    /**
     * #araptype 应收应付=ARAP
     */
    private String araptype;
    /**
     * #nos 运单号
     */
    private String nos;
    /**
     * #company 结算公司
     */
    private String company;
    /**
     * #feename 费用种类
     */
    private String feename;
    /**
     * #amount 金额
     */
    private String amount;
    /**
     * #currency 币种
     */
    private String currency;
    /**
     * #rate 汇率
     */
    private String rate;
    /**
     * #amendtype 添加类型
     */
    private String amendtype;
    /**
     * #remarks 备注
     */
    private String remarks;
    /**
     * #corp 结算地
     */
    private String corp;
    /**
     * #inputer 录入人
     */
    private String inputer;

    public BusCommerceArApResponse(Row row, String userid) {
        this.araptype = getCellValue(row.getCell(0));
        this.nos = getCellValue(row.getCell(1));
        this.company = getCellValue(row.getCell(2));
        this.feename = getCellValue(row.getCell(3));
        this.amount = getCellValue(row.getCell(4));
        this.currency = getCellValue(row.getCell(5));
        this.rate = getCellValue(row.getCell(6));
        this.amendtype = getCellValue(row.getCell(7));
        this.remarks = getCellValue(row.getCell(8));
        this.corp = getCellValue(row.getCell(9));
        this.inputer = userid;

    }

    public BusCommerceArApResponse() {

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


    public String getAraptype() {
        return araptype;
    }

    public void setAraptype(String araptype) {
        this.araptype = araptype;
    }

    public String getNos() {
        return nos;
    }

    public void setNos(String nos) {
        this.nos = nos;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFeename() {
        return feename;
    }

    public void setFeename(String feename) {
        this.feename = feename;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmendtype() {
        return amendtype;
    }

    public void setAmendtype(String amendtype) {
        this.amendtype = amendtype;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }
}
