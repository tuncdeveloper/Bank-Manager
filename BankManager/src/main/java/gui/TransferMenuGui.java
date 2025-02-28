package gui;

import model.BankAccount;
import model.Customer;
import repository.BankAccountDb;

import repository.IBankAccountDb;
import test.InitCall;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TransferMenuGui extends JFrame implements InitCall {

    IBankAccountDb bankAccountDb = new BankAccountDb();

    private JList<BankAccount> bankAccountJList;
    private JButton selectButton, cancelButton;

    private Customer customerLogged;

    public TransferMenuGui(Customer customer) {
        this.customerLogged = customer;
    }

    @Override
    public void initWindow() {
        // Set Flatlaf Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Modern görünüm için
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel panel = initPanel();
        add(panel);
        setTitle("Para Transferi - EFT");
        setSize(600, 500);
        setResizable(true);
        setLocationRelativeTo(null); // Pencereyi ortalar
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapatıldığında yalnızca o pencereyi kapatır
        setVisible(true); // Pencereyi görünür hale getirir
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Kullanıcı bilgilerini ve üst paneli ekle
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout()); // GridBagLayout kullanarak düzenleyelim
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Kenar boşlukları
        gbc.fill = GridBagConstraints.HORIZONTAL; // Yatayda genişlemeye izin verir

        // Kullanıcı bilgilerini ve butonları ekleyelim
        var customerNameLabel = new JLabel("Müşteri: " + customerLogged.getName() + " " + customerLogged.getSurname());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // İki sütun boyunca yayılır
        topPanel.add(customerNameLabel, gbc);

        var customerPhoneLabel = new JLabel("Telefon: " + customerLogged.getPhoneNumber());
        gbc.gridy = 1;
        topPanel.add(customerPhoneLabel, gbc);

        var instructionLabel = new JLabel("Para aktarmak için hesap seçiniz");
        gbc.gridy = 2;
        topPanel.add(instructionLabel, gbc);

        selectButton = new JButton("Seç");
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        topPanel.add(selectButton, gbc);

        cancelButton = new JButton("İptal");
        gbc.gridx = 1; // İptal butonunu sağa hizala
        topPanel.add(cancelButton, gbc);

        panel.add(topPanel, BorderLayout.NORTH); // Üst kısma yerleştir

        // Banka hesaplarını gösterecek kısmı ekleyelim
        showBankAccount(panel);

        // Seç butonuna tıklama işlemi
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectBankAccount();
            }
        });

        // İptal butonuna tıklama işlemi
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return panel;
    }

    public void selectBankAccount() {
        BankAccount selectedAccount = bankAccountJList.getSelectedValue(); // Seçili hesabı al
        if (selectedAccount != null) {
            // İki butonlu JOptionPane ekleyelim
            String message = "Seçilen hesap: " + selectedAccount.getAccountNumber() +
                    "\nBakiye: " + selectedAccount.getBankBalance() +
                    "\nDevam etmek istiyor musunuz?";
            int option = JOptionPane.showOptionDialog(
                    null,
                    message,
                    "Hesap Bilgileri",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Devam Et", "İptal"},
                    "Devam Et"
            );

            if (option == JOptionPane.YES_OPTION) {
                // Devam Et seçeneği seçildi
                JOptionPane.showMessageDialog(null, "İşlem devam ediyor...", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                TransferSelectGui transferSelectGui = new TransferSelectGui(customerLogged, selectedAccount);
                transferSelectGui.initWindow();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Lütfen bir hesap seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showBankAccount(JPanel panel) {
        DefaultListModel<BankAccount> defaultListModel = new DefaultListModel<>();
        bankAccountJList = new JList<>(defaultListModel);
        bankAccountJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Daha modern bir görünüm için JList'e Custom Renderer ekleyelim
        bankAccountJList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel itemPanel = new JPanel(new BorderLayout());

            // Hesap bilgilerini daha detaylı gösterelim: accountNumber, bankBalance, bank_id, iban
            String accountInfo = String.format(
                    "<html><b>Hesap ID:</b> %d - <b>Hesap No:</b> %s - <b>IBAN:</b> %s<br><b>Bakiye:</b> %.2f TL</html>",
                    value.getBankAccountId(),  // Banka hesap ID'si
                    value.getAccountNumber(),  // Hesap numarası
                    value.getIBAN(),           // IBAN
                    value.getBankBalance()     // Bakiye
            );


            JLabel accountLabel = new JLabel(accountInfo);
            itemPanel.add(accountLabel, BorderLayout.CENTER);

            // Seçili öğe için renk değişimi
            itemPanel.setBackground(isSelected ? new Color(0x2196F3) : Color.WHITE);
            itemPanel.setForeground(isSelected ? Color.WHITE : Color.BLACK);

            return itemPanel;
        });

        JScrollPane jScrollPane = new JScrollPane(bankAccountJList);
        panel.add(jScrollPane, BorderLayout.CENTER);

        List<BankAccount> bankAccountList = bankAccountDb.getBankAccountsForCustomer(customerLogged.getCustomerId());

        if (bankAccountList != null && !bankAccountList.isEmpty()) {
            for (BankAccount bankAccount : bankAccountList) {
                defaultListModel.addElement(bankAccount);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Müşteriye ait hesap bulunamadı.", "Bilgilendirme", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
