package gui.jointAdminCustomer;

import model.BankAccount;
import model.Customer;
import repository.BankAccountDb;
import repository.IBankAccountDb;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BankAccountShowGui extends JFrame implements InitCall {

    private Customer customerPerson;
    private IBankAccountDb bankAccountDb = new BankAccountDb();
    DefaultListModel<BankAccount> bankAccountListModel = new DefaultListModel<>();
    JList<BankAccount> bankAccountJList = new JList<>(bankAccountListModel);

    public BankAccountShowGui(Customer customer) {
        this.customerPerson = customer;
    }

    @Override
    public void initWindow() {
        JPanel jPanel = initPanel();
        add(jPanel);
        setTitle(customerPerson.getName() + " Hesapları");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));

        // Kullanıcı Bilgileri Paneli
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridLayout(3, 2, 5, 5));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("Müşteri Bilgileri"));
        userInfoPanel.setBackground(Color.WHITE);

        userInfoPanel.add(new JLabel("Ad:"));
        userInfoPanel.add(new JLabel(customerPerson.getName()));
        userInfoPanel.add(new JLabel("Soyad:"));
        userInfoPanel.add(new JLabel(customerPerson.getSurname()));
        userInfoPanel.add(new JLabel("Telefon:"));
        userInfoPanel.add(new JLabel(customerPerson.getPhoneNumber()));

        panel.add(userInfoPanel, BorderLayout.NORTH);

        showBankAccountList(panel);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton deleteButton = new JButton("Hesabı Sil");
        JButton addButton = new JButton("Yeni Hesap Ekle");
        JButton exitButton = new JButton("Çıkış");

        // Buton Tasarımı
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.WHITE);

        deleteButton.addActionListener(e -> deleteBankAccount());
        addButton.addActionListener(e -> {
            dispose();
            new BankAccountsCreateGui(customerPerson).initWindow();
        });
        exitButton.addActionListener(e -> dispose());

        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(exitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void deleteBankAccount() {
        BankAccount selectedAccount = bankAccountJList.getSelectedValue();
        if (selectedAccount != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Hesabı silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bankAccountDb.deleteBankAccount(selectedAccount.getBankAccountId());
                bankAccountListModel.removeElement(selectedAccount);
                JOptionPane.showMessageDialog(this, "Hesap başarıyla silindi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silmek için bir hesap seçmelisiniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showBankAccountList(JPanel panel) {
        bankAccountJList.setCellRenderer(new BankAccountRenderer());
        JScrollPane scrollPane = new JScrollPane(bankAccountJList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Banka Hesapları"));

        // Banka hesaplarını yükle
        List<BankAccount> bankAccounts = bankAccountDb.getBankAccountsForCustomer(customerPerson.getCustomerId());
        if (bankAccounts != null) {
            for (BankAccount account : bankAccounts) {
                bankAccountListModel.addElement(account);
            }
        }

        panel.add(scrollPane, BorderLayout.CENTER);
    }

    // Banka hesaplarını daha şık göstermek için özel Renderer
    static class BankAccountRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof BankAccount) {
                BankAccount account = (BankAccount) value;
                label.setText("IBAN: " + account.getIBAN() + " | Bakiye: " + account.getBankBalance() + " TL");
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(new Color(70, 130, 180));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.LIGHT_GRAY);
                    label.setForeground(Color.BLACK);
                }
            }
            return label;
        }
    }
}
