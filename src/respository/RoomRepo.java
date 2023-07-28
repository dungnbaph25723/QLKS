package respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bill;
import model.Room;
import utilities.JdbcUntil;

public class RoomRepo {

    Connection conn = JdbcUntil.getConnection();

    public List<Room> getRoomByNumber(String number) {
        String sql = "select * from Room where roomNUmber = ?";
        List<Room> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, number);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getString("id"));
                room.setStatus(rs.getString("Status"));
                room.setKindOfRoom(rs.getString("KindOfRoom"));
                room.setIdPromotion(rs.getString("idpromotion"));
                room.setCode(rs.getString("Code"));
                room.setArea(rs.getString("area"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setLocation(rs.getString("Location"));
                room.setPrice(rs.getString("Price"));
                list.add(room);
            }
        } catch (SQLException ex) {
        }
        return list;
    }

    public List<Room> getAll() {
        String sql = "  select * from Room order by roomNumber asc";
        List<Room> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getString("id"));
                room.setStatus(rs.getString("Status"));
                room.setKindOfRoom(rs.getString("KindOfRoom"));
                room.setIdPromotion(rs.getString("idpromotion"));
                room.setCode(rs.getString("Code"));
                room.setArea(rs.getString("area"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setLocation(rs.getString("Location"));
                room.setPrice(rs.getString("Price"));
                list.add(room);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoomRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void update(String status, String numberRoom) {
        String sql = "update room set [status]=? where roomNumber =? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(status));
            ps.setString(2, numberRoom);
            ps.execute();
            ResultSet rs = ps.getResultSet();
        } catch (SQLException ex) {
            Logger.getLogger(RoomRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//Manh
    public Room getSearchRoom(String roomNumber) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Select room.id,room.status,room.kindOfRoom,room.code,room.roomNumber,room.area,room.location,room.price,idpromotion from room  where room.roomnumber=? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomNumber);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("Id");
                String Status = rs.getString("Status");
                String kor = rs.getString("KindOfRoom");
                String ma = rs.getString("code");
                String rNb = rs.getString("roomNumber");
                String area = rs.getString("area");
                String location = rs.getString("location");
                String price = rs.getString("price");
                String idpro = rs.getString("idPromotion");
                Room r = new Room(id, Status, kor, idpro, ma, rNb, area, location, price);
                return r;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void insert(Room room) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Insert into Room (Status,KindOfRoom,idPromotion,code,roomNumber,area,location,price) values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(room.getStatus()));
            ps.setInt(2, Integer.parseInt(room.getKindOfRoom()));
            ps.setString(3, room.getIdPromotion());
            ps.setString(4, room.getCode());
            ps.setString(5, room.getRoomNumber());
            ps.setString(6, room.getArea());
            ps.setString(7, room.getLocation());
            ps.setString(8, room.getPrice());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateById(String id, Room room) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Update Room set Status=?,KindOfRoom=?,idPromotion=?,code=?,roomNumber=?,area=?,location=?,price=? where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(room.getStatus()));
            ps.setInt(2, Integer.parseInt(room.getKindOfRoom()));
            ps.setString(3, room.getIdPromotion());
            ps.setString(4, room.getCode());
            ps.setString(5, room.getRoomNumber());
            ps.setString(6, room.getArea());
            ps.setString(7, room.getLocation());
            ps.setString(8, room.getPrice());
            ps.setString(9, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Delete from Room where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

//    public List<Room> getAll() {
//        ArrayList<Room> listRoom = new ArrayList<>();
//        try {
//            Connection conn = JdbcUntil.getConnection();
//            String sql = "Select room.id,room.status,room.kindOfRoom,room.code,room.roomNumber,room.area,room.location,room.price,promotionR.Id as 'idPro' from room inner join promotionR on room.idPromotion=promotionR.Id ";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.execute();
//            ResultSet rs = ps.getResultSet();
//            while (rs.next()) {
//                String id = rs.getString("Id");
//                String Status = String.valueOf(rs.getInt("Status"));
//                String kor = String.valueOf(rs.getInt("KindOfRoom"));
//                String code = rs.getString("code");
//                String rNb = rs.getString("roomNumber");
//                String area = rs.getString("area");
//                String location = rs.getString("location");
//                String price = rs.getString("price");
//                String idpro = rs.getString("idPro");
//                Room r = new Room(id, Status, kor, idpro, code, rNb, area, location, price);
//                listRoom.add(r);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return listRoom;
//    }
}
