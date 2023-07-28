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
import viewModel.ViewModelItem;

public class ViewModelItemRepo {

    Connection conn = JdbcUntil.getConnection();

    public List<ViewModelItem> getAll(String idRoom) {
        String sql = "select item.[name],amount,[status] from roomitem inner join item on roomItem.itemId= item.id where roomId=?";
        PreparedStatement ps;
        List<ViewModelItem> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, idRoom);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                ViewModelItem item = new ViewModelItem();
                item.setName(rs.getString("name"));
                item.setAmount(rs.getString("amount"));
                item.setStatus(rs.getString("status"));
                list.add(item);
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewModelItemRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
