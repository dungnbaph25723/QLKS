package model;

public class Service {

    private String id;
    private String code;
    private String name;
    private String price;
    private String notes;
    private String idPromotion;

    public Service() {
    }

    public Service(String id, String code, String name, String price, String notes, String idPromotion) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.notes = notes;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
