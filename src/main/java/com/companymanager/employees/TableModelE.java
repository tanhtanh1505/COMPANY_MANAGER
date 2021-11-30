package com.companymanager.employees;

public class TableModelE {
    private String ssn, name, dob, phone, email, address, role;

    public TableModelE(String ssn, String name, String dob, String phone, String email, String address, String role){
        this.ssn = ssn;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
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

    public String getRole(){
        return role;
    }
}
