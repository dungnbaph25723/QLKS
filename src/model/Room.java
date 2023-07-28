package model;

public class Room {

    private String id;
    private String status;
    private String kindOfRoom;
    private String idPromotion;
    private String code;
    private String roomNumber;
    private String area;
    private String location;
    private String price;

    public Room() {
    }

    public Room(String id, String status, String kindOfRoom, String idPromotion, String code, String roomNumber, String area, String location, String price) {
        this.id = id;
        this.status = status;
        this.kindOfRoom = kindOfRoom;
        this.idPromotion = idPromotion;
        this.code = code;
        this.roomNumber = roomNumber;
        this.area = area;
        this.location = location;
        this.price = price;
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
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKindOfRoom() {
        return kindOfRoom;
    }

    public void setKindOfRoom(String kindOfRoom) {
        this.kindOfRoom = kindOfRoom;
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
