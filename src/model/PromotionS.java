package model;


public class PromotionS {
    private String id;
    private String code;
    private String value;
    private String dateStart;
    private String dateEnd;

    public PromotionS() {
    }

    public PromotionS(String id, String code, String value, String dateStart, String dateEnd) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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
