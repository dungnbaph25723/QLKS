package utilities;

import model.Staff;

public class Auth {

    public static String rule = null;
    public static String id = null;

    public static void clear() {
        Auth.rule = null;
    }

    public static boolean checkRule(String rule) {
        if (rule.equals("nhanvien")) {
            return false;
        }
        return true;
    }

    public static boolean isLogin() {
        return Auth.rule != null;
    }

}
