/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.Item;
import model.Room;
import model.RoomItem;
import respository.RoomItemRepo;
import viewModel.RoomItemVMD;

/**
 *
 * @author admin
 */
public class RoomItemService {

    private RoomItemRepo rr;

    public RoomItemService() {
        rr = new RoomItemRepo();
    }

    public ArrayList<RoomItemVMD> getListRI() {
        return rr.getAllRI();
    }

    public ArrayList<Item> getListI() {
        return rr.getAllI();
    }

    public void insert(RoomItemVMD ri) {
        rr.insert(ri);
    }

    public void delete(String ma) {
        rr.delete(ma);
    }

    public void update(String id, RoomItemVMD ri) {
        rr.update(id, ri);
    }

}
