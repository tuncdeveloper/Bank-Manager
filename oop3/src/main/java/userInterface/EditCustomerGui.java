package userInterface;

import domain.Customer;
import database.DbPostgre;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditCustomerGui extends JFrame implements InitCall {

    DbPostgre dbPostgre = new DbPostgre();
    Customer customer;  // Seçilen müşteri

    // Constructor'da seçilen müşteri bilgisi aktarılıyor
    public EditCustomerGui(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Müşteri Düzenle");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        // Müşteri bilgilerini düzenlemek için alanlar
        JLabel nameLabel = new JLabel("Ad:");
        JTextField nameField = new JTextField(customer.getName());
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel surnameLabel = new JLabel("Soyadı:");
        JTextField surnameField = new JTextField(customer.getSurname());
        panel.add(surnameLabel);
        panel.add(surnameField);

        JLabel phoneNumberLabel = new JLabel("Telefon numarası:");
        JTextField phoneNumberField = new JTextField(customer.getPhoneNumber());
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(customer.getPassword());
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Butonlar
        JButton saveButton = new JButton("Kaydet");
        JButton cancelButton = new JButton("İptal");
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
               saveCommit(nameField,surnameField,phoneNumberField,passwordField);
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

    public void saveCommit(JTextField nameField,JTextField surnameField,JTextField phoneNumberField,JPasswordField passwordField){
        // Güncellenen bilgileri müşteriye set ediyoruz
        customer.setName(nameField.getText());
        customer.setSurname(surnameField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
        customer.setPassword(new String(passwordField.getPassword()));

        // Veritabanında güncelliyoruz
        dbPostgre.updateCustomer(customer);
        dispose();
        JOptionPane.showMessageDialog(null, "Müşteri bilgileri başarıyla güncellendi.");
        AdminGui adminGui = new AdminGui();
        adminGui.initWindow();
    }

}

