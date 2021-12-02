package com.companymanager;

import javafx.event.ActionEvent;

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
        SwitchScene.to(event, "salary-view.fxml");
    }

    public void logoutBtn(ActionEvent event) {
        SwitchScene.to(event, "setting-view.fxml");
    }
}
