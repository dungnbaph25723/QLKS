package service;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bill;
import respository.BillRepo;
import utilities.ReadWriteData;

public class BillService implements IService<Bill, String> {

    private BillRepo repo;
    private ReadWriteData readWriteData;

    public BillService() {
        repo = new BillRepo();
        readWriteData = new ReadWriteData();
    }

    @Override
    public String insert(Bill entity) {
        if (entity.getIdClient().equals("")) {
            return null;
        }
        repo.insert(entity);
        entity.setCode(entity.getCode().substring(2));
        System.out.println(entity.getCode().substring(2));
        try {
            readWriteData.ghidl(Integer.parseInt(entity.getCode()), "maHd.txt");
        } catch (IOException ex) {
            Logger.getLogger(BillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "thêm thành công";
    }

    public List<Bill> searchHd(String idClient) {
        if (repo.searchHd(idClient).isEmpty()) {
            return null;
        }
        return repo.searchHd(idClient);
    }

    public String getId(String number, Date date) {
        if (number.equals("") || date.equals("")) {
            return null;
        }
        return repo.getId(number, date);
    }

    @Override
    public void update(Bill entity, String id) {
        repo.update(entity, id);
    }

    @Override
    public void delete(String id) {
        repo.delete(id);
    }

    @Override
    public List<Bill> getAll() {
        return repo.getAll();
    }

//    public boolean getByCode(String code) {
//        for (Bill bill : getAll()) {
//            if (code.equals(bill.getCode())) {
//                return true;
//            }
//        }
//        return false;
//    }

}
