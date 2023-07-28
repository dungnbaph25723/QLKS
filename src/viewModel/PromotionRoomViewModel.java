
package viewModel;

public class PromotionRoomViewModel {
    private String id;
    private String code;
    private String value;
    private String dateStart;
    private String dateEnd;

    public PromotionRoomViewModel() {
    }

    public PromotionRoomViewModel(String code, String value, String dateStart, String dateEnd) {
        this.code = code;
        this.value = value;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
  
    public PromotionRoomViewModel(String id,String code, String value, String dateStart, String dateEnd) {
        this.id = id;
        this.code=code;
        this.value = value;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
