package com.companymanager.employees;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditEmployee {
    private int mode = 1;

    public TextField ssn;
    public TextField name;
    public TextField phone;
    public TextField email;
    public TextField address;
    public TextField role;
    public DatePicker dob;

    public void editMode(String ssnE) {
        try {
            this.mode = 2;
            ssn.setEditable(false);
            ResultSet res = DatabaseManager.executeQuery("select * from employees where ssn = '" + ssnE + "';");
            ssn.setText(ssnE);
            if (res.next()) {
                name.setText(res.getString("name"));
                dob.setValue(LocalDate.parse(res.getString("dob")));
                phone.setText(res.getString("phone"));
                email.setText(res.getString("email"));
                address.setText(res.getString("address"));
                role.setText(res.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submit(ActionEvent event) throws SQLException {
        String query = "";
        if(mode == 1) {
            ResultSet res = DatabaseManager.executeQuery("select ssn from employees where ssn = '" + ssn.getText()+ "';");
            if (res.next()) {
                Notifications.create()
                        .text("SSN is existed!")
                        .showError();
                return;
            }

            query = "INSERT INTO `employees`(`ssn`, `name`, `dob`, `phone`, `email`, `address`, `role`) " +
                    "VALUES ('" + ssn.getText() + "','" + name.getText() + "','" + dob.getValue().toString() + "','" +
                    phone.getText() + "','" + email.getText() + "','" + address.getText() + "','" + role.getText() + "')";
        }
        else {
            query = "UPDATE `employees` " +
                    "SET `name` = '"+ name.getText() +"', " +
                    "`dob` = '"+ dob.getValue().toString() + "'," +
                    "`phone` = '"+ phone.getText() +"', " +
                    "`email` = '" + email.getText() + "', " +
                    "`address` = '" + address.getText() + "', " +
                    "`role` = '" + role.getText() + "' " +
                    "WHERE `employees`.`ssn` = '" + ssn.getText() +"'";
        }

        DatabaseManager.executeUpdate(query);
        Notifications.create()
                .text("Add Success!")
                .showInformation();
        exit(event);
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "employees-view.fxml");
    }
}
