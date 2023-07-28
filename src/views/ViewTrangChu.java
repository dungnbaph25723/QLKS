package views;

import utilities.Tinhtien;
import com.google.zxing.qrcode.encoder.QRCode;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import service.CheckoutService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.Bill;
import model.BillRoom;
import model.Client;
import model.Item;
import model.PromotionR;
import model.PromotionS;
import model.Room;
import model.Service;
import model.Staff;
import viewModel.DVcheckout;
import viewModel.ThongtinCheckout;

import respository.RoomBillRepo;
import respository.ViewModelHDRepo;
import service.BillService;
import service.ClientService;
import service.DichVuService;
import service.Impl.ItemService;
import service.KhachHangService;
import service.PromotionRService;
import service.PromotionSService;
import service.RoomBillService;
import service.RoomBillServiceService;
import service.RoomItemService;
import service.RoomService;
import service.ServiceService;
import service.ServiceViewModelService;
import service.StaffService;
import service.ViewModelHdService;
import service.ViewModelItemService;
import utilities.Auth;
import utilities.DaysBetween2Dates;
import utilities.RandomCode;
import utilities.ReadWriteData;
import utilities.StringHandling;
import viewModel.HoaDon;
import viewModel.PhieuDVViewModel;
import viewModel.RoomItemVMD;
import viewModel.ViewModelItem;
import viewModel.staffviewmodel;
import static views.QrCode.client;

public class ViewTrangChu extends javax.swing.JFrame {

    private int temp = 0;
    private String tenPhong = "";
    private int tempCheck = 0;
    private String code = "";
    private JPanel jpanelTemp;
    public ButtonGroup gr = new ButtonGroup();
    public ButtonGroup gr1 = new ButtonGroup();
    public ButtonGroup grFormKh = new ButtonGroup();
    public ButtonGroup grFormtt = new ButtonGroup();
    public ButtonGroup buttonGroup;
    public ButtonGroup buttonGroupCV = new ButtonGroup();
    private RandomCode rand = new RandomCode();
    private ReadWriteData readWriteData = new ReadWriteData();
    private DaysBetween2Dates between2Dates = new DaysBetween2Dates();
    private Auth auth;

    private RoomItemService ir;
    private PromotionSService promotionSService;
    private ClientService clienService;
    private BillService billService;
    private RoomService roomService;
    private PromotionRService promotionRService;
    private RoomBillService roomBillService;
    private ServiceService serviceService;
    private RoomBillServiceService roomBillServiceService;
    private KhachHangService khachHangService;
    private DichVuService dichVuService;
    private ServiceViewModelService serviceViewModelService;
    private StaffService nhanvienService;
    private ItemService is;
    Calendar calendar = Calendar.getInstance();

    public ViewTrangChu() {
        initComponents();
        //set cbb
        cbbTrangthaiTP.removeAllItems();
        cbbTrangthaiTP.addItem("");
        cbbTrangthaiTP.addItem("Chưa thanh toán");
        cbbTrangthaiTP.addItem("Ðã thanh toán");

        this.setSize(GetMaxWidth(), GetMaxHeight());
        serviceViewModelService = new ServiceViewModelService();
        khachHangService = new KhachHangService();
        is = new ItemService();
        dichVuService = new DichVuService();
        clienService = new ClientService();
        billService = new BillService();
        roomService = new RoomService();
        roomBillService = new RoomBillService();
        promotionRService = new PromotionRService();
        serviceService = new ServiceService();
        roomBillServiceService = new RoomBillServiceService();
        promotionSService = new PromotionSService();
        nhanvienService = new StaffService();
        ir = new RoomItemService();
        auth = new Auth();
        if (auth.rule.equals("nhanvien")) {
            btnXoa.setEnabled(false);
            btnDelete.setEnabled(false);
            jTabTrangChu.setEnabledAt(6, false);
            jTabTrangChu.setEnabledAt(7, false);
            btnXoaDV.setEnabled(false);
            btnSuaDV.setEnabled(false);
            btnThemdv.setEnabled(false);
        }
        gr.add(rdNu);
        gr.add(rdNam);
        grFormKh.add(rdNamKH);
        grFormKh.add(rdNuKh);
        gr1.add(chkALL);
        gr1.add(chkCTT);
        gr1.add(chkTT);
        grFormtt.add(chkCD);
        grFormtt.add(chkTc);
        grFormtt.add(chkDD);
        grFormtt.add(chkSC);
        grFormtt.add(chkSS);
        grFormtt.add(chkCk);
        buttonGroupCV.add(rdNv);
        buttonGroupCV.add(rdQl);
        // chon ngay
        csTraPhong.setDate(
                new java.util.Date());
        csTraPhong.setMinSelectableDate(
                new java.util.Date());
        maxNs();

        new Thread() {

            public void run() {
                while (true) {
                    Calendar calendar = new GregorianCalendar();
                    String hour = (calendar.get(calendar.HOUR_OF_DAY) < 9) ? "0" + calendar.get(calendar.HOUR_OF_DAY) : "" + calendar.get(calendar.HOUR_OF_DAY) + "";
                    String minute = (calendar.get(calendar.MINUTE) < 9) ? "0" + calendar.get(calendar.MINUTE) : "" + calendar.get(calendar.MINUTE) + "";
                    int am_pm = calendar.get(calendar.AM_PM);
                    String day_night;
                    if (am_pm == calendar.AM) {
                        day_night = "AM";
                    } else {
                        day_night = "PM";
                    }
                    lbThoiGian.setText(hour + " : " + minute + " " + day_night);
                }
            }
        }
                .start();
        loadPhongSS();
        loadCbDv();
        loadSl();
        // manh
        loadDataPro();
        loadDataRoom();
        loadCbViTri();
        //nam
        loadBill();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "0");
        }
        //
        loadDV();
        loadKH();
        loadDichVu();
        loadDataSProS();

        //tuan
        rdGoiTinh();
        showDataTable(nhanvienService.getAll());
        //tu
        loadDataCBItem();
        loadCBRoom();
        loadData();
        // het init
    }

    public int GetMaxWidth() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    }

    public int GetMaxHeight() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    }

    public void loadPanel(String floor, String trangThai) {
        int t1 = 0, t2 = 0, t3 = 0;
        List<Room> list = new ArrayList<>();
        if (floor.equals("all")) {
            list = roomService.getAll();
        } else {
            for (Room room : roomService.getAll()) {
                if (room.getLocation().equals(floor)) {
                    list.add(room);
                }
            }
        }
        System.out.println(list.get(0));
        List<Room> listTimKiem = new ArrayList<>();
        for (Room room : roomService.getAll()) {
            if (room.getStatus().equals(trangThai)) {
                listTimKiem.add(room);
            }
            if (room.getLocation().equals("Tầng 1")) {
                t1++;
            }
            if (room.getLocation().equals("Tầng 2")) {
                t2++;
            }
            if (room.getLocation().equals("Tầng 3")) {
                t3++;
            }
        }
        // tìm thay -> list = list tim kiem
        if (!listTimKiem.isEmpty() && (trangThai.equals("1") || trangThai.equals("2") || trangThai.equals("3") || trangThai.equals("4") || trangThai.equals("5"))) {
            list = new ArrayList<>();
            list = listTimKiem;
        }
        // neu k tim thay va trang thai != 0-> list new
        if (listTimKiem.isEmpty() && !trangThai.equals("0")) {
            list = new ArrayList<>();
        }

        for (Room room : list) {
            JPanel jPanel = new JPanel();
            JLabel jLabel = new JLabel();
            JLabel jLabel1 = new JLabel();
            JLabel jLabel2 = new JLabel();
            JLabel jLabel3 = new JLabel();
            JLabel jLabel4 = new JLabel();
            JLabel jLabel5 = new JLabel();
            JLabel jLabel6 = new JLabel();
            jPanel.setName(room.getRoomNumber());

            if (room.getStatus().equals("1")) {
                jPanel.setBackground(new java.awt.Color(204, 204, 255));
            }
            if (room.getStatus().equals("2")) {
                jPanel.setBackground(new java.awt.Color(204, 255, 255));
            }
            if (room.getStatus().equals("3")) {
                jPanel.setBackground(new java.awt.Color(204, 255, 204));
            }
            if (room.getStatus().equals("4")) {
                jPanel.setBackground(new java.awt.Color(221, 216, 216));
            }
            if (room.getStatus().equals("5")) {
                jPanel.setBackground(new java.awt.Color(255, 153, 0));
            }
            jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, room.getRoomNumber(), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
            jPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel1.setText("Loại:");

            jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel2.setText("Diện tích:");

            jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel3.setText("Giá:");

            jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel4.setText(Integer.parseInt(room.getKindOfRoom()) == 1 ? "Phòng đơn" : Integer.parseInt(room.getKindOfRoom()) == 2 ? "Phòng đôi" : "Phòng VIP");

            jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel5.setText(room.getArea() + " " + "m2");

            jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            jLabel6.setText(room.getPrice() + " " + "VNĐ");

            javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel);
            jPanel.setLayout(jPanel10Layout);
            jPanel10Layout.setHorizontalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel3))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                    .addContainerGap())
            );
            jPanel10Layout.setVerticalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addGap(16, 16, 16)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel4))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel5))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel6))
                                    .addContainerGap())
            );

            jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    popupPhong.show(jPanel, evt.getX(), evt.getY());
                    tenPhong = jPanel.getName();
                    jpanelTemp = jPanel;
                }
            });

            if (room.getLocation().equals("Tầng 2")) {
                jPnTang2.add(jPanel);
                jPnTang2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 2" + " " + "(" + t2 + ")", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
            }
            if (room.getLocation().equals("Tầng 1")) {
                jPnTang1.add(jPanel);
                jPnTang1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 1" + " " + "(" + t1 + ")", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
            }
            if (room.getLocation().equals("Tầng 3")) {
                jPnTang3.add(jPanel);
                jPnTang3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 3" + " " + "(" + t3 + ")", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N

            }
        }
    }

    void maxNs() {
        int year = calendar.get(Calendar.YEAR) - 14;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String maxNs = day + "/" + month + "/" + year;
        try {
            csNgaySinh.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(maxNs));
            csNgaySinh.setMaxSelectableDate(new SimpleDateFormat("dd/MM/yyyy").parse(maxNs));

        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadCbDv() {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) cbDichVu.getModel();
        int tempCB = 0;
        for (Service service : serviceService.getAll()) {
            for (int i = 0; i < cbDichVu.getMaximumRowCount(); i++) {
                if (service.getName().equals(cbDichVu.getItemAt(i))) {
                    tempCB = 1;
                    break;
                }
            }
            if (tempCB == 1) {
                tempCB = 0;
                break;
            }
            defaultComboBoxModel.addElement(service.getName());
        }
    }

    void loadPhongSS() {
        DefaultTableModel defaultTableModelds = (DefaultTableModel) tbDsPhong.getModel();
        defaultTableModelds.setRowCount(0);
        int stt = 0;
        for (Room room : roomService.getAll()) {
            if (room.getStatus().equals("1")) {
                stt = stt + 1;
                defaultTableModelds.addRow(new Object[]{stt, room.getRoomNumber(), Integer.parseInt(room.getKindOfRoom()) == 1 ? "Phòng đơn" : Integer.parseInt(room.getKindOfRoom()) == 2 ? "Phòng đôi" : "Phòng vip", room.getLocation(), room.getPrice()});
            }

        }
    }

    public void loadSl() {
        int ss = 0, ck = 0, cd = 0, dd = 0, sc = 0;
        for (Room room : roomService.getAll()) {
            if (room.getStatus().equals("1")) {
                ss = ss + 1;
            }
            if (room.getStatus().equals("2")) {
                ck = ck + 1;
            }
            if (room.getStatus().equals("3")) {
                cd = cd + 1;
            }
            if (room.getStatus().equals("4")) {
                dd = dd + 1;
            }
            if (room.getStatus().equals("5")) {
                sc = sc + 1;
            }
        }
        jLbAll.setText("(" + roomService.getAll().size() + ")");
        jLbSS.setText("(" + ss + ")");
        jLbCK.setText("(" + ck + ")");
        jLbCD.setText("(" + cd + ")");
        jLbDD.setText("(" + dd + ")");
        jLbSC.setText("(" + sc + ")");
    }

    void reset(Client client) {
        txtCCCD.setText(client.getIdPersonCard());
        txtMaKH.setText(client.getCode());
        txtSDT.setText(client.getCustomPhone());
        txtDiaChi.setText(client.getAddress());
        txtTenKhachHang.setText(client.getName());
        gr.clearSelection();
        maxNs();
        tempCheck = 0;
    }

    public void fillRoom(Room room) {
        txtMaPhong.setText(room.getCode());
        txtSoPhong.setText(room.getRoomNumber());
        txtAreaRoom.setText(room.getArea());
        String loaiPhong = "";
        if (room.getKindOfRoom() != null) {
            loaiPhong = Integer.parseInt(room.getKindOfRoom()) == 1 ? "Phòng đơn" : Integer.parseInt(room.getKindOfRoom()) == 2 ? "Phòng đôi" : "Phòng vip";
        }
        txtKindOfRoom.setText(loaiPhong);
        txtLocationRoom.setText(room.getLocation());
        txtGiaPhong.setText(room.getPrice());
    }

    void fillClient(Client client) {
        txtTenKhachHang.setText(client.getName().trim());
        try {
            if (client.getDateOfBirth() != null) {
                if (client.getDateOfBirth().indexOf("-") != -1) {
                    client.setDateOfBirth(client.getDateOfBirth().substring(8) + "/" + client.getDateOfBirth().substring(5, 7) + "/" + client.getDateOfBirth().substring(0, 4));
                }
                csNgaySinh.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(client.getDateOfBirth()));
            }
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (client.getSex().equals(null)) {
            return;
        }
        if (client.getSex().equals("Nam")) {
            rdNam.setSelected(true);
        } else {
            rdNu.setSelected(true);
        }
        txtCCCD.setText(client.getIdPersonCard().trim());
        txtDiaChi.setText(client.getAddress().trim());
        txtSDT.setText(client.getCustomPhone().trim());
        txtMaKH.setText(client.getCode());

    }

    public class threadChuY extends Thread {

        public void run() {
            while (true) {
                try {
                    threadChuY.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (txtCCCD.getText().length() == 12) {
                    jLbCheckcc.setText("");
                    Client client = new Client();
                    if (!clienService.checkTrung(txtCCCD.getText().trim()).isEmpty()) {
                        tempCheck = 1;
                        jLbCheckTen.setText("");
                        jLbCheckGt.setText("");
                        jLbChecksdt.setText("");
                        jLbCheckDc.setText("");
                        client = clienService.checkTrung(txtCCCD.getText().trim()).get(0);
                        fillClient(client);
                        return;
                    }
                } else {
                    jLbCheckcc.setText("*");
                    jLbCheckcc.setForeground(Color.red);
                    jLbCheckcc.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (txtTenKhachHang.getText().length() != 0) {
                    jLbCheckTen.setText("");
                } else {
                    jLbCheckTen.setText("*");
                    jLbCheckTen.setForeground(Color.red);
                    jLbCheckTen.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (gr.isSelected(rdNam.getModel()) || gr.isSelected(rdNu.getModel())) {
                    jLbCheckGt.setText("");
                } else {
                    jLbCheckGt.setText("*");
                    jLbCheckGt.setForeground(Color.red);
                    jLbCheckGt.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (txtSDT.getText().length() == 10) {
                    jLbChecksdt.setText("");
                } else {
                    jLbChecksdt.setText("*");
                    jLbChecksdt.setForeground(Color.red);
                    jLbChecksdt.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (txtDiaChi.getText().length() != 0) {
                    jLbCheckDc.setText("");
                } else {
                    jLbCheckDc.setText("*");
                    jLbCheckDc.setForeground(Color.red);
                    jLbCheckDc.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }
                if (jTabTrangChu.getSelectedIndex() != 1) {
                    return;
                }
            }
        }
    }

    // tu
    // begin
    public void loadData() {

        DefaultTableModel defaultTableModel = (DefaultTableModel) tb_item.getModel();
        defaultTableModel.setRowCount(0);
        for (Item i : is.getListI()) {
            defaultTableModel.addRow(new Object[]{
                i.getCode(), i.getName()
            });
        }
    }

    public void loadSearch() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) tb_item.getModel();
        defaultTableModel.setRowCount(0);
        for (Item i : is.getSearch(txt_tk.getText())) {
            defaultTableModel.addRow(new Object[]{
                i.getId(), i.getCode(), i.getName()
            });
        }
    }

    private Item getFormItem() {
        Item i = new Item();
        String code = rand.createCode("TB", "ThietBi.txt");

        for (Item item : is.getListI()) {
            if (item.getCode().equals(code)) {
                try {
                    readWriteData.ghidl(Integer.parseInt(item.getCode().substring(2)), "ThietBi.txt");
                    getFormItem();
                } catch (IOException ex) {
                    Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        i.setCode(code);
        i.setName(txt_name1.getText());
        return i;

    }

    void clearF() {
        cbmp.setSelectedIndex(0);
        cbmtb.setSelectedIndex(0);
        txt_statusRI.setText("");
        txt_amount.setText("");
    }

    public void loadDataRoomItem() {
        DefaultTableModel dtm;
        dtm = (DefaultTableModel) tb_roomItem.getModel();
        dtm.setRowCount(0);
        int i = 1;

        for (RoomItemVMD r : this.ir.getListRI()) {
            dtm.addRow(new Object[]{i,
                r.getNumberR(), r.getNameI(), r.getStatusRI(), r.getAmountRI()
            });
            i++;
        }
    }

    private RoomItemVMD getForm() {
        String cr = cbmp.getSelectedItem().toString();
        String ci = cbmtb.getSelectedItem().toString();
        String sta = txt_statusRI.getText();
        int so = Integer.parseInt(txt_amount.getText());
        String idR = "";
        for (Room r : roomService.getAll()) {
            if (cr.equals(r.getRoomNumber())) {
                idR = r.getId();
                break;
            }
        }
        String idI = "";
        for (Item r : ir.getListI()) {
            if (ci.equals(r.getName())) {
                idI = r.getId();
                break;
            }
        }
        RoomItemVMD ri = new RoomItemVMD(idR, idI, sta, so);

        return ri;
    }

    public ArrayList<String> getNameRoom() {
        String name = "";
        ArrayList<String> listNameR = new ArrayList<>();
        for (Room r : this.roomService.getAll()) {
            name = r.getRoomNumber();
            listNameR.add(name);
        }
        return listNameR;
    }

    public void loadCBRoom() {
        DefaultComboBoxModel dcm;
        dcm = (DefaultComboBoxModel) cbmp.getModel();
        for (String code : this.getNameRoom()) {
            dcm.addElement(code);
        }
    }

    public ArrayList<String> getNameItem() {
        String nameString = "";
        ArrayList<String> listName = new ArrayList<>();
        for (Item it : this.ir.getListI()) {
            nameString = it.getName();
            listName.add(nameString);
        }
        return listName;
    }

    public void loadDataCBItem() {
        DefaultComboBoxModel dcm;
        dcm = (DefaultComboBoxModel) cbmtb.getModel();
        for (String code : this.getNameItem()) {
            dcm.addElement(code);
        }
    }

    private String getIDRI() {
        RoomItemVMD ri = this.getForm();
        String id = "";
        for (RoomItemVMD r : ir.getListRI()) {
            if (ri.getRoomID().equals(r.getRoomID())) {
                id = r.getRoomID();
                break;
            }
        }
        return id;
    }
    //end
    //
// tuan
//begin

    public void rdGoiTinh() {

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rd_nu);
        buttonGroup.add(rd_nam);
    }

    private void showDataTable(List<staffviewmodel> lists) {
        DefaultTableModel dtm = (DefaultTableModel) tb_qlnv.getModel();;
        dtm.setRowCount(0);
        for (staffviewmodel cv : lists) {
            if (!cv.getCode().equals("admin")) {
                dtm.addRow(cv.toDataRow());
            }
        }
    }

    private void clearForm() {
        txt_code.setText("");
        txt_name.setText("");
        csNgaySinhnv.setDate(null);
        txt_address.setText("");
        txt_idpre.setText("");
        buttonGroup.clearSelection();
        txt_phone.setText("");
        buttonGroupCV.clearSelection();
    }

    private Staff getData() {
        String code = rand.createCode("nv", "NhanVien.txt");
        for (staffviewmodel nv : nhanvienService.getAll()) {
            if (nv.getCode().equals(code)) {
                try {
                    readWriteData.ghidl(Integer.parseInt(nv.getCode().substring(2)), "NhanVien.txt");
                    getData();
                } catch (IOException ex) {
                    Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("oanh");
        String name = this.txt_name.getText().trim();
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(csNgaySinhnv.getDate());
        String sex;
        if (rd_nam.isSelected()) {
            sex = "Nam";
        } else {
            sex = "Nữ";
        }
        String address = this.txt_address.getText().trim();
        String idPersonCard = this.txt_idpre.getText().trim();

        for (staffviewmodel object : nhanvienService.getAll()) {
            if (object.getIdPersonCard().equals(idPersonCard)) {
                JOptionPane.showMessageDialog(this, "Trùng mã căn cước công dân với nhân viên khác");
            }
        }
        String phone = this.txt_phone.getText().trim();
        if (code.length() == 0
                || name.length() == 0
                || address.length() == 0
                || idPersonCard.length() == 0
                || phone.length() == 0) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
            return null;
        } else if (rd_nam.isSelected() == false && rd_nu.isSelected() == false) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
            return null;
        }
        String rule;

        if (rdNv.isSelected()) {
            rule = "nhanvien";
        } else {
            rule = "quanly";
        }

        StringHandling hand = new StringHandling();
        Staff nv = new Staff("", code, name, dateOfBirth, sex, address, idPersonCard, phone, hand.splitNameStaff(name.toLowerCase()) + code.toLowerCase(), rand.randomAlphaNumeric(10), rule);
        return nv;
    }

//end
//
//phan manh lam view co so vat chat
// begin
    public void loadDataPro() {
        DefaultComboBoxModel cbm = (DefaultComboBoxModel) cbmgg.getModel();
        for (String stt : this.getcodePro()) {
            cbm.addElement(stt);
        }
    }

    public void loadCbViTri() {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) cbVt.getModel();
        defaultComboBoxModel.removeAllElements();
        defaultComboBoxModel.addElement("Tầng 1");
        defaultComboBoxModel.addElement("Tầng 2");
        defaultComboBoxModel.addElement("Tầng 3");
    }

    public ArrayList<String> getcodePro() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        ArrayList<String> listName = new ArrayList<>();
        String code = "";
        for (PromotionR stt : this.promotionRService.getAll()) {
            if (between2Dates.daysBetween2Dates(stt.getDateEnd(), date) <= 0) {
                code = stt.getCode();
                listName.add(code);
            }
        }
        return listName;
    }

    public void loadDataRoom() {
        DefaultTableModel dlm = (DefaultTableModel) tbqlp.getModel();
        dlm.setRowCount(0);
        int stt = 1;
        for (Room r : this.roomService.getAll()) {
            Object[] rowData = {
                stt, getStatus(r.getStatus()), getKof(r.getKindOfRoom()), getNamePro(r.getIdPromotion()), r.getCode(), r.getRoomNumber(), r.getArea() + " m2", r.getLocation(), r.getPrice() + " vnđ"
            };
            dlm.addRow(rowData);
            stt++;
        }
    }

    public String getStatus(String stt) {
        String check = stt.equals("1") ? "Sẵn Sàng" : stt.equals("2") ? "Có Khách" : stt.equals("3") ? "Chưa Dọn" : stt.equals("4") ? "Đang Dọn" : "Đang Sửa";
        return check;
    }

    public String getStt(String stt) {
        String check = stt.equals("Sẵn Sàng") ? "1" : stt.equals("Có Khách") ? "2" : stt.equals("Chưa Dọn") ? "3" : stt.equals("Đang Dọn") ? "4" : "5";
        return check;
    }

    public String getKof(String kor) {
        String check = Integer.parseInt(kor) == 1 ? "Phòng Đơn" : Integer.parseInt(kor) == 2 ? "Phòng Đôi" : "Phòng Vip";
        return check;
    }

    public String getKoff(String kor) {
        String check = String.valueOf(kor).equals("Phòng Đơn") ? "1" : String.valueOf(kor).equals("Phòng Đôi") ? "2" : "3";
        return check;

    }

    public String getNamePro(String id) {
        String value = "";
        if (id == null) {
            return "";
        }
        for (PromotionR pr : this.promotionRService.getAll()) {
            if (id.equals(pr.getId())) {
                value = pr.getValue();
                break;
            }
        }
        return value;
    }

    public Room getFromDataDelete() {
        String status = cbstt.getSelectedItem().toString();
        String kor = cblp.getSelectedItem().toString();
        String code = lbma.getText();
        String rmb = txtsp.getText();
        String area = txtdt.getText();
        String lct = cbVt.getSelectedItem().toString();
        String pri = txtg.getText();
        String codepro = cbmgg.getSelectedItem().toString();
        String idPro = null;
        for (PromotionR pr : this.promotionRService.getAll()) {
            if (codepro.equals(pr.getCode())) {
                idPro = pr.getId();
                break;
            }
        }
        Room r = new Room("", status, kor, idPro, code, rmb, area, lct, pri);
        return r;
    }

    public String getIdFromData() {
        Room r1 = this.getFromDataDelete();
        String id = null;
        for (Room r : this.roomService.getAll()) {
            if (r1.getCode().equals(r.getCode())) {
                id = r.getId();
                break;
            }
        }
        return id;
    }

    public void loadTableSearch() {
        DefaultTableModel dlm = (DefaultTableModel) tbqlp.getModel();
        dlm.setRowCount(0);
        int stt = 1;
        Room r = this.roomService.getSearchRoom(txttk.getText().toString().toUpperCase());
        Object[] rowData = {
            stt, getStatus(r.getStatus()), getKof(r.getKindOfRoom()), getNamePro(r.getIdPromotion()), r.getCode(), r.getRoomNumber(), r.getArea(), r.getLocation(), r.getPrice()};
        dlm.addRow(rowData);
        tbqlp.setRowSelectionInterval(0, 0);
    }

    public Room getFromData() {

        String status = cbstt.getSelectedItem().toString();
        String kor = cblp.getSelectedItem().toString();
        String code = rand.createCode("ph", "phong.txt");
        for (Room room : roomService.getAll()) {
            if (room.getCode().equals(code)) {
                try {
                    readWriteData.ghidl(Integer.parseInt(room.getCode().substring(2)), "phong.txt");
                    getFromData();
                } catch (IOException ex) {
                    Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        String rmb = txtsp.getText();
        for (Room room : roomService.getAll()) {
            if (room.getRoomNumber().equals(rmb)) {
                JOptionPane.showMessageDialog(this, "Số phòng bị trùng vui lòng nhập lại!!");
                return null;
            }
        }
        String area = txtdt.getText();
        String lct = cbVt.getSelectedItem().toString();
        String pri = txtg.getText();
        String codepro = cbmgg.getSelectedItem().toString();
        String idPro = null;
        for (PromotionR pr : this.promotionRService.getAll()) {
            if (codepro.equals(pr.getCode())) {
                idPro = pr.getId();
                break;
            }
        }
        Room r = new Room("", status, kor, idPro, code, rmb, area, lct, pri);
        return r;
    }

    // end
    //
    // Nam
    //begin
    public void loadBill() {
        DefaultTableModel dtm = (DefaultTableModel) tblBill.getModel();
        dtm.setRowCount(0);
        int stt = 0;
        ViewModelHdService modelHdService = new ViewModelHdService();
        for (HoaDon bill : modelHdService.getAll()) {
            stt++;
            dtm.addRow(new Object[]{
                stt,
                bill.getCode(),
                bill.getNameClient(),
                bill.getNameStaff(),
                bill.getDate(),
                bill.getDateCheckIn(),
                bill.getDateCheckOut(),
                bill.getPrice() + " vnđ",
                bill.getStatus().equals("0") ? "Chưa thanh toán" : "Đã thanh toán",}
            );
        }
    }

    public boolean loadTableSearchHD() {
        DefaultTableModel dtm = (DefaultTableModel) tblBill.getModel();
        dtm.setRowCount(0);
        int stt = 1;
        ViewModelHdService modelHdService = new ViewModelHdService();

        for (HoaDon bill : modelHdService.getAll()) {
            if (bill.getCode().equals(txtTkHd.getText().trim().toUpperCase())) {
                stt++;
                dtm.addRow(new Object[]{
                    stt,
                    bill.getCode(),
                    bill.getNameClient(),
                    bill.getNameStaff(),
                    bill.getDate(),
                    bill.getDateCheckIn(),
                    bill.getDateCheckOut(),
                    bill.getPrice(),
                    bill.getStatus().equals("0") ? "Chưa thanh toán" : "Đã thanh toán",}
                );
                tblBill.setRowSelectionInterval(0, 0);
                return true;
            }
        }
        return false;
    }

    private Bill getDataDelete() {
        String Code = txtMa.getText();
        String Price = txtTien.getText();
        String status = cbTrangThai.getSelectedItem().toString();
        String date = txtNgayTao.getText();

        String id = "";
        String idClient = "";
        String idStaff = "";
        for (Bill b : this.billService.getAll()) {
            if (Code.equals(b.getCode())) {
                id = b.getId();
                break;
            }
        }

        Bill b = new Bill(id, idClient, idStaff, Code, Price, status, date);
        return b;

    }

    private String getIDBill() {
        Bill b = this.getDataDelete();
        String id = "";
        for (Bill bi : this.billService.getAll()) {
            if (b.getCode().equals(bi.getCode())) {
                id = bi.getId();
                break;
            }
        }
        return id;
    }

    public String getSttHD(String stt) {
        String check = null;
        if (stt.equals("Chưa thanh toán")) {
            check = "0";
        } else if (stt.equals("Đã thanh toán")) {
            check = "1";
        }
        return check;
    }
    //end
    //

    // dung
    public String getIdFromDataDv() {
        Service s1 = this.getDV();
        String id = null;
        for (Service x : this.serviceService.getAll()) {
            if (s1.getCode().equals(x.getCode())) {
                id = x.getId();
                break;
            }
        }
        return id;
    }

    public void loadDV() {
        DefaultTableModel dtm = (DefaultTableModel) tbTTDichVu.getModel();
        dtm.setRowCount(0);
        for (PhieuDVViewModel x : dichVuService.list()) {
            dtm.addRow(new Object[]{
                x.getCodeBill(), x.getRoomNumber(), x.getNameService(), x.getCodeService(), x.getDateOfHire(), x.getTimes(), x.getPromotionService() + " vnđ", x.getPriceService() + " vnđ"
            });
        }
    }

    public void loadKH() {
        DefaultTableModel dtm = (DefaultTableModel) tblTTKhach.getModel();
        dtm.setRowCount(0);

        for (Client x : khachHangService.getList()) {
            dtm.addRow(new Object[]{
                x.getCode(), x.getName(), x.getDateOfBirth(), x.getSex(), x.getIdPersonCard(), x.getCustomPhone(), x.getAddress()
            });
        }
    }

    public void loadDichVu() {
        DefaultTableModel dtm = (DefaultTableModel) tbDichVU.getModel();
        dtm.setRowCount(0);
        int stt = 1;
        for (Service x : this.serviceViewModelService.getAll()) {
            dtm.addRow(new Object[]{stt++, x.getCode(), x.getName(), x.getPrice(), x.getNotes(), getNamePromotionS(x.getIdPromotion())
            });
        }
    }

    public void loadDataSProS() {
        DefaultComboBoxModel dcm = (DefaultComboBoxModel) cbGgs.getModel();
        for (String name : this.getListcodePromotionS()) {
            dcm.addElement(name);
        }

    }

    public String getNamePromotionS(String id) {
        String value = "";
        if (id == null) {
            value = "";
            return value;
        }
        for (PromotionS p : this.promotionSService.getList()) {
            if (id.equals(p.getId())) {
                value = p.getValue();
                break;
            }
        }
        return value;
    }

    public ArrayList<String> getListcodePromotionS() {
        ArrayList<String> listname = new ArrayList<>();
        String code = "";
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        for (PromotionS x : this.promotionSService.getList()) {
            if (between2Dates.daysBetween2Dates(x.getDateEnd(), date) <= 0) {
                code = x.getCode();
                listname.add(code);
            }
        }
        return listname;
    }

    public void loadDataSearchKH(String name) {
        DefaultTableModel dlm = (DefaultTableModel) tblTTKhach.getModel();
        dlm.setRowCount(0);
        for (Client x : this.khachHangService.search(name)) {
            Object[] rowData = {
                x.getCode(), x.getName(), x.getDateOfBirth(), x.getSex(), x.getIdPersonCard(), x.getIdPersonCard(), x.getAddress()
            };
            dlm.addRow(rowData);
        }
    }

    public Client getListKH() {
        Client c = new Client();
        c.setCode(txtMaKHFormKh.getText());
        c.setName(txt_tenKH.getText());
        c.setDateOfBirth(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(csNgaySinh.getDate())));
        if (rdNamKH.isSelected()) {
            c.setSex("Nam");
        } else {
            c.setSex("Nữ");
        }
        c.setIdPersonCard(txt_CCCD.getText());
        c.setCustomPhone(txt_sdt.getText());
        c.setAddress(txtDiaChiKh.getText());
        return c;

    }

    public Service getDV() {
        String code = rand.createCode("DV", "DichVu.txt");
        for (Service ser : serviceService.getAll()) {
            if (ser.getCode().equals(code)) {
                try {
                    readWriteData.ghidl(Integer.parseInt(ser.getCode().substring(2)), "DichVu.txt");
                    getDV();
                } catch (IOException ex) {
                    Logger.getLogger(ServiceViewModelService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        String maDV = txtMaDV.getText();
        String tenDV = txtTenDV.getText();
        String gia = txtGia.getText();
        String gc = txtGhiChu.getText();
        String codepro = cbGgs.getSelectedItem().toString();
        String idPro = null;
        for (PromotionS p : this.promotionSService.getList()) {
            if (codepro.equals(p.getCode())) {
                idPro = p.getId();
                break;
            }
        }

        Service s = new Service("", code, tenDV, gia, gc, idPro);
        return s;
    }

    public Service getDVUpdate() {

        String code = txtMaDV.getText();
        String maDV = txtMaDV.getText();
        String tenDV = txtTenDV.getText();
        String gia = txtGia.getText();
        String gc = txtGhiChu.getText();
        String codepro = cbGgs.getSelectedItem().toString();
        String idPro = null;
        for (PromotionS p : this.promotionSService.getList()) {
            if (codepro.equals(p.getCode())) {
                idPro = p.getId();
                break;
            }
        }

        Service s = new Service("", code, tenDV, gia, gc, idPro);
        return s;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupPhong = new javax.swing.JPopupMenu();
        jMenuThuePhong = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuTrangThai = new javax.swing.JMenu();
        jMenuSS = new javax.swing.JMenuItem();
        jMenuCD = new javax.swing.JMenuItem();
        jMenuDD = new javax.swing.JMenuItem();
        jMenuSC = new javax.swing.JMenuItem();
        menuDichVu = new javax.swing.JMenuItem();
        jMenuCheckout = new javax.swing.JMenuItem();
        popupHoaDon = new javax.swing.JPopupMenu();
        menuChiTiet = new javax.swing.JMenuItem();
        popupNV = new javax.swing.JPopupMenu();
        menuAcount = new javax.swing.JMenuItem();
        jPanel38 = new javax.swing.JPanel();
        txtTenKS = new javax.swing.JLabel();
        lbThoiGian = new javax.swing.JLabel();
        jTabTrangChu = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLbSS = new javax.swing.JLabel();
        jLbCK = new javax.swing.JLabel();
        jLbCD = new javax.swing.JLabel();
        jLbDD = new javax.swing.JLabel();
        jLbSC = new javax.swing.JLabel();
        jLbAll = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPnTang3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPnTang2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPnTang1 = new javax.swing.JPanel();
        btnDx = new javax.swing.JButton();
        chkTc = new javax.swing.JCheckBox();
        chkSS = new javax.swing.JCheckBox();
        chkCk = new javax.swing.JCheckBox();
        chkCD = new javax.swing.JCheckBox();
        chkDD = new javax.swing.JCheckBox();
        chkSC = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel40 = new javax.swing.JPanel();
        pnInforKh = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        rdNam = new javax.swing.JRadioButton();
        rdNu = new javax.swing.JRadioButton();
        btnQuetMa = new javax.swing.JButton();
        btnThuePhong = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        txtMaKH = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        csNgaySinh = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jLbCheckTen = new javax.swing.JLabel();
        jLbCheckGt = new javax.swing.JLabel();
        jLbCheckcc = new javax.swing.JLabel();
        jLbChecksdt = new javax.swing.JLabel();
        jLbCheckDc = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbNoiThat = new javax.swing.JTable();
        txtMaPhong = new javax.swing.JTextField();
        txtSoPhong = new javax.swing.JTextField();
        txtAreaRoom = new javax.swing.JTextField();
        txtLocationRoom = new javax.swing.JTextField();
        txtKindOfRoom = new javax.swing.JTextField();
        txtGiaGiam = new javax.swing.JTextField();
        btnDoiPhong = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtGiaPhong = new javax.swing.JTextField();
        btnHuyPhong = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        csTraPhong = new com.toedter.calendar.JDateChooser();
        jScrollPane11 = new javax.swing.JScrollPane();
        tbDsPhong = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        csCheckinTP = new com.toedter.calendar.JDateChooser();
        jLabel54 = new javax.swing.JLabel();
        csCheckoutTP = new com.toedter.calendar.JDateChooser();
        jLabel55 = new javax.swing.JLabel();
        txtGiaphongTP = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        txtGiamgiaTP = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtCccdTP = new javax.swing.JTextField();
        txtKhachhangTP = new javax.swing.JTextField();
        txtSophongTP = new javax.swing.JTextField();
        txtMahdTP = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        txtThanhtienTP = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        cbbTrangthaiTP = new javax.swing.JComboBox<>();
        btnTraPhongTP = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtPhuthuTP = new javax.swing.JTextArea();
        btnHuy = new javax.swing.JButton();
        jLabel63 = new javax.swing.JLabel();
        csLichTraPhong = new com.toedter.calendar.JDateChooser();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblDVTP = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel39 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtSoPhongDV = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtMaDv = new javax.swing.JTextField();
        cbDichVu = new javax.swing.JComboBox<>();
        txtGiamGiaDV = new javax.swing.JTextField();
        txtGiaDv = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        btnThemDv = new javax.swing.JButton();
        btnHuyDv = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        csNgaySd = new com.toedter.calendar.JDateChooser();
        jLabel40 = new javax.swing.JLabel();
        jPnThemDv = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        txtTenDV = new javax.swing.JTextField();
        txtMaDV = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        cbGgs = new javax.swing.JComboBox<>();
        btn_GiamGia = new javax.swing.JButton();
        jLabel77 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        tbDichVU = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        btnLamMoi = new javax.swing.JButton();
        btnXoaDV = new javax.swing.JButton();
        btnSuaDV = new javax.swing.JButton();
        btnThemdv = new javax.swing.JButton();
        jScrollPane21 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbTTDichVu = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        txtMaKHFormKh = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        txt_tenKH = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        txt_CCCD = new javax.swing.JTextField();
        btnSua2 = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnLammoi = new javax.swing.JButton();
        csDateKH = new com.toedter.calendar.JDateChooser();
        jScrollPane22 = new javax.swing.JScrollPane();
        txtDiaChiKh = new javax.swing.JTextArea();
        rdNamKH = new javax.swing.JRadioButton();
        rdNuKh = new javax.swing.JRadioButton();
        jLabel78 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        txtTimkiemKh = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTTKhach = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblBill = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtTien = new javax.swing.JTextField();
        btn_sua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        cbTrangThai = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtNvTao = new javax.swing.JTextField();
        txtTenKh = new javax.swing.JTextField();
        txtNgayThue = new javax.swing.JTextField();
        txtNgayTra = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        btnResetHd = new javax.swing.JButton();
        chkTT = new javax.swing.JCheckBox();
        chkCTT = new javax.swing.JCheckBox();
        chkALL = new javax.swing.JCheckBox();
        jPanel32 = new javax.swing.JPanel();
        txtTkHd = new javax.swing.JTextField();
        btnTKHd = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        tb_qlnv = new javax.swing.JTable();
        jPanel33 = new javax.swing.JPanel();
        btn_timkiem = new javax.swing.JButton();
        btn_loat = new javax.swing.JButton();
        btn_xoa1 = new javax.swing.JButton();
        btn_sua2 = new javax.swing.JButton();
        btn_them1 = new javax.swing.JButton();
        jab1 = new javax.swing.JLabel();
        rd_nu = new javax.swing.JRadioButton();
        rd_nam = new javax.swing.JRadioButton();
        txt_timkiem = new javax.swing.JTextField();
        jScrollPane24 = new javax.swing.JScrollPane();
        txt_address = new javax.swing.JTextArea();
        csNgaySinhnv = new com.toedter.calendar.JDateChooser();
        txt_name = new javax.swing.JTextField();
        txt_code = new javax.swing.JTextField();
        jab = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        txt_phone = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_idpre = new javax.swing.JTextField();
        rdQl = new javax.swing.JRadioButton();
        rdNv = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jPanel18 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        btnxoa = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbqlp = new javax.swing.JTable();
        btnreset = new javax.swing.JButton();
        cbstt = new javax.swing.JComboBox<>();
        cblp = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        cbmgg = new javax.swing.JComboBox<>();
        txtsp = new javax.swing.JTextField();
        btntk = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        txtdt = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txttk = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        txtg = new javax.swing.JTextField();
        btnthem = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        btnthemnhanh = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();
        lbma = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        cbVt = new javax.swing.JComboBox<>();
        jPanel22 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        txt_amount = new javax.swing.JTextField();
        jScrollPane25 = new javax.swing.JScrollPane();
        tb_roomItem = new javax.swing.JTable();
        txt_statusRI = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        btn_them2 = new javax.swing.JButton();
        btn_sua1 = new javax.swing.JButton();
        btn_xoa2 = new javax.swing.JButton();
        cbmtb = new javax.swing.JComboBox<>();
        cbmp = new javax.swing.JComboBox<>();
        jPanel23 = new javax.swing.JPanel();
        btn_them = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        btn_search = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        btn_clear = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        txt_tk = new javax.swing.JTextField();
        txt_code1 = new javax.swing.JTextField();
        txt_name1 = new javax.swing.JTextField();
        btn_sua3 = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        tb_item = new javax.swing.JTable();

        jMenuThuePhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuThuePhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_rent_16px.png"))); // NOI18N
        jMenuThuePhong.setText("Thuê phòng");
        jMenuThuePhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuThuePhongActionPerformed(evt);
            }
        });
        popupPhong.add(jMenuThuePhong);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        jMenuItem2.setText("Sửa phòng");
        popupPhong.add(jMenuItem2);

        jMenuTrangThai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_view_more_16px.png"))); // NOI18N
        jMenuTrangThai.setText("Đổi trạng thái");
        jMenuTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenuSS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuSS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        jMenuSS.setText("Sẵn sàng");
        jMenuSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSSActionPerformed(evt);
            }
        });
        jMenuTrangThai.add(jMenuSS);

        jMenuCD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuCD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_broom_16px.png"))); // NOI18N
        jMenuCD.setText("Chưa dọn");
        jMenuCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCDActionPerformed(evt);
            }
        });
        jMenuTrangThai.add(jMenuCD);

        jMenuDD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuDD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_vacuuming_16px.png"))); // NOI18N
        jMenuDD.setText("Đang dọn");
        jMenuDD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDDActionPerformed(evt);
            }
        });
        jMenuTrangThai.add(jMenuDD);

        jMenuSC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuSC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Tools_16px.png"))); // NOI18N
        jMenuSC.setText("Sửa chữa");
        jMenuSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSCActionPerformed(evt);
            }
        });
        jMenuTrangThai.add(jMenuSC);

        popupPhong.add(jMenuTrangThai);

        menuDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        menuDichVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_service_16px.png"))); // NOI18N
        menuDichVu.setText("Thêm dịch vụ");
        menuDichVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDichVuActionPerformed(evt);
            }
        });
        popupPhong.add(menuDichVu);

        jMenuCheckout.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuCheckout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Money_Bag_Pounds_16px.png"))); // NOI18N
        jMenuCheckout.setText("Trả phòng");
        jMenuCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCheckoutActionPerformed(evt);
            }
        });
        popupPhong.add(jMenuCheckout);

        menuChiTiet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_view_more_16px.png"))); // NOI18N
        menuChiTiet.setText("Chi Tiết");
        menuChiTiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuChiTietActionPerformed(evt);
            }
        });
        popupHoaDon.add(menuChiTiet);

        menuAcount.setText("Acount");
        menuAcount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAcountActionPerformed(evt);
            }
        });
        popupNV.add(menuAcount);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 204, 204));

        jPanel38.setBackground(new java.awt.Color(255, 204, 204));
        jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtTenKS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenKS.setText("Phần mềm quản lý khách sạn Tây Côn Lĩnh");

        lbThoiGian.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbThoiGian.setText("Thời gian");

        jTabTrangChu.setBackground(new java.awt.Color(255, 204, 204));
        jTabTrangChu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabTrangChu.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("");

        jLbSS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLbSS.setText("(0)");

        jLbCK.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLbCK.setText("(0)");

        jLbCD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLbCD.setText("(0)");

        jLbDD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLbDD.setText("(0)");

        jLbSC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLbSC.setText("(0)");

        jLbAll.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLbAll.setText("(0)");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(978, 500));

        jPanel9.setBackground(new java.awt.Color(255, 204, 204));

        jPnTang3.setBackground(new java.awt.Color(255, 255, 204));
        jPnTang3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 3", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPnTang3.setAutoscrolls(true);
        jPnTang3.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane9.setViewportView(jPnTang3);

        jPnTang2.setBackground(new java.awt.Color(255, 255, 204));
        jPnTang2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 2", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPnTang2.setAutoscrolls(true);
        jPnTang2.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane10.setViewportView(jPnTang2);

        jPnTang1.setBackground(new java.awt.Color(255, 255, 204));
        jPnTang1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 1", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPnTang1.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane8.setViewportView(jPnTang1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane10)
                    .addComponent(jScrollPane8)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(281, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane10, jScrollPane8, jScrollPane9});

        jScrollPane3.setViewportView(jPanel9);

        btnDx.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Logout_16px.png"))); // NOI18N
        btnDx.setText("Đăng xuất");
        btnDx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDxActionPerformed(evt);
            }
        });

        chkTc.setBackground(new java.awt.Color(255, 204, 204));
        chkTc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkTc.setSelected(true);
        chkTc.setText("Tất cả:");
        chkTc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTcActionPerformed(evt);
            }
        });

        chkSS.setBackground(new java.awt.Color(204, 204, 255));
        chkSS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkSS.setText("Sẵn sàng:");
        chkSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSSActionPerformed(evt);
            }
        });

        chkCk.setBackground(new java.awt.Color(204, 255, 255));
        chkCk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkCk.setText("Có khách:");
        chkCk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCkActionPerformed(evt);
            }
        });

        chkCD.setBackground(new java.awt.Color(204, 255, 204));
        chkCD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkCD.setText("Chưa dọn:");
        chkCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCDActionPerformed(evt);
            }
        });

        chkDD.setBackground(new java.awt.Color(221, 216, 216));
        chkDD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkDD.setText("Đang Dọn:");
        chkDD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDDActionPerformed(evt);
            }
        });

        chkSC.setBackground(new java.awt.Color(255, 153, 0));
        chkSC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkSC.setText("Sửa chữa:");
        chkSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(chkTc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLbAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkSS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLbSS)
                        .addGap(40, 40, 40)
                        .addComponent(chkCk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLbCK)
                        .addGap(40, 40, 40)
                        .addComponent(chkCD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLbCD)
                        .addGap(40, 40, 40)
                        .addComponent(chkDD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLbDD)
                        .addGap(40, 40, 40)
                        .addComponent(chkSC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLbSC)
                        .addGap(126, 126, 126)
                        .addComponent(btnDx)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLbAll)
                    .addComponent(jLbSS)
                    .addComponent(jLbCK)
                    .addComponent(jLbCD)
                    .addComponent(jLbDD)
                    .addComponent(jLbSC)
                    .addComponent(btnDx)
                    .addComponent(chkTc)
                    .addComponent(chkSS)
                    .addComponent(chkCk)
                    .addComponent(chkCD)
                    .addComponent(chkDD)
                    .addComponent(chkSC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLbCD, jLbCK, jLbDD, jLbSC, jLbSS});

        jTabTrangChu.addTab("Trang chủ", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(874, 400));

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane7.setPreferredSize(new java.awt.Dimension(1200, 612));

        jPanel40.setBackground(new java.awt.Color(255, 204, 204));

        pnInforKh.setBackground(new java.awt.Color(255, 255, 204));
        pnInforKh.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N
        pnInforKh.setPreferredSize(new java.awt.Dimension(380, 390));
        pnInforKh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnInforKhMouseEntered(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel22.setText("Họ và Tên:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setText("Giới tính:");
        jLabel23.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setText("Ngày Sinh:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setText("Số cccd:");
        jLabel25.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel26.setText("Số điện thoại:");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setText("Địa chỉ:");

        txtTenKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenKhachHang.setMinimumSize(new java.awt.Dimension(65, 22));
        txtTenKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenKhachHangActionPerformed(evt);
            }
        });

        txtCCCD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCCCD.setMinimumSize(new java.awt.Dimension(65, 22));
        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });

        txtSDT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSDT.setMinimumSize(new java.awt.Dimension(65, 22));
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        rdNam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdNam.setText("Nam");

        rdNu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdNu.setText("Nữ");

        btnQuetMa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnQuetMa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Qr_Code_16px.png"))); // NOI18N
        btnQuetMa.setText("Quét mã");
        btnQuetMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetMaActionPerformed(evt);
            }
        });

        btnThuePhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnThuePhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_rent_16px.png"))); // NOI18N
        btnThuePhong.setText("Thuê phòng");
        btnThuePhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThuePhongActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaKH.setEnabled(false);

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel35.setText("Mã:");
        jLabel35.setPreferredSize(new java.awt.Dimension(85, 20));

        csNgaySinh.setDateFormatString("dd/MM/yyyy");
        csNgaySinh.setFocusable(false);
        csNgaySinh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        csNgaySinh.setMaxSelectableDate(new java.util.Date(1136052108000L));
        csNgaySinh.setMinSelectableDate(new java.util.Date(-631173535000L));

        txtDiaChi.setColumns(20);
        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDiaChi.setRows(5);
        txtDiaChi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane2.setViewportView(txtDiaChi);

        jLbCheckTen.setText(" ");

        jLbCheckGt.setText(" ");

        jLbCheckcc.setText(" ");

        jLbChecksdt.setText(" ");

        jLbCheckDc.setText(" ");

        jLabel90.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_male_24px_1.png"))); // NOI18N

        jLabel91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_female_24px.png"))); // NOI18N

        javax.swing.GroupLayout pnInforKhLayout = new javax.swing.GroupLayout(pnInforKh);
        pnInforKh.setLayout(pnInforKhLayout);
        pnInforKhLayout.setHorizontalGroup(
            pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnInforKhLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addComponent(btnReset)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnInforKhLayout.createSequentialGroup()
                                .addComponent(btnQuetMa, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnThuePhong))
                            .addGroup(pnInforKhLayout.createSequentialGroup()
                                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel27))
                                .addGap(17, 17, 17)
                                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnInforKhLayout.createSequentialGroup()
                                        .addComponent(rdNam)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel90)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rdNu)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel91))
                                    .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnInforKhLayout.createSequentialGroup()
                                            .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLbCheckTen)
                                            .addGap(12, 12, 12))
                                        .addGroup(pnInforKhLayout.createSequentialGroup()
                                            .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnInforKhLayout.createSequentialGroup()
                                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLbCheckDc))
                                                .addGroup(pnInforKhLayout.createSequentialGroup()
                                                    .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(pnInforKhLayout.createSequentialGroup()
                                                            .addGap(1, 1, 1)
                                                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(txtCCCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLbCheckGt)
                                                        .addComponent(jLbCheckcc)
                                                        .addComponent(jLbChecksdt))))
                                            .addGap(0, 0, Short.MAX_VALUE)))
                                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(csNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(8, 36, Short.MAX_VALUE))))
        );

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnQuetMa, btnReset});

        pnInforKhLayout.setVerticalGroup(
            pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnInforKhLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLbCheckTen))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(csNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdNam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rdNu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLbCheckcc))
                        .addGap(18, 18, 18)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLbChecksdt))
                        .addGap(18, 18, 18)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLbCheckDc)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnQuetMa, javax.swing.GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(btnThuePhong))
                        .addGap(18, 23, Short.MAX_VALUE)
                        .addComponent(btnReset)
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addComponent(jLbCheckGt)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnQuetMa, btnReset});

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csNgaySinh, txtTenKhachHang});

        jPanel11.setBackground(new java.awt.Color(255, 255, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin phòng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(380, 490));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Mã Phòng:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Số phòng:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Diện tích:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Vị trí:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("Giá:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Loại phòng:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel20.setText("Nội thất trong phòng");

        tbNoiThat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbNoiThat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên", "Tình trạng", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbNoiThat.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbNoiThat);
        if (tbNoiThat.getColumnModel().getColumnCount() > 0) {
            tbNoiThat.getColumnModel().getColumn(0).setResizable(false);
            tbNoiThat.getColumnModel().getColumn(1).setResizable(false);
            tbNoiThat.getColumnModel().getColumn(2).setResizable(false);
        }

        txtMaPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaPhong.setEnabled(false);
        txtMaPhong.setPreferredSize(new java.awt.Dimension(65, 22));

        txtSoPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSoPhong.setEnabled(false);
        txtSoPhong.setPreferredSize(new java.awt.Dimension(65, 22));

        txtAreaRoom.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAreaRoom.setEnabled(false);
        txtAreaRoom.setMinimumSize(new java.awt.Dimension(65, 22));

        txtLocationRoom.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtLocationRoom.setEnabled(false);
        txtLocationRoom.setPreferredSize(new java.awt.Dimension(65, 22));

        txtKindOfRoom.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtKindOfRoom.setEnabled(false);
        txtKindOfRoom.setMinimumSize(new java.awt.Dimension(65, 22));

        txtGiaGiam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiaGiam.setText("0");
        txtGiaGiam.setEnabled(false);
        txtGiaGiam.setMinimumSize(new java.awt.Dimension(65, 22));

        btnDoiPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDoiPhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btnDoiPhong.setText("Đổi phòng");
        btnDoiPhong.setPreferredSize(new java.awt.Dimension(125, 26));
        btnDoiPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiPhongActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel30.setText("Giảm giá:");

        txtGiaPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiaPhong.setEnabled(false);

        btnHuyPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnHuyPhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_16px.png"))); // NOI18N
        btnHuyPhong.setText("Hủy");
        btnHuyPhong.setPreferredSize(new java.awt.Dimension(125, 26));
        btnHuyPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyPhongActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("VNĐ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("VNĐ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("m2");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("Ngày trả phòng:");

        csTraPhong.setDateFormatCalendar(null);
        csTraPhong.setDateFormatString("dd/MM/yyyy");
        csTraPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        csTraPhong.setMaxSelectableDate(new java.util.Date(253370743302000L));
        csTraPhong.setMinSelectableDate(new java.util.Date(-62135791098000L));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel18))
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtAreaRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtLocationRoom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtKindOfRoom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(csTraPhong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(txtGiaPhong, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGiaGiam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9)))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnDoiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(btnHuyPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDoiPhong, btnHuyPhong});

        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAreaRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel16)))
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(txtLocationRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKindOfRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)))
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(csTraPhong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDoiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDoiPhong, btnHuyPhong});

        jPanel11Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csTraPhong, txtAreaRoom, txtGiaGiam, txtGiaPhong, txtKindOfRoom, txtLocationRoom, txtMaPhong, txtSoPhong});

        tbDsPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbDsPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Số phòng", "Loại phòng", "Vị trí", "Giá tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDsPhong.setRowHeight(25);
        tbDsPhong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDsPhongMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tbDsPhong);
        if (tbDsPhong.getColumnModel().getColumnCount() > 0) {
            tbDsPhong.getColumnModel().getColumn(0).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(0).setPreferredWidth(30);
            tbDsPhong.getColumnModel().getColumn(1).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(1).setPreferredWidth(50);
            tbDsPhong.getColumnModel().getColumn(2).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(3).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbDsPhong.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Danh sách phòng sẵn sàng cho thuê");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(pnInforKh, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel6))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1157, Short.MAX_VALUE))
        );

        jPanel40Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel11, pnInforKh});

        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnInforKh, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGap(85, 85, 85)))
                .addContainerGap(335, Short.MAX_VALUE))
        );

        jScrollPane7.setViewportView(jPanel40);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1272, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );

        jTabTrangChu.addTab("Thuê phòng", jPanel2);

        jPanel10.setBackground(new java.awt.Color(255, 204, 204));

        jPanel19.setBackground(new java.awt.Color(255, 255, 204));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel48.setText("Khách hàng:");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel49.setText("Số CCCD:");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel50.setText("Mã Hóa đơn:");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel52.setText("Số phòng:");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel53.setText("Thời gian thuê phòng:");

        csCheckinTP.setToolTipText("\n\n");
        csCheckinTP.setDateFormatCalendar(null);
        csCheckinTP.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        csCheckinTP.setEnabled(false);
        csCheckinTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        csCheckinTP.setMaxSelectableDate(new java.util.Date(253370743302000L));
        csCheckinTP.setMinSelectableDate(new java.util.Date(-62135791098000L));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel54.setText("Thời gian trả phòng:");

        csCheckoutTP.setDateFormatCalendar(null);
        csCheckoutTP.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        csCheckoutTP.setEnabled(false);
        csCheckoutTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        csCheckoutTP.setMaxSelectableDate(new java.util.Date(253370743302000L));
        csCheckoutTP.setMinSelectableDate(new java.util.Date(-62135791098000L));

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel55.setText("Giá phòng:");

        txtGiaphongTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiaphongTP.setEnabled(false);

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel56.setText("VNĐ");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel57.setText("Giảm giá:");

        txtGiamgiaTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiamgiaTP.setEnabled(false);

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel58.setText("VNĐ");

        txtCccdTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCccdTP.setEnabled(false);

        txtKhachhangTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtKhachhangTP.setEnabled(false);

        txtSophongTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSophongTP.setEnabled(false);

        txtMahdTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMahdTP.setEnabled(false);

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel59.setText("Thành tiền:");

        txtThanhtienTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtThanhtienTP.setEnabled(false);

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel60.setText("VNĐ");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel61.setText("Trạng thái:");

        cbbTrangthaiTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbbTrangthaiTP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbTrangthaiTP.setEnabled(false);

        btnTraPhongTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnTraPhongTP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Money_Bag_Pounds_16px.png"))); // NOI18N
        btnTraPhongTP.setText("Trả phòng");
        btnTraPhongTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraPhongTPActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel51.setText("Thu thêm:");

        txtPhuthuTP.setColumns(20);
        txtPhuthuTP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPhuthuTP.setRows(5);
        txtPhuthuTP.setEnabled(false);
        jScrollPane15.setViewportView(txtPhuthuTP);

        btnHuy.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnHuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_16px.png"))); // NOI18N
        btnHuy.setText("Hủy");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel63.setText("Lịch trả phòng:");

        csLichTraPhong.setToolTipText("");
        csLichTraPhong.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        csLichTraPhong.setEnabled(false);
        csLichTraPhong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel49)
                            .addComponent(jLabel48))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtKhachhangTP, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addComponent(txtCccdTP)))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel50)
                            .addComponent(jLabel52))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMahdTP, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addComponent(txtSophongTP))))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel53)
                        .addComponent(jLabel63, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel54, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(csCheckoutTP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(csLichTraPhong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(csCheckinTP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(btnHuy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTraPhongTP))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel55)
                                .addComponent(jLabel61))
                            .addComponent(jLabel57))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGiaphongTP, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiamgiaTP, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtThanhtienTP, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbTrangthaiTP, 0, 188, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60)
                    .addComponent(jLabel56)
                    .addComponent(jLabel58))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel19Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnHuy, btnTraPhongTP});

        jPanel19Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbbTrangthaiTP, txtGiamgiaTP, txtGiaphongTP});

        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtKhachhangTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel48))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCccdTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel49))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel50)
                                    .addComponent(txtMahdTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtSophongTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel52)))
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel53)
                                            .addComponent(csCheckinTP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel63)
                                            .addComponent(csLichTraPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel54)
                                            .addComponent(csCheckoutTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel61)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel55)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel51)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel60))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addGap(86, 86, 86)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtGiamgiaTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel57)
                                            .addComponent(jLabel56)))
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addComponent(cbbTrangthaiTP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtGiaphongTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel58))))
                                .addGap(18, 18, 18)
                                .addComponent(txtThanhtienTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHuy)
                            .addComponent(btnTraPhongTP))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel19Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csCheckinTP, csCheckoutTP, csLichTraPhong, txtKhachhangTP});

        jPanel19Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbbTrangthaiTP, txtGiamgiaTP, txtGiaphongTP, txtThanhtienTP});

        csCheckinTP.getAccessibleContext().setAccessibleName("onh");

        tblDVTP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblDVTP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Dịch vụ", "Mã ", "Số lần", "Giảm giá", "Giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDVTP.setRowHeight(25);
        jScrollPane13.setViewportView(tblDVTP);
        if (tblDVTP.getColumnModel().getColumnCount() > 0) {
            tblDVTP.getColumnModel().getColumn(0).setResizable(false);
            tblDVTP.getColumnModel().getColumn(1).setResizable(false);
            tblDVTP.getColumnModel().getColumn(2).setResizable(false);
            tblDVTP.getColumnModel().getColumn(3).setResizable(false);
            tblDVTP.getColumnModel().getColumn(4).setResizable(false);
            tblDVTP.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 1202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 47, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabTrangChu.addTab("Trả phòng", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel39.setBackground(new java.awt.Color(255, 204, 204));

        jPanel14.setBackground(new java.awt.Color(255, 255, 204));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dịch vụ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setText("Số phòng:");

        txtSoPhongDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSoPhongDV.setPreferredSize(new java.awt.Dimension(85, 26));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel33.setText("Dịch vụ:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel34.setText("Mã dịch vụ:");

        txtMaDv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaDv.setEnabled(false);
        txtMaDv.setPreferredSize(new java.awt.Dimension(85, 26));

        cbDichVu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbDichVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dịch vụ" }));
        cbDichVu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbDichVuItemStateChanged(evt);
            }
        });
        cbDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbDichVuMouseClicked(evt);
            }
        });

        txtGiamGiaDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiamGiaDV.setText("0");
        txtGiamGiaDV.setEnabled(false);

        txtGiaDv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiaDv.setEnabled(false);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setText("Giảm giá:");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel37.setText("Giá:");

        btnThemDv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnThemDv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        btnThemDv.setText("Thêm");
        btnThemDv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDvActionPerformed(evt);
            }
        });

        btnHuyDv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnHuyDv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_16px.png"))); // NOI18N
        btnHuyDv.setText("Hủy");
        btnHuyDv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyDvActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel39.setText("VNĐ");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel38.setText("VNĐ");

        csNgaySd.setDateFormatString("dd/MM/yyyy");
        csNgaySd.setEnabled(false);
        csNgaySd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        csNgaySd.setPreferredSize(new java.awt.Dimension(85, 26));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setText("Ngày sử dụng:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel40)
                        .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(btnThemDv)
                        .addGap(44, 44, 44)
                        .addComponent(btnHuyDv))
                    .addComponent(txtSoPhongDV, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(csNgaySd, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaDv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiamGiaDV, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaDv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel38))
                .addContainerGap())
        );

        jPanel14Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbDichVu, csNgaySd, txtGiaDv, txtGiamGiaDV, txtMaDv, txtSoPhongDV});

        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoPhongDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaDv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(csNgaySd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(18, 21, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiamGiaDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39)
                    .addComponent(jLabel36))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaDv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel37))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemDv)
                    .addComponent(btnHuyDv))
                .addGap(57, 57, 57))
        );

        jPanel14Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbDichVu, csNgaySd, txtGiaDv, txtGiamGiaDV, txtMaDv, txtSoPhongDV});

        jPnThemDv.setBackground(new java.awt.Color(255, 255, 204));
        jPnThemDv.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thêm dịch vụ mới", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel73.setText("Mã dịch vụ:");

        txtGia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel74.setText("Tên dịch vụ:");

        jLabel75.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel75.setText("Ghi chú:");

        txtTenDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtMaDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaDV.setEnabled(false);

        jLabel76.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel76.setText("Giá dịch vụ:");

        cbGgs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbGgs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Giảm giá" }));

        btn_GiamGia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_GiamGia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Plus_16px_1.png"))); // NOI18N
        btn_GiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GiamGiaActionPerformed(evt);
            }
        });

        jLabel77.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel77.setText("Mã giảm giá:");

        tbDichVU.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbDichVU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã DV", "Tên DV", "Giá", "Ghi chú", "Mã giảm giá"
            }
        ));
        tbDichVU.setRowHeight(25);
        tbDichVU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDichVUMouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(tbDichVU);

        jPanel28.setBackground(new java.awt.Color(255, 255, 204));

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoaDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoaDV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btnXoaDV.setText("Xoá");
        btnXoaDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaDVActionPerformed(evt);
            }
        });

        btnSuaDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSuaDV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btnSuaDV.setText("Cập nhật");
        btnSuaDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaDVActionPerformed(evt);
            }
        });

        btnThemdv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnThemdv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        btnThemdv.setText("Thêm");
        btnThemdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemdvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnXoaDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThemdv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSuaDV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 29, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(btnLamMoi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoaDV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(btnThemdv)
                .addGap(18, 18, 18)
                .addComponent(btnSuaDV)
                .addContainerGap())
        );

        txtGhiChu.setColumns(20);
        txtGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGhiChu.setRows(5);
        jScrollPane21.setViewportView(txtGhiChu);

        javax.swing.GroupLayout jPnThemDvLayout = new javax.swing.GroupLayout(jPnThemDv);
        jPnThemDv.setLayout(jPnThemDvLayout);
        jPnThemDvLayout.setHorizontalGroup(
            jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnThemDvLayout.createSequentialGroup()
                .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnThemDvLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel73)
                            .addComponent(jLabel76)
                            .addComponent(jLabel77))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGia, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaDV, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbGgs, 0, 154, Short.MAX_VALUE))
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnThemDvLayout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel74)
                                    .addComponent(jLabel75)))
                            .addGroup(jPnThemDvLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_GiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenDV, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(78, 78, 78)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPnThemDvLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 1132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPnThemDvLayout.setVerticalGroup(
            jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnThemDvLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnThemDvLayout.createSequentialGroup()
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel74)
                            .addComponent(txtTenDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel75)
                            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPnThemDvLayout.createSequentialGroup()
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel73))
                        .addGap(18, 18, 18)
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel76))
                        .addGap(18, 18, 18)
                        .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_GiamGia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPnThemDvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbGgs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phiếu Dịch Vụ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        tbTTDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbTTDichVu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Số phòng", "Dịch vụ", "Mã dịch vụ", "Ngày sử dụng", "Số lần", "Giảm giá", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbTTDichVu.setRowHeight(25);
        tbTTDichVu.getTableHeader().setReorderingAllowed(false);
        tbTTDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTTDichVuMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbTTDichVu);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPnThemDv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addComponent(jPnThemDv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane6.setViewportView(jPanel39);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabTrangChu.addTab("Dịch vụ", jPanel4);

        jPanel8.setBackground(new java.awt.Color(255, 204, 204));

        jPanel29.setBackground(new java.awt.Color(255, 204, 204));

        jLabel79.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel79.setText("Mã Khách hàng:");

        txtMaKHFormKh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaKHFormKh.setEnabled(false);

        jLabel80.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel80.setText("Tên Khách Hàng:");

        txt_tenKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel81.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel81.setText("Ngày sinh:");

        jLabel82.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel82.setText("Giới tính:");

        jLabel83.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel83.setText("Số CCCD:");

        jLabel84.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel84.setText("Số điện thoại:");

        jLabel85.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel85.setText("Địa chỉ:");

        txt_sdt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_CCCD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnSua2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSua2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btnSua2.setText("Cập nhật");
        btnSua2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSua2ActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnLammoi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLammoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btnLammoi.setText("Làm mới");
        btnLammoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLammoiActionPerformed(evt);
            }
        });

        csDateKH.setDateFormatString("dd/MM/yyyy");
        csDateKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtDiaChiKh.setColumns(20);
        txtDiaChiKh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDiaChiKh.setRows(5);
        jScrollPane22.setViewportView(txtDiaChiKh);

        rdNamKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdNamKH.setText("Nam");

        rdNuKh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdNuKh.setText("Nữ");

        jLabel78.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_male_24px_1.png"))); // NOI18N

        jLabel88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_female_24px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel80)
                                .addComponent(jLabel79))
                            .addComponent(jLabel82)
                            .addComponent(jLabel83)
                            .addComponent(jLabel84)
                            .addComponent(jLabel81)
                            .addComponent(jLabel85))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLammoi)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(btnSua2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete))
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(csDateKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(txt_sdt, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel30Layout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addComponent(rdNamKH)
                            .addGap(0, 0, 0)
                            .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(62, 62, 62)
                            .addComponent(rdNuKh)
                            .addGap(0, 0, 0)
                            .addComponent(jLabel88))
                        .addComponent(txt_CCCD, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_tenKH)
                        .addComponent(txtMaKHFormKh)))
                .addGap(22, 22, Short.MAX_VALUE))
        );

        jPanel30Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDelete, btnLammoi, btnSua2});

        jPanel30Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel79, jLabel80, jLabel81, jLabel82, jLabel83, jLabel84, jLabel85});

        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(txtMaKHFormKh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_tenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdNamKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel82)
                        .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel88, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdNuKh)))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_CCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(csDateKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel85)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLammoi)
                    .addComponent(btnSua2)
                    .addComponent(btnDelete))
                .addGap(60, 60, 60))
        );

        jPanel30Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csDateKH, txt_sdt});

        jPanel30Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDelete, btnLammoi, btnSua2});

        jLabel87.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel87.setText("Nhập tên khách hàng:");

        txtTimkiemKh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_16px.png"))); // NOI18N
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTimkiemKh, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimkiemKh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addContainerGap())
        );

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        tblTTKhach.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblTTKhach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã khách hàng", "Tên", "Ngày sinh", "Giới tính", "Số căn cước", "Số điện thoại", "Địa chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTTKhach.setRowHeight(25);
        tblTTKhach.getTableHeader().setReorderingAllowed(false);
        tblTTKhach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTTKhachMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblTTKhach);
        if (tblTTKhach.getColumnModel().getColumnCount() > 0) {
            tblTTKhach.getColumnModel().getColumn(0).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblTTKhach.getColumnModel().getColumn(1).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(2).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(3).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(3).setPreferredWidth(30);
            tblTTKhach.getColumnModel().getColumn(4).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(5).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(197, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(343, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1456, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 909, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabTrangChu.addTab("Khách hàng", jPanel8);

        jPanel13.setBackground(new java.awt.Color(255, 204, 204));

        tblBill.setBackground(new java.awt.Color(242, 242, 242));
        tblBill.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Stt", "Mã Hóa đơn", "Khách hàng", "Nhân viên", "Ngày tạo HĐ", "Ngày thuê", "Ngày trả", "Giá tiền", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBill.setRowHeight(25);
        tblBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBillMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblBill);
        if (tblBill.getColumnModel().getColumnCount() > 0) {
            tblBill.getColumnModel().getColumn(0).setResizable(false);
            tblBill.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblBill.getColumnModel().getColumn(1).setResizable(false);
            tblBill.getColumnModel().getColumn(2).setResizable(false);
            tblBill.getColumnModel().getColumn(3).setResizable(false);
            tblBill.getColumnModel().getColumn(4).setResizable(false);
            tblBill.getColumnModel().getColumn(5).setResizable(false);
            tblBill.getColumnModel().getColumn(6).setResizable(false);
            tblBill.getColumnModel().getColumn(7).setResizable(false);
            tblBill.getColumnModel().getColumn(8).setResizable(false);
        }

        jPanel17.setBackground(new java.awt.Color(255, 255, 204));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel47.setText("Mã:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Giá tiền:");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel45.setText("Trạng thái:");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel46.setText("Ngày tạo:");

        txtMa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMa.setEnabled(false);
        txtMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaActionPerformed(evt);
            }
        });

        txtTien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTien.setEnabled(false);
        txtTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienActionPerformed(evt);
            }
        });

        btn_sua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Dollar_Coin_16px.png"))); // NOI18N
        btn_sua.setText("Thanh toán");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btnXoa.setText("Xóa hóa đơn");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        cbTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chưa thanh toán", "Đã thanh toán" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Khách hàng:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Nhân viên tạo:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Ngày thuê:");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel32.setText("Ngày trả phong:");

        txtNvTao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNvTao.setEnabled(false);

        txtTenKh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenKh.setEnabled(false);

        txtNgayThue.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNgayThue.setEnabled(false);

        txtNgayTra.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNgayTra.setEnabled(false);

        txtNgayTao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNgayTao.setEnabled(false);

        btnResetHd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnResetHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btnResetHd.setText("Reset");
        btnResetHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetHdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayTao)
                            .addComponent(txtTien, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMa, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbTrangThai, 0, 183, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel32)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTenKh)
                    .addComponent(txtNvTao)
                    .addComponent(txtNgayThue)
                    .addComponent(txtNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetHd)
                    .addComponent(btn_sua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel17Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnResetHd, btnXoa, btn_sua});

        jPanel17Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel12, jLabel45, jLabel46, jLabel47});

        jPanel17Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel32, jLabel7, jLabel8});

        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(txtTenKh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(btnResetHd)
                        .addGap(49, 49, 49))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel47))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtNvTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(btn_sua)
                                .addGap(27, 27, 27)
                                .addComponent(btnXoa)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txtNgayThue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32)
                            .addComponent(txtNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))))
        );

        chkTT.setBackground(new java.awt.Color(255, 204, 204));
        chkTT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkTT.setText("Hóa đơn đã thanh toán");
        chkTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTTActionPerformed(evt);
            }
        });

        chkCTT.setBackground(new java.awt.Color(255, 204, 204));
        chkCTT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkCTT.setText("Hóa đơn chưa thanh toán");
        chkCTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCTTActionPerformed(evt);
            }
        });

        chkALL.setBackground(new java.awt.Color(255, 204, 204));
        chkALL.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkALL.setText("Tất cả");
        chkALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkALLActionPerformed(evt);
            }
        });

        txtTkHd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnTKHd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnTKHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_16px.png"))); // NOI18N
        btnTKHd.setText("Tìm kiếm");
        btnTKHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTKHdActionPerformed(evt);
            }
        });

        jLabel89.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel89.setText("Nhập mã hóa đơn:");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel89)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtTkHd, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTKHd)))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel89)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTkHd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTKHd))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane12)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkCTT)
                            .addComponent(chkALL, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkTT, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkALL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkCTT)
                        .addGap(18, 18, 18)
                        .addComponent(chkTT)))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabTrangChu.addTab("Hóa đơn", jPanel6);

        jPanel12.setBackground(new java.awt.Color(255, 204, 204));

        tb_qlnv.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_qlnv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Giới tính", "Địa chỉ", "Số căn cước", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_qlnv.setRowHeight(25);
        tb_qlnv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_qlnvMouseClicked(evt);
            }
        });
        jScrollPane23.setViewportView(tb_qlnv);
        if (tb_qlnv.getColumnModel().getColumnCount() > 0) {
            tb_qlnv.getColumnModel().getColumn(1).setResizable(false);
            tb_qlnv.getColumnModel().getColumn(2).setResizable(false);
            tb_qlnv.getColumnModel().getColumn(3).setResizable(false);
            tb_qlnv.getColumnModel().getColumn(4).setResizable(false);
            tb_qlnv.getColumnModel().getColumn(5).setResizable(false);
            tb_qlnv.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel33.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_timkiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_timkiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_16px.png"))); // NOI18N
        btn_timkiem.setText("Tìm kiếm");
        btn_timkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timkiemActionPerformed(evt);
            }
        });

        btn_loat.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_loat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btn_loat.setText("Làm mới");
        btn_loat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loatActionPerformed(evt);
            }
        });

        btn_xoa1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_xoa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btn_xoa1.setText("Xóa");
        btn_xoa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoa1ActionPerformed(evt);
            }
        });

        btn_sua2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_sua2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btn_sua2.setText("Sửa");
        btn_sua2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sua2ActionPerformed(evt);
            }
        });

        btn_them1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_them1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        btn_them1.setText("Thêm");
        btn_them1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_them1ActionPerformed(evt);
            }
        });

        jab1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jab1.setText("Giới tính:");

        rd_nu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rd_nu.setText("Nữ");
        rd_nu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_nuActionPerformed(evt);
            }
        });

        rd_nam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rd_nam.setText("Nam");

        txt_timkiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_address.setColumns(20);
        txt_address.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_address.setRows(5);
        jScrollPane24.setViewportView(txt_address);

        csNgaySinhnv.setDateFormatString("dd/MM/yyyy");
        csNgaySinhnv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_name.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_code.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_code.setEnabled(false);

        jab.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jab.setText("Ngày sinh: ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Tên nhân viên:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Mã nhân viên:");

        jLabel86.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel86.setText("Địa chỉ: ");

        txt_phone.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Phone:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("CCCD: ");

        txt_idpre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        rdQl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdQl.setText("Quản lý");

        rdNv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdNv.setText("Nhân viên");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Chức vụ:");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setText("Nhập mã nhân viên:");

        jLabel92.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_male_24px_1.png"))); // NOI18N

        jLabel93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_female_24px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jab)
                                    .addComponent(jab1))
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(csNgaySinhnv, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(txt_name)
                            .addComponent(txt_code)
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addComponent(rd_nam)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(rd_nu)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel93)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(18, 18, 18)
                                    .addComponent(txt_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel33Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(18, 18, 18)
                                    .addComponent(txt_idpre, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addComponent(jLabel86)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGap(0, 99, Short.MAX_VALUE)
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btn_timkiem)))
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdNv)
                    .addComponent(rdQl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_them1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_sua2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_xoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_loat, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel33Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_loat, btn_sua2, btn_them1, btn_timkiem, btn_xoa1});

        jPanel33Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3, jab, jab1});

        jPanel33Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel4, jLabel5, jLabel86});

        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(0, 16, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(btn_them1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(btn_sua2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_xoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(btn_loat, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(rdNv))
                                .addGap(18, 18, 18)
                                .addComponent(rdQl))
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txt_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txt_idpre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(txt_phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel86)
                                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jab, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(csNgaySinhnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel92, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(rd_nam)
                                            .addComponent(rd_nu)
                                            .addComponent(jab1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );

        jPanel33Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_loat, btn_sua2, btn_them1, btn_timkiem, btn_xoa1});

        jPanel33Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csNgaySinhnv, txt_name});

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane23)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabTrangChu.addTab("Nhân viên", jPanel5);

        jPanel15.setBackground(new java.awt.Color(255, 204, 204));

        jScrollPane16.setBackground(new java.awt.Color(255, 204, 204));
        jScrollPane16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel18.setBackground(new java.awt.Color(255, 204, 204));

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cập nhật phòng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        btnxoa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnxoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btnxoa.setText("Xóa phòng");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        tbqlp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbqlp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Trạng thái", "Loại Phòng", "Giảm giá", "Mã", "Số Phòng", "Diện tích", "Vị trí Phòng", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbqlp.setRowHeight(25);
        tbqlp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbqlpMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tbqlp);
        if (tbqlp.getColumnModel().getColumnCount() > 0) {
            tbqlp.getColumnModel().getColumn(0).setResizable(false);
            tbqlp.getColumnModel().getColumn(0).setPreferredWidth(40);
            tbqlp.getColumnModel().getColumn(1).setResizable(false);
            tbqlp.getColumnModel().getColumn(2).setResizable(false);
            tbqlp.getColumnModel().getColumn(3).setResizable(false);
            tbqlp.getColumnModel().getColumn(4).setResizable(false);
            tbqlp.getColumnModel().getColumn(5).setResizable(false);
            tbqlp.getColumnModel().getColumn(6).setResizable(false);
            tbqlp.getColumnModel().getColumn(7).setResizable(false);
            tbqlp.getColumnModel().getColumn(8).setResizable(false);
        }

        btnreset.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnreset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btnreset.setText("Reset");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
            }
        });

        cbstt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbstt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sẵn Sàng", "Có Khách", "Chưa Dọn", "Đang Dọn", "Đang Sửa" }));

        cblp.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cblp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phòng Đơn", "Phòng Đôi", "Phòng Vip" }));

        jLabel62.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel62.setText("Mã giảm giá");

        cbmgg.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbmgg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Giảm giá" }));
        cbmgg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmggActionPerformed(evt);
            }
        });

        txtsp.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btntk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btntk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_16px.png"))); // NOI18N
        btntk.setText("Tìm kiếm");
        btntk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntkActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel64.setText("Trạng thái:");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel65.setText("Tìm kiếm theo mã:");

        txtdt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel66.setText("Loại Phòng:");

        txttk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel67.setText("Mã phòng:");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel68.setText("Số phòng:");

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel69.setText("Diện tích:");

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel70.setText("Vị Trí:");

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel71.setText("Giá:");

        txtg.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnthem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnthem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        btnthem.setText("Thêm phòng");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        btnsua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnsua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btnsua.setText("Sửa phòng");
        btnsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaActionPerformed(evt);
            }
        });

        btnthemnhanh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnthemnhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Plus_16px_1.png"))); // NOI18N
        btnthemnhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemnhanhActionPerformed(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel72.setText("m2");

        lbma.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbma.setText("-");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel28.setText("VNĐ");

        cbVt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbVt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cblp, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbVt, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbstt, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbma, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtsp))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtg)
                                    .addComponent(txtdt, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addGap(18, 18, 18)
                        .addComponent(cbmgg, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel72, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(btnthemnhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnsua)
                    .addComponent(btnthem)
                    .addComponent(btnxoa)
                    .addComponent(btnreset))
                .addGap(220, 220, 220))
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(238, 238, 238)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txttk, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btntk))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 1163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel21Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnreset, btnsua, btnthem, btnxoa});

        jPanel21Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel64, jLabel66, jLabel67, jLabel70});

        jPanel21Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel62, jLabel68, jLabel69, jLabel71});

        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btntk)))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel67)
                            .addComponent(lbma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel64)
                            .addComponent(cbstt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel66)
                            .addComponent(cblp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbVt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel70))
                        .addGap(24, 24, 24))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(btnthem)
                                .addGap(18, 18, 18)
                                .addComponent(btnsua)
                                .addGap(18, 18, 18)
                                .addComponent(btnxoa)
                                .addGap(18, 18, 18)
                                .addComponent(btnreset))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbmgg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnthemnhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel62)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel68)
                                    .addComponent(txtsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel69)
                                    .addComponent(txtdt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel72))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel71)
                                    .addComponent(txtg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel21Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtdt, txtg, txtsp});

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý thiết bị", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cập nhật thiết bị trong phòng", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        jLabel94.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel94.setText("Tên thiết bị:");

        jLabel95.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel95.setText("Trạng thái:");

        jLabel96.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel96.setText("Số lượng:");

        txt_amount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tb_roomItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_roomItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Số phòng", "Tên thiết bị", "Trạng thái", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_roomItem.setRowHeight(25);
        tb_roomItem.getTableHeader().setReorderingAllowed(false);
        tb_roomItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_roomItemMouseClicked(evt);
            }
        });
        jScrollPane25.setViewportView(tb_roomItem);
        if (tb_roomItem.getColumnModel().getColumnCount() > 0) {
            tb_roomItem.getColumnModel().getColumn(0).setResizable(false);
            tb_roomItem.getColumnModel().getColumn(1).setResizable(false);
            tb_roomItem.getColumnModel().getColumn(2).setResizable(false);
            tb_roomItem.getColumnModel().getColumn(4).setResizable(false);
        }

        txt_statusRI.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel97.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel97.setText("Số phòng:");

        btn_them2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_them2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        btn_them2.setText("Thêm");
        btn_them2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_them2ActionPerformed(evt);
            }
        });

        btn_sua1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_sua1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btn_sua1.setText("Sửa");
        btn_sua1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sua1ActionPerformed(evt);
            }
        });

        btn_xoa2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_xoa2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btn_xoa2.setText("Xóa");
        btn_xoa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoa2ActionPerformed(evt);
            }
        });

        cbmtb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbmtb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn thiết bị" }));
        cbmtb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmtbActionPerformed(evt);
            }
        });

        cbmp.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn phòng" }));
        cbmp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbmpItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel95, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel94, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel96)
                            .addComponent(jLabel97))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_statusRI, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_amount)
                            .addComponent(cbmtb, 0, 199, Short.MAX_VALUE)
                            .addComponent(cbmp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_sua1)
                            .addComponent(btn_xoa2)
                            .addComponent(btn_them2)))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel34Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_sua1, btn_them2, btn_xoa2});

        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(btn_them2)
                    .addComponent(cbmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(cbmtb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_sua1))
                .addGap(27, 27, 27)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(txt_statusRI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_xoa2))
                .addGap(26, 26, 26)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel96))
                .addGap(58, 58, 58)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cập nhật danh sách thiết bị", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        btn_them.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        btn_xoa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        btn_search.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_16px.png"))); // NOI18N
        btn_search.setText("Search");
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel43.setText("Tên thiết bị:");

        btn_clear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setText("Mã:");

        jLabel98.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel98.setText("Nhập mã:");

        txt_tk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_code1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_code1.setEnabled(false);

        txt_name1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btn_sua3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_sua3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btn_sua3.setText("Sửa");
        btn_sua3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sua3ActionPerformed(evt);
            }
        });

        tb_item.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Code", "Name"
            }
        ));
        tb_item.setRowHeight(25);
        tb_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_itemMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tb_item);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_code1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(jLabel98)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_tk, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btn_them)
                                    .addComponent(jLabel43))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_name1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                        .addComponent(btn_sua3)
                                        .addGap(31, 31, 31)))))
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(btn_search)
                                .addGap(89, 89, 89))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel23Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(btn_xoa)
                                .addGap(28, 28, 28)
                                .addComponent(btn_clear)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel23Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_clear, btn_sua3, btn_them, btn_xoa});

        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_tk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98)
                    .addComponent(btn_search))
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(txt_code1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44)))
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(txt_name1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43)))
                .addGap(30, 30, 30)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_sua3)
                    .addComponent(btn_xoa)
                    .addComponent(btn_clear))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        jScrollPane16.setViewportView(jPanel18);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabTrangChu.addTab("Cơ sở vật chất", jPanel7);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(txtTenKS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 842, Short.MAX_VALUE)
                        .addComponent(lbThoiGian)
                        .addGap(25, 25, 25))))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jTabTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKS)
                    .addComponent(lbThoiGian))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        reset(new Client());
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnThuePhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThuePhongActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Xác nhận cho thuê phòng.", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (!txtMaPhong.getText().equals("")) {
                // them khach hang
                if (tempCheck == 0) {
                    txtMaKH.setText(rand.createCode("kh", "maKh.txt"));
                    for (Client client : clienService.getAll()) {
                        if (client.getCode().equals(txtMaKH.getText())) {
                            try {
                                readWriteData.ghidl(Integer.parseInt(txtMaKH.getText().substring(2)), "maKh.txt");

                            } catch (IOException ex) {
                                Logger.getLogger(ClientService.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            btnThuePhongActionPerformed(evt);
                        }
                    }
                    Client client = new Client();
                    client.setName(txtTenKhachHang.getText().trim());
                    client.setAddress(txtDiaChi.getText().trim());
                    client.setCustomPhone(txtSDT.getText().trim());
                    client.setDateOfBirth(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(csNgaySinh.getDate())));

                    if (rdNu.isSelected()) {
                        client.setSex("Nữ");
                    } else {
                        client.setSex("Nam");
                    }
                    client.setIdPersonCard(txtCCCD.getText().trim());
                    client.setCode(txtMaKH.getText().trim());
                    String string = clienService.insert(client);
                    if (string != null) {
                        JOptionPane.showMessageDialog(this, string);
                    }
                }
                // them hoa don
                List<Client> listKh = clienService.checkTrung(txtCCCD.getText().trim());
                if (listKh == null || listKh.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng");
                    return;
                }
                String idClient = listKh.get(0).getId();

                long noDay = between2Dates.daysBetween2Dates(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), new SimpleDateFormat("yyyy-MM-dd").format(csTraPhong.getDate()));
                if (noDay == 0) {
                    JOptionPane.showMessageDialog(this, "Chọn ngày trả phòng");
                    return;
                }
                if (billService.searchHd(idClient) == null) {
                    Bill bill = new Bill();
                    if (clienService.checkTrung(txtCCCD.getText().trim()) == null) {
                        return;
                    }
                    bill.setIdClient(idClient);
                    bill.setIdStaff(auth.id);
                    String maHd = rand.createCode("hd", "maHd.txt");
                    for (Bill billTemp : billService.getAll()) {
                        if (billTemp.getCode().equals(maHd)) {
                            try {
                                readWriteData.ghidl(Integer.parseInt(maHd.substring(2)), "maHd.txt");

                            } catch (IOException ex) {
                                Logger.getLogger(ClientService.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            btnThuePhongActionPerformed(evt);
                        }
                    }
                    bill.setCode(maHd);
                    bill.setPrice(String.valueOf(noDay * (Float.parseFloat(txtGiaPhong.getText().trim()) - Float.parseFloat(txtGiaGiam.getText().trim()))));
                    bill.setStatus("0");// 0 chua thanh toan
                    bill.setDate(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                    billService.insert(bill);
                }
                // thêm phòng vào hóa đơn chi tiết
                BillRoom roomBill = new BillRoom();
                Room room = roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0);
                roomBill.setBillId(billService.searchHd(idClient).get(0).getId());// id hoa don
                roomBill.setRoomId(room.getId());
                roomBill.setPriceRoom(txtGiaPhong.getText());
                roomBill.setPromotionRoom(txtGiaGiam.getText());
                roomBill.setDateCheckIn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

                roomBill.setDateCheckout(new SimpleDateFormat("yyyy-MM-dd 12:00:00").format(csTraPhong.getDate()));
                roomBillService.insert(roomBill);
                JOptionPane.showMessageDialog(this, "Thành Công!!");
                room.setStatus("2");
                roomService.update(room, room.getRoomNumber());
                if (room.getLocation().equals("Tầng 1")) {
                    jPnTang1.removeAll();
                }
                if (room.getLocation().equals("Tầng 2")) {
                    jPnTang2.removeAll();
                }
                if (room.getLocation().equals("Tầng 3")) {
                    jPnTang3.removeAll();
                }
                loadSl();
                loadPanel(room.getLocation(), "0");
                btnResetActionPerformed(evt);
                btnHuyPhongActionPerformed(evt);
                loadKH();
                loadBill();
            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn phòng");
                return;
            }
        }
    }//GEN-LAST:event_btnThuePhongActionPerformed

    private void btnDoiPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiPhongActionPerformed

        if (temp == 0) {
            temp = 1;
            txtSoPhong.setEnabled(true);
            return;
        }
        if (temp == 1) {
            if (roomService.getRoomByNumber(txtSoPhong.getText().trim()) == null || !roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0).getStatus().equals("1")) {
                JOptionPane.showMessageDialog(this, "Nhập sai số phòng hoặc phòng chưa sẵn sàng.");
                return;
            }
            PromotionRService promotionRService = new PromotionRService();

            Room room = roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0);
            String ngay = String.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
            PromotionR pr = promotionRService.searchPromotionR(room.getIdPromotion(), ngay);
            if (pr != null) {
                txtGiaGiam.setText(pr.getValue());
            } else {
                txtGiaGiam.setText("0");
            }
            if (room.getStatus().equals("1")) {
                fillRoom(room);
            }
            ViewModelItemService viewItemService = new ViewModelItemService();
            if (viewItemService.getAll(room.getId()) != null) {
                DefaultTableModel defaultTableModel = (DefaultTableModel) tbNoiThat.getModel();
                defaultTableModel.setRowCount(0);
                for (ViewModelItem item : viewItemService.getAll(room.getId())) {
                    defaultTableModel.addRow(new Object[]{item.getName(), item.getStatus(), item.getAmount()});
                }
            }
            txtSoPhong.setEnabled(false);
            temp = 0;
        }
    }//GEN-LAST:event_btnDoiPhongActionPerformed

    private void btnQuetMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetMaActionPerformed
        QrCode qrCode = new QrCode();
        if (qrCode.isVisible() == true) {
            qrCode.show();
        } else {
            qrCode.setVisible(true);
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1500);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewTrangChu.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    if (qrCode.temp == 1) {
                        fillClient(qrCode.client);
                        return;
                    }
                }
            }
        }.start();
        qrCode.temp = 0;
    }//GEN-LAST:event_btnQuetMaActionPerformed

    private void pnInforKhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnInforKhMouseEntered
        threadChuY t1 = new threadChuY();
        if (txtMaKH.getText().equals("")) {
            t1.start();
        } else {
            t1.stop();
        }

    }//GEN-LAST:event_pnInforKhMouseEntered

    private void btnThemDvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDvActionPerformed
        if (roomService.getRoomByNumber(txtSoPhongDV.getText().trim()) == null) {
            JOptionPane.showMessageDialog(this, "Xem lại số phòng");
            return;
        }
        String idRoom = roomService.getRoomByNumber(txtSoPhongDV.getText().trim()).get(0).getId();
        String idService = serviceService.getByCode(txtMaDv.getText().trim()).get(0).getId();
        String idBill = billService.getId(txtSoPhongDV.getText().trim(), new java.util.Date());
        model.RoomBillService roomBillService = new model.RoomBillService();
        roomBillService.setIdBill(idBill);
        roomBillService.setIdRoom(idRoom);
        roomBillService.setIdService(idService);
        roomBillService.setPriceService(txtGiaDv.getText().trim());
        roomBillService.setPromotionService(txtGiamGiaDV.getText().trim());
        roomBillService.setDateofHire(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        JOptionPane.showMessageDialog(this, roomBillServiceService.insert(roomBillService));
        loadDV();
    }//GEN-LAST:event_btnThemDvActionPerformed

    private void cbDichVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDichVuItemStateChanged
        for (Service service : serviceService.getAll()) {
            if (cbDichVu.getSelectedItem().equals(service.getName())) {
                csNgaySd.setDate(new java.util.Date());
                txtMaDv.setText(service.getCode());
                txtGiaDv.setText(service.getPrice());

                String ngay = String.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
                PromotionS pr = promotionSService.searchPromotionS(service.getIdPromotion(), ngay);
                if (pr != null) {
                    txtGiamGiaDV.setText(pr.getValue());
                } else {
                    txtGiamGiaDV.setText("0");
                }
            }
        }
    }//GEN-LAST:event_cbDichVuItemStateChanged

    private void btnHuyPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyPhongActionPerformed
        Room room = new Room();
        fillRoom(room);
        if (txtSoPhong.isEnabled() == true) {
            temp = 0;
            txtSoPhong.setEnabled(false);
        }
        csTraPhong.setDate(new java.util.Date());
        txtGiaGiam.setText("0");
    }//GEN-LAST:event_btnHuyPhongActionPerformed

    private void jMenuThuePhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuThuePhongActionPerformed
        PromotionRService promotionRService = new PromotionRService();
        Room room = roomService.getRoomByNumber(tenPhong).get(0);
        if (room.getStatus().equals("1")) {
            String ngay = String.valueOf(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE));
            PromotionR pr = promotionRService.searchPromotionR(room.getIdPromotion(), ngay);
            if (pr != null) {
                txtGiaGiam.setText(pr.getValue());
            }
            ViewModelItemService viewItemService = new ViewModelItemService();
            if (viewItemService.getAll(room.getId()) != null) {
                DefaultTableModel defaultTableModel = (DefaultTableModel) tbNoiThat.getModel();
                defaultTableModel.setRowCount(0);
                for (ViewModelItem item : viewItemService.getAll(room.getId())) {
                    defaultTableModel.addRow(new Object[]{item.getName(), item.getStatus(), item.getAmount()});
                }
            }
            fillRoom(room);
            jTabTrangChu.setSelectedIndex(1);
        } else {
            JOptionPane.showMessageDialog(this, "Phòng chưa sẵn sàng cho thuê");
            txtSoPhong.setText("");
        }
    }//GEN-LAST:event_jMenuThuePhongActionPerformed

    private void cbDichVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbDichVuMouseClicked
        loadCbDv();
    }//GEN-LAST:event_cbDichVuMouseClicked

    private void btnDxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDxActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Muốn đăng xuất khỏi chương trình?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            QrCode.client = null;
            new ViewDangNhap().setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnDxActionPerformed

    private void jMenuSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSSActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(204, 204, 255));
        Room room = new Room();
        room.setStatus("1");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();
        loadPhongSS();
    }//GEN-LAST:event_jMenuSSActionPerformed

    private void jMenuCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCDActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(204, 255, 204));
        Room room = new Room();
        room.setStatus("3");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();
        loadPhongSS();

    }//GEN-LAST:event_jMenuCDActionPerformed

    private void jMenuDDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDDActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(221, 216, 216));
        Room room = new Room();
        room.setStatus("4");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();
        loadPhongSS();
    }//GEN-LAST:event_jMenuDDActionPerformed

    private void jMenuSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSCActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(255, 153, 0));
        Room room = new Room();
        room.setStatus("5");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();
        loadPhongSS();

    }//GEN-LAST:event_jMenuSCActionPerformed

    private void menuDichVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDichVuActionPerformed
        Room room = roomService.getRoomByNumber(tenPhong).get(0);
        if (room.getStatus().equals("2")) {
            jTabTrangChu.setSelectedIndex(3);
            txtSoPhongDV.setText(tenPhong);
        } else {
            JOptionPane.showMessageDialog(this, "Phòng chưa được thuê.");
        }
    }//GEN-LAST:event_menuDichVuActionPerformed

    public void SetdataCheckout(ThongtinCheckout tt) {
        Tinhtien tinh = new Tinhtien();

        double phuthu = tinh.tiSoCheckOut(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())), tt.getIdbill());
        double phuthu1 = tinh.tiSoCheckIn(tt.getCheckin(), tt.getIdbill());
        phuthu1 = (double) Math.round(phuthu1 * 100) / 100;
        phuthu = (double) Math.round(phuthu * 100) / 100;

        double tongtien = Double.parseDouble(tinh.tinhTienDv(tt.getIdbill()))
                + Double.parseDouble(tinh.tinhTienPhong(phuthu, phuthu1, tt.getIdbill()));
        txtKhachhangTP.setText(tt.getTenkhachhang());
        txtCccdTP.setText(tt.getCCCD());
        txtMahdTP.setText(tt.getMaHD());
        txtSophongTP.setText(tt.getSophong());
        tt.setCheckin(tt.getCheckin().substring(8, 10) + "/" + tt.getCheckin().substring(5, 7) + "/" + tt.getCheckin().substring(0, 4) + " " + tt.getCheckin().substring(11));
        try {
            csCheckinTP.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(tt.getCheckin()));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        tt.setCheckout(tt.getCheckout().substring(8, 10) + "/" + tt.getCheckout().substring(5, 7) + "/" + tt.getCheckout().substring(0, 4) + " " + tt.getCheckout().substring(11));
        try {
            csLichTraPhong.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(tt.getCheckout()));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        tt.setCheckout(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
        try {
            csCheckoutTP.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(tt.getCheckout()));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
//        csCheckinTP.setDateFormatString(tt.getCheckin());
//        csCheckoutTP.setDate(new java.util.Date());
        txtGiaphongTP.setText(tt.getGiaphong());
        txtGiamgiaTP.setText(tt.getGiamgia());
        if (tt.getTrangthaiHD() == 0) {
            cbbTrangthaiTP.setSelectedIndex(1);
        }
        if (tt.getTrangthaiHD() == 1) {
            cbbTrangthaiTP.setSelectedIndex(2);
        }
        txtThanhtienTP.setText(String.valueOf(tongtien));
        String tongPhuThu = null;

        tongPhuThu = (phuthu1 > 1.0 && phuthu1 < 1.5) ? "Thêm 30% tổng tiền thuê phòng sớm" : (phuthu1 > 1.4) ? "Thêm 50% tổng tiền thuê phòng sớm" : "";
        if (phuthu > 1 && !tongPhuThu.equals("")) {
            tongPhuThu = tongPhuThu + " và " + ((phuthu < 1.5) ? "\n30% tổng tiền trả phòng muộn" : (phuthu > 1.4 && phuthu < 1.6) ? "\n50% tổng tiền trả phòng muộn" : (phuthu > 1.9) ? "\n100% tổng tiền trả phòng muộn" : "");
            txtPhuthuTP.setText(tongPhuThu);
            return;
        }
        if (phuthu > 1 && tongPhuThu.equals("")) {
            tongPhuThu = tongPhuThu + ((phuthu < 1.5) ? "30% tổng tiền trả phòng muộn" : (phuthu > 1.4 && phuthu < 1.6) ? "50% tổng tiền trả phòng muộn" : (phuthu > 1.9) ? "100% tổng tiền trả phòng muộn" : "");
            txtPhuthuTP.setText(tongPhuThu);
            return;
        }
        tongPhuThu = "";
        txtPhuthuTP.setText(tongPhuThu);

    }
    private void jMenuCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCheckoutActionPerformed
        // Checkout
        CheckoutService cs = new CheckoutService();
        Room room = roomService.getRoomByNumber(tenPhong).get(0);
        if (room.getStatus().equals("2")) {
            jTabTrangChu.setSelectedIndex(2);
            //Hien thi bang DV:
            DefaultTableModel defaultTableModelds = (DefaultTableModel) tblDVTP.getModel();
            defaultTableModelds.setRowCount(0);
            double gia = 0;
            for (DVcheckout x : cs.GetDVcheckout(room.getId(), roomBillService.getAll().get(0).getBillId())) {

                defaultTableModelds.addRow(new Object[]{
                    x.getTen(), x.getMa(), x.getSoluong(), x.getGiamgia() + " vnđ", x.getGia() + " vnđ", (Double.parseDouble(x.getGia()) - Double.parseDouble(x.getGiamgia())) * Integer.parseInt(x.getSoluong()) + " vnđ"});
                gia = gia + (Double.parseDouble(x.getGia()) - Double.parseDouble(x.getGiamgia())) * Integer.parseInt(x.getSoluong());
            }
            defaultTableModelds.addRow(new Object[]{"Tổng tiền", "", "", "", "", gia + " vnđ"});
            //Hien thi thong tin len form
            List<ThongtinCheckout> list = cs.Getthongtincheckout(room.getId(), roomBillService.getAll().get(0).getBillId());
            System.out.println(list);
            for (ThongtinCheckout tt : cs.Getthongtincheckout(room.getId(), roomBillService.getAll().get(0).getBillId())) {
                SetdataCheckout(tt);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Phòng chưa được thuê.");
            return;
        }
    }//GEN-LAST:event_jMenuCheckoutActionPerformed

    private void btnxoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaActionPerformed
        // TODO add your handling code here:
//        int row = tbqlp.getSelectedRow();
        if (lbma.equals("-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần xóa!");
        } else {
            Room r = this.getFromDataDelete();
            if (!r.getStatus().equals("Có Khách")) {
                int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?", "message", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {

                    this.roomService.delete(getIdFromData());
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataRoom();
                    if (r.getLocation().equals("Tầng 1")) {
                        jPnTang1.removeAll();
                    }
                    if (r.getLocation().equals("Tầng 2")) {
                        jPnTang2.removeAll();
                    }
                    if (r.getLocation().equals("Tầng 3")) {
                        jPnTang3.removeAll();
                    }
                    loadPanel(r.getLocation(), "0");
                    loadSl();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Phòng đang có khách không thể xóa.");
                return;
            }

        }
    }//GEN-LAST:event_btnxoaActionPerformed

    private void tbqlpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbqlpMouseClicked
        // TODO add your handling code here:
        int row = tbqlp.getSelectedRow();
        cbstt.setSelectedItem(this.tbqlp.getValueAt(row, 1).toString());
        cblp.setSelectedItem(tbqlp.getValueAt(row, 2).toString());
        cbmgg.setSelectedItem(this.tbqlp.getValueAt(row, 3).toString());
        lbma.setText(this.tbqlp.getValueAt(row, 4).toString());
        txtsp.setText(this.tbqlp.getValueAt(row, 5).toString());
        cbVt.setSelectedItem(this.tbqlp.getValueAt(row, 7).toString());

        txtdt.setText(this.tbqlp.getValueAt(row, 6).toString().substring(0, this.tbqlp.getValueAt(row, 6).toString().length() - 3));
        txtg.setText(this.tbqlp.getValueAt(row, 8).toString().substring(0, this.tbqlp.getValueAt(row, 8).toString().length() - 4));
    }//GEN-LAST:event_tbqlpMouseClicked

    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
        // TODO add your handling code here:
        lbma.setText("-");
        cbstt.setSelectedIndex(0);
        cblp.setSelectedIndex(0);
        txttk.setText("");
        txtdt.setText("");
        cbVt.setSelectedIndex(0);
        txtg.setText("");
        txtsp.setText("");
        cbmgg.setSelectedIndex(0);
    }//GEN-LAST:event_btnresetActionPerformed

    private void cbmggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmggActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbmggActionPerformed

    private void btntkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntkActionPerformed
        // TODO add your handling code here:
        int check = 0;
        if (txttk.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mời nhập phòng cần tìm!");
        } else {
            for (Room r : this.roomService.getAll()) {
                if (txttk.getText().toUpperCase().equals(r.getRoomNumber())) {
                    this.loadTableSearch();
                    JOptionPane.showMessageDialog(this, "Tìm thành công!");
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                JOptionPane.showMessageDialog(this, "Không có phòng cần tìm!");
            }
        }
    }//GEN-LAST:event_btntkActionPerformed

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
        // TODO add your handling code here:
        if (txtsp.getText().equals("")
                || txtdt.getText().equals("")
                || txtg.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
        } else {
            Room r = this.getFromData();
            if (r != null) {
                r.setStatus(getStt(r.getStatus()));
                r.setKindOfRoom(getKoff(r.getKindOfRoom()));
                this.roomService.insert(r);
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDataRoom();
                if (r.getLocation().equals("Tầng 1")) {
                    jPnTang1.removeAll();
                }
                if (r.getLocation().equals("Tầng 2")) {
                    jPnTang2.removeAll();
                }
                if (r.getLocation().equals("Tầng 3")) {
                    jPnTang3.removeAll();
                }
                loadPanel(r.getLocation(), "0");
                loadSl();
                loadPhongSS();
            }
        }
    }//GEN-LAST:event_btnthemActionPerformed

    private void btnsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuaActionPerformed
        // TODO add your handling code here:
        int row = tbqlp.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần sửa!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa không?", "message", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                Room r = this.getFromDataDelete();
                r.setStatus(getStt(r.getStatus()));
                r.setKindOfRoom(getKoff(r.getKindOfRoom()));
                this.roomService.updateById(getIdFromData(), r);
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadDataRoom();
                if (r.getLocation().equals("Tầng 1")) {
                    jPnTang1.removeAll();
                }
                if (r.getLocation().equals("Tầng 2")) {
                    jPnTang2.removeAll();
                }
                if (r.getLocation().equals("Tầng 3")) {
                    jPnTang3.removeAll();
                }
                loadPanel(r.getLocation(), "0");
            }
        }
    }//GEN-LAST:event_btnsuaActionPerformed

    private void btnthemnhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemnhanhActionPerformed
        // TODO add your handling code here:
        ViewPromotionR main = new ViewPromotionR();
        main.setVisible(true);

    }//GEN-LAST:event_btnthemnhanhActionPerformed

    private void tblBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillMouseClicked
        int row = tblBill.getSelectedRow();
        txtMa.setText(tblBill.getValueAt(row, 1).toString());
        txtTien.setText(tblBill.getValueAt(row, 7).toString());
        String tt = tblBill.getValueAt(row, 8).toString();
        cbTrangThai.setSelectedItem(tt);
        if (tt.equals("Đã thanh toán")) {
            cbTrangThai.setEnabled(false);
        }
        if (tt.equals("Chưa thanh toán")) {
            cbTrangThai.setEnabled(true);

        }
        txtNgayTao.setText(tblBill.getValueAt(row, 4).toString());
        txtTenKh.setText(tblBill.getValueAt(row, 2).toString());
        txtNvTao.setText(tblBill.getValueAt(row, 3).toString());
        txtNgayThue.setText(tblBill.getValueAt(row, 5).toString());
        txtNgayTra.setText(tblBill.getValueAt(row, 6).toString());
        popupHoaDon.show(tblBill, evt.getX(), evt.getY());
        code = tblBill.getValueAt(row, 1).toString();

    }//GEN-LAST:event_tblBillMouseClicked

    private void txtMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaActionPerformed

    private void txtTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTienActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        int row = tblBill.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần sửa!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa không?", "message", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                Bill b = this.getDataDelete();
                b.setCode((b.getCode()));
                b.setPrice(b.getPrice().substring(0, b.getPrice().indexOf(" ")).trim());
                b.setStatus(getSttHD(b.getStatus()));
                b.setDate((b.getDate()));
                this.billService.update(b, this.getIDBill());
//                for (Bill bill : billService.getAll()) {
//                    if (b.getCode().equals(bill.getCode()) && !bill.getStatus().equals("0")) {
//                        JOptionPane.showm
//                    }
//                }
//                System.out.println(this.getIDBill());
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadBill();
            }
        }
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblBill.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần xóa!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?", "message", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                Bill b = this.getDataDelete();
                this.billService.delete(getIDBill());
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadBill();
            }
        }

    }//GEN-LAST:event_btnXoaActionPerformed

    private void chkTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTTActionPerformed
        DefaultTableModel dtm = (DefaultTableModel) tblBill.getModel();
        dtm.setRowCount(0);
        int stt = 0;
        ViewModelHdService modelHdService = new ViewModelHdService();
        for (HoaDon bill : modelHdService.getAll()) {
            if (bill.getStatus().equals("1")) {
                stt++;
                dtm.addRow(new Object[]{
                    stt,
                    bill.getCode(),
                    bill.getNameClient(),
                    bill.getNameStaff(),
                    bill.getDate(),
                    bill.getDateCheckIn(),
                    bill.getDateCheckOut(),
                    bill.getPrice(),
                    bill.getStatus().equals("0") ? "Chưa thanh toán" : "Đã thanh toán",}
                );
            }
        }
    }//GEN-LAST:event_chkTTActionPerformed

    private void chkCTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCTTActionPerformed
        DefaultTableModel dtm = (DefaultTableModel) tblBill.getModel();
        dtm.setRowCount(0);
        int stt = 0;
        ViewModelHdService modelHdService = new ViewModelHdService();
        for (HoaDon bill : modelHdService.getAll()) {
            if (bill.getStatus().equals("0")) {
                stt++;
                dtm.addRow(new Object[]{
                    stt,
                    bill.getCode(),
                    bill.getNameClient(),
                    bill.getNameStaff(),
                    bill.getDate(),
                    bill.getDateCheckIn(),
                    bill.getDateCheckOut(),
                    bill.getPrice(),
                    bill.getStatus().equals("0") ? "Chưa thanh toán" : "Đã thanh toán",}
                );
            }
        }
    }//GEN-LAST:event_chkCTTActionPerformed

    private void chkALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkALLActionPerformed
        loadBill();
    }//GEN-LAST:event_chkALLActionPerformed

    private void btnResetHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetHdActionPerformed
        gr1.clearSelection();
        txtMa.setText("");
        txtNgayTao.setText("");
        txtNgayThue.setText("");
        txtNgayTra.setText("");
        txtTien.setText("");
        txtTenKh.setText("");
        txtNvTao.setText("");
        cbTrangThai.setSelectedIndex(0);
    }//GEN-LAST:event_btnResetHdActionPerformed

    private void btnTKHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTKHdActionPerformed
        if (txtTkHd.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mời nhập mã hóa đơn cần tìm!");
        } else {
            if (this.loadTableSearchHD()) {
                JOptionPane.showMessageDialog(this, "Tìm thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Mã Hóa đơn cần tìm không tồn tại!");
            }
        }
    }//GEN-LAST:event_btnTKHdActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        txtKhachhangTP.setText("");
        txtCccdTP.setText("");
        txtMahdTP.setText("");
        txtGiamgiaTP.setText("");
        csCheckinTP.setDate(null);
        csCheckoutTP.setDate(null);
        csLichTraPhong.setDate(null);
        txtPhuthuTP.setText("");
        txtGiaphongTP.setText("");
        txtThanhtienTP.setText("");
        txtSophongTP.setText("");
        cbbTrangthaiTP.setSelectedIndex(0);
        DefaultTableModel defaultTableModelds = (DefaultTableModel) tblDVTP.getModel();
        defaultTableModelds.setRowCount(0);
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnTraPhongTPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraPhongTPActionPerformed
        if (!txtKhachhangTP.getText().equals("")) {
            jMenuCDActionPerformed(evt);
            String id = null;
            for (Bill bill : billService.getAll()) {
                if (bill.getCode().equals(txtMahdTP.getText())) {
                    id = bill.getId();
                    bill.setPrice(txtThanhtienTP.getText());
                    billService.update(bill, id);
                    BillRoom billRoom = new BillRoom();
                    billRoom.setDateCheckout(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(csCheckoutTP.getDate()));
                    billRoom.setBillId(id);
                    for (Room room : roomService.getAll()) {
                        if (room.getRoomNumber().equals(txtSophongTP)) {
                            billRoom.setRoomId(room.getId());
                        }
                    }
                    roomBillService.update(billRoom, id);
                    loadBill();
                    jTabTrangChu.setSelectedIndex(5);
                    break;
                }
            }

            btnHuyActionPerformed(evt);
        } else {
            JOptionPane.showMessageDialog(this, "Chọn phòng để trả!");
        }
    }//GEN-LAST:event_btnTraPhongTPActionPerformed

    private void btn_GiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GiamGiaActionPerformed
        // TODO add your handling code here:
        ViewPromotionS v = new ViewPromotionS();
        v.setVisible(true);

    }//GEN-LAST:event_btn_GiamGiaActionPerformed

    private void btnThemdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemdvActionPerformed
        // TODO add your handling code here:
        Service s = this.getDV();
        if (s == null) {
            return;
        }
        this.serviceViewModelService.insert(s);
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        loadDichVu();
    }//GEN-LAST:event_btnThemdvActionPerformed

    private void btnSuaDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaDVActionPerformed
        // TODO add your handling code here:
        int row = tbDichVU.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần sửa!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa không?", "message", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                Service s = this.getDVUpdate();
                for (Service service : serviceService.getAll()) {
                    if (service.getCode().equals(txtMaDV.getText())) {
                        this.serviceViewModelService.update(service.getCode(), s);
                    }
                }

                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadDichVu();
            }
        }
    }//GEN-LAST:event_btnSuaDVActionPerformed

    private void btnXoaDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaDVActionPerformed
        // TODO add your handling code here:
        int row = tbDichVU.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần xóa!");
        } else {
            String ma = txtMaDV.getText();
            serviceViewModelService.delete(ma);
            loadDichVu();
        }
    }//GEN-LAST:event_btnXoaDVActionPerformed

    private void tbDichVUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDichVUMouseClicked
        // TODO add your handling code here:
        int row = tbDichVU.getSelectedRow();
        txtMaDV.setText(tbDichVU.getValueAt(row, 1).toString());
        txtTenDV.setText(tbDichVU.getValueAt(row, 2).toString());
        txtGia.setText(tbDichVU.getValueAt(row, 3).toString());
        txtGhiChu.setText(tbDichVU.getValueAt(row, 4).toString());
    }//GEN-LAST:event_tbDichVUMouseClicked

    private void tbTTDichVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTTDichVuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbTTDichVuMouseClicked

    private void btnSua2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSua2ActionPerformed
        // TODO add your handling code here:
        int row = tblTTKhach.getSelectedRow();
        Client c = this.getListKH();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần sửa!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn muốn sửa không?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String ma = txtMaKHFormKh.getText();
            this.khachHangService.update(ma, c);
            loadKH();
        }
    }//GEN-LAST:event_btnSua2ActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        //         TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, khachHangService.delete(txtMaKHFormKh.getText()));
            loadKH();
            btnLammoiActionPerformed(evt);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnLammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLammoiActionPerformed
        // TODO add your handling code here:
        txtMaKHFormKh.setText("");
        txt_tenKH.setText("");
        txt_CCCD.setText("");
        txtDiaChiKh.setText("");
        grFormKh.clearSelection();
        txt_sdt.setText("");
        csDateKH.setDate(null);
        loadKH();
    }//GEN-LAST:event_btnLammoiActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String ma = txtTimkiemKh.getText().toString();
        StringHandling hand = new StringHandling();

        for (Client x : this.khachHangService.getList()) {
            if (hand.firstUpper(ma).trim().equals(x.getName().trim())) {

                loadDataSearchKH(hand.firstUpper(ma).trim());
                JOptionPane.showMessageDialog(this, "Tìm thấy!");
                tblTTKhach.setColumnSelectionInterval(0, tblTTKhach.getRowCount());
                break;
            }

        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblTTKhachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTTKhachMouseClicked
        // TODO add your handling code here:
        int row = tblTTKhach.getSelectedRow();
        txtMaKHFormKh.setText(tblTTKhach.getValueAt(row, 0).toString());
        txt_tenKH.setText(tblTTKhach.getValueAt(row, 1).toString());
        //        csDate.setDate(tblTTKhach.getValueAt(row, 2).toString());
        String sex = tblTTKhach.getValueAt(row, 3).toString();
        if (sex.equals("Nam")) {
            rdNamKH.setSelected(true);
        } else {
            rdNuKh.setSelected(true);
        }
        txt_CCCD.setText(tblTTKhach.getValueAt(row, 4).toString());
        txt_sdt.setText(tblTTKhach.getValueAt(row, 5).toString());
        txtDiaChiKh.setText(tblTTKhach.getValueAt(row, 6).toString());
        String date = tblTTKhach.getValueAt(row, 2).toString();
        if (tblTTKhach.getValueAt(row, 2).toString().indexOf("-") != -1) {
            date = (date.substring(8) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4));
        }
        try {
            csDateKH.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblTTKhachMouseClicked

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        txtMaDV.setText("");
        txtGhiChu.setText("");
        txtGia.setText("");
        txtTenDV.setText("");

        cbGgs.setSelectedIndex(0);
        loadDichVu();
        loadDataSProS();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnHuyDvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyDvActionPerformed
        txtSoPhongDV.setText("");
        cbDichVu.setSelectedIndex(0);
        txtMaDv.setText("");
        csNgaySd.setDate(null);
        txtGiamGiaDV.setText("");
        txtGiaDv.setText("");
    }//GEN-LAST:event_btnHuyDvActionPerformed

    private void chkSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSSActionPerformed

        jPnTang1.removeAll();
        jPnTang2.removeAll();
        jPnTang3.removeAll();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "1");
        }
    }//GEN-LAST:event_chkSSActionPerformed

    private void chkTcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTcActionPerformed
        jPnTang1.removeAll();
        jPnTang2.removeAll();
        jPnTang3.removeAll();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "0");
        }
    }//GEN-LAST:event_chkTcActionPerformed

    private void chkCkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCkActionPerformed
        jPnTang1.removeAll();
        jPnTang2.removeAll();
        jPnTang3.removeAll();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "2");
        }

    }//GEN-LAST:event_chkCkActionPerformed

    private void chkCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCDActionPerformed
        jPnTang1.removeAll();
        jPnTang2.removeAll();
        jPnTang3.removeAll();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "3");
        }

    }//GEN-LAST:event_chkCDActionPerformed

    private void chkDDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDDActionPerformed
        jPnTang1.removeAll();
        jPnTang2.removeAll();
        jPnTang3.removeAll();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "4");
        }

    }//GEN-LAST:event_chkDDActionPerformed

    private void chkSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSCActionPerformed
        jPnTang1.removeAll();
        jPnTang2.removeAll();
        jPnTang3.removeAll();
        if (!roomService.getAll().isEmpty()) {
            loadPanel("all", "5");
        }

    }//GEN-LAST:event_chkSCActionPerformed

    private void tb_qlnvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_qlnvMouseClicked
        int row = tb_qlnv.getSelectedRow();
        String date = tb_qlnv.getValueAt(row, 2).toString();
        date = date.substring(8) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
        txt_code.setText(tb_qlnv.getValueAt(row, 0).toString());
        txt_name.setText(tb_qlnv.getValueAt(row, 1).toString());
        try {
            csNgaySinhnv.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
        }
        String gt = this.tb_qlnv.getValueAt(row, 3).toString().trim();
        if (gt.equals("Nam")) {
            rd_nam.setSelected(true);
        } else {
            rd_nu.setSelected(true);
        }
        for (staffviewmodel object : nhanvienService.getAll()) {
            if (object.getCode().equals(tb_qlnv.getValueAt(row, 0).toString())) {
                if (object.getRule().equals("nhanvien")) {
                    rdNv.setSelected(true);
                } else {
                    rdQl.setSelected(true);
                }
            }
        }
        txt_address.setText(tb_qlnv.getValueAt(row, 4).toString());
        txt_idpre.setText(tb_qlnv.getValueAt(row, 5).toString());
        txt_phone.setText(tb_qlnv.getValueAt(row, 6).toString());
        popupNV.show(tb_qlnv, evt.getX(), evt.getY());
    }//GEN-LAST:event_tb_qlnvMouseClicked

    private void btn_them1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_them1ActionPerformed
        Staff s = this.getData();
        String string = nhanvienService.add(s);
//        JOptionPane.showMessageDialog(this, string);
        if (string.equals("Thêm thành công")) {
            JOptionPane.showMessageDialog(this, string + "\nTài khoản của nhân viên:\n Tên:" + s.getUser() + "\nMật khẩu:" + s.getPassWord());
        } else {
            JOptionPane.showMessageDialog(this, string);
        }
        showDataTable(nhanvienService.getAll());
        clearForm();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_them1ActionPerformed

    private void btn_sua2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sua2ActionPerformed
        for (staffviewmodel nv : nhanvienService.getAll()) {
            if (nv.getCode().equals(txt_code.getText())) {
                JOptionPane.showMessageDialog(this, nhanvienService.update(nv.getId(), getData()));
                showDataTable(nhanvienService.getAll());
            }
        }
        showDataTable(nhanvienService.getAll());
        clearForm();
    }//GEN-LAST:event_btn_sua2ActionPerformed

    private void btn_xoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoa1ActionPerformed
        for (staffviewmodel nv : nhanvienService.getAll()) {
            if (nv.getCode().equals(txt_code.getText())) {
                JOptionPane.showMessageDialog(this, nhanvienService.delete(nv.getId()));
                showDataTable(nhanvienService.getAll());
                clearForm();
            }

        }

    }//GEN-LAST:event_btn_xoa1ActionPerformed
    public void loadTableSearchNV() {
        DefaultTableModel dlm = (DefaultTableModel) tb_qlnv.getModel();
        dlm.setRowCount(0);
        Staff r = this.nhanvienService.tim(txt_timkiem.getText().toString());
        Object[] rowData = {
            r.getCode(), r.getName(), r.getDateOfBirth(), r.getSex(), r.getAddress(), r.getIdPersonCard(), r.getPhone()
        };
        dlm.addRow(rowData);
    }
    private void btn_timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timkiemActionPerformed
        // TODO add your handling code here:
        int check = 0;
        if (txt_timkiem.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mời nhập đối tượng cần tìm");
        } else {
            for (staffviewmodel r : this.nhanvienService.getAll()) {
                if (txt_timkiem.getText().toUpperCase().equals(r.getCode())) {
                    this.loadTableSearchNV();
                    JOptionPane.showMessageDialog(this, "Tìm thành công");
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                JOptionPane.showMessageDialog(this, "Không có đối tượng cần tìm");
            }
        }
    }//GEN-LAST:event_btn_timkiemActionPerformed

    private void btn_loatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loatActionPerformed
        // TODO add your handling code here:
        showDataTable(nhanvienService.getAll());
        clearForm();
    }//GEN-LAST:event_btn_loatActionPerformed

    private void rd_nuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_nuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rd_nuActionPerformed

    private void tb_roomItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_roomItemMouseClicked
        // TODO add your handling code here:
        int row = tb_roomItem.getSelectedRow();
        cbmp.setSelectedItem(tb_roomItem.getValueAt(row, 1));
        cbmtb.setSelectedItem(tb_roomItem.getValueAt(row, 2));
        txt_statusRI.setText((String) tb_roomItem.getValueAt(row, 3));
        txt_amount.setText((String) tb_roomItem.getValueAt(row, 4).toString());
    }//GEN-LAST:event_tb_roomItemMouseClicked

    private void btn_them2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_them2ActionPerformed
        // TODO add your handling code here:
        RoomItemVMD r = this.getForm();
        ir.insert(r);
        loadDataRoomItem();
        clearF();
    }//GEN-LAST:event_btn_them2ActionPerformed

    private void btn_sua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sua1ActionPerformed
        //         TODO add your handling code here:
        RoomItemVMD r = this.getForm();
        System.out.println(r.getRoomID());
        int row = tb_roomItem.getSelectedRow();
        ir.update(r.getRoomID(), r);
        loadDataRoomItem();
        clearF();
    }//GEN-LAST:event_btn_sua1ActionPerformed

    private void btn_xoa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoa2ActionPerformed
        //         TODO add your handling code here:
        int row = tb_roomItem.getSelectedRow();
        //        String ma = tb_roomItem.getValueAt(row, 1).toString();
        ir.delete(getIDRI());
        loadDataRoomItem();
        clearF();
    }//GEN-LAST:event_btn_xoa2ActionPerformed

    private void cbmtbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmtbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbmtbActionPerformed

    private void cbmpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbmpItemStateChanged
        DefaultTableModel dtm;
        dtm = (DefaultTableModel) tb_roomItem.getModel();
        dtm.setRowCount(0);

        for (RoomItemVMD r : this.ir.getListRI()) {

            if (r.getNumberR().equals(cbmp.getSelectedItem().toString())) {
                int i = 1;
                dtm.addRow(new Object[]{i,
                    r.getNumberR(), r.getNameI(), r.getStatusRI(), r.getAmountRI()
                });
                i++;

            } else {
                dtm.setRowCount(0);
            }
        }
    }//GEN-LAST:event_cbmpItemStateChanged

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        Item i = this.getFormItem();
        is.insert(i);
        loadData();
        clearF();
        loadDataCBItem();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        int row = tb_item.getSelectedRow();
        String id = tb_item.getValueAt(row, 0).toString();
        is.delete(id);
        loadData();
        clearF();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
        int bien = 0;
        for (Item i : is.getListI()) {
            if (txt_tk.getText().equals(i.getCode())) {
                loadSearch();
                JOptionPane.showMessageDialog(this, "Kết quả");
                bien = 1;
                break;
            }
        }
        if (bien == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy");
        }
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // TODO add your handling code here:
        clearF();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_sua3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sua3ActionPerformed
        // TODO add your handling code here:
        Item i = this.getFormItem();
        int row = tb_item.getSelectedRow();
        String id = tb_item.getValueAt(row, 0).toString();
        is.update(i, id);
        loadData();
        clearF();
    }//GEN-LAST:event_btn_sua3ActionPerformed

    private void tb_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_itemMouseClicked
        // TODO add your handling code here:
        int row = tb_item.getSelectedRow();
        txt_code.setText((String) tb_item.getValueAt(row, 0));
        txt_name.setText((String) tb_item.getValueAt(row, 1));
    }//GEN-LAST:event_tb_itemMouseClicked

    private void menuChiTietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuChiTietActionPerformed
        new ChiTietHoaDon(code).setVisible(true);
    }//GEN-LAST:event_menuChiTietActionPerformed

    private void tbDsPhongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDsPhongMouseClicked

        txtSoPhong.setText(tbDsPhong.getValueAt(tbDsPhong.getSelectedRow(), 1).toString());
        PromotionRService promotionRService = new PromotionRService();

        Room room = roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0);
        String ngay = String.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        PromotionR pr = promotionRService.searchPromotionR(room.getIdPromotion(), ngay);
        if (pr != null) {
            txtGiaGiam.setText(pr.getValue());
        } else {
            txtGiaGiam.setText("0");
        }
        if (room.getStatus().equals("1")) {
            fillRoom(room);
        }
        ViewModelItemService viewItemService = new ViewModelItemService();
        if (viewItemService.getAll(room.getId()) != null) {
            DefaultTableModel defaultTableModel = (DefaultTableModel) tbNoiThat.getModel();
            defaultTableModel.setRowCount(0);
            for (ViewModelItem item : viewItemService.getAll(room.getId())) {
                defaultTableModel.addRow(new Object[]{item.getName(), item.getStatus(), item.getAmount()});
            }
        }
    }//GEN-LAST:event_tbDsPhongMouseClicked

    private void menuAcountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAcountActionPerformed
        if (auth.rule.equals("admin")) {
            Staff nv = nhanvienService.tim(txt_code.getText());
            JOptionPane.showMessageDialog(this, "Tài khoản: " + nv.getUser() + "\n" + "Pass: " + nv.getPassWord());
        } else {
            JOptionPane.showMessageDialog(this, "admin mới có quyền xem!");
        }
    }//GEN-LAST:event_menuAcountActionPerformed

    private void txtTenKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenKhachHangActionPerformed
        this.txtCCCD.requestFocus();
    }//GEN-LAST:event_txtTenKhachHangActionPerformed

    private void txtCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCCDActionPerformed
        this.txtSDT.requestFocus();
    }//GEN-LAST:event_txtCCCDActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        this.txtDiaChi.requestFocus();
    }//GEN-LAST:event_txtSDTActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new ViewTrangChu().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDoiPhong;
    private javax.swing.JButton btnDx;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnHuyDv;
    private javax.swing.JButton btnHuyPhong;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLammoi;
    private javax.swing.JButton btnQuetMa;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetHd;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSua2;
    private javax.swing.JButton btnSuaDV;
    private javax.swing.JButton btnTKHd;
    private javax.swing.JButton btnThemDv;
    private javax.swing.JButton btnThemdv;
    private javax.swing.JButton btnThuePhong;
    private javax.swing.JButton btnTraPhongTP;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaDV;
    private javax.swing.JButton btn_GiamGia;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_loat;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_sua1;
    private javax.swing.JButton btn_sua2;
    private javax.swing.JButton btn_sua3;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_them1;
    private javax.swing.JButton btn_them2;
    private javax.swing.JButton btn_timkiem;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JButton btn_xoa1;
    private javax.swing.JButton btn_xoa2;
    private javax.swing.JButton btnreset;
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnthem;
    private javax.swing.JButton btnthemnhanh;
    private javax.swing.JButton btntk;
    private javax.swing.JButton btnxoa;
    private javax.swing.JComboBox<String> cbDichVu;
    private javax.swing.JComboBox<String> cbGgs;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JComboBox<String> cbVt;
    private javax.swing.JComboBox<String> cbbTrangthaiTP;
    private javax.swing.JComboBox<String> cblp;
    private javax.swing.JComboBox<String> cbmgg;
    private javax.swing.JComboBox<String> cbmp;
    private javax.swing.JComboBox<String> cbmtb;
    private javax.swing.JComboBox<String> cbstt;
    private javax.swing.JCheckBox chkALL;
    private javax.swing.JCheckBox chkCD;
    private javax.swing.JCheckBox chkCTT;
    private javax.swing.JCheckBox chkCk;
    private javax.swing.JCheckBox chkDD;
    private javax.swing.JCheckBox chkSC;
    private javax.swing.JCheckBox chkSS;
    private javax.swing.JCheckBox chkTT;
    private javax.swing.JCheckBox chkTc;
    private com.toedter.calendar.JDateChooser csCheckinTP;
    private com.toedter.calendar.JDateChooser csCheckoutTP;
    private com.toedter.calendar.JDateChooser csDateKH;
    private com.toedter.calendar.JDateChooser csLichTraPhong;
    private com.toedter.calendar.JDateChooser csNgaySd;
    private com.toedter.calendar.JDateChooser csNgaySinh;
    private com.toedter.calendar.JDateChooser csNgaySinhnv;
    private com.toedter.calendar.JDateChooser csTraPhong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLbAll;
    private javax.swing.JLabel jLbCD;
    private javax.swing.JLabel jLbCK;
    private javax.swing.JLabel jLbCheckDc;
    private javax.swing.JLabel jLbCheckGt;
    private javax.swing.JLabel jLbCheckTen;
    private javax.swing.JLabel jLbCheckcc;
    private javax.swing.JLabel jLbChecksdt;
    private javax.swing.JLabel jLbDD;
    private javax.swing.JLabel jLbSC;
    private javax.swing.JLabel jLbSS;
    private javax.swing.JMenuItem jMenuCD;
    private javax.swing.JMenuItem jMenuCheckout;
    private javax.swing.JMenuItem jMenuDD;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuSC;
    private javax.swing.JMenuItem jMenuSS;
    private javax.swing.JMenuItem jMenuThuePhong;
    private javax.swing.JMenu jMenuTrangThai;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPnTang1;
    private javax.swing.JPanel jPnTang2;
    private javax.swing.JPanel jPnTang3;
    private javax.swing.JPanel jPnThemDv;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabTrangChu;
    private javax.swing.JLabel jab;
    private javax.swing.JLabel jab1;
    private javax.swing.JLabel lbThoiGian;
    private javax.swing.JLabel lbma;
    private javax.swing.JMenuItem menuAcount;
    private javax.swing.JMenuItem menuChiTiet;
    private javax.swing.JMenuItem menuDichVu;
    private javax.swing.JPanel pnInforKh;
    private javax.swing.JPopupMenu popupHoaDon;
    private javax.swing.JPopupMenu popupNV;
    private javax.swing.JPopupMenu popupPhong;
    private javax.swing.JRadioButton rdNam;
    private javax.swing.JRadioButton rdNamKH;
    private javax.swing.JRadioButton rdNu;
    private javax.swing.JRadioButton rdNuKh;
    private javax.swing.JRadioButton rdNv;
    private javax.swing.JRadioButton rdQl;
    private javax.swing.JRadioButton rd_nam;
    private javax.swing.JRadioButton rd_nu;
    private javax.swing.JTable tbDichVU;
    private javax.swing.JTable tbDsPhong;
    private javax.swing.JTable tbNoiThat;
    private javax.swing.JTable tbTTDichVu;
    private javax.swing.JTable tb_item;
    private javax.swing.JTable tb_qlnv;
    private javax.swing.JTable tb_roomItem;
    private javax.swing.JTable tblBill;
    private javax.swing.JTable tblDVTP;
    private javax.swing.JTable tblTTKhach;
    private javax.swing.JTable tbqlp;
    private javax.swing.JTextField txtAreaRoom;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtCccdTP;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextArea txtDiaChiKh;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtGiaDv;
    private javax.swing.JTextField txtGiaGiam;
    private javax.swing.JTextField txtGiaPhong;
    private javax.swing.JTextField txtGiamGiaDV;
    private javax.swing.JTextField txtGiamgiaTP;
    private javax.swing.JTextField txtGiaphongTP;
    private javax.swing.JTextField txtKhachhangTP;
    private javax.swing.JTextField txtKindOfRoom;
    private javax.swing.JTextField txtLocationRoom;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtMaDV;
    private javax.swing.JTextField txtMaDv;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKHFormKh;
    private javax.swing.JTextField txtMaPhong;
    private javax.swing.JTextField txtMahdTP;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNgayThue;
    private javax.swing.JTextField txtNgayTra;
    private javax.swing.JTextField txtNvTao;
    private javax.swing.JTextArea txtPhuthuTP;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSoPhong;
    private javax.swing.JTextField txtSoPhongDV;
    private javax.swing.JTextField txtSophongTP;
    private javax.swing.JTextField txtTenDV;
    private javax.swing.JLabel txtTenKS;
    private javax.swing.JTextField txtTenKh;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtThanhtienTP;
    private javax.swing.JTextField txtTien;
    private javax.swing.JTextField txtTimkiemKh;
    private javax.swing.JTextField txtTkHd;
    private javax.swing.JTextField txt_CCCD;
    private javax.swing.JTextArea txt_address;
    private javax.swing.JTextField txt_amount;
    private javax.swing.JTextField txt_code;
    private javax.swing.JTextField txt_code1;
    private javax.swing.JTextField txt_idpre;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_name1;
    private javax.swing.JTextField txt_phone;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_statusRI;
    private javax.swing.JTextField txt_tenKH;
    private javax.swing.JTextField txt_timkiem;
    private javax.swing.JTextField txt_tk;
    private javax.swing.JTextField txtdt;
    private javax.swing.JTextField txtg;
    private javax.swing.JTextField txtsp;
    private javax.swing.JTextField txttk;
    // End of variables declaration//GEN-END:variables

}
