package com.companymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public TextField user;
    public PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseManager.connect();
    }

    public void loginBtn(ActionEvent event) throws IOException {
        if(Login.login(user.getText(), password.getText())){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            if(Mode.forManager())
                loader.setLocation(getClass().getResource("board-view.fxml"));
            else
                loader.setLocation(getClass().getResource("board-for-employee.fxml"));
            Parent eWordParent = loader.load();
            Scene scene = new Scene(eWordParent);
            stage.setScene(scene);
        }
    }


    public void forgotPassword(MouseEvent mouseEvent) {
        if(Login.existUser(user.getText())){
            Login.forgotPassword(user.getText());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Tài khoản không tồn tại!");
            alert.setContentText("Vui lòng nhập lại tài khoản");
            alert.show();
        }
    }
}