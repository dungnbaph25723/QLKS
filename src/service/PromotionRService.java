package service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PromotionR;
import respository.PromotionRRepo;
import utilities.ReadWriteData;

public class PromotionRService implements IService<PromotionR, String> {

    private PromotionRRepo repo;
    private ReadWriteData readWriteData;

    public PromotionRService() {
        repo = new PromotionRRepo();
        readWriteData = new ReadWriteData();

    }

    public PromotionR searchPromotionR(String id, String dateEnd) {
        return repo.searchPromotionR(id, dateEnd);
    }

    @Override
    public String insert(PromotionR pr) {
        boolean addPromo = repo.add(pr);
        pr.setCode(pr.getCode().substring(2));
        try {
            readWriteData.ghidl(Integer.parseInt(pr.getCode()), "phong.txt");
        } catch (IOException ex) {
            Logger.getLogger(PromotionRService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (addPromo) {
            return "Thêm thất bại";
        }
        return "Thêm thành công";
    }

    @Override
    public void update(PromotionR pr, String id) {
        boolean updatepromo = repo.update(id, pr);
    }

    @Override
    public void delete(String id) {
        boolean deletePromo = repo.delete(id);
    }

    @Override
    public List<PromotionR> getAll() {
        return this.repo.getAll();
    }

}
