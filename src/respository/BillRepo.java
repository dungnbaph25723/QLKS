package respository;

import java.sql.Connection;
import java.util.Date;
import model.Bill;
import utilities.JdbcUntil;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillRepo {

    Connection conn = JdbcUntil.getConnection();

    public void insert(Bill bill) {
        try {
            String sql = "insert into bill(idClient,idStaff,code,Price,[status],[date]) values(?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bill.getIdClient());
            ps.setString(2, bill.getIdStaff());
            ps.setString(3, bill.getCode());
            ps.setString(4, bill.getPrice());
            ps.setString(5, bill.getStatus());
            ps.setString(6, bill.getDate());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete(String id) {
        String sql = "delete Bill where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(Bill bill, String id) {
        String sql = "update bill set Price = ?,[status] = ? where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            System.out.println(bill.getPrice());
            ps.setString(1, bill.getPrice());
            ps.setInt(2, Integer.parseInt(bill.getStatus()));
            ps.setString(3, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Bill> getAll() {
        String sql = "select * from Bill";
        List<Bill> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getString("id"));
                bill.setIdClient(rs.getString("idClient"));
                bill.setIdStaff(rs.getString("idStaff"));
                bill.setCode(rs.getString("code"));
                bill.setPrice(rs.getString("price"));
                bill.setStatus(rs.getString("status"));
                bill.setDate(String.valueOf(rs.getDate("date")));
                list.add(bill);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public String getId(String number, Date date) {
        String sql = "  select bill.id from bill "
                + "inner join RoomBill on bill.id= RoomBill.BillId"
                + " inner join room on Room.id= RoomBill.RoomId"
                + " where room.roomNumber=? "
                + "and (? between RoomBill.dateCheckIn and RoomBill.dateCheckOut) "
                + "and bill.[status]=0";
        String id = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, number);
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                id = rs.getString("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public List<Bill> searchHd(String idClient) {
        String sql = "select * from Bill where idClient=? and [status]=0";
        List<Bill> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idClient);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getString("id"));
                bill.setIdClient(rs.getString("idClient"));
                bill.setIdStaff(rs.getString("idStaff"));
                bill.setCode(rs.getString("code"));
                bill.setPrice(rs.getString("price"));
                bill.setStatus("status");
                bill.setDate("date");
                list.add(bill);
            }
        } catch (SQLException ex) {
        }
        return list;
    }
}
