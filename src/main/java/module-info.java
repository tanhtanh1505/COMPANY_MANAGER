module com.companymanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires java.sql;
    requires java.mail;
    requires activation;

    opens com.companymanager to javafx.fxml;
    exports com.companymanager;
    exports com.companymanager.employees;
    opens com.companymanager.employees to javafx.fxml;
    exports com.companymanager.Notice;
    opens com.companymanager.Notice to javafx.fxml;
    exports com.companymanager.customers;
    opens com.companymanager.customers to javafx.fxml;
    exports com.companymanager.orders;
    opens com.companymanager.orders to javafx.fxml;
    exports com.companymanager.products;
    opens com.companymanager.products to javafx.fxml;
}