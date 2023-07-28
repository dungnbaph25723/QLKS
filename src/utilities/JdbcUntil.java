package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcUntil {

    static Connection conn;

    public static Connection getConnection() {
        if (JdbcUntil.conn == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String user = "sa", pass = "123456", url = "jdbc:sqlserver://localhost:1433;databaseName=QLPhongKhachSan;encrypt=true;trustServerCertificate=true";

                conn = DriverManager.getConnection(url, user, pass);
            } catch (SQLException ex) {
                Logger.getLogger(JdbcUntil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JdbcUntil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }

}
