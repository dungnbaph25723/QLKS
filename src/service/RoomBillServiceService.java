package service;

import java.util.List;
import model.RoomBillService;
import respository.RoomBillServiceRepo;

public class RoomBillServiceService implements IService<model.RoomBillService, String> {

    private RoomBillServiceRepo repo;

    public RoomBillServiceService() {
        repo = new RoomBillServiceRepo();
    }

    @Override
    public String insert(RoomBillService entity) {
        List<RoomBillService> list = repo.searchByID(entity.getIdBill(), entity.getIdService(), entity.getIdRoom());
        if (!list.isEmpty()) {
            entity.setTimes(list.get(0).getTimes() + 1);
            entity.setDateofHire(list.get(0).getDateofHire()+", "+entity.getDateofHire());
            repo.update(entity, entity.getIdBill(), entity.getIdService(), entity.getIdRoom());
            return "Thêm thành công";
        }
        System.out.println("oanh");
        entity.setTimes(1);
        repo.insert(entity);
        return "Thêm thành công";

    }

    @Override
    public void update(RoomBillService entity, String id) {
   
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<RoomBillService> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
