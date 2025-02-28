package gui.jointAdminCustomer;

import com.formdev.flatlaf.FlatLightLaf;
import model.BankAccount;
import manager.BankAccountFactory;
import repository.BankAccountDb;
import repository.IBankAccountDb;
import test.InitCall;
import model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountsCreateGui extends JFrame implements InitCall {
    private JTextField ibanField;
    private JTextField accountNoField;
    private JTextField balanceField;
    private JButton createAccountButton;
    private JButton exitButton;

    private Customer customer;
    private IBankAccountDb bankAccountDb = new BankAccountDb();
    private List<BankAccount> bankAccountList = new ArrayList<>();

    public BankAccountsCreateGui(Customer customer) {
        this.customer = customer;
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Modern UI
        } catch (Exception e) {
            e.printStackTrace();
        }
        initWindow();
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Banka Hesap Yönetimi");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Müşteri Bilgileri
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Ad Soyad:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(customer.getName() + " " + customer.getSurname()), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Müşteri ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(customer.getCustomerId())), gbc);

        // IBAN
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("IBAN:"), gbc);
        gbc.gridx = 1;
        ibanField = new JTextField();
        ibanField.setEditable(false);
        ibanField.setColumns(20);
        ibanField.setPreferredSize(new Dimension(250, 30));
        panel.add(ibanField, gbc);

        // Hesap No
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Hesap No:"), gbc);
        gbc.gridx = 1;
        accountNoField = new JTextField();
        accountNoField.setEditable(false);
        accountNoField.setColumns(20);
        accountNoField.setPreferredSize(new Dimension(250, 30));
        panel.add(accountNoField, gbc);

        // Bakiye
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Bakiye:"), gbc);
        gbc.gridx = 1;
        balanceField = new JTextField();
        balanceField.setEditable(false);
        balanceField.setColumns(15);
        balanceField.setPreferredSize(new Dimension(200, 30));
        panel.add(balanceField, gbc);

        // Hesap Oluştur Butonu
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        createAccountButton = new JButton("Hesap Oluştur");
        createAccountButton.setBackground(new Color(76, 175, 80));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 14));
        createAccountButton.addActionListener(e -> setCreateAccountButton());
        panel.add(createAccountButton, gbc);

        // Çıkış Butonu
        gbc.gridy = 6;
        exitButton = new JButton("Çıkış");
        exitButton.setBackground(new Color(244, 67, 54));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(e -> {
            dispose();
            new BankAccountShowGui(customer).initWindow();
        });
        panel.add(exitButton, gbc);

        return panel;
    }

    public void setCreateAccountButton() {
        BankAccountFactory bankAccountFactory = new BankAccountFactory();
        BankAccount bankAccount = new BankAccount();

        String iban = bankAccountFactory.generateRandomIban();
        String accountNo = bankAccountFactory.generateRandomAccountNumber();
        double balance = bankAccountFactory.generateRandomBalance();

        bankAccount.setIBAN(iban);
        bankAccount.setAccountNumber(accountNo);
        bankAccount.setBankBalance(balance);
        bankAccount.setCustomerIdFk(customer.getCustomerId());

        bankAccountDb.addBankAccount(bankAccount);
        bankAccountList.add(bankAccount);

        ibanField.setText(iban);
        accountNoField.setText(accountNo);
        balanceField.setText(String.valueOf(balance));

        JOptionPane.showMessageDialog(this, "Hesap başarıyla oluşturuldu!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
    }
}
