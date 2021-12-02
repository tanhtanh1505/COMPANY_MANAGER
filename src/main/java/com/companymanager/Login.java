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
                        "Xin chào " + res.getString("name") + ",\n"+
                        "Tài khoản của bạn đã được khôi phục\n" +
                        "   - User: " + user +"\n" +
                        "   - Password: " + newPass +"\n" +
                        "Đổi lại mật khẩu trong ứng dụng -> cài đặt -> đổi mật khẩu";
                Mail.sendMail(res.getString("email"), "Khôi phục tài khoản thành công", content);
                query = "UPDATE `account` SET `password` = '" +newPass+ "' WHERE `account`.`ssn` = '"+user+"';";
                DatabaseManager.executeUpdate(query);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Khôi phục mật khẩu thành công");
                alert.setContentText("Mật khẩu mới đã được gửi vào email đăng kí!");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Đã có lỗi xảy ra!");
                alert.setContentText("Vui lòng thử lại");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
