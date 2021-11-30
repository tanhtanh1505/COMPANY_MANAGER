package com.companymanager.orders;

public class TableInfoOrder {
    private String productCode, productName, priceEach, quantityOrdered;

    public TableInfoOrder(String productCode, String productName, String priceEach, String quantityOrdered){
        this.productCode = productCode;
        this.productName = productName;
        this.priceEach = priceEach;
        this.quantityOrdered = quantityOrdered;
    }

    public String getPriceEach() {
        return priceEach;
    }

    public String getQuantityOrdered() {
        return quantityOrdered;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }
}
