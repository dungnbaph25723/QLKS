/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import model.Item;
import viewModel.ViewModelItem;
import java.util.ArrayList;
import java.util.List;
import respository.ViewModelItemRepo;
import service.IService;

/**
 *
 * @author Admin
 */
public class ViewModelItemService {

    private ViewModelItemRepo repo;

    public ViewModelItemService() {
        repo = new ViewModelItemRepo();
    }

    public List<ViewModelItem> getAll(String idRoom) {
        return repo.getAll(idRoom);
    }
}
