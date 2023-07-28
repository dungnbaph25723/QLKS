/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

import java.util.Date;


public class staffviewmodel {
    private String id;
    private String code;
    private String name;
    private String dateOfBirth;
    private String sex;
    private String address;
    private String idPersonCard;
    private String user;
    private String phone;
    private String rule;

    public staffviewmodel() {
    }

    public staffviewmodel(String id, String code, String name, String dateOfBirth, String sex, String address, String idPersonCard, String user, String phone, String rule) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.address = address;
        this.idPersonCard = idPersonCard;
        this.user = user;
        this.phone = phone;
        this.rule = rule;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Object[] toDataRow() {
        return new Object[]{code,name, dateOfBirth,sex,address,idPersonCard,phone};
    }
}
