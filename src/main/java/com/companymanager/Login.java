package com.companymanager;

import com.companymanager.Notice.Mail;
import javafx.scene.control.Alert;
import org.controlsfx.control.Notifications;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Login {
    public static boolean login(String user, String password){
        try {
        ResultSet res = DatabaseManager.getTable("account");
        if(res == null)
            return false;

        while(res.next()){
            if(res.getString(1).equals(user)) {
                if(res.getString(2).equals(password)) {
                    Mode.setUserLogin(user);
                    String qr = "select role from employees where ssn ='" +user+"';";
                    ResultSet rs = DatabaseManager.executeQuery(qr);
                    if(rs != null && rs.next()) {
                        if(rs.getString("role").equals("boss") || rs.getString("role").equals("leader"))
                            Mode.setManagerMode();
                        qr = "INSERT INTO `historylogin` (`ssn`, `timeLogin`) VALUES ('"+user+"', current_timestamp());";
                        DatabaseManager.executeUpdate(qr);
                        Notifications.create()
                                .text("Login success")
                                .showInformation();
                        return true;
                    }
                }
                else {
                    Notifications.create()
                            .text("Error password")
                            .showInformation();
                    return false;
                }
            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean existUser(String user){
        try {
            ResultSet res = DatabaseManager.executeQuery("select ssn from account where ssn ='" +user+"';");
            return res != null && res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void forgotPassword(String user){
        try {
            String query = "SELECT a.password, e.email, e.name \n" +
                    "FROM account a\n" +
                    "INNER JOIN employees e\n" +
                    "ON a.ssn = e.ssn\n" +
                    "WHERE a.ssn = '"+user+"'";
            ResultSet res = DatabaseManager.executeQuery(query);
            if(res != null && res.next()){
                Random r = new Random();
                String newPass = (char)(r.nextInt(32) + 65) + String.valueOf(r.nextInt(1000)) + String.valueOf(r.nextInt(1000));

                String content =
                        "Xin ch??o " + res.getString("name") + ",\n"+
                        "T??i kho???n c???a b???n ???? ???????c kh??i ph???c\n" +
                        "   - User: " + user +"\n" +
                        "   - Password: " + newPass +"\n" +
                        "?????i l???i m???t kh???u trong ???ng d???ng -> c??i ?????t -> ?????i m???t kh???u";
                Mail.sendMail(res.getString("email"), "Kh??i ph???c t??i kho???n th??nh c??ng", content);
                query = "UPDATE `account` SET `password` = '" +newPass+ "' WHERE `account`.`ssn` = '"+user+"';";
                DatabaseManager.executeUpdate(query);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Kh??i ph???c m???t kh???u th??nh c??ng");
                alert.setContentText("M???t kh???u m???i ???? ???????c g???i v??o email ????ng k??!");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("???? c?? l???i x???y ra!");
                alert.setContentText("Vui l??ng th??? l???i");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
