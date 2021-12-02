package com.companymanager.salary;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import com.companymanager.employees.TableModelE;
import com.companymanager.salary.TableSalary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Salary implements Initializable {
    public DatePicker selectDate;
    public TextField selectRatio;
    public Label rest;
    @FXML
    TableView<TableSalary> table;
    @FXML
    TableColumn<TableSalary, String> ssn;
    @FXML
    TableColumn<TableSalary, String> name;
    @FXML
    TableColumn<TableSalary, String> dob;
    @FXML
    TableColumn<TableSalary, String> address;
    @FXML
    TableColumn<TableSalary, String> numOrder;
    @FXML
    TableColumn<TableSalary, String> totalSalary;

    ObservableList<TableSalary> listView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ssn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        numOrder.setCellValueFactory(new PropertyValueFactory<>("numOrder"));
        totalSalary.setCellValueFactory(new PropertyValueFactory<>("totalSalary"));

    }

    public void calcSalary(ActionEvent event) throws SQLException {
        String ratio = selectRatio.getText();
        int curMonth = selectDate.getValue().getMonth().getValue();

        String query = "SELECT e.ssn, e.name, e.dob, e.address, IF(numberOrder.numOrder IS NULL, 0, numberOrder.numOrder)numOrder , IF(result.salary IS NULL, 0, result.salary) totalSalary\n" +
                "FROM employees e\n" +
                "LEFT JOIN(\n" +
                "SELECT res.essn, SUM(res.total) salary\n" +
                "FROM\n" +
                "(SELECT t.essn, totalPriceOfOrder.orderNumber, totalPriceOfOrder.total\n" +
                "FROM temp_orders_employees t\n" +
                "INNER JOIN\n" +
                "(SELECT od.orderNumber, sum(od.quantityOrdered * (p.sellPrice - p.buyPrice) * "+ratio+")/\n" +
                " \t(SELECT COUNT(essn) FROM temp_orders_employees WHERE orderNumber = od.orderNumber) total\n" +
                "FROM orderdetails od\n" +
                "inner join products p on od.productCode = p.productCode\n" +
                "INNER JOIN orders o on o.orderNumber = od.orderNumber\n" +
                "WHERE o.status = 'Shipped'  AND month(o.shippedDate) = '"+curMonth+"'\n" +
                "GROUP BY od.orderNumber) totalPriceOfOrder\n" +
                "ON t.orderNumber = totalPriceOfOrder.orderNumber) res\n" +
                "GROUP BY res.essn) result\n" +
                "ON result.essn = e.ssn\n" +
                "LEFT JOIN(\n" +
                "    SELECT t.essn, COUNT(essn) as numOrder\n" +
                "\tFROM temp_orders_employees t\n" +
                "\tINNER JOIN (SELECT * FROM orders WHERE month(shippedDate) = '"+curMonth+"') o ON o.orderNumber = t.orderNumber\n" +
                "\tGROUP BY essn) numberOrder \n" +
                "ON numberOrder.essn = e.ssn";
        ResultSet res = DatabaseManager.executeQuery(query);
        table.getItems().clear();
        listView.clear();
        while (res.next()){
            listView.add(new TableSalary(res.getString("ssn"), res.getString("name"),
                    res.getString("dob"), res.getString("address"),
                    res.getString("numOrder"), res.getString("totalSalary")));
        }
        table.setItems(listView);

        query = "SELECT (SELECT sum(od.quantityOrdered * (p.sellPrice - p.buyPrice))\n" +
                "                FROM orderdetails od\n" +
                "                inner join products p on od.productCode = p.productCode\n" +
                "                inner join orders o on od.orderNumber = o.orderNumber\n" +
                "                where month(o.shippedDate) = '"+curMonth+"' and o.status = 'Shipped'\n" +
                "                group by month(o.shippedDate))\n" +
                "- SUM(x.totalSalary)\n" +
                "FROM\n" +
                "(SELECT IF(result.salary IS NULL, 0, result.salary) totalSalary\n" +
                "FROM employees e\n" +
                "LEFT JOIN(\n" +
                "SELECT res.essn, SUM(res.total) salary\n" +
                "FROM\n" +
                "(SELECT t.essn, totalPriceOfOrder.orderNumber, totalPriceOfOrder.total\n" +
                "FROM temp_orders_employees t\n" +
                "INNER JOIN\n" +
                "(SELECT od.orderNumber, sum(od.quantityOrdered * (p.sellPrice - p.buyPrice) * "+ratio+")/\n" +
                " \t(SELECT COUNT(essn) FROM temp_orders_employees WHERE orderNumber = od.orderNumber) total\n" +
                "FROM orderdetails od\n" +
                "inner join products p on od.productCode = p.productCode\n" +
                "INNER JOIN orders o on o.orderNumber = od.orderNumber\n" +
                "WHERE o.status = 'Shipped'  AND month(o.shippedDate) = '"+curMonth+"'\n" +
                "GROUP BY od.orderNumber) totalPriceOfOrder\n" +
                "ON t.orderNumber = totalPriceOfOrder.orderNumber) res\n" +
                "GROUP BY res.essn) result\n" +
                "ON result.essn = e.ssn) x";
        res = DatabaseManager.executeQuery(query);
        if(res != null && res.next()){
            rest.setText("Dư: " + res.getString(1));
        }
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "board-view.fxml");
    }
}
