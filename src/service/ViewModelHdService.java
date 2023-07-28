package service;

import java.util.List;
import respository.ViewModelHDRepo;
import viewModel.HoaDon;

public class ViewModelHdService {

    private ViewModelHDRepo repo;

    public ViewModelHdService() {
        repo = new ViewModelHDRepo();
    }

    public List<HoaDon> getAll() {
        return repo.getAll();
    }
}
