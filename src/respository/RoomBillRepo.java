package respository;

import utilities.JdbcUntil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BillRoom;

public class RoomBillRepo {

    Connection conn = JdbcUntil.getConnection();

    public void insert(BillRoom billRoom) {
        String sql = "insert into RoomBill(roomId,billId,priceRoom,promotionRoom,datecheckin,datecheckOut) values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, billRoom.getRoomId());
            ps.setString(2, billRoom.getBillId());
            ps.setString(3, billRoom.getPriceRoom());
            ps.setString(4, billRoom.getPromotionRoom());
            ps.setString(5, billRoom.getDateCheckIn());
            ps.setString(6, billRoom.getDateCheckout());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RoomBillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(String id) {
        String sql = "delete roomBill where Billid=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(BillRoom billRoom, String idbill, String idroom) {
        String sql = "update roombill set datecheckout = ? where idbill = ? and idroom=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, billRoom.getDateCheckIn());
            ps.setString(1, billRoom.getDateCheckout());
            ps.setString(2, idbill);
            ps.setString(3, idroom);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println("sai insert");
        }
    }

    public List<BillRoom> getAll() {
        String sql = "select * from roomBill order by datecheckin desc";
        List<BillRoom> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                BillRoom billRoom = new BillRoom();
                billRoom.setBillId(rs.getString("billid"));
                billRoom.setRoomId(rs.getString("roomid"));
                billRoom.setPriceRoom(rs.getString("priceRoom"));
                billRoom.setPromotionRoom(rs.getString("promotionRoom"));
                billRoom.setDateCheckIn(rs.getString("dateCheckIn"));
                billRoom.setDateCheckout(rs.getString("datecheckout"));
                list.add(billRoom);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
