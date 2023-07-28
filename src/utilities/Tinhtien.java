package utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.RoomBillService;

public class Tinhtien {

    StringHandling handling = new StringHandling();
    Connection conn = JdbcUntil.getConnection();
    RoomBillService billService = new RoomBillService();

    public Tinhtien() {

    }

    public double tiSoCheckOut(String dateCheckOut, String billId) {
        String sqlTinhGio = "select tang = case when datediff(MINUTE,dateCheckout,?) <= 0 then 1.0 when  datediff(MINUTE,dateCheckout,?) <= 180 then 1.3 when  datediff(MINUTE,dateCheckout,?) <= 360 then 1.5 when  datediff(MINUTE,dateCheckout,?) >360 then 2.0 end from roomBill where billid=?";
        double tiSoCheckOut = 1;
        try {
            PreparedStatement ps = conn.prepareStatement(sqlTinhGio);
            ps.setString(1, dateCheckOut);
            ps.setString(2, dateCheckOut);
            ps.setString(3, dateCheckOut);
            ps.setString(4, dateCheckOut);
            ps.setString(5, billId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                tiSoCheckOut = rs.getFloat("tang");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tinhtien.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tiSoCheckOut;
    }

    public double tiSoCheckIn(String dateCheckIn, String billId) {
        String sqlTinhGioCheckIn = "select datediff(MINUTE,?,dateCheckIn) as'tg', tang = case when datediff(MINUTE,? ,dateCheckIn) <  540 then 1.5 when  datediff(MINUTE,? ,dateCheckIn) < 840 then 1.3 when  datediff(MINUTE,? ,dateCheckIn) >=840 then 1.0 end from roomBill where billId=?";
        double tiSoCheckIn = 1;
        dateCheckIn = handling.splitDateCheckIn(dateCheckIn);
        try {
            PreparedStatement ps = conn.prepareStatement(sqlTinhGioCheckIn);
            ps.setString(1, dateCheckIn);
            ps.setString(2, dateCheckIn);
            ps.setString(3, dateCheckIn);
            ps.setString(4, dateCheckIn);
            ps.setString(5, billId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                tiSoCheckIn = rs.getFloat("tang");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tinhtien.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tiSoCheckIn;
    }

    public String tinhTienDv(String billId) {
        String sql = "select sum((RoomBillService.priceService-RoomBillService.promotionService) * times) as'tienDv' from roomBillService where idBill=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, billId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return String.valueOf(rs.getInt("tiendv"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tinhtien.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String tinhTienPhong(double tiSoCheckOut, double tiSoCheckIn, String billId) {
        String sql = "  select sum((RoomBill.priceRoom-RoomBill.promotionRoom) * datediff(DAY,dateCheckin,dateCheckout))*(1 + ? + ?) as'tienPhong' from roomBill where billId=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, tiSoCheckOut-1);
            ps.setDouble(2, tiSoCheckIn-1);
            ps.setString(3, billId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return String.valueOf(rs.getInt("tienPhong"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tinhtien.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        Tinhtien tt = new Tinhtien();
//        tt.tinhTienDv();
    }
}
