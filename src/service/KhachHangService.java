/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import respository.ClientRepo;
import respository.KhachHangRepo;
import viewModel.DSKhachhang;
import utilities.ReadWriteData;
import utilities.StringHandling;

public class KhachHangService {

    private KhachHangRepo KHRepo;

    public KhachHangService() {
        KHRepo = new KhachHangRepo();
    }

    public ArrayList<Client> getList() {
        return KHRepo.getAll();
    }

    public String insert(Client c) {
        boolean add = KHRepo.insert(c);
        if (add) {
            return "them thanh cong";
        } else {
            return "ko thanh cong";
        }
    }

    public String update(String ma, Client c) {
        boolean add = KHRepo.update(ma, c);
        if (add) {
            return "sua thanh cong";
        } else {
            return "ko thanh cong";
        }
    }

    public String delete(String ma) {
        boolean add = KHRepo.delete(ma);
        if (add) {
            return "xoa thanh cong";
        } else {
            return "ko thanh cong";
        }
    }

    public ArrayList<Client> search(String ma) {
        return KHRepo.search(ma);
    }

}
