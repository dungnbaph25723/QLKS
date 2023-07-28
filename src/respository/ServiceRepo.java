package respository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Service;
import utilities.JdbcUntil;

public class ServiceRepo {

    Connection conn = JdbcUntil.getConnection();

    public List<Service> getIdByCode(String code) {
        List<Service> list = new ArrayList<>();
        try {
            String sql = " select * from service where code = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getString("id"));
                service.setCode(rs.getString("code"));
                service.setName(rs.getString("name"));
                service.setPrice(rs.getString("price"));
                service.setNotes(rs.getString("notes"));
                service.setIdPromotion(rs.getString("idPromotion"));
                list.add(service);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Service> getAll() {
        List<Service> list = new ArrayList<>();
        try {
            String sql = " select * from service  ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getString("id"));
                service.setCode(rs.getString("code"));
                service.setName(rs.getString("name"));
                service.setPrice(rs.getString("price"));
                service.setNotes(rs.getString("notes"));
                service.setIdPromotion(rs.getString("idPromotion"));
                list.add(service);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
