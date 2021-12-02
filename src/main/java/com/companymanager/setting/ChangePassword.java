package com.companymanager.setting;

import com.companymanager.DatabaseManager;
import com.companymanager.Mode;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePassword {
    public TextField oldPass;
    public TextField newPass1;
    public TextField newPass2;

    public void submit(ActionEvent event) throws SQLException, InterruptedException {
        String query = "select password from account where ssn ='" + Mode.getUserLogin()+"';";
        ResultSet res = DatabaseManager.executeQuery(query);
        if(res != null && res.next()){
            String oldP = res.getString("password");
            if(oldP.equals(oldPass.getText())){
                if(newPass1.getText().equals(newPass2.getText())){
                    query = "UPDATE `account` SET `password` = '"+newPass1.getText()+"' WHERE `account`.`ssn` = '"+Mode.getUserLogin()+"';";
                    DatabaseManager.executeUpdate(query);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                    Notifications.create()
                            .title("Successfully!")
                            .text("Your password have been changed!")
                            .showInformation();
                }
                else {
                    Notifications.create()
                            .text("2 field new password must be same")
                            .showError();
                }
            }
            else {
                Notifications.create()
                        .title("Error old password!")
                        .text("Reset password if you forgot")
                        .showError();
            }
        }
    }
}
