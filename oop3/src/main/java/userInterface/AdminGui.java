package userInterface;

import database.DbPostgre;
import domain.BankAccount;
import domain.Customer;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminGui extends JFrame implements InitCall {

    DbPostgre dbPostgre = new DbPostgre();
    JList<Customer> customerJList;
    DefaultListModel<Customer> customerListModel = new DefaultListModel<>();

    public AdminGui() {
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

        // Alt panelde butonlar için yer açalım
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Çıkış Butonu
        JButton exitButton = new JButton("Çıkış");
        buttonPanel.add(exitButton);

        // Güncelle Butonu
        JButton updateButton = new JButton("Kişi Güncelle");
        buttonPanel.add(updateButton);

        // Silme Butonu
        JButton deleteButton = new JButton("Kişi Sil");
        buttonPanel.add(deleteButton);

        // Ekle Butonu
        JButton addButton = new JButton("Kişi Ekle");
        buttonPanel.add(addButton);

        // Hesapları Görüntüle Butonu
        JButton viewAccountsButton = new JButton("Hesapları Görüntüle");
        buttonPanel.add(viewAccountsButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);


        showCustomerList(panel);
        // Ekleme ve silme butonları için olaylar
        addButton.addActionListener(e -> addShortCommit());

        deleteButton.addActionListener(e -> deleteCommit(customerListModel));

        updateButton.addActionListener(e -> updateCommit());

        exitButton.addActionListener(e -> dispose());

        // Hesapları Görüntüle butonunun işlevi
        viewAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAccountsCommit();
            }
        });

        return panel;
    }

    // Kişinin TC'sini sorgulayan ve doğrulandığında hesapları gösteren yöntem
    public void viewAccountsCommit() {
        try {
            String input = JOptionPane.showInputDialog("Lütfen kullanıcının TC kimlik numarasını giriniz:");

            if (input != null && !input.trim().isEmpty()) {
                int tcInput = Integer.parseInt(input);

                Customer customer = dbPostgre.findCustomer(tcInput);

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
        RegisterGui registerGui = new RegisterGui();
        registerGui.initWindow();
        dispose();
        AdminGui adminGui = new AdminGui();
        adminGui.initWindow();
    }

    public void deleteCommit(DefaultListModel<Customer> listModel) {
        Customer selectedCustomer = customerJList.getSelectedValue();

        if (selectedCustomer != null) {
            dbPostgre.deleteBankAccountsForCustomer(selectedCustomer);
            dbPostgre.deleteCustomer(selectedCustomer);
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
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Düzenlemek için bir müşteri seçiniz!!!");
        }
    }


    public void showCustomerList(JPanel panel){
        // Listeyi göstermek için bir alan
        customerJList = new JList<>(customerListModel);
        customerJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        List<Customer> customers = dbPostgre.customerShowList();
        if (customers != null) {
            for (Customer customer : customers) {
                customerListModel.addElement(customer); // Customer sınıfınızda uygun bir toString() metodu olmalı
            }
        }

        JScrollPane scrollPane1 = new JScrollPane(customerJList);
        panel.add(scrollPane1, BorderLayout.CENTER);
    }
}
