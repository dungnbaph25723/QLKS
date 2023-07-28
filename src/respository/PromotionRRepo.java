package respository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PromotionR;
import utilities.JdbcUntil;
import viewModel.PromotionRoomViewModel;

public class PromotionRRepo {

    public PromotionR searchPromotionR(String id, String dateEnd) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "select * from promotionR where id=? and dateEnd>=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, dateEnd);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                PromotionR pr = new PromotionR();
                pr.setId(rs.getString("id"));
                pr.setCode(rs.getString("code"));
                pr.setValue(rs.getString("value"));
                pr.setDateEnd(String.valueOf(rs.getDate("dateend")));
                pr.setDateStart(String.valueOf(rs.getString("dateStart")));
                return pr;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PromotionRRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<PromotionR> getAll() {
        List<PromotionR> listSTT = new ArrayList<>();
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Select * from promotionR ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("Id");
                String code = rs.getString("code");
                String vl = rs.getString("value");
                String ds = rs.getString("dateStart");
                String de = rs.getString("dateEnd");

                PromotionR pro = new PromotionR(id, vl, ds, de, code);
                listSTT.add(pro);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listSTT;
    }

    public boolean add(PromotionR pr) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "INSERT INTO promotionR"
                    + "(code,value,dateStart,dateEnd)"
                    + "VALUES (?,?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pr.getCode());
            ps.setString(2, pr.getValue());
            ps.setDate(3, Date.valueOf(pr.getDateStart()));
            ps.setDate(4, Date.valueOf(pr.getDateEnd()));
            ps.execute();
            System.out.println("Truy vấn thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean delete(String id) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "DELETE  promotionR WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
            System.out.println("Xóa thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean update(String id, PromotionR pr) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "UPDATE promotionR SET code=?,value=?, dateStart=?, dateEnd=? WHERE id =?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pr.getCode());
            ps.setString(2, pr.getValue());
            ps.setDate(3, Date.valueOf(pr.getDateStart()));
            ps.setDate(4, Date.valueOf(pr.getDateEnd()));
            ps.setString(5, id);
            ps.execute();
            System.out.println("Truy vấn thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check > 0;
    }
}
