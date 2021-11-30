package com.companymanager.orders;

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

public class Orders implements Initializable {
    @FXML
    TableView<TableOrder> table;
    @FXML
    TableColumn<TableOrder, String> orderNumber;
    @FXML
    TableColumn<TableOrder, String> orderDate;
    @FXML
    TableColumn<TableOrder, String> requiredDate;
    @FXML
    TableColumn<TableOrder, String> shippedDate;
    @FXML
    TableColumn<TableOrder, String> status;
    @FXML
    TableColumn<TableOrder, String> comment;
    @FXML
    TableColumn<TableOrder, String> customerNumber;

    ObservableList<TableOrder> listView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
            orderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
            requiredDate.setCellValueFactory(new PropertyValueFactory<>("requiredDate"));
            shippedDate.setCellValueFactory(new PropertyValueFactory<>("shippedDate"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            customerNumber.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));

            ResultSet res = DatabaseManager.getTable("orders");
            while (res.next()) {
                listView.add(new TableOrder(res.getString("orderNumber"), res.getString("orderDate"),
                        res.getString("requiredDate"), res.getString("shippedDate"),
                        res.getString("status"), res.getString("comment"), res.getString("customerNumber")));

            }
            table.setItems(listView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent event) {
        SwitchScene.to(event, "edit-order.fxml");
    }

    public void edit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-order.fxml"));
        Parent boardParent = loader.load();
        EditOrder editOrder = loader.getController();
        editOrder.editMode(table.getSelectionModel().getSelectedItem().getOrderNumber());
        Scene scene = new Scene(boardParent);
        stage.setScene(scene);
    }

    public void delete(ActionEvent event) {
        String orderNumber = table.getSelectionModel().getSelectedItem().getOrderNumber();
        String deleteQuery = "DELETE FROM orders WHERE orderNumber = " + orderNumber + ";";
        DatabaseManager.executeUpdate(deleteQuery);
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }

    public void exit(ActionEvent event) throws IOException {
        SwitchScene.to(event, "board-view.fxml");
    }

    public void info(ActionEvent event) throws IOException, SQLException {
        ResultSet res = DatabaseManager.executeQuery("select name from customers where ssn = '" + table.getSelectionModel().getSelectedItem().getCustomerNumber() + "';");
        String nameCus = "";
        if(res.next()){
            nameCus = res.getString("name");
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("info-order.fxml"));
        Parent boardParent = loader.load();
        InfoOrder infoOrder = loader.getController();
        infoOrder.init(table.getSelectionModel().getSelectedItem().getOrderNumber(),
                table.getSelectionModel().getSelectedItem().getOrderDate(),
                nameCus,
                table.getSelectionModel().getSelectedItem().getStatus());
        Scene scene = new Scene(boardParent);
        stage.setScene(scene);
    }
}
