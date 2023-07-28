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
import viewModel.HoaDon;

public class ViewModelHDRepo {

    Connection conn = JdbcUntil.getConnection();

    public List<HoaDon> getAll() {
        String sql = "select bill.id,Client.[name] as 'nameClient', staff.[name] as 'nameStaff', bill.code,bill.price, bill.status,bill.date, roomBill.datecheckin,roombill.datecheckout from Bill inner join Client on bill.idClient= client.id inner join staff on bill.idstaff= staff.id inner join roomBill on bill.id= roombill.billid order by [status] asc ";
        List<HoaDon> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                HoaDon bill = new HoaDon();
                bill.setId(rs.getString("id"));
                bill.setNameClient(rs.getString("nameClient"));
                bill.setNameStaff(rs.getString("nameStaff"));
                bill.setCode(rs.getString("code"));
                bill.setPrice(rs.getString("price"));
                bill.setStatus(String.valueOf(rs.getInt("status")));
                bill.setDate(String.valueOf(rs.getDate("date")));
                bill.setDateCheckIn(String.valueOf(rs.getDate("datecheckIN")));
                bill.setDateCheckOut(String.valueOf(rs.getDate("datecheckout")));
                list.add(bill);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
