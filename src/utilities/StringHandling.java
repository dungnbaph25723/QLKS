package utilities;

import java.util.ArrayList;
import java.util.List;
import model.Client;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringHandling {

    public String splitDateCheckIn(String string) {
        String[] arr = string.split("\\s");
        return arr[0];
    }

    public StringHandling() {
    }

    public String firstUpper(String string) {
        String[] arr = string.split("\\s");
        String out = "";
        for (String string1 : arr) {
            string1 = string1.substring(0, 1).toUpperCase() + string1.substring(1).toLowerCase();
            out = out + " " + string1;
        }
        return out;
    }

    public Client splitString(String string) {
        Client client = new Client();
        String[] arr = string.split("\\|");

        client.setIdPersonCard(arr[0]);
        client.setName(arr[2]);

        arr[3] = arr[3].substring(0, 2) + "/" + arr[3].substring(2, 4) + "/" + arr[3].substring(4);
        client.setDateOfBirth(arr[3]);
        client.setSex(arr[4]);
        client.setAddress(arr[5]);
        return client;
    }

    public String splitNameStaff(String name) {
        name = name.trim().toLowerCase();
        
        String temp = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        temp.replaceAll("đ", "d");
        String[] arr = temp.split("\\s");
        String string = "" + arr[arr.length - 1];
        for (int i = 0;
                i < arr.length - 1; i++) {
            String temp1 = arr[i].substring(0, 1);
            string = string + temp1;
        }
        return string;
    }

    public static void main(String[] args) {
        StringHandling hand = new StringHandling();
        System.out.println(hand.splitNameStaff(hand.firstUpper("Pham đuc oanh")));

    }

}
