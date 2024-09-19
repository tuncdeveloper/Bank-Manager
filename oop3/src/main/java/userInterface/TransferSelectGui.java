package userInterface;

import database.DbPostgre;
import domain.BankAccount;
import domain.Customer;
import test.InitCall;

import javax.swing.*;
import java.awt.*;

public class TransferSelectGui extends JFrame implements InitCall {

    private Customer loggedCustomer;
    private BankAccount loggedBankAccount;
    private DbPostgre dbPostgre = new DbPostgre();
    private JLabel customerBalanceLabel, customerNameLabel, customerPhoneLabel,customerIBANlabel,customerAccountLabel ,ibanLabel, enterAmountLabel;
    private JButton findButton, cancelButton, sendMoneyButton;
    private JTextField ibanField, accountNumberField, nameField, surnameField, ballanceField, enterAmountField;
    private boolean isFindButtonClicked = false;

    TransferSelectGui(Customer loggedCustomer, BankAccount loggedBankAccount) {
        this.loggedCustomer = loggedCustomer;
        this.loggedBankAccount = loggedBankAccount;
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Kişi Seç");
        setSize(600, 400);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Müşteri ve bakiye bilgileri üst panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 1));

        customerNameLabel = new JLabel("Müşteri: " + loggedCustomer.getName() + " " + loggedCustomer.getSurname());
        customerPhoneLabel = new JLabel("Telefon: " + loggedCustomer.getPhoneNumber());
        customerIBANlabel = new JLabel("IBAN: "+loggedBankAccount.getIBAN());
        customerAccountLabel = new JLabel("Hesap No: "+loggedBankAccount.getAccountNumber());
        customerBalanceLabel = new JLabel("Bakiye: " + loggedBankAccount.getBankBalance());
        customerBalanceLabel.setForeground(Color.RED); // Kırmızı renk
        customerBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Kalın ve belirgin font


        topPanel.add(customerNameLabel);
        topPanel.add(customerIBANlabel);
        topPanel.add(customerPhoneLabel);
        topPanel.add(customerAccountLabel);
        topPanel.add(customerBalanceLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        // IBAN girişi ve bulma butonu orta panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        ibanLabel = new JLabel("IBAN Giriniz:");
        ibanField = new JTextField(20);
        findButton = new JButton("Bul");
        cancelButton = new JButton("İptal");

        searchPanel.add(ibanLabel);
        searchPanel.add(ibanField);
        searchPanel.add(findButton);
        searchPanel.add(cancelButton);

        panel.add(searchPanel, BorderLayout.CENTER);

        // Bilgiler ve işlem butonları alt panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(5, 2));

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

        // Bakiye Giriniz alanı
        enterAmountLabel = new JLabel("Bakiye Giriniz:");
        enterAmountField = new JTextField();
        bottomPanel.add(enterAmountLabel);
        bottomPanel.add(enterAmountField);

        // Para Gönder butonu
        sendMoneyButton = new JButton("Para Gönder");
        bottomPanel.add(new JLabel()); // Boş bir etiket, hizalama için
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
                Customer customer = dbPostgre.getCustomerForBankAccountIBAN(iban);
                BankAccount account = dbPostgre.findBankAccount(iban);
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
            // Banka hesaplarını bul
            BankAccount myBankAccount = dbPostgre.findBankAccount(loggedBankAccount.getIBAN());
            BankAccount otherBankAccount = dbPostgre.findBankAccount(ibanField.getText());
            Customer selectedCustomer = dbPostgre.getCustomerForBankAccountIBAN(ibanField.getText());

            // Eğer 'Bul' butonuna basılmadıysa uyarı mesajı göster
            if (!isFindButtonClicked) {
                JOptionPane.showMessageDialog(null, "Lütfen önce 'Bul' butonuna basarak bilgileri doğrulayın.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tutarın geçerli bir sayı olup olmadığını kontrol et
            String enteredAmountStr = enterAmountField.getText();
            Double enteredAmount = null;

            try {
                enteredAmount = Double.valueOf(enteredAmountStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Girilen tutar geçerli ise işleme devam et
            if (enteredAmount != null) {
                // Eğer yeterli bakiye yoksa
                if (loggedBankAccount.getBankBalance() < enteredAmount) {
                    JOptionPane.showMessageDialog(null, "Bakiye yetersiz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Para transfer işlemini gerçekleştir
                    myBankAccount.setBankBalance(loggedBankAccount.getBankBalance() - enteredAmount);
                    otherBankAccount.setBankBalance(otherBankAccount.getBankBalance() + enteredAmount);

                    dbPostgre.updateBankAccount(myBankAccount);
                    dbPostgre.updateBankAccount(otherBankAccount);

                    // Güncellenmiş bakiyeyi etiketlere yansıt
                    customerBalanceLabel.setText("Bakiye: " + myBankAccount.getBankBalance());

                    JOptionPane.showMessageDialog(null,
                            selectedCustomer.getName()+" "+selectedCustomer.getSurname()+" kullanıcı\n"+otherBankAccount.getAccountNumber()+" hesabına para gönderme işlemi başarılı!\nGönderilen tutar: " + enteredAmount +
                                    "\nKalan bakiye: " + myBankAccount.getBankBalance());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen bir tutar giriniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }
}

