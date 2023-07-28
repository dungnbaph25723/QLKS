package viewModel;

public class PhieuDVViewModel {

    private String codeBill;
    private String roomNumber;
    private String nameService;
    private String codeService;
    private String dateOfHire;
    private String times;
    private String promotionService;
    private String priceService;

    public PhieuDVViewModel() {
    }

    public PhieuDVViewModel(String codeBill, String roomNumber, String nameService, String codeService, String dateOfHire, String times, String promotionService, String priceService) {
        this.codeBill = codeBill;
        this.roomNumber = roomNumber;
        this.nameService = nameService;
        this.codeService = codeService;
        this.dateOfHire = dateOfHire;
        this.times = times;
        this.promotionService = promotionService;
        this.priceService = priceService;
    }

    public String getCodeBill() {
        return codeBill;
    }

    public void setCodeBill(String codeBill) {
        this.codeBill = codeBill;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public String getCodeService() {
        return codeService;
    }

    public void setCodeService(String codeService) {
        this.codeService = codeService;
    }

    public String getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(String dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getPromotionService() {
        return promotionService;
    }

    public void setPromotionService(String promotionService) {
        this.promotionService = promotionService;
    }

    public String getPriceService() {
        return priceService;
    }

    public void setPriceService(String priceService) {
        this.priceService = priceService;
    }
    
    
    
                                   
        
   
    
}
