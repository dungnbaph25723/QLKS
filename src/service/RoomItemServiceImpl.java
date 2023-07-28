///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package Service;
//
//import DomainModel.roomItem;
//import Repository.Room_ItemRepository;
//import ViewModel.Room_ItemViewModel;
//import java.util.ArrayList;
//public class RoomItemServiceImpl implements RoomItemService{
//
//    private Room_ItemRepository ritemRepo=new Room_ItemRepository();
//    
////    @Override
////    public Boolean save(roomItem r) {
////        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
////    }
//
//    @Override
//    public ArrayList<Room_ItemViewModel> list() {
//        return ritemRepo.getList();
//    }
//
//    @Override
//    public String update(String tt, Room_ItemViewModel r) {
//        boolean update = this.ritemRepo.update(tt,r);
//        if (update) {
//            return "Sua thành công";
//        }
//        return "Sua thất bại";
//    }
//
//    @Override
//    public String delete(String tt) {
//        boolean update = this.ritemRepo.delete(tt);
//        if (update) {
//            return "Xoa thành công";
//        }
//        return "Xoa thất bại";
//    }
//
//    
//}
