package com.companymanager.orders;

import com.companymanager.DatabaseManager;
import com.companymanager.HelloApplication;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoOrder {
    public TableView<TableInfoOrder> table;
    public TableColumn<TableOrder, String> productCode;
    public TableColumn<TableOrder, String> productName;
    public TableColumn<TableOrder, String> priceEach;
    public TableColumn<TableOrder, String> quantityOrdered;
    public Label orderNumber;
    public Label totalPrice;
    public Label orderDate;
    public Label status;
    public Label customerName;
    private String _orderNumber;

    ObservableList<TableInfoOrder> listView = FXCollections.observableArrayList();

    public void init(String orderNb, String _orderDate, String cusName, String _status) {
        try {
            _orderNumber = orderNb;
            orderNumber.setText(orderNumber.getText() + orderNb);
            orderDate.setText(orderDate.getText() + _orderDate);
            customerName.setText(customerName.getText() + cusName);
            status.setText(status.getText() + _status);

            productCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            priceEach.setCellValueFactory(new PropertyValueFactory<>("priceEach"));
            quantityOrdered.setCellValueFactory(new PropertyValueFactory<>("quantityOrdered"));

            String qr = "select productCode pc, quantityOrdered,\n" +
                    "(select sellPrice from products where productCode = pc) as sellPrice,\n" +
                    "(select productName from products where productCode = pc) as productName\n" +
                    "from orderdetails where orderNumber = '" +orderNb+ "';";
            ResultSet res = DatabaseManager.executeQuery(qr);
            if(res == null)
                return;
            while (res.next()) {
                listView.add(new TableInfoOrder(res.getString("pc"), res.getString("productName"),
                        res.getString("sellPrice"), res.getString("quantityOrdered")));
            }
            table.setItems(listView);

            qr = "SELECT sum(od.quantityOrdered * p.sellPrice) total\n" +
                    "FROM orderdetails od\n" +
                    "inner join products p on od.productCode = p.productCode\n" +
                    "where od.orderNumber = '"+orderNb+"';";
            res = DatabaseManager.executeQuery(qr);
            if(res != null && res.next())
                totalPrice.setText(totalPrice.getText() + res.getString("total"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("info-order-edit.fxml"));
        Parent boardParent = loader.load();
        Scene secondScene = new Scene(boardParent);
        InfoOrderEdit infoOrderEdit = loader.getController();
        infoOrderEdit.init(_orderNumber, table, totalPrice);
        Stage newWindow = new Stage();
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    public void edit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("info-order-edit.fxml"));
        Parent boardParent = loader.load();
        Scene secondScene = new Scene(boardParent);
        InfoOrderEdit infoOrderEdit = loader.getController();
        infoOrderEdit.init(_orderNumber, table, totalPrice);
        infoOrderEdit.editMode(table.getSelectionModel().getSelectedItem().getProductCode(), table.getSelectionModel().getSelectedItem().getQuantityOrdered());
        Stage newWindow = new Stage();
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    public void delete(ActionEvent event) throws SQLException {
        String pdtCode = table.getSelectionModel().getSelectedItem().getProductCode();
        String deleteQuery = "DELETE FROM orderdetails WHERE orderNumber = '" + _orderNumber + "' and productCode = '" +pdtCode+ "';";
        DatabaseManager.executeUpdate(deleteQuery);
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
        String qr = "SELECT sum(od.quantityOrdered * p.sellPrice) total\n" +
                "FROM orderdetails od\n" +
                "inner join products p on od.productCode = p.productCode\n" +
                "where od.orderNumber = '"+_orderNumber+"';";
        ResultSet res = DatabaseManager.executeQuery(qr);
        if(res != null && res.next())
            totalPrice.setText("Tổng giá: " + res.getString("total"));
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "orders-view.fxml");
    }
}
