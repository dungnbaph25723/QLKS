
package viewModel;


public class ThongtinCheckout {
       String idroom;
       String idbill;
       String tenkhachhang;
       String CCCD;
       String maHD;
       String sophong;
       String checkin;
       String checkout;
       String giaphong;
       String giamgia;
       int trangthaiHD;
       String thanhtien;

    public ThongtinCheckout() {
    }

    public ThongtinCheckout(String idroom, String idbill, String tenkhachhang, String CCCD, String maHD, String sophong, String checkin, String checkout, String giaphong, String giamgia, int trangthaiHD, String thanhtien) {
        this.idroom = idroom;
        this.idbill = idbill;
        this.tenkhachhang = tenkhachhang;
        this.CCCD = CCCD;
        this.maHD = maHD;
        this.sophong = sophong;
        this.checkin = checkin;
        this.checkout = checkout;
        this.giaphong = giaphong;
        this.giamgia = giamgia;
        this.trangthaiHD = trangthaiHD;
        this.thanhtien = thanhtien;
    }

    public String getIdroom() {
        return idroom;
    }

    public void setIdroom(String idroom) {
        this.idroom = idroom;
    }

    public String getIdbill() {
        return idbill;
    }

    public void setIdbill(String idbill) {
        this.idbill = idbill;
    }

    public String getTenkhachhang() {
        return tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        this.tenkhachhang = tenkhachhang;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getSophong() {
        return sophong;
    }

    public void setSophong(String sophong) {
        this.sophong = sophong;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getGiaphong() {
        return giaphong;
    }

    public void setGiaphong(String giaphong) {
        this.giaphong = giaphong;
    }

    public String getGiamgia() {
        return giamgia;
    }

    public void setGiamgia(String giamgia) {
        this.giamgia = giamgia;
    }

    public int getTrangthaiHD() {
        return trangthaiHD;
    }

    public void setTrangthaiHD(int trangthaiHD) {
        this.trangthaiHD = trangthaiHD;
    }

    public String getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(String thanhtien) {
        this.thanhtien = thanhtien;
    }

   
       
}
