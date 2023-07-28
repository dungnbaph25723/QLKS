package service;

import java.util.List;
import model.BillRoom;
import respository.RoomBillRepo;

public class RoomBillService implements IService<BillRoom, String> {

    private RoomBillRepo repo;

    public RoomBillService() {
        repo = new RoomBillRepo();
    }

    @Override
    public String insert(BillRoom entity) {
        repo.insert(entity);
        return null;
    }

    @Override
    public void update(BillRoom entity, String id) {
        repo.update(entity, entity.getBillId(), entity.getRoomId());
    }

    @Override
    public void delete(String id) {
        repo.delete(id);
    }

    @Override
    public List<BillRoom> getAll() {
        return repo.getAll();
    }

}
