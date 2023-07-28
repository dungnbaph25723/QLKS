/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PromotionR;
import model.PromotionS;
import model.PromotionS;
import respository.PromotionRRepo;
import respository.PromotionSRepo;
import utilities.ReadWriteData;

/**
 *
 * @author Admin
 */
public class PromotionSService {

    ReadWriteData readWriteData;
    private PromotionSRepo repo;

    public PromotionSService() {
        readWriteData = new ReadWriteData();
        repo = new PromotionSRepo();
    }

    public PromotionS searchPromotionS(String id, String dateEnd) {
        return repo.searchPromotionS(id, dateEnd);
    }

    public ArrayList<PromotionS> getList() {
        return this.repo.getAll();
    }

    public String insert(PromotionS p) {
        boolean addPromo = repo.insert(p);
        p.setCode(p.getCode().substring(2));
        try {
            readWriteData.ghidl(Integer.parseInt(p.getCode()), "GiamGiaDV.txt");
        } catch (IOException ex) {
            Logger.getLogger(PromotionSService.class.getName()).log(Level.SEVERE, null, ex);

        }
        if (addPromo) {
            return "Thêm thất bại";
        }
        return "Thêm thành công";
    }

    public String delete(String id) {
        boolean deletePromo = repo.delete(id);
        if (deletePromo) {
            return "Xóa thất bại " + id;
        }
        return "Xóa thành công";
    }

    public String update(String id, PromotionS pr) {
        boolean updatepromo = repo.update(id, pr);
        if (updatepromo) {
            return "Sửa thất bại" + id;
        }
        return "Sửa thành công";
    }

}
