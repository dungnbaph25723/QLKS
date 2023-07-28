package respository;

import java.util.ArrayList;
import viewModel.DSKhachhang;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import utilities.JdbcUntil;

public class KhachHangRepo {

    private Connection con = JdbcUntil.getConnection();

    public ArrayList<Client> getAll() {
        ArrayList<Client> list = new ArrayList<>();

        try {
            Connection con = JdbcUntil.getConnection();
            String sql = "select code,name,dateOfBirth ,sex,idPersonCard,customPhone,address from Client  ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                Client c = new Client();
                c.setCode(rs.getString(1));
                c.setName(rs.getString(2));
                c.setDateOfBirth(rs.getString(3));
                c.setSex(rs.getString(4));
                c.setIdPersonCard(rs.getString(5));
                c.setCustomPhone(rs.getString(6));
                c.setAddress(rs.getString(7));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Client c) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sqlInsert = "insert into Client (code,name,dateOfBirth,sex,address,idPersonCard,customPhone) values (?, ? , ? , ?, ? ,? ,? )";
            PreparedStatement ps = conn.prepareStatement(sqlInsert);
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setDate(3, Date.valueOf(c.getDateOfBirth()));
            ps.setString(4, c.getSex());
            ps.setString(5, c.getAddress());
            ps.setString(6, c.getIdPersonCard());
            ps.setString(7, c.getCustomPhone());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean delete(String ma) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sqlDelete = "delete from Client where code=? ";
            PreparedStatement ps = conn.prepareStatement(sqlDelete);
            ps.setString(1, ma);
            check = ps.executeUpdate();
            return check > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//

    public Boolean update(String ma, Client d) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sqlUpdate = "Update Client set name=?, dateOfBirth=?,sex=?,idPersonCard=?,customPhone=?,address=? where code=? ";
            PreparedStatement ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, d.getName());
            ps.setString(2, d.getDateOfBirth());
            ps.setString(3, d.getSex());
            ps.setString(4, d.getIdPersonCard());
            ps.setString(5, d.getCustomPhone());
            ps.setString(6, d.getAddress());
            ps.setString(7, ma);
            check = ps.executeUpdate();
            return check > 0;
            //   System.out.println("truy van thanh cong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Client> search(String ma) {
        ArrayList<Client> list = new ArrayList<>();
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "select code,name,dateOfBirth,sex,address,idPersonCard,customPhone from Client where name =? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Client c = new Client();
                c.setCode(rs.getString(1));
                c.setName(rs.getString(2));
                c.setDateOfBirth(rs.getString(3));
                c.setSex(rs.getString(4));
                c.setAddress(rs.getString(5));
                c.setIdPersonCard(rs.getString(6));
                c.setCustomPhone(rs.getString(7));

                list.add(c);
            }
            System.out.println("ket noi Thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
