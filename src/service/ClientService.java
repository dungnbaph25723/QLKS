package service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import respository.ClientRepo;
import utilities.ReadWriteData;
import utilities.StringHandling;

public class ClientService implements IService<Client, String> {

    private ClientRepo repo;
    private ReadWriteData readWriteData;
    private StringHandling upperLowerCase;

    public ClientService() {
        readWriteData = new ReadWriteData();
        repo = new ClientRepo();
        upperLowerCase = new StringHandling();
    }

    private String checkData(Client entity) {
        if (entity.getName().equals("")
                || entity.getCode().equals("")
                || entity.getDateOfBirth().equals("")
                || entity.getAddress().equals("")
                || entity.getSex() == null
                || entity.getIdPersonCard().equals("")
                || entity.getCustomPhone().equals("")
                || entity.getCode().equals("")) {
            return "trong";
        } else {
            String reSdt = "0[0-9]{9}";
            String reCC = "0[0-9]{11}";
            if (entity.getCustomPhone().matches(reSdt) == false) {
                return "sdt";
            }
            if (entity.getIdPersonCard().matches(reCC) == false) {
                return "canCuoc";
            }
        }
        return "";
    }

    public List<Client> checkTrung(String cccd) {
        if (cccd.equals("")) {
            return null;
        }
        return repo.checkTrung(cccd);
    }

    @Override
    public String insert(Client entity) {
        String check = checkData(entity);
        if (check.equals("trong")) {
            return "Thông tin khách hàng cần nhập đầy đủ";
        } else if (check.equals("canCuoc")) {
            return "Số căn cước phải đủ 12 số";
        } else if (check.equals("sdt")) {
            return "Số điện thoại cần nhập 10 số và bắt đầu bằng 0";
        } else {
            try {
                entity.setName(upperLowerCase.firstUpper(entity.getName()).trim());
                entity.setAddress(upperLowerCase.firstUpper(entity.getAddress()).trim());
                repo.insert(entity);
            } catch (ParseException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                entity.setCode(entity.getCode().substring(2));
                readWriteData.ghidl(Integer.parseInt(entity.getCode()), "maKh.txt");
            } catch (IOException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public void update(Client entity, String id) {
        repo.update(entity, id);
    }

    @Override
    public void delete(String id) {
        repo.delete(id);
    }

    @Override
    public List<Client> getAll() {
        return repo.getAll();
    }
}
