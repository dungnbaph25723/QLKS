/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author admin
 */
public class RoomItemVMD {

    private String codeR;
    private int kinhOfR;
    private String numberR;
    private String roomID;
    private String itemID;
    private String codeI;
    private String nameI;
    private String statusRI;
    private int amountRI;

    public RoomItemVMD() {
    }

    public RoomItemVMD(String codeR, String codeI, String statusRI, int amountRI,String roomID, String itemID ) {
        this.codeR = codeR;
        this.roomID = roomID;
        this.itemID = itemID;
        this.codeI = codeI;
        this.statusRI = statusRI;
        this.amountRI = amountRI;
    }

    public RoomItemVMD(String roomID, String itemID, String statusRI, int amountRI) {
        this.roomID = roomID;
        this.itemID = itemID;
        this.statusRI = statusRI;
        this.amountRI = amountRI;
    }

    public RoomItemVMD(String codeR, int kinhOfR, String numberR, String roomID, String itemID, String codeI, String nameI, String statusRI, int amountRI) {
        this.codeR = codeR;
        this.kinhOfR = kinhOfR;
        this.numberR = numberR;
        this.roomID = roomID;
        this.itemID = itemID;
        this.codeI = codeI;
        this.nameI = nameI;
        this.statusRI = statusRI;
        this.amountRI = amountRI;
    }

    
 
    public String getCodeR() {
        return codeR;
    }

    public void setCodeR(String codeR) {
        this.codeR = codeR;
    }

    public int getKinhOfR() {
        return kinhOfR;
    }

    public void setKinhOfR(int kinhOfR) {
        this.kinhOfR = kinhOfR;
    }

    public String getNumberR() {
        return numberR;
    }

    public void setNumberR(String numberR) {
        this.numberR = numberR;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getCodeI() {
        return codeI;
    }

    public void setCodeI(String codeI) {
        this.codeI = codeI;
    }

    public String getNameI() {
        return nameI;
    }

    public void setNameI(String nameI) {
        this.nameI = nameI;
    }

    public String getStatusRI() {
        return statusRI;
    }

    public void setStatusRI(String statusRI) {
        this.statusRI = statusRI;
    }

    public int getAmountRI() {
        return amountRI;
    }

    public void setAmountRI(int amountRI) {
        this.amountRI = amountRI;
    }

}
