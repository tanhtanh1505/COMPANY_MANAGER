package com.companymanager.orders;

import com.companymanager.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddEmployeeToOrder {
    public TextField ssnE;
    private String orderNumber;
    private Label label;

    public void init(String _orderNum, Label label){
        orderNumber = _orderNum;
        this.label = label;
    }

    public void submit(ActionEvent event) throws SQLException {
        String query = "select ssn from employees where ssn ='" +ssnE.getText()+"';";
        ResultSet res = DatabaseManager.executeQuery(query);
        if(!(res != null && res.next())) {
            Notifications.create()
                    .text("Employee is not existed!")
                    .showError();
            return;
        }

        query = "select essn from `temp_orders_employees` where orderNumber ='"+orderNumber+"' and essn = '"+ssnE.getText()+"';";
        res = DatabaseManager.executeQuery(query);
        if(res != null && res.next()) {
            Notifications.create()
                    .text("Employee is existed in order!")
                    .showError();
            return;
        }
        query = "INSERT INTO `temp_orders_employees` (`orderNumber`, `essn`) VALUES ('"+orderNumber+"', '" +ssnE.getText()+ "');";
        DatabaseManager.executeUpdate(query);
        query = "select name from employees where ssn ='" + ssnE.getText() +"';";
        res = DatabaseManager.executeQuery(query);
        if(res != null && res.next()) {
            label.setText(label.getText() + "\n   " + res.getString("name"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
