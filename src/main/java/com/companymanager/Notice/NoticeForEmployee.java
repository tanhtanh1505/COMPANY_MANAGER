package com.companymanager.Notice;

import com.companymanager.DatabaseManager;
import com.companymanager.Mode;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NoticeForEmployee implements Initializable {
    public ListView listNotice;
    public Label subject;
    public Label content;
    public Label timeSend;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ArrayList<String> listNote = new ArrayList<>();
            String query = "select subject from notification where essn = '" + Mode.getUserLogin() + "';";
            ResultSet res = DatabaseManager.executeQuery(query);
            while (res.next()) {
                listNote.add(res.getString("subject"));
            }
            ObservableList<String> list = FXCollections.observableArrayList(listNote);
            listNotice.setItems(list);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void selectNotice(MouseEvent mouseEvent) throws SQLException {
        String query = "select * from notification where essn ='" + Mode.getUserLogin() +"' and subject ='" + listNotice.getSelectionModel().getSelectedItem() +"';";
        ResultSet res = DatabaseManager.executeQuery(query);
        if(res != null && res.next()){
            timeSend.setText("Thời gian: " + res.getString("timeSend"));
            subject.setText("Tiêu đề: " + res.getString("subject"));
            content.setText(res.getString("content"));
        }
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "board-for-employee.fxml");
    }
}
