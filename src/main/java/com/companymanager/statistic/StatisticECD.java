package com.companymanager.statistic;

import com.companymanager.DatabaseManager;
import com.companymanager.Mode;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class StatisticECD implements Initializable {
    public ImageView image;
    public PieChart charBestSellP;
    public PieChart charBestInterestP;
    public PieChart chartCustomer;
    public PieChart chartEmployee;
    private final ObservableList<PieChart.Data> list1 = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> list2 = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> list3 = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> list4 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ResultSet res;

            String query = "SELECT c.name, if(x.total is null, 0, x.total) as total\n" +
                    "FROM customers c\n" +
                    "LEFT JOIN\n" +
                    "\t(SELECT o.customerNumber, SUM(p.sellPrice*od.quantityOrdered) total \n" +
                    "     FROM orders o\n" +
                    "     INNER JOIN orderdetails od ON od.orderNumber = o.orderNumber\n" +
                    "     INNER JOIN products p ON p.productCode = od.productCode\n" +
                    "     WHERE o.status = 'Shipped'\n" +
                    "     GROUP BY o.customerNumber) x\n" +
                    "ON c.ssn = x.customerNumber";
            res = DatabaseManager.executeQuery(query);
            list1.clear();
            chartCustomer.getData().clear();
            while (true) {
                assert res != null;
                if (!res.next()) break;
                list1.add(new PieChart.Data(res.getString("name"), Integer.parseInt(res.getString("total"))));
            }
            chartCustomer.getData().addAll(list1);

            query = "SELECT (SELECT name FROM employees e WHERE e.ssn = t.essn) as name, COUNT(essn) as nOrder\n" +
                    "FROM temp_orders_employees t\n" +
                    "GROUP BY essn;";
            res = DatabaseManager.executeQuery(query);
            list2.clear();
            chartEmployee.getData().clear();
            while (true) {
                assert res != null;
                if (!res.next()) break;
                list2.add(new PieChart.Data(res.getString("name"), Integer.parseInt(res.getString("nOrder"))));
            }
            chartEmployee.getData().addAll(list2);

            query = "SELECT p.productName, IF(res.s IS NULL, 0, res.s) as number\n" +
                    "FROM products p\n" +
                    "LEFT JOIN\n" +
                    "(SELECT od.productCode, SUM(od.quantityOrdered) s\n" +
                    "FROM orderdetails od\n" +
                    "GROUP BY od.productCode) res\n" +
                    "ON res.productCode = p.productCode";
            res = DatabaseManager.executeQuery(query);
            list3.clear();
            charBestSellP.getData().clear();
            while (true) {
                assert res != null;
                if (!res.next()) break;
                list3.add(new PieChart.Data(res.getString("productName"), Integer.parseInt(res.getString("number"))));
            }
            charBestSellP.getData().addAll(list3);

            query = "SELECT p.productName, (sellPrice - buyPrice)/sellPrice inter\n" +
                    "FROM products p";
            res = DatabaseManager.executeQuery(query);
            list4.clear();
            charBestInterestP.getData().clear();
            while (true) {
                assert res != null;
                if (!res.next()) break;
                list4.add(new PieChart.Data(res.getString("productName"), Double.parseDouble(res.getString("inter"))));
            }
            charBestInterestP.getData().addAll(list4);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "statistics.fxml");
    }
}
