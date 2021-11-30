package com.companymanager.products;

public class TableProduct {
    private String productCode, productName, description, quantityInStock, buyPrice, sellPrice;

    public TableProduct(String productCode, String productName, String description, String quantityInStock, String buyPrice, String sellPrice){
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public String getQuantityInStock() {
        return quantityInStock;
    }

    public String getDescription() {
        return description;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getProductName(){
        return productName;
    }

}
