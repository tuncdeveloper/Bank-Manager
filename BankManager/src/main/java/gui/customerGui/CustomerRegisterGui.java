package gui.customerGui;

import gui.adminGui.AdminMenuGui;
import model.Customer;
import model.CustomerTypeEnum;
import repository.CustomerDb;
import repository.ICustomerDb;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CustomerRegisterGui extends JDialog implements InitCall {

    ICustomerDb customerDb = new CustomerDb();
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
        gbc.insets = new Insets(10, 10, 10, 10);
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

        // TC Kimlik Numarası alanı
        JLabel tcLabel = new JLabel("TC Kimlik Numarası: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(tcLabel, gbc);
        JTextField tcField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tcField, gbc);

        // TC Kimlik No sadece sayı olmalı ve 11 haneden fazla girilememeli
        tcField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || tcField.getText().length() >= 11) {
                    e.consume();
                }
            }
        });

        // Telefon alanı
        JLabel telefonLabel = new JLabel("Telefon Numarası: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(telefonLabel, gbc);
        gbc.gridx = 1;

        JTextField telefonField = new JTextField("+090", 15);
        panel.add(telefonField, gbc);

// Telefon numarası sadece sayısal giriş ve 14 haneli olacak
        telefonField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = telefonField.getText();

                // Eğer girilen karakter rakam değilse veya uzunluk 14'ü geçiyorsa girişe izin verme
                if (!Character.isDigit(c) || text.length() >= 14) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // "+090" kısmının silinmesini engelle
                if (telefonField.getCaretPosition() < 5 && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });


        // Telefon numarasının sadece sayı olmasını sağlayan KeyListener
        telefonField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        // Parola alanı
        JLabel parolaLabel = new JLabel("Parola: ");
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        registerButton.addActionListener(e -> registerCommit(adiField, soyadiField, tcField, telefonField, parolaField));

        // İptal butonu işlemi
        iptalButton.addActionListener(e -> dispose());

        return panel;
    }

    public void registerCommit(JTextField adiField, JTextField soyadiField, JTextField tcField,
                               JTextField telefonField, JPasswordField parolaField) {
        String adi = adiField.getText();
        String soyadi = soyadiField.getText();
        String tc = tcField.getText();
        String telefon = telefonField.getText();
        String parola = new String(parolaField.getPassword());

        // Boş alan kontrolü
        if (adi.isEmpty() || soyadi.isEmpty() || tc.isEmpty() || telefon.isEmpty() || parola.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurunuz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TC Kimlik numarası 11 hane olmalı
        if (tc.length() != 11) {
            JOptionPane.showMessageDialog(this, "TC Kimlik Numarası 11 haneli olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Telefon numarası 14 hane olmalı
        if (telefon.length() != 14) {
            JOptionPane.showMessageDialog(this, "Telefon numarası tam 14 haneli olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Daha önce kayıtlı olup olmadığını kontrol et
        Customer existingCustomer = customerDb.findCustomerByTc(tc);
        if (existingCustomer != null) {
            JOptionPane.showMessageDialog(this, "Bu TC Kimlik Numarası ile kayıtlı bir müşteri zaten var!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Yeni müşteri kaydı
        customer.setName(adi);
        customer.setSurname(soyadi);
        customer.setTc(tc);
        customer.setPhoneNumber(telefon);
        customer.setPassword(parola);
        customer.setCustomerTypeEnum(CustomerTypeEnum.USER);

        // Veritabanına kaydet
        customerDb.newRegister(customer);

        JOptionPane.showMessageDialog(this, customer.getName() + " " + customer.getSurname() + " Kayıt Başarılı!");
        dispose();

    }


}
