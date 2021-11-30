package com.companymanager.employees;

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
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Employees implements Initializable {
    @FXML
    TableView<TableModelE> table;
    @FXML
    TableColumn<TableModelE, String> ssn;
    @FXML
    TableColumn<TableModelE, String> name;
    @FXML
    TableColumn<TableModelE, String> dob;
    @FXML
    TableColumn<TableModelE, String> phone;
    @FXML
    TableColumn<TableModelE, String> email;
    @FXML
    TableColumn<TableModelE, String> address;
    @FXML
    TableColumn<TableModelE, String> role;

    ObservableList<TableModelE> listView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ssn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            role.setCellValueFactory(new PropertyValueFactory<>("role"));

            ResultSet res = DatabaseManager.getTable("employees");
            while (res.next()) {
                listView.add(new TableModelE(res.getString("ssn"), res.getString("name"),
                        res.getString("dob"), res.getString("phone"),
                        res.getString("email"), res.getString("address"), res.getString("role")));

            }
            table.setItems(listView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent event) {
        SwitchScene.to(event, "edit-employee.fxml");
    }

    public void edit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-employee.fxml"));
        Parent boardParent = loader.load();
        EditEmployee editEmployee = loader.getController();
        editEmployee.editMode(table.getSelectionModel().getSelectedItem().getSsn());
        Scene scene = new Scene(boardParent);
        stage.setScene(scene);
    }

    public void delete(ActionEvent event) {
        String role = table.getSelectionModel().getSelectedItem().getRole();
        if(!role.equals("boss")) {
            String ssn = table.getSelectionModel().getSelectedItem().getSsn();
            String deleteQuery = "DELETE FROM employees WHERE ssn = " + ssn + ";";
            DatabaseManager.executeUpdate(deleteQuery);
            table.getItems().remove(table.getSelectionModel().getSelectedItem());
        }
        else {
            Notifications.create()
                    .text("Can't delete boss!")
                    .showInformation();
        }
    }

    public void exit(ActionEvent event) throws IOException {
        SwitchScene.to(event, "board-view.fxml");
    }
}
