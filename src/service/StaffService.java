/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Staff;
import respository.StaffRepo;
import respository.StaffRepositoryImpl;
import utilities.ReadWriteData;
import viewModel.staffviewmodel;

/**
 *
 * @author FPTSHOP
 */
public class StaffService {

    private StaffRepo repo;
    private StaffRepositoryImpl nhanvienRepository = new StaffRepositoryImpl();
    private ReadWriteData readWriteData;

    public StaffService() {
        repo = new StaffRepo();
        readWriteData = new ReadWriteData();
    }

    public List<String> getByUser(String user, String pass) {
        if (user.equals("") || pass.equals("")) {
            return null;
        }
        return repo.selectByUser(user, pass);
    }

    public List<staffviewmodel> getAll() {
        return nhanvienRepository.getAll();
    }

    public String add(Staff nhanvien) {
        boolean addnhanvien = nhanvienRepository.add(nhanvien);

        if (addnhanvien) {
            try {
                readWriteData.ghidl(Integer.parseInt(nhanvien.getCode().substring(2)), "NhanVien.txt");
            } catch (IOException ex) {
                Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "Thêm thành công";
        }
        return "Thêm thất bại";
    }

    public String update(String id, Staff nhanvien) {
        boolean updatenhanvien = nhanvienRepository.update(id, nhanvien);
        if (updatenhanvien) {
            return "Sửa thành công";
        }
        return "Sửa thất bại";
    }

    public String delete(String id) {
        boolean deletenhanvien = nhanvienRepository.delete(id);
        if (deletenhanvien) {
            return "Xóa thành công id" + id;
        }
        return "Xóa thất bại";
    }

    public Staff tim(String code) {
        return this.nhanvienRepository.timkiem(code);
    }

    public boolean sodt(String number) {
        return number.matches("^[0-9]");
    }
}
