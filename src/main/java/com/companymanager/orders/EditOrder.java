package com.companymanager.orders;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditOrder implements Initializable {
    private int mode = 1;

    public TextField orderNumber;
    public ComboBox<String> status;
    public TextField comment;
    public TextField customerNumber;
    public DatePicker requiredDate;
    public DatePicker orderDate;
    public DatePicker shippedDate;
    List<String> lMode = List.of("Prepare", "Ready", "Shipped", "Cancel");
    ObservableList<String> list = FXCollections.observableArrayList(lMode);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        status.setItems(list);
    }

    public void editMode(String orderN) {
        try {
            this.mode = 2;
            orderDate.setEditable(false);
            ResultSet res = DatabaseManager.executeQuery("select * from orders where orderNumber = '" + orderN + "';");
            orderNumber.setText(orderN);
            if (res.next()) {
                orderDate.setValue(LocalDate.parse(res.getString("orderDate")));
                requiredDate.setValue(LocalDate.parse(res.getString("requiredDate")));
                if(res.getString("shippedDate") != null)
                    shippedDate.setValue(LocalDate.parse(res.getString("shippedDate")));
                status.setValue(res.getString("status"));
                comment.setText(res.getString("comment"));
                customerNumber.setText(res.getString("customerNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submit(ActionEvent event) throws SQLException {
        String query = "";
        if(mode == 1) {
            ResultSet res = DatabaseManager.executeQuery("SELECT * FROM `orders` WHERE `orderNumber` = '" + orderNumber.getText() + "';");
            if (res != null && res.next()) {
                Notifications.create()
                        .text("OrderNumber is existed!")
                        .showError();
                return;
            }
            if(shippedDate.getValue() != null) {
                query = "INSERT INTO `orders`(`orderNumber`, `orderDate`, `requiredDate`, `shippedDate`, `status`, `comment`, `customerNumber`) " +
                        "VALUES ('" + orderNumber.getText() + "','" + orderDate.getValue() + "','" + requiredDate.getValue() + "','" +
                        shippedDate.getValue() + "','" + status.getValue() + "','" + comment.getText() + "','" + customerNumber.getText() + "')";
            }
            else {
                query = "INSERT INTO `orders`(`orderNumber`, `orderDate`, `requiredDate`, `status`, `comment`, `customerNumber`) " +
                        "VALUES ('" + orderNumber.getText() + "','" + orderDate.getValue() + "','" + requiredDate.getValue() + "','" +
                        status.getValue() + "','" + comment.getText() + "','" + customerNumber.getText() + "')";
            }
        }
        else {
            query = "UPDATE `orders` " +
                    "SET `orderDate` = '"+ orderDate.getValue() +"', " +
                    "`requiredDate` = '"+ requiredDate.getValue() + "'," +
                    "`shippedDate` = '"+ shippedDate.getValue() +"', " +
                    "`status` = '" + status.getValue() + "', " +
                    "`comment` = '" + comment.getText() + "', " +
                    "`customerNumber` = '" + customerNumber.getText() + "' " +
                    "WHERE `orders`.`orderNumber` = '" + orderNumber.getText() +"'";
        }
        String checkSsnC = "select ssn from customers where ssn = '" + customerNumber.getText() + "';";
        ResultSet r = DatabaseManager.executeQuery(checkSsnC);
        if(r == null || !r.next()){
            Notifications.create()
                    .text("Invalid customer!")
                    .showInformation();
        }
        else {
            DatabaseManager.executeUpdate(query);
            Notifications.create()
                    .text("Add Success!")
                    .showInformation();
            exit(event);
        }
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "orders-view.fxml");
    }
}
