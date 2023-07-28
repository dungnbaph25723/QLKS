package application;

import javax.swing.UIManager;
import views.ViewDangNhap;
import views.ViewTrangChu;

public class Application {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new ViewDangNhap().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
