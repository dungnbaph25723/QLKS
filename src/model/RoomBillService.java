package model;

import java.util.Date;

public class RoomBillService {

    private String idBill;
    private String idService;
    private String idRoom;
    private String priceService;
    private String promotionService;
    private String dateofHire;
    private int times;

    public RoomBillService() {
    }

    public RoomBillService(String idBill, String idService, String idRoom, String priceService, String promotionService, String dateofHire) {
        this.idBill = idBill;
        this.idService = idService;
        this.idRoom = idRoom;
        this.priceService = priceService;
        this.promotionService = promotionService;
        this.dateofHire = dateofHire;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getDateofHire() {
        return dateofHire;
    }

    public void setDateofHire(String dateofHire) {
        this.dateofHire = dateofHire;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getPriceService() {
        return priceService;
    }

    public void setPriceService(String priceService) {
        this.priceService = priceService;
    }

    public String getPromotionService() {
        return promotionService;
    }

    public void setPromotionService(String promotionService) {
        this.promotionService = promotionService;
    }
}
