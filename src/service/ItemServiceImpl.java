///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package Service;
//
//import DomainModel.item;
//import Repository.ItemRepository;
//import ViewModel.ItemViewModel;
//import ViewModel.Room_ItemViewModel;
//import java.util.ArrayList;
//import javax.swing.JOptionPane;
//import View.ViewItem;
//public class ItemServiceImpl implements ItemService {
//    
//
//    private ItemRepository item = new ItemRepository();
//
//    @Override
//    public ArrayList<ItemViewModel> list() {
//        return item.getList();
//    }
//
//
//
//    @Override
//    public String delete(String ma) {
//        boolean delete = this.item.delete(ma);
//        if (delete) {
//            return "Xoá thành công";
////            return JOptionPane.showMessageDialog(, "Xoa thanh cong");
//        }
//        return "Xoá thất bại";
//    }
//
//
//
//    @Override
//    public String insert(ItemViewModel i) {
//        boolean add = this.item.insert(i);
//        if (add) {
//            return "Them thành công";
//        }
//        return "Them thất bại";
//    }
//
//    @Override
//    public String update(String ma, ItemViewModel i) {
//       boolean update = this.item.update(ma,i);
//        if (update) {
//            return "Sua thành công";
//        }
//        return "Sua thất bại";
//    }
//
//
//
// 
//    
//    
//}
