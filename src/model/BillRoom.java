package model;

public class BillRoom {

    private String roomId;
    private String billId;
    private String priceRoom;
    private String promotionRoom;
    private String dateCheckIn;
    private String dateCheckout;

    public BillRoom() {
    }

    public BillRoom(String roomId, String billId, String priceRoom, String promotionRoom, String dateCheckIn, String dateCheckout) {
        this.roomId = roomId;
        this.billId = billId;
        this.priceRoom = priceRoom;
        this.promotionRoom = promotionRoom;
        this.dateCheckIn = dateCheckIn;
        this.dateCheckout = dateCheckout;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPriceRoom() {
        return priceRoom;
    }

    public void setPriceRoom(String priceRoom) {
        this.priceRoom = priceRoom;
    }

    public String getPromotionRoom() {
        return promotionRoom;
    }

    public void setPromotionRoom(String promotionRoom) {
        this.promotionRoom = promotionRoom;
    }

    public String getDateCheckIn() {
        return dateCheckIn;
    }

    public void setDateCheckIn(String dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }

    public String getDateCheckout() {
        return dateCheckout;
    }

    public void setDateCheckout(String dateCheckout) {
        this.dateCheckout = dateCheckout;
    }

}
