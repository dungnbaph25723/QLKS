/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PromotionR;
import model.PromotionS;
import utilities.JdbcUntil;

/**
 *
 * @author Admin
 */
public class PromotionSRepo {

    private Connection con = JdbcUntil.getConnection();

    public ArrayList<PromotionS> getAll() {
        ArrayList<PromotionS> listSTT = new ArrayList<>();
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "select * from promotionS ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                PromotionS p = new PromotionS();

                p.setId(rs.getString("Id"));
                p.setCode(rs.getString("Code"));
                p.setValue(rs.getString("Value"));
                p.setDateStart(rs.getString("DateStart"));
                p.setDateEnd(rs.getString("DateEnd"));

                listSTT.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listSTT;
    }

    public PromotionS searchPromotionS(String id, String dateEnd) {
        String sql = "select * from promotionS where id=? and dateEnd>=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, dateEnd);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                PromotionS p = new PromotionS();
                p.setId(rs.getString("id"));
                p.setCode(rs.getString("code"));
                p.setValue(rs.getString("value"));
                p.setDateEnd(String.valueOf(rs.getDate("dateend")));
                p.setDateStart(String.valueOf(rs.getString("dateStart")));
                return p;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PromotionRRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean insert(PromotionS p) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "INSERT INTO promotionS"
                    + "(code,value,dateStart,dateEnd)"
                    + "VALUES (?,?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getCode());
            ps.setString(2, p.getValue());
            ps.setDate(3, Date.valueOf(p.getDateStart()));
            ps.setDate(4, Date.valueOf(p.getDateEnd()));
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
            String sql = "DELETE  promotionS WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
            System.out.println("Xóa thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean update(String id, PromotionS p) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "UPDATE promotionS SET code=?,value=?, dateStart=?, dateEnd=? WHERE id =?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getCode());
            ps.setString(2, p.getValue());
            ps.setDate(3, Date.valueOf(p.getDateStart()));
            ps.setDate(4, Date.valueOf(p.getDateEnd()));
            ps.setString(5, id);
            ps.execute();
            System.out.println("Truy vấn thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check > 0;
    }
}
