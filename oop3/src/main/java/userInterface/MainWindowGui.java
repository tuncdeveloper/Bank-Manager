package userInterface;

import database.DbPostgre;
import domain.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindowGui extends JFrame {


    DbPostgre dbPostgre = new DbPostgre();

    public MainWindowGui() {
        initWindow();
    }

    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Banka Menü");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel initPanel() {
        // Ana panel oluşturma
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Açık gri arkaplan

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Bileşenler arasında boşluk

        // Başlık
        JLabel title = new JLabel("Hoş Geldiniz");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(60, 63, 65));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);

        // Giriş yapmak için TC ve şifre alanları
        JLabel tcLabel = new JLabel("TC Kimlik No:");
        tcLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;  // grid genişliğini sıfırlıyoruz
        mainPanel.add(tcLabel, gbc);

        JTextField tcField = new JTextField(15);
        tcField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(tcField, gbc);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        // Kayıt Ol ve Giriş Yap butonları
        JButton registerButton = new JButton("Kayıt Ol");
        JButton loginButton = new JButton("Giriş Yap");
        JButton adminButton = new JButton("Yönetici giriş");

        // Butonların görünümü
        registerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        adminButton.setFont(new Font("Arial", Font.PLAIN, 16));
        registerButton.setBackground(new Color(173, 216, 230)); // Açık mavi renk
        loginButton.setBackground(new Color(144, 238, 144)); // Açık yeşil renk
        adminButton.setBackground(new Color(255, 182, 193)); // Açık kırmızı renk


        // Butonlar hizalama - Bu kısım giriş alanlarının altına alındı
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(adminButton, gbc);

        // Butonlara olay dinleyicileri ekleyin
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                RegisterGui registerGui = new RegisterGui();
                registerGui.initWindow();

            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               AdminLoginGui adminLoginGui = new AdminLoginGui();
               adminLoginGui.initWindow();

               // AdminGui adminGui = new AdminGui();
                // adminGui.initWindow();
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loginCommit(tcField, passwordField);

            }
        });

        return mainPanel;
    }

    public void loginCommit(JTextField tcField, JPasswordField passwordField) {
        String tcInput = tcField.getText();
        String password = new String(passwordField.getPassword());

        try {
            int tc = Integer.parseInt(tcInput); // TC numarasını tamsayıya çevirmeye çalış

            Customer findCustomer = dbPostgre.findCustomer(tc);

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(MainWindowGui.this, "Lütfen TC ve şifre girin.");
            } else {
                // TC ve şifreyi kontrol etme
                if (tc == findCustomer.getId() && password.equals(findCustomer.getPassword())) {
                    JOptionPane.showMessageDialog(MainWindowGui.this, "Hoşgeldiniz " + findCustomer.getName() + " " + findCustomer.getSurname() + "\nGiriş yapılıyor...");
                    LoginMenuGui loginMenuGui = new LoginMenuGui(findCustomer);
                    loginMenuGui.initWindow();
                } else {
                    JOptionPane.showMessageDialog(MainWindowGui.this, "TC veya şifre geçersiz.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(MainWindowGui.this, "Geçersiz TC Kimlik numarası girildi. Lütfen sadece rakam kullanın.");
        }
    }

}
