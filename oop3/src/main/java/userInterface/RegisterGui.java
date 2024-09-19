package userInterface;

import database.DbPostgre;
import domain.BankAccount;
import domain.BankAccountFactory;
import domain.Customer;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class RegisterGui extends JDialog implements InitCall {

    DbPostgre dbPostgre = new DbPostgre();
    Customer customer = new Customer();

    @Override
    public void initWindow() {
        JPanel jPanel = initPanel();
        add(jPanel);
        setTitle("Kayıt Yap");
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
        gbc.insets = new Insets(10, 10, 10, 10); // Boşluklar
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adı alanı
        JLabel adiLabel = new JLabel("Adı: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(adiLabel, gbc);
        JTextField adiField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(adiField, gbc);

        // Soyadı alanı
        JLabel soyadiLabel = new JLabel("Soyad: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(soyadiLabel, gbc);
        JTextField soyadiField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(soyadiField, gbc);

        // Telefon alanı
        JLabel telefonLabel = new JLabel("Telefon Numarası: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(telefonLabel, gbc);
        JTextField telefonField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(telefonField, gbc);

        // Telefon numarasının sadece sayı olmasını sağlayan KeyListener
        telefonField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Eğer girilen karakter sayı değilse, giriş engellenir
                if (!Character.isDigit(c)) {
                    e.consume(); // Karakteri yok say
                }
            }
        });

        // Parola alanı
        JLabel parolaLabel = new JLabel("Parola: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(parolaLabel, gbc);
        JPasswordField parolaField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(parolaField, gbc);


        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerButton = new JButton("Kaydet");
        buttonPanel.add(registerButton);
        JButton iptalButton = new JButton("İptal");
        buttonPanel.add(iptalButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Kaydet butonu işlemi
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                registerCommit(adiField, soyadiField, telefonField, parolaField);
            }
        });

        // İptal butonuna tıklama eylemi
        iptalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return panel;
    }

    public void registerCommit(JTextField adiField, JTextField soyadiField, JTextField telefonField,
                               JPasswordField parolaField) {
        String adi = adiField.getText();
        String soyadi = soyadiField.getText();
        String telefon = telefonField.getText();
        String parola = new String(parolaField.getPassword());

        // Müşteri bilgilerini ayarlama
        customer.setName(adi);
        customer.setSurname(soyadi);
        customer.setPhoneNumber(telefon);
        customer.setPassword(parola);

        dbPostgre.newRegister(customer);


        JOptionPane.showMessageDialog(RegisterGui.this,
                customer.getName() + " " + customer.getSurname() + " Kayıt Başarılı!");
        dispose();
    }


}
