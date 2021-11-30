package com.companymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseManager.connect();
    }

    @FXML
    TextField user, password;

    public void loginBtn(ActionEvent event) throws IOException {
        if(Login.login(user.getText(), password.getText())){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("board-view.fxml"));
            Parent eWordParent = loader.load();
            Scene scene = new Scene(eWordParent);
            stage.setScene(scene);
        }
    }


}