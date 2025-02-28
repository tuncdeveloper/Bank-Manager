package gui.customerGui;

import com.formdev.flatlaf.FlatLightLaf;

import model.Customer;
import test.InitCall;
import gui.jointAdminCustomer.BankAccountShowGui;
import gui.TransferMenuGui;

import javax.swing.*;
import java.awt.*;

public class CustomerMenuGui extends JFrame implements InitCall {

    private Customer loggedInCustomer;

    public CustomerMenuGui(Customer customer) {
        this.loggedInCustomer = customer;
        FlatLightLaf.setup(); // FlatLaf UI'yi başlat
        UIManager.put("Button.arc", 20); // Butonları yuvarlat

        initWindow();
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Müşteri Menüsü");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250)); // Arka plan rengini modern bir renk yap

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false); // Arka planı şeffaf yap
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Daha geniş boşluklar
        gbc.anchor = GridBagConstraints.WEST;

        // Kullanıcı Bilgileri
        JLabel nameLabel = new JLabel("Ad: " + loggedInCustomer.getName());
        JLabel surnameLabel = new JLabel("Soyadı: " + loggedInCustomer.getSurname());
        JLabel tcLabel = new JLabel("Tc: " + loggedInCustomer.getTc());
        JLabel phoneLabel = new JLabel("Telefon: " + loggedInCustomer.getPhoneNumber());

        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        surnameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        tcLabel.setFont(new Font("SansSerif",Font.BOLD,14));
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        infoPanel.add(surnameLabel, gbc);

        gbc.gridy = 2;
        infoPanel.add(tcLabel, gbc);

        gbc.gridy = 3;
        infoPanel.add(phoneLabel, gbc);

        // Buton Paneli
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setOpaque(false);

        // Modern buton oluşturma fonksiyonu
        JButton myAccountsButton = createStyledButton("Hesaplarım");
        myAccountsButton.addActionListener(e -> new BankAccountShowGui(loggedInCustomer).initWindow());

        JButton transferButton = createStyledButton("Para Aktar");
        transferButton.addActionListener(e -> new TransferMenuGui(loggedInCustomer).initWindow());

        JButton changePasswordButton = createStyledButton("Parola Değiştir");
        changePasswordButton.addActionListener(e -> new ChangePasswordGui(loggedInCustomer).initWindow());

        JButton exitButton = createStyledButton("Çıkış");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Çıkmak istediğinize emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        buttonPanel.add(myAccountsButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(exitButton);

        // Panelleri Ana Panele Ekle
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // Modern görünümlü buton oluşturan metod
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }
}
