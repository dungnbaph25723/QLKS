package respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.JdbcUntil;

public class StaffRepo {

    Connection conn = JdbcUntil.getConnection();

    public List<String> selectByUser(String user, String pass) {
        String sql = "select id,[rule] from staff where [user] = ? and [pass] = ?";
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                list.add(rs.getString("rule"));
                list.add(rs.getString("id"));
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
