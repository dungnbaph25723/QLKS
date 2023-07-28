
package service;
import java.util.ArrayList;
import respository.CheckoutRepo;
import viewModel.ThongtinCheckout;
import viewModel.DVcheckout;

public class CheckoutService {
    CheckoutRepo cr = new CheckoutRepo();
    public ArrayList<DVcheckout> GetDVcheckout(String id,String idBill){
        return this.cr.GetDVcheckout(id,idBill);
    }
    public ArrayList<ThongtinCheckout> Getthongtincheckout(String id,String idBill){
        return this.cr.GetthongtinCheckout(id,idBill);
    }
    
}
