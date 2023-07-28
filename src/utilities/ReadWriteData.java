/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class ReadWriteData {

    public List<Integer> doc(String nameFile) throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File(nameFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
            }
        }
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        int code = 0;
        List<Integer> listtemp = new ArrayList<>();
        while (fis.available() > 0) {
            code =  dis.readInt();
            listtemp.add(code);
        }
        fis.close();
        dis.close();
        return listtemp;
    }

    public void ghidl(int code, String nameFile) throws FileNotFoundException, IOException {
        File file = new File(nameFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {

            }
        }
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeInt(code);
        fos.close();
        dos.close();
    }
}
