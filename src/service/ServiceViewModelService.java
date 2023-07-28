package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Room;
import model.Service;
import respository.ServiceViewModelrepo;
import utilities.ReadWriteData;

public class ServiceViewModelService {

    ReadWriteData readWriteData;
    ServiceViewModelrepo serRepo;

    public ServiceViewModelService() {
        readWriteData = new ReadWriteData();
        this.serRepo = new ServiceViewModelrepo();
    }

    public void insert(Service s) {
        this.serRepo.insert(s);
            s.setCode(s.getCode().substring(2));
        try {
            readWriteData.ghidl(Integer.parseInt(s.getCode()), "DichVu.txt");
        } catch (IOException ex) {
            Logger.getLogger(ServiceViewModelService.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }

    public void update(String id, Service s) {
        this.serRepo.update(id, s);
    }

    public void delete(String ma) {
        this.serRepo.delete(ma);
    }

   
    public ArrayList<Service> getAll() {
        return this.serRepo.getAll();
    }



}
