package com.companymanager.statistic;

import com.companymanager.DatabaseManager;
import com.companymanager.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Statistics implements Initializable{
    public BarChart<String, Integer> graph;
    public CategoryAxis month;
    public NumberAxis amount;
    public DatePicker selectMonth;
    public PieChart pieChart;
    private final XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
    private final XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
    private final XYChart.Series<String, Integer> series3 = new XYChart.Series<>();
    private final ObservableList<PieChart.Data> list = FXCollections.observableArrayList();

    public void statist(ActionEvent event) throws SQLException {
        int curMonth = selectMonth.getValue().getMonth().getValue();
        String querySell = "SELECT month(o.shippedDate), sum(od.quantityOrdered * p.sellPrice) total\n" +
                "FROM orderdetails od \n" +
                "inner join products p on od.productCode = p.productCode\n" +
                "inner join orders o on od.orderNumber = o.orderNumber\n" +
                "where month(o.shippedDate) <= "+curMonth+"\n" +
                "group by month(o.shippedDate)\n" +
                "order by month(o.shippedDate) desc\n" +
                "limit 3;";
        ResultSet res = DatabaseManager.executeQuery(querySell);
        series1.getData().clear();
        while (true){
            assert res != null;
            if (!res.next()) break;
            series1.getData().add(new XYChart.Data<>("Tháng " + res.getString(1), Integer.parseInt(res.getString(2))));
        }

        String queryBuy = "SELECT month(o.shippedDate), sum(od.quantityOrdered * p.buyPrice) total\n" +
                "FROM orderdetails od \n" +
                "inner join products p on od.productCode = p.productCode\n" +
                "inner join orders o on od.orderNumber = o.orderNumber\n" +
                "where month(o.shippedDate) <= "+curMonth+"\n" +
                "group by month(o.shippedDate)\n" +
                "order by month(o.shippedDate) desc\n" +
                "limit 3;";
        res = DatabaseManager.executeQuery(queryBuy);
        series2.getData().clear();
        while (true){
            assert res != null;
            if (!res.next()) break;
            series2.getData().add(new XYChart.Data<>("Tháng " + res.getString(1), Integer.parseInt(res.getString(2))));
        }

        String queryInterest = "SELECT month(o.shippedDate), sum(od.quantityOrdered * (p.sellPrice - p.buyPrice)) total\n" +
                "FROM orderdetails od \n" +
                "inner join products p on od.productCode = p.productCode\n" +
                "inner join orders o on od.orderNumber = o.orderNumber\n" +
                "where month(o.shippedDate) <= "+curMonth+"\n" +
                "group by month(o.shippedDate)\n" +
                "order by month(o.shippedDate) desc\n" +
                "limit 3;";
        res = DatabaseManager.executeQuery(queryInterest);
        series3.getData().clear();
        while (true){
            assert res != null;
            if (!res.next()) break;
            series3.getData().add(new XYChart.Data<>("Tháng " + res.getString(1), Integer.parseInt(res.getString(2))));
        }

        String query = "SELECT month(shippedDate), count(status)\n" +
                "from orders\n" +
                "where month(shippedDate) <= " +curMonth+ " and status = 'Shipped'\n" +
                "group by month(shippedDate)\n" +
                "order by month(shippedDate) desc\n" +
                "limit 3;";
        res = DatabaseManager.executeQuery(query);
        list.clear();
        pieChart.getData().clear();
        while (true) {
            assert res != null;
            if(!res.next()) break;
            list.add(new PieChart.Data("Tháng " + res.getString(1), Integer.parseInt(res.getString(2))));
        }
        pieChart.getData().addAll(list);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        series1.setName("Bán ra");
        series2.setName("Mua vào");
        series3.setName("Lãi");

        graph.getData().clear();
        graph.getData().addAll(series1, series2, series3);
    }

    public void exit(ActionEvent event) {
        SwitchScene.to(event, "board-view.fxml");
    }

    public void statisticECP(ActionEvent event) {
        SwitchScene.to(event, "statistic-ecd.fxml");
    }
}
