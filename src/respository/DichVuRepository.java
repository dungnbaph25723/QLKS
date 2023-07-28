/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import model.Item;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import utilities.JdbcUntil;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RoomBillService;
import viewModel.PhieuDVViewModel;

public class DichVuRepository {

    private Connection con = JdbcUntil.getConnection();

    public void delete(String id) {
        String sql = "delete from RoomBillService where idBill= ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Boolean update(String id, PhieuDVViewModel dv) {
        int check = 0;
        try {
            Connection conn = JdbcUntil.getConnection();
            String sqlUpdate = "Update roombillservice set priceService=?,promotionService=?,dateofHire=? where idservice = ? ";
            PreparedStatement ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, dv.getPromotionService());
            ps.setString(2, id);
            ps.execute();
            check = ps.executeUpdate();
            return check > 0;
            //   System.out.println("truy van thanh cong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PhieuDVViewModel> getAll() {
        ArrayList<PhieuDVViewModel> list = new ArrayList<>();
        String sql = "select  bill.code,Room.roomNumber,service.name,service.code,dateOfHire,times,promotionService,priceService from RoomBillService inner join service on RoomBillService.IdService= service.id inner join RoomBill on RoomBill.BillId=RoomBillService.IdBill inner join room on room.id= RoomBill.RoomId inner join bill on bill.id=RoomBill.BillId";
        Connection conn = JdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                PhieuDVViewModel dv = new PhieuDVViewModel();

                dv.setCodeBill(rs.getString(1));
                dv.setRoomNumber(rs.getString(2));
                dv.setNameService(rs.getString(3));
                dv.setCodeService(rs.getString(4));
                dv.setDateOfHire(rs.getString(5));
                dv.setTimes(rs.getString(6));
                dv.setPromotionService(rs.getString(7));
                dv.setPriceService(rs.getString(8));

                list.add(dv);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String getId(String code) {
        String sql = "select idservice from roombillService inner join service on roombillservice.idservice = service.id where service.code = ?";
        Connection conn = JdbcUntil.getConnection();
        String string = "";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "idservice");
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                string = (rs.getString("code"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return string;
    }
}
