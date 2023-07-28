package service;

import java.util.List;
import model.Service;
import respository.ServiceRepo;

public class ServiceService implements IService<Service, String> {

    private ServiceRepo repo;

    public ServiceService() {
        repo = new ServiceRepo();
    }

    public List<Service> getByCode(String code) {
        if (code.equals("") || code == null || repo.getIdByCode(code).isEmpty()) {
            return null;
        }
        return repo.getIdByCode(code);

    }

    @Override
    public String insert(Service entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Service entity, String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Service> getAll() {
        return repo.getAll();
    }

}
