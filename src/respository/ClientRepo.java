package respository;

import model.Client;
import java.sql.Connection;
import java.sql.ResultSet;
import utilities.JdbcUntil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRepo {

    private Connection conn = JdbcUntil.getConnection();

    public void insert(Client client) throws ParseException {

        String sql = "insert into client ([name],dateOfBirth,sex,[address],idPersonCard,customPhone,code) values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getName());
            ps.setDate(2, Date.valueOf(client.getDateOfBirth()));
            ps.setString(3, client.getSex());
            ps.setString(4, client.getAddress());
            ps.setString(5, client.getIdPersonCard());
            ps.setString(6, client.getCustomPhone());
            ps.setString(7, client.getCode());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(String id) {
        String sql = "delete Client where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillRepo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void update(Client client, String id) {
        String sql = "update Client set name = ?,dateOfBirth = ?, Sex = ? , Address = ?,customphone = ? where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getName());
            ps.setString(2, client.getDateOfBirth());
            ps.setString(3, client.getSex());
            ps.setString(4, client.getAddress());
            ps.setString(5, client.getCustomPhone());
            ps.setString(6, id);
            ps.execute();
        } catch (SQLException ex) {
          
        }
    }

    public List<Client> getAll() {
        String sql = "select * from client";
        List<Client> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getString("id"));
                client.setName(rs.getString("name"));
                client.setDateOfBirth(String.valueOf(rs.getDate("dateOfBirth")));
                client.setAddress(rs.getString("address"));
                client.setIdPersonCard(rs.getString("idpersoncard"));
                client.setSex(rs.getString("sex"));
                client.setCode(rs.getString("code"));
                client.setCustomPhone(rs.getString("customphone"));
                list.add(client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Client> checkTrung(String cccd) {
        String sql = "select * from client where idpersoncard = ? ";
        List<Client> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cccd);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getString("id"));
                client.setName(rs.getString("name"));
                client.setDateOfBirth(String.valueOf(rs.getDate("dateOfBirth")));
                client.setAddress(rs.getString("address"));
                client.setIdPersonCard(rs.getString("idpersoncard"));
                client.setSex(rs.getString("sex"));
                client.setCode(rs.getString("code"));
                client.setCustomPhone(rs.getString("customphone"));
                list.add(client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
