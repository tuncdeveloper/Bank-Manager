package gui.adminGui;

import model.Customer;
import repository.CustomerDb;
import repository.ICustomerDb;
import test.InitCall;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;

public class EditCustomerGui extends JFrame implements InitCall {

    ICustomerDb customerDb = new CustomerDb();
    Customer customer;  // Seçilen müşteri

    // Constructor'da seçilen müşteri bilgisi aktarılıyor
    public EditCustomerGui(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void initWindow() {
        try {
            // FlatLaf teması kullanılıyor
            UIManager.setLookAndFeel(new FlatLightLaf()); // Ya da FlatDarkLaf() kullanabilirsiniz
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel panel = initPanel();
        add(panel);
        setTitle("Müşteri Düzenle");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        // Müşteri bilgilerini düzenlemek için alanlar
        JLabel nameLabel = new JLabel("Ad:");
        JTextField nameField = new JTextField(customer.getName());
        nameField.setBorder(BorderFactory.createCompoundBorder(
                nameField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel surnameLabel = new JLabel("Soyadı:");
        JTextField surnameField = new JTextField(customer.getSurname());
        surnameField.setBorder(BorderFactory.createCompoundBorder(
                surnameField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(surnameLabel);
        panel.add(surnameField);

        JLabel phoneNumberLabel = new JLabel("Telefon numarası:");
        JTextField phoneNumberField = new JTextField(customer.getPhoneNumber());
        phoneNumberField.setBorder(BorderFactory.createCompoundBorder(
                phoneNumberField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);

        JLabel passwordLabel = new JLabel("Parola:");
        JPasswordField passwordField = new JPasswordField(customer.getPassword());
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Butonlar
        JButton saveButton = new JButton("Kaydet");
        JButton cancelButton = new JButton("İptal");

        // Butonlar için modern bir stil
        saveButton.setBackground(new Color(63, 81, 181));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        cancelButton.setBackground(new Color(211, 47, 47));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        panel.add(saveButton);
        panel.add(cancelButton);

        phoneNumberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Eğer girilen karakter sayı değilse, giriş engellenir
                if (!Character.isDigit(c)) {
                    e.consume(); // Karakteri yok say
                }
            }
        });

        // Kaydet butonuna tıklama işlemi
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCommit(nameField, surnameField, phoneNumberField, passwordField);
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

    public void saveCommit(JTextField nameField, JTextField surnameField, JTextField phoneNumberField, JPasswordField passwordField) {
        // Güncellenen bilgileri müşteriye set ediyoruz
        customer.setName(nameField.getText());
        customer.setSurname(surnameField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
        customer.setPassword(new String(passwordField.getPassword()));

        // Veritabanında güncelliyoruz
        customerDb.updateCustomer(customer);
        dispose();
        JOptionPane.showMessageDialog(null, "Müşteri bilgileri başarıyla güncellendi.");

    }

}
