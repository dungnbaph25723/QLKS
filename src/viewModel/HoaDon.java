package viewModel;

public class HoaDon {

    private String id;
    private String nameClient;
    private String nameStaff;
    private String code;
    private String price;
    private String status;
    private String date;
    private String dateCheckIn;
    private String dateCheckOut;

    public HoaDon() {
    }

    public HoaDon(String id, String nameClient, String nameStaff, String code, String price, String status, String date, String dateCheckIn, String dateCheckOut) {
        this.id = id;
        this.nameClient = nameClient;
        this.nameStaff = nameStaff;
        this.code = code;
        this.price = price;
        this.status = status;
        this.date = date;
        this.dateCheckIn = dateCheckIn;
        this.dateCheckOut = dateCheckOut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getNameStaff() {
        return nameStaff;
    }

    public void setNameStaff(String nameStaff) {
        this.nameStaff = nameStaff;
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

    public String getDateCheckIn() {
        return dateCheckIn;
    }

    public void setDateCheckIn(String dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }

    public String getDateCheckOut() {
        return dateCheckOut;
    }

    public void setDateCheckOut(String dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
    }
    
    

}
