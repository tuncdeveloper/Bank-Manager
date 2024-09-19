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

public class BankAccountShowGui extends JFrame implements InitCall {

    private Customer customerPerson;
    private DbPostgre dbPostgre = new DbPostgre();
    DefaultListModel<BankAccount> bankAccountListModel = new DefaultListModel<>();
    JList<BankAccount> bankAccountJList = new JList<>(bankAccountListModel);

    BankAccountShowGui(Customer customer) {

        this.customerPerson = customer;
    }

    @Override
    public void initWindow() {
        JPanel jPanel = initPanel();
        add(jPanel);
        setTitle(customerPerson.getName() + " Hesapları");
        setSize(700, 500);
        setLocationRelativeTo(null); // Ekranın ortasında açılır
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Çıkışta pencereyi kapatır
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Kullanıcı Bilgileri Paneli
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridLayout(4, 2)); // 4 satır 2 sütun

        userInfoPanel.add(new JLabel("Ad:"));
        userInfoPanel.add(new JLabel(customerPerson.getName()));
        userInfoPanel.add(new JLabel("Soyad:"));
        userInfoPanel.add(new JLabel(customerPerson.getSurname()));
        userInfoPanel.add(new JLabel("Telefon:"));
        userInfoPanel.add(new JLabel(customerPerson.getPhoneNumber()));
        // Diğer bilgiler eklenebilir

        panel.add(userInfoPanel, BorderLayout.NORTH);

        showBankAccountList(panel);


        JButton deleteButton = new JButton("Sil");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBankAccount();
            }
        });

        // Yeni Ekle butonu ekleme
        JButton addButton = new JButton("Ekle");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
               BankAccountsCreateGui bankAccountsCreateGui = new BankAccountsCreateGui(customerPerson);
               bankAccountsCreateGui.initWindow();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton); // Ekle butonu panele ekleniyor

        // Çıkış Butonu
        JButton exitButton = new JButton("Çıkış");
        exitButton.addActionListener(e -> {
            dispose(); // Pencereyi kapatır
        });
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    public void deleteBankAccount(){

        BankAccount selectedAccount = bankAccountJList.getSelectedValue();
        if (selectedAccount != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    BankAccountShowGui.this,
                    "Seçilen hesabı silmek istediğinizden emin misiniz?",
                    "Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dbPostgre.deleteBankAccount(selectedAccount); // Hesabı veritabanından sil
                bankAccountListModel.removeElement(selectedAccount); // Listeyi güncelle
                JOptionPane.showMessageDialog(
                        BankAccountShowGui.this,
                        "Hesap başarıyla silindi.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    BankAccountShowGui.this,
                    "Silmek için bir hesap seçmelisiniz.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    public void showBankAccountList(JPanel panel){
        JScrollPane scrollPane = new JScrollPane(bankAccountJList);

        // Kullanıcının banka hesaplarını çek
        List<BankAccount> bankAccounts = dbPostgre.getBankAccountsForCustomer(customerPerson.getId());
        if (bankAccounts != null) {
            for (BankAccount account : bankAccounts) {
                bankAccountListModel.addElement(account);
            }
        }

        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
