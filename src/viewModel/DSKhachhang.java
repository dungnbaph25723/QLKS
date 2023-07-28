
package viewModel;
import java.util.Calendar;
import java.util.Date;

public class DSKhachhang {
    private String code;
    private String name;
    private String dateOfBirth;
    private String sex;
    private String address;
    private String idPersonCard;
    private String customPhone;
    
    public int tinhtuoi(){
        int tuoi;
        int x = Integer.parseInt(this.dateOfBirth.substring(0, 4));
        return tuoi=(2022-x);
    }
    
    public DSKhachhang() {
    }

    public DSKhachhang(String code, String name, String dateOfBirth, String sex, String address, String idPersonCard, String customPhone) {
        this.code = code;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.address = address;
        this.idPersonCard = idPersonCard;
        this.customPhone = customPhone;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdPersonCard() {
        return idPersonCard;
    }

    public void setIdPersonCard(String idPersonCard) {
        this.idPersonCard = idPersonCard;
    }

    public String getCustomPhone() {
        return customPhone;
    }

    public void setCustomPhone(String customPhone) {
        this.customPhone = customPhone;
    }

}
