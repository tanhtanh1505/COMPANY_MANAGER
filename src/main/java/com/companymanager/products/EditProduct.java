package com.companymanager.products;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditProduct {
    private int mode = 1;
    public TextField productCode;
    public TextField productName;
    public TextField quantityInStock;
    public TextField buyPrice;
    public TextField sellPrice;
    public TextArea description;

    public void editMode(String _productCode) {
        try {
            this.mode = 2;
            productCode.setEditable(false);
            ResultSet res = DatabaseManager.executeQuery("select * from products where productCode = '" + _productCode + "';");
            productCode.setText(_productCode);
            if (res.next()) {
                productName.setText(res.getString("productName"));
                quantityInStock.setText(res.getString("quantityInStock"));
                buyPrice.setText(res.getString("buyPrice"));
                sellPrice.setText(res.getString("sellPrice"));
                description.setText(res.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submit(ActionEvent event) throws SQLException {
        String query = "";
        if(mode == 1) {
            ResultSet res = DatabaseManager.executeQuery("select productCode from products where productCode = '" + productCode.getText() +"';");
            if (res != null && res.next()) {
                Notifications.create()
                        .text("Product is existed!")
                        .showError();
                return;
            }

            query = "INSERT INTO `products`(`productCode`, `productName`, `description`, `quantityInStock`, `buyPrice`, `sellPrice`) " +
                    "VALUES ('" + productCode.getText() + "','" + productName.getText() + "','" + description.getText() + "','" +
                    quantityInStock.getText() + "','" + buyPrice.getText() + "','" + sellPrice.getText() + "')";
        }
        else {
            query = "UPDATE `products` " +
                    "SET `productName` = '"+ productName.getText() +"', " +
                    "`description` = '"+ description.getText() + "'," +
                    "`quantityInStock` = '"+ quantityInStock.getText() +"', " +
                    "`buyPrice` = '" + buyPrice.getText() + "', " +
                    "`sellPrice` = '" + sellPrice.getText() + "' " +
                    "WHERE `products`.`productCode` = '" + productCode.getText() +"'";
        }

        DatabaseManager.executeUpdate(query);
        Notifications.create()
                .text("Success!")
                .showInformation();
        exit(event);
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "products-view.fxml");
    }
}
