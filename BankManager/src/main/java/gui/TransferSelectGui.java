package gui;

import model.BankAccount;
import model.Customer;
import repository.BankAccountDb;
import repository.CustomerDb;
import repository.IBankAccountDb;
import repository.ICustomerDb;
import test.InitCall;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class TransferSelectGui extends JFrame implements InitCall {

    private Customer loggedCustomer;
    private BankAccount loggedBankAccount;
    private IBankAccountDb bankAccountDb = new BankAccountDb();
    private ICustomerDb customerDb = new CustomerDb();
    private JLabel customerBalanceLabel, customerNameLabel, customerPhoneLabel, customerIBANlabel, customerAccountLabel, ibanLabel, enterAmountLabel;
    private JButton findButton, cancelButton, sendMoneyButton;
    private JTextField ibanField, accountNumberField, nameField, surnameField, balanceField, enterAmountField;
    private boolean isFindButtonClicked = false;

    TransferSelectGui(Customer loggedCustomer, BankAccount loggedBankAccount) {
        this.loggedCustomer = loggedCustomer;
        this.loggedBankAccount = loggedBankAccount;
    }

    @Override
    public void initWindow() {
        try {
            // FlatLaf temasını uyguluyoruz
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel panel = initPanel();
        add(panel);
        setTitle("Para Gönder");
        setSize(600, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Üst panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(5, 1));
        topPanel.setBackground(new Color(245, 245, 245)); // Arka plan rengi

        customerNameLabel = new JLabel("Müşteri: " + loggedCustomer.getName() + " " + loggedCustomer.getSurname());
        customerPhoneLabel = new JLabel("Telefon: " + loggedCustomer.getPhoneNumber());
        customerIBANlabel = new JLabel("IBAN: " + loggedBankAccount.getIBAN());
        customerAccountLabel = new JLabel("Hesap No: " + loggedBankAccount.getAccountNumber());
        customerBalanceLabel = new JLabel("Bakiye: " + loggedBankAccount.getBankBalance());

        customerBalanceLabel.setForeground(new Color(255, 59, 48)); // Kırmızı renk
        customerBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Daha temiz bir görünüm için panel ekleme
        topPanel.add(customerNameLabel);
        topPanel.add(customerPhoneLabel);
        topPanel.add(customerIBANlabel);
        topPanel.add(customerAccountLabel);
        topPanel.add(customerBalanceLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        // IBAN girişi ve butonlar orta panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBackground(new Color(255, 255, 255));

        ibanLabel = new JLabel("IBAN Giriniz:");
        ibanLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ibanField = new JTextField(20);
        findButton = new JButton("Bul");
        cancelButton = new JButton("İptal");

        // FlatLaf düğme stilleri
        findButton.setBackground(new Color(28, 168, 255));
        findButton.setForeground(Color.WHITE);
        findButton.setFocusPainted(false);

        cancelButton.setBackground(new Color(255, 59, 48));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        searchPanel.add(ibanLabel);
        searchPanel.add(ibanField);
        searchPanel.add(findButton);
        searchPanel.add(cancelButton);

        panel.add(searchPanel, BorderLayout.CENTER);

        // Alt panel (işlem butonları)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(6, 2));
        bottomPanel.setBackground(new Color(245, 245, 245));

        bottomPanel.add(new JLabel("Ad:"));
        nameField = new JTextField();
        nameField.setEditable(false);
        bottomPanel.add(nameField);

        bottomPanel.add(new JLabel("Soyad:"));
        surnameField = new JTextField();
        surnameField.setEditable(false);
        bottomPanel.add(surnameField);

        bottomPanel.add(new JLabel("Hesap Numarası:"));
        accountNumberField = new JTextField();
        accountNumberField.setEditable(false);
        bottomPanel.add(accountNumberField);

        enterAmountLabel = new JLabel("Gönderilecek Tutar:");
        enterAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        enterAmountField = new JTextField();
        bottomPanel.add(enterAmountLabel);
        bottomPanel.add(enterAmountField);

        sendMoneyButton = new JButton("Para Gönder");
        sendMoneyButton.setBackground(new Color(28, 168, 255));
        sendMoneyButton.setForeground(Color.WHITE);
        sendMoneyButton.setFocusPainted(false);

        bottomPanel.add(new JLabel()); // Boş bir etiket
        bottomPanel.add(sendMoneyButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // "İptal" butonu işlevi
        cancelButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "İşlem iptal edildi.", "Bilgi", JOptionPane.WARNING_MESSAGE);
            dispose();
        });

        // "Bul" butonu işlevi
        findButton.addActionListener(e -> {
            isFindButtonClicked = true;
            String iban = ibanField.getText();

            if (!iban.isEmpty()) {
                Customer customer = customerDb.getCustomerForBankAccountIBAN(iban);
                BankAccount account = bankAccountDb.findBankAccount(iban);
                if (customer != null && account != null) {
                    nameField.setText(customer.getName());
                    surnameField.setText(customer.getSurname());
                    accountNumberField.setText(account.getAccountNumber());
                } else {
                    if (customer == null) {
                        JOptionPane.showMessageDialog(null, "IBAN ile müşteri bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                    if (account == null) {
                        JOptionPane.showMessageDialog(null, "IBAN ile hesap bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen bir IBAN giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });

        sendMoneyButton.addActionListener(e -> {
            // Para gönderim işlemleri
            BankAccount myBankAccount = bankAccountDb.findBankAccount(loggedBankAccount.getIBAN());
            BankAccount otherBankAccount = bankAccountDb.findBankAccount(ibanField.getText());
            Customer selectedCustomer = customerDb.getCustomerForBankAccountIBAN(ibanField.getText());

            if (!isFindButtonClicked) {
                JOptionPane.showMessageDialog(null, "Lütfen önce 'Bul' butonuna basarak bilgileri doğrulayın.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String enteredAmountStr = enterAmountField.getText();
            Double enteredAmount = null;

            try {
                enteredAmount = Double.valueOf(enteredAmountStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (enteredAmount != null) {
                if (loggedBankAccount.getBankBalance() < enteredAmount) {
                    JOptionPane.showMessageDialog(null, "Bakiye yetersiz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                } else {
                    myBankAccount.setBankBalance(loggedBankAccount.getBankBalance() - enteredAmount);
                    otherBankAccount.setBankBalance(otherBankAccount.getBankBalance() + enteredAmount);

                    bankAccountDb.upgradeBankAccount(myBankAccount);
                    bankAccountDb.upgradeBankAccount(otherBankAccount);

                    customerBalanceLabel.setText("Bakiye: " + myBankAccount.getBankBalance());

                    JOptionPane.showMessageDialog(null,
                            selectedCustomer.getName() + " " + selectedCustomer.getSurname() + " kullanıcı\n" +
                                    otherBankAccount.getAccountNumber() + " hesabına para gönderme işlemi başarılı!\n" +
                                    "Gönderilen tutar: " + enteredAmount +
                                    "\nKalan bakiye: " + myBankAccount.getBankBalance());
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen bir tutar giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }
}
