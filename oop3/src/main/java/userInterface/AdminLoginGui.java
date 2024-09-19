package userInterface;

import domain.Admin;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginGui extends JDialog implements InitCall {

    private JTextField usernameField;
    private JPasswordField passwordField;

    Admin admin = new Admin("ferhat", "tunc", "1234");
    AdminGui adminGui = new AdminGui();

    public AdminLoginGui() {
    }

    @Override
    public void initWindow() {
        JPanel jPanel = initPanel();
        add(jPanel);
        setTitle("Admin Girişi");
        pack();
        setLocationRelativeTo(null);
        setModalityType(DEFAULT_MODALITY_TYPE);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Kullanıcı adı alanı
        JLabel usernameLabel = new JLabel("Adı: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Parola alanı
        JLabel passwordLabel = new JLabel("Parola: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Giriş Butonu
        JButton loginButton = new JButton("Giriş Yap");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Çıkış Butonu
        JButton exitButton = new JButton("Çıkış");
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(exitButton, gbc);


        // Giriş işlemi
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            adminLoginCommit();

            }
        });



        // Çıkış butonuna tıklama işlemi
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Pencereyi kapat
            }
        });

        return panel;
    }

    public void adminLoginCommit(){
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword()); // Parola doğru şekilde alınmalı

        System.out.println(admin.getName());
        System.out.println(admin.getPassword());
        // Basit giriş kontrolü
        if (username.equals(admin.getName()) && password.equals(admin.getPassword())) {
            JOptionPane.showMessageDialog(AdminLoginGui.this, "Giriş Başarılı!");
            dispose();
            adminGui.initWindow();

        } else {
            JOptionPane.showMessageDialog(AdminLoginGui.this, "Hatalı Kullanıcı Adı veya Parola!");
        }

    }
}
