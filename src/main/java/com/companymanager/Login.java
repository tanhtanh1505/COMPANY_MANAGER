package com.companymanager;

import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    public static boolean login(String user, String password){
        try {
        ResultSet res = DatabaseManager.getTable("account");
        if(res == null)
            return false;

        while(res.next()){
            if(res.getString(1).equals(user)) {
                if(res.getString(2).equals(password)) {
                    Notifications.create()
                            .text("Login success")
                            .showInformation();
                    return true;
                }
                else {
                    Notifications.create()
                            .text("Error password")
                            .showInformation();
                    return false;
                }
            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
