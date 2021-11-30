package com.companymanager.orders;

import com.companymanager.DatabaseManager;
import com.companymanager.orders.TableInfoOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoOrderEdit {

    public TextField productCode;
    public TextField quantityOrdered;
    private String orderNumber;
    private int mode = 1; //them
    private TableView<TableInfoOrder> table;
    private Label total;

    public void init(String _orderNumber, TableView<TableInfoOrder> table, Label total){
        orderNumber = _orderNumber;
        this.table = table;
        this.total = total;
    }

    public void editMode(String _productCode, String _number){
        productCode.setText(_productCode);
        productCode.setEditable(false);
        quantityOrdered.setText(_number);
        mode = 2;
    }

    public void submit(ActionEvent event) throws SQLException {
        String query = "";
        ResultSet res;
        if(mode == 1){
            res = DatabaseManager.executeQuery("SELECT productCode FROM `products` WHERE `productCode` = '" + productCode.getText() + "';");
            if (res == null || !res.next()) {
                Notifications.create()
                        .text("Product is not exist in stock!")
                        .showError();
                return;
            }
            res = DatabaseManager.executeQuery("SELECT productCode FROM `orderdetails` WHERE `productCode` = '" + productCode.getText() + "' and `orderNumber` = '" +orderNumber+ "';");
            if (res != null && res.next()) {
                Notifications.create()
                        .text("Product is existed in table!")
                        .showError();
                return;
            }

            query = "INSERT INTO `orderdetails` (`orderNumber`, `productCode`, `quantityOrdered`) VALUES ('"+orderNumber + "', '" +productCode.getText() +"', '" +quantityOrdered.getText()+ "');";
            DatabaseManager.executeUpdate(query);

            ObservableList<TableInfoOrder> listView = FXCollections.observableArrayList();
            query = "select productCode pc, quantityOrdered,\n" +
                    "(select sellPrice from products where productCode = pc) as sellPrice,\n" +
                    "(select productName from products where productCode = pc) as productName\n" +
                    "from orderdetails where orderNumber = '" +orderNumber+ "';";
            res = DatabaseManager.executeQuery(query);
            if(res == null)
                return;
            while (res.next()) {
                listView.add(new TableInfoOrder(res.getString("pc"), res.getString("productName"),
                        res.getString("sellPrice"), res.getString("quantityOrdered")));
            }
            table.setItems(listView);

            query = "SELECT sum(od.quantityOrdered * p.sellPrice) total\n" +
                    "FROM orderdetails od\n" +
                    "inner join products p on od.productCode = p.productCode\n" +
                    "where od.orderNumber = '"+orderNumber+"';";
            res = DatabaseManager.executeQuery(query);
            if(res != null && res.next())
                total.setText("Tổng giá: " + res.getString("total"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
        else {
            query = "UPDATE `orderdetails` " +
                    "SET `quantityOrdered` = '"+ quantityOrdered.getText() +
                    "' WHERE `orderdetails`.`orderNumber` = '" + orderNumber +"' and productCode = '" + productCode.getText() + "';";
            DatabaseManager.executeUpdate(query);

            ObservableList<TableInfoOrder> listView = FXCollections.observableArrayList();
            query = "select productCode pc, quantityOrdered,\n" +
                    "(select sellPrice from products where productCode = pc) as sellPrice,\n" +
                    "(select productName from products where productCode = pc) as productName\n" +
                    "from orderdetails where orderNumber = '" +orderNumber+ "';";
            res = DatabaseManager.executeQuery(query);
            if(res == null)
                return;
            while (res.next()) {
                listView.add(new TableInfoOrder(res.getString("pc"), res.getString("productName"),
                        res.getString("sellPrice"), res.getString("quantityOrdered")));
            }
            table.setItems(listView);

            query = "SELECT sum(od.quantityOrdered * p.sellPrice) total\n" +
                    "FROM orderdetails od\n" +
                    "inner join products p on od.productCode = p.productCode\n" +
                    "where od.orderNumber = '"+orderNumber+"';";
            res = DatabaseManager.executeQuery(query);
            if(res != null && res.next())
                total.setText("Tổng giá: " + res.getString("total"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
