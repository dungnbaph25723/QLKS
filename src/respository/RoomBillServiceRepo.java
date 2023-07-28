package respository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RoomBillService;
import utilities.JdbcUntil;

public class RoomBillServiceRepo {

    Connection conn = JdbcUntil.getConnection();

    public void insert(RoomBillService roomBillService) {
        String sql = "insert into RoomBillService(idBill,idService,idRoom,priceService,promotionService,dateofHire,times)values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomBillService.getIdBill());
            ps.setString(2, roomBillService.getIdService());
            ps.setString(3, roomBillService.getIdRoom());
            ps.setString(4, roomBillService.getPriceService());
            ps.setString(5, roomBillService.getPromotionService());
            ps.setString(6, roomBillService.getDateofHire());
            ps.setInt(7, roomBillService.getTimes());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RoomBillServiceRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<RoomBillService> searchByID(String idBill, String idService, String idRoom) {
        List<RoomBillService> list = new ArrayList<>();
        String sql = "select * from roombillService where idBill = ? and idService = ? and idRoom = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idBill);
            ps.setString(2, idService);
            ps.setString(3, idRoom);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                RoomBillService roomBillService = new RoomBillService();
                roomBillService.setIdBill(rs.getString("idbill"));
                roomBillService.setIdRoom(rs.getString("idRoom"));
                roomBillService.setIdService(rs.getString("idservice"));
                roomBillService.setPriceService("priceService");
                roomBillService.setPromotionService("promotionService");
                roomBillService.setDateofHire(rs.getString("dateOfHire"));
                roomBillService.setTimes(rs.getInt("times"));
                list.add(roomBillService);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoomBillServiceRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void update(RoomBillService roomBillService, String idBill, String idService, String idRoom) {
        String sql = "update RoomBillService set times = ?, dateofHire = ? where idBill= ? and idService = ? and idRoom = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomBillService.getTimes());
            ps.setString(2, roomBillService.getDateofHire());
            ps.setString(3, roomBillService.getIdBill());
            ps.setString(4, roomBillService.getIdService());
            ps.setString(5, roomBillService.getIdRoom());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RoomBillServiceRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
