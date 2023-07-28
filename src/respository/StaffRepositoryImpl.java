/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import model.Staff;
import utilities.JdbcUntil;
import viewModel.staffviewmodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asus
 */
public class StaffRepositoryImpl {

    public List<staffviewmodel> getAll() {
        String query = "SELECT[id],[code],[name],[dateOfBirth],[sex] ,[address] ,[idPersonCard],[user],[phone],[rule]FROM QLPhongKhachSan.dbo.staff";
        try {
            Connection con = JdbcUntil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            List<staffviewmodel> listkhachHang = new ArrayList<>();
            while (rs.next()) {
                staffviewmodel nhanvien = new staffviewmodel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10));
                listkhachHang.add(nhanvien);
            }
            return listkhachHang;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean add(Staff nhanvien) {
        int check = 0;
        String sql = "INSERT INTO [dbo].[staff]\n"
                + "           ([code]\n"
                + "           ,[name]\n"
                + "           ,[dateOfBirth]\n"
                + "           ,[sex]\n"
                + "           ,[address]\n"
                + "           ,[idPersonCard]\n"
                + "           ,[user]\n"
                + "           ,[phone],[pass]\n"
                + "           ,[rule])\n"
                + "     VALUES\n"
                + "           (?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection con = JdbcUntil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, nhanvien.getCode());
            ps.setObject(2, nhanvien.getName());
            ps.setObject(3, nhanvien.getDateOfBirth());
            ps.setObject(4, nhanvien.getSex());
            ps.setObject(5, nhanvien.getAddress());
            ps.setObject(6, nhanvien.getIdPersonCard());
            ps.setObject(7, nhanvien.getUser());
            ps.setObject(8, nhanvien.getPhone());
            ps.setObject(9, nhanvien.getPassWord());
            ps.setObject(10, nhanvien.getRule());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean update(String id, Staff nhanvien) {
        int check = 0;
        String sql = "UPDATE [dbo].[staff]\n"
                + "   SET \n" + "[name]=?\n" + ",[dateOfBirth]=?\n" + " ,[sex]=?\n" + ",[address]=?\n" + ",[user]=?\n" + ",[phone]=?\n" + ",[rule]=?\n" + " WHERE id= ?";
        try {
            Connection con = JdbcUntil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, nhanvien.getName());
            ps.setObject(2, nhanvien.getDateOfBirth());
            ps.setObject(3, nhanvien.getSex());
            ps.setObject(4, nhanvien.getAddress());
            ps.setObject(5, nhanvien.getUser());
            ps.setObject(6, nhanvien.getPhone());
            ps.setObject(7, nhanvien.getRule());
            ps.setObject(8, id);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public boolean delete(String id) {
        int check = 0;
        String sql = "DELETE FROM [dbo].[staff]\n"
                + "      WHERE Id = ?";
        try {
            Connection con = JdbcUntil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, id);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check > 0;
    }

    public Staff timkiem(String code) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Select * from staff where staff.code=? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("Id");
                String ma = rs.getString("code");
                String name = rs.getString("name");
                String dateOfBirth = rs.getString("dateOfBirth");
                String sex = rs.getString("sex");
                String address = rs.getString("address");
                String idPersonCard = rs.getString("idPersonCard");
                String user = rs.getString("user");
                String phone = rs.getString("phone");
                String rule = rs.getString("rule");
                String pass= rs.getString("pass");
                Staff nhanvien = new Staff(id, ma, name, dateOfBirth, sex, address, idPersonCard,phone, user, pass, rule);
                return nhanvien;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
