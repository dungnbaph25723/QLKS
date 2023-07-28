/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import model.Room;
import java.sql.Connection;
import utilities.JdbcUntil;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.Service;

public class ServiceViewModelrepo {

    public void insert(Service s) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "insert into service (code,name,price,notes,idPromotion) values(?,?,?,?,?) ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPrice());
            ps.setString(4, s.getNotes());
            ps.setString(5, s.getIdPromotion());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String id, Service s) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "update service set name=?,price=? ,notes=?,idpromotion=? where code=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, s.getName());
            ps.setString(2, s.getPrice());
            ps.setString(3, s.getNotes());
            ps.setString(4, s.getIdPromotion());
            ps.setString(5, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String ma) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Delete service where code=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Service> getAll() {
        ArrayList<Service> list = new ArrayList<>();
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "select code,name,price,notes,idpromotion from service";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Service s = new Service();
                s.setCode(rs.getString("Code"));
                s.setName(rs.getString("Name"));
                s.setPrice(rs.getString("Price"));
                s.setNotes(rs.getString("Notes"));
                s.setIdPromotion(rs.getString("idpromotion"));
                list.add(s);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

//    public Service getSearchRoom(String code) {
//        try {
//            Connection conn = JdbcUntil.getConnection();
//            String sql = "select code,name,price,notes,idpromotion from service where code=? ";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, code);
//            ps.execute();
//            ResultSet rs = ps.getResultSet();
//            while (rs.next()) {
//                Service s=new Service();
//                s.setCode(rs.getString(1));
//                s.setCode(rs.getString(2));
//                s.setCode(rs.getString(3));
//                s.setCode(rs.getString(4));
//                return s;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }
}
