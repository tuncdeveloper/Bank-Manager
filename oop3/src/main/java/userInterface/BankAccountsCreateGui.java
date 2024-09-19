package userInterface;

import database.DbPostgre;
import domain.BankAccount;
import domain.BankAccountFactory;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import domain.Customer; // Customer sınıfını içe aktarma

public class BankAccountsCreateGui extends JFrame implements InitCall {
    private JTextField ibanField;
    private JTextField accountNoField;
    private JTextField balanceField;
    private JButton createAccountButton;
    private JButton exitButton; // Çıkış butonu
    private JList<BankAccount> bankAccountJList;

    private Customer customer; // Customer nesnesi
    BankAccount bankAccount = new BankAccount();
    DbPostgre dbPostgre = new DbPostgre();
    List<BankAccount> bankAccountList = new ArrayList<>();


    // Hesap oluşturma işlemi

    public BankAccountsCreateGui(Customer customer) {
        this.customer = customer; // Customer nesnesini alıyoruz
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Banka Hesap Yönetimi");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 satırlı grid layout

        // Müşteri bilgilerini göstermek için etiketler
        JLabel customerNameLabel = new JLabel("Ad Soyad:");
        JLabel customerNameValue = new JLabel(customer.getName() + " " + customer.getSurname());

        JLabel customerIdLabel = new JLabel("Müşteri ID:");
        JLabel customerIdValue = new JLabel(String.valueOf(customer.getId()));

        // IBAN, Hesap No, ve Bakiye alanları
        JLabel ibanLabel = new JLabel("IBAN:");
        ibanField = new JTextField();
        ibanField.setEditable(false); // Düzenlenemez yapıldı

        JLabel accountNoLabel = new JLabel("Hesap No:");
        accountNoField = new JTextField();
        accountNoField.setEditable(false); // Düzenlenemez yapıldı

        JLabel balanceLabel = new JLabel("Bakiye:");
        balanceField = new JTextField();
        balanceField.setEditable(false); // Düzenlenemez yapıldı

        createAccountButton = new JButton("Hesap Oluştur");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCreateAccountButton();
            }
        });

        // Çıkış Butonu
        exitButton = new JButton("Çıkış");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Pencereyi kapatır
                BankAccountShowGui bankAccountShowGui = new BankAccountShowGui(customer);
                bankAccountShowGui.initWindow();
            }
        });

        // Bileşenleri panele ekleme
        panel.add(customerNameLabel);
        panel.add(customerNameValue);
        panel.add(customerIdLabel);
        panel.add(customerIdValue);
        panel.add(ibanLabel);
        panel.add(ibanField);
        panel.add(accountNoLabel);
        panel.add(accountNoField);
        panel.add(balanceLabel);
        panel.add(balanceField);
        panel.add(createAccountButton);
        panel.add(exitButton); // Çıkış butonunu ekleme

        return panel;
    }

    public void setCreateAccountButton() {
        BankAccountFactory bankAccountFactory = new BankAccountFactory();

        String iban = bankAccountFactory.generateRandomIban();
        String accountNo = bankAccountFactory.generateRandomAccountNumber();
        double balance = bankAccountFactory.generateRandomBalance();

        bankAccount.setIBAN(iban);
        bankAccount.setAccountNumber(accountNo);
        bankAccount.setBankBalance(balance);
        dbPostgre.newBankAccount(bankAccount, customer.getId());
        bankAccountList.add(bankAccount);

        customer.setBankAccounts(bankAccountList);

        ibanField.setText(iban);
        accountNoField.setText(accountNo);
        balanceField.setText(String.valueOf(balance));


        // Hesap oluşturulduğu mesajını göster
        JOptionPane.showMessageDialog(this, "Hesap başarıyla oluşturuldu!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);


    }
}
