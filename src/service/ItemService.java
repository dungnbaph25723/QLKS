/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.Impl;

import java.util.ArrayList;
import model.Item;
import respository.ItemRepo;


/**
 *
 * @author admin
 */
public class ItemService  {

   private  ItemRepo ir;

    public ItemService() {
        ir = new ItemRepo();
    }

     
    public ArrayList<Item> getListI() {
        return ir.getAll();
    }

     
    public void update(Item i, String id) {
        ir.update(i, id);
    }

     
    public void insert(Item i) {
        ir.insert(i);
    }

     
    public void delete(String id) {
        ir.delete(id);
    }

     
    public ArrayList<Item> getSearch(String ma) {
        return ir.getSearch(ma);
    }

}
