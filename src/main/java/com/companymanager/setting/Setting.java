package com.companymanager.setting;

import com.companymanager.HelloApplication;
import com.companymanager.Mode;
import com.companymanager.SwitchScene;
import com.companymanager.orders.InfoOrderEdit;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Setting {

    public void changePassword(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("change-password.fxml"));
        Parent boardParent = loader.load();
        Scene secondScene = new Scene(boardParent);
        Stage newWindow = new Stage();
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    public void logoutBtn(ActionEvent event) {
        Mode.setManagerModeOff();
        SwitchScene.to(event, "hello-view.fxml");
    }

    public void historyLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("history-login.fxml"));
        Parent boardParent = loader.load();
        Scene secondScene = new Scene(boardParent);
        Stage newWindow = new Stage();
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    public void exit(ActionEvent event) {
        if(Mode.forManager())
            SwitchScene.to(event, "board-view.fxml");
        else
            SwitchScene.to(event, "board-for-employee.fxml");
    }
}
