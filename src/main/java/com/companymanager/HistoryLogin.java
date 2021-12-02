package com.companymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HistoryLogin implements Initializable {
    public ListView listHistory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> list = FXCollections.observableArrayList();
            ResultSet res = DatabaseManager.executeQuery("select timeLogin from historyLogin where ssn ='" + Mode.getUserLogin() +"' order by timeLogin desc;");
            while (res.next()) {
                list.add(res.getString("timeLogin"));
            }
            listHistory.setItems(list);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
