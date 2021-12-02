package com.companymanager;

public class Mode {
    private static boolean isManager = false;
    private static String userLogin = "";

    public static void setManagerMode(){
        isManager = true;
    }

    public static void setManagerModeOff(){
        isManager = false;
    }

    public static boolean forManager(){
        return isManager;
    }

    public static void setUserLogin(String user){
        userLogin = user;
    }

    public static String getUserLogin(){
        return userLogin;
    }
}
