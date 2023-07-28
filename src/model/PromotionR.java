package model;

public class PromotionR {

    private String id;
    private String value;
    private String dateStart;
    private String dateEnd;
    private String code;

    public PromotionR() {
    }

    public PromotionR(String id, String value, String dateStart, String dateEnd, String code) {
        this.id = id;
        this.value = value;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.code = code;
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
