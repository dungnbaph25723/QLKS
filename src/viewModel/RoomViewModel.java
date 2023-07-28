/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author TGDD
 */
public class RoomViewModel {
    private String id;
    private String Status;
    private String KindOfRoom;
    private String code;
    private String roomNumber;
    private String area;
    private String location;
    private String price;
    private String idPromotion;

    public RoomViewModel() {
    }

    public RoomViewModel(String id, String Status, String KindOfRoom,String idPromotion, String code, String roomNumber, String area, String location, String price) {
        this.id = id;
        this.Status = Status;
        this.KindOfRoom = KindOfRoom;
        this.code = code;
        this.roomNumber = roomNumber;
        this.area = area;
        this.location = location;
        this.price = price;
        this.idPromotion = idPromotion;
    }

    public String getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(String idPromotion) {
        this.idPromotion = idPromotion;
    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getKindOfRoom() {
        return KindOfRoom;
    }

    public void setKindOfRoom(String KindOfRoom) {
        this.KindOfRoom = KindOfRoom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    
}
