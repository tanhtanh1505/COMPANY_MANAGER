package com.companymanager.customers;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InfoCustomer {
    private int mode = 1;

    public TextField ssn;
    public TextField name;
    public TextField phone;
    public TextField email;
    public TextField address;
    public DatePicker dob;

    public void editMode(String ssnE) {
        try {
            this.mode = 2;
            ssn.setEditable(false);
            ResultSet res = DatabaseManager.executeQuery("select * from customers where ssn = " + ssnE + ";");
            ssn.setText(ssnE);
            if (res.next()) {
                name.setText(res.getString("name"));
                dob.setValue(LocalDate.parse(res.getString("dob")));
                phone.setText(res.getString("phone"));
                email.setText(res.getString("email"));
                address.setText(res.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submit(ActionEvent event) throws SQLException {
        String query = "";
        if(mode == 1) {
            ResultSet res = DatabaseManager.executeQuery("select ssn from customers where ssn = '" + ssn.getText() + "';");
            if (res.next()) {
                Notifications.create()
                        .text("SSN is existed!")
                        .showError();
                return;
            }

            query = "INSERT INTO `customers`(`ssn`, `name`, `dob`, `phone`, `email`, `address`) " +
                    "VALUES ('" + ssn.getText() + "','" + name.getText() + "','" + dob.getValue().toString() + "','" +
                    phone.getText() + "','" + email.getText() + "','" + address.getText() + "')";
        }
        else {
            query = "UPDATE `customers` " +
                    "SET `name` = '"+ name.getText() +"', " +
                    "`dob` = '"+ dob.getValue().toString() + "'," +
                    "`phone` = '"+ phone.getText() +"', " +
                    "`email` = '" + email.getText() + "', " +
                    "`address` = '" + address.getText() + "' " +
                    "WHERE `customers`.`ssn` = '" + ssn.getText() +"'";
        }

        DatabaseManager.executeUpdate(query);
        Notifications.create()
                .text("Add Success!")
                .showInformation();
        exit(event);
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "customers-view.fxml");
    }
}
