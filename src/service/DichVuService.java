/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import java.util.List;
import model.Service;
import viewModel.PhieuDVViewModel;
import respository.DichVuRepository;
import respository.ServiceRepo;

public class DichVuService {

    private DichVuRepository DV = new DichVuRepository();
    private ServiceRepo ser = new ServiceRepo();

    public ArrayList<PhieuDVViewModel> list() {
        return DV.getAll();
    }

}
