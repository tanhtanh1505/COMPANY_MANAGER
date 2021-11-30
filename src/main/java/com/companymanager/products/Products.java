package com.companymanager.products;

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

public class Products implements Initializable {
    @FXML
    TableView<TableProduct> table;
    @FXML
    TableColumn<TableProduct, String> productCode;
    @FXML
    TableColumn<TableProduct, String> productName;
    @FXML
    TableColumn<TableProduct, String> description;
    @FXML
    TableColumn<TableProduct, String> quantityInStock;
    @FXML
    TableColumn<TableProduct, String> buyPrice;
    @FXML
    TableColumn<TableProduct, String> sellPrice;

    ObservableList<TableProduct> listView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            productCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            quantityInStock.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
            buyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
            sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));

            ResultSet res = DatabaseManager.getTable("products");
            while (res.next()) {
                listView.add(new TableProduct(res.getString("productCode"), res.getString("productName"),
                        res.getString("description"), res.getString("quantityInStock"),
                        res.getString("buyPrice"), res.getString("sellPrice")));
            }
            table.setItems(listView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent event) {
        SwitchScene.to(event, "edit-product.fxml");
    }

    public void edit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-product.fxml"));
        Parent boardParent = loader.load();
        EditProduct editProduct = loader.getController();
        editProduct.editMode(table.getSelectionModel().getSelectedItem().getProductCode());
        Scene scene = new Scene(boardParent);
        stage.setScene(scene);
    }

    public void delete(ActionEvent event) {
        String pdtCode = table.getSelectionModel().getSelectedItem().getProductCode();
        String deleteQuery = "DELETE FROM products WHERE productCode = '" + pdtCode + "';";
        DatabaseManager.executeUpdate(deleteQuery);
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }

    public void exit(ActionEvent event) throws IOException {
        SwitchScene.to(event, "board-view.fxml");
    }
}
