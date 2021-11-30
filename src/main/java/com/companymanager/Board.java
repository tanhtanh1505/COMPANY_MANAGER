package com.companymanager;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Board {
    public void infoUser(ActionEvent event) {
        SwitchScene.to(event, "statistics.fxml");
    }

    public void employeeBtn(ActionEvent event) {
        SwitchScene.to(event, "employees-view.fxml");
    }

    public void orderBtn(ActionEvent event) {
        SwitchScene.to(event, "orders-view.fxml");
    }

    public void customerBtn(ActionEvent event) {
        SwitchScene.to(event, "customers-view.fxml");
    }

    public void productBtn(ActionEvent event) {
        SwitchScene.to(event, "products-view.fxml");
    }

    public void noticeBtn(ActionEvent event) {
        SwitchScene.to(event ,"notice-view.fxml");
    }

    public void settingBtn(ActionEvent event) {
    }

    public void logoutBtn(ActionEvent event) {
        SwitchScene.to(event, "hello-view.fxml");
    }
}
