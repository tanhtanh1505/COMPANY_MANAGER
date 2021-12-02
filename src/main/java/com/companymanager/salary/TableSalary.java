package com.companymanager.salary;

public class TableSalary {
    private String ssn, name, dob, address, numOrder, totalSalary;

    public TableSalary(String ssn, String name, String dob, String address, String numOrder, String totalSalary){
        this.ssn = ssn;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.numOrder = numOrder;
        this.totalSalary = totalSalary;
    }

    public String getSsn() {
        return ssn;
    }

    public String getName(){
        return name;
    }

    public String getDob(){
        return dob;
    }

    public String getAddress(){
        return address;
    }

    public String getTotalSalary() {
        return totalSalary;
    }

    public String getNumOrder() {
        return numOrder;
    }
}
