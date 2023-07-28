
package viewModel;


public class DVcheckout {
    String id;
    String ma;
    String ten;
    String gia;
    String giamgia;
    String soluong;

    public DVcheckout() {
    }

    public DVcheckout(String id, String ma, String ten, String gia, String giamgia, String soluong) {
        this.id = id;
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.giamgia = giamgia;
        this.soluong = soluong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getGiamgia() {
        return giamgia;
    }

    public void setGiamgia(String giamgia) {
        this.giamgia = giamgia;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }
    
}
