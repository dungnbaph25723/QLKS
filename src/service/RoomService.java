package service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Room;
import respository.RoomRepo;
import utilities.ReadWriteData;

public class RoomService implements IService<Room, String> {

    private ReadWriteData readWriteData;
    private RoomRepo repo;

    public RoomService() {
        readWriteData = new ReadWriteData();
        repo = new RoomRepo();
    }

    public List<Room> getRoomByNumber(String number) {
        if (number.equals("") || number == null || repo.getRoomByNumber(number).isEmpty()) {
            return null;
        }
        return repo.getRoomByNumber(number);
    }

    @Override
    public String insert(Room entity) {
        this.repo.insert(entity);
        entity.setCode(entity.getCode().substring(2));
        try {
            readWriteData.ghidl(Integer.parseInt(entity.getCode()), "phong.txt");
        } catch (IOException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public void update(Room entity, String roomNumber) {
        repo.update(entity.getStatus(), roomNumber);
    }

    @Override
    public void delete(String id) {
        this.repo.delete(id);
    }

    @Override
    public List<Room> getAll() {
        return repo.getAll();
    }

    public Room getSearchRoom(String roomNumber) {
        return this.repo.getSearchRoom(roomNumber);
    }

    public void updateById(String id, Room room) {
        this.repo.updateById(id, room);
    }

}
