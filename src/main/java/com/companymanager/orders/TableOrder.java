package com.companymanager.orders;

public class TableOrder {
    private String orderNumber, orderDate, requiredDate, shippedDate, status, comment, customerNumber;

    public TableOrder(String orderName, String orderDate, String requiredDate, String shippedDate, String status, String comment, String customerNumber){
        this.orderNumber = orderName;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.status = status;
        this.comment = comment;
        this.customerNumber = customerNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getComment() {
        return comment;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }
}
