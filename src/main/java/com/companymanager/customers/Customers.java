package com.companymanager.customers;

import com.companymanager.DatabaseManager;
import com.companymanager.HelloApplication;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Customers implements Initializable {
    @FXML
    TableView<TableModelC> table;
    @FXML
    TableColumn<TableModelC, String> ssn;
    @FXML
    TableColumn<TableModelC, String> name;
    @FXML
    TableColumn<TableModelC, String> dob;
    @FXML
    TableColumn<TableModelC, String> phone;
    @FXML
    TableColumn<TableModelC, String> email;
    @FXML
    TableColumn<TableModelC, String> address;
    @FXML
    TableColumn<TableModelC, String> total;

    ObservableList<TableModelC> listView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ssn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            total.setCellValueFactory(new PropertyValueFactory<>("total"));

            String qr = "SELECT c.ssn, c.name, c.dob, c.phone, c.email, c.address, if(x.total is null, 0, x.total) as total \n" +
                    "FROM customers c\n" +
                    "LEFT JOIN\n" +
                    "(SELECT o.customerNumber, SUM(p.sellPrice*od.quantityOrdered) total FROM orders o\n" +
                    "INNER JOIN orderdetails od ON od.orderNumber = o.orderNumber\n" +
                    "INNER JOIN products p ON p.productCode = od.productCode\n" +
                    "WHERE o.status = 'Shipped'\n" +
                    "GROUP BY o.customerNumber) x\n" +
                    "ON c.ssn = x.customerNumber";

            ResultSet res = DatabaseManager.executeQuery(qr);
            while (res.next()) {
                listView.add(new TableModelC(res.getString("ssn"), res.getString("name"),
                        res.getString("dob"), res.getString("phone"),
                        res.getString("email"), res.getString("address"),
                        res.getString("total")));
            }
            table.setItems(listView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent event) {
        SwitchScene.to(event, "edit-customer.fxml");
    }

    public void edit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-customer.fxml"));
        Parent boardParent = loader.load();
        InfoCustomer editCustomer = loader.getController();
        editCustomer.editMode(table.getSelectionModel().getSelectedItem().getSsn());
        Scene scene = new Scene(boardParent);
        stage.setScene(scene);
    }

    public void delete(ActionEvent event) {
        String ssn = table.getSelectionModel().getSelectedItem().getSsn();
        String deleteQuery = "DELETE FROM customers WHERE ssn = " + ssn + ";";
        DatabaseManager.executeUpdate(deleteQuery);
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }

    public void exit(ActionEvent event) throws IOException {
        SwitchScene.to(event, "board-view.fxml");
    }
}
