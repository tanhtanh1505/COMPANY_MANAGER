package com.companymanager.customers;

public class TableModelC {
    private String ssn, name, dob, phone, email, address, total;

    public TableModelC(String ssn, String name, String dob, String phone, String email, String address, String total){
        this.ssn = ssn;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.total = total;
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

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }

    public String getAddress(){
        return address;
    }

    public String getTotal(){
        return total;
    }
}
