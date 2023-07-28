package model;

import java.util.Date;

public class Bill {

    private String id;
    private String idClient;
    private String idStaff;
    private String code;

    private String price;
    private String status;
    private String date;

    public Bill() {
    }

    public Bill(String id, String idClient, String idStaff, String code, String price, String status, String date) {
        this.id = id;
        this.idClient = idClient;
        this.idStaff = idStaff;
        this.code = code;

        this.price = price;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(String idStaff) {
        this.idStaff = idStaff;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
