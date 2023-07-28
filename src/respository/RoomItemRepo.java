/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Item;
import model.Room;
import model.RoomItem;
import utilities.JdbcUntil;
import viewModel.RoomItemVMD;

/**
 *
 * @author admin
 */
public class RoomItemRepo {

    public ArrayList<Item> getAllI() {
        ArrayList<Item> listI = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
        String sql = "select id,code,name from item";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                Item i = new Item(id, code, name);
                listI.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listI;

    }

    public void insert(RoomItemVMD ri) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "insert into roomItem(roomId,itemId,status,amount) values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ri.getRoomID());
            ps.setString(2, ri.getItemID());
            ps.setString(3, ri.getStatusRI());
            ps.setInt(4, ri.getAmountRI());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String ma) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "delete from roomItem where roomId=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
            System.out.println("Xoa thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String id, RoomItemVMD ri) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "update roomItem set status=?,amount=? where roomId=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ri.getStatusRI());
            ps.setInt(2, ri.getAmountRI());
            ps.setString(3, id);
            ps.execute();
            System.out.println("Sua thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<RoomItemVMD> getAllRI() {
        ArrayList<RoomItemVMD> listR = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
        String sql = "select A.[RoomNumber]as 'tenphong',C.[name] as 'tentb', A.code as 'maRoom', A.id as 'RoomID', C.code as 'maItem', C.id as 'ItemID', B.status, B.amount from Room A inner join roomItem B on A.id=B.roomId "
                + "inner join item C on B.itemId=C.id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String soPhong = rs.getString("tenPhong");
                String tentb = rs.getString("tentb");
                String roomC = rs.getString("maRoom");
                String itemC = rs.getString("maItem");
                String status = rs.getString("status");
                int amount = rs.getInt("amount");
                String roomid = rs.getString("RoomID");
                String itemid = rs.getString("ItemID");
                RoomItemVMD r = new RoomItemVMD(roomC,0, soPhong, roomid, itemid, itemC, tentb, status, amount);
                listR.add(r);
            }
            System.out.println("ket noi item thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listR;
    }

}
