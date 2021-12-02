package com.companymanager;

import javafx.event.ActionEvent;

public class BoardForEmployee {
    public void orderBtn(ActionEvent event) {
        SwitchScene.to(event, "orders-view.fxml");
    }

    public void productBtn(ActionEvent event) {
        SwitchScene.to(event, "products-view.fxml");
    }

    public void noticeBtn(ActionEvent event) {
        SwitchScene.to(event, "notice-for-employee.fxml");
    }

    public void logoutBtn(ActionEvent event) {
        SwitchScene.to(event, "setting-view.fxml");
    }
}
