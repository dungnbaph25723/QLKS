/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Bill;
import model.BillRoom;
import model.Room;
import service.BillService;
import service.RoomBillService;
import service.CheckoutService;
import service.RoomService;
import utilities.Tinhtien;
import viewModel.DVcheckout;
import viewModel.ThongtinCheckout;

/**
 *
 * @author FPTSHOP
 */
public class ChiTietHoaDon extends javax.swing.JFrame {

    private CheckoutService checkoutService;
    private RoomService roomService;
    private RoomBillService roomBillService;
    private BillService billService;
    private String codeBill = "";

    /**
     * Creates new form ChiTietHoaDon
     */
    public ChiTietHoaDon(String code) {
        initComponents();

        checkoutService = new CheckoutService();
        roomBillService = new RoomBillService();
        roomService = new RoomService();
        billService = new BillService();
        codeBill = code;
        fillPhong();
        fillDV();
    }

    public String getIdBill() {
        String idBill = "";

        for (Bill bill : billService.getAll()) {
            if (bill.getCode().equals(codeBill)) {
                idBill = bill.getId();
                return idBill;
            }
        }
        return null;
    }

    public String getIdRoom() {
        String idRoom = "";

        for (BillRoom billRoom : roomBillService.getAll()) {
            if (billRoom.getBillId().equals(getIdBill())) {
                idRoom = billRoom.getRoomId();
                return idRoom;
            }
        }
        return null;
    }

    public void fillPhong() {
        String idRoom = getIdRoom();

//        for (ThongtinCheckout r : checkoutService.Getthongtincheckout(idRoom)) {
//        for (ThongtinCheckout r : checkoutService.Getthongtincheckout(idRoom)) {
        ArrayList<ThongtinCheckout> list = checkoutService.Getthongtincheckout(idRoom, getIdBill());

        for (Room object : roomService.getAll()) {

            if (object.getId().equals(idRoom)) {
                txtLoaiPhong.setText(object.getKindOfRoom().equals("1") ? "Phòng đơn" : object.getKindOfRoom().equals("2") ? "Phòng đôi" : "Phòng VIP");
            }
        }
        for (int i = 0; i < list.size(); i++) {

            txtSoPhong.setText(list.get(i).getSophong());
            txtGiamGia.setText(list.get(i).getGiamgia());
            txtGia.setText(list.get(i).getGiaphong());
            Tinhtien tinh = new Tinhtien();
            double phuthu1, phuthu;
            phuthu = tinh.tiSoCheckOut(list.get(i).getCheckout(), list.get(i).getIdbill());
            phuthu1 = tinh.tiSoCheckIn(list.get(i).getCheckin(), list.get(i).getIdbill());
            phuthu1 = (double) Math.round(phuthu1 * 100) / 100;
            phuthu = (double) Math.round(phuthu * 100) / 100;

            double tongtien = Double.parseDouble(tinh.tinhTienDv(list.get(i).getIdbill()))
                    + Double.parseDouble(tinh.tinhTienPhong(phuthu, phuthu1, list.get(i).getIdbill()));
            txtThanhTien.setText(String.valueOf(tongtien));
            String tongPhuThu = null;

            tongPhuThu = (phuthu1 > 1.0 && phuthu1 < 1.5) ? "Thêm 30% tổng tiền thuê phòng sớm" : (phuthu1 > 1.4) ? "Thêm 50% tổng tiền thuê phòng sớm" : "";
            if (phuthu > 1 && !tongPhuThu.equals("")) {
                tongPhuThu = tongPhuThu + " và " + ((phuthu < 1.5) ? "\n30% tổng tiền trả phòng muộn" : (phuthu > 1.4 && phuthu < 1.6) ? "\n50% tổng tiền trả phòng muộn" : (phuthu > 1.9) ? "\n100% tổng tiền trả phòng muộn" : "");
                txtThuThem.setText(tongPhuThu);
                return;
            }
            if (phuthu > 1 && tongPhuThu.equals("")) {
                tongPhuThu = tongPhuThu + ((phuthu < 1.5) ? "30% tổng tiền trả phòng muộn" : (phuthu > 1.4 && phuthu < 1.6) ? "50% tổng tiền trả phòng muộn" : (phuthu > 1.9) ? "100% tổng tiền trả phòng muộn" : "");
                txtThuThem.setText(tongPhuThu);
                return;
            }
            tongPhuThu = "";
            txtThuThem.setText(tongPhuThu);

        }
    }

    public void fillDV() {
        CheckoutService cs = new CheckoutService();
        DefaultTableModel defaultTableModelds = (DefaultTableModel) tbTtDv.getModel();
        defaultTableModelds.setRowCount(0);
        double gia = 0;

        ArrayList<DVcheckout> dv = new ArrayList<>();

        dv = cs.GetDVcheckout(getIdRoom(),getIdBill());
//        System.out.println(dv.get(0));
        for (DVcheckout x : dv) {
            System.out.println(x);
            defaultTableModelds.addRow(new Object[]{
                x.getTen(), x.getSoluong(), x.getGiamgia(), x.getGia(), Integer.parseInt(x.getSoluong()) * (Double.parseDouble(x.getGia()) - Double.parseDouble(x.getGiamgia()))+" vnđ"});
            gia = gia +  Integer.parseInt(x.getSoluong()) * (Double.parseDouble(x.getGia()) - Double.parseDouble(x.getGiamgia()));
        }
        defaultTableModelds.addRow(new Object[]{"Tổng tiền", "", "", "", gia + " vnđ"});

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSoPhong = new javax.swing.JTextField();
        txtLoaiPhong = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtThuThem = new javax.swing.JTextArea();
        txtGiamGia = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        txtThanhTien = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTtDv = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();

        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phòng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Số phòng:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Loại phòng:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Giảm giá:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Giá: ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Thu thêm:");

        txtSoPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtLoaiPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtThuThem.setColumns(20);
        txtThuThem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtThuThem.setRows(5);
        jScrollPane2.setViewportView(txtThuThem);

        txtGiamGia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtGia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtThanhTien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Thành tiền:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLoaiPhong))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtSoPhong))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(31, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGia)
                                    .addComponent(txtGiamGia)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtThanhTien)))
                        .addGap(31, 31, 31))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtLoaiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46))))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dịch vụ sử dụng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        tbTtDv.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbTtDv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên dịch vụ", "Số lần", "Giảm giá", "Giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbTtDv.setRowHeight(25);
        jScrollPane1.setViewportView(tbTtDv);
        if (tbTtDv.getColumnModel().getColumnCount() > 0) {
            tbTtDv.getColumnModel().getColumn(0).setResizable(false);
            tbTtDv.getColumnModel().getColumn(1).setResizable(false);
            tbTtDv.getColumnModel().getColumn(2).setResizable(false);
            tbTtDv.getColumnModel().getColumn(3).setResizable(false);
            tbTtDv.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnOk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOk)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnOk)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbTtDv;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtLoaiPhong;
    private javax.swing.JTextField txtSoPhong;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JTextArea txtThuThem;
    // End of variables declaration//GEN-END:variables
}
