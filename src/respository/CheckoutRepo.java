package respository;

import java.util.ArrayList;
import viewModel.DVcheckout;
import java.sql.Connection;
import java.sql.ResultSet;
import utilities.JdbcUntil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import viewModel.ThongtinCheckout;

public class CheckoutRepo {

    public ArrayList<DVcheckout> GetDVcheckout(String idroom,String billid) {
        ArrayList<DVcheckout> list = new ArrayList<>();
        String sql = "Select IdRoom,code,name,priceService,promotionService,times \n"
                + "from Roombillservice inner join service on Roombillservice.IdService=service.id\n"
                + "where IdRoom = ? and idbill=?";
        Connection conn = JdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idroom);
            ps.setString(2, billid);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("idroom");
                String ma = rs.getString("code");
                String ten = rs.getString("name");
                String gia = rs.getString("priceService");
                String giamgia = rs.getString("promotionService");
                String soluong = rs.getString("times");
                DVcheckout dv = new DVcheckout(id, ma, ten, gia, giamgia, soluong);
                list.add(dv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ArrayList<ThongtinCheckout> GetthongtinCheckout(String idroom, String idbill) {
        ArrayList<ThongtinCheckout> list = new ArrayList<>();
        String sql = "Select Roomid,Billid,name,idPersonCard, bill.code,room.roomNumber,dateCheckIn,dateCheckOut,priceRoom,promotionRoom,bill.status,bill.Price\n"
                + "from room inner join RoomBill on room.id=RoomBill.RoomId\n"
                + "inner join bill on RoomBill.BillId=bill.id\n"
                + "inner join Client on bill.idClient=Client.id where roomid=? and billId=?";
        Connection conn = JdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idroom);
            ps.setString(2, idbill);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                ThongtinCheckout tt = new ThongtinCheckout();
                tt.setIdroom(rs.getString("roomid"));
                tt.setIdbill(rs.getString("billid"));
                tt.setTenkhachhang(rs.getString("name"));
                tt.setCCCD(rs.getString("idPersonCard"));
                tt.setMaHD(rs.getString("code"));
                tt.setSophong(rs.getString("roomNumber"));
                tt.setCheckin(rs.getString("dateCheckIn"));
                tt.setCheckout(rs.getString("dateCheckOut"));
                tt.setGiaphong(rs.getString("priceRoom"));
                tt.setGiamgia(rs.getString("promotionRoom"));
                tt.setTrangthaiHD(rs.getInt("status"));
                tt.setThanhtien(rs.getString("price"));
                list.add(tt);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
