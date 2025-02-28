package gui.adminGui;

import model.Customer;
import model.CustomerTypeEnum;
import repository.BankAccountDb;
import repository.CustomerDb;
import repository.IBankAccountDb;
import repository.ICustomerDb;
import test.InitCall;
import gui.jointAdminCustomer.BankAccountShowGui;
import gui.customerGui.CustomerRegisterGui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminMenuGui extends JFrame implements InitCall {

    ICustomerDb customerDb = new CustomerDb();
    IBankAccountDb bankAccountDb = new BankAccountDb();
    JList<Customer> customerJList;
    DefaultListModel<Customer> customerListModel = new DefaultListModel<>();

    public AdminMenuGui() {
        // Set FlatLaf Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initWindow() {
        JPanel jPanel = initPanel();
        add(jPanel);
        setTitle("Admin");
        setSize(600, 500);
        setLocationRelativeTo(null); // Ekranın ortasında açılır
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Çıkışta pencereyi kapatır
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Button Panel with FlatLaf enhancements
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton exitButton = new JButton("Çıkış");
        JButton updateButton = new JButton("Kişi Güncelle");
        JButton deleteButton = new JButton("Kişi Sil");
        JButton addButton = new JButton("Kişi Ekle");
        JButton viewAccountsButton = new JButton("Hesapları Görüntüle");

        // Add buttons to buttonPanel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewAccountsButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Show customer list and handle button actions
        showCustomerList(panel);

        // Action Listeners
        addButton.addActionListener(e -> addShortCommit());
        deleteButton.addActionListener(e -> deleteCommit(customerListModel));
        updateButton.addActionListener(e -> updateCommit());
        exitButton.addActionListener(e -> dispose());
        viewAccountsButton.addActionListener(e -> viewAccountsCommit());

        return panel;
    }

    // Kişinin TC'sini sorgulayan ve doğrulandığında hesapları gösteren yöntem
    public void viewAccountsCommit() {
        try {
            String input = JOptionPane.showInputDialog("Lütfen kullanıcının TC kimlik numarasını giriniz:");

            if (input != null && !input.trim().isEmpty()) {

                Customer customer = customerDb.findCustomerByTc(input);

                if (customer != null) {
                    JOptionPane.showMessageDialog(null, customer.getName() + " kullanıcısı bulundu.");
                    BankAccountShowGui bankAccountShowGui = new BankAccountShowGui(customer);
                    bankAccountShowGui.initWindow();
                } else {
                    JOptionPane.showMessageDialog(null, "Girilen TC kimlik numarasına ait kullanıcı bulunamadı.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir TC kimlik numarası giriniz.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir TC kimlik numarası giriniz.");
        }
    }

    public void addShortCommit() {
        dispose();
        CustomerRegisterGui customerRegisterGui = new CustomerRegisterGui();
        customerRegisterGui.initWindow();
    }

    public void deleteCommit(DefaultListModel<Customer> listModel) {
        Customer selectedCustomer = customerJList.getSelectedValue();

        if (selectedCustomer != null) {
            bankAccountDb.deleteBankAccountsForCustomer(selectedCustomer.getCustomerId());
            customerDb.deleteCustomer(selectedCustomer.getCustomerId());
            listModel.removeElement(selectedCustomer); // Seçili müşteriyi listeden kaldır
            JOptionPane.showMessageDialog(null, selectedCustomer.getName() + " Kullanıcı başarılı bir şekilde silindi");
        } else {
            JOptionPane.showMessageDialog(null, "Silmek için birini seçiniz!!!");
        }
    }

    public void updateCommit() {
        Customer selectedCustomer = customerJList.getSelectedValue();

        if (selectedCustomer != null) {
            EditCustomerGui editCustomerGui = new EditCustomerGui(selectedCustomer);
            editCustomerGui.initWindow();

        } else {
            JOptionPane.showMessageDialog(null, "Düzenlemek için bir müşteri seçiniz!!!");
        }
    }

    public void showCustomerList(JPanel panel) {
        // Listeyi göstermek için bir alan
        customerJList = new JList<>(customerListModel);
        customerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerJList.setFixedCellHeight(50);  // Her bir hücrenin yüksekliği
        customerJList.setFont(new Font("Arial", Font.PLAIN, 14));  // Font ayarları

        // Daha şık bir hücre render'ı
        customerJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Customer) {
                    Customer customer = (Customer) value;

                    // Müşteri bilgilerini belirli bir formatta göstermek
                    String customerInfo = "<html><div style='padding:10px; border-radius:5px; "
                            + (isSelected ? "background-color:#80C5FF; color:#FFFFFF;" : "") // Seçili durum için renk
                            + "'>Müşteri: " + customer.getName() + " " + customer.getSurname() +
                            "<br>T.C. : " + customer.getTc() +
                            "<br>Telefon: " + customer.getPhoneNumber() + "</div></html>";

                    label.setText(customerInfo);
                    label.setBackground(isSelected ? new Color(16, 23, 165) : Color.WHITE);
                    label.setOpaque(true);
                }

                return label;
            }
        });

        // Veritabanından müşteri listesini çekme ve sadece USER olanları ekleme
        List<Customer> customers = customerDb.customerShowList();
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer.getCustomerTypeEnum() == CustomerTypeEnum.USER) {
                    customerListModel.addElement(customer); // Sadece USER olanları listeye ekle
                }
            }
        }

        // ScrollPane özellikleri
        JScrollPane scrollPane1 = new JScrollPane(customerJList);
        scrollPane1.setBorder(BorderFactory.createEmptyBorder());  // Border'ı kaldır
        scrollPane1.getVerticalScrollBar().setUnitIncrement(16);  // Scroll hızını ayarla
        panel.add(scrollPane1, BorderLayout.CENTER);
    }


}
