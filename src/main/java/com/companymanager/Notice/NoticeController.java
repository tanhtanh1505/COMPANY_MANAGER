package com.companymanager.Notice;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NoticeController implements Initializable {
    public ComboBox selectSendMode;
    public TextField sentTo;
    public TextField subject;
    public TextArea content;
    public ListView listName;
    public TextField findNameE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> lMode = List.of("Nhân viên", "Khách hàng", "Tất cả nhân viên", "Tất cả khách hàng");
        selectSendMode.setItems(FXCollections.observableList(lMode));
        selectSendMode.getSelectionModel().select(0);
    }

    public void findName(KeyEvent keyEvent) throws SQLException {
        ResultSet res = DatabaseManager.executeQuery("SELECT `name` FROM `employees` WHERE `name` LIKE '%" + findNameE.getText() + "%'");
        ArrayList<String> lRes = new ArrayList<>();
        while (res.next()){
            lRes.add(res.getString("name"));
        }
        ObservableList<String> listSuggest = FXCollections.observableArrayList(lRes);
        listName.setItems(listSuggest);
    }

    public void selectName(MouseEvent mouseEvent) throws SQLException {
        String name = listName.getSelectionModel().getSelectedItem().toString();
        ResultSet res = DatabaseManager.executeQuery("SELECT `email` FROM `employees` WHERE `name` ='" + name + "'");
        String listRecieveMail = "";
        if(res.next()){
            listRecieveMail = listRecieveMail + res.getString("email") + ",";
        }
        sentTo.setText(sentTo.getText() + listRecieveMail);
    }

    public void send(ActionEvent event) throws SQLException {
        String[] listEmail;

        if(selectSendMode.getSelectionModel().getSelectedIndex() == 2){
            //Send to all employees
            ResultSet res = DatabaseManager.executeQuery("SELECT `email` FROM `employees`;");
            String listRecieveMail = "";
            while (res.next()){
                listRecieveMail = listRecieveMail + res.getString("email") + ",";
            }
            listEmail = listRecieveMail.split(",");
        }
        else if(selectSendMode.getSelectionModel().getSelectedIndex() == 3){
            //Send to all employees
            ResultSet res = DatabaseManager.executeQuery("SELECT `email` FROM `customers`;");
            String listRecieveMail = "";
            while (res.next()){
                listRecieveMail = listRecieveMail + res.getString("email") + ",";
            }
            listEmail = listRecieveMail.split(",");
        }
        else {
            listEmail = sentTo.getText().split(",");
        }
        Mail.sendToListMail(listEmail, subject.getText(), content.getText());
    }

    public void exit(ActionEvent event) {
        SwitchScene.toMenu(event);
    }
}
