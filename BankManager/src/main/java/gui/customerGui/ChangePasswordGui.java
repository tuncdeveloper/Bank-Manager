package gui.customerGui;

import com.formdev.flatlaf.FlatLightLaf;
import model.Customer;
import repository.CustomerDb;
import repository.ICustomerDb;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordGui extends JDialog implements InitCall {

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JButton submitButton, cancelButton;
    private Customer loggedCustomer;

    ChangePasswordGui(Customer customer){
        this.loggedCustomer = customer;
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // FlatLaf modern tema
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Şifre Değiştir");
        setSize(400, 250); // Uygun bir boyut seçebilirsiniz
        setLocationRelativeTo(null); // Ekranın ortasında açılır
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Çıkışta pencereyi kapatır
        setResizable(false);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Modern bir layout kullanımı
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Aralıklar

        JLabel currentPasswordLabel = new JLabel("Mevcut Şifre:");
        JLabel newPasswordLabel = new JLabel("Yeni Şifre:");

        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);

        submitButton = new JButton("Onayla");
        submitButton.setBackground(new Color(76, 175, 80)); // Yeşil buton
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        cancelButton = new JButton("İptal");
        cancelButton.setBackground(new Color(211, 47, 47)); // Kırmızı buton
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        // Layout düzenlemeleri
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(currentPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(newPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(submitButton, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(cancelButton, gbc);

        return panel;
    }

    ICustomerDb customerDb = new CustomerDb();

    private void handleSubmit() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        // Find the customer from the database using the logged customer info
        Customer selectedCustomer = customerDb.findCustomer(loggedCustomer.getCustomerId());

        // Check if the current password matches
        if (currentPassword.equals(selectedCustomer.getPassword())) {
            // Update the password
            selectedCustomer.setPassword(newPassword);
            customerDb.updateCustomer(selectedCustomer); // Update the customer in the DB

            JOptionPane.showMessageDialog(this, "Şifre başarıyla değiştirildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the dialog
        } else {
            JOptionPane.showMessageDialog(this, "Yanlış mevcut şifre.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
