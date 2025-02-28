package gui.mainGui;

import com.formdev.flatlaf.FlatDarculaLaf;
import model.Customer;
import repository.CustomerDb;
import gui.customerGui.CustomerMenuGui;
import gui.customerGui.CustomerRegisterGui;
import gui.adminGui.AdminMenuGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class MainWindowGui extends JFrame {

    CustomerDb customerDb = new CustomerDb();

    public MainWindowGui() {
        FlatDarculaLaf.setup(); // Modern koyu tema
        initWindow();
    }

    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Banka Menü");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel initPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Banka Resmi
        try {
            // Resim dosyasının yolunu alıyoruz
            String imagePath = "/bank.png"; // Resources klasörü altındaki resim

            // Resmin URL'ini alıyoruz
            URL imageURL = getClass().getResource(imagePath);

            if (imageURL != null) {
                // Resmi yükleyip boyutlandırıyoruz
                ImageIcon bankIcon = new ImageIcon(imageURL);
                Image image = bankIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);

                // Resmi bir JLabel üzerine ekliyoruz
                JLabel bankImageLabel = new JLabel(new ImageIcon(image));

                // Resmi ana panele ekliyoruz
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                mainPanel.add(bankImageLabel, gbc);
            } else {
                System.out.println("Resim dosyası bulunamadı!");
            }
        } catch (Exception e) {
            System.out.println("Resim yüklenirken hata oluştu: " + e.getMessage());
        }


        // Başlık
        JLabel title = new JLabel("Hoş Geldiniz");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(title, gbc);

        // Text Fields and Buttons
        JTextField tcField = createStyledTextField("TC Kimlik No");
        JPasswordField passwordField = createStyledPasswordField("Şifre");
        JButton registerButton = createStyledButton("Kayıt Ol", Color.GREEN);
        JButton loginButton = createStyledButton("Giriş Yap", Color.BLUE);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(tcField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(loginButton, gbc);

        // Register Button Action
        registerButton.addActionListener(e -> {
            CustomerRegisterGui customerRegisterGui = new CustomerRegisterGui();
            customerRegisterGui.initWindow();
        });

        // Login Button Action
        loginButton.addActionListener(e -> loginCommit(tcField, passwordField));

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(45, 45, 45));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Footer yüksekliği
        footerPanel.setMinimumSize(new Dimension(getWidth(), 50)); // Footer minimum yüksekliği
        JLabel footerLabel = new JLabel("Banka Uygulaması © 2025");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        // Alt bilgi panelini ana panelin altına ekleyin
        gbc.gridx = 0;
        gbc.gridy = 5; // Footer'ın en alt kısma yerleştirildiğinden emin olun
        gbc.gridwidth = 2; // Alt bilgiyi 2 sütun boyunca genişletiyoruz
        mainPanel.add(footerPanel, gbc);

        return mainPanel;
    }





    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public void loginCommit(JTextField tcField, JPasswordField passwordField) {
        String tc = tcField.getText();
        String password = new String(passwordField.getPassword());

        if (tc.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen TC ve şifre girin.");
            return;
        }

        if (tc.length() != 11) {
            JOptionPane.showMessageDialog(this, "TC Kimlik Numarası 11 haneli olmalıdır!");
            return;
        }

        try {
            Customer findCustomer = customerDb.findCustomerByTcAndPassword(tc, password);

            if (findCustomer != null) {
                JOptionPane.showMessageDialog(this, "Hoşgeldiniz " + findCustomer.getName() + " " + findCustomer.getSurname() + "\nGiriş yapılıyor...");

                if ("ADMIN".equals(findCustomer.getCustomerTypeEnum().toString())) {
                    AdminMenuGui adminMenuGui = new AdminMenuGui();
                    adminMenuGui.initWindow();
                } else {
                    CustomerMenuGui customerMenuGui = new CustomerMenuGui(findCustomer);
                    customerMenuGui.initWindow();
                }
            } else {
                JOptionPane.showMessageDialog(this, "TC veya şifre geçersiz.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bir hata oluştu: " + e.getMessage());
        }
    }
}
