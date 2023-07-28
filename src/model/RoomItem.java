package model;

public class RoomItem {

    private String roomId;
    private String itemId;
    private String status;
    private int amount;

    public RoomItem() {
    }

    public RoomItem(String roomId, String itemId, String status, int amount) {
        this.roomId = roomId;
        this.itemId = itemId;
        this.status = status;
        this.amount = amount;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
